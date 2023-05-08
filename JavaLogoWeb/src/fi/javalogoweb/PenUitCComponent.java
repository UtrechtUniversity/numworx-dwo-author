package fi.javalogoweb;

import logotekenap.Uitvoerblad;

public class PenUitCComponent extends SimpleCommandComponent
{
	
	public PenUitCComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{	
		super(x,y,b,h,sv);
		commandName = "penUit";
		commandNameTranslated = JavaLogoWeb.rb.getString(commandName);
	}
		
	public boolean execute(Uitvoerblad ub, VarSet varSet)
	{	traceKleur = ub.penUit();
		if(traceKleur)schuifveld.updateView(varSet);
		return traceKleur;
	}
	
}
