package fi.javalogoweb;

import java.awt.*;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import logotekenap.Uitvoerblad;

public abstract class CommandComponent extends JPanel 
{
	JavaLogoSchuifVeld schuifveld;
	
	protected boolean vast;
	// protected String label;				// PBgv: deleted, unused. Also deleted setter, references 'if label != null'
	protected boolean caretUp, caretDown;
	protected boolean isStapel = true;
	
	protected String commandName;
	protected String commandNameTranslated;
			
	public boolean traceKleur;
	public Color traceActiveColor = new Color(255,200,200);
	
	// variables for handling mouse events: editting & dragging
	private boolean dragging = false;
	private int startx = 0;			// PBgv: position of mousePressed
	private int starty = 0;;
	private int startCompx = 0;		// PBgv: start position of CommandComponent at mousePressed
	private int startCompy = 0;
	private int dx = 0;				// PBgv: displacement through mouseDragged
	private int dy = 0;
	
	public CommandComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{	setBounds(x,y,b,h);
		setLayout(null);
		schuifveld = sv;
	}
	
	public String getCommandName()
	{
		return commandName;
	}
	
	public String getCommandNameTranslated()
	{
		return commandNameTranslated;
	}
	/**
	 * Standaard hebben nieuw gemaakte CC's de stapeleigenschap op true staan. Dat is onhandig
	 * bij importeren (maar handig bij pakken van de stapel). ProgrammaImporter moet het uit kunnen zetten.
	 */
	void clearStapel()
	{
		isStapel = false;
	}
	
	public boolean isStapel()
	{
		return isStapel;
	}
	
	public void zetVast(boolean b)
	{	vast = b;
	}
	
	/**
	 * Set caret on this CC
	 * Note: ProgrammaComponent will override to avoid carets.
	 * 
	 * @param y		the absolute ypos of the middle of the CC hovering over this CC
	 */
	public void setCaret(int y)
	{
		boolean downcaret = ( y-getAbsoluteLocation().y > getHeight()/2 );
		caretUp = !downcaret;
		caretDown = downcaret;
		((CommandContainer)getParent()).setInsert(this, downcaret);
	}
	
	/* unused
	public CommandComponent getCommandComponentAt(int x, int y)
	{	CommandComponent cc = null;
		Component c = getComponentAt(x,y);
		if(c!=this && c!=null && c instanceof CommandComponent) 
		{	cc = (CommandComponent)c;
			return cc.getCommandComponentAt(x - cc.getLocation().x,y - cc.getLocation().y);
			
		}
		
		return this;
	} */
	
	/**
	 * Geeft absolute positie van deze Component in het JavaLogoSchuifVeld
	 * 
	 * PBgv: omdat muisevents nu absoluut zijn, hebben we ook de absolute positie van componenten nodig.
	 * 
	 * @return absolute positie
	 */
	public Point getAbsoluteLocation()
	{
		Point p = getLocation();
		Component c = getParent();
		while ( c!=null && !( c instanceof JavaLogoSchuifVeld))
		{
			p.translate(c.getLocation().x, c.getLocation().y);
			c = c.getParent();
		}
		return p;
	}
	
	public void mousePressed(int x, int y, int modifiers)
	{	
		//requestFocus();
		if(vast)return;
		startx=x;								// PBgv: '+getLocation.x of y' removed 4x, ook bij dragged
		starty=y;
		Point p = getAbsoluteLocation();		// PBgv: remember startposition of component for further mouse action
		startCompx = p.x;
		startCompy = p.y;
		dx = 0;
		dy = 0;
		//editing = true;
		dragging = false;
	}
	
	private void moveComponent(int dx, int dy)
	{	int x = startCompx + dx;				// PBgv: new Location = original + mouse displacement
		int y = startCompy + dy;		
		if(schuifveld.isGesloten())
		{	x = Math.max(0, Math.min(x, schuifveld.getSize().width-getSize().width));
			y = Math.max(0, Math.min(y, schuifveld.getSize().height-getSize().height));
		}
		setLocation(x,y);
	}
	
	public void mouseDragged(int x, int y, int modifiers)
	{	if(vast)return;
		dx = x-startx;
		dy = y-starty;
		//System.out.println("dx = "+dx);
		//System.out.println("dy = "+dx);
		if(dx*dx+dy*dy>=20 || dragging) 
		{	// System.out.println("MuisDragged: "+e.getX()+", "+e.getY());
			if ( !dragging )		// start dragging a CC
			{	
				requestFocus();		// end possible editing of parameters, see ParameterTextField for details
				dragging = true;
				if(isStapel)
				{	schuifveld.zetStapel(this);		// get new copy from pile in GUI
					isStapel = false;
				}
				//schuifveld.begin();
				schuifveld.zetSchuiver(this);
			}
			schuifveld.traceComponent(this, x, y);
			moveComponent(dx, dy);
			schuifveld.repaint();
		}
	}
	
	/**
	 * Get the width of this CC when it is being dragged.
	 * Normally it will b e small, so you can see where you're putting it. This is not needed
	 * when arranging deeltaken in the ProgrammaPanel, so DeeltaakBodyc will override to retain 
	 * its original width
	 * 
	 * @return	width of this component when dragging it
	 */
	int getDragWidth()
	{
		return JavaLogoSchuifVeld.ccsw;
	}

	/**
	 * Standard CC's enable tracing (carets), but DeeltaakBody's won't (will override to return false)
	 * 
	 * @return true, if we want to seee carets while dragging
	 */
	boolean isTraceable()
	{
		return true;
	}
	
	/**
	 * Drop this component on the JavaLogoSchuifVeld. Usually this means finding the CommandContainer
	 * that will receive this component.
	 * DeeltaakBody's will override to allow the user to move the bodies in the programmaPanel
	 * 
	 * @param x
	 * @param y
	 */
	protected void dropComponent(int x, int y)
	{
		schuifveld.losSchuiver(this, x, y);
		// PBgv: quick fix voor zwevende Commands: als ie op JavaLogoSchuifVeld zelf staat (en niet in een of andere
		//   CommandContainer, dan wordt ie verwijderd
		if( getParent()==schuifveld && !isStapel)
		{	schuifveld.verwijder(this);
		}
	}
	
	public void mouseReleased(int x, int y, int modifiers)
	{	
		if( !dragging && !isStapel) 						// PBgv: !isStapel toegevoegd: niet editten van componenten links
		{	
			// editing of CCs that are 'vast' is allowed: name of 'deeltaak'.
			if ( this instanceof ParameterEditorListener)
			{	
				((ParameterEditorListener)this).parameterComponentClicked(x-getAbsoluteLocation().x, y-getAbsoluteLocation().y);
			} else
			{
				requestFocus();	// end possible editing of parameters, see ParameterTextField for details
			}
		}
		else if ( !vast )
		{ 	
			dropComponent(x, y);
		}
	}
		
	public void tekenOpnieuw()
	{	schuifveld.repaint();
	}
	
	/**
	 * Paint the background of the CommandComponent: rectangles, bgcolor
	 * 
	 * @param g the Graphics context
	 */
	protected abstract void paintBackground(Graphics g);
	
	/**
	 * Paint the text of the CommandComponent: command name and parameters that are not being editted
	 * For the composite components this will be: repetitions for loop / condition / deeltaaknaam / tekenalgoritme
	 * 
	 * @param g
	 */
	protected abstract void paintCommand(Graphics g);

	/**
	 * Paint caret lines (when dragging a CommandComponent)
	 * Can be implemented here, since we only draw carets at top or bottom of CComponent.
	 * 
	 * @param g
	 */
	private void paintCaret(Graphics g)
	{
		g.setColor(Color.green);
		if(caretUp)
		{	g.drawLine(2,2,getSize().width-3,2);
			g.drawLine(2,3,getSize().width-3,3);
			caretUp = false;
		}
		if(caretDown)
		{	g.drawLine(2,getSize().height-3,getSize().width-3,getSize().height-3);
			g.drawLine(2,getSize().height-4,getSize().width-3,getSize().height-4);
			caretDown = false;
		}
	}
	
	/**
	 * Painting of the CComponent in three parts, that are implemented at various levels in class hierarchy
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		paintBackground(g);
		paintCommand(g);
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		paintCaret(g);
	}
	
	public abstract boolean execute(Uitvoerblad ub, VarSet varSet);
	
	public abstract String getCode(String tab);


}
