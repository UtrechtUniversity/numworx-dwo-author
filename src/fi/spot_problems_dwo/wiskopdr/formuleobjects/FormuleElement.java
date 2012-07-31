package fi.spot_problems_dwo.wiskopdr.formuleobjects;

import java.awt.*;

import javax.swing.*;

public class FormuleElement extends Container
{	
	public int ashoogte;
	public FormuleVak formuleVak;
	
	
	public Component add( Component comp ) 
	{	Component c = super.add(comp);
		comp.setFont(getFont());
		return c;
	}
	public Component add( Component comp, int index ) 
	{	Component c = super.add(comp, index);
		comp.setFont(getFont());
		return c;
	}
	
	public void zetMaat()
  	{
	}
	
	public void setEditable(boolean b)
	{	
	}
	
	public void setSelectable(boolean b)
	{	
	}
	
	public boolean isSelected()
  	{	return false;
	}
	
	public void setSelected(boolean b)
  	{	
	}
	
	public void neemFocus(String richting, FormuleElement fe)
	{
	}
	public void neemFocus(String richting)
	{
	}
	public String toString()
	{	return null;
	}
}
