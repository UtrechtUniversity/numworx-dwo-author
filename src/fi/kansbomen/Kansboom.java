package fi.kansbomen;

import java.awt.*;
import java.applet.*;

import javax.swing.*;

public class Kansboom extends JPanel

{ 
	
	
	int hoogteKansboomveld, breedteKansboomveld;
	int aantalKolommen;
	int breedteKolom;
	int aantalKeuzes;
	int t1, t2, t3, t4;
	int aantal1, aantal2, aantal3, aantal4;
	boolean terugleggen, inKleur;
	
	public void init()
	{
		//Alles wat instelbaar is, moet voor de docent instelbaar zijn. 
		//Bovendien moet de docent kunnen aangeven dat het voor de leerling instelbaar is.
		
	hoogteKansboomveld=400; //Standaardmaat in DWO? 
	breedteKansboomveld=600; //Standaardmaat in DWO?
	aantalKolommen=4; //Instelbaar als aantal keuzemomenten (of aantal keer trekken)
	breedteKolom=breedteKansboomveld/aantalKolommen;
	
	aantalKeuzes=2; //Instelbaar. Opties: 2, 3 of 4
	aantal1=2; //Instelbaar. Van 1 tot 10 bijvoorbeeld.
	aantal2=5; //Instelbaar. Van 1 tot 10 bijvoorbeeld.
	aantal3=-1; //Instelbaar. Van 1 tot 10 bijvoorbeeld. Moet standaard -1 zijn als aantalKeuzes < 3, en niet zichtbaar voor docent of leerling.
	aantal4=-1; //Instelbaar. Van 1 tot 10 bijvoorbeeld. Moet standaard -1 zijn als aantalKeuzes < 4, en niet zichtbaar voor docent of leerling.
	
	terugleggen=false; //Instelbaar door middel van aanvinkvakje; true is met terugleggen, false is zonder. 
	inKleur=true; //Instelbaar door middel van aanvinkvakje; bij true heeft elke keuzemogelijkheid zijn eigen kleur.
	}
	
	
	public void paint(Graphics g)
	{ 	g.drawRect(30,30,breedteKansboomveld,hoogteKansboomveld);
		//for(int i=1;i<aantalKolommen;i++)
		//{g.drawLine(30+i*breedteKolom, 30, 30+i*breedteKolom, 30+hoogteKansboomveld);
		//} //Dit tekent kolommen in het kansboomveld. Volgens mij wil ik dat liever niet.
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
			{	t1=0; t2=0; t3=0; t4=0;	}
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
		if(inKleur) //TO DO: Ik wil deze kleuren gelijk hebben aan die in de grafiekentool.
		{	kleur1= Color.RED;
			kleur2= Color.BLUE;
			kleur3= Color.GREEN;
			kleur4= Color.MAGENTA;
		}
		else
		{	kleur1= Color.BLACK;
			kleur2= Color.BLACK;
			kleur3= Color.BLACK;
			kleur4= Color.BLACK;
		}
		
		
		if(t1 <= aantal1 && t2 <= aantal2 && t3 <= aantal3 && t4 <= aantal4)
		{ 	if(t1<aantal1)
			{	gr.setColor(kleur1);
				gr.drawLine(30+i*b, (int) ((2*j+1)/(Math.pow(k,i)*2)*h+30), 30+(i+1)*b, (int) ((2*k*j+1)/(Math.pow(k,i+1)*2)*h+30));
			}
			if(t2<aantal2)
			{	gr.setColor(kleur2);
				gr.drawLine(30+i*b, (int) ((2*j+1)/(Math.pow(k,i)*2)*h+30), 30+(i+1)*b, (int) ((2*k*j+3)/(Math.pow(k,i+1)*2)*h+30));
			}
			if(t3<aantal3)
			{	gr.setColor(kleur3);
				gr.drawLine(30+i*b, (int) ((2*j+1)/(Math.pow(k,i)*2)*h+30), 30+(i+1)*b, (int) ((2*k*j+5)/(Math.pow(k,i+1)*2)*h+30));
			}
			if(t4<aantal4)
			{	gr.setColor(kleur4);
				gr.drawLine(30+i*b, (int) ((2*j+1)/(Math.pow(k,i)*2)*h+30), 30+(i+1)*b, (int) ((2*k*j+7)/(Math.pow(k,i+1)*2)*h+30));
			}
		}
	}	
		
	
	
	

}
