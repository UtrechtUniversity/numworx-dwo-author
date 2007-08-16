package logotekenap;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MuisBeheerder implements MouseListener, MouseMotionListener
{
	private int eerstex, laatstex, eerstey, laatstey, dx, dy;
	private TekenApplet eigenaar;
	private AnimatieBeheerder ab;
	private boolean animatieWasAan;
	private boolean actief;
	
	public MuisBeheerder(TekenApplet ap)
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
	//de AnimatieBeheerder maakt zich met deze methode kenbaar aan de Muisbeheerder  
	//-------------------------------------------------------------------------------------------
	public void meldAnimatieBeheerder(AnimatieBeheerder ab)
	{	this.ab = ab;
	}
	//-------------------------------------------------------------------------------------------
	//afhandeling van de muis gebeurtenissen 
	//-------------------------------------------------------------------------------------------
	public void mousePressed(MouseEvent e)
	{	if(actief)
		{	if(ab!=null && ab.animatieLopend())
			{	animatieWasAan = true;
				ab.onderbreekAnimatie();
			}
			eerstex = e.getX();
			eerstey = e.getY();
			laatstex = e.getX();
			laatstey = e.getY();
			eigenaar.muisDrukActie();
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{	if(actief)
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
	{	eigenaar.muisLosActie();
		if(animatieWasAan)
		{	animatieWasAan = false;
			ab.beginAnimatie();
		}
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
	public int geefX()
	{	return laatstex;
	}
	public int geefY()
	{	return laatstey;
	}
}	
