package fi.mozarch;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;

import fi.beans.base64code.StringCodeObject;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;
// deze moet vanwege interface WiskOpdrApplet
import fi.beans.wiskopdrbeans.InteractiePanel;
// deze moet vanwege interface InteractiePanel
import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class MZInteractiePanel extends JPanel implements InteractiePanel, InteractieEditPanel,
							         ActionListener
									
{
	
	
	int score = 0;
	int scoreMax = 10;

	TekenPanel tekenPanel;
	
	boolean fractielen = false;
	boolean startFiguur = false;
	int fractielType = 1;
	int aantalHoekpunten = 3;
	int aantalPerZijde = 1;
	
	boolean noSetBounds = false;
	
	// parametrisatie
	boolean triangles = true;
	boolean squares = true;
	boolean pentagons = false;
	boolean hexagons = true;
	boolean octagons = true;
	boolean dekagons = false;
	boolean dodekagons = true;
	
	public MZInteractiePanel()
	{
		setLayout(null);
		// echte initiatie vind pas plaats na setBounds
		
		
	}

	
	public void zetOpdracht(Hashtable b, String[] randomVars, Hashtable randomValues)
	{
System.out.println("mzip zetOpdracht");		
		
		boolean fractielen = false; 
		boolean startFiguur = false;
		int aantalHoekpunten = 3;
		int aantalPerZijde = 1;
		int fractielType = 1;

		boolean triangles = true;
		boolean squares = true;
		boolean pentagons = false;
		boolean hexagons = true;
		boolean octagons = true;
		boolean dekagons = false;
		boolean dodekagons = true;

		if (b.containsKey("appletLaunchData"))
		{
System.out.println("aLD found");
			Hashtable appletLaunchData = (Hashtable) b.get("appletLaunchData");
			
			String fractielenString = "false";
			String startFiguurString = "";
			String aantalHoekpuntenString = "3";
			String aantalPerZijdeString = "1";
			
			if (appletLaunchData.containsKey("fractielen"))
				fractielenString = (String) appletLaunchData.get("fractielen");
			if (fractielenString.equals("true") || fractielenString.equals("yes"))
				fractielen = true;
			if (appletLaunchData.containsKey("startFiguur"))
				startFiguurString = (String) appletLaunchData.get("startFiguur");
			if (startFiguurString.length() > 0)
				startFiguur = true;
			if (fractielen)
			{	int grens = startFiguurString.indexOf(',');
				if (grens > 0)
					startFiguurString = startFiguurString.substring(0, grens);
				boolean error = false;
				try
				{	fractielType = Integer.parseInt(startFiguurString);
				}
				catch (NumberFormatException nfe)
				{	error = true;
				}
				if (!error && (fractielType >= 1) && (fractielType <= 3))
				{	// geen actie					
				}
				else
					fractielType = 1;
			}
			else
			{	int grens = startFiguurString.indexOf(',');
				if (grens > 0)
				{	aantalHoekpuntenString = startFiguurString.substring(0, grens);
					aantalPerZijdeString = startFiguurString.substring(grens + 1);
				}
				else
					aantalHoekpuntenString = startFiguurString;
				boolean error = false;
				try
				{	aantalHoekpunten = Integer.parseInt(aantalHoekpuntenString);
				}
				catch (NumberFormatException nfe)
				{	error = true;
				}
				if (!error && ((aantalHoekpunten == 3) || (aantalHoekpunten == 4) || (aantalHoekpunten == 6) ||
						       (aantalHoekpunten == 8) || (aantalHoekpunten == 12)))
				{	// geen actie					
				}
				else
					aantalHoekpunten = 3;
				error = false;
				try
				{	aantalPerZijde = Integer.parseInt(aantalPerZijdeString);
				}
				catch (NumberFormatException nfe)
				{	error = true;
				}
				if (!error && (aantalPerZijde >= 1) && (aantalPerZijde <= 4))
				{	// geen actie					
				}
				else
					aantalPerZijde = 1;
			}

		}
		else
		{
			if (b.containsKey("fractielen"))
				fractielen = ((Boolean) b.get("fractielen")).booleanValue();
			if (b.containsKey("startFiguur"))
				startFiguur = ((Boolean) b.get("startFiguur")).booleanValue();
			if (b.containsKey("fractielType"))
				fractielType = ((Integer) b.get("fractielType")).intValue();
			if (b.containsKey("aantalHoekpunten"))
				aantalHoekpunten = ((Integer) b.get("aantalHoekpunten")).intValue();
			if (b.containsKey("aantalPerZijde"))
				aantalPerZijde = ((Integer) b.get("aantalPerZijde")).intValue();

			if (b.containsKey("triangles"))
				triangles = ((Boolean) b.get("triangles")).booleanValue();
			if (b.containsKey("squares"))
				squares = ((Boolean) b.get("squares")).booleanValue();
			if (b.containsKey("pentagons"))
				pentagons = ((Boolean) b.get("pentagons")).booleanValue();
			if (b.containsKey("hexagons"))
				hexagons = ((Boolean) b.get("hexagons")).booleanValue();
			if (b.containsKey("octagons"))
				octagons = ((Boolean) b.get("octagons")).booleanValue();
			if (b.containsKey("dekagons"))
				dekagons = ((Boolean) b.get("dekagons")).booleanValue();
			if (b.containsKey("dodekagons"))
				dodekagons = ((Boolean) b.get("dodekagons")).booleanValue();

		}
		
		zetFractielen(fractielen);
		zetStartFiguur(startFiguur);
		zetFractielType(fractielType);
		zetAantalHoekpunten(aantalHoekpunten);
		zetAantalPerZijde(aantalPerZijde);

		setTriangles(triangles);
		setSquares(squares);
		setPentagons(pentagons);
		setHexagons(hexagons);
		setOctagons(octagons);
		setDekagons(dekagons);
		setDodekagons(dodekagons);
		
		//HIER state
		if (b.containsKey("appletEditState"))
		{
System.out.println("aES found");
			String appletEditState = (String) b.get("appletEditState");
			
			// decodeer de string
			Object o = StringCodeObject.decodeStringToObject(appletEditState);

			Hashtable h = (Hashtable) o;
			
					
			int tempAantalVlakdelen = 0;
			Vector vlakdelenVector = new Vector();
			if (h.containsKey("vlakdelenVector"))
			{	vlakdelenVector = (Vector) h.get("vlakdelenVector");
				tempAantalVlakdelen = vlakdelenVector.size();
			//System.out.println("set: h contains vv");			
			}
					
//System.out.println("set: tav = " + tempAantalVlakdelen);		
					
			if (tempAantalVlakdelen == 0)
				return;

			int[] tempVolgorde = new int[tempAantalVlakdelen];
			Vlakdeel2[] tempVlakdelen = new Vlakdeel2[tempAantalVlakdelen];
			for (int i = 0; i < tempAantalVlakdelen; i++)
			{	
				Hashtable hv = (Hashtable) vlakdelenVector.elementAt(i);
					
				int fractielT = 3;
				if (hv.containsKey("fractielType"))
					fractielT = ((Integer) hv.get("fractielType")).intValue();
				
				int aantalHoekpt = 4;
				if (hv.containsKey("aantalHoekpunten"))
					aantalHoekpt = ((Integer) hv.get("aantalHoekpunten")).intValue();
						
//System.out.println("set: ahp " + i + " = " + aantalHoekpt);

				int aantalPuntenPZ = 1;
				if (hv.containsKey("aantalPuntenPerZijde"))
					aantalPuntenPZ = ((Integer) hv.get("aantalPuntenPerZijde")).intValue();

//System.out.println("set: apz " + i + " = " + aantalPuntenPZ);

				double positiex = tekenPanel.startX;
				if (hv.containsKey("positiex"))
					positiex = ((Double) hv.get("positiex")).doubleValue();
				double positiey = tekenPanel.startY;
				if (hv.containsKey("positiey"))
					positiey = ((Double) hv.get("positiey")).doubleValue();
				Color kleur = Color.lightGray;
				if (hv.containsKey("kleur"))
					kleur = (Color) hv.get("kleur");
						
				tekenPanel.aantalVlakdelen = 0;
				if (fractielT == 0)
				{	tekenPanel.maakVeelhoek(aantalPuntenPZ, aantalHoekpt, positiex, positiey, kleur);
							
				}
				else
				{	tekenPanel.maakFractiel(fractielT, aantalPuntenPZ, positiex, positiey, kleur);
				}
				tempVlakdelen[i] = tekenPanel.vlakdelen[0];
//System.out.println("set: vd " + i + " ahp = " + tempVlakdelen[i].aantalHoekpunten);
//System.out.println("set: vd " + i + " apz = " + tempVlakdelen[i].aantalPuntenPerZijde);
				tekenPanel.aantalVlakdelen = 0;

			/*			
						boolean isHeap = false;
						if (hv.containsKey("isHeap"))
							isHeap = ((Boolean) hv.get("isHeap")).booleanValue();
						tempVlakdelen[i].isHeap = isHeap;
			*/			
				double orientatie = 0;
				if (hv.containsKey("orientatie"))
					orientatie = ((Double) hv.get("orientatie")).doubleValue();
				tempVlakdelen[i].orientatie = orientatie;

				boolean nieuw = true;
				if (hv.containsKey("nieuw"))
					nieuw = ((Boolean) hv.get("nieuw")).booleanValue();
				tempVlakdelen[i].nieuw = nieuw;
						
				int beginnummer = 0;
				if (hv.containsKey("beginnummer"))
					beginnummer = ((Integer) hv.get("beginnummer")).intValue();
				tempVlakdelen[i].beginnummer = beginnummer;

			/*			
						int aantalHoekpuntenVast = 0;
						if (hv.containsKey("aantalHoekpuntenVast"))
							aantalHoekpuntenVast = ((Integer) hv.get("aantalHoekpuntenVast")).intValue();
						tempVlakdelen[i].aantalHoekpuntenVast = aantalHoekpuntenVast;
			*/			
				int tVolgorde = 0; 
				if (hv.containsKey("volgorde"))
					tVolgorde = ((Integer) hv.get("volgorde")).intValue();
				tempVolgorde[i] = tVolgorde;
						
				Vector hoekpuntenVector = new Vector();
				if (hv.containsKey("hoekpuntenVector"))
					hoekpuntenVector = ((Vector) hv.get("hoekpuntenVector"));

if (hoekpuntenVector.size() != tempVlakdelen[i].hoekpunten.length)
{	
//System.out.println("hpv " + i + " = " + hoekpuntenVector.size());
//System.out.println("hpa " + i + " = " + tempVlakdelen[i].hoekpunten.length);
}

				for (int j = 0; j < hoekpuntenVector.size(); j++)
				{
					// zonder5 plakken
					Punt punt = (Punt) hoekpuntenVector.elementAt(j);
					tempVlakdelen[i].hoekpunten[j] = new HoekpuntMoz(punt.x, punt.y);
							
					// met plakken
					//tempVlakdelen[i].hoekpunten[j] = (HoekpuntMoz) hoekpuntenVector.elementAt(j);
				}		
						
			//System.out.println("set: hv = " + hoekpuntenVector.size());

			} // for

			/*		
					for (int vCnt = 0; vCnt < tempAantalVlakdelen; vCnt++)
					{	volgorde[vCnt] = vCnt;
					}
			*/		

					
			for (int tCnt = 0; tCnt < tempAantalVlakdelen; tCnt++)
			{
				tekenPanel.vlakdelen[tCnt] = tempVlakdelen[tCnt];
				tekenPanel.aantalVlakdelen++;
					
						
						//aantalVlakdelen++;
						//actiefVlakdeel = vlakdelen[tCnt];
						
				tekenPanel.volgorde[tCnt] = tempVolgorde[tCnt];
			}
			
		}
		else
		{
			int tempAantalVlakdelen = 0;
			Vector vlakdelenVector = new Vector();
			if (b.containsKey("vlakdelenVector"))
			{	vlakdelenVector = (Vector) b.get("vlakdelenVector");
				tempAantalVlakdelen = vlakdelenVector.size();
	//System.out.println("set: h contains vv");			
			}
			
	//System.out.println("set: tav = " + tempAantalVlakdelen);		
			
			if (tempAantalVlakdelen == 0)
				return;

			int[] tempVolgorde = new int[tempAantalVlakdelen];
			Vlakdeel2[] tempVlakdelen = new Vlakdeel2[tempAantalVlakdelen];
			for (int i = 0; i < tempAantalVlakdelen; i++)
			{	
				Hashtable hv = (Hashtable) vlakdelenVector.elementAt(i);
				
				int fractielT = 3;
				if (hv.containsKey("fractielType"))
					fractielT = ((Integer) hv.get("fractielType")).intValue();
				int aantalHoekpt = 4;
				if (hv.containsKey("aantalHoekpunten"))
					aantalHoekpt = ((Integer) hv.get("aantalHoekpunten")).intValue();
				
	//System.out.println("set: ap - i = " + aantalHoekpunten);

				int aantalPuntenPZ = 1;
				if (hv.containsKey("aantalPuntenPerZijde"))
					aantalPuntenPZ = ((Integer) hv.get("aantalPuntenPerZijde")).intValue();

				double positiex = tekenPanel.startX;
				if (hv.containsKey("positiex"))
					positiex = ((Double) hv.get("positiex")).doubleValue();
				double positiey = tekenPanel.startY;
				if (hv.containsKey("positiey"))
					positiey = ((Double) hv.get("positiey")).doubleValue();
				Color kleur = Color.lightGray;
				if (hv.containsKey("kleur"))
					kleur = (Color) hv.get("kleur");
				
				tekenPanel.aantalVlakdelen = 0;
				if (fractielT == 0)
				{	tekenPanel.maakVeelhoek(aantalPuntenPZ, aantalHoekpt, positiex, positiey, kleur);
					
				}
				else
				{	tekenPanel.maakFractiel(fractielT, aantalPuntenPZ, positiex, positiey, kleur);
				}
				tempVlakdelen[i] = tekenPanel.vlakdelen[0];
				tekenPanel.aantalVlakdelen = 0;

	/*			
				boolean isHeap = false;
				if (hv.containsKey("isHeap"))
					isHeap = ((Boolean) hv.get("isHeap")).booleanValue();
				tempVlakdelen[i].isHeap = isHeap;
	*/			
				double orientatie = 0;
				if (hv.containsKey("orientatie"))
					orientatie = ((Double) hv.get("orientatie")).doubleValue();
				tempVlakdelen[i].orientatie = orientatie;

				boolean nieuw = true;
				if (hv.containsKey("nieuw"))
					nieuw = ((Boolean) hv.get("nieuw")).booleanValue();
				tempVlakdelen[i].nieuw = nieuw;
				
				int beginnummer = 0;
				if (hv.containsKey("beginnummer"))
					beginnummer = ((Integer) hv.get("beginnummer")).intValue();
				tempVlakdelen[i].beginnummer = beginnummer;

	/*			
				int aantalHoekpuntenVast = 0;
				if (hv.containsKey("aantalHoekpuntenVast"))
					aantalHoekpuntenVast = ((Integer) hv.get("aantalHoekpuntenVast")).intValue();
				tempVlakdelen[i].aantalHoekpuntenVast = aantalHoekpuntenVast;
	*/			
				int tVolgorde = 0; 
				if (hv.containsKey("volgorde"))
					tVolgorde = ((Integer) hv.get("volgorde")).intValue();
				tempVolgorde[i] = tVolgorde;
				
				Vector hoekpuntenVector = new Vector();
				if (hv.containsKey("hoekpuntenVector"))
					hoekpuntenVector = ((Vector) hv.get("hoekpuntenVector"));
				for (int j = 0; j < hoekpuntenVector.size(); j++)
				{
					// zonder5 plakken
					Punt punt = (Punt) hoekpuntenVector.elementAt(j);
					tempVlakdelen[i].hoekpunten[j] = new HoekpuntMoz(punt.x, punt.y);
					
					// met plakken
					//tempVlakdelen[i].hoekpunten[j] = (HoekpuntMoz) hoekpuntenVector.elementAt(j);
				}		
				
	//System.out.println("set: hv = " + hoekpuntenVector.size());

			} // for

	/*		
			for (int vCnt = 0; vCnt < tempAantalVlakdelen; vCnt++)
			{	volgorde[vCnt] = vCnt;
			}
	*/		

			
			for (int tCnt = 0; tCnt < tempAantalVlakdelen; tCnt++)
			{
				tekenPanel.vlakdelen[tCnt] = tempVlakdelen[tCnt];
				tekenPanel.aantalVlakdelen++;
			
				
				//aantalVlakdelen++;
				//actiefVlakdeel = vlakdelen[tCnt];
				
				tekenPanel.volgorde[tCnt] = tempVolgorde[tCnt];
			}
			
		}
	

	}
	
	public void setState(Hashtable b)
	{
		//HIER state
		if (b.containsKey("appletState"))
		{
System.out.println("aES found");
			String appletState = (String) b.get("appletState");
			
			// decodeer de string
			Object o = StringCodeObject.decodeStringToObject(appletState);

			Hashtable h = (Hashtable) o;
			
					
			int tempAantalVlakdelen = 0;
			Vector vlakdelenVector = new Vector();
			if (h.containsKey("vlakdelenVector"))
			{	vlakdelenVector = (Vector) h.get("vlakdelenVector");
				tempAantalVlakdelen = vlakdelenVector.size();
			//System.out.println("set: h contains vv");			
			}
					
			//System.out.println("set: tav = " + tempAantalVlakdelen);		
					
			if (tempAantalVlakdelen == 0)
				return;

			int[] tempVolgorde = new int[tempAantalVlakdelen];
			Vlakdeel2[] tempVlakdelen = new Vlakdeel2[tempAantalVlakdelen];
			for (int i = 0; i < tempAantalVlakdelen; i++)
			{	
				Hashtable hv = (Hashtable) vlakdelenVector.elementAt(i);
					
				int fractielT = 3;
				if (hv.containsKey("fractielType"))
					fractielT = ((Integer) hv.get("fractielType")).intValue();
				int aantalHoekpt = 4;
				if (hv.containsKey("aantalHoekpunten"))
					aantalHoekpt = ((Integer) hv.get("aantalHoekpunten")).intValue();
						
			//System.out.println("set: ap - i = " + aantalHoekpunten);

				int aantalPuntenPZ = 1;
				if (hv.containsKey("aantalPuntenPerZijde"))
					aantalPuntenPZ = ((Integer) hv.get("aantalPuntenPerZijde")).intValue();

				double positiex = tekenPanel.startX;
				if (hv.containsKey("positiex"))
					positiex = ((Double) hv.get("positiex")).doubleValue();
				double positiey = tekenPanel.startY;
				if (hv.containsKey("positiey"))
					positiey = ((Double) hv.get("positiey")).doubleValue();
				Color kleur = Color.lightGray;
				if (hv.containsKey("kleur"))
					kleur = (Color) hv.get("kleur");
						
				tekenPanel.aantalVlakdelen = 0;
				if (fractielT == 0)
				{	tekenPanel.maakVeelhoek(aantalPuntenPZ, aantalHoekpt, positiex, positiey, kleur);
							
				}
				else
				{	tekenPanel.maakFractiel(fractielT, aantalPuntenPZ, positiex, positiey, kleur);
				}
				tempVlakdelen[i] = tekenPanel.vlakdelen[0];
				tekenPanel.aantalVlakdelen = 0;

			/*			
						boolean isHeap = false;
						if (hv.containsKey("isHeap"))
							isHeap = ((Boolean) hv.get("isHeap")).booleanValue();
						tempVlakdelen[i].isHeap = isHeap;
			*/			
				double orientatie = 0;
				if (hv.containsKey("orientatie"))
					orientatie = ((Double) hv.get("orientatie")).doubleValue();
				tempVlakdelen[i].orientatie = orientatie;

				boolean nieuw = true;
				if (hv.containsKey("nieuw"))
					nieuw = ((Boolean) hv.get("nieuw")).booleanValue();
				tempVlakdelen[i].nieuw = nieuw;
						
				int beginnummer = 0;
				if (hv.containsKey("beginnummer"))
					beginnummer = ((Integer) hv.get("beginnummer")).intValue();
				tempVlakdelen[i].beginnummer = beginnummer;

			/*			
						int aantalHoekpuntenVast = 0;
						if (hv.containsKey("aantalHoekpuntenVast"))
							aantalHoekpuntenVast = ((Integer) hv.get("aantalHoekpuntenVast")).intValue();
						tempVlakdelen[i].aantalHoekpuntenVast = aantalHoekpuntenVast;
			*/			
				int tVolgorde = 0; 
				if (hv.containsKey("volgorde"))
					tVolgorde = ((Integer) hv.get("volgorde")).intValue();
				tempVolgorde[i] = tVolgorde;
						
				Vector hoekpuntenVector = new Vector();
				if (hv.containsKey("hoekpuntenVector"))
					hoekpuntenVector = ((Vector) hv.get("hoekpuntenVector"));
				for (int j = 0; j < hoekpuntenVector.size(); j++)
				{
					// zonder5 plakken
					Punt punt = (Punt) hoekpuntenVector.elementAt(j);
					tempVlakdelen[i].hoekpunten[j] = new HoekpuntMoz(punt.x, punt.y);
							
					// met plakken
					//tempVlakdelen[i].hoekpunten[j] = (HoekpuntMoz) hoekpuntenVector.elementAt(j);
				}		
						
			//System.out.println("set: hv = " + hoekpuntenVector.size());

			} // for

			/*		
					for (int vCnt = 0; vCnt < tempAantalVlakdelen; vCnt++)
					{	volgorde[vCnt] = vCnt;
					}
			*/		

					
			for (int tCnt = 0; tCnt < tempAantalVlakdelen; tCnt++)
			{
				tekenPanel.vlakdelen[tCnt] = tempVlakdelen[tCnt];
				tekenPanel.aantalVlakdelen++;
					
						
						//aantalVlakdelen++;
						//actiefVlakdeel = vlakdelen[tCnt];
						
				tekenPanel.volgorde[tCnt] = tempVolgorde[tCnt];
			}
			
		}
		else
		{
			int tempAantalVlakdelen = 0;
			Vector vlakdelenVector = new Vector();
			if (b.containsKey("vlakdelenVector"))
			{	vlakdelenVector = (Vector) b.get("vlakdelenVector");
				tempAantalVlakdelen = vlakdelenVector.size();
	//System.out.println("set: h contains vv");			
			}
			
	//System.out.println("set: tav = " + tempAantalVlakdelen);		
			
			if (tempAantalVlakdelen == 0)
				return;

			int[] tempVolgorde = new int[tempAantalVlakdelen];
			Vlakdeel2[] tempVlakdelen = new Vlakdeel2[tempAantalVlakdelen];
			for (int i = 0; i < tempAantalVlakdelen; i++)
			{	
				Hashtable hv = (Hashtable) vlakdelenVector.elementAt(i);
				
				int fractielT = 3;
				if (hv.containsKey("fractielType"))
					fractielT = ((Integer) hv.get("fractielType")).intValue();
				int aantalHoekpt = 4;
				if (hv.containsKey("aantalHoekpunten"))
					aantalHoekpt = ((Integer) hv.get("aantalHoekpunten")).intValue();
				
	//System.out.println("set: ap - i = " + aantalHoekpunten);

				int aantalPuntenPZ = 1;
				if (hv.containsKey("aantalPuntenPerZijde"))
					aantalPuntenPZ = ((Integer) hv.get("aantalPuntenPerZijde")).intValue();

				double positiex = tekenPanel.startX;
				if (hv.containsKey("positiex"))
					positiex = ((Double) hv.get("positiex")).doubleValue();
				double positiey = tekenPanel.startY;
				if (hv.containsKey("positiey"))
					positiey = ((Double) hv.get("positiey")).doubleValue();
				Color kleur = Color.lightGray;
				if (hv.containsKey("kleur"))
					kleur = (Color) hv.get("kleur");
				
				tekenPanel.aantalVlakdelen = 0;
				if (fractielT == 0)
				{	tekenPanel.maakVeelhoek(aantalPuntenPZ, aantalHoekpt, positiex, positiey, kleur);
					
				}
				else
				{	tekenPanel.maakFractiel(fractielT, aantalPuntenPZ, positiex, positiey, kleur);
				}
				tempVlakdelen[i] = tekenPanel.vlakdelen[0];
				tekenPanel.aantalVlakdelen = 0;

	/*			
				boolean isHeap = false;
				if (hv.containsKey("isHeap"))
					isHeap = ((Boolean) hv.get("isHeap")).booleanValue();
				tempVlakdelen[i].isHeap = isHeap;
	*/			
				double orientatie = 0;
				if (hv.containsKey("orientatie"))
					orientatie = ((Double) hv.get("orientatie")).doubleValue();
				tempVlakdelen[i].orientatie = orientatie;

				boolean nieuw = true;
				if (hv.containsKey("nieuw"))
					nieuw = ((Boolean) hv.get("nieuw")).booleanValue();
				tempVlakdelen[i].nieuw = nieuw;
				
				int beginnummer = 0;
				if (hv.containsKey("beginnummer"))
					beginnummer = ((Integer) hv.get("beginnummer")).intValue();
				tempVlakdelen[i].beginnummer = beginnummer;

	/*			
				int aantalHoekpuntenVast = 0;
				if (hv.containsKey("aantalHoekpuntenVast"))
					aantalHoekpuntenVast = ((Integer) hv.get("aantalHoekpuntenVast")).intValue();
				tempVlakdelen[i].aantalHoekpuntenVast = aantalHoekpuntenVast;
	*/			
				int tVolgorde = 0; 
				if (hv.containsKey("volgorde"))
					tVolgorde = ((Integer) hv.get("volgorde")).intValue();
				tempVolgorde[i] = tVolgorde;
				
				Vector hoekpuntenVector = new Vector();
				if (hv.containsKey("hoekpuntenVector"))
					hoekpuntenVector = ((Vector) hv.get("hoekpuntenVector"));
				for (int j = 0; j < hoekpuntenVector.size(); j++)
				{
					// zonder5 plakken
					Punt punt = (Punt) hoekpuntenVector.elementAt(j);
					tempVlakdelen[i].hoekpunten[j] = new HoekpuntMoz(punt.x, punt.y);
					
					// met plakken
					//tempVlakdelen[i].hoekpunten[j] = (HoekpuntMoz) hoekpuntenVector.elementAt(j);
				}		
				
	//System.out.println("set: hv = " + hoekpuntenVector.size());

			} // for

	/*		
			for (int vCnt = 0; vCnt < tempAantalVlakdelen; vCnt++)
			{	volgorde[vCnt] = vCnt;
			}
	*/		

			
			for (int tCnt = 0; tCnt < tempAantalVlakdelen; tCnt++)
			{
				tekenPanel.vlakdelen[tCnt] = tempVlakdelen[tCnt];
				tekenPanel.aantalVlakdelen++;
			
				
				//aantalVlakdelen++;
				//actiefVlakdeel = vlakdelen[tCnt];
				
				tekenPanel.volgorde[tCnt] = tempVolgorde[tCnt];
			}
			
		}

	}
	
	public void setEditState(Hashtable b)
	{
System.out.println("mzip setEditState");		
		
		boolean fractielen = false; 
		boolean startFiguur = false;
		int aantalHoekpunten = 3;
		int aantalPerZijde = 1;
		int fractielType = 1;

		boolean triangles = true;
		boolean squares = true;
		boolean pentagons = false;
		boolean hexagons = true;
		boolean octagons = true;
		boolean dekagons = false;
		boolean dodekagons = true;

		if (b.containsKey("appletLaunchData"))
		{
System.out.println("aLD found");
			Hashtable appletLaunchData = (Hashtable) b.get("appletLaunchData");
			
			String fractielenString = "false";
			String startFiguurString = "";
			String aantalHoekpuntenString = "3";
			String aantalPerZijdeString = "1";
			
			if (appletLaunchData.containsKey("fractielen"))
				fractielenString = (String) appletLaunchData.get("fractielen");
			if (fractielenString.equals("true") || fractielenString.equals("yes"))
				fractielen = true;
			if (appletLaunchData.containsKey("startFiguur"))
				startFiguurString = (String) appletLaunchData.get("startFiguur");
			if (startFiguurString.length() > 0)
				startFiguur = true;
			if (fractielen)
			{	int grens = startFiguurString.indexOf(',');
				if (grens > 0)
					startFiguurString = startFiguurString.substring(0, grens);
				boolean error = false;
				try
				{	fractielType = Integer.parseInt(startFiguurString);
				}
				catch (NumberFormatException nfe)
				{	error = true;
				}
				if (!error && (fractielType >= 1) && (fractielType <= 3))
				{	// geen actie					
				}
				else
					fractielType = 1;
			}
			else
			{	int grens = startFiguurString.indexOf(',');
				if (grens > 0)
				{	aantalHoekpuntenString = startFiguurString.substring(0, grens);
					aantalPerZijdeString = startFiguurString.substring(grens + 1);
				}
				else
					aantalHoekpuntenString = startFiguurString;
				boolean error = false;
				try
				{	aantalHoekpunten = Integer.parseInt(aantalHoekpuntenString);
				}
				catch (NumberFormatException nfe)
				{	error = true;
				}
				if (!error && ((aantalHoekpunten == 3) || (aantalHoekpunten == 4) || (aantalHoekpunten == 6) ||
						       (aantalHoekpunten == 8) || (aantalHoekpunten == 12)))
				{	// geen actie					
				}
				else
					aantalHoekpunten = 3;
				error = false;
				try
				{	aantalPerZijde = Integer.parseInt(aantalPerZijdeString);
				}
				catch (NumberFormatException nfe)
				{	error = true;
				}
				if (!error && (aantalPerZijde >= 1) && (aantalPerZijde <= 4))
				{	// geen actie					
				}
				else
					aantalPerZijde = 1;
			}

		}
		else
		{
			if (b.containsKey("fractielen"))
				fractielen = ((Boolean) b.get("fractielen")).booleanValue();
			if (b.containsKey("startFiguur"))
				startFiguur = ((Boolean) b.get("startFiguur")).booleanValue();
			if (b.containsKey("fractielType"))
				fractielType = ((Integer) b.get("fractielType")).intValue();
			if (b.containsKey("aantalHoekpunten"))
				aantalHoekpunten = ((Integer) b.get("aantalHoekpunten")).intValue();
			if (b.containsKey("aantalPerZijde"))
				aantalPerZijde = ((Integer) b.get("aantalPerZijde")).intValue();
			
			if (b.containsKey("triangles"))
				triangles = ((Boolean) b.get("triangles")).booleanValue();
			if (b.containsKey("squares"))
				squares = ((Boolean) b.get("squares")).booleanValue();
			if (b.containsKey("pentagons"))
				pentagons = ((Boolean) b.get("pentagons")).booleanValue();
			if (b.containsKey("hexagons"))
				hexagons = ((Boolean) b.get("hexagons")).booleanValue();
			if (b.containsKey("octagons"))
				octagons = ((Boolean) b.get("octagons")).booleanValue();
			if (b.containsKey("dekagons"))
				dekagons = ((Boolean) b.get("dekagons")).booleanValue();
			if (b.containsKey("dodekagons"))
				dodekagons = ((Boolean) b.get("dodekagons")).booleanValue();
			
		}
		
		zetFractielen(fractielen);
		zetStartFiguur(startFiguur);
		zetFractielType(fractielType);
		zetAantalHoekpunten(aantalHoekpunten);
		zetAantalPerZijde(aantalPerZijde);

		setTriangles(triangles);
		setSquares(squares);
		setPentagons(pentagons);
		setHexagons(hexagons);
		setOctagons(octagons);
		setDekagons(dekagons);
		setDodekagons(dodekagons);
		
		
		//HIER state
		if (b.containsKey("appletEditState"))
		{
System.out.println("aES found");
			String appletEditState = (String) b.get("appletEditState");
			
			// decodeer de string
			Object o = StringCodeObject.decodeStringToObject(appletEditState);

			Hashtable h = (Hashtable) o;
			
					
			int tempAantalVlakdelen = 0;
			Vector vlakdelenVector = new Vector();
			if (h.containsKey("vlakdelenVector"))
			{	vlakdelenVector = (Vector) h.get("vlakdelenVector");
				tempAantalVlakdelen = vlakdelenVector.size();
			//System.out.println("set: h contains vv");			
			}
					
			//System.out.println("set: tav = " + tempAantalVlakdelen);		
					
			if (tempAantalVlakdelen == 0)
				return;

			int[] tempVolgorde = new int[tempAantalVlakdelen];
			Vlakdeel2[] tempVlakdelen = new Vlakdeel2[tempAantalVlakdelen];
			for (int i = 0; i < tempAantalVlakdelen; i++)
			{	
				Hashtable hv = (Hashtable) vlakdelenVector.elementAt(i);
					
				int fractielT = 3;
				if (hv.containsKey("fractielType"))
					fractielT = ((Integer) hv.get("fractielType")).intValue();
				int aantalHoekpt = 4;
				if (hv.containsKey("aantalHoekpunten"))
					aantalHoekpt = ((Integer) hv.get("aantalHoekpunten")).intValue();
						
			//System.out.println("set: ap - i = " + aantalHoekpunten);

				int aantalPuntenPZ = 1;
				if (hv.containsKey("aantalPuntenPerZijde"))
					aantalPuntenPZ = ((Integer) hv.get("aantalPuntenPerZijde")).intValue();

				double positiex = tekenPanel.startX;
				if (hv.containsKey("positiex"))
					positiex = ((Double) hv.get("positiex")).doubleValue();
				double positiey = tekenPanel.startY;
				if (hv.containsKey("positiey"))
					positiey = ((Double) hv.get("positiey")).doubleValue();
				Color kleur = Color.lightGray;
				if (hv.containsKey("kleur"))
					kleur = (Color) hv.get("kleur");
						
				tekenPanel.aantalVlakdelen = 0;
				if (fractielT == 0)
				{	tekenPanel.maakVeelhoek(aantalPuntenPZ, aantalHoekpt, positiex, positiey, kleur);
							
				}
				else
				{	tekenPanel.maakFractiel(fractielT, aantalPuntenPZ, positiex, positiey, kleur);
				}
				tempVlakdelen[i] = tekenPanel.vlakdelen[0];
				tekenPanel.aantalVlakdelen = 0;

			/*			
						boolean isHeap = false;
						if (hv.containsKey("isHeap"))
							isHeap = ((Boolean) hv.get("isHeap")).booleanValue();
						tempVlakdelen[i].isHeap = isHeap;
			*/			
				double orientatie = 0;
				if (hv.containsKey("orientatie"))
					orientatie = ((Double) hv.get("orientatie")).doubleValue();
				tempVlakdelen[i].orientatie = orientatie;

				boolean nieuw = true;
				if (hv.containsKey("nieuw"))
					nieuw = ((Boolean) hv.get("nieuw")).booleanValue();
				tempVlakdelen[i].nieuw = nieuw;
						
				int beginnummer = 0;
				if (hv.containsKey("beginnummer"))
					beginnummer = ((Integer) hv.get("beginnummer")).intValue();
				tempVlakdelen[i].beginnummer = beginnummer;

			/*			
						int aantalHoekpuntenVast = 0;
						if (hv.containsKey("aantalHoekpuntenVast"))
							aantalHoekpuntenVast = ((Integer) hv.get("aantalHoekpuntenVast")).intValue();
						tempVlakdelen[i].aantalHoekpuntenVast = aantalHoekpuntenVast;
			*/			
				int tVolgorde = 0; 
				if (hv.containsKey("volgorde"))
					tVolgorde = ((Integer) hv.get("volgorde")).intValue();
				tempVolgorde[i] = tVolgorde;
						
				Vector hoekpuntenVector = new Vector();
				if (hv.containsKey("hoekpuntenVector"))
					hoekpuntenVector = ((Vector) hv.get("hoekpuntenVector"));
				for (int j = 0; j < hoekpuntenVector.size(); j++)
				{
					// zonder5 plakken
					Punt punt = (Punt) hoekpuntenVector.elementAt(j);
					tempVlakdelen[i].hoekpunten[j] = new HoekpuntMoz(punt.x, punt.y);
							
					// met plakken
					//tempVlakdelen[i].hoekpunten[j] = (HoekpuntMoz) hoekpuntenVector.elementAt(j);
				}		
						
			//System.out.println("set: hv = " + hoekpuntenVector.size());

			} // for

			/*		
					for (int vCnt = 0; vCnt < tempAantalVlakdelen; vCnt++)
					{	volgorde[vCnt] = vCnt;
					}
			*/		

					
			for (int tCnt = 0; tCnt < tempAantalVlakdelen; tCnt++)
			{
				tekenPanel.vlakdelen[tCnt] = tempVlakdelen[tCnt];
				tekenPanel.aantalVlakdelen++;
					
						
						//aantalVlakdelen++;
						//actiefVlakdeel = vlakdelen[tCnt];
						
				tekenPanel.volgorde[tCnt] = tempVolgorde[tCnt];
			}
			
		}
		else
		{
			int tempAantalVlakdelen = 0;
			Vector vlakdelenVector = new Vector();
			if (b.containsKey("vlakdelenVector"))
			{	vlakdelenVector = (Vector) b.get("vlakdelenVector");
				tempAantalVlakdelen = vlakdelenVector.size();
System.out.println("set: h contains vv");			
			}
			
	//System.out.println("set: tav = " + tempAantalVlakdelen);		
			
			if (tempAantalVlakdelen == 0)
				return;

			int[] tempVolgorde = new int[tempAantalVlakdelen];
			Vlakdeel2[] tempVlakdelen = new Vlakdeel2[tempAantalVlakdelen];
			for (int i = 0; i < tempAantalVlakdelen; i++)
			{	
				Hashtable hv = (Hashtable) vlakdelenVector.elementAt(i);
				
				int fractielT = 3;
				if (hv.containsKey("fractielType"))
					fractielT = ((Integer) hv.get("fractielType")).intValue();
				int aantalHoekpt = 4;
				if (hv.containsKey("aantalHoekpunten"))
					aantalHoekpt = ((Integer) hv.get("aantalHoekpunten")).intValue();
				
	//System.out.println("set: ap - i = " + aantalHoekpunten);

				int aantalPuntenPZ = 1;
				if (hv.containsKey("aantalPuntenPerZijde"))
					aantalPuntenPZ = ((Integer) hv.get("aantalPuntenPerZijde")).intValue();

				double positiex = tekenPanel.startX;
				if (hv.containsKey("positiex"))
					positiex = ((Double) hv.get("positiex")).doubleValue();
				double positiey = tekenPanel.startY;
				if (hv.containsKey("positiey"))
					positiey = ((Double) hv.get("positiey")).doubleValue();
				Color kleur = Color.lightGray;
				if (hv.containsKey("kleur"))
					kleur = (Color) hv.get("kleur");
				
				tekenPanel.aantalVlakdelen = 0;
				if (fractielT == 0)
				{	tekenPanel.maakVeelhoek(aantalPuntenPZ, aantalHoekpt, positiex, positiey, kleur);
					
				}
				else
				{	tekenPanel.maakFractiel(fractielT, aantalPuntenPZ, positiex, positiey, kleur);
				}
				tempVlakdelen[i] = tekenPanel.vlakdelen[0];
				tekenPanel.aantalVlakdelen = 0;

	/*			
				boolean isHeap = false;
				if (hv.containsKey("isHeap"))
					isHeap = ((Boolean) hv.get("isHeap")).booleanValue();
				tempVlakdelen[i].isHeap = isHeap;
	*/			
				double orientatie = 0;
				if (hv.containsKey("orientatie"))
					orientatie = ((Double) hv.get("orientatie")).doubleValue();
				tempVlakdelen[i].orientatie = orientatie;

				boolean nieuw = true;
				if (hv.containsKey("nieuw"))
					nieuw = ((Boolean) hv.get("nieuw")).booleanValue();
				tempVlakdelen[i].nieuw = nieuw;
				
				int beginnummer = 0;
				if (hv.containsKey("beginnummer"))
					beginnummer = ((Integer) hv.get("beginnummer")).intValue();
				tempVlakdelen[i].beginnummer = beginnummer;

	/*			
				int aantalHoekpuntenVast = 0;
				if (hv.containsKey("aantalHoekpuntenVast"))
					aantalHoekpuntenVast = ((Integer) hv.get("aantalHoekpuntenVast")).intValue();
				tempVlakdelen[i].aantalHoekpuntenVast = aantalHoekpuntenVast;
	*/			
				int tVolgorde = 0; 
				if (hv.containsKey("volgorde"))
					tVolgorde = ((Integer) hv.get("volgorde")).intValue();
				tempVolgorde[i] = tVolgorde;
				
				Vector hoekpuntenVector = new Vector();
				if (hv.containsKey("hoekpuntenVector"))
					hoekpuntenVector = ((Vector) hv.get("hoekpuntenVector"));
				for (int j = 0; j < hoekpuntenVector.size(); j++)
				{
					// zonder5 plakken
					Punt punt = (Punt) hoekpuntenVector.elementAt(j);
					tempVlakdelen[i].hoekpunten[j] = new HoekpuntMoz(punt.x, punt.y);
					
					// met plakken
					//tempVlakdelen[i].hoekpunten[j] = (HoekpuntMoz) hoekpuntenVector.elementAt(j);
				}		
				
	//System.out.println("set: hv = " + hoekpuntenVector.size());

			} // for

	/*		
			for (int vCnt = 0; vCnt < tempAantalVlakdelen; vCnt++)
			{	volgorde[vCnt] = vCnt;
			}
	*/		

			
			for (int tCnt = 0; tCnt < tempAantalVlakdelen; tCnt++)
			{
				tekenPanel.vlakdelen[tCnt] = tempVlakdelen[tCnt];
				tekenPanel.aantalVlakdelen++;
			
				
				//aantalVlakdelen++;
				//actiefVlakdeel = vlakdelen[tCnt];
				
				tekenPanel.volgorde[tCnt] = tempVolgorde[tCnt];
			}
			
		}

	}
	
	public Hashtable getState()
	{
		Hashtable h = new Hashtable();
		
		Vector vlakdelenVector = new Vector();
		for (int i = 0; i < tekenPanel.aantalVlakdelen; i++)
		{	Hashtable hv = new Hashtable();
			hv.put("fractielType", new Integer(tekenPanel.vlakdelen[i].fractielType));
			hv.put("aantalHoekpunten", new Integer(tekenPanel.vlakdelen[i].aantalHoekpunten));
			hv.put("aantalPuntenPerZijde", new Integer(tekenPanel.vlakdelen[i].aantalPuntenPerZijde));
			hv.put("positiex", new Double(tekenPanel.vlakdelen[i].draaipunt.x));
			hv.put("positiey", new Double(tekenPanel.vlakdelen[i].draaipunt.y));
			hv.put("kleur", tekenPanel.vlakdelen[i].kleur);
			hv.put("orientatie", new Double(tekenPanel.vlakdelen[i].orientatie));
			hv.put("nieuw", new Boolean(tekenPanel.vlakdelen[i].nieuw));
			hv.put("beginnummer", new Integer(tekenPanel.vlakdelen[i].beginnummer));
//			hv.put("aantalHoekpuntenVast", new Integer(tekenPanel.vlakdelen[i].aantalHoekpuntenVast));
//			hv.put("isHeap", new Boolean(tekenPanel.vlakdelen[i].isHeap));
			
			hv.put("volgorde", new Integer(tekenPanel.volgorde[i]));
			
			Vector hoekpuntenVector = new Vector();
			for (int j = 0; j < tekenPanel.vlakdelen[i].aantalPunten + 1; j++)
			{	hoekpuntenVector.addElement(
					
					// zonder plakken
					new Punt(tekenPanel.vlakdelen[i].hoekpunten[j].x, tekenPanel.vlakdelen[i].hoekpunten[j].y));
			
					// met plakken
					//tekenPanel.vlakdelen[i].hoekpunten[j]);
			}
			hv.put("hoekpuntenVector", hoekpuntenVector);

//System.out.println("get: hv = " + hoekpuntenVector.size());			
			
			vlakdelenVector.addElement(hv);
		}
//System.out.println("get: vv = " + vlakdelenVector.size());

		h.put("vlakdelenVector", vlakdelenVector);
		
		return h;
		
	}
	
	public Hashtable getEditState()
	{
		Hashtable h = new Hashtable();
		
		h.put("fractielen", new Boolean(fractielen));
		h.put("startFiguur", new Boolean (startFiguur));
		h.put("fractielType", new Integer(fractielType));
		h.put("aantalHoekpunten", new Integer(aantalHoekpunten));
		h.put("aantalPerZijde", new Integer(aantalPerZijde));
		
		h.put("triangles", new Boolean(triangles));
		h.put("squares", new Boolean(squares));
		h.put("pentagons", new Boolean(pentagons));
		h.put("hexagons", new Boolean(hexagons));
		h.put("octagons", new Boolean(octagons));
		h.put("dekagons", new Boolean(dekagons));
		h.put("dodekagons", new Boolean(dodekagons));
		
		Vector vlakdelenVector = new Vector();
		
		for (int i = 0; i < tekenPanel.aantalVlakdelen; i++)
		{	Hashtable hv = new Hashtable();
			hv.put("fractielType", new Integer(tekenPanel.vlakdelen[i].fractielType));
			hv.put("aantalHoekpunten", new Integer(tekenPanel.vlakdelen[i].aantalHoekpunten));
			hv.put("aantalPuntenPerZijde", new Integer(tekenPanel.vlakdelen[i].aantalPuntenPerZijde));
			hv.put("positiex", new Double(tekenPanel.vlakdelen[i].draaipunt.x));
			hv.put("positiey", new Double(tekenPanel.vlakdelen[i].draaipunt.y));
			hv.put("kleur", tekenPanel.vlakdelen[i].kleur);
			hv.put("orientatie", new Double(tekenPanel.vlakdelen[i].orientatie));
			hv.put("nieuw", new Boolean(tekenPanel.vlakdelen[i].nieuw));
			hv.put("beginnummer", new Integer(tekenPanel.vlakdelen[i].beginnummer));
//			hv.put("aantalHoekpuntenVast", new Integer(tekenPanel.vlakdelen[i].aantalHoekpuntenVast));
//			hv.put("isHeap", new Boolean(tekenPanel.vlakdelen[i].isHeap));
			
			hv.put("volgorde", new Integer(tekenPanel.volgorde[i]));
			
			Vector hoekpuntenVector = new Vector();
			for (int j = 0; j < tekenPanel.vlakdelen[i].aantalPunten + 1; j++)
			{	hoekpuntenVector.addElement(
					
					// zonder plakken
					new Punt(tekenPanel.vlakdelen[i].hoekpunten[j].x, tekenPanel.vlakdelen[i].hoekpunten[j].y));
			
					// met plakken
					//tekenPanel.vlakdelen[i].hoekpunten[j]);
			}
			hv.put("hoekpuntenVector", hoekpuntenVector);

//System.out.println("get: hv = " + hoekpuntenVector.size());			
			
			vlakdelenVector.addElement(hv);
		}
//System.out.println("get: vv = " + vlakdelenVector.size());

		h.put("vlakdelenVector", vlakdelenVector);
		
		
		return h;
	}
	
	public void zetFractielen(boolean b)
	{
		fractielen = b;
		tekenPanel.fractielen = b;
		tekenPanel.initialiseer2();
		tekenPanel.repaint();
		
	}
	
	public void zetStartFiguur(boolean b)
	{
		startFiguur = b;
		tekenPanel.beginFig = b;
		tekenPanel.initialiseer2();
		tekenPanel.repaint();
		
	}
	
	public void zetFractielType(int fType)
	{
		fractielType = fType;
		tekenPanel.beginFractielType = fType;
		tekenPanel.initialiseer2();
		tekenPanel.repaint();
		
	}
	
	public void zetAantalHoekpunten(int aantalHPunten)
	{
		aantalHoekpunten = aantalHPunten;
		tekenPanel.beginFigAantalHp = aantalHPunten;
		tekenPanel.initialiseer2();
		tekenPanel.repaint();
		
	}

	public void zetAantalPerZijde(int aantalPZijde)
	{
		aantalPerZijde = aantalPZijde;
		tekenPanel.beginFigAantalPz = aantalPZijde;
		tekenPanel.initialiseer2();
		tekenPanel.repaint();
		
	}
	
	public void setTriangles(boolean b)
	{	triangles = b;
		tekenPanel.triangles = b;
		tekenPanel.initialiseer2();
		tekenPanel.repaint();
	}
	public void setSquares(boolean b)
	{	squares = b;
		tekenPanel.squares = b;
		tekenPanel.initialiseer2();
		tekenPanel.repaint();
	}
	public void setPentagons(boolean b)
	{	pentagons = b;
		tekenPanel.pentagons = b;
		tekenPanel.initialiseer2();
		tekenPanel.repaint();
	}
	public void setHexagons(boolean b)
	{	hexagons = b;
		tekenPanel.hexagons = b;
		tekenPanel.initialiseer2();
		tekenPanel.repaint();
	}
	public void setOctagons(boolean b)
	{	octagons = b;
		tekenPanel.octagons = b;
		tekenPanel.initialiseer2();
		tekenPanel.repaint();
	}
	public void setDekagons(boolean b)
	{	dekagons = b;
		tekenPanel.dekagons = b;
		tekenPanel.initialiseer2();
		tekenPanel.repaint();
	}
	public void setDodekagons(boolean b)
	{	dodekagons = b;
		tekenPanel.dodekagons = b;
		tekenPanel.initialiseer2();
		tekenPanel.repaint();
	}
	
	public InteractieEditPanel getEditPanel()
	{
		return new MZInteractieEditPanel();
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		
//		System.out.println("mzip set bounds " + b + " " + h);
		
		if (h == 1)
			return;
		
		if ((getLocation().x == x) && (getLocation().y == y) && 
			(getSize().width == b) && (getSize().height == h))
				return;
		
		super.setBounds(x, y, b, h);
		
		
		if (tekenPanel == null) 
		{	tekenPanel = new TekenPanel();
			add(tekenPanel, 0);
			tekenPanel.setSize(b, h);
			tekenPanel.init();
//System.out.println("tekenPanel created");			
		}
		else
		{	tekenPanel.setSize(b, h);
			tekenPanel.initialiseer2();
			tekenPanel.tb.setBounds(0, 0, b, h);
			tekenPanel.repaint();

//System.out.println("g3dc sized");		
		}
		
	}
	
	public void wis()
	{}
	
	public void zetMaat()
	{}
	
	public int geefAsHoogte()
	{	return 0;
	}
	
	public int getIpId()
	{	return 0;
	}
	
	public String getIpExpString()
	{	return null;
	}
	
	public int getScore()
	{	return score;
	}
	
	public int getScoreMax()
	{	return scoreMax;
	}
	
	public boolean isCorrect()
	{	return true;
	}
	
	public boolean isFout()
	{	return false;
	}
	
	public void zetMode(int mode)
	{}
	
	public void zetNagekeken(boolean b)
	{}
	
    public void stop()
    {}
    
    public void start()
    {}
    
    public void destroy()
    {}
    
    public void opnieuw()
    {}
    
    public void kijkNa()
    {}
    
    public void kijkNa(int stapNr)
    {}
    
    public void addActionListener(ActionListener al)
    {}

	public void zetBreedte(int b)
	{}
	
	public void zetHoogte(int h)
	{}
    
	public void actionPerformed(ActionEvent e)
	{}
}
