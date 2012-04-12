package fi.algebraexpressies;

import java.awt.*;
import java.awt.event.*;
import fi.algebraexpressies.schuifobjects.*;

import javax.swing.*;

public class Pijl extends JComponent //Component 
				  implements MouseListener, MouseMotionListener
{	
	int x0,y0,x1,y1;
	AlgebraSchuifVeld schuifveld;
	AlgebraSchuifComponent zender, ontvanger;
	Polygon pijlpuntBegin, pijlpuntEind;
	Polygon pijlpuntKlik;
	private int laatstex = 0;
	private int laatstey = 0;
	boolean actief, vast;
	private boolean isStapel;
	
	private Color color = Color.black;	
	
	Image im;
	
	public Pijl(AlgebraSchuifVeld asv)
	{	schuifveld = asv;
		actief = false;
		vast = false;
		isStapel = false;
		setBounds(0, 0, schuifveld.getSize().width, schuifveld.getSize().height);
		addMouseListener(this);
		addMouseMotionListener(this);
		pijlpuntEind = new Polygon();
		pijlpuntKlik = new Polygon();
		
		setOpaque(false);
		
	}
	
	public void setColor(Color color)
	{	this.color = color;
		zender.zetVakKleur(color);
	}

	public Color getColor()
	{	return color;
	}
	
	public void paint(Graphics gIm)
	{ 	//gIm.setClip(Math.min(x0,x1)-7,Math.min(y0,y1)-20,Math.abs(x1-x0)+15,Math.abs(y1-y0)+41);
		//gIm.setColor(Color.black);
		gIm.setColor(color);
		double dx = x1-x0; double dy = y1-y0;
		if (dx!=0 || dy!=0)
		{	double s = Math.sqrt(dx*dx + dy*dy);
			double alpha,beta;
			int rmax = 20; 
			int r;
		
			int xb0,xb1,yb0,yb1, booghoek;
		
			beta = Math.atan((double)dy/Math.abs(dx));
			r = (int)(s/(4*Math.cos(beta)));
			int teken = (int)((dx/Math.abs(dx)));
			if(r < rmax)
				rmax = r;
			
			
			if((dx<2*rmax && dx>-2*rmax) && dy>=0)
			{	double h = 2*Math.sqrt(rmax*rmax - (rmax - teken*dx/2)*(rmax - teken*dx/2));
				alpha = Math.atan(h/Math.abs(dx));
				xb0 = x0 - rmax + teken*rmax;
				yb0 = y0 - rmax;
				xb1 = x1 - rmax - teken*rmax;
				yb1 = y0 + (int)h - rmax;
				booghoek = (int)((Math.PI - 2*alpha)*180/Math.PI);
				if (color == Color.black)
				{	gIm.drawArc(xb0, yb0, 2*rmax, 2*rmax, 90+teken*90, teken*(booghoek+2));
					gIm.drawArc(xb1, yb1, 2*rmax, 2*rmax, -90+teken*90, teken*(booghoek+2));
					gIm.drawLine(x1, y0+(int)h, x1,y1);
				}	
			}
			else if(dy>=2*rmax && dy>=0)
			{	double h = 2*Math.sqrt(rmax*rmax - (rmax - teken*dx/2)*(rmax - teken*dx/2));
				alpha = Math.atan(h/Math.abs(dx));
				xb0 = x0 - rmax + teken*rmax;
				yb0 = y0 - rmax;
				xb1 = x1 - rmax - teken*rmax;
				yb1 = y0 + rmax;
				booghoek = 90;
				if (color == Color.black)
				{	
					gIm.drawArc(xb0, yb0, 2*rmax, 2*rmax, 90+teken*90, teken*(booghoek+2));
					gIm.drawArc(xb1, yb1, 2*rmax, 2*rmax, -90+teken*90, teken*(booghoek+2));
					gIm.drawLine(x1, y0+2*rmax, x1,y1);
					gIm.drawLine(x0 + teken*rmax, y0 + rmax, x1 - teken*rmax,y0 + rmax);
				}	
			}
			else if(dy<2*rmax  && dy>=0)
			{	double b = 2*rmax + 2*Math.sqrt(rmax*rmax - (dy/2)*(dy/2));
				alpha = Math.atan(Math.abs(dy)/b);
				xb0 = x0 - rmax + teken*rmax;
				yb0 = y0 - rmax;
				xb1 = x1 - rmax - teken*rmax;
				yb1 = y1 - rmax;
				booghoek = (int)((Math.PI - 2*alpha)*180/Math.PI);
				if (color == Color.black)
				{
					gIm.drawArc(xb0, yb0, 2*rmax, 2*rmax, 90+teken*90, teken*(booghoek+2));
					gIm.drawArc(xb0+ teken*(int)b-2*teken*rmax, yb1, 2*rmax, 2*rmax, 90, teken*(booghoek-90+2));
					gIm.drawArc(xb1, yb1, 2*rmax, 2*rmax, -90+teken*90, teken*(92));
					gIm.drawLine(x0 + teken*(int)b - teken*rmax, y1 - rmax, x1-teken*rmax,y1 - rmax);
				}	
			}
			else
			{	if(Math.abs(dx/4) < rmax)rmax = Math.abs((int)dx/4);
				xb0 = x0 - rmax + teken*rmax;
				yb0 = y0 - rmax;
				xb1 = x1 - rmax - teken*rmax;
				yb1 = y1 - rmax;
				if (color == Color.black)
				{	
					gIm.drawArc(xb0, yb0, 2*rmax, 2*rmax, 90+teken*90, teken*182);
					gIm.drawArc(xb0+ 2*teken*rmax, yb1, 2*rmax, 2*rmax, 90, teken*92);
					gIm.drawArc(xb1, yb1, 2*rmax, 2*rmax, -90+teken*90, teken*(92));
					gIm.drawLine(xb0 + rmax + teken*rmax , y0 , xb0 + rmax + teken*rmax ,y1 );
					gIm.drawLine(x0 + 3*teken*rmax , y1-rmax , x1 - teken*rmax, y1-rmax );
				}
			}
		}

		pijlpuntBegin = new Polygon();
		pijlpuntBegin.addPoint(x0, y0);
		pijlpuntBegin.addPoint(x0-7, y0-10);
		pijlpuntBegin.addPoint(x0+7, y0-10);
		
		pijlpuntEind = new Polygon();
		pijlpuntEind.addPoint(x1, y1+10);
		pijlpuntEind.addPoint(x1-7, y1);
		pijlpuntEind.addPoint(x1+7, y1);
		
		if (vast)
			gIm.setColor(color);
		else 
			gIm.setColor(Color.gray);
		gIm.fillPolygon(pijlpuntBegin);
		
		if (actief || vast)
			gIm.fillPolygon(pijlpuntEind);
		
		gIm.setColor(Color.black);
		gIm.drawPolygon(pijlpuntBegin);
		if (actief || vast)
			gIm.drawPolygon(pijlpuntEind);
		
		if (!isStapel && !vast && !actief && (im != null))
		{	gIm.drawImage(im, x0, y0, this);
//System.out.println("im");		
		}
		
	}
	public void update(Graphics g)
	{	paint(g);
	}
	public boolean contains(int x, int y)
	{	Polygon pijlpuntRaak = new Polygon();
		pijlpuntRaak.addPoint(x1, y1+15);
		pijlpuntRaak.addPoint(x1-12, y1-3);
		pijlpuntRaak.addPoint(x1+12, y1-3);
		return pijlpuntRaak.contains(x,y);
	}
	public void zetStapel(boolean b)
	{	isStapel = b;
	}
	public void zetZender(AlgebraSchuifComponent r)
	{	zender = r;
	}
	public void zetPlaats(int x, int y)
	{	x0 = x;
		y0 = y;
		x1 = x;
		y1 = y-10;
	}
	public void zetEind(int x, int y)
	{	x1 = x;
		y1 = y-10;
	}
	public void zetBegin(int x, int y)
	{	x0 = x;
		y0 = y;
	}
	public void verplaatsBegin(int dx, int dy)
	{	x0 = x0 + dx;
		y0 = y0 + dy;
		if(!vast)
		{	x1 = x1 + dx;
			y1 = y1 + dy;
		}
		repaint();
	}
	public void verplaatsEind(int dx, int dy)
	{	x1 = x1 + dx;
		y1 = y1 + dy;
		repaint();
	}
	public void plaatsOpGridBegin()
	{	int x;
		int y;
		x = x0+300;
		y = y0+300;
		int ex = x%10;
		int ey = y%10;
		if(ex<5)verplaatsBegin(-ex,0);
		else verplaatsBegin(10-ex,0);
		if(ey<5)verplaatsBegin(0,-ey);
		else verplaatsBegin(0,10-ey);
	}
	public void plaatsOpGridEind()
	{	int x;
		int y;
		x = x1+300;
		y = y1+300;
		int ex = x%10;
		int ey = y%10;
		if(ex<5)verplaatsEind(-ex,0);
		else verplaatsEind(10-ex,0);
		if(ey<5)verplaatsEind(0,-ey);
		else verplaatsEind(0,10-ey);
	}
	public void verplaats(int dx,int dy)
	{	if(!actief)
		{	x0 = x0 + dx;
			y0 = y0 + dy;
		}
		else
		{	x1 = x1 + dx;
			y1 = y1 + dy;
		}
		repaint();
	}
	public void pijlTerug()
	{	vast = false;
		ontvanger = null;
		x1 = x0;
		y1 = y0-10;
	}
	
	public void zetVerbonden(AlgebraSchuifComponent asc)
	{	vast = true;
		ontvanger = asc;
		Pijl p = new Pijl(schuifveld);
		zender.voegPijlToe(p);		
	}
	
	public void mousePressed(MouseEvent e)
	{	
		
		if (schuifveld.alleenInvullen)
			return;
		if (schuifveld.isDemo)
			return;
		if (schuifveld.frozen)
			return;
		
		
		
		schuifveld.start();
		schuifveld.zetOpSchuifLaag(this);
		requestFocus();
		vast = false;
		actief = true;
		if(ontvanger!=null )
		{	ontvanger.maakLos(this);
			ontvanger.zetVeranderd(20);
		}
		zender.verwijderPijl();
		laatstex = e.getX();
		laatstey = e.getY();
	}	
	
	public void mouseDragged(MouseEvent e)
	{	
		if (schuifveld.alleenInvullen)
			return;
		if (schuifveld.isDemo)
			return;
		if (schuifveld.frozen)
			return;
		
		if(zender.isStapel)return;
		if(actief)
		{	int dx = e.getX() - laatstex;
			int dy =  e.getY() - laatstey;
			x1 = x1 + dx;
			y1 = y1 + dy;
			repaint();
			laatstex = e.getX();
			laatstey = e.getY();
		}
	}
	
	public void mouseReleased(MouseEvent e)
	{	
		if (schuifveld.alleenInvullen)
			return;
		if (schuifveld.isDemo)
			return;
		if (schuifveld.frozen)
			return;
		
		plaatsOpGridEind();
		for (int i = 0; i < schuifveld.aantalSc; i++)
		{	boolean b = schuifveld.schuifcomponenten[i].meldAan(this,x1,y1+5);
			if(b)
			{	 vast = true;
				 ontvanger = schuifveld.schuifcomponenten[i];
				 zender.voegPijlToe(new Pijl(schuifveld));				
				 actief = false;
				 schuifveld.zetTerugSchuifLaag(this);
				 return;
			}
		}
		if (actief)
			pijlTerug();
		actief = false;
		schuifveld.zetTerugSchuifLaag(this);
	}
	public void mouseMoved(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
}

