package fi.normaleverdeling;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;
import fi.normaleverdeling.NormaalPanel.TextAL;

public class NormaalEditPanel extends JPanel implements InteractieEditPanel
{	
	protected static int editBreedte = 290;	
	protected static Color bgColor = new Color(Integer.parseInt("DDEEFF", 16));

	protected NormaalPanel normaalPanel;


	// fonts
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;

	int offset = 15;
	int editX, editX2;

	JLabel kansOptiesLabel;
	JCheckBox kansLinksOptieBox,
			  kansRechtsOptieBox,
			  tweeGrenzenOptieBox;
			  
	JLabel berekenbaarLabel;
	JCheckBox berekenbaarZichtbaarBox,
			  muBerekenbaarBox,
			  sigmaBerekenbaarBox;
			  
	JLabel vasteWaardeLabel;
	JCheckBox muVastBox,
			  sigmaVastBox;
	
	JLabel sliderLabel;
	JCheckBox muSliderBox,
				sigmaSliderBox,
				grensSliderBox,
				kansSliderBox;
	
	JLabel waardenLabel;
	JCheckBox muZichtbaarBox,
				sigmaZichtbaarBox,
				grensZichtbaarBox,
				kansZichtbaarBox;
	
	JLabel waardenFigLabel;
	JCheckBox muZichtbaarFigBox,
				sigmaZichtbaarFigBox,
				grensZichtbaarFigBox,
				kansZichtbaarFigBox;
	
	//JLabel checkOptiesLabel;
	JCheckBox checkOptiesBox;
	JCheckBox checkMuBox,
			checkSigmaBox,
			checkGrensBox,
			checkGrensLinksBox,
			checkGrensRechtsBox,
			checkKansBox;
	
	JLabel maxScoreLabel;
	
	JTextField checkMuWaardeVeld,
			checkSigmaWaardeVeld,
			checkGrensWaardeVeld,
			checkGrensLinksWaardeVeld,
			checkGrensRechtsWaardeVeld,
			checkKansWaardeVeld;

	JTextField maxScoreVeld;

	public NormaalEditPanel(int w, int h)
	{	//normaleVerdeling = o;
	
		setBackground(bgColor);
		setLayout(null);
		setSize(w, h);
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		normaalPanel = new NormaalPanel(500, 450);
		normaalPanel.setLocation(0, 0);
		add(normaalPanel);
		
		editX = getSize().width - editBreedte;
		editX2 = getSize().width - editBreedte / 2;
		
		kansOptiesLabel = new JLabel(
			NormaleVerdeling.rb.getString("kansOptiesTekst"));
		kansOptiesLabel.setFont(theBoldFont);
		int width = theBoldFM.stringWidth(kansOptiesLabel.getText());
		int height = 3 * theBoldFM.getHeight() / 2;
		kansOptiesLabel.setSize(width, height);
		add(kansOptiesLabel);
		
		ActionListener listener = new KansOptiesAL();
		
		kansLinksOptieBox = 
			maakCheckBox(NormaleVerdeling.rb.getString("kansLinksTekst"), true, listener);
		kansRechtsOptieBox = 
			maakCheckBox(NormaleVerdeling.rb.getString("kansRechtsTekst"), true, listener);
		tweeGrenzenOptieBox = 
			maakCheckBox(NormaleVerdeling.rb.getString("tweeGrenzenTekst"), true, listener);
		
		berekenbaarLabel = maakLabel(NormaleVerdeling.rb.getString("berekenbaarTekst"));
		
		berekenbaarZichtbaarBox = 
			maakCheckBox(NormaleVerdeling.rb.getString("berekenbaarZichtbaarTekst"), true, new BerekenbaarZichtbaarAL());
		muBerekenbaarBox = 
			maakCheckBox(NormaleVerdeling.rb.getString("muTekst"), false, new MuBerekenbaarAL());
		sigmaBerekenbaarBox = 
			maakCheckBox(NormaleVerdeling.rb.getString("sigmaTekst"), false, new SigmaBerekenbaarAL());
		
		vasteWaardeLabel = maakLabel(NormaleVerdeling.rb.getString("vasteWaardeTekst"));
		
		muVastBox = 
			maakCheckBox(NormaleVerdeling.rb.getString("muTekst"), false, new MuVastAL());
		sigmaVastBox = 
			maakCheckBox(NormaleVerdeling.rb.getString("sigmaTekst"), false, new SigmaVastAL());
		
		sliderLabel = maakLabel(NormaleVerdeling.rb.getString("sliderLabelTekst"));
		
		muSliderBox = 
			maakCheckBox(NormaleVerdeling.rb.getString("muSliderTekst"), false, new MuSliderAL());
		sigmaSliderBox = 
			maakCheckBox(NormaleVerdeling.rb.getString("sigmaSliderTekst"), false, new SigmaSliderAL());
		grensSliderBox = 
			maakCheckBox(NormaleVerdeling.rb.getString("grensSliderTekst"), false, new GrensSliderAL());
		kansSliderBox = 
			maakCheckBox(NormaleVerdeling.rb.getString("kansSliderTekst"), false, new KansSliderAL());
		
		waardenLabel = maakLabel(NormaleVerdeling.rb.getString("waardenLabelTekst"));
		
		listener = new WaardenAL();
		
		muZichtbaarBox = 
			maakCheckBox(NormaleVerdeling.rb.getString("muZichtbaarTekst"), true, listener);
		sigmaZichtbaarBox = 
			maakCheckBox(NormaleVerdeling.rb.getString("sigmaZichtbaarTekst"), true, listener);
		grensZichtbaarBox = 
			maakCheckBox(NormaleVerdeling.rb.getString("grensZichtbaarTekst"), true, listener);
		kansZichtbaarBox = 
			maakCheckBox(NormaleVerdeling.rb.getString("kansZichtbaarTekst"), true, listener);
		
		waardenFigLabel = maakLabel(NormaleVerdeling.rb.getString("waardenFigLabelTekst"));
		
		muZichtbaarFigBox = maakCheckBox(NormaleVerdeling.rb.getString("muZichtbaarFigTekst"), true, listener);
		sigmaZichtbaarFigBox = maakCheckBox(NormaleVerdeling.rb.getString("sigmaZichtbaarFigTekst"), true, listener);
		grensZichtbaarFigBox = maakCheckBox(NormaleVerdeling.rb.getString("grensZichtbaarFigTekst"), true, listener);
		kansZichtbaarFigBox = maakCheckBox(NormaleVerdeling.rb.getString("kansZichtbaarFigTekst"), true, listener);
		
		listener = new CheckOptiesAL();		
		
		//checkOptiesLabel = maakLabel(NormaleVerdeling.rb.getString("checkOptiesLabelTekst"));
		checkOptiesBox = maakCheckBox(NormaleVerdeling.rb.getString("checkOptiesBoxTekst"), false, listener);
		checkOptiesBox.setFont(theBoldFont);
		
		checkMuBox = maakCheckBox(NormaleVerdeling.rb.getString("checkMuTekst"), false, listener);
		checkSigmaBox = maakCheckBox(NormaleVerdeling.rb.getString("checkSigmaTekst"), false, listener);
		checkGrensBox = maakCheckBox(NormaleVerdeling.rb.getString("checkGrensTekst"), false, listener);
		checkGrensLinksBox = maakCheckBox(NormaleVerdeling.rb.getString("checkGrensLinksTekst"), false, listener);
		checkGrensRechtsBox = maakCheckBox(NormaleVerdeling.rb.getString("checkGrensRechtsTekst"), false, listener);
		checkKansBox = maakCheckBox(NormaleVerdeling.rb.getString("checkKansTekst"), false, listener);

		checkMuBox.setVisible(false);
		checkSigmaBox.setVisible(false);
		checkGrensBox.setVisible(false);
		checkGrensLinksBox.setVisible(false);
		checkGrensRechtsBox.setVisible(false);
		checkKansBox.setVisible(false);
		
// maxScoreLabel		
		
//		listener = new CheckValuesAL();
		
		checkMuWaardeVeld = maakTextField(60,20, false);
		checkSigmaWaardeVeld = maakTextField(60,20, false);
		checkGrensWaardeVeld = maakTextField(60,20, false);
		checkGrensLinksWaardeVeld = maakTextField(60,20, false);
		checkGrensRechtsWaardeVeld = maakTextField(60,20, false);
		checkKansWaardeVeld = maakTextField(60,20, false);
		
		checkMuWaardeVeld.addKeyListener(new InputKL(checkMuWaardeVeld, true, false));
		checkMuWaardeVeld.addActionListener(new TextAL(checkMuWaardeVeld));
		checkMuWaardeVeld.addFocusListener(new TextFL(checkMuWaardeVeld));
		
		checkSigmaWaardeVeld.addKeyListener(new InputKL(checkSigmaWaardeVeld, false, false));
		checkSigmaWaardeVeld.addActionListener(new TextAL(checkSigmaWaardeVeld));
		checkSigmaWaardeVeld.addFocusListener(new TextFL(checkSigmaWaardeVeld));
		
		checkGrensWaardeVeld.addKeyListener(new InputKL(checkGrensWaardeVeld, true, false));
		checkGrensWaardeVeld.addActionListener(new TextAL(checkGrensWaardeVeld));
		checkGrensWaardeVeld.addFocusListener(new TextFL(checkGrensWaardeVeld));
		
		checkGrensLinksWaardeVeld.addKeyListener(new InputKL(checkGrensLinksWaardeVeld, true, false));
		checkGrensLinksWaardeVeld.addActionListener(new TextAL(checkGrensLinksWaardeVeld));
		checkGrensLinksWaardeVeld.addFocusListener(new TextFL(checkGrensLinksWaardeVeld));
				
		checkGrensRechtsWaardeVeld.addKeyListener(new InputKL(checkGrensRechtsWaardeVeld, true, false));
		checkGrensRechtsWaardeVeld.addActionListener(new TextAL(checkGrensRechtsWaardeVeld));
		checkGrensRechtsWaardeVeld.addFocusListener(new TextFL(checkGrensRechtsWaardeVeld));
		
		checkKansWaardeVeld.addKeyListener(new InputKL(checkKansWaardeVeld, false, false));
		checkKansWaardeVeld.addActionListener(new TextAL(checkKansWaardeVeld));
		checkKansWaardeVeld.addFocusListener(new TextFL(checkKansWaardeVeld));
				

		
// andere listener voor maxScoreVeld?
		
		maxScoreVeld = maakTextField(40, 20, false);
		maxScoreLabel = maakLabel(NormaleVerdeling.rb.getString("maxScoreTekst"));
		maxScoreLabel.setFont(theFont);
		maxScoreLabel.setVisible(false);
		
		maxScoreVeld.addKeyListener(new InputKL(maxScoreVeld, false, true));		
		maxScoreVeld.addActionListener(new TextAL(maxScoreVeld));
		maxScoreVeld.addFocusListener(new TextFL(maxScoreVeld));
		
		enableCheckVelden(false);
		
		plaatsComponenten();
	}
	
	private JCheckBox maakCheckBox(String s, boolean selected, ActionListener al)
	{	JCheckBox checkbox = new JCheckBox(s);
		checkbox.setFont(theFont);
		checkbox.setBackground(bgColor);
		int width = theFM.stringWidth(s) + 30;
		int height = 3 * theFM.getHeight() / 2;
		checkbox.setSize(width,height);
		checkbox.setSelected(selected);
		checkbox.addActionListener(al);
		add(checkbox);
		
		return checkbox;
	}
	
	private JLabel maakLabel (String s)
	{	JLabel label = new JLabel(s);
		label.setFont(theBoldFont);
		int width = theBoldFM.stringWidth(s);
		int height = 3 * theBoldFM.getHeight() / 2;
		label.setSize(width, height);
		add(label);
		return label;
	}
	
//	private JTextField maakTextField(int b, int h, boolean visible, ActionListener al)
	private JTextField maakTextField(int b, int h, boolean visible)
	{	JTextField textField = new JTextField();
		textField.setFont(theFont);
		textField.setSize(b,h);
		//textField.addActionListener(al);
		
		textField.setVisible(visible);
		add(textField);
		
		return textField;
	}
	
	public void plaatsComponenten()
	{	kansOptiesLabel.setLocation(editX + offset, offset);
		kansLinksOptieBox.setLocation(editX + offset, 
			kansOptiesLabel.getLocation().y + 
			kansOptiesLabel.getSize().height);
		kansRechtsOptieBox.setLocation(editX + offset, 
			kansLinksOptieBox.getLocation().y + 
			kansLinksOptieBox.getSize().height);
		tweeGrenzenOptieBox.setLocation(editX + offset, 
			kansRechtsOptieBox.getLocation().y + 
			kansRechtsOptieBox.getSize().height);

		berekenbaarLabel.setLocation(editX + offset, 
			tweeGrenzenOptieBox.getLocation().y + 
			tweeGrenzenOptieBox.getSize().height + offset);
		berekenbaarZichtbaarBox.setLocation(editX + offset, 			
			berekenbaarLabel.getLocation().y + 
			berekenbaarLabel.getSize().height);
		muBerekenbaarBox.setLocation(editX + offset, 			
				berekenbaarZichtbaarBox.getLocation().y + 
				berekenbaarZichtbaarBox.getSize().height);
		sigmaBerekenbaarBox.setLocation(editX + offset, 			
			muBerekenbaarBox.getLocation().y + 
			muBerekenbaarBox.getSize().height);
		
		vasteWaardeLabel.setLocation(editX + offset, 
			sigmaBerekenbaarBox.getLocation().y + 
			sigmaBerekenbaarBox.getSize().height + offset);
		muVastBox.setLocation(editX + offset, 			
			vasteWaardeLabel.getLocation().y + 
			vasteWaardeLabel.getSize().height);
		sigmaVastBox.setLocation(editX + offset, 			
			muVastBox.getLocation().y + 
			muVastBox.getSize().height);
		
		sliderLabel.setLocation(editX + offset, 
				sigmaVastBox.getLocation().y + 
				sigmaVastBox.getSize().height + offset);
		muSliderBox.setLocation(editX + offset, 			
				sliderLabel.getLocation().y + 
				sliderLabel.getSize().height);
		sigmaSliderBox.setLocation(editX + offset, 			
				muSliderBox.getLocation().y + 
				muSliderBox.getSize().height);
		grensSliderBox.setLocation(editX + offset, 			
				sigmaSliderBox.getLocation().y + 
				sigmaSliderBox.getSize().height);
		kansSliderBox.setLocation(editX + offset, 			
				grensSliderBox.getLocation().y + 
				grensSliderBox.getSize().height);
		
		waardenLabel.setLocation(editX2 + offset, offset);
		muZichtbaarBox.setLocation(editX2 + offset, 			
				waardenLabel.getLocation().y + 
				waardenLabel.getSize().height);
		sigmaZichtbaarBox.setLocation(editX2 + offset, 			
				muZichtbaarBox.getLocation().y + 
				muZichtbaarBox.getSize().height);
		grensZichtbaarBox.setLocation(editX2 + offset, 			
				sigmaZichtbaarBox.getLocation().y + 
				sigmaZichtbaarBox.getSize().height);
		kansZichtbaarBox.setLocation(editX2 + offset, 			
				grensZichtbaarBox.getLocation().y + 
				grensZichtbaarBox.getSize().height);
		
		waardenFigLabel.setLocation(editX2 + offset, 
				kansZichtbaarBox.getLocation().y + 
				kansZichtbaarBox.getSize().height + offset);
		muZichtbaarFigBox.setLocation(editX2 + offset, 			
				waardenFigLabel.getLocation().y + 
				waardenFigLabel.getSize().height);
		sigmaZichtbaarFigBox.setLocation(editX2 + offset, 			
				muZichtbaarFigBox.getLocation().y + 
				muZichtbaarFigBox.getSize().height);
		grensZichtbaarFigBox.setLocation(editX2 + offset, 			
				sigmaZichtbaarFigBox.getLocation().y + 
				sigmaZichtbaarFigBox.getSize().height);
		kansZichtbaarFigBox.setLocation(editX2 + offset, 			
				grensZichtbaarFigBox.getLocation().y + 
				grensZichtbaarFigBox.getSize().height);
		
		//checkOptiesLabel.setLocation(editX2 + offset,
		checkOptiesBox.setLocation(editX2 + offset,
				kansZichtbaarFigBox.getLocation().y + 
				kansZichtbaarFigBox.getSize().height + offset);
		checkMuBox.setLocation(editX2 + offset, 			
				//checkOptiesLabel.getLocation().y + 
				//checkOptiesLabel.getSize().height);
				checkOptiesBox.getLocation().y +
				checkOptiesBox.getSize().height);
		checkSigmaBox.setLocation(editX2 + offset, 			
				checkMuBox.getLocation().y + 
				checkMuBox.getSize().height);
		checkGrensBox.setLocation(editX2 + offset, 			
				checkSigmaBox.getLocation().y + 
				checkSigmaBox.getSize().height);
		checkGrensLinksBox.setLocation(editX2 + offset, 			
				checkGrensBox.getLocation().y + 
				checkGrensBox.getSize().height);
		checkGrensRechtsBox.setLocation(editX2 + offset, 			
				checkGrensLinksBox.getLocation().y + 
				checkGrensLinksBox.getSize().height);
		checkKansBox.setLocation(editX2 + offset, 			
				checkGrensRechtsBox.getLocation().y + 
				checkGrensRechtsBox.getSize().height);
		
		checkMuWaardeVeld.setLocation(checkMuBox.getLocation().x + checkMuBox.getSize().width, 			
				//checkOptiesLabel.getLocation().y + 
				//checkOptiesLabel.getSize().height);
				checkOptiesBox.getLocation().y +
				checkOptiesBox.getSize().height);
		checkSigmaWaardeVeld.setLocation(checkSigmaBox.getLocation().x + checkSigmaBox.getSize().width, 			
				checkMuBox.getLocation().y + 
				checkMuBox.getSize().height);
		checkGrensWaardeVeld.setLocation(checkGrensBox.getLocation().x + checkGrensBox.getSize().width, 			
				checkSigmaBox.getLocation().y + 
				checkSigmaBox.getSize().height);
		checkGrensLinksWaardeVeld.setLocation(checkGrensLinksBox.getLocation().x + checkGrensLinksBox.getSize().width, 			
				checkGrensBox.getLocation().y + 
				checkGrensBox.getSize().height);
		checkGrensRechtsWaardeVeld.setLocation(checkGrensRechtsBox.getLocation().x + checkGrensRechtsBox.getSize().width, 			
				checkGrensLinksBox.getLocation().y + 
				checkGrensLinksBox.getSize().height);
		checkKansWaardeVeld.setLocation(checkKansBox.getLocation().x + checkKansBox.getSize().width, 			
				checkGrensRechtsBox.getLocation().y + 
				checkGrensRechtsBox.getSize().height);
		

		maxScoreVeld.setLocation(checkKansWaardeVeld.getLocation().x + checkKansWaardeVeld.getSize().width -
				                 maxScoreVeld.getSize().width, 
				                 checkKansBox.getLocation().y + checkKansBox.getSize().height);
		maxScoreLabel.setLocation(maxScoreVeld.getLocation().x - maxScoreLabel.getSize().width,
								  maxScoreVeld.getLocation().y);
		
	}

	public void enableBerekenbaarOpties(boolean b)
	{	if (b)
		{	berekenbaarLabel.setForeground(Color.black);
			muBerekenbaarBox.setForeground(Color.black);
			muBerekenbaarBox.setEnabled(true);
			sigmaBerekenbaarBox.setForeground(Color.black);
			sigmaBerekenbaarBox.setEnabled(true);			
		}
		else
		{	berekenbaarLabel.setForeground(Color.gray);
			muBerekenbaarBox.setForeground(Color.gray);
			muBerekenbaarBox.setEnabled(false);
			sigmaBerekenbaarBox.setForeground(Color.gray);
			sigmaBerekenbaarBox.setEnabled(false);			
		}
	}

	
	class KansOptiesAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	if (!kansLinksOptieBox.isSelected() &&
				!kansRechtsOptieBox.isSelected() &&
				!tweeGrenzenOptieBox.isSelected())
			{	kansLinksOptieBox.setSelected(true);
			}	
			
			if (!kansLinksOptieBox.isSelected() &&
				!kansRechtsOptieBox.isSelected() &&
				tweeGrenzenOptieBox.isSelected())
			{	enableBerekenbaarOpties(false);
			}	
			else
			{	enableBerekenbaarOpties(true);
			}	
			
			normaalPanel.kansLinksOptie = kansLinksOptieBox.isSelected();
			normaalPanel.kansRechtsOptie = kansRechtsOptieBox.isSelected();
			normaalPanel.tweeGrenzenOptie = tweeGrenzenOptieBox.isSelected();
			normaalPanel.zetKansOpties();
			
		}
	}
	
	class BerekenbaarZichtbaarAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	boolean berekenbaarZichtbaar = berekenbaarZichtbaarBox.isSelected();
			normaalPanel.zetberekenbaarZichtbaar(berekenbaarZichtbaar);		
		}
	}
	
	class MuBerekenbaarAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	boolean muBerekenbaarOptie = muBerekenbaarBox.isSelected();
			if (muBerekenbaarOptie)
				muVastBox.setSelected(false);
			
			normaalPanel.zetMuBerekenbaarOptie(muBerekenbaarOptie);		
		}
	}
	
	class SigmaBerekenbaarAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	boolean sigmaBerekenbaarOptie = sigmaBerekenbaarBox.isSelected();
			if (sigmaBerekenbaarOptie)
				sigmaVastBox.setSelected(false);
			
			normaalPanel.zetSigmaBerekenbaarOptie(sigmaBerekenbaarOptie);		
		}
	}
	
	class MuVastAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	boolean muVastOptie = muVastBox.isSelected();
			if (muVastOptie)
				muBerekenbaarBox.setSelected(false);
				
			normaalPanel.zetMuVastOptie(muVastOptie);	
		}
	}
	
	class SigmaVastAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	boolean sigmaVastOptie = sigmaVastBox.isSelected();
			if (sigmaVastOptie)
				sigmaBerekenbaarBox.setSelected(false);
				
			normaalPanel.zetSigmaVastOptie(sigmaVastOptie);		
		}
	}
	
	class MuSliderAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	boolean muSliderOptie = muSliderBox.isSelected();
			//if (muSliderOptie)
				//muBerekenbaarBox.setSelected(false);
				
			normaalPanel.zetMuSliderOptie(muSliderOptie);	
		}
	}
	
	class SigmaSliderAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	boolean sigmaSliderOptie = sigmaSliderBox.isSelected();
			//if (sigmaSliderOptie)
				//muBerekenbaarBox.setSelected(false);
				
			normaalPanel.zetSigmaSliderOptie(sigmaSliderOptie);	
		}
	}
	
	class GrensSliderAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	boolean grensSliderOptie = grensSliderBox.isSelected();
			//if (kansSliderOptie)
				//muBerekenbaarBox.setSelected(false);
				
			normaalPanel.zetGrensSliderOptie(grensSliderOptie);	
		}
	}
	
	class KansSliderAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	boolean kansSliderOptie = kansSliderBox.isSelected();
			//if (kansSliderOptie)
				//muBerekenbaarBox.setSelected(false);
				
			normaalPanel.zetKansSliderOptie(kansSliderOptie);	
		}
	}
	
	class WaardenAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	if (e.getSource() == muZichtbaarBox)
				normaalPanel.zetMuZichtbaarOptie(muZichtbaarBox.isSelected());	
			if (e.getSource() == sigmaZichtbaarBox)
				normaalPanel.zetSigmaZichtbaarOptie(sigmaZichtbaarBox.isSelected());	
			if (e.getSource() == grensZichtbaarBox)
				normaalPanel.zetGrensZichtbaarOptie(grensZichtbaarBox.isSelected());	
			if (e.getSource() == kansZichtbaarBox)
				normaalPanel.zetKansZichtbaarOptie(kansZichtbaarBox.isSelected());

			if (e.getSource() == muZichtbaarFigBox)
				normaalPanel.zetMuZichtbaarFigOptie(muZichtbaarFigBox.isSelected());	
			if (e.getSource() == sigmaZichtbaarFigBox)
				normaalPanel.zetSigmaZichtbaarFigOptie(sigmaZichtbaarFigBox.isSelected());	
			if (e.getSource() == grensZichtbaarFigBox)
				normaalPanel.zetGrensZichtbaarFigOptie(grensZichtbaarFigBox.isSelected());	
			if (e.getSource() == kansZichtbaarFigBox)
				normaalPanel.zetKansZichtbaarFigOptie(kansZichtbaarFigBox.isSelected());
		}
	}
	
	class CheckOptiesAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	
			if (e.getSource() == checkOptiesBox)
			{
				normaalPanel.zetKijkOpdrachtNa(checkOptiesBox.isSelected());
								
				if (checkOptiesBox.isSelected())
				{
					showCheckItems(true);
				}
				else
				{
					showCheckItems(false);
				}
			}
				
		
			if (e.getSource() == checkMuBox)
			{	checkMuWaardeVeld.setEditable(checkMuBox.isSelected());
				if (checkMuBox.isSelected())
					checkMuWaardeVeld.requestFocus();
				else
					checkMuWaardeVeld.setText("");
			}
			if (e.getSource() == checkSigmaBox)
			{	checkSigmaWaardeVeld.setEditable(checkSigmaBox.isSelected());
				if (checkSigmaBox.isSelected())
					checkSigmaWaardeVeld.requestFocus();
				else
					checkSigmaWaardeVeld.setText("");
			}
			if (e.getSource() == checkGrensBox)
			{	checkGrensWaardeVeld.setEditable(checkGrensBox.isSelected());
				if (checkGrensBox.isSelected())
					checkGrensWaardeVeld.requestFocus();
				else
					checkGrensWaardeVeld.setText("");
			}
			if (e.getSource() == checkGrensLinksBox)
			{	checkGrensLinksWaardeVeld.setEditable(checkGrensLinksBox.isSelected());
				if (checkGrensLinksBox.isSelected())
					checkGrensLinksWaardeVeld.requestFocus();
				else
					checkGrensLinksWaardeVeld.setText("");
			}
			if (e.getSource() == checkGrensRechtsBox)
			{	checkGrensRechtsWaardeVeld.setEditable(checkGrensRechtsBox.isSelected());
				if (checkGrensRechtsBox.isSelected())
					checkGrensRechtsWaardeVeld.requestFocus();
				else
					checkGrensRechtsWaardeVeld.setText("");
			}
			if (e.getSource() == checkKansBox)
			{	checkKansWaardeVeld.setEditable(checkKansBox.isSelected());
				if (checkKansBox.isSelected())
					checkKansWaardeVeld.requestFocus();
				else
					checkKansWaardeVeld.setText("");
			}
						
		}
	}
/*	
	class CheckValuesAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	
		}
	}
*/	
	public void showCheckItems(boolean b)
	{
		checkMuBox.setVisible(b);
		checkSigmaBox.setVisible(b);
		checkGrensBox.setVisible(b);
		checkGrensLinksBox.setVisible(b);
		checkGrensRechtsBox.setVisible(b);
		checkKansBox.setVisible(b);
		
		checkMuWaardeVeld.setVisible(b);
		checkSigmaWaardeVeld.setVisible(b);
		checkGrensWaardeVeld.setVisible(b);
		checkGrensLinksWaardeVeld.setVisible(b);
		checkGrensRechtsWaardeVeld.setVisible(b);
		checkKansWaardeVeld.setVisible(b);
		
		maxScoreLabel.setVisible(b);
		maxScoreVeld.setVisible(b);
	}

	public void enableCheckVelden(boolean b)
	{
		checkMuWaardeVeld.setEditable(b);
		checkSigmaWaardeVeld.setEditable(b);
		checkGrensWaardeVeld.setEditable(b);
		checkGrensLinksWaardeVeld.setEditable(b);
		checkGrensRechtsWaardeVeld.setEditable(b);
		checkKansWaardeVeld.setEditable(b);
		
	}
	
	public void setEditState(Hashtable b)
	{	
		boolean kansLinksOptie = true;
		boolean kansRechtsOptie = true;
		boolean tweeGrenzenOptie = true;
		
		if (b.containsKey("kanslinksoptie"))
			kansLinksOptie = ((Boolean) b.get("kanslinksoptie")).booleanValue();
		if (b.containsKey("kansrechtsoptie"))
			kansRechtsOptie = ((Boolean) b.get("kansrechtsoptie")).booleanValue();
		if (b.containsKey("tweegrenzenoptie"))
			tweeGrenzenOptie = ((Boolean) b.get("tweegrenzenoptie")).booleanValue();
		
		kansLinksOptieBox.setSelected(kansLinksOptie);
		kansRechtsOptieBox.setSelected(kansRechtsOptie);	
		tweeGrenzenOptieBox.setSelected(tweeGrenzenOptie);
	
		boolean berekenbaarZichtbaar = true;	
		boolean muBerekenbaarOptie = false;
		boolean sigmaBerekenbaarOptie = false;
		
		if (b.containsKey("berekenbaarZichtbaar"))
			berekenbaarZichtbaar = ((Boolean) b.get("berekenbaarZichtbaar")).booleanValue();
		if (b.containsKey("muberekenbaaroptie"))
			muBerekenbaarOptie = ((Boolean) b.get("muberekenbaaroptie")).booleanValue();
		if (b.containsKey("sigmaberekenbaaroptie"))
			sigmaBerekenbaarOptie = ((Boolean) b.get("sigmaberekenbaaroptie")).booleanValue();
		
		berekenbaarZichtbaarBox.setSelected(berekenbaarZichtbaar);
		muBerekenbaarBox.setSelected(muBerekenbaarOptie);		
		sigmaBerekenbaarBox.setSelected(sigmaBerekenbaarOptie);				
		
		boolean muVastOptie = false;
		boolean sigmaVastOptie = false;
		
		if (b.containsKey("muvastoptie"))
			muVastOptie = ((Boolean) b.get("muvastoptie")).booleanValue();
		if (b.containsKey("sigmavastoptie"))
			sigmaVastOptie = ((Boolean) b.get("sigmavastoptie")).booleanValue();
				
		if (muBerekenbaarOptie)
			muVastOptie = false;
			
		if (sigmaBerekenbaarOptie)
			sigmaVastOptie = false;
					
		muVastBox.setSelected(muVastOptie);		
		sigmaVastBox.setSelected(sigmaVastOptie);		
		
		boolean muSliderOptie = false;
		boolean sigmaSliderOptie = false;
		boolean kansSliderOptie = false;
		boolean grensSliderOptie = true;
		
		if (b.containsKey("muSliderOptie")) 
			muSliderOptie = ((Boolean) b.get("muSliderOptie")).booleanValue();
		if (b.containsKey("sigmaSliderOptie")) 
			sigmaSliderOptie = ((Boolean) b.get("sigmaSliderOptie")).booleanValue();
		if (b.containsKey("kansSliderOptie")) 
			kansSliderOptie = ((Boolean) b.get("kansSliderOptie")).booleanValue();
		if (b.containsKey("grensSliderOptie")) 
			grensSliderOptie = ((Boolean) b.get("grensSliderOptie")).booleanValue();
		
		muSliderBox.setSelected(muSliderOptie);		
		sigmaSliderBox.setSelected(sigmaSliderOptie);	
		grensSliderBox.setSelected(grensSliderOptie);	
		kansSliderBox.setSelected(kansSliderOptie);
		
		if (!kansLinksOptieBox.isSelected() &&
			!kansRechtsOptieBox.isSelected() &&
			tweeGrenzenOptieBox.isSelected())
		{	enableBerekenbaarOpties(false);
		}	
		else
		{	enableBerekenbaarOpties(true);
		}	
		
		boolean muZichtbaarOptie = true;
		boolean sigmaZichtbaarOptie = true;
		boolean kansZichtbaarOptie = true;
		boolean grensZichtbaarOptie = true;
		
		if (b.containsKey("muZichtbaarOptie")) 
			muZichtbaarOptie = ((Boolean) b.get("muZichtbaarOptie")).booleanValue();
		if (b.containsKey("sigmaZichtbaarOptie")) 
			sigmaZichtbaarOptie = ((Boolean) b.get("sigmaZichtbaarOptie")).booleanValue();
		if (b.containsKey("kansZichtbaarOptie")) 
			kansZichtbaarOptie = ((Boolean) b.get("kansZichtbaarOptie")).booleanValue();
		if (b.containsKey("grensZichtbaarOptie")) 
			grensZichtbaarOptie = ((Boolean) b.get("grensZichtbaarOptie")).booleanValue();
		
		muZichtbaarBox.setSelected(muZichtbaarOptie);		
		sigmaZichtbaarBox.setSelected(sigmaZichtbaarOptie);	
		grensZichtbaarBox.setSelected(grensZichtbaarOptie);	
		kansZichtbaarBox.setSelected(kansZichtbaarOptie);
		
		boolean muZichtbaarFigOptie = true;
		boolean sigmaZichtbaarFigOptie = true;
		boolean kansZichtbaarFigOptie = true;
		boolean grensZichtbaarFigOptie = true;
		
		if (b.containsKey("muZichtbaarFigOptie")) 
			muZichtbaarFigOptie = ((Boolean) b.get("muZichtbaarFigOptie")).booleanValue();
		if (b.containsKey("sigmaZichtbaarFigOptie")) 
			sigmaZichtbaarFigOptie = ((Boolean) b.get("sigmaZichtbaarFigOptie")).booleanValue();
		if (b.containsKey("kansZichtbaarFigOptie")) 
			kansZichtbaarFigOptie = ((Boolean) b.get("kansZichtbaarFigOptie")).booleanValue();
		if (b.containsKey("grensZichtbaarFigOptie")) 
			grensZichtbaarFigOptie = ((Boolean) b.get("grensZichtbaarFigOptie")).booleanValue();
		
		muZichtbaarFigBox.setSelected(muZichtbaarFigOptie);		
		sigmaZichtbaarFigBox.setSelected(sigmaZichtbaarFigOptie);	
		grensZichtbaarFigBox.setSelected(grensZichtbaarFigOptie);	
		kansZichtbaarFigBox.setSelected(kansZichtbaarFigOptie);
		
		boolean kijkNa = false;
		
		if (b.containsKey("kijkNa"))
			kijkNa = ((Boolean) b.get("kijkNa")).booleanValue();
		
		checkOptiesBox.setSelected(kijkNa);
		showCheckItems(kijkNa);
		
		if (kijkNa)
		{
			
			normaalPanel.zetKijkOpdrachtNa(true);
			
			boolean kijkMuNa = false;
			boolean kijkSigmaNa = false;
			boolean kijkGrensNa = false;
			boolean kijkGrensLinksNa = false;
			boolean kijkGrensRechtsNa = false;
			boolean kijkKansNa = false;
			
			if (b.containsKey("kijkMuNa"))
			{	kijkMuNa = ((Boolean) b.get("kijkMuNa")).booleanValue();
				checkMuBox.setSelected(kijkMuNa);
				checkMuWaardeVeld.setEditable(kijkMuNa);
				if (kijkMuNa)
				{	if (b.containsKey("checkMu"))
						checkMuWaardeVeld.setText((String) b.get("checkMu"));
				}
			}
			if (b.containsKey("kijkSigmaNa"))
			{	kijkSigmaNa = ((Boolean) b.get("kijkSigmaNa")).booleanValue();
				checkSigmaBox.setSelected(kijkSigmaNa);
				checkSigmaWaardeVeld.setEditable(kijkSigmaNa);
				if (kijkSigmaNa)
				{	if (b.containsKey("checkSigma"))
						checkSigmaWaardeVeld.setText((String) b.get("checkSigma"));
				}
			}
			if (b.containsKey("kijkGrensNa"))
			{	kijkGrensNa = ((Boolean) b.get("kijkGrensNa")).booleanValue();
				checkGrensBox.setSelected(kijkGrensNa);
				checkGrensWaardeVeld.setEditable(kijkGrensNa);
				if (kijkGrensNa)
				{	if (b.containsKey("checkGrens"))
						checkGrensWaardeVeld.setText((String) b.get("checkGrens"));
				}
			}
			if (b.containsKey("kijkGrensLinksNa"))
			{	kijkGrensLinksNa = ((Boolean) b.get("kijkGrensLinksNa")).booleanValue();
				checkGrensLinksBox.setSelected(kijkGrensLinksNa);
				checkGrensLinksWaardeVeld.setEditable(kijkGrensLinksNa);
				if (kijkGrensLinksNa)
				{	if (b.containsKey("checkGrensLinks"))
						checkGrensLinksWaardeVeld.setText((String) b.get("checkGrensLinks"));
				}
			}
			if (b.containsKey("kijkGrensRechtsNa"))
			{	kijkGrensRechtsNa = ((Boolean) b.get("kijkGrensRechtsNa")).booleanValue();
				checkGrensRechtsBox.setSelected(kijkGrensRechtsNa);
				checkGrensRechtsWaardeVeld.setEditable(kijkGrensRechtsNa);
				if (kijkGrensRechtsNa)
				{	if (b.containsKey("checkGrensRechts"))
						checkGrensRechtsWaardeVeld.setText((String) b.get("checkGrensRechts"));
				}
			}
			if (b.containsKey("kijkKansNa"))
			{	kijkKansNa = ((Boolean) b.get("kijkKansNa")).booleanValue();
				checkKansBox.setSelected(kijkKansNa);
				checkKansWaardeVeld.setEditable(kijkKansNa);
				if (kijkKansNa)
				{	if (b.containsKey("checkKans"))
						checkKansWaardeVeld.setText((String) b.get("checkKans"));
				}
			}

			if (b.containsKey("maxScore"))
			{	maxScoreVeld.setText((String) b.get("maxScore"));
			}
			
		}
			
		
		
		normaalPanel.setEditState(b);
	}

	public Hashtable getEditState()
	{	
	    Hashtable h = normaalPanel.getEditState();
	    //Hashtable h = new Hashtable();
	    
	    h.put("kijkNa", new Boolean(checkOptiesBox.isSelected()));
	    
	    if (checkOptiesBox.isSelected())
	    {
	    	h.put("kijkMuNa", new Boolean(checkMuBox.isSelected()));
	    	if (checkMuBox.isSelected())
	    		h.put("checkMu", checkMuWaardeVeld.getText());
	    	
	    	h.put("kijkSigmaNa", new Boolean(checkSigmaBox.isSelected()));
	    	if (checkSigmaBox.isSelected())
	    		h.put("checkSigma", checkSigmaWaardeVeld.getText());
	    	
	    	h.put("kijkGrensNa", new Boolean(checkGrensBox.isSelected()));
	    	if (checkGrensBox.isSelected())
	    		h.put("checkGrens", checkGrensWaardeVeld.getText());
	    	
	    	h.put("kijkGrensLinksNa", new Boolean(checkGrensLinksBox.isSelected()));
	    	if (checkGrensLinksBox.isSelected())
	    		h.put("checkGrensLinks", checkGrensLinksWaardeVeld.getText());
	    	
	    	h.put("kijkGrensRechtsNa", new Boolean(checkGrensRechtsBox.isSelected()));
	    	if (checkGrensRechtsBox.isSelected())
	    		h.put("checkGrensRechts", checkGrensRechtsWaardeVeld.getText());

	    	h.put("kijkKansNa", new Boolean(checkKansBox.isSelected()));
	    	if (checkKansBox.isSelected())
	    		h.put("checkKans", checkKansWaardeVeld.getText());
	    	
	    	h.put("maxScore", maxScoreVeld.getText());
	    	
	    	int scoreMax = 0;
	    	try{
	    		scoreMax = Integer.parseInt(maxScoreVeld.getText());
	    	}
	    	catch(NumberFormatException e){}
	    	h.put("scoreMax", new Integer(scoreMax));
	    	
	    }

	    return h;
	}

	// dit moet natuurlijk met een super !!		
	public void setBounds(int x, int y, int b, int h)
	{	super.setBounds(x, y, b, h);
	
		editX = getSize().width - editBreedte;
		editX2 = getSize().width - editBreedte/2;
		// dit alleen als alles al geconstrueerd is
		if (normaalPanel != null)
		{	//normaalPanel.setBounds(0, 0, b - editBreedte, h);
			plaatsComponenten();	
		}	
	}

	public void zetBreedte(int b)
	{	normaalPanel.setBounds(normaalPanel.getLocation().x, normaalPanel.getLocation().y, b, normaalPanel.getSize().height);
	}
	
	public void zetHoogte(int h)
	{	normaalPanel.setBounds(normaalPanel.getLocation().x, normaalPanel.getLocation().y, normaalPanel.getSize().width, h);
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
	{}

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