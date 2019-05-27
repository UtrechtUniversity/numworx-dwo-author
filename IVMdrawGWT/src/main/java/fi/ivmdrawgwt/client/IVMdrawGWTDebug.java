package fi.ivmdrawgwt.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;

public class IVMdrawGWTDebug extends IVMdrawGWT {
	public HorizontalPanel mainPanel = new HorizontalPanel();
	public Label label = new Label("hello world!");

	@Override
	public void onModuleLoad() {
		ivmDrawGWTClientBundle = GWT.create(IVMdrawGWTClientBundle.class);
		ivmDrawCss = ivmDrawGWTClientBundle.getKladjeGWTCSS();
		ivmDrawCss.ensureInjected();

		dlp = new DockLayoutPanel(Style.Unit.PX);
		dlp.addStyleName(ivmDrawCss.dock());
		dlp.setPixelSize(breedte , hoogte);

		mainPanel.add(dlp);
		mainPanel.add(label);

		RootLayoutPanel.get().add(mainPanel);
		RootLayoutPanel.get().addStyleName(ivmDrawCss.root());

//		RootPanel.get().add(mainPanel);
		ivmFeedbackGWTField = new IVMfeedbackGWTField(this);

		Map<String, Object> launchdata = new HashMap<>();
		Map<String, Number> random = Collections.emptyMap();

		init(breedte, hoogte, launchdata, random );
	}



}
