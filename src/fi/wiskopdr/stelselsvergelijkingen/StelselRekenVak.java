package fi.wiskopdr.stelselsvergelijkingen;

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.MissingResourceException;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;

import fi.beans.stringutils.StringUtils;
import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wnwidgets.NWButtonUI;
import fi.wiskopdr.AntwoordVak;
import fi.wiskopdr.ImageComponent;
import fi.wiskopdr.SimpelAntwoordVergelijkingVak;
import fi.wiskopdr.TekstVakPanel;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.expressies.Algebra;
import fi.wiskopdr.expressies.BasisExpressie;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.expressies.Functie;
import fi.wiskopdr.expressies.FunctieDefSet;
import fi.wiskopdr.expressies.Vergelijking;
import fi.wiskopdr.expressies.VergelijkingMeerv;
import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.formuleobjects.FormuleEditor;
import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.wiskopdr.formuleobjects.FormuleVak;
import fi.wiskopdr.formuleobjects.TabletOwner;
import fi.wiskopdr.opdrnav.MyOpdrContainer;
import fi.wiskopdr.opdrnav.OpdrNavStruct;
import fi.wiskopdr.tekstobjects.FeedbackTekstArea;
import fi.wiskopdr.tekstobjects.TekstArea;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;

public class StelselRekenVak extends JPanel  {
	
	StelselEditor hoofdEditor;
	String[] varNamen = {"n", "p"};
	Expressie[][] oplossingen = {{new BasisExpressie(0), new BasisExpressie(2)}};
	
	private String[] randomVars;
	private Hashtable randomValues;
	
//	JScrollPane scrollPane;
//	JPanel contentPanel;
	
	public StelselRekenVak()
	{
		setLayout(null);
		setBackground(Color.white);
//		contentPanel = new JPanel();
//		contentPanel.setLayout(null);
//		contentPanel.setBounds(0, 0, getWidth(), getHeight() - 20);
//		scrollPane = new JScrollPane(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//		super.add(scrollPane);
		
		hoofdEditor = new StelselEditor(this);
		hoofdEditor.setBounds(0, 0, getWidth(), getHeight());
		add(hoofdEditor);
	}
	
	public void setBounds(int x, int y, int b, int h)
	{
		super.setBounds(x, y, b, h);
//		contentPanel.setBounds(x, y, b, h);
		hoofdEditor.setBounds(0, 0, b, h);
	}
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{
		//String antwoordString = "";
		//String variabelenString = "";
		int puntenGelijkwaardig = 10;
		Hashtable[] answerModels = null;
		boolean hasFeedback = false;
		int scoreMax = 10;
		boolean check = true;
		boolean teltMee = true;
		boolean formuleToolBijFocus = false;
		boolean logOption = false;
		String logID = "";
		boolean boxMetRand = true;
		boolean[][] logObjectives = null;

//		if (h.containsKey("antwoordString"))
//			antwoordString = (String) h.get("antwoordString");
//		if (h.containsKey("variabelenString"))
//			variabelenString = (String) h.get("variabelenString");
		if (h.containsKey("scoreMax"))
			scoreMax = ((Integer) h.get("scoreMax")).intValue();
		if (h.containsKey("answerModels"))
			answerModels = (Hashtable[]) h.get("answerModels");
		if (h.containsKey("hasFeedback"))
			hasFeedback = ((Boolean) h.get("hasFeedback")).booleanValue();
		if (h.containsKey("check"))
			check = ((Boolean) h.get("check")).booleanValue();
		if (h.containsKey("teltMee"))
			teltMee = ((Boolean) h.get("teltMee")).booleanValue();
		if (h.containsKey("formuleToolBijFocus"))
			formuleToolBijFocus = ((Boolean) h.get("formuleToolBijFocus")).booleanValue();
		if (h.containsKey("logOption"))
			logOption = ((Boolean) h.get("logOption")).booleanValue();
		if (h.containsKey("logID"))
			logID = (String) h.get("logID");
		if (h.containsKey("boxMetRand"))
			boxMetRand = ((Boolean) h.get("boxMetRand")).booleanValue();
		if (h.containsKey("logObjectives"))
			logObjectives = (boolean[][]) h.get("logObjectives");

//		try
//		{
//			antwoordString = FormuleParser.randomizeTekstVakString(antwoordString, randomVars, randomValues);
//		}
//		catch (Exception e)
//		{
//		}
//		antwoordString = StringUtils.replaceStr(antwoordString, " ", "");
//		
//		try
//		{
//			variabelenString = FormuleParser.randomizeTekstVakString(variabelenString, randomVars, randomValues);
//		}
//		catch (Exception e)
//		{
//		}
//		variabelenString = StringUtils.replaceStr(variabelenString, " ", "");
//		
//		try{
//			//haakjes weghalen
//			variabelenString = variabelenString.substring(3, variabelenString.length() - 2);
//			varNamen = StringUtils.split(variabelenString, ",");
//		}
//		catch(Exception e)
//		{}
//		
//		try{
//			//splitsen in verschillende oplossingen. Eerst $f en @ weghalen.
//			antwoordString = antwoordString.substring(2, antwoordString.length() - 1);
//			antwoordString = StringUtils.replaceStr(antwoordString, "),(", "):(");
//			String[] oplossingenStrings = StringUtils.split(antwoordString, ":");
//			oplossingen = new Expressie[oplossingenStrings.length][varNamen.length];
//			for(int i = 0; i < oplossingenStrings.length; i++)
//			{
//				//haakjes verwijderen:
//				String opl = oplossingenStrings[i].substring(1, oplossingenStrings[i].length() - 1);
//				String[] varWaardes;
//				if(opl.contains(";"))
//					varWaardes = StringUtils.split(opl, ";");
//				else
//					varWaardes = StringUtils.split(opl, ",");
//				for(int j = 0; j < varNamen.length; j++)
//				{	oplossingen[i][j] = FormuleParser.geefExpressie("$f" + varWaardes[j] + "@");
//				}
//			}
//		}
//		catch(Exception e)
//		{}
//			
//		hoofdEditor.zetVarNamen(varNamen);
//		hoofdEditor.zetOplossingen(oplossingen);
//		
//		
		
		
		
		//this.antwoordString = antwoordString;
		//this.variabelenString = variabelenString;
		//this.answerModels = answerModels;
		//this.hasFeedback = hasFeedback;
		//this.check = check;
		//this.teltMee = teltMee;
		this.randomVars = randomVars;
		this.randomValues = randomValues;
		//this.formuleToolBijFocus = formuleToolBijFocus;
		//this.logOption = logOption;
		//this.logID = logID;
		//this.logObjectives = logObjectives;

//		if (formuleVak != null)
//			formuleVak.zetStippels(!boxMetRand);
//		
//		add(formuleVak);
//		zetJuisteAntwoord(antwoordString);
//		
//		if(fontOvererving && getParent() instanceof TekstInteractiePanelVak)
//		{	Font geerftFont = ((TekstInteractiePanelVak)getParent()).getTekstVak().getFont();
//			if (!geerftFont.getName().equals("TimesRoman") && WiskOpdr.formTimes && !WiskOpdr.mac) {
//				geerftFont = new Font("TimesRoman", geerftFont.getStyle(), geerftFont.getSize() * 6 / 5);
//			}
//			formuleVakFont = geerftFont;
//			formuleVak.setFont(formuleVakFont);
//		}
	}
	
	public void zetVarNamen(String[] varNamen)
	{
		this.varNamen = varNamen;
		hoofdEditor.zetVarNamen(varNamen);
	}
	
	public void zetJuisteOplossingen(Expressie[][] oplossingen)
	{
		this.oplossingen = oplossingen;
		hoofdEditor.zetOplossingen(oplossingen);
		
	}
	
	public void plaatsEditors()
	{
		System.out.println("plaatsEditors");
		//uitrekenen hoeveel kolommen er onderaan zijn. 
		//Die allemaal evenveel ruimte geven
		//De breedtes van de kolommen erboven zijn dan de sommen van de breedtes van hun kinderen.
		
		int aantalKolommen = hoofdEditor.geefEindAantalKinderen();
		
		int kolomBreedte = getWidth()/aantalKolommen;
		hoofdEditor.setSizes(kolomBreedte);
		hoofdEditor.setLocation(0, 0);
		hoofdEditor.scrollRectToVisible(new Rectangle(0, 0, 1, 1));
		if(hoofdEditor.heeftKinderen())
			hoofdEditor.setLocations();
		repaint();
		
	}
	
	public StelselEditor geefHoofdEditor()
	{
		return hoofdEditor;
	}
	
//	public Component add(Component c)
//	{
//		contentPanel.add(c);
//		return c;
//	}
}
