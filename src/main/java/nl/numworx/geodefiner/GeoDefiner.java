package nl.numworx.geodefiner;

import java.net.URL;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JComponent;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookWidgetEditIF;
import org.cbook.cbookif.CBookWidgetIF;
import org.cbook.cbookif.CBookWidgetInstanceIF;

import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;
import fi.euclides.math.IntegerFactory;
import fi.euclides.model.math.Numbers;
//import fi.euclides.openmath.Popcorn;
import fi.euclides.swing.DoubleFormat;
//import fi.euclides.swing.SwingSymbols;
import fi.wiskopdr.WiskOpdr;

public class GeoDefiner extends JApplet implements CBookWidgetIF, WiskOpdrApplet {

	private static final long serialVersionUID = -8167425499542355350L;

	public static final boolean isExperimental;
	
	static {
//		Popcorn.map = new SwingSymbols();
		isExperimental = WiskOpdr.isExperimental();
		Numbers.setFactory(IntegerFactory.INSTANCE);
	}
		
	public static void main(String[] args) {
		
	}
	
	public GeoDefiner() {
		this(JComponent.getDefaultLocale());
	}
	
	public GeoDefiner(Locale locale) {
		DoubleFormat.setLocale(locale);
		Messages.setLocale(locale);
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
