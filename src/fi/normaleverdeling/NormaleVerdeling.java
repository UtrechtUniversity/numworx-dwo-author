package fi.normaleverdeling;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;


import fi.beans.copyright.*;
import fi.beans.mainframe.JApplet;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;
// deze moet vanwege WiskOpdrApplet
import fi.beans.wiskopdrbeans.InteractiePanel;
// deze moet vanwege InteractiePanel
import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class NormaleVerdeling extends JApplet implements WiskOpdrApplet
{
	protected static ResourceBundle rb;	
	protected static String langArg;
	protected static Color bgColor;

	protected static int bigWidth = 790;
	protected static int bigHeight = 450;
	protected static int editBreedte = 290;	
	
	NormaalPanel normaalPanel;

// tijdelijk	
	NormaalEditPanel normaalEditPanel;


	public NormaleVerdeling(Locale language)
    {   langArg = language.getLanguage();
		rb = ResourceBundle.getBundle("fi.normaleverdeling.text.Text", language);
    }
    
   
	public NormaleVerdeling()
	{	langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.normaleverdeling.text.Text", language);
	}
	
	public void init() 
	{	
		getContentPane().setLayout(null);
		
		//instelling taal
		langArg = getParameter("language");
		if (langArg == null) 
			langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.normaleverdeling.text.Text", language);
		
		//instelling achtergrondkleur
		bgColor = new Color(Integer.parseInt("DDEEFF", 16));
		String kleurcode = getParameter("bgcolor");
		if (kleurcode != null)
			bgColor = new Color(Integer.parseInt(kleurcode.substring(1), 16));
		getContentPane().setBackground(bgColor);
	
		
// tijdelijk		
/*
		normaalEditPanel = new NormaalEditPanel(getSize().width, getSize().height);
		normaalEditPanel.setLocation(0, 0);
		getContentPane().add(normaalEditPanel);
		normaalPanel = normaalEditPanel.normaalPanel;
*/		
// wordt later vervangen door
// afmetingen in html aanpassen!

		normaalPanel = new NormaalPanel(getSize().width, getSize().height);
		normaalPanel.setLocation(0, 0);
		getContentPane().add(normaalPanel);
		
		
		//Fi-logo, copyright
		FIButton fiButton = new FIButton("info",
			new String[]
			{	"NormaleVerdeling",
				"versie-info: 20110208",
				"auteur: Peter Boon",
				"programmeur: Huub Nilwik",
				"Freudenthal Instituut",
				"www.fi.uu.nl",
				""
			});
		fiButton.setBounds(normaalPanel.getSize().width - 20, 0, 20, 30);
		// toevoegen aan normaalPanel		
		normaalPanel.add(fiButton);
		
		// lees nu hier de parameters uit de html
		Hashtable h = new Hashtable();

		// default waarden		
		double muValue = 0;		
		double sigmaValue = 1;
		double grensValue = muValue + 1;		
		double grensLinksValue = muValue - 1;		
		double grensRechtsValue = muValue + 1;		
		double kansValue = 6e-1d;		
		int kansKeuzeValue = NormaalPanel.KANSLINKS;		
		int berekenKeuzeValue = NormaalPanel.BEREKENKANS;		

		boolean kansLinksOptie = true;
		boolean kansRechtsOptie = true;
		boolean tweeGrenzenOptie = true;

		boolean berekenbaarZichtbaar = true;	
		boolean muBerekenbaarOptie = false;
		boolean sigmaBerekenbaarOptie = false;

		boolean muVastOptie = false;
		boolean sigmaVastOptie = false;

		boolean muSliderOptie = false;
		boolean sigmaSliderOptie = false;
		boolean grensSliderOptie = true;
		boolean kansSliderOptie = false;

		boolean muZichtbaarOptie = true;
		boolean sigmaZichtbaarOptie = true;
		boolean grensZichtbaarOptie = true;
		boolean kansZichtbaarOptie = true;
	
		boolean muZichtbaarFigOptie = true;
		boolean sigmaZichtbaarFigOptie = true;
		boolean grensZichtbaarFigOptie = true;
		boolean kansZichtbaarFigOptie = true;

		// mu
		String muString = getParameter("mu");
		if ((muString != null) && !muString.equals(""))
		{	
			muString = muString.replace(',', '.');
		
			double muVal = 0;
			boolean error = false;
			try
			{	muVal = Double.parseDouble(muString);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			if (!error)
			{	if (muVal > normaalPanel.muMax - normaalPanel.NZERO)
					muValue = normaalPanel.muMax;
				else if (muVal < normaalPanel.muMin + normaalPanel.NZERO)	
					muValue = normaalPanel.muMin;
				else
					muValue = muVal;	
			}
		}
		
		// sigma
		String sigmaString = getParameter("sigma");
		if ((sigmaString != null) && !sigmaString.equals(""))
		{	
			sigmaString = sigmaString.replace(',', '.');		

			double sigmaVal = 0;
			boolean error = false;
			try
			{	sigmaVal = Double.parseDouble(sigmaString);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			if (!error)
			{	if (sigmaVal > normaalPanel.sigmaMax - normaalPanel.NZERO)
					sigmaValue = normaalPanel.sigmaMax;
				else if (sigmaVal < normaalPanel.sigmaMin + normaalPanel.NZERO)	
					sigmaValue = normaalPanel.sigmaMin;
				else
					sigmaValue = sigmaVal;	
			}
		}

		// grens
		String grensString = getParameter("grens");
		if ((grensString != null) && !grensString.equals(""))
		{	
			grensString = grensString.replace(',', '.');		
		
			double grensVal = 0;
			boolean error = false;
			try
			{	grensVal = Double.parseDouble(grensString);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			if (!error)
			{	
				if (grensVal > muValue + normaalPanel.maxX - normaalPanel.NZERO)
				{	grensValue = muValue + normaalPanel.maxX;
				}
				else if (grensVal < muValue + normaalPanel.minX + normaalPanel.NZERO)	
				{	grensValue = muValue + normaalPanel.minX;
				}
				else
					grensValue = grensVal;	
			}
		}

		// grensLinks	
		String grensLinksString = getParameter("grenslinks");
		if ((grensLinksString != null) && !grensLinksString.equals(""))
		{	
			grensLinksString = grensLinksString.replace(',', '.');		
		
			double grensLinksVal = 0;
			boolean error = false;
			try
			{	grensLinksVal = Double.parseDouble(grensLinksString);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			if (!error)
			{	if (grensLinksVal > muValue + 1 - normaalPanel.NZERO)
					grensLinksValue = muValue + 1;
				else if (grensLinksVal < muValue + normaalPanel.minX + normaalPanel.NZERO)	
					grensLinksValue = muValue + normaalPanel.minX;
				else
					grensLinksValue = grensLinksVal;	
			}
		}

		// grensRechts
		String grensRechtsString = getParameter("grensrechts");
		if ((grensRechtsString != null) && !grensRechtsString.equals(""))
		{	
			grensRechtsString = grensRechtsString.replace(',', '.');		
		
			double grensRechtsVal = 0;
			boolean error = false;
			try
			{	grensRechtsVal = Double.parseDouble(grensRechtsString);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			if (!error)
			{	if (grensRechtsVal > muValue + normaalPanel.maxX - normaalPanel.NZERO)
					grensRechtsValue = muValue + normaalPanel.maxX;
				else if (grensRechtsVal < grensLinksValue + 5e-1d + normaalPanel.NZERO)	
					grensRechtsValue = grensLinksValue + 5e-1d;
				else
					grensRechtsValue = grensRechtsVal;	
			}
		}

		// kans
		String kansString = getParameter("kans");
		if ((kansString != null) && !kansString.equals(""))
		{	
			kansString = kansString.replace(',', '.');		
		
			double kansVal = 0;
			boolean error = false;
			try
			{	kansVal = Double.parseDouble(kansString);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			if (!error)
			{	if (kansVal > 1 - normaalPanel.NZERO)
					kansValue = 1;
				else if (kansVal < normaalPanel.NZERO)	
					kansValue = 0;
				else
					kansValue = kansVal;	
			}
		}

		// kansKeuze
		String kansKeuzeString = getParameter("kanskeuze");
		if ((kansKeuzeString != null) && !kansKeuzeString.equals(""))
		{	int kansKeuzeVal = 0;
			boolean error = false;
			try
			{	kansKeuzeVal = Integer.parseInt(kansKeuzeString);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			if (!error)
			{	if (kansKeuzeVal > normaalPanel.TWEEGRENZEN)
					kansKeuzeValue = normaalPanel.TWEEGRENZEN;
				else if (kansKeuzeVal < normaalPanel.KANSLINKS)	
					kansKeuzeValue = normaalPanel.KANSLINKS;
				else
					kansKeuzeValue = kansKeuzeVal;	
			}
		}

		// berekenKeuze
		String berekenKeuzeString = getParameter("berekenkeuze");
		if ((berekenKeuzeString != null) && !berekenKeuzeString.equals(""))
		{	int berekenKeuzeVal = 0;
			boolean error = false;
			try
			{	berekenKeuzeVal = Integer.parseInt(berekenKeuzeString);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			if (!error)
			{	if (berekenKeuzeVal > normaalPanel.BEREKENGRENSRECHTS)
					berekenKeuzeValue = normaalPanel.BEREKENGRENSRECHTS;
				else if (berekenKeuzeVal < normaalPanel.BEREKENMU)	
					berekenKeuzeValue = normaalPanel.BEREKENMU;
				else
					berekenKeuzeValue = berekenKeuzeVal;	
			}
			
			if ((berekenKeuzeValue >= NormaalPanel.BEREKENGRENSLINKS) &&
				(kansKeuzeValue <= NormaalPanel.KANSRECHTS))
				berekenKeuzeValue = NormaalPanel.BEREKENGRENS;
		}

		// kansLinksOptie
		String kansLinksOptieString = getParameter("kanslinksoptie");
		if ((kansLinksOptieString != null) && kansLinksOptieString.equals("no"))
		{	kansLinksOptie = false;
		}
		// kansRechtsOptie
		String kansRechtsOptieString = getParameter("kansrechtsoptie");
		if ((kansRechtsOptieString != null) && kansRechtsOptieString.equals("no"))
		{	kansRechtsOptie = false;
		}
		// tweeGrenzenOptie
		String tweeGrenzenOptieString = getParameter("tweegrenzenoptie");
		if ((tweeGrenzenOptieString != null) && tweeGrenzenOptieString.equals("no"))
		{	tweeGrenzenOptie = false;
		}
	
		// correctie
		if (!kansLinksOptie && !kansRechtsOptie && !tweeGrenzenOptie)
		{	kansLinksOptie = true;
		}

		// berekenbaarZichtbaar
		String berekenbaarZichtbaarString = getParameter("berekenbaarzichtbaar");
		if ((berekenbaarZichtbaarString != null) && 
		     berekenbaarZichtbaarString.equals("no"))
		{	berekenbaarZichtbaar = false;
		}
		
		// muBerekenbaarOptie
		String muBerekenbaarOptieString = getParameter("muberekenbaaroptie");
		if ((muBerekenbaarOptieString != null) && muBerekenbaarOptieString.equals("yes"))
		{	muBerekenbaarOptie = true;
		}

		if (!muBerekenbaarOptie && (berekenKeuzeValue == NormaalPanel.BEREKENMU))
		{	berekenKeuzeValue = NormaalPanel.BEREKENKANS;
		}
				
		// sigmaBerekenbaarOptie
		String sigmaBerekenbaarOptieString = getParameter("sigmaberekenbaaroptie");
		if ((sigmaBerekenbaarOptieString != null) && 
			 sigmaBerekenbaarOptieString.equals("yes"))
		{	sigmaBerekenbaarOptie = true;
		}
		
		if (!sigmaBerekenbaarOptie && (berekenKeuzeValue == NormaalPanel.BEREKENSIGMA))
		{	berekenKeuzeValue = NormaalPanel.BEREKENKANS;
		}
		
		// muVastOptie
		String muVastOptieString = getParameter("muvastoptie");
		if ((muVastOptieString != null) && muVastOptieString.equals("yes"))
		{	muVastOptie = true;
		}

		// sigmaVastOptie
		String sigmaVastOptieString = getParameter("sigmavastoptie");
		if ((sigmaVastOptieString != null) && sigmaVastOptieString.equals("yes"))
		{	sigmaVastOptie = true;
		}

		// slider opties
		// muSliderOptie
		String muSliderOptieString = getParameter("muslideroptie");
		if ((muSliderOptieString != null) && muSliderOptieString.equals("yes"))
		{	muSliderOptie = true;
		}
		// sigmaSliderOptie
		String sigmaSliderOptieString = getParameter("sigmaslideroptie");
		if ((sigmaSliderOptieString != null) && sigmaSliderOptieString.equals("yes"))
		{	sigmaSliderOptie = true;
		}
		// grensSliderOptie
		String grensSliderOptieString = getParameter("grensslideroptie");
		if ((grensSliderOptieString != null) && grensSliderOptieString.equals("no"))
		{	grensSliderOptie = false;
		}
		
		// kansSliderOptie
		String kansSliderOptieString = getParameter("kansslideroptie");
		if ((kansSliderOptieString != null) && kansSliderOptieString.equals("yes"))
		{	kansSliderOptie = true;
		}
		
		// muZichtbaarOptie
		String muZichtbaarOptieString = getParameter("muzichtbaaroptie");
		if ((muZichtbaarOptieString != null) && muZichtbaarOptieString.equals("no"))
		{	muZichtbaarOptie = false;
		}
		// sigmaZichtbaarOptie
		String sigmaZichtbaarOptieString = getParameter("sigmazichtbaaroptie");
		if ((sigmaZichtbaarOptieString != null) && sigmaZichtbaarOptieString.equals("no"))
		{	sigmaZichtbaarOptie = false;
		}
		// grensZichtbaarOptie
		String grensZichtbaarOptieString = getParameter("grenszichtbaaroptie");
		if ((grensZichtbaarOptieString != null) && grensZichtbaarOptieString.equals("no"))
		{	grensZichtbaarOptie = false;
		}
		// kansZichtbaarOptie
		String kansZichtbaarOptieString = getParameter("kanszichtbaaroptie");
		if ((kansZichtbaarOptieString != null) && kansZichtbaarOptieString.equals("no"))
		{	kansZichtbaarOptie = false;
		}
		
		// muZichtbaarFigOptie
		String muZichtbaarFigOptieString = getParameter("muzichtbaarfigoptie");
		if ((muZichtbaarFigOptieString != null) && muZichtbaarFigOptieString.equals("no"))
		{	muZichtbaarFigOptie = false;
		}
		// sigmaZichtbaarFigOptie
		String sigmaZichtbaarFigOptieString = getParameter("sigmazichtbaarfigoptie");
		if ((sigmaZichtbaarFigOptieString != null) && sigmaZichtbaarFigOptieString.equals("no"))
		{	sigmaZichtbaarFigOptie = false;
		}
		// grensZichtbaarFigOptie
		String grensZichtbaarFigOptieString = getParameter("grenszichtbaarfigoptie");
		if ((grensZichtbaarFigOptieString != null) && grensZichtbaarFigOptieString.equals("no"))
		{	grensZichtbaarFigOptie = false;
		}
		// kansZichtbaarFigOptie
		String kansZichtbaarFigOptieString = getParameter("kanszichtbaarfigoptie");
		if ((kansZichtbaarFigOptieString != null) && kansZichtbaarFigOptieString.equals("no"))
		{	kansZichtbaarFigOptie = false;
		}
		
	
		

		h.put("mu", new Double(muValue));
		h.put("sigma", new Double(sigmaValue));
		h.put("grens", new Double(grensValue));
		h.put("grenslinks", new Double(grensLinksValue));
		h.put("grensrechts", new Double(grensRechtsValue));
		h.put("kans", new Double(kansValue));
		h.put("kanskeuze", new Integer(kansKeuzeValue));
		h.put("berekenkeuze", new Integer(berekenKeuzeValue));
		h.put("kanslinksoptie", new Boolean(kansLinksOptie));
		h.put("kansrechtsoptie", new Boolean(kansRechtsOptie));
		h.put("tweegrenzenoptie", new Boolean(tweeGrenzenOptie));		
		
		h.put("berekenbaarZichtbaar", new Boolean(berekenbaarZichtbaar));				
		h.put("muberekenbaaroptie", new Boolean(muBerekenbaarOptie));		
		h.put("sigmaberekenbaaroptie", new Boolean(sigmaBerekenbaarOptie));		
		h.put("muvastoptie", new Boolean(muVastOptie));				
		h.put("sigmavastoptie", new Boolean(sigmaVastOptie));				

	    h.put("muSliderOptie", new Boolean(muSliderOptie));
		h.put("sigmaSliderOptie", new Boolean(sigmaSliderOptie));	  
		h.put("grensSliderOptie", new Boolean(grensSliderOptie));	  		  	    
		h.put("kansSliderOptie", new Boolean(kansSliderOptie));	
		
		h.put("muZichtbaarOptie", new Boolean(muZichtbaarOptie));
		h.put("sigmaZichtbaarOptie", new Boolean(sigmaZichtbaarOptie));
		h.put("grensZichtbaarOptie", new Boolean(grensZichtbaarOptie));	
		h.put("kansZichtbaarOptie", new Boolean(kansZichtbaarOptie));	
		
		h.put("muZichtbaarFigOptie", new Boolean(muZichtbaarFigOptie));
		h.put("sigmaZichtbaarFigOptie", new Boolean(sigmaZichtbaarFigOptie));
		h.put("grensZichtbaarFigOptie", new Boolean(grensZichtbaarFigOptie));	
		h.put("kansZichtbaarFigOptie", new Boolean(kansZichtbaarFigOptie));	
		

// testing
//normaalEditPanel.setEditState(h);

// definitieve versie
		normaalPanel.setEditState(h);
		
	} // init

	// interface WiskOpdrApplet 
	public InteractiePanel getInteractiePanel()
	{	return new NormaalPanel(bigWidth - editBreedte, bigHeight);
	}

}
