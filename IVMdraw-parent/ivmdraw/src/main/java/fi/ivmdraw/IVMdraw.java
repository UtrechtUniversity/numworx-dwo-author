package fi.ivmdraw;

import java.util.Locale;
import java.util.ResourceBundle;

import fi.beans.mainframe.JApplet;
import fi.beans.mainframe.MainFrame;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;
import fi.ivmdraw.common.Model;

@SuppressWarnings("serial")
public class IVMdraw extends JApplet implements WiskOpdrApplet {

	protected static ResourceBundle rb;
	protected static String langArg;
	Model model;

	public IVMdraw() {
		langArg = "nl";
		Locale language = new Locale(langArg, "");
		rb = ResourceBundle.getBundle("fi.ivmdraw.text.Text", language);
		model = new Model();
	}

	public IVMdraw(Locale locale) {
		this();
		setLocale(locale);
		langArg = locale.getLanguage();
		rb = ResourceBundle.getBundle("fi.ivmdraw.text.Text", locale);
	}

	public IVMdrawPanel getInteractiePanel() {
		return new IVMdrawPanel(this);
	}

	public static void main(String[] args) {
		IVMdraw applet = new IVMdraw(Locale.forLanguageTag("nl"));
		MainFrame frame = new MainFrame(applet, 400, 400);
		frame.setSize(400, 400);
		frame.setTitle("IVMdraw");
		frame.pack();
		frame.show();
	}

	IVMdrawPanel panel;

	public void init() {
		panel = getInteractiePanel();
		setContentPane(panel);
	}

	@Override
	public void start() {
		panel.start();
	}

	@Override
	public void stop() {
		panel.stop();
	}

	@Override
	public void destroy() {
		panel.destroy();
	}

}
