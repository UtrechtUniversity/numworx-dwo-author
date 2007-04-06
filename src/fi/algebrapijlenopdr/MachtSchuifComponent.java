package fi.algebrapijlenopdr;

import java.awt.Polygon;
import java.awt.*;
import java.awt.event.*;
import fi.algebrapijlenopdr.expressies_ap.*;

public class MachtSchuifComponent extends BewerkingSchuifComponent 
{	
	public MachtSchuifComponent(AlgebraSchuifVeld asv,int x, int y, int b, int h)
	{	super(asv,x,y,b,h);
		
		beginw = new BasisExpressie("2");
		
		tf = new TextField();
		if(!links)tf.setBounds(30,1,16,15);
		else tf.setBounds(20,1,16,15);
		tf.addActionListener(this);
		tf.addFocusListener(this);
		tf.setVisible(false);
		tf.setEnabled(false);
	}
	
	public void zetLinks(boolean b)
	{	links = b;
		if(!links)tf.setBounds(30,1,16,15);
		else tf.setBounds(20,1,16,15);
		for(int i=0 ; i<aantalPu ; i++)
		{	pijlUit[i].zetLinks(b);
			if(!links)pijlUit[i].zetPlaats(getLocation().x + getSize().width+9 ,getLocation().y + 10 );
			else pijlUit[i].zetPlaats(getLocation().x - 10 ,getLocation().y + 10 );
		}
		if(!links)
		{	plusMinKnop.setLocation(getSize().width-12,1);
		}
		else
		{	plusMinKnop.setLocation(getSize().width-22,1);
		}
		schuifveld.tekenOpnieuw();
	}
	
	public void paint(Graphics g)
  	{ 	super.paint(g);
		
		g.setColor(Color.black);
		String s1 = "...";
		String s2 = Expressie.df.format(beginw.geefWaarde());
		
		Font f1 = new Font("SansSerrif",Font.PLAIN,14);
		Font f2 = new Font("SansSerrif",Font.PLAIN,10);
		g.setFont(f);
		
		int sccrollCorr = 0;
		if(scrollable)sccrollCorr = 5;
		if(!links)
		{	g.setFont(f1);
			g.drawString(s1,20-sccrollCorr,getSize().height-4);
			g.setFont(f2);
			g.drawString(s2,35-sccrollCorr,getSize().height-8);
		}
		else 
		{	g.setFont(f1);
			g.drawString(s1,10-sccrollCorr,getSize().height-4);
			g.setFont(f2);
			g.drawString(s2,25-sccrollCorr,getSize().height-8);
		}
	}
	
	public void zetMaat()
	{	int b = 50;
		int h = 20;
		int corr = 0;
		if(beginw!=null)
		{	b = beginw.breedte;
			if(b > 10)b = b+40;
			else b = 50;
			
		}
		
		setSize(b,h);
		if(!links)
		{	tf.setBounds(30,1,b-31,15);
			plusMinKnop.setLocation(b-12,2);
		}
		else 
		{	tf.setBounds(20,1,b-31,15);
			plusMinKnop.setLocation(b-22,2);
		}

	}
	
	public Expressie geefUitvoer(int max)
	{	if(AlgebraPijlenOpdr.simplify)
		{	Expressie uitv = new Expressie();
			if(pijlIn1==null)return null;
			Expressie e1 = pijlIn1.zender.geefUitvoer(max-1);
			Expressie e2 = beginw;
			if(e1==null)return null;
			if(e2.geefWaarde().doubleValue()==0)uitv = new BasisExpressie("1");
			else if(e2.geefWaarde().doubleValue()==1)uitv = e1;
			else uitv = new Macht(e1,e2);
			return uitv;
		}
		else
		{	Expressie uitv = new Expressie();
			if(pijlIn1==null)return null;
			Expressie e1 = pijlIn1.zender.geefUitvoer(max-1);
			Expressie e2 = beginw;
			if(e1==null)return null;
			if(e2.geefWaarde().doubleValue()==0)uitv = new BasisExpressie("1");
			else if(e2.geefWaarde().doubleValue()==1)uitv = e1;
			else uitv = new Macht(e1,e2);
			return uitv;
		}
	}
	
	public Expressie geefVerborgenUitvoer(int max)
	{	Expressie uitv = new Expressie();
		if(pijlIn1==null)return null;
		Expressie e1 = pijlIn1.zender.geefVerborgenUitvoer(max-1);
		Expressie e2 = beginw;
		if(e1==null)return null;
		if(e2.geefWaarde().doubleValue()==0)uitv = new BasisExpressie("1");
		else if(e2.geefWaarde().doubleValue()==1)uitv = e1;
		else uitv = new Macht(e1,e2);
		return uitv;
	}
	
	public void zetInvulWaarde()
	{	boolean isGeldigeInvoer=true;
		{	try
			{	Double w = Double.valueOf(tf.getText());
			}
			catch(NumberFormatException ex)
			{	isGeldigeInvoer = false;
				tf.setText(Expressie.df.format(beginw.geefWaarde()));
			}
		}
		if(isGeldigeInvoer)
		{	beginw = new BasisExpressie( tf.getText());
			beginw.zetMaat(fm);
		}
		else
		{	beginw = new BasisExpressie("2");
			beginw.zetMaat(fm);
		}
		zetMaat();
		zetVeranderd(20);
		
		tf.setEnabled(false);
		remove(tf);
		tf.setVisible(false);
		schuifveld.tekenOpnieuw();
	}
}
