package nl.numworx.geodefiner;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookWidgetEditIF;
import org.cbook.cbookif.CBookWidgetIF;
import org.cbook.cbookif.CBookWidgetInstanceIF;

public class GeoDefiner implements  CBookWidgetIF {

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
}
