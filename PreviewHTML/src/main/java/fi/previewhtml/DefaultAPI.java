package fi.previewhtml;

import java.applet.Applet;

import fi.beans.scorm.SCORM12APIInterface;

public class DefaultAPI implements SCORM12APIInterface {

	public DefaultAPI() {
	}
	
	public DefaultAPI(Applet applet) { // part of SCORM12Interface
	}

	@Override
	public String LMSInitialize(String iParam) {
		return "true";
	}

	@Override
	public String LMSFinish(String iParam) {
		return "true";
	}

	@Override
	public String LMSGetValue(String iDataModelElement) {
		return "";
	}

	@Override
	public String LMSSetValue(String iDataModelElement, String iValue) {
		return "true";
	}

	@Override
	public String LMSCommit(String iParam) {
		return "true";
	}

	@Override
	public String LMSGetLastError() {
		return "0";
	}

	@Override
	public String LMSGetErrorString(String iErrorCode) {
		return "";
	}

	@Override
	public String LMSGetDiagnostic(String iErrorCode) {
		return "";
	}

}
