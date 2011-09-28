package fi.algebraexpressies;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

import fi.algebraexpressies.schuifobjects.*;
import fi.algebraexpressies.expressies.*;

public class AlgebraSchuifComponent extends SchuifComponent 
{	
	 int soort;
	 Pijl pijlIn1, pijlIn2;
	 Pijl[] pijlUit;
	 int aantalPu;
	 boolean isStapel;
	 boolean open;

	 
	 
	public AlgebraSchuifComponent(int srt, SchuifVeld sv, int x, int y, int b, int h)
	{	super(x, y, b, h, sv);
		soort = srt;
		isStapel = true;
		open = true;
		aantalPu = 0;
		pijlUit = new Pijl[10];
	}

	public Hashtable getState()
	{	boolean isStapel = false;
		
		isStapel = this.isStapel;
		
		Hashtable h = new Hashtable();
	    h.put("isStapel", new Boolean(isStapel));
	    
	    return h;
	}

    public void setState(Hashtable h)
    {	boolean isStapel = ((Boolean) h.get("isStapel")).booleanValue();
		
		this.isStapel = isStapel;
		
    }

	public void zetVakKleur(Color color)
	{	
	}
    
	public void paint(Graphics gIm)
  	{ 	
		
		if (soort == 1 && open)
		{	gIm.setColor(Color.gray);
			gIm.fillOval(18, 5, 3, 3);
		}
		else if (soort == 2)
		{	gIm.setColor(Color.gray);
			gIm.fillOval(8, 5, 3, 3);
			gIm.fillOval(28, 5, 3, 3);
		}
		super.paint(gIm);
	}
	
	public boolean contains(int x, int y)
	{	return (new Rectangle(0, 10, getSize().width, getSize().height - 10)).contains(x,y);
	}
	
	public void zetBoomZichtbaar(boolean b)
	{	if (pijlIn1 != null)
			pijlIn1.zender.zetBoomZichtbaar(b);
		if (pijlIn2 != null)
			pijlIn2.zender.zetBoomZichtbaar(b);
		setVisible(b);
		for (int i = 0; i < aantalPu; i++)
		{	pijlUit[i].setVisible(b);
		}
		repaint();
	}
	
	public void voegPijlToe(Pijl p)
	{	if (aantalPu < 10)
		{	p.zetPlaats(getLocation().x + getSize().width - 20 ,getLocation().y + getSize().height + 10);
			pijlUit[aantalPu] = p;
			//schuifveld.add(p,0);
			schuifveld.add(p);
			p.zetZender(this);
			aantalPu++;
		}
	}
	
	public void verwijderPijl()
	{	for (int i = aantalPu - 1; i > -1; i--)
		{	if (!pijlUit[i].actief && !pijlUit[i].vast)
			{	schuifveld.remove(pijlUit[i]);
				pijlUit[i] = null;
				for (int j = i; j < aantalPu - 1; j++)
				{	pijlUit[j] = pijlUit[j + 1];
				}
				aantalPu--;
				return;
			}
		}
	}
	
	public boolean meldAan(Pijl p, int x, int y)
	{	if (soort == 0)
		{	return false;
		}
		else if (soort == 1)
		{	Rectangle ingang1 = new Rectangle(0, 0, getSize().width, getSize().height);
			if (pijlIn1 == null && ingang1.contains(x - getLocation().x, y - getLocation().y))
			{	pijlIn1 = p;
				pijlIn1.zetEind(getLocation().x + 20, getLocation().y + 10);
				zetVeranderd(20);
				schuifveld.tekenOpnieuw();
				return true;
			}
		}
		else if (soort == 2)
		{	Rectangle ingang1 = new Rectangle(0, 0, getSize().width / 2, getSize().height);
			Rectangle ingang2 = new Rectangle(getSize().width / 2, 0, getSize().width / 2, getSize().height);
			if (pijlIn1 == null && ingang1.contains(x - getLocation().x, y - getLocation().y))
			{	pijlIn1 = p;
				pijlIn1.zetEind(getLocation().x + 10, getLocation().y + 10);
				zetVeranderd(20);
				schuifveld.tekenOpnieuw();
				return true;
			}
			else if (pijlIn2 == null && ingang2.contains(x - getLocation().x, y - getLocation().y))
			{	pijlIn2 = p;
				pijlIn2.zetEind(getLocation().x + 30, getLocation().y + 10);
				zetVeranderd(20);
				schuifveld.tekenOpnieuw();
				return true;
			}
		}
		return false;
	}
	
	public void verbind(Pijl p, boolean links)
	{	
		if (soort == 1)
		{	pijlIn1 = p;
			pijlIn1.zetEind(getLocation().x + 20, getLocation().y + 10);
		}
		else if (soort == 2)
		{	
			if (links)
			{	pijlIn1 = p;
				pijlIn1.zetEind(getLocation().x + 10, getLocation().y + 10);
			}
			else 
			{	pijlIn2 = p;
				pijlIn2.zetEind(getLocation().x + 30, getLocation().y + 10);
			}
		}
		
		
	}
	
	public void maakLos(Pijl p)
	{	if (p == pijlIn1)
		{	pijlIn1 = null;
		}
		else if (p == pijlIn2)
		{	pijlIn2 = null;
		}
	}
	
	public void setSize(int b, int h)
	{	super.setSize(b, h);
		
		for (int i = 0; i < aantalPu; i++)
		{	if (pijlUit[i].vast || pijlUit[i].actief)
			{	pijlUit[i].zetBegin(getLocation().x + getSize().width - 20 ,getLocation().y + getSize().height + 10);
			}
			else 
			{	pijlUit[i].zetPlaats(getLocation().x + getSize().width - 20 ,getLocation().y + getSize().height + 10);
			}
		}
	}
	
	public void zetMaat()
	{
	}
	
	public Expressie geefUitvoer(int max)
	{	return null;
	}

	public Expressie geefVerborgenUitvoer(int max)
	{	return null;
	}
	
	public void zetVeranderd(int max)
	{	for (int i = 0; i < aantalPu; i++)
		{	if (pijlUit[i].vast && max > 0)
			{	pijlUit[i].ontvanger.zetVeranderd(max - 1);
			}
		}
	}
	
	public void mousePressed(MouseEvent e)
	{	
		if (((AlgebraSchuifVeld) schuifveld).isDemo)
			return;		
		
		requestFocus();
		super.mousePressed(e);
	}
	
	public void mouseDragged(MouseEvent e)
	{	
		if (((AlgebraSchuifVeld) schuifveld).alleenInvullen)
			return;
		if (((AlgebraSchuifVeld) schuifveld).isDemo)
			return;		

		if ((this instanceof UitvoerSchuifComponent) &&
				(((UitvoerSchuifComponent) this).muisrechts)
			   )	
				return;
		
		if (isStapel)
		{	((AlgebraSchuifVeld) schuifveld).zetStapel(this);
			isStapel = false;
			schuifveld.tekenOpnieuw();
		}
		super.mouseDragged(e);
		
		int dx = e.getX() - startx;
		int dy =  e.getY() - starty;
		for (int i = 0; i < aantalPu; i++)
		{	if (pijlUit[i] != null)
				pijlUit[i].verplaatsBegin(dx, dy);
		}
		if (pijlIn1 != null)
			pijlIn1.verplaatsEind(dx, dy);
		if (pijlIn2 != null)
			pijlIn2.verplaatsEind(dx, dy);
	}
	
	public void mouseReleased(MouseEvent e)
	{	
		if (((AlgebraSchuifVeld) schuifveld).isDemo)
			return;		
		
		super.mouseReleased(e);
		if (!isStapel && (getLocation().x < 80 || getLocation().x > schuifveld.getSize().width
						 || getLocation().y < 0 || getLocation().y > schuifveld.getSize().height))
		{	((AlgebraSchuifVeld) schuifveld).verwijder(this);
		}

		
		schuifveld.tekenOpnieuw();
	}
}


