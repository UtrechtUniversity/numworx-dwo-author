package nl.numworx.geodefiner;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.JPanel;

import fi.wiskopdr.VariableCollection;
import fi.wiskopdr.tekstobjects.TekstEditor;
import fi.wiskopdr.tekstobjects.TekstVak;

@SuppressWarnings("serial")
class RandomPanel extends JPanel implements ActionListener, FocusListener {

	private TekstEditor randomVarEditor;
	final private Map<String,Number> randomVars;

	@Inject RandomPanel( @Named("random") Map<String,Number> randomVars) {
		super(null);
		this.randomVars = randomVars;
		setName("Default Random Vars");
		TekstVak tekstVak = new TekstVak();
		tekstVak.addFocusListener(this);
		randomVarEditor = new TekstEditor(true, false, true, tekstVak);
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

	@Override
	public void focusGained(FocusEvent e) {
	}

	@Override
	public void focusLost(FocusEvent e) {
		getRandomVars();
	}
	
	
}
