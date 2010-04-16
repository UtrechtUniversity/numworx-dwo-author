package fi.normaleverdeling;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;

public class NormaalEditPanel extends JPanel implements InteractieEditPanel
{	
	protected static int editBreedte = 150;	
	protected static Color bgColor = new Color(Integer.parseInt("DDEEFF", 16));

	protected NormaalPanel normaalPanel;


	// fonts
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;

	int offset = 15;
	int editX;

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


	public NormaalEditPanel(int w, int h)
	{	//normaleVerdeling = o;
	
		setBackground(bgColor);
		setLayout(null);
		setSize(w, h);
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		
		normaalPanel = new NormaalPanel(w - editBreedte, h);
		normaalPanel.setLocation(0, 0);
		add(normaalPanel);
		
		editX = getSize().width - editBreedte;
		
		kansOptiesLabel = new JLabel(
			NormaleVerdeling.rb.getString("kansOptiesTekst"));
		kansOptiesLabel.setFont(theBoldFont);
		int width = theBoldFM.stringWidth(kansOptiesLabel.getText());
		int height = 3 * theBoldFM.getHeight() / 2;
		kansOptiesLabel.setSize(width, height);
		add(kansOptiesLabel);
		
		kansLinksOptieBox = new JCheckBox(
			NormaleVerdeling.rb.getString("kansLinksTekst"));
		kansLinksOptieBox.setFont(theFont);
		kansLinksOptieBox.setBackground(bgColor);
		width = theFM.stringWidth(kansLinksOptieBox.getText()) + 30;
		height = 3 * theFM.getHeight() / 2;
		kansLinksOptieBox.setSize(width, height);
		add(kansLinksOptieBox);
		kansLinksOptieBox.setSelected(true);
		
		kansRechtsOptieBox = new JCheckBox(
			NormaleVerdeling.rb.getString("kansRechtsTekst"));
		kansRechtsOptieBox.setFont(theFont);
		kansRechtsOptieBox.setBackground(bgColor);
		width = theFM.stringWidth(kansRechtsOptieBox.getText()) + 30;
		//height = 3 * theFM.getHeight() / 2;
		kansRechtsOptieBox.setSize(width, height);
		add(kansRechtsOptieBox);
		kansRechtsOptieBox.setSelected(true);
		
		tweeGrenzenOptieBox = new JCheckBox(
			NormaleVerdeling.rb.getString("tweeGrenzenTekst"));
		tweeGrenzenOptieBox.setFont(theFont);
		tweeGrenzenOptieBox.setBackground(bgColor);
		width = theFM.stringWidth(tweeGrenzenOptieBox.getText()) + 30;
		//height = 3 * theFM.getHeight() / 2;
		tweeGrenzenOptieBox.setSize(width, height);
		add(tweeGrenzenOptieBox);
		tweeGrenzenOptieBox.setSelected(true);

		KansOptiesAL listener = new KansOptiesAL();
		kansLinksOptieBox.addActionListener(listener);
		kansRechtsOptieBox.addActionListener(listener);
		tweeGrenzenOptieBox.addActionListener(listener);
	
		berekenbaarLabel = new JLabel(
			NormaleVerdeling.rb.getString("berekenbaarTekst"));
		berekenbaarLabel.setFont(theBoldFont);
		width = theBoldFM.stringWidth(berekenbaarLabel.getText());
		height = 3 * theBoldFM.getHeight() / 2;
		berekenbaarLabel.setSize(width, height);
		add(berekenbaarLabel);
		
		berekenbaarZichtbaarBox = new JCheckBox(
				NormaleVerdeling.rb.getString("berekenbaarZichtbaarTekst"));
		berekenbaarZichtbaarBox.setFont(theFont);
		berekenbaarZichtbaarBox.setBackground(bgColor);
			width = theFM.stringWidth(berekenbaarZichtbaarBox.getText()) + 30;
			height = 3 * theFM.getHeight() / 2;
			berekenbaarZichtbaarBox.setSize(width, height);
			add(berekenbaarZichtbaarBox);
			berekenbaarZichtbaarBox.addActionListener(new BerekenbaarZichtbaarAL());
			berekenbaarZichtbaarBox.setSelected(true);
			
		muBerekenbaarBox = new JCheckBox(
			NormaleVerdeling.rb.getString("muTekst"));
		muBerekenbaarBox.setFont(theFont);
		muBerekenbaarBox.setBackground(bgColor);
		width = theFM.stringWidth(muBerekenbaarBox.getText()) + 30;
		height = 3 * theFM.getHeight() / 2;
		muBerekenbaarBox.setSize(width, height);
		add(muBerekenbaarBox);
		muBerekenbaarBox.addActionListener(new MuBerekenbaarAL());

		sigmaBerekenbaarBox = new JCheckBox(
			NormaleVerdeling.rb.getString("sigmaTekst"));
		sigmaBerekenbaarBox.setFont(theFont);
		sigmaBerekenbaarBox.setBackground(bgColor);
		width = theFM.stringWidth(sigmaBerekenbaarBox.getText()) + 30;
		//height = 3 * theFM.getHeight() / 2;
		sigmaBerekenbaarBox.setSize(width, height);
		add(sigmaBerekenbaarBox);
		sigmaBerekenbaarBox.addActionListener(new SigmaBerekenbaarAL());		
		
		vasteWaardeLabel = new JLabel(
			NormaleVerdeling.rb.getString("vasteWaardeTekst"));
		vasteWaardeLabel.setFont(theBoldFont);
		width = theBoldFM.stringWidth(vasteWaardeLabel.getText());
		height = 3 * theBoldFM.getHeight() / 2;
		vasteWaardeLabel.setSize(width, height);
		add(vasteWaardeLabel);
		
		muVastBox = new JCheckBox(
			NormaleVerdeling.rb.getString("muTekst"));
		muVastBox.setFont(theFont);
		muVastBox.setBackground(bgColor);
		width = theFM.stringWidth(muVastBox.getText()) + 30;
		height = 3 * theFM.getHeight() / 2;
		muVastBox.setSize(width, height);
		add(muVastBox);
		muVastBox.addActionListener(new MuVastAL());		

		sigmaVastBox = new JCheckBox(
			NormaleVerdeling.rb.getString("sigmaTekst"));
		sigmaVastBox.setFont(theFont);
		sigmaVastBox.setBackground(bgColor);
		width = theFM.stringWidth(sigmaVastBox.getText()) + 30;
		//height = 3 * theFM.getHeight() / 2;
		sigmaVastBox.setSize(width, height);
		add(sigmaVastBox);
		sigmaVastBox.addActionListener(new SigmaVastAL());				
		
		sliderLabel = new JLabel(
			NormaleVerdeling.rb.getString("sliderLabelTekst"));
		sliderLabel.setFont(theBoldFont);
		width = theBoldFM.stringWidth(sliderLabel.getText());
		height = 3 * theBoldFM.getHeight() / 2;
		sliderLabel.setSize(width, height);
		add(sliderLabel);
		
		muSliderBox = new JCheckBox(
			NormaleVerdeling.rb.getString("muSliderTekst"));
		muSliderBox.setFont(theFont);
		muSliderBox.setBackground(bgColor);
		width = theFM.stringWidth(muSliderBox.getText()) + 30;
		height = 3 * theFM.getHeight() / 2;
		muSliderBox.setSize(width, height);
		add(muSliderBox);
		muSliderBox.addActionListener(new MuSliderAL());		
			sigmaSliderBox = new JCheckBox(
			NormaleVerdeling.rb.getString("sigmaSliderTekst"));
		sigmaSliderBox.setFont(theFont);
		sigmaSliderBox.setBackground(bgColor);
		width = theFM.stringWidth(sigmaSliderBox.getText()) + 30;
		//height = 3 * theFM.getHeight() / 2;
		sigmaSliderBox.setSize(width, height);
		add(sigmaSliderBox);
		sigmaSliderBox.addActionListener(new SigmaSliderAL());
		
		grensSliderBox = new JCheckBox(
				NormaleVerdeling.rb.getString("grensSliderTekst"));
		grensSliderBox.setFont(theFont);
		grensSliderBox.setBackground(bgColor);
		width = theFM.stringWidth(grensSliderBox.getText()) + 30;
		//height = 3 * theFM.getHeight() / 2;
		grensSliderBox.setSize(width, height);
		add(grensSliderBox);
		grensSliderBox.addActionListener(new GrensSliderAL());
		
		kansSliderBox = new JCheckBox(
				NormaleVerdeling.rb.getString("kansSliderTekst"));
		kansSliderBox.setFont(theFont);
		kansSliderBox.setBackground(bgColor);
		width = theFM.stringWidth(kansSliderBox.getText()) + 30;
		//height = 3 * theFM.getHeight() / 2;
		kansSliderBox.setSize(width, height);
		add(kansSliderBox);
		kansSliderBox.addActionListener(new KansSliderAL());
				
		waardenLabel = new JLabel(NormaleVerdeling.rb.getString("waardenLabelTekst"));
		waardenLabel.setFont(theBoldFont);
		width = theBoldFM.stringWidth(waardenLabel.getText());
		height = 3 * theBoldFM.getHeight() / 2;
		waardenLabel.setSize(width, height);
		add(waardenLabel);	
		
		muZichtbaarBox = new JCheckBox(NormaleVerdeling.rb.getString("muZichtbaarTekst"));
		muZichtbaarBox.setFont(theFont);
		muZichtbaarBox.setBackground(bgColor);
		width = theFM.stringWidth(muZichtbaarBox.getText()) + 30;
		height = 3 * theFM.getHeight() / 2;
		muZichtbaarBox.setSize(width, height);
		add(muZichtbaarBox);
		muZichtbaarBox.addActionListener(new WaardenAL());	
		muZichtbaarBox.setSelected(true);
		
		sigmaZichtbaarBox = new JCheckBox(NormaleVerdeling.rb.getString("sigmaZichtbaarTekst"));
		sigmaZichtbaarBox.setFont(theFont);
		sigmaZichtbaarBox.setBackground(bgColor);
		width = theFM.stringWidth(sigmaZichtbaarBox.getText()) + 30;
		//height = 3 * theFM.getHeight() / 2;
		sigmaZichtbaarBox.setSize(width, height);
		add(sigmaZichtbaarBox);
		sigmaZichtbaarBox.addActionListener(new WaardenAL());
		sigmaZichtbaarBox.setSelected(true);
		
		
		grensZichtbaarBox = new JCheckBox(NormaleVerdeling.rb.getString("grensZichtbaarTekst"));
		grensZichtbaarBox.setFont(theFont);
		grensZichtbaarBox.setBackground(bgColor);
		width = theFM.stringWidth(grensSliderBox.getText()) + 30;
		//height = 3 * theFM.getHeight() / 2;
		grensZichtbaarBox.setSize(width, height);
		add(grensZichtbaarBox);
		grensZichtbaarBox.addActionListener(new WaardenAL());
		grensZichtbaarBox.setSelected(true);
		
		kansZichtbaarBox = new JCheckBox(NormaleVerdeling.rb.getString("kansZichtbaarTekst"));
		kansZichtbaarBox.setFont(theFont);
		kansZichtbaarBox.setBackground(bgColor);
		width = theFM.stringWidth(kansZichtbaarBox.getText()) + 30;
		//height = 3 * theFM.getHeight() / 2;
		kansZichtbaarBox.setSize(width, height);
		add(kansZichtbaarBox);
		kansZichtbaarBox.addActionListener(new WaardenAL());
		kansZichtbaarBox.setSelected(true);
		
		waardenFigLabel = new JLabel(NormaleVerdeling.rb.getString("waardenFigLabelTekst"));
		waardenFigLabel.setFont(theBoldFont);
		width = theBoldFM.stringWidth(waardenFigLabel.getText());
		height = 3 * theBoldFM.getHeight() / 2;
		waardenFigLabel.setSize(width, height);
		add(waardenFigLabel);	
		
		muZichtbaarFigBox = new JCheckBox(NormaleVerdeling.rb.getString("muZichtbaarTekst"));
		muZichtbaarFigBox.setFont(theFont);
		muZichtbaarFigBox.setBackground(bgColor);
		width = theFM.stringWidth(muZichtbaarFigBox.getText()) + 30;
		height = 3 * theFM.getHeight() / 2;
		muZichtbaarFigBox.setSize(width, height);
		add(muZichtbaarFigBox);
		muZichtbaarFigBox.addActionListener(new WaardenAL());	
		muZichtbaarFigBox.setSelected(true);
		
		sigmaZichtbaarFigBox = new JCheckBox(NormaleVerdeling.rb.getString("sigmaZichtbaarFigTekst"));
		sigmaZichtbaarFigBox.setFont(theFont);
		sigmaZichtbaarFigBox.setBackground(bgColor);
		width = theFM.stringWidth(sigmaZichtbaarFigBox.getText()) + 30;
		//height = 3 * theFM.getHeight() / 2;
		sigmaZichtbaarFigBox.setSize(width, height);
		add(sigmaZichtbaarFigBox);
		sigmaZichtbaarFigBox.addActionListener(new WaardenAL());
		sigmaZichtbaarFigBox.setSelected(true);
		
		grensZichtbaarFigBox = new JCheckBox(NormaleVerdeling.rb.getString("grensZichtbaarFigTekst"));
		grensZichtbaarFigBox.setFont(theFont);
		grensZichtbaarFigBox.setBackground(bgColor);
		width = theFM.stringWidth(grensZichtbaarFigBox.getText()) + 30;
		//height = 3 * theFM.getHeight() / 2;
		grensZichtbaarFigBox.setSize(width, height);
		add(grensZichtbaarFigBox);
		grensZichtbaarFigBox.addActionListener(new WaardenAL());
		grensZichtbaarFigBox.setSelected(true);
		
		kansZichtbaarFigBox = new JCheckBox(NormaleVerdeling.rb.getString("kansZichtbaarFigTekst"));
		kansZichtbaarFigBox.setFont(theFont);
		kansZichtbaarFigBox.setBackground(bgColor);
		width = theFM.stringWidth(kansZichtbaarFigBox.getText()) + 30;
		//height = 3 * theFM.getHeight() / 2;
		kansZichtbaarFigBox.setSize(width, height);
		add(kansZichtbaarFigBox);
		kansZichtbaarFigBox.addActionListener(new WaardenAL());
		kansZichtbaarFigBox.setSelected(true);
			
		plaatsComponenten();
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
		
		waardenLabel.setLocation(editX + offset, 
				kansSliderBox.getLocation().y + 
				kansSliderBox.getSize().height + offset);
		muZichtbaarBox.setLocation(editX + offset, 			
				waardenLabel.getLocation().y + 
				waardenLabel.getSize().height);
		sigmaZichtbaarBox.setLocation(editX + offset, 			
				muZichtbaarBox.getLocation().y + 
				muZichtbaarBox.getSize().height);
		grensZichtbaarBox.setLocation(editX + offset, 			
				sigmaZichtbaarBox.getLocation().y + 
				sigmaZichtbaarBox.getSize().height);
		kansZichtbaarBox.setLocation(editX + offset, 			
				grensZichtbaarBox.getLocation().y + 
				grensZichtbaarBox.getSize().height);
		
		waardenFigLabel.setLocation(editX + offset, 
				kansZichtbaarBox.getLocation().y + 
				kansZichtbaarBox.getSize().height + offset);
		muZichtbaarFigBox.setLocation(editX + offset, 			
				waardenFigLabel.getLocation().y + 
				waardenFigLabel.getSize().height);
		sigmaZichtbaarFigBox.setLocation(editX + offset, 			
				muZichtbaarFigBox.getLocation().y + 
				muZichtbaarFigBox.getSize().height);
		grensZichtbaarFigBox.setLocation(editX + offset, 			
				sigmaZichtbaarFigBox.getLocation().y + 
				sigmaZichtbaarFigBox.getSize().height);
		kansZichtbaarFigBox.setLocation(editX + offset, 			
				grensZichtbaarFigBox.getLocation().y + 
				grensZichtbaarFigBox.getSize().height);
			
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
		{	if(e.getSource()==muZichtbaarBox)normaalPanel.zetMuZichtbaarOptie(muZichtbaarBox.isSelected());	
			if(e.getSource()==sigmaZichtbaarBox)normaalPanel.zetSigmaZichtbaarOptie(sigmaZichtbaarBox.isSelected());	
			if(e.getSource()==grensZichtbaarBox)normaalPanel.zetGrensZichtbaarOptie(grensZichtbaarBox.isSelected());	
			if(e.getSource()==kansZichtbaarBox)normaalPanel.zetKansZichtbaarOptie(kansZichtbaarBox.isSelected());
			if(e.getSource()==muZichtbaarFigBox)normaalPanel.zetMuZichtbaarFigOptie(muZichtbaarFigBox.isSelected());	
			if(e.getSource()==sigmaZichtbaarFigBox)normaalPanel.zetSigmaZichtbaarFigOptie(sigmaZichtbaarFigBox.isSelected());	
			if(e.getSource()==grensZichtbaarFigBox)normaalPanel.zetGrensZichtbaarFigOptie(grensZichtbaarFigBox.isSelected());	
			if(e.getSource()==kansZichtbaarFigBox)normaalPanel.zetKansZichtbaarFigOptie(kansZichtbaarFigBox.isSelected());
		}
	}
	
	
	
	public void setEditState(Hashtable b)
	{	boolean kansLinksOptie = true;
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
		
		boolean muBerekenbaarOptie = false;
		boolean sigmaBerekenbaarOptie = false;
		
		if (b.containsKey("muberekenbaaroptie"))
			muBerekenbaarOptie = 
				((Boolean) b.get("muberekenbaaroptie")).booleanValue();
		if (b.containsKey("sigmaberekenbaaroptie"))
			sigmaBerekenbaarOptie = 
				((Boolean) b.get("sigmaberekenbaaroptie")).booleanValue();
		
		muBerekenbaarBox.setSelected(muBerekenbaarOptie);		
		sigmaBerekenbaarBox.setSelected(sigmaBerekenbaarOptie);				
		
		boolean muVastOptie = false;
		boolean sigmaVastOptie = false;
		
		if (b.containsKey("muvastoptie"))muVastOptie = 	((Boolean) b.get("muvastoptie")).booleanValue();
		if (b.containsKey("sigmavastoptie"))sigmaVastOptie = 	((Boolean) b.get("sigmavastoptie")).booleanValue();
				
				
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
		
		if (b.containsKey("muSliderOptie")) muSliderOptie = ((Boolean) b.get("muSliderOptie")).booleanValue();
		if (b.containsKey("sigmaSliderOptie")) sigmaSliderOptie = ((Boolean) b.get("sigmaSliderOptie")).booleanValue();
		if (b.containsKey("kansSliderOptie")) kansSliderOptie = ((Boolean) b.get("kansSliderOptie")).booleanValue();
		if (b.containsKey("grensSliderOptie")) grensSliderOptie = ((Boolean) b.get("grensSliderOptie")).booleanValue();
				
				
		if (muBerekenbaarOptie)
			muVastOptie = false;
			
		if (sigmaBerekenbaarOptie)
			sigmaVastOptie = false;
					
		
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
		
		if (b.containsKey("muZichtbaarOptie")) muZichtbaarOptie = ((Boolean) b.get("muZichtbaarOptie")).booleanValue();
		if (b.containsKey("sigmaZichtbaarOptie")) sigmaZichtbaarOptie = ((Boolean) b.get("sigmaZichtbaarOptie")).booleanValue();
		if (b.containsKey("kansZichtbaarOptie")) kansZichtbaarOptie = ((Boolean) b.get("kansZichtbaarOptie")).booleanValue();
		if (b.containsKey("grensZichtbaarOptie")) grensZichtbaarOptie = ((Boolean) b.get("grensZichtbaarOptie")).booleanValue();
		
		muZichtbaarBox.setSelected(muZichtbaarOptie);		
		sigmaZichtbaarBox.setSelected(sigmaZichtbaarOptie);	
		grensZichtbaarBox.setSelected(grensZichtbaarOptie);	
		kansZichtbaarBox.setSelected(kansZichtbaarOptie);
		
		boolean muZichtbaarFigOptie = true;
		boolean sigmaZichtbaarFigOptie = true;
		boolean kansZichtbaarFigOptie = true;
		boolean grensZichtbaarFigOptie = true;
		
		if (b.containsKey("muZichtbaarFigOptie")) muZichtbaarFigOptie = ((Boolean) b.get("muZichtbaarFigOptie")).booleanValue();
		if (b.containsKey("sigmaZichtbaarFigOptie")) sigmaZichtbaarFigOptie = ((Boolean) b.get("sigmaZichtbaarFigOptie")).booleanValue();
		if (b.containsKey("kansZichtbaarFigOptie")) kansZichtbaarFigOptie = ((Boolean) b.get("kansZichtbaarFigOptie")).booleanValue();
		if (b.containsKey("grensZichtbaarFigOptie")) grensZichtbaarFigOptie = ((Boolean) b.get("grensZichtbaarFigOptie")).booleanValue();
		
		muZichtbaarFigBox.setSelected(muZichtbaarFigOptie);		
		sigmaZichtbaarFigBox.setSelected(sigmaZichtbaarFigOptie);	
		grensZichtbaarFigBox.setSelected(kansZichtbaarFigOptie);	
		kansZichtbaarFigBox.setSelected(grensZichtbaarFigOptie);
		
		
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
	
		// dit alleen als alles al geconstrueerd is
		if (normaalPanel != null)
		{	normaalPanel.setBounds(0, 0, b - editBreedte, h);
			plaatsComponenten();	
		}	
	}

	public void zetBreedte(int b)
	{	setBounds(getLocation().x, getLocation().y, b, getSize().height);
	}
	
	public void zetHoogte(int h)
	{	setBounds(getLocation().x, getLocation().y, getSize().width, h);
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