package fi.mathscratchgwt.client;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class MathScratchGWTDebug extends MathScratchGWT {

	@Override
	public void onModuleLoad() {

		prepareResources();
		
		dlp = new DockLayoutPanel(Style.Unit.PX);
		dlp.addStyleName(mathScratchCss.dock());
		dlp.setPixelSize(breedte , hoogte );
		
		RootLayoutPanel.get().add(dlp);
		RootLayoutPanel.get().addStyleName(mathScratchCss.root());
		
		init(breedte, hoogte, new HashMap<String, Object>(), new HashMap<String, Number>());
	}

}
