package fi.beans.browser;

import fi.beans.scorm.SCORM12APIInterface;
import fi.beans.scorm.SCORM2004APIInterface;

public class Scorm2004API extends FilterAPI implements SCORM2004APIInterface, SCORM12APIInterface , nl.numworx.swingbrowser.scorm.SCORM2004APIInterface{

  /**
   * 
   */

  public String Commit(String arg) { return LMSCommit(arg); }
  public String GetValue(String name) { return LMSGetValue(name); }
  public String GetLastError() { return LMSGetLastError(); }

  public String SetValue(String name, String value) {
    return LMSSetValue(name, value);
  }

  public String Terminate(String arg) { return LMSFinish(arg); }      
  public String Initialize(String arg) {
    return LMSInitialize(arg); }
  
  public String GetErrorString(String iErrorCode) { return LMSGetErrorString(iErrorCode); }
  public String GetDiagnostic(String iErrorCode) {return LMSGetDiagnostic(iErrorCode); }
  
  Scorm2004API(SCORM12APIInterface scorm12apiInterface) {
    super(scorm12apiInterface);
  }
}