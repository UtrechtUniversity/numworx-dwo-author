/*
 * File: IVMdrawGWTDebug.java
 *
 * Should only be used as the entry point for local testing and debugging.
 *
 */

package fi.ivmdrawgwt.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;

public class IVMdrawGWTDebug extends IVMdrawGWT {

	/**
	 * onModuleLoad that should be used for local testing and debugging.
	 */
	@Override
	public void onModuleLoad() {
		super.onModuleLoad();

		Map<String, Object> launchdata = new HashMap<>();
		Map<String, Number> random = Collections.emptyMap();

		init(breedte, hoogte, launchdata, random );
	}
}
