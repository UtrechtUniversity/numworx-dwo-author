package fi.javalogoweb3d;

import java.awt.Component;

import fi.javalogoweb3d.parameters.NumericParameter;
import fi.logotekenap3d.TraceBeheerder;
import fi.logotekenap3d.TekenApplet3D;

public class ForLoopCommandComponent extends LoopCommandComponent
{

	public ForLoopCommandComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{
		super(x, y, b, h, sv);
		
		loopCondition = new NumericParameter();
		commandName = "Herhaal";
		commandNameTranslated = JavaLogoWeb3d.rb.getString(commandName);
		naString = " keer";
		naStringTranslated = JavaLogoWeb3d.rb.getString(naString);
		createLoopEditor();
	}

	@Override
	public boolean executeContent(TraceBeheerder trb, TekenApplet3D ub, VarSet varSet)
	{	
		if ( !loopCondition.isCorrect(varSet) ) return false; 
		traceKleur = trb.commandExecuted(varSet.getLevel());
		if ( traceKleur ) 
		{
			trb.setCommandInfo(getCommandNameTranslated()+" "+loopCondition.getValueText()+" "+naStringTranslated, varSet);
			return traceKleur;
		}
		for(int i=0 ; i<(int) ((NumericParameter)loopCondition).getValue() ; i++)
		{	
			for(int j=0 ; j<loopBlock.getComponentCount() ; j++)
			{	
				Component c = loopBlock.getComponent(j);
				if(c instanceof CommandComponent)
				{	boolean tracekleur = ((CommandComponent)c).execute(trb, ub, varSet);
					if(tracekleur) return true;
				}
			}
		}
		return false;
	}
	

}
