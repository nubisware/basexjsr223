# BaseXJSR223
A JSR223 wrapper for BaseX XQuery 3.1 engine.

BaseX [1] is our favorite platform for developing services and data oriented applications. Its comprehensive XQuery 3.1 engine turns out to be a real Swiss Army knife in several scenarios.

In order to exploit BaseX for scripting other complex software such as BPM engines, we've created basexjsr223 which is a JSR223 compliant wrapper of BaseX XQuery engine.

In order to test BaseXJSR223, you'll have to add to your classpath the BaseXJSR223.jar (download from releases) together with BaseX.jar which you can extract from the BaseX distributable available at [2]. 

At this point you could just write and run a class like the following:

```
import javax.script.Bindings;
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

}
```

There are several names for whom the EngineManager will return BaseXJS223 engine instances ("BaseX", "basex","xquery", "Xquery", "XQuery", "xquery3","xquery3.1"). 

It is also possible to get the engine by file extensions ("xq", "xqm", "xquery", "xml") and MIME type ("application/xquery").

Besides passing the XQuery context at the time of instantiation of the Bindings, it is also possible to pass it through a special key:

`bindings.put(BaseXScriptEngine.CONTEXT_KEY,context);`

Please check the class AllTests.java for some useful examples on how to use the engine.

Note for developers: in order to build BaseXJSR223 you need gradle and `gradle build` from inside the repository's root folder will generate the library and execute the tests.

[1] http://www.basex.org

[2] http://www.basex.org/download
