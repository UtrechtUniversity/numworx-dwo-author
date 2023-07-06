package fi.draaibank;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;

import fi.beans.base64code.StringCodeObject;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;
// deze moet vanwege interface WiskOpdrApplet
import fi.beans.wiskopdrbeans.InteractiePanel;
// deze moet vanwege interface InteractiePanel
import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class DBInteractiePanel extends JPanel implements InteractiePanel, InteractieEditPanel,
							                             ActionListener
									
{
	TekenPanel tekenPanel;
	
	int score = 0;
	int scoreMax = 0;
	
	boolean noSetBounds = false;
	
	boolean demoVersion = false;
	boolean zoomOption = false;
	
	static ImageIcon vergrootIcon, verkleinIcon;
	
	public DBInteractiePanel()
	{
		setLayout(null);
		// echte initiatie vind pas plaats na setBounds
		
		java.net.URL imageURL = Draaibank.class.getResource("resources/zoominknop.gif");
		if (imageURL != null) 
		{
			vergrootIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading zoominknop.gif");
		}
		imageURL = Draaibank.class.getResource("resources/zoomuitknop.gif");
		if (imageURL != null) 
		{
			verkleinIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading zoomuitknop.gif");
		}
		

		
	}

	public void zetDemoVersion(boolean b)
	{
		demoVersion = b;
		tekenPanel.zetDemoVersion(demoVersion);		
	}

	public void zetZoomOption(boolean b)
	{
		zoomOption = b;
		tekenPanel.zetZoomOption(zoomOption);		
	}

	public void zetOpdracht(Hashtable b, String[] randomVars, Hashtable randomValues)
	{
System.out.println("dbip zetOpdracht");		
		
		boolean demoVersion = false;
		boolean zoomOption = false;

		if (b.containsKey("demoVersion"))
			demoVersion = ((Boolean) b.get("demoVersion")).booleanValue();
		if (b.containsKey("zoomOption"))
			zoomOption = ((Boolean) b.get("zoomOption")).booleanValue();
		

		zetDemoVersion(demoVersion);
		zetZoomOption(zoomOption);
// HIER de state		
			
		tekenPanel.setState(b);
		
	}
	
	public void setState(Hashtable b)
	{
		
// HIER de state		
		tekenPanel.setState(b);

	}
	
	public void setEditState(Hashtable b)
	{
System.out.println("dbip setEditState");
		
		boolean demoVersion = false;
		boolean zoomOption = false;

		if (b.containsKey("demoVersion"))
			demoVersion = ((Boolean) b.get("demoVersion")).booleanValue();
		if (b.containsKey("zoomOption"))
			zoomOption = ((Boolean) b.get("zoomOption")).booleanValue();
		
		zetDemoVersion(demoVersion);
		zetZoomOption(zoomOption);
		
// HIER de state		
		tekenPanel.setState(b);
			
	}
	
	public Hashtable getState()
	{
		Hashtable h = tekenPanel.getState();
		
// HIER de state		
		
		return h;
		
	}
	
	public Hashtable getEditState()
	{
		Hashtable h = tekenPanel.getState();
		
		h.put("demoVersion", new Boolean(demoVersion));
		h.put("zoomOption", new Boolean(zoomOption));

// HIER de state		
		
		return h;
	}
	
	
	
	public InteractieEditPanel getEditPanel()
	{
		return new DBInteractieEditPanel();
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		
//		System.out.println("tip set bounds " + b + " " + h);
		
		if (h == 1)
			return;
		
		super.setBounds(x, y, b, h);
		
		
		if (tekenPanel == null) 
		{	tekenPanel = new TekenPanel();
			add(tekenPanel, 0);
			
			tekenPanel.setSize(b, h);
			
			tekenPanel.init();
			
//ControlPanel??			
			
//System.out.println("tekenPanel created");			
		}
		else
		{	tekenPanel.setSize(b, h);
			
			tekenPanel.initialiseer2();
			
			//tekenPanel.tb.setBounds(0, 0, b, h);
		
			tekenPanel.repaint();
		}
	}
	
	public void wis()
	{}
	
	public void zetMaat()
	{}
	
	public int geefAsHoogte()
	{	return 0;
	}
	
	public int getIpId()
	{	return 0;
	}
	
	public String getIpExpString()
	{	return null;
	}
	
	public int getScore()
	{	return score;
	}
	
	public int getScoreMax()
	{	return scoreMax;
	}
	
	public int[][] getScoreObjectives()
	{   return null;
	}
	
	public boolean isCorrect()
	{	return true;
	}
	
	public boolean isFout()
	{	return false;
	}
	
	public void zetMode(int mode)
	{}
	
	public void zetNagekeken(boolean b)
	{}
	
    public void stop()
    {}
    
    public void start()
    {}
    
    public void destroy()
    {}
    
    public void opnieuw()
    {}
    
    public void kijkNa()
    {}
    
    public void kijkNa(int stapNr)
    {}
    
    public void addActionListener(ActionListener al)
    {}

	public void zetBreedte(int b)
	{}
	
	public void zetHoogte(int h)
	{}
    
	public void actionPerformed(ActionEvent e)
	{}

}
