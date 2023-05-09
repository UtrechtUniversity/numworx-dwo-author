package nl.numworx.geodefiner.tools;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fi.beans.numworxlf.JOptionPane;
import fi.euclides.model.Destroyable;
import nl.numworx.geodefiner.common.UIShim;
import nl.numworx.geodefiner.ui.UIEditor;

class PuntConfig extends AbstractAction implements ChangeListener  {

	UIShim<Destroyable, UIEditor> pointModel;
	Action action;

	@Override
	public void actionPerformed(ActionEvent e) {
		UIEditor editor = getEditor();
		Icon icon = (Icon) action.getValue(LARGE_ICON_KEY);
		String name = (String) action.getValue(NAME);
		editor.setName(name);
		int ok = JOptionPane.showConfirmDialog((Component) e.getSource(), editor, name, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, icon);
				
		if(ok == JOptionPane.YES_OPTION) {
			editor.commit();
			pointModel.set = true;
		}
	}

	private UIEditor getEditor() {
		return pointModel.editor();
	}

	private ButtonModel checkModel;
	PuntConfig(Icon icon, Action action) {
		super("", icon);
		setEnabled(false);
		this.action = action;
		this.pointModel = (UIShim<Destroyable, UIEditor>) action.getValue("model");
	}

	@Override
	public void putValue(String key, Object newValue) {
		if ("checkModel".equals(key)) {
			checkModel = (ButtonModel) newValue;
			checkModel.addChangeListener(this);
			setEnabled(checkModel.isSelected());
		}
		super.putValue(key, newValue);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		setEnabled(checkModel.isSelected());
	}
	
}