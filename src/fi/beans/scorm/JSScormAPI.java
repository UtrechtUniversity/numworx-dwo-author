package fi.beans.scorm;

import java.applet.*;
import java.net.*;
import java.io.*;

import netscape.javascript.*;

public class JSScormAPI implements SCORM12APIInterface {
	private JSObject window;

	/**
	 * @author Bastiaan Grutters
	 * @author Alexander Elias
	 */

	public JSScormAPI(Applet parent) {
		try {
			window = JSObject.getWindow(parent);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String LMSInitialize(String iParam) {
		return null;
	}

	public String LMSFinish(String iParam) {
		return null;
	}

	public String LMSGetValue(String iDataModelElement) {
		String result = "";
		if (window != null) {
			try {
				System.out.println("JSAPI->LMSGetValue(" + iDataModelElement + ")");
				Object[] args = new Object[1];
				args[0] = iDataModelElement;
				result = (String) window.call("LMSGetValue", args);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (result != null && !result.equals("")) {
			if (iDataModelElement.equals("cmi.suspend_data"))
				result = URLCoder.decode(result);
			System.out.println("Result:\r\n" + result);
		}

		return result;
	}

	public String LMSSetValue(String iDataModelElement, String iValue) {
		String result = "";
		if (iDataModelElement.equals("cmi.suspend_data"))
			iValue = URLCoder.encode(iValue);
		if (window != null) {
			try {
				Object[] args = new Object[2];
				args[0] = iDataModelElement;
				args[1] = iValue;
				result = (String) window.call("LMSSetValue", args);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public String LMSCommit(String iParam) {
		return null;
	}

	public String LMSGetLastError() {
		return null;
	}

	public String LMSGetErrorString(String iErrorCode) {
		return null;
	}

	public String LMSGetDiagnostic(String iErrorCode) {
		return null;
	}
}