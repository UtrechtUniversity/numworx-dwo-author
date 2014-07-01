package fi.wiskopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


public class RegelVak extends FormuleElement
{	
	protected FontMetrics fm;
	protected FontMetrics fm2;
	
	protected  FormuleRegel kind1;
	protected  FormuleRegel kind2;
	protected  FormuleRegel kind3;
	protected  FormuleRegel kind4;
	protected  boolean selected = false;
	
	protected int width;
	protected int height;
	protected int h;
	protected int asc;
	protected int desc;
	protected int k1w;
	protected int k2w;
	protected int k3w;
	protected int k4w;
	protected int k1h;
	protected int k2h;
	protected int k3h;
	protected int k4h;
	protected int k1x;
	protected int k2x;
	protected int k3x;
	protected int k4x;
	protected int k1y;
	protected int k2y;
	protected int k3y;
	protected int k4y;
	protected int k1a;
	protected int k2a;
	protected int k3a;
	protected int k4a;
	
	Color fgColor = new Color(0,0,0);
	
	public void zetMaat()
	{	fm = getFontMetrics(getFont());
		if(kind1!=null) k1w = kind1.getSize().width;
		if(kind2!=null) k2w = kind2.getSize().width;
		if(kind3!=null) k3w = kind3.getSize().width;
		if(kind4!=null) k4w = kind4.getSize().width;
		if(kind1!=null) k1h = kind1.getSize().height;
		if(kind2!=null) k2h = kind2.getSize().height;
		if(kind3!=null) k3h = kind3.getSize().height;
		if(kind4!=null) k4h = kind4.getSize().height;
		if(kind1!=null) k1a = kind1.ashoogte;
		if(kind2!=null) k2a = kind2.ashoogte;
		if(kind3!=null) k3a = kind3.ashoogte;
		if(kind4!=null) k4a = kind4.ashoogte;
		asc = fm.getAscent();
		desc = fm.getDescent();
		width = getSize().width;
		height = getSize().height;
	}
	
	public void vulVak(String s)
	{	kind1.removeAll();
		if(kind2!=null)kind2.removeAll();
		if(kind3!=null)kind3.removeAll();
		if(kind4!=null)kind4.removeAll();
		FormuleRegel formuleRegel = kind1;
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
					ov.setFGColor(fgColor);
					formuleRegel.add(ov);
					ov.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='a')
				{	AftrekVak av = new AftrekVak(formuleVak);
					av.setFGColor(fgColor);
					formuleRegel.add(av);
					av.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='v')
				{	VermenigvuldigingVak vv = new VermenigvuldigingVak(formuleVak);
					vv.setFGColor(fgColor);
					formuleRegel.add(vv);
					vv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='b')
				{	BreukVak bv = new BreukVak(formuleVak);
					bv.setFGColor(fgColor);
					formuleRegel.add(bv);
					bv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='p')
				{	PowerVak pv = new PowerVak(formuleVak);
					pv.setFGColor(fgColor);
					formuleRegel.add(pv);
					pv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='w')
				{	WortelVak wv = new WortelVak(formuleVak);
					wv.setFGColor(fgColor);
					formuleRegel.add(wv);
					wv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='W')
				{	NdeWortelVak nwv = new NdeWortelVak(formuleVak);
					nwv.setFGColor(fgColor);
					formuleRegel.add(nwv);
					nwv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='L')
				{	NdeLogVak nlv = new NdeLogVak(formuleVak);
					nlv.setFGColor(fgColor);
					formuleRegel.add(nlv);
					nlv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='d')
				{	DiffVak dv = new DiffVak(formuleVak);
					dv.setFGColor(fgColor);
					formuleRegel.add(dv);
					dv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='D')
				{	DiffPartialVak dv = new DiffPartialVak(formuleVak);
					dv.setFGColor(fgColor);
					formuleRegel.add(dv);
					dv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='P')
				{	PrimitieveVak pv = new PrimitieveVak(formuleVak);
					pv.setFGColor(fgColor);
					formuleRegel.add(pv);
					pv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='T')
				{	LimietVak lv = new LimietVak(formuleVak);
					lv.setFGColor(fgColor);
					formuleRegel.add(lv);
					lv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='S')
                {   SigmaVak sv = new SigmaVak(formuleVak);
                	sv.setFGColor(fgColor);
                    formuleRegel.add(sv);
                    sv.vulVak(s.substring(2,eind));
                    s = s.substring(eind);
                }
				else if(ch1=='i')
				{	IntegraalVak iv = new IntegraalVak(formuleVak);
					iv.setFGColor(fgColor);
					formuleRegel.add(iv);
					iv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='q')
				{	PrvVak iv = new PrvVak(formuleVak);
					iv.setFGColor(fgColor);
					formuleRegel.add(iv);
					iv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='m')
				{	MachtVak mv = new MachtVak(formuleVak);
					mv.setFGColor(fgColor);
					formuleRegel.add(mv);
					mv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='h')
				{	HaakjesVak hv = new HaakjesVak(formuleVak);
					hv.setFGColor(fgColor);
					formuleRegel.add(hv);
					hv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='r')
				{	AbsVak av = new AbsVak(formuleVak);
					av.setFGColor(fgColor);
					formuleRegel.add(av);
					av.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='c')
                {   ConjugVak cv = new ConjugVak(formuleVak);
                	cv.setFGColor(fgColor);
                    formuleRegel.add(cv);
                    cv.vulVak(s.substring(2,eind));
                    s = s.substring(eind);
                }
				else if(ch1=='y')
				{	BinVak iv = new BinVak(formuleVak);
					iv.setFGColor(fgColor);
					formuleRegel.add(iv);
					iv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
                else if(ch1=='s')
                {   SubscriptVak sv = new SubscriptVak(formuleVak);
                	sv.setFGColor(fgColor);
                    formuleRegel.add(sv);
                    sv.vulVak(s.substring(2,eind));
                    s = s.substring(eind);
                }
				else if(ch1=='n')
				{	formuleRegel = kind2;
					s = s.substring(2);
				}
				else if(ch1=='k')
				{	formuleRegel = kind3;
					s = s.substring(2);
				}
				else if(ch1=='l')
				{	formuleRegel = kind4;
					s = s.substring(2);
				}
				
			}
			else
			{	formuleRegel.add(new FormuleTeken(s.charAt(0)));
				/**/int nr = formuleRegel.getComponentCount();
				FormuleTeken ft1, ft2, ft3;
				Component cm1;
				Component cm2;
				Component cm3;
				
				cm3 = formuleRegel.getComponent(nr-1);
				ft3 = (FormuleTeken)cm3;
				if(ft3.geefChar()=='e' || ft3.geefChar()=='d' && FormuleTeken.isDiffOperator())
				{	ft3.zetFunctieTeken(true);
				}
				if(nr>1)
				{	cm2 = formuleRegel.getComponent(nr-2);
					if(cm2 instanceof FormuleTeken)
					{	ft2 = (FormuleTeken)cm2;
						if(ft2.geefChar()=='l' && ft3.geefChar()=='n')
						{	ft2.zetFunctieTeken(true);
							ft3.zetFunctieTeken(true);
						}
						if(nr>2)
						{	cm1 = formuleRegel.getComponent(nr-3);
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
				s = s.substring(1);
			}
		}
		kind1.zetMaat();
		if(kind2!=null)kind2.zetMaat();
		if(kind3!=null)kind3.zetMaat();
		if(kind4!=null)kind4.zetMaat();
		
		//kind1.zetOpEind();
	}
	
	public void setFGColor(Color c){
		fgColor = c;
		kind1.setFGColor(c);
		if(kind2!=null)kind2.setFGColor(c);
		if(kind3!=null)kind3.setFGColor(c);
		if(kind4!=null)kind4.setFGColor(c);
	}
	
	public FormuleRegel geefKind1()
	{	return kind1;
	}
	
	public FormuleRegel geefKind2()
	{	return kind2;
	}
	
	public void setEditable(boolean b)
	{	kind1.setEditable(b);
		if(kind2!=null)kind2.setEditable(b);
		if(kind3!=null)kind3.setEditable(b);
		if(kind4!=null)kind4.setEditable(b);
	}
	
	public void setSelectable(boolean b)
	{	kind1.setSelectable(b);
		if(kind2!=null)kind2.setSelectable(b);
		if(kind3!=null)kind3.setSelectable(b);
		if(kind4!=null)kind4.setSelectable(b);
	}
	
	public void setSelected(boolean b)
	{	selected = b;
		kind1.setSelected(b);
		if(kind2!=null)kind2.setSelected(b);
		if(kind3!=null)kind3.setSelected(b);
		if(kind4!=null)kind4.setSelected(b);
		repaint();
	}
	
	public boolean isSelected()
	{	return selected;
	}
	
	
	public void neemFocus(String richting,FormuleElement fe)
	{	if(getParent() instanceof FormuleElement)
		{	((FormuleElement)getParent()).neemFocus(richting,this);
		}
	}
	
	public void neemFocus(String richting)
	{	kind1.neemFocus(richting);
	}
	
}

