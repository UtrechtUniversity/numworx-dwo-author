package fi.javalogoweb;

import java.awt.Component;

import logotekenap.Uitvoerblad;

public class ForLoopCommandComponent extends LoopCommandComponent
{

	public ForLoopCommandComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{
		super(x, y, b, h, sv);
		
		loopCondition = new NumericParameter();
		commandName = "Herhaal";
		commandNameTranslated = JavaLogoWeb.rb.getString(commandName);
		naString = " keer";
		naStringTranslated = JavaLogoWeb.rb.getString(naString);
		createLoopEditor();
	}

	@Override
	public boolean executeContent(Uitvoerblad ub, VarSet varSet)
	{	
		if ( !loopCondition.isCorrect(varSet) ) return false; 
		for(int i=0 ; i<(int) ((NumericParameter)loopCondition).getValue() ; i++)
		{	
			for(int j=0 ; j<loopBlock.getComponentCount() ; j++)
			{	
				Component c = loopBlock.getComponent(j);
				if(c instanceof CommandComponent)
				{	boolean tracekleur = ((CommandComponent)c).execute(ub, varSet);
					if(tracekleur) return true;
				}
			}
		}
		return false;
	}
	

}
