package fi.javalogoweb3d;

import fi.logotekenap3d.TraceBeheerder;
import fi.logotekenap3d.TekenApplet3D;

public class PenUitCComponent extends SimpleCommandComponent
{
	
	public PenUitCComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{	
		super(x,y,b,h,sv);
		commandName = "penUit";
		commandNameTranslated = JavaLogoWeb3d.rb.getString(commandName);
	}
		
	@Override
	public boolean execute(TraceBeheerder trb, TekenApplet3D ub, VarSet varSet)
	{	
		ub.penUit();
		traceKleur = trb.commandExecuted(varSet.getLevel());
		if ( traceKleur ) trb.setCommandInfo(getCommandNameTranslated(), varSet);
		return traceKleur;
	}
	
}
