package fi.javalogoweb;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import logotekenap.Uitvoerblad;
import fi.beans.stringutils.StringUtils;
import fi.javalogoweb.expressies.*;

public class VarInputComponent extends SimpleCommandComponent implements ParameterEditorListener
{
	private TextParameter waarde;
	private IdentifierParameter varnaamParam;
	
	private boolean editingName = false;
	private boolean editingValue = false;
	private ParameterTextField paramEditor;
	
	private int separatorX;
	private String equalsString = " = input( ";
	private int equalsWidth;
	FontMetrics fm;
	
	public VarInputComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{	
		super(x,y,b,h,sv);
		commandName = "variabele = input( )";				// is virtually irrelevant, parameter holds the real name
		commandNameTranslated = "input";
		waarde = new TextParameter();
		varnaamParam = new IdentifierParameter("variabele");
		
		fm = getFontMetrics(JavaLogoWeb.defaultfont);
		separatorX = 10+fm.stringWidth("variabele" +equalsString);
		equalsWidth = fm.stringWidth(equalsString);
		paramEditor = new ParameterTextField(10, 4, 60, 17, this);
		add(paramEditor);		
	}
	
	@Override
	public void parameterEdited(String text)
	{
		if ( editingName )
		{
			varnaamParam.setParameter(text);
			editingName = false;
			separatorX = 10+fm.stringWidth(varnaamParam.getParameterText()+equalsString);
		} 
		else if ( editingValue )
		{
			waarde.setParameter(text);
			editingValue = false;
		}
		schuifveld.repaint();
	}

	public void editParameter(boolean name)
	{
		if ( name )
		{
			paramEditor.setLocation(10, 4);
			paramEditor.vulIn(varnaamParam.getParameterText());
			separatorX = paramEditor.getX()+paramEditor.getWidth()+1;
			editingName = true;
		}
		else
		{
			paramEditor.setLocation(separatorX+2, 4);
			paramEditor.vulIn(waarde.getParameterText());
			editingValue = true;
		}
		//schuifveld.tekenOpnieuw();
	}

	/**
	 * Determine what to edit given the click on pos x,y
	 * 
	 * This method looks a bit messy because the flow is:
	 * 1. when currently not editing: edit the part nearest to x
	 * 2. when editing a part and x is near the other part: switch editing to the other part
	 * 3. when editing a part and x is near the same part: stop editing.
	 * 
	 * @see fi.javalogoweb.ParameterEditorListener#parameterComponentClicked(int, int)
	 */
	@Override
	public void parameterComponentClicked(int x, int y)
	{
		boolean newEdit;
		boolean onName = ( x < separatorX );
		if ( editingName )
		{
			newEdit = !onName;				// newEdit true: going from name to value
			parameterEdited(paramEditor.getText());
		}
		else if ( editingValue )
		{
			newEdit = onName;				// newEdit true: going from value to name
			parameterEdited(paramEditor.getText());
		} else
		{
			newEdit = true;					// we weren't editing anything, so start
		}
		if ( newEdit )
		{	
			editParameter(onName);
		} else
		{
			paramEditor.setVisible(false);
			paramEditor.setEnabled(false);
			
		}
		schuifveld.repaint();
	}
	
	/**
	 * Set varnamee & expression directly (ProgrammaImporter)
	 * 
	 * @param name
	 * @param exp
	 */
	void setVariable(String name, String exp)
	{
		varnaamParam.setParameter(name.trim());
		separatorX = 10+fm.stringWidth(varnaamParam.getParameterText()+equalsString);
		waarde.setParameter(exp);
	}
	
	public boolean execute(Uitvoerblad ub, VarSet varSet)
	{	
		// don't add to VarSet when name is wrong or expression is wrong 
		// determine the correctness of the expression for real, with the current varSet!
		if ( !(varnaamParam.isCorrect()  && waarde.isCorrect(varSet)) ) return false; 

		Expressie expressie = new BasisExpressie(0.0);
		varSet.setVar(varnaamParam.getParameterText(), expressie);		
		traceKleur = ub.varAanpassing(varnaamParam.getParameterText(),Double.toString(expressie.geefWaarde()));
		if ( traceKleur ) 
		{
			schuifveld.updateView(varSet);
		}
		return traceKleur;
	}
	
	@Override
	protected void paintCommand(Graphics g)
	{
		g.setFont(JavaLogoWeb.defaultfont);
		g.setColor(Color.BLACK);
		if ( editingName )
		{
			g.drawString(equalsString+waarde.getParameterText() + " )", separatorX, 18);		
		}
		else if ( editingValue )
		{
			g.drawString(varnaamParam.getParameterText()+equalsString, 10, 18);
			g.drawString(")", separatorX+10+paramEditor.getWidth(), 18);
		} else
		{	// paint parts of the equation in RED if they are incorrect;
			if ( !varnaamParam.isCorrect() ) g.setColor(Color.RED);
			g.drawString(varnaamParam.getParameterText(), 10, 18);
			g.setColor(Color.BLACK);
			if ( !(varnaamParam.isCorrect()  && waarde.isCorrect()) ) g.setColor(Color.RED);
			g.drawString(equalsString, separatorX-equalsWidth, 18);
			g.setColor(Color.BLACK);
			if ( !waarde.isCorrect() )g.setColor(Color.RED);
			g.drawString(waarde.getParameterText() + " )", separatorX, 18);
		}
	}
	
	public String getCode(String tab)
	{	String s = tab + varnaamParam.getParameterText()+equalsString+waarde.getParameterText() + " )" + "\n";
		return s;
	}	
}
