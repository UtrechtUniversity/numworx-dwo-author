package fi.algebrapijlenopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;


import fi.algebrapijlenopdr.expressies_ap.*;
import fi.algebrapijlenopdr.schuifobjects.*;

public class TabelComponent extends Container implements ActionListener, MouseListener, MouseMotionListener
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
	 
	 
	 private int beginx;
	 private int eenheidx= 14;
	 private boolean raak=false;
	 
	 private String defaultVarnaam;
	 
	 
	
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
		else breedteInv = 30;
		breedteUitv = 33;
		
		
		
	}
	
	public void setDefaultVarnaam(String s)
	{	defaultVarnaam = s;
		exp = new BasisExpressie(defaultVarnaam);
	}
	
	/*public Hashtable getState()
	{	int beginwaarde  = 0;
		int selectnummer  = 0;
		double schaalFactorX  = 1;
						
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
    }*/
    
	public void paint(Graphics g)
	{	int breedte = getSize().width;
		int hoogte = getSize().height;
		
		g.setFont(f);
		
		if(dubbel)
		{	breedteUitv = breedte-10-breedteInv;
			
			g.setColor(Color.white);
			g.fillRect(breedteInv+6,15,breedteUitv-10,hoogte - 31);
			g.setColor(Color.black);
			g.drawRect(breedteInv+6,15,breedteUitv-10,hoogte - 31);

			g.setColor(Color.white);
			g.fillRect(3,15,breedteInv,hoogte - 31);
			g.setColor(Color.black);
			g.drawRect(3,15,breedteInv,hoogte - 31);
			
			//g.setColor(new Color(220,220,220));
			//g.fillRect(3,0,breedteInv,15);
			//g.setColor(Color.black);
			//g.drawRect(3,0,breedteInv,15);
			
			if(selectMogelijk && selectnummer>-1 && selectnummer<8 && exp!=null && !exp.geefVarNaam().equals(""))
			{	g.setColor(new Color(255,200,200));
				g.fillRect(breedteInv+6,15+selectnummer*15+beginx%eenheidx,breedteUitv-10,16);
				g.fillRect(3,15+selectnummer*15+beginx%eenheidx,breedteInv,16);
				g.setColor(Color.black);
				g.drawRect(breedteInv+6,15+selectnummer*15+beginx%eenheidx,breedteUitv-10,16);
				g.drawRect(3,15+selectnummer*15+beginx%eenheidx,breedteInv,16);
			}
			/*pijlPlusContain = new Polygon();
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
			g.drawPolygon(pijlMin);	*/
			
			int knopPlusX = 15;
			int knopPlusY = 4;
			int knopMinX = 15;
			int knopMinY = hoogte-5;
			
			//if(AlgebraPijlen.formule)
			//{	knopPlusX = 15;
			//	knopPlusY = hoogte-13;
			//	knopMinX = 40;
			//	knopMinY = hoogte-5;
			//}
			
			pijlPlusContain = new Polygon();
			pijlPlusContain.addPoint(knopPlusX-10,knopPlusY+8);
			pijlPlusContain.addPoint(knopPlusX+10,knopPlusY+8);
			pijlPlusContain.addPoint(knopPlusX,knopPlusY-4);
				
			pijlPlus = new Polygon();
			pijlPlus.addPoint(knopPlusX-5,knopPlusY+8);
			pijlPlus.addPoint(knopPlusX+5,knopPlusY+8);
			pijlPlus.addPoint(knopPlusX,knopPlusY);
			g.fillPolygon(pijlPlus);
			g.drawPolygon(pijlPlus);
		
			pijlMinContain = new Polygon();
			pijlMinContain.addPoint(knopMinX-10,knopMinY-8);
			pijlMinContain.addPoint(knopMinX+10,knopMinY-8);
			pijlMinContain.addPoint(knopMinX,knopMinY+5);
			
			pijlMin = new Polygon();
			pijlMin.addPoint(knopMinX-5,knopMinY-8);
			pijlMin.addPoint(knopMinX+5,knopMinY-8);
			pijlMin.addPoint(knopMinX,knopMinY);
			g.fillPolygon(pijlMin);
			g.drawPolygon(pijlMin);	
			
			if(exp!=null)
			{	String s = exp.geefVarNaam();
				if(s!=null && !s.equals(""))
				{	//g.drawString(s,5,12);
					for(int i=0 ; i<8 ; i++)
					{	if(exp.isWaarde(schaalFactorX*(i+beginwaarde)))
						{	double d = exp.geefW(schaalFactorX*(i+beginwaarde));
							if(i<7 && beginx>0 || i>0 && beginx<0 ||beginx%eenheidx==0) g.drawString(exp.df.format(d),breedteInv+8,28+i*15+beginx%eenheidx);
						}
						else g.drawString("-",breedteInv+8,28+i*15+beginx%eenheidx);
						//g.drawString(exp.df.format(schaalFactorX*(i+beginwaarde)),5,28+i*15+beginx);
					}
				}
			}
		}
		else
		{	breedteUitv = breedte-10;
			g.setColor(Color.white);
			g.fillRect(3,15,breedte-17,hoogte - 31);
			g.setColor(Color.black);
			g.drawRect(3,15,breedte-17,hoogte - 31);
			
			if(selectMogelijk && selectnummer>-1 && selectnummer<8 && exp!=null && !exp.geefVarNaam().equals(""))
			{	g.setColor(new Color(255,200,200));
				if(selectnummer<7 && beginx>0 || selectnummer>0 && beginx<0 ||beginx%eenheidx==0)g.fillRect(3,15+selectnummer*15+beginx%eenheidx,breedte-17,16);
				g.setColor(Color.black);
				if(selectnummer<7 && beginx>0 || selectnummer>0 && beginx<0 ||beginx%eenheidx==0)g.drawRect(3,15+selectnummer*15+beginx%eenheidx,breedte-17,16);
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
							if(i<7 && beginx>0 || i>0 && beginx<0 ||beginx%eenheidx==0)g.drawString(exp.df.format(d),8,28+i*15+beginx%eenheidx);
						}
						else g.drawString("-",8,28+i*15+beginx%eenheidx);
					}
				}
			}
		}
		super.paint(g);
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
		{	exp = new BasisExpressie(defaultVarnaam); 
			varNaam = defaultVarnaam;
		}
	}
	
	public void zetTabel(int beginwaarde, int selectnummer, String varN, double schaalFactorX, double beginx)
	{	varNaam = exp.geefVarNaam();
		//System.out.println("test3"+varNaam +varN);
	
		if(varNaam.equals(varN))
		{	this.beginwaarde = beginwaarde;
			this.selectnummer = selectnummer;
			this.schaalFactorX = schaalFactorX;
			this.beginx = (int)Math.round(beginx);
			
			repaint();
		}
	}
	
	public void zetSelectMogelijk(boolean b)
	{	selectMogelijk = b;
	}
	
	public int geefBreedte()
	{	if(dubbel)
		{	breedteInv = 20;
			breedteUitv = 23;
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
			int b = breedteInv + breedteUitv + 20;
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
			int b = breedteUitv + 20;
			return b;	
		}
		
	
	}
	
	public void actionPerformed(ActionEvent e)
	{
		
	}
	
	public void mousePressed(MouseEvent e)
	{	starty = e.getY();
		raak = (new Rectangle(4,17,getSize().width-9,115)).contains(e.getX(),e.getY());
		if(new Rectangle(0,17,getSize().width-5,getSize().height-34).contains(e.getX(), e.getY()))
			setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
		
				
		if(pijlPlusContain.contains(e.getX(),e.getY()))
		{	beginwaarde--;
			selectnummer++;
			//((AlgebraSchuifVeld)getParent().getParent()).zetTabellen(beginwaarde,selectnummer,varNaam,schaalFactorX);
			repaint();
		}
		else if(pijlMinContain.contains(e.getX(),e.getY()))
		{	beginwaarde++;
			selectnummer--;
			//((AlgebraSchuifVeld)getParent().getParent()).zetTabellen(beginwaarde,selectnummer,varNaam, schaalFactorX);
			repaint();
		}
		else
		{	
		}
		beginx = -beginwaarde*eenheidx;
		//((AlgebraSchuifVeld)getParent().getParent()).zoomStateHolder.setBeginwaarde(varNaam, beginwaarde);
        //((AlgebraSchuifVeld)getParent().getParent()).zoomStateHolder.setZoomStates(varNaam);
        
		if(!raak)((SchuifComponent)getParent()).mousePressed(e);
	}	
	
	/*public void mousePressed(MouseEvent e)
	{	requestFocus();
		starty = e.getY();
		raak = contains(e.getX(),e.getY());
	}	*/
	
	public void mouseDragged(MouseEvent e)
	{	if(raak)
		{
			int dy =  e.getY() - starty;
		
			beginx = beginx+dy;
			int b = beginwaarde;
			beginwaarde = -(int)Math.round(beginx/eenheidx);
			selectnummer = selectnummer + b - beginwaarde;
			//repaint();
			starty = e.getY();
			
			((AlgebraSchuifVeld)(getParent().getParent())).zoomStateHolder.setSelectnummer(varNaam, selectnummer);
	        ((AlgebraSchuifVeld)(getParent().getParent())).zoomStateHolder.setBeginwaarde(varNaam, beginwaarde);
	        ((AlgebraSchuifVeld)(getParent().getParent())).zoomStateHolder.setBeginx(varNaam, beginx);
	        ((AlgebraSchuifVeld)(getParent().getParent())).zoomStateHolder.setZoomStates(varNaam);
			
		}

	}
	/*public void mouseDragged(MouseEvent e)
	{	
		
	}*/
	
	public void mouseReleased(MouseEvent e)
	{	setCursor(new Cursor(Cursor.HAND_CURSOR));
		int beginxOud = beginx;
		int b = beginwaarde;
		if(beginx>0)beginx = (beginx+eenheidx/2)/eenheidx*eenheidx;
		else beginx = (beginx-eenheidx/2)/eenheidx*eenheidx;
		beginwaarde = -(int)Math.round(beginx/eenheidx);
		selectnummer = selectnummer + b - beginwaarde;
		beginx = -beginwaarde*eenheidx;
		
		
		((SchuifComponent)getParent()).mouseReleased(e);
		
		if(beginx == beginxOud)
		{	for(int i=0 ; i<8  ; i++)
			{	if((new Rectangle(4,17+i*15,getSize().width-9,15)).contains(e.getX(),e.getY()))
				{	if(selectnummer==i)selectnummer=999;
					else selectnummer = i;
					//((AlgebraSchuifVeld)getParent().getParent()).zetTabellen(beginwaarde,selectnummer,varNaam, schaalFactorX);
				}
			}
		}
		((AlgebraSchuifVeld)(getParent().getParent())).zoomStateHolder.setSelectnummer(varNaam, selectnummer);
        ((AlgebraSchuifVeld)(getParent().getParent())).zoomStateHolder.setBeginwaarde(varNaam, beginwaarde);
        ((AlgebraSchuifVeld)(getParent().getParent())).zoomStateHolder.setBeginx(varNaam, beginx);
        ((AlgebraSchuifVeld)(getParent().getParent())).zoomStateHolder.setZoomStates(varNaam);
        
        
		
	}
	public void mouseMoved(MouseEvent e){;}
	public void mouseExited(MouseEvent e)
	{	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	
	}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e)
	{	//if(new Rectangle(0,17,getSize().width-5,getSize().height-34).contains(e.getX(), e.getY()))
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	
	}	
}
