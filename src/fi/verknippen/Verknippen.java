package fi.verknippen;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;

import javax.swing.*;

import fi.beans.appletutil.AppletUtil;
import fi.beans.copyright.*;
import fi.beans.scorm.*;
import fi.beans.base64code.*;

import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;

public class Verknippen extends JApplet implements ScormAppletIF , WiskOpdrParamEditApplet, WiskOpdrApplet
{
	// scormgebeuren
	protected static ResourceBundle rb;
	protected static String langArg;
	protected static Color bgColor = new Color(255, 255, 198);
	
	protected SCORM12APIInterface api;
	boolean scormed = false;
	boolean reviewMode = false;	
	
	// DWO-component-gebeuren
	boolean isDWOComponent = false;
	
	//InteractiePanelAdapter ipa = null;
	
	public static void main(String[] args)    
	{	int width = 790;
        int height = 485;
        Verknippen verknippen = new Verknippen();
        verknippen.scormed = true;
		ScormMainFrame mf = new ScormMainFrame(verknippen, width, height);
		mf.setTitle("Verknippen Scormed");
		mf.pack();
		mf.show();

		verknippen.setLocation(mf.getInsets().left, mf.getInsets().top);		
		
		int framebreedte = width + mf.getInsets().left + mf.getInsets().right;
		int framehoogte = height + mf.getInsets().top + mf.getInsets().bottom;
		mf.setSize(framebreedte, framehoogte);

	}
	
	// andere kleuren in DrawingPanel	
	
	DrawingPanel drawingPanel;
	BottomPanel bottomPanel;
	int bottomHeight = 55;
	int offSet = 10;
	
	int maxTaken = 4;
	int taakNummer = 1;
	// maximum aantal figuren binnen een taak
	int maxFiguren = 10;
	int aantalFiguren = 1;
	String defaultFiguurString = "2,0|10,0|8,8|0,8";
	String defaultGrijsFiguurString = "2,0|10,0|8,8|0,8";	
	
	
	FIButton fiButton;
	
	OpdrachtSelector selector;
	Opdracht[] opdrachten;
	Opdracht currentOpdracht;
	int currentNum;
	
	JLabel opdrachtLabel, opdrachtLabel2;
	Button opnieuwButton;
	TextField oppervlakteTextField;
	Button okButton;
		
	Choice vergelijkChoice;
	
	int minGrid = 16;
	int maxGrid = 50;
	// default gridSize wordt gezet in elke opdracht
	//int gridSize = 20;

	// parametrisatie
	boolean showGrid = false;
	boolean showShadow = false;
	boolean showSizes = false;
	boolean largeOvals = false;
	
	boolean showBottomPanel = true;
	int scoreMax = 10;
	
	// fonts
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;

	static Color buttonColor = Color.yellow;

	AppletUtil au;	
	Image foutKruis, goedVink;

	public Verknippen()
	{	
		langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.verknippen.text.Text", language);	
	}
	
	public Verknippen(Locale language)
	{	
		langArg = language.getLanguage();
		rb = ResourceBundle.getBundle("fi.verknippen.text.Text", language);	
	}
	
	public void init() 
	{	try
		{	api = Scorm.findAPI(this);
		}
		catch(Exception e){}
		
		getContentPane().setLayout(null);

		au = new AppletUtil(this);
		
		MediaTracker tr = new MediaTracker(this);		
		foutKruis = au.getImage("resources/foutkruis.gif");
		tr.addImage(foutKruis, 0);
		goedVink = au.getImage("resources/goedkrul_en.gif");
		tr.addImage(goedVink, 1);		
	    try
	    {	tr.waitForAll();
	    } 
	    catch(Exception e) {}		

		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
	
		//instelling taal
		String langArg = getParameter("language");
		if (langArg == null) 
			langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.verknippen.text.Text",language);
		
		//instelling achtergrondkleur
		bgColor = new Color(255, 255, 198);
		String kleurcode = getParameter("bgcolor");
		if	(kleurcode != null)
			bgColor = new Color(Integer.parseInt(kleurcode.substring(1), 16));
		//setBackground(bgColor);
		getContentPane().setBackground(bgColor);
		
		// taakNummer
		String taakString = getParameter("taaknummer");
		if	((taakString != null) && !taakString.equals(""))
		{	int taakNum = 0;
			boolean error = false;
			try
			{	taakNum = Integer.parseInt(taakString);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}	
			if (!error && (taakNum >= 0) && (taakNum <= maxTaken))
			{	taakNummer = taakNum;
			}
		}		

		
		// bottomPanel tonen?
		String showBottomPanelString = getParameter("toonbalk");
		if ((showBottomPanelString != null) && (showBottomPanelString.equals("no") || showBottomPanelString.equals("false")))
			showBottomPanel = false;
		
		if (!showBottomPanel)
			bottomHeight = 0;
		
		// rooster tonen?
		String showGridString = getParameter("toonrooster");
		if ((showGridString != null) && (showGridString.equals("yes") || showGridString.equals("true")))
			showGrid = true;

		// schaduw tonen?
		String showShadowString = getParameter("toonschaduw");
		if ((showShadowString != null) && (showShadowString.equals("yes") || showShadowString.equals("true")))
			showShadow = true;
	
		// afmetingen tonen? alleen als schaduw ook getoond wordt
		if (showShadow)
		{	String showSizesString = getParameter("toonafmetingen");
			if ((showSizesString != null) && (showSizesString.equals("yes") || showSizesString.equals("true")))
				showSizes = true;
		}

		// grotere balletjes
		String largeOvalsString = getParameter("groteballetjes");
		if ((largeOvalsString != null) && (largeOvalsString.equals("yes") || largeOvalsString.equals("true")))
			largeOvals = true;
		
		// scoreMax
		String scoreMaxString = getParameter("scoremax");
		if	((scoreMaxString != null) && !scoreMaxString.equals(""))
		{	int scoreM = 0;
			boolean error = false;
			try
			{	scoreM = Integer.parseInt(scoreMaxString);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}	
			if (!error && (scoreM >= 0))
			{	scoreMax = scoreM;
			}
		}		
		

if (scormed)
{	bgColor = new Color(Integer.parseInt("FFFFC6", 16));
	taakNummer = 4;
	showGrid = true;
	largeOvals = true;
}
	
		// aantal figuren
		String aantalString = getParameter("aantalfiguren");
		if	((aantalString != null) && !aantalString.equals(""))
		{	int num = 0;
			boolean error = false;
			try
			{	num = Integer.parseInt(aantalString);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}	
			if (!error && (num > 0) && (num <= maxFiguren))
			{	aantalFiguren = num;
			}
		}		
		// default 1 figuur
		
if (scormed)
{	aantalFiguren = 2;
}		
		
		opdrachten = new Opdracht[aantalFiguren];
		for (int oCnt = 0; oCnt < aantalFiguren; oCnt++)
		{	
			// opdrachtnummer
			opdrachten[oCnt] = new Opdracht(oCnt + 1);
			
			// lees de rode figuur voor deze opdracht			
			String figString = getParameter("figuur" + opdrachten[oCnt].opdrachtNum);
			if ((figString != null) && !figString.equals(""))
			{	Vector figCoords = processFiguurString(figString);
				if (figCoords.size() > 0)
				{	opdrachten[oCnt].figuurCoordinaten = figCoords;
				}
				else
				{	figCoords = processFiguurString(defaultFiguurString);
					opdrachten[oCnt].figuurCoordinaten = figCoords;
				}
			}
			else 
			{	opdrachten[oCnt].figuurCoordinaten = 
					processFiguurString(defaultFiguurString);
			}
			
			// lees de grijze figuur voor deze opdracht			
			String grijsFigString = getParameter("figuurgrijs" + opdrachten[oCnt].opdrachtNum);
			if ((grijsFigString != null) && !grijsFigString.equals(""))
			{	Vector grijsFigCoords = processFiguurString(grijsFigString);
				if (grijsFigCoords.size() > 0)
				{	opdrachten[oCnt].grijsFiguurCoordinaten = grijsFigCoords;
				}
				else
				{	grijsFigCoords = processFiguurString(defaultGrijsFiguurString);
					opdrachten[oCnt].grijsFiguurCoordinaten = grijsFigCoords;
				}
			}
			else 
			{	opdrachten[oCnt].grijsFiguurCoordinaten = 
					processFiguurString(defaultGrijsFiguurString);
			}
			
			
			// lees gridSize voor deze opdracht
			String gridString = getParameter("grid" + opdrachten[oCnt].opdrachtNum);
			if ((gridString != null) && !gridString.equals(""))
			{	int gridNum = 0;
				boolean error = false;
				try
				{	gridNum = Integer.parseInt(gridString);
				}
				catch (NumberFormatException nfe)
				{	error = true;
				}	
				if (!error && (gridNum >= minGrid) && (gridNum <= maxGrid))
				{	opdrachten[oCnt].gridSize = gridNum;
				}	
			}	
			
			// lees oppervlakte van de rode figuur voor deze opdracht
			String oppervlakteString = getParameter(
    								   "oppervlakte" + opdrachten[oCnt].opdrachtNum);
			if ((oppervlakteString != null) && !oppervlakteString.equals(""))
			{	int oppervlakteNum = 0;
				boolean error = false;
				try
				{	oppervlakteNum = Integer.parseInt(oppervlakteString);
				}
				catch (NumberFormatException nfe)
				{	error = true;
				}	
				if (!error && (oppervlakteNum > 0))
				{	opdrachten[oCnt].oppervlakte = oppervlakteNum;
				}	
			}	

			// lees oppervlakte van de grijze figuur voor deze opdracht
			String oppervlakteGrijsString = getParameter(
    								   "oppervlaktegrijs" + opdrachten[oCnt].opdrachtNum);
			if ((oppervlakteGrijsString != null) && !oppervlakteGrijsString.equals(""))
			{	int oppervlakteGrijsNum = 0;
				boolean error = false;
				try
				{	oppervlakteGrijsNum = Integer.parseInt(oppervlakteGrijsString);
				}
				catch (NumberFormatException nfe)
				{	error = true;
				}	
				if (!error && (oppervlakteGrijsNum > 0))
				{	opdrachten[oCnt].oppervlakteGrijs = oppervlakteGrijsNum;
				}	
			}	

if (scormed)
{	if (oCnt == 0)
	{	opdrachten[oCnt].figuurCoordinaten = 
			processFiguurString("0,0|2,0|2,2|8,2|8,4|5,4|5,7|3,7|3,6|0,6");
		opdrachten[oCnt].grijsFiguurCoordinaten = 
			processFiguurString("3,0|8,0|8,2|7,2|7,4|5,4|5,5|3,5|3,6|0,6|0,2|3,2");	
		opdrachten[oCnt].gridSize = 26;
		opdrachten[oCnt].oppervlakte = 32;
		opdrachten[oCnt].oppervlakteGrijs = 32;	
	}
	if (oCnt == 1)
	{	opdrachten[oCnt].figuurCoordinaten = 
			processFiguurString("0,0|2,0|2,2|8,2|8,4|6,4|6,6|4,6|4,8|2,8|2,4|0,4");
		opdrachten[oCnt].grijsFiguurCoordinaten = 
			processFiguurString("0,0|3,0|3,1|5,1|5,0|8,0|8,2|6,2|6,6|2,6|2,2|0,2");	
		opdrachten[oCnt].gridSize = 24;	
		opdrachten[oCnt].oppervlakte = 32;
		opdrachten[oCnt].oppervlakteGrijs = 30;
	}
	
}
			
			// maak drawingPanel voor deze opdracht
			opdrachten[oCnt].drawingPanel = new DrawingPanel(this, largeOvals);
			opdrachten[oCnt].drawingPanel.setBounds(0, 0, getSize().width,
													getSize().height - bottomHeight);
			opdrachten[oCnt].drawingPanel.setVisible(false);										
			getContentPane().add(opdrachten[oCnt].drawingPanel);
			// rooster tonen?
			opdrachten[oCnt].drawingPanel.showGrid = showGrid;
			// zet de gridsize
			opdrachten[oCnt].drawingPanel.gridSize = opdrachten[oCnt].gridSize;
			
			if (taakNummer < 4)
			{
				// zet de figuur	
				KnipPolygon kp = new KnipPolygon(opdrachten[oCnt].drawingPanel, 
									 opdrachten[oCnt].figuurCoordinaten,
									 KnipPolygon.CENTER);
								 
				if ((taakNummer == 2) || (taakNummer == 3))
					kp.setLabelPoint();
								 
				opdrachten[oCnt].drawingPanel.addKnipPolygon(kp);
				// schaduw figuur
				if (showShadow)
				{	KnipPolygon sp = new KnipPolygon(opdrachten[oCnt].drawingPanel, 
									 	opdrachten[oCnt].figuurCoordinaten,
									 	KnipPolygon.CENTER);
					opdrachten[oCnt].drawingPanel.shadowPolygon = sp;				 	
					if (showSizes)
						opdrachten[oCnt].drawingPanel.showSizes = true;
				}
			}
			else // taakNummer==4
			{	// zet de rode figuur	
				KnipPolygon kp = new KnipPolygon(opdrachten[oCnt].drawingPanel, 
									 opdrachten[oCnt].figuurCoordinaten,
									 KnipPolygon.RIGHTAL);
									 
				opdrachten[oCnt].drawingPanel.addKnipPolygon(kp);									 
				// schaduw figuur
				if (showShadow)
				{	KnipPolygon sp = new KnipPolygon(opdrachten[oCnt].drawingPanel, 
									 	opdrachten[oCnt].figuurCoordinaten,
									 	KnipPolygon.RIGHTAL);
					opdrachten[oCnt].drawingPanel.shadowPolygon = sp;				 	
					if (showSizes)
						opdrachten[oCnt].drawingPanel.showSizes = true;
				}				
				
				// zet de grijze figuur
				KnipPolygon gp = new KnipPolygon(opdrachten[oCnt].drawingPanel, 
									 opdrachten[oCnt].grijsFiguurCoordinaten,
									 KnipPolygon.LEFTAL);
				
				opdrachten[oCnt].drawingPanel.grijsPolygon = gp;				 					
			}
			
		} // for	

		currentOpdracht = opdrachten[0];
		currentNum = 0;
		currentOpdracht.drawingPanel.setVisible(true);

		// bottomPanel		
		bottomPanel = new BottomPanel(this);
		bottomPanel.setBounds(0, getSize().height - bottomHeight, 
			getSize().width, bottomHeight);
		if (showBottomPanel)
			getContentPane().add(bottomPanel);

		//Fi-logo, copyright
		fiButton = new FIButton("Verknippen",new String[]
			{	"versie-info: 20110905",
				"auteurs: Monica Wijers, Frans van Galen",
				"programmeur: Huub Nilwik",
				"Freudenthal Instituut",
				"www.fi.uu.nl",
				""
			});
		fiButton.setBounds(getSize().width - offSet - 20, offSet, 20, 30);
		bottomPanel.add(fiButton);

//		opnieuwButton = new JButton(rb.getString("opnieuwTekst"));
		opnieuwButton = new Button(rb.getString("opnieuwTekst"));
		opnieuwButton.setBackground(buttonColor);
		opnieuwButton.setFont(theFont);
		int width = theFM.stringWidth(rb.getString("opnieuwTekst")) + 35;

		if (taakNummer == 1)
			opnieuwButton.setBounds(
				fiButton.getLocation().x - 2 * offSet - width,
				offSet,
				width, 3 * theFM.getHeight() / 2);
		else // taakNummer==2 of taakNummer==3		
			opnieuwButton.setBounds(
				fiButton.getLocation().x - offSet - width,
				offSet,
				width, 3 * theFM.getHeight() / 2);

		bottomPanel.add(opnieuwButton);
		opnieuwButton.addActionListener(new OpnieuwAL());	
		
		oppervlakteTextField = new TextField("");
		oppervlakteTextField.setFont(theBoldFont);		
		width = theBoldFM.stringWidth("XXXXXX") + 15;
		oppervlakteTextField.setBounds(
			opnieuwButton.getLocation().x - 2 * offSet - width,
			offSet,
			width, 3 * theFM.getHeight() / 2);

		if ((taakNummer == 2) || (taakNummer == 3))
		{	bottomPanel.add(oppervlakteTextField);
			oppervlakteTextField.addFocusListener(new TextFL());
			oppervlakteTextField.addActionListener(new TextAL());
			oppervlakteTextField.addKeyListener(new InputKL());
		}		

		vergelijkChoice = new Choice();
		vergelijkChoice.add(rb.getString("groterTekst"));
		vergelijkChoice.add(rb.getString("kleinerTekst"));
		vergelijkChoice.add(rb.getString("evengrootTekst"));
		vergelijkChoice.setFont(theBoldFont);		
		vergelijkChoice.setBackground(Color.white);		
		width = Math.max(theBoldFM.stringWidth(rb.getString("groterTekst")),
					Math.max(theBoldFM.stringWidth(rb.getString("kleinerTekst")),
							 theBoldFM.stringWidth(rb.getString("evengrootTekst"))))
				+ 30;
		vergelijkChoice.setBounds(
			opnieuwButton.getLocation().x - 2 * offSet - width,
			offSet / 2,
			width, 3 * theFM.getHeight() / 2);
		vergelijkChoice.addItemListener(new VergelijkIL());	

		okButton = new Button("ok");
		okButton.setBackground(buttonColor);
		okButton.setFont(theFont);
		width = theFM.stringWidth("ok") + 35;
		okButton.setBounds(
			vergelijkChoice.getLocation().x + vergelijkChoice.getSize().width -
			width,
			vergelijkChoice.getLocation().y + vergelijkChoice.getSize().height - 2,
			width, 3 * theFM.getHeight() / 2);
		okButton.addActionListener(new OkAL());	
			
		if (taakNummer == 4)
		{	bottomPanel.add(vergelijkChoice);
		}	
		
		// width ad hoc voor maximum 10
		selector = new OpdrachtSelector(aantalFiguren);		
		selector.setBounds(offSet, offSet, 280, bottomHeight);
		bottomPanel.add(selector);
		selector.addItemListener(new OpdrachtIL());
		
		opdrachtLabel = new JLabel();
		opdrachtLabel.setFont(theBoldFont);
opdrachtLabel.setOpaque(true);
opdrachtLabel.setBackground(Color.orange);		

		opdrachtLabel2 = new JLabel();
		opdrachtLabel2.setFont(theBoldFont);
opdrachtLabel2.setOpaque(true);
opdrachtLabel2.setBackground(Color.orange);		


		if (taakNummer == 1)
		{	opdrachtLabel.setText(rb.getString("maakRechthoekTekst"));
			opdrachtLabel.setBounds(
				selector.getLocation().x + selector.getSize().width + 2 * offSet,
				offSet,
				getSize().width - 
				(selector.getSize().width + 2 * offSet +
				 getSize().width - opnieuwButton.getLocation().x + 2 * offSet),
				3 * theBoldFM.getHeight() / 2);
			
		}	
		else if ((taakNummer == 2) || (taakNummer == 3))
		{	opdrachtLabel.setText(rb.getString("watIsOppervlakteTekst"));
			opdrachtLabel.setBounds(
				selector.getLocation().x + selector.getSize().width + 3 * offSet,
				offSet,
				getSize().width - 
				(selector.getSize().width + 3 * offSet +
				 getSize().width - oppervlakteTextField.getLocation().x + 2 * offSet),
				3 * theBoldFM.getHeight() / 2);
		
		}
		else if (taakNummer == 4)
		{	
			opdrachtLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			opdrachtLabel.setText(rb.getString("oppervlakteRoodTekst") + "   ");
			opdrachtLabel.setBounds(
				selector.getLocation().x + selector.getSize().width + 2 * offSet,
				offSet / 2 - 2,
				getSize().width - 
				(selector.getSize().width + 2 * offSet +
				 getSize().width - vergelijkChoice.getLocation().x + offSet),
				3 * theBoldFM.getHeight() / 2);
				
			opdrachtLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
			opdrachtLabel2.setText(rb.getString("oppervlakteGrijsTekst") + "   ");	
			opdrachtLabel2.setBounds(
				opdrachtLabel.getLocation().x,
				opdrachtLabel.getLocation().y + opdrachtLabel.getSize().height,
				opdrachtLabel.getSize().width,
				3 * theBoldFM.getHeight() / 2);
				
			bottomPanel.add(opdrachtLabel2);
			bottomPanel.add(okButton);
		}

		bottomPanel.add(opdrachtLabel);	


		if (aantalFiguren == 1)
			setSingleComponent();

	}
	
	public void setSingleComponent()
	{	
/*	
		if (bottomPanel != null) 
			getContentPane().remove(bottomPanel); 
		if (currentOpdracht != null)
			currentOpdracht.drawingPanel.setBounds(
				0, 0, getSize().width, getSize().height);
*/				

		isDWOComponent = true;
		bottomPanel.remove(selector);
		bottomPanel.remove(fiButton);
		
		opnieuwButton.setLocation(
			getSize().width - opnieuwButton.getSize().width - offSet,
			offSet);
		
		
		if (taakNummer == 1)
		{	
			opdrachtLabel.setBounds(
				offSet, offSet,
				//getSize().width - 
				//	(getSize().width - opnieuwButton.getLocation().x + 2 * offSet),
				theBoldFM.stringWidth(opdrachtLabel.getText()) + 10,
				3 * theBoldFM.getHeight() / 2);
		}
		else if ((taakNummer == 2) || (taakNummer == 3))
		{
			opdrachtLabel.setBounds(
				offSet, offSet,
				//getSize().width - 
				//	(getSize().width - oppervlakteTextField.getLocation().x + 2 * offSet),
				theBoldFM.stringWidth(opdrachtLabel.getText()) + 10,
				3 * theBoldFM.getHeight() / 2);
			oppervlakteTextField.setLocation(
				opdrachtLabel.getLocation().x + opdrachtLabel.getSize().width + offSet,
				offSet);
			
		}
		else if (taakNummer == 4)
		{
			opdrachtLabel.setBounds(
				offSet / 2, offSet / 2 - 2,
				//getSize().width - 
				//	(getSize().width - vergelijkChoice.getLocation().x + offSet),
				theBoldFM.stringWidth(opdrachtLabel.getText()) + 10,	
				3 * theBoldFM.getHeight() / 2);
			
			vergelijkChoice.setLocation(
				opdrachtLabel.getLocation().x + opdrachtLabel.getSize().width + offSet,
				vergelijkChoice.getLocation().y);
			
			//opdrachtLabel2.setHorizontalAlignment(SwingConstants.LEFT);
			opdrachtLabel2.setSize(theBoldFM.stringWidth(opdrachtLabel2.getText()) + 10, 3 * theBoldFM.getHeight() / 2);
			opdrachtLabel2.setLocation(offSet / 2,
				//opdrachtLabel.getLocation().x + opdrachtLabel.getSize().width -
				//opdrachtLabel2.getSize().width, 
				opdrachtLabel2.getLocation().y);
			
			okButton.setLocation(vergelijkChoice.getLocation().x + vergelijkChoice.getSize().width -
				                 okButton.getSize().width, okButton.getLocation().y);

		}
/*		
		
		opdrachtLabel2.setLocation(opdrachtLabel.getLocation().x, 
								   opdrachtLabel2.getLocation().y);
								   
		vergelijkChoice.setLocation(
			opdrachtLabel.getLocation().x + opdrachtLabel.getSize().width,
			offSet / 2);
								   
		okButton.setLocation(
			vergelijkChoice.getLocation().x + vergelijkChoice.getSize().width -
			okButton.getSize().width,
			vergelijkChoice.getLocation().y + vergelijkChoice.getSize().height - 2);
*/								   
	}

	public Vector processFiguurString(String fString)
	{	fString = removeAllBlanks(fString);
		Vector figCoordStrings = parseOnChar(fString, '|');
		Vector figCoords = new Vector();
		for (int cCnt = 0; cCnt < figCoordStrings.size(); cCnt++)
		{	String aCoordString = (String) figCoordStrings.elementAt(cCnt);
			int kommaIndex = aCoordString.indexOf(',');
			if ((kommaIndex >= 0) && (kommaIndex < (aCoordString.length() - 1)))
			{	String xCoordString = aCoordString.substring(0, kommaIndex);
				String yCoordString = aCoordString.substring(kommaIndex + 1);
				int xCoord = 0;
				int yCoord = 0;
				boolean error = false;
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
		}
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

    public void select(int i)
    {   
    	currentOpdracht.drawingPanel.setVisible(false);
    	i = i % opdrachten.length;
        selector.select(i);
        currentOpdracht = opdrachten[i];
        currentNum = i;
        currentOpdracht.drawingPanel.setVisible(true);
        if (taakNummer == 1)
        {
	        //if (currentOpdracht.drawingPanel.figureIsRectangle)
	        if (currentOpdracht.antwoordOK)
	        {	opdrachtLabel.setText(rb.getString("rechthoekTekst"));
    	    }
	        else
    	    {	opdrachtLabel.setText(rb.getString("maakRechthoekTekst"));
        	}
        }
        else if ((taakNummer == 2) || (taakNummer == 3))
        {
			// antwoord invullen        	
			if (currentOpdracht.antwoord > 0)
			{	oppervlakteTextField.setText("" + currentOpdracht.antwoord);
				
				if (isDWOComponent)
				{	if (currentOpdracht.antwoordOK)
					{	bottomPanel.showGoed = true;
						bottomPanel.showFout = false;
					}
					else
					{	bottomPanel.showGoed = false;
						bottomPanel.showFout = true;
					}
				}
			
			
			}
			else // geen antwoord gegeven
			{	oppervlakteTextField.setText("");
				if (isDWOComponent)
				{	bottomPanel.showGoed = false;
					bottomPanel.showFout = false;
				}
			}
		}		
        else if (taakNummer == 4)
        {	if (currentOpdracht.antwoord > 0)
        	{	vergelijkChoice.select(currentOpdracht.antwoord - 1);
        
        		if (isDWOComponent)
				{	
					if (currentOpdracht.antwoordOK && (currentOpdracht.antwoordenFout == 0))
					{	bottomPanel.showGoed = true;
						bottomPanel.showFout = false;
					}
					else if (currentOpdracht.antwoordOK && (currentOpdracht.antwoordenFout > 0))
					{	bottomPanel.showGoed = true;
						bottomPanel.showFout = true;
					}
					else
					{	bottomPanel.showGoed = false;
						bottomPanel.showFout = true;
					}
        		}
        	}
        	else // geen antwoord gegeven
        	{	vergelijkChoice.select(0);
        		if (isDWOComponent)
				{	bottomPanel.showGoed = false;
					bottomPanel.showFout = false;
				}
        		
        	}
        }
    }


	class OpdrachtIL implements ItemListener
	{	public void itemStateChanged(ItemEvent e)
		{	select(((Number) e.getItem()).intValue()); 
		}
	}

	class VergelijkIL implements ItemListener
	{	public void itemStateChanged(ItemEvent e)
		{	selector.setState(currentNum, selector.INITIAL);
			currentOpdracht.antwoord = 0;
			currentOpdracht.antwoordOK = false;
			if (isDWOComponent)
			{	bottomPanel.showGoed = false;
				bottomPanel.showFout = false;
			}	
			
			repaint();

		}
	}

	class OkAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	String choice = vergelijkChoice.getSelectedItem();
			currentOpdracht.antwoord = vergelijkChoice.getSelectedIndex() + 1;
			boolean ok = false;
			if (choice.equals(rb.getString("evengrootTekst")) &&
			    (currentOpdracht.oppervlakte == currentOpdracht.oppervlakteGrijs))
			{	ok = true;    
			}
			if (choice.equals(rb.getString("groterTekst")) &&
			    (currentOpdracht.oppervlakte > currentOpdracht.oppervlakteGrijs))
			{	ok = true;    
			}
			if (choice.equals(rb.getString("kleinerTekst")) &&
			    (currentOpdracht.oppervlakte < currentOpdracht.oppervlakteGrijs))
			{	ok = true;    
			}
			if (ok && (currentOpdracht.antwoordenFout == 0))
			{	selector.setState(currentNum, selector.GREEN);
				currentOpdracht.antwoordOK = true;
				if (isDWOComponent)
				{	bottomPanel.showGoed = true;
					bottomPanel.showFout = false;
					
					//if (ipa != null)				
					//	ipa.produceAction("changed");
					
				}	
			}
			else if (ok && (currentOpdracht.antwoordenFout > 0))
			{	selector.setState(currentNum, selector.ORANGE);
				currentOpdracht.antwoordOK = true;
				if (isDWOComponent)
				{	bottomPanel.showGoed = true;
					bottomPanel.showFout = true;
					
					//if (ipa != null)				
					//	ipa.produceAction("changed");
					
				}	
				
			}
			else
			{	currentOpdracht.antwoordenFout++;
				selector.setState(currentNum, selector.RED);	
				currentOpdracht.antwoordOK = false;
				if (isDWOComponent)
				{	bottomPanel.showGoed = false;
					bottomPanel.showFout = true;
					
					//if (ipa != null)				
					//	ipa.produceAction("changed");
					
				}	
			
			}
			
			repaint();
		}
	}	

	public void opnieuwAction()
	{
		currentOpdracht.drawingPanel.removeAllKnipPolygons();
		
		KnipPolygon kp = new KnipPolygon(currentOpdracht.drawingPanel, 
										 currentOpdracht.figuurCoordinaten,
										 KnipPolygon.CENTER);
										 
		if (taakNummer == 4)
		{	kp = new KnipPolygon(currentOpdracht.drawingPanel, 
										 currentOpdracht.figuurCoordinaten,
										 KnipPolygon.RIGHTAL);
		}												 
		if ((taakNummer == 2) || (taakNummer == 3))
			kp.setLabelPoint();
										 
		currentOpdracht.drawingPanel.addKnipPolygon(kp);        

		currentOpdracht.drawingPanel.oval1Pos = null;
		currentOpdracht.drawingPanel.oval2Pos = null;			        
		currentOpdracht.drawingPanel.oval3Pos = null;
		currentOpdracht.drawingPanel.repaint();
		currentOpdracht.drawingPanel.figureIsRectangle = false;

		currentOpdracht.antwoordOK = false;
		currentOpdracht.antwoord = 0;
		currentOpdracht.antwoordenFout = 0;

		if (taakNummer == 1)
		{	opdrachtLabel.setText(rb.getString("maakRechthoekTekst"));
		}
		else // taakNummer==2 of taakNummer==3 of taakNummer==4
		{	currentOpdracht.antwoord = 0;
			oppervlakteTextField.setText("");
		}	
		
		selector.setState(currentNum, selector.INITIAL);
		
		if (isDWOComponent)
		{	bottomPanel.showGoed = false;
			bottomPanel.showFout = false;
		}	
		
		repaint();
		
	}
	
	class OpnieuwAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{
			opnieuwAction();
			
			//if (ipa != null)
			//	ipa.produceAction("changed");
/*		
			currentOpdracht.drawingPanel.removeAllKnipPolygons();
			
			KnipPolygon kp = new KnipPolygon(currentOpdracht.drawingPanel, 
											 currentOpdracht.figuurCoordinaten,
											 KnipPolygon.CENTER);
											 
			if (taakNummer == 4)
			{	kp = new KnipPolygon(currentOpdracht.drawingPanel, 
											 currentOpdracht.figuurCoordinaten,
											 KnipPolygon.RIGHTAL);
			}												 
			if ((taakNummer == 2) || (taakNummer == 3))
				kp.setLabelPoint();
											 
			currentOpdracht.drawingPanel.addKnipPolygon(kp);        

			currentOpdracht.drawingPanel.oval1Pos = null;
			currentOpdracht.drawingPanel.oval2Pos = null;			        
			currentOpdracht.drawingPanel.oval3Pos = null;
			currentOpdracht.drawingPanel.repaint();
			currentOpdracht.drawingPanel.figureIsRectangle = false;

			currentOpdracht.antwoordOK = false;
			currentOpdracht.antwoord = 0;
			currentOpdracht.antwoordenFout = 0;

			if (taakNummer == 1)
			{	opdrachtLabel.setText(rb.getString("maakRechthoekTekst"));
			}
			else // taakNummer==2 of taakNummer==3 of taakNummer==4
			{	currentOpdracht.antwoord = 0;
				oppervlakteTextField.setText("");
			}	
			
			selector.setState(currentNum, selector.INITIAL);
			
			if (isDWOComponent)
			{	bottomPanel.showGoed = false;
				bottomPanel.showFout = false;
			}	
			
			repaint();
*/			
		}
	}

	public void focusLostAction()
	{	String text = oppervlakteTextField.getText();

//System.out.println("fl = " + text);			

		String text1 = trimTrailingZeros(text);
		boolean changed1 = (text.length() != text1.length());
		String text2 = addLeadingZero(text1);
		boolean changed2 = (text1.length() != text2.length());
		if (changed1 || changed2)
		{	text = text2;
			oppervlakteTextField.setText(text);
		}
		
		boolean error = false;
		int oNum = 0;
		try
		{	oNum = Integer.parseInt(text);
		}
		catch (NumberFormatException nfe)
		{	error = true;
		}
		if (!error)
		{	currentOpdracht.antwoord = oNum;
			if (currentOpdracht.antwoord == currentOpdracht.oppervlakte)
			{	selector.setState(currentNum, selector.GREEN);
				currentOpdracht.antwoordOK = true;
				if (isDWOComponent)
				{	bottomPanel.showGoed = true;
					bottomPanel.showFout = false;
				}	
			}
			else
			{	selector.setState(currentNum, selector.RED);
				currentOpdracht.antwoordOK = false;
				if (isDWOComponent)
				{	bottomPanel.showGoed = false;
					bottomPanel.showFout = true;
				}	

			}
//System.out.println("o = " + oNum);			
		}	
			
		repaint();

	}

	class TextFL implements FocusListener
	{	public void focusGained(FocusEvent e)
		{
		}
		public void focusLost(FocusEvent e)
		{	focusLostAction();
		}
	}
	class TextAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	
			String text = oppervlakteTextField.getText();
			
			String text1 = trimTrailingZeros(text);
			boolean changed1 = (text.length() != text1.length());
			String text2 = addLeadingZero(text1);
			boolean changed2 = (text1.length() != text2.length());
			if (changed1 || changed2)
			{	text = text2;
				oppervlakteTextField.setText(text);
			}
			
			boolean error = false;
			int oNum = 0;
			try
			{	oNum = Integer.parseInt(text);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			if (!error)
			{	currentOpdracht.antwoord = oNum;
				if (currentOpdracht.antwoord == currentOpdracht.oppervlakte)
				{	selector.setState(currentNum, selector.GREEN);
					currentOpdracht.antwoordOK = true;
					if (isDWOComponent)
					{	bottomPanel.showGoed = true;
						bottomPanel.showFout = false;
						
						//if (ipa != null)				
						//	ipa.produceAction("changed");
						
					}	
				}
				else
				{	selector.setState(currentNum, selector.RED);			
					currentOpdracht.antwoordOK = false;
					if (isDWOComponent)
					{	bottomPanel.showGoed = false;
						bottomPanel.showFout = true;
						
						//if (ipa != null)				
						//	ipa.produceAction("changed");
						
					}	

				}
			}	
			
			repaint();

		}
	}

	public String trimTrailingZeros(String s)
	{	String txt = new String(s);
		if (txt.indexOf('.') < 0)
			return txt;
		char c = txt.charAt(txt.length() - 1);
		while (c == '0')
		{	txt = removeCharAt(txt, txt.length() - 1);
			c = txt.charAt(txt.length() - 1);
		}	
		c = txt.charAt(txt.length() - 1);
		if (c == '.')
			txt = removeCharAt(txt, txt.length() - 1);
		return txt;		
	}				
		
	public String addLeadingZero(String s)
	{	String txt = new String(s);
		// met minteken
		if ((txt.length() >= 2) && (txt.charAt(0) == '-') &&
			(txt.charAt(1) == '.'))
		{	txt = "-0" + txt.substring(1);
		}	
		// zonder minteken
		if ((txt.length() >= 1) && (txt.charAt(0) == '.'))
		{	txt = "0" + txt;
		}
		return txt;
	}

	public String removeCharAt(String s, int index)
	{	String txt = new String(s);
		// eerste
		if (index == 0)
			txt = txt.substring(1);
		// laatste	
		else if (index == (txt.length() - 1))
			txt = txt.substring(0, txt.length() - 1);
		// middenin	
		else
		{	String txt1 = txt.substring(0, index);
			String txt2 = txt.substring(index + 1);
			txt = txt1 + txt2;
		}
		return txt;
	}		
	
	class InputKL extends KeyAdapter
	{	public void keyReleased(KeyEvent e)
		{	
			String txt = oppervlakteTextField.getText();
			boolean corrected = false;
			// kijk of txt illegale characters bevat
			// dit zou er maximaal 1 moeten zijn
			int index = -1;
			for (int cCnt = 0; cCnt < txt.length(); cCnt++)
			{	char c = txt.charAt(cCnt);
				if (!isLegal(c))
					index = cCnt;
			}
			// verwijder illegaal karakter
			if (index >= 0)
			{	txt = removeCharAt(txt, index);
				corrected = true;
			}
			// leading zeros, leiden niet tot een NumberFormatException
			// geen minteken
			if (//(txt.indexOf('-') < 0) && 
				(txt.length() >= 2) &&
				(txt.charAt(0) == '0') && Character.isDigit(txt.charAt(1)))
			{	txt = removeCharAt(txt, 0);
				corrected = true;
			}
			
// trailing zeros na(!) decimale punt oplossen 
// bij actionPerformed of focusLost			
			
			if (corrected)
				oppervlakteTextField.setText(txt);
			
		}
		
		public boolean isLegal(char c)
		{	return Character.isDigit(c); 
		}
	}

	// scormgebeuren

	public void start()
	{	if (api != null)
		{	String s = api.LMSGetValue("cmi.suspend_data");
			if (s != null && !s.equals(""))
			{	setState(s);
//System.out.println("api setState");			
			}
		}
		else
		{
//System.out.println("api not setState");			
		}
	}
	
	public void stopSco()
	{	stop();
		api = null;
	}

	public void stop()
	{	if (api != null)
		{	String s = getState();
			String d = new Double(getScore()).toString();
			api.LMSSetValue("cmi.core.score.raw",d);
			api.LMSSetValue("cmi.suspend_data",s);
		}
	}

	public void setState(String s)
	{	
		
//System.out.println("setState");		
		
		// decodeer de string
		Object o = StringCodeObject.decodeStringToObject(s);
		// cast
		Vector scormOpdrachten = (Vector) o;
	    // herstel de state van het applet
	    for (int oCnt = 0; oCnt < aantalFiguren; oCnt++)
	    {	ScormOpdracht scoOpdracht = 
	    		(ScormOpdracht) scormOpdrachten.elementAt(oCnt);
	    	if (scoOpdracht.isCurrent)	
	    		currentNum = oCnt;
	    	opdrachten[oCnt].antwoord = scoOpdracht.antwoord % 100;
//System.out.println("a = " + opdrachten[oCnt].antwoord);	    	
	    	opdrachten[oCnt].antwoordOK = scoOpdracht.antwoordOK;	    
	    	opdrachten[oCnt].antwoordenFout = scoOpdracht.antwoord / 100;
//System.out.println("afout = " + opdrachten[oCnt].antwoordenFout);	    	
	    	opdrachten[oCnt].drawingPanel.knipPolygons.removeAllElements();
			for (int pCnt = 0; pCnt < scoOpdracht.figuurPolygons.size(); pCnt++)
	    	{	ScormPolygon sp = 
	    			(ScormPolygon) scoOpdracht.figuurPolygons.elementAt(pCnt);
	    		KnipPolygon kp = new KnipPolygon(sp.realPoints, 
	    							opdrachten[oCnt].drawingPanel);
	    		
	    		kp.oppervlakte = sp.oppervlakte;
	    		
	    		opdrachten[oCnt].drawingPanel.knipPolygons.addElement(kp);
	    		
	    		if ((taakNummer == 2) || (taakNummer == 3))
	    			kp.setLabelPoint();
	    		
	    		
	    	}	    	
	    		
	    	if ((opdrachten[oCnt].antwoord > 0) && (opdrachten[oCnt].antwoordenFout == 0) && opdrachten[oCnt].antwoordOK)
	    		selector.setState(currentNum, selector.GREEN);
	    	else if ((opdrachten[oCnt].antwoord > 0) && (opdrachten[oCnt].antwoordenFout > 0) && opdrachten[oCnt].antwoordOK)
	    		selector.setState(currentNum, selector.ORANGE);
	    	else if ((opdrachten[oCnt].antwoord > 0) && !opdrachten[oCnt].antwoordOK)
	    		selector.setState(currentNum, selector.RED);
	    }

		// dit zet de antwoorden
		select(currentNum);

	}

	public String getState()
	{	
		// creeer de gegevens die de state bepalen
		Vector scormOpdrachten = new Vector();
	    
	    for (int oCnt = 0; oCnt < aantalFiguren; oCnt++)
	    {	ScormOpdracht scoOpdracht = new ScormOpdracht(oCnt + 1);
	    	if (currentNum == oCnt)
	    		scoOpdracht.isCurrent = true;
	    	scoOpdracht.antwoord = 100 * opdrachten[oCnt].antwoordenFout + opdrachten[oCnt].antwoord;
	    	scoOpdracht.antwoordOK = opdrachten[oCnt].antwoordOK;
//	    	scoOpdracht.antwoordenFout = opdrachten[oCnt].antwoordenFout;
	    	Vector knipPolygons = opdrachten[oCnt].drawingPanel.knipPolygons;
	    	for (int pCnt = 0; pCnt < knipPolygons.size(); pCnt++)
	    	{	KnipPolygon kp = (KnipPolygon) knipPolygons.elementAt(pCnt);
	    		ScormPolygon sp = new ScormPolygon(kp.realPoints);
	    		sp.oppervlakte = kp.oppervlakte;
	    		scoOpdracht.figuurPolygons.addElement(sp);
	    	}	 
	    		
	    	scormOpdrachten.addElement(scoOpdracht);	
	    }
		

	    // codeer deze gegevens tot een string
	    String s = StringCodeObject.encodeObjectToString(scormOpdrachten);

	    return s;
	}

	public double getScore()
	{	return 0.5;
	}
	
	public InteractiePanel getInteractiePanel()
	{	
/*		
		if (ipa == null)
			ipa = new InteractiePanelAdapter(this);
		
		return ipa;
*/		
		return new VerknippenInteractiePanel();
	}
	
	public boolean hasEditMode()
	{	return false;
	}

    public ScormEditComponentIF getEditComponent(Hashtable launchdata)
    {	return null;
    }
    
    public Hashtable getDefaultParameters()
    {
    	Hashtable h = new Hashtable();

    	h.put("taaknummer", "1");
    	h.put("toonbalk", "true");
    	h.put("toonrooster", "no");
    	h.put("groteballetjes", "no");
    	h.put("toonschaduw", "no");
    	h.put("toonafmetingen", "no");
    	h.put("figuur1", "2,0|10,0|8,8|0,8");
    	h.put("grid1", "20");    	
    	h.put("oppervlakte1", "64");    	
    	h.put("figuurgrijs1", "0,0|8,0|8,8|0,8");    	
    	h.put("oppervlaktegrijs1", "64");    	    	
    	h.put("scoremax", "10");
    	
    	return h;
    }
    
    public Parameter[] getEditableParameters()
	{	
    	Parameter[] parameters = new Parameter[12];
		
		DataType type = new ScormString();
		DataType btype = new ScormBoolean();
		
		Parameter param = new Parameter("taaknummer", "Nummer van de taak", type);
		param.setHelpText("1 (rechthoek), 2, 3 (oppervlakte) of 4 (vergelijken)");		
		parameters[0] = param;
		
		param = new Parameter("toonbalk", "Balk onderaan", btype);
		//param.setHelpText("vul in: yes of no");
		parameters[1] = param;
		
		param = new Parameter("toonrooster", "Rooster zichtbaar", btype);
		//param.setHelpText("vul in: yes of no");
		parameters[2] = param;
		
		param = new Parameter("groteballetjes", "Grote balletjes", btype);
		//param.setHelpText("vul in: yes of no");		
		parameters[3] = param;

		param = new Parameter("toonschaduw", "Schaduw zichtbaar", btype);
		//param.setHelpText("vul in: yes of no");
		parameters[4] = param;
		
		param = new Parameter("toonafmetingen", "Afmetingen zichtbaar", btype);
		//param.setHelpText("vul in: yes of no, yes alleen als toonschaduw = yes");
		parameters[5] = param;
		
		// aantal figuren wordt gefixeerd op 1
		
		param = new Parameter("figuur1", "Rode figuur", type);
		param.setHelpText("vul bv in: 0,0|2,0|2,2|5,2|5,4|7,4|7,6|0,6");
		parameters[6] = param;
		
		param = new Parameter("grid1", "Grid in pixels", type);
		param.setHelpText("minimum 16, maximum 50, even getal");
		parameters[7] = param;
		
		param = new Parameter("oppervlakte1", "Oppervlakte rood", type);
		param.setHelpText("oppervlakte rode figuur in grid-eenheden");
		parameters[8] = param;
		
		param = new Parameter("figuurgrijs1", "Grijze figuur (taak 4)", type);
		param.setHelpText("vul bv in: 0,0|2,0|2,2|5,2|5,4|7,4|7,6|0,6");
		parameters[9] = param;
		
		param = new Parameter("oppervlaktegrijs1", "Oppervlakte grijs", type);
		param.setHelpText("oppervlakte grijze figuur in grid-eenheden");
		parameters[10] = param;

		param = new Parameter("scoremax", "Maximum score", type);
		param.setHelpText("maximum score");		
		parameters[11] = param;
		
		return parameters;
    }

    public Parameter[] getAllParameters()
    {	return null;
    }
}
