package nl.numworx.geodefiner.tools;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fi.euclides.event.EventHandler;
import fi.euclides.model.Destroyable;
import fi.euclides.model.VrijPunt;
import fi.euclides.swing.AWTViewer;
import nl.numworx.geodefiner.GeoDefiner;
import nl.numworx.geodefiner.common.UIShim;
import nl.numworx.geodefiner.ui.PointModel;
import nl.numworx.geodefiner.ui.UIEditor;

@SuppressWarnings("serial")
public class PuntAction extends fi.euclides.swing.PuntAction {

	private ImageIcon editImage = new ImageIcon(getClass().getResource("/nl/numworx/geodefiner/resources/edit.gif"));

	class PuntConfig extends AbstractAction implements ChangeListener  {

		@Override
		public void actionPerformed(ActionEvent e) {
			UIEditor editor = getEditor();
			Icon icon = (Icon) PuntAction.this.getValue(LARGE_ICON_KEY);
			String name = (String) PuntAction.this.getValue(NAME);
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
		PuntConfig( Icon icon) {
			super("", icon);
			setEnabled(false);
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

	UIShim<Destroyable, UIEditor> pointModel;
	public PuntAction(String name, String icon, EventHandler handler, AWTViewer viewer, UIShim<Destroyable, UIEditor> model) {
		super(name, icon, handler, viewer);
		if (GeoDefiner.isExperimental && GeoDefiner.isPremium) {
			putValue("config", new PuntConfig(editImage));
			pointModel = (model);
			putValue("model", pointModel);
		}
	}


}
