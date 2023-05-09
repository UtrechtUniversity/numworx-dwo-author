package nl.numworx.geodefiner.applet;

import java.applet.AppletStub;
import java.net.URL;
import java.util.Locale;

import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import nl.numworx.geodefiner.GeoDefiner;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.CBookService;
import org.cbook.cbookif.Constants;
import org.cbook.cbookif.ServiceImpl;

import cbookeditor.CBookEditor;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.FormuleButton;

public class Main extends JApplet implements CBookContext, Constants, CBookEventListener, AppletStub {
	private JTextArea area;
	private CBookService service;
	private CBookEditor editor;
	private String learner_id;
	
	@Override
	public void init() {
		JComponent.setDefaultLocale(new Locale("en"));
		
		WiskOpdr wiskopdr = new WiskOpdr();
		WiskOpdr.applet = wiskopdr;
		WiskOpdr.dwo_env = "test";
		wiskopdr.setStub(this);
		
// request some sort of identifier.
		learner_id = getParameter("cmi.learner_id");
		if(learner_id == null) learner_id = "";
		else learner_id = learner_id.trim();
		
		area = new JTextArea();
		area.setRows(5);

		service = new ServiceImpl();

// This one always there.
		service.registerWidget(new nl.numworx.geodefiner.GeoDefiner());

		editor = createCBookEditor();
		setContentPane(editor);
		setSize(editor.getPreferredSize());
	}

	private CBookEditor createCBookEditor() {
		return new CBookEditor(new JScrollPane(area), service, this, this);
	}

	public Object getProperty(String key) {
		if( LEARNER_ID .equals( key)) {
			return learner_id;
		}
		
		if( "AppletContext".equals(key))
			return getAppletContext();
// can throw ClassNotFoundError
//		try {
//			if("JSObject".equals(key))
//				return JSObject.getWindow(this);
//		} catch (Throwable _) {
//		}
		return null;
	}

	public void acceptCBookEvent(CBookEvent event) {
		area.append(event.getSource().getClass().getName());
		area.append(": ");
		area.append(event.getCommand());
		area.append(", ");
		area.append(String.valueOf(event.getParameters()));
		area.append("\n");
		String tekst = editor.getSuccessStatus() + " " + editor.getScore() + "/" + editor.getMaxScore();
		showStatus(tekst);		
	}

	public void destroy() {
		showStatus("destroy");
		editor.destroy();
	}

	public void appletResize(int width, int height) {
		
	}


}
