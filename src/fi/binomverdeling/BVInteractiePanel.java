package fi.binomverdeling;

import java.awt.TextField;
import java.awt.Color;
import java.awt.event.*;
import java.util.Hashtable;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.beans.wiskopdrbeans.*;


public class BVInteractiePanel extends JPanel implements InteractiePanel
{
	
	public BVInteractiePanel()
	{
		setLayout(null);
		
		//Test-textfield
		JTextField textField = new JTextField();
		textField.setBounds(50,100,200,25);
		add(textField);
	}
	
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{
		
	}
	
	public void setState(Hashtable h)
	{
		
	}
	
	
	public void setEditState(Hashtable h)
	{
		
		
		
	}
	
	public Hashtable getState()
	{
		Hashtable h = new Hashtable();
		return h;
	}
	
	public Hashtable getEditState()
	{
		Hashtable h = new Hashtable();
		return h;
	}
	
	public InteractieEditPanel getEditPanel()
	{
		return new BVInteractieEditPanel();
	}
	public void setBounds(int x, int y, int b, int h)
	{	
		super.setBounds(x,y,b,h);
	}
	public void wis()
	{
		
	}
	public void zetMaat()
	{
	
	}
	public int geefAsHoogte()
	{
		return 0;
	}
	public int getIpId()
	{
		return 0;
	}
	
	public int getScore()
	{
		return 0;
	}
	public int getScoreMax()
	{
		return 0;
	}
	public boolean isCorrect()
	{
		return true;
	}
	public boolean isFout()
	{
		return false;
	}
	public void zetMode(int mode)
	{
	
	}
	public void zetNagekeken(boolean b)
	{
	
	}
    public void stop()
	{
	
	}
    public void start()
	{
    	
	}
   
    public void destroy()
	{
	
	}
    public void opnieuw()
	{
	
	}
    public void kijkNa()
	{
	
	}
    public void kijkNa(int stapNr)
	{
	
	}
    public void addActionListener(ActionListener al)
	{
	
	}
	
	
	
}
