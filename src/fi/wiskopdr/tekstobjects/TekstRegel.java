package fi.wiskopdr.tekstobjects;

import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.event.*;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JPanel;

import fi.wiskopdr.TekstVakPanel;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.FormuleElement;
import fi.wiskopdr.formuleobjects.FormuleRegel;
import fi.wiskopdr.formuleobjects.FormuleTeken;

public class TekstRegel extends JPanel implements TekstElement,MouseListener, MouseMotionListener,KeyListener//, FocusListener
{	
	private TekstVak tekstVak;
	private int ashoogte;
	
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
	
	int correctieCursief = 0;
		
	Color bgColor = new Color(255,255,255);
    //private Font font = new Font("SansSerif", Font.PLAIN,11);
	
	boolean textRtoL = !ComponentOrientation.getOrientation(WiskOpdr.language).isLeftToRight();
	
	public TekstRegel(TekstVak tv)
	{	bgColor = getBackground();
		
		tekstVak = tv;
		editable = tv.isEditable();
		selectable = tv.isSelectable();
		
		setLayout(null);
		addMouseListener(this);
		addMouseMotionListener(this);
		//addKeyListener(this);
		//addFocusListener(this);
		
		setFont(tv.getFont());
		fm = getFontMetrics(getFont());
		setSize(fm.getAscent()/2,fm.getAscent()+fm.getDescent());
		ashoogte = fm.getAscent();///2;
		
		setOpaque(false);
	}
	
	public void setBackground(Color c)
	{	super.setBackground(c);
		Component[] components = getComponents();
		for(int i=0 ; i<components.length ; i++)
		{
			components[i].setBackground(c);
		}
		
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
            zetMaat();
		}
		else 
		{	setSize(fm.getAscent()/2, fm.getAscent() + fm.getDescent());
			ashoogte = fm.getAscent();///2;
		}
        
	}
	
	public void setForeground(Color c)
	{
		super.setForeground(c);
		Component[] components = getComponents();
		for(int i=0 ; i<components.length ; i++)
		{
			components[i].setForeground(c);
		}
	}
	
	static RenderingHints.Key KEY_TEXT_LCD_CONTRAST;
	static Object VALUE_TEXT_ANTIALIAS_LCD_HRGB;
	static {
		// if 1.6
		//KEY_TEXT_LDC_CONTRAST = RenderingHints.KEY_TEXT_LCD_CONTRAST;
		//VALUE_TEXT_ANTIALIAS_LCD_HRGB = RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB;
		try {
			KEY_TEXT_LCD_CONTRAST = (Key) RenderingHints.class.getField("KEY_TEXT_LCD_CONTRAST").get(null);
			VALUE_TEXT_ANTIALIAS_LCD_HRGB = RenderingHints.class.getField("VALUE_TEXT_ANTIALIAS_LCD_HRGB").get(null);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// Ignore if <=1.5
		}
	}
	
	public void paint(Graphics gr)
	{	
		Graphics g  = gr;
	    //if(WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("GR"))
		if( gr instanceof Graphics2D)
	    {   g = (Graphics2D)gr;
		    String jVersion =  System.getProperty("java.specification.version");
	    	if(!(jVersion.equals("1.3") || jVersion.equals("1.4") || jVersion.equals("1.5")))
			{	((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		        ((Graphics2D)g).setRenderingHint(KEY_TEXT_LCD_CONTRAST, new Integer(100));
		    }
	    	else
	    	{	((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    	}
	    }
	    
        g.setFont(WiskOpdr.tekstFontKlein);
        g.setColor(getBackground());
		if(selected)g.setColor(Color.black);
		//g.fillRect(0,0,getSize().width-1,getSize().height-1);
		
		if( getComponentCount() ==0)//hasFocus ||
		{	//g.setColor(new Color(200,200,200));
			//g.drawRect(0,0,getSize().width-1,getSize().height-1);
		}
		super.paint(g);
		//Formuletekens worden direct getekend en niet binnen het eigen component.
		//Bij 'italic' fonts vallen ze namelijk soms buiten het component.
		boolean inDeMaak = false;
		String woord = "";
		Point location = null;
		int length0 = 0; 
		int length1 = 0;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(getComponent(i)instanceof TekstTeken && i<getComponentCount()-1)
			{	TekstTeken tt = (TekstTeken)getComponent(i);
				if(!inDeMaak) 
				{	if(textRtoL) location = new Point(tt.getLocation().x+tt.getWidth(),tt.getLocation().y);
					else location = new Point(tt.getLocation().x,tt.getLocation().y);
					inDeMaak = true;
					length0 = 0; 
					length1 = 0;
					
				}
				length0 = fm.stringWidth(woord);
				woord += tt.geefChar();
				length1 = fm.stringWidth(woord);
				if(textRtoL)tt.setBounds(location.x - length1, tt.getLocation().y, length1-length0, tt.getHeight());
			}
			else
			{	if(getComponent(i)instanceof TekstTeken)
				{	TekstTeken tt = (TekstTeken)getComponent(i);
					length0 = fm.stringWidth(woord);
					woord += tt.geefChar();
					length1 = fm.stringWidth(woord);
					if(textRtoL && location!=null)tt.setBounds(location.x - length1, tt.getLocation().y, length1-length0, tt.getHeight());
				}
				g.setColor(getForeground());
				g.setFont(getFont());
				if(location!=null)
				{	if(textRtoL) g.drawString(woord, location.x-fm.stringWidth(woord), location.y+fm.getAscent());
					else g.drawString(woord, location.x, location.y+fm.getAscent());
				}
				inDeMaak = false;
				woord = "";
			}
			
		}
		/*
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(getComponent(i)instanceof TekstTeken)
			{	TekstTeken tt = (TekstTeken)getComponent(i);
				tt.paint(g,tt.getLocation().x,tt.getLocation().y);
			}
		}
		*/
		g.setColor(Color.black);
		if(caretVisible && editable && aan)
		{	g.drawLine(caretX,0,caretX,getSize().height);
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
	
	public void setLocation(int x, int y)
    {
		if(textRtoL)
        {
            Container parent = getParent();
            int xNew = parent.getWidth()-x-getWidth();
            super.setLocation(xNew,y);
        }
        else
        {
            super.setLocation(x,y);
        }
    }
	
	public void resize()
	{
		int b=0;
		int h1=0;
		int h2=0;
		
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	
			int hoogte = getComponent(i).getSize().height;
			int ash = ((TekstElement)getComponent(i)).getAsHoogte();
			if(ash>h1)h1=ash;
			if(hoogte-ash>h2)h2=hoogte-ash;
		}
		if(getComponentCount()>0)
		{	TekstElement feStart = (TekstElement)getComponent(getComponentCount()-1);
			TekstElement feEnd = (TekstElement)getComponent(0);
			boolean bStart = feStart instanceof TekstTeken && Character.isLetter(((TekstTeken)feStart).geefChar());
			boolean bEnd = feEnd instanceof TekstTeken && Character.isLetter(((TekstTeken)feEnd).geefChar());
			if(bStart || bEnd)
			{	correctieCursief = 2;
				b+=correctieCursief;
			}
			else correctieCursief = 0;
		}
		
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	getComponent(i).setLocation(b,h1-((TekstElement)getComponent(i)).getAsHoogte());
			b += getComponent(i).getSize().width;
		}
		if(getComponentCount()>0)
		{	setSize(b+Math.max(1,correctieCursief),h1+h2);
			ashoogte = h1;
		}
		else 
		{	setSize(fm.getAscent()/2, fm.getAscent() + fm.getDescent());
			ashoogte = fm.getAscent();///2;
		}
		if(textRtoL)
		{	
			boolean inDeMaak = false;
			String woord = "";
			Point location = null;
			int length0 = 0; 
			int length1 = 0;
			for(int i=Math.max(0, 0) ; i<getComponentCount()  ; i++)
			{	if(getComponent(i)instanceof TekstTeken && i<getComponentCount()-1)
				{	TekstTeken tt = (TekstTeken)getComponent(i);
					if(!inDeMaak) 
					{	if(textRtoL) location = new Point(tt.getLocation().x+tt.getWidth(),tt.getLocation().y);
						else location = new Point(tt.getLocation().x,tt.getLocation().y);
						inDeMaak = true;
						length0 = 0; 
						length1 = 0;
						
					}
					length0 = fm.stringWidth(woord);
					woord += tt.geefChar();
					length1 = fm.stringWidth(woord);
					if(textRtoL)tt.setBounds(location.x - length1, tt.getLocation().y, length1-length0, tt.getHeight());
					
				}
				else
				{	inDeMaak = false;
					woord = "";
				}
				
			}
			
			
			b = getWidth()-1;
			for(int i=0 ; i<getComponentCount()  ; i++)
			{	b -= getComponent(i).getWidth();
				getComponent(i).setLocation(b,h1-((TekstElement)getComponent(i)).getAsHoogte());
				if(i==caretPos) caretX = b;
			}
		}
	}
	public void zetMaat()
	{	resize();
		if(getParent()instanceof TekstElement)((TekstElement)getParent()).zetMaat();
	}

	public void neemFocus(String richting, TekstElement fe)
	{	requestFocus();
		tekstVak.zetTekstFocus();
		tekstVak.zetActieveRegel(this);
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(getComponent(i)==fe)
			{	if(richting.equals("rechts"))
				{	caretPos = i+1;
					setCaretPosition(((Component)fe).getLocation().x + ((Component)fe).getSize().width -2);
					
				}
				else
				{	caretPos = i;
					setCaretPosition(((Component)fe).getLocation().x);
				}
			}
		}
	}
	
	public void neemFocus(String richting)
	{	requestFocus();
		tekstVak.zetTekstFocus();
		tekstVak.zetActieveRegel(this);
		if(richting.equals("rechts"))
		{	caretPos = 0;
			caretX = 0;
			if(textRtoL)caretX = getWidth()-1;
		}
		if(richting.equals("links"))
		{	caretPos = getComponentCount();
			if(getComponentCount()==0)caretX=0;
			else caretX = getSize().width - 1;
			if(textRtoL)caretX = 0;
		}
	}
	
	public void setCaret(int pos)
    {	caretPos = pos;
    	int x = correctieCursief;
    	if(textRtoL)x = getWidth() - correctieCursief;
        for(int i=0 ; i<pos  ; i++)
		{	x = x + getComponent(i).getSize().width;
		    if(textRtoL)x = x - 2*getComponent(i).getSize().width;
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
    {	int posX = correctieCursief;
        if(textRtoL) posX = getWidth()-correctieCursief;
		int grensX = 0;
		if(textRtoL)grensX = getWidth();
        for(int i=0 ; i<getComponentCount()  ; i++)
		{	grensX = posX + getComponent(i) .getSize().width/2;
		    if(textRtoL)grensX = posX - getComponent(i) .getSize().width;
			if(!textRtoL && x<grensX || textRtoL && x>grensX)
			{	caretPos = i;
				caretX = posX;
				if(textRtoL)caretX = getWidth() - posX;
				break;
			}
			else
			{	caretPos = i+1;
				caretX = posX + getComponent(i) .getSize().width;
				if(textRtoL)caretX = posX - getComponent(i) .getSize().width;;
			}
			posX += getComponent(i) .getSize().width;
			if(textRtoL)posX -= 2*getComponent(i) .getSize().width;
		}
		deSelect();
		tekstVak.setCaretPosition(this,caretPos);
    }
	
	public void setSelection(int x1, int x2)
    {	
		if(textRtoL) {
			int xRes = x1;
			x1 =  getWidth()-x2;
			x2 =  getWidth()-xRes;
		}
		int posX = 0;
		int grensX = 0;
		int comp1=0;
		int comp2=-1;
        for(int i=0 ; i<getComponentCount()  ; i++)
		{	grensX = posX + getComponent(i).getSize().width;
			if(x1<=grensX)
			{	comp1 = i;
				break;
			}
			else
			{	comp1 = i+1;
			}
			posX += getComponent(i) .getSize().width;
		}
		posX = correctieCursief;
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
		
		//System.out.println("");
    }
	
	public void deSelect()
	{	//caretVisible = true;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	((TekstElement)getComponent(i)).setSelected(false);
		}
	}
	
	/*public int geefLengteEersteW()
	{	int lengte = 0;
		for(int i=0 ; i<getComponentCount(); i++)
		{	if(((TekstElement)getComponent(i)).isSpatie())
			{	return lengte;
			}
			else
			{	lengte += getComponent(i).getSize().width;
			}
		}
		return lengte;
	}
	
	public void wrapTerug()
	{	TekstElement[] overflow;
		int aantalLetters = 0;
		for(int i=0 ; i<getComponentCount(); i++)
		{	if(((TekstElement)getComponent(i)).isSpatie())
			{	break;
			}
			else
			{	aantalLetters++;
			}
		}
		overflow = new TekstElement[aantalLetters];
		for(int i=0 ; i<aantalLetters; i++)
		{	overflow[i] = (TekstElement)getComponent(0);
			remove(getComponent(0));
		}
		//zetMaat();
		tekstVak.wrapTerug(this,overflow);		
		
	}*/
	
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
		//if(WiskOpdr.language.equals(new Locale("fa"))) caretX = getWidth();
        //else 
            caretX = 0;
		zetMaat();
	}
	
	//int teller = 0;
	public void insert(TekstElement te)
	{	//if(pos<=getComponentCount())
		{	
		    add((Component)te,caretPos);
			zetMaat();
			if(textRtoL) caretX -= getComponent(caretPos) .getSize().width;
			else caretX += getComponent(caretPos) .getSize().width;
			caretPos++;
			
			//System.out.println("insert (tekstTegel)" + teller);
		}
	}
	
	public void insertZZ(TekstElement te)
	{	//if(pos<=getComponentCount())
		{	
		    add((Component)te,caretPos);
			//zetMaat();
			//if(textRtoL) ;//caretX -= getComponent(caretPos) .getSize().width;
			//else caretX += getComponent(caretPos) .getSize().width;
			caretPos++;
		}
	}
	
	
	
	/*public void wrap()
	{	System.out.println("wrap");
		TekstElement[] overflow;
		int wrapPositie = getComponentCount();
		int spatieIndex1 = 0;
		int spatieIndex2 = 0;
		TekstElement te1 = null;
		TekstElement te2 = null;
		for(int i=getComponentCount()-1 ; i>-1 ; i--)
		{	TekstElement te = (TekstElement)getComponent(i);
			if(((TekstElement)getComponent(i)).isSpatie() && te.getLocation().x > tekstVak.getSize().width)
			{	te1 = te;
				spatieIndex1 = i;
			}
			else if (((TekstElement)getComponent(i)).isSpatie() && te.getLocation().x <= tekstVak.getSize().width)
			{	if(te2==null)
				{	te2 = te;
					spatieIndex2 = i;
				}
			}
		}
		if(te1==null && te2!=null)
		{	wrapPositie = spatieIndex2;
		}
		else if(te1!=null)
		{	wrapPositie = spatieIndex1;
		}
		else return;
		int max = getComponentCount();
		overflow = new TekstElement[max - wrapPositie];
		int aantalOverflow = 0;
		for(int i=wrapPositie ; i<max ; i++)
		{	if(i>wrapPositie)
			{	overflow[aantalOverflow] = (TekstElement)getComponent(wrapPositie);
				aantalOverflow++;
			}
			remove(wrapPositie);
			if(caretPos > getComponentCount())caretPos--;
		}
		caretVisible = true;
		zetMaat();
		//tekstVak.wrap(this,overflow);
	}
	
	public boolean caretAanEind()
	{	return caretPos >= getComponentCount();
	}*/
	
	public void mousePressed(MouseEvent e)
	{	boolean templateEditable = !(tekstVak.getParent()instanceof TekstVakPanel && ((TekstVakPanel)tekstVak.getParent()).templateModeFill) || TekstVakPanel.TEMPLATE_EDITOR;
		if(selectable && templateEditable)
		{	tekstVak.zetTekstFocus();
			startx = e.getX();
			//System.out.println("startx"+startx);
			starty = e.getY();
			
			setCaretPosition(e.getX());
			if(e.getSource()==this)setCaretPosition(e.getX());
			if(e.getSource()instanceof FormuleRegel)setCaretPosition(e.getX());
			if(e.getSource()instanceof TekstInteractiePanelVak)setCaretPosition(e.getX());
		}
	
	}
	
	public void mouseDragged(MouseEvent e)
	{	boolean templateEditable = !(tekstVak.getParent()instanceof TekstVakPanel && ((TekstVakPanel)tekstVak.getParent()).templateModeFill) || TekstVakPanel.TEMPLATE_EDITOR;
		if(selectable && templateEditable)
		{	/*if(e.getX()<0 || e.getX()>getSize().width || e.getY()<0 || e.getY()>getSize().height)
			{	terug = false;
				
				if(getParent().getParent()instanceof TekstRegel)
				{	MouseEvent en = new MouseEvent((TekstRegel)getParent().getParent(),e.getID(),e.getWhen(),e.getModifiers(), e.getX()+getLocation().x+getParent().getLocation().x,e.getY()+getLocation().y+getParent().getLocation().y,1,false);
					
					if(eersteKeer)
					{	((TekstRegel)getParent().getParent()).mousePressed(en);
						eersteKeer=false;
					}
					((TekstRegel)getParent().getParent()).mouseDragged(en);
				}
			}*/
			if(Math.abs(startx-e.getX())<3 && Math.abs(starty-e.getY())<3)return;
			if(e.getY()>getSize().height)
			{	TekstRegel volg = tekstVak.geefVolgendeRegel(this);
				if(volg==null) return;
				setSelection(startx,this.getSize().width);
				MouseEvent en = new MouseEvent(volg,e.getID(),e.getWhen(),e.getModifiers(), 0,0,1,false);
				MouseEvent ed = new MouseEvent(volg,e.getID(),e.getWhen(),e.getModifiers(), e.getX(),e.getY()-getSize().height,1,false);
				
				if(eersteKeer)
				{	volg.mousePressed(en);
					eersteKeer=false;
				}
				volg.mouseDragged(ed);	
			}
// Wim: als ik het goed begrijp....
			else if(e.getY() < 0) {
				TekstRegel vorig = tekstVak.geefVorigeRegel(this);
				if(vorig == null) return;
				setSelection(0,startx);
				MouseEvent en = new MouseEvent(vorig,e.getID(),e.getWhen(),e.getModifiers(), vorig.getWidth(),vorig.getHeight(),1,false);
				MouseEvent ed = new MouseEvent(vorig,e.getID(),e.getWhen(),e.getModifiers(), e.getX(),e.getY()+vorig.getHeight(),1,false);
				
				if(eersteKeer)
				{	vorig.mousePressed(en);
					eersteKeer=false;
				}
				vorig.mouseDragged(ed);	
			}
// tot hier
			else
			{	terug = true;
				tekstVak.setSelected(false);
				tekstVak.requestFocus();
				tekstVak.zetActieveRegel(this);
				if(e.getX() < startx)setSelection(e.getX(),startx);
				else setSelection(startx,e.getX());
                tekstVak.requestFocus();
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
	
	public void mouseReleased(MouseEvent e)
	{	eersteKeer=true;
		startx = 0;
		starty = 0;
	}
		
	public void mouseEntered(MouseEvent e){;}
	public void mouseMoved(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	
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
                caretX = correctieCursief;
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
            {	tekstVak.finish();
			}
			else if ((kt != KeyEvent.VK_ESCAPE) &&
	                (kt != KeyEvent.VK_BACK_SPACE) &&
               		(kc != KeyEvent.VK_ENTER)
                    && (kc != KeyEvent.VK_SHIFT)
                    
                   )
      		{	if(!caretVisible)
				{	deleteSelection();
				}
				add(new TekstTeken((char)kt),caretPos);
				caretX += getComponent(caretPos) .getSize().width;
				caretPos++;
				int nr = caretPos;
            } 
            
            zetMaat();   
            repaint();
		}
	}
	
	/*public void focusGained(FocusEvent e)
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
			//tekstVak.focusLost(this);
			caretVisible = false;
			hasFocus = false;
			repaint();
		}  
	}*/
	
	
	public String toString()
	{	String s = "";
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	s = s + ((TekstElement)getComponent(i)).toString();
		}
		return s;
	}
	
	public void knipper(boolean b)
	{	if(kd!=null)
		{	kd.maakDood();
			kd=null;
		}
		if (b && selectable)
		{	kd = new KnipperDraad();
			kd.start();
			caretVisible = true;
			hasFocus = true;
			repaint();
		}
		else if(selectable)
		{	caretVisible = false;
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


