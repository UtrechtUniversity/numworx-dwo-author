package nl.numworx.geodefiner.tools;

import javax.swing.ImageIcon;

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
			putValue("config", new PuntConfig(editImage, this));
		}
	}


}
