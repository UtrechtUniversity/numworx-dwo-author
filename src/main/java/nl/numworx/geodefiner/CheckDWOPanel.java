package nl.numworx.geodefiner;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.Box;
import javax.swing.BoxLayout;
import fi.beans.numworxlf.JButton;
import fi.beans.numworxlf.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import fi.beans.numworxlf.JTextField;
import fi.beans.numworxlf.NumworxTextFieldUI;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nl.numworx.geodefiner.common.Randomizer;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.event.Tracker;
import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.openmath.Expression;
import fi.wiskopdr.ObjectiveChoiceButton;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.FormuleEditor;

class CheckDWOPanel extends JPanel implements ChangeListener, ActionListener {

	private static final Integer DEFAULT_SCORE = Integer.valueOf(10);
	private JFormattedTextField score;
	private JCheckBox checkDWO, extern, logCB;
	private FormuleEditor formule;
	private JTextField logID;
	private Tracker tracker;
	
	@Inject @Named("checkBtn") javax.swing.JButton checkBtn;
	@Inject @Named("validator") JComponent validator = this;
	ObjectiveChoiceButton objBtn;

	@Inject void setTracker(Tracker tracker) {
		this.tracker = tracker;
//		Randomizer random = tracker.adapt(Randomizer.class);
//		Expression expr = tracker.adapt(Expression.class);
	}

	private boolean checkFormula() {
		if(tracker != null) {
			String string = formule.formuleVak.toString();
			if("$f@".equals(string)) return false;
			try {
				Randomizer random = tracker.adapt(Randomizer.class);
				string = random.randomize(string);
				FormuleParser parser = new FormuleParser(string.substring(2)); // remove "$f"
				OMObject object = parser.logic();
				Expression expr = tracker.adapt(Expression.class);
				Destroyable test = expr.interpret(object, new Label(), tracker.getMapper());
				test.destroy();
				return false;
			} catch(Throwable t) {
				PropertyChangeEvent event = new PropertyChangeEvent(formule, "feedback", string, t);
				firePropertyChange(event);
				return true;
			}
		} else 
			return false;
	}
	
	
	
	@Inject CheckDWOPanel() {
		super(null);
		BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(layout);
		setName("CheckDWO");
		checkDWO = new JCheckBox(Messages.getString("check"));
		extern = new JCheckBox(Messages.getString("extern"));
		extern.setEnabled(checkDWO.isSelected());
		checkDWO.addChangeListener(this);
		extern.addChangeListener(this);
		score = new JFormattedTextField(DEFAULT_SCORE);
		score.setUI(NumworxTextFieldUI.createUI(score));
		score.setColumns(5);
		score.setMaximumSize(score.getPreferredSize());
		formule = new FormuleEditor(false);
		formule.setHeader(false);
		formule.setPreferredSize(new Dimension(200,50));
		formule.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));
		formule.formuleVak.addActionListener(this);
		if(WiskOpdr.objectives!=null)
			objBtn = new ObjectiveChoiceButton(WiskOpdr.objectives, WiskOpdr.categorieString, WiskOpdr.studentModel);
		add(checkDWO);
		add(extern);
		Box hbox = Box.createHorizontalBox();
		hbox.add(new JLabel(Messages.getString("score"))); hbox.add(score);
		add(hbox);
		add(new JLabel("CheckDWO = "));
		add(formule);
		if(objBtn != null) add(objBtn);
		
		hbox = Box.createHorizontalBox();
		logCB = new JCheckBox("Log");
		logID = new JTextField(15);
		
		Dimension max = logID.getPreferredSize();
		logID.setMaximumSize(max);
		max.width = logCB.getPreferredSize().width;
		logCB.setMinimumSize(max);
		logCB.addActionListener(ev -> {logID.setVisible(logCB.isSelected());
		                               logID.invalidate();
		                               this.validate();
		                              });
		hbox.add(logCB);
		hbox.add(logID);
		hbox.add(Box.createHorizontalGlue());
		hbox.validate();
		hbox.setMinimumSize(hbox.getPreferredSize());
		hbox.setPreferredSize(hbox.getPreferredSize());
        logID.setVisible(logCB.isSelected());
		
		add(hbox);
		add(Box.createGlue());
//// track focuslost
//		VetoableChangeListener verifier = new VetoableChangeListener() {
//
//			@Override
//			public void vetoableChange(PropertyChangeEvent evt)
//					throws PropertyVetoException {
//				System.out.println(evt);
//				
//			}};
//		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
//// FIXME destroyer!!!!
//		manager.addVetoableChangeListener("focusOwner", verifier);
		
	}
	
	int getMaxScore() {
		if(checkDWO.isSelected())
			return ((Number) score.getValue()).intValue();
		else
			return 0;
	}
	
	public void firePropertyChange(PropertyChangeEvent e) {
		for (PropertyChangeListener l : getPropertyChangeListeners(e.getPropertyName())) {
			l.propertyChange(e);
		}
	}
	
	
	public boolean isLogOption() {
	  return logCB.isSelected();
	}
	public void setLogOption(boolean b) {
	  logCB.setSelected(b);
	  logID.setVisible(b);
	}
	public String getLogID() {
	  return logID.getText();
	}
	public void setLogID(String id) {
	  logID.setText(id);
	}
	
	public void setChoices(boolean[][] choices) {
		if(objBtn != null)
			objBtn.setChoices(choices);
	}

	public boolean[][] getChoices() {
		if(objBtn != null)
			return objBtn.getChoices();
		return null;
	}

	Map<String,Object> toMap() {
		Map<String,Object> result = new TreeMap<String,Object>();
		result.put("check", checkDWO.isSelected());
		result.put("score", score.getValue());
		result.put("formule", formule.formuleVak.toString());
		result.put("extern", extern.isSelected());
		return result;
	}
	
	void fromMap(ObjectMap map) {
		if(map.containsKey("score"))
			score.setValue(map.getInt("score"));
		else
			score.setValue(DEFAULT_SCORE);
		checkDWO.setSelected(map.getBoolean("check", false));
		extern.setSelected(map.getBoolean("extern",false));
		formule.formuleVak.vulVak(map.getString("formule"));
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(checkBtn != null) {
			checkBtn.setVisible(checkDWO.isSelected() && !extern.isSelected());
			checkBtn.invalidate();
			validator.validate();
		}
// okay?
		extern.setEnabled(checkDWO.isSelected());
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		//if("zetMaat".equals(cmd)|| "focus".equals(cmd)||"formChanged".equals(cmd)||"".equals(cmd)) return;
		if("ingevuld".equals(cmd))
			checkFormula();
	}
	
}
