package fi.algebraexpressies;

import java.awt.Polygon;
import java.awt.*;
import java.awt.event.*;
import fi.algebraexpressies.expressies.*;
import fi.algebraexpressies.schuifobjects.*;

public class TabelComponent extends Component implements MouseListener, MouseMotionListener
{	
	 private Polygon pijlPlus, pijlMin;
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
	 
	
	public TabelComponent()
	{	addMouseListener(this);
		addMouseMotionListener(this);
		f = new Font("SansSerrif",Font.PLAIN,12);
		fm = getFontMetrics(f);
		beginwaarde = 0;
		selectnummer = 999;
		schaalFactorX = 1;
		selectMogelijk = false;
		breedteInv = 20;
		breedteUitv = 33;
	}
	
	public void paint(Graphics g)
	{	int breedte = getSize().width;
		int hoogte = getSize().height;
		breedteUitv = breedte-10-breedteInv;
		g.setFont(f);
			
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
			
		pijlPlus = new Polygon();
		pijlPlus.addPoint(breedteInv+6+breedteUitv/2-5,12);
		pijlPlus.addPoint(breedteInv+6+breedteUitv/2+5,12);
		pijlPlus.addPoint(breedteInv+6+breedteUitv/2,4);
		g.fillPolygon(pijlPlus);
		g.drawPolygon(pijlPlus);
		
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
	
	public void zetExpressie(Expressie e)
	{	if(e!=null && e.geefWaarde()==null && e.geefVarNaam()!=null)exp = e;
		else exp = null;
		//varNaam = e.geefVarNaam();
		//int n = geefBreedte();
		//repaint();
	}
	
	public void zetTabel(int beginwaarde, int selectnummer, String varN, double schaalFactorX)
	{	//if(varNaam.equals(varN))
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
	
	public void mousePressed(MouseEvent e)
	{	if(pijlPlus.contains(e.getX(),e.getY()))
		{	beginwaarde--;
			selectnummer++;
			((UitvoerSchuifComponent)getParent()).zetGrafiekTabel(beginwaarde,selectnummer,varNaam,schaalFactorX);
			repaint();
		}
		else if(pijlMin.contains(e.getX(),e.getY()))
		{	beginwaarde++;
			selectnummer--;
			((UitvoerSchuifComponent)getParent()).zetGrafiekTabel(beginwaarde,selectnummer,varNaam, schaalFactorX);
			repaint();
		}
		else
		{	for(int i=0 ; i<8 ; i++)
			{	if((new Rectangle(4,17+i*15,getSize().width-9,15)).contains(e.getX(),e.getY()))
				{	if(selectnummer==i)selectnummer=999;
					else selectnummer = i;
					((UitvoerSchuifComponent)getParent()).zetGrafiekTabel(beginwaarde,selectnummer,varNaam, schaalFactorX);
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
