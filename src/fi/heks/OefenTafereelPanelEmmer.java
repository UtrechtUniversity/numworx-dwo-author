package fi.heks;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.util.*;
import fi.heks.scobjects.*;
import fi.beans.appletutil.*;
import fi.heks.vectortek.*;

public class OefenTafereelPanelEmmer extends ScPanel implements  MouseListener, MouseMotionListener, ActionListener
{	
	private AppletUtil au;
	private ActionListener actionListener;
	private AchtergrondContainer achtergrond;
	
	private Tekening blokjePlus,blokjeMin, blokjeSleep, blokjeSleepMin;
	private Emmer emmerbinnen, emmerbuiten, emmerSleep;
	private Tekening pot, potinhoud, erinpijl, eruitpijl, vloer;
	private ZinkAnimatieEmmer za;
	private GetalComponent tc;
	private ScPanel sleeppanel;
	private Polygon[] p; 
	//private AudioClip plons, bubbel;
	
	private int laatstex, laatstey;
	private boolean instelbaar;
	private boolean  raakSleep, raakSleepMin;
	private boolean plusEruit, minEruit, pasEruit;
	private boolean[] kleurBlokjes = {true,false,true,true,true,true,false,false,false,false,true};
	
	private boolean  erinMogelijk, eruitMogelijk;
	private int aantalKerenGebruikt;
	private int emmerInhoud;
	
	private boolean actief = true;
	
	public OefenTafereelPanelEmmer(int x, int y, int b, int h, Applet applet)
	{	super(x,y,b,h);
		//setBackground(new Color(255,255,220));
		setBackground(getBackground());
			
		au = new AppletUtil(applet);
		//plons = au.getAudioClip("resources/watersplash.au");
		//bubbel = au.getAudioClip("resources/bubble.au");
		
		//raakPlusBuiten = false;
		//raakPlusBinnen = false;
		//raakMinBuiten = false;
		//raakMinBinnen = false;
		raakSleep = false;
		plusEruit = false;
		minEruit = false;
		pasEruit = false;
		erinMogelijk = false;
		eruitMogelijk = false;
		instelbaar = false;
		
		//actief = false;
		aantalKerenGebruikt = 0;
		
		Color color_01 = new Color(240,240,240);
		String kleurcode = applet.getParameter("color_01");
		if(kleurcode!=null)color_01 = new Color(Integer.parseInt(kleurcode.substring(1),16));
		
		sleeppanel = new ScPanel(0,0,b,h-5);
		//sleeppanel.setBackground(color_01);
		sleeppanel.addMouseListener(this);
		sleeppanel.addMouseMotionListener(this);
		achtergrond = new AchtergrondContainer(0,0,b,h-5);
		
		pot = new Tekening(40,210,350,300,au,"potnieuw.gif");
		achtergrond.add(pot);
		
		erinpijl = new Tekening(20,20,250,180,au,"gebogenpijl.gif");
		erinpijl.setVisible(false);
		sleeppanel.add(erinpijl);
		
		eruitpijl = new Tekening(230,20,250,180,au,"gebogenpijl.gif");
		eruitpijl.setVisible(false);
		sleeppanel.add(eruitpijl);
		
		za = new ZinkAnimatieEmmer(110,240,210,200,applet);
		//za.zetBellenAan(false);
		sleeppanel.add(za,0);
		
		potinhoud = new Tekening(40,210,350,300,au,"inhoudnieuw.gif");
		sleeppanel.add(potinhoud, 0);
		
		emmerbinnen = new Emmer(120,300,110,125,applet);
		emmerbinnen.addActionListener(this);
		sleeppanel.add(emmerbinnen,0);
						
		emmerbuiten = new Emmer(360,15,110,125,applet);
		emmerbuiten.addActionListener(this);
		sleeppanel.add(emmerbuiten);
		
		emmerSleep = new Emmer(-150,-150,110,125,applet);
		sleeppanel.add(emmerSleep,0);
		
		
		
		
		
		
		
		vloer = new Tekening(0,365,430,160, au,"vloer.gif");
		//achtergrond.add(vloer);
		
		tc = new GetalComponent(360,300,130,50);
		tc.zetAlsTemp(true);
		tc.zetWaarde(0);
		sleeppanel.add(tc,0);
		sleeppanel.add(achtergrond);
		add(sleeppanel);
	}
	
	
	public void zetErinMogelijk()
	{	erinMogelijk = true;
		eruitMogelijk = false;
		eruitpijl.setVisible(false);
		erinpijl.setVisible(true);	
		
	}
	
	public void zetEruitMogelijk()
	{	erinMogelijk = false;
		eruitMogelijk = true;
		eruitpijl.setVisible(true);
		erinpijl.setVisible(false);
	}
	
	public void zetOpnieuw()
	{	emmerbuiten.setVisible(true);
		emmerbinnen.setVisible(true);
		emmerbuiten.zetBegin();
		emmerbinnen.zetBegin();
		erinMogelijk = false;
		eruitMogelijk = false;
		eruitpijl.setVisible(false);
		erinpijl.setVisible(false);
		tc.zetWaarde(0);
		//za.start();
	}
	
	public void stop()
	{	//za.stop();
	}
	
	public void zetBeginTemp(int temp)
	{	tc.zetWaarde(temp);
	}
	
	public void zetInstelbaar(boolean b)
	{	instelbaar = b;
		tc.zetInstelbaar(b);
		if(b)
		{	tc.addActionListener(this);
		}
		else
		{	tc.removeActionListener(this);
		}
	}
	
	public void zetActief(boolean b)
	{	actief = b;
		//if(!b)
		//{	tc.zetBekend(b);
		//}
		emmerbinnen.zetInstelbaar(b);
		emmerbuiten.zetInstelbaar(b);
		tc.repaint();
	}
	
	public void zetGebruikt(int aantal)
	{	aantalKerenGebruikt = aantal;
		tc.zetBekend(false);
		tc.repaint();
	}
	public int geefEmmerInhoud()
	{	return emmerInhoud;
	}
	
	public int geefGebruikt()
	{	return aantalKerenGebruikt;
	}
	
	public int geefTemp()
	{	return tc.geefWaarde();
	}
	
	public void addActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.add(actionListener, listener);
	}
	
	public void removeActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}
	
	public void mousePressed(MouseEvent e)
	{	if(!actief)return;
		laatstex = e.getX();
		laatstey = e.getY();
		//if(instelbaar)tc.zetInstelbaar(false);
		
		//p = new Polygon[11];
		//for (int i=0 ; i<11 ; i++)
		//{	p[i] = ((VeelhoekTek)(potinhoud.to[i])).basisPolygon;
		//}
		
		if(emmerbuiten.raakt(e.getX(),e.getY()) && erinMogelijk)
		{	emmerSleep.setLocation((int)(schaal*360),(int)(schaal*15));
		}
			
		if(emmerbinnen.raakt(e.getX(),e.getY()) && eruitMogelijk)
		{	emmerSleep.setLocation((int)(schaal*120), (int)(schaal*300));
			plusEruit = true;
		}
		
		if(emmerSleep.raakt(e.getX(),e.getY()))
		{   raakSleep = true;
		}
		
		
		//int x = e.getX()-potinhoud.getLocation().x;
		//int y = e.getY()-potinhoud.getLocation().y;
		
		
		
		/*for(int i=10 ; i>-1 ; i--)
		{	if(p[i].contains(x,y))
			{	if(kleurBlokjes[i] && eruitMogelijk)
				{	blokjeSleep.setLocation(e.getX()-blokjeSleep.getSize().width/2,e.getY()-blokjeSleep.getSize().height/2);
					blokjeSleep.repaint();
					plusEruit = true;
				}
				else if(eruitMogelijk)
				{	blokjeSleepMin.setLocation(e.getX()-blokjeSleep.getSize().width/2,e.getY()-blokjeSleep.getSize().height/2);
					blokjeSleepMin.repaint();
					minEruit = true;
				}
				
				return;
			}
		}*/
		
	}	
	public void mouseDragged(MouseEvent e)
	{	if(!actief)return;
		int dx = e.getX() - laatstex;
		int dy =  e.getY() - laatstey;
				
		if(raakSleep)
		{	emmerSleep.setLocation(emmerSleep.getLocation().x + dx, emmerSleep.getLocation().y + dy);
			Polygon p = ((VulKrommeTek)(pot.to[2])).buigPolygon;
			int lx = pot.getLocation().x;
			int ly = pot.getLocation().y;
			for(int i=0 ; i<p.npoints ; i++)
			{	if(emmerSleep.raakt(p.xpoints[i] + lx, p.ypoints[i] + ly))
				{	emmerSleep.setLocation(emmerSleep.getLocation().x - dx, emmerSleep.getLocation().y - dy);
					raakSleep = false;
				}
			}
			emmerSleep.repaint();
		}
		
		else if(emmerSleep.raakt(e.getX(),e.getY()))
		{   raakSleep = true;
		}
		
		if(!plusEruit && emmerSleep.getLocation().x + emmerSleep.getSize().width < za.getLocation().x + za.getSize().width 
			&& emmerSleep.getLocation().x > za.getLocation().x 
			&& emmerSleep.getLocation().y > za.getLocation().y
			&& emmerSleep.getLocation().y + emmerSleep.getSize().height < za.getLocation().y  + za.getSize().height)
		{	//plons.play();
			za.start(true,emmerSleep.getLocation().x - za.getLocation().x );
			tc.verhoog(emmerInhoud);
			if(actionListener!=null)
			{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "erin"));
			}
			if(!erinMogelijk)emmerSleep.setLocation(-150,-150);
			else emmerSleep.setLocation((int)(emmerbuiten.getLocation().x),(int)(emmerbuiten.getLocation().y));
			raakSleep = false;
			pasEruit = false;
		}
		
		
		if(plusEruit && emmerSleep.getLocation().y  < za.getLocation().y )
		{	plusEruit = false;
			pasEruit = true;
			//bubbel.play();
			tc.verlaag(emmerInhoud);
			if(actionListener!=null)
			{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,"eruit"));
			}
		}
		
		laatstex = e.getX();
		laatstey = e.getY();
	}
	
	public void mouseReleased(MouseEvent e)
	{	raakSleep = false;
		if(instelbaar)tc.zetInstelbaar(true);
		
		if(!plusEruit &&  emmerSleep.getLocation().x + emmerSleep.getSize().width < pot.getLocation().x + pot.getSize().width 
			&& emmerSleep.getLocation().x > pot.getLocation().x
		   && emmerSleep.getLocation().y + emmerSleep.getSize().height < za.getLocation().y  + za.getSize().height)
		{	
			int x = emmerSleep.getLocation().x;
			//if(!erinMogelijk)
				emmerSleep.setLocation(-150,-150);
			//else 
			//emmerSleep.setLocation((int)(emmerbuiten.getLocation().x),(int)(emmerbuiten.getLocation().y));
			//plons.play();
			za.start(true,x - za.getLocation().x );
			tc.verhoog(emmerInhoud);
			if(actionListener!=null)
			{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,"erin"));
			}
		}
		else
		{	//if(!erinMogelijk)
				emmerSleep.setLocation(-150,-150);
			//else emmerSleep.setLocation((int)(emmerbuiten.getLocation().x),(int)(emmerbuiten.getLocation().y));
		}
		
		
		plusEruit = false;
		minEruit = false;
		pasEruit = false;
		//((TweeManierenPanel)(getParent())).controleer();
	}
	
	public void mouseMoved(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==emmerbuiten)
		{	zetErinMogelijk();
			emmerInhoud=emmerbuiten.geefInhoud();
			emmerbinnen.setVisible(false);
			emmerSleep.zetInhoud(emmerInhoud);
			za.zetInhoud(emmerInhoud);
			
		}
		else if(e.getSource()==emmerbinnen)
		{	zetEruitMogelijk();
			emmerInhoud=emmerbinnen.geefInhoud();
			emmerbuiten.setVisible(false);
			emmerSleep.zetInhoud(emmerInhoud);
			za.zetInhoud(emmerInhoud);
			
		}
		if(actionListener!=null)
			{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,"instelling"));
			}
			
		//if(tc.isBekend())
		//{	actief = true;
		//	aantalKerenGebruikt++;
		//}
		//else actief = false;
	}
}
