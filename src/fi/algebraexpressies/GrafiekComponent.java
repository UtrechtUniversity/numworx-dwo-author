package fi.algebraexpressies;

import java.awt.Polygon;
import java.awt.*;
import java.awt.event.*;
import fi.algebraexpressies.expressies.*;
import fi.algebraexpressies.schuifobjects.*;


public class GrafiekComponent extends AlgebraSchuifComponent implements ActionListener, MouseListener, MouseMotionListener
{	
	private PlusMinKnop pmKnopY,pmKnopX; 
	private Polygon pijlPlus, pijlMin;
	 
	Pijl[] pijlenIn;
	int aantalPijlenIn;
	
	 private Expressie[] expressies;
	 private int maxAantalExpressies;
	 private int aantalExpressies;
	 
	 private boolean gevuld;
	 private int beginwaarde;
	 private int selectnummer;
	 private String varNaam;
	 private String formuleNaam;
	 private int xmin, xmax, ymin, ymax;
	 private double beginx, beginy;
	 private int veldx, veldy, veldb, veldh;
	 private int eenheid;
	 private double schaalFactorY;
	 private int factorRijNummerY;
	 private double schaalFactorX;
	 private int factorRijNummerX;
	 
	 int startxv = 0;
	 int startyv = 0;
	 
	 private GrafiekVeld gv;
	 
	
	public GrafiekComponent(SchuifVeld sv,int x, int y, int b, int h)
	{	
		super(1,sv,x,y,b,h);
		maxAantalExpressies = 10;
		isStapel = false;
		expressies = new Expressie[maxAantalExpressies];
		aantalExpressies = 0;
		
		pijlenIn = new Pijl[maxAantalExpressies];
		aantalPijlenIn = 0;
		beginwaarde = 0;
		selectnummer = 999;
		xmin = 0; 
		ymin = 0;
		xmax = 10;
		ymax = 10;
		eenheid = 20;
		beginx = eenheid;
		beginy = eenheid;
		veldx = 30;
		veldy = 40;
		veldb = 160;
		veldh = 160;
		schaalFactorY = 1;
		factorRijNummerY = 99;
		schaalFactorX = 1;
		factorRijNummerX = 99;
		varNaam = "";
		formuleNaam = "";
		
		gv = new GrafiekVeld(veldx,veldy,veldb,veldh);
		gv.addMouseListener(this);
		gv.addMouseMotionListener(this);
		
		add(gv);
		
		pmKnopX = new PlusMinKnop(20,h-18,24,12, 1);
		pmKnopX.addActionListener(this);
		add(pmKnopX);
		
		pmKnopY = new PlusMinKnop(5,h-40,12,24, 0);
		pmKnopY.addActionListener(this);
		add(pmKnopY);
	}
	
	public void paint(Graphics g)
	{	
		int breedte = getSize().width;
		int hoogte = getSize().height;
		g.setColor(new Color(220,220,220));
		g.fillRect(0,10,breedte-1,hoogte - 11);
		g.setColor(Color.black);
		g.drawRect(0,10,breedte-1,hoogte - 11);
		g.setColor(Color.white);
		g.fillRect(veldx-1,veldy-1,veldb+1,veldh+1);
		g.setColor(Color.black);
		g.drawRect(veldx-1,veldy-1,veldb+1,veldh+1);
		
		FontMetrics fm = g.getFontMetrics();
		int woordbreedte = fm.stringWidth(varNaam);
		g.drawString(varNaam, breedte-13-woordbreedte,hoogte-3);
		g.drawString(formuleNaam,10,30);
		
		/*if(exp!=null && !exp.geefVarNaam().equals("") && selectnummer>-1 && selectnummer<11)
		{	g.setColor(new Color(255,200,200));
			g.fillRect(veldx+(selectnummer+1)*eenheid-eenheid/2,veldy+veldh,eenheid,eenheid);
			g.setColor(Color.black);
			g.drawRect(veldx+(selectnummer+1)*eenheid-eenheid/2,veldy+veldh,eenheid,eenheid);
		}*/
		if(aantalPijlenIn>0 && expressies[0]!=null && !expressies[0].geefVarNaam().equals("") && selectnummer>-1 && selectnummer<11)
		{	g.setColor(new Color(255,200,200));
			g.fillRect(veldx+(selectnummer+1)*eenheid-eenheid/2,veldy+veldh,eenheid,eenheid);
			g.setColor(Color.black);
			g.drawRect(veldx+(selectnummer+1)*eenheid-eenheid/2,veldy+veldh,eenheid,eenheid);
		}
		
		int imin = -(int)Math.round(beginx/eenheid); 
		int imax = 1+veldb/eenheid-(int)Math.round(beginx/eenheid);
		int jmin = -(int)Math.round(beginy/eenheid); 
		int jmax = 1+veldb/eenheid-(int)Math.round(beginy/eenheid);
		int bx = (int)beginx;
		int by = (int)beginy;
		
		for(int i=1 ; i<1+veldb/eenheid ; i++)
		{	new Expressie();
			String getal = Expressie.df.format(schaalFactorX*(imin+i));
			woordbreedte = fm.stringWidth(getal);
			if(schaalFactorX>0.5 && schaalFactorX<5)g.drawString(getal,veldx+i*eenheid-woordbreedte/2,veldy+veldh+13);
			else if((i-beginwaarde-1)%2==0)g.drawString(getal,veldx+i*eenheid-woordbreedte/2,veldy+veldh+13);
		}
		for(int j=1 ; j<1+veldh/eenheid ; j++)
		{	String getal = Expressie.df.format(schaalFactorY*(jmin+j));
			woordbreedte = fm.stringWidth(getal);
			g.drawString(getal,veldx-3-woordbreedte,veldy+veldh+5-(j*eenheid));
		}
		super.paint(g);	
	}	
		

	/*public void zetExpressie(Expressie e)
	{	if(e!=null && e.geefWaarde()==null )
		{	exp = e;
			varNaam = e.geefVarNaam();
		}
		else 
		{	exp = null;
			varNaam = "";
			formuleNaam = "";
		}
		gv.tekenOpnieuw();
	}*/
	
	public void zetExpressie(int nr,Expressie e)
	{	Expressie exp = null;
		if(e!=null && e.geefWaarde()==null )
		{	exp = e;
			if(varNaam.equals(""))varNaam = e.geefVarNaam();
		}
		else exp = null;
		expressies[nr] = exp;
		gv.tekenOpnieuw();
	}
	
	public void zetTabel(int beginwaarde, int selectnummer, String varN, double schaalFactorX)
	{	//if(varNaam.equals(varN))
		{	this.beginwaarde = beginwaarde;
			beginx = eenheid-eenheid*beginwaarde;
			this.selectnummer = selectnummer;
			this.schaalFactorX = schaalFactorX;
			gv.tekenOpnieuw();
			UitvoerSchuifComponent usc = null;
			for(int i=0 ; i<aantalPijlenIn ; i++)
			{	usc = (UitvoerSchuifComponent)pijlenIn[i].zender;
				usc.zetTabel(beginwaarde,selectnummer, varNaam, schaalFactorX);
			}
		}
	}
	
	/*public void zetVeranderd(int max)
	{	if(pijlIn1!=null)
		{	exp = pijlIn1.zender.geefUitvoer(20);
			formuleNaam = ((UitvoerSchuifComponent)pijlIn1.zender).geefLabelTekst();
		}
		else exp = null;
		zetExpressie(exp);
		
		super.zetVeranderd(max);
	}*/
	public void zetVeranderd(int max)
	{	
		for(int i=0 ; i<aantalPijlenIn ; i++)
		{	Expressie e = pijlenIn[i].zender.geefUitvoer(20);
			zetExpressie(i,e);
			formuleNaam = ((UitvoerSchuifComponent)pijlenIn[i].zender).geefLabelTekst();
			
		}
		super.zetVeranderd(max);
	}
	
	/*public boolean meldAan(Pijl p, int x, int y)
	{	if(!(p.zender instanceof UitvoerSchuifComponent))return false;
		if(super.meldAan(p,x,y))
		{	((UitvoerSchuifComponent)p.zender).zetGrafiek(true,this);
			((UitvoerSchuifComponent)p.zender).zetTabel(beginwaarde,selectnummer, varNaam, schaalFactorX);
			
			return true;
		}
		else return false;
	}*/
	public boolean meldAan(Pijl p, int x, int y)
	{	if(!(p.zender instanceof UitvoerSchuifComponent))return false;
		if(p.zender.geefUitvoer(20)!=null && !varNaam.equals("") && !varNaam.equals(p.zender.geefUitvoer(20).geefVarNaam()))return false;
		Rectangle ingang = new Rectangle(0,-10,getSize().width, getSize().height+10);
		if(aantalPijlenIn <10 && ingang.contains(x-getLocation().x,y-getLocation().y))
		{	p.zetEind(getLocation().x+10+aantalPijlenIn*15 , getLocation().y+10);
			pijlenIn[aantalPijlenIn] = p;
			aantalPijlenIn++;
			((UitvoerSchuifComponent)p.zender).zetGrafiek(true,this);
			((UitvoerSchuifComponent)p.zender).zetTabel(beginwaarde,selectnummer, varNaam, schaalFactorX);
			zetVeranderd(20);
			schuifveld.tekenOpnieuw();
			return true;
		}
		return false;
	}
	
	/*public void maakLos(Pijl p)
	{	((UitvoerSchuifComponent)p.zender).zetGrafiek(false,this);
		super.maakLos(p);
	}*/
	public void maakLos(Pijl p)
	{	for(int i=0 ; i<aantalPijlenIn ; i++)
		{	if(p==pijlenIn[i])
			{	for(int j=i ; j<aantalPijlenIn-1 ; j++)
				{	pijlenIn[j] = pijlenIn[j+1];
					pijlenIn[j].zetEind(getLocation().x+10+j*15 , getLocation().y+10);
					expressies[j] = expressies[j+1];
				}
				aantalPijlenIn--;
				break;
			}
		}
		//((UitvoerSchuifComponent)p.zender).zetGrafiek(false,this);
		selectnummer = 999;
		if(aantalPijlenIn==0)
		{	//((AlgebraSchuifVeld)getParent()).zetTabellen(0,selectnummer, varNaam, 1);
			zetTabel(0,selectnummer, varNaam, 1);
			varNaam = "";
			formuleNaam = "";
			beginy = eenheid;
		}
	}
	
	public void mousePressed(MouseEvent e)
	{	if(e.getSource()==gv)
		{	startxv = e.getX();
			startyv = e.getY();
		}
		super.mousePressed(e);
	}	
	
	public void mouseDragged(MouseEvent e)
	{	if(e.getSource()==gv)
		{	int dx = e.getX() - startxv;
			int dy =  e.getY() - startyv;
		
			beginx = beginx+dx;
			beginy = beginy-dy;
			int b = beginwaarde;
			beginwaarde = 1-(int)Math.round(beginx/eenheid);
			selectnummer = selectnummer + b - beginwaarde;
			gv.tekenOpnieuw();
		
			startxv = e.getX();
			startyv = e.getY();
		}
		else
		{	super.mouseDragged(e);
			int dx = e.getX() - startx;
			int dy =  e.getY() - starty;
			for(int i=0 ; i<aantalPijlenIn ; i++)
			{	pijlenIn[i].verplaatsEind(dx,dy);
			}
			//schuifveld.tekenOpnieuw();
		}
	}
	
	/*public void mouseReleased(MouseEvent e)
	{	if(e.getSource()==gv)
		{	beginx = eenheid*Math.round(beginx/eenheid);
			beginy = eenheid*Math.round(beginy/eenheid);

			UitvoerSchuifComponent usc = null;
			if(pijlIn1!=null)
			{	usc = (UitvoerSchuifComponent)pijlIn1.zender;
				usc.zetTabel(beginwaarde,selectnummer, varNaam, schaalFactorX);
			}
			gv.tekenOpnieuw();
		}
		super.mouseReleased(e);
	}*/
	public void mouseReleased(MouseEvent e)
	{	if(e.getSource()==gv)
		{	beginx = eenheid*Math.round(beginx/eenheid);
			beginy = eenheid*Math.round(beginy/eenheid);

			UitvoerSchuifComponent usc = null;
			for(int i=0 ; i<aantalPijlenIn ; i++)
			{	usc = (UitvoerSchuifComponent)pijlenIn[i].zender;
				usc.zetTabel(beginwaarde,selectnummer, varNaam, schaalFactorX);
			}
			gv.tekenOpnieuw();
		}
		super.mouseReleased(e);
	}
	
	public void mouseMoved(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==pmKnopY)
		{	if(e.getActionCommand().equals("plus"))
			{	if(factorRijNummerY%3==1)schaalFactorY*=2.5;
				else schaalFactorY*=2;
				factorRijNummerY++;
			}
			else if(e.getActionCommand().equals("min"))
			{	if(factorRijNummerY%3==2)schaalFactorY/=2.5;
				else schaalFactorY/=2;
				factorRijNummerY--;
			}
		}
		else if(e.getSource()==pmKnopX)
		{	if(e.getActionCommand().equals("plus"))
			{	if(factorRijNummerX%3==1)schaalFactorX*=2.5;
				else schaalFactorX*=2;
				factorRijNummerX++;
			}
			else if(e.getActionCommand().equals("min"))
			{	if(factorRijNummerX%3==2)schaalFactorX/=2.5;
				else schaalFactorX/=2;
				factorRijNummerX--;
			}
		}
		UitvoerSchuifComponent usc = null;
		for(int i=0 ; i<aantalPijlenIn ; i++)
		{	usc = (UitvoerSchuifComponent)pijlenIn[i].zender;
			usc.zetTabel(beginwaarde,selectnummer, varNaam, schaalFactorX);
		}
		schuifveld.tekenOpnieuw();
		gv.tekenOpnieuw();
	}
	
	public void mouseEntered(MouseEvent e){;}	
	
	class GrafiekVeld extends Component
	{
		private Image im;
  		private Graphics gIm;
		private boolean veranderd;
	
		public GrafiekVeld(int x, int y, int b, int h)
		{	
			setBounds(x,y,b,h);
			veranderd = true;
		}
		
		public void paint(Graphics g)
		{	int breedte = getSize().width;
			int hoogte = getSize().height;
			if(veranderd)			
			{	if (im == null)
				{	im = createImage(breedte, hoogte);
					gIm = im.getGraphics();
				}
				gIm.setColor(Color.white);
				gIm.fillRect(0,0,breedte,hoogte);
				tekenFunctie(gIm);
				veranderd = false;
			}
			g.drawImage(im, 0, 0, null);
		}
		
		public void tekenOpnieuw()
		{	veranderd = true;
			repaint();
		}
		
		public void	tekenFunctie(Graphics g)
		{	int breedte = getSize().width;
			int hoogte = getSize().height;
			g.setClip(0,0,breedte,hoogte);
			int imin = -(int)Math.round(beginx/eenheid); 
			int imax = 1+breedte/eenheid-(int)Math.round(beginx/eenheid);
			int bx = (int)beginx;
			for(int i=imin ; i<imax ; i++)
			{	g.setColor(Color.lightGray);
				g.drawLine(bx+i*eenheid,0,bx+i*eenheid,hoogte);
			}
			int jmin = -(int)Math.round(beginy/eenheid); 
			int jmax = 1+hoogte/eenheid-(int)Math.round(beginy/eenheid);
			int by = (int)beginy;
			for(int j=jmin ; j<jmax ; j++)
			{	g.setColor(Color.lightGray);
				g.drawLine(0,hoogte-(by+j*eenheid),breedte,hoogte-(by+j*eenheid));
			}	
			g.setColor(Color.black);
			if(bx>1 && bx<breedte)
			{	g.drawLine(bx-1,0,bx-1,hoogte);	
				g.drawLine(bx,0,bx,hoogte);
			}
			if(by>0 && by<hoogte)
			{	g.drawLine(0,hoogte-(by+1),breedte,hoogte-(by+1));
				g.drawLine(0,hoogte-(by),breedte,hoogte-(by));
			}
			/*if(exp!=null && exp.geefVarNaam()!=null && !exp.geefVarNaam().equals(""))
			{
				for(int i=0 ; i<breedte ; i++)
				{	double ii = i;
					boolean b0 = exp.isWaarde(schaalFactorX*(-beginx)/eenheid + schaalFactorX*ii/eenheid);
					boolean b1 = exp.isWaarde(schaalFactorX*(-beginx)/eenheid + schaalFactorX*(ii+1)/eenheid);
					if(b0 && b1)
					{	double d0 = exp.geefW(schaalFactorX*(-beginx)/eenheid + schaalFactorX*ii/eenheid);//dd0.doubleValue();
						double d1 = exp.geefW(schaalFactorX*(-beginx)/eenheid + schaalFactorX*(ii+1)/eenheid);//dd1.doubleValue();
						int x0 = i;
						int x1 = i+1;
						double dy0 = Math.round(hoogte -(beginy+eenheid*d0/schaalFactorY));
						double dy1 = Math.round(hoogte -(beginy+eenheid*d1/schaalFactorY));
						if(dy0>1000)dy0 = 1000;
						if(dy0<-1000)dy0 = -1000;
						if(dy1>1000)dy1 = 1000;
						if(dy1<-1000)dy1 = -1000;
						int y0 = (int)dy0;
						int y1 = (int)dy1;
						g.drawLine(x0,y0,x1,y1);
					}
				}
				g.setColor(new Color(255,0,0));
				double d = bx+1.0*((selectnummer+beginwaarde)*eenheid);
				int x = (int)d;
				if(exp.isWaarde((selectnummer+beginwaarde)*schaalFactorX) && selectnummer<8 && selectnummer>-1)
				{	double d0 = exp.geefW((selectnummer+beginwaarde)*schaalFactorX);
					int y = (int)Math.round(hoogte -(beginy+eenheid*d0/schaalFactorY));
					g.fillOval(x-2,y-2,5,5);
					g.drawLine(x,y,x,hoogte);
					g.drawLine(x,y,0,y);
				}
			}*/
			for(int j=0 ; j<aantalPijlenIn ; j++)
			{
				if(expressies[j]!=null && expressies[j].geefVarNaam()!=null && varNaam.equals(expressies[j].geefVarNaam())&& !expressies[j].geefVarNaam().equals(""))
				{	g.setColor(Color.black);
					for(int i=0 ; i<breedte ; i++)
					{	double ii = i;
						boolean b0 = expressies[j].isWaarde(schaalFactorX*(-beginx)/eenheid + schaalFactorX*ii/eenheid);
						boolean b1 = expressies[j].isWaarde(schaalFactorX*(-beginx)/eenheid + schaalFactorX*(ii+1)/eenheid);
						if(b0 && b1)
						{	double d0 = expressies[j].geefW(schaalFactorX*(-beginx)/eenheid + schaalFactorX*ii/eenheid);//dd0.doubleValue();
							double d1 = expressies[j].geefW(schaalFactorX*(-beginx)/eenheid + schaalFactorX*(ii+1)/eenheid);//dd1.doubleValue();
							int x0 = i;
							int x1 = i+1;
							double dy0 = Math.round(hoogte -(beginy+eenheid*d0/schaalFactorY));
							double dy1 = Math.round(hoogte -(beginy+eenheid*d1/schaalFactorY));
							if(dy0>1000)dy0 = 1000;
							if(dy0<-1000)dy0 = -1000;
							if(dy1>1000)dy1 = 1000;
							if(dy1<-1000)dy1 = -1000;
							int y0 = (int)dy0;
							int y1 = (int)dy1;
							g.drawLine(x0,y0,x1,y1);
						}
					}
					g.setColor(new Color(255,0,0));
					double d = bx+1.0*((selectnummer+beginwaarde)*eenheid);
					int x = (int)d;
					if(expressies[j].isWaarde((selectnummer+beginwaarde)*schaalFactorX) && selectnummer<8 && selectnummer>-1)
					{	double d0 = expressies[j].geefW((selectnummer+beginwaarde)*schaalFactorX);
						int y = (int)Math.round(hoogte -(beginy+eenheid*d0/schaalFactorY));
						g.fillOval(x-2,y-2,5,5);
						g.drawLine(x,y,x,hoogte);
						g.drawLine(x,y,0,y);
					}
				}
			}
		}
	}
}
