package fi.geomalgebra;

import java.awt.*;
import java.awt.event.*;

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

	boolean negatieveWaarden = true; 

	public LineaalVer(int x, int y)
	{	addMouseListener(this);
		breedte = 20;
		hoogte = y-breedte-50;

		setBounds(2,5,breedte,hoogte);
		
		aantal = hoogte/(2*schaal)+2;
		
		getalknoppen = new Rectangle[2*aantal+1];
		getallen = new int[2*aantal+1];
		nulPositie = hoogte/2;
		min = -aantal+2-(nulPositie-hoogte/2)/(schaal);
		max = aantal+2-(nulPositie-hoogte/2)/(schaal);
		
	}
	
	public void paintComponent(Graphics g)
	{	
		g.setColor(new Color(220,220,220));
		g.fillRect(0, 0, getSize().width, getSize().height);
		g.setColor(Color.black);
		
		if (negatieveWaarden)
			g.drawLine(breedte-1,0,breedte-1,hoogte);
		else
			g.drawLine(breedte-1,0,breedte-1,nulPositie);
		aantal = hoogte / (2 * schaal);
		
		int einde = max - 3;
		if (!negatieveWaarden)
			einde = max - 2;

		for (int i = min; i < einde; i++)
		{	int dy = nulPositie - i * schaal;
			getalknoppen[i - min] = null;
			if ((negatieveWaarden && i < 0) || i > 0)
			{
				g.drawLine(breedte, dy, breedte - 6, dy);
				g.drawLine(breedte, dy - schaal / 2, breedte - 4, dy - schaal / 2);
				g.drawString(Integer.toString(i), breedte - 18, dy + 3);
				getalknoppen[i-min] = new Rectangle(0,dy-8,breedte,10);
				getallen[i-min] = i;
			}
		}
		g.setFont(new Font("SansSerif", Font.BOLD, 12));
		g.drawLine(breedte, nulPositie, breedte - 6, nulPositie);
		g.drawLine(breedte, nulPositie - schaal / 2, breedte - 4, nulPositie - schaal / 2);
		g.drawString(Integer.toString(0),breedte - 18, nulPositie + 3);
		
	}
	
	public void zetNegatieveWaarden(boolean b)
	{
		negatieveWaarden = b;
		if (negatieveWaarden)
		{	nulPositie = hoogte / 2;
			aantal = hoogte /(2 * schaal) + 2;
			min = -aantal + 2 - (nulPositie - hoogte / 2) / (schaal);
			max = aantal + 2 - (nulPositie - hoogte / 2) / (schaal);
		}
		else
		{
			nulPositie = hoogte - schaal;
			aantal = hoogte /(2 * schaal) + 2;
			min = -1;
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
	{	for(int i=0 ; i<2*aantal+1 ; i++)
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