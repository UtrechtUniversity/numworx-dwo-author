package fi.wiskopdr.tekstobjects;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.*;

import netscape.javascript.JSObject;

import fi.wiskopdr.WiskOpdr;


public class LinkRegel extends JPanel implements TekstElement, MouseListener, MouseMotionListener,KeyListener, FocusListener
{	
	private TekstVak tekstVak;
	private int ashoogte;
	
	private static LinkIF wiskOpdr;
	private FontMetrics fm;
	private boolean caretVisible = false;
	private KnipperDraad kd;
	private boolean selectable = true;
	private boolean editable = true;
	private boolean hasFocus = false;
	private boolean selected = false;
	private boolean aan = false;
	private int caretX = 0;
	private int caretPos = 0;
	private int kc;
	
	private int startx = 0;
	private int starty = 0;
	
	private boolean eersteKeer=true;
	private boolean terug=true;
		
	Color bgColor = new Color(255,255,255);
	//private Link link = new Link("link","http://",400,400);
	String[] httpString = new String[] {"http://", "http://", "http://", "http://",
			"http://", "http://", "http://", "http://", "http://", "http://"};
	
	private Link link = new Link("link", httpString, 400, 400, null);
	
	public static void setJSObjectOwner(LinkIF linkIF)
	{	LinkRegel.wiskOpdr = linkIF;
	}
	
	public LinkRegel(TekstLinkVak tv)
	{	bgColor = getBackground();
		//tekstVak = tv;
				
		setLayout(null);
		addMouseListener(this);
		addMouseMotionListener(this);
		//addKeyListener(this);
		//addFocusListener(this);
		
		setFont(tv.getFont());
		fm = getFontMetrics(getFont());
		setSize(fm.getAscent()/2,fm.getAscent()+fm.getDescent());
		ashoogte = fm.getAscent()/2;
		
		setOpaque(false);
				
	    findJSObject(wiskOpdr);

	}

	/**
	 * Initialize linkif with a JSObject. 
	 * Code shared with WidgetBridge.
	 * @param linkif may be null
	 * 
	 */

	public static void findJSObject(LinkIF linkif) {
		if(linkif == null || linkif.getJSObject() != null) return;
		AppletContext ac = null;
		ac = linkif.getAppletContext();
		Applet ap = null;
		if(ac!=null) 
		{
//				ap = ac.getApplet("wiskopdr");
//				if(ap==null)ap = ac.getApplet("DWO");
// Wim: Vraagje: Maakt het eigenlijk uit welk Window de applets teruggeven? Is het altijd dezelfde?
// zoiets van één Window per AppletContext?
			Enumeration em = ac.getApplets();
			if(em != null) while(em.hasMoreElements())
			{
				Applet candidate = (Applet) em.nextElement();
				if(ap == null /*|| betterCandidate(ap, object) */ )
					ap = candidate; break;
			}
		}

		try
		{	if(ap!=null) linkif.setJSObject(JSObject.getWindow(ap));
		} 
		catch( Exception e )
		{	e.printStackTrace(); 
		}
	}
	
	public void setBackground(Color c)
	{	super.setBackground(c);
		Component[] components = getComponents();
		for(int i=0 ; i<components.length ; i++)
		{
			components[i].setBackground(c);
		}
		
	}
	
	public Link getLink()
	{	return link;
	}
	
	public void setLink(Link link)
	{	this.link = link;
	}
	
	public void setFont(Font f)
	{	super.setFont(f);
		fm = getFontMetrics(getFont());
		int b=1;
		int h1=0;
		int h2=0;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	//((TekstElement)getComponent(i)).setFont(f);
			getComponent(i).setFont(f);
			int hoogte = getComponent(i).getSize().height;
			int ash = ((TekstElement)getComponent(i)).getAsHoogte();
			if(ash>h1)h1=ash;
			if(hoogte-ash>h2)h2=hoogte-ash;
		}
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	getComponent(i).setLocation(b,h1-((TekstElement)getComponent(i)).getAsHoogte());
			b += getComponent(i).getSize().width;
		}
		if(getComponentCount()>0)
		{	setSize(b,h1+h2);
			ashoogte = h1;
		}
		else 
		{	setSize(fm.getAscent()/2, fm.getAscent() + fm.getDescent());
			ashoogte = fm.getAscent()/2;
		}
	}
	
	public void paint(Graphics g)
	{	g.setColor(getBackground());
		if(selected)
			g.setColor(Color.black);
		g.fillRect(0,0,getSize().width-1,getSize().height-1);
		
		
		
		if( getComponentCount() ==0)//hasFocus ||
		{	g.setColor(new Color(200,200,200));
			g.drawRect(0,0,getSize().width-1,getSize().height-1);
		}
		super.paint(g);
		//Formuletekens worden direct getekend en niet binnen het eigen component.
		//Bij 'italic' fonts vallen ze namelijk soms buiten het component.
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(getComponent(i)instanceof TekstTeken)
			{	
				TekstTeken tt = (TekstTeken)getComponent(i);
				tt.setFont(getFont());
				tt.paint(g,tt.getLocation().x,tt.getLocation().y);
			}
		}
		g.setColor(Color.black);
		if(caretVisible && editable && aan)
		{	g.drawLine(caretX,0,caretX,getSize().height);
		}
		
		
		
	}
	
	public void setUnderlined(boolean b)
	{	Component[] components = getComponents();
		for(int i=0 ; i<components.length ; i++)
		{	if(components[i] instanceof TekstTeken)((TekstTeken)components[i]).setUnderlined(b);
		}
		
	}
	
	public void setEditable(boolean b)
	{	editable = b;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	((TekstElement)getComponent(i)).setEditable(b);
		}	
	}
	
	public void setSelectable(boolean b)
	{	selectable = b;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	((TekstElement)getComponent(i)).setSelectable(b);
		}
	}
	
	public void setSelected(boolean b)
	{	//if(selected!=b)
		{	if(b)caretVisible = false;
			selected = b;
			for(int i=0 ; i<getComponentCount()  ; i++)
			{	((TekstElement)getComponent(i)).setSelected(b);
			}
			repaint();
		}
	}
	
	public boolean isSelected()
	{	return selected;
	}
	
	public void zetMaat()
	{	int b=1;
		int h1=0;
		int h2=0;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	int hoogte = getComponent(i).getSize().height;
			int ash = ((TekstElement)getComponent(i)).getAsHoogte();
			if(ash>h1)h1=ash;
			if(hoogte-ash>h2)h2=hoogte-ash;
		}
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	getComponent(i).setLocation(b,h1-((TekstElement)getComponent(i)).getAsHoogte());
			b += getComponent(i).getSize().width;
		}
		if(getComponentCount()>0)
		{	setSize(b,h1+h2);
			ashoogte = h1;
		}
		else 
		{	setSize(fm.getAscent()/2, fm.getAscent() + fm.getDescent());
			ashoogte = fm.getAscent()/2;
		}
		
		if(getParent()instanceof TekstElement)((TekstElement)getParent()).zetMaat();
	}

	public void neemFocus(String richting, TekstElement fe)
	{	requestFocus();
		//tekstVak.zetActieveRegel(this);
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(getComponent(i)==fe)
			{	if(richting.equals("rechts"))
				{	caretPos = i+1;
					caretX = ((Component)fe).getLocation().x + ((Component)fe).getSize().width -1;
				}
				else
				{	caretPos = i;
					caretX = ((Component)fe).getLocation().x;
				}
			}
		}
	}
	
	public void neemFocus(String richting)
	{	requestFocus();
		//tekstVak.zetActieveRegel(this);
		if(richting.equals("rechts"))
		{	caretPos = 0;
			caretX = 0;
		}
		if(richting.equals("links"))
		{	caretPos = getComponentCount();
			if(getComponentCount()==0)caretX=0;
			else caretX = getSize().width - 1;
		}
	}
	
	public void setCaret(int pos)
    {	caretPos = pos;
    	int x = 0;
        for(int i=0 ; i<pos  ; i++)
		{	x = x + getComponent(i) .getSize().width;
		}
		caretX = x;
		repaint();
	}
	
	public void setCaretVisible(boolean b)
	{	if (b)
		{	if(kd!=null)
			{	kd.maakDood();
				kd=null;
			}
			kd = new KnipperDraad();
			kd.start();
			caretVisible = true;
			hasFocus = true;
			repaint();
		} 
		else
		{	if(kd!=null)
			{	kd.maakDood();
				kd=null;
			}
			caretVisible = false;
			hasFocus = false;
			repaint();
		}
	}	
	public void setCaretPosition(int x)
    {	int posX = 0;
		int grensX = 0;
        for(int i=0 ; i<getComponentCount()  ; i++)
		{	grensX = posX + getComponent(i) .getSize().width/2;
			if(x<grensX)
			{	caretPos = i;
				caretX = posX;
				break;
			}
			else
			{	caretPos = i+1;
				caretX = posX + getComponent(i) .getSize().width;
			}
			posX += getComponent(i) .getSize().width;
		}
		deSelect();
    }
	
	public void setSelection(int x1, int x2)
    {	int posX = 0;
		int grensX = 0;
		int comp1=0;
		int comp2=-1;
        for(int i=0 ; i<getComponentCount()  ; i++)
		{	grensX = posX + getComponent(i) .getSize().width;
			if(x1<grensX)
			{	comp1 = i;
				break;
			}
			else
			{	comp1 = i+1;
			}
			posX += getComponent(i) .getSize().width;
		}
		posX = 0;
		grensX = 0;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	grensX = posX;// + getComponent(i) .getSize().width/2;
			if(x2<grensX)
			{	comp2 = i-1;
				caretPos = i;
				caretX = posX;
				break;
			}
			else
			{	comp2 = i;
				caretPos = i+1;
				caretX = posX + getComponent(i) .getSize().width;
			}
			posX += getComponent(i) .getSize().width;
			
		}
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(i>=comp1 && i<=comp2)
			{	((TekstElement)getComponent(i)).setSelected(true);
				caretVisible = false;
			}
			else ((TekstElement)getComponent(i)).setSelected(false);
		}
		repaint();
    }
	
	public void deSelect()
	{	//caretVisible = true;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	((TekstElement)getComponent(i)).setSelected(false);
		}
	}
	
	public void deleteSelection()
	{	for(int i=getComponentCount() -1 ; i>-1 ; i--)
		{	if(((TekstElement)getComponent(i)).isSelected())
			{	caretPos--;
				caretX -= getComponent(i) .getSize().width;
				remove(i);
			}
		}
		caretVisible = true;
		zetMaat();
		startx = 0;
		starty = 0;
	}
	
	public void removeAll()
	{	super.removeAll();
		caretPos = 0;
		caretX = 0;
		zetMaat();
	}
	
	public void insert(TekstElement te)
	{	//if(pos<=getComponentCount())
		{	((Component)te).setForeground(getForeground());
			add((Component)te,caretPos);
			zetMaat();
			caretX += getComponent(caretPos) .getSize().width;
			caretPos++;
		}
	}
	
	public void editLink()
	{	Link newLink = AddLinkDialog.editLink(this,link);
        	if(newLink!=null)link = newLink;
        	removeAll();
        	String s = link.getLinkTekst();
        	for(int i=0 ; i<s.length(); i++) 
        	insert(new TekstTeken(s.charAt(i)));
		
		
	}
	
	public void mousePressed(MouseEvent e)
	{	if(editable)// && (e.getModifiers()== InputEvent.BUTTON3_MASK || e.isControlDown()))
		{	editLink();
            
			return;
		}
		//else if(!editable)
		//{	link.activate();
		//	
		//	
		//}
		else
		{	requestFocus();
			startx = e.getX();
			starty = e.getY();
			setCaretPosition(e.getX());
			setSelected(false);
			caretVisible = true;	
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{	if(selectable)
		{	if(e.getX()<0 || e.getX()>getSize().width || e.getY()<0 || e.getY()>getSize().height)
			{	terug = false;
				
				if(getParent().getParent()instanceof TekstRegel)
				{	MouseEvent en = new MouseEvent((TekstRegel)getParent().getParent(),e.getID(),e.getWhen(),e.getModifiers(), e.getX()+getLocation().x+getParent().getLocation().x,e.getY()+getLocation().y+getParent().getLocation().y,1,false);
					
					if(eersteKeer)
					{	((TekstRegel)getParent().getParent()).mousePressed(en);
						eersteKeer=false;
					}
					((TekstRegel)getParent().getParent()).mouseDragged(en);
				}
			}
			else
			{	terug = true;
				setSelected(false);
				requestFocus();
				//formuleVak.zetActieveRegel(this);
				if(e.getX() < startx)setSelection(e.getX(),startx);
				else setSelection(startx,e.getX());
				
			}
			
		}
	}
		
	public void mouseClicked(MouseEvent e)
	{	if(selectable)
		{	int clickCount = e.getClickCount();
			if(clickCount>1)
			{	for(int i=0 ; i<getComponentCount()  ; i++)
				{	((TekstElement)getComponent(i)).setSelected(true);
				}
				repaint();
				caretVisible = false;
				caretPos = getComponentCount() ;
				if(getComponentCount() >0)caretX = getSize().width-1;
				else caretX = 0;
			}
		}
	}
	
	public int vraagTotaalScoreOp()
	{
		int scoreTotaal = 0;
		//bijv het totaal van wat nu onder de bolletjes staat. dat is het totaal van
		//alle scorecomponenten.
		// of de totaalsom van orScores in opdrNavStruct.
		
		return scoreTotaal;
	}
	
	public void mouseReleased(MouseEvent e)
	{	
		if(!editable)
		{	link.activate(vraagTotaalScoreOp());
		}
		else {
			eersteKeer=true;
			startx = 0;
			starty = 0;
		}
	}
		
	public void mouseEntered(MouseEvent e)
	{	setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
	public void mouseMoved(MouseEvent e){;}
	public void mouseExited(MouseEvent e)
	{	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	public void keyPressed(KeyEvent e)
	{   if (editable)
        {   kc = e.getKeyCode();
           if (kc == KeyEvent.VK_LEFT)
            {   if (caretPos > 0 && getComponent(caretPos-1)instanceof TekstTeken)
                {   caretPos--;
                    caretX -= getComponent(caretPos) .getSize().width;
					deSelect();
                }
				else if(!(caretPos > 0))
				{	if(getParent() instanceof TekstElement)
					{	((TekstElement)getParent()).neemFocus("links",this);
					}
				}
				else
				{	((TekstElement)getComponent(caretPos-1)).neemFocus("links");
				}
			}
            else if (kc == KeyEvent.VK_RIGHT)
            {   if (caretPos < getComponentCount()&& getComponent(caretPos)instanceof TekstTeken)
                {   caretPos++;
                    caretX += getComponent(caretPos-1).getSize().width;
					deSelect();
				}
				else if (!(caretPos < getComponentCount()))
				{	if(getParent() instanceof TekstElement)
					{	((TekstElement)getParent()).neemFocus("rechts",this);
					}
				}
				else
				{	((TekstElement)getComponent(caretPos)).neemFocus("rechts");
				}
            }
			else if (kc == KeyEvent.VK_HOME)
            {   caretPos = 0;
                caretX = 0;
                deSelect();
            }
            else if (kc == KeyEvent.VK_END)
            {   caretPos = getComponentCount() ;
				if(getComponentCount() >0)caretX = getSize().width-1;
				else caretX = 0;
				deSelect();
			}
            else if (kc == KeyEvent.VK_DELETE)
            {	if(caretVisible)
				{	if(caretPos<getComponentCount() )
					{	remove(caretPos);
						zetMaat();
					}
				}
				else
				{	deleteSelection();
				}
            } 
			else if (kc == KeyEvent.VK_BACK_SPACE)
            {   if(caretVisible)
				{	if(caretPos>0)
					{	caretPos--;
						caretX -= getComponent(caretPos) .getSize().width;
						remove(caretPos);
						zetMaat();
					}
				}
				else
				{	deleteSelection();
				}
	  		}
	  		
         	repaint();
         	
		}
	}
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e)
    {	if (editable)
		{   // kc initialized by keyPressed
            int kt = e.getKeyChar();
            if (kt == KeyEvent.VK_ENTER)
            {	//tekstVak.finish();
			}
			else if ((kt != KeyEvent.VK_ESCAPE) &&
	                (kt != KeyEvent.VK_BACK_SPACE) &&
	                (kt != KeyEvent.VK_DELETE) &&
               		(kc != KeyEvent.VK_ENTER)
                    && (kc != KeyEvent.VK_SHIFT)
                    
                   )
      		{	if(!caretVisible)
				{	deleteSelection();
				}
				TekstTeken tekstTeken = new TekstTeken((char)kt);
				//tekstTeken.setFont(getFont());
				insert(tekstTeken);	
				tekstTeken.setFont(getFont());
				//add(new TekstTeken((char)kt),caretPos);
				//caretX += getComponent(caretPos) .getSize().width;
				//caretPos++;
				//int nr = caretPos;
            } 
            
            zetMaat();   
            repaint();
		}
	}
	
	public void focusGained(FocusEvent e)
    {   if (selectable)
		{	if(kd!=null)
			{	kd.maakDood();
				kd=null;
			}
			kd = new KnipperDraad();
			kd.start();
			caretVisible = true;
			hasFocus = true;
			repaint();
		}  
	}
	public void focusLost(FocusEvent e)
	{   if (selectable)
		{	if(kd!=null)
			{	kd.maakDood();
				kd=null;
			}
			//tekstVak.focusLost(e);
			caretVisible = false;
			hasFocus = false;
			repaint();
		}  
	}
	
	
	public String toString()
	{	String s = "";
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	s = s + ((TekstElement)getComponent(i)).toString();
		}
		s = s + link.toString();
		return s;
	}
	
	public void knipper(boolean b)
	{	if (b && selectable)
		{	if(kd!=null)
			{	kd.maakDood();
				kd=null;
			}
			kd = new KnipperDraad();
			kd.start();
			caretVisible = true;
			hasFocus = true;
			repaint();
		}
		else if(selectable)
		{	if(kd!=null)
			{	kd.maakDood();
				kd=null;
			}
			caretVisible = false;
			hasFocus = false;
			repaint();
		}   
	}
	
	class KnipperDraad extends Thread 
	{	boolean dood = false;
		public void run()
		{	while(!dood)
			{	int delay = 500;
				long t = System.currentTimeMillis();
				try
				{	t = t+delay;
					sleep(Math.max(1, t-System.currentTimeMillis()));
				}
    			catch(InterruptedException e)    // geen ;
				{   };
				if(aan)aan = false;
				else aan = true;
				repaint();
			}
		}
		public void maakDood()
		{	dood = true;
		}
	}
	
	//implementation of TekstElement
 	public Component add( Component comp ) 
	{	Component c = super.add(comp);
		if(getFont()!=null)comp.setFont(getFont());
		return c;
	}
	public Component add( Component comp, int index ) 
	{	Component c = super.add(comp, index);
		if(getFont()!=null)comp.setFont(getFont());
		return c;
	}
	
	public boolean isSpatie()
	{	return false;
	}
	
	public TekstVak getTekstVak()
	{
		return tekstVak;
	}
	
	public int getAsHoogte()
	{
		return ashoogte;
	}
	
}


