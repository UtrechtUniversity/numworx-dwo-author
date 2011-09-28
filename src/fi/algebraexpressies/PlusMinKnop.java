package fi.algebraexpressies;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class PlusMinKnop extends JComponent //Component 
						 implements MouseListener
{	
	private Polygon pijlPlus, pijlMin;
	protected ActionListener actionListener = null;
	public static int VERTIKAAL = 0;
	public static int HORIZONTAAL = 1;
	
	
	public PlusMinKnop(int x, int y, int b, int h, int soort)
	{	setBounds(x,y,b,h);
		addMouseListener(this);
		pijlPlus = new Polygon();
		pijlMin = new Polygon();
		if(soort==PlusMinKnop.VERTIKAAL)
		{	pijlPlus.addPoint(0,h/2-1);
			pijlPlus.addPoint(b,h/2-1);
			pijlPlus.addPoint(b/2,0);
			
			pijlMin.addPoint(0,h/2+1);
			pijlMin.addPoint(b,h/2+1);
			pijlMin.addPoint(b/2,h);
		}
		else
		{	pijlPlus.addPoint(b/2+1,0);
			pijlPlus.addPoint(b/2+1,h);
			pijlPlus.addPoint(b,h/2);
			
			pijlMin.addPoint(b/2-1,0);
			pijlMin.addPoint(b/2-1,h);
			pijlMin.addPoint(0,h/2);
		}
		
	}
	public void paint(Graphics g)
	{	g.setColor(Color.black);
		g.fillPolygon(pijlPlus);
		g.drawPolygon(pijlPlus);
		g.fillPolygon(pijlMin);
		g.drawPolygon(pijlMin);
	}
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}
  
	public void mousePressed(MouseEvent e)
	{	if(pijlPlus.contains(e.getX(),e.getY()))
		{	if (actionListener != null)
 			{	actionListener.actionPerformed( new ActionEvent(this, 0, "plus") );
 			}
		}
		else if(pijlMin.contains(e.getX(),e.getY()))
		{	if (actionListener != null)
 			{	actionListener.actionPerformed( new ActionEvent(this, 0, "min") );
 			}
		}
	}	
	public void mouseDragged(MouseEvent e){;}
	public void mouseReleased(MouseEvent e){;}
	public void mouseMoved(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}	
}
