package fi.algebrapijlenopdr;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import fi.algebrapijlenopdr.formuleobjects.*;
import fi.algebrapijlenopdr.expressies.*;


public class AntwoordFormuleVak extends FormuleEditor 
{
	private static Image GOEDKRUL,FOUTKRUIS, HALFKRUL;
	private static int GOED = 1;
	private static int FOUT = 0;
	private static int HALF = 2;
	private static int GEEN = 3;
	
	private static int HERLEIDING_GEEN = 0;
	private static int HERLEIDING_VEELTERM_ZH = 1;
	private static int HERLEIDING_1_MACHT = 2;
	private static int HERLEIDING_MACHT_Z_NEG_BREUK_EXP = 3;
	private static int HERLEIDING_1_BREUK = 4;
	
	private boolean	herleiding;
	private boolean	exact;
	private int soortHerleiding = 0;
	private boolean ingevuld = false;
	
	private boolean isGelijkwaardig = false;
	private boolean isHerleid = false;
	private boolean isExact = false;
	private int puntenGelijkwaardig = 10;
	private int puntenHerleiding = 0;
	private int puntenExact = 0;
	
	private int score;
	private boolean correct;
	
	private ImageComponent goedIC, foutIC, halfIC, huidigIC;
	private Expressie juisteAntwoord, juisteAntwoordExtra;
	
	
	public AntwoordFormuleVak()
	{	super(false);
		formuleVak.addActionListener(this);
		
		goedIC = new ImageComponent(GOEDKRUL);
		goedIC.setLocation(0,0);
		goedIC.setVisible(false);
		add(goedIC);
		
		foutIC = new ImageComponent(FOUTKRUIS);
		foutIC.setLocation(0,0);
		foutIC.setVisible(false);
		add(foutIC);
		
		halfIC = new ImageComponent(HALFKRUL);
		halfIC.setLocation(0,0);
		halfIC.setVisible(false);
		add(halfIC);
	}
	
	public void setScoreData(boolean herleiding, boolean exact, int soortHerleiding,int puntenGelijkwaardig, int puntenHerleiding, int puntenExact)
	{	this.herleiding = herleiding;
		this.exact = exact;
		this.soortHerleiding = soortHerleiding;
		this.puntenGelijkwaardig = puntenGelijkwaardig;
		this.puntenHerleiding = puntenHerleiding;
		this.puntenExact = puntenExact;
	}
	
	public Expressie geefExpressie()
	{	return formuleVak.geefExpressie();
	}
	
	public void vulVak(String s)
	{	formuleVak.vulVak(s);
	}
	
	public String toString()
	{	return formuleVak.toString();
	}
	
	private void zetGoedFout(int uitslag)
	{	if(huidigIC!=null)huidigIC.setVisible(false);
		if(uitslag==GEEN)return;
		
		if(uitslag==GOED)huidigIC = goedIC;
		else if(uitslag==FOUT)huidigIC = foutIC;
		else if(uitslag==HALF)huidigIC = halfIC;
		else if(uitslag==GEEN)huidigIC = halfIC;
		huidigIC.setLocation(getSize().width-75,getSize().height-65);//getSize().width-30,getSize().height-30);
		huidigIC.setVisible(true);
	}
	
	public static void zetPlaatjes(Image gk, Image fk, Image hk)
	{	GOEDKRUL = gk;
		FOUTKRUIS = fk;
		HALFKRUL = hk;
	}
	
	public void zetJuisteAntwoord(String s)
	{	
		int ofPlaats = s.indexOf("of");
		if(ofPlaats>-1)
		{	String s1 = s.substring(2,ofPlaats);
			String s2 = s.substring(ofPlaats+2,s.length()-1);
			int n = s1.indexOf("=");
			if(n>-1)
			{	String s11 = s1.substring(0,n);
				String s12 = s1.substring(n+1);
				FormuleParser p = new FormuleParser();
				Expressie e11 = p.parse(p.schoon(p.formuleString("$f" + s11 + "@")));
				Expressie e12 = p.parse(p.schoon(p.formuleString("$f" + s12 + "@")));
				
				if(e11!=null && e12!=null)
				{	 juisteAntwoord = new Aftrekking(e11,e12);
				}
				else return;
			}
			else 
			{	FormuleParser p = new FormuleParser();
				juisteAntwoord = p.parse(p.schoon(p.formuleString("$f" + s1 + "@")));
			}
			n = s2.indexOf("=");
			if(n>-1)
			{	String s21 = s2.substring(0,n);
				String s22 = s2.substring(n+1);
				FormuleParser p = new FormuleParser();
				Expressie e21 = p.parse(p.schoon(p.formuleString("$f" + s21 + "@")));
				Expressie e22 = p.parse(p.schoon(p.formuleString("$f" + s22 + "@")));
				if(e21!=null && e22!=null)
				{	juisteAntwoordExtra = new Aftrekking(e21,e22);
				}
				else return;
			}
			else 
			{	FormuleParser p = new FormuleParser();
				juisteAntwoordExtra = p.parse(p.schoon(p.formuleString("$f" + s2 + "@")));
			}
		}
		else
		{	
			int n = s.indexOf("=");
			if(n>-1)
			{	String s1 = s.substring(2,n);
				String s2 = s.substring(n+1,s.length()-1);
				FormuleParser p = new FormuleParser();
				Expressie e1 = p.parse(p.schoon(p.formuleString("$f" + s1 + "@")));
				Expressie e2 = p.parse(p.schoon(p.formuleString("$f" + s2 + "@")));
				
				if(e1!=null && e2!=null)
				{	juisteAntwoord = new Aftrekking(e1,e2);
				}
			}
			else
			{	FormuleParser p = new FormuleParser();
				juisteAntwoord = FormuleParser.parse(p.schoon(p.formuleString(s)));
			}
		}
		//formuleVak.vulVak(s);
	}
	
	public void stop()
	{	checkAntwoord();
		kijkNa();
		if(ingevuld) produceAction("changed");
			
			
		/*Expressie antwoord = formuleVak.geefExpressie();
		if(antwoord!=null)formuleVak.vulVak("$f" + antwoord.toString() + "@");
		if(antwoord!=null && juisteAntwoord!=null && Algebra.isGelijkwaardig(antwoord,juisteAntwoord))
		{	zetGoedFout(GOED);
			produceAction("blijktgoed");
		}
		else 
		{	if(antwoord!=null)zetGoedFout(FOUT);
			else zetGoedFout(GEEN);
			produceAction("blijktfout");
		}*/
	}
	
	public void kijkNa()
	{	checkAntwoord();
		if(!herleiding && !exact) 
		{	if(isGelijkwaardig)
			{	zetGoedFout(GOED);
				score = puntenGelijkwaardig;
				correct = true;
			}
			else
			{	zetGoedFout(FOUT);
				score = 0;
				correct = false;
			}
		}
		else if(herleiding && !exact)
		{	if(isGelijkwaardig && isHerleid)
			{	zetGoedFout(GOED);
				score = puntenGelijkwaardig + puntenHerleiding;
				correct = true;
			}
			else if(isGelijkwaardig && !isHerleid)
			{	zetGoedFout(HALF);
				score = puntenGelijkwaardig;
				correct = false;
			}
			else 
			{	zetGoedFout(FOUT);
				score = 0;
				correct = false;
			}
		}
		else if(exact)
		{	if(isGelijkwaardig && isExact)
			{	zetGoedFout(GOED);
				score = puntenGelijkwaardig + puntenExact;
				correct = true;
			}
			else if(isGelijkwaardig && !isExact)
			{	zetGoedFout(HALF);
				score = puntenGelijkwaardig;
				correct = false;
			}
			else 
			{	zetGoedFout(FOUT);
				score = 0;
				correct = false;
			}
		}
	}

	public static boolean isHerleidingZH(Expressie gegevenExp, Expressie gevrExp)
	{	Expressie gevrHerlExpressie = Algebra.herleid(Algebra.verwijderHaakjes(gevrExp));
		boolean herleiding = false;
		String s = gegevenExp.toString();
		boolean haakjes = s.indexOf("$h")>-1;
		herleiding = !haakjes 
					&& Algebra.geefAantalMachten(gegevenExp)==Algebra.geefAantalMachten(gevrHerlExpressie) 
					&& Algebra.geefTermen(gegevenExp, new Vector()).size()==Algebra.geefTermen(gevrHerlExpressie, new Vector()).size() 
					&& Algebra.geefAantalFactorenTermen(gegevenExp)<=Algebra.geefAantalBreukPlusGetal(gevrHerlExpressie) + Algebra.geefAantalFactorenTermen(gevrHerlExpressie);
		return herleiding;
	}	
	
	public static boolean isMacht(Expressie gegevenExp, Expressie gevrExp)
	{	boolean herleiding = false;
		String s = gegevenExp.toString();
		boolean wortels = s.indexOf("$w")>-1 || s.indexOf("$W")>-1 ;
		herleiding = !wortels 
					&& (gegevenExp instanceof Macht
						|| gegevenExp instanceof Aftrekking && gegevenExp.kind1.geefWaarde()==0 && gegevenExp.kind2 instanceof Macht
						|| gegevenExp instanceof Vermenigvuldiging && !Double.isNaN(gegevenExp.kind1.geefWaarde()) && gegevenExp.kind2 instanceof Macht
						|| gegevenExp instanceof Aftrekking && gegevenExp.kind1.geefWaarde()==0 && gegevenExp.kind2 instanceof Vermenigvuldiging && !Double.isNaN(gegevenExp.kind2.kind1.geefWaarde())  && gegevenExp.kind2.kind2 instanceof Macht
						);
		return herleiding;
	}
	
	public static boolean isZonderGebrokenOfNegExp(Expressie gegevenExp, Expressie gevrExp)
	{	boolean herleiding = true;
		Vector v = Algebra.geefMachten(gegevenExp,new Vector());
		for(int i=0 ; i<v.size(); i++)
		{	Expressie e = (Expressie)v.elementAt(i);
		   	if(Algebra.geefDelingen(e.kind2, new Vector()).size()>0)
		   	{	herleiding = false;
		   		break;
		   	}
		   	else if(Algebra.geefAftrekkingen(e.kind2, new Vector()).size()>0)
		   	{	herleiding = false;
		   		break;
		   	}
		}
		if(Algebra.geefWortels(gegevenExp, new Vector()).size()>1)herleiding = false;
		return herleiding;
	}	
	
	public static boolean isBreukHerleiding(Expressie gegevenExp, Expressie gevrExp)
	{	boolean herleiding = true;
		Vector v = Algebra.geefTermen(gegevenExp,new Vector());
		if(v.size()>1)
		{	herleiding = false;
		}
		else
		{	boolean breuk = gegevenExp instanceof Deling 
							|| gegevenExp instanceof Aftrekking 
							&& gegevenExp.kind1.geefWaarde()==0
							&& gegevenExp.kind2 instanceof Deling;
			if(!breuk)herleiding = false;
		}
		return herleiding;
	}
	
	public void checkAntwoord()	
	{	isGelijkwaardig = false;
		isHerleid = false;
		isExact = false;
		ingevuld  = false;	
		
		String s = formuleVak.toString();
		int n = s.indexOf("=");
		if(n>-1)
		{	String s1 = s.substring(2,n);
			String s2 = s.substring(n+1,s.length()-1);
			FormuleParser p = new FormuleParser();
			Expressie e1 = FormuleParser.parse(p.schoon(p.formuleString("$f" + s1 + "@")));
			Expressie e2 = p.parse(p.schoon(p.formuleString("$f" + s2 + "@")));
			
			if(e1!=null && e2!=null)
			{	ingevuld  = true;
				Expressie e = new Aftrekking(e1,e2);
				if(juisteAntwoordExtra==null)
				{	isGelijkwaardig = Algebra.zijnEvenredig(e,juisteAntwoord);
				}
				else
				{	isGelijkwaardig = Algebra.zijnEvenredig(e,juisteAntwoord)
									|| Algebra.zijnEvenredig(e,juisteAntwoordExtra);
				}
				
			}
		}
		else
		{	Expressie antwoord = formuleVak.geefExpressie();
			if(antwoord!=null)
			{	//formuleVak.vulVak("$f" + antwoord.toString() + "@");
				ingevuld  = true;
			}
				
			if(antwoord==null || juisteAntwoord==null) return;
			
			if(juisteAntwoordExtra==null)
			{	isGelijkwaardig = Algebra.isGelijkwaardig(antwoord,juisteAntwoord);
			}
			else
			{	isGelijkwaardig = Algebra.isGelijkwaardig(antwoord,juisteAntwoord)
								|| Algebra.isGelijkwaardig(antwoord,juisteAntwoordExtra);
			}
			
			if(herleiding)
			{	if(soortHerleiding==HERLEIDING_VEELTERM_ZH) isHerleid = isHerleidingZH(antwoord,juisteAntwoord);
				else if(soortHerleiding==HERLEIDING_1_MACHT) isHerleid = isMacht(antwoord,juisteAntwoord);
				else if(soortHerleiding==HERLEIDING_MACHT_Z_NEG_BREUK_EXP) isHerleid = isZonderGebrokenOfNegExp(antwoord,juisteAntwoord);
				else if(soortHerleiding==HERLEIDING_1_BREUK) isHerleid = isBreukHerleiding(antwoord,juisteAntwoord);
			}
			
			if(exact)
			{	isExact = antwoord.toString().equals(juisteAntwoord.toString());
			}
		}
		
	}	
	
	public boolean isGelijkwaardig()
	{	return isGelijkwaardig;
	}
	public boolean isHerleid()
	{	return isHerleid;
	}
	public boolean isExact()
	{	return isExact;
	}
	
	public int getScore()
	{	return score;
	}

	public boolean isCorrect()
	{	return correct;
	}
	
	public void actionPerformed(ActionEvent e)
	{	super.actionPerformed(e);
		if(e.getSource()==formuleVak && e.getActionCommand().equals("ingevuld"))
		{	kijkNa();
			if(ingevuld)produceAction("changed");
		}
	}
	
	/*public void actionPerformed(ActionEvent e)
	{	super.actionPerformed(e);
		if(e.getSource()==formuleVak && e.getActionCommand().equals("ingevuld"))
		{	Expressie antwoord = formuleVak.geefExpressie();
			if(antwoord!=null)formuleVak.vulVak("$f" + antwoord.toString() + "@");
			if(antwoord!=null && juisteAntwoord!=null && Algebra.isGelijkwaardig(antwoord,juisteAntwoord))
			{	zetGoedFout(GOED);
				produceAction("goed");
			}
			else
			{	zetGoedFout(FOUT);
				produceAction("fout");
			}
			
		}
	}*/
	
	
	//ActionProducer
	private ActionListener actionListener = null;
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}	
 	
 	public void produceAction(String command)
 	{	if (actionListener != null)
 		{	actionListener.actionPerformed( new ActionEvent(this, 0, command) );
 		}
 	}
 	//
}
