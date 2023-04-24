package fi.algebrapijlenopdr.tekstobjects;

import java.awt.*;
import java.awt.event.*;
import fi.algebrapijlenopdr.formuleobjects.*;
import fi.algebrapijlenopdr.tekstobjects.*;

public class TekstDeelVak extends TekstElement implements ActionListener
{
	private Font font = new Font("TimesRoman",Font.PLAIN,13);
	private FontMetrics fm;
	private boolean selected = false;
	
	public TekstDeelVak(TekstVak tv)
	{	setLayout(null);
		setBackground(getBackground());
		tekstVak = tv;
	}
	
	public void vulVak(String s)
	{	
	}
	
	public FormuleVak geefFormuleVak()
	{	return null;
	}
	
	public TekstArea geefAntwoordVak()
	{	return null;
	}
	
	public void setEditable(boolean b)
	{	
	}
	
	public void setSelectable(boolean b)
	{	
	}
	
	public void setSelected(boolean b)
	{	
	}
	
	public boolean isSelected()
	{	return false;
	}
	
	public void zetMaat()
	{	
	}
	
	public String toString()
	{	return null;
	}
	
	public void actionPerformed(ActionEvent e)
	{	
	}
	
}
