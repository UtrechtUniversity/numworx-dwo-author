package fi.algebrapijlenopdr.tekstobjects;

import java.awt.*;
import java.awt.event.*;
import fi.algebrapijlenopdr.formuleobjects.*;
import fi.algebrapijlenopdr.tekstobjects.*;

public class TekstFormuleVak extends TekstElement implements ActionListener
{
	private FormuleVak formuleVak;
	private Font font = new Font("TimesRoman",Font.PLAIN,13);
	private FontMetrics fm;
	private boolean selected = false;
	
	public TekstFormuleVak(TekstVak tv)
	{	setLayout(null);
		setBackground(getBackground());
		tekstVak = tv;
	
		formuleVak = new FormuleVak();
		formuleVak.setLocation(0,0);
		formuleVak.addActionListener(this);
		setFont(font);
		add(formuleVak);
				
		
				
		setSize(formuleVak.getSize().width,formuleVak.getSize().height);
		ashoogte = formuleVak.ashoogte;
	}
	
	public void vulVak(String s)
	{	formuleVak.vulVak(s);
	}
	
	public FormuleVak geefFormuleVak()
	{	return formuleVak;
	}
	
	public void setEditable(boolean b)
	{	formuleVak.setEditable(b);
	}
	
	public void setSelectable(boolean b)
	{	formuleVak.setSelectable(b);
	}
	
	public void setSelected(boolean b)
	{	if(selected!=b)
		{	selected = b;
			formuleVak.setSelected(b);
			repaint();
		}
	}
	
	public boolean isSelected()
	{	return selected;
	}
	
	public void zetMaat()
	{	setSize(formuleVak.getSize().width, formuleVak.getSize().height);
		formuleVak.setLocation(0,0);
		ashoogte = formuleVak.ashoogte;
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
		if(getParent()instanceof TekstElement)((TekstElement)getParent()).zetMaat();
	}
	
	public String toString()
	{	return formuleVak.toString();
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals("focus")) tekstVak.zetFormuleVak(formuleVak);
	}
	
}
