package fi.calculatordwo;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class CalculatorDwoInteractieEditPanel  extends JPanel implements ActionListener, InteractieEditPanel

{	int editWidth = 190;
	int editHeight = 550; 
	int cdipBreedte = 540; // startbreedte ip
	int cdipHoogte = 300; // starthoogte ip
	
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	int offset = 4;
	int currentX;
	int currentY;
	int width;
	int height;
	
	protected CalculatorDwoInteractiePanel cdip;
	JRadioButton scientificButton, easyButton, citoButton;
	ButtonGroup groep;
	int rmMode;

	
	public CalculatorDwoInteractieEditPanel ()
	{
		setLayout(null);
		cdip = new CalculatorDwoInteractiePanel();
		cdip.setBounds(0,0,cdipBreedte,cdipHoogte);
		add(cdip);
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		width = editWidth - 2 * offset;
		height = 3 * theFM.getHeight() / 2;
		currentX = cdip.getSize().width + offset;
		currentY = offset;
		
		easyButton = new JRadioButton("Eenvoudig");
		easyButton.setBounds(currentX, currentY, width, height);
		add(easyButton);
		
		currentY += height + offset;
		
		scientificButton = new JRadioButton("Wetenschappelijk");
		scientificButton.setSelected(true);
		scientificButton.setBounds(currentX, currentY, width, height);
		add(scientificButton);
		
		currentY += height + offset;
		
		citoButton = new JRadioButton("Cito-versie");
		citoButton.setBounds(currentX, currentY, width, height);
		add(citoButton);
		
		groep = new ButtonGroup();
		groep.add(easyButton);
		groep.add(scientificButton);
		groep.add(citoButton);
		
		easyButton.addActionListener(this);
		scientificButton.addActionListener(this);
		citoButton.addActionListener(this);

	}

	public void setEditState(Hashtable h) {
		
		if(h.containsKey("rmMode"))
			rmMode = ((Integer)h.get("rmMode")).intValue();
		if(rmMode == 0)
			easyButton.setSelected(true);
		else if(rmMode == 1)
			scientificButton.setSelected(true);
		else
			citoButton.setSelected(true);
		
		cdip.setEditState(h);
	}

	public Hashtable getEditState() {
		Hashtable h = cdip.getEditState();
		return h;
	}

	public void zetBreedte(int b) {
		cdipBreedte = b;
		
		setBounds(getLocation().x, getLocation().y, cdipBreedte + editWidth, Math.max(cdipHoogte, editHeight));		
	}

	public void zetHoogte(int h) {
		cdipHoogte = h;
		
		setBounds(getLocation().x, getLocation().y, cdipBreedte + editWidth, Math.max(cdipHoogte, editHeight));		
	}

	public void wis() {}

	public void zetMode(int mode) {}

	public void stop() {}

	public void start() {}

	public void addActionListener(ActionListener al) {}

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == easyButton)
		{	rmMode = 0;
			cdip.zetRmMode(rmMode);
		}
		else if(e.getSource() == scientificButton)
		{	rmMode = 1;
			cdip.zetRmMode(rmMode);
		}
		else if(e.getSource() == citoButton)
		{	rmMode = 2;
			cdip.zetRmMode(rmMode);			
		}
			
	}

}
