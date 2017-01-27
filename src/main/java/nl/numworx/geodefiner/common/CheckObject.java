package nl.numworx.geodefiner.common;

import java.util.HashMap;
import java.util.Map;

import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;

public class CheckObject {
	int maxScore;
	int present = Label.FALSE;
	Destroyable item, cache;
	String formule = "";
	
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
		if(formule == null) formule = "";
		this.formule = formule;
	}
	/**
	 * @param maxScore the maxScore to set
	 */
	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}

	public void setMaxScore(Number maxScore) {
		if (maxScore == null) this.maxScore = 0;
		else this.maxScore = maxScore.intValue();
	}

	public void fromMap(ObjectMap map) {
		if(map.containsKey("score"))
				setMaxScore(map.getInt("score"));
		else 
			setMaxScore(0);
		if(map.containsKey("value")) 
				setFormule(map.getString("value"));
		else
			setFormule("");
	}
	
	public Map toMap() {
		HashMap map = new HashMap();
		if(maxScore > 0)
			map.put("score", maxScore);
		if(!formule.isEmpty())
			map.put("value", formule);
		return map;
	}
	
}
