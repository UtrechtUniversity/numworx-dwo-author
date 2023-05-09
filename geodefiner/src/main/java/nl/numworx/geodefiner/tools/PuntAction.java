package nl.numworx.geodefiner.tools;

import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;

import fi.euclides.event.EventHandler;
import fi.euclides.model.Destroyable;
import fi.euclides.swing.AWTViewer;
import nl.numworx.geodefiner.GeoDefiner;
import nl.numworx.geodefiner.common.UIShim;
import nl.numworx.geodefiner.ui.UIEditor;

@SuppressWarnings("serial")
public class PuntAction extends fi.euclides.swing.PuntAction {

	private ImageIcon editImage = new ImageIcon(getClass().getResource("/nl/numworx/geodefiner/resources/edit.gif"));

	public PuntAction(String name, String icon, EventHandler handler, AWTViewer viewer, UIShim<Destroyable, UIEditor> model) {
		super(name, icon, handler, viewer);
		if (GeoDefiner.isPremium) {
			putValue("model", model);
			PuntConfig puntConfig = new PuntConfig(editImage, this) {
				public void stateChanged(ChangeEvent e) {
				}

				@Override
				public boolean isEnabled() {
					return true;
				}

				@Override
				public void setEnabled(boolean newValue) {
					super.setEnabled(true);
				}
				
			};
			puntConfig.setEnabled(true);
			putValue("config", puntConfig);
		}
	}


}
