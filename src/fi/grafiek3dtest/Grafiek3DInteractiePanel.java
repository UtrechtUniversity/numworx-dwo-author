package fi.grafiek3dtest;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.*;

import fi.beans.wiskopdrbeans.WiskOpdrApplet;
// deze moet vanwege interface WiskOpdrApplet
import fi.beans.wiskopdrbeans.InteractiePanel;
// deze moet vanwege interface InteractiePanel
import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class Grafiek3DInteractiePanel extends JPanel 
									  implements InteractiePanel, InteractieEditPanel,
									  	         ActionListener
									
{
	
	Hashtable images = null;
	String[] imageNames = 
	{	"zoominknop.gif",
		"zoomuitknop.gif",
	};
	
	int score = 0;
	int scoreMax = 10;
	
	Grafiek3DComponent g3dc;
	
	boolean noSetBounds = false;	
	
	public Grafiek3DInteractiePanel()
	{
		setLayout(null);
		// echte initiatie vind pas plaats na setBounds
		
		images = new Hashtable();
		Image[] image = new Image[imageNames.length];
		for (int i = 0; i < imageNames.length; i++)
		{	java.net.URL imageURL = Grafiek3D.class.getResource("resources/" + imageNames[i]);
			if (imageURL != null)
			{	ImageIcon imageIcon = new ImageIcon(imageURL);
				image[i] = imageIcon.getImage();
			}	
		}
		for (int i = 0; i < imageNames.length; i++)
		{	
//if (image[i] != null)
//System.out.println("im " + i + " not null");	
			images.put(imageNames[i], image[i]);
		}
		
		
	}

	
	public void zetOpdracht(Hashtable b, String[] randomVars, Hashtable randomValues)
	{
		g3dc.zetOpdracht(b, randomVars, randomValues);
	}
	
	public void setState(Hashtable b)
	{
		g3dc.setState(b);
	}
	
	public void setEditState(Hashtable b)
	{
		g3dc.setEditState(b);
	}
	
	public Hashtable getState()
	{
		Hashtable h = g3dc.getState();
		
		return h;
		
	}
	
	public Hashtable getEditState()
	{
		Hashtable h = g3dc.getEditState();
		
		
		return h;
	}
	
	
	public void zetZoomOptie(boolean b)
	{	g3dc.zetZoomOptie(b);
	}
	
	public void zetTranslateOptie(boolean b)
	{	g3dc.zetTranslateOptie(b);
	}
	
	public void zetSolidDraadKeuzeOptie(boolean b)
	{	g3dc.zetSolidDraadKeuzeOptie(b);
	}
	
	public void zetFinerKeuzeOptie(boolean b)
	{	g3dc.zetFinerKeuzeOptie(b);
	}
	
	public void zetAsKeuzeOptie(boolean b)
	{	g3dc.zetAsKeuzeOptie(b);
	}
	
	public void zetLabelKeuzeOptie(boolean b)
	{	g3dc.zetLabelKeuzeOptie(b);
	}

	public void zetProjectieKeuzeOptie(boolean b)
	{	g3dc.zetProjectieKeuzeOptie(b);
	}

	public void zetKleurKeuzeOptie(boolean b)
	{	g3dc.zetKleurKeuzeOptie(b);
		
	}
	
	public void zetFunctieTypeKeuze(boolean b)
	{
		g3dc.zetFunctieTypeKeuze(b);
	}

	public void zetVoorbeeldenEnabled(boolean b)
	{
		g3dc.zetVoorbeeldenEnabled(b);
	}
	
	public void zetFiguurIsDemo(boolean b)
	{	g3dc.zetFiguurIsDemo(b);
		
	}
	
	public InteractieEditPanel getEditPanel()
	{
		return new Grafiek3DInteractieEditPanel();
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		
//		System.out.println("gip set bounds " + b + " " + h);
		
		if (h == 1)
			return;
		
		super.setBounds(x, y, b, h);
		
		if (g3dc == null) 
		{	g3dc = new Grafiek3DComponent(0, 0, b, h, images, imageNames);
			add(g3dc, 0);
//System.out.println("g3dc created");			
		}
		else
		{	g3dc.setSize(b, h);

// HIER NEW MODEL?Nee?		
//System.out.println("g3dc sized");		
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
	
	//public String getIpExpString();
	
	public int getScore()
	{	return score;
	}
	
	public int getScoreMax()
	{	return scoreMax;
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
