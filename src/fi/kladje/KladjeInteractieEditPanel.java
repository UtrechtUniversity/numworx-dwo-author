package fi.kladje;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;
import fi.wiskopdr.ObjectiveChoiceButton;

public class KladjeInteractieEditPanel extends JPanel implements InteractieEditPanel,
																	ActionListener	
{	
	int editWidth = 250;
	int editHeight = 500; 
	int klipBreedte = 500; // startbreedte spip
	int klipHoogte = 450; // starthoogte spip
	
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	int offset = 10;
	boolean componentsCreated = false;
	
	boolean noSetBounds = false;	

	protected KladjeInteractiePanel klip;

	int scoreMax = 0;
	
	JCheckBox kleurkeuzeBox;
	ButtonGroup achtergrondGroep;
	JRadioButton blancoButton, lijnenButton, ruitjes20Button, ruitjes40Button, ruitjes80Button; 
	JCheckBox lijnTekenenBox, rechthoekTekenenBox, cirkelTekenenBox, tekstTekenenBox, formuleOptieBox, ivmOptieBox;
	JCheckBox roterenBox, schalenBox;
	
	private JCheckBox checkCB;
	private JLabel scoreLabel;
	private JTextField scoreTF;
	private ObjectiveChoiceButton objectiveBtn;
	
	private boolean check;
	
	
	JLabel translationLabel;
	JTextField translationXTF, translationYTF;
	JLabel scaleLabel;
	JTextField scaleTF;
	FormuleInstellingenButton fiButton;
	
	public KladjeInteractieEditPanel()
	{
		setLayout(null);
		//setOpaque(false);

//System.out.println("kliep " + getBackground().toString());

		klip = new KladjeInteractiePanel();
		add(klip);
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		
		int width = editWidth - 2 * offset;
		int height = 3 * theFM.getHeight() / 2;
		int currentX = klip.getSize().width + offset;
		int currentX2 = offset;
		int currentY = offset;
	
		kleurkeuzeBox = new JCheckBox(Kladje.rb.getString("kleurkeuzeTekst"), true);
		kleurkeuzeBox.setFont(theFont);
		kleurkeuzeBox.setBackground(Color.white);
		kleurkeuzeBox.setBounds(currentX, currentY, width, height);
		add(kleurkeuzeBox);
		kleurkeuzeBox.addActionListener(this);
		
		currentY += height + 2 * offset;
		
		achtergrondGroep = new ButtonGroup();
		
		blancoButton = new JRadioButton(Kladje.rb.getString("blancoTekst"), true);
		blancoButton.setFont(theFont);
		blancoButton.setBackground(Color.white);
		blancoButton.setBounds(currentX, currentY, width, height);
		add(blancoButton);
		blancoButton.addActionListener(this);
		achtergrondGroep.add(blancoButton);
		
		currentY += height;// + offset / 3;		
		
		lijnenButton = new JRadioButton(Kladje.rb.getString("lijnenTekst"), false);
		lijnenButton.setFont(theFont);
		lijnenButton.setBackground(Color.white);
		lijnenButton.setBounds(currentX, currentY, width, height);
		add(lijnenButton);
		lijnenButton.addActionListener(this);
		achtergrondGroep.add(lijnenButton);
		
		currentY += height;// + offset / 3;		
		
		ruitjes20Button = new JRadioButton(Kladje.rb.getString("ruitjesTekst"), false);
		ruitjes20Button.setFont(theFont);
		ruitjes20Button.setBackground(Color.white);
		ruitjes20Button.setBounds(currentX, currentY, width, height);
		add(ruitjes20Button);
		ruitjes20Button.addActionListener(this);
		achtergrondGroep.add(ruitjes20Button);
		
		currentY += height;// + offset / 3;
		
		ruitjes40Button = new JRadioButton(Kladje.rb.getString("ruitjes40Tekst"), false);
		ruitjes40Button.setFont(theFont);
		ruitjes40Button.setBackground(Color.white);
		ruitjes40Button.setBounds(currentX, currentY, width, height);
		add(ruitjes40Button);
		ruitjes40Button.addActionListener(this);
		achtergrondGroep.add(ruitjes40Button);
		
		currentY += height;// + offset / 3;
		
		ruitjes80Button = new JRadioButton(Kladje.rb.getString("ruitjes80Tekst"), false);
		ruitjes80Button.setFont(theFont);
		ruitjes80Button.setBackground(Color.white);
		ruitjes80Button.setBounds(currentX, currentY, width, height);
		add(ruitjes80Button);
		ruitjes80Button.addActionListener(this);
		achtergrondGroep.add(ruitjes80Button);
		
		currentY += height + 2 * offset;
		
		lijnTekenenBox = new JCheckBox(Kladje.rb.getString("lijnTekenenTekst"), true);
		lijnTekenenBox.setFont(theFont);
		lijnTekenenBox.setBackground(Color.white);
		lijnTekenenBox.setBounds(currentX, currentY, width, height);
		add(lijnTekenenBox);
		lijnTekenenBox.addActionListener(this);
		
		currentY += height;// + offset;
		
		rechthoekTekenenBox = new JCheckBox(Kladje.rb.getString("rechthoekTekenenTekst"), true);
		rechthoekTekenenBox.setFont(theFont);
		rechthoekTekenenBox.setBackground(Color.white);
		rechthoekTekenenBox.setBounds(currentX, currentY, width, height);
		add(rechthoekTekenenBox);
		rechthoekTekenenBox.addActionListener(this);
		
		currentY += height;// + offset;

		cirkelTekenenBox = new JCheckBox(Kladje.rb.getString("cirkelTekenenTekst"), true);
		cirkelTekenenBox.setFont(theFont);
		cirkelTekenenBox.setBackground(Color.white);
		cirkelTekenenBox.setBounds(currentX, currentY, width, height);
		add(cirkelTekenenBox);
		cirkelTekenenBox.addActionListener(this);
		
		currentY += height;// + offset;

		tekstTekenenBox = new JCheckBox(Kladje.rb.getString("tekstTekenenTekst"), true);
		tekstTekenenBox.setFont(theFont);
		tekstTekenenBox.setBackground(Color.white);
		tekstTekenenBox.setBounds(currentX, currentY, width, height);
		add(tekstTekenenBox);
		tekstTekenenBox.addActionListener(this);
		
		currentY += height;// + offset;

		formuleOptieBox = new JCheckBox(Kladje.rb.getString("formuleOptieTekst"), false);
		formuleOptieBox.setFont(theFont);
		formuleOptieBox.setBackground(Color.white);
		formuleOptieBox.setBounds(currentX, currentY, 120, height);
		add(formuleOptieBox);
		formuleOptieBox.setVisible(Kladje.isPremium);
		formuleOptieBox.addActionListener(this);
		
		fiButton = new FormuleInstellingenButton("settings");
		fiButton.setFont(theFont);
		fiButton.setBounds(currentX+120, currentY, 80, height);
		add(fiButton);
		fiButton.addActionListener(this);
		fiButton.setVisible(false);
		
		currentY += height + 2 * offset;

		roterenBox = new JCheckBox(Kladje.rb.getString("roterenTekst"), true);
		roterenBox.setFont(theFont);
		roterenBox.setBackground(Color.white);
		roterenBox.setBounds(currentX, currentY, width, height);
		add(roterenBox);
		roterenBox.addActionListener(this);
		
		currentY += height;// + offset;
		
		schalenBox = new JCheckBox(Kladje.rb.getString("schalenTekst"), true);
		schalenBox.setFont(theFont);
		schalenBox.setBackground(Color.white);
		schalenBox.setBounds(currentX, currentY, width, height);
		add(schalenBox);
		schalenBox.addActionListener(this);
		
		currentY += height + 2*offset;
		
		translationLabel = new JLabel("translation");
		translationLabel.setFont(theFont);
		translationLabel.setBounds(currentX, currentY, 100, height);
		//add(translationLabel);
		
		translationXTF = new JTextField("0");
		translationXTF.setFont(theFont);
		translationXTF.setBounds(currentX+100, currentY, 30, height);
		translationXTF.addActionListener(this);
		//add(translationXTF);
		
		translationYTF = new JTextField("0");
		translationYTF.setFont(theFont);
		translationYTF.setBounds(currentX+140, currentY, 30, height);
		translationYTF.addActionListener(this);
		//add(translationYTF);
		
		checkCB = new JCheckBox(Kladje.rb.getString("checkCBLabel"));
		checkCB.setFont(theFont);
		checkCB.setBounds(currentX, currentY, 200, height);
		checkCB.addActionListener(this);
		add(checkCB);
		
		currentY += height;
		
		scaleLabel = new JLabel("scale");
		scaleLabel.setFont(theFont);
		scaleLabel.setBounds(currentX, currentY, 100, height);
		//add(scaleLabel);
		
		scaleTF = new JTextField("1.0");
		scaleTF.setFont(theFont);
		scaleTF.setBounds(currentX+100, currentY, 30, height);
		scaleTF.addActionListener(this);
		//add(scaleTF);
		
		scoreLabel = new JLabel(Kladje.rb.getString("scoreLabel"));
		scoreLabel.setFont(theFont);
		scoreLabel.setBounds(currentX+30, currentY, 60, height);
		scoreLabel.setVisible(false);
		add(scoreLabel);
		
		scoreTF = new JTextField("0");
		scoreTF.setFont(theFont);
		scoreTF.setBounds(currentX+90, currentY, 40, height);
		scoreTF.setVisible(false);
		add(scoreTF);
		objectiveBtn = new ObjectiveChoiceButton();
		objectiveBtn.setBounds(currentX+90+44, currentY, objectiveBtn.getPreferredSize().width, height);
		objectiveBtn.setVisible(false);
		add(objectiveBtn);
		
		currentY += height;
		
		ivmOptieBox = new JCheckBox(Kladje.rb.getString("ivmOptieTekst"), false);
		ivmOptieBox.setFont(theFont);
		ivmOptieBox.setBackground(Color.white);
		ivmOptieBox.setBounds(currentX, currentY, width, height);
		add(ivmOptieBox);
		ivmOptieBox.addActionListener(this);
		ivmOptieBox.setVisible(Kladje.isPremium);
		
		componentsCreated = true;
	}	
	
	public void plaatsComponenten()
	{
		if (componentsCreated)
		{
			kleurkeuzeBox.setLocation(klip.getSize().width + offset, kleurkeuzeBox.getLocation().y);
			blancoButton.setLocation(klip.getSize().width + offset, blancoButton.getLocation().y);
			lijnenButton.setLocation(klip.getSize().width + offset, lijnenButton.getLocation().y);
			ruitjes20Button.setLocation(klip.getSize().width + offset, ruitjes20Button.getLocation().y);
			ruitjes40Button.setLocation(klip.getSize().width + offset, ruitjes40Button.getLocation().y);
			ruitjes80Button.setLocation(klip.getSize().width + offset, ruitjes80Button.getLocation().y);
			
			lijnTekenenBox.setLocation(klip.getSize().width + offset, lijnTekenenBox.getLocation().y);
			rechthoekTekenenBox.setLocation(klip.getSize().width + offset, rechthoekTekenenBox.getLocation().y);
			cirkelTekenenBox.setLocation(klip.getSize().width + offset, cirkelTekenenBox.getLocation().y);
			tekstTekenenBox.setLocation(klip.getSize().width + offset, tekstTekenenBox.getLocation().y);
			formuleOptieBox.setLocation(klip.getSize().width + offset, formuleOptieBox.getLocation().y);
			
			fiButton.setLocation(klip.getSize().width+120 + offset, formuleOptieBox.getLocation().y);
			
			roterenBox.setLocation(klip.getSize().width + offset, roterenBox.getLocation().y);
			schalenBox.setLocation(klip.getSize().width + offset, schalenBox.getLocation().y);
			
			translationLabel.setLocation(klip.getSize().width + offset, translationLabel.getLocation().y);
			translationXTF.setLocation(klip.getSize().width + offset+100, translationXTF.getLocation().y);
			translationYTF.setLocation(klip.getSize().width + offset+130, translationYTF.getLocation().y);
			
			scaleLabel.setLocation(klip.getSize().width + offset, scaleLabel.getLocation().y);
			scaleTF.setLocation(klip.getSize().width + offset+100, scaleTF.getLocation().y);
			ivmOptieBox.setLocation(klip.getSize().width + offset, ivmOptieBox.getLocation().y);
			
			checkCB.setLocation(klip.getSize().width + offset, checkCB.getLocation().y);
			scoreLabel.setLocation(klip.getSize().width + offset+30, scoreLabel.getLocation().y);
			scoreTF.setLocation(klip.getSize().width + offset+90, scoreTF.getLocation().y);
			objectiveBtn.setLocation(klip.getSize().width + offset+90+44, scoreTF.getLocation().y);
		}
	}
	
	public void setEditState(Hashtable b)
	{
		
//System.out.println("kliep setEditState");

		boolean kleurkeuze = true;
		if (b.containsKey("kleurkeuze"))
			kleurkeuze = ((Boolean) b.get("kleurkeuze")).booleanValue();
		kleurkeuzeBox.setSelected(kleurkeuze);
		boolean lijnen = false;
		if (b.containsKey("lijnen"))
			lijnen = ((Boolean) b.get("lijnen")).booleanValue();
		lijnenButton.setSelected(lijnen);
		boolean ruitjes = false;
		if (b.containsKey("ruitjes"))
			ruitjes = ((Boolean) b.get("ruitjes")).booleanValue();
		int ruitjessize = 20;
		if (b.containsKey("ruitjessize"))
			ruitjessize = ((Integer) b.get("ruitjessize")).intValue();
		if (ruitjessize == 40)
			ruitjes40Button.setSelected(true);
		else if (ruitjessize == 80)
			ruitjes80Button.setSelected(true);
		else
			ruitjes20Button.setSelected(true);
		
		boolean lijnTekenen = true;
		if (b.containsKey("lijnTekenen"))
			lijnTekenen = ((Boolean) b.get("lijnTekenen")).booleanValue();
		lijnTekenenBox.setSelected(lijnTekenen);
		boolean rechthoekTekenen = true;
		if (b.containsKey("rechthoekTekenen"))
			rechthoekTekenen = ((Boolean) b.get("rechthoekTekenen")).booleanValue();
		rechthoekTekenenBox.setSelected(rechthoekTekenen);
		boolean cirkelTekenen = true;
		if (b.containsKey("cirkelTekenen"))
			cirkelTekenen = ((Boolean) b.get("cirkelTekenen")).booleanValue();
		cirkelTekenenBox.setSelected(cirkelTekenen);
		boolean tekstTekenen = true;
		if (b.containsKey("tekstTekenen"))
			tekstTekenen = ((Boolean) b.get("tekstTekenen")).booleanValue();
		tekstTekenenBox.setSelected(tekstTekenen);
		boolean formuleOptie = false;
		if (b.containsKey("formuleOptie"))
			formuleOptie = ((Boolean) b.get("formuleOptie")).booleanValue() && Kladje.isPremium;
		formuleOptieBox.setSelected(formuleOptie);
		fiButton.setVisible(formuleOptie );
		boolean ivmOptie = false;
		if (b.containsKey("ivmOptie"))
			ivmOptie = ((Boolean) b.get("ivmOptie")).booleanValue()&& Kladje.isPremium;
		ivmOptieBox.setSelected(ivmOptie);
		
		boolean roteren = true;
		if (b.containsKey("roteren"))
			roteren = ((Boolean) b.get("roteren")).booleanValue();
		roterenBox.setSelected(roteren);
		boolean schalen = true;
		if (b.containsKey("schalen"))
			schalen = ((Boolean) b.get("schalen")).booleanValue();
		schalenBox.setSelected(schalen);
		
		int translationx = 0;
		if(b.containsKey("translationX"))
			translationx = ((Integer) b.get("translationX")).intValue();
		translationXTF.setText(""+translationx);
		int translationy = 0;
		if(b.containsKey("translationY"))
			translationy = ((Integer) b.get("translationY")).intValue();
		translationYTF.setText(""+translationy);
		
		double scale = 1.0;
		if(b.containsKey("scale"))
			scale = ((Double) b.get("scale")).doubleValue();
		scaleTF.setText(""+scale);
		
		if (b.containsKey("klipBreedte"))
			klipBreedte = ((Integer) b.get("klipBreedte")).intValue();
		if (b.containsKey("klipHoogte"))
			klipHoogte = ((Integer) b.get("klipHoogte")).intValue();
		
		setBounds(getLocation().x, getLocation().y, klipBreedte + editWidth, Math.max(klipHoogte, editHeight));
		
		if (b.containsKey("formuleInstellingen")) {
			Hashtable formuleInstellingen = ((Hashtable) b.get("formuleInstellingen"));
			fiButton.setInstellingen(formuleInstellingen);
		}
		
		if (b.containsKey("check")) {
			check = ((Boolean) b.get("check")).booleanValue();
			checkCB.setSelected(check);
		}
		scoreLabel.setVisible(checkCB.isSelected());
		scoreTF.setVisible(checkCB.isSelected());
		
		if (b.containsKey("scoreMax")) {
			scoreMax = ((Integer) b.get("scoreMax")).intValue();
			scoreTF.setText(""+scoreMax);
		}
		if (ObjectiveChoiceButton.hasObjectiveChoices())
		{
			objectiveBtn.setVisible(checkCB.isSelected());
			objectiveBtn.setEditState(b);
		} else 
			objectiveBtn.setVisible(false);
		
		// HIER !!
		klip.setEditState(b);		
		
	}
	
	public Hashtable getEditState()
	{
System.out.println("kliep getEditState");

		Hashtable h = klip.getEditState();
		
		try {
			scoreMax = Integer.parseInt(scoreTF.getText());
			if (scoreMax > 0 && objectiveBtn.hasObjectiveChoices())
				h.putAll(objectiveBtn.getEditState(scoreMax));
		}
		catch(Exception e) {}
		check = checkCB.isSelected();	
		
		h.put("check", new Boolean(check));
		h.put("scoreMax", new Integer(scoreMax));
		
		h.put("klipBreedte", new Integer(klipBreedte));
		h.put("klipHoogte", new Integer(klipHoogte));
		
		if(formuleOptieBox.isSelected() && fiButton.getInstellingen()!=null)
		{
			h.put("formuleInstellingen", fiButton.getInstellingen());
			h.put("premium", Boolean.TRUE); // De formule optiebox = premium.
		}
		
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
		
		super.setBounds(x, y, klipBreedte + editWidth, Math.max(klipHoogte, editHeight));
		
//System.out.println("spiep setBounds " + x + " " + y + " " + (spipBreedte + editWidth) + " " + 
//					Math.max(spipHoogte, editHeight));
	
		if (klip != null)
			klip.setBounds(0, 0, klipBreedte, klipHoogte);
		
		plaatsComponenten();
		
//System.out.println("setBounds " + x + " " + y + " " + b + " " + h);		
		
	}
	
	public void zetBreedte(int b)
	{	
		klipBreedte = b;
		
		setBounds(getLocation().x, getLocation().y, klipBreedte + editWidth, Math.max(klipHoogte, editHeight));		
		plaatsComponenten();
	}
	
	public void zetHoogte(int h)
	{	
		klipHoogte = h;
		
		setBounds(getLocation().x, getLocation().y, klipBreedte + editWidth, Math.max(klipHoogte, editHeight));		
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
		if (e.getSource() == kleurkeuzeBox)
		{
			klip.zetKleurkeuze(kleurkeuzeBox.isSelected());
		}
		else if (e.getSource() == blancoButton)
		{
			klip.zetLijnen(false);
			klip.zetRuitjes(false,20);
		}
		else if (e.getSource() == lijnenButton)
		{
			klip.zetLijnen(lijnenButton.isSelected());
		}
		else if (e.getSource() == ruitjes20Button)
		{
			klip.zetRuitjes(ruitjes20Button.isSelected(),20);
		}
		else if (e.getSource() == ruitjes40Button)
		{
			klip.zetRuitjes(ruitjes40Button.isSelected(),40);
		}
		else if (e.getSource() == ruitjes80Button)
		{
			klip.zetRuitjes(ruitjes80Button.isSelected(),80);
		}
		else if (e.getSource() == lijnTekenenBox)
		{
			klip.zetLijnTekenen(lijnTekenenBox.isSelected());
		}
		else if (e.getSource() == rechthoekTekenenBox)
		{
			klip.zetRechthoekTekenen(rechthoekTekenenBox.isSelected());
		}
		else if (e.getSource() == cirkelTekenenBox)
		{
			klip.zetCirkelTekenen(cirkelTekenenBox.isSelected());
		}
		else if (e.getSource() == tekstTekenenBox)
		{
			klip.zetTekstTekenen(tekstTekenenBox.isSelected());
		}
		else if (e.getSource() == formuleOptieBox)
		{
			klip.zetFormuleOptie(formuleOptieBox.isSelected());
			fiButton.setVisible(formuleOptieBox.isSelected());
		}
		else if (e.getSource() == ivmOptieBox)
		{
			klip.zetIvmOptie(ivmOptieBox.isSelected());
		}
		else if (e.getSource() == roterenBox)
		{
			klip.zetRoteren(roterenBox.isSelected());
		}
		else if (e.getSource() == schalenBox)
		{
			klip.zetSchalen(schalenBox.isSelected());
		}
		else if (e.getSource() == translationXTF || e.getSource() == translationYTF)
		{
			klip.zetTranslation(Integer.parseInt(translationXTF.getText()), Integer.parseInt(translationYTF.getText()));
		}
		else if (e.getSource() == scaleTF)
		{
			klip.zetScale(Double.parseDouble(scaleTF.getText()));
		}
		else if (e.getSource() == checkCB)
		{
			scoreLabel.setVisible(checkCB.isSelected());
			scoreTF.setVisible(checkCB.isSelected());
			scoreTF.setText("0");
			objectiveBtn.setVisible(checkCB.isSelected() && objectiveBtn.hasObjectiveChoices());
		}

	}

}
