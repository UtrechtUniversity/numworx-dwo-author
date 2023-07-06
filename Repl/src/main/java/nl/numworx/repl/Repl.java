package nl.numworx.repl;

import java.util.Locale;

import fi.beans.mainframe.JApplet;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;

public class Repl extends JApplet implements WiskOpdrApplet {

	public InteractiePanel getInteractiePanel() {
		return new ReplInteractiePanel();
	}

	public static void main(String[] args) {
	}

	private final Locale locale;

	public Repl(Locale locale) {
		this.locale = locale;
	}
	
	
}
