package fi.grafiek3dtest.tekstobjects;

import java.awt.*;
import javax.swing.*;

public interface TekstElement
{
	//public int ashoogte;
	//public TekstVak tekstVak;
	
	
	public Component add( Component comp );
	
	public Component add( Component comp, int index ); 
	
	public void zetMaat();
  	
	public void setEditable(boolean b);
	
	public void setSelectable(boolean b);
	
	public boolean isSelected();
  	
	public boolean isSpatie();
	
	public void setSelected(boolean b);
  		
	public void neemFocus(String richting, TekstElement fe);
	
	public void neemFocus(String richting);
	
	public String toString();
	
	public TekstVak getTekstVak();
	
	public int getAsHoogte();
	
}

/*
 public class TekstElement extends  JLayeredPane
{
	public int ashoogte;
	public TekstVak tekstVak;
	
	
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
	
	public boolean isSpatie()
	{	return false;
	}
	
	public void setSelected(boolean b)
  	{	
	}
	
	public void neemFocus(String richting, TekstElement fe)
	{
	}
	public void neemFocus(String richting)
	{
	}
	public String toString()
	{	return null;
	}
}
*/
