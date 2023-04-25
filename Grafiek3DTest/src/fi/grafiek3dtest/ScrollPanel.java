package fi.grafiek3dtest;

import javax.swing.*;
import fi.grafiek3dtest.formuleobjects.*;

public class ScrollPanel extends JPanel implements TabletOwner
{
	private Tablet tablet;
	private boolean tabletAdded;
	private FormuleVakHouder tabletUser;

	  
	public void zetTabletUser(FormuleVakHouder formuleVakHouder)
	{	if (tablet == null) 
			return;
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
		
	}
	
	public void zetTablet(FormuleVakHouder formuleVakHouder, int x, int y)
	{	if (tablet == null) 
		{	tablet = new Tablet(formuleVakHouder);
			tablet.setLocation(x,y);
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
	}
	
	public void addTablet(FormuleVakHouder formuleVakHouder, int x, int y)
	{	
//System.out.println("sp y = " + getLocation().y);		
		
		if (tablet == null) 
		{	tablet = new Tablet(formuleVakHouder);
		}
		if (!tabletAdded)
		{	add(tablet,0);
			//tablet.setLocation(x,y);
			tablet.setLocation(x,y + Math.abs(getLocation().y));
			tabletAdded = true;
			//resize();
            repaint();
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
	}
	
	public void removeTablet()
	{	
//System.out.println("sp remove");		
		if (tablet == null)
			return;
        remove(tablet);
        //resize();
        repaint();
		tabletAdded = false;
	}
	
	public Tablet getTablet()
	{	return tablet;
	}
	
}
