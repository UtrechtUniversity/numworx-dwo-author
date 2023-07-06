package fi.javalogoweb;

import logotekenap.Uitvoerblad;
import fi.javalogoweb.expressies.*;

public class InvoerCComponent extends SimpleCommandComponent
{
	
	public InvoerCComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{	
		super(x,y,b,h,sv);
		commandName = "invoer";
	}
	
	public boolean execute(Uitvoerblad ub, VarSet varSet)
	{	//String varNaam = gc1.geefTekst();
		String varNaam = "var1234";
		traceKleur = ub.invoer(varNaam);
		//double value = Double.NaN;
		//if(traceKleur) 
		double value = ub.geefInvoer();
		if(!Double.isNaN(value))varSet.setVar(varNaam, new BasisExpressie(value));
		if(traceKleur)schuifveld.updateView(varSet);
		return traceKleur;
	}
	
	public String getCode(String tab)
	{	String s = tab + "invoer(\"var1234\")" + "\n";
		return s;
	}
	
}