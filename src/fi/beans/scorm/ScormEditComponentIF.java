// Source file: C:\\fi\\beans\\scorm\\ScormEditComponentIF.java

package fi.beans.scorm;

import java.awt.Component;
import java.util.Hashtable;

public interface ScormEditComponentIF {

    public Component getComponent();

    public Hashtable getLaunchData();
    
    public void setState(Hashtable h);
    
    public void end();
    
    public void reset();
}