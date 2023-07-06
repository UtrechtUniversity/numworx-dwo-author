package fi.beans.browser;

import fi.beans.scorm.SCORM12APIInterface;

public class FilterAPI implements SCORM12APIInterface {

	final private SCORM12APIInterface api;
	
	public FilterAPI(SCORM12APIInterface api) {
		this.api = api;
	}

	public String LMSInitialize(String iParam) {
		return api.LMSInitialize(iParam);
	}

	public String LMSFinish(String iParam) {
		return api.LMSFinish(iParam);
	}

	public String LMSGetValue(String iDataModelElement) {
		return api.LMSGetValue(iDataModelElement);
	}

	public String LMSSetValue(String iDataModelElement, String iValue) {
		return api.LMSSetValue(iDataModelElement, iValue);
	}

	public String LMSCommit(String iParam) {
		return api.LMSCommit(iParam);
	}

	public String LMSGetLastError() {
		return api.LMSGetLastError();
	}

	public String LMSGetErrorString(String iErrorCode) {
		return api.LMSGetErrorString(iErrorCode);
	}

	public String LMSGetDiagnostic(String iErrorCode) {
		return api.LMSGetDiagnostic(iErrorCode);
	}

}
