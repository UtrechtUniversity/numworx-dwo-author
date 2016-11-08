package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JPanel;

import nl.tue.win.riaca.openmath.lang.OMObject;
import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.openmath.Popcorn;
import fi.euclides.swing.SwingSymbols;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.FormuleEditor;

class CommandPanel extends JPanel implements ActionListener {
	
	FormuleEditor editor = new FormuleEditor(false) {

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			if(e.getSource() == this.formuleVak)
				produceAction(e.getActionCommand());
		} 
	};
	
	//@Inject
	Map<String, Number> random = Collections.emptyMap();
	//@Inject
	nl.numworx.geodefiner.common.Instance instance;

	static {
		Popcorn.map = new SwingSymbols();
		WiskOpdr.applet = new WiskOpdr();
	}
	
	
	CommandPanel() {
		super(new BorderLayout());
		add(Box.createVerticalStrut(90), BorderLayout.WEST);
		add(editor, BorderLayout.CENTER);
		editor.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == editor && "ingevuld".equals(e.getActionCommand()))
		{
			String string = editor.formuleVak.toString();
			String substring = randomize(string.substring(2));
			FormuleParser parser = new FormuleParser(substring);
			OMObject object;
			try {
				object = parser.parse();
				firePropertyChange("command", string, object);
				editor.formuleVak.vulVak("$f@");
				
			} catch (fi.euclides.formuleobjects.ParseException e1) {
				e1.printStackTrace();
			}
			
			return;
		}
	}

	
	private String randomize(String input) {
		if(random != null)
			return instance.randomize(random, input);
		else 
			return input;
	}
	
}
