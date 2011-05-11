package fi.nabouwenaanzichten;

import java.awt.event.*;
import java.awt.*;
import java.util.Hashtable;

import javax.swing.*;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
//import fi.beans.wiskopdrbeans.InteractiePanel;


public class NabouwenAanzichtenInteractieEditPanel extends JPanel
			                                       implements InteractieEditPanel, ActionListener 
{

	NabouwenAanzichtenInteractiePanel naip;
	int editWidth = 180;
	
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	int offset = 10;
	boolean componentsCreated = false;
	JCheckBox rotatieVastBox;
	JCheckBox nietBouwenSlopenBox;
	JCheckBox keuzeBouwenSlopenBox;
	JCheckBox perspectiefBox;
	ButtonGroup frontGroup;
	JRadioButton pijlButton, balkButton, geenButton;
	JCheckBox plattegrondBox;
	JLabel plattegrondLabel;
	ButtonGroup aanzichtGroup;
	JRadioButton bouwselButton, drieButton, bovenButton, voorButton, rechtsButton;
	JLabel roosterLabel;
	JTextField roosterTextField;
	
	
	public NabouwenAanzichtenInteractieEditPanel()
	{
		setLayout(null);
		setBackground(new Color(230, 240, 255));
		
		naip = new NabouwenAanzichtenInteractiePanel();
		
//System.out.println("naip w = " + naip.getSize().width);
//System.out.println("naip h = " + naip.getSize().height);

		setSize(naip.getSize().width + editWidth, naip.getSize().height);
		naip.setLocation(0, 0);
		add(naip);

//System.out.println("naiep w = " + getSize().width);
//System.out.println("naiep h = " + getSize().height);
		
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		int width = 0;
		int height = 3 * theFM.getHeight() / 2;
		int currentX = naip.getSize().width + offset;
		int currentY = offset;
		
		rotatieVastBox = new JCheckBox(NabouwenAanzichten.rb.getString("rotatieVast"));
		rotatieVastBox.setFont(theFont);
		rotatieVastBox.setBackground(Color.white);
		width = theFM.stringWidth(rotatieVastBox.getText()) + 40;
		rotatieVastBox.setBounds(currentX, currentY, width, height);
		add(rotatieVastBox);
		rotatieVastBox.addActionListener(this);
		currentY += height + offset / 2;
		
		nietBouwenSlopenBox = new JCheckBox(NabouwenAanzichten.rb.getString("isVoorbeeld"));
		nietBouwenSlopenBox.setFont(theFont);
		nietBouwenSlopenBox.setBackground(Color.white);
		width = theFM.stringWidth(nietBouwenSlopenBox.getText()) + 40;
		nietBouwenSlopenBox.setBounds(currentX, currentY, width, height);
		add(nietBouwenSlopenBox);
		nietBouwenSlopenBox.addActionListener(this);
		
		currentY += height + offset / 2;
		
		keuzeBouwenSlopenBox = new JCheckBox(NabouwenAanzichten.rb.getString("bouwSloopKeuze"));
		keuzeBouwenSlopenBox.setFont(theFont);
		keuzeBouwenSlopenBox.setBackground(Color.white);
		width = theFM.stringWidth(keuzeBouwenSlopenBox.getText()) + 35;
		keuzeBouwenSlopenBox.setBounds(currentX, currentY, width, height);
		add(keuzeBouwenSlopenBox);
		keuzeBouwenSlopenBox.addActionListener(this);
		
		currentY += height + offset / 2;
		
		perspectiefBox = new JCheckBox(NabouwenAanzichten.rb.getString("perspectief"));
		perspectiefBox.setFont(theFont);
		perspectiefBox.setBackground(Color.white);
		perspectiefBox.setSelected(true);
		width = theFM.stringWidth(perspectiefBox.getText()) + 40;
		perspectiefBox.setBounds(currentX, currentY, width, height);
		add(perspectiefBox);
		perspectiefBox.addActionListener(this);
		
		currentY += height + offset;

		frontGroup = new ButtonGroup();

		pijlButton = new JRadioButton(NabouwenAanzichten.rb.getString("zwartePijl"), true);
		frontGroup.add(pijlButton);
		pijlButton.setFont(theFont);
		pijlButton.setBackground(Color.white);
		width = theFM.stringWidth(pijlButton.getText()) + 40;
		pijlButton.setBounds(currentX, currentY, width, height);
		add(pijlButton);
		pijlButton.addActionListener(this);
		
		currentY += height; // + offset / 5;		

		balkButton = new JRadioButton(NabouwenAanzichten.rb.getString("rodeBalk"));
		frontGroup.add(balkButton);
		balkButton.setFont(theFont);
		balkButton.setBackground(Color.white);
		width = theFM.stringWidth(balkButton.getText()) + 40;
		balkButton.setBounds(currentX, currentY, width, height);
		add(balkButton);
		balkButton.addActionListener(this);
		
		currentY += height; // + offset / 5;		

		geenButton = new JRadioButton(NabouwenAanzichten.rb.getString("geenVoorkant"));
		frontGroup.add(geenButton);
		geenButton.setFont(theFont);
		geenButton.setBackground(Color.white);
		width = theFM.stringWidth(geenButton.getText()) + 35;
		geenButton.setBounds(currentX, currentY, width, height);
		add(geenButton);
		geenButton.addActionListener(this);
		
		currentY += height + offset;		
		
		plattegrondBox = new JCheckBox(NabouwenAanzichten.rb.getString("plattegrond"));
		plattegrondBox.setFont(theFont);
		plattegrondBox.setBackground(Color.white);
		width = theFM.stringWidth(plattegrondBox.getText()) + 35;
		plattegrondBox.setBounds(currentX, currentY, width, theFM.getHeight());
		add(plattegrondBox);
		plattegrondBox.addActionListener(this);
		
		currentY += theFM.getHeight();

		plattegrondLabel = new JLabel(NabouwenAanzichten.rb.getString("plattegrond2"));
		plattegrondLabel.setFont(theFont);
		plattegrondLabel.setBackground(Color.white);
		width = theFM.stringWidth(plattegrondLabel.getText());
		plattegrondLabel.setBounds(currentX, currentY, width, theFM.getHeight());
		add(plattegrondLabel);
		
		currentY += height + offset;
		
		aanzichtGroup = new ButtonGroup();

		bouwselButton = new JRadioButton(NabouwenAanzichten.rb.getString("blokkenbouwsel"), true);
		aanzichtGroup.add(bouwselButton);
		bouwselButton.setFont(theFont);
		bouwselButton.setBackground(Color.white);
		width = theFM.stringWidth(bouwselButton.getText()) + 40;
		bouwselButton.setBounds(currentX, currentY, width, height);
		add(bouwselButton);
		bouwselButton.addActionListener(this);
		
		currentY += height; // + offset / 5;		
		
		drieButton = new JRadioButton(NabouwenAanzichten.rb.getString("3Aanzichten"));
		aanzichtGroup.add(drieButton);
		drieButton.setFont(theFont);
		drieButton.setBackground(Color.white);
		width = theFM.stringWidth(drieButton.getText()) + 40;
		drieButton.setBounds(currentX, currentY, width, height);
		add(drieButton);
		drieButton.addActionListener(this);
		
		currentY += height; // + offset / 5;		

		bovenButton = new JRadioButton(NabouwenAanzichten.rb.getString("bovenAanzicht"));
		aanzichtGroup.add(bovenButton);
		bovenButton.setFont(theFont);
		bovenButton.setBackground(Color.white);
		width = theFM.stringWidth(bovenButton.getText()) + 40;
		bovenButton.setBounds(currentX, currentY, width, height);
		add(bovenButton);
		bovenButton.addActionListener(this);
		
		currentY += height; // + offset / 5;		

		voorButton = new JRadioButton(NabouwenAanzichten.rb.getString("voorAanzicht"));
		aanzichtGroup.add(voorButton);
		voorButton.setFont(theFont);
		voorButton.setBackground(Color.white);
		width = theFM.stringWidth(voorButton.getText()) + 40;
		voorButton.setBounds(currentX, currentY, width, height);
		add(voorButton);
		voorButton.addActionListener(this);
		
		currentY += height; // + offset / 5;		

		rechtsButton = new JRadioButton(NabouwenAanzichten.rb.getString("rechtsAanzicht"));
		aanzichtGroup.add(rechtsButton);
		rechtsButton.setFont(theFont);
		rechtsButton.setBackground(Color.white);
		width = theFM.stringWidth(rechtsButton.getText()) + 40;
		rechtsButton.setBounds(currentX, currentY, width, height);
		add(rechtsButton);
		rechtsButton.addActionListener(this);
		
		currentY += height + offset;
		
		roosterLabel = new JLabel(NabouwenAanzichten.rb.getString("roosterGrootte"));
		roosterLabel.setFont(theFont);
		roosterLabel.setBackground(Color.white);
		width = theFM.stringWidth(roosterLabel.getText());
		roosterLabel.setBounds(currentX, currentY, width, theFM.getHeight());
		add(roosterLabel);
		
		currentY += height; // + offset;

		roosterTextField = new JTextField("4");
		roosterTextField.setFont(theFont);
		//roosterLabel.setBackground(Color.white);
		width = theFM.stringWidth("XXXXXX");
		roosterTextField.setBounds(currentX + 2 * offset, currentY, width, height);
		add(roosterTextField);

		roosterTextField.addKeyListener(new InputKL(roosterTextField, false, true));
		roosterTextField.addActionListener(new TextAL(roosterTextField));
		roosterTextField.addFocusListener(new TextFL(roosterTextField));
		
		currentY += height + offset;
		
		
		componentsCreated = true;

		
//System.out.println(getBackground().toString());		
		
	}
	
	public void plaatsComponenten()
	{
		if (componentsCreated)
		{	rotatieVastBox.setLocation(naip.getSize().width + offset, rotatieVastBox.getLocation().y);
			nietBouwenSlopenBox.setLocation(naip.getSize().width + offset, nietBouwenSlopenBox.getLocation().y);
			keuzeBouwenSlopenBox.setLocation(naip.getSize().width + offset, keuzeBouwenSlopenBox.getLocation().y);
			perspectiefBox.setLocation(naip.getSize().width + offset, perspectiefBox.getLocation().y);
			
			pijlButton.setLocation(naip.getSize().width + offset, pijlButton.getLocation().y);
			balkButton.setLocation(naip.getSize().width + offset, balkButton.getLocation().y);
			geenButton.setLocation(naip.getSize().width + offset, geenButton.getLocation().y);
			
			plattegrondBox.setLocation(naip.getSize().width + offset, plattegrondBox.getLocation().y);
			plattegrondLabel.setLocation(naip.getSize().width + offset, plattegrondLabel.getLocation().y);
			
			bouwselButton.setLocation(naip.getSize().width + offset, bouwselButton.getLocation().y);
			drieButton.setLocation(naip.getSize().width + offset, drieButton.getLocation().y);
			bovenButton.setLocation(naip.getSize().width + offset, bovenButton.getLocation().y);
			voorButton.setLocation(naip.getSize().width + offset, voorButton.getLocation().y);
			rechtsButton.setLocation(naip.getSize().width + offset, rechtsButton.getLocation().y);
			
			roosterLabel.setLocation(naip.getSize().width + offset, roosterLabel.getLocation().y);
			roosterTextField.setLocation(naip.getSize().width + 3 * offset, roosterTextField.getLocation().y);
			
			
		}
	}
	
	
	public void setEditState(Hashtable b)
	{	
		boolean rotatieVast = false;
		if (b.containsKey("rotatieVast"))
			rotatieVast = ((Boolean) b.get("rotatieVast")).booleanValue();
		rotatieVastBox.setSelected(rotatieVast);

		boolean nietBouwenSlopen = false;
		if (b.containsKey("nietBouwenSlopen"))
			nietBouwenSlopen = ((Boolean) b.get("nietBouwenSlopen")).booleanValue();
		nietBouwenSlopenBox.setSelected(nietBouwenSlopen);
		
		boolean keuzeBouwenSlopen = false;
		if (b.containsKey("keuzeBouwenSlopen"))
			keuzeBouwenSlopen = ((Boolean) b.get("keuzeBouwenSlopen")).booleanValue();
		keuzeBouwenSlopenBox.setSelected(keuzeBouwenSlopen);

		boolean perspectief = true;
		if (b.containsKey("perspectief"))
			perspectief = ((Boolean) b.get("perspectief")).booleanValue();
		perspectiefBox.setSelected(perspectief);
		
		boolean pijlAan = true;
		if (b.containsKey("pijlAan"))
			pijlAan = ((Boolean) b.get("pijlAan")).booleanValue();
		pijlButton.setSelected(pijlAan);

		boolean balkAan = false;
		if (b.containsKey("balkAan"))
			balkAan = ((Boolean) b.get("balkAan")).booleanValue();
		balkButton.setSelected(balkAan);
		
		if (!pijlAan && !balkAan)
			geenButton.setSelected(true);
		
		boolean bovenAanzichtMetHoogtes = false;
		if (b.containsKey("bovenAanzichtMetHoogtes"))
			bovenAanzichtMetHoogtes = ((Boolean) b.get("bovenAanzichtMetHoogtes")).booleanValue();
		plattegrondBox.setSelected(bovenAanzichtMetHoogtes);
		
		if (plattegrondBox.isSelected())
		{
			rotatieVastBox.setEnabled(false);
			perspectiefBox.setEnabled(false);
			
			bouwselButton.setEnabled(false);
			drieButton.setEnabled(false);
			bovenButton.setEnabled(false);
			voorButton.setEnabled(false);
			rechtsButton.setEnabled(false);
			
			roosterTextField.setEnabled(false);
		}
			
		boolean blokkenBouwsel = true;
		if (b.containsKey("blokkenBouwsel"))
			blokkenBouwsel = ((Boolean) b.get("blokkenBouwsel")).booleanValue();
		bouwselButton.setSelected(blokkenBouwsel);

		boolean drieAanzichten = false;
		if (b.containsKey("drieAanzichten"))
			drieAanzichten = ((Boolean) b.get("drieAanzichten")).booleanValue();
		drieButton.setSelected(drieAanzichten);
		
		boolean bovenAanzicht = false;
		if (b.containsKey("bovenAanzicht"))
			bovenAanzicht = ((Boolean) b.get("bovenAanzicht")).booleanValue();
		bovenButton.setSelected(bovenAanzicht);
		
		boolean voorAanzicht = false;
		if (b.containsKey("voorAanzicht"))
			voorAanzicht = ((Boolean) b.get("voorAanzicht")).booleanValue();
		voorButton.setSelected(voorAanzicht);
		
		boolean rechtsAanzicht = false;
		if (b.containsKey("rechtsAanzicht"))
			rechtsAanzicht = ((Boolean) b.get("rechtsAanzicht")).booleanValue();
		rechtsButton.setSelected(rechtsAanzicht);
		
		int roosterGrootte = 4;
		if (b.containsKey("roosterGrootte"))
			roosterGrootte = ((Integer) b.get("roosterGrootte")).intValue();
		
		roosterTextField.setText("" + roosterGrootte);
		
		naip.setEditState(b);
	}
	
	public Hashtable getEditState()
	{		
		Hashtable h = naip.getEditState(); 
	
	
	
		return h;
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		super.setBounds(x, y, b, h);
		
		if (naip != null)
			naip.setBounds(0, 0, Math.max(0, b - editWidth), h);
		
		plaatsComponenten();
		
	}
	
	public void zetBreedte(int b)
	{	naip.setBounds(naip.getLocation().x, naip.getLocation().y, Math.max(0, b), naip.getSize().height);
		plaatsComponenten();
	}
	
	public void zetHoogte(int h)
	{	naip.setBounds(naip.getLocation().x, naip.getLocation().y, naip.getSize().width, h);
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
	{	if (e.getSource() == rotatieVastBox)
		{	naip.zetRotatieVast(rotatieVastBox.isSelected());
		}
		else if (e.getSource() == nietBouwenSlopenBox)
		{	naip.zetNietBouwenSlopen(nietBouwenSlopenBox.isSelected());
			if (nietBouwenSlopenBox.isSelected())
			{	keuzeBouwenSlopenBox.setSelected(false);
				naip.zetKeuzeBouwenSlopen(false);
			}
		}
		else if (e.getSource() == keuzeBouwenSlopenBox)
		{	naip.zetKeuzeBouwenSlopen(keuzeBouwenSlopenBox.isSelected());
		}
		else if (e.getSource() == perspectiefBox)
		{	naip.zetPerspectief(perspectiefBox.isSelected());
		}
		else if (e.getSource() == pijlButton)
		{	naip.zetPijlAan(pijlButton.isSelected());
		}
		else if (e.getSource() == balkButton)
		{	naip.zetBalkAan(balkButton.isSelected());
		}
		else if (e.getSource() == geenButton)
		{	if (geenButton.isSelected())
			{	naip.zetPijlAan(false);
				naip.zetBalkAan(false);
			}
		}
		else if (e.getSource() == plattegrondBox)
		{	boolean selected = plattegrondBox.isSelected();
			naip.zetBovenAanzichtMetHoogtes(selected);
			if (selected)
			{	rotatieVastBox.setEnabled(false);
				perspectiefBox.setEnabled(false);
				
				bouwselButton.setEnabled(false);
				drieButton.setEnabled(false);
				bovenButton.setEnabled(false);
				voorButton.setEnabled(false);
				rechtsButton.setEnabled(false);
				
				roosterTextField.setEnabled(false);
			}
			else
			{	rotatieVastBox.setEnabled(true);
				perspectiefBox.setEnabled(true);
				
				bouwselButton.setEnabled(true);
				drieButton.setEnabled(true);
				bovenButton.setEnabled(true);
				voorButton.setEnabled(true);
				rechtsButton.setEnabled(true);
				
				roosterTextField.setEnabled(true);
				
			}
			
		}
		else if (e.getSource() == bouwselButton)
		{	if (bouwselButton.isSelected())
			{
				naip.zetBlokkenBouwsel(true);
			}
			
		}
		else if (e.getSource() == drieButton)
		{	if (drieButton.isSelected())
			{
				naip.zetDrieAanzichten(true);
			}	
			
		}
		else if (e.getSource() == bovenButton)
		{	if (bovenButton.isSelected())
			{
				naip.zetBovenAanzicht(true);
			}	
			
		}
	
		else if (e.getSource() == voorButton)
		{	if (voorButton.isSelected())
			{
				naip.zetVoorAanzicht(true);
			}	
			
		}
	
		else if (e.getSource() == rechtsButton)
		{	if (rechtsButton.isSelected())
			{
				naip.zetRechtsAanzicht(true);
			}	
			
		}
	
	
	}

	class TextFL implements FocusListener
	{		
		JTextField inputTextField;
		
		public TextFL(JTextField input)
		{	inputTextField = input;
		}

	
		public void focusGained(FocusEvent e)
		{
		}
		public void focusLost(FocusEvent e)
		{	// invoer user
			String text = inputTextField.getText();
			String oldText = "" + naip.v.kr.maxAantal;
			
			// komma gebruikt
			if (text.indexOf(',') >= 0)
			{	String text1 = trimTrailingZeros(text, ',');
				boolean changed1 = (text.length() != text1.length());
				String text2 = addLeadingZero(text1, ',');
				boolean changed2 = (text1.length() != text2.length());
				if (changed1 || changed2)
				{	text = text2;
				}
			}
			// punt gebruikt
			if (text.indexOf('.') >= 0)
			{	String text1 = trimTrailingZeros(text, '.');
				boolean changed1 = (text.length() != text1.length());
				String text2 = addLeadingZero(text1, '.');
				boolean changed2 = (text1.length() != text2.length());
				if (changed1 || changed2)
				{	text = text2;
				}
			}	
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
			
			int rGrootte = (int) userInput;

			if ((rGrootte >= 2) && (rGrootte <= 15))
			{
				naip.zetRoosterGrootte(rGrootte);
			}
			else
			{
				inputTextField.setText(oldText);
			}
			
/*
			if (inputTextField == checkMuWaardeVeld)
			{	if (userInput < normaalPanel.muMin + normaalPanel.NZERO)
				{	userInput = normaalPanel.muMin;
					inputTextField.setText(UF.format(userInput, 0));
				}
				if (userInput > normaalPanel.muMax - normaalPanel.NZERO)
				{	userInput = normaalPanel.muMax;
					inputTextField.setText(UF.format(userInput, 0));
				}
			}
			else if (inputTextField == checkSigmaWaardeVeld)
			{	if (userInput < normaalPanel.sigmaMin + normaalPanel.NZERO)
				{	userInput = normaalPanel.sigmaMin;
					inputTextField.setText(UF.format(userInput, 2));
				}
				if (userInput > normaalPanel.sigmaMax - normaalPanel.NZERO)
				{	userInput = normaalPanel.sigmaMax;
					inputTextField.setText(UF.format(userInput, 0));
				}
			}
			else if (inputTextField == checkGrensWaardeVeld)
			{	if (userInput < normaalPanel.muMin + normaalPanel.NZERO)
				{	userInput = normaalPanel.muMin;
					inputTextField.setText(UF.format(userInput, 2));
				}
				if (userInput > normaalPanel.muMax - normaalPanel.NZERO)
				{	userInput = normaalPanel.muMax;
					inputTextField.setText(UF.format(userInput, 2));
				}
			}
			else if (inputTextField == checkGrensLinksWaardeVeld)
			{	if (userInput < normaalPanel.muMin + normaalPanel.NZERO)
				{	userInput = normaalPanel.muMin;
					inputTextField.setText(UF.format(userInput, 2));
				}
				if (userInput > normaalPanel.muMax - normaalPanel.NZERO)
				{	userInput = normaalPanel.muMax;
					inputTextField.setText(UF.format(userInput, 2));
				}
			}
			else if (inputTextField == checkGrensRechtsWaardeVeld)
			{	if (userInput < normaalPanel.muMin + normaalPanel.NZERO)
				{	userInput = normaalPanel.muMin;
					inputTextField.setText(UF.format(userInput, 2));
				}
				if (userInput > normaalPanel.muMax - normaalPanel.NZERO)
				{	userInput = normaalPanel.muMax;
					inputTextField.setText(UF.format(userInput, 2));
				}
			}
			else if (inputTextField == checkKansWaardeVeld)
			{	if (userInput < normaalPanel.NZERO)
				{	userInput = normaalPanel.NZERO;
					inputTextField.setText(UF.format(userInput, 3));
				}
				if (userInput > 1 - normaalPanel.NZERO)
				{	userInput = 1 - normaalPanel.NZERO;
					inputTextField.setText(UF.format(userInput, 3));
				}
			}
			else if (inputTextField == maxScoreVeld)
			{	// nothing to do
			}			
*/			
		} // focusLost
	}


	class TextAL implements ActionListener
	{	
		JTextField inputTextField;
		
		public TextAL(JTextField input)
		{	inputTextField = input;
		}
		
		public void actionPerformed(ActionEvent e)
		{	
			String text = inputTextField.getText();
			String oldText = "" + naip.v.kr.maxAantal;
			
			// komma gebruikt
			if (text.indexOf(',') >= 0)
			{	String text1 = trimTrailingZeros(text, ',');
				boolean changed1 = (text.length() != text1.length());
				String text2 = addLeadingZero(text1, ',');
				boolean changed2 = (text1.length() != text2.length());
				if (changed1 || changed2)
				{	text = text2;
				}
			}
			// punt gebruikt
			if (text.indexOf('.') >= 0)
			{	String text1 = trimTrailingZeros(text, '.');
				boolean changed1 = (text.length() != text1.length());
				String text2 = addLeadingZero(text1, '.');
				boolean changed2 = (text1.length() != text2.length());
				if (changed1 || changed2)
				{	text = text2;
				}
			}	
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
			
			int rGrootte = (int) userInput;

			if ((rGrootte >= 2) && (rGrootte <= 15))
			{
				naip.zetRoosterGrootte(rGrootte);
			}
			else
			{
				inputTextField.setText(oldText);
			}
			
/*			
			if (inputTextField == checkMuWaardeVeld)
			{	if (userInput < normaalPanel.muMin + normaalPanel.NZERO)
				{	userInput = normaalPanel.muMin;
					inputTextField.setText(UF.format(userInput, 0));
				}
				if (userInput > normaalPanel.muMax - normaalPanel.NZERO)
				{	userInput = normaalPanel.muMax;
					inputTextField.setText(UF.format(userInput, 0));
				}
			}
			else if (inputTextField == checkSigmaWaardeVeld)
			{	if (userInput < normaalPanel.sigmaMin + normaalPanel.NZERO)
				{	userInput = normaalPanel.sigmaMin;
					inputTextField.setText(UF.format(userInput, 2));
				}
				if (userInput > normaalPanel.sigmaMax - normaalPanel.NZERO)
				{	userInput = normaalPanel.sigmaMax;
					inputTextField.setText(UF.format(userInput, 0));
				}
			}
			else if (inputTextField == checkGrensWaardeVeld)
			{	if (userInput < normaalPanel.muMin + normaalPanel.NZERO)
				{	userInput = normaalPanel.muMin;
					inputTextField.setText(UF.format(userInput, 2));
				}
				if (userInput > normaalPanel.muMax - normaalPanel.NZERO)
				{	userInput = normaalPanel.muMax;
					inputTextField.setText(UF.format(userInput, 2));
				}
			}
			else if (inputTextField == checkGrensLinksWaardeVeld)
			{	if (userInput < normaalPanel.muMin + normaalPanel.NZERO)
				{	userInput = normaalPanel.muMin;
					inputTextField.setText(UF.format(userInput, 2));
				}
				if (userInput > normaalPanel.muMax - normaalPanel.NZERO)
				{	userInput = normaalPanel.muMax;
					inputTextField.setText(UF.format(userInput, 2));
				}
			}
			else if (inputTextField == checkGrensRechtsWaardeVeld)
			{	if (userInput < normaalPanel.muMin + normaalPanel.NZERO)
				{	userInput = normaalPanel.muMin;
					inputTextField.setText(UF.format(userInput, 2));
				}
				if (userInput > normaalPanel.muMax - normaalPanel.NZERO)
				{	userInput = normaalPanel.muMax;
					inputTextField.setText(UF.format(userInput, 2));
				}
			}
			else if (inputTextField == checkKansWaardeVeld)
			{	if (userInput < normaalPanel.NZERO)
				{	userInput = normaalPanel.NZERO;
					inputTextField.setText(UF.format(userInput, 3));
				}
				if (userInput > 1 - normaalPanel.NZERO)
				{	userInput = 1 - normaalPanel.NZERO;
					inputTextField.setText(UF.format(userInput, 3));
				}
			}
			else if (inputTextField == maxScoreVeld)
			{	// nothing to do
			}			
*/			
			
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
		
	public String addLeadingZero(String s, char decSep)
	{	String txt = new String(s);
		// met minteken
		if ((txt.length() >= 2) && (txt.charAt(0) == '-') &&
			(txt.charAt(1) == decSep))
		{	txt = "-0" + txt.substring(1);
		}	
		// zonder minteken
		if ((txt.length() >= 1) && (txt.charAt(0) == decSep))
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
	{	
		JTextField inputTextField;
		boolean minusAllowed;
		boolean posIntegerInput;
		
		public InputKL(JTextField input, boolean minAllowed, boolean posIntInput)
		{	inputTextField = input;
			minusAllowed = minAllowed;
			posIntegerInput = posIntInput;
		}
		public void keyReleased(KeyEvent e)
		{	
			inputTextField.setForeground(Color.black);
		
			String txt = inputTextField.getText();
			
			//om randomvariabele in te kunnen vullen
			if (isLegal(txt))
				return;

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
			
			// dubbele decimale komma
			// voldoende er twee te zoeken
			int pIndex1 = txt.indexOf(',');
			int pIndex2 = txt.lastIndexOf(',');
			if ((pIndex1 >= 0) && (pIndex2 >= 0) && (pIndex1 != pIndex2))
			{	// verwijderen
				txt = removeCharAt(txt, pIndex2);
				corrected = true;
			}

			// dubbele decimale punt
			// voldoende er twee te zoeken
			pIndex1 = txt.indexOf('.');
			pIndex2 = txt.lastIndexOf('.');
			if ((pIndex1 >= 0) && (pIndex2 >= 0) && (pIndex1 != pIndex2))
			{	// verwijderen
				txt = removeCharAt(txt, pIndex2);
				corrected = true;
			}
			
			// komma na decimale punt
			pIndex1 = txt.indexOf('.');
			pIndex2 = txt.lastIndexOf(',');
			if ((pIndex1 >= 0) && (pIndex2 >= 0) && (pIndex1 < pIndex2))
			{	// verwijderen
				txt = removeCharAt(txt, pIndex2);
				corrected = true;
			}
			
			// punt na decimale komma
			pIndex1 = txt.indexOf(',');
			pIndex2 = txt.lastIndexOf('.');
			if ((pIndex1 >= 0) && (pIndex2 >= 0) && (pIndex1 < pIndex2))
			{	// verwijderen
				txt = removeCharAt(txt, pIndex2);
				corrected = true;
			}
			
			// proberen een legaal karakter voor het
			// minteken (dit staat dan op plek 1) in te vullen
			if (txt.indexOf('-') == 1)
			{	txt = removeCharAt(txt, 0);
				corrected = true;
			}
			
			// minteken
			// alleen vooraan if any
			int minIndex = txt.lastIndexOf('-');
			if (minIndex > 0)
			{	txt = removeCharAt(txt, minIndex);
				corrected = true;
			}
			
			
			// leading zeros, leiden niet tot een NumberFormatException
			// geval met minteken
			if ((txt.indexOf('-') == 0) && (txt.length() >= 3) &&
				(txt.charAt(1) == '0') && Character.isDigit(txt.charAt(2)))
			{	txt = removeCharAt(txt, 1);
				corrected = true;
			}
			
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
		
		public boolean isLegal(String s)
		{	
			if (s != null && s.length() > 0)
				return s.charAt(0) == '#';
			else 
				return false;
		}
		
		public boolean isLegal(char c)
		{	if (posIntegerInput)
				return Character.isDigit(c);
			
			if (minusAllowed)
				return Character.isDigit(c) || (c == ',') || (c == '.') || (c == '-');
			else	
				return Character.isDigit(c) || (c == ',') || (c == '.');
		}
	}	
	
}
