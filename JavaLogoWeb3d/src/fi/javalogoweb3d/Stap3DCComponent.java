package fi.javalogoweb3d;

import java.awt.Color;
import java.awt.Graphics;

import fi.javalogoweb3d.parameters.NumericParameter;
import fi.logotekenap3d.TraceBeheerder;
import fi.logotekenap3d.TekenApplet3D;

/**
 * 
 * @author huub
 */
public class Stap3DCComponent extends ParameterCommandComponent implements ParameterEditorListener
{
	//protected int separatorX;
	//private boolean editingFirstParam;
	
	public Stap3DCComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{
		super(x, y, b, h, sv);
		
		commandName = "stap3d";
		commandNameTranslated = JavaLogoWeb3d.rb.getString(commandName);
		noParameters = 3;
		parameters[0] = new NumericParameter();
		parameters[1] = new NumericParameter();
		parameters[2] = new NumericParameter();
	}

	protected void paintCommand(Graphics g)
	{
		if (getParent() == schuifveld)
		{	
			g.setFont(JavaLogoWeb3d.defaultfont);
			g.setColor(Color.black);
			g.drawString(getCommandNameTranslated() + strOpen + strClose, 10, 18);
		}
		else 
			super.paintCommand(g);
	}

	@Override
	public boolean execute(TraceBeheerder trb, TekenApplet3D ub, VarSet varSet)
	{
		if (!(parameters[0].isCorrect(varSet) && parameters[1].isCorrect(varSet) && parameters[2].isCorrect(varSet))) 
			return false; 
		double valueX = ((NumericParameter)parameters[0]).getValue();
		double valueY = ((NumericParameter)parameters[1]).getValue();
		double valueZ = ((NumericParameter)parameters[2]).getValue();
		ub.stap(valueX, valueY,valueZ);
		traceKleur = trb.commandExecuted(varSet.getLevel());
		if (traceKleur ) 
			trb.setCommandInfo(getActualCall(), varSet);
		return traceKleur;
	}

}
