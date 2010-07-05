package fi.normaleverdeling;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;

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
	
	JLabel checkOptiesLabel;
	JCheckBox checkMuBox,
			checkSigmaBox,
			checkGrensBox,
			checkGrensLinksBox,
			checkGrensRechtsBox,
			checkKansBox;
	
	JTextField checkMuWaardeVeld,
			checkSigmaWaardeVeld,
			checkGrensWaardeVeld,
			checkGrensLinksWaardeVeld,
			checkGrensRechtsWaardeVeld,
			checkKansWaardeVeld;


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
		
		checkOptiesLabel = maakLabel(NormaleVerdeling.rb.getString("checkOptiesLabelTekst"));
		
		checkMuBox = maakCheckBox(NormaleVerdeling.rb.getString("checkMuTekst"), false, listener);
		checkSigmaBox = maakCheckBox(NormaleVerdeling.rb.getString("checkSigmaTekst"), false, listener);
		checkGrensBox = maakCheckBox(NormaleVerdeling.rb.getString("checkGrensTekst"), false, listener);
		checkGrensLinksBox = maakCheckBox(NormaleVerdeling.rb.getString("checkGrensLinksTekst"), false, listener);
		checkGrensRechtsBox = maakCheckBox(NormaleVerdeling.rb.getString("checkGrensRechtsTekst"), false, listener);
		checkKansBox = maakCheckBox(NormaleVerdeling.rb.getString("checkKansTekst"), false, listener);
		
		listener = new CheckValuesAL();
		
		checkMuWaardeVeld = maakTextField(60,20, true, listener);
		checkSigmaWaardeVeld = maakTextField(60,20, true, listener);
		checkGrensWaardeVeld = maakTextField(60,20, true, listener);
		checkGrensLinksWaardeVeld = maakTextField(60,20, true, listener);
		checkGrensRechtsWaardeVeld = maakTextField(60,20, true, listener);
		checkKansWaardeVeld = maakTextField(60,20, true, listener);
		
		
		
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
	
	private JTextField maakTextField(int b, int h, boolean visible, ActionListener al)
	{	JTextField textField = new JTextField();
		textField.setFont(theFont);
		textField.setSize(b,h);
		textField.addActionListener(al);
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
		
		checkOptiesLabel.setLocation(editX2 + offset, 
				kansZichtbaarFigBox.getLocation().y + 
				kansZichtbaarFigBox.getSize().height + offset);
		checkMuBox.setLocation(editX2 + offset, 			
				checkOptiesLabel.getLocation().y + 
				checkOptiesLabel.getSize().height);
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
				checkOptiesLabel.getLocation().y + 
				checkOptiesLabel.getSize().height);
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
		{	//if(e.getSource()==checkMuBox)normaalPanel.zetCheckMuOptie(checkMuBox.isSelected());	
			//if(e.getSource()==checkSigmaBox)normaalPanel.zetCheckSigmaOptie(checkSigmaBox.isSelected());	
			//if(e.getSource()==checkGrensBox)normaalPanel.zetCheckGrensOptie(checkGrensBox.isSelected());	
			//if(e.getSource()==checkGrensLinksBox)normaalPanel.zetCheckGrensLinksOptie(checkGrensLinksBox.isSelected());
			//if(e.getSource()==checkGrensRechtsBox)normaalPanel.zetCheckGrensRechtsOptie(checkGrensRechtsBox.isSelected());	
			//if(e.getSource()==checkKansBox)normaalPanel.zetCheckKansOptie(checkKansBox.isSelected());	
			
		}
	}
	
	class CheckValuesAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	//if(e.getSource()==checkMuBox)normaalPanel.zetCheckMuOptie(checkMuBox.isSelected());	
			//if(e.getSource()==checkSigmaBox)normaalPanel.zetCheckSigmaOptie(checkSigmaBox.isSelected());	
			//if(e.getSource()==checkGrensBox)normaalPanel.zetCheckGrensOptie(checkGrensBox.isSelected());	
			//if(e.getSource()==checkGrensLinksBox)normaalPanel.zetCheckGrensLinksOptie(checkGrensLinksBox.isSelected());
			//if(e.getSource()==checkGrensRechtsBox)normaalPanel.zetCheckGrensRechtsOptie(checkGrensRechtsBox.isSelected());	
			//if(e.getSource()==checkKansBox)normaalPanel.zetCheckKansOptie(checkKansBox.isSelected());	
			
		}
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
		
		
		normaalPanel.setEditState(b);
	}

	public Hashtable getEditState()
	{	
	    Hashtable h = normaalPanel.getEditState();
	    //Hashtable h = new Hashtable();
	    

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

}