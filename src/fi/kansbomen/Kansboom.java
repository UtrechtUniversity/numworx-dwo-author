package fi.kansbomen;

import java.awt.*;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Kansboom extends JPanel

{ 
	static int BREEDTEVOLGORDE=60;
	int hoogteKansboomveld = 400;
	int hoogteKansboom;
	int breedteKansboomveld = 600;
	int aantalKolommen;
	int breedteKolom;
	int rijhoogte;
	
	int breedteVolgordekolom, breedteKansNaastKolom, breedteKansOnderKolom;
	boolean volgorde, eindkansNaast, eindkansOnder, bovenbalk;
	int offset = 2;
	
	Color[] gekleurdeRij, zwarteRij;
	
	int aantalOpties;
	int[] teller;
	int[] aantal;
	boolean terugleggen, kleur, letters, kans, breukOnder;
	String volgordeString;
	String[] letter = {"b","g","r","c","o","m"};
	String trekkingTekst = Kansbomen.rb.getString("trekkingBalkTekst");
	
	Color backgroundColor = Color.white;
	
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	public Kansboom()
	{
	    theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		gekleurdeRij = new Color[6];
		gekleurdeRij[0] = new Color(0,0,255);
		gekleurdeRij[1] = new Color(0,200,0);
		gekleurdeRij[2] = new Color(255,50,50);
		gekleurdeRij[3] = new Color(0,220,220);
		gekleurdeRij[4] = new Color(255,180,0);
		gekleurdeRij[5] = new Color(220,0,220);
		
		zwarteRij = new Color[6];
		for(int i = 0; i < 6; i++)
			zwarteRij[i] = new Color(0,0,0);
		
    	aantalKolommen = 3; 
    	    	
    	aantalOpties = 4; 
    	aantal = new int[6];
    	for(int i = 0; i<4; i++)
    	aantal[i] = 4; 
    	aantal[4] = -1;
    	aantal[5] = -1;
    	teller = new int[6];
    	
    	volgorde = false;
    	eindkansNaast = false;
    	eindkansOnder = false;
    	bovenbalk = true;
    	if(bovenbalk)
    		rijhoogte = theFM.getHeight()+offset;
    	else
    		rijhoogte = 0;
    	
    	breedteVolgordekolom=aantalKolommen*theFM.charWidth('a') + 2* offset;
    	if(volgorde)
    		breedteKolom = (breedteKansboomveld - breedteVolgordekolom)/aantalKolommen;
    	else if(eindkansNaast)
    		breedteKolom = (breedteKansboomveld - breedteKansNaastKolom)/aantalKolommen;
    	else if(eindkansOnder)
    		breedteKolom = (breedteKansboomveld - breedteKansOnderKolom)/aantalKolommen;
    	else 
    		breedteKolom=breedteKansboomveld/aantalKolommen;
    	if(bovenbalk)
    		
    		hoogteKansboom = hoogteKansboomveld - rijhoogte;
    	else
    		hoogteKansboom = hoogteKansboomveld;
    	//setSize(breedteKansboomveld, hoogteKansboomveld);
    	
    	terugleggen = true;  
    	kleur = true; 
    	letters = false;
    	kans = false;
    	breukOnder = false;
    	
	}
	
	
	public void paint(Graphics g)
	{ 	g.setColor(backgroundColor);
		g.fillRect(0, 0, getSize().width, getSize().height);
	    
		this.tekenKansboom(g);
		
	}
	
	private void tekenKansboom(Graphics gr)
	{ 	
		int k = aantalOpties;
		Graphics2D gr2 = (Graphics2D) gr;
		gr2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		        RenderingHints.VALUE_ANTIALIAS_ON);	
		for(int i=0; i<aantalKolommen; i++)
		{	zetStartTellers();
			for(int j=0; j<Math.pow(k,i); j++)
			{	if(!terugleggen)
					this.zetTellers(i,j);	
				this.tekenKinderen(i, j, teller, gr);	
			}
		}
		gr.setColor(Color.BLACK);
		gr.setFont(theFont);
		if(volgorde)
			this.tekenVolgordeStrings(gr);
		else if(eindkansNaast)
			this.tekenEindkansenNaast(gr);
		else if(eindkansOnder)
			this.tekenEindkansenOnder(gr);
		if(letters)
			this.tekenLetters(gr);
		else if(kans)
			this.tekenKansen(gr);
		if(bovenbalk)
			this.tekenBovenbalk(gr);			
	}
	
	public boolean bestaanKinderen(int[] aantal, int[] teller)
	{
		boolean bestaanKinderen = true;
		for(int i = 0; i < teller.length; i++)
			bestaanKinderen = bestaanKinderen && teller[i] <= aantal[i];
		return bestaanKinderen;
	}
	
	private void zetStartTellers()
	{
		for(int p = 0; p < aantalOpties; p++)
			teller[p] = 0;
		for(int p = aantalOpties; p < 6; p++)
			teller[p] = -1;
	}
	
	private void zetTellers(int i, int j)
	{	int d = 0;
		int h = 0;
		int k = aantalOpties;
		int mod = 0;
		int macht = 0;
		
		zetStartTellers();
		
		for(int s=0; s<i; s++)
		{	d=(int) (j/Math.pow(k,s));
			mod=d%k;
			macht=(int) Math.pow(10,s);
			h=h+macht*mod;
		}
		for(int q=0; q<i; q++)
		{	int p = i-q-1;
			for(int n = 5; n > -1; n--)
			{
				if(h >= n * Math.pow(10, p))
				{
					teller[n]++;
					h = (int) (h - n * Math.pow(10, p));
					break;
				}
			}
		}	
	}
	

	private void tekenKinderen(int i, int j, int[] teller, Graphics gr)
	{
		int b = breedteKolom;
		int h = hoogteKansboom;
		int k = aantalOpties;
		
		Color[] kleurRij = new Color[6];
		if(kleur) 
			kleurRij = gekleurdeRij;
		else
		{	for(int p = 0; p < 6; p++)
			kleurRij[p]= Color.BLACK;
		}
		
		if(bestaanKinderen(aantal, teller))
		{ 	for(int p = 0; p < aantalOpties; p++)
			if(teller[p] < aantal[p])
			{	gr.setColor(kleurRij[p]);
				gr.drawLine(i*b, (int) ((2*j+1)/(Math.pow(k,i)*2)*h)+rijhoogte, (i+1)*b, (int) ((2*k*j+2*p+1)/(Math.pow(k,i+1)*2)*h)+rijhoogte);
			}
			
		}
	}
	
	
	private void tekenLetters(Graphics gr)
	{
		int b = breedteKolom;
		int h = hoogteKansboom;
		int k = aantalOpties;
		
		gr.setColor(backgroundColor);
		for(int i=0; i<aantalKolommen; i++)
		{
			gr.fillRect((2*i+1)*b/2-theFM.charWidth('a')/2-offset, rijhoogte, theFM.charWidth('a')+2*offset,h-rijhoogte);
		}
	
		gr.setColor(Color.BLACK);
		for(int i=0; i<aantalKolommen; i++)
		{	zetStartTellers();
			for(int j=0; j<Math.pow(k,i); j++)
			{	if(!terugleggen)
					this.zetTellers(i,j);	
				if(bestaanKinderen(aantal,teller))
				{ 	for(int p = 0; p < k; p++)
					if(teller[p]<aantal[p])
						gr.drawString(letter[p],(2*i+1)*b/2-theFM.charWidth('a')/2, 
								(int) (h*(4*k*j+k+2*p+1)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3+rijhoogte);
				}		
			}
		}	
	}
	
	private void tekenKansen(Graphics gr)
	{
		int k = aantalOpties;
		int a = 0;
		
		for(int p = 0; p < aantalOpties; p++)
			a += aantal[p];
		
		for(int i=0; i<aantalKolommen; i++)
		{	zetStartTellers();
			for(int j=0; j<Math.pow(k,i); j++)
			{if(terugleggen)
				{
				gr.setColor(backgroundColor);
				
				if(breukOnder)
					for(int p = 0; p < aantalOpties; p++)
						tekenRechthoekOnder(p, aantal[p], a, gr, i, j);
				else 
					for(int p = 0; p < aantalOpties; p++)
						tekenRechthoekNaast(p, aantal[p], a, gr, i, j);
				gr.setColor(Color.BLACK);
				if(breukOnder)
					for(int p = 0; p < aantalOpties; p++)
						tekenBreukOnder(p, aantal[p], a, gr, i, j);	
				else 
					for(int p = 0; p < aantalOpties; p++)
						tekenBreukNaast(p, aantal[p], a, gr, i, j);
				}
			else //zonder terugleggen
				{	
				this.zetTellers(i,j);	
				gr.setColor(backgroundColor);
				if(bestaanKinderen(aantal,teller))
				{ 	for(int p = 0; p < aantalOpties; p++)
					if(teller[p]<aantal[p])
						if(breukOnder)
						tekenRechthoekOnder(p, aantal[p]-teller[p], a-i, gr, i, j);
						else	
						tekenRechthoekNaast(p, aantal[p]-teller[p], a-i, gr, i, j);
					}	
				gr.setColor(Color.BLACK);
					if(bestaanKinderen(aantal,teller))
					{ 
						for(int p = 0; p < aantalOpties ; p++)
							if(teller[p]<aantal[p])	
								if(breukOnder)
								tekenBreukOnder(p, aantal[p]-teller[p], a-i, gr, i, j);
								else
								tekenBreukNaast(p, aantal[p]-teller[p], a-i, gr, i, j);
					}	
				}
			}
		}
	}
	
	
	
	public void tekenVolgordeStrings(Graphics gr)
	{	int k = aantalOpties;
		int n = aantalKolommen;
		int b = breedteKansboomveld;
		int h = hoogteKansboom;
		for(int j=0; j<Math.pow(k,n); j++)
		{	volgordeString = bepaalVolgordeString(j);
			
			zetTellers(n,j);
			if(terugleggen)
				gr.drawString(volgordeString, b - breedteVolgordekolom + offset, 
						(int) ((2*j+1)*h/(2*Math.pow(k,n)))+theFM.getHeight()/3+rijhoogte);
			else if(bestaanKinderen(aantal,teller))
				gr.drawString(volgordeString, b - breedteVolgordekolom + offset, 
						(int) ((2*j+1)*h/(2*Math.pow(k,n)))+theFM.getHeight()/3+rijhoogte);
		}
		
	}
	
	public String bepaalVolgordeString(int j)
	{
		volgordeString = "";
		int positie=j;
		
		for(int i = 1; i < aantalKolommen + 1; i++)
		{	for(int p = 0; p < 6; p++)
			{	if(positie < (p + 1) * Math.pow(aantalOpties, aantalKolommen - i))
				{	volgordeString = volgordeString + letter[p];
					positie -= p * Math.pow(aantalOpties, aantalKolommen-i);
					break;
				}
			}
		}
		return volgordeString;
	}
	
	public int[] eindkansMetTerug()
	{
		int n = aantalKolommen;
		int a = 0;
		for (int i = 0; i < aantalOpties; i++)
			a += aantal[i];
		int totaalTeller = 1;
		
		for(int i = 0; i < aantalOpties; i++)
			totaalTeller *= Math.pow(aantal[i], Math.max(teller[i], 0));
		
		int[] breuk = simplify(totaalTeller,(int) Math.pow(a, n));
		return breuk;
		
	}
	
	public int[] eindkansZonderTerug()
	{
		int n = aantalKolommen;
		int[] s = new int[aantalOpties];
		int noemer = 1;
		int totaalTeller = 1;
		int a = 0;
		for (int i = 0; i < aantalOpties; i++)
			a += aantal[i];
		
		for(int p = 0; p < aantalOpties; p++)
			s[p] = 1;
		noemer = 1;
		for(int p = 0; p < aantalOpties; p++)
			for(int i = 0; i < teller[p]; i++)
				s[p] *= aantal[p] - i;
		for(int i=0; i < n; i++)	
			noemer *= a-i;
		for(int p = 0; p < aantalOpties; p++)
			totaalTeller *= s[p];
					
		int[] breuk = simplify(totaalTeller, noemer);
		return breuk;
	}
	
	public void tekenEindkansenNaast(Graphics gr)
	{	int k = aantalOpties;
		int n = aantalKolommen;
		int b = breedteKansboomveld;
		int h = hoogteKansboom;
		
		for(int j=0; j<Math.pow(k,n); j++)
		{	zetTellers(n,j);
			if(terugleggen)
			{
				gr.drawString(breukNaast(eindkansMetTerug()), b - breedteKansNaastKolom + offset, 
						(int) ((2*j+1)*h/(2*Math.pow(k,n)))+theFM.getHeight()/3+rijhoogte);
			}
			else if(bestaanKinderen(aantal,teller))
			{	
				gr.drawString(breukNaast(eindkansZonderTerug()), b - breedteKansNaastKolom + offset, 
						(int) ((2*j+1)*h/(2*Math.pow(k,n)))+theFM.getHeight()/3+rijhoogte);
			}
		}
	}

	
	public void tekenEindkansenOnder(Graphics gr)
	{	int k = aantalOpties;
		int n = aantalKolommen;
		
		for(int j=0; j<Math.pow(k,n); j++)
		{	zetTellers(n,j);
			if(terugleggen)
			{	
				tekenEindBreukOnder(eindkansMetTerug()[0], eindkansMetTerug()[1], gr, j);
			}
			else if(bestaanKinderen(aantal,teller))
			{	
				tekenEindBreukOnder(eindkansZonderTerug()[0], eindkansZonderTerug()[1], gr, j);
			}
		}
	}
	
	
	public void tekenBovenbalk(Graphics gr)
	{
		gr.setFont(theBoldFont);
		int n = aantalKolommen;
		int xpos, ypos;
		
		for(int i=0; i<n; i++)
		{	xpos = (2*i+1) * breedteKolom / 2 - theBoldFM.stringWidth(Kansbomen.rb.getString("trekkingBalkTekst")+" "+(i+1))/2;
			ypos = rijhoogte;
			gr.drawString(trekkingTekst+" "+(i+1), xpos, ypos);
		}	
	}
	
	public int berekenBreedteKansNaastKolom()
	{
		int m = 0;
		int k = aantalOpties;
		int n = aantalKolommen;
		
		for(int j=0; j<Math.pow(k,n); j++)
		{	zetTellers(n,j);
			if(terugleggen)
				m = Math.max(theFM.stringWidth(breukNaast(eindkansMetTerug())),m);
			else if(bestaanKinderen(aantal, teller))
			{	
				m = Math.max(theFM.stringWidth(breukNaast(eindkansZonderTerug())),m);
			}
		}
		return m + 2 * offset;
	}
	
	public int berekenBreedteKansOnderKolom()
	{
		int m = 0;
		int k = aantalOpties;
		int n = aantalKolommen;
		
		for(int j=0; j<Math.pow(k,n); j++)
		{	zetTellers(n,j);
			if(terugleggen)
				m = Math.max(breukBreedte(eindkansMetTerug()[0], eindkansMetTerug()[1]),m);
			else if(bestaanKinderen(aantal,teller))
			{	
				m = Math.max(breukBreedte(eindkansZonderTerug()[0],eindkansZonderTerug()[1]),m);
			}
		}
		return m + 2 * offset;
	}
	
	public int berekenBreedteVolgordekolom()
	{
		int m = 0;
		int k = aantalOpties;
		int n = aantalKolommen;
		for(int j=0; j<Math.pow(k,n); j++)
		{	volgordeString = bepaalVolgordeString(j);
			m = Math.max(theFM.stringWidth(volgordeString),m);
		}
		
		return m + 2 * offset;
	}
	
	public void setSize(int b, int h)
	{	breedteKansboomveld = b;
	    hoogteKansboomveld = h;
	    
	    breedteVolgordekolom = berekenBreedteVolgordekolom();
	    breedteKansNaastKolom = berekenBreedteKansNaastKolom();
	    breedteKansOnderKolom = berekenBreedteKansOnderKolom();
	    if(volgorde)
	    	breedteKolom = (breedteKansboomveld - breedteVolgordekolom)/aantalKolommen;
	    else if(eindkansNaast)
	    	breedteKolom = (breedteKansboomveld - breedteKansNaastKolom)/aantalKolommen;
	    else if(eindkansOnder)
	    	breedteKolom = (breedteKansboomveld - breedteKansOnderKolom)/aantalKolommen;
	    else	
	    	breedteKolom=breedteKansboomveld/aantalKolommen;
	    if(bovenbalk)
	    	hoogteKansboom = h-rijhoogte;
	    else
	    	hoogteKansboom = h;
	    super.setSize(b,h);
	}
		
	public void zetKleur(boolean b)
	{
		kleur = b;
		repaint();
	}
	
	public void zetKansVolgorde(int i)
	{
		volgorde = false;
		eindkansNaast = false;
		eindkansOnder = false;
		if(i==1)
			volgorde = true;
		else if(i==2)
			eindkansNaast = true;
		else if(i==3)
			eindkansOnder = true;
		setSize(breedteKansboomveld, hoogteKansboomveld);
		repaint();
	}
	
	public void zetTerugleggen(boolean b)
	{
		terugleggen = b;
		setSize(breedteKansboomveld, hoogteKansboomveld);
		repaint();
	}
	
	public void zetTrekkingen(int i)
	{
		aantalKolommen = i;
		setSize(breedteKansboomveld, hoogteKansboomveld);
		repaint();
	}
	
	public void zetLabelsKeuze(int i)
	{
		letters = false;
		kans = false;
		breukOnder = false;
		if(i==1)
			letters =true;
		else if(i==2)
			kans = true;
		else if(i==3)
		{	kans = true;
			breukOnder = true;
		}
		repaint();
	}
	
	public void zetOpties(int k, int[] opties)
	{
		aantalOpties = k;
		for(int i = 0; i < aantalOpties; i++)
			aantal[i] = opties[i];
		for(int i = aantalOpties; i < 6; i++)
			aantal[i] = -1;
		setSize(breedteKansboomveld, hoogteKansboomveld);
		repaint();
	}
	
	public void zetBovenbalkZichtbaar(boolean b)
	{
		bovenbalk = b;
		if(bovenbalk)
    		rijhoogte = theFM.getHeight()+offset;
    	else
    		rijhoogte = 0;
		setSize(breedteKansboomveld, hoogteKansboomveld);
		repaint();
	}

	  public int[] simplify(int nom, int denom)
	    {   if (denom < 0)
	        {   nom = - nom;
	            denom = - denom;
	        }
	        if (nom == 0)
	            denom = 1;
	        else
	        {   int g = gcd(nom, denom);
	            nom = nom / g;
	            denom = denom / g;
	        }
	        int[] breuk = {nom, denom}; 
	        return breuk;
	    }
	  
	  public int gcd(int a, int b)
	  {   int m = Math.abs(a);
		  int n = Math.abs(b);
		  int temp = 0;
		  while ( n != 0 )
		  {   temp = m % n;
		      m = n;
		      n = temp;
		  }
		  return m;
	  }
	  
	  public String breukNaast(int[] breuk)
	  {
		  return breuk[0] +"/" + breuk[1];
	  }
	  
	  public void tekenRechthoekNaast(int a, int p, int q, Graphics gr, int i, int j)
	  {	int b = breedteKolom;
		int h = hoogteKansboom;
		int k = aantalOpties;
		gr.fillRect((2*i+1)*b/2-theFM.stringWidth(breukNaast(simplify(p,q)))/2-offset,
				(int) (h*(4*k*j+k+2*a+1)/(4*Math.pow(k,i+1)))-theFM.getHeight()/2+rijhoogte,
				theFM.stringWidth(breukNaast(simplify(p,q)))+2*offset,theFM.getHeight());
		  	  }
	  
	  public void tekenBreukNaast(int a, int p, int q, Graphics gr, int i, int j)
	  {	int b = breedteKolom;
		int h = hoogteKansboom;
		int k = aantalOpties;
		gr.drawString(breukNaast(simplify(p,q)),(2*i+1)*b/2-theFM.stringWidth(breukNaast(simplify(p,q)))/2, 
					(int) (h*(4*k*j+k+2*a+1)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3+rijhoogte);
	  }
	  
	  public int breukBreedte(int nom, int denom)
	  {		int breukBreedte = Math.max(theFM.stringWidth(""+simplify(nom,denom)[0]),
			  theFM.stringWidth(""+simplify(nom,denom)[1]));
	  		return breukBreedte;
	  }
	  
	  //scheef onder elkaar
	/*
	  public void tekenRechthoekOnder(int a, int p, int q, Graphics gr, int i, int j)
	  {	int b = breedteKolom;
		int h = hoogteKansboom;
		int k = aantalOpties;
		gr.fillRect(i*b + (a * b)/(k+1)-breukBreedte(p,q)/2 - offset,
			(int) (h*(k*(2*j*k+2*j+k)+(1-a)*k+2*a*a-a)/(2*(k+1)*Math.pow(k,i+1)))-theFM.getHeight()+rijhoogte,
			 breukBreedte(p,q) + 2 * offset, 2 * theFM.getHeight() + 1);	
	  }
	  
	  public void tekenBreukOnder(int a, int p, int q, Graphics gr, int i, int j)
	  {	int b = breedteKolom;
		int h = hoogteKansboom;
		int k = aantalOpties;
		  int[] breuk = simplify(p,q);
		  gr.drawString("" + breuk[0], (a * b)/(k+1)+i*b - theFM.stringWidth("" + breuk[0])/2, 
				  (int) (h*(k*(2*j*k+2*j+k)+(1-a)*k+2*a*a-a)/(2*(k+1)*Math.pow(k,i+1))) - theFM.getHeight()/6 +rijhoogte);
		  gr.drawString("" + breuk[1], (a * b)/(k+1)+i*b  - theFM.stringWidth("" + breuk[1])/2, 
				  (int) (h*(k*(2*j*k+2*j+k)+(1-a)*k+2*a*a-a)/(2*(k+1)*Math.pow(k,i+1)))+ 5 * theFM.getHeight()/6 +rijhoogte);
		  gr.drawLine((a * b)/(k+1)+i*b  - breukBreedte(p,q)/2, (int) (h*(k*(2*j*k+2*j+k)+(1-a)*k+2*a*a-a)/(2*(k+1)*Math.pow(k,i+1))) + rijhoogte, 
				  (a * b)/(k+1)+i*b  + breukBreedte(p,q)/2, (int) (h*(k*(2*j*k+2*j+k)+(1-a)*k+2*a*a-a)/(2*(k+1)*Math.pow(k,i+1))) + rijhoogte);
	  }
	  */
	  
	  //recht onder elkaar
	 
	  public void tekenRechthoekOnder(int a, int p, int q, Graphics gr, int i, int j)
	  {	int b = breedteKolom;
		int h = hoogteKansboom;
		int k = aantalOpties;
		gr.fillRect((2*i+1)*b/2 - breukBreedte(p,q)/2 - offset,
			(int) (h*(4*k*j+k+2*a+1)/(4*Math.pow(k,i+1))) - theFM.getHeight() + rijhoogte,
			 breukBreedte(p,q) + 2 * offset, 2 * theFM.getHeight() + 1);	
	  }
	  
	  public void tekenBreukOnder(int a, int p, int q, Graphics gr, int i, int j)
	  {	int b = breedteKolom;
		int h = hoogteKansboom;
		int k = aantalOpties;
		  int[] breuk = simplify(p,q);
		  gr.drawString("" + breuk[0], (2*i+1)*b/2 - theFM.stringWidth("" + breuk[0])/2, 
				  (int) (h*(4*k*j+k+2*a+1)/(4*Math.pow(k,i+1))) - theFM.getHeight()/6 +rijhoogte);
		  gr.drawString("" + breuk[1], (2*i+1)*b/2  - theFM.stringWidth("" + breuk[1])/2, 
				  (int) (h*(4*k*j+k+2*a+1)/(4*Math.pow(k,i+1)))+ 5 * theFM.getHeight()/6 +rijhoogte);
		  gr.drawLine((2*i+1)*b/2  - breukBreedte(p,q)/2, (int) (h*(4*k*j+k+2*a+1)/(4*Math.pow(k,i+1))) + rijhoogte, 
				  (2*i+1)*b/2  + breukBreedte(p,q)/2, (int) (h*(4*k*j+k+2*a+1)/(4*Math.pow(k,i+1))) + rijhoogte);
	  }
	
	 
	  public void tekenEindBreukOnder(int p, int q, Graphics gr, int j)
	  {	int k = aantalOpties;
		int n = aantalKolommen;
		int b = breedteKansboomveld;
		int h = hoogteKansboom;
	  
		  int[] breuk = simplify(p,q);
		  gr.drawString("" + breuk[0], b - breedteKansOnderKolom/2 - theFM.stringWidth("" + breuk[0])/2, 
				  (int) ((2*j+1)*h/(2*Math.pow(k,n))) - theFM.getHeight()/6 +rijhoogte);
		  gr.drawString("" + breuk[1], b - breedteKansOnderKolom/2  - theFM.stringWidth("" + breuk[1])/2, 
				  (int) ((2*j+1)*h/(2*Math.pow(k,n)))+ 5 * theFM.getHeight()/6 +rijhoogte);
		  gr.drawLine(b - breedteKansOnderKolom/2  - breukBreedte(p,q)/2, (int) ((2*j+1)*h/(2*Math.pow(k,n))) + rijhoogte, 
				  b - breedteKansOnderKolom/2  + breukBreedte(p,q)/2, (int) ((2*j+1)*h/(2*Math.pow(k,n))) + rijhoogte);
	  }

	  
}
