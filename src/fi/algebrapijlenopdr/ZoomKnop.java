package fi.algebrapijlenopdr;

import java.awt.*;
import java.awt.event.*;

public class ZoomKnop extends Component implements MouseListener	
{		private Image im;
	private Graphics gIm;		protected String code;
	private Font defaultfont = new Font("SansSerif", Font.PLAIN, 16);	private FontMetrics fm;
	protected Color bgColor = new Color(210,210,210);		
	protected Color fgColor = Color.black;	protected boolean focus = false;
	protected boolean actief = false;	
				public ZoomKnop(String s)
	{	code = s;
		addMouseListener(this);		setFont(defaultfont);
		fm = this.getFontMetrics(defaultfont);	}
	
		
	public void zetActief(boolean b)
	{	actief = b;
		repaint();
	}			public void setBackground(Color c)
	{	//bgColor = c;
		//bgUseColor = c;	}
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
	{			g.setColor(fgColor);
		{	g.setColor(bgColor);
			g.fillRect(0,0,getSize().width,getSize().height);			if(focus)
			{	if(actief)
					g.setColor(bgColor.darker());				else g.setColor(bgColor.brighter());
				g.drawLine(0,0,getSize().width-1,0);				g.drawLine(0,0,0,getSize().height-1);
				if(actief)
					g.setColor(bgColor.brighter());				else g.setColor(bgColor.darker());
				g.drawLine(getSize().width-1,0,getSize().width-1,getSize().height-1);
				g.drawLine(0,getSize().height-1,getSize().width-1,getSize().height-1);
			}		}		int b = getSize().width;
		int h = getSize().height;		g.setColor(Color.black);
		if(code.equals("zoominx"))		{	/*g.drawLine(3,12,6,9);
			g.drawLine(3,12,6,15);
			g.drawLine(4,12,7,9);
			g.drawLine(4,12,7,15);
			g.drawLine(3,12,10,12);
			g.drawLine(15,12,22,12);
			g.drawLine(19,9,22,12);
			g.drawLine(19,15,22,12);
			g.drawLine(18,9,21,12);
			g.drawLine(18,15,21,12);*/
			
			g.drawOval(8,3,8,8);
			g.drawLine(12,5,12,9);
			g.drawLine(10,7,14,7);
			
			g.drawLine(8,18,17,18);
			g.drawLine(8,18,10,16);
			g.drawLine(8,18,10,20);
			g.drawLine(17,18,15,16);
			g.drawLine(17,18,15,20);
		}		else if(code.equals("zoomuitx"))		{	/*g.drawLine(7,9,10,12);
			g.drawLine(7,15,10,12);
			g.drawLine(6,9,9,12);
			g.drawLine(6,15,9,12);
			g.drawLine(3,12,10,12);
			g.drawLine(15,12,22,12);
			g.drawLine(18,9,15,12);
			g.drawLine(18,15,15,12);
			g.drawLine(19,9,16,12);
			g.drawLine(19,15,16,12);*/
			
			g.drawOval(8,3,8,8);
			g.drawLine(10,7,14,7);
			
			g.drawLine(8,18,17,18);
			g.drawLine(8,18,10,16);
			g.drawLine(8,18,10,20);
			g.drawLine(17,18,15,16);
			g.drawLine(17,18,15,20);
			
			
		}		else if(code.equals("zoominy"))		{	g.drawLine(12,3,9,6);
			g.drawLine(12,3,15,6);
			g.drawLine(12,4,9,7);
			g.drawLine(12,4,15,7);
			g.drawLine(12,3,12,10);
			g.drawLine(12,15,12,22);
			g.drawLine(9,19,12,22);
			g.drawLine(15,19,12,22);
			g.drawLine(9,18,12,21);
			g.drawLine(15,18,12,21);
		}
		else if(code.equals("zoominysmal"))
		{	g.drawLine(4,3,1,6);
			g.drawLine(4,3,7,6);
			g.drawLine(4,4,1,7);
			g.drawLine(4,4,7,7);
			g.drawLine(4,3,4,10);
			g.drawLine(4,15,4,22);
			g.drawLine(1,19,4,22);
			g.drawLine(7,19,4,22);
			g.drawLine(1,18,4,21);
			g.drawLine(7,18,4,21);
		}		else if(code.equals("zoomuity"))		{	g.drawLine(9,7,12,10);
			g.drawLine(15,7,12,10);
			g.drawLine(9,6,12,9);
			g.drawLine(15,6,12,9);
			g.drawLine(12,3,12,10);
			g.drawLine(12,15,12,22);
			g.drawLine(9,19,12,15);
			g.drawLine(15,19,12,15);
			g.drawLine(9,19,12,18);
			g.drawLine(15,19,12,18);
		}
		else if(code.equals("zoomuitysmal"))
		{	g.drawLine(1,7,4,10);
			g.drawLine(7,7,4,10);
			g.drawLine(1,6,4,9);
			g.drawLine(7,6,4,9);
			g.drawLine(4,3,4,10);
			g.drawLine(4,15,4,22);
			g.drawLine(1,19,4,15);
			g.drawLine(7,19,4,15);
			g.drawLine(1,18,4,18);
			g.drawLine(7,18,4,18);
		}
		else if(code.equals("zoominxsmal"))
		{	g.drawOval(1,3,8,8);
			g.drawLine(5,5,5,9);
			g.drawLine(3,7,7,7);
			
			g.drawLine(1,18,10,18);
			g.drawLine(1,18,3,16);
			g.drawLine(1,18,3,20);
			g.drawLine(10,18,8,16);
			g.drawLine(10,18,8,20);
			
		}
		else if(code.equals("zoomuitxsmal"))
		{	g.drawOval(1,3,8,8);
			g.drawLine(3,7,7,7);
			
			g.drawLine(1,18,10,18);
			g.drawLine(1,18,3,16);
			g.drawLine(1,18,3,20);
			g.drawLine(10,18,8,16);
			g.drawLine(10,18,8,20);
		}		else if(code.equals("zoomin"))		{	g.drawOval(4,4,12,12);
			g.drawLine(7,10,13,10);
			g.drawLine(10,7,10,13);
			g.drawLine(14,14,20,20);
			g.drawLine(15,14,21,20);
		}		else if(code.equals("zoomuit"))		{	g.drawOval(4,4,12,12);
			g.drawLine(7,10,13,10);
			g.drawLine(14,14,20,20);
			g.drawLine(15,14,21,20);
		}		else if(code.equals("standaard"))		{	g.drawLine(4,12,20,12);
			g.drawLine(12,4,12,20);
			//g.drawLine(14,14,20,20);
			//g.drawLine(15,14,21,20);
		}
	}
	
	public void mousePressed(MouseEvent e)	{	actief = true;		repaint();
	}	
	public void mouseReleased(MouseEvent e) 
 	{	actief = false;		if ( isEnabled() )
 		{	produceAction("knop");
 		}		repaint();
 	}  
	
	public void mouseEntered(MouseEvent e)	{	focus = true;
		setCursor(new Cursor(Cursor.HAND_CURSOR ));
		repaint();
		produceAction("focus");	}
	public void mouseExited(MouseEvent e)
	{	focus = false;		setCursor(new Cursor(Cursor.DEFAULT_CURSOR ));
		repaint();
		produceAction("focus");	}
	public void mouseClicked(MouseEvent e){;}
	
//	ActionProducer
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
 	//
}