package fi.algebrapijlenopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


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
	
	Color bgColor = new Color(255,255,255);
	
	public FormuleRegel(FormuleVak fv)
	{	formuleVak = fv;
		editable = fv.isEditable();
		selectable = fv.isSelectable();
		
		setLayout(null);
		setBackground(getBackground());
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		addFocusListener(this);
		
		setFont(fv.getFont());
		fm = getFontMetrics(getFont());
		setSize(fm.getAscent()/2,fm.getAscent()+fm.getDescent());
		ashoogte = fm.getAscent()/2;
		
	}
	
	public void insert(FormuleElement fe)
	{	add(fe,caretPos);
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
	{	while(s.length()>0)
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
				else if(ch1=='m')
				{	MachtVak mv = new MachtVak(formuleVak);
					insert(mv);
					mv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='h')
				{	HaakjesVak hv = new HaakjesVak(formuleVak);
					hv.vulVak(s.substring(2,eind));
					insert(hv);
					s = s.substring(eind);
				}
				
				
				
			}
			else
			{	insert(new FormuleTeken(s.charAt(0)));
				/**/int nr = getComponentCount();
				FormuleTeken ft1, ft2, ft3;
				Component cm1;
				Component cm2;
				Component cm3;
				
				cm3 = getComponent(nr-1);
				/*ft3 = (FormuleTeken)cm3;
				if(ft3.geefChar()=='e')
				{	ft3.zetFunctieTeken(true);
				}
				if(nr>1)
				{	cm2 = getComponent(nr-2);
					if(cm2 instanceof FormuleTeken)
					{	ft2 = (FormuleTeken)cm2;
						if(ft2.geefChar()=='l' && ft3.geefChar()=='n')
						{	ft2.zetFunctieTeken(true);
							ft3.zetFunctieTeken(true);
						}
						if(nr>2)
						{	cm1 = getComponent(nr-3);
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
				}*/
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
		{	int hoogte = getComponent(i).getSize().height;
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
		}
		else 
		{	setSize(fm.getAscent()/2, fm.getAscent() + fm.getDescent());
			ashoogte = fm.getAscent()/2;
		}
	}
		
	public void paint(Graphics g)
	{	g.setColor(getBackground());
		if(selected)g.setColor(Color.black);
		g.fillRect(0,0,getSize().width-1,getSize().height-1);
		
		if( getComponentCount() ==0)//hasFocus ||
		{	g.setColor(new Color(200,200,200));
			g.drawRect(0,0,getSize().width-1,getSize().height-1);
		}
		super.paint(g);
		//Formuletekens worden direct getekend en niet binnen het eigen component.
		//Bij 'italic' fonts vallen ze namelijk soms buiten het component.
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	if(getComponent(i)instanceof FormuleTeken)
			{	FormuleTeken ft = (FormuleTeken)getComponent(i);
				ft.paint(g,ft.getLocation().x,ft.getLocation().y);
			}
		}
		g.setColor(Color.black);
		if(caretVisible && hasFocus && editable && aan)
		{	g.drawLine(caretX,0,caretX,getSize().height);
		}
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
	
	public void zetMaat()
	{	int b=1;
		int h1=0;
		int h2=0;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	int hoogte = getComponent(i).getSize().height;
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
		add(wv,caretPos);
		
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
		add(wv,caretPos);
		
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
		add(hv,caretPos);
		
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
		add(bv,caretPos);
		
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
		add(mv,caretPos);
		
		caretVisible = false;
		hasFocus = false;
		mv.geefKind1().requestFocus();
		formuleVak.zetActieveRegel(mv.geefKind1());
		zetMaat();
	}
	
	public void zetKwadraatVak()
	{	MachtVak mv = new MachtVak(formuleVak);
		mv.kind1.add(new FormuleTeken('2'));
		insert(mv);
		zetMaat();
		requestFocus();
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
			caretX = 0;
		}
		if(richting.equals("links"))
		{	caretPos = getComponentCount();
			if(getComponentCount()==0)caretX=0;
			else caretX = getSize().width - 1;
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
		repaint();
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
			if(rv instanceof WortelVak || rv instanceof HaakjesVak)
			{	String rest = null;
				rest = rv.kind1.toString();
				int cp = fr.caretPos;
				int cx = fr.caretX;
				fr.neemFocus("links",rv);
				fr.remove(rv);
				fr.insert(rest);
				fr.caretPos = cp;
				fr.caretX = cx;	
			}
			else
			{	fr.neemFocus("links",rv);
				fr.remove(rv);
				fr.zetMaat();
			}
		}
	}
	
	public void deleteSelection()
	{	for(int i=getComponentCount() -1 ; i>-1 ; i--)
		{	if(((FormuleElement)getComponent(i)).isSelected())
			{	//if(getComponentCount() >1)setSize(getSize().width - getComponent(i) .getSize().width, getSize().height);
				//else setSize(fm.getAscent()/2, getSize().height);
				caretPos--;
				caretX -= getComponent(i) .getSize().width;
				remove(i);
			}
		}
		caretVisible = true;
		zetMaat();
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
	
	public void mousePressed(MouseEvent e)
	{	if(selectable)
		{	requestFocus();
			formuleVak.zetActieveRegel(this);
			startx = e.getX();
			starty = e.getY();
			setCaretPosition(e.getX());
			formuleVak.setSelected(false);
			caretVisible = true;
		}		
	}
	
	public void mouseDragged(MouseEvent e)
	{	if(selectable)
		{	if(e.getX()<0 || e.getX()>getSize().width || e.getY()<0 || e.getY()>getSize().height)
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
		
		/*if(terug)
		{	String s = "";
			//s = toString();
			for(int i=0 ; i<getComponentCount()  ; i++)
			{	if(((FormuleElement)getComponent(i)).isSelected())
				{	s = s + ((FormuleElement)getComponent(i)).toString();
				}
			}*/
			//formuleVak.verwerkSelectie();
		//}
	}

		
	public void mouseEntered(MouseEvent e){;}
	public void mouseMoved(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	
	public void keyPressed(KeyEvent e)
	{   kc = e.getKeyCode();
		
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
				else if (kc == KeyEvent.VK_DOWN && getParent() instanceof BreukVak)
                {   ((BreukVak)getParent()).kind2.neemFocus("rechts");
                }
				else if (kc == KeyEvent.VK_UP && getParent() instanceof BreukVak)
                {   ((BreukVak)getParent()).kind1.neemFocus("rechts");
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
						{	//if(getComponentCount() >1)setSize(getSize().width - getComponent(caretPos) .getSize().width, getSize().height);
							//else setSize(fm.getAscent()/2, getSize().height);
							remove(caretPos);
							zetMaat();
						}
						else if(getComponentCount()==0 )
						{	deleteThis();
						}
					}
					else
					{	deleteSelection();
					}
					formuleVak.addState();
					
                } // delete
				else if (kc == KeyEvent.VK_BACK_SPACE)
                {   if(caretVisible)
					{	if(caretPos>0)
						{	caretPos--;
							//if(getComponentCount() >1)setSize(getSize().width - getComponent(caretPos) .getSize().width, getSize().height);
							//else setSize(fm.getAscent()/2, getSize().height);
							caretX -= getComponent(caretPos) .getSize().width;
							remove(caretPos);
							zetMaat();
						}
						else if(getComponentCount()==0 || caretPos==0 && (getParent() instanceof WortelVak || getParent() instanceof HaakjesVak ))
						{	deleteThis();
						}
					}
					else
					{	deleteSelection();
					}
					formuleVak.addState();
                }
			//zetMaat();
			repaint();
         }
	}
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e)
    {	if (editable)
		{   // kc initialized by keyPressed
                int kt = e.getKeyChar();
                if (kt == KeyEvent.VK_ENTER)
                {	formuleVak.finish();
					//formuleVak.requestFocus();
				}
				else if ((kt != KeyEvent.VK_ESCAPE) &&
                    (kt != KeyEvent.VK_BACK_SPACE) &&
                    (kc != KeyEvent.VK_ENTER)
                    && (kc != KeyEvent.VK_SHIFT)
                    && (kc != KeyEvent.VK_DELETE)
                    && !e.isControlDown()
                   )
                {	if(!caretVisible)
					{	deleteSelection();
					}
					add(new FormuleTeken((char)kt),caretPos);
					//if(getComponentCount() >1)setSize(getSize().width + getComponent(caretPos) .getSize().width, getSize().height);
					//else setSize(getComponent(caretPos) .getSize().width+1, getSize().height);
					caretX += getComponent(caretPos) .getSize().width;
					caretPos++;
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
							/**/if(nr>2)
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
			if(!editable)formuleVak.setSelected(false);
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
}

