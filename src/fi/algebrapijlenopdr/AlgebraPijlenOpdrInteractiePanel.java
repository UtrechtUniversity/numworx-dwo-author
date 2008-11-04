package fi.algebrapijlenopdr;

import java.awt.*;
import java.awt.event.*;

import fi.algebrapijlenopdr.schuifobjects.*;

import java.util.Enumeration;
import java.util.Hashtable;
import java.lang.reflect.Constructor;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class AlgebraPijlenOpdrInteractiePanel extends Panel implements InteractiePanel, InteractieEditPanel
{	
	private AlgebraSchuifVeld algebraSchuifVeld;

	
	
	public AlgebraPijlenOpdrInteractiePanel()
	{	setLayout(null);
		// echter initiatie vind pas plaats na setBounds
	}
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{	//Hashtable state = null;
		//if(h.containsKey("state"))state = (Hashtable)h.get("state");
		algebraSchuifVeld.setEditModeState(h);
		System.out.println(h.toString());
	}
	
	public void setState(Hashtable h)
	{	//Hashtable state = null;
		//if(h.containsKey("state"))state = (Hashtable)h.get("state");
		algebraSchuifVeld.setState(h);
	}
	public void setEditState(Hashtable h)
	{	//Hashtable state = null;
		//if(h.containsKey("state"))state = (Hashtable)h.get("state");
		algebraSchuifVeld.setEditModeState(h);
	}
	public Hashtable getState()
	{	//Hashtable state = null;
		//state = algebraSchuifVeld.getState();
		//Hashtable h = new Hashtable();
		//h.put("state", state);
		//return h;
		return algebraSchuifVeld.getState();
	}
	public Hashtable getEditState()
	{	//Hashtable state = null;
		//state = algebraSchuifVeld.getState();
		//Hashtable h = new Hashtable();
		//h.put("state", state);
		//return h;
		return algebraSchuifVeld.getState();
	}
	public InteractieEditPanel getEditPanel()
	{	return new AlgebraPijlenOpdrInteractiePanel();
	}	
	public void setBounds(int x, int y, int b, int h)
	{	super.setBounds(x,y,b,h);
		if(algebraSchuifVeld==null) 
		{	algebraSchuifVeld = new AlgebraSchuifVeld(0,0,b,h);
			add(algebraSchuifVeld,0);
		}
		algebraSchuifVeld.start();
	}
	
	public void zetBreedte(int b){}
	
	public void zetHoogte(int h){}
	
	public void wis(){}
	
	public void zetMaat(){}
	
	public int geefAsHoogte(){return 0;}
	
	public int getScore(){return 0;}
	
	public int getScoreMax(){return 0;}
	
	public boolean isCorrect(){return true;}
	
	public boolean isFout(){return false;}
	
	public void zetMode(int mode){}
	
	public void zetNagekeken(boolean b){}
	
    public void stop(){}
    
    public void start(){algebraSchuifVeld.tekenOpnieuw();}
    
    public void destroy(){}
    
    public void opnieuw(){}
    
    public void kijkNa(){}
    
    public void kijkNa(int stapNr){}
    
    public void addActionListener(ActionListener al){}
    
	public void actionPerformed(ActionEvent e){}
}
