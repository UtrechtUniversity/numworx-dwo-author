package fi.geomalgebra;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Hashtable;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;

public class GAInteractieEditPanel extends JPanel implements InteractieEditPanel,
																	ActionListener	
{	
	int editWidth = 200;
	int editHeight = 500; 
	int gaipBreedte = 500; // startbreedte gaip
	int gaipHoogte = 450; // starthoogte gaip
	
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	int offset = 10;
	boolean componentsCreated = false;
	
	boolean noSetBounds = false;	

	protected GAInteractiePanel gaip;

	
	JCheckBox varWaardeBox, oppWaardeBox, formuleBox, constructieToolsBox, alleenOppervlaktesBox,
	          werkbladBox, oppervlaktesZichtbaarBox, lengtesBreedtesZichtbaarBox,
	          negatieveWaardenBox, puzzelenBox;
	
	JCheckBox kijkNaActiefBox;
	ButtonGroup naKijkGroup;
	JRadioButton equivalentButton, gelijkButton;
	JButton antwoordFormuleButton;
	JLabel maxScoreLabel;	
	JTextField maxScoreVeld;	

	int scoreMax = 0;
	
	ExpressiePanel antwoordFormulePanel;
	JLabel doelFormuleLabel;
	int ePanelHeight;

	public GAInteractieEditPanel()
	{
		setLayout(null);
		gaip = new GAInteractiePanel();
		add(gaip);
		
		gaip.gaiep = this;
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		int width = editWidth - 2 * offset;
		int height = 3 * theFM.getHeight() / 2;
		int currentX = gaip.getSize().width + offset;
		int currentY = offset;
		
		ePanelHeight = height;
		antwoordFormulePanel = new ExpressiePanel(200, ePanelHeight);
		antwoordFormulePanel.setVisible(false);
		gaip.add(antwoordFormulePanel, 0);
		
		doelFormuleLabel = new JLabel(GeomAlgebra.rb.getString("doelFormuleTekst"));
		doelFormuleLabel.setFont(theBoldFont);
		doelFormuleLabel.setBackground(Color.white);
		doelFormuleLabel.setSize(theBoldFM.stringWidth(doelFormuleLabel.getText()), 3 * theBoldFM.getHeight() / 2);
		doelFormuleLabel.setVisible(false);
		gaip.add(doelFormuleLabel);
		
		varWaardeBox = new JCheckBox(GeomAlgebra.rb.getString("varWaardeTekst"), false);
		varWaardeBox.setFont(theFont);
		varWaardeBox.setBackground(Color.white);
		varWaardeBox.setBounds(currentX, currentY, width, height);
		add(varWaardeBox);
		varWaardeBox.addActionListener(this);
		
		currentY += height + offset;
		
		oppWaardeBox = new JCheckBox(GeomAlgebra.rb.getString("oppWaardeTekst"), false);
		oppWaardeBox.setFont(theFont);
		oppWaardeBox.setBackground(Color.white);
		oppWaardeBox.setBounds(currentX, currentY, width, height);
		add(oppWaardeBox);
		oppWaardeBox.addActionListener(this);
		
		currentY += height + offset;

		formuleBox = new JCheckBox(GeomAlgebra.rb.getString("formuleTekst"), true);
		formuleBox.setFont(theFont);
		formuleBox.setBackground(Color.white);
		formuleBox.setBounds(currentX, currentY, width, height);
		add(formuleBox);
		formuleBox.addActionListener(this);
		
		currentY += height + offset;

		constructieToolsBox = new JCheckBox(GeomAlgebra.rb.getString("constructieToolsTekst"), true);
		constructieToolsBox.setFont(theFont);
		constructieToolsBox.setBackground(Color.white);
		constructieToolsBox.setBounds(currentX, currentY, width, height);
		add(constructieToolsBox);
		constructieToolsBox.addActionListener(this);
		
		currentY += height + offset;

		alleenOppervlaktesBox = new JCheckBox(GeomAlgebra.rb.getString("alleenOppervlaktesTekst"), false);
		alleenOppervlaktesBox.setFont(theFont);
		alleenOppervlaktesBox.setBackground(Color.white);
		alleenOppervlaktesBox.setBounds(currentX, currentY, width, height);
		add(alleenOppervlaktesBox);
		alleenOppervlaktesBox.addActionListener(this);
		
		currentY += height + offset;

		werkbladBox = new JCheckBox(GeomAlgebra.rb.getString("werkbladTekst"), false);
		werkbladBox.setFont(theFont);
		werkbladBox.setBackground(Color.white);
		werkbladBox.setBounds(currentX, currentY, width, height);
		add(werkbladBox);
		werkbladBox.addActionListener(this);
		
		currentY += height + offset;

		oppervlaktesZichtbaarBox = new JCheckBox(GeomAlgebra.rb.getString("oppervlaktesZichtbaarTekst"), true);
		oppervlaktesZichtbaarBox.setFont(theFont);
		oppervlaktesZichtbaarBox.setBackground(Color.white);
		oppervlaktesZichtbaarBox.setBounds(currentX, currentY, width, height);
		add(oppervlaktesZichtbaarBox);
		oppervlaktesZichtbaarBox.addActionListener(this);
		
		currentY += height + offset;

		lengtesBreedtesZichtbaarBox = new JCheckBox(GeomAlgebra.rb.getString("lengtesBreedtesZichtbaarTekst"), true);
		lengtesBreedtesZichtbaarBox.setFont(theFont);
		lengtesBreedtesZichtbaarBox.setBackground(Color.white);
		lengtesBreedtesZichtbaarBox.setBounds(currentX, currentY, width, height);
		add(lengtesBreedtesZichtbaarBox);
		lengtesBreedtesZichtbaarBox.setEnabled(false);
		lengtesBreedtesZichtbaarBox.addActionListener(this);
		
		currentY += height + offset;

		negatieveWaardenBox = new JCheckBox(GeomAlgebra.rb.getString("negatieveWaardenTekst"), true);
		negatieveWaardenBox.setFont(theFont);
		negatieveWaardenBox.setBackground(Color.white);
		negatieveWaardenBox.setBounds(currentX, currentY, width, height);
		add(negatieveWaardenBox);
		negatieveWaardenBox.addActionListener(this);
		
		currentY += height + offset;
		
		puzzelenBox = new JCheckBox(GeomAlgebra.rb.getString("puzzelenTekst"), false);
		puzzelenBox.setFont(theFont);
		puzzelenBox.setBackground(Color.white);
		puzzelenBox.setBounds(currentX, currentY, width, height);
		add(puzzelenBox);
		puzzelenBox.addActionListener(this);
		
		currentY += height + 2 * offset;
		
		kijkNaActiefBox = new JCheckBox(GeomAlgebra.rb.getString("kijkNaActiefTekst"), false);
		kijkNaActiefBox.setFont(theFont);
		kijkNaActiefBox.setBackground(Color.white);
		kijkNaActiefBox.setBounds(currentX, currentY, width, height);
		add(kijkNaActiefBox);
		kijkNaActiefBox.addActionListener(this);
		
		currentY += height + offset / 2;
		
		naKijkGroup = new ButtonGroup();

		equivalentButton = new JRadioButton(GeomAlgebra.rb.getString("equivalentTekst"), true);
		equivalentButton.setFont(theFont);
		equivalentButton.setBackground(Color.white);
		equivalentButton.setBounds(currentX + offset, currentY, width - offset, height);
		equivalentButton.setVisible(false);
		add(equivalentButton);
		equivalentButton.addActionListener(this);
		naKijkGroup.add(equivalentButton);
		
		currentY += height;
		
		gelijkButton = new JRadioButton(GeomAlgebra.rb.getString("gelijkTekst"), false);
		gelijkButton.setFont(theFont);
		gelijkButton.setBackground(Color.white);
		gelijkButton.setBounds(currentX + offset, currentY, width - offset, height);
		gelijkButton.setVisible(false);
		add(gelijkButton);
		gelijkButton.addActionListener(this);
		naKijkGroup.add(gelijkButton);

		currentY += height + offset;
		
/*		
		antwoordFormuleButton = new JButton(GeomAlgebra.rb.getString("antwoordFormuleTekst"));
		antwoordFormuleButton.setFont(theFont);
		int w = theFM.stringWidth(antwoordFormuleButton.getText()) + 40;
		currentX = (editWidth - w) / 2;
		antwoordFormuleButton.setBounds(currentX, currentY, w, 3 * theFM.getHeight() / 2);
		//antwoordFormuleButton.setVisible(false);
		//add(antwoordFormuleButton);
		antwoordFormuleButton.addActionListener(this);
*/		
		//currentY += height + offset;		
		
		maxScoreLabel = new JLabel(GeomAlgebra.rb.getString("scoreMaxTekst"));
		maxScoreLabel.setFont(theFont);
		maxScoreLabel.setBackground(Color.white);
		width = theFM.stringWidth(maxScoreLabel.getText());
		maxScoreLabel.setBounds(currentX + offset, currentY + 3, width, theFM.getHeight());
		maxScoreLabel.setVisible(false);
		add(maxScoreLabel);
		
		currentY += height; // + offset;
		
		maxScoreVeld = new JTextField("" + scoreMax);
		maxScoreVeld.setFont(theFont);
		maxScoreVeld.setBackground(Color.white);
		width = theFM.stringWidth("XXXXXX");
		maxScoreVeld.setBounds(currentX + 2 * offset, currentY, width, height);
				//maxScoreLabel.getLocation().x + maxScoreLabel.getSize().width + offset,
				//currentY, width, height);
		add(maxScoreVeld);
		//maxScoreVeld.setEditable(false);
		maxScoreVeld.setVisible(false);

		maxScoreVeld.addKeyListener(new InputKL2(maxScoreVeld));
		maxScoreVeld.addActionListener(new TextAL2(maxScoreVeld));
		maxScoreVeld.addFocusListener(new TextFL2(maxScoreVeld));
		
		componentsCreated = true;
	}	
	
	public void plaatsComponenten()
	{
		if (componentsCreated)
		{	
			varWaardeBox.setLocation(gaip.getSize().width + offset, varWaardeBox.getLocation().y);
			oppWaardeBox.setLocation(gaip.getSize().width + offset, oppWaardeBox.getLocation().y);
			formuleBox.setLocation(gaip.getSize().width + offset, formuleBox.getLocation().y);
			constructieToolsBox.setLocation(gaip.getSize().width + offset, constructieToolsBox.getLocation().y);
			alleenOppervlaktesBox.setLocation(gaip.getSize().width + offset, alleenOppervlaktesBox.getLocation().y);
			werkbladBox.setLocation(gaip.getSize().width + offset, werkbladBox.getLocation().y);
			oppervlaktesZichtbaarBox.setLocation(gaip.getSize().width + offset, oppervlaktesZichtbaarBox.getLocation().y);
			lengtesBreedtesZichtbaarBox.setLocation(gaip.getSize().width + offset, lengtesBreedtesZichtbaarBox.getLocation().y);
			negatieveWaardenBox.setLocation(gaip.getSize().width + offset, negatieveWaardenBox.getLocation().y);			
			puzzelenBox.setLocation(gaip.getSize().width + offset, puzzelenBox.getLocation().y);
			
			kijkNaActiefBox.setLocation(gaip.getSize().width + offset, kijkNaActiefBox.getLocation().y);
			equivalentButton.setLocation(gaip.getSize().width + 2 * offset, equivalentButton.getLocation().y);
			gelijkButton.setLocation(gaip.getSize().width + 2 * offset, gelijkButton.getLocation().y);
						
			maxScoreLabel.setLocation(gaip.getSize().width + 2 * offset, maxScoreLabel.getLocation().y);
			maxScoreVeld.setLocation(gaip.getSize().width + 3 * offset, maxScoreVeld.getLocation().y);
			
		}
	}
	
	public void setEditState(Hashtable b)
	{
		
//System.out.println("gaiep setEditState");

		boolean varWaardeZichtbaar = false;
		boolean oppWaardeZichtbaar = false;
		boolean formuleZichtbaar = true;
		boolean constructieTools = true;
		boolean alleenOppervlaktes = false;
		boolean werkblad = false;
		boolean oppervlaktesZichtbaar = true;
		boolean lengtesBreedtesZichtbaar = true;
		boolean negatieveWaarden = true;
		boolean puzzelen = false;
		
		boolean kijkNaActief = false;
		boolean equivalent = true;
		// antwoord ophalen
		String antwoordFormuleString = "";
		int scoreMax = 0;
	

		if (b.containsKey("appletLaunchData"))
		{
//System.out.println("aLD found");

			Hashtable appletLaunchData = (Hashtable) b.get("appletLaunchData");
			
			String varWaardeZichtbaarString = "false";
			String oppWaardeZichtbaarString = "false";
			String formuleZichtbaarString = "true";
			String constructieToolsString = "true";
			String alleenOppervlaktesString = "false";
			
			if (appletLaunchData.containsKey("varWaarde"))
				varWaardeZichtbaarString = (String) appletLaunchData.get("varWaarde");
			if (varWaardeZichtbaarString.equals("true") || varWaardeZichtbaarString.equals("yes"))
				varWaardeZichtbaar = true;
			if (appletLaunchData.containsKey("oppWaarde"))
				oppWaardeZichtbaarString = (String) appletLaunchData.get("oppWaarde");
			if (oppWaardeZichtbaarString.equals("true") || oppWaardeZichtbaarString.equals("yes"))
				oppWaardeZichtbaar = true;
			if (appletLaunchData.containsKey("formule"))
				formuleZichtbaarString = (String) appletLaunchData.get("formule");
			if (formuleZichtbaarString.equals("false") || formuleZichtbaarString.equals("no"))
				formuleZichtbaar = false;
			if (appletLaunchData.containsKey("constructieTools"))
				constructieToolsString = (String) appletLaunchData.get("constructieTools");
			if (constructieToolsString.equals("false") || constructieToolsString.equals("no"))
				constructieTools = false;
			if (appletLaunchData.containsKey("alleenOppervlaktes"))
				alleenOppervlaktesString = (String) appletLaunchData.get("alleenOppervlaktes");
			if (alleenOppervlaktesString.equals("true") || alleenOppervlaktesString.equals("yes"))
				alleenOppervlaktes = true;
						
			

		}
		else
		{
			if (b.containsKey("varWaardeZichtbaar"))
				varWaardeZichtbaar = ((Boolean) b.get("varWaardeZichtbaar")).booleanValue();
			if (b.containsKey("oppWaardeZichtbaar"))
				oppWaardeZichtbaar = ((Boolean) b.get("oppWaardeZichtbaar")).booleanValue();
			if (b.containsKey("formuleZichtbaar"))
				formuleZichtbaar = ((Boolean) b.get("formuleZichtbaar")).booleanValue();
			if (b.containsKey("constructieTools"))
				constructieTools = ((Boolean) b.get("constructieTools")).booleanValue();
			if (b.containsKey("alleenOppervlaktes"))
				alleenOppervlaktes = ((Boolean) b.get("alleenOppervlaktes")).booleanValue();
			if (b.containsKey("werkblad"))
				werkblad = ((Boolean) b.get("werkblad")).booleanValue();
			if (b.containsKey("oppervlaktesZichtbaar"))
				oppervlaktesZichtbaar = ((Boolean) b.get("oppervlaktesZichtbaar")).booleanValue();
			if (b.containsKey("lengtesBreedtesZichtbaar"))
				lengtesBreedtesZichtbaar = ((Boolean) b.get("lengtesBreedtesZichtbaar")).booleanValue();
			if (b.containsKey("negatieveWaarden"))
				negatieveWaarden = ((Boolean) b.get("negatieveWaarden")).booleanValue();
			if (b.containsKey("puzzelen"))
				puzzelen = ((Boolean) b.get("puzzelen")).booleanValue();
			
			
			if (b.containsKey("kijkNaActief"))
				kijkNaActief = ((Boolean) b.get("kijkNaActief")).booleanValue();
			if (b.containsKey("equivalent"))
				equivalent = ((Boolean) b.get("equivalent")).booleanValue();

//System.out.println("equivalent = " + equivalent);			
			
			if (b.containsKey("antwoordFormuleString"))
			{	antwoordFormuleString = (String) b.get("antwoordFormuleString");
			}

			if (b.containsKey("scoreMax"))
				scoreMax = ((Integer) b.get("scoreMax")).intValue();

		}
		
		varWaardeBox.setSelected(varWaardeZichtbaar);
		oppWaardeBox.setSelected(oppWaardeZichtbaar);
		formuleBox.setSelected(formuleZichtbaar);
		constructieToolsBox.setSelected(constructieTools);
		alleenOppervlaktesBox.setSelected(alleenOppervlaktes);
		werkbladBox.setSelected(werkblad);
		oppervlaktesZichtbaarBox.setSelected(oppervlaktesZichtbaar);
		lengtesBreedtesZichtbaarBox.setSelected(lengtesBreedtesZichtbaar || constructieTools);
		lengtesBreedtesZichtbaarBox.setEnabled(!constructieTools);
		negatieveWaardenBox.setSelected(negatieveWaarden);
		puzzelenBox.setSelected(puzzelen);
		
		kijkNaActiefBox.setSelected(kijkNaActief);
		equivalentButton.setSelected(equivalent);
		gelijkButton.setSelected(!equivalent);
		
//System.out.println("set afs = " + antwoordFormuleString);		
		antwoordFormulePanel.zetExpressieString(antwoordFormuleString);
		
		maxScoreVeld.setText("" + scoreMax);
		
		equivalentButton.setVisible(kijkNaActiefBox.isSelected());
		gelijkButton.setVisible(kijkNaActiefBox.isSelected());
		maxScoreLabel.setVisible(kijkNaActiefBox.isSelected());
		maxScoreVeld.setVisible(kijkNaActiefBox.isSelected());
		
		
		if (b.containsKey("gaipBreedte"))
			gaipBreedte = ((Integer) b.get("gaipBreedte")).intValue();
		if (b.containsKey("gaipHoogte"))
			gaipHoogte = ((Integer) b.get("gaipHoogte")).intValue();
		
		setBounds(getLocation().x, getLocation().y, gaipBreedte + editWidth, Math.max(gaipHoogte, editHeight));
		
		if (kijkNaActiefBox.isSelected())
		{
			antwoordFormulePanel.setVisible(true);
			doelFormuleLabel.setVisible(true);
		}
		
		// HIER !!
		gaip.setEditState(b);		
		
	}
	
	public Hashtable getEditState()
	{
//System.out.println("gaiep getEditState");

		Hashtable h = gaip.getEditState(); 
		
	
//System.out.println("get afs = " + antwoordFormulePanel.getExpressieString());
//System.out.println("get afscorr = " + antwoordFormulePanel.getCorrectExpressieString());
		h.put("antwoordFormuleString", antwoordFormulePanel.getExpressieString());
		h.put("antwoordFormuleStringCorrect", antwoordFormulePanel.getCorrectExpressieString());
		
		h.put("scoreMax", new Integer(scoreMax));
		
		h.put("gaipBreedte", new Integer(gaipBreedte));
		h.put("gaipHoogte", new Integer(gaipHoogte));
		
		return h;
		
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		if (noSetBounds)
		{	noSetBounds = false;
			return;
		}
//System.out.println("spiep setBounds raw " + x + " " + y + " " + b + " " + h);

		if ((h <= 1) || (x < 0) || (b <= 1))
			return;
		
		super.setBounds(x, y, gaipBreedte + editWidth, Math.max(gaipHoogte, editHeight));
		
//System.out.println("spiep setBounds " + x + " " + y + " " + (spipBreedte + editWidth) + " " + 
//					Math.max(spipHoogte, editHeight));
	
		if (gaip != null)
		{	gaip.setBounds(0, 0, gaipBreedte, gaipHoogte);
		
			doelFormuleLabel.setLocation(gaip.kijkNaPanel.getLocation().x + 
					                     gaip.kijkNaButton.getSize().width + offset, 
					                     gaipHoogte - doelFormuleLabel.getSize().height);
		
			antwoordFormulePanel.setLocation(doelFormuleLabel.getLocation().x + 
											 doelFormuleLabel.getSize().width + offset, 
					                         gaipHoogte - ePanelHeight - 1);
			antwoordFormulePanel.setSize(gaip.getSize().width - antwoordFormulePanel.getLocation().x,
					                     ePanelHeight);		
		}
		
		//antwoordFormulePanel.setLocation(gaipBreedte / 2, gaipHoogte - ePanelHeight);
		
		plaatsComponenten();
		
//System.out.println("setBounds " + x + " " + y + " " + b + " " + h);		
		
	}
	
	public void zetBreedte(int b)
	{	
		gaipBreedte = b;
		
		setBounds(getLocation().x, getLocation().y, gaipBreedte + editWidth, Math.max(gaipHoogte, editHeight));		
		plaatsComponenten();
	}
	
	public void zetHoogte(int h)
	{	
		gaipHoogte = h;
		
		setBounds(getLocation().x, getLocation().y, gaipBreedte + editWidth, Math.max(gaipHoogte, editHeight));		
	}
	
	public void wis()
	{}
    
	public void zetMode(int mode)
	{}
	
    public void stop()
    {}
    
    public void start()
    {}
    
    public void addActionListener(ActionListener al)
    {}
    
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == varWaardeBox)
		{
			gaip.zetVarWaardeZichtbaar(varWaardeBox.isSelected());
		}
		else if (e.getSource() == oppWaardeBox)
		{
			gaip.zetOppWaardeZichtbaar(oppWaardeBox.isSelected());
		}
		else if (e.getSource() == formuleBox)
		{
			gaip.zetFormuleZichtbaar(formuleBox.isSelected());
		}
		else if (e.getSource() == constructieToolsBox)
		{
			gaip.zetConstructieTools(constructieToolsBox.isSelected());
			lengtesBreedtesZichtbaarBox.setSelected(true);
			gaip.zetLengtesBreedtesZichtbaar(lengtesBreedtesZichtbaarBox.isSelected());
			lengtesBreedtesZichtbaarBox.setEnabled(!constructieToolsBox.isSelected());
		}
		else if (e.getSource() == alleenOppervlaktesBox)
		{
			gaip.zetAlleenOppervlaktes(alleenOppervlaktesBox.isSelected());
		}
		else if (e.getSource() == werkbladBox)
		{
			gaip.zetWerkblad(werkbladBox.isSelected());
		}
		else if (e.getSource() == oppervlaktesZichtbaarBox)
		{
			gaip.zetOppervlaktesZichtbaar(oppervlaktesZichtbaarBox.isSelected());
		}
		else if (e.getSource() == lengtesBreedtesZichtbaarBox)
		{
			gaip.zetLengtesBreedtesZichtbaar(lengtesBreedtesZichtbaarBox.isSelected());
		}
		else if (e.getSource() == negatieveWaardenBox)
		{
			gaip.zetNegatieveWaarden(negatieveWaardenBox.isSelected());
		}
		else if (e.getSource() == puzzelenBox)
		{
			gaip.zetPuzzelen(puzzelenBox.isSelected());
		}
		
		
		else if (e.getSource() == kijkNaActiefBox)
		{
			gaip.zetKijkNaActief(kijkNaActiefBox.isSelected());
			doelFormuleLabel.setVisible(kijkNaActiefBox.isSelected());
			antwoordFormulePanel.setVisible(kijkNaActiefBox.isSelected());
			
			
			equivalentButton.setVisible(kijkNaActiefBox.isSelected());
			gelijkButton.setVisible(kijkNaActiefBox.isSelected());
			maxScoreLabel.setVisible(kijkNaActiefBox.isSelected());
			maxScoreVeld.setVisible(kijkNaActiefBox.isSelected());
			
			if(!kijkNaActiefBox.isSelected()) scoreMax = 0;
		}
		else if (e.getSource() == equivalentButton)
		{
			gaip.zetEquivalent(equivalentButton.isSelected());
		}
		else if (e.getSource() == gelijkButton)
		{
			gaip.zetEquivalent(equivalentButton.isSelected());
		}
					

			
		
	}

	class TextFL2 implements FocusListener
	{		
		JTextField inputTextField;
		
		public TextFL2(JTextField input)
		{	inputTextField = input;
		}

		public void focusGained(FocusEvent e)
		{
		}
		public void focusLost(FocusEvent e)
		{	// invoer user
			String text = inputTextField.getText();
			String oldText = "" + scoreMax;
			
			inputTextField.setText(text);				

			String format = new String(text);		
			format = format.replace(',', '.');		

			double userInput = 0;
			boolean error = false;
			try
			{	userInput = Double.parseDouble(format);
			}
			catch (NumberFormatException nfe)
			{	error = true;
//System.out.println("nfe");			
			}
			// dit zou niet moeten gebeuren
			// Peter: nu wel bij de definitie van een random variabele ipv een double			
			if (error)
				return;

			if (inputTextField == maxScoreVeld)
			{	
			
				int mScore = (int) userInput;

				if ((mScore >= 1) && (mScore <= 1500))
				{
					scoreMax = mScore;
				}
				else
				{
					inputTextField.setText(oldText);
				}
			}
			
		} // focusLost
	}


	class TextAL2 implements ActionListener
	{	
		JTextField inputTextField;
		
		public TextAL2(JTextField input)
		{	inputTextField = input;
		}
		
		public void actionPerformed(ActionEvent e)
		{	
			String text = inputTextField.getText();
			String oldText = "" + scoreMax;
			
			inputTextField.setText(text);				

			String format = new String(text);		
			format = format.replace(',', '.');		

			double userInput = 0;
			boolean error = false;
			try
			{	userInput = Double.parseDouble(format);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			// dit zou niet moeten gebeuren  
			// Peter: nu wel bij de definitie van een random variabele ipv een double
			if (error)
			{	return;
			}

			
			if (inputTextField == maxScoreVeld)
			{	
			
				int mScore = (int) userInput;

				if ((mScore >= 1) && (mScore <= 1500))
				{
					scoreMax = mScore;
				}
				else
				{
					inputTextField.setText(oldText);
				}
			}
			
			
		} // actionPerformed
	}
	
	public String trimTrailingZeros(String s, char decSep)
	{	String txt = new String(s);
		if (txt.indexOf(decSep) < 0)
			return txt;
		char c = txt.charAt(txt.length() - 1);
		while (c == '0')
		{	txt = removeCharAt(txt, txt.length() - 1);
			c = txt.charAt(txt.length() - 1);
		}	
		c = txt.charAt(txt.length() - 1);
		if (c == decSep)
			txt = removeCharAt(txt, txt.length() - 1);
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

	class InputKL2 extends KeyAdapter
	{	
		JTextField inputTextField;
		
		public InputKL2(JTextField input)
		{	inputTextField = input;
		}
		
		public void keyReleased(KeyEvent e)
		{	
			inputTextField.setForeground(Color.black);
		
			String txt = inputTextField.getText();
			
//System.out.println(txt);
				
			boolean corrected = false;

			// kijk of txt illegale characters bevat
			// dit zou er maximaal 1 moeten zijn
			int index = -1;
			for (int cCnt = 0; cCnt < txt.length(); cCnt++)
			{	char c = txt.charAt(cCnt);
				if (!isLegal(c))
				{	index = cCnt;
//System.out.println("illegal " + index);				
				}
			}	
			// verwijder illegaal karakter
			if (index >= 0)
			{	txt = removeCharAt(txt, index);
				corrected = true;
//System.out.println("corr " + txt);							
			}
			
//System.out.println(txt);			
			
			// leading zeros, leiden niet tot een NumberFormatException	
			// geen minteken
			if ((txt.indexOf('-') < 0) && (txt.length() >= 2) &&
				(txt.charAt(0) == '0') && Character.isDigit(txt.charAt(1)))
			{	txt = removeCharAt(txt, 0);
				corrected = true;
			}
			
			// trailing zeros na(!) decimale punt oplossen 
			// bij actionPerformed of focusLost			

			if (corrected)
			{	
//System.out.println("corr " + txt);							
				inputTextField.setText(txt);
			
			}
			
		}
		
		
		public boolean isLegal(char c)
		{	return Character.isDigit(c);
		}
	}	
	
}
