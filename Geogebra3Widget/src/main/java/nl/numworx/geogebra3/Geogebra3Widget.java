package nl.numworx.geogebra3;

import java.util.Locale;

import fi.beans.mainframe.JApplet;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;

public class Geogebra3Widget extends JApplet implements WiskOpdrApplet {
	
	public static void main(String[] args) {
	}

	public Geogebra3Widget(Locale locale) {
		setLocale(locale);
	}

	@Override
	public InteractiePanel getInteractiePanel() {
		
		return new Geogebra3Panel();
	}


}
