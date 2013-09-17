package fi.draaibank;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;

public class DBInteractieEditPanel extends JPanel implements InteractieEditPanel,
																	ActionListener	
{	
	int editWidth = 190;
	int editHeight = 500; 
	int dbipBreedte = 500; // startbreedte dbip
	int dbipHoogte = 450; // starthoogte dbip
	
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	int offset = 10;
	boolean componentsCreated = false;
	
	boolean noSetBounds = false;	

	protected DBInteractiePanel dbip;

	int scoreMax = 10;
	
	JCheckBox demoVersionBox, zoomOptionBox;
	
	
	public DBInteractieEditPanel()
	{
		setLayout(null);
		dbip = new DBInteractiePanel();
		add(dbip);
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		
		int width = editWidth - 3 * offset;
		int height = 3 * theFM.getHeight() / 2;
		int currentX = dbip.getSize().width + offset;
		int currentX2 = offset;
		int currentY = offset;
		

		demoVersionBox = new JCheckBox(Draaibank.rb.getString("demoVersionTekst"), false);
		demoVersionBox.setFont(theFont);
		demoVersionBox.setBackground(Color.white);
		demoVersionBox.setBounds(currentX, currentY, width, height);
		add(demoVersionBox);
		demoVersionBox.addActionListener(this);
		
		currentY += height + 2 * offset;

		zoomOptionBox = new JCheckBox(Draaibank.rb.getString("zoomOptionTekst"), false);
		zoomOptionBox.setFont(theFont);
		zoomOptionBox.setBackground(Color.white);
		zoomOptionBox.setBounds(currentX, currentY, width, height);
		add(zoomOptionBox);
		zoomOptionBox.addActionListener(this);
		
		currentY += height + 2 * offset;
		
		componentsCreated = true;
	}	
	
	public void plaatsComponenten()
	{
		if (componentsCreated)
		{	
			
			demoVersionBox.setLocation(dbip.getSize().width + 2 * offset, demoVersionBox.getLocation().y);
			zoomOptionBox.setLocation(dbip.getSize().width + 2 * offset, zoomOptionBox.getLocation().y);
		}	
		
	}
	
	public void setEditState(Hashtable b)
	{
		
System.out.println("dbiep setEditState");

		boolean demoVersion = false;
		boolean zoomOption = false;

		if (b.containsKey("demoVersion"))
			demoVersion = ((Boolean) b.get("demoVersion")).booleanValue();
		if (b.containsKey("zoomOption"))
			zoomOption = ((Boolean) b.get("zoomOption")).booleanValue();
			
		demoVersionBox.setSelected(demoVersion);
		zoomOptionBox.setSelected(zoomOption);
		

		if (b.containsKey("dbipBreedte"))
			dbipBreedte = ((Integer) b.get("dbipBreedte")).intValue();
		if (b.containsKey("dbipHoogte"))
			dbipHoogte = ((Integer) b.get("dbipHoogte")).intValue();
		
		setBounds(getLocation().x, getLocation().y, dbipBreedte + editWidth, Math.max(dbipHoogte, editHeight));
		
		// HIER !!
		dbip.setEditState(b);		
		
	}
	
	public Hashtable getEditState()
	{
System.out.println("dbiep getEditState");

		Hashtable h = dbip.getEditState(); 
		
		h.put("scoreMax", new Integer(scoreMax));
		
		h.put("dbipBreedte", new Integer(dbipBreedte));
		h.put("dbipHoogte", new Integer(dbipHoogte));
		
		return h;
		
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		if (noSetBounds)
		{	noSetBounds = false;
			return;
		}
//System.out.println("tiep setBounds raw " + x + " " + y + " " + b + " " + h);

		if ((h <= 1) || (x < 0) || (b <= 1))
			return;
		
		super.setBounds(x, y, dbipBreedte + editWidth, Math.max(dbipHoogte, editHeight));
		
//System.out.println("tiep setBounds " + x + " " + y + " " + (tipBreedte + editWidth) + " " + 
//					Math.max(tipHoogte, editHeight));
	
		if (dbip != null)
			dbip.setBounds(0, 0, dbipBreedte, dbipHoogte);
		
		plaatsComponenten();
		
//System.out.println("setBounds " + x + " " + y + " " + b + " " + h);		
		
	}
	
	public void zetBreedte(int b)
	{	
		dbipBreedte = b;
		
		setBounds(getLocation().x, getLocation().y, dbipBreedte + editWidth, Math.max(dbipHoogte, editHeight));		
		plaatsComponenten();
	}
	
	public void zetHoogte(int h)
	{	
		dbipHoogte = h;
		
		setBounds(getLocation().x, getLocation().y, dbipBreedte + editWidth, Math.max(dbipHoogte, editHeight));		
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
		if (e.getSource() == demoVersionBox)
		{
			dbip.zetDemoVersion(demoVersionBox.isSelected());
		}
		else if (e.getSource() == zoomOptionBox)
		{
			dbip.zetZoomOption(zoomOptionBox.isSelected());
		}

	}

}
