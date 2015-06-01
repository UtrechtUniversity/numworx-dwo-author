package fi.javalogoweb;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

import logotekenap.*;

public class ProgrammaComponent extends CompositeCommandComponent implements MouseWheelListener, MouseListener, MouseMotionListener
{	
	protected String defaultName = "";
	protected CommandContainer commandBlock;
	
	protected JPanel maskPanel;
	
	private Polygon arrowOut;
	private Polygon arrowIn;
	
	/**
	 * Height of header (name+buttons)
	 */
	public static final int headerHeight = 25;
	
	/**
	 * ProgrammaComponent Small Width: width when PC is narrow
	 */
	public static final int pcsw = 180;
	
	/**
	 * ProgrammaComponent Large Width: width when PC is made wide
	 */
	public static final int pclw = 320;
	
	/**
	 * ProgrammaComponent height when closed
	 */
	public static final int pcclosedh = headerHeight+10;
	
	/**
	 * ProgrammaComponent minimum open height 
	 */
	public static final int pcminoh = 160;
	
	/**
	 * ProgrammaComponent maximum open height 
	 */
	public static final int pcmaxoh = 320;
	
	/**
	 * Indicates if the user can change te height of this PC. False for the main algorithm, true for deeltaken.
	 * Note: we handle height changes in this class, rather than in DeeltaakBody, to concentrate all associated
	 * mouse handling in one class...
	 */
	protected boolean isHeightFixed = true;
	
	/**
	 * indicates if this component has been collapsed to it's header 
	 */
	private boolean isOpen = false;
	
	/**
	 * 
	 */
	private boolean isWide;
	/**
	 * narrowX remembers the original xpos of the PC, when the PC is made wide, because
	 * generally the PC will have to move left to fit inside the ProgrammaPanel
	 */
	private int narrowX;
	
	/**
	 * previousX & -Y remember the old location of the component when it is dragged.
	 * When the user drags the PC outside the programmaPanel, it is simply put back
	 */
	protected int previousX;
	protected int previousY;
	
	public ProgrammaComponent(int x, int y, int b, int h, String pn, JavaLogoSchuifVeld sv)
	{	
		super(x,y,b,h,sv);
		defaultName = pn;
		isStapel = false;
		isWide = false;
		narrowX = x;
		
		maskPanel = new JPanel();
		maskPanel.setBounds(0, headerHeight, b, h-headerHeight-1);
		maskPanel.setLayout(null);
		add(maskPanel);
		addMouseWheelListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

		commandBlock = new CommandContainer(0, 0, b, h-headerHeight-1, this);
		maskPanel.add(commandBlock);
		
		createHArrows();
	}
	
	void clearProgram()
	{
		commandBlock.removeAll();
	}
	
	/**
	 * Gets the name of this ProgrammaComponent.
	 * In the base class this is the default name. Subclasses must override to return the name
	 * typed by the user from their IdentifierParameter.
	 * 
	 * @return	the default name for this ProgrammaComponent
	 */
	public String getProgramName()
	{
		return defaultName;
	}

	public void addCComponent(CommandComponent cc)
	{	
		commandBlock.addCComponent(cc);
	}
	
	/**
	 * PC's don't resize when user drops or removes CC's. Resizing is left to the user,
	 * when the containers get too big, the scroll wheel will work.
	 * 
	 * @see fi.javalogoweb.CompositeCommandComponent#containerHeightChanged(int)
	 */
	@Override
	void containerHeightChanged(int h)
	{	}
	
	/**
	 * This method also adjusts the sizes of the CommandContainer and the maskPanel.
	 * 
	 * @see java.awt.Component#setSize(int, int)
	 */
	@Override
	public void setSize(int w, int h)
	{
		int cbh = commandBlock.getHeight();
		super.setSize(w, h);
		maskPanel.setSize(w, h-headerHeight-1);
		if ( h-headerHeight-1 > cbh )
		{
			// on open, heightt may be greater than the content. Increase height of container
			// to avoid 'grey rectangle' in this component.
			commandBlock.setSize(commandBlock.getWidth(), h-headerHeight-1);
		}
		commandBlock.setLocation(0, 0);
		commandBlock.setWidth(w);
		// also set minimum height, so container won't reduce height when rearranging the components
		commandBlock.setMinimumHeight(h-headerHeight-1);
	}
	
	/**
	 * Change the width when arrow in header is clicked: alternate between two fixed widths
	 */
	private void changeWidth()
	{
		if ( isWide )
		{
			setSize(pcsw, getHeight());
			setLocation(narrowX, getY());
			isWide = false;
		} else
		{
			setSize(pclw, getHeight());
			narrowX = getX();
			setLocation(Math.min(getX(), JavaLogoSchuifVeld.ppw-pclw), getY());
			isWide = true;
		}
	}
	
	void changeHeight()
	{
		if ( isHeightFixed ) return;
		int newh;
		if ( isOpen )
		{
			newh = pcclosedh;
		} else
		{
			newh = Math.min(pcmaxoh, Math.max(pcminoh, commandBlock.getContentHeight()+headerHeight+20));
		}
		isOpen = !isOpen;
		setSize(getWidth(), newh);
		setLocation(getX(), Math.min(getY(), JavaLogoSchuifVeld.pph-newh));
	}
	
	private void createHArrows()
	{
		arrowOut = new Polygon();
		arrowOut.addPoint(pcsw-14, 7);
		arrowOut.addPoint(pcsw-14, 19);
		arrowOut.addPoint(pcsw-6, 13);
		arrowIn = new Polygon();
		arrowIn.addPoint(pclw-6, 7);
		arrowIn.addPoint(pclw-6, 19);
		arrowIn.addPoint(pclw-14, 13);
	}
	
	/**
	 * Cannot set caret on a ProgrammaComponent, because it is the one and only CC that's not in a
	 * CommandContainer, it's the root of the tree!
	 * 
	 * @see fi.javalogoweb.CommandComponent#setCaret(int)
	 */
	@Override
	public void setCaret(int y)
	{  }

	@Override
	protected void paintBackground(Graphics g)
	{
		g.setColor(new Color(187,221,255));//new Color(230,240,255);
		g.fillRect(1,1,getWidth()-1,headerHeight-1);
		g.setColor(Color.BLACK);
		g.drawRect(0,0,getWidth()-1,headerHeight);
		g.drawRect(1,1,getWidth()-3,headerHeight-2);
		// always draw a line at the bottom, so the PC won't be 'open' when scrolling
		g.drawLine(0, getHeight()-1, getWidth()-1, getHeight()-1);
		if ( isWide )
		{
			g.fillPolygon(arrowIn);
		} else
		{
			g.fillPolygon(arrowOut);
		}
		if ( !isHeightFixed )
		{	
			if ( isOpen )
			{
				g.drawLine(getWidth()-45, 12, getWidth()-29, 12);
				g.drawLine(getWidth()-45, 13, getWidth()-29, 13);
			} else
			{
				g.drawRect(getWidth()-45, 6, 14, 14);
			}
		}
	}

	@Override
	protected void paintCommand(Graphics g)
	{
		g.setFont(JavaLogoWeb.boldfont);
		g.setColor(Color.BLACK);
		g.drawString(defaultName,10,18);
	}
	
	@Override
	public boolean execute(Uitvoerblad ub, VarSet varSet)
	{	for(int i=0 ; i<commandBlock.getComponentCount() ; i++)
		{	Component c = commandBlock.getComponent(i);
			if (c instanceof CommandComponent)
			{	boolean tracekleur = ((CommandComponent)c).execute(ub, varSet);
				if(tracekleur)return true;
			}
		}
		return false;
	}	
	
	@Override
	public String getCode(String tab)
	{	
		return commandBlock.getCode(tab);
	}
	
/* 
 * Implement MouseListeners: wheel is scrolling, pass on other events
 */
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int heightSurplus = commandBlock.getHeight()-(getHeight()-headerHeight);
		if ( heightSurplus > 0 )
		{	
			int newY = Math.max(-heightSurplus, Math.min(0, commandBlock.getY()+8*e.getWheelRotation()));
			commandBlock.setLocation(commandBlock.getX(), newY);
		}
	}
	
	/**
	 * The mousePressed event is used (also) to bring the ProgrammaComponent to the front of the ProgrammaPanel
	 * and to handle actions that resize the PC.
	 */
	@Override
	public void mousePressed(MouseEvent e) 
	{
		// Bring the component to the front, only if it's not already in front, because of focus
		if ( this != getParent().getComponent(0) )
		{	
			getParent().setComponentZOrder(this, 0);
			schuifveld.repaint();
		}
		// remember location in case of dragging
		previousX = getX();
		previousY = getY();
		// check if click is inside rectangle in the top right corner or the PC for resize.
		if ( e.getX() > getWidth()-2*headerHeight && e.getY() < headerHeight )
		{
			if ( e.getX() > getWidth()-headerHeight )
			{
				changeWidth();
				schuifveld.repaint();
				// after resize, no further mouse handling
				return;
			} else
			{
				if ( !isHeightFixed )
				{	changeHeight();
					schuifveld.repaint();
					// after resize, no further mouse handling
					return;
				}
			}
		}
		// when PC has been brought to front, continue normal mouse event handling
		schuifveld.mousePressed(getAbsoluteLocation().x+e.getX(), getAbsoluteLocation().y+e.getY(), e.getModifiersEx());
	}
	
	@Override
	public void mouseReleased(MouseEvent e) 
	{
		schuifveld.mouseReleased(getAbsoluteLocation().x+e.getX(), getAbsoluteLocation().y+e.getY(), e.getModifiersEx());
	}
	
	@Override
	public void mouseDragged(MouseEvent e) 
	{
		schuifveld.mouseDragged(getAbsoluteLocation().x+e.getX(), getAbsoluteLocation().y+e.getY(), e.getModifiersEx());
		
	}
	
	@Override
	public void mouseMoved(MouseEvent e) 
	{
		// unused		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		// unused		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) 
	{
		// unused
	}
	
	@Override
	public void mouseExited(MouseEvent e) 
	{
		// unused
	}

}
