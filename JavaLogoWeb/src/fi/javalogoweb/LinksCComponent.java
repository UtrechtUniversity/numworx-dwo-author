package fi.javalogoweb;

import logotekenap.Uitvoerblad;

public class LinksCComponent  extends ParameterCommandComponent
{
	public LinksCComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{	
		super(x,y,b,h,sv);
		parameter1 = new NumericParameter();
		commandName = "links";
		commandNameTranslated = JavaLogoWeb.rb.getString(commandName);
		createEditor();
	}
		
	public boolean execute(Uitvoerblad ub, VarSet varSet)
	{	
		if ( !parameter1.isCorrect(varSet) ) return false; 
		traceKleur = ub.links( ((NumericParameter)parameter1).getValue());
		if(traceKleur)schuifveld.updateView(varSet);
		return traceKleur;
	}
	
}

