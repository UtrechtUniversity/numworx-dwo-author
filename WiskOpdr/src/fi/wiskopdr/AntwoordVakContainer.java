package fi.wiskopdr;

import java.util.Hashtable;

public class AntwoordVakContainer 
{
	protected AntwoordVak antwoordVak;
	protected AntwoordFormuleVak antwoordFormuleVak;
	protected AntwoordVergelijkingVak antwoordVergelijkingVak;
	
	public void zetOpdracht(Hashtable h)
	{
		String antwoordString = "$f@";
		String startString = "$f@";
		boolean herleiding = false;
		boolean exact = false;
		boolean stappen = true;
		int soortHerleiding = 0;
		int puntenGelijkwaardig = 10;
		int puntenHerleiding = 0;
		int puntenExact = 0;
		boolean vergelijking = false;
		boolean eindOplossingNodig = true;
		int puntenEindOplossing = 0;
		boolean bewerkingKnoppen = false;
		boolean abcKnop = false;
		boolean subKnop = false;
		
		if(h.containsKey("antwoordString")) antwoordString = (String)h.get("antwoordString");
		if(h.containsKey("startString")) startString = (String)h.get("startString");
		if(h.containsKey("herleiding")) herleiding = ((Boolean)h.get("herleiding")).booleanValue();
		if(h.containsKey("exact")) exact = ((Boolean)h.get("exact")).booleanValue();
		if(h.containsKey("stappen")) stappen = ((Boolean)h.get("stappen")).booleanValue();
		if(h.containsKey("soortHerleiding")) soortHerleiding = ((Integer)h.get("soortHerleiding")).intValue();
		if(h.containsKey("puntenGelijkwaardig")) puntenGelijkwaardig = ((Integer)h.get("puntenGelijkwaardig")).intValue();
		if(h.containsKey("puntenHerleiding")) puntenHerleiding = ((Integer)h.get("puntenHerleiding")).intValue();
		if(h.containsKey("puntenExact")) puntenExact = ((Integer)h.get("puntenExact")).intValue();
		if(h.containsKey("puntenEindOplossing")) puntenEindOplossing = ((Integer)h.get("puntenEindOplossing")).intValue();
		if(h.containsKey("vergelijking")) vergelijking = ((Boolean)h.get("vergelijking")).booleanValue();
		if(h.containsKey("eindOplossingNodig")) eindOplossingNodig = ((Boolean)h.get("eindOplossingNodig")).booleanValue();
		if(h.containsKey("bewerkingKnoppen")) bewerkingKnoppen = ((Boolean)h.get("bewerkingKnoppen")).booleanValue();
		if(h.containsKey("abcKnop")) abcKnop = ((Boolean)h.get("abcKnop")).booleanValue();
		if(h.containsKey("subKnop")) subKnop = ((Boolean)h.get("subKnop")).booleanValue();
		
	}
	

}
