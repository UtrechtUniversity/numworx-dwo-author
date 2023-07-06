package fi.algebrapijlenopdr.opdrnav;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

import javax.swing.*;

public class OpdrContainer extends JPanel 
{
	protected static int OEFENEN = 0;
	protected static int OEFENEN_STRAFPUNTEN = 1;
	protected static int ZELFTOETS = 2;
	protected static int EINDTOETS = 3;
	
	protected int score;
	protected boolean correct;
	protected int mode;
	
	public void start()
	{	
	}
	
	public void stop()
	{	
	}
	
	public void destroy()
	{	
	}
	
	
	public void setState(Hashtable h)
	{
	}
	
	public Hashtable getState()
	{	return null;
	}
	
	public void setEditState(String s)
	{	
	}
	
	public void setNewState(Hashtable h)
	{	
	}
	
	public String getEditState()
	{	return null;
	}
	
	public int getScore()
	{	return score;
	}
	
	public boolean isCorrect()
	{	return correct;
	}

	public void kijkNa()
	{
	}

	public void opnieuw()
	{
	}

	public void zetMode(int mode)
	{	this.mode = mode;
	}

	public void addActionListener(ActionListener l) 
 	{	
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	
 	}	
 	
}
