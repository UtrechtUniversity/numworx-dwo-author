package fi.mozarch;

import java.awt.*;
import java.awt.event.*;

public class LWButton extends Container implements MouseListener	
{	
	private Font defaultfont = new Font("SansSerif", Font.BOLD, 12);
	private Color bgColor = new Color(180, 180, 180);		
	private Color fgColor = Color.black;	private Color bgUseColor = bgColor;	private Color fgUseColor = fgColor;
	
	protected ActionListener actionListener = null;
	protected String opschrift = "";	
	public LWButton(String str)
	{	setLayout(null);		opschrift = str;
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
		
	public void setEnabled(boolean b)
	{	super.setEnabled(b);
		if ( isVisible() )
		{	repaint();
		}
	}		public void setBackground(Color c)
	{	bgColor = c;
		bgUseColor = c;		//bgBright = bgColor.brighter();		//bgDark = bgColor.darker();
	}
	
	public void paint(Graphics g)
	{			g.setColor(bgUseColor);
		g.fillRect(0, 0, getSize().width, getSize().height);		g.setColor(fgUseColor);
		g.drawRect(0, 0, getSize().width-1, getSize().height-1);		Font f = new Font("SansSerif", Font.BOLD, (int)(3*getSize().height/5));
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		int woordbreedte = fm.stringWidth(opschrift);
		g.drawString(opschrift,(getSize().width - woordbreedte)/2, (getSize().height+fm.getHeight())/2 - fm.getDescent());		super.paint(g);
	}
		
	public void mousePressed(MouseEvent e)	{	bgUseColor = bgColor;
		//bgBright = bgColor.darker();		//bgDark = bgColor.brighter();		//repaint();
	}	
	public void mouseReleased(MouseEvent e) 
 	{	if ( isEnabled() )
 		{	if (actionListener != null)
 			{	actionListener.actionPerformed( new ActionEvent(this, 0, opschrift) );
 			}
 		}		//bgBright = bgColor.brighter();		//bgDark = bgColor.darker();		repaint();
 	}  
	
	public void mouseEntered(MouseEvent e)	{	bgUseColor = bgColor.brighter();
		setCursor(new Cursor(Cursor.HAND_CURSOR ));
		repaint();	}
	public void mouseExited(MouseEvent e)
	{	bgUseColor = bgColor;		setCursor(new Cursor(Cursor.DEFAULT_CURSOR ));
		repaint();	}
	public void mouseClicked(MouseEvent e){;}
	
}