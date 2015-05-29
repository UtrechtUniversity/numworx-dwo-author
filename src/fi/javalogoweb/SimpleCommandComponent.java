package fi.javalogoweb;

import java.awt.Color;
import java.awt.Graphics;

import logotekenap.Uitvoerblad;

public abstract class SimpleCommandComponent extends CommandComponent
{
	public SimpleCommandComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{
		super(x, y, b, h, sv);
	}
	
	/**
	 * Paint background of simple command: just a rectangle, fits all simple (non-composite) CCommands.
	 * 
	 * @see fi.javalogoweb.CommandComponent#paintBackground(java.awt.Graphics)
	 */
	@Override
	protected void paintBackground(Graphics g)
	{
		g.setColor(new Color(238,238,238));
		if(traceKleur)g.setColor(traceActiveColor);
		g.fillRect(0,0,getSize().width-1,getSize().height-1);
		g.setColor(Color.black);
		g.drawRect(0,0,getSize().width-1,getSize().height-1);
		g.drawRect(1,1,getSize().width-3,getSize().height-3);		
	}
	
	/**
	 * Implemented here for CCommands without parameters.
	 * CCommands with parameters must override to display parameters.
	 * 
	 * @see fi.javalogoweb.CommandComponent#paintCommand(java.awt.Graphics)
	 */
	@Override
	protected void paintCommand(Graphics g)
	{
		g.setFont(JavaLogoWeb.defaultfont);
		g.setColor(Color.black);
		g.drawString(commandNameTranslated+"( )",10,18);
	}

	/**
	 * Get a string value of the command in this componenent. 
	 * Implemented here for covenience so simple commands don't need to override.
	 * Parameter commands, however, must override
	 * 
	 * @see fi.javalogoweb.CommandComponent#getCode(java.lang.String)
	 */
	@Override
	public String getCode(String tab)
	{	
		String s = tab + commandName+"( )" + "\n";
		return s;
	}
}
