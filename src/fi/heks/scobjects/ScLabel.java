package fi.heks.scobjects;

import java.awt.*;
import java.awt.event.*;

public class ScLabel extends Component implements  ScObject	
{		public static int CENTER = 0;	public static int RECHTS = 2;	public static int LINKS = 1;		private int uitlijning;
	
	protected String opschrift;
	protected Font labelfont;					
	public double schaal;
	public double relx,rely,relb,relh;
	public boolean resized;
	

	public ScLabel(int x, int y, int b, int h, String str)
	{	opschrift = str;
		schaal = 1;
		relx = x;
		rely = y; 
		relb = b;
		relh = h;
		setBounds(x,y,b,h);
		
		uitlijning = CENTER;
		
	}
	
	
	
	public String getLabel()
	{	return opschrift;
	}
	
	public void setLabel(String label)
	{	opschrift = label;		repaint();
	}
	public void paint(Graphics gr)
	{	
		Graphics g ;
	    {   g = (Graphics2D)gr;
	    	if(System.getProperty("java.specification.version").equals("1.6"))
			{	((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, new Integer(100));
		    }
	    	else
	    	{	((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    	}
	     }
	    		g.setColor(this.getForeground());
		Font f = new Font("SansSerif", Font.PLAIN, (int)(3*schaal*relh/4));
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		int woordbreedte=0;
		if(opschrift!=null)woordbreedte = fm.stringWidth(opschrift);
		int beginx=0;
		if(uitlijning == 0) beginx = (getSize().width - woordbreedte)/2;
		else if(uitlijning == 1) beginx = 0;
		else if(uitlijning == 2) beginx = getSize().width - woordbreedte;
		if(opschrift!=null)g.drawString(opschrift, beginx, (getSize().height +fm.getHeight())/2 - fm.getDescent());
	}
		public void setResized(boolean b)
	{	resized = b;
	}
	
	public void schaal(double s)
	{	schaal = s;
		int x = (int)(schaal*relx);
		int y = (int)(schaal*rely);
		int b = (int)(schaal*relb);
		int h = (int)(schaal*relh);
		setBounds(x,y,b,h);
	}	
	public void lijnUit(int soort)	{	uitlijning = soort;	}
}