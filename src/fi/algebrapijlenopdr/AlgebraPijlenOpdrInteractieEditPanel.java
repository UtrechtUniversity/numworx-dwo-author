package fi.algebrapijlenopdr;

import java.awt.event.*;
import java.awt.*;
import java.util.Hashtable;

import javax.swing.*;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class AlgebraPijlenOpdrInteractieEditPanel extends JPanel
												  implements InteractieEditPanel, ActionListener 
{
	AlgebraPijlenOpdrInteractiePanel apoip;
	int editWidth = 180;
	int editHeight = 500; 
	int apoipBreedte = 500; // startbreedte apoip
	int apoipHoogte = 450; // starthoogte apoip
	
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	int offset = 10;
	boolean componentsCreated = false;
	
	boolean noSetBounds = false;	
	
	ButtonGroup kettingGroup;
	JRadioButton toolkitButton, invulButton, demoButton;
	
	JCheckBox brugklasBox, terugHeenBox, tabelBox, grafiekBox, scrollBox, zoomBox;
	
	public AlgebraPijlenOpdrInteractieEditPanel()
	{
		setLayout(null);
		apoip = new AlgebraPijlenOpdrInteractiePanel();
		add(apoip);
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		// viewerOptiesPanel
		
		int width = 0;
		int height = 3 * theFM.getHeight() / 2;
		int currentX = apoip.getSize().width + offset;
		int currentX2 = offset;
		int currentY = offset;
		
		kettingGroup = new ButtonGroup();

		toolkitButton = new JRadioButton(AlgebraPijlenOpdr.rb.getString("toolkitTekst"), true);
		kettingGroup.add(toolkitButton);
		toolkitButton.setFont(theFont);
		toolkitButton.setBackground(Color.white);
		width = theFM.stringWidth(toolkitButton.getText()) + 40;
		toolkitButton.setBounds(currentX, currentY, width, height);
		add(toolkitButton);
		//viewerOptiesPanel.add(toolkitButton);
		toolkitButton.addActionListener(this);
		
		currentY += height; // + offset / 5;		
		
		invulButton = new JRadioButton(AlgebraPijlenOpdr.rb.getString("invulTekst"));
		kettingGroup.add(invulButton);
		invulButton.setFont(theFont);
		invulButton.setBackground(Color.white);
		width = theFM.stringWidth(invulButton.getText()) + 35;
		invulButton.setBounds(currentX, currentY, width, height);
		add(invulButton);
		//viewerOptiesPanel.add(invulButton);
		invulButton.addActionListener(this);
		
		currentY += height; // + offset / 5;		
		
		demoButton = new JRadioButton(AlgebraPijlenOpdr.rb.getString("demoTekst"));
		kettingGroup.add(demoButton);
		demoButton.setFont(theFont);
		demoButton.setBackground(Color.white);
		width = theFM.stringWidth(demoButton.getText()) + 40;
		demoButton.setBounds(currentX, currentY, width, height);
		add(demoButton);
		//viewerOptiesPanel.add(demoButton);
		demoButton.addActionListener(this);
		
		currentY += height + offset;		
		
		
		brugklasBox = new JCheckBox(AlgebraPijlenOpdr.rb.getString("brugklasTekst"));
		brugklasBox.setFont(theFont);
		brugklasBox.setBackground(Color.white);
		width = theFM.stringWidth(brugklasBox.getText()) + 40;
		brugklasBox.setBounds(currentX, currentY, width, height);
		add(brugklasBox);
		//viewerOptiesPanel.add(brugklasBox);
		brugklasBox.addActionListener(this);

		currentY += height + offset / 2;
		
		terugHeenBox = new JCheckBox(AlgebraPijlenOpdr.rb.getString("terugHeenTekst"));
		terugHeenBox.setFont(theFont);
		terugHeenBox.setBackground(Color.white);
		width = theFM.stringWidth(terugHeenBox.getText()) + 35;
		terugHeenBox.setBounds(currentX, currentY, width, height);
		terugHeenBox.setSelected(true);
		add(terugHeenBox);
		//viewerOptiesPanel.add(terugHeenBox);
		terugHeenBox.addActionListener(this);

		currentY += height + offset / 2;

		tabelBox = new JCheckBox(AlgebraPijlenOpdr.rb.getString("tabelTekst"));
		tabelBox.setFont(theFont);
		tabelBox.setBackground(Color.white);
		width = theFM.stringWidth(tabelBox.getText()) + 40;
		tabelBox.setBounds(currentX, currentY, width, height);
		tabelBox.setSelected(true);
		add(tabelBox);
		//viewerOptiesPanel.add(tabelBox);
		tabelBox.addActionListener(this);

		currentY += height + offset / 2;

		grafiekBox = new JCheckBox(AlgebraPijlenOpdr.rb.getString("grafiekTekst"));
		grafiekBox.setFont(theFont);
		grafiekBox.setBackground(Color.white);
		width = theFM.stringWidth(grafiekBox.getText()) + 40;
		grafiekBox.setBounds(currentX, currentY, width, height);
		grafiekBox.setSelected(true);
		add(grafiekBox);
		//viewerOptiesPanel.add(grafiekBox);
		grafiekBox.addActionListener(this);

		currentY += height + offset / 2;

		scrollBox = new JCheckBox(AlgebraPijlenOpdr.rb.getString("scrollTekst"));
		scrollBox.setFont(theFont);
		scrollBox.setBackground(Color.white);
		width = theFM.stringWidth(scrollBox.getText()) + 35;
		scrollBox.setBounds(currentX, currentY, width, height);
		scrollBox.setSelected(true);
		add(scrollBox);
		//viewerOptiesPanel.add(scrollBox);
		scrollBox.addActionListener(this);

		currentY += height + offset / 2;

		zoomBox = new JCheckBox(AlgebraPijlenOpdr.rb.getString("zoomTekst"));
		zoomBox.setFont(theFont);
		zoomBox.setBackground(Color.white);
		width = theFM.stringWidth(zoomBox.getText()) + 35;
		zoomBox.setBounds(currentX, currentY, width, height);
		zoomBox.setSelected(true);
		add(zoomBox);
		//viewerOptiesPanel.add(zoomBox);
		zoomBox.addActionListener(this);

		currentY += height + offset / 2;
		
		componentsCreated = true;		
	}

	public void plaatsComponenten()
	{
		if (componentsCreated)
		{	
			toolkitButton.setLocation(apoip.getSize().width + offset, toolkitButton.getLocation().y);
			invulButton.setLocation(apoip.getSize().width + offset, invulButton.getLocation().y);
			demoButton.setLocation(apoip.getSize().width + offset, demoButton.getLocation().y);
			
			brugklasBox.setLocation(apoip.getSize().width + offset, brugklasBox.getLocation().y);
			terugHeenBox.setLocation(apoip.getSize().width + offset, terugHeenBox.getLocation().y);
			tabelBox.setLocation(apoip.getSize().width + offset, tabelBox.getLocation().y);
			grafiekBox.setLocation(apoip.getSize().width + offset, grafiekBox.getLocation().y);
			scrollBox.setLocation(apoip.getSize().width + offset, scrollBox.getLocation().y);
			zoomBox.setLocation(apoip.getSize().width + offset, zoomBox.getLocation().y);
			
//			tabbedPane.setBounds(naip.getSize().width, 0, editWidth, getSize().height);
			
			repaint();
						
		}
	}
	
	public void setEditState(Hashtable b)
	{
		boolean toolkit = true;
		if (b.containsKey("toolkit"))
			toolkit = ((Boolean) b.get("toolkit")).booleanValue();
		toolkitButton.setSelected(toolkit);
		
		boolean alleenInvullen = false;
		if (b.containsKey("alleenInvullen"))
			alleenInvullen = ((Boolean) b.get("alleenInvullen")).booleanValue();
		invulButton.setSelected(alleenInvullen);
		
		if (invulButton.isSelected())
		{	brugklasBox.setEnabled(false);
			terugHeenBox.setEnabled(false);
			tabelBox.setEnabled(false);
			grafiekBox.setEnabled(false);
//			scrollBox.setEnabled(true);
//			zoomBox.setEnabled(true);
					
		}		
		
		boolean isDemo = false;
		if (b.containsKey("isDemo"))
			isDemo = ((Boolean) b.get("isDemo")).booleanValue();
		demoButton.setSelected(isDemo);
			
		if (demoButton.isSelected())
		{	brugklasBox.setEnabled(false);
			terugHeenBox.setEnabled(false);
			tabelBox.setEnabled(false);
			grafiekBox.setEnabled(false);
			scrollBox.setEnabled(false);
			zoomBox.setEnabled(false);
		}		
		
		boolean brugklas = false;
		if (b.containsKey("brugklas"))
			brugklas = ((Boolean) b.get("brugklas")).booleanValue();
		brugklasBox.setSelected(brugklas);
		
		boolean terugHeen = true;
		if (b.containsKey("terugHeen"))
			terugHeen = ((Boolean) b.get("terugHeen")).booleanValue();
		terugHeenBox.setSelected(terugHeen);
		
		boolean tabelOptie = true;
		if (b.containsKey("tabelOptie"))
			tabelOptie = ((Boolean) b.get("tabelOptie")).booleanValue();
		tabelBox.setSelected(tabelOptie);
		
		boolean grafiekOptie = true;
		if (b.containsKey("grafiekOptie"))
			grafiekOptie = ((Boolean) b.get("grafiekOptie")).booleanValue();
		grafiekBox.setSelected(grafiekOptie);
		
		boolean scrollOptie = true;
		if (b.containsKey("scrollOptie"))
			scrollOptie = ((Boolean) b.get("scrollOptie")).booleanValue();
		scrollBox.setSelected(scrollOptie);
		
		boolean zoomOptie = true;
		if (b.containsKey("zoomOptie"))
			zoomOptie = ((Boolean) b.get("zoomOptie")).booleanValue();
		zoomBox.setSelected(zoomOptie);
		
		apoip.setEditState(b);
		
		if (b.containsKey("apoipBreedte"))
			apoipBreedte = ((Integer) b.get("apoipBreedte")).intValue();
		if (b.containsKey("apiepHoogte"))
			apoipHoogte = ((Integer) b.get("bpipHoogte")).intValue();
		
		setBounds(getLocation().x, getLocation().y, apoipBreedte + editWidth, Math.max(apoipHoogte, editHeight));
		
	}
	
	
	public Hashtable getEditState()
	{		
		Hashtable h = apoip.getEditState(); 
	
		h.put("apoipBreedte", new Integer(apoipBreedte));
		h.put("apoipHoogte", new Integer(apoipHoogte));
		
		return h;
	}
	
	public void setBounds(int x, int y, int b, int h)
	{
		
		if (noSetBounds)
		{
			noSetBounds = false;
			return;
		}

System.out.println("apoiep setBounds raw " + x + " " + y + " " + b + " " + h);

		if ((h <= 1) || (x < 0) || (b <= 1))
			return;
		
		super.setBounds(x, y, apoipBreedte + editWidth, Math.max(apoipHoogte, editHeight));
		
		System.out.println("apoiep setBounds " + x + " " + y + " " + (apoipBreedte + editWidth) + " " + 
							Math.max(apoipHoogte, editHeight));

		
		if (apoip != null)
			apoip.setBounds(0, 0, apoipBreedte, apoipHoogte);
		
		plaatsComponenten();
		
//System.out.println("setBounds " + x + " " + y + " " + b + " " + h);		
		
	}
	
	public void zetBreedte(int b)
	{	
		apoipBreedte = b;
		
		setBounds(getLocation().x, getLocation().y, apoipBreedte + editWidth, Math.max(apoipHoogte, editHeight));		
		//apoip.setBounds(apoip.getLocation().x, apoip.getLocation().y, Math.max(0, b), apoip.getSize().height);
		plaatsComponenten();
	}
	
	public void zetHoogte(int h)
	{	
		apoipHoogte = h;
		
		setBounds(getLocation().x, getLocation().y, apoipBreedte + editWidth, Math.max(apoipHoogte, editHeight));		
		//apoip.setBounds(apoip.getLocation().x, apoip.getLocation().y, apoip.getSize().width, h);
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
	{	if (e.getSource() == toolkitButton)
		{	
			if (toolkitButton.isSelected())
			{	
				apoip.zetToolkit(true);
				
				brugklasBox.setEnabled(true);
				terugHeenBox.setEnabled(true);
				tabelBox.setEnabled(true);
				grafiekBox.setEnabled(true);
				scrollBox.setEnabled(true);
				zoomBox.setEnabled(true);
			
			}
		}
		else if (e.getSource() == invulButton)
		{	if (invulButton.isSelected())
			{	
				apoip.zetAlleenInvullen(true);
			
				brugklasBox.setEnabled(false);
				terugHeenBox.setEnabled(false);
				tabelBox.setEnabled(false);
				grafiekBox.setEnabled(false);
				scrollBox.setEnabled(true);
				zoomBox.setEnabled(true);
						
			}
		}
		else if (e.getSource() == demoButton)
		{	if (demoButton.isSelected())
			{	
				apoip.zetIsDemo(true);
			
				brugklasBox.setEnabled(false);
				terugHeenBox.setEnabled(false);
				tabelBox.setEnabled(false);
				grafiekBox.setEnabled(false);
				scrollBox.setEnabled(false);
				zoomBox.setEnabled(false);
			}
		}
		
		else if (e.getSource() == brugklasBox)
		{
			apoip.zetBrugklas(brugklasBox.isSelected());
		}
		else if (e.getSource() == terugHeenBox)
		{
			apoip.zetTerugHeen(terugHeenBox.isSelected());
		}
		else if (e.getSource() == tabelBox)
		{
			apoip.zetTabelOptie(tabelBox.isSelected());
		}
		else if (e.getSource() == grafiekBox)
		{
			apoip.zetGrafiekOptie(grafiekBox.isSelected());
		}
		else if (e.getSource() == scrollBox)
		{
			apoip.zetScrollOptie(scrollBox.isSelected());
		}
		else if (e.getSource() == zoomBox)
		{
			apoip.zetZoomOptie(zoomBox.isSelected());
		}
		
	}
}
