package logotekenap;

/**
 * InvoerVar is het gemeenschappelijke stuk van de InvoerVariabele (met pijltjes)
 * en de SchuifInvoerVariabele
 *
 * @author Paul Bergervoet
 * @version 2.0  3 maart 2000
 */
 
 import java.awt.*;

public abstract class InvoerVar extends Panel		// en is daarmee een Component
{	protected int waarde;					// huidige waarde
	protected int initw;					// beginwaarde
	protected int minw;					// minimum waarde
	protected int maxw;					// maximum waarde
	protected String naam;					// bijschrift
	
	protected TekenApplet baas;
	
	protected boolean enabled = true;
	
/**
 * Maak communicatie met TekenApplet mogelijk door het zetten van de 'baas'. 
 * Dit moet gebeuren in maakZichtbaar(Component), die daarvoor een test moet doen 
 * op het type van de Component: 	
 *	if ( (Component) instanceof InvoerVar ) (Component).zetBaas(this);
 *
 * NB: aanroepen van baas.beschermdeInvoerActie(...) komt hier niet voor.
 * Dit gebeurt uitsluitend vanuit de subklassen.
 */

	void zetBaas(TekenApplet ta)
	{	baas = ta;
	}
	
/**
 * Geeft de waarde van de InvoerVariabele.
 *
 * @return een int (geheel getal): de waarde van de InvoerVariabele.
 */

	public int geefWaarde()
	{	return waarde;
	}
	
/**
 * Geef de InvoerVariabele een nieuwe waarde.
 * Er wordt gecontroleerd of de nieuwe waarde tussen het minimum en het maximum ligt.
 * Als die te groot is, wordt het maximum de nieuwe waarde van de InvoerVariabele.
 * Als die te klein is, wordt het minimum de nieuwe waarde van de InvoerVariabele.
 *
 * @param w de nieuwe waarde.
 */

	abstract public void zetWaarde(int w);	// NB: overrides nodig, voor zetten van de Slider, NumOnlyField!
	
/**
 * Verhoog de waarde van de InvoerVariabele.
 * Er wordt gecontroleerd of de nieuwe waarde boven het maximum uitkomt.
 * Als die te groot is, wordt het maximum de nieuwe waarde van de InvoerVariabele.
 *
 * @param d de verhoging.
 */

	public void verhoogWaarde(int d)
	{	zetWaarde(waarde+d);
	}
	
/**
 * Verlaag de waarde van de InvoerVariabele.
 * Er wordt gecontroleerd of de nieuwe waarde onder het minimum uitkomt.
 * Als die te klein is, wordt het minimum de nieuwe waarde van de InvoerVariabele.
 *
 * @param d de verlaging.
 */
	public void verlaagWaarde(int d)
	{	zetWaarde(waarde-d);
	}
	
/**
 * Maakt het veranderen van de waarde van de InvoerVariabele mogelijk.
 * Dit is alleen nodig als de invoer eerder uitgezet is. Een nieuwe InvoerVariabele staat
 * altijd aan.
 */

	abstract public void zetInvoerAan();	// NB: override bij beide subklassen nodig!
	
/**
 * Maakt het veranderen van de waarde van de InvoerVariabele ONmogelijk.
 * Dit geldt alleen voor het invoerveld in de user-interface. De programmeur kan de waarde nog
 * veranderen met de methode zetWaarde.
 * Het uitzetten van een InvoerVariabele kan vooral handig zijn bij een animatie, wanneer je niet
 * wilt dat de gebruiker de waarden verandert, terwijl de animatie loopt.
 */

	abstract public void zetInvoerUit();	// NB: override bij beide subklassen nodig!

/**
 * Ook setEnabled herdefini‘ren, zodat deze aanroepen goed lopen (ihb voor setEnabledAll)
 */
 
	public void setEnabled(boolean b)
	{	if ( b )
		{	zetInvoerAan();
		} else
		{	zetInvoerUit();
		}
	}
	
/**
 * Waarde veranderen na intikken in NumOnlyField
 */

	abstract void numberTyped(int n);	// NB: override bij beide subklassen nodig!

}	// end class InvoerVar

