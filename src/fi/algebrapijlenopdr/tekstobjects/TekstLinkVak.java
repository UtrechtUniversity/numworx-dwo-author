package fi.algebrapijlenopdr.tekstobjects;

import java.awt.*;
import java.awt.event.*;
import fi.algebrapijlenopdr.formuleobjects.*;
import fi.algebrapijlenopdr.tekstobjects.*;

public class TekstLinkVak extends TekstDeelVak implements ActionListener
{
	private TekstRegel tekstVak;
	private Font font = new Font("SansSerif",Font.BOLD,12);
	private FontMetrics fm;
	private boolean selected = false;
	
	public TekstLinkVak(TekstVak tv)
	{	super(tv);
		setFont(font);
		fm = getFontMetrics(getFont());
	
		tekstVak = new TekstRegel();
		tekstVak.setFont(font);
		tekstVak.setForeground(Color.blue);
		tekstVak.setLocation(0,0);
		tekstVak.setEditable(true);
		
		add(tekstVak);
				
		setSize(tekstVak.getSize().width,tekstVak.getSize().height);
		ashoogte = tekstVak.ashoogte;
	}
	
	public void vulVak(String s)
	{	tekstVak.vulVak(s);
	}
	
	public FormuleVak geefTekstVak()
	{	return tekstVak;
	}
	
	public void setEditable(boolean b)
	{	tekstVak.setEditable(true);
	}
	
	public void zetMaat()
	{	setSize(tekstVak.getSize().width, tekstVak.getSize().height);
		tekstVak.setLocation(0,0);
		ashoogte = tekstVak.ashoogte;
		if(getParent()instanceof TekstElement)((TekstElement)getParent()).zetMaat();
	}
	
	public String toString()
	{	return "$L" + tekstVak.toString() + "@";
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals("focus"))requestFocus();//tekstVak.zetAntwoordVak(antwoordVak);
	}
	
}
