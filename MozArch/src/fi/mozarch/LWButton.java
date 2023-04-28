package fi.mozarch;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class LWButton extends JButton implements MouseListener	
{	
	private Font defaultfont = new Font("SansSerif", Font.BOLD, 14);
	protected ActionListener actionListener = null;
	protected String opschrift = "";	
	public LWButton(String str)
	{	super(str);
		
		setFont(defaultfont);		opschrift = str;
		
		addMouseListener(this);	}
	
	
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}
  
	public String getLabel()
	{	return opschrift;
	}
		public void mousePressed(MouseEvent e) {}	
	public void mouseReleased(MouseEvent e) 
 	{	if (isEnabled())
 		{	if (actionListener != null)
 			{	actionListener.actionPerformed(new ActionEvent(this, 0, opschrift));
 			}
 		} 	}  
	
	public void mouseEntered(MouseEvent e)	{	setCursor(new Cursor(Cursor.HAND_CURSOR ));
	}
	public void mouseExited(MouseEvent e)
	{	setCursor(new Cursor(Cursor.DEFAULT_CURSOR ));
	}
	public void mouseClicked(MouseEvent e){;}
	
}