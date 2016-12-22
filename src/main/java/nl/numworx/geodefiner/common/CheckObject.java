package nl.numworx.geodefiner.common;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;

public class CheckObject {
	int maxScore;
	int present = Label.FALSE;
	Destroyable item, cache;
	String formule;
	
	public int getScore() {
		return (present >= 0) ? maxScore : 0;
	}
	public int getMaxScore() {
		return maxScore;
	}
	public String getFormule() {
		return formule;
	}
	public void setFormule(String formule) {
		this.formule = formule;
	}
	
	
}
