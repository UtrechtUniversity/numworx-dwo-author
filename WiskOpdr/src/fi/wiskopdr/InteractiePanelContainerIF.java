package fi.wiskopdr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;

import fi.beans.wiskopdrbeans.*;
import fi.wiskopdr.opdrnav.XWidgetManager;

public interface InteractiePanelContainerIF 
{
	public void zetOpdracht(Hashtable b, String[] randomVars, Hashtable randomValues);
	
	public void setState(Hashtable b);
	
	public void setEditState(Hashtable b);
	
	public Hashtable getState();
	
	public Hashtable getEditState();
	
	public Vector geefInteractiePanels();
	
	public void setBounds(int x, int y, int b, int h);
	
	public void wis();
	
	public InteractiePanel getInteractiePanel(int ID);
	
	public boolean zetFocus();
	    
	public int getScore();
	
	public int[][] getScoreObjectives();
	
	public int getScoreMax();
	
	public int[][] getScoreMaxObjectives();
	
	public int[][] getMeasuredMisconceptions();
	
	public int[][] getPossibleMisconceptions();
	
	public boolean isCorrect();
	
	public boolean isFout();
	
	public void zetMode(int mode);
	
	public void zetNagekeken(boolean b);
	
	public void closePopup();
	
    public void stop();
    
    public void start();
    
    public void destroy();
    
    public void opnieuw();
    
    public void kijkNa();
    
    public void kijkNa(int stapNr);
    
    public void addActionListener(ActionListener al);
    
	public void actionPerformed(ActionEvent e);
	
	public void initConnections(XWidgetManager instance);

    default public void  getOngezien(Collection<String> result) {
    }
	
}
