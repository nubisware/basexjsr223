package BaseXJSR223;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.nubisware.jsr223.basex.BaseXScriptEngine;
import com.nubisware.jsr223.basex.BaseXScriptEngineFactory;

public class AllTests {

	//short term development tests
	protected BaseXScriptEngineFactory factory = new BaseXScriptEngineFactory();
	protected ScriptEngineManager manager = new ScriptEngineManager();
	protected ScriptEngine engine;
	
	public AllTests() {
		for(String ext : factory.getExtensions()) {
			manager.registerEngineExtension(ext, factory);
		}
		for(String mime : factory.getMimeTypes()) {
			manager.registerEngineMimeType(mime, factory);
		}
		manager.registerEngineName(factory.getEngineName(), factory);
		engine = manager.getEngineByName("BaseX");
	}
	
	@Test
	public void testBaseXSEF() {
		assertNotNull(factory);
		assertEquals("BaseX", factory.getEngineName());
	}
	
	@Test
	public void testBaseEngineRetrieval() {
		ScriptEngine engine = factory.getScriptEngine();
		assertNotNull(engine);
		engine = manager.getEngineByExtension("xq");
		assertNotNull(engine);
		engine = manager.getEngineByExtension("xqm");
		assertNotNull(engine);
		engine = manager.getEngineByExtension("xquery");
		assertNotNull(engine);
		engine = manager.getEngineByMimeType("application/xquery");
		assertNotNull(engine);
		engine = manager.getEngineByName("BaseX");
		assertNotNull(engine);
	}
	
	@Test
	public void testStringQuery() {
		assertNotNull(engine);
		String[] expected = {"a", "b", "c"};
		try {
			Object res = engine.eval("1+1");
			assertEquals(res, new BigInteger("2"));
			res = engine.eval("'hello world'");
			assertEquals(res, "hello world");
			res = engine.eval("1 = 1");
			assertEquals(res, true);
			res = engine.eval("1 = 11");
			assertEquals(res, false);
			String[] resarr = (String[])engine.eval("('a','b','c')");
			for(int i=0; i < resarr.length; i++) {
				assertEquals(resarr[i], expected[i]);
			}
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testReaderQuery() {
		assertNotNull(engine);
		try {
			Object res = engine.eval(new StringReader("1+1"));
			assertEquals(res, new BigInteger("2"));
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testQueryWithBindings() {
		assertNotNull(engine);
		try {
			Bindings bindings = engine.createBindings();
			bindings.put("in", 1);
			Object res = engine.eval("declare variable $in external; 1+$in", bindings);
			assertEquals(res, new BigInteger("2"));
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testQueryWithContext() {
		BaseXScriptEngine bxengine = (BaseXScriptEngine)this.engine;
		assertNotNull(engine);
		String[] context = {"a", "b", "c"};
		try {
			Bindings bindings = engine.createBindings();
			bindings.put(BaseXScriptEngine.CONTEXT_KEY,context);
			Object res = engine.eval("count(.)", bindings);
			assertEquals(res, new BigInteger("3"));
			
			bindings = engine.createBindings();
			bxengine.bindXQueryContext(bindings, context);
			res = engine.eval("count(.)", bindings);
			assertEquals(res, new BigInteger("3"));
			
			bindings = bxengine.createBindings(context);
			res = engine.eval("count(.)", bindings);
			assertEquals(res, new BigInteger("3"));
		    
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testQueryWithXMLContext() {
		BaseXScriptEngine bxengine = (BaseXScriptEngine)this.engine;
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    final DocumentBuilder builder = factory.newDocumentBuilder();
		    final Document doc = builder.parse(new ByteArrayInputStream("<collection><item>1</item><item>2</item></collection>".getBytes()));
			
		    Bindings bindings = bxengine.createBindings(doc);
		    Object res = engine.eval("xs:int(sum(./collection/item/data()))", bindings);
		    
		    assertEquals(res, 3);
	    
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
