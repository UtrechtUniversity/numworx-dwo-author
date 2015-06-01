package fi.javalogoweb;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import fi.javalogoweb.expressies.*;
import fi.javalogoweb.formuleobjects.*;
import logotekenap.Uitvoerblad;

public abstract class ParameterCommandComponent extends SimpleCommandComponent implements ParameterEditorListener
{
	protected TAParameter parameter1;

	protected boolean isEditing = false;
	protected ParameterTextField paramEditor;

	protected FontMetrics fm;

	/**
	 * Strings opening and closing the parameterpart of the string representation of this CC.
	 */
	protected static final String strOpen = "( ";
	protected static final String strClose = " )";
	
	/**
	 * String for the part of the parameter section to be painted in front of the TextField when editing.
	 * Generally this is just strOpen, but when there are 2+ parameters, this will also hold the first param
	 * when editting the second.
	 */
	protected String strBeforeEditor;
	/**
	 * String for the part of the parameter section to be painted after the TextField when editing.
	 * Generally this is just strClose, but when there are 2+ parameters, this will also hold the second param
	 * when editting the first.
	 */
	protected String strAfterEditor;

	public ParameterCommandComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{
		super(x, y, b, h, sv);
	}
	
	/**
	 * Create parameter field, strings. Part of constructor, but can not be put there because
	 * commandName hasn't been set. Subclasses must call this after setting the command name. Hmmm...
	 */
	protected void createEditor()
	{
		strBeforeEditor = strOpen;
		strAfterEditor = strClose;
		fm = getFontMetrics(JavaLogoWeb.defaultfont);
		paramEditor = new ParameterTextField(10, 4, 60, 17, this);
		add(paramEditor);		
	}
	
	protected String frontString()
	{
		return commandName+"( ";
	}

	@Override
	public void parameterEdited(String text)
	{
		parameter1.setParameter(text);
		isEditing = false;
		schuifveld.repaint();
	}

	@Override
	public void parameterComponentClicked(int x, int y)
	{
		// if the component is clicked again when editing, stop the editor
		if ( isEditing )
		{
			parameter1.setParameter(paramEditor.getText());
			isEditing = false;
			paramEditor.setVisible(false);
			paramEditor.setEnabled(false);
		} else
		{
			isEditing = true;
			paramEditor.setLocation(10+fm.stringWidth(getCommandName()+strOpen), 4);
			paramEditor.vulIn(parameter1.getParameterText());
		}
		schuifveld.repaint();
	}
	
	/**
	 * Set parameter directly (ProgrammaImporter)
	 * 
	 * @param s
	 */
	void setParameter(String s)
	{
		parameter1.setParameter(s);
	}
	
	/**
	 * Get the parameter text. Simple here, but CC's with >1 parameter will override
	 * 
	 * @return 	combined parameter texts
	 */
	protected String getFullParameterText()
	{
		return parameter1.getParameterText();
	}
	
	/**
	 * Checks is parameter is ok. Simple here, but CC's with >1 parameter will override
	 * 
	 * @return	boolean
	 */
	protected boolean isCorrect()
	{
		return parameter1.isCorrect();
	}
	
	@Override
	protected void paintCommand(Graphics g)
	{
		g.setFont(JavaLogoWeb.defaultfont);
		g.setColor(Color.black);
		if ( isEditing )
		{
			g.drawString(getCommandNameTranslated()+strBeforeEditor, 10, 18);
			g.drawString(strAfterEditor, paramEditor.getX()+paramEditor.getWidth()+1, 18);		
		} else
		{
			if ( !isCorrect() )g.setColor(Color.RED);
			g.drawString(getCommandNameTranslated() + strOpen +  getFullParameterText() + strClose, 10, 18);
		}
	}

	/**
	 * Get a string value of the command in this componenent. 
	 * Implemented here for convenience so simple parameter commands don't need to override.
	 * More complex ones (color?), however, must override
	 * 
	 * @see fi.javalogoweb.CommandComponent#getCode(java.lang.String)
	 */
	@Override
	public String getCode(String tab)
	{	
		String s = tab + getCommandName() + strOpen +  getFullParameterText() + strClose + "\n";
		return s;
	}
}
