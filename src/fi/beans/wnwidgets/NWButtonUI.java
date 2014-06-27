package fi.beans.wnwidgets;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Hashtable;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.basic.BasicButtonUI;

import fi.beans.appletutil.AppletUtil;
import fi.wiskopdr.formuleobjects.FormuleButton;

public class NWButtonUI extends BasicButtonUI {

	private String skin;
	private static Hashtable _instances;
	private static NWButtonUI _instance;
	
	private static String[] imageNames = 
	{	
		"mw_plus.gif",
		"mw_min.gif",
		"mw_maal.gif",
		"mw_deel.gif",
		"mw_haakjesweg.gif",	
		"mw_herleid.gif",
		"mw_abc.gif",
		"mw_sub.gif",
		"mw_tip.gif",
		"mw_help.gif",
		"mw_losop.gif",
		
		"mw_gelijkwaardig.gif",
		"mw_terug.gif",
		
		"mw_haakjes.gif",
		"mw_breuk.gif",
		"mw_kwadraat.gif",
		"mw_macht.gif",
		"mw_wortel.gif",
		"mw_ndewortel.gif",
		"mw_ndewortel.gif",
		"mw_ndelog.gif",
		"mw_diff.gif",
		"mw_primitieve.gif",
		"mw_integraal.gif",
		"mw_prv.gif",
		"mw_abs.gif",
		"mw_subscript.gif",
		"mw_bin.gif",
		"mw_limiet0.gif",
		"mw_limiet1.gif",
		"mw_limiet2.gif",
		
		"mw_vraagteken.gif",
		
		"mw_meer.gif",
		"mw_meerpallet_1.gif",
		"mw_meerpallet_2.gif",
		"mw_meerpallet_3.gif",
		"mw_meerpallet_4.gif",
		"mw_meerpallet_5.gif",
		"mw_meerpallet_6.gif",
		"mw_meerpallet_7.gif",
		"mw_meerpallet_8.gif",
		"mw_meerpallet_9.gif",
		"mw_meerpallet_10.gif",
		"mw_meerpallet_11.gif",
		"mw_meerpallet_12.gif",
		"mw_meerpallet_13.gif",
		"mw_meerpallet_14.gif",
		"mw_meerpallet_15.gif",
		"mw_meerpallet_16.gif",
		"mw_meerpallet_17.gif",
		"mw_meerpallet_18.gif",
		"mw_meerpallet_19.gif",
		"mw_meerpallet_37.gif",
		"mw_meerpallet_38.gif",
		"mw_meerpallet_39.gif",
		"mw_meerpallet_40.gif",
		"mw_meerpallet_41.gif",
		"mw_meerpallet_42.gif",
		
		"mw_formbutton_skin_white.png",
		"mw_formbutton_skin_gray.png",
		"mw_formbutton_skin_rood.png",
		"mw_formbutton_skin_orange.png",
		"mw_formbutton_skin_tablet.png",
		"wngrafiekbutton.png",
		"wngeogebrabutton.png",
		"wntekstvakbutton.png",
		
		"gr_formbutton_skin_blue.png",
		"gr_formbutton_skin_blue_full.png",
		"gr_formbutton_skin_graf.gif",
		
		"gr_zoomstandaard.png",
		"gr_zoomin.png",
		"gr_zoominx.png",
		"gr_zoominy.png",
		"gr_zoomuit.png",
		"gr_zoomuitx.png",
		"gr_zoomuity.png",
	};
	
	private static Hashtable images; 
	
	public static void setIcon(String code, JButton button){
		String imageName = "";
		if(code.equals("±")) imageName = "mw_meerpallet_1.gif";
		else if(code.equals("\u2248")) imageName = "mw_meerpallet_2.gif";
		else if(code.equals("?")) imageName = "mw_vraagteken.gif";
		else if(code.equals("\u2260")) imageName = "mw_meerpallet_3.gif";
		else if(code.equals("<")) imageName = "mw_meerpallet_4.gif";
		else if(code.equals("\u2264")) imageName = "mw_meerpallet_5.gif";
		else if(code.equals("\u2265")) imageName = "mw_meerpallet_6.gif";
		else if(code.equals(">")) imageName = "mw_meerpallet_7.gif";
		else if(code.equals("\u2227")) imageName = "mw_meerpallet_8.gif";
		else if(code.equals("\u2228")) imageName = "mw_meerpallet_9.gif";
		else if(code.equals("\u2205")) imageName = "mw_meerpallet_10.gif";
		else if(code.equals("pi")) imageName = "mw_meerpallet_11.gif";
		else if(code.equals("e")) imageName = "mw_meerpallet_12.gif";
		else if(code.equals("\u03b1")) imageName = "mw_meerpallet_13.gif";
		else if(code.equals("\u03b2")) imageName = "mw_meerpallet_14.gif";
		else if(code.equals("\u03b3")) imageName = "mw_meerpallet_15.gif";
		else if(code.equals("\u03bc")) imageName = "mw_meerpallet_16.gif";
		else if(code.equals("\u03c3")) imageName = "mw_meerpallet_17.gif";
		else if(code.equals("\u2218")) imageName = "mw_meerpallet_18.gif";
		else if(code.equals("\u221e")) imageName = "mw_meerpallet_19.gif";
		else if(code.equals("[")) imageName = "mw_meerpallet_37.gif";
		else if(code.equals("]")) imageName = "mw_meerpallet_38.gif";
		else if(code.equals("\u3008")) imageName = "mw_meerpallet_39.gif";
		else if(code.equals("\u3009")) imageName = "mw_meerpallet_40.gif";
		else if(code.equals("\u2190")) imageName = "mw_meerpallet_41.gif";
		else if(code.equals("\u2192")) imageName = "mw_meerpallet_42.gif";
		else if(code.equals("grafiekcomponent")) imageName = "wngrafiekbutton.png";
		else if(code.equals("tekstvak")) imageName = "wntekstvakbutton.png";
		else if(code.equals("geogebra")) imageName = "wngeogebrabutton.png";
		else if(code.equals("gr_zoomstandaard")) imageName = "gr_zoomstandaard.png";
		else if(code.equals("gr_zoomin")) imageName = "gr_zoomin.png";
		else if(code.equals("gr_zoominx")) imageName = "gr_zoominx.png";
		else if(code.equals("gr_zoominy")) imageName = "gr_zoominy.png";
		else if(code.equals("gr_zoomuit")) imageName = "gr_zoomuit.png";
		else if(code.equals("gr_zoomuitx")) imageName = "gr_zoomuitx.png";
		else if(code.equals("gr_zoomuity")) imageName = "gr_zoomuity.png";
		else imageName = "mw_"+code+".gif";
		//boolean hasIcon = true;
		try{
			button.setIcon(new ImageIcon((Image)images.get(imageName)));
		}
		catch(Exception e)
		{	//hasIcon = false;
		}
		//if(!hasIcon)button.setText(code);
	}
	
	static public NWButtonUI getInstance(String skin) {
		if(_instances==null) _instances = new Hashtable();
		if(_instances.containsKey(skin))
			return (NWButtonUI)_instances.get(skin);
		else {
			
			_instance = new NWButtonUI(skin);
			_instances.put(skin, _instance);
			return _instance;
		}
		
	}
	
	public static void loadImages(String[] imageNames, JComponent c)
	{	if(images==null)images = new Hashtable();
		MediaTracker tr = new MediaTracker(c);
		Image[] image = new Image[imageNames.length];
		for(int i=0 ; i<imageNames.length ; i++)
		{	image[i] = Toolkit.getDefaultToolkit().getImage((new NWButtonUI()).getClass().getResource("resources/" + imageNames[i]));
			tr.addImage(image[i], 0);
		}
		try{tr.waitForAll();} catch(Exception e) {};
		for(int i=0 ; i<imageNames.length ; i++)
		{	images.put(imageNames[i], image[i]);
		}
		
	}
	
	public static Image loadImage(String imageName, Component c)
	{	MediaTracker tr = new MediaTracker(c);
		Image image = Toolkit.getDefaultToolkit().getImage((new NWButtonUI()).getClass().getResource("resources/" + imageName));
		tr.addImage(image, 0);
		try{tr.waitForAll();} catch(Exception e) {};
		return image;
	}
	
	/**
	 * 
	 */
	public NWButtonUI() {
		super();
	}
	
	NWButtonUI(String skin) {
		super();
		this.skin = skin;
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicButtonUI#paint(java.awt.Graphics, javax.swing.JComponent)
	 */
	public void paint(Graphics g, JComponent c) {
		if("mw_formbutton_skin_white.png".equals(skin))
		{	super.paint(g, c);
			return;
		}
		Dimension size = c.getSize();
		Image skinImage = (Image)images.get(skin);
		
		ButtonModel model = ((AbstractButton)c).getModel();
		boolean rollOver = model.isRollover();
		boolean isPressed = model.isPressed();
		
		if(!c.isEnabled() && "gr_formbutton_skin_blue.png".equals(skin))
		{	
			g.setColor(new Color(200,200,200));
			g.fillRect(0,0,c.getWidth(),c.getHeight());
			g.setColor(Color.white);
			g.fillRect(1,1,c.getWidth()-2,c.getHeight()-2);
			c.setForeground(new Color(200,200,200));
		}
		else if(isPressed && "gr_formbutton_skin_blue.png".equals(skin))
		{	g.setColor(new Color(70,117,186));
			g.fillRect(0,0,c.getWidth(),c.getHeight());
			c.setForeground(Color.white);
		}
		else if(isPressed && "gr_formbutton_skin_blue_full.png".equals(skin))
		{	g.setColor(new Color(70,117,186));
			g.fillRect(0,0,c.getWidth(),c.getHeight());
			g.setColor(Color.white);
			g.fillRect(1,1,c.getWidth()-2,c.getHeight()-2);
			c.setForeground(new Color(70,117,186));
		}
		else if(rollOver && "gr_formbutton_skin_blue.png".equals(skin))
		{	g.setColor(new Color(70,117,186));
			g.fillRect(0,0,c.getWidth(),c.getHeight());
			c.setForeground(Color.white);
		}
		else if(rollOver && "gr_formbutton_skin_blue_full.png".equals(skin))
		{	g.setColor(new Color(70,117,186));
			g.fillRect(0,0,c.getWidth(),c.getHeight());
			g.setColor(Color.white);
			g.fillRect(1,1,c.getWidth()-2,c.getHeight()-2);
			c.setForeground(new Color(70,117,186));
		}
		
		else 
		{	if("gr_formbutton_skin_blue.png".equals(skin))c.setForeground(new Color(70,117,186));
			if("gr_formbutton_skin_blue_full.png".equals(skin))c.setForeground(Color.white);
			int w = skinImage.getWidth(c);
			int h = skinImage.getHeight(c);
			int W = 4;
//			int a = Math.min(size.height, size.width);
//			g.setColor(Color.pink);
//			g.fillRoundRect(0, 0, size.width, size.height, a, a);
//			g.setColor(Color.gray);
//			g.drawRoundRect(0, 0, size.width, size.height, a, a);
			g.drawImage(skinImage, 0, 0, W, size.height, 0, 0, W, h, c);
			g.drawImage(skinImage, size.width-W, 0, size.width, size.height, w-W, 0, w, h, c);
			g.drawImage(skinImage, W, 0, size.width-W, size.height, W, 0, W+8, h, c);
		}
		if(c instanceof FormuleButton && ((JButton)c).getIcon()==null)((FormuleButton)c).paintCode(g);
		
		
		
		super.paint(g, c);
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicButtonUI#paintButtonPressed(java.awt.Graphics, javax.swing.AbstractButton)
	 */
	protected void paintButtonPressed(Graphics g, AbstractButton b) {
		// TODO Auto-generated method stub
		super.paintButtonPressed(g, b);
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicButtonUI#paintFocus(java.awt.Graphics, javax.swing.AbstractButton, java.awt.Rectangle, java.awt.Rectangle, java.awt.Rectangle)
	 */
	protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect,
			Rectangle textRect, Rectangle iconRect) {
		// TODO Auto-generated method stub
		super.paintFocus(g, b, viewRect, textRect, iconRect);
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicButtonUI#paintIcon(java.awt.Graphics, javax.swing.JComponent, java.awt.Rectangle)
	 */
	protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
		// TODO Auto-generated method stub
		int x = (c.getWidth() - iconRect.width)/2;
		int y = (c.getHeight() - iconRect.height)/2;
		super.paintIcon(g, c, new Rectangle(x,y,iconRect.width,iconRect.height));
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicButtonUI#paintText(java.awt.Graphics, javax.swing.AbstractButton, java.awt.Rectangle, java.lang.String)
	 */
	protected void paintText(Graphics g, AbstractButton b, Rectangle textRect,
			String text) {
		// TODO Auto-generated method stub
		super.paintText(g, b, textRect, text);
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
	 */
	public void update(Graphics g, JComponent c) {
		// TODO Auto-generated method stub
		super.update(g, c);
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicButtonUI#installUI(javax.swing.JComponent)
	 */
	public void installUI(JComponent c) {
		// TODO Auto-generated method stub
		super.installUI(c);
		if(images==null)loadImages(imageNames, c);
		c.setBorder(null);
		c.setOpaque(false);
		c.setFont(new Font(c.getFont().getFamily(), Font.PLAIN, 12));
	}
	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicButtonUI#getPreferredSize(javax.swing.JComponent)
	 */
	public Dimension getPreferredSize(JComponent c) {
		Dimension s = super.getPreferredSize(c);
		Image skinImage = (Image)images.get(skin);
		s.height = skinImage.getHeight(null);
		s.width  = skinImage.getWidth(null);
		return s;
	}

}
