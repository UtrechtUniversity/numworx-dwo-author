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
	int cdipHoogte = 450; // starthoogte ip
	
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
		easyButton.setSelected(true);
		easyButton.setBounds(currentX, currentY, width, height);
		add(easyButton);
		
		currentY += height + offset;
		
		scientificButton = new JRadioButton("Wetenschappelijk");
		scientificButton.setBounds(currentX, currentY, width, height);
		add(scientificButton);
		
		groep = new ButtonGroup();
		groep.add(easyButton);
		groep.add(scientificButton);
		
		easyButton.addActionListener(this);
		scientificButton.addActionListener(this);

	}

	public void setEditState(Hashtable b) {
		// TODO Auto-generated method stub
		
	}

	public Hashtable getEditState() {
		// TODO Auto-generated method stub
		return null;
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

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
