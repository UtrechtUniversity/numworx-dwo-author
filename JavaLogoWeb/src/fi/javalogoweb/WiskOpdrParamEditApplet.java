package fi.javalogoweb;

import java.util.Hashtable;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;

public interface WiskOpdrParamEditApplet extends WiskOpdrApplet {
	
	public Hashtable getDefaultParameters();
	
	public void setSingleComponent();
	

}
