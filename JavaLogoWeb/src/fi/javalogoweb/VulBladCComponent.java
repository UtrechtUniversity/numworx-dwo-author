package fi.javalogoweb;

import java.awt.Color;

import logotekenap.Uitvoerblad;

public class VulBladCComponent extends ParameterCommandComponent
{

	public VulBladCComponent(int x, int y, int b, int h,
			JavaLogoSchuifVeld sv)
	{
		super(x, y, b, h, sv);
		parameter1 = new ColorParameter();
		commandName = "vulBlad";
		commandNameTranslated = JavaLogoWeb.rb.getString(commandName);
		createEditor();
	}

	@Override
	public boolean execute(Uitvoerblad ub, VarSet varSet)
	{
		if ( !parameter1.isCorrect(varSet) ) return false; 
		Color cl = ((ColorParameter)parameter1).getColor();
		traceKleur = ub.vulBlad(cl.getRed(), cl.getGreen(), cl.getBlue());
		if(traceKleur)schuifveld.updateView(varSet);
		return traceKleur;
	}

}
