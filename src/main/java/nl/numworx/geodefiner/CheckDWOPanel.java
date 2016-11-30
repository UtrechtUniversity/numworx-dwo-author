package nl.numworx.geodefiner;

import java.awt.Dimension;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.wiskopdr.formuleobjects.FormuleEditor;

class CheckDWOPanel extends JPanel {

	private static final Integer DEFAULT_SCORE = Integer.valueOf(10);
	private JFormattedTextField score;
	private JCheckBox checkDWO;
	private FormuleEditor formule;

	CheckDWOPanel() {
		super();
		setName("CheckDWO");
		checkDWO = new JCheckBox("check");
		score = new JFormattedTextField(DEFAULT_SCORE);
		formule = new FormuleEditor(false);
		formule.setHeader(false);
		formule.setPreferredSize(new Dimension(200,50));
		
		add(checkDWO);
		add(score);
		add(formule);
	}
	
	int getMaxScore() {
		if(checkDWO.isSelected())
			return ((Number) score.getValue()).intValue();
		else
			return 0;
	}
	
	Map<String,Object> toMap() {
		Map<String,Object> result = new TreeMap<String,Object>();
		result.put("check", checkDWO.isSelected());
		result.put("score", score.getValue());
		result.put("formule", formule.formuleVak.toString());
		return result;
	}
	
	void fromMap(ObjectMap map) {
		if(map.containsKey("score"))
			score.setValue(map.getInt("score"));
		else
			score.setValue(DEFAULT_SCORE);
		checkDWO.setSelected(map.getBoolean("check", false));
		formule.formuleVak.vulVak(map.getString("formule"));
	}
	
}
