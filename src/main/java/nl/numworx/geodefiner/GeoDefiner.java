package nl.numworx.geodefiner;

import java.net.URL;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JApplet;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookWidgetEditIF;
import org.cbook.cbookif.CBookWidgetIF;
import org.cbook.cbookif.CBookWidgetInstanceIF;

import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;
import fi.euclides.swing.DoubleFormat;

public class GeoDefiner extends JApplet implements  CBookWidgetIF, WiskOpdrApplet {

	private static final long serialVersionUID = -8167425499542355350L;

	static {
		DoubleFormat.setLocale(Locale.getDefault());
	}
		
	public static void main(String[] args) {
		
	}
	
	
	public GeoDefiner() {		
	}
	
	public GeoDefiner(Locale locale) {
		DoubleFormat.setLocale(locale);
	}
	
	public CBookWidgetEditIF getEditor(CBookContext context) {
		Editor editor = new Editor(context);
		return editor;
	}

	public Icon getIcon() {
		URL u = getClass().getResource("resources/geodefiner.png");
		if (u != null)
			return new ImageIcon(u);
		return null;
	}

	public CBookWidgetInstanceIF getInstance(CBookContext context) {
		Instance instance = new Instance();
		return instance;
	}

	public String toString() {
		return "GeoDefiner";
	}

	public InteractiePanel getInteractiePanel() {
		return new GeoDefinerInteractiePanel();
	}
}
