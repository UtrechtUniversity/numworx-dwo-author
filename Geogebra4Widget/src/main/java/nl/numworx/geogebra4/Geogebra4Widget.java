package nl.numworx.geogebra4;

import java.util.Locale;
import java.util.function.Function;

import fi.beans.mainframe.JApplet;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;


import geogebra.cas.mpreduce.CASmpreduce;

import org.mathpiper.mpreduce.Interpreter2;

@SuppressWarnings("serial")
public class Geogebra4Widget extends JApplet implements WiskOpdrApplet, Function<String, String> {

	public static void main(String[] args) {
	}

	public Geogebra4Widget(Locale locale) {
		setLocale(locale);
	}
	
	public Geogebra4Widget() {
	}
	
	@Override
	public InteractiePanel getInteractiePanel() {
		return new GeogebraPanel();
	}

	@Override
	public String apply(String command) {		
		return evaluate(command);
	}
	
	private static boolean loaded;
	private  static Interpreter2 getInstance() {
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

	
	private static String evaluate(String command) {
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
