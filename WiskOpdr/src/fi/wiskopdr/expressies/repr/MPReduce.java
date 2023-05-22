package fi.wiskopdr.expressies.repr;

import geogebra.cas.mpreduce.CASmpreduce;

import org.mathpiper.mpreduce.Interpreter2;

// Utility class
public class MPReduce {
	private MPReduce() {}
	
	private static boolean loaded;
	public  static Interpreter2 getInstance() {
		final Interpreter2 staticInterpreter = CASmpreduce.getStaticInterpreter();
		if(!loaded)
		try { 
			staticInterpreter.evaluate("load_package ineq");
			staticInterpreter.evaluate("load_package boolean");
			staticInterpreter.evaluate("procedure prv(f!*, a!*, b!*, x!*); limit!-(f!*,x!*,b!*)-limit!+(f!*,x!*,a!*)");
			loaded = true;
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return staticInterpreter;
	}

	
	public static String evaluate(String command) {
		String r = null;
		try {
			r = getInstance().evaluate(command);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if(r != null)
			return r.trim();
		return r;
	}
}
