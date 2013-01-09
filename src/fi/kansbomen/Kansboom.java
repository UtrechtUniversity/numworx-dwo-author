package fi.kansbomen;

import java.awt.*;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Kansboom extends JPanel

{ 
	static int HOOGTE = 400; 
	static int BREEDTE = 600;
	static int BREEDTEVOLGORDE=60;
	int hoogteKansboomveld;
	int hoogteKansboom;
	int breedteKansboomveld;
	int aantalKolommen;
	int breedteKolom;
	int rijhoogte;
	
	int breedteVolgordekolom, breedteKansNaastKolom, breedteKansOnderKolom;
	boolean volgorde, eindkansNaast, eindkansOnder, bovenbalk;
	int offset = 2;
	
	int aantalOpties;
	int[] teller;
	//int aantal1, aantal2, aantal3, aantal4, aantal5, aantal6;//te vervangen door aantal[]
	int[] aantal;
	boolean terugleggen, kleur, letters, kans, breukOnder;
	String volgordeString;
	String[] letter = {"dummy","b","g","r","c","o","m"};
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
		
    	hoogteKansboomveld=HOOGTE; 
    	breedteKansboomveld=BREEDTE;
    	aantalKolommen=3; 
    	    	
    	aantalOpties=4; 
    	aantal = new int[7];
    	for(int i = 1; i<7; i++)
    	aantal[i]=4; 
    	teller = new int[7];
    	
    	/*
    	aantal2=4; 
    	aantal3=4; 
    	aantal4=4; 
    	*/
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
		boolean bestaanKinderen = teller[1] <= aantal[1] && teller[2] <= aantal[2] && 
					teller[3] <= aantal[3] && teller[4] <= aantal[4] &&
					teller[5] <= aantal[5] && teller[6] <= aantal[6];
		return bestaanKinderen;
	}
	
	private void zetStartTellers()
	{
		for(int p = 1; p < 7; p++)
			if(aantalOpties < p)
				teller[p] = -1; 
			else teller[p] = 0;
	}
	
	private void zetTellers(int i, int j)
	{	int d = 0;
		int h = 0;
		int k = aantalOpties;
		int mod = 0;
		int macht = 0;
		
		zetStartTellers();
		
		
		/*
		teller[1]=0; teller[2]=0; 
		if(aantalOpties<3)
			teller[3]=-1; 
		else teller[3]=0;
		if(aantalOpties<4)
			teller[4]=-1;
		else teller[4]=0;
		*/
		
		for(int s=0; s<i; s++)
		{	d=(int) (j/Math.pow(k,s));
			mod=d%k;
			macht=(int) Math.pow(10,s);
			h=h+macht*mod;
		}
		/*
		for(int q=1; q<i+1; q++)
		{	int p = i-q;
			if(h>=(int) 3*Math.pow(10,p))
			{	teller[4]++;
				h=(int) (h-3*Math.pow(10,p));
			}
			else if(h>=(int) 2*Math.pow(10,p))
			{	teller[3]++;
				h=(int) (h-2*Math.pow(10,p));
			}
			else if(h>=(int) Math.pow(10,p))
			{	teller[2]++;
				h=(int) (h-Math.pow(10,p));
			}
			else teller[1]++;
		}	
		*/
		for(int q=1; q<i+1; q++)
		{	int p = i-q;
			if(h >= (int) 5 * Math.pow(10,p))
			{	teller[6]++;
				h = (int) (h - 5 * Math.pow(10,p));
			}
			else if(h >= (int) 4 * Math.pow(10,p))
			{	teller[5]++;
				h = (int) (h - 4 * Math.pow(10,p));
			}
			else if(h >= (int) 3 * Math.pow(10,p))
			{	teller[4]++;
				h = (int) (h - 3 * Math.pow(10,p));
			}
			else if(h >= (int) 2 * Math.pow(10,p))
			{	teller[3]++;
				h = (int) (h - 2 * Math.pow(10,p));
			}
			else if(h >= (int) Math.pow(10,p))
			{	teller[2]++;
				h = (int) (h - Math.pow(10,p));
			}
			else teller[1]++;
		}	
	}
	

	private void tekenKinderen(int i, int j, int[] teller, Graphics gr)
	{
		int b = breedteKolom;
		int h = hoogteKansboom;
		int k = aantalOpties;
		
		Color[] kleurRij = new Color[7];
		if(kleur) 
		{	kleurRij[1] = new Color(0,0,255);
			kleurRij[2] = new Color(0,200,0);
			kleurRij[3] = new Color(255,50,50);
			kleurRij[4] = new Color(0,220,220);
			kleurRij[5] = new Color(220,220,0);
			kleurRij[6] = new Color(220,0,220);
		}
		else
		{	for(int p = 1; p<7; p++)
			kleurRij[p]= Color.BLACK;
			/*
			kleur2= Color.BLACK;
			kleur3= Color.BLACK;
			kleur4= Color.BLACK;
			*/
		}
		
		if(bestaanKinderen(aantal, teller))
		{ 	for(int p = 1; p < 7; p++)
			if(teller[7-p]<aantal[7-p])
			{	gr.setColor(kleurRij[7-p]);
				gr.drawLine(i*b, (int) ((2*j+1)/(Math.pow(k,i)*2)*h)+rijhoogte, (i+1)*b, (int) ((2*k*j+13-2*p)/(Math.pow(k,i+1)*2)*h)+rijhoogte);
			}
			
		/*
			if(teller[4]<aantal[4])
			{	gr.setColor(kleurRij[4]);
				gr.drawLine(i*b, (int) ((2*j+1)/(Math.pow(k,i)*2)*h)+rijhoogte, (i+1)*b, (int) ((2*k*j+7)/(Math.pow(k,i+1)*2)*h)+rijhoogte);
			}
			if(teller[3]<aantal[3])
			{	gr.setColor(kleurRij[3]);
				gr.drawLine(i*b, (int) ((2*j+1)/(Math.pow(k,i)*2)*h)+rijhoogte, (i+1)*b, (int) ((2*k*j+5)/(Math.pow(k,i+1)*2)*h)+rijhoogte);
			}
			if(teller[2]<aantal[2])
			{	gr.setColor(kleurRij[2]);
				gr.drawLine(i*b, (int) ((2*j+1)/(Math.pow(k,i)*2)*h)+rijhoogte, (i+1)*b, (int) ((2*k*j+3)/(Math.pow(k,i+1)*2)*h)+rijhoogte);
			}
			if(teller[1]<aantal[1])
			{	gr.setColor(kleurRij[1]);
				gr.drawLine(i*b, (int) ((2*j+1)/(Math.pow(k,i)*2)*h)+rijhoogte, (i+1)*b, (int) ((2*k*j+1)/(Math.pow(k,i+1)*2)*h)+rijhoogte);
			}
		*/	
		}
	}
	
	
	private void tekenLetters(Graphics gr)
	{
		int b = breedteKolom;
		int h = hoogteKansboom;
		int k = aantalOpties;
		
		gr.setColor(backgroundColor);
		for(int i=1; i<aantalKolommen+1; i++)
		{
			gr.fillRect((2*i-1)*b/2-theFM.charWidth('a')/2-offset, rijhoogte, theFM.charWidth('a')+2*offset,h-rijhoogte);
		}
	
		gr.setColor(Color.BLACK);
		for(int i=0; i<aantalKolommen; i++)
		{	zetStartTellers();
			for(int j=0; j<Math.pow(k,i); j++)
			{	if(!terugleggen)
					this.zetTellers(i,j);	
				if(bestaanKinderen(aantal,teller))
				{ 	for(int p = 1; p < k+1; p++)
					if(teller[7-p]<aantal[7-p])
						gr.drawString(letter[7-p],(2*i+1)*b/2-theFM.charWidth('a')/2, 
								(int) (h*(4*k*j+k+13-2*p)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3+rijhoogte);
					/*	
					if(teller[4]<aantal[4])
						gr.drawString(letter[4],(2*i+1)*b/2-theFM.charWidth('a')/2, 
							(int) (h*(4*k*j+k+7)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3+rijhoogte);
					if(teller[3]<aantal[3])
						gr.drawString(letter[3],(2*i+1)*b/2-theFM.charWidth('a')/2, 
							(int) (h*(4*k*j+k+5)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3+rijhoogte);
					if(teller[2]<aantal[2])
						gr.drawString(letter[2],(2*i+1)*b/2-theFM.charWidth('a')/2, 
							(int) (h*(4*k*j+k+3)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3+rijhoogte);
					if(teller[1]<aantal[1])
						gr.drawString(letter[1],(2*i+1)*b/2-theFM.charWidth('a')/2, 
							(int) (h*(4*k*j+k+1)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3+rijhoogte);
					*/
				}		
			}
		}	
	}
	
	private void tekenKansen(Graphics gr)
	{
		int k = aantalOpties;
		int a = aantal[1] + aantal[2] + aantal[3] + aantal[4];
		for(int p = 1; p < 7; p++)
			if(aantalOpties<p)
				a -= aantal[p];
		/*
		if(aantalOpties<3)
			a -= aantal[3];
		if(aantalOpties<4)
			a -= aantal[4];
			*/
		
		for(int i=0; i<aantalKolommen; i++)
		{	zetStartTellers();
			for(int j=0; j<Math.pow(k,i); j++)
			{if(terugleggen)
				{
				gr.setColor(backgroundColor);
				
				if(breukOnder)
					{
					for(int p = 1; p < 7; p++)
						if(teller[p] > -1)
							tekenRechthoekOnder(p, aantal[p], a, gr, i, j);
					/*
					tekenRechthoekOnder(1, aantal[1], a, gr, i, j);
					tekenRechthoekOnder(2, aantal[2], a, gr, i, j);
					if(teller[3]>-1)
						tekenRechthoekOnder(3, aantal[3], a, gr, i, j);
					if(teller[4]>-1)
						tekenRechthoekOnder(4, aantal[4], a, gr, i, j);
						*/
					}
				else 
					for(int p = 1; p < 7; p++)
						if(teller[p] > -1)
							tekenRechthoekNaast(p, aantal[p], a, gr, i, j);
					/*	
				{
					tekenRechthoekNaast(1, aantal[1], a, gr, i, j);
					tekenRechthoekNaast(2, aantal[2], a, gr, i, j);
					if(teller[3]>-1)
						tekenRechthoekNaast(3, aantal[3], a, gr, i, j);
					if(teller[4]>-1)
						tekenRechthoekNaast(4, aantal[4], a, gr, i, j);
					}
					*/
				gr.setColor(Color.BLACK);
				if(breukOnder)
					for(int p = 1; p < 7; p++)
					{	if(teller[p] > -1)
							tekenBreukOnder(p, aantal[p], a, gr, i, j);	
					}
				/*
				{
					tekenBreukOnder(1, aantal[1], a, gr, i, j);
					tekenBreukOnder(2, aantal[2], a, gr, i, j);
					if(teller[3]>-1)
						tekenBreukOnder(3, aantal[3], a, gr, i, j);
					if(teller[4]>-1)
						tekenBreukOnder(4, aantal[4], a, gr, i, j);
					}
					*/
				else 
					for(int p = 1; p < 7; p++)
						if(teller[p] > -1)
							tekenBreukNaast(p, aantal[p], a, gr, i, j);
					/*
					{
					tekenBreukNaast(1, aantal[1], a, gr, i, j);
					tekenBreukNaast(2, aantal[2], a, gr, i, j);
					if(teller[3]>-1)
					tekenBreukNaast(3, aantal[3], a, gr, i, j);
					if(teller[4]>-1)
					tekenBreukNaast(4, aantal[4], a, gr, i, j);
					}
					*/
				}
			else //zonder terugleggen
				{	
				this.zetTellers(i,j);	
				gr.setColor(backgroundColor);
				if(bestaanKinderen(aantal,teller))
				{ 	for(int p = 1; p < 5; p++)
					if(teller[5-p]<aantal[5-p])
						if(breukOnder)
						tekenRechthoekOnder(5-p, aantal[5-p]-teller[5-p], a-i, gr, i, j);
						else	
						tekenRechthoekNaast(5-p, aantal[5-p]-teller[5-p], a-i, gr, i, j);
					/*
					if(teller[4]<aantal[4])
						if(breukOnder)
						tekenRechthoekOnder(4, aantal[4]-teller[4], a-i, gr, i, j);
						else	
						tekenRechthoekNaast(4, aantal[4]-teller[4], a-i, gr, i, j);
					if(teller[3]<aantal[3])
						if(breukOnder)
						tekenRechthoekOnder(3, aantal[3]-teller[3], a-i, gr, i, j);
						else
						tekenRechthoekNaast(3, aantal[3]-teller[3], a-i, gr, i, j);
					if(teller[2]<aantal[2])
						if(breukOnder)
						tekenRechthoekOnder(2, aantal[2]-teller[2], a-i, gr, i, j);
						else
						tekenRechthoekNaast(2, aantal[2]-teller[2], a-i, gr, i, j);
					if(teller[1]<aantal[1])
						if(breukOnder)
						tekenRechthoekOnder(1, aantal[1]-teller[1], a-i, gr, i, j);
						else
						tekenRechthoekNaast(1, aantal[1]-teller[1], a-i, gr, i, j);
						*/
					}	
				gr.setColor(Color.BLACK);
					if(bestaanKinderen(aantal,teller))
					{ 
						for(int p = 1; p < 5; p++)
							if(teller[5-p]<aantal[5-p])	
								if(breukOnder)
								tekenBreukOnder(5-p, aantal[5-p]-teller[5-p], a-i, gr, i, j);
								else
								tekenBreukNaast(5-p, aantal[5-p]-teller[5-p], a-i, gr, i, j);
					/*
						if(t4<aantal[4])	
						if(breukOnder)
						tekenBreukOnder(4, aantal[4]-t4, a-i, gr, i, j);
						else
						tekenBreukNaast(4, aantal[4]-t4, a-i, gr, i, j);
					if(t3<aantal[3])
						if(breukOnder)
						tekenBreukOnder(3, aantal[3]-t3, a-i, gr, i, j);
						else
						tekenBreukNaast(3, aantal[3]-t3, a-i, gr, i, j);
					if(t2<aantal2)
						if(breukOnder)
						tekenBreukOnder(2, aantal2-t2, a-i, gr, i, j);
						else
						tekenBreukNaast(2, aantal2-t2, a-i, gr, i, j);
					if(t1<aantal1)
						if(breukOnder)
						tekenBreukOnder(1, aantal1-t1, a-i, gr, i, j);
						else
						tekenBreukNaast(1, aantal1-t1, a-i, gr, i, j);
						*/
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
		int positie;
		for(int j=1; j<Math.pow(k,n)+1; j++)
		{	volgordeString = "";
			positie=j-1;
			for(int i=1; i<n+1; i++)
				if(positie<Math.pow(k,n-i))
					volgordeString = volgordeString + letter[1];
				else if(positie<2*Math.pow(k,n-i))
				{
					volgordeString = volgordeString + letter[2];
					positie -= Math.pow(k,n-i);
				}
				else if(positie<3*Math.pow(k,n-i))
				{
					volgordeString = volgordeString + letter[3];
					positie -= 2*Math.pow(k,n-i);
				}
				else if(positie<4*Math.pow(k,n-i))
				{	
					volgordeString = volgordeString + letter[4];
					positie -= 3*Math.pow(k,n-i);
				}
				else if(positie<5*Math.pow(k,n-i))
				{	
					volgordeString = volgordeString + letter[5];
					positie -= 4*Math.pow(k,n-i);
				}
				else if(positie<6*Math.pow(k,n-i))
				{	
					volgordeString = volgordeString + letter[6];
					positie -= 5*Math.pow(k,n-i);
				}
			zetTellers(n,j-1);
			if(terugleggen)
				gr.drawString(volgordeString, b - breedteVolgordekolom + offset, 
						(int) ((2*j-1)*h/(2*Math.pow(k,n)))+theFM.getHeight()/3+rijhoogte);
			else if(bestaanKinderen(aantal,teller))
				gr.drawString(volgordeString, b - breedteVolgordekolom + offset, 
						(int) ((2*j-1)*h/(2*Math.pow(k,n)))+theFM.getHeight()/3+rijhoogte);
		}
		
	}
	
	public int[] eindkansMetTerug()
	{
		int n = aantalKolommen;
		int a = aantal[1] + aantal[2] + aantal[3] + aantal[4] + aantal[5] + aantal[6];
		for(int p = 1; p < 7; p++)
			if(aantalOpties<p)
			a -= aantal[p];
		int totaalTeller;
		
		totaalTeller = (int) (Math.pow(aantal[1], teller[1])*Math.pow(aantal[2], teller[2])*
				Math.pow(aantal[3], Math.max(teller[3], 0))*Math.pow(aantal[4], Math.max(teller[4],0))*
				Math.pow(aantal[5], Math.max(teller[5], 0))*Math.pow(aantal[6], Math.max(teller[6],0)));
		int[] breuk = simplify(totaalTeller,(int) Math.pow(a, n));
		return breuk;
		
	}
	
	public int[] eindkansZonderTerug()
	{
		int n = aantalKolommen;
		int[] s = new int[7];
		int ss;
		int a = aantal[1] + aantal[2] + aantal[3] + aantal[4] + aantal[5] + aantal[6];
		
		for(int p = 1; p < 7; p++)
			s[p] = 1;
			ss = 1;
			for(int p = 1; p < 7; p++)
				for(int i = 0; i < teller[p]; i++)
					s[p] *= aantal[p] - i;
			for(int i=0; i < n; i++)	
				ss *= a-i;
			
		int[] breuk = simplify(s[1]*s[2]*s[3]*s[4]*s[5]*s[6],ss);
		return breuk;
	}
	
	public void tekenEindkansenNaast(Graphics gr)
	{	int k = aantalOpties;
		int n = aantalKolommen;
		int b = breedteKansboomveld;
		int h = hoogteKansboom;
		
		for(int j=1; j<Math.pow(k,n)+1; j++)
		{	zetTellers(n,j-1);
			if(terugleggen)
			{
				gr.drawString(breukNaast(eindkansMetTerug()), b - breedteKansNaastKolom + offset, 
						(int) ((2*j-1)*h/(2*Math.pow(k,n)))+theFM.getHeight()/3+rijhoogte);
			}
			else if(bestaanKinderen(aantal,teller))
			{	
				gr.drawString(breukNaast(eindkansZonderTerug()), b - breedteKansNaastKolom + offset, 
						(int) ((2*j-1)*h/(2*Math.pow(k,n)))+theFM.getHeight()/3+rijhoogte);
			}
		}
	}

	
	public void tekenEindkansenOnder(Graphics gr)
	{	int k = aantalOpties;
		int n = aantalKolommen;
		
		for(int j=1; j<Math.pow(k,n)+1; j++)
		{	zetTellers(n,j-1);
			if(terugleggen)
			{	
				tekenEindBreukOnder(eindkansMetTerug()[0], eindkansMetTerug()[1], gr, j);
			}
			else if(teller[1] <= aantal[1] && teller[2] <= aantal[2] && teller[3] <= aantal[3] && teller[4] <= aantal[4])
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
		
		for(int j=1; j<Math.pow(k,n)+1; j++)
		{	zetTellers(n,j-1);
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
		
		for(int j=1; j<Math.pow(k,n)+1; j++)
		{	zetTellers(n,j-1);
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
		int positie;
		for(int j=1; j<Math.pow(k,n)+1; j++)
		{	volgordeString = "";
			positie=j-1;
			for(int i=1; i<n+1; i++)
				if(positie<Math.pow(k,n-i))
					volgordeString = volgordeString + letter[1];
				else if(positie<2*Math.pow(k,n-i))
				{
					volgordeString = volgordeString + letter[2];
					positie -= Math.pow(k,n-i);
				}
				else if(positie<3*Math.pow(k,n-i))
				{
					volgordeString = volgordeString + letter[3];
					positie -= 2*Math.pow(k,n-i);
				}
				else if(positie<4*Math.pow(k,n-i))
				{	
					volgordeString = volgordeString + letter[4];
					positie -= 3*Math.pow(k,n-i);
				}
				else if(positie<5*Math.pow(k,n-i))
				{	
					volgordeString = volgordeString + letter[5];
					positie -= 4*Math.pow(k,n-i);
				}
				else if(positie<6*Math.pow(k,n-i))
				{	
					volgordeString = volgordeString + letter[6];
					positie -= 5*Math.pow(k,n-i);
				}
			
			m = Math.max(theFM.stringWidth(volgordeString),m);
		}
		
		return m + 2 * offset;
	}
	
	public void setSize(int b, int h)
	{	//hier aanpassen voor bovenbalk.
	    breedteKansboomveld = b;
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
		if(i==0)
		{	volgorde = false;
			eindkansNaast = false;
			eindkansOnder = false;
		}
		else if(i==1)
		{	volgorde = true;
			eindkansNaast = false;
			eindkansOnder = false;
		}
		else if(i==2)
		{	volgorde = false; 
			eindkansNaast = true;
			eindkansOnder = false;
		}
		else if(i==3)
		{	volgorde = false; 
			eindkansNaast = false;
			eindkansOnder = true;
		}
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
		if(i==0)
		{	letters = false;
			kans = false;
			breukOnder = false;
		}
		else if(i==1)
		{	letters =true;
			kans = false;
			breukOnder = false;
		}
		else if(i==2)
		{	letters = false;
			kans = true;
			breukOnder = false;
		}
		else if(i==3)
		{	letters = false;
			kans = true;
			breukOnder = true;
		}
		repaint();
	}
	
	public void zetAantalOpties(int i, int w3, int w4, int w5, int w6)
	{
		aantalOpties = i;
		if(i < 6)
			aantal[6] = -1;
		else if(aantal[6] == -1)
			aantal[6] = w6;
		if(i < 5)
			aantal[5] = -1;
		else if(aantal[5] == -1)
			aantal[5] = w5;
		if(i < 4)
			aantal[4] = -1;
		else if(aantal[4] == -1)
			aantal[4] = w4;
		if(i < 3)
			aantal[3] = -1;
		else if(aantal[3] == -1)
			aantal[3] = w3;
		setSize(breedteKansboomveld, hoogteKansboomveld);
		repaint();
	}
	
	public void zetAantalVanOptie(int i, int j)
	{
		if(i == 1)
			aantal[1] = j;
		else if(i == 2)
			aantal[2] = j;
		else if(i == 3)
			aantal[3] = j;
		else if(i == 4)
			aantal[4] = j;
		else if(i == 5)
			aantal[5] = j;
		else if(i == 6)
			aantal[6] = j;
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
	    { //Deze methode vereenvoudigt breuken.
		  
		  // positive denominator
	        if (denom < 0)
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
				(int) (h*(4*k*j+k+2*a-1)/(4*Math.pow(k,i+1)))-theFM.getHeight()/2+rijhoogte,
				theFM.stringWidth(breukNaast(simplify(p,q)))+2*offset,theFM.getHeight());
		  	  }
	  
	  public void tekenBreukNaast(int a, int p, int q, Graphics gr, int i, int j)
	  {	int b = breedteKolom;
		int h = hoogteKansboom;
		int k = aantalOpties;
		gr.drawString(breukNaast(simplify(p,q)),(2*i+1)*b/2-theFM.stringWidth(breukNaast(simplify(p,q)))/2, 
					(int) (h*(4*k*j+k+2*a-1)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3+rijhoogte);
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
			(int) (h*(4*k*j+k+2*a-1)/(4*Math.pow(k,i+1))) - theFM.getHeight() + rijhoogte,
			 breukBreedte(p,q) + 2 * offset, 2 * theFM.getHeight() + 1);	
	  }
	  
	  public void tekenBreukOnder(int a, int p, int q, Graphics gr, int i, int j)
	  {	int b = breedteKolom;
		int h = hoogteKansboom;
		int k = aantalOpties;
		  int[] breuk = simplify(p,q);
		  gr.drawString("" + breuk[0], (2*i+1)*b/2 - theFM.stringWidth("" + breuk[0])/2, 
				  (int) (h*(4*k*j+k+2*a-1)/(4*Math.pow(k,i+1))) - theFM.getHeight()/6 +rijhoogte);
		  gr.drawString("" + breuk[1], (2*i+1)*b/2  - theFM.stringWidth("" + breuk[1])/2, 
				  (int) (h*(4*k*j+k+2*a-1)/(4*Math.pow(k,i+1)))+ 5 * theFM.getHeight()/6 +rijhoogte);
		  gr.drawLine((2*i+1)*b/2  - breukBreedte(p,q)/2, (int) (h*(4*k*j+k+2*a-1)/(4*Math.pow(k,i+1))) + rijhoogte, 
				  (2*i+1)*b/2  + breukBreedte(p,q)/2, (int) (h*(4*k*j+k+2*a-1)/(4*Math.pow(k,i+1))) + rijhoogte);
	  }
	 
	  public void tekenEindBreukOnder(int p, int q, Graphics gr, int j)
	  {	int k = aantalOpties;
		int n = aantalKolommen;
		int b = breedteKansboomveld;
		int h = hoogteKansboom;
	  
		  int[] breuk = simplify(p,q);
		  gr.drawString("" + breuk[0], b - breedteKansOnderKolom/2 - theFM.stringWidth("" + breuk[0])/2, 
				  (int) ((2*j-1)*h/(2*Math.pow(k,n))) - theFM.getHeight()/6 +rijhoogte);
		  gr.drawString("" + breuk[1], b - breedteKansOnderKolom/2  - theFM.stringWidth("" + breuk[1])/2, 
				  (int) ((2*j-1)*h/(2*Math.pow(k,n)))+ 5 * theFM.getHeight()/6 +rijhoogte);
		  gr.drawLine(b - breedteKansOnderKolom/2  - breukBreedte(p,q)/2, (int) ((2*j-1)*h/(2*Math.pow(k,n))) + rijhoogte, 
				  b - breedteKansOnderKolom/2  + breukBreedte(p,q)/2, (int) ((2*j-1)*h/(2*Math.pow(k,n))) + rijhoogte);
	  }

	  
}
