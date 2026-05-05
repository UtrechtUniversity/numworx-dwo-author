package nl.numworx.fsm.editor;

import java.awt.BorderLayout;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JPanel;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

@SuppressWarnings("serial")
public class FSMInterActieEditPanel extends JPanel implements InteractieEditPanel {

	FSMInteractiePanel interactiePanel;
	Editor editor;
	
	public FSMInterActieEditPanel(FSMInteractiePanel interactiePanel) {
		super(new BorderLayout());
		this.interactiePanel = interactiePanel;
		editor = new Editor(interactiePanel, interactiePanel.rb);
		add(editor, BorderLayout.CENTER);
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setEditState(Hashtable b) {
		if (b == null) b = new Hashtable();
		editor.setLaunchData(b);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Hashtable getEditState() {
		Map<String, ?> m = editor.getLaunchData();
		return new Hashtable(m);
	}

	@Override
	public void zetBreedte(int b) {
		editor.setInstanceWidth(b);
	}

	@Override
	public void zetHoogte(int h) {
		editor.setInstanceHeight(h);
	}

	@Override
	public void stop() {
		editor.stop();
	}

	@Override
	public void start() {
		editor.start();
	}

}
