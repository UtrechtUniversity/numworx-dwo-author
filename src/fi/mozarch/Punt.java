package fi.mozarch;

import java.io.Serializable;



public class Punt implements Serializable
{	
	double x, y;
		
	Punt(double x, double y)
	{	this.x = x;
		this.y = y;
	}
		
	Punt(Punt p)
	{	this.x = p.x;
		this.y = p.y;
	}
}

