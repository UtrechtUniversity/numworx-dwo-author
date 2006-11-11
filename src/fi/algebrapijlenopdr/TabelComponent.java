package fi.algebrapijlenopdr;

import java.awt.Polygon;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import fi.algebrapijlenopdr.expressies_ap.*;
import fi.algebrapijlenopdr.schuifobjects.*;

public class TabelComponent extends Component implements MouseListener, MouseMotionListener
{	
	 private Polygon pijlPlus, pijlMin, pijlPlusContain, pijlMinContain;
	 private Expressie exp;
	 private boolean selectMogelijk;
	 private int beginwaarde;
	 private int selectnummer;
	 private double schaalFactorX;
	 private int breedteInv;
	 private int breedteUitv;
	 private String varNaam;
	 Font f;
	 FontMetrics fm;
	 private boolean dubbel;
	 private int starty;
	 
	
	public TabelComponent()
	{	addMouseListener(this);
		addMouseMotionListener(this);
		f = new Font("SansSerrif",Font.PLAIN,12);
		fm = getFontMetrics(f);
		beginwaarde = 0;
		selectnummer = 999;
		schaalFactorX = 1;
		selectMogelijk = true;
		dubbel = false;
		if(dubbel)breedteInv = 20;
		else breedteInv = 20;
		breedteUitv = 33;
		
		exp = new BasisExpressie("x");
	}
	
	public Hashtable getState()
	{	int beginwaarde  = 0;
		int selectnummer  = 0;
		double schaalFactorX  = 0;
						
		beginwaarde = this.beginwaarde;
		selectnummer = this.selectnummer;
		schaalFactorX = this.schaalFactorX;
						
		Hashtable h = new Hashtable();
	    h.put("beginwaarde", new Integer(beginwaarde));
	    h.put("selectnummer", new Integer(selectnummer));
	    h.put("schaalFactorX", new Double(schaalFactorX));
	    return h;
	}

    public void setState(Hashtable h)
    {	int beginwaarde = ((Integer)h.get("beginwaarde")).intValue();
    	int selectnummer = ((Integer)h.get("selectnummer")).intValue();
    	double schaalFactorX = ((Double)h.get("schaalFactorX")).doubleValue();
    				
		this.beginwaarde = beginwaarde;
		this.selectnummer = selectnummer;
		this.schaalFactorX = schaalFactorX;
    }
    
	public void paint(Graphics g)
	{	int breedte = getSize().width;
		int hoogte = getSize().height;
		
		g.setFont(f);
		
		if(dubbel)
		{	breedteUitv = breedte-10-breedteInv;
			
			g.setColor(Color.white);
			g.fillRect(breedteInv+6,15,breedteUitv,hoogte - 31);
			g.setColor(Color.black);
			g.drawRect(breedteInv+6,15,breedteUitv,hoogte - 31);

			g.setColor(Color.white);
			g.fillRect(3,15,breedteInv,hoogte - 31);
			g.setColor(Color.black);
			g.drawRect(3,15,breedteInv,hoogte - 31);
			
			g.setColor(new Color(220,220,220));
			g.fillRect(3,0,breedteInv,15);
			g.setColor(Color.black);
			g.drawRect(3,0,breedteInv,15);
			
			if(selectMogelijk && selectnummer>-1 && selectnummer<8 && exp!=null && !exp.geefVarNaam().equals(""))
			{	g.setColor(new Color(255,200,200));
				g.fillRect(breedteInv+6,15+selectnummer*15,breedteUitv,16);
				g.fillRect(3,15+selectnummer*15,breedteInv,16);
				g.setColor(Color.black);
				g.drawRect(breedteInv+6,15+selectnummer*15,breedteUitv,16);
				g.drawRect(3,15+selectnummer*15,breedteInv,16);
			}
			pijlPlusContain = new Polygon();
			pijlPlusContain.addPoint(breedteInv+6+breedteUitv/2-25,12);
			pijlPlusContain.addPoint(breedteInv+6+breedteUitv/2+25,12);
			pijlPlusContain.addPoint(breedteInv+6+breedteUitv/2,0);
				
			pijlPlus = new Polygon();
			pijlPlus.addPoint(breedteInv+6+breedteUitv/2-5,12);
			pijlPlus.addPoint(breedteInv+6+breedteUitv/2+5,12);
			pijlPlus.addPoint(breedteInv+6+breedteUitv/2,4);
			g.fillPolygon(pijlPlus);
			g.drawPolygon(pijlPlus);
		
			pijlMinContain = new Polygon();
			pijlMinContain.addPoint(breedteInv+6+breedteUitv/2-25,hoogte-13);
			pijlMinContain.addPoint(breedteInv+6+breedteUitv/2+25,hoogte-13);
			pijlMinContain.addPoint(breedteInv+6+breedteUitv/2,hoogte);
			
			pijlMin = new Polygon();
			pijlMin.addPoint(breedteInv+6+breedteUitv/2-5,hoogte-13);
			pijlMin.addPoint(breedteInv+6+breedteUitv/2+5,hoogte-13);
			pijlMin.addPoint(breedteInv+6+breedteUitv/2,hoogte-5);
			g.fillPolygon(pijlMin);
			g.drawPolygon(pijlMin);	
			if(exp!=null)
			{	String s = exp.geefVarNaam();
				if(s!=null && !s.equals(""))
				{	g.drawString(s,5,12);
					for(int i=0 ; i<8 ; i++)
					{	if(exp.isWaarde(schaalFactorX*(i+beginwaarde)))
						{	double d = exp.geefW(schaalFactorX*(i+beginwaarde));
							g.drawString(exp.df.format(d),breedteInv+8,28+i*15);
						}
						else g.drawString("-",breedteInv+8,28+i*15);
						g.drawString(exp.df.format(schaalFactorX*(i+beginwaarde)),5,28+i*15);
					}
				}
			}
		}
		else
		{	breedteUitv = breedte-10;
			g.setColor(Color.white);
			g.fillRect(3,15,breedte-7,hoogte - 31);
			g.setColor(Color.black);
			g.drawRect(3,15,breedte-7,hoogte - 31);
			
			if(selectMogelijk && selectnummer>-1 && selectnummer<8 && exp!=null && !exp.geefVarNaam().equals(""))
			{	g.setColor(new Color(255,200,200));
				g.fillRect(3,15+selectnummer*15,breedte-7,16);
				g.setColor(Color.black);
				g.drawRect(3,15+selectnummer*15,breedte-7,16);
			}
			
			pijlPlusContain = new Polygon();
			pijlPlusContain.addPoint(breedteUitv/2-25,12);
			pijlPlusContain.addPoint(breedteUitv/2+25,12);
			pijlPlusContain.addPoint(breedteUitv/2,0);
				
			pijlPlus = new Polygon();
			pijlPlus.addPoint(breedteUitv/2-5,12);
			pijlPlus.addPoint(breedteUitv/2+5,12);
			pijlPlus.addPoint(breedteUitv/2,4);
			g.fillPolygon(pijlPlus);
			g.drawPolygon(pijlPlus);
		
			pijlMinContain = new Polygon();
			pijlMinContain.addPoint(breedteUitv/2-25,hoogte-13);
			pijlMinContain.addPoint(breedteUitv/2+25,hoogte-13);
			pijlMinContain.addPoint(breedteUitv/2,hoogte);
			
			pijlMin = new Polygon();
			pijlMin.addPoint(breedteUitv/2-5,hoogte-13);
			pijlMin.addPoint(breedteUitv/2+5,hoogte-13);
			pijlMin.addPoint(breedteUitv/2,hoogte-5);
			g.fillPolygon(pijlMin);
			g.drawPolygon(pijlMin);	
			if(exp!=null)
			{	String s = exp.geefVarNaam();
				if(s!=null && !s.equals(""))
				{	for(int i=0 ; i<8 ; i++)
					{	if(exp.isWaarde(schaalFactorX*(i+beginwaarde)))
						{	double d = exp.geefW(schaalFactorX*(i+beginwaarde));
							g.drawString(exp.df.format(d),8,28+i*15);
						}
						else g.drawString("-",8,28+i*15);
					}
				}
			}
		}
	}
	
	public void zetDubbel(boolean b)
	{	dubbel = b;
	}
	
	public void zetExpressie(Expressie e)
	{	if(e!=null  && e.geefVarNaam()!=null)//&& e.geefWaarde()==null
		{	exp = e;
			varNaam = e.geefVarNaam();
		}
		else //exp = null;
		exp = new BasisExpressie("x"); 
	}
	
	public void zetTabel(int beginwaarde, int selectnummer, String varN, double schaalFactorX)
	{	if(varNaam.equals(varN))
		{	this.beginwaarde = beginwaarde;
			this.selectnummer = selectnummer;
			this.schaalFactorX = schaalFactorX;
			repaint();
		}
	}
	
	public void zetSelectMogelijk(boolean b)
	{	selectMogelijk = b;
	}
	
	public int geefBreedte()
	{	if(dubbel)
		{	breedteInv = 20;
			breedteUitv = 33;
			if(exp!=null)
			{	varNaam = exp.geefVarNaam();
				if(varNaam!=null && !varNaam.equals(""))
				{	breedteInv = Math.max(breedteInv,fm.stringWidth(varNaam)+4);
					
					for(int i=0 ; i<8 ; i++)
					{	if(exp.isWaarde(schaalFactorX*i+beginwaarde))
						{	double d = exp.geefW(schaalFactorX*(i+beginwaarde));
							String sUitv = exp.df.format(d);
							breedteUitv = Math.max(breedteUitv,fm.stringWidth(sUitv)+4);
						}
						String sInv = exp.df.format(schaalFactorX*(i+beginwaarde));
						breedteInv = Math.max(breedteInv,fm.stringWidth(sInv)+4);
					}
				}
			}
			int b = breedteInv + breedteUitv + 10;
			return b;	
		}
		else
		{	breedteUitv = 30;
			if(exp!=null)
			{	varNaam = exp.geefVarNaam();
				if(varNaam!=null && !varNaam.equals(""))
				{				
					for(int i=0 ; i<8 ; i++)
					{	if(exp.isWaarde(schaalFactorX*(i+beginwaarde)))
						{	double d = exp.geefW(schaalFactorX*(i+beginwaarde));
							String sUitv = exp.df.format(d);
							breedteUitv = Math.max(breedteUitv,fm.stringWidth(sUitv)+4);
						}
					}
				}
			}
			int b = breedteUitv + 10;
			return b;	
		}
	}
	
	public void mousePressed(MouseEvent e)
	{	if(pijlPlusContain.contains(e.getX(),e.getY()))
		{	beginwaarde--;
			selectnummer++;
			((AlgebraSchuifVeld)getParent().getParent()).zetTabellen(beginwaarde,selectnummer,varNaam,schaalFactorX);
			repaint();
		}
		else if(pijlMinContain.contains(e.getX(),e.getY()))
		{	beginwaarde++;
			selectnummer--;
			((AlgebraSchuifVeld)getParent().getParent()).zetTabellen(beginwaarde,selectnummer,varNaam, schaalFactorX);
			repaint();
		}
		else
		{	for(int i=0 ; i<8 ; i++)
			{	if((new Rectangle(4,17+i*15,getSize().width-9,15)).contains(e.getX(),e.getY()))
				{	if(selectnummer==i)selectnummer=999;
					else selectnummer = i;
					((AlgebraSchuifVeld)getParent().getParent()).zetTabellen(beginwaarde,selectnummer,varNaam, schaalFactorX);
					
				}
			}
		}
		((SchuifComponent)getParent()).mousePressed(e);
	}	
	public void mouseDragged(MouseEvent e)
	{	((SchuifComponent)getParent()).mouseDragged(e);
	}
	
	public void mouseReleased(MouseEvent e)
	{	((SchuifComponent)getParent()).mouseReleased(e);
	}
	public void mouseMoved(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}	
}
