package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import fi.beans.grnuminput.*;

public class ZeshoekroosterProg extends TekenApplet3D 
{	
	double xhoek,yhoek,hoek,k, l, r, x, y, z;
	Matrix3D matrot, matbegin, mateenh;
	

	
	public void initialiseer()
	{	
		hoek = 0;
		k = 30;
	}
	
	public void tekenprogramma()
	{	tb.mat.initialiseer();

		penUit();stap(-280,150,0);penAan();
		for(int i=0 ; i<4 ; i++)
		{	rij(4);
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
}
