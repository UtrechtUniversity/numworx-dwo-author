package nl.numworx.geodefiner.tools;

import javax.swing.ImageIcon;

import fi.euclides.event.EventHandler;
import fi.euclides.model.Destroyable;
import fi.euclides.swing.AWTViewer;
import nl.numworx.geodefiner.GeoDefiner;
import nl.numworx.geodefiner.common.UIShim;
import nl.numworx.geodefiner.ui.UIEditor;

public class CirkelAction extends fi.euclides.swing.CirkelAction {
	private ImageIcon editImage = new ImageIcon(getClass().getResource("/nl/numworx/geodefiner/resources/edit.gif"));
	public CirkelAction(String name, String icon, EventHandler handler, AWTViewer viewer, UIShim<Destroyable, UIEditor> uimodel) {
		super(name, icon, handler, viewer);
		if (GeoDefiner.isPremium) {
			handler.setDecorator(uimodel);
			putValue("model", uimodel);
			putValue("config", new PuntConfig(editImage, this));
		}	
	}

}
