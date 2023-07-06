package fi.nabouwenaanzichten;

import java.awt.*;
import java.awt.event.*;

public class KeuzeLijst extends Component implements MouseListener
{	
	private String[] items;
	private boolean[] gemaakt;
	private boolean[] gemaaktPlus;
	private int aantalItems;
	private int maxItemBreedte;
	private int itemHoogte;
	private int aantalItemsPerKolom;
	private int selectieNummer;
	private ActionListener actionListener;
	private Font f;
	private FontMetrics fm ;
	
	public KeuzeLijst(int x, int y, int b, int h)
	{	setBounds(x,y,b,h);
		actionListener = null;
		addMouseListener(this);
		
		items = new String[100];
		gemaakt = new boolean[100];
		gemaaktPlus = new boolean[100];
		for(int i=0 ; i<100 ; i++)
		{	gemaakt[i] = false;
			gemaaktPlus[i] = false;
		}
		aantalItems = 0;
		selectieNummer = -1;
		itemHoogte = 18;
		maxItemBreedte = 0;
		aantalItemsPerKolom = h/itemHoogte;
		f = new Font("SansSerif", Font.PLAIN,itemHoogte*2/3);
		fm = getFontMetrics(f);
	}
	
	public void paint(Graphics g)	
	{	g.setColor(new Color(255,255,255));
		g.fillRect(0,0,getSize().width-1, getSize().height-1);
		
		g.setColor(Color.lightGray);
		int x = selectieNummer/aantalItemsPerKolom*maxItemBreedte;
		int y = 5+selectieNummer%aantalItemsPerKolom*itemHoogte;
		if(selectieNummer!=-1)g.fillRect(x,y,maxItemBreedte,itemHoogte);
		
		g.setColor(Color.black);
		g.setFont(f);
		for(int i=0 ; i<aantalItems ; i++)
		{	x = 14+i/aantalItemsPerKolom*maxItemBreedte;
			y = 5+i%aantalItemsPerKolom*itemHoogte + fm.getAscent() + fm.getLeading()/2 ;
			if(gemaakt[i])
			{	g.setColor(new Color(255,255,0));
				if(gemaaktPlus[i])g.setColor(new Color(0,180,0));
				g.fillOval(x-10,y-8,itemHoogte/3+1,itemHoogte/3+1);
				g.setColor(Color.black);
				g.drawOval(x-10,y-8,itemHoogte/3,itemHoogte/3);
			}
			g.drawString(items[i],x,y);
		}
		g.setColor(Color.black);
		g.drawRect(0,0,getSize().width-1, getSize().height-1);
	}
	
	public void voegItemToe(String s)
	{	items[aantalItems] = s;
		aantalItems++;
		maxItemBreedte = Math.max(fm.stringWidth(s)+20,maxItemBreedte);
	}
	
	public int geefSelectieNummer()
	{	return selectieNummer;
	}
	
	public void zetSelectieNummer(int n)
	{	selectieNummer = n;
		repaint();
	}
	
	public void zetGemaakt(boolean b)
	{	if(selectieNummer!=-1)
		{	boolean bool = gemaakt[selectieNummer];
			gemaakt[selectieNummer] = b;
			if(b!=bool)repaint();
		}
	}
	
	public void zetGemaaktPlus(boolean b)
	{	if(selectieNummer!=-1)
		{	boolean bool = gemaaktPlus[selectieNummer];
			gemaaktPlus[selectieNummer] = b;
			if(b!=bool)repaint();
		}
	}
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}
	
	public void mouseClicked(MouseEvent e){;}
	public void mousePressed(MouseEvent e)
	{	for(int i=0 ; i<aantalItems  ; i++)
		{	int x = i/aantalItemsPerKolom*maxItemBreedte;
			int y = i%aantalItemsPerKolom*itemHoogte;
			Rectangle r = new Rectangle(x,y,maxItemBreedte,itemHoogte);
			if(r.contains(e.getX(), e.getY()))
			{	if(selectieNummer==i)return;//selectieNummer = -1;
				else selectieNummer = i;
				break;
			}
		}
		repaint();
		if (actionListener != null)
 		{	actionListener.actionPerformed( new ActionEvent(this, 0, null) );
 		}
	}
	public void mouseReleased(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
}
