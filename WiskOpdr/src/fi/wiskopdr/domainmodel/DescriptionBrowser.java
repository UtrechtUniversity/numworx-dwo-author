package fi.wiskopdr.domainmodel;

import java.util.Locale;

import javax.swing.JComponent;

import fi.wiskopdr.SimpleSwingBrowser;
import nl.numworx.swingbrowser.api.ConsoleEvent;
import nl.numworx.swingbrowser.scorm.ConsoleListener;
import nl.numworx.swingbrowser.scorm.SCORM2004APIInterface;

public class DescriptionBrowser extends SimpleSwingBrowser implements SCORM2004APIInterface, ConsoleListener {

  private String description;

  public DescriptionBrowser() {
    super();
    browser.setAPI(this);
    browser.addConsoleListener(this);
  }

  public void setDescription(String description) {
    this.description = description;
    Locale locale = JComponent.getDefaultLocale();
    String profile = "77"; ///?????
    long random = System.currentTimeMillis();
    String url = "https://app.dwo.nl/dwo/apps/player.html?footer=none&locale="
        + locale
        + "#cmi.launch_data:0";
    loadURL(url);
  }
  
  
  @Override
  public String Initialize(String dummy) {
    return "true";
  }

  @Override
  public String Commit(String dummy) {    // TODO Auto-generated method stub
    return "";
  }

  @Override
  public String Terminate(String dummy) {    // TODO Auto-generated method stub
    return "";
  }

  @Override
  public String GetValue(String key) {
    if ("dme.abo_type".equals(key)) return "premium";
    if ("cmi.launch_data".equals(key))
      return description;
    return "";
  }

  @Override
  public String SetValue(String key, String value) {
    return "true"; // DROP on the floor
  }

  @Override
  public String GetLastError() {
    return "0";
  }

  @Override
  public String GetDiagnostic(String iErrorCode) {
    return "";
  }

  @Override
  public String GetErrorString(String iErrorCode) {
    return "";
  }

  @Override
  public void onConsole(ConsoleEvent event) {
    System.err.println(event.getMessage());
    
  }

}
