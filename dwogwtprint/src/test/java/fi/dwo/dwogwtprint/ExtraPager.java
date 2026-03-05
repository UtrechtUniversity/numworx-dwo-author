package fi.dwo.dwogwtprint;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import nl.uu.fi.dwo.lms.jclient.lib.rest.managers.PublicProfileManager;
import nl.uu.fi.dwo.lms.jclient.lib.rest.transport.StoredRestManager;
import nl.uu.fi.dwo.rest.dom.entities.DomDwoProfileFull;

public class ExtraPager extends Pager {
	int profileid;
	DomDwoProfileFull profile;
	String scodata;
	Properties p = new Properties();
	
	public ExtraPager(int sco, int profile) {
		
		// TODO Auto-generated constructor stub
		String u = "http://localhost:8080/dwo/rest/public/scoData/getJSONLaunchDataBytes?scoId=" + sco;
		
		try {
			StoredRestManager.getInstance().getAuthenticator().setServerUrlPath(new URL("http://localhost:8080/dwo/"));
			URL url = new URL(u);
			InputStream data = url.openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(data));
			scodata = "";
			reader.lines().forEach(s -> scodata += s);
			p.put("cmi.launch_data", scodata);
			p.put("dme.abo_type", "premium");
			p.put("cmi.learner_name", "wim - ja wim");
			p.put("cmi.score.raw", "10");
			p.put("cmi.comments_from_lms.0.timestamp", "2026-02-03T13:21:12.573+0000");
			p.put("dme.sco_name", "testsco");
			p.put("dme.team", "team X");		
			this.profile = PublicProfileManager.get(profile);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public String GetValue(String key) {
		return p.getProperty(key, "");
	}

}
