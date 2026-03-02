package fi.dwo.dwogwtprint;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import org.osgi.util.promise.Deferred;
import org.osgi.util.promise.Promise;

import nl.numworx.swingbrowser.scorm.SCORM2004APIInterface;
import nl.uu.fi.dwo.rest.dom.entities.DomDwoProfileFull;
import nl.uu.fi.dwo.lms.jclient.lib.rest.managers.PublicProfileManager;
import nl.uu.fi.dwo.lms.jclient.lib.rest.transport.StoredRestManager;

public class Pager implements Printable, SCORM2004APIInterface {

	int profileid;
	DomDwoProfileFull profile;
	String scodata;
	
	Properties p = new Properties();
	Deferred<Void> terminated = new Deferred<>();
	private SCORM2004APIInterface delegate;
	
	public Pager(int sco, int profile) {
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

	public Promise<Void> getTerminated() {
		return terminated.getPromise();
	}
		
	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		return NO_SUCH_PAGE;
	}

	@Override
	public String Initialize(String dummy) {
		return "true";
	}

	@Override
	public String Commit(String dummy) {
		return "true";
	}

	@Override
	public String Terminate(String dummy) {
		terminated.resolve(null);
		return "true";
	}

	@Override
	public String GetValue(String key) {
		System.out.println("getValue " + key);
		if (delegate != null) {			
			String value = delegate.GetValue(key);
			if ("cmi.launch_data".equals(key)) {
				return toJSON(value);
			}
			//return value;
		}
		return p.getProperty(key, "");
	}

	private String toJSON(String value) {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public String SetValue(String key, String value) {
		return "true";
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
		return "No Error";
	}

	public void reset() {
		terminated = new Deferred<Void>();
		
	}

	public void setDelegate(SCORM2004APIInterface painter) {
		if (painter == this)
			delegate = null;
		else
			this.delegate = painter;
		
	}

}
