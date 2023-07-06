package fi.javalogoweb3d;

import fi.javalogoweb3d.parameters.NumericParameter;
import fi.logotekenap3d.TraceBeheerder;
import fi.logotekenap3d.TekenApplet3D;

public class LinksCComponent  extends ParameterCommandComponent
{
	public LinksCComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{	
		super(x,y,b,h,sv);
		noParameters = 1;
		parameters[0] = new NumericParameter();
		commandName = "links";
		commandNameTranslated = JavaLogoWeb3d.rb.getString(commandName);
	}
		
	public boolean execute(TraceBeheerder trb, TekenApplet3D ub, VarSet varSet)
	{	
		if ( !parameters[0].isCorrect(varSet) ) return false; 
		ub.links( ((NumericParameter)parameters[0]).getValue());
		traceKleur = trb.commandExecuted(varSet.getLevel());
		if ( traceKleur ) trb.setCommandInfo(getActualCall(), varSet);
		return traceKleur;
	}
	
}

