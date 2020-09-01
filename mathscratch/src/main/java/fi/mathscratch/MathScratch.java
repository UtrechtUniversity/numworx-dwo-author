package fi.mathscratch;

import java.awt.Color;
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
	
	public static Color colorBlue1 = new Color(49,71,112);
	public static Color colorBlue2 = new Color(38,115,182);
	public static Color colorBlue3 = new Color(120,150,202);
	public static Color colorBlue4 = new Color(180,195,228);
	public static Color colorBlue5 = new Color(211,229,244);
	public static Color colorBlue6 = new Color(229,240,249);
	
	public static Color colorGray1 = new Color(206,207,208);
	public static Color colorGray2 = new Color(221,223,225);
	public static Color colorGray3 = new Color(237,239,241);
	
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
