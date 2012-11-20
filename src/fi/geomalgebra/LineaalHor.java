package fi.geomalgebra;

import java.awt.Polygon;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import fi.geomalgebra.text.*;

import javax.swing.*;

class LineaalHor extends JPanel  implements MouseListener 
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

	boolean negatieveWaarden = true;
	
	public LineaalHor(int x, int y)
	{	addMouseListener(this);
		hoogte = 20;
		breedte = x - hoogte;
		//setBackground(new Color(220,220,220));
		setBounds(hoogte / 2, y - hoogte - 42, breedte + hoogte / 2 - 1, hoogte);
		
		aantal = breedte /(2 * schaal) + 2;
//System.out.println("aantal lh = " + aantal);		
		getalknoppen = new Rectangle[2 * aantal + 1];
		getallen = new int[2 * aantal + 1];
		nulPositie = breedte / 2;
		min = -aantal + 2 - (nulPositie - breedte / 2) / (schaal);
		max = aantal + 2 - (nulPositie - breedte / 2) / (schaal);
//System.out.println("min lh = " + min);
//System.out.println("max lh = " + max);
	}
	
	
	
	//public void paint(Graphics g)
	public void paintComponent(Graphics g)
	{	
		g.setColor(new Color(220,220,220));
		g.fillRect(0, 0, getSize().width, getSize().height);
		g.setColor(Color.black);
		if (negatieveWaarden)
			g.drawLine(0, 0, breedte, 0);
		else
			g.drawLine(nulPositie, 0, breedte, 0);
		aantal = breedte / (2 * schaal);

		int einde = max - 3;
		if (!negatieveWaarden)
			einde = max - 2;
		for (int i = min; i < einde; i++)
		//for (int i = min; i < max - 3; i++)
		{	//int dx = breedte / 2 + i * schaal;
			int dx = nulPositie + i * schaal;
			getalknoppen[i - min] = null;
			if ((negatieveWaarden && i < 0) || (i > 0))
			{	g.drawLine(dx, 0, dx, 6);
				//g.drawLine(dx+schaal/2,0,dx+schaal/2,4);
				g.drawLine(dx - schaal / 2, 0, dx - schaal / 2, 4);
				if (i < 0 || i >= 10) 
					dx -= 3;
				if (dx >= 3)
					g.drawString(Integer.toString(i), dx - 3, 18);
				
				getalknoppen[i - min] = new Rectangle(dx - 3, 0, 10, hoogte);
				getallen[i - min] = i;
			}	
		}
		g.setFont(new Font("SansSerif", Font.BOLD, 12));
		//g.drawString(Integer.toString(0), breedte / 2 - 3, 18);
		g.drawLine(nulPositie, 0, nulPositie, 6);
		g.drawString(Integer.toString(0), nulPositie - 3, 18);
	}
	
	public void zetNegatieveWaarden(boolean b)
	{
		negatieveWaarden = b;
		if (negatieveWaarden)
		{	nulPositie = breedte / 2;
			aantal = breedte /(2 * schaal) + 2;
			min = -aantal + 2 - (nulPositie - breedte / 2) / (schaal);
			max = aantal + 2 - (nulPositie - breedte / 2) / (schaal);
		}
		else
		{
			nulPositie = 2 * schaal;
			aantal = breedte /(2 * schaal) + 2;
			min = -2;
			max = min + 2 * aantal;
		}
		repaint();
		
	}
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}
	
	public void mousePressed(MouseEvent e)
	{	for (int i = 0; i < 2 * aantal + 1; i++)
		{	if ((getalknoppen[i] != null) && getalknoppen[i].contains(e.getX(), e.getY()))
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