package fi.beans.wiskopdrbeans;

import java.awt.AWTEventMulticaster;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

public interface InteractiePanel 
{
	public void zetOpdracht(Hashtable b, String[] randomVars, Hashtable randomValues);
	
	public void setState(Hashtable b);
	
	/**
	 * Used in tekstInteractiePanel.
	 * @param b
	 */
	public void setEditState(Hashtable b);
	
	public Hashtable getState();
	
	/**
	 * Used in tekstInteractiePanel. 
	 * @param b
	 */
	public Hashtable getEditState();
	
	/**
	 * Different setup?
	 * @return
	 */
	public InteractieEditPanel getEditPanel();
		
	/**
	 * Used without (cast).
	 * @see java.awt.Component
	 * @deprecated
	 */
	public void setBounds(int x, int y, int b, int h);
	
	/**
	 * Init methode. just before zetOpdracht
	 */
	public void wis();
	/**
	 * Nederlandse versie van doLayout(). Hier een andere methode voor vinden
	 * @deprecated
	 */
	public void zetMaat();
	
	/*
	 * Wordt niet gebruikt in deze context. Alleen bij een paar widgets.
	 * @return ?
	 */
	//public int geefAsHoogte();
	
	/**
	 * Waar wordt dit gebruikt? Volgens mij nog nergens. Future?
	 */
	public int getIpId();
	
	//public String getIpExpString();
	
	public int getScore();
	
	public int[][] getScoreObjectives();
	
	public int getScoreMax();
	
// deze twee samen nemen in een enum SuccessStatus getSuccessStatus();	(SCORM 2004: PASSED, FAILED, UNKNWON)
	public boolean isCorrect();	
	public boolean isFout();
	
	public void zetMode(int mode);

// Wordt gebruikt om vinkjes te zetten.	
	public void zetNagekeken(boolean b);
	
    public void stop();
    
    public void start();
    
    public void destroy();
    
    /**
     * Someone presses "opnieuw" button.
     */
    public void opnieuw();
    
    public void kijkNa();
    
    /**
     * wordt alleen gebruikt om -2 door te geven (klaar knop)
     * @param stapNr
     */
    public void kijkNa(int stapNr);
    
    
    /**
     * Wire interactionpanels.
     * @param al listener
     */
    public void addActionListener(ActionListener al);
    
	//public void actionPerformed(ActionEvent e);
	
}
