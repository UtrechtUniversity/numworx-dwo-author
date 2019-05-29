package fi.ivmdrawgwt.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;

public class IVMdrawGWTDebug extends IVMdrawGWT {

	@Override
	public void onModuleLoad() {
		ivmDrawGWTClientBundle = GWT.create(IVMdrawGWTClientBundle.class);
		ivmDrawCss = ivmDrawGWTClientBundle.getKladjeGWTCSS();
		ivmDrawCss.ensureInjected();

		dlp = new DockLayoutPanel(Style.Unit.PX);
		dlp.addStyleName(ivmDrawCss.dock());
		dlp.setPixelSize(breedte , hoogte);

		super.mainPanel.add(dlp);
		super.mainPanel.add(super.label);

		super.mainPanel.setWidgetLeftWidth(dlp, 0, Style.Unit.PCT, 50, Style.Unit.PCT);
		super.mainPanel.setWidgetRightWidth(super.label, 40, Style.Unit.PCT, 20, Style.Unit.PCT);
		super.mainPanel.setWidgetTopHeight(super.label, 5, Style.Unit.PCT, 50, Style.Unit.PCT);

		super.label.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
		super.label.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);

		RootLayoutPanel.get().add(super.mainPanel);
		RootLayoutPanel.get().addStyleName(ivmDrawCss.root());

		ivmFeedbackGWTField = new IVMfeedbackGWTField(this);

		Map<String, Object> launchdata = new HashMap<>();
		Map<String, Number> random = Collections.emptyMap();

		init(breedte, hoogte, launchdata, random );
	}
}
