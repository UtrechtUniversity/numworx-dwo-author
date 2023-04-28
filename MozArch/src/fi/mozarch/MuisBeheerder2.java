package fi.mozarch;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

class MuisBeheerder2 implements MouseListener, MouseMotionListener
{
	private int eerstex, laatstex, eerstey, laatstey, dx, dy;
	private TekenPanel eigenaar;
	private boolean actief;
	
	public MuisBeheerder2(TekenPanel ap)
	{	eigenaar = ap;
		actief = true;
		eerstex = 0;
		eerstey = 0;
		laatstex = 0;
		laatstey = 0;
		dx = 0;
		dy = 0;
	}
	//-------------------------------------------------------------------------------------------
	//afhandeling van de muis gebeurtenissen 
	//-------------------------------------------------------------------------------------------
	public void mousePressed(MouseEvent e)
	{	if (actief)
		{	
			eerstex = e.getX();
			eerstey = e.getY();
			laatstex = e.getX();
			laatstey = e.getY();
			eigenaar.muisDrukActie();
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{	if (actief)
		{	int x = e.getX();
			int y = e.getY();
			dx = x - laatstex;
			dy = laatstey - y;
			eigenaar.muisSleepActie();
			laatstex = x;
			laatstey = y;	
		}
	}
	public void mouseReleased(MouseEvent e)
	{	
		eigenaar.muisLosActie();
	}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
	public void mouseMoved(MouseEvent e){;}
	
	//-------------------------------------------------------------------------------------------
	//deze methode wordt gebruikt door de TraceBeheerder
	//-------------------------------------------------------------------------------------------
	public void setEnableMuisActie(boolean b)
	{	actief = b;
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt door de muishandlers in het leerlingenprogramma
	//-------------------------------------------------------------------------------------------
	public int geefSleepdx()
	{	return dx;
	}
	public int geefSleepdy()
	{	return dy;
	}
	public int geefDrukx()
	{	return eerstex;
	}
	public int geefDruky()
	{	return eerstey;
	}
}	

