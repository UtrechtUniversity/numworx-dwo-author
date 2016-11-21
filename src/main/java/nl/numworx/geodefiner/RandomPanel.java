package nl.numworx.geodefiner;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JPanel;

import fi.wiskopdr.VariableCollection;
import fi.wiskopdr.tekstobjects.TekstEditor;
import fi.wiskopdr.tekstobjects.TekstVak;

class RandomPanel extends JPanel implements ActionListener {

	private TekstEditor randomVarEditor;
	private Map<String,Number> randomVars = new LinkedHashMap<String, Number>();

	RandomPanel() {
		super(null);
		setName("Default Random Vars");
		randomVarEditor = new TekstEditor(true, false, true, new TekstVak());
		randomVarEditor.setHeader(true);
		randomVarEditor.setBounds(0,0,200,200);
		randomVarEditor.setResizable(true);
		randomVarEditor.addActionListener(this);
		add(randomVarEditor);
	}

	public void actionPerformed(ActionEvent e) {
		getRandomVars(); // reflect text
	}

	public String getText() {
		return randomVarEditor.getText();
	}
	
	public void setText(String text) {
		if(text == null) text = "";
		randomVarEditor.zetTekst(text);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		Insets insets = getInsets();
		randomVarEditor.setBounds(insets.left, insets.top, width-insets.left-insets.right, height-insets.bottom-insets.top);
		super.setBounds(x, y, width, height);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Number> getRandomVars() {
		VariableCollection vc = new VariableCollection();
		vc.setVariables(getText());
		randomVars.keySet().retainAll(Arrays.asList(vc.getVariableNames()));
		randomVars.putAll(vc.getRandomValues());
		return randomVars;
	}

	@Override
	public void setVisible(boolean aFlag) {
		if(!aFlag) getRandomVars(); // on hidden, update randomvars
		super.setVisible(aFlag);
	}
	
	
}
