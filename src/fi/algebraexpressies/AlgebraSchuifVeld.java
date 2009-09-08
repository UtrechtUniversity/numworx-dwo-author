package fi.algebraexpressies;

import java.awt.*;
import java.awt.event.*;
import fi.algebraexpressies.schuifobjects.*;

public class AlgebraSchuifVeld extends SchuifVeld implements ItemListener, MouseListener, MouseMotionListener,ActionListener
{	
	
	private Button wisKnop;
	InvulPanel ip;
	private Checkbox grafiekCheckbox;
	AlgebraSchuifComponent[] schuifcomponenten;
	GrafiekComponent grafiekComponent;
	int aantalSc;
	
	boolean buttonsAdded;
	
	public AlgebraSchuifVeld(int x, int y, int b, int h)
	{	super(x,y,b,h);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		
		ip = new InvulPanel(this,8,400,90,50);
		ip.setBackground(Color.lightGray);
		add(ip);
		
		grafiekCheckbox = new Checkbox(AlgebraExpressies.rb.getString("grafiekLabel"));
		grafiekCheckbox.addItemListener(this);
		grafiekCheckbox.setBounds( 8,340,80,20);
		grafiekCheckbox.setBackground(Color.lightGray);
		add(grafiekCheckbox);
		
		wisKnop = new Button(AlgebraExpressies.rb.getString("wisKnopLabel"));
		wisKnop.setBounds(18,370,70,20);
		wisKnop.addActionListener(this);
		add(wisKnop);
		
		maakStapel();
		
		grafiekComponent = new GrafiekComponent(this,500,200,200,230);
		
		
		
	}
	
	
	
	public void maakStapel()
	{	aantalSc = 0;
		schuifcomponenten = new AlgebraSchuifComponent[200];
		schuifcomponenten[aantalSc] = new UitvoerSchuifComponent(this,30,80,40,30);			aantalSc++;
		schuifcomponenten[aantalSc] = new OptelSchuifComponent(this,7,150,40,30);			aantalSc++;
		schuifcomponenten[aantalSc] = new AftrekSchuifComponent(this,54,150,40,30);			aantalSc++;
		schuifcomponenten[aantalSc] = new VermenigvuldigSchuifComponent(this,7,190,40,30);	aantalSc++;
		schuifcomponenten[aantalSc] = new DeelSchuifComponent(this,54,190,40,30);			aantalSc++;
		schuifcomponenten[aantalSc] = new KwadraatSchuifComponent(this,7,230,40,30);		aantalSc++;
		schuifcomponenten[aantalSc] = new WortelSchuifComponent(this,54,230,40,30);			aantalSc++;
		schuifcomponenten[aantalSc] = new MachtSchuifComponent(this,7,270,40,30);			aantalSc++;
		
		int max = aantalSc;
		for(int i=0 ; i<max ; i++)
		{	schuifcomponenten[i].voegPijlToe(new Pijl(this));
			add(schuifcomponenten[i]);
		}
		add(grafiekCheckbox,0);
		add(wisKnop,0);
		add(ip,0);
	}
	
	public void tekenAchtergrond(Graphics g)
	{	Dimension dd = getSize();				
		g.setColor(Color.white);
		g.fillRect(0,0,dd.width,dd.height);
		g.setColor(Color.lightGray);
		g.fillRect(0,0,100,dd.height);
		g.setColor(Color.black);
		g.drawLine(100,0,100,dd.height-1);
		g.drawRect(0,0,dd.width-1,dd.height-1);
		
		//g.drawString(Integer.toString(aantalSc),20,10);
		//g.drawString(Integer.toString(schuiflaag.getComponentCount()),50,10);
		//g.drawString(Integer.toString(getComponentCount()),80,10);
		
		//int intveranderd=0,intresized=0;
		//if(veranderd)intveranderd=1;
		//if(resized)intresized=1;
		///g.drawString(Integer.toString(intveranderd),80,25);
		//g.drawString(Integer.toString(intresized),80,40);
		
		//g.drawString("Invoer",35,25);
		
		//g.drawString(AlgebraExpressies.rb.getString("invoerVakLabel"),35,80);
		//g.drawString(AlgebraExpressies.rb.getString("bewerkingenLabel"),20,145);
		
		String s = null;
		FontMetrics fm = g.getFontMetrics();
		int lengte = 0;
		
		s = AlgebraExpressies.rb.getString("invoerVakLabel");
		lengte = fm.stringWidth(s);
		if(lengte>100) 
		{	g.setFont(new Font(g.getFont().getName(),Font.PLAIN, g.getFont().getSize()-1));
			fm = g.getFontMetrics();
			lengte = fm.stringWidth(s);
		}
		g.drawString(s,55-lengte/2,80);
		
		s = AlgebraExpressies.rb.getString("bewerkingenLabel");
		lengte = fm.stringWidth(s);
		if(lengte>100) 
		{	g.setFont(new Font(g.getFont().getName(),Font.PLAIN, g.getFont().getSize()-1));
			fm = g.getFontMetrics();
			lengte = fm.stringWidth(s);
		}
		g.drawString(s,55-lengte/2,145);
		
	}
	
	public void zetSchuiver(SchuifComponent sc)
	{	schuiflaag.add(sc,0);
		AlgebraSchuifComponent asc = (AlgebraSchuifComponent)sc;
		for(int i=0 ; i<asc.aantalPu ; i++)
		{	if(asc.pijlUit[i]!=null)schuiflaag.add(asc.pijlUit[i]);
		}
		if(asc.pijlIn1!=null)schuiflaag.add(asc.pijlIn1);
		if(asc.pijlIn2!=null)schuiflaag.add(asc.pijlIn2);
		if(asc instanceof GrafiekComponent)
		{	GrafiekComponent gsc = (GrafiekComponent)asc;
			for(int i=0 ; i<gsc.aantalPijlenIn ; i++)
			{	schuiflaag.add(gsc.pijlenIn[i]);
			}
		}
		tekenOpnieuw();
	}
	
	public void losSchuiver(SchuifComponent sc)
	{	add(sc,0);
		AlgebraSchuifComponent asc = (AlgebraSchuifComponent)sc;
		for(int i=0 ; i<asc.aantalPu ; i++)
		{	if(asc.pijlUit[i]!=null)add(asc.pijlUit[i]);
		}
		if(asc.pijlIn1!=null)add(asc.pijlIn1);
		if(asc.pijlIn2!=null)add(asc.pijlIn2);
		
		if(asc instanceof GrafiekComponent)
		{	GrafiekComponent gsc = (GrafiekComponent)asc;
			for(int i=0 ; i<gsc.aantalPijlenIn ; i++)
			{	add(gsc.pijlenIn[i]);
			}
		}
		tekenOpnieuw();
	}
	
	public void zetStapel(AlgebraSchuifComponent asc)
	{	int x = asc.getLocation().x;
		int y = asc.getLocation().y;
		int b = asc.getSize().width;
		int h = asc.getSize().height;
		//if(asc instanceof InvoerSchuifComponent)
		//{ schuifcomponenten[aantalSc] = new InvoerSchuifComponent(this ,x,y,b,h);
		//}
		if(asc instanceof UitvoerSchuifComponent)
		{ schuifcomponenten[aantalSc] = new UitvoerSchuifComponent(this ,x,y,b,h);
		}
		else if(asc instanceof OptelSchuifComponent)
		{ schuifcomponenten[aantalSc] = new OptelSchuifComponent(this ,x,y,b,h);
		}
		else if(asc instanceof AftrekSchuifComponent)
		{ schuifcomponenten[aantalSc] = new AftrekSchuifComponent(this ,x,y,b,h);
		}
		else if(asc instanceof VermenigvuldigSchuifComponent)
		{ schuifcomponenten[aantalSc] = new VermenigvuldigSchuifComponent(this ,x,y,b,h);
		}
		else if(asc instanceof DeelSchuifComponent)
		{ schuifcomponenten[aantalSc] = new DeelSchuifComponent(this ,x,y,b,h);
		}
		else if(asc instanceof KwadraatSchuifComponent)
		{ schuifcomponenten[aantalSc] = new KwadraatSchuifComponent(this ,x,y,b,h);
		}
		else if(asc instanceof WortelSchuifComponent)
		{ schuifcomponenten[aantalSc] = new WortelSchuifComponent(this ,x,y,b,h);
		}
		else if(asc instanceof MachtSchuifComponent)
		{ schuifcomponenten[aantalSc] = new MachtSchuifComponent(this ,x,y,b,h);
		}
		else return;
		schuifcomponenten[aantalSc].voegPijlToe(new Pijl(this));
		add(schuifcomponenten[aantalSc]);
		aantalSc++;
		
		if(!buttonsAdded)
		{	getParent().add(grafiekCheckbox,0);
			getParent().add(wisKnop,0);
			getParent().add(ip,0);
			buttonsAdded = true;
		}
	}
	
	
	
	public void verwijder(AlgebraSchuifComponent sc)
	{	for(int i=0 ; i<aantalSc ; i++)
		{	if(schuifcomponenten[i]==sc)
			{	if(sc instanceof GrafiekComponent)
				{	GrafiekComponent gsc = (GrafiekComponent)sc;
					while(gsc.aantalPijlenIn >0)
					{	Pijl p = gsc.pijlenIn[0];
						gsc.maakLos(gsc.pijlenIn[0]);
						p.zender.verwijderPijl();
						p.pijlTerug();
					}
				}
				if(sc.pijlIn1 != null)
				{	Pijl p = sc.pijlIn1;
					sc.maakLos(sc.pijlIn1);
					p.zender.verwijderPijl();
					p.pijlTerug();
				}
				if(sc.pijlIn2 != null)
				{	Pijl p = sc.pijlIn2;
					sc.maakLos(sc.pijlIn2);
					p.zender.verwijderPijl();
					p.pijlTerug();
					
				}	
				for(int k=0 ; k<sc.aantalPu ; k++)
				{	if(sc.pijlUit[k].ontvanger!=null)
					{	AlgebraSchuifComponent as = sc.pijlUit[k].ontvanger;
						as.maakLos(sc.pijlUit[k]);
						as.zetVeranderd(20);
					}					
					remove(sc.pijlUit[k]);
				}
				remove(sc);
				for(int j=i ; j<aantalSc ; j++)
				{	schuifcomponenten[j] = schuifcomponenten[j+1];
				}
				aantalSc--;
				tekenOpnieuw();
				return;
			}
		}
	}
	
	public void zetVeranderd()
	{	for(int i=0 ; i<aantalSc ; i++)
		{	if(schuifcomponenten[i]instanceof UitvoerSchuifComponent)
			{	boolean b = !ip.isExpr();
				((UitvoerSchuifComponent)schuifcomponenten[i]).zetToonWaarde(b);
			}
		}
		tekenOpnieuw();
	}
	
	public void setSize(int b, int h)
	{	for(int i=0 ; i<getComponentCount() ; i++)
		{	if(this.getComponent(i)instanceof Pijl)
			{	getComponent(i).setSize(b,h);
			}
		}
		super.setSize(b,h);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==wisKnop)
		{	int n = aantalSc;
			for(int i=0 ; i<n ; i++)
			{	verwijder(schuifcomponenten[0]);
			}
			maakStapel();
			if(grafiekCheckbox.getState())
			{	schuifcomponenten[aantalSc] = grafiekComponent;
				aantalSc++;
				add(grafiekComponent);
			}
		}
	}
	
	public void itemStateChanged(ItemEvent e)
	{	if(e.getSource()==grafiekCheckbox)
		{	boolean b = grafiekCheckbox.getState();
			if(b)
			{	grafiekComponent = new GrafiekComponent(this,500,200,200,230);
				schuifcomponenten[aantalSc] = grafiekComponent;
				aantalSc++;
				add(grafiekComponent);
			}
			else
			{	verwijder(grafiekComponent);
			}
			tekenOpnieuw();
		}
		
	}
	
	public void mousePressed(MouseEvent e)
	{	requestFocus();
		start = true;
		
	}	
	public void mouseDragged(MouseEvent e)
	{	
	}
	public void mouseReleased(MouseEvent e)
	{	
	
	}
	
	public void mouseMoved(MouseEvent e){;}
	
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
	
	
}
