package nl.numworx.fsm;

import java.awt.Component;
import java.awt.Graphics;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookWidgetEditIF;
import org.cbook.cbookif.CBookWidgetIF;
import org.cbook.cbookif.CBookWidgetInstanceIF;

import fi.beans.mainframe.JApplet;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;
import fi.euclides.swing.DoubleFormat;
import nl.numworx.fsm.editor.Editor;
import nl.numworx.fsm.editor.FSMInteractiePanel;
import nl.numworx.fsm.editor.Instance;

public class FSM extends JApplet implements WiskOpdrApplet, CBookWidgetIF {

	private static final long serialVersionUID = 1L;
	private ResourceBundle rb;

	public FSM() {
		this(JComponent.getDefaultLocale());
	}
	
	public FSM(Locale locale) {
		DoubleFormat.setLocale(locale);
		rb = ResourceBundle.getBundle("nl.numworx.fsm.shared.resources.Text", locale);
	}

	@Override
	public InteractiePanel getInteractiePanel() {
		return new FSMInteractiePanel(); // TODO
	}

	public static void main(String[] args) {
	}

	@Override
	public CBookWidgetInstanceIF getInstance(CBookContext context) {
		return new Instance(context);
	}

	@Override
	public CBookWidgetEditIF getEditor(CBookContext context) {
		return new Editor(context, rb);
	}

	@Override
	public Icon getIcon() {
		
		return new Icon() {

			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				
			}

			@Override
			public int getIconWidth() {
				return 24;
			}

			@Override
			public int getIconHeight() {
				return 24;
			} };
	}

	@Override
	public String toString() {
		return "Finite State Machine";
	}

}
