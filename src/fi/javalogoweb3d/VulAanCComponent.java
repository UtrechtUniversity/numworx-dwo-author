package fi.javalogoweb3d;

import java.awt.Color;

import fi.javalogoweb3d.parameters.ColorParameter;
import fi.logotekenap3d.TraceBeheerder;
import fi.logotekenap3d.TekenApplet3D;

public class VulAanCComponent extends ParameterCommandComponent
{
	
	public VulAanCComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{	
		super(x,y,b,h,sv);
		noParameters = 1;
		parameters[0] = new ColorParameter();
		commandName = "vulAan";
		commandNameTranslated = JavaLogoWeb3d.rb.getString(commandName);
	}
	
	@Override
	public boolean execute(TraceBeheerder trb, TekenApplet3D ub, VarSet varSet)
	{	 
		if ( !parameters[0].isCorrect(varSet) ) return false; 
		Color cl = ((ColorParameter)parameters[0]).getColor();
		ub.vulAan(cl.getRed(), cl.getGreen(), cl.getBlue());
		traceKleur = trb.commandExecuted(varSet.getLevel());
		if ( traceKleur ) trb.setCommandInfo(getActualCall(), varSet);
		return traceKleur;
	}
	
}