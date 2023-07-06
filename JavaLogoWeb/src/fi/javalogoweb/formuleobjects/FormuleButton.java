package fi.javalogoweb.formuleobjects;

import java.awt.*;
import java.awt.event.*;
//import fi.wiskopdr.WiskOpdr;

public class FormuleButton extends Panel implements MouseListener	
{	
	private Image im;
	private Graphics gIm;
	
	protected String code;
	private Font defaultfont = new Font("SansSerif", Font.PLAIN, 13);
	private FontMetrics fm;
	protected Color bgColor = new Color(210,210,210);	
	protected Color fgColor = Color.black;
	protected Color bgUseColor = bgColor;
	protected Color fgUseColor = fgColor;
	private Color vlakkleur;
	private int kubusNummer;
	private Rectangle[] zijvlakken;
	protected boolean focus = false;
	protected boolean actief = false;
	protected boolean focusable = true;
	
	
	protected ActionListener actionListener = null;

		
	public FormuleButton(String s)
	{	code = s;
		addMouseListener(this);
		setFont(defaultfont);
		fm = this.getFontMetrics(defaultfont);
	}
	
	public void setFocusable(boolean b)
	{	focusable = b;
	}
	
	public String getCode()
	{	return code;
	}
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}
  
	
	public void zetActief(boolean b)
	{	actief = b;
		repaint();
	}
		
	public void setBackground(Color c)
	{	//bgColor = c;
		//bgUseColor = c;
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
	
	public void paintBuffer(Graphics gr)
	{	
		Graphics g;
	        /*if(WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("GR"))
	        {     g = (Graphics2D)gr;
	              ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        }
	        else */
	        g=gr;
		g.setColor(fgColor);
		{	g.setColor(bgColor);
			g.fillRect(0,0,getSize().width,getSize().height);
			if(!focusable || focus)
			{	if(actief)g.setColor(bgColor.darker());
				else g.setColor(bgColor.brighter());
				g.drawLine(0,0,getSize().width-1,0);
				g.drawLine(0,0,0,getSize().height-1);
				if(actief)g.setColor(bgColor.brighter());
				else g.setColor(bgColor.darker());
				g.drawLine(getSize().width-1,0,getSize().width-1,getSize().height-1);
				g.drawLine(0,getSize().height-1,getSize().width-1,getSize().height-1);
			}
		}
		int b = getSize().width;
		int h = getSize().height;
		g.setColor(Color.black);
		if(code.equals("wortel"))
		{	g.drawLine(3,2*h/4,h/4,h-3);
			g.drawLine(4,2*h/4,h/4+1,h-3);
			g.drawLine(h/4+1,h-3,h/2,3);
			g.drawLine(h/2,3,b-3,3);
            g.setColor(Color.white);
            g.fillRect(h/2+2,6,4,10);
            g.setColor(Color.gray);
			g.drawRect(h/2+2,6,4,10);
		}
		else if(code.equals("macht"))
		{	g.setColor(Color.white);
            g.fillRect(6,6,4,10);
            g.fillRect(13,3,3,6);
            g.setColor(Color.gray);
            g.drawRect(6,6,4,10);
			g.drawRect(13,3,3,6);
		
		}
		else if(code.equals("kwadraat"))
		{	g.setColor(Color.white);
            g.fillRect(6,6,4,10);
            g.setColor(Color.gray);
            g.drawRect(6,6,4,10);
            g.setColor(Color.black);
            g.setFont(new Font("SansSerif",Font.PLAIN, 8));
			g.drawString("2",13,8);
		}
		else if(code.equals("breuk"))
		{	g.setColor(Color.white);
            g.fillRect(8,3,4,5);
            g.fillRect(8,12,4,5);
            g.setColor(Color.gray);
            g.drawRect(8,3,4,5);
            g.drawRect(8,12,4,5);
            g.setColor(Color.black);
			g.drawLine(7,10,13,10);
			
		}
		else if(code.equals("haakjes"))
		{	g.drawString("(",3,15);
			g.drawString(")",13,15);
            g.setColor(Color.white);
            g.fillRect(8,5,4,10);
            g.setColor(Color.gray);
            g.drawRect(8,5,4,10);
            
		}
		else if(code.equals("ndewortel"))
		{	g.drawLine(3,2*h/4+3,h/4,h-3);
			g.drawLine(4,2*h/4+3,h/4+1,h-3);
			g.drawLine(h/4+1,h-3,h/2,3);
			g.drawLine(h/2,3,b-3,3);
            g.setColor(Color.white);
			g.fillRect(h/2+2,6,4,10);
			g.fillRect(3,3,3,6);
            g.setColor(Color.gray);
            g.drawRect(h/2+2,6,4,10);
            g.drawRect(3,3,3,6);
		}
		else if(code.equals("ndelog"))
		{	g.drawString("log",7,15);
			g.setColor(Color.white);
			//g.fillRect(h/2+12,6,4,10);
			g.fillRect(3,3,3,6);
	        g.setColor(Color.gray);
	        //g.drawRect(h/2+12,6,4,10);
	        g.drawRect(3,3,3,6);
		}
		else if(code.equals("formule"))
		{	g.drawString("F",7,15);
		}
		else if(code.equals("tablet"))
		{	g.setColor(Color.gray);
			g.drawRect(2,2,11,11);
			for(int i=1 ; i<4 ; i++)
			{	for(int j=1 ; j<4 ; j++)
				{ 	g.drawRect(3*i+1,3*j+1,1,1);
				}
			}
			
		}
		else if(code.equals("plus"))
		{	g.drawLine(b/4+2,h/2,3*b/4-2,h/2);
			g.drawLine(b/2,h/4+2,b/2,3*h/4-2);
		}
		else if(code.equals("min"))
		{	g.drawLine(b/4+2,h/2,3*b/4-2,h/2);
		}
		else if(code.equals("maal"))
		{	g.drawLine(b/4+2,h/4+2,3*b/4-2,3*h/4-2);
			g.drawLine(b/4+2,3*h/4-2,3*b/4-2,h/4+2);
		}
		else if(code.equals("deel"))
		{	g.fillRect(b/2,h/4+1,2,2);
			g.fillRect(b/2,3*h/4-2,2,2);
			g.drawLine(b/4+2,b/2,3*b/4-2,b/2);
		}
		/*else if(code.equals("haakjesWeg"))
		{	if(WiskOpdr.mobileVersion)
			{	g.setFont(new Font("SansSerif", Font.PLAIN, 11));
				g.drawString("(",3,12);
				g.drawString(")",9,12);
			}
			else
			{	g.drawString("(",5,15);
				g.drawString(")",11,15);
			}
			g.drawLine(b/5,h/5,4*b/5,4*h/5);
		}*/
		else if(code.equals("back"))
		{	g.drawLine(b/4+2,h/2,3*b/4-2,h/2);
			g.drawLine(b/4+2,h/2+1,3*b/4-2,h/2+1);
			g.drawLine(b/4+2,h/2,b/4+5,h/2-3);
			g.drawLine(b/4+2,h/2+1,b/4+5,h/2-2);
			g.drawLine(b/4+2,h/2,b/4+5,h/2+3);
			g.drawLine(b/4+2,h/2+1,b/4+5,h/2+4);
		}
		else if(code.equals("pi"))
		{	g.setFont(new Font("TimesRoman", Font.ITALIC , 16));
			g.drawString("\u03C0",5,15);
			//g.setFont(new Font("SansSerif", Font.PLAIN, 13));
		}
		else if(code.equals("123"))
		{	g.setFont(new Font("SansSerif", Font.PLAIN, 10));
			
			/*if(WiskOpdr.mobileVersion)
			{	g.setFont(new Font("SansSerif", Font.PLAIN, 8));
			}*/
			g.drawString(code,1,3*getSize().height/4);
			g.setFont(new Font("SansSerif", Font.PLAIN, 13));
		}
		else 
		{	
			int w = g.getFontMetrics().stringWidth(code);
			g.drawString(code,(b-w)/2,3*getSize().height/4);
			
		}
		
	}
	
	public void mousePressed(MouseEvent e)
	{	actief = true;
		//if(focusable)
			repaint();
	}
	
	public void mouseReleased(MouseEvent e) 
 	{	actief = false;
		if ( isEnabled() )
 		{	if (actionListener != null)
 			{	actionListener.actionPerformed( new ActionEvent(this, 0, "knop") );
 			}
 		}
		//if(focusable)
			repaint();
 	}  
	
	public void mouseEntered(MouseEvent e)
	{	focus = true;
		setCursor(new Cursor(Cursor.HAND_CURSOR ));
		if(focusable)repaint();
	}
	public void mouseExited(MouseEvent e)
	{	focus = false;
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR ));
		if(focusable)repaint();
	}
	public void mouseClicked(MouseEvent e){;}
	
}