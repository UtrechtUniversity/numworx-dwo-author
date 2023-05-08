package fi.javalogoweb;

import java.awt.Color;

import logotekenap.Uitvoerblad;

public class VulAanCComponent extends ParameterCommandComponent
{
	
	public VulAanCComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{	
		super(x,y,b,h,sv);
		parameter1 = new ColorParameter();
		commandName = "vulAan";
		commandNameTranslated = JavaLogoWeb.rb.getString(commandName);
		createEditor();
	}
	
	public boolean execute(Uitvoerblad ub, VarSet varSet)
	{	 
		if ( !parameter1.isCorrect(varSet) ) return false; 
		Color cl = ((ColorParameter)parameter1).getColor();
		traceKleur = ub.vulAan(cl.getRed(), cl.getGreen(), cl.getBlue());
		if(traceKleur)schuifveld.updateView(varSet);
		return traceKleur;
	}
	
}