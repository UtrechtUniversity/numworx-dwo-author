package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.util.Collections;
import java.util.Hashtable;

import javax.swing.JPanel;

import org.cbook.cbookif.CBookContext;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class GeoDefinerInteractieEditPanel extends JPanel implements
		InteractieEditPanel, CBookContext {

	private static final long serialVersionUID = 4981417988189908524L;

	Editor editor;
	
	GeoDefinerInteractieEditPanel() {
		super(null);
		editor = new Editor(this);
		editor.setLocation(0, 0);
		editor.setInstanceWidth(500);
		editor.setInstanceHeight(450);
		editor.setLaunchData(Collections.EMPTY_MAP);
		add(editor);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		editor.setSize(width, height);
		editor.invalidate();
		editor.doLayout();
	}

	public void setEditState(Hashtable b) {
		editor.setLaunchData(b);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Hashtable getEditState() {
		return new Hashtable( editor.getLaunchData() );
	}

	public void zetBreedte(int b) {
		editor.setInstanceWidth(b);
	}

	public void zetHoogte(int h) {
		editor.setInstanceHeight(h);
	}

	public void stop() {
		editor.stop();
	}

	public void start() {
		editor.start();
	}

	public Object getProperty(String arg0) {
		if("randomVars".equals(arg0))
			return Collections.EMPTY_MAP; // FIXME
		return null;
	}

}
