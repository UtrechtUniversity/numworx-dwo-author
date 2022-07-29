package fi.wiskopdr.tekstobjects;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import fi.beans.base64code.StringCodeObject;
import fi.beans.iconan.DefaultImageCache;
import fi.beans.iconan.Iconan;
import fi.beans.iconan.ImageCache;
import fi.beans.iconan.SimpleCache;
import fi.wiskopdr.TekstVakPanel;
import fi.wiskopdr.WiskOpdr;

public class TekstImageVak extends TekstDeelVak implements MouseListener, MouseMotionListener {

	public static String IMAGE_MAP = "$IMAGE$MAP$";
	private boolean selected;
	private boolean editMode;
	
	private int startX, startY;
	private boolean eersteKeer = true;
	
	public static void setImageMap(Object map, ImageCache cache)
	{
		if(map != null)
			imagemap = (Hashtable)map;
		imagecache = cache;
	}

	public static Hashtable getImageMap()
	{
		return imagemap;
	}
	
	public static ImageCache getImageCache() {
	  return imagecache;
	}
	
	static private Hashtable imagemap = new Hashtable();
	static private ImageCache imagecache = new DefaultImageCache();

	private Dialog imageDialog;
	private Iconan iconman;
	
	private String imagename = "EMPTY";
	private JComponent image;
	
	public TekstImageVak(TekstVak tekstVak) {
		super(tekstVak);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public String toString()
	{	return "$I" + imagename + "@";
	}
	
	public static String getImageMapString()
	{	String s = StringCodeObject.encodeObjectToString(imagemap);
	    return s;
	}
	
	public static void setImageMapString(String s)
	{	if(s==null || s.equals(""))return;
		Object o = StringCodeObject.decodeStringToObject(s);
		imagemap = (Hashtable)o;
		imagecache = new DefaultImageCache();
	}

	public void editImage() {
		if(iconman==null)
			iconman = new Iconan(WiskOpdr.applet, tekstVak, getImageMap(), getImageCache());
		iconman.editImage(imagename, tekstVak, this);
		
//		if(imageDialog == null)
//		{
//			Frame f = JOptionPane.getFrameForComponent(tekstVak);
//			imageDialog = new Dialog(f,"title", true);
//			imageDialog.setLayout(new BorderLayout());
//			iconman = new Iconan(WiskOpdr.applet, tekstVak, imagemap);
//			imageDialog.add(iconman);
//			imageDialog.pack();
//			iconman.addActionListener(this);
//		}
//		iconman.select(imagename);
//		imageDialog.show();

		zetMaat();
	}

	public void paint(Graphics g)
	{	

	  super.paint(g);
	  Dimension size = getSize();
		if(image == null)
		{	g.setColor(Color.black);
			g.drawRect(0,0,size.width-1,size.height-1);
		} 
		else if (selected  && editMode)
		{	drawImage(g, size);
			g.setColor(new Color(0,0,0,128));
			g.fillRect(0,0,size.width-1,size.height-1);
		}
		
		
		else {
			drawImage(g, size);
		}
	}

	private void drawImage(Graphics g, Dimension size) {
//		try {
//			g.drawImage(image, 0, 0, size.width, size.height, this);
//		} catch (Exception e) {
//			// SecurityException mostly.
//			java.util.logging.Logger.getLogger(getClass().getName()).severe("drawImage " + e);
//		}
	}
	
	public void setSelected(boolean b)
	{	selected = b;
	}
	
	public boolean isSelected()
	{	return selected;
	}

	/* (non-Javadoc)
	 * @see fi.wiskopdr.tekstobjects.TekstElement#zetMaat()
	 */
	public void zetMaat() {
		int w = iconman.getWidth(imagename);
		int h = iconman.getHeight(imagename);
		boolean vol = iconman.isVolBreedte(imagename);
		if(h == -1) h = 16;
		if(w == -1) w = 16;
		setSize(w,h);
		ashoogte=15; // TODO getVAlign?
		if(getParent()instanceof TekstElement) {
			((TekstElement)getParent()).zetMaat();
		}
		if(vol) {
			int volWidth = tekstVak.getSize().width-2*tekstVak.geefMarge();
			int volHeight = volWidth*h/w;
			setSize(volWidth,volHeight);
		}
		if(getParent()instanceof TekstElement) {
			((TekstElement)getParent()).zetMaat();
		}
	}
	/**
	 * restore ImageVak met plaatje.
	 * @see fi.wiskopdr.tekstobjects.TekstDeelVak#vulVak(java.lang.String)
	 */
	public void vulVak(String s) {
		imagename = s;
		if(iconman == null)
		{
			iconman = new Iconan(WiskOpdr.applet, imagemap, imagecache);
		}
		if(image != null) 
		  remove(image);
		image = iconman.getComponent(s);
		if(image != null) {
		  add(image);
          image.setBounds(0, 0, getWidth(), getHeight());
		}
		zetMaat();
	}
	
	public void setBounds(int x, int y, int w , int h) { // Layoutmanager
	  super.setBounds(x, y,   w, h);
	  if(image != null) 
	    image.setSize(w,h);
	}
	
	public void setEditMode(boolean b)
	{	editMode = b;
        //if(b)setBackground(Color.lightGray);
		
	}
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mousePressed(MouseEvent e) {
		if(editMode && e.isPopupTrigger())
			editImage();
		startX = e.getX();
		startY = e.getY();
		
	}
	public void mouseReleased(MouseEvent e) {
		if(editMode && e.isPopupTrigger())
			editImage();
		eersteKeer = true;
	}
	
	public void mouseMoved(MouseEvent e)
	{
	}
	
	public void mouseDragged(MouseEvent e)
	{	
		if(selectable)
		{	if(Math.abs(startX-e.getX())>=3 || Math.abs(startY-e.getY())>=3)
			{	//draggingToSelect = true;
			}
			if(Math.abs(startX-e.getX())<3 && Math.abs(startY-e.getY())<3)
			{	
				return;
			}
			if(getParent()instanceof TekstRegel)
			{	MouseEvent en = new MouseEvent((TekstRegel)getParent(),e.getID(),e.getWhen(),e.getModifiers(), getLocation().x,e.getY()+getLocation().y,1,false);
				MouseEvent ed = new MouseEvent((TekstRegel)getParent(),e.getID(),e.getWhen(),e.getModifiers(), e.getX()+getLocation().x,e.getY()+getLocation().y,1,false);
			
				if(eersteKeer)
				{	((TekstRegel)getParent()).mousePressed(en);
					eersteKeer=false;
				}
			}
			//waiting = false;
			if(e.getX()<0 || e.getX()>getSize().width || e.getY()<0 || e.getY()>getSize().height)
			{	//terug = false;
				
				
				if(getParent()instanceof TekstRegel)
				{	MouseEvent en = new MouseEvent((TekstRegel)getParent(),e.getID(),e.getWhen(),e.getModifiers(), getLocation().x,e.getY()+getLocation().y,1,false);
					MouseEvent ed = new MouseEvent((TekstRegel)getParent(),e.getID(),e.getWhen(),e.getModifiers(), e.getX()+getLocation().x,e.getY()+getLocation().y,1,false);
				
					if(eersteKeer)
					{	((TekstRegel)getParent()).mousePressed(en);
						eersteKeer=false;
					}
					((TekstRegel)getParent()).mouseDragged(ed);
					//((FormuleElement)getParent().getParent()).requestFocus();
					//formuleVak.zetActieveRegel((FormuleRegel)getParent().getParent());
					//((FormuleRegel)getParent().getParent()).setSelection(((FormuleElement)getParent()).getLocation().x, ((FormuleElement)getParent()).getLocation().x+1);
				}
				
			}
			else
			{	setSelected(true);
				
				
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see fi.wiskopdr.tekstobjects.TekstDeelVak#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==iconman)
		{
			String name = e.getActionCommand();
			if(!"".equals(name))
			{
				imagename = name;
				if(image != null) 
				  remove(image);
				this.image = iconman.getComponent(name);
				if(image != null) {
				  add(image);
				  image.setBounds(0, 0, getWidth(), getHeight());
				}
				repaint();
				WiskOpdr.setLaunchDataChanged();
			}
			if("cancel".equals(e.getActionCommand())) 
	        {
	            setSelected(true);
	            tekstVak.layoutTekst();
	            tekstVak.deleteSelection();
	            tekstVak.requestFocus();
	            
	        }
		}

		if(imageDialog!=null)
			imageDialog.hide();
	}

}
