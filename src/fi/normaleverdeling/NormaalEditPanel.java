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
	JCheckBox muBerekenbaarBox,
			  sigmaBerekenbaarBox;
			  
	JLabel vasteWaardeLabel;
	JCheckBox muVastBox,
			  sigmaVastBox;
			  			  


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
		muBerekenbaarBox.setLocation(editX + offset, 			
			berekenbaarLabel.getLocation().y + 
			berekenbaarLabel.getSize().height);
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
		
		if (b.containsKey("muvastoptie"))
			muVastOptie = 
				((Boolean) b.get("muvastoptie")).booleanValue();
		if (b.containsKey("sigmavastoptie"))
			sigmaVastOptie = 
				((Boolean) b.get("sigmavastoptie")).booleanValue();
				
		if (muBerekenbaarOptie)
			muVastOptie = false;
			
		if (sigmaBerekenbaarOptie)
			sigmaVastOptie = false;
					
		
		muVastBox.setSelected(muVastOptie);		
		sigmaVastBox.setSelected(sigmaVastOptie);				
		

		if (!kansLinksOptieBox.isSelected() &&
			!kansRechtsOptieBox.isSelected() &&
			tweeGrenzenOptieBox.isSelected())
		{	enableBerekenbaarOpties(false);
		}	
		else
		{	enableBerekenbaarOpties(true);
		}	
		
		
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