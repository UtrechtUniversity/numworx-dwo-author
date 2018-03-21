package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.Box;
import javax.swing.JPanel;

import nl.numworx.geodefiner.common.Randomizer;
import nl.tue.win.riaca.openmath.lang.OMObject;
import fi.euclides.formuleobjects.FormuleParser;
import fi.wiskopdr.formuleobjects.FormuleEditor;

@SuppressWarnings("serial")
public class CommandPanel extends JPanel implements ActionListener, PropertyChangeListener, Randomizer {
	
	private FormuleEditor editor = new FormuleEditor(false) {

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			if(e.getSource() == this.formuleVak)
				produceAction(e.getActionCommand());
		} 
	};
	

	private Object config;
	private final Randomizer random;

	@Inject CommandPanel(Randomizer random) {
		super(new BorderLayout());
		this.random = random;
		add(Box.createVerticalStrut(120), BorderLayout.WEST);
		add(editor, BorderLayout.CENTER);
		editor.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == editor && "ingevuld".equals(e.getActionCommand()))
		{
			String string = editor.formuleVak.toString();
			while(string.startsWith("$f "))
				string = "$f" + string.substring(3);
			
			String substring = randomize(string);
			FormuleParser parser = new FormuleParser(substring.substring(2));
			OMObject object;
			try {
				object = parser.parse();
				firePropertyChange("command", string, object);
				editor.formuleVak.vulVak("$f@");
				
			} catch (fi.euclides.formuleobjects.ParseException e1) {
				firePropertyChange("feedback", substring, e1);
			} catch (fi.euclides.expr.InterpretException e2) {
				firePropertyChange("feedback", substring, e2);
			} catch (fi.euclides.formuleobjects.TokenMgrError e3) { // Error?
				firePropertyChange("feedback", substring, e3);				
			}
			return;
		}
	}

	
	public String randomize(String input) {
		if(random != null)
			return random.randomize(input);
		else 
			return input;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if("command".equals(evt.getPropertyName())) {
			String cmd = (String) evt.getNewValue();
			Object config = evt.getOldValue();
			firePropertyChange("config", this.config, config);
			this.config = config;
			editor.formuleVak.vulVak(cmd);
		}
	}
	
	public String toString() {
		return editor.formuleVak.toString();
	}

	public void fromString(String cmd) {
		editor.formuleVak.vulVak(cmd);
	}
	
	
}
