package fi.javalogoweb3d;

import java.awt.Graphics;

import fi.javalogoweb3d.parameters.NumericParameter;
import fi.logotekenap3d.TraceBeheerder;
import fi.logotekenap3d.TekenApplet3D;

/**
 * CC for a 'deeltaak call' so this is not the deeltaak itself but the little block you
 * can drag around to drop in your algoritm
 * 
 * @author Berge020
 */
public class DeeltaakCallCComponent extends ParameterCommandComponent
{
	private DeeltaakBodyComponent deeltaakBody;
	
	/**
	 * Creates a new DeeltaakCallCComponent witn zero parameters (typically called at startup)
	 * 
	 * @param x
	 * @param y
	 * @param b
	 * @param h
	 * @param index
	 * @param sv
	 */
	public DeeltaakCallCComponent(int x, int y, int b, int h, int index, JavaLogoSchuifVeld sv)
	{	
		super(x,y,b,h,sv);
		String commandNameBase = "deeltaak";
		commandName = commandNameBase+index;
		commandNameTranslated = JavaLogoWeb3d.rb.getString(commandNameBase)+index;
		noParameters = 0;
		// no explicit construction of body. This is done separately in JavaLogoSchuifVeld.initialize()
		// and ProgrammaImporter, because in the latter case it's 'names first, bodies later'
	}
	
	/**
	 * Creates a DCC from another one, with the right number of parameters
	 * Typically used when picking up a DDC from the 'pile'
	 * 
	 * @param dcc
	 * @param sv
	 */
	public DeeltaakCallCComponent(DeeltaakCallCComponent dcc, JavaLogoSchuifVeld sv)
	{
		super(dcc.getX(), dcc.getY(), dcc.getWidth(), dcc.getHeight(), sv);
		commandName = dcc.getCommandName();
		commandNameTranslated = dcc.getCommandNameTranslated();
		deeltaakBody = dcc.getBody();
		// noParameters will be zero after call of constructor of ParameterCC.
		checkParameterList();
	}
	
	public DeeltaakBodyComponent getBody()
	{	
		return deeltaakBody;
	}
	
	// vanuit Importer..
	public void setBody(DeeltaakBodyComponent pc)
	{	
		deeltaakBody = pc;
		checkParameterList();
	}
	
	/**
	 * Gets the command name from the DeeltaakBodyComponent, since it is there that the user
	 * can change the name of a Deeltaak.
	 * Name may not yet created (import). In that case, return the default name 'deeltaaki'.
	 * Note: also return incorrect name for editing.
	 * 
	 * @return		the name of the deeltaak
	 */
	@Override
	public String getCommandName()
	{	
		if ( deeltaakBody != null )
		{
			return deeltaakBody.getProgramName();
		} 
		return commandName ;		
	}
	
	public String getCommandNameTranslated()
	{	
		if ( deeltaakBody != null )
		{
			return deeltaakBody.getProgramName();
		} 
		return commandNameTranslated ;		
	}
	
	@Override
	protected boolean isCorrect()
	{
		if ( !deeltaakBody.isHeaderValid() ) return false;
		return super.isCorrect();
	}
	
	@Override
	protected String getFullParameterText()
	{
		checkParameterList();						// this implies checking for updates at every repaint
		return super.getFullParameterText();
	}
	
	@Override
	protected void paintBackground(Graphics g)
	{
		super.paintBackground(g);
		g.drawRect(5,1,getSize().width-11,getSize().height-1);
		g.drawRect(6,1,getSize().width-13,getSize().height-3);		
	}
	
	private void checkParameterList()
	{
		int np = deeltaakBody.getParameterCount();
		if ( np > noParameters )
		{
			for ( int i=noParameters; i<np; i++ )
			{
				parameters[i] = new NumericParameter();
			}
		}
		noParameters = np;
	}

	@Override
	public boolean execute(TraceBeheerder trb, TekenApplet3D ub, VarSet varSet)
	{	
		// don't run a deeltaak with invalid name
		if ( !deeltaakBody.isHeaderValid() ) return false;
		// don't start execution if any of the parameters is incorrect with the given VarSet
		for ( int i=0; i<noParameters; i++ )
		{
			if ( !parameters[i].isCorrect(varSet) ) return false;
		}
		varSet.increaseLevel("-- "+getActualCall(), true);
		for ( int i=0; i<noParameters; i++ )
		{
			// get parameter name form body and add local var with value of parameter (call by value)
			varSet.setParameter(deeltaakBody.getParameterName(i), ((NumericParameter)parameters[i]).getValue());		
		}
		// deeltaak call starting is a step, we may stop at that
		// Note that execution is stopped now, no need to match increase and decrese of VarSet level.
		traceKleur = trb.commandExecuted(varSet.getLevel());
		if ( traceKleur ) 
		{	
			trb.setCommandInfo(getActualCall(), varSet);
			return traceKleur;
		}
		// execution may also stop at a command in the body. Call will be pink then, too.
		traceKleur = deeltaakBody.execute(trb, ub, varSet);
		varSet.decreaseLevel();
		return traceKleur;
	}
	
}