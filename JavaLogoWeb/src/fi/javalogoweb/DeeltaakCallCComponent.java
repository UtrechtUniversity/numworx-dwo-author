package fi.javalogoweb;

import java.awt.Graphics;

import logotekenap.Uitvoerblad;

/**
 * CC for a 'deeltaak call' so this is not the deeltaak itself but the little block you
 * can drag around to drop in your algoritm
 * ToDo
 * - fix problem with fetching the name from the body header
 * - test if deeltaak has a parameter. If so, don't edit
 * - create local var at run: works ok, todo is right name etc
 * - traverse order in varset when looking up vers, also executing!!!
 * 
 * @author Berge020
 */
public class DeeltaakCallCComponent extends ParameterCommandComponent
{
	private DeeltaakBodyComponent deeltaakBody;
	
	public DeeltaakCallCComponent(int x, int y, int b, int h, int index, JavaLogoSchuifVeld sv)
	{	
		super(x,y,b,h,sv);
		String commandNameBase = "deeltaak";
		commandName = commandNameBase+index;
		commandNameTranslated = JavaLogoWeb.rb.getString(commandNameBase)+index;
		parameter1 = new NumericParameter();
		createEditor();
		// no explicit construction of body. This is done separately in JavaLogoSchuifVeld.initialize()
		// and ProgrammaImporter, because in the latter case it's 'names first, bodies later'
	}
	
	public DeeltaakCallCComponent(DeeltaakCallCComponent dcc, JavaLogoSchuifVeld sv)
	{
		super(dcc.getX(), dcc.getY(), dcc.getWidth(), dcc.getHeight(), sv);
		commandName = dcc.getCommandName();
		deeltaakBody = dcc.getBody();
		parameter1 = new NumericParameter();
		createEditor();
	}
	
	public DeeltaakBodyComponent getBody()
	{	return deeltaakBody;
	}
	
	// vanuit Importer..
	public void setBody(DeeltaakBodyComponent pc)
	{	deeltaakBody = pc;
	}
	
	/**
	 * Gets the command name from the DeeltaakBodyComponent, since it is there that the user
	 * can change the name of a Deeltaak.
	 * Name may be incorrect or body not yet created (import). In that case, return the 
	 * default name 'deeltaaki'.
	 * 
	 * @return		the name of the deeltaak
	 */
	@Override
	public String getCommandName()
	{	
		if ( deeltaakBody != null )
		{
			if ( deeltaakBody.isNameValid() )
				return deeltaakBody.getProgramName();
		} 
		return commandName ;		
	}
	
	public String getCommandNameTranslated()
	{	
		if ( deeltaakBody != null )
		{
			if ( deeltaakBody.isNameValid() )
				return deeltaakBody.getProgramName();
		} 
		return commandNameTranslated ;		
	}
	
	/**
	 * Get the paramtertext if deeltaak has a parameter. If not, return empty string
	 * @see fi.javalogoweb.ParameterCommandComponent#getFullParameterText()
	 */
	@Override
	protected String getFullParameterText()
	{
		if ( !deeltaakBody.hasParameter() ) return "";
		return parameter1.getParameterText();
	}
	
	/**
	 * Handle mouse event that starts parameter editting. Don't edit when deeltaak doesn't have
	 * a parameter, follow normal procedure otherwise.
	 * 
	 * @see fi.javalogoweb.ParameterCommandComponent#parameterComponentClicked(int, int)
	 */
	@Override
	public void parameterComponentClicked(int x, int y)
	{
		if ( !deeltaakBody.hasParameter() ) return;
		super.parameterComponentClicked(x, y);
	}

	
	@Override
	protected void paintBackground(Graphics g)
	{
		super.paintBackground(g);
		g.drawRect(5,1,getSize().width-11,getSize().height-1);
		g.drawRect(6,1,getSize().width-13,getSize().height-3);		
	}

/*	@Override
	protected void paintCommand(Graphics g)
	{
		g.setFont(JavaLogoWeb.defaultfont);
		g.setColor(Color.black);
		g.drawString(getCommandName(),20,18);
		
	}
*/	
	@Override
	public boolean execute(Uitvoerblad ub, VarSet varSet)
	{	
		varSet.increaseLevel("Deeltaak: "+getCommandName());
		if ( deeltaakBody.hasParameter() )
		{
			// test value of parameter, supplied at this call
			if ( !parameter1.isCorrect(varSet) ) return false; 
			// get parameter name form body and add local var with value of parameter (call by value)
			varSet.setParameter(deeltaakBody.getParameterName(), ((NumericParameter)parameter1).getValue());		
		}
		traceKleur  = deeltaakBody.execute(ub, varSet);
		//if (traceKleur ) schuifveld.traceVariables(varSet);
		varSet.decreaseLevel();
		return traceKleur;
	}
	
/*	@Override
	public String getCode(String tab)
	{	String s = tab + getCommandName() + "\n";
		return s;
	}
*/
}