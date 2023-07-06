package fi.wiskopdr;

import java.awt.*;
import java.awt.event.*;

public class LWCheckbox extends Component implements MouseListener	
{		private Image im;
	private Graphics gIm;		protected Color bgColor = new Color(210,210,210);		
	protected Color fgColor = Color.black;	protected boolean aan = false;	
	protected ActionListener actionListener = null;
			public LWCheckbox()
	{	addMouseListener(this);	}
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}
  
	
	public void zetAan(boolean b)
	{	aan = b;
		repaint();
	}		
	public boolean isAan()
	{	return aan;
	}	public void setBackground(Color c)
	{	bgColor = c;
	}
		public void paint(Graphics g)
	{	{ 	if(im==null)
			{	im = createImage(getSize().width,getSize().height);
  				gIm = im.getGraphics();
			}
			gIm.setColor(getBackground());
			gIm.fillRect(0,0,getSize().width,getSize().height);
			paintBuffer(gIm);
			g.drawImage(im, 0, 0, null);
  		}
	}
	
	public void update(Graphics g)
	{	paint(g);
	}	
	public void paintBuffer(Graphics g)
	{			g.setColor(bgColor);
		g.fillRect(0,0,getSize().width,getSize().height);		g.setColor(fgColor);
		g.drawRect(0,0,getSize().width-1,getSize().height-1);		if(aan)
		{	g.drawLine(2,getSize().height/2-1,getSize().width/3,getSize().height-3);			g.drawLine(2,getSize().height/2-2,getSize().width/3,getSize().height-4);			g.drawLine(2,getSize().height/2-3,getSize().width/3,getSize().height-5);						g.drawLine(getSize().width/3,getSize().height-3,getSize().width-3,3);			g.drawLine(getSize().width/3,getSize().height-4,getSize().width-3,2);			g.drawLine(getSize().width/3,getSize().height-5,getSize().width-3,1);
		}	}
	public void mousePressed(MouseEvent e)	{	if(!aan)aan = true;
		else aan = false;		repaint();
	}	
	public void mouseReleased(MouseEvent e) 
 	{	if (actionListener != null)
 		{	actionListener.actionPerformed( new ActionEvent(this, 0, "") );
 		}
		repaint();
 	}  
	
	public void mouseEntered(MouseEvent e)	{		}
	public void mouseExited(MouseEvent e)
	{		}
	public void mouseClicked(MouseEvent e){;}
	
}