package fi.algebrapijlenopdr;

import java.awt.*;
import java.awt.event.*;
import fi.algebrapijlenopdr.schuifobjects.*;
import fi.algebrapijlenopdr.expressies_ap.*;
import java.util.Hashtable;

public class AlgebraSchuifComponent extends SchuifComponent 
{	
	 int soort;
	 Pijl pijlIn1, pijlIn2;
	 Pijl[] pijlUit;
	 int aantalPu;
	 boolean isStapel;
	 boolean open;
	 boolean links = false;
	 boolean label = false;
	
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
		boolean links = false;	
		
		isStapel = this.isStapel;
		links = this.links;
		
		Hashtable h = new Hashtable();
	    h.put("isStapel", new Boolean(isStapel));
	    h.put("links", new Boolean(links));
	    
	    return h;
	}

    public void setState(Hashtable h)
    {	boolean isStapel = ((Boolean) h.get("isStapel")).booleanValue();
		boolean links = ((Boolean) h.get("links")).booleanValue();
		
		this.isStapel = isStapel;
		this.links = links;
		
		//if(links)zetLinks(true);
		zetLinks(links);
    }
    
	public void zetLinks(boolean b)
	{	links = b;
		for(int i=0 ; i<aantalPu ; i++)
		{	pijlUit[i].zetLinks(b);
			if(!links)pijlUit[i].zetPlaats(getLocation().x + getSize().width+9 ,getLocation().y + 10 );
			else pijlUit[i].zetPlaats(getLocation().x - 10 ,getLocation().y + 10 );
		}
	}
	
	public void toonLabel(boolean b)
	{	label = b;
		if(b)setLocation(getLocation().x, getLocation().y - 20);
		else setLocation(getLocation().x, getLocation().y + 20);
	}
	
	public void zetVakKleur(Color color)
	{	
	}
	
	public void paint(Graphics gIm)
  	{ 	
		
		if (open)
		{	gIm.setColor(Color.gray);
			if (!links)
			{	if (label)
					gIm.fillOval(5,28,3,3);
				else 
					gIm.fillOval(5,8,3,3);
				
			}
			else 
			{	if (label)
					gIm.fillOval(5,28,3,3);
				else 
					gIm.fillOval(getSize().width-7,8,3,3);
			}
		}
		super.paint(gIm);
	}
	
	public boolean contains(int x, int y)
	{	if(new Rectangle(0,getSize().height-120,10,25).contains(x,y))return true;
		if(new Rectangle(0,getSize().height-70,10,25).contains(x,y))return true;
		if(!links)return (new Rectangle(10,0,getSize().width-10,getSize().height)).contains(x,y);
		else return (new Rectangle(0,0,getSize().width-10,getSize().height)).contains(x,y);
	}
	
	public void zetKettingZichtbaar(boolean b)
	{	if (pijlIn1 != null)
			pijlIn1.zender.zetKettingZichtbaar(b);
		setVisible(b);
		for (int i = 0; i < aantalPu ; i++)
		{	pijlUit[i].setVisible(b);
		}
		repaint();
	}
	
	public void voegPijlToe(Pijl p)
	{	if (aantalPu < 10)
		{	if (!links)
			{	if (label)
					p.zetPlaats(getLocation().x + getSize().width + 9, getLocation().y + 30);
				else 
					p.zetPlaats(getLocation().x + getSize().width + 9, getLocation().y + 10);
			}
			else 
			{	if (label) 
					p.zetPlaats(getLocation().x - 10, getLocation().y + 30);
				else 
					p.zetPlaats(getLocation().x - 10, getLocation().y + 10);
			}
			pijlUit[aantalPu] = p;
			//schuifveld.add(p,0);
			schuifveld.add(p);
			p.zetZender(this);
			aantalPu++;
		}
	}
	
	public void verwijderPijl()
	{	for(int i=aantalPu-1 ; i>-1 ; i--)
		{	if(!pijlUit[i].actief && !pijlUit[i].vast)
			{	schuifveld.remove(pijlUit[i]);
				pijlUit[i] = null;
				for(int j=i ; j<aantalPu-1 ; j++)
				{	pijlUit[j] = pijlUit[j+1];
				}
				aantalPu--;
				return;
			}
		}
	}
	
	public boolean meldAan(Pijl p, int x, int y)
	{	if(soort==0)
		{	return false;
		}
		//else if(soort==1)
		{	Rectangle ingang1;
			if(!links)ingang1 = new Rectangle(-10,0,getSize().width+10, getSize().height+5);
			else ingang1 = new Rectangle(0,0,getSize().width+10, getSize().height+5);
			if(pijlIn1==null && ingang1.contains(x-getLocation().x,y-getLocation().y))
			{	pijlIn1 = p;
				if(!links)
				{	if(label)pijlIn1.zetEind(getLocation().x  , getLocation().y+30);
					else pijlIn1.zetEind(getLocation().x  , getLocation().y+10);
				}
				else 
				{	if(label)pijlIn1.zetEind(getLocation().x + getSize().width  , getLocation().y+30);
					else pijlIn1.zetEind(getLocation().x + getSize().width  , getLocation().y+10);
				}
				zetVeranderd(20);
				schuifveld.tekenOpnieuw();
				return true;
			}
		}
		/*else if(soort==2)
		{	Rectangle ingang1 = new Rectangle(0,0,getSize().width/2, getSize().height);
			Rectangle ingang2 = new Rectangle(getSize().width/2,0,getSize().width/2, getSize().height);
			if(pijlIn1==null && ingang1.contains(x-getLocation().x,y-getLocation().y))
			{	pijlIn1 = p;
				pijlIn1.zetEind(getLocation().x+10 , getLocation().y+10);
				zetVeranderd(20);
				schuifveld.tekenOpnieuw();
				return true;
			}
			else if(pijlIn2==null && ingang2.contains(x-getLocation().x,y-getLocation().y))
			{	pijlIn2 = p;
				pijlIn2.zetEind(getLocation().x+30 , getLocation().y+10);
				zetVeranderd(20);
				schuifveld.tekenOpnieuw();
				return true;
			}
		}*/
		return false;
	}
	
	public void verbind(Pijl p)
	{	pijlIn1 = p;
		if(!links)
		{	if(label)pijlIn1.zetEind(getLocation().x  , getLocation().y+30);
			else pijlIn1.zetEind(getLocation().x , getLocation().y+10);
		}
		else 
		{	if(label)pijlIn1.zetEind(getLocation().x + getSize().width + 10 , getLocation().y+30);
			else pijlIn1.zetEind(getLocation().x + getSize().width + 10 , getLocation().y+10);
		}
	}
	
	public void maakLos(Pijl p)
	{	if(p==pijlIn1)
		{	pijlIn1 = null;
		}
		//else if(p==pijlIn2)
		//{	pijlIn2 = null;
		//}
	}
	
	public void setSize(int b, int h)
	{	super.setSize(b,h);
		
		for(int i=0 ; i<aantalPu ; i++)
		{	if(pijlUit[i].vast || pijlUit[i].actief)
			{	if(!links)
				{	//if(this instanceof UitvoerSchuifComponent && ((UitvoerSchuifComponent)this).isLabelZichtbaar())label = true;
					//else label = false;
					if(label)
					{	pijlUit[i].zetBegin(getLocation().x + getSize().width+10 ,getLocation().y + 30 );
					}
					else
					{	pijlUit[i].zetBegin(getLocation().x + getSize().width+10 ,getLocation().y + 10 );
					}
				}
				else 
				{	if(label)
					{	pijlUit[i].zetBegin(getLocation().x - 10 ,getLocation().y + 30 );
					}
					else
					{	pijlUit[i].zetBegin(getLocation().x - 10 ,getLocation().y + 10 );
					}
				}
			}
			else 
			{	if(!links)
				{	if(label)
					{	pijlUit[i].zetPlaats(getLocation().x + getSize().width+9 ,getLocation().y + 30 );
					}
					else
					{	pijlUit[i].zetPlaats(getLocation().x + getSize().width+9 ,getLocation().y + 10 );
					}
				}
				else 
				{	if(label)
					{	pijlUit[i].zetPlaats(getLocation().x -10 ,getLocation().y + 30 );
					}
					else
					{	pijlUit[i].zetPlaats(getLocation().x -10 ,getLocation().y + 10 );
					}
				}
			}
			if(pijlIn1!=null)
			{	if(!links)
				{	if(label)
					{	pijlIn1.zetEind(getLocation().x  , getLocation().y+30);
					}
					else
					{	pijlIn1.zetEind(getLocation().x  , getLocation().y+10);
					}
				}
				else 
				{	if(label)
					{	pijlIn1.zetEind(getLocation().x + getSize().width   , getLocation().y+30);
					}
					else
					{	pijlIn1.zetEind(getLocation().x + getSize().width  , getLocation().y+10);
					}
				}
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
	{	for(int i=0 ; i<aantalPu ; i++)
		{	if(pijlUit[i].vast && max>0)
			{	pijlUit[i].ontvanger.zetVeranderd(max-1);
			}
		}
	}
	
	public void mousePressed(MouseEvent e)
	{	if (((AlgebraSchuifVeld) schuifveld).fixed)
			return;
		if (((AlgebraSchuifVeld) schuifveld).alleenInvullen)
		{	
//System.out.println("pressed alleenInvullen return");
//			return;
		}
		if (((AlgebraSchuifVeld) schuifveld).isDemo)
			return;		
		requestFocus();
		super.mousePressed(e);
	}
	
	public void mouseDragged(MouseEvent e)
	{	if (((AlgebraSchuifVeld) schuifveld).fixed)
			return;
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
		
		schuifveld.tekenOpnieuw();		
	}
	
	public void mouseReleased(MouseEvent e)
	{	if (((AlgebraSchuifVeld) schuifveld).fixed)
			return;
//		if (((AlgebraSchuifVeld) schuifveld).alleenInvullen)
//			return;
		if (((AlgebraSchuifVeld) schuifveld).isDemo)
			return;		

		super.mouseReleased(e);
		if (!isStapel && (getLocation().x < 80 || getLocation().x > schuifveld.getSize().width
						|| getLocation().y < 0 || getLocation().y > schuifveld.getSize().height))
		{	((AlgebraSchuifVeld)schuifveld).verwijder(this);
		}
		
// check dit!!
		
		if (!isStapel && this instanceof UitvoerSchuifComponent)
		{	boolean tabelNodig = ((AlgebraSchuifVeld) schuifveld).tabelCheckbox.isSelected();
			if (tabelNodig && !((UitvoerSchuifComponent) this).tabelZichtbaar)
				((UitvoerSchuifComponent) this).zetTabelAan(true);
		}
		
		schuifveld.tekenOpnieuw();
	}
}


