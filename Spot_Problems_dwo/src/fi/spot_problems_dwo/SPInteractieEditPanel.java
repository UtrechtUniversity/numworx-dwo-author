package fi.spot_problems_dwo;

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

public class SPInteractieEditPanel extends JPanel implements InteractieEditPanel,
																	ActionListener	
{	
	int editWidth = 190;
	int editHeight = 500; 
	int spipBreedte = 500; // startbreedte spip
	int spipHoogte = 450; // starthoogte spip
	
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	int offset = 10;
	boolean componentsCreated = false;

	JComboBox levelKeuzeCombo, level1Combo, level2Combo, level3Combo;
	
	boolean levelKeuzeComboEnabled = true;
	boolean level1ComboEnabled = true;
	boolean level2ComboEnabled = true;
	boolean level3ComboEnabled = true;
	
	JCheckBox kijkNaBox;	
	JLabel scoreMaxLabel;
	JTextField scoreMaxVeld;
	
	JCheckBox antwoordVakBox;
	
	boolean noSetBounds = false;	

	protected SPInteractiePanel spip;

	int scoreMax = 10;
	
	
	public SPInteractieEditPanel()
	{
		setLayout(null);
		spip = new SPInteractiePanel();
		add(spip);
		spip.setBackground(getBackground());
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		
		int width = editWidth - 3 * offset;
		int height = 3 * theFM.getHeight() / 2;
		int currentX = spip.getSize().width + offset;
		int currentX2 = offset;
		int currentY = offset;

		levelKeuzeCombo = new JComboBox();
		levelKeuzeCombo.addItem(Spot_Problems_dwo.rb.getString("niveauTekst") + " 1");
		levelKeuzeCombo.addItem(Spot_Problems_dwo.rb.getString("niveauTekst") + " 2");
		levelKeuzeCombo.addItem(Spot_Problems_dwo.rb.getString("niveauTekst") + " 3");
		levelKeuzeCombo.setFont(theFont);
		levelKeuzeCombo.setBackground(Color.white);
		width = theFM.stringWidth(Spot_Problems_dwo.rb.getString("niveauTekst") + " X") + 50;
		levelKeuzeCombo.setBounds(currentX + offset, currentY + 3, width, height);
		add(levelKeuzeCombo);
		levelKeuzeCombo.addActionListener(this);
		
		currentY += height + offset;
		
		level1Combo = new JComboBox();
		level1Combo.addItem(Spot_Problems_dwo.rb.getString("vNumbersText"));
		level1Combo.addItem(Spot_Problems_dwo.rb.getString("wNumbersText"));
		level1Combo.addItem(Spot_Problems_dwo.rb.getString("sqrNumbersText"));
		level1Combo.addItem(Spot_Problems_dwo.rb.getString("boxNumbersText"));
		level1Combo.addItem(Spot_Problems_dwo.rb.getString("plusNumbersText"));
		level1Combo.addItem(Spot_Problems_dwo.rb.getString("towerNumbersText"));
		level1Combo.addItem(Spot_Problems_dwo.rb.getString("fNumbers1Text"));
		level1Combo.addItem(Spot_Problems_dwo.rb.getString("flapNumbersText"));
		level1Combo.addItem(Spot_Problems_dwo.rb.getString("hNumbers1Text"));
		level1Combo.addItem(Spot_Problems_dwo.rb.getString("hNumbers2Text"));
		level1Combo.addItem(Spot_Problems_dwo.rb.getString("xNumbers1Text"));
		level1Combo.addItem(Spot_Problems_dwo.rb.getString("lNumbers1Text"));
		level1Combo.setFont(theFont);
		level1Combo.setBackground(Color.white);
		width = editWidth - 3 * offset;
		level1Combo.setBounds(currentX + offset, currentY, width, height);
		add(level1Combo);
		level1Combo.addActionListener(this);

		level2Combo = new JComboBox();
		level2Combo.addItem(Spot_Problems_dwo.rb.getString("xNumbers2Text"));
		level2Combo.addItem(Spot_Problems_dwo.rb.getString("tableNumbersText"));
		level2Combo.addItem(Spot_Problems_dwo.rb.getString("oblNumbersText"));
		level2Combo.addItem(Spot_Problems_dwo.rb.getString("triaNumbers1Text"));
		level2Combo.addItem(Spot_Problems_dwo.rb.getString("triaNumbers2Text"));
		level2Combo.addItem(Spot_Problems_dwo.rb.getString("triaNumbers3Text"));
		level2Combo.addItem(Spot_Problems_dwo.rb.getString("fNumbers2Text"));
		level2Combo.addItem(Spot_Problems_dwo.rb.getString("flipNumbersText"));
		level2Combo.addItem(Spot_Problems_dwo.rb.getString("tileNumbers1Text"));
		level2Combo.addItem(Spot_Problems_dwo.rb.getString("tileNumbers2Text"));
		level2Combo.addItem(Spot_Problems_dwo.rb.getString("stairNumbersText"));
		level2Combo.addItem(Spot_Problems_dwo.rb.getString("zNumbersText"));
		level2Combo.setFont(theFont);
		level2Combo.setBackground(Color.white);
		width = editWidth - 3 * offset;
		level2Combo.setBounds(currentX + offset, currentY, width, height);
		level2Combo.setVisible(false);
		add(level2Combo);
		level2Combo.addActionListener(this);

		level3Combo = new JComboBox();
		level3Combo.addItem(Spot_Problems_dwo.rb.getString("lNumbers2Text"));
		level3Combo.addItem(Spot_Problems_dwo.rb.getString("insectNumbersText"));
		level3Combo.addItem(Spot_Problems_dwo.rb.getString("spiderNumbersText"));
		level3Combo.addItem(Spot_Problems_dwo.rb.getString("pentaNumbersText"));
		level3Combo.addItem(Spot_Problems_dwo.rb.getString("spiralNumbersText"));
		level3Combo.addItem(Spot_Problems_dwo.rb.getString("blockNumbers1Text"));
		level3Combo.addItem(Spot_Problems_dwo.rb.getString("tileNumbers3Text"));
		level3Combo.addItem(Spot_Problems_dwo.rb.getString("tileNumbers4Text"));
		level3Combo.addItem(Spot_Problems_dwo.rb.getString("blockNumbers2Text"));
		level3Combo.addItem(Spot_Problems_dwo.rb.getString("blockNumbers3Text"));
		level3Combo.addItem(Spot_Problems_dwo.rb.getString("triaNumbers4Text"));
		level3Combo.addItem(Spot_Problems_dwo.rb.getString("piramidNumbersText"));
		level3Combo.setFont(theFont);
		level3Combo.setBackground(Color.white);
		width = editWidth - 3 * offset;
		level3Combo.setBounds(currentX + offset, currentY, width, height);
		level3Combo.setVisible(false);
		add(level3Combo);
		level3Combo.addActionListener(this);
		
		currentY += height + 3 * offset;
		
		
		kijkNaBox = new JCheckBox(Spot_Problems_dwo.rb.getString("kijkNaActiefTekst"), true);
		kijkNaBox.setFont(theFont);
		kijkNaBox.setBackground(Color.white);
		kijkNaBox.setBounds(currentX, currentY, editWidth - 3 * offset, 3 * theFM.getHeight() / 2);
		add(kijkNaBox);
		kijkNaBox.addActionListener(this);
		
		currentY += height + offset;
		
		
		scoreMaxLabel = new JLabel(Spot_Problems_dwo.rb.getString("scoreMaxTekst"));
		scoreMaxLabel.setFont(theFont);
		scoreMaxLabel.setBackground(Color.white);
		width = theFM.stringWidth(scoreMaxLabel.getText());
		scoreMaxLabel.setBounds(currentX + offset, currentY + 3, width, theFM.getHeight());
		add(scoreMaxLabel);
		
		currentY += height; // + offset;
		
		scoreMaxVeld = new JTextField("" + scoreMax);
		scoreMaxVeld.setFont(theFont);
		scoreMaxVeld.setBackground(Color.white);
		width = theFM.stringWidth("XXXXXX");
		scoreMaxVeld.setBounds(currentX + 2 * offset, currentY, width, height);
				//maxScoreLabel.getLocation().x + maxScoreLabel.getSize().width + offset,
				//currentY, width, height);
		add(scoreMaxVeld);
		//maxScoreVeld.setVisible(false);

		scoreMaxVeld.addKeyListener(new InputKL2(scoreMaxVeld));
		scoreMaxVeld.addActionListener(new TextAL2(scoreMaxVeld));
		scoreMaxVeld.addFocusListener(new TextFL2(scoreMaxVeld));

		currentY += height + offset;
		
		antwoordVakBox = new JCheckBox(Spot_Problems_dwo.rb.getString("antwoordVakTekst"), true);
		antwoordVakBox.setFont(theFont);
		antwoordVakBox.setBackground(Color.white);
		antwoordVakBox.setBounds(currentX, currentY, editWidth - 3 * offset, 3 * theFM.getHeight() / 2);
		add(antwoordVakBox);
		antwoordVakBox.addActionListener(this);
		
		currentY += height + offset;

		componentsCreated = true;

		
	}	
	
	public void plaatsComponenten()
	{	levelKeuzeCombo.setLocation(spip.getSize().width + 2 * offset, levelKeuzeCombo.getLocation().y);
		level1Combo.setLocation(spip.getSize().width + 2 * offset, level1Combo.getLocation().y);
		level2Combo.setLocation(spip.getSize().width + 2 * offset, level2Combo.getLocation().y);
		level3Combo.setLocation(spip.getSize().width + 2 * offset, level3Combo.getLocation().y);
		
		kijkNaBox.setLocation(spip.getSize().width + 2 * offset, kijkNaBox.getLocation().y);
		scoreMaxLabel.setLocation(spip.getSize().width + 2 * offset, scoreMaxLabel.getLocation().y);
		scoreMaxVeld.setLocation(spip.getSize().width + 3 * offset, scoreMaxVeld.getLocation().y);
		
		antwoordVakBox.setLocation(spip.getSize().width + 2 * offset, antwoordVakBox.getLocation().y);
	}
	
	public void zetLevel(int level)
	{	if (level == 1)
		{	level1Combo.setVisible(true);
			level2Combo.setVisible(false);
			level3Combo.setVisible(false);
		}
		else if (level == 2)
		{	level1Combo.setVisible(false);
			level2Combo.setVisible(true);
			level3Combo.setVisible(false);
		}
		else if (level == 3)
		{	level1Combo.setVisible(false);
			level2Combo.setVisible(false);
			level3Combo.setVisible(true);
		}
	
	}
	
	public void setEditState(Hashtable b)
	{
		
System.out.println("spiep setEditState");

		int level = 1;
		int level1Keuze = 1;
		int level2Keuze = 1;
		int level3Keuze = 1;

		if (b.containsKey("level"))
			level = ((Integer) b.get("level")).intValue();
		if (b.containsKey("level1Keuze"))
			level1Keuze = ((Integer) b.get("level1Keuze")).intValue();
		if (b.containsKey("level2Keuze"))
			level2Keuze = ((Integer) b.get("level2Keuze")).intValue();
		if (b.containsKey("level3Keuze"))
			level3Keuze = ((Integer) b.get("level3Keuze")).intValue();
		
		levelKeuzeComboEnabled = false;
		levelKeuzeCombo.setSelectedIndex(level - 1);
		levelKeuzeComboEnabled = true;
		level1ComboEnabled = false;
		level1Combo.setSelectedIndex(level1Keuze - 1);
		level1ComboEnabled = true;
		level2ComboEnabled = false;
		level2Combo.setSelectedIndex(level2Keuze - 1);
		level2ComboEnabled = true;
		level3ComboEnabled = false;
		level3Combo.setSelectedIndex(level3Keuze - 1);
		level3ComboEnabled = true;

		zetLevel(level);
		
		spip.zetLevel1Keuze(level1Keuze);
		spip.zetLevel2Keuze(level2Keuze);
		spip.zetLevel3Keuze(level3Keuze);
		// HIER
		spip.zetLevel(level);
		
		boolean antwoordVakZichtbaar = true;
		if (b.containsKey("antwoordVakZichtbaar"))
			antwoordVakZichtbaar = ((Boolean) b.get("antwoordVakZichtbaar")).booleanValue();
		antwoordVakBox.setSelected(antwoordVakZichtbaar);

		boolean kijkNaActief = true;
		if (b.containsKey("kijkNaActief"))
			kijkNaActief = ((Boolean) b.get("kijkNaActief")).booleanValue();
		kijkNaBox.setSelected(kijkNaActief);
		if (!kijkNaActief)
		{
			scoreMaxLabel.setEnabled(false);
			scoreMaxVeld.setEnabled(false);
		}
			
		
		
		int scoreMax = 10;
		if (b.containsKey("scoreMax"))
			scoreMax = ((Integer) b.get("scoreMax")).intValue();
		scoreMaxVeld.setText("" + scoreMax);
		this.scoreMax = scoreMax;
		
		
		if (b.containsKey("spipBreedte"))
			spipBreedte = ((Integer) b.get("spipBreedte")).intValue();
		if (b.containsKey("spipHoogte"))
			spipHoogte = ((Integer) b.get("spipHoogte")).intValue();
		
		setBounds(getLocation().x, getLocation().y, spipBreedte + editWidth, Math.max(spipHoogte, editHeight));
		
		// HIER !!
		spip.setEditState(b);		
		
	}
	
	public Hashtable getEditState()
	{
System.out.println("spiep getEditState");

		Hashtable h = spip.getEditState(); 
		
		h.put("scoreMax", new Integer(scoreMax));
		
		h.put("spipBreedte", new Integer(spipBreedte));
		h.put("spipHoogte", new Integer(spipHoogte));
		
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
		
		super.setBounds(x, y, spipBreedte + editWidth, Math.max(spipHoogte, editHeight));
		
//System.out.println("spiep setBounds " + x + " " + y + " " + (spipBreedte + editWidth) + " " + 
//					Math.max(spipHoogte, editHeight));
	
		if (spip != null)
			spip.setBounds(0, 0, spipBreedte, spipHoogte);
		
		plaatsComponenten();
		
//System.out.println("setBounds " + x + " " + y + " " + b + " " + h);		
		
	}
	
	public void zetBreedte(int b)
	{	
		spipBreedte = b;
		
		setBounds(getLocation().x, getLocation().y, spipBreedte + editWidth, Math.max(spipHoogte, editHeight));		
		plaatsComponenten();
	}
	
	public void zetHoogte(int h)
	{	
		spipHoogte = h;
		
		setBounds(getLocation().x, getLocation().y, spipBreedte + editWidth, Math.max(spipHoogte, editHeight));		
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
	{	if (e.getSource() == levelKeuzeCombo)
		{	zetLevel(levelKeuzeCombo.getSelectedIndex() + 1);
			spip.zetLevel(levelKeuzeCombo.getSelectedIndex() + 1);
		
		}
		else if (e.getSource() == level1Combo)
		{	spip.zetLevel1Keuze(level1Combo.getSelectedIndex() + 1);
			
		}
		else if (e.getSource() == level2Combo)
		{	spip.zetLevel2Keuze(level2Combo.getSelectedIndex() + 1);
			
		}
		else if (e.getSource() == level3Combo)
		{	spip.zetLevel3Keuze(level3Combo.getSelectedIndex() + 1);
			
		}
		else if (e.getSource() == kijkNaBox)
		{	spip.zetKijkNaActief(kijkNaBox.isSelected());
			scoreMaxLabel.setEnabled(kijkNaBox.isSelected());
			scoreMaxVeld.setEnabled(kijkNaBox.isSelected());
			
		}
		else if (e.getSource() == antwoordVakBox)
		{	spip.zetAntwoordVak(antwoordVakBox.isSelected());
			//scoreMaxLabel.setEnabled(kijkNaBox.isSelected());
			//scoreMaxVeld.setEnabled(kijkNaBox.isSelected());
			
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
			String oldText = "";
			if (inputTextField == scoreMaxVeld)
				oldText = "" + scoreMax;
			
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

			if (inputTextField == scoreMaxVeld)
			{	int mScore = (int) userInput;
				if ((mScore >= 1) && (mScore <= 1500))
				{	scoreMax = mScore;
				}
				else
				{	inputTextField.setText(oldText);
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
			String oldText = "";
			if (inputTextField == scoreMaxVeld)
				oldText = "" + scoreMax;
			
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

			if (inputTextField == scoreMaxVeld)
			{	int mScore = (int) userInput;
				if ((mScore >= 1) && (mScore <= 1500))
				{	scoreMax = mScore;
				}
				else
				{	inputTextField.setText(oldText);
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
