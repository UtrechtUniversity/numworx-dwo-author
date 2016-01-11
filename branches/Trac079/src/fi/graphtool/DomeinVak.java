package fi.graphtool;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import fi.wiskopdr.ImageComponent;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.FormuleElement;
import fi.wiskopdr.formuleobjects.FormuleVak;
import fi.wiskopdr.formuleobjects.FormuleVakHouder;
import fi.wiskopdr.formuleobjects.Tablet;
import fi.wiskopdr.formuleobjects.TabletOwner;
import fi.wiskopdr.tekstobjects.TekstElement;

public class DomeinVak extends JPanel implements ActionListener, MouseListener, FormuleVakHouder {
	
	private Component formuleComponent;
	private int ashoogte;
	private int minBreedte;
	private boolean tabletAan;
	
	private Font formuleVakFont = (WiskOpdr.mac||WiskOpdr.zoefi) ? WiskOpdr.formuleFont1Mac : WiskOpdr.formuleFont1; //new Font("TimesRoman",Font.PLAIN,16);
	
	
	public DomeinVak()
	{
		setLayout(null);
		addMouseListener(this);
		if(!WiskOpdr.formTimes) formuleVakFont = WiskOpdr.tekstFont;
		
		formuleComponent = new FormuleVak();
		formuleComponent.setFont(formuleVakFont);
		((FormuleVak)formuleComponent).setBorder(false);
		((FormuleVak)formuleComponent).addActionListener(this);
		formuleComponent.setLocation(0,0);
		add(formuleComponent);
		
		setOpaque(false);
	}
	
	 public int getAsHoogte()
	    {
	        return ((FormuleElement)formuleComponent).ashoogte + (getFontMetrics(formuleVakFont)).getAscent()/2;
	    }
		
		public FormuleVak geefFormuleVak()
		{	return (FormuleVak)formuleComponent;
		}
		
		public void zetTabletAan(boolean b)
		{	tabletAan = b;
		}
		
		public void paintComponent(Graphics g)
		{	g.setColor(Color.white);
			g.fillRect(1,2,getSize().width-2, getSize().height-4);
			
			g.setColor(Color.gray);
			g.drawRect(1,2,getSize().width-2, getSize().height-4);
			
		}
		
		public void zetMinBreedte(int b)
		{	minBreedte = b;	
		}
		
		public void zetMaat()
		{	setSize(Math.max(minBreedte, formuleComponent.getSize().width+20), formuleComponent.getSize().height+8);
			formuleComponent.setLocation(4,4);
			ashoogte = ((FormuleElement)formuleComponent).ashoogte+3;
			
		}
		
		public int geefAsHoogte()
		{
			return ashoogte + (getFontMetrics(formuleVakFont)).getAscent()/2;
		}
		
		public void actionPerformed(ActionEvent e) 
		{	if(e.getSource()==formuleComponent && e.getActionCommand().equals("focus"))
			{	zetTabletUser();
			}
			if(e.getSource()==formuleComponent && e.getActionCommand().equals("zetMaat") )
			{	zetMaat();
				this.getParent().doLayout();
			}
		}

		public void activateTablet()
		{	
			Container parent = getParent();
			int x = parent.getLocation().x;
			int y = parent.getLocation().y;
			int h = parent.getSize().height;
			for(int i=0 ; parent!=null && i<40 ; i++)
			{	if(parent instanceof TabletOwner) 
				{	((TabletOwner)parent).addTablet(this, x+20, y+h+20);
					Tablet tablet = ((TabletOwner)parent).getTablet();
					if(tablet==null) break;
					int tx = Math.min(parent.getSize().width-tablet.getSize().width, x+20);
					int ty = y+h+20+tablet.getSize().height>parent.getSize().height ? y-tablet.getSize().height-10 : y+h+20;
					tablet.setLocation(tx, ty);
					break;
				}
				else 
				{	parent = parent.getParent();
					if(parent==null)return;
					x += parent.getLocation().x;
					y += parent.getLocation().y;
				}
			}
			
		}
		
		public void zetTabletUser()
		{	Container parent = getParent();
			for(int i=0 ; parent!=null && i<40 ; i++)
			{	if(parent instanceof TabletOwner) 
				{	((TabletOwner)parent).zetTabletUser(this);
					break;
				}
				else 
				{	parent = parent.getParent();
				}
			}
		}
		
		public void mousePressed(MouseEvent e)
		{	formuleComponent.requestFocus();
			((FormuleVak)formuleComponent).zetOpEind();
				activateTablet();
		}
		
		public void mouseClicked(MouseEvent e){;}
		public void mouseReleased(MouseEvent e){;}
		public void mouseEntered(MouseEvent e){;}
		public void mouseExited(MouseEvent e){;}

		/*
		
		//ActionProducer
		private ActionListener actionListener = null;
		
		public void addActionListener(ActionListener l) 
	 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
	 	}
	 	
	 	public void removeActionListener(ActionListener l)
	 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
	 	}	
	 	
	 	public void produceAction(String command)
	 	{	if (actionListener != null)
	 		{	actionListener.actionPerformed( new ActionEvent(this, 0, command) );
	 		}
	 	}
	 	//end ActionProducer
	 	  */
	 	 
}
