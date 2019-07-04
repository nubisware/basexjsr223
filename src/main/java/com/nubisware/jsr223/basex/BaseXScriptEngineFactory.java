package com.nubisware.jsr223.basex;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import org.basex.util.Prop;

public class BaseXScriptEngineFactory implements ScriptEngineFactory {

	public final static Map<String, Object> parameters;
	static {
		parameters = new HashMap<String, Object>();
	}
	
	public BaseXScriptEngineFactory() {
		
	}
	
	@Override
	public String getEngineName() {
		return Prop.NAME;
	}

	@Override
	public String getEngineVersion() {
		return Prop.VERSION;
	}

	@Override
	public List<String> getExtensions() {
		return Arrays.asList("xq", "xqm", "xquery", "xml");
	}

	@Override
	public String getLanguageName() {
		return "XQuery";
	}

	@Override
	public String getLanguageVersion() {
		return "3.1";
	}

	@Override
	public String getMethodCallSyntax(String object, String method, String... args) {
		StringBuilder stringBuilder = new StringBuilder();
	    for (int i = 0; i < args.length; i++) {
	      if (i > 0) {
	        stringBuilder.append(", ");
	      }
	      stringBuilder.append('$');
	      stringBuilder.append(args[i]);
	    }
	    
		String prolog = "declare namespace cl = \"the.object.class\";";
		String instance = "let $" + object + " := cl:new()";		
	    return prolog + "\n" + instance + "\n" + "$" + object + ":" + method + "(" + stringBuilder + ")";
	}

	@Override
	public List<String> getMimeTypes() {
		return Arrays.asList("application/xquery");
	}

	@Override
	public List<String> getNames() {
		return Arrays.asList(this.getEngineName(),"basex","xquery", "Xquery","XQuery", "xquery3","xquery3.1");
	}

	@Override
	public String getOutputStatement(String stmt) {
		return stmt;
	}

	@Override
	public Object getParameter(String param) {
		return BaseXScriptEngineFactory.parameters.get(param);
	}

	@Override
	public String getProgram(String... arg0) {
		return null;
	}

	@Override
	public ScriptEngine getScriptEngine() {
		ScriptEngine engine =  new BaseXScriptEngine(this);
		return engine;
	}

}
