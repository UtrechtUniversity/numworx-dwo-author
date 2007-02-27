package fi.geomalgebra;

import java.awt.Polygon;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import fi.geomalgebra.text.*;

class LineaalHor extends Panel  implements MouseListener 
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

	
	public LineaalHor(int x, int y)
	{	addMouseListener(this);
		hoogte = 20;
		breedte = x-hoogte;
		setBackground(new Color(220,220,220));
		setBounds(hoogte/2,y-hoogte-42,breedte+hoogte/2-1,hoogte);
		
		aantal = breedte/(2*schaal)+2;
		getalknoppen = new Rectangle[2*aantal+1];
		getallen = new int[2*aantal+1];
		nulPositie = breedte/2;
		min = -aantal+2-(nulPositie-breedte/2)/(schaal);
		max = aantal+2-(nulPositie-breedte/2)/(schaal);
	}
	public void paint(Graphics g)
	{	g.drawLine(0,0,breedte,0);
		aantal = breedte/(2*schaal);
		for(int i=min ; i<max-3 ; i++)
		{	int dx = breedte/2 + i*schaal;
			g.drawLine(dx,0,dx,6);
			//g.drawLine(dx+schaal/2,0,dx+schaal/2,4);
			g.drawLine(dx-schaal/2,0,dx-schaal/2,4);
			if(i<0) dx -= 3;
			g.drawString(Integer.toString(i),dx-3,18);
			getalknoppen[i-min] = new Rectangle(dx-3,0,10,hoogte);
			getallen[i-min] = i;
		}
		g.setFont(new Font("SansSerif", Font.BOLD, 12));
		g.drawString(Integer.toString(0),breedte/2-3,18);
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