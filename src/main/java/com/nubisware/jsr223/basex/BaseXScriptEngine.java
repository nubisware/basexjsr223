package com.nubisware.jsr223.basex;

import java.io.IOException;
import java.io.Reader;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.basex.core.Context;
import org.basex.query.QueryException;
import org.basex.query.QueryProcessor;

public class BaseXScriptEngine implements ScriptEngine{

	private ScriptEngineFactory factory;
	private ScriptContext context;

	public final static String CONTEXT_KEY = "_._";
	
	protected static ScriptException toScriptException(QueryException ex) {
		return new ScriptException(ex.getLocalizedMessage(), ex.file(), ex.line(), ex.column());
	}
	
	public BaseXScriptEngine() {
		this(null);
	}
	
	public BaseXScriptEngine(ScriptEngineFactory factory) {
		this.factory = factory;
	}
	
	protected String readQuery(Reader reader) throws IOException {
		char[] arr = new char[8 * 1024];
	    StringBuilder buffer = new StringBuilder();
	    int numCharsRead;
	    while ((numCharsRead = reader.read(arr, 0, arr.length)) != -1) {
	        buffer.append(arr, 0, numCharsRead);
	    }
	    reader.close();
	    return buffer.toString();
	}
	
	@Override
	/**
	 * Create Bindings without a context
	 */
	public Bindings createBindings() {
		return new SimpleBindings();
	}

	/**
	 * Create Bindings and set context as XQuery context
	 * @param context the object to be set as context of the query
	 * @return an instance of SimpleBindings that can be populated with external variable bindings.
	 */
	public Bindings createBindings(Object context) {
		Bindings bindings = this.createBindings();
		return this.bindXQueryContext(bindings, context);
	}
	
	/**
	 * Set XQuery context object to bindings
	 * @param bindings the bindings to be populated with a context object
	 * @param context the object to be set as context of the XQuery
	 * @return the updated Bindings
	 */
	public Bindings bindXQueryContext(Bindings bindings, Object context) {
		bindings.put(CONTEXT_KEY, context);
		return bindings;
	}
	
	@Override
	public Object eval(String query) throws ScriptException {
		return eval(query, (Bindings)null);
	}

	@Override
	public Object eval(Reader reader) throws ScriptException {
		return eval(reader, (Bindings)null);
	}

	@Override
	public Object eval(String query, ScriptContext ctx) throws ScriptException {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public Object eval(Reader reader, ScriptContext ctx) throws ScriptException {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public Object eval(String query, Bindings bindings) throws ScriptException {
		QueryProcessor processor = new QueryProcessor(query, new Context());
		try {
			if(bindings != null) {
				for(String k : bindings.keySet()) {
					Object value = bindings.get(k);
					if(k.equals(CONTEXT_KEY)) {
						//System.out.println("Binding context to " + value);
						processor.context(value);
					}else {
						//System.out.println("Binding variable " + k + " to " + value);
						processor.bind(k, value);
					}
				}
			}
			return processor.value().toJava();
		} catch (QueryException e) {
			throw toScriptException(e);
		} finally {
			processor.close();
		}
	}

	@Override
	public Object eval(Reader reader, Bindings bindings) throws ScriptException {
		String query;
		try {
			query = readQuery(reader);
		} catch (IOException e) {
			throw new ScriptException(e);
		}
		return eval(query, bindings);
	}

	@Override
	public Object get(String key) {
		if(key == "CONTEXt_KEY") {
			return BaseXScriptEngine.CONTEXT_KEY;
		}else return null;
	}

	@Override
	public Bindings getBindings(int scope) {
		return null;
	}

	@Override
	public ScriptContext getContext() {
		return this.context;
	}

	@Override
	public ScriptEngineFactory getFactory() {
		return this.factory;
	}

	@Override
	public void put(String key, Object value) {
		return;
	}

	@Override
	public void setBindings(Bindings bindings, int scope) {
		return;
	}

	@Override
	public void setContext(ScriptContext ctx) {
		this.context = ctx;
	}

}
