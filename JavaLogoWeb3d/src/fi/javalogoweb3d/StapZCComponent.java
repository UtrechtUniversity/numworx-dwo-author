package fi.javalogoweb3d;

import fi.javalogoweb3d.parameters.NumericParameter;
import fi.logotekenap3d.TraceBeheerder;
import fi.logotekenap3d.TekenApplet3D;

public class StapZCComponent  extends ParameterCommandComponent
{
	public StapZCComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{	
		super(x,y,b,h,sv);
		noParameters = 1;
		parameters[0] = new NumericParameter();
		commandName = "stapz";
		commandNameTranslated = JavaLogoWeb3d.rb.getString(commandName);
	}
		
	public boolean execute(TraceBeheerder trb, TekenApplet3D ub, VarSet varSet)
	{	
		if (!parameters[0].isCorrect(varSet)) 
			return false; 
		ub.stapz(((NumericParameter) parameters[0]).getValue());
		traceKleur = trb.commandExecuted(varSet.getLevel());
		if (traceKleur) 
			trb.setCommandInfo(getActualCall(), varSet);
		return traceKleur;
	}
	
}
