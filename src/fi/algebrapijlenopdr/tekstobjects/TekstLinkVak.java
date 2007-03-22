package fi.algebrapijlenopdr.tekstobjects;

import java.awt.*;
import java.awt.event.*;
import fi.algebrapijlenopdr.formuleobjects.*;
import fi.algebrapijlenopdr.tekstobjects.*;

public class TekstLinkVak extends TekstDeelVak implements ActionListener
{
	private LinkRegel linkRegel;
	private Font font = new Font("SansSerif",Font.BOLD,12);
	private FontMetrics fm;
	private boolean selected = false;
	
	public TekstLinkVak(TekstVak tv)
	{	super(tv);
		setFont(font);
		fm = getFontMetrics(getFont());
	
		linkRegel = new LinkRegel(this);
		linkRegel.addKeyListener(linkRegel);
		linkRegel.addFocusListener(linkRegel);
		linkRegel.setFont(font);
		linkRegel.setForeground(Color.blue);
		linkRegel.setLocation(0,0);
		linkRegel.setEditable(true);
		
		add(linkRegel);
				
		setSize(linkRegel.getSize().width,linkRegel.getSize().height);
		ashoogte = linkRegel.ashoogte;
	}
	
	public void requestFocus()
	{	linkRegel.requestFocus();
	}
	
	public void vulVak(String s)
	{	
		for(int i=0 ; i<s.length()&& i<s.indexOf("$") ; i++) 
		linkRegel.insert(new TekstTeken(s.charAt(i)));
	
	}
	
	public LinkRegel geefTekstVak()
	{	return linkRegel;
	}
	
	public void setEditable(boolean b)
	{	linkRegel.setEditable(b);
	}
	
	public void setSelectable(boolean b)
	{	linkRegel.setSelectable(b);
	}
	
	public void zetMaat()
	{	setSize(linkRegel.getSize().width, linkRegel.getSize().height);
		linkRegel.setLocation(0,0);
		ashoogte = linkRegel.ashoogte;
		if(getParent()instanceof TekstElement)((TekstElement)getParent()).zetMaat();
	}
	
	public String toString()
	{	return "$L" + linkRegel.toString() + "@";
	}
	
	public String getUrl()
	{	return linkRegel.getLink().getUrl();
	}
	
	public int getWidth()
	{	return linkRegel.getLink().getWidth();
	}
	
	public int getHeight()
	{	return linkRegel.getLink().getHeight();
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals("focus"))requestFocus();//tekstVak.zetAntwoordVak(antwoordVak);
	}
	
}
