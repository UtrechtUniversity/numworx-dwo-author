package fi.wiskopdr.formuleobjects;

import java.awt.*;

import javax.swing.*;

import fi.beans.stringutils.StringUtils;

public class FormuleElement extends JPanel
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
	public String toMathML() {
		// TODO more escapes? nu: &, <
				String string = toString();
				string = StringUtils.replaceStr(string, "&", "&amp;"); // THIS ORDER
				string = StringUtils.replaceStr(string, "<", "&lt;");
				return "<mtext>"+ string + "</mtext>";
			}
}
