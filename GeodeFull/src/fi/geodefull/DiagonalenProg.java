package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import fi.beans.grnuminput.*;

public class DiagonalenProg extends TekenApplet3D implements NumberListener
{	
	double r;
	int aantal;
	NumberArrow oprolSl;
	Punt[] punten;
	
	public void initialiseer()
	{	oprolSl = new NumberArrow(3,40,9,1,0,"aantal hoekpunten","");
		oprolSl.addNumberListener(this);
		oprolSl.setValue(9);
		rg.gridbag.setConstraints(oprolSl, rg.c);
		rg.add(oprolSl);
		oprolSl.setBackground(Color.white);
		rg.setBackground(Color.white);
		aantal = 9;
		r=220;
	}
	
	public void tekenprogramma()
	{	punten = new Punt[aantal];
		for(int i=0 ; i<aantal ; i++)
		{	double h = -Math.PI/2 + 2*i*Math.PI/aantal;
			double a = r*Math.cos(h);
			double b = r*Math.sin(h);
			punten[i] = new Punt(a,b);
		}
		penUit();stap(punten[0].x , punten[0].y);penAan();
		for(int i=0 ; i<aantal-1 ; i++)
		{	for(int j=i+2 ; j<aantal ; j++)
			{	stap(punten[j].x-punten[i].x , punten[j].y-punten[i].y);
				stap(-(punten[j].x-punten[i].x) , -(punten[j].y-punten[i].y));
			}
			stap(punten[i+1].x-punten[i].x , punten[i+1].y-punten[i].y);
		}
	}

	public void numberChanged(String name,double val)
	{	aantal = (int)val;
		tekenOpnieuw();
	}
}
