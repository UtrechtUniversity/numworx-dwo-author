package fi.heks;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import fi.heks.scobjects.*;
import fi.beans.appletutil.*;
import fi.heks.vectortek.*;

public class Emmer extends ScContainer implements ActionListener//, FocusListener, MouseListener
{	
	private Tekening emmertekening;
	private Applet eigenaar;
	private AppletUtil au;
	private GetalComponent etiket;
	private ActionListener actionListener;
	private int inhoud;
	
	public Emmer(int x, int y, int b, int h, Applet applet)
	{	super(x,y,b,h);
		eigenaar = applet;
		au = new AppletUtil(eigenaar);
		
		etiket = new GetalComponent(b/6,h*3/7,b*2/3,h/3);
		etiket.zetInstelbaar(true);
		etiket.zetBekend(false);
		etiket.addActionListener(this);
		add(etiket,0);
		
		emmertekening = new Tekening(0,0,b,h,au,"emmer.gif");
		zetBegin();
		add(emmertekening);
		
		
	}
	
	public boolean raakt(int x, int y)
	{	int lx = getLocation().x;
		int ly = getLocation().y;
		if(emmertekening.contains(x-lx,y-ly))return true;
		else return false;
	}
	
	public int geefInhoud()
	{	return inhoud;
	}
	
	public void zetBegin()
	{	etiket.zetInstelbaar(true);
		zetInhoud(0);
		etiket.zetBekend(false);
	}
	
	public void zetInstelbaar(boolean b)
	{	etiket.zetInstelbaar(b);
	}
	
	public void zetInhoud(int aantal)
	{	inhoud = aantal;
		etiket.zetWaarde(aantal);
		Color vulkleur;
		if(aantal>0)vulkleur = Color.red;
		else vulkleur = new Color(0,100,255);
		if(aantal==0 || aantal==-999)
		{	for(int i=6 ; i<10 ; i++)
			{	emmertekening.to[i].zetVulkleur(null);
				emmertekening.to[i].zetLijnkleur(null);
			}
		}
		else if(Math.abs(aantal)==1)
		{	for(int i=6 ; i<10 ; i++)
			{	emmertekening.to[i].zetVulkleur(null);
				emmertekening.to[i].zetLijnkleur(null);
			}
			emmertekening.to[6].zetVulkleur(vulkleur);
			emmertekening.to[6].zetLijnkleur(Color.black);
			emmertekening.to[7].zetVulkleur(vulkleur);
			emmertekening.to[7].zetLijnkleur(Color.black);
		}
		else if(Math.abs(aantal)==2)
		{	for(int i=6 ; i<10 ; i++)
			{	emmertekening.to[i].zetVulkleur(null);
				emmertekening.to[i].zetLijnkleur(null);
			}
			emmertekening.to[7].zetVulkleur(vulkleur);
			emmertekening.to[7].zetLijnkleur(Color.black);
			emmertekening.to[6].zetVulkleur(vulkleur);
			emmertekening.to[6].zetLijnkleur(Color.black);
			emmertekening.to[8].zetVulkleur(vulkleur);
			emmertekening.to[8].zetLijnkleur(Color.black);
		}
		else
		{	for(int i=6 ; i<10 ; i++)
			{	emmertekening.to[i].zetVulkleur(vulkleur);
				emmertekening.to[i].zetLijnkleur(Color.black);
			}
		}
		emmertekening.repaint();
		
	}	
	
	public void addActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.add(actionListener, listener);
	}
	
	public void removeActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(etiket.isBekend())
		{	zetInstelbaar(false);
			zetInhoud(etiket.geefWaarde());
			if(actionListener!=null)
			{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""+this));
			}
		}
	}
}
