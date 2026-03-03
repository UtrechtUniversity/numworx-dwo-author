package fi.wiskopdr.tekstobjects;

import java.net.URL;

import fi.beans.mainframe.AppletContext;
import fi.beans.mainframe.AppletStub;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;
import fi.wiskopdr.WiskOpdr;

class Stub implements AppletStub {

private static AppletStub instance = new Stub();
private Stub() {}
static void setStub(WiskOpdrApplet o) {
  try {
    o.setStub(instance);
  } catch(Throwable e) {
    // ignore
  }
}
  
@Override
public boolean isActive() {
  return true;
}

@Override
public URL getDocumentBase() {
  return WiskOpdr.applet.getDocumentBase();
}

@Override
public URL getCodeBase() {
  return WiskOpdr.applet.getCodeBase();
}

@Override
public String getParameter(String name) {
  return WiskOpdr.applet.getParameter(name);
}

@Override
public AppletContext getAppletContext() {
  return (AppletContext) WiskOpdr.applet.getAppletContext();
}

@Override
public void appletResize(int width, int height) {
}}