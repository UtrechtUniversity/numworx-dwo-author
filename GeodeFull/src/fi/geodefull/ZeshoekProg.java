package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import fi.beans.grnuminput.*;

public class ZeshoekProg extends TekenApplet3D implements NumberListener
{	
	double xhoek,yhoek,hoek,k, l, r, x, y, z;
	Matrix3D matrot, matbegin, mateenh;
	NumberSlider oprolSl;

	
	public void initialiseer()
	{	oprolSl = new NumberSlider(0,30,0,0,"","");
		oprolSl.addNumberListener(this);
		oprolSl.setValue(0);
		//rg.gridbag.setConstraints(oprolSl, rg.c);
		//rg.add(oprolSl);
		oprolSl.setBackground(Color.white);
		add(oprolSl,"South");
		maakMuisActieMogelijk();
		hoek = 0;
		k = 30;
	}
	
	public void tekenprogramma()
	{	tb.mat.initialiseer();
		xdraai(xhoek+hoek);ydraai(yhoek);
		
		l = 9*Math.sqrt(3)*k;
		if(hoek==0)
		{	x=l;y=5*k;z=0;
		}
		else
		{
			r = 360*l/(6*hoek*2*Math.PI);
			x = r*Math.sin(6*hoek*Math.PI/180);
			y = 5*k;
			z = 0.5*r*(1-Math.cos(6*hoek*Math.PI/180));
		}
		
		penUit();stap(-x,y,z);penAan();
		ydraai(-6*hoek);
		for(int i=0 ; i<4 ; i++)
		{	rij(6);
		}
	}
	void rij(int aantal)
	{	for(int i=0 ; i<aantal ; i++)
		{	zeshoekZwart(k);
			penUit();rechts(120);vooruit(k);links(120);ydraai(hoek); rechts(60);vooruit(k);links(60);penAan();
			zeshoek(k,"wit");
			penUit();rechts(120);vooruit(k);links(60);vooruit(k);links(60);penAan();
			ydraai(hoek);
			zeshoek(k,"wit");
			penUit();rechts(120);vooruit(k);links(60);vooruit(k);links(60);penAan();
		}
		links(120);vooruit(k);links(60);
		for(int i=0 ; i<aantal ; i++)
		{	zeshoekZwart(k);
			penUit();rechts(120);vooruit(k);links(120);ydraai(hoek); rechts(60);vooruit(k);links(60);penAan();
			zeshoek(k,"wit");
			penUit();rechts(120);vooruit(k);links(60);vooruit(k);links(60);penAan();
			ydraai(hoek);
			zeshoek(k,"wit");
			penUit();rechts(120);vooruit(k);links(60);vooruit(k);links(60);penAan();
		}
		links(120);vooruit(k);links(60);
		penUit();vooruit(-3*k);penAan();
		
	}
	void zeshoek(double zijde, String kleur)
	{	vulAan(kleur);
		for(int i=0 ; i<6 ; i++)
		{	vooruit(zijde);
			rechts(60);
		}
		vulUit();
		vulAan("grijs");
		for(int i=0 ; i<6 ; i++)
		{	links(60);
			vooruit(-zijde);
		}
		vulUit();
	}
	void zeshoekZwart(double zijde)
	{	vulAan("zwart");
		vooruit(zijde);rechts(60);
		vooruit(zijde);rechts(120);
		ydraai(-hoek);links(60);
		vooruit(zijde);rechts(60);
		vooruit(zijde);rechts(60);
		vooruit(zijde);rechts(120);
		ydraai(-hoek);links(60);
		vooruit(zijde);rechts(60);
		vulUit();
		vulAan("grijs");
		links(60);vooruit(-zijde);
		rechts(60);ydraai(hoek);
		links(120);vooruit(-zijde);
		links(60);vooruit(-zijde);
		links(60);vooruit(-zijde);
		rechts(60);ydraai(hoek);
		links(120);vooruit(-zijde);
		links(60);vooruit(-zijde);
		vulUit();
	}

	public void numberChanged(String name,double val)
	{	hoek = val;
		tekenOpnieuw();
	}
	
	public void muisSleepActie()
	{	
		{	xhoek += -0.5*geefSleepdy();
			yhoek += 0.5*geefSleepdx();
			tekenOpnieuw();
		}
	}
	public void muisLosActie()
	{	if((geefDrukx()-geefX())*(geefDrukx()-geefX()) + (geefDruky()-geefY())*(geefDruky()-geefY()) < 10 )
		{	//	muisKkActie();
		}
		tekenOpnieuw();
	}
	public void animatie()
	{	while(animatieStatus())
		{	matrot.initialiseer();
			matrot.ydraaiAbs(1);
			matrot.xdraaiAbs(0);
			matbegin.mult(matrot);
			tekenOpnieuw();
		}
	}
}
