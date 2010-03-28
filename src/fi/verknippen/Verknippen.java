package fi.verknippen;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;

import javax.swing.*;

import fi.beans.copyright.*;
import fi.beans.scorm.*;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;
import fi.beans.base64code.*;
import fi.verknippen.InteractiePanelAdapter;

public class Verknippen extends JApplet implements ScormAppletIF , WiskOpdrParamEditApplet
{
	// scormgebeuren
	protected static ResourceBundle rb;
	protected SCORM12APIInterface api;
	private TextField textField;
	
	public static void main(String[] args)    
	{	int width = 790;
        int height = 485;
		ScormMainFrame mf = new ScormMainFrame(new Verknippen(),width, height);
		mf.setTitle("Verknippen Scormed");
		mf.pack();
		mf.show();
		mf.setSize(width, height);
	}
	
	Color bgColor;
	
	// andere kleuren in DrawingPanel	
	
	DrawingPanel drawingPanel;
	BottomPanel bottomPanel;
	int bottomHeight = 50;
	int offSet = 10;
	
	int maxTaken = 3;
	int taakNummer = 1;
	// maximum aantal figuren binnen een taak
	int maxFiguren = 10;
	int aantalFiguren = 1;
	String defaultFiguurString = "0,0|8,0|8,8|0,8";
	OpdrachtSelector selector;
	Opdracht[] opdrachten;
	Opdracht currentOpdracht;
	int currentNum;
	
	JLabel opdrachtLabel;
	//JButton opnieuwButton;
	Button opnieuwButton;
	TextField oppervlakteTextField;
	
	int minGrid = 16;
	int maxGrid = 50;
	//int gridSize = 30;

	// parametrisatie
	boolean showGrid = false;
	boolean showShadow = false;
	boolean showSizes = false;
	
	boolean largeOvals = false;
	
	// fonts
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;

	Color buttonColor = Color.yellow;

	public Verknippen()
	{	Locale language = new Locale ("nl", "");
		//applet=this;
		//rb = ResourceBundle.getBundle("fi.fruitbalanceapplet.text.Text",language);
	}
	
	public Verknippen(Locale language)
	{	
		//applet=this;
		//rb = ResourceBundle.getBundle("fi.fruitbalanceapplet.text.Text",language);
	}
	
	public void init() 
	{	try
		{	api = Scorm.findAPI(this);
		}
		catch(Exception e){}
		
		getContentPane().setLayout(null);

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
		bgColor = new Color(255,255,255);
		String kleurcode = getParameter("bgcolor");
		if	(kleurcode != null)
			bgColor = new Color(Integer.parseInt(kleurcode.substring(1),16));
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
			if (!error && (taakNum > 0) && (taakNum <= maxTaken))
			{	taakNummer = taakNum;
			}
		}		
	
		// rooster tonen?
		String showGridString = getParameter("toonrooster");
		if ((showGridString != null) && showGridString.equals("yes"))
			showGrid = true;

		// schaduw tonen?
		String showShadowString = getParameter("toonschaduw");
		if ((showShadowString != null) && showShadowString.equals("yes"))
			showShadow = true;
	
		// afmetingen tonen? alleen als schaduw ook getoond wordt
		if (showShadow)
		{	String showSizesString = getParameter("toonafmetingen");
			if ((showSizesString != null) && showSizesString.equals("yes"))
				showSizes = true;
		}

		// grotere balletjes
		String largeOvalsString = getParameter("groteballetjes");
		if ((largeOvalsString != null) && largeOvalsString.equals("yes"))
			largeOvals = true;
	
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
		
		opdrachten = new Opdracht[aantalFiguren];
		for (int oCnt = 0; oCnt < aantalFiguren; oCnt++)
		{	
			// opdrachtnummer
			opdrachten[oCnt] = new Opdracht(oCnt + 1);
			
			// lees figuur vor deze opdracht			
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
			
			// lees oppervlakte voor deze opdracht
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
			// zet de figuur	
			KnipPolygon kp = new KnipPolygon(opdrachten[oCnt].drawingPanel, 
								 opdrachten[oCnt].figuurCoordinaten);
								 
if ((taakNummer == 2) || (taakNummer == 3))
	kp.setLabelPoint();
								 
			opdrachten[oCnt].drawingPanel.addKnipPolygon(kp);
			// schaduw figuur
			if (showShadow)
			{	KnipPolygon sp = new KnipPolygon(opdrachten[oCnt].drawingPanel, 
								 	opdrachten[oCnt].figuurCoordinaten);
				opdrachten[oCnt].drawingPanel.shadowPolygon = sp;				 	
				if (showSizes)
					opdrachten[oCnt].drawingPanel.showSizes = true;
			}
			
			
		} // for	

		currentOpdracht = opdrachten[0];
		currentNum = 0;
		currentOpdracht.drawingPanel.setVisible(true);

		// bottomPanel		
		bottomPanel = new BottomPanel(this);
		bottomPanel.setBounds(0, getSize().height - bottomHeight, 
			getSize().width, bottomHeight);
		getContentPane().add(bottomPanel);

		//Fi-logo, copyright
		FIButton fiButton = new FIButton("Verknippen",new String[]
			{	"versie-info: 20090909",
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
		int width = theFM.stringWidth(rb.getString("opnieuwTekst")) + 55;

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
			opnieuwButton.getLocation().x - offSet - width,
			offSet,
			width, 3 * theFM.getHeight() / 2);

		if ((taakNummer == 2) || (taakNummer == 3))
		{	bottomPanel.add(oppervlakteTextField);
			oppervlakteTextField.addFocusListener(new TextFL());
			oppervlakteTextField.addActionListener(new TextAL());
			oppervlakteTextField.addKeyListener(new InputKL());
		}		
		
		// width ad hoc voor maximum 10
		selector = new OpdrachtSelector(aantalFiguren);		
		selector.setBounds(offSet, offSet, 280, bottomHeight);
		bottomPanel.add(selector);
		selector.addItemListener(new OpdrachtIL());
		
		opdrachtLabel = new JLabel();
		opdrachtLabel.setFont(theBoldFont);
		opdrachtLabel.setText(rb.getString("maakRechthoekTekst"));
//opdrachtLabel.setOpaque(true);
//opdrachtLabel.setBackground(Color.orange);		

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
		else // taakNummer==2 of taakNummer==3
		{	opdrachtLabel.setText(rb.getString("watIsOppervlakteTekst"));
			opdrachtLabel.setBounds(
				selector.getLocation().x + selector.getSize().width + 2 * offSet,
				offSet,
				getSize().width - 
				(selector.getSize().width + 2 * offSet +
				 getSize().width - oppervlakteTextField.getLocation().x + 2 * offSet),
				3 * theBoldFM.getHeight() / 2);
		
		}

		bottomPanel.add(opdrachtLabel);	
		
		//Test-textfield
		textField = new TextField();
		textField.setBounds(50,100,200,25);
		//add(textField);
	}
	
	public void setSingleComponent()
	{	if(bottomPanel!=null) getContentPane().remove(bottomPanel); 
		if(currentOpdracht!=null)currentOpdracht.drawingPanel.setBounds(0, 0, getSize().width, getSize().height);
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
	        if (currentOpdracht.drawingPanel.figureIsRectangle)
	        {	opdrachtLabel.setText(rb.getString("rechthoekTekst"));
    	    }
	        else
    	    {	opdrachtLabel.setText(rb.getString("maakRechthoekTekst"));
        	}
        }
        else // taakNummer==2 of taakNummer==3
        {
			// antwoord invullen        	
			if (currentOpdracht.antwoord > 0)
				oppervlakteTextField.setText("" + currentOpdracht.antwoord);
			else
				oppervlakteTextField.setText("");
        }	
    }


	class OpdrachtIL implements ItemListener
	{	public void itemStateChanged(ItemEvent e)
		{	select(((Number) e.getItem()).intValue()); 
		}
	}

	class OpnieuwAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	currentOpdracht.drawingPanel.removeAllKnipPolygons();
			KnipPolygon kp = new KnipPolygon(currentOpdracht.drawingPanel, 
											 currentOpdracht.figuurCoordinaten);
if ((taakNummer == 2) || (taakNummer == 3))
	kp.setLabelPoint();
											 
			currentOpdracht.drawingPanel.addKnipPolygon(kp);        
			currentOpdracht.drawingPanel.oval1Pos = null;
			currentOpdracht.drawingPanel.oval2Pos = null;			        
			currentOpdracht.drawingPanel.oval3Pos = null;
			currentOpdracht.drawingPanel.repaint();
			currentOpdracht.drawingPanel.figureIsRectangle = false;
			if (taakNummer == 1)
				opdrachtLabel.setText(rb.getString("maakRechthoekTekst"));
			else // taakNummer == 2 of taakNummer==3
			{	currentOpdracht.antwoord = 0;
				oppervlakteTextField.setText("");
			}	
			selector.setState(currentNum, selector.INITIAL);

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
				selector.setState(currentNum, selector.GREEN);
			else
				selector.setState(currentNum, selector.RED);
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
					selector.setState(currentNum, selector.GREEN);
				else
					selector.setState(currentNum, selector.RED);			
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
/*			
			// dubbele decimale punt
			// voldoende er twee te zoeken
			int pIndex1 = txt.indexOf('.');
			int pIndex2 = txt.lastIndexOf('.');
			if ((pIndex1 >= 0) && (pIndex2 >= 0) && (pIndex1 != pIndex2))
			{	// verwijderen
				txt = removeCharAt(txt, pIndex2);
				corrected = true;
			}
*/
/*			
			// proberen een legaal karakter voor het
			// minteken (dit staat dan op plek 1) in te vullen
			if (txt.indexOf('-') == 1)
			{	txt = removeCharAt(txt, 0);
				corrected = true;
			}
*/			
/*
			// minteken
			// alleen vooraan if any
			int minIndex = txt.lastIndexOf('-');
			if (minIndex > 0)
			{	txt = removeCharAt(txt, minIndex);
				corrected = true;
			}
*/			
			// leading zeros, leiden niet tot een NumberFormatException
/*			
			// geval met minteken
			if ((txt.indexOf('-') == 0) && (txt.length() >= 3) &&
				(txt.charAt(1) == '0') && Character.isDigit(txt.charAt(2)))
			{	txt = removeCharAt(txt, 1);
				corrected = true;
			}
*/			
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
		{	return Character.isDigit(c); // || (c == '-') || (c == '.');
		}
	}

	// scormgebeuren

	public void start()
	{	if (api != null)
		{	String s = api.LMSGetValue("cmi.suspend_data");
			if (s != null && !s.equals(""))
				setState(s);
		}
	}
	
	public void stopSco()
	{	stop();
		api = null;
	}
	public void stop()
	{	if(api != null)
		{	String s = getState();
			String d = new Double(getScore()).toString();
			api.LMSSetValue("cmi.core.score.raw",d);
			api.LMSSetValue("cmi.suspend_data",s);
		}
	}
	public void setState(String s)
	{	//decodeer de string
		Object o = StringCodeObject.decodeStringToObject(s);
		Hashtable h = (Hashtable)o;
		//haal de data uit de hashtabel
		//String text = (String)h.get("text");
	    //herstel de state van het applet
	    //textField.setText(text);
	}
	public String getState()
	{	String text = null;
		//vraag de gegevens op die de state bepalen
	    //text = textField.getText();
	    Hashtable h = new Hashtable();
	    //voeg de gegeven toe aan de hashtable
	    //h.put("text", text);
	    //codeer de hashtable tot string
	    String s = StringCodeObject.encodeObjectToString(h);
	    return s;
	}
	public double getScore()
	{	return 0.5;
	}
	
	public InteractiePanel getInteractiePanel()
	{
		return new InteractiePanelAdapter(this);
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
    	h.put("taaknummer","1");
    	h.put("toonrooster","no");
    	h.put("groteballetjes","no");
    	h.put("toonschaduw","no");
    	h.put("toonafmetingen","no");
    	h.put("figuur1", "0,0|8,0|8,8|0,8");
    	
    	return h;
    }
    
    public Parameter[] getEditableParameters()
	{	
    	Parameter[] parameters = new Parameter[6];
		
		DataType type = new ScormString();
		Parameter param = new Parameter("taaknummer", "Nummer van de taak", type);
		parameters[0] = param;
		
		param = new Parameter("toonrooster", "Rooster zichtbaar", type);
		param.setHelpText("vul in: yes of no");
		parameters[1] = param;
		
		param = new Parameter("groteballetjes", "Grote balletjes", type);
		parameters[2] = param;

		param = new Parameter("toonschaduw", "Schaduw zichtbaar", type);
		param.setHelpText("vul in: yes of no");
		parameters[3] = param;
		
		param = new Parameter("toonafmetingen", "Afmetingen zichtbaar", type);
		param.setHelpText("vul in: yes of no");
		parameters[4] = param;
		
		param = new Parameter("figuur1", "Figuur", type);
		param.setHelpText("vul bv in: 0,0|2,0|2,2|5,2|5,4|7,4|7,6|0,6");
		parameters[5] = param;
		return parameters;
    }
    public Parameter[] getAllParameters()
    {	return null;
    }
}
