package fi.beans.lwmobjects_swing;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class LWMMouseHandler extends javax.swing.JComponent 
							 implements MouseListener, MouseMotionListener
{
	Vector listeningTo = new Vector();
	LWMContainer activeLWMContainer = null;
	LWMComponent activeLWMComponent = null;
	int activeOffsetX, activeOffsetY;
	
	int draggCnt = 0;
	
	public boolean fixed = false;
	
	public LWMMouseHandler(int x, int y, int w, int b)
	{
		setBounds(x, y, w, b);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void addLWMContainer(LWMContainer lwmCon)
	{
		listeningTo.add(lwmCon);
	}
	
	
	public void mousePressed(MouseEvent e)
	{
		if (fixed)
			return;
		
		for (int i = 0; i < listeningTo.size(); i++)
		{	LWMContainer lwmContainer = (LWMContainer) listeningTo.elementAt(i);
			Component[] components = lwmContainer.getComponents();
			//for (int j = components.length - 1; j >= 0; j--)
			for (int j = 0; j < components.length; j++)
			{	LWMComponent lwmComponent = (LWMComponent) components[j];
				if ((e.getX() >= lwmContainer.getLocation().x + lwmComponent.getLocation().x) &&
					(e.getX() <= lwmContainer.getLocation().x + lwmComponent.getLocation().x + lwmComponent.width) &&	
					(e.getY() >= lwmContainer.getLocation().y + lwmComponent.getLocation().y) &&
					(e.getY() <= lwmContainer.getLocation().y + lwmComponent.getLocation().y + lwmComponent.height))
				{
					activeLWMComponent = lwmComponent;
					activeLWMComponent.mousePressed(e.getX(), e.getY());
					
					return;
					
				}
			}
		}
	
	}
	
	public void mouseDragged(MouseEvent e)
	{
		
		if (fixed)
			return;
		
		if (activeLWMComponent != null)
		{	
			activeLWMComponent.mouseDragged(e.getX(), e.getY()); 
		}
	}
	

	public void mouseReleased(MouseEvent e)
	{
		if (fixed)
			return;
		
		if (activeLWMComponent != null)
		{	activeLWMComponent.mouseReleased(e.getX(), e.getY());
		}
		
		activeLWMContainer = null;
		activeLWMComponent = null;
	}

	/**
	 * Does nothing.
	 */
		public void mouseEntered(MouseEvent e)
		{}
		
	/**
	 * Does nothing.
	 */
		public void mouseExited(MouseEvent e)
		{}
		
	/**
	 * Does nothing.
	 */
		public void mouseClicked(MouseEvent e)
		{}
		
	// ------- Event handling: MouseMotionListener --------------
		
	/**
	 * Does nothing.
	 */
		public void mouseMoved(MouseEvent e)
		{}

}
