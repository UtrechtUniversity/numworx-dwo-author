package fi.algebrapijlenopdr.tekstobjects;

import java.awt.*;
import java.awt.event.*;
import fi.algebrapijlenopdr.formuleobjects.*;
import fi.algebrapijlenopdr.tekstobjects.*;

public class TekstLinkVak extends TekstDeelVak implements ActionListener
{
	private TekstArea antwoordVak;
	private Font font = new Font("SansSerif",Font.PLAIN,12);
	private FontMetrics fm;
	private boolean selected = false;
	
	public TekstLinkVak(TekstVak tv)
	{	super(tv);
		setFont(font);
		fm = getFontMetrics(getFont());
	
		antwoordVak = new TekstArea();
		antwoordVak.setBounds(0,0,30,20);
		antwoordVak.setEditable(true);
		
		add(antwoordVak);
				
		setSize(antwoordVak.getSize().width,antwoordVak.getSize().height);
		//ashoogte = antwoordVak.getSize().height;
	}
	
	public void vulVak(String s)
	{	antwoordVak.setText(s);
	}
	
	public TekstArea geefAntwoordVak()
	{	return antwoordVak;
	}
	
	public void setEditable(boolean b)
	{	antwoordVak.setEditable(true);
	}
	
	public void zetMaat()
	{	setSize(antwoordVak.getSize().width, antwoordVak.getSize().height);
		antwoordVak.setLocation(0,0);
		//ashoogte = antwoordVak.getSize().height;
		if(getParent()instanceof TekstElement)((TekstElement)getParent()).zetMaat();
	}
	
	public String toString()
	{	return "$L" + antwoordVak.getText() + "@";
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals("focus"))requestFocus();//tekstVak.zetAntwoordVak(antwoordVak);
	}
	
}
