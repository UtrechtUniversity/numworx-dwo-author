package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import fi.beans.grnuminput.*;


public class van6Naar5Prog extends TekenApplet3D implements NumberListener
{	
	double zijde,kz,factor,alpha,phi,rad;
	NumberSlider oprolSl;

	
	public void initialiseer()
	{	oprolSl = new NumberSlider(0,60,0,0,"","");
		oprolSl.addNumberListener(this);
		oprolSl.setValue(0);
		rg.gridbag.setConstraints(oprolSl, rg.c);
		rg.add(oprolSl);
		oprolSl.setBackground(Color.white);
		rg.setBackground(Color.white);
		
		zijde = 25;
		phi = 60;
		alpha = 0.2*(360-phi);
		rad = 0.5*zijde/Math.sin(0.5*alpha*Math.PI/180);
		kz = 2*rad*Math.sin(0.5*phi*Math.PI/180);
		factor = kz/zijde;
	}
	public void tekenprogramma()
	{	tb.mat.initialiseer();
		rechts(phi/2);
		vooruit(rad);
		rechts(90+alpha/2);
		vijfhoek(zijde);
		for(int i=0 ; i<5 ; i++)
		{	zeshoekfr(zijde,2,true);
			vooruit(zijde);
			rechts(alpha);
		}
		rechts(phi/2-alpha/2);
		zeshoekfr(factor*zijde,2,true);
		
		
	}
	void vijfhoek(double z)
	{	
		vulAan("zwart");
		for(int i=0 ; i<4 ; i++)
		{	vooruit(z);
			rechts(alpha);
		}
		vooruit(z);
		rechts(alpha/2+phi/2);
		vooruit(factor*z);
		rechts(alpha/2+phi/2);
		vulUit();
	}
	void zeshoek(double z)
	{	vulAan("wit");
		for(int i=0 ; i<6   ; i++)
		{	vooruit(z);
			rechts(60);
		}
		vulUit();
	}
	void zeshoekfr(double z, int niv, boolean drie)
	{	links(120);
		zeshoek(z);
		vooruit(z);
		rechts(60);
		vooruit(z);
		rechts(60);
		vooruit(z);
		rechts(60);
		vooruit(z);
		rechts(180);
		vooruit(z);
		rechts(alpha);
		links(120);
		zeshoek(z);
		rechts(120);
		vooruit(z);
		rechts(alpha/2+phi/2);
		if(niv>0)
		{	
			zeshoekfr(z*factor,niv-1,true);
			
		}
		vooruit(factor*z);
		rechts(alpha/2+phi/2);
		if(drie && niv>0)
		{	
			zeshoekfr(z,niv-1,false);
			
		}
		vijfhoek(z);
		vooruit(z);
		rechts(alpha);
		vooruit(z);
		rechts(alpha);
		vooruit(z);
		links(120-alpha);
		vooruit(z);
		rechts(60);
		vooruit(z);
		rechts(180);
	}
	public void numberChanged(String name,double val)
	{	phi = 60-val;
		 alpha = 0.2*(360-phi);
		rad = 0.5*zijde/Math.sin(0.5*alpha*Math.PI/180);
		kz = 2*rad*Math.sin(0.5*phi*Math.PI/180);
		factor = kz/zijde;
		tekenOpnieuw();
	}
}
