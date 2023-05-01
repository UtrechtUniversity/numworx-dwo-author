package fi.calculatordwo;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class ReplaceCaret extends DefaultCaret 
{
	protected synchronized void damage(Rectangle r) 
	{	if (r == null) return;
		x = r.x; 
		y = r.y;
		height = r.height;		
		if (width <= 0) 
		{	width = getComponent().getWidth();
		}
		repaint(); 
	}
	
	public void paint(Graphics g) 
	{	JTextComponent comp = getComponent();
	 	if (comp == null) return;
	
	 	int dot = getDot();
	 	Rectangle r = null;
	 	char dotChar;
	 	try 
	 	{	r = comp.modelToView(dot);
	 		if (r == null) return;
	 		dotChar = comp.getText(dot, 1).charAt(0);
	 	} 
	 	catch (BadLocationException e) { 
	 		 
	 		System.out.println("bad location exception");
	 		return;
	 	}
	
	 	if( (x != r.x) || (y != r.y) ) 
	 	{	repaint();
	 		x = r.x;
	 		y = r.y;
	 		height = r.height/10;
	 	}
	
	 	g.setColor(comp.getCaretColor());
	 	g.setXORMode(comp.getBackground()); // do this to draw in XOR mode
	
	 	
	 		
	 	width = g.getFontMetrics().charWidth(dotChar);
	 	if(width == 0)
	 		width = 10;
	 	if (isVisible()) 
	 		g.fillRect(r.x, r.y + 9*r.height/10, width, r.height/10);
	}
}