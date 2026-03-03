package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import fi.beans.grnuminput.*;

public class VeelhoekProg extends TekenApplet3D implements NumberListener
{	
	double aantal,xhoek,yhoek,hoek,k, l, r, x, y, z,a,n;
	Matrix3D matrot, matbegin, mateenh;
	NumberArrow oprolSl;

	
	public void initialiseer()
	{	oprolSl = new NumberArrow(3,20,5,1,0,"aantal hoekpunten","");
		oprolSl.addNumberListener(this);
		oprolSl.setValue(5);
		rg.gridbag.setConstraints(oprolSl, rg.c);
		rg.add(oprolSl);
		oprolSl.setBackground(Color.white);
		rg.setBackground(Color.white);
		aantal = 5;
		hoek = 60;
		k = 200;
		r = 50;
		a = 2*r*Math.sin(Math.PI/180);
	}
	
	public void tekenprogramma()
	{	tb.mat.initialiseer();
		hoek = 180-360.0/aantal;
		n=hoek/2;
		penUit();vooruit(-k);
		stap(-20,-40);schrijf(Float.toString((float)hoek)+" graden");stap(20,40);penAan();
		links(hoek/2);
				
		vooruit(1.5*k);vooruit(-1.5*k);
		rechts(hoek);
		vooruit(1.5*k);vooruit(-1.5*k);
		links(hoek);
		
		vooruit(r);rechts(91);
		stapz(-10);
		for(int i=0 ; i<n ; i++)
		{	vooruit(a);
			rechts(2);
		}
		stapz(10);
		for(int i=0 ; i<n ; i++)
		{	rechts(-2);
			vooruit(-a);
		} 
		links(91);vooruit(-r);
		
		
		double koorde = 2*k*Math.cos(0.5*hoek*Math.PI/180);
		vulAan("rood");
		for(int i=0 ; i<aantal ; i++)
		{	vooruit(koorde);
			rechts(180-hoek);
		}
		vulUit();
	}
	
	public void numberChanged(String name,double val)
	{	aantal = val;
		tekenOpnieuw();
	}
}
