package fi.verknippen;

import java.awt.AWTEventMulticaster;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Point;
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

public class VerknippenInteractiePanel extends JPanel implements InteractiePanel, InteractieEditPanel,
							         ActionListener
									
{	Image foutKruis, goedKrul, halfKrul;
	ImageIcon foutKruisIcon, goedKrulIcon, halfKrulIcon;
	Image penDefault, penRollover, penSelected, gumDefault, gumRollover, gumSelected;
	ImageIcon penDefaultIcon, penRolloverIcon, penSelectedIcon, gumDefaultIcon, gumRolloverIcon, gumSelectedIcon;
	Image tekenCursor, gumCursor;
	ImageIcon tekenCursorIcon, gumCursorIcon; 
	
	
	// fonts
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;

	int bottomHeight = 55;	
	DrawingPanel2 drawingPanel2;
	BottomPanel2 bottomPanel2;
	
	int taakNummer = 1;
	boolean balkOnderaan = true;
	boolean roosterZichtbaar = false;
	boolean groteBalletjes = false;
	boolean schaduwZichtbaar = false;
	boolean afmetingenZichtbaar = false;
	boolean tekenGumOptie = false;
	boolean figuurTransparant = true;
   	String rodeFiguurString =  "2,0|10,0|8,8|0,8";
   	Vector rodeFiguurCoordinaten = new Vector();
	int gridSize = 20;  
	int minGrid = 16;
	int maxGrid = 50;
	int oppervlakteRood = 64;    	
	String grijzeFiguurString = "0,0|8,0|8,8|0,8";
	Vector grijzeFiguurCoordinaten = new Vector();
	int oppervlakteGrijs = 64;    	    	
	
	
	int antwoord = 0;
	int antwoordenFout = 0;
	boolean antwoordOK = false;

	int score = 0;
	int scoreMax = 10;
	
	boolean kijkNaActief = true;
    boolean ingevuld;
	private boolean nagekeken;
	private int mode;

    Vector listeners = new Vector();
    
	boolean noSetBounds = false;	
	
	
	public VerknippenInteractiePanel()
	{
		setLayout(null);
		// echte initiatie vind pas plaats na setBounds
		
		java.net.URL imageURL = Verknippen.class.getResource("resources/goedkrul_en.gif");
		if (imageURL != null) 
		{
		    goedKrulIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading goedkrul.gif.");
		}
		imageURL = Verknippen.class.getResource("resources/foutkruis.gif");
		if (imageURL != null) 
		{
			foutKruisIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading foutkruis.gif");
		}
		imageURL = Verknippen.class.getResource("resources/goedkrulhalf.gif");
		if (imageURL != null) 
		{
			halfKrulIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading goedkrulhalf.gif");
		}
		imageURL = Verknippen.class.getResource("resources/teken_penknop_default.gif");
		if (imageURL != null) 
		{
			penDefaultIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_penknop_default.gif");
		}
		imageURL = Verknippen.class.getResource("resources/teken_penknop_rollover.gif");
		if (imageURL != null) 
		{
			penRolloverIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_penknop_rollover.gif");
		}
		imageURL = Verknippen.class.getResource("resources/teken_penknop_selected.gif");
		if (imageURL != null) 
		{
			penSelectedIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_penknop_selected.gif");
		}
		imageURL = Verknippen.class.getResource("resources/teken_gumknop_default.gif");
		if (imageURL != null) 
		{
			gumDefaultIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_gumknop_default.gif");
		}
		imageURL = Verknippen.class.getResource("resources/teken_gumknop_rollover.gif");
		if (imageURL != null) 
		{
			gumRolloverIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_gumknop_rollover.gif");
		}
		imageURL = Verknippen.class.getResource("resources/teken_gumknop_selected.gif");
		if (imageURL != null) 
		{
			gumSelectedIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_gumknop_selected.gif");
		}
		imageURL = Verknippen.class.getResource("resources/tekencursor.gif");
		if (imageURL != null) 
		{
			tekenCursorIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading tekencursor.gif");
		}
		imageURL = Verknippen.class.getResource("resources/gumcursor.gif");
		if (imageURL != null) 
		{
			gumCursorIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading gumcursor.gif");
		}
		
		goedKrul = goedKrulIcon.getImage();
		foutKruis = foutKruisIcon.getImage();
		halfKrul = halfKrulIcon.getImage();
		penDefault = penDefaultIcon.getImage();
		penRollover = penRolloverIcon.getImage();
		penSelected = penSelectedIcon.getImage();
		gumDefault = gumDefaultIcon.getImage();
		gumRollover = gumRolloverIcon.getImage();
		gumSelected = gumSelectedIcon.getImage();
		tekenCursor = tekenCursorIcon.getImage();
		gumCursor = gumCursorIcon.getImage();
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		rodeFiguurCoordinaten = processFiguurString(rodeFiguurString);
		grijzeFiguurCoordinaten = processFiguurString(grijzeFiguurString);
	}

	public void opnieuwAction()
	{
		drawingPanel2.removeAllKnipPolygons();
		
		drawingPanel2.rectangles.removeAllElements();
		
		KnipPolygon2 kp = new KnipPolygon2(drawingPanel2, rodeFiguurCoordinaten,
										   KnipPolygon2.CENTER);
										 
		if (taakNummer == 4)
		{	kp = new KnipPolygon2(drawingPanel2, rodeFiguurCoordinaten,
								  KnipPolygon2.RIGHTAL);
		}												 
		if ((taakNummer == 2) || (taakNummer == 3))
			kp.setLabelPoint();
										 
		drawingPanel2.addKnipPolygon(kp);        

		drawingPanel2.oval1Pos = null;
		drawingPanel2.oval2Pos = null;			        
		drawingPanel2.oval3Pos = null;
		drawingPanel2.repaint();
		drawingPanel2.figureIsRectangle = false;

		antwoordOK = false;
		antwoord = 0;
		antwoordenFout = 0;

		if (taakNummer == 1)
		{	bottomPanel2.opdrachtLabel.setText(Verknippen.rb.getString("maakRechthoekTekst"));
		}
		else // taakNummer==2 of taakNummer==3 of taakNummer==4
		{	antwoord = 0;
			bottomPanel2.oppervlakteTextField.setText("");
		}	
		
		bottomPanel2.showGoed = false;
		bottomPanel2.showFout = false;

		
		repaint();
		
	}
	
	public void zetTaakNummer(int taakNum, boolean docent)
	{	taakNummer = taakNum;
		// drawingPanel2
		drawingPanel2.oval3Pos = null;
		if (taakNummer < 4)
		{	drawingPanel2.grijsPolygon = null;	
		}
		else // taakNummer==4
		{	zetGrijzeFiguur(grijzeFiguurString);
		}

		if (docent)
			drawingPanel2.removeAllKnipPolygons();
		
		if (drawingPanel2.knipPolygons.size() == 0)
			zetRodeFiguur(rodeFiguurString);
		else
		{	rodeFiguurCoordinaten = processFiguurString(rodeFiguurString);
		}
		
		
		if ((taakNummer == 2) || (taakNummer == 3))
		{
			for (int kpCnt = 0; kpCnt < drawingPanel2.knipPolygons.size(); kpCnt++)
			{	KnipPolygon2 kp = (KnipPolygon2) drawingPanel2.knipPolygons.elementAt(kpCnt);
				kp.setLabelPoint();
			}
			
		}
		else
		{
			for (int kpCnt = 0; kpCnt < drawingPanel2.knipPolygons.size(); kpCnt++)
			{	KnipPolygon2 kp = (KnipPolygon2) drawingPanel2.knipPolygons.elementAt(kpCnt);
				kp.labelPoint = null;
				kp.labelRect = null;
			}
			
		}
		zetSchaduwZichtbaar(schaduwZichtbaar);
		bottomPanel2.zetTaakNummer(taakNummer);
		
	}
	
	public void zetBalkOnderaan(boolean b)
	{	balkOnderaan = b;
		drawingPanel2.zetBorder(b);
		setSize(getSize().width, getSize().height);
		if (balkOnderaan)
		{	bottomPanel2.setVisible(true);
			bottomPanel2.setSize(getSize().width, bottomHeight);
			drawingPanel2.setSize(getSize().width, getSize().height - bottomHeight);
			zetGridSize(gridSize);
		}
		else
		{	bottomPanel2.setVisible(false);
			bottomPanel2.setSize(0,0);
			drawingPanel2.setSize(getSize().width, getSize().height);
			zetGridSize(gridSize);
		}
		
	}
	
	
	public void zetRoosterZichtbaar(boolean b)
	{	roosterZichtbaar = b;
		drawingPanel2.showGrid = roosterZichtbaar;
		drawingPanel2.repaint();
	}
	
	public void zetGroteBalletjes(boolean b)
	{	groteBalletjes = b;
		drawingPanel2.zetBalletjesGrootte();
	}

	public void zetSchaduwZichtbaar(boolean b)
	{	schaduwZichtbaar = b;
		if (schaduwZichtbaar)
		{	Vector rodeFiguurCoordinaten = processFiguurString(rodeFiguurString);
			if (rodeFiguurCoordinaten == null)
				return;
			this.rodeFiguurCoordinaten = rodeFiguurCoordinaten;
			
			if (taakNummer < 4)
			{	KnipPolygon2 sp = new KnipPolygon2(drawingPanel2, rodeFiguurCoordinaten, KnipPolygon2.CENTER);
				drawingPanel2.shadowPolygon = sp;
			}
			else // taakNummer== 4
			{	KnipPolygon2 sp = new KnipPolygon2(drawingPanel2, rodeFiguurCoordinaten, KnipPolygon2.RIGHTAL);
				drawingPanel2.shadowPolygon = sp;				 	
			}	
			drawingPanel2.repaint();
		}
		else
		{	drawingPanel2.shadowPolygon = null;
			drawingPanel2.repaint();
		}
		
	}

	public void zetAfmetingenZichtbaar(boolean b)
	{	afmetingenZichtbaar = b;
		drawingPanel2.showSizes = afmetingenZichtbaar;
		drawingPanel2.repaint();
		
	}

	public void zetTekenGumOptie(boolean b)
	{	tekenGumOptie = b;
		drawingPanel2.zetTekenGumOptie(tekenGumOptie);
		if (tekenGumOptie)
			zetRoosterZichtbaar(true);
		drawingPanel2.repaint();
		
	}

	public void zetFiguurTransparant(boolean b)
	{	figuurTransparant = b;
		drawingPanel2.zetFiguurTransparant(figuurTransparant);

	}
	
	
	public void zetGridSize(int gSize)
	{	gridSize = gSize;
		drawingPanel2.gridSize = gridSize;
		zetRodeFiguur(rodeFiguurString);
		zetGrijzeFiguur(grijzeFiguurString);
		drawingPanel2.rectangles.removeAllElements();
		drawingPanel2.repaint();
	}
	
	public void zetRodeFiguur(String rodeFiguurString)
	{	Vector rodeFiguurCoordinaten = processFiguurString(rodeFiguurString);
		if (rodeFiguurCoordinaten == null)
			return;
		this.rodeFiguurString = rodeFiguurString;
		this.rodeFiguurCoordinaten = rodeFiguurCoordinaten;
		drawingPanel2.removeAllKnipPolygons();
		KnipPolygon2 kp = null;
		if (taakNummer < 4)
			kp = new KnipPolygon2(drawingPanel2, rodeFiguurCoordinaten, KnipPolygon2.CENTER);
		else
			kp = new KnipPolygon2(drawingPanel2, rodeFiguurCoordinaten, KnipPolygon2.RIGHTAL);
		drawingPanel2.addKnipPolygon(kp);
		
		zetSchaduwZichtbaar(schaduwZichtbaar);
	
	}

	public void zetOppervlakteRood(int oppervlakteRood)
	{	this.oppervlakteRood = oppervlakteRood;
	}
	
	public void zetGrijzeFiguur(String grijzeFiguurString)
	{	Vector grijzeFiguurCoordinaten = processFiguurString(grijzeFiguurString);
		if (grijzeFiguurCoordinaten == null)
			return;
		this.grijzeFiguurString = grijzeFiguurString;
		this.grijzeFiguurCoordinaten = grijzeFiguurCoordinaten;
		if (taakNummer == 4)
			drawingPanel2.grijsPolygon = new KnipPolygon2(drawingPanel2, rodeFiguurCoordinaten, KnipPolygon2.LEFTAL);
		
	}

	public void zetOppervlakteGrijs(int oppervlakteGrijs)
	{	this.oppervlakteGrijs = oppervlakteGrijs;
	}
	
	public Vector processFiguurString(String fString)
	{	fString = removeAllBlanks(fString);
		Vector figCoordStrings = parseOnChar(fString, '|');
		if (figCoordStrings.size() == 0)
			return null;
		Vector figCoords = new Vector();
		boolean error = false;
		for (int cCnt = 0; cCnt < figCoordStrings.size(); cCnt++)
		{	String aCoordString = (String) figCoordStrings.elementAt(cCnt);
			int kommaIndex = aCoordString.indexOf(',');
			if ((kommaIndex >= 0) && (kommaIndex < (aCoordString.length() - 1)))
			{	String xCoordString = aCoordString.substring(0, kommaIndex);
				String yCoordString = aCoordString.substring(kommaIndex + 1);
				int xCoord = 0;
				int yCoord = 0;
				//boolean error = false;
				try
				{	xCoord = Integer.parseInt(xCoordString);
					yCoord = Integer.parseInt(yCoordString);	
				}
				catch (NumberFormatException nfe)
				{	error = true;
				}
				if (!error)
				{	Point figPoint = new Point(xCoord, yCoord);
					figCoords.addElement(figPoint);
				}
			}
			else
				error = true;
		}
		if (error)
			return null;
		else
			return figCoords;
			
	} 

	public Vector parseOnChar(String inString, char c)
	{	Vector result = new Vector();
		if ((inString == null) || (inString.length() == 0))
			return result;
		int index = inString.indexOf(c);
		while (index >= 0)
		{	String word = inString.substring(0,index);
			if (word.length() > 0)
			{	word = skipSpaces(word);
				result.addElement(word);
			}	
			if (inString.length() > (index + 1))					
				inString = inString.substring(index + 1);
			else 	
				inString = "";
			index = inString.indexOf(c);	
		}	
		if (inString.length() > 0)
		{	inString = skipSpaces(inString);
			result.addElement(inString);
		}	
		return result;	
	}

	public String skipSpaces(String inString)
	{	String result = inString;
		while ((result.length() > 0) && (result.charAt(0) == ' '))
			result = result.substring(1);
		return result;	
	}

    public String removeAllBlanks(String s)
    {   int index = s.indexOf(' ');
        while (index >= 0)
        {   s = s.substring(0, index) + s.substring(index + 1);
            index = s.indexOf(' ');
        }
        return s;
    }
	
	
	public void zetOpdracht(Hashtable b, String[] randomVars, Hashtable randomValues)
	{
		
System.out.println("vip zetOpdracht");

		int taakNummer = 1;
		boolean balkOnderaan = true;
		boolean roosterZichtbaar = false;
		boolean groteBalletjes = false;
		boolean schaduwZichtbaar = false;
		boolean afmetingenZichtbaar = false;
		boolean tekenGumOptie = false;
		boolean figuurTransparant = true;
		int gridSize = 20;		
	   	String rodeFiguurString =  "2,0|10,0|8,8|0,8";
		int oppervlakteRood = 64;    	
		String grijzeFiguurString = "0,0|8,0|8,8|0,8";
		int oppervlakteGrijs = 64;
		int scoreMax = 10;
		
		if (b.containsKey("appletLaunchData"))
		{
System.out.println("aLD found");
			Hashtable appletLaunchData = (Hashtable) b.get("appletLaunchData");
			String balkOnderaanString = "true";
			String roosterZichtbaarString = "false";
			String groteBalletjesString = "false";
			String schaduwZichtbaarString = "false";
			String afmetingenZichtbaarString = "false";
			
			String grid1String = "";
			String figuur1String = "";
			String oppervlakte1String = "";
			String figuurgrijs1String = "";
			String oppervlaktegrijs1String = "";
			String scoreMaxString = "";

			boolean error;
			
		
			if (appletLaunchData.containsKey("taaknummer"))
				taakNummer = Integer.parseInt((String) appletLaunchData.get("taaknummer"));
			if (appletLaunchData.containsKey("toonbalk"))
				balkOnderaanString = (String) appletLaunchData.get("toonbalk");
			if (balkOnderaanString.equals("false") || balkOnderaanString.equals("no"))
				balkOnderaan = false;
			if (appletLaunchData.containsKey("toonrooster"))
				roosterZichtbaarString = (String) appletLaunchData.get("toonrooster");
			if (roosterZichtbaarString.equals("true") || roosterZichtbaarString.equals("yes"))
				roosterZichtbaar = true;
			if (appletLaunchData.containsKey("groteballetjes"))
				groteBalletjesString = (String) appletLaunchData.get("groteballetjes");
			if (groteBalletjesString.equals("true") || groteBalletjesString.equals("yes"))
				groteBalletjes = true;
			if (appletLaunchData.containsKey("toonschaduw"))
				schaduwZichtbaarString = (String) appletLaunchData.get("toonschaduw");
			if (schaduwZichtbaarString.equals("true") || schaduwZichtbaarString.equals("yes"))
				schaduwZichtbaar = true;
			
//System.out.println("schaduwZichtbaar = " + schaduwZichtbaar);			
			
			if (appletLaunchData.containsKey("toonafmetingen"))
				afmetingenZichtbaarString = (String) appletLaunchData.get("toonafmetingen");
			if (afmetingenZichtbaarString.equals("true") || afmetingenZichtbaarString.equals("yes"))
				afmetingenZichtbaar = true;
			if (appletLaunchData.containsKey("grid1"))
				gridSize = Integer.parseInt((String) appletLaunchData.get("grid1"));
			
//System.out.println("gridSize = " + gridSize);
			
			if ((gridSize < minGrid) || (gridSize > maxGrid))
				gridSize = 20;
			
			if (appletLaunchData.containsKey("figuur1"))
				rodeFiguurString = (String) appletLaunchData.get("figuur1");
			
//System.out.println("rodeFiguurString = " + rodeFiguurString);			
			
			if (appletLaunchData.containsKey("oppervlakte1"))
				oppervlakte1String = (String) appletLaunchData.get("oppervlakte1");
			int oppervlakte1 = 0;
			error = false;
			try
			{	oppervlakte1 = Integer.parseInt(oppervlakte1String);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			if (!error)
				oppervlakteRood = oppervlakte1;
			
			if (appletLaunchData.containsKey("figuurgrijs1"))
				grijzeFiguurString = (String) appletLaunchData.get("figuurgrijs1");
			
			if (appletLaunchData.containsKey("oppervlaktegrijs1"))
				oppervlaktegrijs1String = (String) appletLaunchData.get("oppervlaktegrijs1");
			int oppervlaktegrijs1 = 0;
			error = false;
			try
			{	oppervlaktegrijs1 = Integer.parseInt(oppervlaktegrijs1String);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			if (!error)
				oppervlakteGrijs = oppervlaktegrijs1;
			
			if (appletLaunchData.containsKey("scoreMax"))
				scoreMaxString = (String) appletLaunchData.get("scoreMax");
			int scMax = 0;
			error = false;
			try
			{	scMax = Integer.parseInt(scoreMaxString);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			if (!error)
				scoreMax = scMax;
			

		}
		else
		{

			if (b.containsKey("taakNummer"))
				taakNummer = ((Integer) b.get("taakNummer")).intValue();
//System.out.println("get tn = " + taakNummer);			
			if (b.containsKey("balkOnderaan"))
				balkOnderaan = ((Boolean) b.get("balkOnderaan")).booleanValue();
			if (b.containsKey("roosterZichtbaar"))
				roosterZichtbaar = ((Boolean) b.get("roosterZichtbaar")).booleanValue();
			if (b.containsKey("groteBalletjes"))
				groteBalletjes = ((Boolean) b.get("groteBalletjes")).booleanValue();
			if (b.containsKey("schaduwZichtbaar"))
				schaduwZichtbaar = ((Boolean) b.get("schaduwZichtbaar")).booleanValue();
			if (b.containsKey("afmetingenZichtbaar"))
				afmetingenZichtbaar = ((Boolean) b.get("afmetingenZichtbaar")).booleanValue();
			if (b.containsKey("tekenGumOptie"))
				tekenGumOptie = ((Boolean) b.get("tekenGumOptie")).booleanValue();
			if (b.containsKey("figuurTransparant"))
				figuurTransparant = ((Boolean) b.get("figuurTransparant")).booleanValue();
			if (b.containsKey("gridSize"))
				gridSize = ((Integer) b.get("gridSize")).intValue();
			if (b.containsKey("rodeFiguurString"))
				rodeFiguurString = (String) b.get("rodeFiguurString");
			if (b.containsKey("oppervlakteRood"))
				oppervlakteRood = ((Integer) b.get("oppervlakteRood")).intValue();
			if (b.containsKey("grijzeFiguurString"))
				grijzeFiguurString = (String) b.get("grijzeFiguurString");
			if (b.containsKey("oppervlakteGrijs"))
				oppervlakteGrijs = ((Integer) b.get("oppervlakteGrijs")).intValue();
			if (b.containsKey("scoreMax"))
				scoreMax = ((Integer) b.get("scoreMax")).intValue();
		
			
		}
		zetBalkOnderaan(balkOnderaan);
		zetRoosterZichtbaar(roosterZichtbaar);
		zetGroteBalletjes(groteBalletjes);
		//zetSchaduwZichtbaar(schaduwZichtbaar);
		this.schaduwZichtbaar = schaduwZichtbaar;
		zetAfmetingenZichtbaar(afmetingenZichtbaar);
		zetTekenGumOptie(tekenGumOptie);
		zetGridSize(gridSize);
		this.rodeFiguurString = rodeFiguurString; 
		zetOppervlakteRood(oppervlakteRood);
		zetGrijzeFiguur(grijzeFiguurString);
		zetOppervlakteGrijs(oppervlakteGrijs);
		
		if (b.containsKey("appletEditState"))
		{
System.out.println("aES found");
			String appletEditState = (String) b.get("appletEditState");
			
			// decodeer de string
			Object o = StringCodeObject.decodeStringToObject(appletEditState);
			// cast
			Vector scormOpdrachten = (Vector) o;
		    // herstel de state van het applet
		    //for (int oCnt = 0; oCnt < aantalFiguren; oCnt++)
		    //{	
		    	ScormOpdracht scoOpdracht = 
		    		(ScormOpdracht) scormOpdrachten.elementAt(0);
		    	//if (scoOpdracht.isCurrent)	
		    	//	currentNum = oCnt;
		    	antwoord = scoOpdracht.antwoord % 100;
	//System.out.println("a = " + opdrachten[oCnt].antwoord);	    	
		    	antwoordOK = scoOpdracht.antwoordOK;	    
		    	antwoordenFout = scoOpdracht.antwoord / 100;
	//System.out.println("afout = " + opdrachten[oCnt].antwoordenFout);	    	
		    	drawingPanel2.knipPolygons.removeAllElements();
				for (int pCnt = 0; pCnt < scoOpdracht.figuurPolygons.size(); pCnt++)
		    	{	ScormPolygon sp = 
		    			(ScormPolygon) scoOpdracht.figuurPolygons.elementAt(pCnt);
		    		KnipPolygon2 kp = new KnipPolygon2(sp.realPoints, drawingPanel2);
		    		
		    		kp.oppervlakte = sp.oppervlakte;
		    		
		    		drawingPanel2.knipPolygons.addElement(kp);
		    		
		    		if ((taakNummer == 2) || (taakNummer == 3))
		    			kp.setLabelPoint();
		    		
		    		
		    	}	    	
		    		

			zetAntwoorden();

		}
			
		else
		{	ScormOpdracht scormOpdracht = null;
		
			if (b.containsKey("scormOpdracht"))			
				scormOpdracht = (ScormOpdracht) b.get("scormOpdracht");
			
			if (scormOpdracht == null)
				return;
			
	    	antwoord = scormOpdracht.antwoord % 100;
	    	//System.out.println("a = " + opdrachten[oCnt].antwoord);	    	
	    	antwoordOK = scormOpdracht.antwoordOK;	    
	    	antwoordenFout = scormOpdracht.antwoord / 100;
	    	//System.out.println("afout = " + opdrachten[oCnt].antwoordenFout);	    	
	    	drawingPanel2.knipPolygons.removeAllElements();
			for (int pCnt = 0; pCnt < scormOpdracht.figuurPolygons.size(); pCnt++)
	    	{	ScormPolygon sp = 
    			(ScormPolygon) scormOpdracht.figuurPolygons.elementAt(pCnt);
	    		KnipPolygon2 kp = new KnipPolygon2(sp.realPoints, drawingPanel2);
	    		    		
	    		kp.oppervlakte = sp.oppervlakte;
	    		    		
	    		drawingPanel2.knipPolygons.addElement(kp);
	    		    		
	    		if ((taakNummer == 2) || (taakNummer == 3))
	    			kp.setLabelPoint();
	    		    		
	    		    		
	    	}	    	
	    	zetAntwoorden();
	    	zetFiguurTransparant(figuurTransparant);
	    	
	    	Vector rectangles = new Vector();
			if (b.containsKey("rechthoeken"))			
				rectangles = (Vector) b.get("rechthoeken");
			drawingPanel2.rectangles = rectangles;
	    	
		}

		// HIER
		zetTaakNummer(taakNummer, false);
		
	}
	
	public void setState(Hashtable b)
	{
		
System.out.println("vip setState");

		if (b.containsKey("appletState"))
		{
System.out.println("aS found");
			String appletState = (String) b.get("appletState");
			
			// decodeer de string
			Object o = StringCodeObject.decodeStringToObject(appletState);
			// cast
			Vector scormOpdrachten = (Vector) o;
		    // herstel de state van het applet
		    //for (int oCnt = 0; oCnt < aantalFiguren; oCnt++)
		    //{	
		    	ScormOpdracht scoOpdracht = 
		    		(ScormOpdracht) scormOpdrachten.elementAt(0);
		    	//if (scoOpdracht.isCurrent)	
		    	//	currentNum = oCnt;
		    	antwoord = scoOpdracht.antwoord % 100;
	//System.out.println("a = " + opdrachten[oCnt].antwoord);	    	
		    	antwoordOK = scoOpdracht.antwoordOK;	    
		    	antwoordenFout = scoOpdracht.antwoord / 100;
	//System.out.println("afout = " + opdrachten[oCnt].antwoordenFout);	    	
		    	drawingPanel2.knipPolygons.removeAllElements();
				for (int pCnt = 0; pCnt < scoOpdracht.figuurPolygons.size(); pCnt++)
		    	{	ScormPolygon sp = 
		    			(ScormPolygon) scoOpdracht.figuurPolygons.elementAt(pCnt);
		    		KnipPolygon2 kp = new KnipPolygon2(sp.realPoints, drawingPanel2);
		    		
		    		kp.oppervlakte = sp.oppervlakte;
		    		
		    		drawingPanel2.knipPolygons.addElement(kp);
		    		
		    		if ((taakNummer == 2) || (taakNummer == 3))
		    			kp.setLabelPoint();
		    		
		    		
		    	}	    	
		    		

			zetAntwoorden();

		}
			
		else
		{	ScormOpdracht scormOpdracht = null;
		
			if (b.containsKey("scormOpdracht"))			
				scormOpdracht = (ScormOpdracht) b.get("scormOpdracht");
			
			if (scormOpdracht == null)
				return;
			
	    	antwoord = scormOpdracht.antwoord % 100;
	    	//System.out.println("a = " + opdrachten[oCnt].antwoord);	    	
	    	antwoordOK = scormOpdracht.antwoordOK;	    
	    	antwoordenFout = scormOpdracht.antwoord / 100;
	    	
//System.out.println("antwoord = " + antwoord);	    	
	    	
	    	drawingPanel2.knipPolygons.removeAllElements();
			for (int pCnt = 0; pCnt < scormOpdracht.figuurPolygons.size(); pCnt++)
	    	{	ScormPolygon sp = 
    			(ScormPolygon) scormOpdracht.figuurPolygons.elementAt(pCnt);
	    		KnipPolygon2 kp = new KnipPolygon2(sp.realPoints, drawingPanel2);
	    		    		
	    		kp.oppervlakte = sp.oppervlakte;
	    		    		
	    		drawingPanel2.knipPolygons.addElement(kp);
	    		    		
	    		if ((taakNummer == 2) || (taakNummer == 3))
	    			kp.setLabelPoint();
	    		    		
	    		    		
	    	}	    	
	    	zetAntwoorden();
	    	
	    	Vector rectangles = new Vector();
			if (b.containsKey("rechthoeken"))			
				rectangles = (Vector) b.get("rechthoeken");
			drawingPanel2.rectangles = rectangles;
 
		}
	}

    public void zetAntwoorden()
    {   
        if (taakNummer == 1)
        {
	        //if (currentOpdracht.drawingPanel.figureIsRectangle)
	        if (antwoordOK)
	        {	bottomPanel2.opdrachtLabel.setText(Verknippen.rb.getString("rechthoekTekst"));
    	    }
	        else
    	    {	bottomPanel2.opdrachtLabel.setText(Verknippen.rb.getString("maakRechthoekTekst"));
        	}
        }
        else if ((taakNummer == 2) || (taakNummer == 3))
        {
			// antwoord invullen        	
			if (antwoord > 0)
			{	bottomPanel2.oppervlakteTextField.setText("" + antwoord);
				
				if (antwoordOK)
				{	bottomPanel2.showGoed = true;
					bottomPanel2.showFout = false;
				}
				else
				{	bottomPanel2.showGoed = false;
					bottomPanel2.showFout = true;
				}
			}
			else // geen antwoord gegeven
			{	bottomPanel2.oppervlakteTextField.setText("");
				bottomPanel2.showGoed = false;
				bottomPanel2.showFout = false;
			}
		}		
        else if (taakNummer == 4)
        {	
        	
//System.out.println("zetAntwoorden tn4 = " + antwoord);      
//System.out.println("zetAntwoorden tn4 = " + antwoordOK);
//System.out.println("zetAntwoorden tn4 = " + antwoordenFout);

        	if (antwoord > 0)
        	{	
        		bottomPanel2.vergelijkEnabled = false;
        		bottomPanel2.vergelijkChoice.setSelectedIndex(antwoord - 1);
        		bottomPanel2.vergelijkEnabled = true;
        
				if (antwoordOK && (antwoordenFout == 0))
				{	bottomPanel2.showGoed = true;
					bottomPanel2.showFout = false;
					bottomPanel2.showHalfGoed = false;
				}
				else if (antwoordOK && (antwoordenFout > 0))
				{	bottomPanel2.showGoed = false;
					bottomPanel2.showFout = false;
					bottomPanel2.showHalfGoed = true;
				}
				else
				{	bottomPanel2.showGoed = false;
					bottomPanel2.showFout = true;
					bottomPanel2.showHalfGoed = false;
				}
        	}
        	else // geen antwoord gegeven
        	{	bottomPanel2.vergelijkEnabled = false;
        		bottomPanel2.vergelijkChoice.setSelectedIndex(0);
        		bottomPanel2.vergelijkEnabled = true;
				bottomPanel2.showGoed = false;
				bottomPanel2.showFout = false;
				bottomPanel2.showHalfGoed = false;
				        		
        	}
        }
        
        bottomPanel2.repaint();
        
        kijkNa();
    }
	
	public void setEditState(Hashtable b)
	{
System.out.println("vip setEditState");

		int taakNummer = 1;
		boolean balkOnderaan = true;
		boolean roosterZichtbaar = false;
		boolean groteBalletjes = false;
		boolean schaduwZichtbaar = false;
		boolean afmetingenZichtbaar = false;
		boolean tekenGumOptie = false;
		boolean figuurTransparant = true;
		int gridSize = 20;		
	   	String rodeFiguurString =  "2,0|10,0|8,8|0,8";
		int oppervlakteRood = 64;    	
		String grijzeFiguurString = "0,0|8,0|8,8|0,8";
		int oppervlakteGrijs = 64;
		int scoreMax = 10;

		if (b.containsKey("appletLaunchData"))
		{
System.out.println("aLD found");
			Hashtable appletLaunchData = (Hashtable) b.get("appletLaunchData");
			String balkOnderaanString = "true";
			String roosterZichtbaarString = "false";
			String groteBalletjesString = "false";
			String schaduwZichtbaarString = "false";
			String afmetingenZichtbaarString = "false";
			
			String grid1String = "";
			String figuur1String = "";
			String oppervlakte1String = "";
			String figuurgrijs1String = "";
			String oppervlaktegrijs1String = "";
			String scoreMaxString = "";

			boolean error;
			
		
			if (appletLaunchData.containsKey("taaknummer"))
				taakNummer = Integer.parseInt((String) appletLaunchData.get("taaknummer"));
			if (appletLaunchData.containsKey("toonbalk"))
				balkOnderaanString = (String) appletLaunchData.get("toonbalk");
			if (balkOnderaanString.equals("false") || balkOnderaanString.equals("no"))
				balkOnderaan = false;
			if (appletLaunchData.containsKey("toonrooster"))
				roosterZichtbaarString = (String) appletLaunchData.get("toonrooster");
			if (roosterZichtbaarString.equals("true") || roosterZichtbaarString.equals("yes"))
				roosterZichtbaar = true;
			if (appletLaunchData.containsKey("groteballetjes"))
				groteBalletjesString = (String) appletLaunchData.get("groteballetjes");
			if (groteBalletjesString.equals("true") || groteBalletjesString.equals("yes"))
				groteBalletjes = true;
			if (appletLaunchData.containsKey("toonschaduw"))
				schaduwZichtbaarString = (String) appletLaunchData.get("toonschaduw");
			if (schaduwZichtbaarString.equals("true") || schaduwZichtbaarString.equals("yes"))
				schaduwZichtbaar = true;
			if (appletLaunchData.containsKey("toonafmetingen"))
				afmetingenZichtbaarString = (String) appletLaunchData.get("toonafmetingen");
			if (afmetingenZichtbaarString.equals("true") || afmetingenZichtbaarString.equals("yes"))
				afmetingenZichtbaar = true;
			if (appletLaunchData.containsKey("grid1"))
				gridSize = Integer.parseInt((String) appletLaunchData.get("grid1"));
			
//System.out.println("gridSize = " + gridSize);			
			
			if ((gridSize < minGrid) || (gridSize > maxGrid))
				gridSize = 20;
			
			if (appletLaunchData.containsKey("figuur1"))
				rodeFiguurString = (String) appletLaunchData.get("figuur1");
			
//System.out.println("rodeFiguurString = " + rodeFiguurString);			
			
			if (appletLaunchData.containsKey("oppervlakte1"))
				oppervlakte1String = (String) appletLaunchData.get("oppervlakte1");
			int oppervlakte1 = 0;
			error = false;
			try
			{	oppervlakte1 = Integer.parseInt(oppervlakte1String);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			if (!error)
				oppervlakteRood = oppervlakte1;
			
			if (appletLaunchData.containsKey("figuurgrijs1"))
				grijzeFiguurString = (String) appletLaunchData.get("figuurgrijs1");
			
			if (appletLaunchData.containsKey("oppervlaktegrijs1"))
				oppervlaktegrijs1String = (String) appletLaunchData.get("oppervlaktegrijs1");
			int oppervlaktegrijs1 = 0;
			error = false;
			try
			{	oppervlaktegrijs1 = Integer.parseInt(oppervlaktegrijs1String);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			if (!error)
				oppervlakteGrijs = oppervlaktegrijs1;
			
			if (appletLaunchData.containsKey("scoreMax"))
				scoreMaxString = (String) appletLaunchData.get("scoreMax");
			int scMax = 0;
			error = false;
			try
			{	scMax = Integer.parseInt(scoreMaxString);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			if (!error)
				scoreMax = scMax;
			
			
			

		}
		else
		{

			if (b.containsKey("taakNummer"))
				taakNummer = ((Integer) b.get("taakNummer")).intValue();
//System.out.println("get tn = " + taakNummer);			
			if (b.containsKey("balkOnderaan"))
				balkOnderaan = ((Boolean) b.get("balkOnderaan")).booleanValue();
			if (b.containsKey("roosterZichtbaar"))
				roosterZichtbaar = ((Boolean) b.get("roosterZichtbaar")).booleanValue();
			if (b.containsKey("groteBalletjes"))
				groteBalletjes = ((Boolean) b.get("groteBalletjes")).booleanValue();
			if (b.containsKey("schaduwZichtbaar"))
				schaduwZichtbaar = ((Boolean) b.get("schaduwZichtbaar")).booleanValue();
			if (b.containsKey("afmetingenZichtbaar"))
				afmetingenZichtbaar = ((Boolean) b.get("afmetingenZichtbaar")).booleanValue();
			if (b.containsKey("tekenGumOptie"))
				tekenGumOptie = ((Boolean) b.get("tekenGumOptie")).booleanValue();
			if (b.containsKey("figuurTransparant"))
				figuurTransparant = ((Boolean) b.get("figuurTransparant")).booleanValue();
			if (b.containsKey("gridSize"))
				gridSize = ((Integer) b.get("gridSize")).intValue();
			if (b.containsKey("rodeFiguurString"))
				rodeFiguurString = (String) b.get("rodeFiguurString");
			if (b.containsKey("oppervlakteRood"))
				oppervlakteRood = ((Integer) b.get("oppervlakteRood")).intValue();
			if (b.containsKey("grijzeFiguurString"))
				grijzeFiguurString = (String) b.get("grijzeFiguurString");
			if (b.containsKey("oppervlakteGrijs"))
				oppervlakteGrijs = ((Integer) b.get("oppervlakteGrijs")).intValue();
			if (b.containsKey("scoreMax"))
				scoreMax = ((Integer) b.get("scoreMax")).intValue();
		
			
		}
		zetBalkOnderaan(balkOnderaan);
		zetRoosterZichtbaar(roosterZichtbaar);
		zetGroteBalletjes(groteBalletjes);
		//zetSchaduwZichtbaar(schaduwZichtbaar);
		this.schaduwZichtbaar = schaduwZichtbaar; 
		zetAfmetingenZichtbaar(afmetingenZichtbaar);
		zetTekenGumOptie(tekenGumOptie);
		zetGridSize(gridSize);
		this.rodeFiguurString = rodeFiguurString;
		zetOppervlakteRood(oppervlakteRood);
		zetGrijzeFiguur(grijzeFiguurString);
		zetOppervlakteGrijs(oppervlakteGrijs);

//HIER state
		if (b.containsKey("appletEditState"))
		{
System.out.println("aES found");
			String appletEditState = (String) b.get("appletEditState");
			
			// decodeer de string
			Object o = StringCodeObject.decodeStringToObject(appletEditState);
			// cast
			Vector scormOpdrachten = (Vector) o;
		    // herstel de state van het applet
		    //for (int oCnt = 0; oCnt < aantalFiguren; oCnt++)
		    //{	
		    	ScormOpdracht scoOpdracht = 
		    		(ScormOpdracht) scormOpdrachten.elementAt(0);
		    	//if (scoOpdracht.isCurrent)	
		    	//	currentNum = oCnt;
		    	antwoord = scoOpdracht.antwoord % 100;
	//System.out.println("a = " + opdrachten[oCnt].antwoord);	    	
		    	antwoordOK = scoOpdracht.antwoordOK;	    
		    	antwoordenFout = scoOpdracht.antwoord / 100;
	//System.out.println("afout = " + opdrachten[oCnt].antwoordenFout);	    	
		    	drawingPanel2.knipPolygons.removeAllElements();
				for (int pCnt = 0; pCnt < scoOpdracht.figuurPolygons.size(); pCnt++)
		    	{	ScormPolygon sp = 
		    			(ScormPolygon) scoOpdracht.figuurPolygons.elementAt(pCnt);
		    		KnipPolygon2 kp = new KnipPolygon2(sp.realPoints, drawingPanel2);
		    		
		    		kp.oppervlakte = sp.oppervlakte;
		    		
		    		drawingPanel2.knipPolygons.addElement(kp);
		    		
		    		if ((taakNummer == 2) || (taakNummer == 3))
		    			kp.setLabelPoint();
		    		
		    		
		    	}	    	
		    		

			zetAntwoorden();

		}
			
		else
		{	ScormOpdracht scormOpdracht = null;
		
			if (b.containsKey("scormOpdracht"))			
				scormOpdracht = (ScormOpdracht) b.get("scormOpdracht");
			
			if (scormOpdracht == null)
				return;
			
	    	antwoord = scormOpdracht.antwoord % 100;
	    	//System.out.println("a = " + opdrachten[oCnt].antwoord);	    	
	    	antwoordOK = scormOpdracht.antwoordOK;	    
	    	antwoordenFout = scormOpdracht.antwoord / 100;
	    	//System.out.println("afout = " + opdrachten[oCnt].antwoordenFout);	    	
	    	drawingPanel2.knipPolygons.removeAllElements();
			for (int pCnt = 0; pCnt < scormOpdracht.figuurPolygons.size(); pCnt++)
	    	{	ScormPolygon sp = 
    			(ScormPolygon) scormOpdracht.figuurPolygons.elementAt(pCnt);
	    		KnipPolygon2 kp = new KnipPolygon2(sp.realPoints, drawingPanel2);
	    		    		
	    		kp.oppervlakte = sp.oppervlakte;
	    		    		
	    		drawingPanel2.knipPolygons.addElement(kp);
	    		    		
	    		if ((taakNummer == 2) || (taakNummer == 3))
	    			kp.setLabelPoint();
	    		    		
	    		    		
	    	}	    	
	    	zetAntwoorden();
	    	zetFiguurTransparant(figuurTransparant);
	    	
	    	Vector rectangles = new Vector();
			if (b.containsKey("rechthoeken"))			
			{	rectangles = (Vector) b.get("rechthoeken");
System.out.println("rects = " + rectangles.size());			
			}
			drawingPanel2.rectangles = rectangles;
	    	
		}
		
		
		// HIER
		zetTaakNummer(taakNummer, false);
		
		
	}
	
	public Hashtable getState()
	{
System.out.println("vip getState");		
		
		Hashtable h = new Hashtable();

//System.out.println("antwoord = " + antwoord);		
		
		ScormOpdracht scormOpdracht = new ScormOpdracht(0);
		scormOpdracht.antwoord = 100 * antwoordenFout + antwoord;
		scormOpdracht.antwoordOK = antwoordOK;
    	for (int pCnt = 0; pCnt < drawingPanel2.knipPolygons.size(); pCnt++)
    	{	KnipPolygon2 kp = (KnipPolygon2) drawingPanel2.knipPolygons.elementAt(pCnt);
    		ScormPolygon sp = new ScormPolygon(kp.realPoints);
    		sp.oppervlakte = kp.oppervlakte;
    		scormOpdracht.figuurPolygons.addElement(sp);
    	}		
		
    	h.put("scormOpdracht", scormOpdracht);
    	
    	h.put("rechthoeken", drawingPanel2.rectangles);
    	
		return h;
		
	}
	
	public Hashtable getEditState()
	{
		
System.out.println("vip getEditState");	
		Hashtable h = new Hashtable();
		
		h.put("taakNummer", new Integer(taakNummer));
//System.out.println("put tn = " + taakNummer);		
		h.put("balkOnderaan", new Boolean(balkOnderaan));
		h.put("roosterZichtbaar", new Boolean(roosterZichtbaar));
		h.put("groteBalletjes", new Boolean(groteBalletjes));
		h.put("schaduwZichtbaar", new Boolean(schaduwZichtbaar));
		h.put("afmetingenZichtbaar", new Boolean(afmetingenZichtbaar));
		h.put("tekenGumOptie", new Boolean(tekenGumOptie));
		h.put("figuurTransparant", new Boolean(figuurTransparant));
		h.put("gridSize", new Integer(gridSize));
		h.put("rodeFiguurString", rodeFiguurString);
		h.put("oppervlakteRood", new Integer(oppervlakteRood));
		h.put("grijzeFiguurString", grijzeFiguurString);
		h.put("oppervlakteGrijs", new Integer(oppervlakteGrijs));

		ScormOpdracht scormOpdracht = new ScormOpdracht(0);
    	scormOpdracht.antwoord = 100 * antwoordenFout + antwoord;
    	scormOpdracht.antwoordOK = antwoordOK;
    	for (int pCnt = 0; pCnt < drawingPanel2.knipPolygons.size(); pCnt++)
    	{	KnipPolygon2 kp = (KnipPolygon2) drawingPanel2.knipPolygons.elementAt(pCnt);
    		ScormPolygon sp = new ScormPolygon(kp.realPoints);
    		sp.oppervlakte = kp.oppervlakte;
    		scormOpdracht.figuurPolygons.addElement(sp);
    	}		
		
    	h.put("scormOpdracht", scormOpdracht);
    	
    	h.put("rechthoeken", drawingPanel2.rectangles);    	
//System.out.println("put rects = " + drawingPanel2.rectangles.size());    	
		
		return h;
	}
	
	
	public InteractieEditPanel getEditPanel()
	{
		return new VerknippenInteractieEditPanel();
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		
//System.out.println("vip set bounds " + b + " " + h);
		
		if (h == 1)
			return;
		
		if ((getLocation().x == x) && (getLocation().y == y) && 
			(getSize().width == b) && (getSize().height == h))
			return;
		super.setBounds(x, y, b, h);
		
		
		if (bottomPanel2 == null) 
		{	bottomPanel2 = new BottomPanel2(this, getSize().width, bottomHeight);
			bottomPanel2.setLocation(0, getSize().height - bottomHeight);
			add(bottomPanel2);
			drawingPanel2 = new DrawingPanel2(this, groteBalletjes);
			drawingPanel2.setBounds(0, 0, getSize().width, getSize().height - bottomHeight);
			// zet de defaultfiguur	
			KnipPolygon2 kp = new KnipPolygon2(drawingPanel2, rodeFiguurCoordinaten, KnipPolygon2.CENTER);
			drawingPanel2.addKnipPolygon(kp);
			add(drawingPanel2);

//System.out.println("comps created");			
		}
		else
		{	
			if (balkOnderaan)
			{	bottomPanel2.setVisible(true);
				bottomPanel2.setSize(getSize().width, bottomHeight);
				drawingPanel2.setSize(getSize().width, getSize().height - bottomHeight);
				zetGridSize(gridSize);
			}
			else
			{	bottomPanel2.setVisible(false);
				bottomPanel2.setSize(b, h);
				drawingPanel2.setSize(getSize().width, getSize().height);
				zetGridSize(gridSize);
			}
				

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
	{	if (!kijkNaActief)
			return true;
		return 
			score == scoreMax;
	}
	
	public boolean isFout()
	{	if (!kijkNaActief)
			return false;
		return score == 0;
	}
	
	public void zetMode(int mode)
    {   this.mode = mode;
    	kijkNaActief = mode == 0 || mode == 1;
    }
	
	public void zetNagekeken(boolean b)
	{	if (ingevuld) 
		nagekeken = b;
	}

	
    public void stop()
    {}
    
    public void start()
    {}
    
    public void destroy()
    {}
    
    public void opnieuw()
    {
    	opnieuwAction();
    	score = 0;
    	produceAction("changed");
    }	
    
    public void kijkNa()
    {
    	if (!balkOnderaan && (taakNummer != 1))
    		return;
//System.out.println("kijkNa() - 1");
    	
    	if (taakNummer == 1)
    	{	
    		if (drawingPanel2 != null)
    		{    
    			boolean ok = drawingPanel2.figureIsRectangle;
    			if (ok)
    				score = scoreMax;
    			else 
    				score = 0;
    		}	
         
//System.out.println("kijkNa() - 2");         
        } 
		else if ( (taakNummer == 2) || (taakNummer == 3)) 
		{
			boolean ok = antwoordOK;
			if (ok)
				score = scoreMax;
			else 
				score = 0;
			ingevuld = antwoord > 0;
			
		}
		else if (taakNummer == 4)
		{
			boolean ok = antwoordOK;
			int fouten = antwoordenFout;
			if (ok && (fouten == 0))
				score = scoreMax;
			else if (ok & (fouten > 0))
				score = scoreMax / 2;
			else
				score = 0;
			ingevuld = antwoord > 0;
		}
    	
    	
	
	}
    	
    
    public void kijkNa(int stapNr)
    {	kijkNa();
    }
    
	public void zetBreedte(int b)
	{}
	
	public void zetHoogte(int h)
	{}
    
	public void actionPerformed(ActionEvent e)
	{}
	
	//ActionProducer
	private ActionListener actionListener = null;
	
	public void addActionListener(ActionListener al) 
 	{	actionListener = AWTEventMulticaster.add(actionListener, al);
 	}
 	
 	public void removeActionListener(ActionListener al)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, al);
 	}	
 	
 	public void produceAction(String command)
 	{	if (actionListener != null)
 		{	kijkNa();
 	        actionListener.actionPerformed(new ActionEvent(this, 0, command));
 		    
 		}
 	}
 	//end ActionProducer
	
}
