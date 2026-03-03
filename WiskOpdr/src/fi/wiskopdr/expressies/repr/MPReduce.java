package fi.wiskopdr.expressies.repr;

import java.util.function.Function;

import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;

// Utility class
public class MPReduce {
	private MPReduce() {}
	
	private static Function<String,String> interpreter;
	private  static Function<String,String> getInstance() {
		if(interpreter == null)
		try { 
		  interpreter = (Function<String, String>) TekstInteractiePanelVak.newWiskOpdrApplet("nl.numworx.geogebra4.Geogebra4Widget", WiskOpdr.language);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return interpreter;
	}

	
	public static String evaluate(String command) {
		String r = null;
		try {
			r = getInstance().apply(command);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if(r != null)
			return r.trim();
		return r;
	}
}
