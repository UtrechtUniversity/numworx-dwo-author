package fi.javalogoweb;

import logotekenap.Uitvoerblad;
import fi.javalogoweb.expressies.*;

public class PrintlCComponent extends ParameterCommandComponent
{
	
	public PrintlCComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{	
		super(x,y,b,h,sv);
		parameter1 = new TextParameter();
		commandName = "println";
		commandNameTranslated = JavaLogoWeb.rb.getString(commandName);
		createEditor();
	}
	
	public boolean execute(Uitvoerblad ub, VarSet varSet)
	{	
		if ( !parameter1.isCorrect(varSet) ) return false; 
		traceKleur = ub.printl( ((TextParameter)parameter1).getValueText());
		if(traceKleur)schuifveld.updateView(varSet);
		return traceKleur;
	}	
}