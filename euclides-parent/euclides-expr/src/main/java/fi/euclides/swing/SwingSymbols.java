package fi.euclides.swing;

import java.io.InputStream;
import java.util.Properties;

import fi.euclides.openmath.Symbols;

@SuppressWarnings("serial")
public class SwingSymbols extends Properties implements Symbols {
	
	public SwingSymbols() {
		try {
		    InputStream in = getClass().getResourceAsStream("popcorn.properties");
		    load(in);
		    in.close();
		} catch(Exception e) {}
	}
	
	public String getString(String key) {
		return getProperty(key);
	}
}
