package fi.mathscratch;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JApplet;

import fi.beans.mainframe.MainFrame;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;
import fi.mathscratch.common.Model;

public class MathScratch extends JApplet implements WiskOpdrApplet {

	protected static ResourceBundle rb;
	protected static String langArg;
	private Model model;
	private MathScratchPanel interactiePanel;
	
	public MathScratch() {
		langArg = "nl";
		Locale language = new Locale(langArg, "");
		rb = ResourceBundle.getBundle("fi.mathscratch.text.Text", language);
		model = new Model();
	}
	
	public MathScratch(Locale locale) {
		this();
		setLocale(locale);
		langArg = locale.getLanguage();
		rb = ResourceBundle.getBundle("fi.mathscratch.text.Text", locale);
		
	}
	
	public InteractiePanel getInteractiePanel() {
		return new MathScratchPanel(this);
	}
	
	public static void main(String[] args) {
		MathScratch applet = new MathScratch(Locale.forLanguageTag("nl"));
		MainFrame frame = new MainFrame(applet, 400, 400);
		frame.setSize(400, 400);
		frame.setTitle("MathScratch");
		frame.pack();
		frame.show();
	}

}
