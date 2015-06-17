package fi.javalogoweb;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import fi.beans.stringutils.StringUtils;
import fi.javalogoweb.expressies.BasisExpressie;
import fi.javalogoweb.expressies.Expressie;
import fi.javalogoweb.formuleobjects.FormuleParser;
import logotekenap.Uitvoerblad;

/**
 * NStap: experiment voor het editten van twee parameters, waarbij je ze samen kunt editten als 'x,y'
 * maar ook apart.
 * 
 * @author berge020
 */
public class StapCComponent extends ParameterCommandComponent implements ParameterEditorListener
{
	protected TAParameter parameter2;
	protected int separatorX;
	private boolean editingFirstParam;
	
	public StapCComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{
		super(x, y, b, h, sv);
		
		// hier even volledig geïmplementeerd voor de stap-opdracht
		commandName = "stap";
		commandNameTranslated = JavaLogoWeb.rb.getString(commandName);
		parameter1 = new NumericParameter();
		parameter2 = new NumericParameter();
		fm = getFontMetrics(JavaLogoWeb.defaultfont);
		createEditor();
		separatorX=10+fm.stringWidth(getCommandName()+strOpen+"0");
	}

	@Override
	public void parameterEdited(String text)
	{
		if ( editingFirstParam )
		{
			parameter1.setParameter(text);
		} else
		{
			parameter2.setParameter(text);
		}
		isEditing = false;
		strBeforeEditor = strOpen;
		strAfterEditor = strClose;
		separatorX = 10+fm.stringWidth(getCommandName()+strOpen+parameter1.getParameterText());
		schuifveld.repaint();
	}
	
	void editParameter(boolean first)
	{
		isEditing = true;
		editingFirstParam = first;
		if ( first )
		{
			strBeforeEditor = strOpen;
			strAfterEditor = ", "+parameter2.getParameterText()+strClose;
			paramEditor.setLocation(10+fm.stringWidth(getCommandName()+strBeforeEditor), paramEditor.getY());
			separatorX = paramEditor.getX()+paramEditor.getWidth();
			paramEditor.vulIn(parameter1.getParameterText());
		} else
		{
			strBeforeEditor = "( "+parameter1.getParameterText()+", ";
			strAfterEditor = " )";
			paramEditor.setLocation(10+fm.stringWidth(getCommandName()+strBeforeEditor), paramEditor.getY());
			separatorX = paramEditor.getX();
			paramEditor.vulIn(parameter2.getParameterText());
		}
	}

	@Override
	public void parameterComponentClicked(int x, int y)
	{
		boolean newEdit;
		boolean onFirst = ( x < separatorX );
		if ( isEditing )
		{
			if ( editingFirstParam )
			{
				newEdit = !onFirst;				// newEdit true: going from first to second param
				parameterEdited(paramEditor.getText());
			} else
			{
				newEdit = onFirst;				// newEdit true: going from second to first param
				parameterEdited(paramEditor.getText());
			}
		} else
		{
			newEdit = true;					// we weren't editing anything, so start
		}
		if ( newEdit )
		{	
			editParameter(onFirst);
		} else
		{
			paramEditor.setVisible(false);
			paramEditor.setEnabled(false);
			separatorX = fm.stringWidth(getCommandName()+strOpen+parameter1.getParameterText());
		}
		schuifveld.repaint();
	}
	
	@Override
	protected String getFullParameterText()
	{
		return parameter1.getParameterText() + ", " + parameter2.getParameterText();
	}
	
	/**
	 * Set parameter directly (ProgrammaImporter)
	 * 
	 * @param s
	 */
	void setParameter(String s)
	{
		String parts[] = StringUtils.split(s, ",");
		if ( parts.length>0 )
			parameter1.setParameter(parts[0]);
		if ( parts.length>1 )
			parameter2.setParameter(parts[1]);
	}
	
	@Override
	protected boolean isCorrect()
	{
		return parameter1.isCorrect() && parameter2.isCorrect();
	}
	
	@Override
	public boolean execute(Uitvoerblad ub, VarSet varSet)
	{
		if ( !(parameter1.isCorrect(varSet) && parameter2.isCorrect(varSet)) ) return false; 
		double valueX = ((NumericParameter)parameter1).getValue();
		double valueY = ((NumericParameter)parameter2).getValue();
		traceKleur = ub.stap(valueX, valueY);
		if(traceKleur)schuifveld.updateView(varSet);
		return traceKleur;
	}

}
