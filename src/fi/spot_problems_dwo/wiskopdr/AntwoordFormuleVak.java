package fi.spot_problems_dwo.wiskopdr;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import fi.spot_problems_dwo.wiskopdr.formuleobjects.*;
import fi.spot_problems_dwo.wiskopdr.expressies.*;


public class AntwoordFormuleVak extends FormuleEditor 
{
	private static Image GOEDKRUL,FOUTKRUIS, HALFKRUL;
	private static int GOED = 1;
	private static int FOUT = 0;
	private static int HALF = 2;
	private static int GEEN = 3;
	
	private BewerkingButton gelijkwaardigKnop,terugKnop;
	
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
	private boolean stapOk;
	
	private ImageComponent goedIC, foutIC, halfIC, huidigIC;
	private Expressie juisteAntwoord;
	
	private PijlVak[] pijlVakken;
	private FormuleVak[] formuleVakken;
	private int stapNr;
	
	private boolean stappen;
	private boolean hasPrefix;
	private String prefix;
	private FormuleVak[] prefixVakken;
	
	//tester voor parser
	/*private String eString,e1String,e2String,e3String;
	
	public void paint(Graphics g)
	{	super.paint(g);
		g.drawString(eString,20,100);
		g.drawString(e1String,20,120);
		g.drawString(e2String,20,140);
		g.drawString(e3String,20,160);
	}*/
	//
	
	public AntwoordFormuleVak()
	{	super(true);
		//formuleVak.addActionListener(this);
		//formuleVak.setLocation(30,20);
		remove(formuleVak);
		
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
		
		gelijkwaardigKnop = new BewerkingButton("gelijkwaardig");
		gelijkwaardigKnop.setBounds(280,2,20,20);
		gelijkwaardigKnop.addActionListener(this);
		zetOpBalk(gelijkwaardigKnop);
		
		terugKnop = new BewerkingButton("terug");
		terugKnop.setBounds(302,2,20,20);
		terugKnop.addActionListener(this);
		zetOpBalk(terugKnop);
		
		pijlVakken = new PijlVak[100];
		formuleVakken = new FormuleVak[100];
		prefixVakken = new FormuleVak[100];
		
		formuleVakken[0] = new FormuleVak();
		formuleVakken[0].addActionListener(this);
		formuleVakken[0].setLocation(30,20);
		add(formuleVakken[0]);
		
		formuleVak = formuleVakken[0];
	}
	
	public void setState(Hashtable h)
	{	if(h==null)return;
	    for(int i=0 ; i<stapNr+1; i++)
	    {	remove(formuleVakken[i]);
	    }
	    for(int i=0 ; i<stapNr; i++)
	    {	remove(pijlVakken[i]);
	    }
	    
	    int stapNr = ((Integer)h.get("stapNr")).intValue();
		String[] formuleVakInhouden = (String[])h.get("formuleVakInhouden");
		int[] vvY = (int[])h.get("vvY");
		int[] pvY = (int[])h.get("pvY");
		boolean ingevuld = ((Boolean)h.get("ingevuld")).booleanValue();
		
		this.stapNr = stapNr;
		formuleVakken = new FormuleVak[100];
		for(int i=0 ; i<stapNr+1; i++)
	    {	formuleVakken[i] = new FormuleVak();
	    	if(i<stapNr) formuleVakken[i].setEditable(false);
	    	int x = 30;
	    	if(prefix!=null) 
	    	{	prefixVakken[i] = new FormuleVak();
	    		prefixVakken[i].setLocation(30,vvY[i]);
	    		prefixVakken[i].setEditable(false);
	    		prefixVakken[i].setSelectable(false);
				prefixVakken[i].vulVak(prefix);
				add(prefixVakken[i]);
				x = 30 + prefixVakken[0].getSize().width;
	    	}
	    	formuleVakken[i].setLocation(x,vvY[i]);
	    	formuleVakken[i].addActionListener(this);
	    	add(formuleVakken[i]);
	    	formuleVakken[i].vulVak(formuleVakInhouden[i]);
	    }
	    
	    pijlVakken = new PijlVak[100];
		for(int i=0 ; i<stapNr; i++)
	    {	pijlVakken[i] = new PijlVak("gelijkwaardig");
	    	pijlVakken[i].setLocation(getSize().width-150,pvY[i]);
	    	add(pijlVakken[i]);
	    }
	    
	    this.ingevuld = ingevuld;
	    
	    formuleVakken[stapNr].setEditable(true);
	    formuleVak = formuleVakken[stapNr];
	   	kijkNa();
	
	}
	
	public Hashtable getState()
	{	int stapNr=0;
		String[] formuleVakInhouden=null;
		int[] vvY = null;
		int[] pvY = null;
		boolean ingevuld = true;
		
		stapNr = this.stapNr;
		ingevuld = this.ingevuld;
		if(!ingevuld)
		{	try
			{	formuleVak.finish();
			}
			catch(Exception e){}
		}	
	    
		formuleVakInhouden = new String[stapNr+1];
		vvY = new int[stapNr+1];
		for(int i=0 ; i<stapNr+1; i++)
	    {	formuleVakInhouden[i] = formuleVakken[i].toString();
	    	vvY[i] = formuleVakken[i].getLocation().y;
	    }
	    
	    
	    pvY = new int[stapNr];
		for(int i=0 ; i<stapNr; i++)
	    {	pvY[i] = pijlVakken[i].getLocation().y;
	    }
	    
	    ingevuld = this.ingevuld;
	    			    
	    Hashtable h = new Hashtable();
	    h.put("stapNr", new Integer(stapNr));
	    h.put("formuleVakInhouden", formuleVakInhouden);
	    h.put("vvY", vvY);
	    h.put("pvY", pvY);
	    h.put("ingevuld", new Boolean(ingevuld));
	    
	    return h;
	}
	
	public void zetStappen(boolean b)
	{	stappen = b;
		gelijkwaardigKnop.setVisible(b);
		terugKnop.setVisible(b);
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
		int y = formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height-20;
		huidigIC.setLocation(5,y);
		huidigIC.setVisible(true);
	}
	
	
	public static void zetPlaatjes(Image gk, Image fk, Image hk)
	{	GOEDKRUL = gk;
		FOUTKRUIS = fk;
		HALFKRUL = hk;
	}
	
	public void zetPlaatjes2(Image gk, Image fk, Image hk)
	{	GOEDKRUL = gk;
		FOUTKRUIS = fk;
		HALFKRUL = hk;
		
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
	
	public void zetJuisteAntwoord(String s)
	{	int index = s.indexOf("=");
		if (index > -1)
		{	prefix = s.substring(0, index + 1) + "@";
			hasPrefix = true;
			s = "$f"+ s.substring(index + 1);
			prefixVakken[0] = new FormuleVak();
			prefixVakken[0].setLocation(30, 20);
			prefixVakken[0].setEditable(false);
			prefixVakken[0].setSelectable(false);
			prefixVakken[0].vulVak(prefix);
			add(prefixVakken[0]);
			
			int x = 30 + prefixVakken[0].getSize().width;
			remove(formuleVakken[0]);
			formuleVakken[0] = new FormuleVak();
			formuleVakken[0].setLocation(x, 20);
			formuleVakken[0].addActionListener(this);
			add(formuleVakken[0]);
			formuleVak = formuleVakken[0];
			zetGoedFout(GEEN);
		}
	
		FormuleParser p = new FormuleParser();
		juisteAntwoord = p.parse(p.schoon(p.formuleString(s)));
		//formuleVak.vulVak(s);
	}

	public String geefAntwoord()
	{
		return formuleVakken[0].toString();
	}
	
	public void zetAntwoord(String a)
	{
		formuleVakken[0].vulVak(a);
	}
	
	public void stop()
	{	checkAntwoord();
		kijkNa();
		if (ingevuld) 
			produceAction("changed");
			
	}
	
	public void start()
	{	formuleVak.requestFocus();
	}
	
	public void kijkNa()
	{	checkAntwoord();
		if (!ingevuld)
		{	zetGoedFout(GEEN);
			return;
		}
		if (!herleiding && !exact) 
		{	if (isGelijkwaardig)
			{	zetGoedFout(GOED);
				score = puntenGelijkwaardig;
				correct = true;
				stapOk = true;
			}
			else
			{	zetGoedFout(FOUT);
				score = 0;
				correct = false;
			}
		}
		else if (herleiding && !exact)
		{	if(isGelijkwaardig && isHerleid)
			{	zetGoedFout(GOED);
				score = puntenGelijkwaardig + puntenHerleiding;
				correct = true;
				stapOk = true;
			}
			else if(isGelijkwaardig && !isHerleid)
			{	zetGoedFout(HALF);
				score = puntenGelijkwaardig;
				correct = false;
				stapOk = true;
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
				stapOk = true;
			}
			else if(isGelijkwaardig && !isExact)
			{	zetGoedFout(HALF);
				score = puntenGelijkwaardig;
				correct = false;
				stapOk = true;
			}
			else 
			{	zetGoedFout(FOUT);
				score = 0;
				correct = false;
			}
		}
	}

	
	public void checkAntwoord()	
	{	
		/*eString = formuleVak.toString();
		e1String = FormuleParser.formuleString(eString);
		e2String = FormuleParser.schoon(e1String);
		e3String = FormuleParser.pel(e2String);*/
	
		ingevuld  = false;	
		Expressie antwoord = formuleVak.geefExpressie();
		if (antwoord != null)
		{	//formuleVak.vulVak("$f" + antwoord.toString() + "@");
			ingevuld  = true;
		}
		
//System.out.println("ingevuld = " + ingevuld);

		isGelijkwaardig = AntwoordChecker.checkGelijkwaardig(antwoord, juisteAntwoord);
		isHerleid = AntwoordChecker.checkHerleiding(antwoord,juisteAntwoord, soortHerleiding);
		isExact = AntwoordChecker.checkExact(antwoord,juisteAntwoord);
		repaint();
		
//System.out.println("isGelijkwaardig = " + isGelijkwaardig);		
	}	
	
	/*public boolean isGelijkwaardig()
	{	return isGelijkwaardig;
	}
	public boolean isHerleid()
	{	return isHerleid;
	}
	public boolean isExact()
	{	return isExact;
	}*/
	
	public int getScore()
	{	return score;
	}

	public boolean isCorrect()
	{	return correct;
	}
	
	private void maakStap()
	{	if(stapOk )
		{	stapOk = false;
			pijlVakken[stapNr] = new PijlVak("gelijkwaardig");
			int y = formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height/2;
			pijlVakken[stapNr].setLocation(getSize().width-150,y);
			add(pijlVakken[stapNr]);
			
			formuleVakken[stapNr].setEditable(false);
			
			formuleVakken[stapNr+1] = new FormuleVak();
			y = formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height + 30;
			int x = 30;
			if(prefix!=null) 
	    	{	prefixVakken[stapNr+1] = new FormuleVak();
	    		prefixVakken[stapNr+1].setLocation(30,y);
				prefixVakken[stapNr+1].vulVak(prefix);
				prefixVakken[stapNr+1].setEditable(false);
				prefixVakken[stapNr+1].setSelectable(false);
				add(prefixVakken[stapNr+1]);
				x = 30 + prefixVakken[0].getSize().width;
	    	}
			formuleVakken[stapNr+1].setLocation(x,y);
			formuleVakken[stapNr+1].addActionListener(this);
			add(formuleVakken[stapNr+1]);
			
			
			
			formuleVak = formuleVakken[stapNr+1];
			zetGoedFout(GEEN);
				
			stapNr++;
		}
	}
	
			
	private void stapTerug()
	{	if(stapNr>0)
		{	remove(formuleVakken[stapNr]);
			remove(pijlVakken[stapNr-1]);
			if(prefixVakken[stapNr]!=null)remove(prefixVakken[stapNr]);
			formuleVakken[stapNr-1].setEditable(true);
			
			formuleVak = formuleVakken[stapNr-1];
			stapNr--;
			stapOk = false;
			kijkNa();
			produceAction("changed");
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{	super.actionPerformed(e);
		if (e.getSource() == formuleVak && e.getActionCommand().equals("ingevuld"))
		{	
			
System.out.println("aVak action");			
			kijkNa();
			if (ingevuld)
				produceAction("changed");
		}
		else if(e.getSource()==gelijkwaardigKnop)
		{	maakStap();
			formuleVak.requestFocus();
		}
		else if(e.getSource()==terugKnop)
		{	stapTerug();
			formuleVak.requestFocus();
		}
		else if(e.getActionCommand().equals("zetMaat") && prefix!=null)
		{	int h1 = prefixVakken[stapNr].getLocation().y;
			int h2 = formuleVakken[stapNr].getLocation().y;		
			int dh = formuleVakken[stapNr].ashoogte-prefixVakken[stapNr].ashoogte;
			prefixVakken[stapNr].setLocation(30,h2+dh);
		}
	}
	
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
