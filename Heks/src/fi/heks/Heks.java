package fi.heks;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import fi.heks.scobjects.*;
import fi.beans.scorm.*;
import fi.beans.appletutil.AppletUtil;
import fi.beans.base64code.*;
import fi.beans.copyright.*;
import fi.beans.mainframe.JApplet;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;

public class Heks extends JApplet implements WiskOpdrApplet, ScormAppletIF, ComponentListener 
{
	
	// taal
	protected static String langArg;

	public double schaal;
	private ScPanel tp;
	static int bladNummer;

	public static ResourceBundle rb;
	public static Locale language;

	private SCORM12APIInterface api;
	private long sessionStartTime;

	private Hashtable defaultParamValues;

	public static void main(String[] args) 
	{
		int width = 800;
		int height = 475;
		ScormMainFrame mf = new ScormMainFrame(new Heks(), width, height);
		mf.setSize(width, height);
		mf.show();
	}

	public Heks()
	{	
		langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.heks.text.Text", language);	
	}
	
	public Heks(Locale language)
	{	
		
//System.out.println("Heks(Locale)");		

		langArg = language.getLanguage();
		rb = ResourceBundle.getBundle("fi.heks.text.Text", language);	
	}

	public void init() 
	{
		String variantString = super.getParameter("variant");
		int variant = 1;
		if (variantString != null)
			variant = Integer.parseInt(variantString);

		defaultParamValues = makeDefaultParamValues(variant);

		try 
		{
			api = Scorm.findAPI(this);
		} 
		catch (Exception e) 
		{}

		setLayout(null);
		// addComponentListener(this);

		// Color bgcolor = new Color(230,240,255);
		Color bgcolor = Color.white;
		String kleurcode = getParameter("bgcolor");
		if (kleurcode != null)
			bgcolor = new Color(Integer.parseInt(kleurcode.substring(1), 16));
		setBackground(bgcolor);
		// setBackground(new Color(255,255,220));

		String langArg = getParameter("language");
		if (langArg == null)
			langArg = "nl";
		language = new Locale(langArg, "");
		rb = ResourceBundle.getBundle("fi.heks.text.Text", language);

		String bladNummerString = getParameter("blad");
		try 
		{
			bladNummer = Integer.parseInt(bladNummerString);
		} 
		catch (NumberFormatException e) 
		{
			bladNummer = 1;
		}
		//bladNummer = 21;

		if (bladNummer == 1)
			tp = new TafereelPanel2(5, 5, 790, 565, this);
		else if (bladNummer == 2)
			tp = new TafereelPanel(5, 5, 790, 565, this);
		else if (bladNummer == 3)
			tp = new BlokjesErbijPanel(5, 5, 790, 565, this);
		else if (bladNummer == 4)
			tp = new OefentoetsPanel(5, 5, 790, 565, this, Som.PLUS);
		else if (bladNummer == 5)
			tp = new TweeManierenPanel(5, 5, 790, 565, this);
		else if (bladNummer == 6)
			tp = new BlokjesEruitPanel(5, 5, 790, 565, this);
		else if (bladNummer == 7)
			tp = new OefentoetsPanel(5, 5, 790, 565, this, Som.MIN);
		else if (bladNummer == 8)
			tp = new OefentoetsPanel(5, 5, 790, 565, this, Som.PLUSMIN);
		else if (bladNummer == 9)
			tp = new OefentoetsPanelExtra(5, 5, 790, 565, this, Som.PLUSMIN, false);
		else if (bladNummer == 10)
			tp = new TafereelPanelEmmer(5, 5, 790, 565, this);
		else if (bladNummer == 11)
			tp = new OefentoetsPanelExtra(5, 5, 790, 565, this, Som.MAAL, true);
		else if (bladNummer == 12)
			tp = new OefentoetsPanelExtra(5, 5, 790, 565, this, Som.MAAL, false);
		else if (bladNummer == 13)
			tp = new BlokjesErbijPanelExtra(5, 5, 790, 565, this);
		else if (bladNummer == 14)
			tp = new BlokjesEruitPanelExtra(5, 5, 790, 565, this);
		else if (bladNummer == 15)
			tp = new BlokjesMaalPanel(5, 5, 790, 565, this);
		else if (bladNummer == 16)
			tp = new BlokjesMaalPanelExtra(5, 5, 790, 565, this);
		else if (bladNummer == 21)
			tp = new TafereelPanel2_WN(5, 5, 790, 665, this);
		else if (bladNummer == 22)
			tp = new TafereelPanel_WN(5, 5, 790, 665, this);
		else if (bladNummer == 23)
			tp = new TafereelPanelEmmer_WN(5, 5, 790, 665, this);
		else if (bladNummer == 24)
			tp = new TafereelPanelHulpKetel_WN(5, 5, 3 * 266 / 2, 3 * 151 / 2, this);
		else if (bladNummer == 25)
			tp = new OefentoetsPanel_WN(5, 5, 990, 665, this, Som.PLUS);
		else if (bladNummer == 26)
			tp = new BlokjesErbijPanel_WN(5, 5, 560, 400, this);
		else if (bladNummer == 27)
			tp = new OefentoetsPanel_WN(5, 5, 990, 665, this, Som.PLUSMIN);
		else if (bladNummer == 28)
			tp = new OefentoetsPanelExtra_WN(5, 5, 990, 665, this, Som.PLUSMIN, false);
		else if (bladNummer == 29)
			tp = new OefentoetsPanelExtra_WN(5, 5, 990, 665, this, Som.MAAL, true);

		double sx = ((1.0 * getSize().width) / tp.getSize().width);
		double sy = ((1.0 * getSize().height) / tp.getSize().height);
		double schaal = Math.min(sx, sy) * 0.95;
		// let op schaalt nu hard op 0,8
		tp.schaal(0.80);
		int x = (int) ((sx - schaal) * tp.getSize().width / 2);
		int y = (int) ((sy - schaal) * tp.getSize().height / 2);
		// tp.setLocation(x,y+10);
		tp.setLocation(20, 20);
		add(tp);

		AppletUtil au = new AppletUtil(this);

		Image mwlogo = au.getImage("resources/MW_logo.gif");
		MediaTracker tr = new MediaTracker(this);
		tr.addImage(mwlogo, 0);
		try 
		{
			tr.waitForAll();
		} 
		catch (Exception e) 
		{
		}

		ImageComponent MWLogo = new ImageComponent(mwlogo);
		MWLogo.setBackground(getBackground());
		MWLogo.setLocation(getSize().width - 35, 0);
		if (bladNummer < 20)
			add(MWLogo, 0);

		FIButton fiButton = new FIButton("De Heks", 
				                         new String[] { "", 
				                                        "versie-info: 20060131", 
				                                        "auteur: Peter Boon ea.", 
				                                        "programmeur: Peter Boon",
				                                        "copyright: Wolters Noordhof", 
				                                        ""});
		fiButton.setBounds(2, 2, 20, 30);
		// tp.add(fiButton);

	}

	public Hashtable getDefaultParamValues(int variant) 
	{
		return makeDefaultParamValues(variant);
	}

	public String getParameter(String name) 
	{
		String value = super.getParameter(name);
		if (value == null)
			value = (String) defaultParamValues.get(name);
		return value;
	}

	private Hashtable makeDefaultParamValues(int variant) 
	{
		Hashtable h = new Hashtable();
		h.put("language", "nl");
		h.put("bgcolor", "#FFFFFF");
		h.put("color_01", "#FFFFFF");
		h.put("blad", "" + (variant + 1));

		return h;
	}

	
	public InteractiePanel getInteractiePanel()
	{	
		return new HeksInteractiePanel();
	}

	public void start() 
	{
		tp.start();
		sessionStartTime = System.currentTimeMillis();
		if (api != null) 
		{
			String s = api.LMSGetValue("cmi.suspend_data");
			if (s != null && !s.equals(""))
				setState(s);
			//
			// api.LMSSetValue("cmi.launch_data",StringCodeObject.encodeObjectToString(defaultParamValues));
			//

		}

	}

	public void stopSco() 
	{
		if (api != null) 
		{
			stop();
			api = null;
		}

	}

	public void stop() 
	{
		tp.stop();
		if (api != null) {
			String s = getState();
			String d = new Double(getScore()).toString();
			String t = getSessionTime();
			api.LMSSetValue("cmi.core.session_time", t);
			api.LMSSetValue("cmi.core.score.raw", d);
			api.LMSSetValue("cmi.suspend_data", s);
			// api.LMSSetValue("USER_GROUP","UG_TEACHER");
		}
	}

	public double getScore() 
	{
		return tp.getScore();
	}

	public String getSessionTime() 
	{
		long sessionTime = System.currentTimeMillis() - sessionStartTime;
		String s = "";
		int hours = (int) sessionTime / 3600000;
		int minutes = (int) sessionTime / 60000 - hours * 60;
		int seconds = (int) sessionTime / 1000 - hours * 3600 - minutes * 60;
		if (hours < 10)
			s += "0";
		s += hours;
		s += ":";
		if (minutes < 10)
			s += "0";
		s += minutes;
		s += ":";
		if (seconds < 10)
			s += "0";
		s += seconds;
		return s;
	}

	public void setState(String s) 
	{
		Object o = StringCodeObject.decodeStringToObject(s);
		if (o == null)
			return;
		Hashtable h = (Hashtable) o;

		try 
		{

			Hashtable tpState = (Hashtable) h.get("tpState");
			tp.setState(tpState);
		} 
		catch (Exception ex) 
		{
			System.out.println("setStateFout");
		}

	}

	public String getState() 
	{
		Hashtable tpState = null;

		tpState = tp.getState();

		Hashtable h = new Hashtable();
		if (tpState != null)
			h.put("tpState", tpState);

		String s = StringCodeObject.encodeObjectToString(h);
		return s;

	}

	public boolean hasEditMode() 
	{
		return false;
	}

	public ScormEditComponentIF getEditComponent(Hashtable launchData) {
		return null;
	}

	public Parameter[] getEditableParameters() 
	{
		return null;
	}

	public Parameter[] getAllParameters() 
	{
		Parameter[] parameters = new Parameter[4];
		DataType type = null;
		Parameter param = null;

		type = new ScormString();
		param = new Parameter("language", "Taal", type);
		parameters[0] = param;

		type = new ScormInteger();
		type.setSize(8);
		param = new Parameter("bgcolor", "Achtergrondkleur", type);
		parameters[1] = param;

		type = new ScormInteger();
		type.setSize(8);
		param = new Parameter("color_01", "Achtergrondkleur Ketel", type);
		parameters[2] = param;

		type = new ScormInteger();
		type.setSize(4);
		param = new Parameter("blad", "Bladnummer", type);
		parameters[3] = param;

		return parameters;

	}

	public void componentResized(ComponentEvent e) 
	{
		double sx = (1.0 * getSize().width - 10) / tp.getSize().width;
		double sy = (1.0 * getSize().height - 10) / tp.getSize().height;
		schaal = Math.min(sx, sy);
		tp.schaal(schaal);

		int x = (int) ((sx - schaal) * tp.getSize().width / 2);
		int y = (int) ((sy - schaal) * tp.getSize().height / 2);
		tp.setLocation(x + 5, y + 5);
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

}
