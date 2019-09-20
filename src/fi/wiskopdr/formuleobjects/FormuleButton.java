package fi.wiskopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

import fi.wiskopdr.WiskOpdr;
import fi.beans.wnwidgets.*;

public class FormuleButton extends JButton implements MouseListener	
{	
	public static final String IMAGE = "I";
	
	private Image im;
	private Graphics gIm;
	
	protected String code;
	private Font defaultfont = new Font("SansSerif", Font.PLAIN, 13);
	private FontMetrics fm;
	protected Color bgColor = new Color(221,222,225);
	private boolean opaque = false;
	protected Color fgColor = Color.black;
	protected Color bgUseColor = bgColor;
	protected Color fgUseColor = fgColor;
	private Color vlakkleur;
	private int kubusNummer;
	private Rectangle[] zijvlakken;
	protected boolean focus = false;
	protected boolean actief = false;
	protected boolean focusable = true;
	protected boolean toggle = false;
	boolean toggleAan = false;
	private Icon popupButtonImage;
	
	private static String[] imageNames = 
	{	
		"formuleknop.gif",
		"linkknop.gif",
		"imageknop.gif",
		"grafiekknop.gif",
		"appletknop.gif",	
		"rmknop.gif",
		"geogebra.gif",
		"cbook.png",
		"cindy.png",
		"eslate.gif",
		"epsilonwriter.png",
		"reseticon.gif",
		"sknop.gif",
		"cknop.gif",
		"tknop.gif",
		"opsomming.png",
		
		"wnformbutton.gif",
		"wnformbuttonrood.gif",
		"wnformbuttonoranje.gif"//,
		//"wngrafiekbutton.png",
		//"wngeogebrabutton.png",
		//"wntekstvakbutton.png"
	};
	
	private static Hashtable images;
	
	
	protected ActionListener actionListener = null;
	
	public static int EDITORKNOP = 0;
	public static int BEWERKINGSKNOP = 1;
	public static int NAVIGATIEKNOP = 2;
	public static int TABLETKNOP = 3;
	public static int MEERKNOP = 4;
	
	private int soort = 0;

	public FormuleButton()
	{
		this("",0);
	}
	
	public FormuleButton(String s)
	{
		this(s,0);
	}
	
	/**
	 * 
	 * @param s
	 * @param soort
	 */
	public FormuleButton(String s, int soort)
	{
		super(s);
		code = s;
		this.soort = soort;
		addMouseListener(this);
		setFont(defaultfont);
		fm = this.getFontMetrics(defaultfont);
		UIManager.put("ToolTip.background", new ColorUIResource(new Color(255, 255, 230)));
		// setToolTipText(s);
		setBorder(null);
		if (images == null)
		{
			images = new Hashtable();
			WiskOpdr.loadImages(images, imageNames);
		}
		if (soort == BEWERKINGSKNOP)
			bgColor = new Color(255, 150, 150);
		if ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
		{
			NWButtonUI ui = NWButtonUI.getInstance("mw_formbutton_skin_gray.png");
			if (soort == EDITORKNOP)
				ui = NWButtonUI.getInstance("mw_formbutton_skin_gray.png");
			if (soort == BEWERKINGSKNOP)
				ui = NWButtonUI.getInstance("mw_formbutton_skin_rood.png");
			if ("MW".equals(WiskOpdr.deployVariant) && soort == NAVIGATIEKNOP)
				ui = NWButtonUI.getInstance("mw_formbutton_skin_orange.png");
			else if ("GR".equals(WiskOpdr.deployVariant) && soort == NAVIGATIEKNOP)
				ui = NWButtonUI.getInstance("gr_formbutton_skin_graf.gif");
			if (soort == TABLETKNOP)
				ui = NWButtonUI.getInstance("mw_formbutton_skin_tablet.png");
			if ("GR".equals(WiskOpdr.deployVariant) && soort == TABLETKNOP)
				ui = NWButtonUI.getInstance("mw_formbutton_skin_gray.png");
			if (soort == MEERKNOP)
				ui = NWButtonUI.getInstance("mw_formbutton_skin_white.png");
			setUI(ui);
			NWButtonUI.setIcon(code, this);
			if (code.equals("\u2227"))
				setToolTipText(" " + WiskOpdr.rb.getString("enLabel") + " ");
			if (code.equals("\u2228"))
				setToolTipText(" " + WiskOpdr.rb.getString("ofLabel") + " ");
			if (code.equals("\u2205"))
				setToolTipText(" " + WiskOpdr.rb.getString("geenOplossingen") + " ");
			
			
			  
		}
		if(code.equals("formule"))
           setToolTipText(WiskOpdr.rb.getString("TE_formuleButtonTooltip"));
		if(code.equals("grafiektool") || code.equals("grafiekcomponent"))
		  setToolTipText(WiskOpdr.rb.getString("TE_graphButtonTooltip"));
		if(code.equals("link"))
		  setToolTipText(WiskOpdr.rb.getString("TE_linkButtonTooltip"));
		if(code.equals("image"))
		  setToolTipText(WiskOpdr.rb.getString("TE_imageButtonTooltip"));
		if(code.equals("antwoordvak"))
		  setToolTipText(WiskOpdr.rb.getString("TE_answerboxButtonTooltip"));
		if(code.equals("interactiecomponent"))
          setToolTipText(WiskOpdr.rb.getString("TE_widgetButtonTooltip"));
		if(code.equals("geogebra"))
          setToolTipText(WiskOpdr.rb.getString("TE_geogebraButtonTooltip"));
		if(code.equals("tekstvak"))
          setToolTipText(WiskOpdr.rb.getString("TE_tekstVakButtonTooltip"));
		if(code.equals("sknop"))
		  setToolTipText(WiskOpdr.rb.getString("TE_compositeButtonTooltip"));
		if(code.equals("tknop"))
		  setToolTipText(WiskOpdr.rb.getString("TE_templateButtonTooltip"));
		if(code.equals("cknop"))
		  setToolTipText(WiskOpdr.rb.getString("TE_templateCompButtonTooltip"));
		if(code.equals("crosswidget"))
		  setToolTipText(WiskOpdr.rb.getString("TE_x-widgetButtonTooltip"));
		if(code.equals("opsomming"))
			  setToolTipText(WiskOpdr.rb.getString("TE_opsommingButtonTooltip"));
		 
		
	}
	
	public void setPopupButtonImage(final Image image)
	{
	    popupButtonImage = image == null ? null : new Icon() {

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
          g.drawImage(image,x,y, getWidth(), getHeight(), getParent());
        }

        @Override
        public int getIconWidth() {
          return getWidth();
        }

        @Override
        public int getIconHeight() {
          return getHeight();
        } }; // image;
	}
	
	public void setPopupButtonIcon(Icon icon) {
	  popupButtonImage = icon;
	}
	
	
	
	public static Image getImage(String name)
	{	return(Image)images.get(name);
	}
	
	public void setFocusable(boolean b)
	{	focusable = b;
	}
	
	public String getCode()
	{	return code;
	}
	
	public void setCode(String s)
	{	 code = s;
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
	{	bgColor = c;
		opaque=true;
	
		//bgUseColor = c;
	}
	
	public void setToggle(boolean b)
	{	toggle = b;
	}
	
	public boolean isToggleAan()
	{	return toggleAan;
	}
	
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
	{	
	    if(popupButtonImage!=null){
	        //g.drawImage(popupButtonImage,0,0,getWidth(), getHeight(),getParent());
	      popupButtonImage.paintIcon(this, g, 0, 0);
	        return;
	    }
		//Graphics g;
	        /*if(WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("GR"))
	        {     g = (Graphics2D)gr;
	              ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        }*/
		if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
		{	//g = (Graphics2D)gr;
            //((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			super.paintComponent(g);
			return;
		}
		//else g=gr;
		g.setColor(fgColor);
		{	g.setColor(bgColor);
			g.fillRect(0,0,getSize().width,getSize().height);
			
			int red = bgColor.getRed();
			int green = bgColor.getGreen();
			int blue = bgColor.getBlue();
//			for(int i=0 ; i<10 ; i++)
//			{
//				g.setColor(new Color(red + i*(245-red)/10,green + i*(245-green)/10,blue + i*(245-blue)/10));
//				g.fillRect(0,getHeight()-(i+1)*getHeight()/10, getWidth(),getHeight()/10+1);
//			}
			if(!focusable || focus || toggleAan)
			{	if(actief || toggleAan)g.setColor(bgColor.darker());
				else g.setColor(bgColor.brighter());
				g.drawLine(0,0,getSize().width-1,0);
				g.drawLine(0,0,0,getSize().height-1);
				if(actief || toggleAan)g.setColor(bgColor.brighter());
				else g.setColor(bgColor.darker());
				g.drawLine(getSize().width-1,0,getSize().width-1,getSize().height-1);
				g.drawLine(0,getSize().height-1,getSize().width-1,getSize().height-1);
			}
		}
		if(soort==BEWERKINGSKNOP)
		{	if(actief || toggleAan)g.setColor(bgColor.darker());
			else g.setColor(bgColor.brighter());
			g.drawLine(0,0,getSize().width-1,0);
			g.drawLine(0,0,0,getSize().height-1);
			if(focus)
			{	g.drawLine(1,1,getSize().width-2,1);
				g.drawLine(1,1,1,getSize().height-2);
			}
			if(actief || toggleAan)g.setColor(bgColor.brighter());
			else g.setColor(bgColor.darker());
			g.drawLine(getSize().width-1,0,getSize().width-1,getSize().height-1);
			g.drawLine(0,getSize().height-1,getSize().width-1,getSize().height-1);
			if(focus)
			{	g.drawLine(getSize().width-2,1,getSize().width-2,getSize().height-2);
				g.drawLine(1,getSize().height-2,getSize().width-2,getSize().height-2);
			}
		}
		if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
		{	g.setColor(bgColor);
		
			if(code.equals("wortel") 
					|| code.equals("macht") 
					|| code.equals("haakjes")
					|| code.equals("kwadraat") 
					|| code.equals("ndewortel") 
					|| code.equals("breuk") 
					|| code.equals("wortel"))
				g.drawImage(getImage("wnformbutton.gif"),0,0,null);
			if(code.equals("gelijkwaardig")  
					|| code.equals("terug"))
				g.drawImage(getImage("wnformbuttonoranje.gif"),0,0,null);
			else if(code.equals("meer")){
				
				g.fillRect(0,0,getSize().width,getSize().height);
			}
			else g.drawImage(getImage("wnformbutton.gif"),0,0,null);
		}
		paintCode(g);
		
		
		
		
		
	}
	
	public void paintCode(Graphics g)
	{
		int b = getSize().width;
		int h = getSize().height;
		if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
		{	b = getSize().width-2;
			h = getSize().height-2;
		}
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
		else if(code.equals("wortelbewerk"))
        {   g.drawLine(3,2*h/4,h/4,h-3);
            g.drawLine(4,2*h/4,h/4+1,h-3);
            g.drawLine(h/4+1,h-3,h/2,3);
            g.drawLine(h/2,3,b-3,3);
           
        }
		else if(code.equals("macht"))
		{	g.setColor(Color.white);
            g.fillRect(6,6,4,10);
            g.fillRect(13,3,3,6);
            g.setColor(Color.gray);
            g.drawRect(6,6,4,10);
			g.drawRect(13,3,3,6);
		
		}
		else if(code.equals("subscript"))
		{	g.setColor(Color.white);
            g.fillRect(6,3,4,10);
            g.fillRect(13,10,3,6);
            g.setColor(Color.gray);
            g.drawRect(6,3,4,10);
			g.drawRect(13,10,3,6);
		
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
		else if(code.equals("bin"))
		{	
			int hoogte = 20;
			int breedte = 20;
			 h =20;
			int hh = h/2;
			 b = h/6;
			int bb = b/2;
			
			int c = 3;
			int d = 2;
			
			g.drawLine(c+b, d, c+b-bb, d+bb);
			g.drawLine(c+b-bb, d+bb, c, d+hh-b);
			g.drawLine(c, d+hh-b, c, hoogte-hh+b-d);		
			g.drawLine(c+b-bb, hoogte-bb-d, c, hoogte-hh+b-d);
			g.drawLine(c+b, hoogte-d, c+b-bb, hoogte-bb-d);
			
			g.drawLine(breedte-b-1-c, d, breedte-b+bb-1-c, d+bb);
			g.drawLine(breedte-b+bb-1-c, d+bb, breedte-1-c, d+hh-b);
			g.drawLine(breedte-1-c, d+hh-b, breedte-1-c, hoogte-hh+b-d);		
			g.drawLine(breedte-b+bb-1-c, hoogte-bb-d, breedte-1-c, hoogte-hh+b-d);
			g.drawLine(breedte-b-1-c, hoogte-d, breedte-b+bb-1-c, hoogte-bb-d);
			g.setColor(Color.white);
            g.fillRect(8,3,4,5);
            g.fillRect(8,12,4,5);
            g.setColor(Color.gray);
            g.drawRect(8,3,4,5);
            g.drawRect(8,12,4,5);
            g.setColor(Color.black);
		}
		else if(code.equals("vector"))
		{
			// drie invulvakjes
			g.setColor(Color.white);
			g.fillRect(9, 4, 2, 3);
			g.fillRect(9, 9, 2, 3);
			g.fillRect(9, 14, 2, 3);
			g.setColor(Color.gray);
			g.drawRect(9, 4, 2, 3);
			g.drawRect(9, 9, 2, 3);
			g.drawRect(9, 14, 2, 3);

			// rechte haak links
			g.setColor(WiskOpdr.isPremium()?Color.black:Color.gray);
			g.drawLine(3, 3, 5, 3);
			g.drawLine(3, 17, 5, 17);
			g.drawLine(3, 3, 3, 17);
			// rechte haak rechts
			g.drawLine(14, 3, 16, 3);
			g.drawLine(14, 17, 16, 17);
			g.drawLine(16, 3, 16, 17);
		}
		else if(code.equals("vectornotatie"))
		{
			g.setColor(Color.white);
            g.fillRect(6, 9, 6, 9);
            g.setColor(Color.gray);
            g.drawRect(6, 9, 6, 9);
            g.setColor(WiskOpdr.isPremium()?Color.black:Color.gray);
            // pijl boven vak
            g.drawLine(6, 4, 12, 4);
            g.drawLine(9, 2, 12, 4);
            g.drawLine(9, 6, 12, 4);
		}
		else if(code.equals("matrix"))
		{
			// 2 x 3 invulvakjes
			// kolom 1
			g.setColor(Color.white);
			g.fillRect(7, 4, 2, 3);
			g.fillRect(7, 9, 2, 3);
			g.fillRect(7, 14, 2, 3);
			g.setColor(Color.gray);
			g.drawRect(7, 4, 2, 3);
			g.drawRect(7, 9, 2, 3);
			g.drawRect(7, 14, 2, 3);
			// kolom 2
			g.setColor(Color.white);
			g.fillRect(12, 4, 2, 3);
			g.fillRect(12, 9, 2, 3);
			g.fillRect(12, 14, 2, 3);
			g.setColor(Color.gray);
			g.drawRect(12, 4, 2, 3);
			g.drawRect(12, 9, 2, 3);
			g.drawRect(12, 14, 2, 3);
			// kolom 3
			g.setColor(Color.white);
			g.fillRect(17, 4, 2, 3);
			g.fillRect(17, 9, 2, 3);
			g.fillRect(17, 14, 2, 3);
			g.setColor(Color.gray);
			g.drawRect(17, 4, 2, 3);
			g.drawRect(17, 9, 2, 3);
			g.drawRect(17, 14, 2, 3);

			// rechte haak links
			g.setColor(WiskOpdr.isPremium()?Color.black:Color.gray);
			g.drawLine(3, 3, 5, 3);
			g.drawLine(3, 17, 5, 17);
			g.drawLine(3, 3, 3, 17);
			// rechte haak rechts
			g.drawLine(21, 3, 23, 3);
			g.drawLine(21, 17, 23, 17);
			g.drawLine(23, 3, 23, 17);
		}
		else if(code.equals("haakjes"))
		{	g.drawString("(",3,15);
			g.drawString(")",13,15);
            g.setColor(Color.white);
            g.fillRect(8,5,4,10);
            g.setColor(Color.gray);
            g.drawRect(8,5,4,10);
            
		}
		else if(code.equals("abs"))
		{	g.drawString("|",3,15);
			g.drawString("|",13,15);
            g.setColor(Color.white);
            g.fillRect(7,5,4,10);
            g.setColor(Color.gray);
            g.drawRect(7,5,4,10);
            
		}
		else if(code.equals("conjug"))
		{
			g.setColor(Color.white);
            g.fillRect(7,6,4,10);
            g.setColor(Color.gray);
            g.drawRect(7,6,4,10);
            g.setColor(Color.black);
            g.drawLine(7,3,11,3);
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
		{	g.setFont(new Font("SansSerif", Font.PLAIN, 10));
			g.drawString("log",7,15);
			g.setColor(Color.white);
			//g.fillRect(h/2+12,6,4,10);
			g.fillRect(3,3,3,6);
	        g.setColor(Color.gray);
	        //g.drawRect(h/2+12,6,4,10);
	        g.drawRect(3,3,3,6);
		}
		else if(code.equals("diff"))
		{	g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        
			g.drawString("d",7,10);
			g.drawString("dx",5,18);
		}
		else if(code.equals("diff_partial"))
        {
			g.setFont(new Font("SansSerif", Font.PLAIN, 9));
        
            g.drawString("\u2202", 8, 10);
            g.drawString("\u2202x", 6, 18);
        }
		else if(code.equals("limiet0"))
		{	g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        
			g.drawString("lim",2,10);
			g.setFont(new Font("SansSerif", Font.PLAIN, 8));
			g.drawString("x",2,18);
			g.drawString("a",12,18);
			g.drawLine(6,14,10,14);
			g.drawLine(9,13,10,14);
			g.drawLine(9,15,10,14);
			
		}
		else if(code.equals("limiet1"))
		{	g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        
			g.drawString("lim",2,10);
			g.setFont(new Font("SansSerif", Font.PLAIN, 8));
			g.drawString("x",2,18);
			g.drawString("a",12,18);
			g.drawLine(8,12,8,16);
			g.drawLine(7,15,8,16);
			g.drawLine(9,15,8,16);
			
		}
		else if(code.equals("limiet2"))
		{	g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        
			g.drawString("lim",2,10);
			g.setFont(new Font("SansSerif", Font.PLAIN, 8));
			g.drawString("x",2,18);
			g.drawString("a",12,18);
			g.drawLine(8,12,8,16);
			g.drawLine(7,13,8,12);
			g.drawLine(9,13,8,12);
			
		}
		else if(code.equals("integraal"))
		{	g.setColor(Color.white);
			g.fillRect(14,3,2,5);
			g.fillRect(11,12,2,5);
	        g.setColor(Color.gray);
	        g.drawRect(14,3,2,5);
	        g.drawRect(11,12,2,5);
	        
	        g.setColor(Color.black);
	        g.drawArc(7,3,4,4,0,180);
	        g.drawArc(3,12,4,4,180,180);
	        g.drawLine(7,5,7,14);
		}
		else if(code.equals("primitieve"))
		{	g.setColor(Color.black);
	        g.drawArc(7,3,4,4,0,180);
	        g.drawArc(3,12,4,4,180,180);
	        g.drawLine(7,5,7,14);
		}
		else if(code.equals("prv"))
		{	g.setColor(Color.white);
			g.fillRect(14,3,2,5);
			g.fillRect(14,12,2,5);
	        g.setColor(Color.gray);
	        g.drawRect(14,3,2,5);
	        g.drawRect(14,12,2,5);
	        
	        g.setColor(Color.black);
	        g.drawLine(3,3,5,3);
	        g.drawLine(3,17,5,17);
	        g.drawLine(3,3,3,17);
	        g.drawLine(9,3,11,3);
	        g.drawLine(9,17,11,17);
	        g.drawLine(11,3,11,17);
		}
		else if(code.equals("stelsel"))
		{
			g.setColor(Color.white);
            g.fillRect(10,4,4,5);
            g.fillRect(10,11,4,5);
            g.setColor(Color.gray);
            g.drawRect(10,4,4,5);
            g.drawRect(10,11,4,5);
	        
			g.setColor(Color.black);
			g.drawArc(5, 2, 6, 6, 90, 90);
			g.drawLine(5, 4, 5, 7);
			g.drawArc(-1, 4, 6, 6, 270, 90);
			g.drawArc(-1, 10, 6, 6, 0, 90);
			g.drawLine(5, 12, 5, 15);
			g.drawArc(5, 11, 6, 6, 180, 90);
		}
		else if(code.equals("formule"))
		{	//g.drawString("F",7,15);
			g.drawImage(getImage("formuleknop.gif"),1,1,null);
		}
		else if(code.equals(WiskOpdr.rb.getString("grafiekComponentLabel")))
		{	//g.drawString("A",7,15);
			g.drawImage(getImage("grafiekknop.gif"),1,1,null);
			//if("MW".equals(WiskOpdr.deployVariant))g.drawImage(getImage("wngrafiekbutton.png"),0,0,null);
		}
		else if(code.equals("grafiekcomponent") || code.equals("grafiektool"))
		{	//g.drawString("A",7,15);
			g.drawImage(getImage("grafiekknop.gif"),1,1,null);
			//if("MW".equals(WiskOpdr.deployVariant))g.drawImage(getImage("wngrafiekbutton.png"),0,0,null);
		}
		else if(code.equals("link"))
		{	//g.drawString("L",7,15);
			g.drawImage(getImage("linkknop.gif"),1,1,null);
		}
		else if(code.equals("image"))
		{	//g.drawString("I",7,15);
			g.drawImage(getImage("imageknop.gif"),1,1,null);
		}
		else if(code.equals("rmvak"))
		{	//g.drawString("I",7,15);
			g.drawImage(getImage("rmknop.gif"),3,3,null);
		}
		else if(code.equals("geogebra"))
		{	//g.drawString("I",7,15);
			g.drawImage(getImage("geogebra.gif"),2,2,null);
			//if("MW".equals(WiskOpdr.deployVariant))g.drawImage(getImage("wngeogebrabutton.png"),0,0,null);
		}
		else if(code.equals("sknop"))
        {   //g.drawString("I",7,15);
            g.drawImage(getImage("sknop.gif"),2,2,null);
            //if("MW".equals(WiskOpdr.deployVariant))g.drawImage(getImage("wngeogebrabutton.png"),0,0,null);
        }
		else if(code.equals("cknop"))
        {   //g.drawString("I",7,15);
            g.drawImage(getImage("cknop.gif"),2,2,null);
            //if("MW".equals(WiskOpdr.deployVariant))g.drawImage(getImage("wngeogebrabutton.png"),0,0,null);
        }
		else if(code.equals("tknop"))
        {   //g.drawString("I",7,15);
            g.drawImage(getImage("tknop.gif"),2,2,null);
            //if("MW".equals(WiskOpdr.deployVariant))g.drawImage(getImage("wngeogebrabutton.png"),0,0,null);
        }
		else if(code.equals("opsomming"))
        {   //g.drawString("I",7,15);
            g.drawImage(getImage("opsomming.png"),2,2,null);
            //if("MW".equals(WiskOpdr.deployVariant))g.drawImage(getImage("wngeogebrabutton.png"),0,0,null);
        }
		else if(code.equals("cbook"))
		{	//g.drawString("I",7,15);
			g.drawImage(getImage("cbook.png"),2,2,null);
			//if("MW".equals(WiskOpdr.deployVariant))g.drawImage(getImage("wngeogebrabutton.png"),0,0,null);
		}
		else if(code.equals("cindy"))
		{	//g.drawString("I",7,15);
			g.drawImage(getImage("cindy.png"),2,2,null);
			//if("MW".equals(WiskOpdr.deployVariant))g.drawImage(getImage("wngeogebrabutton.png"),0,0,null);
		}
		else if(code.equals("eslate"))
		{	//g.drawString("I",7,15);
			g.drawImage(getImage("eslate.gif"),2,2,null);
			//if("MW".equals(WiskOpdr.deployVariant))g.drawImage(getImage("wngeogebrabutton.png"),0,0,null);
		}
		else if(code.equals("epsilonwriter"))
		{	//g.drawString("I",7,15);
			g.drawImage(getImage("epsilonwriter.png"),2,2,null);
			//if("MW".equals(WiskOpdr.deployVariant))g.drawImage(getImage("wngeogebrabutton.png"),0,0,null);
		}
		else if(code.equals("rmvakklein"))
		{	//g.drawString("I",7,15);
			g.drawImage(getImage("rmknop.gif"),1,1,null);
		}
		else if(code.equals(WiskOpdr.rb.getString("interactieVakLabel")))
		{	g.drawImage(getImage("appletknop.gif"),1,1,null);
		}
		else if(code.equals("interactiecomponent"))
		{	g.drawImage(getImage("appletknop.gif"),1,1,null);
		}
		
		else if(code.equals("reseticon"))
		{	g.drawImage(getImage("reseticon.gif"),0,0,null);
		} 
		else if(code.equals(WiskOpdr.rb.getString("antwoordVakLabel")))
		{	//g.drawString("V",7,15);
			g.setColor(Color.white);
	        g.fillRect(3,5,13,10);
	        g.setColor(Color.gray);
	        g.drawRect(3,5,13,10);
		}
		else if(code.equals("antwoordvak"))
		{	//g.drawString("V",7,15);
			g.setColor(Color.white);
	        g.fillRect(3,5,13,10);
	        g.setColor(Color.gray);
	        g.drawRect(3,5,13,10);
		}
		else if(code.equals("uitwerking"))
		{	//g.drawString("V",7,15);
			g.setColor(Color.white);
	        g.fillRect(2,5,14,15);
	        g.setColor(Color.gray);
	        g.drawRect(2,5,14,15);
	        g.drawLine(5,8,13,8);
	        g.drawLine(5,11,13,11);
	        g.drawLine(5,14,13,14);
	        g.drawLine(5,17,13,17);
	    }
		else if(code.equals(WiskOpdr.rb.getString("tekstVakLabel")))
		{	g.setColor(Color.gray);
	        g.drawRect(3,5,13,10);
		}
		else if(code.equals("tekstvak"))
		{	g.setColor(Color.gray);
	        g.drawRect(3,5,13,10);
	        //if("MW".equals(WiskOpdr.deployVariant))g.drawImage(getImage("wntekstvakbutton.png"),0,0,null);
		}
		else if(code.equals("crosswidget"))
		{	g.setColor(new Color(255,200,0));
	        g.drawRect(1,1,15,15);
	        g.drawLine(1, 1, 15, 15);
	        g.drawLine(1, 15, 15, 1);
	        g.drawLine(8, 1, 8, 15);
	        g.drawLine(0, 8, 15, 8);
	        //if("MW".equals(WiskOpdr.deployVariant))g.drawImage(getImage("wntekstvakbutton.png"),0,0,null);
		}
		else if(code.equals("tekstVakLabel"))
		{	g.setColor(Color.gray);
	        g.drawRect(3,5,13,10);
	        //if("MW".equals(WiskOpdr.deployVariant))g.drawImage(getImage("wntekstvakbutton.png"),0,0,null);
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
		else if(code.equals("resize"))
		{	g.setColor(Color.gray);
			g.drawRect(2,2,11,11);
			g.drawRect(3,3,9,9);
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
		else if(code.equals("close"))
		{	g.drawLine(2,2,8,8);
			g.drawLine(2,8,8,2);
		}
		else if(code.equals("deel"))
		{	g.fillRect(b/2,h/4+1,2,2);
			g.fillRect(b/2,3*h/4-2,2,2);
			g.drawLine(b/4+2,b/2,3*b/4-2,b/2);
		}
		else if(code.equals("haakjesWeg"))
		{	g.drawString("(",5,15);
			g.drawString(")",11,15);
			g.drawLine(b/5,h/5,4*b/5,4*h/5);
		}
		else if(code.equals("\u3008"))
		{
			g.drawLine(13, 5, 8, 10);
            g.drawLine(8, 10, 13, 15);
		}
		else if(code.equals("\u3009"))
        {  
			g.drawLine(9, 5, 14, 10);
            g.drawLine(14, 10, 9, 15);
        }
		else if(code.equals("sigma"))
        {   g.drawString("\u03A3",5,15);
            g.setColor(Color.white);
            g.fillRect(14,3,2,5);
            g.fillRect(14,12,2,5);
            g.setColor(Color.gray);
            g.drawRect(14,3,2,5);
            g.drawRect(14,12,2,5);
        }
		else if(code.equals("back"))
		{	g.drawLine(b/4+2,h/2,3*b/4-2,h/2);
			g.drawLine(b/4+2,h/2+1,3*b/4-2,h/2+1);
			g.drawLine(b/4+2,h/2,b/4+5,h/2-3);
			g.drawLine(b/4+2,h/2+1,b/4+5,h/2-2);
			g.drawLine(b/4+2,h/2,b/4+5,h/2+3);
			g.drawLine(b/4+2,h/2+1,b/4+5,h/2+4);
		}
		else if(code.equals("gelijkwaardig"))
		{	//if(WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("GR")) {
            //   g.setFont(new Font("SansSerif", Font.PLAIN, 12));
            //    g.drawString(WiskOpdr.rb.getString("gelijkwaardigKnopLabelGR"),5,15);
            //}
            //else 
            {
                g.setColor(new Color(100,100,100));
                g.drawLine(b/2,h/6,b/2,h-h/6);
                g.drawLine(b/2+1,h/6,b/2+1,h-h/6);
                g.drawLine(b/2-1,h/6,b/2-1,h-h/6);
                //g.drawLine(b/2,h-h/6,b/2+3,h-2*h/6);
    			//g.drawLine(b/2,h-h/6,b/2-3,h-2*h/6);
    			g.drawLine(b/2+1,h-h/6,b/2+3+1,h-2*h/6);
    			g.drawLine(b/2-1,h-h/6,b/2-3-1,h-2*h/6);
    			g.drawLine(b/2+1,h-h/6-1,b/2+3+1,h-2*h/6-1);
    			g.drawLine(b/2-1,h-h/6-1,b/2-3-1,h-2*h/6-1);
    			g.drawLine(b/2+1,h-h/6-2,b/2+3+1,h-2*h/6-2);
    			g.drawLine(b/2-1,h-h/6-2,b/2-3-1,h-2*h/6-2);
            }      
		}
		else if(code.equals("terug"))
		{	//if(WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("GR")) {
            //	g.setFont(new Font("SansSerif", Font.PLAIN, 12));
            //	g.drawString(WiskOpdr.rb.getString("terugKnopLabelGR"),5,15);
            //}
            //else 
            {g.setColor(new Color(100,100,100));
    			g.drawLine(b/2,h/6,b/2,h-h/6);
    			g.drawLine(b/2-1,h/6,b/2-1,h-h/6);
    			g.drawLine(b/2+1,h/6,b/2+1,h-h/6);
    			g.drawLine(b/2+1,h/6,b/2+3+1,2*h/6);
    			g.drawLine(b/2-1,h/6,b/2-3-1,2*h/6);
    			g.drawLine(b/2+1,h/6+1,b/2+3+1,2*h/6+1);
    			g.drawLine(b/2-1,h/6+1,b/2-3-1,2*h/6+1);
    			g.drawLine(b/2+1,h/6+2,b/2+3+1,2*h/6+2);
    			g.drawLine(b/2-1,h/6+2,b/2-3-1,2*h/6+2);
            }
		}
		else if(code.equals("pi"))
		{	g.setFont(new Font("TimesRoman", Font.ITALIC , 16));
			if(WiskOpdr.mac) g.setFont(new Font("SansSerif", Font.ITALIC , 13));
			g.drawString("\u03C0",5,15);
			//g.setFont(new Font("SansSerif", Font.PLAIN, 13));
		}
		else if(code.equals("123"))
		{	g.setFont(new Font("SansSerif", Font.PLAIN, 10));
			
			
			g.drawString(code,1,3*getSize().height/4);
			g.setFont(new Font("SansSerif", Font.PLAIN, 13));
		}
		else if(code.equals("?"))
		{	g.setFont(new Font("SansSerif", Font.BOLD, 13));
			g.drawString(code,3,5*getSize().height/6);
		}
        else if(code.equals("meer"))
        {   g.setFont(new Font("SansSerif", Font.PLAIN, 10));
            g.drawString(WiskOpdr.rb.getString("meerLabel"),5,3*getSize().height/6);
            g.setFont(new Font("SansSerif", Font.PLAIN, 13));
            //g.drawLine(b/2-5,h/2+2,b/2+5,h/2+2);
            g.drawLine(b/2-4,h/2+3,b/2+4,h/2+3);
            g.drawLine(b/2-3,h/2+4,b/2+3,h/2+4);
            g.drawLine(b/2-2,h/2+5,b/2+2,h/2+5);
            g.drawLine(b/2-1,h/2+6,b/2+1,h/2+6);
            g.drawLine(b/2-0,h/2+7,b/2+0,h/2+7);
            
        }
        else if(code.equals("haakjesweg"))
		{	
			g.drawString("(",5,15);
			g.drawString(")",11,15);
			g.drawLine(b/5,h/5,4*b/5,4*h/5);
		}
		else if(code.equals("ontbind"))
		{	g.drawString("(",11,15);
			g.drawString(")",5,15);
		}
		
		else if(code.equals("splits"))
		{	g.drawLine(10,10,16,16);
			g.drawLine(10,10,4,16);
			g.drawLine(10,4,10,10);
			g.drawLine(16,16,16,12);
			g.drawLine(16,16,12,16);
			g.drawLine(4,16,8,16);
			g.drawLine(4,16,4,12);
		}
		else if(code.equals("herleid"))
		{	g.drawRect(b/5,h/6,b/5,h/2);
			g.drawRect(b/2+1,h/6,b/5,h/2);
			g.drawLine(b/5,4*h/5,4*b/5-1,4*h/5);
			g.drawLine(b/5,4*h/5,b/5-1,4*h/5-1);
			g.drawLine(4*b/5-1,4*h/5,4*b/5,4*h/5-1);
		}
		else if(code.equals("abc") || code.equals("123") || code.equals("sub"))
		{	g.setFont(new Font("SansSerif", Font.PLAIN, 10));
			
			if(code.equals("abc"))g.drawString(WiskOpdr.rb.getString("abc"),1,15);
			else if(code.equals("sub"))g.drawString("sub",1,15);
			g.setFont(new Font("SansSerif", Font.PLAIN, 13));
		}
		else 
		{	
			int w = g.getFontMetrics().stringWidth(code);
			g.drawString(code,(b-w)/2,3*getSize().height/4);
			
		}
	}
	
	public void setBounds(boolean v, int x, int y, int w, int h)
	{
	  if(v)
	    super.setBounds(y,x,w,h);
	  else
	    super.setBounds(x,y,w,h);
	}
	
	public void mousePressed(MouseEvent e)
	{	actief = true;
		if(toggle)
			toggleAan = !toggleAan;
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