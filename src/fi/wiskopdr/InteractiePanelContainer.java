package fi.wiskopdr;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;


import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.wiskopdr.opdrnav.XWidgetManager;

public class InteractiePanelContainer extends JPanel implements ActionListener, InteractiePanelContainerIF
{
	InteractiePanel interactiePanel;
	int mode;
	
	public InteractiePanelContainer()
	{	setLayout(null);
		//setOpaque(false);
	}
	
	public InteractiePanelContainer(InteractiePanel interactiePanel)
	{	setLayout(null);
		this.interactiePanel = interactiePanel;
	}
	
	public InteractiePanel getInteractiePanel(int ID)
	{	if(interactiePanel!=null && interactiePanel.getIpId()==ID) return interactiePanel;
		return null;
	}
	
	public Vector geefInteractiePanels()
	{	return null;
	}
	
	public boolean zetFocus()
	{
		return false;
	}
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{	
		int soortInteractiePanel = 0;
		Hashtable interactiePanelLaunchState = null;
		
		if(h.containsKey("soortInteractiePanel")) soortInteractiePanel = ((Integer)h.get("soortInteractiePanel")).intValue();
		if(h.containsKey("interactiePanelLaunchState")) interactiePanelLaunchState = (Hashtable)h.get("interactiePanelLaunchState");
		
		//// voor compatability met oude versie
		boolean vergelijking = false;
		if(h.containsKey("vergelijking")|| interactiePanelLaunchState==null) 
		{	if(h.containsKey("vergelijking")) vergelijking = ((Boolean)h.get("vergelijking")).booleanValue();
			if(vergelijking)
			{	if(interactiePanel==null || !(interactiePanel instanceof AntwoordVergelijkingVak))
				{	if(interactiePanel!=null)remove((Component)interactiePanel);
					interactiePanel = new AntwoordVergelijkingVak();
					interactiePanel.setBounds(0,0,getSize().width, getSize().height);
					interactiePanel.addActionListener(this);
					add((Component)interactiePanel,0);
				}
			}
			else
			{	if(interactiePanel==null || !(interactiePanel instanceof AntwoordFormuleVak))
				{	if(interactiePanel!=null)remove((Component)interactiePanel);
					interactiePanel = new AntwoordFormuleVak();
					interactiePanel.setBounds(0,0,getSize().width, getSize().height);
					interactiePanel.addActionListener(this);
					add((Component)interactiePanel,0);
				}
			}
			if(interactiePanel!=null) {
				interactiePanel.wis();
				interactiePanel.zetOpdracht(h,randomVars,randomValues);// voor compatability met oude versie
			}
			return;
		}
		////
		
		
		/**/
		else if(soortInteractiePanel == 0)
		{	if(interactiePanel==null || !(interactiePanel instanceof AntwoordFormuleVak))
			{	if(interactiePanel!=null)remove((Component)interactiePanel);
				interactiePanel = new AntwoordFormuleVak();
				interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				interactiePanel.addActionListener(this);
				add((Component)interactiePanel,0);
			}
			if(interactiePanel!=null) {
				interactiePanel.wis();
				interactiePanel.zetOpdracht(interactiePanelLaunchState,randomVars,randomValues);
			}	
		}
		else if(soortInteractiePanel == 1)
		{
			if(interactiePanel==null || !(interactiePanel instanceof AntwoordVergelijkingVak))
			{	if(interactiePanel!=null)remove((Component)interactiePanel);
				interactiePanel = new AntwoordVergelijkingVak();
				interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				interactiePanel.addActionListener(this);
				add((Component)interactiePanel,0);
			}
			if(interactiePanel!=null) {
				interactiePanel.wis();
				interactiePanel.zetOpdracht(interactiePanelLaunchState,randomVars,randomValues);
			}
		}
		else if(interactiePanel!=null)
		{	//interactiePanel.wis();
			interactiePanel.zetOpdracht(interactiePanelLaunchState,randomVars,randomValues);
			
		}
		
		
		
		/*else if(interactiePanel!=null)
		{	interactiePanel.wis();
			//interactiePanel.zetOpdracht(interactiePanelLaunchState,randomVars,randomValues);
			interactiePanel.zetOpdracht(h,randomVars,randomValues);
			
		}*/
	}
	
	public void setBounds(int x, int y, int b, int h)
	{	if(interactiePanel!=null) interactiePanel.setBounds(0,0,b,h);
		super.setBounds(x,y,b,h);
	}
	
	public void zetMode(int mode)
	{	this.mode = mode;
		if(interactiePanel!=null) 
			interactiePanel.zetMode(mode);
	}
	
	
	public void setState(Hashtable h)
	{
		//Hashtable interactiePanelState = null;
		
		//if(h.containsKey("interactiePanelState")) interactiePanelState = (Hashtable)h.get("interactiePanelState");
		
		if(interactiePanel!=null)
			interactiePanel.setState(h);
		
	}
	
	public Hashtable getState()
	{
		//Hashtable interactiePanelState = null;
		
		//interactiePanelState = interactiePanel.getState();
		
		//Hashtable h = new Hashtable();				
		//h.put("interactiePanelState",interactiePanelState);
		
		if(interactiePanel!=null)
			return interactiePanel.getState();
		return null;
	}
	
	public void setEditState(Hashtable b)
	{
		
	}
	
	public Hashtable getEditState()
	{	return null;
		
	}
	
	public void wis()
    {   if(interactiePanel!=null)interactiePanel.wis();
    }
	
	public int getScore()
	{	if(interactiePanel!=null)
			return interactiePanel.getScore();
		return 0;
	}
	
	public int[][] getScoreObjectives()
	{	return null;
	}
	
	public int getScoreMax()
	{	if(interactiePanel!=null)
			return interactiePanel.getScoreMax();
		return 0;
	}
	
	public int[][] getScoreMaxObjectives()
	{	return null;
	}

	public boolean isCorrect()
	{	if(interactiePanel!=null)
			return interactiePanel.isCorrect();
		return true;	
	}
	
	public boolean isFout()
	{	if(interactiePanel!=null)
			return interactiePanel.isFout();
		return false;
	}
    
    public void stop()
    {   if(interactiePanel!=null)
    		interactiePanel.stop();
    }
    
    public void closePopup()
    {  
    }
    
    public void start()
    {   if(interactiePanel!=null) 
    		interactiePanel.start();
    }
    
    public void destroy()
    {
        //interactiePanel.destroy();
    }
    
    public void opnieuw()
    {   if(interactiePanel!=null)
    		interactiePanel.opnieuw();
    }
    
    public void kijkNa()
    {   if(interactiePanel!=null)
    		interactiePanel.kijkNa();
    }
    
    public void kijkNa(int stapNr)
    {  	if(interactiePanel!=null)
    		interactiePanel.kijkNa(stapNr);
    }
	
    public void zetNagekeken(boolean b)
    {   if(interactiePanel!=null)
    		interactiePanel.zetNagekeken(b);
    }
	
    public void initConnections(XWidgetManager manager)
    {
    	
    }
    
	public void actionPerformed(ActionEvent e)
	{	produceAction(e.getActionCommand());
	}
	
	//	ActionProducer
	private ActionListener actionListener = null;
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}	
 	
 	public void produceAction(String command)
 	{	if (actionListener != null)
 		{	actionListener.actionPerformed( new ActionEvent(this, 0, command) );
 		}
 	}
 	//

}
