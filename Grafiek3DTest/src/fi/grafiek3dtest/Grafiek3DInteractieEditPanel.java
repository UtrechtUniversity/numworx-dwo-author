package fi.grafiek3dtest;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;

public class Grafiek3DInteractieEditPanel extends JPanel implements InteractieEditPanel,
																	ActionListener	
{	
	int editWidth = 190;
	int editHeight = 500; 
	int gipBreedte = 500; // startbreedte gip
	int gipHoogte = 450; // starthoogte gip
	
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	int offset = 10;
	boolean componentsCreated = false;
	
	boolean noSetBounds = false;	

	protected Grafiek3DInteractiePanel gip;

	int scoreMax = 10;
	
	JCheckBox zoomOptionBox, translateOptionBox, wireFrameOptionBox, refineOptionBox, 
			  axesChoiceOptionBox, labelChoiceOptionBox, projectionChoiceOptionBox, colorChoiceOptionBox, 
			  figureChoiceOptionBox, examplesOptionBox, figureIsDemoBox;
	
	public Grafiek3DInteractieEditPanel()
	{
		setLayout(null);
		gip = new Grafiek3DInteractiePanel();
		add(gip);
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		
		int width = editWidth - 2 * offset;
		int height = 3 * theFM.getHeight() / 2;
		int currentX = gip.getSize().width + offset;
		int currentX2 = offset;
		int currentY = offset;
		
		zoomOptionBox = new JCheckBox(Grafiek3DTest.rb.getString("zoomOptieTekst"), true);
		zoomOptionBox.setFont(theFont);
		zoomOptionBox.setBackground(Color.white);
		zoomOptionBox.setBounds(currentX, currentY, width, height);
		add(zoomOptionBox);
		zoomOptionBox.addActionListener(this);
		
		currentY += height + offset;
		
		translateOptionBox = new JCheckBox(Grafiek3DTest.rb.getString("transleerOptieTekst"), true);
		translateOptionBox.setFont(theFont);
		translateOptionBox.setBackground(Color.white);
		translateOptionBox.setBounds(currentX, currentY, width, height);
		add(translateOptionBox);
		translateOptionBox.addActionListener(this);
		
		currentY += height + offset;

		wireFrameOptionBox = new JCheckBox(Grafiek3DTest.rb.getString("wireFrameOptieTekst"), true);
		wireFrameOptionBox.setFont(theFont);
		wireFrameOptionBox.setBackground(Color.white);
		wireFrameOptionBox.setBounds(currentX, currentY, width, height);
		add(wireFrameOptionBox);
		wireFrameOptionBox.addActionListener(this);
		
		currentY += height + offset;

		refineOptionBox = new JCheckBox(Grafiek3DTest.rb.getString("verfijnOptieTekst"), true);
		refineOptionBox.setFont(theFont);
		refineOptionBox.setBackground(Color.white);
		refineOptionBox.setBounds(currentX, currentY, width, height);
		add(refineOptionBox);
		refineOptionBox.addActionListener(this);
		
		currentY += height + offset;

		axesChoiceOptionBox = new JCheckBox(Grafiek3DTest.rb.getString("assenKeuzeOptieTekst"), true);
		axesChoiceOptionBox.setFont(theFont);
		axesChoiceOptionBox.setBackground(Color.white);
		axesChoiceOptionBox.setBounds(currentX, currentY, width, height);
		add(axesChoiceOptionBox);
		axesChoiceOptionBox.addActionListener(this);
		currentY += height + offset;

		labelChoiceOptionBox = new JCheckBox(Grafiek3DTest.rb.getString("labelKeuzeOptieTekst"), true);
		labelChoiceOptionBox.setFont(theFont);
		labelChoiceOptionBox.setBackground(Color.white);
		labelChoiceOptionBox.setBounds(currentX, currentY, width, height);
		add(labelChoiceOptionBox);
		labelChoiceOptionBox.addActionListener(this);
		
		currentY += height + offset;

		projectionChoiceOptionBox = new JCheckBox(Grafiek3DTest.rb.getString("projectieKeuzeOptieTekst"), true);
		projectionChoiceOptionBox.setFont(theFont);
		projectionChoiceOptionBox.setBackground(Color.white);
		projectionChoiceOptionBox.setBounds(currentX, currentY, width, height);
		add(projectionChoiceOptionBox);
		projectionChoiceOptionBox.addActionListener(this);
		
		currentY += height + offset;

		colorChoiceOptionBox = new JCheckBox(Grafiek3DTest.rb.getString("kleurKeuzeOptieTekst"), true);
		colorChoiceOptionBox.setFont(theFont);
		colorChoiceOptionBox.setBackground(Color.white);
		colorChoiceOptionBox.setBounds(currentX, currentY, width, height);
		add(colorChoiceOptionBox);
		colorChoiceOptionBox.addActionListener(this);
		
		currentY += height + 2 * offset;
		
		figureChoiceOptionBox = new JCheckBox(Grafiek3DTest.rb.getString("figuurKeuzeOptieTekst"), true);
		figureChoiceOptionBox.setFont(theFont);
		figureChoiceOptionBox.setBackground(Color.white);
		figureChoiceOptionBox.setBounds(currentX, currentY, width, height);
		add(figureChoiceOptionBox);
		figureChoiceOptionBox.addActionListener(this);
		
		currentY += height + offset;

		examplesOptionBox = new JCheckBox(Grafiek3DTest.rb.getString("voorbeeldenOptieTekst"), true);
		examplesOptionBox.setFont(theFont);
		examplesOptionBox.setBackground(Color.white);
		examplesOptionBox.setBounds(currentX, currentY, width, height);
		add(examplesOptionBox);
		examplesOptionBox.addActionListener(this);
		
		currentY += height + offset;
		
		figureIsDemoBox = new JCheckBox(Grafiek3DTest.rb.getString("figuurIsDemoTekst"));
		figureIsDemoBox.setFont(theFont);
		figureIsDemoBox.setBackground(Color.white);
		figureIsDemoBox.setBounds(currentX, currentY, width, height);
		add(figureIsDemoBox);
		figureIsDemoBox.addActionListener(this);
		
		currentY += height + 2 * offset;
		
	}	
	
	public void plaatsComponenten()
	{
		zoomOptionBox.setLocation(gip.getSize().width + offset, zoomOptionBox.getLocation().y);
		translateOptionBox.setLocation(gip.getSize().width + offset, translateOptionBox.getLocation().y);
		wireFrameOptionBox.setLocation(gip.getSize().width + offset, wireFrameOptionBox.getLocation().y);
		refineOptionBox.setLocation(gip.getSize().width + offset, refineOptionBox.getLocation().y);
		axesChoiceOptionBox.setLocation(gip.getSize().width + offset, axesChoiceOptionBox.getLocation().y);
		labelChoiceOptionBox.setLocation(gip.getSize().width + offset, labelChoiceOptionBox.getLocation().y);
		projectionChoiceOptionBox.setLocation(gip.getSize().width + offset, projectionChoiceOptionBox.getLocation().y);
		colorChoiceOptionBox.setLocation(gip.getSize().width + offset, colorChoiceOptionBox.getLocation().y);		
		
		figureChoiceOptionBox.setLocation(gip.getSize().width + offset, figureChoiceOptionBox.getLocation().y);
		examplesOptionBox.setLocation(gip.getSize().width + offset, examplesOptionBox.getLocation().y);
		figureIsDemoBox.setLocation(gip.getSize().width + offset, figureIsDemoBox.getLocation().y);
	}
	
	public void setEditState(Hashtable b)
	{
		boolean zoomOptie = true;
		if (b.containsKey("zoomOptie"))
			zoomOptie = ((Boolean) b.get("zoomOptie")).booleanValue();
		zoomOptionBox.setSelected(zoomOptie);
		
		boolean translateOptie = true;
		if (b.containsKey("translateOptie"))
			translateOptie = ((Boolean) b.get("translateOptie")).booleanValue();
		translateOptionBox.setSelected(translateOptie);
			
		boolean solidDraadKeuzeOptie = true;
		if (b.containsKey("solidDraadKeuzeOptie"))
			solidDraadKeuzeOptie = ((Boolean) b.get("solidDraadKeuzeOptie")).booleanValue();
		wireFrameOptionBox.setSelected(solidDraadKeuzeOptie);
		
		boolean finerKeuzeOptie = true;
		if (b.containsKey("finerKeuzeOptie"))
			finerKeuzeOptie = ((Boolean) b.get("finerKeuzeOptie")).booleanValue();
		refineOptionBox.setSelected(finerKeuzeOptie);
		
		boolean asKeuzeOptie = true;
		if (b.containsKey("asKeuzeOptie"))
			asKeuzeOptie = ((Boolean) b.get("asKeuzeOptie")).booleanValue();
		axesChoiceOptionBox.setSelected(asKeuzeOptie);

		boolean labelKeuzeOptie = true;
		if (b.containsKey("labelKeuzeOptie"))
			labelKeuzeOptie = ((Boolean) b.get("labelKeuzeOptie")).booleanValue();
		labelChoiceOptionBox.setSelected(labelKeuzeOptie);
		
		boolean projectieKeuzeOptie = true;
		if (b.containsKey("projectieKeuzeOptie"))
			projectieKeuzeOptie = ((Boolean) b.get("projectieKeuzeOptie")).booleanValue();
		projectionChoiceOptionBox.setSelected(projectieKeuzeOptie);
		
		boolean kleurKeuzeOptie = true;
		if (b.containsKey("kleurKeuzeOptie"))
			kleurKeuzeOptie = ((Boolean) b.get("kleurKeuzeOptie")).booleanValue();
		colorChoiceOptionBox.setSelected(kleurKeuzeOptie);
		
		boolean functieTypeKeuze = true;
		if (b.containsKey("functieTypeKeuze"))
			functieTypeKeuze = ((Boolean) b.get("functieTypeKeuze")).booleanValue();
		figureChoiceOptionBox.setSelected(functieTypeKeuze);
		
		boolean voorbeeldenEnabled = true;
		if (b.containsKey("voorbeeldenEnabled"))
			voorbeeldenEnabled = ((Boolean) b.get("voorbeeldenEnabled")).booleanValue();
		examplesOptionBox.setSelected(voorbeeldenEnabled);
		
		boolean figuurIsDemo = false;
		if (b.containsKey("figuurIsDemo"))
			figuurIsDemo = ((Boolean) b.get("figuurIsDemo")).booleanValue();
		figureIsDemoBox.setSelected(figuurIsDemo);
		
		if (b.containsKey("gipBreedte"))
			gipBreedte = ((Integer) b.get("gipBreedte")).intValue();
		if (b.containsKey("gipHoogte"))
			gipHoogte = ((Integer) b.get("gipHoogte")).intValue();
		
		setBounds(getLocation().x, getLocation().y, gipBreedte + editWidth, Math.max(gipHoogte, editHeight));
		
		// HIER !!
		gip.setEditState(b);		
		
	}
	
	public Hashtable getEditState()
	{
		Hashtable h = gip.getEditState(); 
		
		h.put("scoreMax", new Integer(scoreMax));
		
		h.put("gipBreedte", new Integer(gipBreedte));
		h.put("gipHoogte", new Integer(gipHoogte));
		
		return h;
		
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		if (noSetBounds)
		{	noSetBounds = false;
			return;
		}
//System.out.println("giep setBounds raw " + x + " " + y + " " + b + " " + h);

		if ((h <= 1) || (x < 0) || (b <= 1))
			return;
		
		super.setBounds(x, y, gipBreedte + editWidth, Math.max(gipHoogte, editHeight));
		
//System.out.println("giep setBounds " + x + " " + y + " " + (gipBreedte + editWidth) + " " + 
//					Math.max(gipHoogte, editHeight));
	
		if (gip != null)
			gip.setBounds(0, 0, gipBreedte, gipHoogte);
		
		plaatsComponenten();
		
//System.out.println("setBounds " + x + " " + y + " " + b + " " + h);		
		
	}
	
	public void zetBreedte(int b)
	{	
		gipBreedte = b;
		
		setBounds(getLocation().x, getLocation().y, gipBreedte + editWidth, Math.max(gipHoogte, editHeight));		
		plaatsComponenten();
	}
	
	public void zetHoogte(int h)
	{	
		gipHoogte = h;
		
		setBounds(getLocation().x, getLocation().y, gipBreedte + editWidth, Math.max(gipHoogte, editHeight));		
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
	{
		if (e.getSource() == zoomOptionBox)
		{
			gip.zetZoomOptie(zoomOptionBox.isSelected());
		}
		else if (e.getSource() == translateOptionBox)
		{
			gip.zetTranslateOptie(translateOptionBox.isSelected());
		}
		else if (e.getSource() == wireFrameOptionBox)
		{
			gip.zetSolidDraadKeuzeOptie(wireFrameOptionBox.isSelected());
		}
		else if (e.getSource() == refineOptionBox)
		{
			gip.zetFinerKeuzeOptie(refineOptionBox.isSelected());
		}
		else if (e.getSource() == axesChoiceOptionBox)
		{
			gip.zetAsKeuzeOptie(axesChoiceOptionBox.isSelected());
		}
		else if (e.getSource() == labelChoiceOptionBox)
		{
			gip.zetLabelKeuzeOptie(labelChoiceOptionBox.isSelected());
		}
		else if (e.getSource() == projectionChoiceOptionBox)
		{
			gip.zetProjectieKeuzeOptie(projectionChoiceOptionBox.isSelected());
		}
		else if (e.getSource() == colorChoiceOptionBox)
		{
			gip.zetKleurKeuzeOptie(colorChoiceOptionBox.isSelected());
		}
		else if (e.getSource() == figureChoiceOptionBox)
		{
			gip.zetFunctieTypeKeuze(figureChoiceOptionBox.isSelected());
		}
		else if (e.getSource() == examplesOptionBox)
		{
			gip.zetVoorbeeldenEnabled(examplesOptionBox.isSelected());
		}
		else if (e.getSource() == figureIsDemoBox)
		{
			gip.zetFiguurIsDemo(figureIsDemoBox.isSelected());
		}
		
		

	}

}
