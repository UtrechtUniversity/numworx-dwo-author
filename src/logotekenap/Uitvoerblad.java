package logotekenap;

import javax.swing.JPanel;

/**
 * abstract superclass for all output panels (now 'teken' en 'reken')
 * this resolves the problem of the 'teken'...
 * 
 * Note:	design drawback is that all subclaasses must implement ALL commands, 
 * 			the irrelevant ones will be dummies or error messages
 * 
 * @author Berge020
 */
public abstract class Uitvoerblad extends JPanel
{
	protected TraceBeheerder trb;

	public void meldTraceBeheerder(TraceBeheerder trb)
	{	this.trb = trb;
	}
	  
	public boolean checkKeuze(String voorwaarde)
	{	if(trb!=null && trb.geefTraceStatus())
			return trb.volgendeMethode(voorwaarde);
		else return false;
	}

	/**
	 * Tekenopdrachten
	 */
	public abstract boolean links(double dHoek);
	
  	public abstract boolean rechts(double dHoek);

	public abstract boolean vooruit(double dy);
  	
	public abstract boolean stap(double dx,double dy);
  	
	// probably the rgb version in the future, ook voor rekenblad???
	public abstract boolean penAan(int r, int g, int b);
  	
	public abstract boolean penUit();
	
	public abstract boolean vulAan(int r, int g, int b);
	
	public abstract boolean vulUit();
	
	public abstract boolean vulBlad(int r, int g, int b);
	
	/**
	 * Opdrachten in- en uitvoer
	 */
	public abstract boolean printl(String s);
	
	public abstract boolean print(String s);
	
	// (nog) niet geïmplemeteerd...
	public abstract boolean invoer(String varNaam);
	
	public abstract double geefInvoer();
	
	/**
	 * Algemeen
	 */
 	public abstract void tekenOpnieuw();
		
	public abstract boolean varAanpassing(String varNaam, String varValue);
	
}
