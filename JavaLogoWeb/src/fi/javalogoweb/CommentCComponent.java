package fi.javalogoweb;

import java.awt.Color;
import java.awt.Graphics;

import logotekenap.Uitvoerblad;

public class CommentCComponent extends ParameterCommandComponent
{
	
	public CommentCComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{	
		super(x,y,b,h,sv);
		parameter1 = new TextParameter();
		commandName = "#";
		commandNameTranslated = (commandName);
		createEditor();
		strBeforeEditor = " ";
		strAfterEditor = "";
	}
	
	public boolean execute(Uitvoerblad ub, VarSet varSet)
	{	
		return false;
	}
	@Override
	public String getCode(String tab)
	{	
		String s = tab + getCommandName() + strBeforeEditor + getFullParameterText() + strAfterEditor + "\n";
		return s;
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
			g.drawString(getCommandNameTranslated() + strBeforeEditor + getFullParameterText() + strAfterEditor, 10, 18);
		}
	}

}