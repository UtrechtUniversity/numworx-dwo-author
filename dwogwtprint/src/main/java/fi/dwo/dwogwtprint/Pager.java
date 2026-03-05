package fi.dwo.dwogwtprint;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import org.osgi.util.promise.Deferred;
import org.osgi.util.promise.Promise;

import nl.numworx.swingbrowser.scorm.SCORM2004APIInterface;

public class Pager implements Printable, SCORM2004APIInterface {

	Deferred<Void> terminated = new Deferred<>();
	private SCORM2004APIInterface delegate;
	
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
			return value;
		}
		return "";
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
