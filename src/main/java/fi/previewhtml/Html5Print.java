package fi.previewhtml;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fi.beans.scorm.SCORM12APIInterface;

public class Html5Print {
	final static Logger LOG = Logger.getLogger(Html5Print.class.getName());
	
	final SCORM12APIInterface api;
	Map<String,Object> launchData = Collections.emptyMap();
	Map<String,Object> suspendData = Collections.emptyMap();
	int aantalOpdrachten = 1;
	
	public Html5Print(SCORM12APIInterface api) {
		this.api = api;
	}

	public void init() {
		String launch = api.LMSGetValue("cmi.launch_data");
		try {
			Object parse = new JSONParser().parse(launch);
			if (parse instanceof Map) launchData = (Map<String, Object>) parse;
		} catch (ParseException e) {
			LOG.log(Level.SEVERE, "init launchdata",e);
		}
		aantalOpdrachten = Integer.parseInt(launchData.getOrDefault("aantalOpdrachten_1", "1").toString());
		
		String suspend = api.LMSGetValue("cmi.suspend_data");
		if (!suspend.isEmpty())
		try {
			Object parse = new JSONParser().parse(suspend);
			if (parse instanceof Map) suspendData = (Map<String,Object>) parse;
		} catch(ParseException e) {
			LOG.log(Level.SEVERE, "init suspenddata", e);
		}
	}
	
	
}
