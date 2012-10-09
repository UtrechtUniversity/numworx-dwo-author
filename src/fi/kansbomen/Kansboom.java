package fi.kansbomen;

import java.awt.*;

import javax.swing.JPanel;

public class Kansboom extends JPanel

{ 
	
	
	static int HOOGTE = 400; 
	static int BREEDTE = 600;
	int hoogteKansboomveld;
	int breedteKansboomveld;
	int aantalKolommen;
	int breedteKolom;
	int aantalKeuzes;
	int t1, t2, t3, t4;
	int aantal1, aantal2, aantal3, aantal4;
	boolean terugleggen, kleur;
	
	Color backgroundColor = Color.white;
	
	public Kansboom()
	{
	    
		//Alles wat instelbaar is, moet voor de docent instelbaar zijn. 
		//Bovendien moet de docent kunnen aangeven dat het voor de leerling instelbaar is.
		
    	hoogteKansboomveld=HOOGTE; 
    	breedteKansboomveld=BREEDTE; 
    	aantalKolommen=3; //Instelbaar als aantal keuzemomenten (of aantal keer trekken)
    	breedteKolom=breedteKansboomveld/aantalKolommen;
    	//setSize(breedteKansboomveld, hoogteKansboomveld);
    	
    	
    	aantalKeuzes=4; //Instelbaar. Opties: 2, 3 of 4
    	aantal1=4; //Instelbaar. Van 1 tot 10 bijvoorbeeld.
    	aantal2=4; //Instelbaar. Van 1 tot 10 bijvoorbeeld.
    	aantal3=4; //Instelbaar. Van 1 tot 10 bijvoorbeeld. Moet standaard -1 zijn als aantalKeuzes < 3, en niet zichtbaar voor docent of leerling.
    	aantal4=4; //Instelbaar. Van 1 tot 10 bijvoorbeeld. Moet standaard -1 zijn als aantalKeuzes < 4, en niet zichtbaar voor docent of leerling.
    	
    	terugleggen=true; //Instelbaar door middel van aanvinkvakje; true is met terugleggen, false is zonder. 
    	kleur=true; //Instelbaar door middel van aanvinkvakje; bij true heeft elke keuzemogelijkheid zijn eigen kleur.
	}
	
	
	public void paint(Graphics g)
	{ 	g.setColor(backgroundColor);
		g.fillRect(0, 0, getSize().width, getSize().height);
	    
		this.tekenKansboom(g);
		
	}
	
	private void tekenKansboom(Graphics gr)
	{ 	
		int k = aantalKeuzes;
		int h;
		int d = 0;
		int mod = 0;
		int macht = 0;
		
		
		for(int i=0; i<aantalKolommen; i++)
		{	for(int j=0; j<Math.pow(k,i); j++)
			{	h=0;
				for(int s=0; s<i; s++)
				{	d=(int) (j/Math.pow(k,s));
					mod=d%k;
					macht=(int) Math.pow(10,s);
					h=h+macht*mod;
				}
			if(terugleggen)
			{	t1=0; t2=0; 
				if(aantalKeuzes<3)
				t3=-1; 
				else t3=0;
				if(aantalKeuzes<4)
				t4=-1;
				else t4=0;	
			}
			else
				this.zetTellers(h,i);
			this.tekenKinderen(i,j,t1, t2, t3, t4,gr);	
			}
		}
	}
	
		
	private void zetTellers(int h, int i)
	{	t1=0; t2=0; 
		if(aantalKeuzes<3)
			t3=-1; 
		else t3=0;
		if(aantalKeuzes<4)
			t4=-1;
		else t4=0;
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
	
		Graphics2D gr2 = (Graphics2D) gr;
		gr2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		        RenderingHints.VALUE_ANTIALIAS_ON);
		
		if(t1 <= aantal1 && t2 <= aantal2 && t3 <= aantal3 && t4 <= aantal4)
		{ 	if(t4<aantal4)
			{	gr2.setColor(kleur4);
				gr2.drawLine(i*b, (int) ((2*j+1)/(Math.pow(k,i)*2)*h), (i+1)*b, (int) ((2*k*j+7)/(Math.pow(k,i+1)*2)*h));
			}
			if(t3<aantal3)
			{	gr2.setColor(kleur3);
				gr2.drawLine(i*b, (int) ((2*j+1)/(Math.pow(k,i)*2)*h), (i+1)*b, (int) ((2*k*j+5)/(Math.pow(k,i+1)*2)*h));
			}
			if(t2<aantal2)
			{	gr2.setColor(kleur2);
				gr2.drawLine(i*b, (int) ((2*j+1)/(Math.pow(k,i)*2)*h), (i+1)*b, (int) ((2*k*j+3)/(Math.pow(k,i+1)*2)*h));
			}
			if(t1<aantal1)
			{	gr2.setColor(kleur1);
				gr2.drawLine(i*b, (int) ((2*j+1)/(Math.pow(k,i)*2)*h), (i+1)*b, (int) ((2*k*j+1)/(Math.pow(k,i+1)*2)*h));
			}
			
		}
	}
	
	public void setSize(int b, int h)
	{
	    breedteKansboomveld = b;
	    hoogteKansboomveld = h;
	    breedteKolom=breedteKansboomveld/aantalKolommen;
	    super.setSize(b,h);
	}
		
	public void zetKleur(boolean b)
	{
		kleur = b;
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
	
	public void zetAantalOpties(int i)
	{
		aantalKeuzes = i;
		if(i < 4)
			aantal4 = -1;
		else if(aantal4 == -1)
			aantal4 = 4;
		if(i < 3)
			aantal3 = -1;
		else if(aantal3 == -1)
			aantal3 = 4;
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
