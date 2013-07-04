package fi.calculatordwo;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class CalculatorDwoInteractieEditPanel  extends JPanel implements ActionListener, InteractieEditPanel

{	int editWidth = 200;
	int editHeight = 550; 
	int cdipBreedte = 500; // startbreedte ip
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
	JCheckBox gonioBox, logaritmeBox, gradenInstelbaarBox;
	int rmMode = 1;
	boolean	gradenInstelbaar = true;
	boolean gonioKnoppen = true;
	boolean logaritmeKnoppen = true;

	
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
		
		citoButton = new JRadioButton(CalculatorDwo.rb.getString("citoButton"));
		citoButton.setBounds(currentX, currentY, width, height);
		add(citoButton);
		
		currentY += height + offset;
		
		easyButton = new JRadioButton(CalculatorDwo.rb.getString("eenvoudigButton"));
		easyButton.setBounds(currentX, currentY, width, height);
		add(easyButton);
		
		currentY += height + offset;
		
		scientificButton = new JRadioButton(CalculatorDwo.rb.getString("wetenschappelijkButton"));
		scientificButton.setSelected(true);
		scientificButton.setBounds(currentX, currentY, width, height);
		add(scientificButton);
		
		currentY += height + offset;
		
		groep = new ButtonGroup();
		groep.add(citoButton);
		groep.add(easyButton);
		groep.add(scientificButton);
		
		citoButton.addActionListener(this);
		easyButton.addActionListener(this);
		scientificButton.addActionListener(this);
		
		currentX += 20;
		
		gonioBox = new JCheckBox(CalculatorDwo.rb.getString("gonioBox"));
		gonioBox.setBounds(currentX, currentY, width, height);
		gonioBox.setSelected(true);
		add(gonioBox);
		gonioBox.addActionListener(this);
		
		currentY += height + offset;
		
		logaritmeBox = new JCheckBox(CalculatorDwo.rb.getString("logaritmeBox"));
		logaritmeBox.setBounds(currentX, currentY, width, height);
		logaritmeBox.setSelected(true);
		add(logaritmeBox);
		logaritmeBox.addActionListener(this);
		
		currentY += height + offset;
		
		gradenInstelbaarBox = new JCheckBox(CalculatorDwo.rb.getString("gradenInstelbaarBox"));
		gradenInstelbaarBox.setBounds(currentX, currentY, width, height);
		gradenInstelbaarBox.setSelected(true);
		add(gradenInstelbaarBox);
		gradenInstelbaarBox.addActionListener(this);

	}

	public void setEditState(Hashtable h) {
		
		if(h.containsKey("rmMode"))
			rmMode = ((Integer)h.get("rmMode")).intValue();
		if(h.containsKey("gradenInstelbaar"))
			gradenInstelbaar = ((Boolean)h.get("gradenInstelbaar")).booleanValue();
		gradenInstelbaarBox.setSelected(gradenInstelbaar);
		if(rmMode == 0)
		{	easyButton.setSelected(true);
			gonioBox.setVisible(false);
			logaritmeBox.setVisible(false);
			gradenInstelbaarBox.setVisible(false);
		}
		else if(rmMode == 1)
		{	scientificButton.setSelected(true);
			gonioBox.setVisible(true);
			logaritmeBox.setVisible(true);
			gradenInstelbaarBox.setVisible(true);
		}
		else
		{	citoButton.setSelected(true);
			gonioBox.setVisible(false);
			logaritmeBox.setVisible(false);
			gradenInstelbaarBox.setVisible(false);
		}
		
		if (h.containsKey("cdipBreedte"))
			cdipBreedte = ((Integer) h.get("cdipBreedte")).intValue();
		if (h.containsKey("cdipHoogte"))
			cdipHoogte = ((Integer) h.get("cdipHoogte")).intValue();
		
		cdip.setEditState(h);
	}

	public Hashtable getEditState() {
		Hashtable h = cdip.getEditState();
		
		h.put("cdipBreedte", new Integer(cdipBreedte));
		h.put("cdipHoogte", new Integer(cdipHoogte));
		return h;
	}
	
	public void setBounds(int x, int y, int b, int h)
	{
		if ((h <= 1) || (x < 0) || (b <= 1))
			return;
		super.setBounds(x, y, cdipBreedte + editWidth, Math.max(cdipHoogte, editHeight));
		if (cdip != null)
			cdip.setBounds(0, 0, cdipBreedte, cdipHoogte);
		
		//plaatsComponenten();
		
		
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
			cdip.zetRmMode(rmMode, gradenInstelbaar);
			gonioBox.setVisible(false);
			logaritmeBox.setVisible(false);
			gradenInstelbaarBox.setVisible(false);
		}
		else if(e.getSource() == scientificButton)
		{	rmMode = 1;
			cdip.zetRmMode(rmMode, gradenInstelbaar);
			gonioBox.setVisible(true);
			logaritmeBox.setVisible(true);
			gradenInstelbaarBox.setVisible(true);
		}
		else if(e.getSource() == citoButton)
		{	rmMode = 2;
			cdip.zetRmMode(rmMode, gradenInstelbaar);
			gonioBox.setVisible(false);
			logaritmeBox.setVisible(false);
			gradenInstelbaarBox.setVisible(false);
		}
		else if(e.getSource() == gonioBox)
		{
			gonioKnoppen = gonioBox.isSelected();
			cdip.gonioKnoppen = gonioKnoppen;
			if(!gonioKnoppen)
			{	gradenInstelbaar = false; 
				gradenInstelbaarBox.setSelected(false);
				gradenInstelbaarBox.setEnabled(false);
			}
			else
			{
				gradenInstelbaarBox.setSelected(gradenInstelbaar);
				gradenInstelbaarBox.setEnabled(true);
			}
			cdip.zetRmMode(rmMode, gradenInstelbaar);
			
		}
		else if(e.getSource() == logaritmeBox)
		{
			logaritmeKnoppen = logaritmeBox.isSelected();
			cdip.logaritmeKnoppen = logaritmeKnoppen;
			cdip.zetRmMode(rmMode, gradenInstelbaar);
		}
		else if(e.getSource() == gradenInstelbaarBox)
		{
			gradenInstelbaar = gradenInstelbaarBox.isSelected();
			cdip.zetRmMode(rmMode, gradenInstelbaar);
		}
			
	}

}
