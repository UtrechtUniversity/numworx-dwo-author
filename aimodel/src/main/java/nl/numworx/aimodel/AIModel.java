package nl.numworx.aimodel;

import java.util.Locale;

import fi.beans.mainframe.JApplet;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;

public class AIModel extends JApplet implements WiskOpdrApplet {

	public InteractiePanel getInteractiePanel() {
		return new AIModelInteractiePanel();
	}

	public static void main(String[] args) {
	}

	private final Locale locale;

	public AIModel(Locale locale) {
		this.locale = locale;
	}
	
	
}