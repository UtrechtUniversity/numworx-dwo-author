package fi.algebrapijlenopdr.tekstobjects;

import java.awt.*;
import java.awt.event.*;
import fi.algebrapijlenopdr.formuleobjects.*;
import fi.algebrapijlenopdr.tekstobjects.*;

public class TekstLinkVak extends TekstDeelVak implements ActionListener
{
	private LinkRegel link;
	private Font font = new Font("SansSerif",Font.BOLD,12);
	private FontMetrics fm;
	private boolean selected = false;
	
	public TekstLinkVak(TekstVak tv)
	{	super(tv);
		setFont(font);
		fm = getFontMetrics(getFont());
	
		link = new LinkRegel(this);
		link.addKeyListener(link);
		link.addFocusListener(link);
		link.setFont(font);
		link.setForeground(Color.blue);
		link.setLocation(0,0);
		link.setEditable(true);
		
		add(link);
				
		setSize(link.getSize().width,link.getSize().height);
		ashoogte = link.ashoogte;
	}
	
	public void requestFocus()
	{	link.requestFocus();
	}
	
	public void vulVak(String s)
	{	for(int i=0 ; i<s.length() ; i++) 
		link.insert(new TekstTeken(s.charAt(i)));
	
	}
	
	public LinkRegel geefTekstVak()
	{	return link;
	}
	
	public void setEditable(boolean b)
	{	link.setEditable(b);
	}
	
	public void setSelectable(boolean b)
	{	link.setSelectable(b);
	}
	
	public void zetMaat()
	{	setSize(link.getSize().width, link.getSize().height);
		link.setLocation(0,0);
		ashoogte = link.ashoogte;
		if(getParent()instanceof TekstElement)((TekstElement)getParent()).zetMaat();
	}
	
	public String toString()
	{	return "$L" + link.toString() + "@";
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals("focus"))requestFocus();//tekstVak.zetAntwoordVak(antwoordVak);
	}
	
}
