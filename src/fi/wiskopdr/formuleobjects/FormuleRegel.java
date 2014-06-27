package fi.wiskopdr.formuleobjects;

import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.event.*;
import java.util.Collections;
import java.util.Vector;

import fi.wiskopdr.tekstobjects.TekstRegel;
import fi.wiskopdr.AntwoordFormuleVak;
import fi.wiskopdr.AntwoordVergelijkingVak;
import fi.wiskopdr.SimpelAntwoordFormuleVak;
import fi.wiskopdr.SimpelAntwoordVergelijkingVak;
import fi.wiskopdr.WiskOpdr;


public class FormuleRegel extends FormuleElement implements MouseListener, MouseMotionListener,KeyListener, FocusListener
{	
	private FontMetrics fm;
	private boolean caretVisible = false;
	private KnipperDraad kd;
	private boolean selectable = true;
	private boolean editable = true;
	private boolean hasFocus = true;
	private boolean selected = false;
	private boolean aan;
	private int caretX = 0;
	private int caretPos = 0;
	private int kc;
	
	private int startx = 0;
	private int starty = 0;
	
	private boolean eersteKeer=true;
	private boolean terug=true;
	int correctieCursief = 0;
	private boolean stippels;
	
	Color bgColor = new Color(255,255,255);
	Color fgColor = new Color(0,0,0);
	
	public FormuleRegel(FormuleVak fv)
	{	formuleVak = fv;
		editable = fv.isEditable();
		selectable = fv.isSelectable();
		
		setLayout(null);	
		setBackground(fv.getBackground());
		setOpaque(false);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		addFocusListener(this);
		
		setFont(fv.getFont());
		fm = getFontMetrics(getFont());
		setSize(fm.getAscent()/2,fm.getAscent()+fm.getDescent());
		ashoogte = fm.getAscent()/2;
		
		setFocusTraversalKeys(
                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
	}
	
	public void removeAll()
	{ 	caretX = correctieCursief;
		caretPos = 0;
		super.removeAll();
	}
	
	public void insert(FormuleElement fe)
	{	add(fe,caretPos);
		WiskOpdr.setLaunchDataChanged();
		if(fe instanceof RegelVak)((RegelVak)fe).setFGColor(fgColor);
		caretX += getComponent(caretPos) .getSize().width;
		caretPos++;
		
				
		/**/int nr = caretPos;
					FormuleTeken ft1, ft2, ft3;
					Component cm1;
					Component cm2;
					Component cm3;
					
					cm3 = this.getComponent(nr-1);
					if(cm3 instanceof FormuleTeken)
					{	ft3 = (FormuleTeken)cm3;
						if(ft3.geefChar()=='e')
						{	ft3.zetFunctieTeken(true);
						}
						if(nr>1)
						{	cm2 = this.getComponent(nr-2);
							if(cm2 instanceof FormuleTeken)
							{	ft2 = (FormuleTeken)cm2;
								if(ft2.geefChar()=='l' && ft3.geefChar()=='n')
								{	ft2.zetFunctieTeken(true);
									ft3.zetFunctieTeken(true);
								}
								if(ft2.geefChar()=='p' && ft3.geefChar()=='i')
								{	caretPos--;
									caretX -= getComponent(caretPos) .getSize().width;
									remove(cm3);
									caretPos--;
									caretX -= getComponent(caretPos) .getSize().width;
									remove(cm2);
									
									FormuleTeken ft = new FormuleTeken('\u03C0');
									//ft.zetFunctieTeken(true);
									add(ft,caretPos);
									caretX += getComponent(caretPos) .getSize().width;
									caretPos++;
								}
								if(ft2.geefChar()=='>' && ft3.geefChar()=='=')
								{	caretPos--;
									caretX -= getComponent(caretPos) .getSize().width;
									remove(cm3);
									caretPos--;
									caretX -= getComponent(caretPos) .getSize().width;
									remove(cm2);
									
									FormuleTeken ft = new FormuleTeken('\u2265');
									//ft.zetFunctieTeken(true);
									add(ft,caretPos);
									caretX += getComponent(caretPos) .getSize().width;
									caretPos++;
								}
								if(ft2.geefChar()=='<' && ft3.geefChar()=='=')
								{	caretPos--;
									caretX -= getComponent(caretPos) .getSize().width;
									remove(cm3);
									caretPos--;
									caretX -= getComponent(caretPos) .getSize().width;
									remove(cm2);
									
									FormuleTeken ft = new FormuleTeken('\u2264');
									//ft.zetFunctieTeken(true);
									add(ft,caretPos);
									caretX += getComponent(caretPos) .getSize().width;
									caretPos++;
								}
								if(nr>2)
								{	cm1 = this.getComponent(nr-3);
									if(cm1 instanceof FormuleTeken && cm2 instanceof FormuleTeken)
									{	ft1 = (FormuleTeken)cm1;
										if(ft1.geefChar()=='s' && ft2.geefChar()=='i' && ft3.geefChar()=='n'
										   || ft1.geefChar()=='c' && ft2.geefChar()=='o' && ft3.geefChar()=='s'
										   || ft1.geefChar()=='t' && ft2.geefChar()=='a' && ft3.geefChar()=='n'
										   || ft1.geefChar()=='l' && ft2.geefChar()=='o' && ft3.geefChar()=='g')
										{	ft1.zetFunctieTeken(true);
											ft2.zetFunctieTeken(true);
											ft3.zetFunctieTeken(true);
										}
									}
								}
							}
						}
					}
	}
	
	public void insert(String s)
	{	if(s==null)return;
		while(s.length()>0)
		{	char ch0 = s.charAt(0);
			if(ch0=='@')
			{	break;
			}
			else if(ch0=='$')
			{	int niv = 1;
				int eind = 0;
				String sz = s.substring(2);
				while(niv>0 )
				{	int eindB = sz.indexOf("$");
					int eindE = sz.indexOf("@");
					if(eindB < eindE && eindB!=-1)
					{	eind = eindB;
						niv++;
					}
					else
					{	eind = eindE;
						niv--;
					}
					sz = sz.substring(eind+1);
				}
				eind = s.length()-sz.length();				
				char ch1 = s.charAt(1);
				if(ch1=='o')
				{	OptelVak ov = new OptelVak(formuleVak);
					ov.vulVak(s.substring(2,eind));
					insert(ov);
					s = s.substring(eind);
				}
				else if(ch1=='a')
				{	AftrekVak av = new AftrekVak(formuleVak);
					av.vulVak(s.substring(2,eind));
					insert(av);
					s = s.substring(eind);
				}
				else if(ch1=='v')
				{	VermenigvuldigingVak vv = new VermenigvuldigingVak(formuleVak);
					vv.vulVak(s.substring(2,eind));
					insert(vv);
					s = s.substring(eind);
				}
				else if(ch1=='b')
				{	BreukVak bv = new BreukVak(formuleVak);
					bv.vulVak(s.substring(2,eind));
					insert(bv);
					s = s.substring(eind);
				}
				else if(ch1=='p')
				{	PowerVak pv = new PowerVak(formuleVak);
					pv.vulVak(s.substring(2,eind));
					insert(pv);
					s = s.substring(eind);
				}
				else if(ch1=='w')
				{	WortelVak wv = new WortelVak(formuleVak);
					wv.vulVak(s.substring(2,eind));
					insert(wv);
					s = s.substring(eind);
				}
				else if(ch1=='W')
				{	NdeWortelVak nwv = new NdeWortelVak(formuleVak);
					nwv.vulVak(s.substring(2,eind));
					insert(nwv);
					s = s.substring(eind);
				}
				else if(ch1=='L')
				{	NdeLogVak nlv = new NdeLogVak(formuleVak);
					nlv.vulVak(s.substring(2,eind));
					insert(nlv);
					s = s.substring(eind);
				}
				else if(ch1=='d')
				{	DiffVak dv = new DiffVak(formuleVak);
					dv.vulVak(s.substring(2,eind));
					insert(dv);
					s = s.substring(eind);
				}
				else if(ch1=='P')
				{	PrimitieveVak pv = new PrimitieveVak(formuleVak);
					pv.vulVak(s.substring(2,eind));
					insert(pv);
					s = s.substring(eind);
				}
				else if(ch1=='T')
				{	LimietVak lv = new LimietVak(formuleVak);
					lv.vulVak(s.substring(2,eind));
					insert(lv);
					s = s.substring(eind);
				}
				else if(ch1=='S')
                {   SigmaVak sv = new SigmaVak(formuleVak);
                	sv.vulVak(s.substring(2,eind));
                	insert(sv);
                    s = s.substring(eind);
                }
				else if(ch1=='i')
				{	IntegraalVak nlv = new IntegraalVak(formuleVak);
					nlv.vulVak(s.substring(2,eind));
					insert(nlv);
					s = s.substring(eind);
				}
				else if(ch1=='q')
				{	PrvVak nlv = new PrvVak(formuleVak);
					nlv.vulVak(s.substring(2,eind));
					insert(nlv);
					s = s.substring(eind);
				}
				else if(ch1=='m')
				{	MachtVak mv = new MachtVak(formuleVak);
					insert(mv);
					mv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
                else if(ch1=='s')
                {   SubscriptVak sv = new SubscriptVak(formuleVak);
                    insert(sv);
                    sv.vulVak(s.substring(2,eind));
                    s = s.substring(eind);
                }
				else if(ch1=='h')
				{	HaakjesVak hv = new HaakjesVak(formuleVak);
					hv.vulVak(s.substring(2,eind));
					insert(hv);
					s = s.substring(eind);
				}
				else if(ch1=='r')
				{	AbsVak av = new AbsVak(formuleVak);
					av.setFGColor(fgColor);
					av.vulVak(s.substring(2,eind));
					insert(av);
					s = s.substring(eind);
				}
				else if(ch1=='c')
                {   ConjugVak av = new ConjugVak(formuleVak);
                    av.vulVak(s.substring(2,eind));
                    insert(av);
                    s = s.substring(eind);
                }
				else if(ch1=='y')
				{	BinVak iv = new BinVak(formuleVak);
					iv.vulVak(s.substring(2,eind));
					insert(iv);
					s = s.substring(eind);
				}
				
				
			}
			else
			{	insert(new FormuleTeken(s.charAt(0)));
				int nr = getComponentCount();
				FormuleTeken ft1, ft2, ft3;
				Component cm1;
				Component cm2;
				Component cm3;
				
				cm3 = getComponent(nr-1);
				if(cm3 instanceof FormuleTeken)
				{	ft3 = (FormuleTeken)cm3;
					if(ft3.geefChar()=='e')
					{	ft3.zetFunctieTeken(true);
					}
					if(nr>1)
					{	cm2 = this.getComponent(nr-2);
						if(cm2 instanceof FormuleTeken)
						{	ft2 = (FormuleTeken)cm2;
							if(ft2.geefChar()=='l' && ft3.geefChar()=='n')
							{	ft2.zetFunctieTeken(true);
								ft3.zetFunctieTeken(true);
							}
							if(ft2.geefChar()=='p' && ft3.geefChar()=='i')
							{	caretPos--;
								caretX -= getComponent(caretPos) .getSize().width;
								remove(cm3);
								caretPos--;
							caretX -= getComponent(caretPos) .getSize().width;
								remove(cm2);
								
								FormuleTeken ft = new FormuleTeken('\u03C0');
								//ft.zetFunctieTeken(true);
								add(ft,caretPos);
								caretX += getComponent(caretPos) .getSize().width;
								caretPos++;
							}
							if(ft2.geefChar()=='>' && ft3.geefChar()=='=')
							{	caretPos--;
								caretX -= getComponent(caretPos) .getSize().width;
								remove(cm3);
								caretPos--;
								caretX -= getComponent(caretPos) .getSize().width;
								remove(cm2);
								
								FormuleTeken ft = new FormuleTeken('\u2265');
								//ft.zetFunctieTeken(true);
								add(ft,caretPos);
								caretX += getComponent(caretPos) .getSize().width;
								caretPos++;
							}
							if(ft2.geefChar()=='<' && ft3.geefChar()=='=')
							{	caretPos--;
								caretX -= getComponent(caretPos) .getSize().width;
								remove(cm3);
								caretPos--;
								caretX -= getComponent(caretPos) .getSize().width;
								remove(cm2);
								
								FormuleTeken ft = new FormuleTeken('\u2264');
								//ft.zetFunctieTeken(true);
								add(ft,caretPos);
								caretX += getComponent(caretPos) .getSize().width;
								caretPos++;
							}
							if(nr>2)
							{	cm1 = this.getComponent(nr-3);
								if(cm1 instanceof FormuleTeken && cm2 instanceof FormuleTeken)
								{	ft1 = (FormuleTeken)cm1;
									if(ft1.geefChar()=='s' && ft2.geefChar()=='i' && ft3.geefChar()=='n'
									   || ft1.geefChar()=='c' && ft2.geefChar()=='o' && ft3.geefChar()=='s'
									   || ft1.geefChar()=='t' && ft2.geefChar()=='a' && ft3.geefChar()=='n'
									   || ft1.geefChar()=='l' && ft2.geefChar()=='o' && ft3.geefChar()=='g'
									   || ft1.geefChar()=='a' && ft2.geefChar()=='r' && ft3.geefChar()=='c')
									{	ft1.zetFunctieTeken(true);
										ft2.zetFunctieTeken(true);
										ft3.zetFunctieTeken(true);
									}
								}
							}
						}
					}
				}
				s = s.substring(1);
			}
		}
	
		zetMaat();
	}
	
	public void setFont(Font f)
	{	super.setFont(f);
		fm = getFontMetrics(getFont());
		int b=1;
		int h1=0;
		int h2=0;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	((FormuleElement)getComponent(i)).setFont(f);
			int hoogte = getComponent(i).getSize().height;
			int ash = ((FormuleElement)getComponent(i)).ashoogte;
			if(ash>h1)h1=ash;
			if(hoogte-ash>h2)h2=hoogte-ash;
		}
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	getComponent(i).setLocation(b,h1-((FormuleElement)getComponent(i)).ashoogte);
			b += getComponent(i).getSize().width;
		}
		if(getComponentCount()>0)
		{	setSize(b,h1+h2);
			ashoogte = h1;
            zetMaat();
		}
		else 
		{	setSize(fm.getAscent()/2, fm.getAscent() + fm.getDescent());
			ashoogte = fm.getAscent()/2;
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
	
	public void setFGColor(Color c){
		fgColor = c;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(getComponent(i) instanceof RegelVak)((RegelVak)getComponent(i)). setFGColor(c);
		}
	}
	public void paint(Graphics gr)
	{	
		/*Graphics g;
	    if(getFont().getSize()>16)//WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("GR"))
	    {     g = (Graphics2D)gr;
	         ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    }
	    else 
	    g=gr;*/
		
		Graphics g ;
	    if(!WiskOpdr.formTimes)
	    {   g = (Graphics2D)gr;
	    	if(System.getProperty("java.specification.version").equals("1.6") || System.getProperty("java.specification.version").equals("1.7"))
			{	//((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		        ((Graphics2D)g).setRenderingHint(KEY_TEXT_LCD_CONTRAST, new Integer(100));
		        //((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		    }
	    	else
	    	{	((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    	}
	    }
	    else if(getFont().getSize()>16)//WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("GR"))
	    {     g = (Graphics2D)gr;
	         ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    }
	    else 
	    g=gr;
	    
	    g.setColor(getBackground());
		if(selected)g.setColor(Color.black);
		//g.fillRect(0,0,getSize().width-1,getSize().height-1);
		
		if( !stippels && getComponentCount() ==0 && formuleVak.hasBorder() || getComponentCount() ==0 && getParent()!=formuleVak)//hasFocus ||
		{	g.setColor(new Color(200,200,200));
			g.drawRect(0,0,getSize().width-1,getSize().height-1);
			
		}
		else if(stippels && getComponentCount() ==0 && !formuleVak.hasBorder())//hasFocus || 
		{
			g.setColor(new Color(0,0,0));	
			g.drawLine(0,getSize().height-fm.getDescent()-1,0,getSize().height-fm.getDescent()-1);
			g.drawLine((getSize().width-1)/2, getSize().height-fm.getDescent()-1,(getSize().width-1)/2,getSize().height-fm.getDescent()-1);
			g.drawLine((getSize().width-1)/2*2, getSize().height-fm.getDescent()-1,(getSize().width-1)/2*2,getSize().height-fm.getDescent()-1);
		}
		else super.paint(g);
		
		//paintBorder(g);
		//paintChildren(g);
		
		
		//Formuletekens worden direct getekend en niet binnen het eigen component.
		//Bij 'italic' fonts vallen ze namelijk soms buiten het component.
		
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(getComponent(i)instanceof FormuleTeken)
			{	
				//((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
				//((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
				//((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, new Integer(100));
				FormuleTeken ft = (FormuleTeken)getComponent(i);
				ft.paint(g,ft.getLocation().x,ft.getLocation().y, fgColor);
				//((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			}
		}
		g.setColor(Color.black);
		if(caretVisible && hasFocus && editable && aan)
		{	g.drawLine(caretX,0,caretX,getSize().height);
		}
		//paintChildren(g);
	}
	
	public void setEditable(boolean b)
	{	editable = b;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	((FormuleElement)getComponent(i)).setEditable(b);
		}	
	}
	
	public void setSelectable(boolean b)
	{	selectable = b;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	((FormuleElement)getComponent(i)).setSelectable(b);
		}
	}
	
	public void setSelected(boolean b)
	{	//if(selected!=b)
		{	if(b)caretVisible = false;
			selected = b;
			for(int i=0 ; i<getComponentCount()  ; i++)
			{	((FormuleElement)getComponent(i)).setSelected(b);
			}
			repaint();
		}
	}
	
	public boolean isSelected()
	{	return selected;
	}
	
	public void zetStippels(boolean b)
	{	stippels = b;
	}
	
	public void zetMaat()
	{	int b=0;
		int h1=0;
		int h2=0;
		
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	int hoogte = getComponent(i).getSize().height;
			int ash = ((FormuleElement)getComponent(i)).ashoogte;
			if(ash>h1)h1=ash;
			if(hoogte-ash>h2)h2=hoogte-ash;
		}
		if(getComponentCount()>0)
		{	FormuleElement feStart = (FormuleElement)getComponent(getComponentCount()-1);
			FormuleElement feEnd = (FormuleElement)getComponent(0);
			boolean bStart = feStart instanceof FormuleTeken && Character.isLetter(((FormuleTeken)feStart).geefChar());
			boolean bEnd = feEnd instanceof FormuleTeken && Character.isLetter(((FormuleTeken)feEnd).geefChar());
			if(bStart || bEnd)
			{	correctieCursief = 2;
				b+=correctieCursief;
			}
			else correctieCursief = 0;
		}
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	getComponent(i).setLocation(b,h1-((FormuleElement)getComponent(i)).ashoogte);
			b += getComponent(i).getSize().width;
		}
		if(getComponentCount()>0)
		{	//FormuleElement fe = (FormuleElement)getComponent(getComponentCount()-1);
			//if(fe instanceof FormuleTeken && Character.isLetter(((FormuleTeken)fe).geefChar()))correctieCursief = 2;
			setSize(b+Math.max(1,correctieCursief),h1+h2);
			ashoogte = h1;
		}
		else 
		{	setSize(fm.getAscent()/2, fm.getAscent() + fm.getDescent());
			ashoogte = fm.getAscent()/2;
		}
		
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
	}

	public void zetWortelVak()
	{	
		FormuleElement[] selectieRij = new FormuleElement[100];
		int aantalElementen = 0;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(((FormuleElement)getComponent(i)).isSelected())
			{	selectieRij[aantalElementen] = ((FormuleElement)getComponent(i));
				aantalElementen++;
			}
		}
		if(!caretVisible)
		{	deleteSelection();
		}
		WortelVak wv = new WortelVak(formuleVak);
		wv.setFGColor(fgColor);
		add(wv,caretPos);
		formuleVak.changed();
		int x=0;
		for(int i=0 ; i<aantalElementen  ; i++)
		{	wv.geefKind1().insert(selectieRij[i]);
		}
		wv.geefKind1().zetMaat();
		
		caretVisible = false;
		hasFocus = false;
		wv.geefKind1().requestFocus();
		formuleVak.zetActieveRegel(wv.geefKind1());
		zetMaat();
		
	}
	
	public void zetNdeWortelVak()
	{	FormuleElement[] selectieRij = new FormuleElement[100];
		int aantalElementen = 0;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(((FormuleElement)getComponent(i)).isSelected())
			{	selectieRij[aantalElementen] = ((FormuleElement)getComponent(i));
				aantalElementen++;
			}
		}
		if(!caretVisible)
		{	deleteSelection();
		}
		NdeWortelVak wv = new NdeWortelVak(formuleVak);
		wv.setFGColor(fgColor);
		add(wv,caretPos);
		formuleVak.changed();
		int x=0;
		for(int i=0 ; i<aantalElementen  ; i++)
		{	wv.geefKind1().insert(selectieRij[i]);
		}
		wv.geefKind1().zetMaat();
		
		caretVisible = false;
		hasFocus = false;
		wv.geefKind1().requestFocus();
		formuleVak.zetActieveRegel(wv.geefKind1());
		zetMaat();
		
	}
	
	public void zetNdeLogVak()
	{	FormuleElement[] selectieRij = new FormuleElement[100];
		int aantalElementen = 0;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(((FormuleElement)getComponent(i)).isSelected())
			{	selectieRij[aantalElementen] = ((FormuleElement)getComponent(i));
				aantalElementen++;
			}
		}
		if(!caretVisible)
		{	deleteSelection();
		}
		NdeLogVak wv = new NdeLogVak(formuleVak);
		wv.setFGColor(fgColor);
		add(wv,caretPos);
		formuleVak.changed();
		int x=0;
		for(int i=0 ; i<aantalElementen  ; i++)
		{	wv.geefKind1().insert(selectieRij[i]);
		}
		wv.geefKind1().zetMaat();
		
		caretVisible = false;
		hasFocus = false;
		wv.geefKind1().requestFocus();
		formuleVak.zetActieveRegel(wv.geefKind1());
		zetMaat();
		
	}
	
	public void zetDiffVak()
	{	FormuleElement[] selectieRij = new FormuleElement[100];
		int aantalElementen = 0;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(((FormuleElement)getComponent(i)).isSelected())
			{	selectieRij[aantalElementen] = ((FormuleElement)getComponent(i));
				aantalElementen++;
			}
		}
		if(!caretVisible)
		{	deleteSelection();
		}
		DiffVak wv = new DiffVak(formuleVak);
		wv.setFGColor(fgColor);
		add(wv,caretPos);
		formuleVak.changed();
		int x=0;
		for(int i=0 ; i<aantalElementen  ; i++)
		{	wv.geefKind1().insert(selectieRij[i]);
		}
		wv.geefKind1().zetMaat();
		
		caretVisible = false;
		hasFocus = false;
		wv.geefKind1().requestFocus();
		formuleVak.zetActieveRegel(wv.geefKind1());
		zetMaat();
		
	}
	
	public void zetDiffPartialVak()
    {   FormuleElement[] selectieRij = new FormuleElement[100];
        int aantalElementen = 0;
        for(int i=0 ; i<getComponentCount()  ; i++)
        {   if(((FormuleElement)getComponent(i)).isSelected())
            {   selectieRij[aantalElementen] = ((FormuleElement)getComponent(i));
                aantalElementen++;
            }
        }
        if(!caretVisible)
        {   deleteSelection();
        }
        DiffPartialVak wv = new DiffPartialVak(formuleVak);
        wv.setFGColor(fgColor);
        add(wv,caretPos);
        formuleVak.changed();
        int x=0;
        for(int i=0 ; i<aantalElementen  ; i++)
        {   wv.geefKind1().insert(selectieRij[i]);
        }
        wv.geefKind1().zetMaat();
        
        caretVisible = false;
        hasFocus = false;
        wv.geefKind1().requestFocus();
        formuleVak.zetActieveRegel(wv.geefKind1());
        zetMaat();
        
    }
	
	public void zetLimietVak(int richting)
	{	FormuleElement[] selectieRij = new FormuleElement[100];
		int aantalElementen = 0;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(((FormuleElement)getComponent(i)).isSelected())
			{	selectieRij[aantalElementen] = ((FormuleElement)getComponent(i));
				aantalElementen++;
			}
		}
		if(!caretVisible)
		{	deleteSelection();
		}
		LimietVak wv = new LimietVak(formuleVak);
		wv.setFGColor(fgColor);
		wv.zetRichting(richting);
		add(wv,caretPos);
		formuleVak.changed();
		int x=0;
		for(int i=0 ; i<aantalElementen  ; i++)
		{	wv.geefKind1().insert(selectieRij[i]);
		}
		wv.geefKind1().zetMaat();
		
		caretVisible = false;
		hasFocus = false;
		wv.geefKind1().requestFocus();
		formuleVak.zetActieveRegel(wv.geefKind1());
		zetMaat();
		
	}
	
	public void zetIntegraalVak()
	{	FormuleElement[] selectieRij = new FormuleElement[100];
		int aantalElementen = 0;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(((FormuleElement)getComponent(i)).isSelected())
			{	selectieRij[aantalElementen] = ((FormuleElement)getComponent(i));
				aantalElementen++;
			}
		}
		if(!caretVisible)
		{	deleteSelection();
		}
		IntegraalVak wv = new IntegraalVak(formuleVak);
		wv.setFGColor(fgColor);
		add(wv,caretPos);
		formuleVak.changed();
		int x=0;
		for(int i=0 ; i<aantalElementen  ; i++)
		{	wv.geefKind1().insert(selectieRij[i]);
		}
		wv.geefKind1().zetMaat();
		
		caretVisible = false;
		hasFocus = false;
		wv.geefKind1().requestFocus();
		formuleVak.zetActieveRegel(wv.geefKind1());
		zetMaat();
		
	}
	
	public void zetPrvVak()
	{	FormuleElement[] selectieRij = new FormuleElement[100];
		int aantalElementen = 0;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(((FormuleElement)getComponent(i)).isSelected())
			{	selectieRij[aantalElementen] = ((FormuleElement)getComponent(i));
				aantalElementen++;
			}
		}
		if(!caretVisible)
		{	deleteSelection();
		}
		PrvVak wv = new PrvVak(formuleVak);
		wv.setFGColor(fgColor);
		add(wv,caretPos);
		formuleVak.changed();
		int x=0;
		for(int i=0 ; i<aantalElementen  ; i++)
		{	wv.geefKind1().insert(selectieRij[i]);
		}
		wv.geefKind1().zetMaat();
		
		caretVisible = false;
		hasFocus = false;
		wv.geefKind1().requestFocus();
		formuleVak.zetActieveRegel(wv.geefKind1());
		zetMaat();
		
	}
	
	public void zetAbsVak()
	{	FormuleElement[] selectieRij = new FormuleElement[100];
		int aantalElementen = 0;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(((FormuleElement)getComponent(i)).isSelected())
			{	selectieRij[aantalElementen] = ((FormuleElement)getComponent(i));
				aantalElementen++;
			}
		}
		if(!caretVisible)
		{	deleteSelection();
		}
		AbsVak wv = new AbsVak(formuleVak);
		wv.setFGColor(fgColor);
		add(wv,caretPos);
		formuleVak.changed();
		int x=0;
		for(int i=0 ; i<aantalElementen  ; i++)
		{	wv.geefKind1().insert(selectieRij[i]);
		}
		wv.geefKind1().zetMaat();
		
		caretVisible = false;
		hasFocus = false;
		wv.geefKind1().requestFocus();
		formuleVak.zetActieveRegel(wv.geefKind1());
		zetMaat();
		
	}
	
	public void zetConjugVak()
    {   FormuleElement[] selectieRij = new FormuleElement[100];
        int aantalElementen = 0;
        for(int i=0 ; i<getComponentCount()  ; i++)
        {   if(((FormuleElement)getComponent(i)).isSelected())
            {   selectieRij[aantalElementen] = ((FormuleElement)getComponent(i));
                aantalElementen++;
            }
        }
        if(!caretVisible)
        {   deleteSelection();
        }
        ConjugVak wv = new ConjugVak(formuleVak);
        wv.setFGColor(fgColor);
        add(wv,caretPos);
        formuleVak.changed();
        int x=0;
        for(int i=0 ; i<aantalElementen  ; i++)
        {   wv.geefKind1().insert(selectieRij[i]);
        }
        wv.geefKind1().zetMaat();
        
        caretVisible = false;
        hasFocus = false;
        wv.geefKind1().requestFocus();
        formuleVak.zetActieveRegel(wv.geefKind1());
        zetMaat();
        
    }
	
	public void zetHaakjesVak()
	{	FormuleElement[] selectieRij = new FormuleElement[100];
		int aantalElementen = 0;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(((FormuleElement)getComponent(i)).isSelected())
			{	selectieRij[aantalElementen] = ((FormuleElement)getComponent(i));
				aantalElementen++;
			}
		}
		if(!caretVisible)
		{	deleteSelection();
		}
		HaakjesVak hv = new HaakjesVak(formuleVak);
		hv.setFGColor(fgColor);
		//LimietVak hv = new LimietVak(formuleVak);
		
		add(hv,caretPos);
		formuleVak.changed();
		int x=0;
		for(int i=0 ; i<aantalElementen  ; i++)
		{	hv.geefKind1().insert(selectieRij[i]);
		}
		hv.geefKind1().zetMaat();
		
		caretVisible = false;
		hasFocus = false;
		hv.geefKind1().requestFocus();
		formuleVak.zetActieveRegel(hv.geefKind1());
		zetMaat();
		
	}
	
	public void zetPrimitieveVak()
	{	FormuleElement[] selectieRij = new FormuleElement[100];
		int aantalElementen = 0;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(((FormuleElement)getComponent(i)).isSelected())
			{	selectieRij[aantalElementen] = ((FormuleElement)getComponent(i));
				aantalElementen++;
			}
		}
		if(!caretVisible)
		{	deleteSelection();
		}
		PrimitieveVak hv = new PrimitieveVak(formuleVak);
		hv.setFGColor(fgColor);
		//LimietVak hv = new LimietVak(formuleVak);
		
		add(hv,caretPos);
		formuleVak.changed();
		int x=0;
		for(int i=0 ; i<aantalElementen  ; i++)
		{	hv.geefKind1().insert(selectieRij[i]);
		}
		hv.geefKind1().zetMaat();
		
		caretVisible = false;
		hasFocus = false;
		hv.geefKind1().requestFocus();
		formuleVak.zetActieveRegel(hv.geefKind1());
		zetMaat();
		
	}
	
	public void zetSigmaVak()
    {   FormuleElement[] selectieRij = new FormuleElement[100];
        int aantalElementen = 0;
        for(int i=0 ; i<getComponentCount()  ; i++)
        {   if(((FormuleElement)getComponent(i)).isSelected())
            {   selectieRij[aantalElementen] = ((FormuleElement)getComponent(i));
                aantalElementen++;
            }
        }
        if(!caretVisible)
        {   deleteSelection();
        }
        SigmaVak hv = new SigmaVak(formuleVak);
        hv.setFGColor(fgColor);
        //LimietVak hv = new LimietVak(formuleVak);
        
        add(hv,caretPos);
        formuleVak.changed();
        int x=0;
        for(int i=0 ; i<aantalElementen  ; i++)
        {   hv.geefKind1().insert(selectieRij[i]);
        }
        hv.geefKind1().zetMaat();
        
        caretVisible = false;
        hasFocus = false;
        hv.geefKind1().requestFocus();
        formuleVak.zetActieveRegel(hv.geefKind1());
        zetMaat();
        
    }
	
	public void zetBreukVak()
	{	FormuleElement[] selectieRij = new FormuleElement[100];
		int aantalElementen = 0;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(((FormuleElement)getComponent(i)).isSelected())
			{	selectieRij[aantalElementen] = ((FormuleElement)getComponent(i));
				aantalElementen++;
			}
		}
		if(!caretVisible)
		{	deleteSelection();
		}
		BreukVak bv = new BreukVak(formuleVak);
		bv.setFGColor(fgColor);
		add(bv,caretPos);
		formuleVak.changed();
		int x=0;
		for(int i=0 ; i<aantalElementen  ; i++)
		{	bv.geefKind1().insert(selectieRij[i]);
		}
		bv.geefKind1().zetMaat();
		
		caretVisible = false;
		hasFocus = false;
		if(aantalElementen==0)
		{	bv.geefKind1().requestFocus();
			formuleVak.zetActieveRegel(bv.geefKind1());
		}
		else
		{	bv.geefKind2().requestFocus();
			formuleVak.zetActieveRegel(bv.geefKind2());
		}
		zetMaat();
		
	}
	
	public void zetMachtVak()
	{	MachtVak mv = new MachtVak(formuleVak); 
		mv.setFGColor(fgColor);
		add(mv,caretPos);
		formuleVak.changed();
		caretVisible = false;
		hasFocus = false;
		mv.geefKind1().requestFocus();
		formuleVak.zetActieveRegel(mv.geefKind1());
        mv.zetMaat();
        
	}
    
    public void zetSubscriptVak()
    {   SubscriptVak sv = new SubscriptVak(formuleVak);
    	sv.setFGColor(fgColor);
        add(sv,caretPos);
        formuleVak.changed();
        caretVisible = false;
        hasFocus = false;
        sv.geefKind1().requestFocus();
        formuleVak.zetActieveRegel(sv.geefKind1());
        sv.zetMaat();
        
    }
	
	public void zetKwadraatVak()
	{	MachtVak mv = new MachtVak(formuleVak);
		mv.setFGColor(fgColor);
		mv.kind1.add(new FormuleTeken('2'));
		insert(mv);
		formuleVak.changed();
		mv.zetMaat();
		
		requestFocus();
	}
	
	public void zetBinVak()
	{	BinVak bv = new BinVak(formuleVak);
		bv.setFGColor(fgColor);
		insert(bv);
		formuleVak.changed();
		caretVisible = false;
        hasFocus = false;
        bv.geefKind1().requestFocus();
        formuleVak.zetActieveRegel(bv.geefKind1());
        bv.zetMaat();
		
		
	}
	
	public int geefVoorgangerHoogte(FormuleElement fe)
	{	for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(i>0 && getComponent(i)==fe)
			{	return getComponent(i-1).getSize().height;
			}
		}
		return fm.getAscent()+fm.getDescent();
	}
	
	public int geefVoorgangerAsHoogte(FormuleElement fe)
	{	for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(i>0 && getComponent(i)==fe)
			{	return ((FormuleElement)getComponent(i-1)).ashoogte;
			}
		}
		return fm.getAscent()/2;
	}
	
	public void neemFocus(String richting, FormuleElement fe)
	{	requestFocus();
		formuleVak.zetActieveRegel(this);
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(getComponent(i)==fe)
			{	if(richting.equals("rechts"))
				{	caretPos = i+1;
					caretX = fe.getLocation().x + fe.getSize().width -1;
				}
				else
				{	caretPos = i;
					caretX = fe.getLocation().x-1; //wat x!!!!????
				}
			}
		}
	}
	
	public void neemFocus(String richting)
	{	requestFocus();
		formuleVak.zetActieveRegel(this);
		if(richting.equals("rechts"))
		{	caretPos = 0;
			caretX = correctieCursief;
		}
		if(richting.equals("links"))
		{	caretPos = getComponentCount();
			if(getComponentCount()==0)caretX=correctieCursief;
			else caretX = getSize().width - 1;
		}
	}
		
	public void setCaretPosition(int x)
    {	int posX = correctieCursief;
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
		repaint();
    }
	
	public void setSelection(int x1, int x2)
    {	int posX = correctieCursief;
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
			{	((FormuleElement)getComponent(i)).setSelected(true);
				caretVisible = false;
			}
			else ((FormuleElement)getComponent(i)).setSelected(false);
		}
		repaint();
    }
	
	public void deSelect()
	{	//caretVisible = true;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	((FormuleElement)getComponent(i)).setSelected(false);
		}
	}
	
	public void deleteThis()
	{	if(getParent().getParent()instanceof FormuleRegel)
		{	FormuleRegel fr = ((FormuleRegel)getParent().getParent());
			RegelVak rv = (RegelVak)getParent();
			if(rv instanceof BreukVak)
			{	String rest = null;
				if(this==rv.kind1)
				{	rest = rv.kind2.toString();
				}
				else 
				{	rest = rv.kind1.toString();
				}
				fr.neemFocus("links",rv);
				fr.remove(getParent());
				fr.insert(rest);	
			}
			else if(rv instanceof WortelVak || rv instanceof HaakjesVak)
			{	String rest = null;
				rest = rv.kind1.toString();
				int cp = fr.caretPos;
				int cx = fr.caretX;
				fr.neemFocus("links",rv);
				fr.remove(rv);
				fr.insert(rest);
				fr.caretPos = cp;
				fr.caretX = cx;	
				fr.neemFocus("links");
			}
			else if(rv.kind1==this)
			{	fr.neemFocus("links",rv);
				fr.remove(rv);
				fr.zetMaat();
				fr.neemFocus("links");
			}
		}
	}
	
	public boolean deleteSelection()
	{	boolean selectionExisted = false;
		for(int i=getComponentCount() -1 ; i>-1 ; i--)
		{	if(((FormuleElement)getComponent(i)).isSelected())
			{	selectionExisted = true;
				if(caretPos==i+1)
				{	caretPos--;
					caretX -= getComponent(i) .getSize().width;
				}
				remove(i);
			}
		}
		caretVisible = true;
		zetMaat();
		return selectionExisted;
	}
	
	public void zetOpEind()
	{	if(selectable)
		{	requestFocus();
			formuleVak.zetActieveRegel(this);
			caretPos = getComponentCount() ;
			if(getComponentCount() >0)caretX = getSize().width-1;
			else caretX = 0;
			deSelect();
			formuleVak.setSelected(false);
			caretVisible = true;
			repaint();
		}	
	}
	
	public void zetOpBegin()
	{	if(selectable)
		{	requestFocus();
			formuleVak.zetActieveRegel(this);
			caretPos = 0 ;
			caretX = correctieCursief;
			deSelect();
			formuleVak.setSelected(false);
			caretVisible = true;
			repaint();
		}	
	}
	private boolean waiting;
	private ShowPopupThread showPopupThread;
	
	public void mousePressed(MouseEvent e)
	{	
		if(e.getModifiers()== e.BUTTON3_MASK || e.isControlDown())
		{	formuleVak.showPopup(e.getX(),e.getY());
			return;
		}
		if(selectable)
		{	requestFocus();
			//formuleVak.zetActieveRegel(this);
			startx = e.getX();
			starty = e.getY();
			setCaretPosition(e.getX());
			//formuleVak.setSelected(false);
			caretVisible = true;
		}		
	}
	
	public void mouseDragged(MouseEvent e)
	{	if(selectable)
		{	if(Math.abs(startx-e.getX())>=3 || Math.abs(starty-e.getY())>=3)
			{	waiting = false;
				//return;
			}
			if(Math.abs(startx-e.getX())<3 && Math.abs(starty-e.getY())<3)
			{	
				return;
			}
			//waiting = false;
			if(e.getX()<0 || e.getX()>getSize().width || e.getY()<0 || e.getY()>getSize().height)
			{	terug = false;
				
				if(getParent().getParent()instanceof FormuleRegel)
				{	MouseEvent en = new MouseEvent((FormuleRegel)getParent().getParent(),e.getID(),e.getWhen(),e.getModifiers(), e.getX()+getLocation().x+getParent().getLocation().x,e.getY()+getLocation().y+getParent().getLocation().y,1,false);
					
					if(eersteKeer)
					{	((FormuleRegel)getParent().getParent()).mousePressed(en);
						eersteKeer=false;
					}
					((FormuleRegel)getParent().getParent()).mouseDragged(en);
					//((FormuleElement)getParent().getParent()).requestFocus();
					//formuleVak.zetActieveRegel((FormuleRegel)getParent().getParent());
					//((FormuleRegel)getParent().getParent()).setSelection(((FormuleElement)getParent()).getLocation().x, ((FormuleElement)getParent()).getLocation().x+1);
				}
				if(getParent().getParent().getParent()instanceof TekstRegel)
				{	MouseEvent en = new MouseEvent((TekstRegel)getParent().getParent().getParent(),e.getID(),e.getWhen(),e.getModifiers(), e.getX()+getLocation().x+getParent().getLocation().x+getParent().getParent().getLocation().x,e.getY()+getLocation().y+getParent().getLocation().y,1,false);
					
					if(eersteKeer)
					{	((TekstRegel)getParent().getParent().getParent()).mousePressed(en);
						eersteKeer=false;
					}
					((TekstRegel)getParent().getParent().getParent()).mouseDragged(en);
					//((FormuleElement)getParent().getParent()).requestFocus();
					//formuleVak.zetActieveRegel((FormuleRegel)getParent().getParent());
					//((FormuleRegel)getParent().getParent()).setSelection(((FormuleElement)getParent()).getLocation().x, ((FormuleElement)getParent()).getLocation().x+1);
				}
				
			}
			else
			{	terug = true;
				formuleVak.setSelected(false);
				requestFocus();
				formuleVak.zetActieveRegel(this);
				if(e.getX() < startx)setSelection(e.getX(),startx);
				else setSelection(startx,e.getX());
				
				//formuleVak.zetActieveRegel(this);
			}
		}
	}
		
	public void mouseClicked(MouseEvent e)
	{	if(selectable)
		{	int clickCount = e.getClickCount();
			if(clickCount>1)
			{	for(int i=0 ; i<getComponentCount()  ; i++)
				{	((FormuleElement)getComponent(i)).setSelected(true);
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
		waiting = false;
		
		if(selectable &&  Math.abs(startx-e.getX())<3 && Math.abs(starty-e.getY())<3)
		{	
			formuleVak.setSelected(false);
			formuleVak.zetActieveRegel(this);
			
		}
		
		/*if(terug)
		{	String s = "";
			//s = toString();
			for(int i=0 ; i<getComponentCount()  ; i++)
			{	if(((FormuleElement)getComponent(i)).isSelected())
				{	s = s + ((FormuleElement)getComponent(i)).toString();
				}
			}*/
			formuleVak.verwerkSelectie();
		//}
	}

		
	public void mouseEntered(MouseEvent e){;}
	public void mouseMoved(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	
	
	public void delete()
	{	boolean selectionExited = deleteSelection();
		if(!selectionExited)
		{	if(caretPos<getComponentCount() )
			{	remove(caretPos);
				zetMaat();
			}
			else if(getComponentCount()==0 )
			{	deleteThis();
			}
		}
		formuleVak.addState();
	}
	
	public void backspace()
	{	boolean selectionExited = deleteSelection();
		if(!selectionExited)
		{	if(caretPos>0)
			{	caretPos--;
				caretX -= getComponent(caretPos) .getSize().width;
				remove(caretPos);
				zetMaat();
			}
			else if(getComponentCount()==0 || caretPos==0 
					&& (getParent() instanceof WortelVak 
							|| getParent() instanceof HaakjesVak 
							|| getParent() instanceof NdeWortelVak
							|| getParent() instanceof NdeLogVak))
			{	deleteThis();
			}
		}
		formuleVak.addState();
	}
	
	public void keyPressed(KeyEvent e)
	{   kc = e.getKeyCode();
	
		if (kc == KeyEvent.VK_TAB)
	    {	verplaatsFocus();
	    	return;
	    }
	
		if (e.isControlDown() && kc == KeyEvent.VK_C)
        {	copySelection();
        }
                
		if (editable)
            {   if (kc == KeyEvent.VK_CONTROL)
                {	return;
                }
            	if (e.isControlDown() && kc == KeyEvent.VK_Z)
                {	formuleVak.undo();
                	return;
                }
                if (e.isControlDown() && kc == KeyEvent.VK_Y)
                {	formuleVak.redo();
                	return;
                }
                
                if(WiskOpdr.fToets)
                {    if (e.isAltDown() && kc == KeyEvent.VK_F1)
	                {   zetDiffVak();
	                }
	                else if (e.isAltDown() && kc == KeyEvent.VK_F2)
	                {   zetLimietVak(0);
	                }
	                
	                else if (kc == KeyEvent.VK_F1)
	                {   zetWortelVak();
	                }
	                else if (kc == KeyEvent.VK_F2)
	                {   zetMachtVak();
	                }
	                else if (kc == KeyEvent.VK_F3)
	                {   zetKwadraatVak();
	                }
	                else if (kc == KeyEvent.VK_F4)
	                {   zetBreukVak();
	                }
	                else if (kc == KeyEvent.VK_F5)
	                {   zetHaakjesVak();
	                }
	                else if (kc == KeyEvent.VK_F6)
	                {   zetNdeWortelVak();
	                }
	                else if (kc == KeyEvent.VK_F7)
	                {   zetIntegraalVak();
	                }
	                else if (kc == KeyEvent.VK_F8)
	                {   zetPrvVak();
	                }
	                else if (kc == KeyEvent.VK_F9)
	                {   zetNdeLogVak();
	                }
	                else if (kc == KeyEvent.VK_F10)
	                {   zetAbsVak();
	                }
	                else if (kc == KeyEvent.VK_F11)
	                {   zetSubscriptVak();
	                }
	                else if (kc == KeyEvent.VK_F12)
	                {   zetBinVak();
	                }
                }
               
                
                
            	//if (kc == KeyEvent.VK_ENTER)
                //{	formuleVak.finish();
				//	formuleVak.requestFocus();
				//}
				if (e.isControlDown() && kc == KeyEvent.VK_V)
                {	deleteSelection();
                	insert(formuleVak.clipboard);
                	formuleVak.addState();
         
                }
                else if (e.isControlDown() && kc == KeyEvent.VK_X)
                {	copySelection();
                	deleteSelection();
                	formuleVak.addState();
                	
                }
                else if (e.isShiftDown() && kc == KeyEvent.VK_LEFT)
                {   if (caretPos > 0)
                    {   caretPos--;
                        caretX -= getComponent(caretPos) .getSize().width;
                        boolean b = ((FormuleElement)getComponent(caretPos)).isSelected();
						((FormuleElement)getComponent(caretPos)).setSelected(!b);
                    }
					else if(!(caretPos > 0))
					{	if(getParent() instanceof FormuleElement)
						{	((FormuleElement)getParent()).neemFocus("links",this);
							boolean b = ((FormuleElement)getParent()).isSelected();
							((FormuleElement)getParent()).setSelected(!b);
						}
					}
					caretVisible = false;
                }
				else if (kc == KeyEvent.VK_LEFT)
                {   if (caretPos > 0 && getComponent(caretPos-1)instanceof FormuleTeken)
                    {   caretPos--;
                        caretX -= getComponent(caretPos) .getSize().width;
						deSelect();
                    }
					else if(!(caretPos > 0))
					{	if(getParent() instanceof FormuleElement)
						{	((FormuleElement)getParent()).neemFocus("links",this);
						}
					}
					else
					{	((FormuleElement)getComponent(caretPos-1)).neemFocus("links");
					}
                }
				else if (e.isShiftDown() && kc == KeyEvent.VK_RIGHT)
                {   if (caretPos < getComponentCount())
                    {   caretPos++;
                        caretX += getComponent(caretPos-1).getSize().width;
                        boolean b = ((FormuleElement)getComponent(caretPos-1)).isSelected();
                        ((FormuleElement)getComponent(caretPos-1)).setSelected(!b);
					}
					else if (!(caretPos < getComponentCount()))
					{	if(getParent() instanceof FormuleElement)
						{	((FormuleElement)getParent()).neemFocus("rechts",this);
							boolean b = ((FormuleElement)getParent()).isSelected();
							((FormuleElement)getParent()).setSelected(!b);
						}
					}
                	caretVisible = false;
                }
                else if (kc == KeyEvent.VK_RIGHT)
                {   if (caretPos < getComponentCount()&& getComponent(caretPos)instanceof FormuleTeken)
                    {   caretPos++;
                        caretX += getComponent(caretPos-1).getSize().width;
						deSelect();
					}
					else if (!(caretPos < getComponentCount()))
					{	if(getParent() instanceof FormuleElement)
						{	((FormuleElement)getParent()).neemFocus("rechts",this);
						}
					}
					else
					{	((FormuleElement)getComponent(caretPos)).neemFocus("rechts");
					}
                }
                else if (kc == KeyEvent.VK_DOWN && getParent() instanceof BinVak)
                {   ((BinVak)getParent()).kind2.neemFocus("rechts");
                }
				else if (kc == KeyEvent.VK_UP && getParent() instanceof BinVak)
                {   ((BinVak)getParent()).kind1.neemFocus("rechts");
                }
				else if (kc == KeyEvent.VK_DOWN && getParent() instanceof BreukVak)
                {   ((BreukVak)getParent()).kind2.neemFocus("rechts");
                }
				else if (kc == KeyEvent.VK_UP && getParent() instanceof BreukVak)
                {   ((BreukVak)getParent()).kind1.neemFocus("rechts");
                }
                else if (kc == KeyEvent.VK_DOWN && getParent() instanceof NdeLogVak)
                {   ((NdeLogVak)getParent()).kind1.neemFocus("rechts");
                }
                else if (kc == KeyEvent.VK_UP && getParent() instanceof NdeLogVak)
                {   ((NdeLogVak)getParent()).kind2.neemFocus("rechts");
                }
                else if (kc == KeyEvent.VK_DOWN && getParent() instanceof NdeWortelVak)
                {   ((NdeWortelVak)getParent()).kind1.neemFocus("rechts");
                }
                else if (kc == KeyEvent.VK_UP && getParent() instanceof NdeWortelVak)
                {   ((NdeWortelVak)getParent()).kind2.neemFocus("rechts");
                }
                else if (kc == KeyEvent.VK_DOWN && getParent() instanceof IntegraalVak)
                {   if(((IntegraalVak)getParent()).kind1.hasFocus())((IntegraalVak)getParent()).kind2.neemFocus("rechts");
                    if(((IntegraalVak)getParent()).kind3.hasFocus())((IntegraalVak)getParent()).kind1.neemFocus("rechts");
                }
                else if (kc == KeyEvent.VK_UP && getParent() instanceof IntegraalVak)
                {   if(((IntegraalVak)getParent()).kind1.hasFocus())((IntegraalVak)getParent()).kind3.neemFocus("rechts");
                    if(((IntegraalVak)getParent()).kind2.hasFocus())((IntegraalVak)getParent()).kind1.neemFocus("rechts");
                }
                else if (kc == KeyEvent.VK_DOWN && getParent() instanceof PrvVak)
                {   if(((PrvVak)getParent()).kind1.hasFocus())((PrvVak)getParent()).kind2.neemFocus("rechts");
                    if(((PrvVak)getParent()).kind3.hasFocus())((PrvVak)getParent()).kind1.neemFocus("rechts");
                }
                else if (kc == KeyEvent.VK_UP && getParent() instanceof PrvVak)
                {   if(((PrvVak)getParent()).kind1.hasFocus())((PrvVak)getParent()).kind3.neemFocus("rechts");
                    if(((PrvVak)getParent()).kind2.hasFocus())((PrvVak)getParent()).kind1.neemFocus("rechts");
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
                {	delete();
				} 
				else if (kc == KeyEvent.VK_BACK_SPACE)
                {   backspace();
                }
			//zetMaat();
			repaint();
         }
	}
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e)
    {	int pc = e.getKeyChar();
    	if (pc == KeyEvent.VK_TAB) 
    	{	return;
		}
    	if (editable)
		{   // kc initialized by keyPressed
                int kt = e.getKeyChar();
                formuleVak.changed();
                if (kt == KeyEvent.VK_ENTER)
                {	formuleVak.finish();
					//formuleVak.requestFocus();
				}
                
				else if ((kc != KeyEvent.VK_ESCAPE) &&
                    (kc != KeyEvent.VK_BACK_SPACE) &&
                    (kc != KeyEvent.VK_ENTER)
                    && (kc != KeyEvent.VK_SHIFT)
                    && (kc != KeyEvent.VK_DELETE)
                    && !e.isControlDown()
                   )
                {	
				    
				    if(!caretVisible)
					{	deleteSelection();
					}
				    if(e.isAltDown())
				    {
				        if(kc == KeyEvent.VK_A) kt = '\u03b1';
				        else if (kc == KeyEvent.VK_B) kt = '\u03b2';
				        else if (kc == KeyEvent.VK_G) kt = '\u03b3';
				        else if (kc == KeyEvent.VK_D) kt = '\u03b4';
				        else if (kc == KeyEvent.VK_E) kt = '\u03b5';
				        else if (kc == KeyEvent.VK_Z) kt = '\u03b6';
				        else if (kc == KeyEvent.VK_H) kt = '\u03b7';
				        else if (kc == KeyEvent.VK_Q) kt = '\u03b8';
				        else if (kc == KeyEvent.VK_I) kt = '\u03b9';
				        else if (kc == KeyEvent.VK_K) kt = '\u03ba';
				        else if (kc == KeyEvent.VK_L) kt = '\u03bb';
				        else if (kc == KeyEvent.VK_M) kt = '\u03bc';
				        else if (kc == KeyEvent.VK_N) kt = '\u03bd';
				        else if (kc == KeyEvent.VK_X) kt = '\u03be';
				        else if (kc == KeyEvent.VK_O) kt = '\u03bf';
				        else if (kc == KeyEvent.VK_P) kt = '\u03c0';
				        else if (kc == KeyEvent.VK_R) kt = '\u03c1';
				        else if (kc == KeyEvent.VK_R) kt = '\u03c2';
				        else if (kc == KeyEvent.VK_S) kt = '\u03c3';
				        else if (kc == KeyEvent.VK_T) kt = '\u03c4';
				        else if (kc == KeyEvent.VK_U) kt = '\u03c5';
				        else if (kc == KeyEvent.VK_V || kc == KeyEvent.VK_F) kt = '\u03c6';
				        else if (kc == KeyEvent.VK_C) kt = '\u03c7';
				        else if (kc == KeyEvent.VK_Y) kt = '\u03c8';
				        else if (kc == KeyEvent.VK_W) kt = '\u03c9';
				        
				    }
				    //{"\u03b1"   ,"\u03b2"   ,"\u03b3"  ,"\u03b4"  ,"\u03b5"  ,"\u03b6"  ,"\u03b7"  ,"\u03b8"  ,"\u03b9"  ,"\u03ba"  ,"\u03bb"  ,"\u03bc"    ,"\u03bd"   },
		           // {"\u03be"  ,"\u03bf"  ,"\u03c0"  ,"\u03c1"  ,"\u03c2"   ,"\u03c3"   ,"\u03c4"  ,"\u03c5"  ,"\u03c6" ,"\u03c7","\u03c8"  , "\u03c9","\u221e"         },
		           // {"\u2264"  ,"\u2265"  ,"\u00b1"  ,"\u2260"  ,"\u00f7"   ,"\u00d7"   ,"\u00b0"  ,"\u2030"  ,"\u2202" ,"\u2206","\u2207"  , "\u2227","\u2228"         },
		           // {"123"  ,"\u2200"  ,"\u2203"  ,"\u2204"  ,"\u2205"   ,"\u00ac"      ,"\u2229"  ,"\u222a"  ,"\u2208" ,   "\u2209",   "\u2282"    , "\u2283","\u2284"         },
		            
				    if((char)kt=='$' || (char)kt=='@')return;
				    add(new FormuleTeken((char)kt),caretPos);
				    WiskOpdr.setLaunchDataChanged();
					//if(getComponentCount() >1)setSize(getSize().width + getComponent(caretPos) .getSize().width, getSize().height);
					//else setSize(getComponent(caretPos) .getSize().width+1, getSize().height);
					
				    caretX += getComponent(caretPos) .getSize().width;
					caretPos++;
					setCaretPosition(caretX);
					
					formuleVak.addState();
					
					int nr = caretPos;
					FormuleTeken ft1, ft2, ft3;
					Component cm1;
					Component cm2;
					Component cm3;
					
					cm3 = this.getComponent(nr-1);
					ft3 = (FormuleTeken)cm3;
					if(ft3.geefChar()=='e')
					{	ft3.zetFunctieTeken(true);
					}
					if(nr>1)
					{	cm2 = this.getComponent(nr-2);
						if(cm2 instanceof FormuleTeken)
						{	ft2 = (FormuleTeken)cm2;
							if(ft2.geefChar()=='l' && ft3.geefChar()=='n')
							{	ft2.zetFunctieTeken(true);
								ft3.zetFunctieTeken(true);
							}
							if(ft2.geefChar()=='p' && ft3.geefChar()=='i')
							{	caretPos--;
								caretX -= getComponent(caretPos) .getSize().width;
								remove(cm3);
								caretPos--;
								caretX -= getComponent(caretPos) .getSize().width;
								remove(cm2);
								FormuleTeken ft = new FormuleTeken('\u03C0');
								//ft.zetFunctieTeken(true);
								add(ft,caretPos);
								caretX += getComponent(caretPos) .getSize().width;
								caretPos++;
							}
							if(ft2.geefChar()=='>' && ft3.geefChar()=='=')
							{	caretPos--;
								caretX -= getComponent(caretPos) .getSize().width;
								remove(cm3);
								caretPos--;
								caretX -= getComponent(caretPos) .getSize().width;
								remove(cm2);
								
								FormuleTeken ft = new FormuleTeken('\u2265');
								//ft.zetFunctieTeken(true);
								add(ft,caretPos);
								caretX += getComponent(caretPos) .getSize().width;
								caretPos++;
							}
							if(ft2.geefChar()=='<' && ft3.geefChar()=='=')
							{	caretPos--;
								caretX -= getComponent(caretPos) .getSize().width;
								remove(cm3);
								caretPos--;
								caretX -= getComponent(caretPos) .getSize().width;
								remove(cm2);
								
								FormuleTeken ft = new FormuleTeken('\u2264');
								//ft.zetFunctieTeken(true);
								add(ft,caretPos);
								caretX += getComponent(caretPos) .getSize().width;
								caretPos++;
							}
							if(ft2.geefChar()=='^' && Character.isDigit(ft3.geefChar()))
							{	caretPos--;
								caretX -= getComponent(caretPos) .getSize().width;
								remove(cm3);
								caretPos--;
								caretX -= getComponent(caretPos) .getSize().width;
								remove(cm2);
								
								MachtVak mv = new MachtVak(formuleVak);
								insert(mv);
								mv.vulVak("" + ft3.geefChar());
								
								//caretX += getComponent(caretPos) .getSize().width;
								
							}
							/**/if(nr>2)
							{	cm1 = this.getComponent(nr-3);
								if(cm1 instanceof FormuleTeken && cm2 instanceof FormuleTeken)
								{	ft1 = (FormuleTeken)cm1;
									if(ft1.geefChar()=='s' && ft2.geefChar()=='i' && ft3.geefChar()=='n'
									   || ft1.geefChar()=='c' && ft2.geefChar()=='o' && ft3.geefChar()=='s'
									   || ft1.geefChar()=='t' && ft2.geefChar()=='a' && ft3.geefChar()=='n'
									   || ft1.geefChar()=='l' && ft2.geefChar()=='o' && ft3.geefChar()=='g'
									   || ft1.geefChar()=='a' && ft2.geefChar()=='r' && ft3.geefChar()=='c')
									{	ft1.zetFunctieTeken(true);
										ft2.zetFunctieTeken(true);
										ft3.zetFunctieTeken(true);
									}
								}
							}
						}
					}/**/
					
                } // all typed keys except Esc, Backspace, Enter
            zetMaat();   
            repaint();
            }
	}
	
	public void copySelection()
	{	String s = "";
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	FormuleElement fe = ((FormuleElement)getComponent(i));
			if(fe.isSelected())s = s + fe.toString();
		}
		FormuleVak.clipboard = s;
	}
	
	public void verplaatsFocus()
	{
		if(formuleVak.getParent() instanceof SimpelAntwoordFormuleVak)((SimpelAntwoordFormuleVak)(formuleVak.getParent())).verplaatsFocus();
		else if(formuleVak.getParent() instanceof SimpelAntwoordVergelijkingVak)((SimpelAntwoordVergelijkingVak)(formuleVak.getParent())).verplaatsFocus();
		else if(formuleVak.getParent().getParent().getParent().getParent().getParent() instanceof AntwoordFormuleVak)((AntwoordFormuleVak)(formuleVak.getParent().getParent().getParent().getParent().getParent())).verplaatsFocus();
		else if(formuleVak.getParent().getParent().getParent().getParent().getParent() instanceof AntwoordVergelijkingVak)((AntwoordVergelijkingVak)(formuleVak.getParent().getParent().getParent().getParent().getParent())).verplaatsFocus();
		
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
			//if(!editable)formuleVak.setSelected(false);
			caretVisible = false;
			hasFocus = false;
			repaint();
		}   
	}
	
	public String toString()
	{	String s = "";
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	s = s + ((FormuleElement)getComponent(i)).toString();
		}
		return s;
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
	class ShowPopupThread extends Thread 
	{	
		final MouseEvent ee;
		boolean dood = false;
		
		public ShowPopupThread(MouseEvent e)
		{	ee = e;
		}
		
		public void run()
		{	try
    		{   sleep(400);
			}
    		catch(InterruptedException e)    
			{ }
    		if(waiting && !dood )
    		{
    			formuleVak.showPopup(ee.getX(), ee.getY());
    		}
    		waiting = false;
		}
		public void maakDood()
		{	dood = true;
		}
	}
}

