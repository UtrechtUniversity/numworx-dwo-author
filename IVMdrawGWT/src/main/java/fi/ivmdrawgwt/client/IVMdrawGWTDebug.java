package fi.ivmdrawgwt.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class IVMdrawGWTDebug extends IVMdrawGWT {

	@Override
	public void onModuleLoad() {
		
		ivmDrawGWTClientBundle = GWT.create(IVMdrawGWTClientBundle.class);
		ivmDrawCss = ivmDrawGWTClientBundle.getKladjeGWTCSS();
		ivmDrawCss.ensureInjected();
		
		dlp = new DockLayoutPanel(Style.Unit.PX);
		dlp.addStyleName(ivmDrawCss.dock());
		dlp.setPixelSize(breedte , hoogte );

		
		RootLayoutPanel.get().add(dlp);
		RootLayoutPanel.get().addStyleName(ivmDrawCss.root());

		Map<String, Object> launchdata = new HashMap<>();
		Map<String, Number> random = Collections.emptyMap();

		init(breedte, hoogte, launchdata, random );
	}
}
