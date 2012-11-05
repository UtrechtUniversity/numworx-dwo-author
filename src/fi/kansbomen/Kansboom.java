package fi.kansbomen;

import java.awt.*;

import javax.swing.JPanel;

public class Kansboom extends JPanel

{ 
	static int HOOGTE = 400; 
	static int BREEDTE = 600;
	static int BREEDTEVOLGORDE=60;
	int hoogteKansboomveld;
	int breedteKansboomveld;
	int aantalKolommen;
	int breedteKolom;
	
	int breedteVolgordekolom, breedteKanskolom;
	boolean volgorde, eindkans;
	int offset = 2;
	
	int aantalKeuzes;
	int t1, t2, t3, t4;
	int aantal1, aantal2, aantal3, aantal4;
	boolean terugleggen, kleur, letter, kans;
	String volgordeString;
	
	String letter1 = "b";
	String letter2 = "g";
	String letter3 = "r";
	String letter4 = "c";
	
	Color backgroundColor = Color.white;
	
	Font theFont;
	FontMetrics theFM;
	//Font theBoldFont;
	//FontMetrics theBoldFM;
	
	public Kansboom()
	{
	    
		//Alles wat instelbaar is, moet voor de docent instelbaar zijn. 
		//Bovendien moet de docent kunnen aangeven dat het voor de leerling instelbaar is.
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		
    	hoogteKansboomveld=HOOGTE; 
    	breedteKansboomveld=BREEDTE; 
    	aantalKolommen=3; //Instelbaar als aantal keuzemomenten (of aantal keer trekken)
    	    	
    	aantalKeuzes=4; //Instelbaar. Opties: 2, 3 of 4
    	aantal1=4; //Instelbaar. Van 1 tot 10 bijvoorbeeld.
    	aantal2=4; //Instelbaar. Van 1 tot 10 bijvoorbeeld.
    	aantal3=4; //Instelbaar. Van 1 tot 10 bijvoorbeeld. Moet standaard -1 zijn als aantalKeuzes < 3, en niet zichtbaar voor docent of leerling.
    	aantal4=4; //Instelbaar. Van 1 tot 10 bijvoorbeeld. Moet standaard -1 zijn als aantalKeuzes < 4, en niet zichtbaar voor docent of leerling.
    	
    	volgorde = false;
    	eindkans = true;
    	breedteVolgordekolom=aantalKolommen*theFM.charWidth('a') + 2* offset;
    	if(volgorde || eindkans)
    		breedteKolom=(breedteKansboomveld-breedteVolgordekolom)/aantalKolommen;
    	else 
    		breedteKolom=breedteKansboomveld/aantalKolommen;
    	//setSize(breedteKansboomveld, hoogteKansboomveld);
    	
    	terugleggen = true; //Instelbaar door middel van aanvinkvakje; true is met terugleggen, false is zonder. 
    	kleur = true; //Instelbaar door middel van aanvinkvakje; bij true heeft elke keuzemogelijkheid zijn eigen kleur.
    	letter = false;
    	kans = false;
	}
	
	
	public void paint(Graphics g)
	{ 	g.setColor(backgroundColor);
		g.fillRect(0, 0, getSize().width, getSize().height);
	    
		this.tekenKansboom(g);
		
	}
	
	private void tekenKansboom(Graphics gr)
	{ 	
		int k = aantalKeuzes;
		Graphics2D gr2 = (Graphics2D) gr;
		gr2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		        RenderingHints.VALUE_ANTIALIAS_ON);	
		for(int i=0; i<aantalKolommen; i++)
		{	t1=0; t2=0; 
			if(aantalKeuzes<3)
				t3=-1; 
			else t3=0;
			if(aantalKeuzes<4)
				t4=-1;
			else t4=0;
			for(int j=0; j<Math.pow(k,i); j++)
			{	if(!terugleggen)
					this.zetTellers(i,j);	
				this.tekenKinderen(i, j, t1, t2, t3, t4, gr);	
			}
		}
		gr.setColor(Color.BLACK);
		gr.setFont(theFont);
		if(volgorde)
			this.tekenVolgordeStrings(gr);
		else if(eindkans)
			this.tekenEindkansen(gr);
		if(letter)
			this.tekenLetters(gr);
		else if(kans)
			this.tekenKansen(gr);
	}
	
	private void zetTellers(int i, int j)
	{	int d = 0;
		int h = 0;
		int k = aantalKeuzes;
		int mod = 0;
		int macht = 0;
		
		t1=0; t2=0; 
		if(aantalKeuzes<3)
			t3=-1; 
		else t3=0;
		if(aantalKeuzes<4)
			t4=-1;
		else t4=0;
		
		for(int s=0; s<i; s++)
		{	d=(int) (j/Math.pow(k,s));
			mod=d%k;
			macht=(int) Math.pow(10,s);
			h=h+macht*mod;
		}
		for(int q=1; q<i+1; q++)
		{	int p = i-q;
			if(h>=(int) 3*Math.pow(10,p))
			{	t4++;
				h=(int) (h-3*Math.pow(10,p));
			}
			else if(h>=(int) 2*Math.pow(10,p))
			{	t3++;
				h=(int) (h-2*Math.pow(10,p));
			}
			else if(h>=(int) Math.pow(10,p))
			{	t2++;
				h=(int) (h-Math.pow(10,p));
			}
			else t1++;
		}	
	}
	

	private void tekenKinderen(int i, int j, int t1, int t2, int t3, int t4, Graphics gr)
	{
		int b = breedteKolom;
		int h = hoogteKansboomveld;
		int k = aantalKeuzes;
		
		Color kleur1, kleur2, kleur3, kleur4;
		if(kleur) 
		{	kleur1= new Color(0,0,255);
			kleur2= new Color(0,200,0);
			kleur3= new Color(255,50,50);
			kleur4= new Color(0,220,220);
		}
		else
		{	kleur1= Color.BLACK;
			kleur2= Color.BLACK;
			kleur3= Color.BLACK;
			kleur4= Color.BLACK;
		}
		
		if(t1 <= aantal1 && t2 <= aantal2 && t3 <= aantal3 && t4 <= aantal4)
		{ 	if(t4<aantal4)
			{	gr.setColor(kleur4);
				gr.drawLine(i*b, (int) ((2*j+1)/(Math.pow(k,i)*2)*h), (i+1)*b, (int) ((2*k*j+7)/(Math.pow(k,i+1)*2)*h));
				if(letter)
				{	gr.setColor(backgroundColor);
					gr.fillRect((2*i+1)*b/2-theFM.charWidth('a')/2-offset, (int) (h*(4*k*j+k+7)/(4*Math.pow(k,i+1)))-theFM.getHeight()/2, theFM.charWidth('a')+2*offset, theFM.getHeight());
					gr.setColor(Color.BLACK);
					gr.drawString("d",(2*i+1)*b/2-theFM.charWidth('a')/2, (int) (h*(4*k*j+k+7)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3);
				}
			}
			if(t3<aantal3)
			{	gr.setColor(kleur3);
				gr.drawLine(i*b, (int) ((2*j+1)/(Math.pow(k,i)*2)*h), (i+1)*b, (int) ((2*k*j+5)/(Math.pow(k,i+1)*2)*h));
				if(letter)
				{	gr.setColor(backgroundColor);
					gr.fillRect((2*i+1)*b/2-theFM.charWidth('a')/2-offset, (int) (h*(4*k*j+k+5)/(4*Math.pow(k,i+1)))-theFM.getHeight()/2, theFM.charWidth('a')+2*offset, theFM.getHeight());
					gr.setColor(Color.BLACK);
					gr.drawString("c",(2*i+1)*b/2-theFM.charWidth('a')/2, (int) (h*(4*k*j+k+5)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3);
				}
			}
			if(t2<aantal2)
			{	gr.setColor(kleur2);
				gr.drawLine(i*b, (int) ((2*j+1)/(Math.pow(k,i)*2)*h), (i+1)*b, (int) ((2*k*j+3)/(Math.pow(k,i+1)*2)*h));
				if(letter)
				{	gr.setColor(backgroundColor);
					gr.fillRect((2*i+1)*b/2-theFM.charWidth('a')/2-offset, (int) (h*(4*k*j+k+3)/(4*Math.pow(k,i+1)))-theFM.getHeight()/2, theFM.charWidth('a')+2*offset, theFM.getHeight());
					gr.setColor(Color.BLACK);
					gr.drawString("b",(2*i+1)*b/2-theFM.charWidth('a')/2, (int) (h*(4*k*j+k+3)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3);
				}
			}
			if(t1<aantal1)
			{	gr.setColor(kleur1);
				gr.drawLine(i*b, (int) ((2*j+1)/(Math.pow(k,i)*2)*h), (i+1)*b, (int) ((2*k*j+1)/(Math.pow(k,i+1)*2)*h));
				if(letter)
				{	gr.setColor(backgroundColor);
					gr.fillRect((2*i+1)*b/2-theFM.charWidth('a')/2-offset, (int) (h*(4*k*j+k+1)/(4*Math.pow(k,i+1)))-theFM.getHeight()/2, theFM.charWidth('a')+2*offset, theFM.getHeight());
					gr.setColor(Color.BLACK);
					gr.drawString("a",(2*i+1)*b/2-theFM.charWidth('a')/2, (int) (h*(4*k*j+k+1)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3);
				}
			}
			
		}
	}
	
	
	private void tekenLetters(Graphics gr)
	{
		int b = breedteKolom;
		int h = hoogteKansboomveld;
		int k = aantalKeuzes;
		
		gr.setColor(backgroundColor);
		for(int i=1; i<k; i++)
		{
			gr.fillRect((2*i-1)*b/2-theFM.charWidth('a')/2-offset, 0, theFM.charWidth('a')+2*offset,h);
		}
	
		gr.setColor(Color.BLACK);
		for(int i=0; i<aantalKolommen; i++)
		{	t1=0; t2=0; 
			if(aantalKeuzes<3)
				t3=-1; 
			else t3=0;
			if(aantalKeuzes<4)
				t4=-1;
			else t4=0;
			for(int j=0; j<Math.pow(k,i); j++)
			{	if(!terugleggen)
					this.zetTellers(i,j);	
				if(t1 <= aantal1 && t2 <= aantal2 && t3 <= aantal3 && t4 <= aantal4)
				{ 	if(t4<aantal4)
						gr.drawString(letter4,(2*i+1)*b/2-theFM.charWidth('a')/2, (int) (h*(4*k*j+k+7)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3);
					if(t3<aantal3)
						gr.drawString(letter3,(2*i+1)*b/2-theFM.charWidth('a')/2, (int) (h*(4*k*j+k+5)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3);
					if(t2<aantal2)
						gr.drawString(letter2,(2*i+1)*b/2-theFM.charWidth('a')/2, (int) (h*(4*k*j+k+3)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3);
					if(t1<aantal1)
						gr.drawString(letter1,(2*i+1)*b/2-theFM.charWidth('a')/2, (int) (h*(4*k*j+k+1)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3);
				}		
			}
		}
		
	}
	
	
	private void tekenKansen(Graphics gr)
	{
		int b = breedteKolom;
		int h = hoogteKansboomveld;
		int k = aantalKeuzes;
		int a = aantal1 + aantal2 + aantal3 + aantal4;
		if(aantalKeuzes<3)
			a -= aantal3;
		if(aantalKeuzes<4)
			a -= aantal4;
		
		for(int i=0; i<aantalKolommen; i++)
		{	t1=0; t2=0; 
			if(aantalKeuzes<3)
				t3=-1;
			else t3=0;
			if(aantalKeuzes<4)
				t4=-1;
			else t4=0;
			for(int j=0; j<Math.pow(k,i); j++)
			{	if(terugleggen)
				{
				gr.setColor(backgroundColor);
				gr.fillRect((2*i+1)*b/2-theFM.stringWidth(aantal1+"/"+a)/2-offset,(int) (h*(4*k*j+k+1)/(4*Math.pow(k,i+1)))-theFM.getHeight()/2,theFM.stringWidth(aantal1+"/"+a)+2*offset,theFM.getHeight());
				gr.fillRect((2*i+1)*b/2-theFM.stringWidth(aantal2+"/"+a)/2-offset,(int) (h*(4*k*j+k+3)/(4*Math.pow(k,i+1)))-theFM.getHeight()/2,theFM.stringWidth(aantal2+"/"+a)+2*offset,theFM.getHeight());
				if(t3>-1)
				gr.fillRect((2*i+1)*b/2-theFM.stringWidth(aantal3+"/"+a)/2-offset,(int) (h*(4*k*j+k+5)/(4*Math.pow(k,i+1)))-theFM.getHeight()/2,theFM.stringWidth(aantal3+"/"+a)+2*offset,theFM.getHeight());
				if(t4>-1)
				gr.fillRect((2*i+1)*b/2-theFM.stringWidth(aantal4+"/"+a)/2-offset,(int) (h*(4*k*j+k+7)/(4*Math.pow(k,i+1)))-theFM.getHeight()/2,theFM.stringWidth(aantal4+"/"+a)+2*offset,theFM.getHeight());
				
				gr.setColor(Color.BLACK);
				gr.drawString(aantal1+"/"+a, (2*i+1)*b/2-theFM.stringWidth(aantal1+"/"+a)/2, (int) (h*(4*k*j+k+1)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3);
				gr.drawString(aantal2+"/"+a, (2*i+1)*b/2-theFM.stringWidth(aantal2+"/"+a)/2, (int) (h*(4*k*j+k+3)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3);
				if(t3>-1)
				gr.drawString(aantal3+"/"+a, (2*i+1)*b/2-theFM.stringWidth(aantal3+"/"+a)/2, (int) (h*(4*k*j+k+5)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3);
				if(t4>-1)
				gr.drawString(aantal4+"/"+a, (2*i+1)*b/2-theFM.stringWidth(aantal4+"/"+a)/2, (int) (h*(4*k*j+k+7)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3);
			//Hier wil ik graag vereenvoudigde breuken van maken, die ook liefst onder elkaar weergegeven worden
			//(al is dit ruimtetechnisch misschien makkelijker, maar dan moet de breedte van de strook
			//zich wel aanpassen aan de breedste en niet aan de eerste.
				}
				else
				{	this.zetTellers(i,j);	
					gr.setColor(backgroundColor);
					if(t1 <= aantal1 && t2 <= aantal2 && t3 <= aantal3 && t4 <= aantal4)
					{ 	if(t4<aantal4)
							gr.fillRect((2*i+1)*b/2-theFM.stringWidth(aantal4-t4+"/"+(a-i))/2-offset,(int) (h*(4*k*j+k+7)/(4*Math.pow(k,i+1)))-theFM.getHeight()/2,theFM.stringWidth(aantal4-t4+"/"+(a-i))+2*offset,theFM.getHeight());
						if(t3<aantal3)
							gr.fillRect((2*i+1)*b/2-theFM.stringWidth(aantal3-t3+"/"+(a-i))/2-offset,(int) (h*(4*k*j+k+5)/(4*Math.pow(k,i+1)))-theFM.getHeight()/2,theFM.stringWidth(aantal3-t3+"/"+(a-i))+2*offset,theFM.getHeight());
						if(t2<aantal2)
							gr.fillRect((2*i+1)*b/2-theFM.stringWidth(aantal2-t2+"/"+(a-i))/2-offset,(int) (h*(4*k*j+k+3)/(4*Math.pow(k,i+1)))-theFM.getHeight()/2,theFM.stringWidth(aantal2-t2+"/"+(a-i))+2*offset,theFM.getHeight());
						if(t1<aantal1)
							gr.fillRect((2*i+1)*b/2-theFM.stringWidth(aantal1-t1+"/"+(a-i))/2-offset,(int) (h*(4*k*j+k+1)/(4*Math.pow(k,i+1)))-theFM.getHeight()/2,theFM.stringWidth(aantal1-t1+"/"+(a-i))+2*offset,theFM.getHeight());
					}	
					gr.setColor(Color.BLACK);
					if(t1 <= aantal1 && t2 <= aantal2 && t3 <= aantal3 && t4 <= aantal4)
					{ 	if(t4<aantal4)					
							gr.drawString(aantal4-t4+"/"+(a-i),(2*i+1)*b/2-theFM.stringWidth(aantal4-t4+"/"+(a-i))/2, (int) (h*(4*k*j+k+7)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3);
						if(t3<aantal3)						
							gr.drawString(aantal3-t3+"/"+(a-i),(2*i+1)*b/2-theFM.stringWidth(aantal3-t3+"/"+(a-i))/2, (int) (h*(4*k*j+k+5)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3);
						if(t2<aantal2)
							gr.drawString(aantal2-t2+"/"+(a-i),(2*i+1)*b/2-theFM.stringWidth(aantal2-t2+"/"+(a-i))/2, (int) (h*(4*k*j+k+3)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3);
						if(t1<aantal1)
							gr.drawString(aantal1-t1+"/"+(a-i),(2*i+1)*b/2-theFM.stringWidth(aantal1-t1+"/"+(a-i))/2, (int) (h*(4*k*j+k+1)/(4*Math.pow(k,i+1)))+theFM.getHeight()/3);
					}	
				}
			}
		}
		
	}
	
	public void tekenVolgordeStrings(Graphics gr)
	{	int k = aantalKeuzes;
		int n = aantalKolommen;
		int b = breedteKansboomveld;
		int h = hoogteKansboomveld;
		int positie;
		for(int j=1; j<Math.pow(k,n)+1; j++)
		{	volgordeString = "";
			positie=j-1;
			for(int i=1; i<n+1; i++)
				if(positie<Math.pow(k,n-i))
					volgordeString = volgordeString + letter1;
				else if(positie<2*Math.pow(k,n-i))
				{
					volgordeString = volgordeString + letter2;
					positie -= Math.pow(k,n-i);
				}
				else if(positie<3*Math.pow(k,n-i))
				{
					volgordeString = volgordeString + letter3;
					positie -= 2*Math.pow(k,n-i);
				}
				else if(positie<4*Math.pow(k,n-i))
				{	
					volgordeString = volgordeString + letter4;
					positie -= 3*Math.pow(k,n-i);
				}
			zetTellers(n,j-1);
			if(terugleggen)
				gr.drawString(volgordeString, b - breedteVolgordekolom + offset, (int) ((2*j-1)*h/(2*Math.pow(k,n)))+theFM.getHeight()/3);
			else if(t1 <= aantal1 && t2 <= aantal2 && t3 <= aantal3 && t4 <= aantal4)
				gr.drawString(volgordeString, b - breedteVolgordekolom + offset, (int) ((2*j-1)*h/(2*Math.pow(k,n)))+theFM.getHeight()/3);
		}
		
	}
	
	public void tekenEindkansen(Graphics gr)
	{	int k = aantalKeuzes;
		int n = aantalKolommen;
		int b = breedteKansboomveld;
		int h = hoogteKansboomveld;
		int s1, s2, s3, s4, s;
		int a = aantal1 + aantal2 + aantal3 + aantal4;
		if(aantalKeuzes<3)
			a -= aantal3;
		if(aantalKeuzes<4)
			a -= aantal4;
		
		for(int j=1; j<Math.pow(k,n)+1; j++)
		{	zetTellers(n,j-1);
			if(terugleggen)
				gr.drawString((int) (Math.pow(aantal1, t1)*Math.pow(aantal2, t2)*Math.pow(aantal3, Math.max(t3, 0))*Math.pow(aantal4, Math.max(t4,0)))+"/"+(int) Math.pow(a, n),
						b - breedteKanskolom + offset, (int) ((2*j-1)*h/(2*Math.pow(k,n)))+theFM.getHeight()/3);
			else if(t1 <= aantal1 && t2 <= aantal2 && t3 <= aantal3 && t4 <= aantal4)
			{	s1 = 1; s2 = 1; s3 = 1; s4 = 1; s = 1;
				for(int i=0; i < t1; i++)
					s1 *= aantal1-i;
				for(int i=0; i < t2; i++)
					s2 *= aantal2-i;
				for(int i=0; i < t3; i++)
					s3 *= aantal3-i;
				for(int i=0; i < t4; i++)
					s4 *= aantal4-i;
				for(int i=0; i < n; i++)	
					s *= a-i;
				gr.drawString(s1*s2*s3*s4+"/"+s, b - breedteKanskolom + offset, (int) ((2*j-1)*h/(2*Math.pow(k,n)))+theFM.getHeight()/3);
			}
		}
	}
	
	
	public void setSize(int b, int h)
	{
	    breedteKansboomveld = b;
	    hoogteKansboomveld = h;
	    breedteVolgordekolom = aantalKolommen*theFM.charWidth('a') + 2 * offset;
	    breedteKanskolom = 2*aantalKolommen*theFM.charWidth('a') + 2 * offset;
	    if(volgorde)
	    	breedteKolom=(breedteKansboomveld-breedteVolgordekolom)/aantalKolommen;
	    else if(eindkans)
	    	breedteKolom=(breedteKansboomveld-breedteKanskolom)/aantalKolommen;
	    else	
	    	breedteKolom=breedteKansboomveld/aantalKolommen;
	    super.setSize(b,h);
	}
		
	public void zetKleur(boolean b)
	{
		kleur = b;
		repaint();
	}
	
	public void zetVolgorde(boolean b)
	{
		volgorde = b;
		setSize(breedteKansboomveld, hoogteKansboomveld);
		repaint();
	}
	
	public void zetTerugleggen(boolean b)
	{
		terugleggen = b;
		repaint();
	}
	
	public void zetKeuzeMomenten(int i)
	{
		aantalKolommen = i;
		setSize(breedteKansboomveld, hoogteKansboomveld);
		repaint();
	}
	
	public void zetLabelsKeuze(int i)
	{
		if(i==0)
		{	letter = false;
			kans = false;
		}
		else if(i==1)
		{	letter = true;
			kans = false;
		}
		else if(i==2)
		{	letter = false;
			kans = true;
		}
		repaint();
	}
	
	public void zetAantalOpties(int i, int w3, int w4)
	{
		aantalKeuzes = i;
		if(i < 4)
			aantal4 = -1;
		else if(aantal4 == -1)
			aantal4 = w4;
		if(i < 3)
			aantal3 = -1;
		else if(aantal3 == -1)
			aantal3 = w3;
		repaint();
	}
	
	public void zetAantalVanOptie(int i, int j)
	{
		if(i == 1)
			aantal1 = j;
		else if(i == 2)
			aantal2 = j;
		else if(i == 3)
			aantal3 = j;
		else if(i ==4)
			aantal4 = j;
		
		repaint();
	}

}
