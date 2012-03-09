package fi.grafiek3dtest;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;

public class Grafiek3DInteractieEditPanel extends JPanel implements InteractieEditPanel
{	
	int editWidth = 180;
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
	
	
	public Grafiek3DInteractieEditPanel()
	{
		setLayout(null);
		gip = new Grafiek3DInteractiePanel();
		add(gip);
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		// viewerOptiesPanel
		
		int width = 0;
		int height = 3 * theFM.getHeight() / 2;
		int currentX = gip.getSize().width + offset;
		int currentX2 = offset;
		int currentY = offset;
	}	
	
	public void plaatsComponenten()
	{
		
	}
	
	public void setEditState(Hashtable b)
	{
		
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
		
System.out.println("giep setBounds " + x + " " + y + " " + (gipBreedte + editWidth) + " " + 
					Math.max(gipHoogte, editHeight));
	
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
	{}

}
