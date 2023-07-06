package fi.tegels;

import java.awt.Point;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class NoSer 
{
	public static boolean equalSS(SchuifStuk ss1, SchuifStuk ss2)
	{	
		boolean kleurEqual = ss1.kleur.equals(ss2.kleur);
		if (!kleurEqual)
			return false;
		boolean equalNumPoints = ss1.aantalPunten == ss2.aantalPunten;
		if (!equalNumPoints)
			return false;
//volgorde??
		boolean equalPoints = true;
		for (int pCnt = 0; pCnt < ss1.punten.length; pCnt++)
		{
			equalPoints = equalPoints && (ss1.punten[pCnt].x == ss2.punten[pCnt].x) &&
			(ss1.punten[pCnt].y == ss2.punten[pCnt].y);
		}
		
		return equalPoints;
	}
	
	public static HashMap<String,Object> getSSState(SchuifStuk ss)
	{
		HashMap<String,Object> h = new HashMap<String,Object>();
		
		h.put("aantalPunten", new Integer(ss.aantalPunten));
		ArrayList<Integer> puntenX = new ArrayList<Integer>();
		for (int pCnt = 0; pCnt < ss.punten.length; pCnt++)
			puntenX.add(new Integer(ss.punten[pCnt].x));
		h.put("puntenX", puntenX);
		ArrayList<Integer> puntenY = new ArrayList<Integer>();
		for (int pCnt = 0; pCnt < ss.punten.length; pCnt++)
			puntenY.add(new Integer(ss.punten[pCnt].y));
		h.put("puntenY", puntenY);
		h.put("kleur", new String("rgb(" + ss.kleur.getRed()+ "," + ss.kleur.getGreen() + "," + ss.kleur.getBlue() + ")"));
		h.put("positieX", new Integer(ss.positie.x));
		h.put("positieY", new Integer(ss.positie.y));
		
		return h;
	}


}
