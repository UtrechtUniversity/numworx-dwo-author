package fi.verknippen;

import java.awt.Rectangle;
import java.awt.Point;
import java.util.Vector;
import java.io.Serializable;

public class Opdracht
{
	int opdrachtNum;
	Vector figuurCoordinaten = new Vector();
	Vector grijsFiguurCoordinaten = new Vector();
	int gridSize = 20;
	// in grideenheden!
	int oppervlakte = 0;
	int oppervlakteGrijs = 0;
	int antwoord = 0;
	boolean antwoordOK = false;
	
	int antwoordenFout = 0;
	
	DrawingPanel drawingPanel;
		
	public Opdracht(int num)
	{	opdrachtNum = num;
	} 


}

class ScormOpdracht implements Serializable
{	
	int opdrachtNum;
	boolean isCurrent = false;
	Vector figuurPolygons = new Vector();
	int antwoord = 0;
	boolean antwoordOK = false;
	int antwoordenFout = 0;	

	public ScormOpdracht(int num)
	{	opdrachtNum = num;
	} 
	
}
