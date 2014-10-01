package fi.balansfruit;

import java.awt.*;
import java.awt.event.*;

import fi.beans.lwmobjects_swing.*;

/**
 * De LWMRootContainer van de balansfruit-applet.
 * Extra functies:
 * - controleert de stand van de balans door het totaalgewicht van de SchaalContainers op te vragen
 * - heeft een paint die afhankelijk is van de stand van de stand van de balans.
 * - NB: als de stand van de balans verandert, moeten de schaalcontainers meebewegen. Dit wordt
 * gedaan door een setSize(..), zodat iedere drop-positie boven de schaal goed is.
 * 
 * @author Paul Bergervoet
 *
 * @version 0, 28 augustus 2000
 */

class BalansFruit extends LWMRootContainer
	implements ActionListener, LWMListener
{	// Variables
	private Image balansEvenwicht;
	private Image balansLinks;				// links zwaarder
	private Image balansRechts;
	private SchaalContainer links;
	private SchaalContainer rechts;
	private LWMContainer voorraad;
	private int balanceStatus = 0;			// -1: links zwaarder 0: evenwicht 1: rechts zwaarder
	private boolean started = false;
	
	public BalansFruit( SchaalContainer l, SchaalContainer r, LWMContainer v, int w, int h)
	{	super(w, h);			// standaard LWMRootContainer zonder plaatje, zie paint.
		links = l;
		rechts = r;
		voorraad = v;
		balanceStatus = 0;		// init: beide schalen leeg
		started = false;
	}
	
	void setImages(Image be, Image bl, Image br)
	{	balansEvenwicht = be;
		balansLinks = bl;
		balansRechts = br;
	}
	
	void jeKanStarten()
	{	started = true;
		repaint();
	}
	
	public int getBalanceStatus() {
		return balanceStatus;
	}
	
	public void setBalance()
	{
		int newBalanceStatus;
		double wl = links.getTotalWeight();
		double wr = rechts.getTotalWeight();
		if ( wl > wr + 0.0000001 )						// links zwaarder?
		{	newBalanceStatus = -1;
		} else if ( wl < wr - 0.0000001 )					// rechts zwaarder?
		{	newBalanceStatus = 1;
		} else
		{	newBalanceStatus = 0;
		}
		//if ( newBalanceStatus != balanceStatus )	// stand veranderd?
		{	balanceStatus = newBalanceStatus;	// verzet status
			links.setSize(links.getWidth(), BalansFruitApplet.EVENWICHTY - balanceStatus* BalansFruitApplet.VERSCHIL);
										// links zwaarder => stat==-1 -> container groter
			rechts.setSize(rechts.getWidth(), BalansFruitApplet.EVENWICHTY + balanceStatus* BalansFruitApplet.VERSCHIL);
			repaint();
		}
		produceAction("setBalanced");
	}
	
	public void componentMoved(LWMComponent obj, LWMContainer from, LWMContainer to)
	{	setBalance();
	}
	
	public void paint(Graphics g)
	{	g.setColor(BalansFruitApplet.ACHTERGROND);
		g.fillRect(0, 0, getWidth(), getHeight());
		if ( !started )
		{	g.setFont(new Font("Serif", Font.BOLD, 24));
			g.setColor(Color.black);
			g.drawString("Ogenblikje", 20, 80);
			// DON'T paint components, their images are not ready yet
		} else
		{	if ( balanceStatus == -1 )
			{	g.drawImage(balansLinks, 0, BalansFruitApplet.OFFSETY, this);
			} else if ( balanceStatus == 0 )
			{	g.drawImage(balansEvenwicht, 0, BalansFruitApplet.OFFSETY, this);
			} else 	//  balanceStatus == 1
			{	g.drawImage(balansRechts, 0, BalansFruitApplet.OFFSETY, this);
			}
			super.paint(g);
		}
	}

/**
 * Reageer op de enige knop in de balansfruitapplet: "maak leeg"
 */
	public void actionPerformed(ActionEvent e)
	{	maakWeegschaalLeeg();
	}
	
	public void maakWeegschaalLeeg()
	{
		LWMComponent c;
		int teller;
		int aantal;
		aantal = links.getComponentCount();
		for ( teller=0; teller<aantal; teller++ )
		{	// NB: steeds component met index 0 weghalen. Na remove van de eerste component
			// uit 0..n blijft er een reeks 0..n-1 over!
			c = (LWMComponent)( links.getComponent(0) );
			removeLWMComponent(c);
			voorraad.addLWMComponent(c, teller*(200/aantal), 0);		// gravity will pull it down!
		}
		aantal = rechts.getComponentCount();
		for ( teller=0; teller<aantal; teller++ )
		{	c = (LWMComponent)( rechts.getComponent(0) );
			removeLWMComponent(c);
			voorraad.addLWMComponent(c, 250+teller*(200/aantal), 0);
		}
		balanceStatus = 0;
		repaint();
	}
	
	public void maakVoorraadLeeg()
	{
		LWMComponent c;
		int teller;
		int aantal;
		aantal = voorraad.getComponentCount();
		for ( teller=0; teller<aantal; teller++ )
		{	c = (LWMComponent)( voorraad.getComponent(0) );
			voorraad.removeLWMComponent(c);
			
		}
		
		repaint();
	}
	
	//ActionProducer
    private ActionListener actionListener = null;
    
    public void addActionListener(ActionListener l) 
    {   actionListener = AWTEventMulticaster.add(actionListener,l);
    }
    
    public void removeActionListener(ActionListener l)
    {   actionListener = AWTEventMulticaster.remove(actionListener, l);
    }   
    
    public void produceAction(String command)
    {   if (actionListener != null)
        {   actionListener.actionPerformed( new ActionEvent(this, 0, command) );
        }
    }
}
