package fi.grafiek3dtest;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

import javax.swing.*;

import fi.grafiek3dtest.formuleobjects.FormuleButton;

public class ZoomKnop extends FormuleButton implements MouseListener	
{		//private Image im;
	//private Graphics gIm;
	
	Image zoomInImage, zoomUitImage;		protected String code;
	private Font defaultfont = new Font("SansSerif", Font.BOLD, 14);
	private Font defaultfont2 = new Font("SansSerif", Font.BOLD, 18);
	private Font defaultfont3 = new Font("SansSerif", Font.PLAIN, 10);
	private Font defaultfont4 = new Font("SansSerif", Font.PLAIN, 12);
		private FontMetrics fm;
	private FontMetrics fm2;
	private FontMetrics fm3;
	private FontMetrics fm4;
	protected Color bgColor = new Color(210,210,210);		
	protected Color fgColor = Color.black;	protected boolean focus = false;
	protected boolean actief = false;	
	int releaseCnt = 0;
	
	private static String[] imageNames = 
	{	"zoominknop.gif",
		"zoomuitknop.gif",
		"zoominxknop.gif",
		"zoomuitxknop.gif",
		"zoominyknop.gif",
		"zoomuityknop.gif",
	};
	
	private static Hashtable images;
	
	Image knopImage = null;
			
	public ZoomKnop(String s)
	{	code = s;
		addMouseListener(this);
		
		setFont(defaultfont);
		fm = this.getFontMetrics(defaultfont);
		fm2 = this.getFontMetrics(defaultfont2);
		fm3 = this.getFontMetrics(defaultfont3);
		fm4 = this.getFontMetrics(defaultfont4);
		
		setBorder(null);
		if (images == null)
		{	images = new Hashtable();
			//Grafiek3DTest.loadImages(images, imageNames);
		}
	}
		public ZoomKnop(String s, Image knopIm)
	{	code = s;
		addMouseListener(this);
		
		knopImage = knopIm;
		
		setFont(defaultfont);
		fm = this.getFontMetrics(defaultfont);
		fm2 = this.getFontMetrics(defaultfont2);
		fm3 = this.getFontMetrics(defaultfont3);
		fm4 = this.getFontMetrics(defaultfont4);
		
		setBorder(null);
		if (images == null)
		{	images = new Hashtable();
			//Grafiek3DTest.loadImages(images, imageNames);
		}	}
	
	public static Image getImage(String name)
	{	return(Image)images.get(name);
	}
		
	public String getCode()
	{	return code;
	}
	
	public void setCode(String s)
	{	 code = s;
	}
	
	public void zetActief(boolean b)
	{	actief = b;
		repaint();
	}			public void setBackground(Color c)
	{	//bgColor = c;
		//bgUseColor = c;	}
		/*public void paint(Graphics g)
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
	}*/	
	public void paintComponent(Graphics g)
	{	//super.paintComponent(g);		g.setColor(fgColor);
		{	//g.setColor(bgColor);
			g.setColor(getParent().getBackground());
			g.fillRect(0,0,getSize().width,getSize().height);			if(focus)
			{	if(actief)
					g.setColor(bgColor.darker());				else 
					g.setColor(bgColor.brighter());
				g.drawLine(0,0,getSize().width-1,0);				g.drawLine(0,0,0,getSize().height-1);
				if(actief)
					g.setColor(bgColor.brighter());				else 
					g.setColor(bgColor.darker());
				g.drawLine(getSize().width-1,0,getSize().width-1,getSize().height-1);
				g.drawLine(0,getSize().height-1,getSize().width-1,getSize().height-1);
			}		}		int b = getSize().width;
		int h = getSize().height;		g.setColor(Color.black);
		if (code.equals("zoominx"))		{	g.drawImage(getImage("zoominxknop.gif"),1,1,null);
		}		else if(code.equals("zoomuitx"))		{	g.drawImage(getImage("zoomuitxknop.gif"),1,1,null);
		}		else if(code.equals("zoominy"))		{	g.drawImage(getImage("zoominyknop.gif"),1,1,null);
		}		else if(code.equals("zoomuity"))		{	g.drawImage(getImage("zoomuityknop.gif"),1,1,null);
		}		else if(code.equals("zoomin"))		{	//g.drawImage(getImage("zoominknop.gif"),1,1,null);
			g.drawImage(knopImage, 1, 1, null);
		}		else if(code.equals("zoomuit"))		{	//g.drawImage(getImage("zoomuitknop.gif"),1,1,null);
			g.drawImage(knopImage, 1, 1, null);			
		}		else if(code.equals("standaard"))		{	g.drawLine(4,10,16,10);
			g.drawLine(10,4,10,16);
		}
		else if (code.equals("transplus"))
		{	g.setFont(defaultfont);
			int w = fm.stringWidth("+");
			int as = fm.getAscent();
			g.drawString("+", (b-w)/2, (h-as)/2 + as - 3);
			
		}
		else if (code.equals("transmin"))
		{	g.setFont(defaultfont2);
			int w = fm2.stringWidth("-");
			int as = fm2.getAscent();
			g.drawString("-", (b-w)/2, (h-as)/2 + as - 5);
			
		}
		else if (code.equals("xasnaam"))
		{	g.setFont(defaultfont);
			int w = fm.stringWidth("x-" + Grafiek3DTest.rb.getString("asTekst"));
			int as = fm.getAscent();
			g.drawString("x-" + Grafiek3DTest.rb.getString("asTekst"), (b-w)/2, (h-as)/2 + as - 3);
			
		}
		else if (code.equals("yasnaam"))
		{	g.setFont(defaultfont);
			int w = fm.stringWidth("y-" + Grafiek3DTest.rb.getString("asTekst"));
			int as = fm.getAscent();
			int ht = fm.getHeight();
			g.drawString("y-" + Grafiek3DTest.rb.getString("asTekst"), (b-w)/2, (h-ht)/2 + as - 3);
			
		}
		else if (code.equals("zasnaam"))
		{	g.setFont(defaultfont);
			int w = fm.stringWidth("z-" + Grafiek3DTest.rb.getString("asTekst"));
			int as = fm.getAscent();
			g.drawString("z-" + Grafiek3DTest.rb.getString("asTekst"), (b-w)/2, (h-as)/2 + as - 3);
			
		}
		else if (code.equals("solid"))
		{	Polygon p = new Polygon();
			p.addPoint(3, 3);
			p.addPoint(b-4, 3);
			p.addPoint(b-4, h-4);
			p.addPoint(3, h-4);
			g.setColor(Grafiek3DComponent.graphColor);
			g.fillPolygon(p);
			g.setColor(Grafiek3DComponent.graphOutlineColor);
			g.drawPolygon(p);
					
		}
		else if (code.equals("draad"))
		{	Polygon p = new Polygon();
			p.addPoint(3, 3);
			p.addPoint(b-4, 3);
			p.addPoint(b-4, h-4);
			p.addPoint(3, h-4);
			g.setColor(Grafiek3DComponent.wireFrameColor);
			g.drawPolygon(p);
					
		}
		else if (code.equals("finerplus"))
		{	Polygon p = new Polygon();
			p.addPoint(3, 3);
			p.addPoint(b-4, 3);
			p.addPoint(b-4, h-4);
			p.addPoint(3, h-4);
			g.setColor(Color.black);
			g.drawPolygon(p);
			g.drawLine(b/3, 3, b/3, h-4);
			g.drawLine(2*b/3-1, 3, 2*b/3-1, h-4);
			g.drawLine(3, h/3, b-4, h/3);
			g.drawLine(3, 2*h/3-1, b-4, 2*h/3-1);
			
			
		}
		else if (code.equals("finermin"))
		{	Polygon p = new Polygon();
			p.addPoint(3, 3);
			p.addPoint(b-4, 3);
			p.addPoint(b-4, h-4);
			p.addPoint(3, h-4);
			if (isEnabled())
				g.setColor(Color.black);
			else
				g.setColor(Color.gray);
			g.drawPolygon(p);
			g.drawLine(b/2, 3, b/2, h-4);
			g.drawLine(3, h/2, b-4, h/2);
			
		}
		
		else if (code.equals("askeuze"))
		{	
/*			
			g.setFont(defaultfont3);
			int w = fm3.stringWidth(Grafiek3DTest.rb.getString("assenTekst"));
			int as = fm3.getAscent();
			g.drawString(Grafiek3DTest.rb.getString("assenTekst"), (b-w)/2, (h-as)/2 + as - 3);
*/
			
			Graphics2D gr = (Graphics2D) g;
			gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			//gr.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);
			gr.setStroke(new BasicStroke(1.3f));
			
			gr.drawLine(b/2, 0, b/2, h/2);
			gr.drawLine(b/2, h/2, b, h/2);
			gr.drawLine(b/2, h/2, 4, h-6);
			
		}
		else if (code.equals("labelkeuze"))
		{	g.setFont(defaultfont4);
			int w = fm4.stringWidth("1.5");
			int as = fm4.getAscent();
			g.drawString("1.5", (b-w)/2, (h-as)/2 + as - 3);
			
		}
		
		
	}
	
	public void mousePressed(MouseEvent e)	{	actief = true;		repaint();
	}	
	public void mouseReleased(MouseEvent e) 
 	{	actief = false;		if (isEnabled())
 		{	produceAction("knop" + releaseCnt);
 			releaseCnt++;
 			releaseCnt = releaseCnt % 2;
 		}		repaint();
 	}  
	
	public void mouseEntered(MouseEvent e)	{	focus = true;
		setCursor(new Cursor(Cursor.HAND_CURSOR ));
		repaint();
		produceAction("focus");	}
	public void mouseExited(MouseEvent e)
	{	focus = false;		setCursor(new Cursor(Cursor.DEFAULT_CURSOR ));
		getParent().repaint();
		//repaint();
		//produceAction("focus");	}
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
 		{
//if (!command.equals("focus")) 		
//System.out.println("command " + command + releaseCnt + " " + code);

 			actionListener.actionPerformed(new ActionEvent(this, 0, command));
	
 		}
 	}
 	//
}