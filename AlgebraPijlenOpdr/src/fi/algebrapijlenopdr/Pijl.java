package fi.algebrapijlenopdr;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import fi.algebrapijlenopdr.schuifobjects.*;

public class Pijl extends JComponent 
				  implements MouseListener, MouseMotionListener
{	
	int x0,y0,x1,y1;
	AlgebraSchuifVeld schuifveld;
	AlgebraSchuifComponent zender, ontvanger;
	Polygon pijlpuntBegin, pijlpuntEind;
	Polygon pijlpuntKlik;
	private int laatstex = 0;
	private int laatstey = 0;
	boolean actief;
	boolean vast;
	private boolean isStapel;
	private boolean links = false;
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
	
	public void zetLinks(boolean b)
	{	links = b;
	}
	
	public void setColor(Color color)
	{	this.color = color;
		zender.zetVakKleur(color);
	}
	
	public Color getColor()
	{	return color;
	}
	
	public void paint(Graphics gIm)
  	{ 	
		//gIm.setClip(Math.min(x0,x1)-7,Math.min(y0,y1)-20,Math.abs(x1-x0)+15,Math.abs(y1-y0)+41);
		// pijl naar rechts, alleen deze kan je aan de grafiek vastmaken
		if(!links)
		{	gIm.setColor(color);
			double dx = x1-x0; double dy = y1-y0;
			int teken = (int)((dy/Math.abs(dy)));
			double s = Math.sqrt(dx*dx + dy*dy);
			double a;
			int r0, r1;
			double dr;
			int xc0,xc1,yc0,yc1, booghoek;
			if(Math.abs((double)dy/(double)dx)>0.04 && dx>=0)
			{	a = Math.atan((double)dx/Math.abs(dy));
				r0 = (int)(s/(4*Math.cos(a)));
				dr = s/(4*Math.cos(a)) - r0;
				if(dr>0.25)r1 = r0+1;
				else r1 = r0;
				xc0 = x0;
				yc0 = y0+r0*teken;
				xc1 = x1;
				yc1 = y1-r1*teken;
				booghoek = (int)((2*a - Math.PI)*180/Math.PI);
				if (color == Color.black)
					gIm.drawArc(xc0-r0, yc0-r0, 2*r0, 2*r0, teken*90, teken*(booghoek-1));
				if (color == Color.black)
					gIm.drawArc(xc1-r1, yc1-r1, 2*r1, 2*r1, teken*270, teken*(booghoek-1));
			}
			else if(Math.abs(dy)>1 && dx<0)
			{	r0 = (int)Math.abs(dy/4);
				dr = Math.abs(dy/4) - r0;
				if(dr>0.25)r1 = r0+1;
				else r1 = r0;
				xc0 = x0;
				yc0 = y0+r0*teken;
				xc1 = x1;
				yc1 = y1-r1*teken;
				booghoek = -180;
				if (color == Color.black)
					gIm.drawArc(xc0-r0, yc0-r0, 2*r0, 2*r0, teken*90, teken*(booghoek-1));
				if (color == Color.black)
					gIm.drawArc(xc1-r1, yc1-r1, 2*r1, 2*r1, teken*270, teken*(booghoek-1));
				if (color == Color.black)
					gIm.drawLine(x0,yc0+teken*r0,x1,yc1-teken*r1);
			}
			else
			{	if (color == Color.black)
					gIm.drawLine(x0, y0, x1, y1);
			}
			if (vast)
				gIm.setColor(color);
			else 
				gIm.setColor(Color.gray);
		
			pijlpuntBegin = new Polygon();
			pijlpuntBegin.addPoint(x0, y0);
			pijlpuntBegin.addPoint(x0-10, y0-7);
			pijlpuntBegin.addPoint(x0-10, y0+7);
			gIm.fillPolygon(pijlpuntBegin);
			if(color!=Color.black)
			{	//gIm.fillRect(x0-10, y0-10,20,20);
				//gIm.drawString(AlgebraPijlenOpdr.rb.getString("grafiekLabel"),x0+12,y0);
			}
		
			pijlpuntEind = new Polygon();
			pijlpuntEind.addPoint(x1+10, y1);
			pijlpuntEind.addPoint(x1, y1-7);
			pijlpuntEind.addPoint(x1, y1+7);
			gIm.fillPolygon(pijlpuntEind);
		
			pijlpuntKlik = new Polygon();
			pijlpuntKlik.addPoint(x1+15, y1);
			pijlpuntKlik.addPoint(x1-2, y1-13);
			pijlpuntKlik.addPoint(x1-2, y1+13);
		
			if (vast) 
				gIm.setColor(color);
			else 
				gIm.setColor(Color.gray);
			gIm.fillPolygon(pijlpuntBegin);
			gIm.fillPolygon(pijlpuntEind);
			gIm.setColor(Color.black);
			gIm.drawPolygon(pijlpuntBegin);
			gIm.drawPolygon(pijlpuntEind);
			
			if (!isStapel && !vast && !actief && (im != null))
			{	gIm.drawImage(im, x0, y0, this);
//System.out.println("im rechts");			
			}
		}
		else // pijl naar links
		{	gIm.setColor(Color.black);
			double dx = x0-x1; double dy = y0-y1;
			int teken = (int)((dy/Math.abs(dy)));
			double s = Math.sqrt(dx*dx + dy*dy);
			double a;
			int r0, r1;
			double dr;
			int xc0,xc1,yc0,yc1, booghoek;
			if(Math.abs((double)dy/(double)dx)>0.04 && dx>=0)
			{	a = Math.atan((double)dx/Math.abs(dy));
				r0 = (int)(s/(4*Math.cos(a)));
				dr = s/(4*Math.cos(a)) - r0;
				if(dr>0.25)r1 = r0+1;
				else r1 = r0;
				xc0 = x1;
				yc0 = y1+r0*teken;
				xc1 = x0;
				yc1 = y0-r1*teken;
				booghoek = (int)((2*a - Math.PI)*180/Math.PI);
				gIm.drawArc(xc0-r0, yc0-r0, 2*r0, 2*r0, teken*90, teken*(booghoek-1));
				gIm.drawArc(xc1-r1, yc1-r1, 2*r1, 2*r1, teken*270, teken*(booghoek-1));
			}
			else if(Math.abs(dy)>1 && dx<0)
			{	r0 = (int)Math.abs(dy/4);
				dr = Math.abs(dy/4) - r0;
				if(dr>0.25)r1 = r0+1;
				else r1 = r0;
				xc0 = x1;
				yc0 = y1+r0*teken;
				xc1 = x0;
				yc1 = y0-r1*teken;
				booghoek = -180;
				gIm.drawArc(xc0-r0, yc0-r0, 2*r0, 2*r0, teken*90, teken*(booghoek-1));
				gIm.drawArc(xc1-r1, yc1-r1, 2*r1, 2*r1, teken*270, teken*(booghoek-1));
				gIm.drawLine(x1,yc0+teken*r0,x0,yc1-teken*r1);
			}
			else
			{	gIm.drawLine(x0,y0,x1,y1);
			}
			if(vast)gIm.setColor(Color.red);
			else gIm.setColor(Color.gray);
		
			pijlpuntBegin = new Polygon();
			pijlpuntBegin.addPoint(x0, y0);
			pijlpuntBegin.addPoint(x0+10, y0-7);
			pijlpuntBegin.addPoint(x0+10, y0+7);
			gIm.fillPolygon(pijlpuntBegin);
		
			pijlpuntEind = new Polygon();
			pijlpuntEind.addPoint(x1-10, y1);
			pijlpuntEind.addPoint(x1, y1-7);
			pijlpuntEind.addPoint(x1, y1+7);
			gIm.fillPolygon(pijlpuntEind);
		
			pijlpuntKlik = new Polygon();
			pijlpuntKlik.addPoint(x1-15, y1);
			pijlpuntKlik.addPoint(x1+2, y1-13);
			pijlpuntKlik.addPoint(x1+2, y1+13);
		
			if(vast)
				gIm.setColor(Color.black);
			else 
				gIm.setColor(Color.gray);
			gIm.fillPolygon(pijlpuntBegin);
			gIm.fillPolygon(pijlpuntEind);
			gIm.setColor(Color.black);
			gIm.drawPolygon(pijlpuntBegin);
			gIm.drawPolygon(pijlpuntEind);
			
			if (!isStapel && !vast && !actief && (im != null))
			{	gIm.drawImage(im,x0,y0, this);
//System.out.println("im links");			
			}
		}	
			
		
		
		
		
	}
	
	public void update(Graphics g)
	{	paint(g);
	}
	
	public boolean contains(int x, int y)
	{	return pijlpuntKlik.contains(x,y);
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
		if(ontvanger==null)
		{	if(!links)x1 = x - 10;
	        else x1 = x + 10;
			y1 = y;
		}
	}
	public void zetEind(int x, int y)
	{	//if(!links)x1 = x - 10;
        //else 
        x1 = x;// + 10;
		y1 = y;
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
		
		if (ontvanger != null && ontvanger.pijlUit != null && ontvanger.pijlUit[0] != null)
			ontvanger.pijlUit[0].im = null;
	
		ontvanger = null;
		if(!links)x1 = x0 - 10;
        else x1 = x0 + 10;
		y1 = y0;
	}
	public void zetVerbonden(AlgebraSchuifComponent asc)
	{	vast = true;
		ontvanger = asc;
		Pijl p = new Pijl(schuifveld);
		p.zetLinks(links);
		zender.voegPijlToe(p);		
	}
	public void mousePressed(MouseEvent e)
	{	if (schuifveld.fixed)
			return;
		if (schuifveld.alleenInvullen)
			return;
		if (schuifveld.isDemo)
			return;
		if (schuifveld.frozen)
			return;
		

		getParent().setComponentZOrder(this, 0);
		
		schuifveld.start();
//tijdelijk		
		//schuifveld.zetOpSchuifLaag(this);
		requestFocus();
		vast = false;
		actief = true;
		if (ontvanger != null)
		{	ontvanger.maakLos(this);
			ontvanger.zetVeranderd(20);
		}
		zender.verwijderPijl();
		laatstex = e.getX();
		laatstey = e.getY();
		
		im = null;
		
		schuifveld.tekenOpnieuw();
	}	
	
	public void mouseDragged(MouseEvent e)
	{	if(schuifveld.fixed)
			return;
		if (schuifveld.alleenInvullen)
			return;
		if (schuifveld.isDemo)
			return;
		if (schuifveld.frozen)
			return;
	
	
		if (isStapel) return;
		if(actief)
		{	int dx = e.getX() - laatstex;
			int dy =  e.getY() - laatstey;
			x1 = x1 + dx;
			y1 = y1 + dy;
			repaint();
			laatstex = e.getX();
			laatstey = e.getY();
		}
		
		schuifveld.tekenOpnieuw();
	}
	
	public void mouseReleased(MouseEvent e)
	{	if (schuifveld.fixed)
			return;
		if (schuifveld.alleenInvullen)
			return;
		if (schuifveld.isDemo)
			return;
		if (schuifveld.frozen)
			return;
	
		schuifveld.changed = true;
		
		plaatsOpGridEind();
		for (int i = 0 ; i < schuifveld.aantalSc; i++)
		{	boolean b = false;
			if (schuifveld.schuifcomponenten[i].isVisible() && 
				!schuifveld.schuifcomponenten[i].isStapel && 
				!zender.isStapel && schuifveld.schuifcomponenten[i].links == links)
			{	if (!links)
					b = schuifveld.schuifcomponenten[i].meldAan(this, x1 + 10, y1);
				else 
					b = schuifveld.schuifcomponenten[i].meldAan(this, x1 - 10, y1);
			}
			if(b)
			{	 vast = true;
				 ontvanger = schuifveld.schuifcomponenten[i];
				 Pijl p = new Pijl(schuifveld);
				 p.zetLinks(links);
				 zender.voegPijlToe(p);				
				 actief = false;
//tijdelijk				 
				 //schuifveld.zetTerugSchuifLaag(this);
				 return;
			}
		}
		if (actief) 
			pijlTerug();
		actief = false;
//tijdelijk		
		//schuifveld.zetTerugSchuifLaag(this);
		
		schuifveld.tekenOpnieuw();
	}
	
	public void mouseMoved(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
}

