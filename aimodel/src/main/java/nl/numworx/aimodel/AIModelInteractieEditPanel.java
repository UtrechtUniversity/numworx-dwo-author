package nl.numworx.aimodel;

import java.util.Hashtable;
import java.util.Objects;

import javax.swing.JPanel;

import fi.beans.numworxlf.JCheckBox;
import fi.beans.numworxlf.JLabel;
import fi.beans.numworxlf.JTextField;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.wiskopdr.ObjectiveChoiceButton;

public class AIModelInteractieEditPanel extends JPanel implements InteractieEditPanel {

	private ObjectiveChoiceButton objectives; // voor de logging
	private JTextField logid;
	private JCheckBox  logOption;
	
	public AIModelInteractieEditPanel() {
		logOption = new JCheckBox("logID");
		logid = new JTextField();
		logid.setColumns(10);
		objectives = new ObjectiveChoiceButton();
		objectives.setEnabled(ObjectiveChoiceButton.hasObjectiveChoices());
// FIXME fatsoeneren, nu even niet		
		add(logOption);
		add(logid);
		add(objectives);
		
	}

	public Hashtable getEditState() {
		Hashtable launchData = new Hashtable();
		launchData.put("logOption", logOption.isSelected());
		launchData.put("logID", logid.getText());
		launchData.putAll(objectives.getEditState(0));
		return launchData;
	}

	public void setEditState(Hashtable state) {
		boolean logOption = false;
		String logID = "";
		if (state.containsKey("logOption"))
			logOption = Boolean.TRUE.equals(state.get("logOption"));
		if (state.containsKey("logID")) 
			logID = Objects.toString(state.get("logID"), logID);
		
		
		this.logOption.setSelected(logOption);
		this.logid.setText(logID);
		objectives.setEditState(state);
		
	}

	public void start() {
	}

	public void stop() {
	}

	public void zetBreedte(int arg0) {
	}

	public void zetHoogte(int arg0) {
	}

}
