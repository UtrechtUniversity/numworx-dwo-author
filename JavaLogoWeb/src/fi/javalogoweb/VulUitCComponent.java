package fi.javalogoweb;

import logotekenap.Uitvoerblad;

public class VulUitCComponent extends SimpleCommandComponent
{
	public VulUitCComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{	super(x,y,b,h,sv);
		commandName = "vulUit";
		commandNameTranslated = JavaLogoWeb.rb.getString(commandName);
	}
	
	public boolean execute(Uitvoerblad ub, VarSet varSet)
	{	traceKleur = ub.vulUit();
		if(traceKleur)schuifveld.updateView(varSet);
		return traceKleur;
	}
	
}