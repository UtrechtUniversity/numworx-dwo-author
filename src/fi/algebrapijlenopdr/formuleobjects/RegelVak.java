package fi.algebrapijlenopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


public class RegelVak extends FormuleElement
{	
	protected FontMetrics fm;
	protected FontMetrics fm2;
	
	protected  FormuleRegel kind1;
	protected  FormuleRegel kind2;
	protected  boolean selected = false;
	
	public void vulVak(String s)
	{	kind1.removeAll();
		if(kind2!=null)kind2.removeAll();
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
					formuleRegel.add(ov);
					ov.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='a')
				{	AftrekVak av = new AftrekVak(formuleVak);
					formuleRegel.add(av);
					av.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='v')
				{	VermenigvuldigingVak vv = new VermenigvuldigingVak(formuleVak);
					formuleRegel.add(vv);
					vv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='b')
				{	BreukVak bv = new BreukVak(formuleVak);
					formuleRegel.add(bv);
					bv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='p')
				{	PowerVak pv = new PowerVak(formuleVak);
					formuleRegel.add(pv);
					pv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='w')
				{	WortelVak wv = new WortelVak(formuleVak);
					formuleRegel.add(wv);
					wv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='W')
				{	NdeWortelVak nwv = new NdeWortelVak(formuleVak);
					formuleRegel.add(nwv);
					nwv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='m')
				{	MachtVak mv = new MachtVak(formuleVak);
					formuleRegel.add(mv);
					mv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='h')
				{	HaakjesVak hv = new HaakjesVak(formuleVak);
					formuleRegel.add(hv);
					hv.vulVak(s.substring(2,eind));
					s = s.substring(eind);
				}
				else if(ch1=='n')
				{	formuleRegel = kind2;
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
				if(ft3.geefChar()=='e')
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
								   || ft1.geefChar()=='l' && ft2.geefChar()=='o' && ft3.geefChar()=='g')
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
	}
	
	public void setSelectable(boolean b)
	{	kind1.setSelectable(b);
		if(kind2!=null)kind2.setSelectable(b);
	}
	
	public void setSelected(boolean b)
	{	selected = b;
		kind1.setSelected(b);
		if(kind2!=null)kind2.setSelected(b);
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

