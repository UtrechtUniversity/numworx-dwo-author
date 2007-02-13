package fi.algebrapijlenopdr.tekstobjects;

import java.awt.*;
import java.awt.event.*;
import fi.algebrapijlenopdr.formuleobjects.*;
import fi.algebrapijlenopdr.tekstobjects.*;

public class TekstFormuleVak extends TekstDeelVak implements ActionListener
{
	private FormuleVak formuleVak;
	private Font font = new Font("TimesRoman",Font.PLAIN,12);
	private FontMetrics fm;
	private boolean selected = false;
	
	public TekstFormuleVak(TekstVak tv)
	{	super(tv);
	
		formuleVak = new FormuleVak();
		formuleVak.setLocation(0,0);
		formuleVak.addActionListener(this);
		setFont(tv.getFont());
		add(formuleVak);
				
		
				
		setSize(formuleVak.getSize().width,formuleVak.getSize().height);
		ashoogte = formuleVak.ashoogte+1;
	}
	
	public void setFont(Font font)
	{	Font f = new Font("TimesRoman", font.getStyle(), font.getSize()+1);
		super.setFont(f);
		formuleVak.setFont(f);
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
		ashoogte = formuleVak.ashoogte+1;
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
