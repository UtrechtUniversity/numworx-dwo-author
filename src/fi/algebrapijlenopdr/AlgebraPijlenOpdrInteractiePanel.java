package fi.algebrapijlenopdr;

import java.awt.*;
import java.awt.event.*;

import fi.algebrapijlenopdr.schuifobjects.*;

import java.util.Enumeration;
import java.util.Hashtable;
import java.lang.reflect.Constructor;

import javax.swing.*;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class AlgebraPijlenOpdrInteractiePanel extends JPanel implements InteractiePanel, InteractieEditPanel
{	
	private AlgebraSchuifVeld algebraSchuifVeld;

	
	
	public AlgebraPijlenOpdrInteractiePanel()
	{	setLayout(null);
		// echte initiatie vind pas plaats na setBounds
	
//System.out.println("APO-IPa");	
	}
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{	
		algebraSchuifVeld.setEditModeState(h);
	}
	
	public void setState(Hashtable h)
	{	
		algebraSchuifVeld.setState(h);
	}
	
	public void setEditState(Hashtable h)
	{	
		algebraSchuifVeld.setEditModeState(h);
	}
	
	public Hashtable getState()
	{	
		return algebraSchuifVeld.getState();
	}
	
	public Hashtable getEditState()
	{	
		return algebraSchuifVeld.getState();
	}
	
	public void zetToolkit(boolean b)
	{
		algebraSchuifVeld.zetToolkit(b);
	}
	
	public void zetAlleenInvullen(boolean b)
	{
		algebraSchuifVeld.zetAlleenInvullen(b);
	}
	
	public void zetIsDemo(boolean b)
	{
		algebraSchuifVeld.zetIsDemo(b);
	}
	
	public void zetBrugklas(boolean b)
	{
		algebraSchuifVeld.zetBrugklas(b);
	}
	
	public void zetTerugHeen(boolean b)
	{
		algebraSchuifVeld.zetTerugHeen(b);
	}
	
	public void zetTabelOptie(boolean b)
	{
		algebraSchuifVeld.zetTabelOptie(b);
	}

	public void zetGrafiekOptie(boolean b)
	{
		algebraSchuifVeld.zetGrafiekOptie(b);
	}

	public void zetScrollOptie(boolean b)
	{
		algebraSchuifVeld.zetScrollOptie(b);
	}

	public void zetZoomOptie(boolean b)
	{
		algebraSchuifVeld.zetZoomOptie(b);
	}
	
	public InteractieEditPanel getEditPanel()
	{	//return new AlgebraPijlenOpdrInteractiePanel();
		return new AlgebraPijlenOpdrInteractieEditPanel();
	}	
	
	public void setBounds(int x, int y, int b, int h)
	{	
//System.out.println("apoip set bounds");
		
		if (h == 1)
			return;
		
		super.setBounds(x, y, b, h);
		if (algebraSchuifVeld == null) 
		{	algebraSchuifVeld = new AlgebraSchuifVeld(0, 0, b, h);
			add(algebraSchuifVeld, 0);
//System.out.println("as created");			
		}
		else
		{	algebraSchuifVeld.setSize(b, h);
//System.out.println("as sized");		
		}
		algebraSchuifVeld.start();
		
		algebraSchuifVeld.tekenOpnieuw();
	}
	
	public void zetBreedte(int b)
	{	algebraSchuifVeld.setSize(b, algebraSchuifVeld.getSize().height);
	
		algebraSchuifVeld.tekenOpnieuw();
	}
	
	public void zetHoogte(int h)
	{	algebraSchuifVeld.setSize(algebraSchuifVeld.getSize().width, h);
	
		algebraSchuifVeld.tekenOpnieuw();
	}
	
	public void wis(){}
	
	public void zetMaat(){}
	
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
	{	return 0;
	}
	
	public int getScoreMax()
	{	return 0;
	}
	
	public boolean isCorrect()
	{	return true;
	}
	
	public boolean isFout()
	{	return false;
	}
	
	public void zetMode(int mode){}
	
	public void zetNagekeken(boolean b){}
	
    public void stop(){}
    
    public void start()
    {	algebraSchuifVeld.tekenOpnieuw();
    }
    
    public void destroy(){}
    
    public void opnieuw(){}
    
    public void kijkNa(){}
    
    public void kijkNa(int stapNr){}
    
    public void addActionListener(ActionListener al){}
    
	public void actionPerformed(ActionEvent e){}
}
