package fi.javalogoweb3d;

import java.awt.*;

import javax.swing.JPanel;

/**
 * CommandContainer is a JPanel that will hold a list of CommandComponents, corresponding to a 'block'
 * in programming languages. It is used by HerhaalCC, KeuzeCC, DeeltaakCC, ProgrammaCC...
 * Note: CommandComponent is NOT a CommandComponent, it can not be manipulated apart from its owner.
 * 
 * Height of the container is always (at least) the sum of the heights of its components.
 * Scrolling will be managed by the owner CommandComponent
 * 
 * @author Berge020
 */
public class CommandContainer extends JPanel
{
	private CompositeCommandComponent owner;

	/**
	 * Minimum height of the container. Will be a small number for the containers in control structures,
	 * more for the deeltaken and the 'tekenalgoritme'.
	 */
	private int minimumHeight;
	
	/**
	 * sum of heights of the CC's in this container
	 */
	private int contentHeight;
	
	/**
	 * Since the container is not a CommandComponent, it must handle carets itself
	 * Caret can only be 'up', you never drop a CC at the bottom of a container.
	 */
	private boolean caretUp;
	
	/**
	 * position for the insertion of a new CC, or -1 if there is none and the CC should go as last.
	 */
	private int insertPos;
	
	public CommandContainer(int x, int y, int b, int h, CompositeCommandComponent cc)
	{	
		setBounds(x,y,b,h);
		setLayout(null);
		owner = cc;
		// we assume we create this object at its minimum height
		minimumHeight = h;
		caretUp = false;
		insertPos = -1;
	}
	
	public CompositeCommandComponent getOwner()
	{
		return owner;
	}
	
	public void addCComponent(CommandComponent cc)
	{	
		cc.setBounds(0,0, getSize().width, cc.getSize().height);
		super.add(cc, insertPos);
		reArrange();
	}
	
	public CommandContainer getCommandContainerAt(int x, int y)
	{	CommandContainer cc = null;
		Component c = getComponentAt(x,y);
		if(c!=this && c!=null  && c instanceof CommandContainer) 
		{	cc = (CommandContainer)c;
			return cc.getCommandContainerAt(x - cc.getLocation().x,y - cc.getLocation().y);
		}
		
		return this;
	}
	
	/**
	 * Set the caret on the CommandContainer itself.
	 * This is called by JavaLogoSchuifVeld.traceComponent when the dragged CC is hovering
	 * over the empty space of the container. Set Caret at the top of the container if it
	 * is empty, or at the bottom of the last CC if it is not.
	 * Note: in the latter case insertPos will be set through call from CommandComponent.setCaret: setInsert
	 * 
	 * @param y		y-pos of mouse on hovering CC. x is irrelevant
	 */
	public void setCaret(int y)
	{
		if ( getComponentCount()==0 )
		{
			caretUp = true;
			insertPos = -1;
		} else
		{
			Component c = getComponent(getComponentCount()-1);
			((CommandComponent)c).setCaret(y);
		}
	}

	/**
	 * Set the insertPos for this container when caret is set on a CC inside it
	 * 
	 * @param commandComponent		the CC that gets a caret
	 * @param downcaret				top or bottom, causes a difference of 1 in insertPos
	 */
	public void setInsert(CommandComponent commandComponent, boolean downcaret)
	{
		for (int i=0; i<getComponentCount(); i++)
		{
			if ( getComponent(i)==commandComponent)
			{
				insertPos = i;
				if (downcaret) insertPos++;
				return;
			}
		}
		insertPos=-1;
	}
	
	public void removeAll()
	{
		super.removeAll();
		insertPos = -1;
		reArrange();
	}
	
	public void remove(Component c)
	{	super.remove(c);
		reArrange();
	}
		
	/**
	 * Set the minimum height. This method should not be called on containers for control structures,
	 * their height should be equal to the sum of heights of the CC's inside (with small minimum)
	 * Must be called for deeltaakbodies, when resize arrows are used.
	 * 
	 * @param h the new minimum height
	 */
	public void setMinimumHeight(int h)
	{
		minimumHeight = h;
	}
	
	public void setWidth(int b)
	{	for(int i=0 ; i<getComponentCount() ; i++)
		{	Component c = getComponent(i);
			if(c instanceof CommandComponent)c.setSize(b,c.getSize().height);
		}
		super.setSize(b, getSize().height);
	}
	
	/**
	 * Reposition the CC's and calculate contentHeight after insert/delete of a CC.
	 * Notify owner of the change, so it can change its own height.
	 */
	public void reArrange()
	{	int hoogte = 0;
		for(int i=0 ; i<getComponentCount(); i++)
		{	Component c = getComponent(i);
			if(c instanceof CommandComponent)
			{	getComponent(i).setLocation(0,hoogte);
				hoogte += getComponent(i).getSize().height-2;
			}
		}
		contentHeight = hoogte;
		// hoogte is som van de hoogte van zijn CComponenten, met minimum
		int h = Math.max(contentHeight+12, minimumHeight);
		setSize(getSize().width, h);
		owner.containerHeightChanged(h);
		
	}
	
	int getContentHeight()
	{
		return contentHeight;
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(new Color(238,238,170));
		g.fillRect(1,1,getSize().width-1,getSize().height-1);
		g.setColor(Color.black);
		g.drawRect(0,0,getSize().width-1,getSize().height-1);
		g.drawRect(1,1,getSize().width-3,getSize().height-3);
		if(caretUp)
		{	
			g.setColor(Color.green);
			g.drawLine(2,2,getSize().width-3,2);
			g.drawLine(2,3,getSize().width-3,3);
			caretUp = false;
		}
	}
	
	public String getCode(String tab)
	{
		String s = "";
		for(int i=0 ; i<getComponentCount(); i++)
		{	Component c = getComponent(i);
			if(c instanceof CommandComponent)
			{	
				s = s +((CommandComponent)c).getCode(tab);
			}
		}
		return s;
	}
}
