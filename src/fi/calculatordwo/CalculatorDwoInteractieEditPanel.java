package fi.calculatordwo;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class CalculatorDwoInteractieEditPanel  extends JPanel implements ActionListener, InteractieEditPanel

{	int editWidth = 190;
	int editHeight = 550; 
	int cdipBreedte = 500; // startbreedte ip
	int cdipHoogte = 250; // starthoogte ip
	
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
	JRadioButton scientificButton, easyButton;
	ButtonGroup groep;
	boolean wetenschappelijk;

	
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
		
		groep = new ButtonGroup();
		groep.add(easyButton);
		groep.add(scientificButton);
		
		easyButton.addActionListener(this);
		scientificButton.addActionListener(this);

	}

	public void setEditState(Hashtable h) {
		if(h.containsKey("wetenschappelijk"))
			wetenschappelijk = ((Boolean) h.get("wetenschappelijk")).booleanValue();
		if(wetenschappelijk)
			scientificButton.setSelected(true);
		else
			easyButton.setSelected(true);
			
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
		{	wetenschappelijk = false;
			cdip.zetWetenschappelijk(wetenschappelijk);
		}
		else if(e.getSource() == scientificButton)
		{	wetenschappelijk = true;
			cdip.zetWetenschappelijk(wetenschappelijk);
		}
			
	}

}
