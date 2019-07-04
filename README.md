# basexjsr223
JSR223 wrapper for BaseX XQuery 3.1 engine

BaseX [1] is our favorite platform for developing services and data oriented applications. Its comprehensive XQuery 3.1 engine turns out to be a real Swiss Army knife in several scenarios.

In order to exploit BaseX for scripting other complex software such as BPM platforms we've created basexjsr223 which is a JSR223 compliant wrapper of BaseX XQuery engine.

In order to test BaseXJSR223, you'll have to add BaseXJSR223.jar to your classpath together with BaseX.jar that you can extract from the BaseX distributable at [2]. 

At this point you could just create a class like the following:

`import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.nubisware.jsr223.basex.BaseXScriptEngine;

public class StandaloneTest {

	public static void main(String[] args) {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = (ScriptEngine) manager.getEngineByName("BaseX");
		try {
			//create bindings and set XQuery context to 1
			Bindings b = ((BaseXScriptEngine)engine).createBindings(1);
			b.put("offset", 1);
			System.out.println(engine.eval("declare variable $offset external; . + $offset", b));
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

}`

There are several names for whom the EngineManager will return BaseXJS223 engines ("BaseX", "basex", "xquery", "Xquery", "Xquery3"...) and it is also possible to get the engine by file extensions ("xq", "xqm", "xquery", "xml") and MIME type ("application/xquery").

Besides passing the XQuery context at the time of creation of the ScriptBindings, it is also possible to add it through a special key:

`bindings.put(BaseXScriptEngine.CONTEXT_KEY,context);`

Please check the class AllTests.java for some useful examples on how to use the engine.

[1] http://www.basex.org
[2] http://www.basex.org/download
