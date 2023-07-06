package fi.wiskopdr.tekstobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

import javax.swing.JPanel;

import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.opdrnav.XWidgetManager;
import fi.wiskopdr.tekstobjects.*;

public class TekstDeelVak extends JPanel implements TekstElement,  ActionListener, XWidgetManager.HasWidgetManager
{
	protected int ashoogte;
	protected TekstVak tekstVak;
	
	protected boolean selectable = true;
	protected boolean selected = false;
	
	//private Font font = new Font("TimesRoman",Font.PLAIN,13);
	private FontMetrics fm;
	//private boolean selected = false;
	
	public TekstDeelVak(TekstVak tv)
	{	setLayout(null);
		setBackground(getBackground());
		tekstVak = tv;
		setOpaque(false);
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
	{	selectable = b;
	}
	
	public void setSelected(boolean b)
	{	selected = b;
	}
	
	public boolean isSelected()
	{	return selected;
	}
	
	public void zetMaat()
	{	
	}
	
	public String toString()
	{	return null;
	}
	
	public String toCompleteString()
	{	return toString();
	}
	
	public void actionPerformed(ActionEvent e)
	{	
	}
	
//	ActionProducer
	private ActionListener actionListener = null;
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}	
 	
 	public void produceAction(String command)
 	{	if (actionListener != null)
 		{	actionListener.actionPerformed( new ActionEvent(this, 0, command) );
 		}
 	}
 	//
 	
 // implementation TekstElement
 	public Component add( Component comp ) 
	{	Component c = super.add(comp);
		if(getFont()!=null)comp.setFont(getFont());
		return c;
	}
	public Component add( Component comp, int index ) 
	{	Component c = super.add(comp, index);
		if(getFont()!=null)comp.setFont(getFont());
		return c;
	}
	
	
	public boolean isSpatie()
	{	return false;
	}
	
	
	public void neemFocus(String richting, TekstElement fe)
	{
	}
	public void neemFocus(String richting)
	{
	}
	
	public TekstVak getTekstVak()
	{
		return tekstVak;
	}
	
	public int getAsHoogte()
	{
		return ashoogte;
	}
//	



	public XWidgetManager getXWidgetManager() {
		return tekstVak.getXWidgetManager();
	}
	

	
}
