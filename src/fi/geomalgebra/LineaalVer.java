package fi.geomalgebra;

import java.awt.Polygon;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import fi.geomalgebra.text.*;

import javax.swing.*;

class LineaalVer extends JPanel  implements MouseListener 
{	
	int schaal = 24;
	int aantal;
	int breedte;
	int hoogte;
	protected ActionListener actionListener = null;
	int min, max;
	int huidigeWaarde;
	Rectangle[] getalknoppen;
	int[] getallen;
	int nulPositie;
	
	public LineaalVer(int x, int y)
	{	addMouseListener(this);
		breedte = 20;
		hoogte = y-breedte-50;
		setBackground(new Color(220,220,220));
		setBounds(2,5,breedte,hoogte);
		
		aantal = hoogte/(2*schaal)+2;
		getalknoppen = new Rectangle[2*aantal+1];
		getallen = new int[2*aantal+1];
		nulPositie = hoogte/2;
		min = -aantal+2-(nulPositie-hoogte/2)/(schaal);
		max = aantal+2-(nulPositie-hoogte/2)/(schaal);
	}
	
	//public void paint(Graphics g)
	public void paintComponent(Graphics g)
	{	g.drawLine(breedte-1,0,breedte-1,hoogte);
		aantal = hoogte/(2*schaal);
		for(int i=min ; i<max-3 ; i++)
		{	int dy = hoogte/2 - i*schaal;
			g.drawLine(breedte,dy,breedte-6,dy);
			g.drawLine(breedte,dy-schaal/2,breedte-4,dy-schaal/2);
			g.drawString(Integer.toString(i),breedte-18,dy+3);
			getalknoppen[i-min] = new Rectangle(0,dy-8,breedte,10);
			getallen[i-min] = i;
		}
		g.setFont(new Font("SansSerif", Font.BOLD, 12));
		g.drawString(Integer.toString(0),breedte-18,hoogte/2+3);
		
	}
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}
	
	public void mousePressed(MouseEvent e)
	{	for(int i=0 ; i<2*aantal+1 ; i++)
		{	if(getalknoppen[i].contains(e.getX(), e.getY()))
			{	huidigeWaarde = getallen[i];
				if (actionListener != null)
 				{	actionListener.actionPerformed( new ActionEvent(this, 0, "maakBasis", huidigeWaarde) );
 				}
				return;
			}
		}
	}
	
	public void mouseReleased(MouseEvent e)	{;}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
			
}