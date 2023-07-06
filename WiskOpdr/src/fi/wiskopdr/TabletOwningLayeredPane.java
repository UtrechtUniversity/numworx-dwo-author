package fi.wiskopdr;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JLayeredPane;
import javax.swing.JRootPane;

import fi.wiskopdr.formuleobjects.FormuleVakHouder;
import fi.wiskopdr.formuleobjects.Tablet;
import fi.wiskopdr.formuleobjects.TabletOwner;

/**
 * Class voor het kunnen tonen van een tablet.
 * 
 * @author borku102
 *
 */
public class TabletOwningLayeredPane extends JLayeredPane implements TabletOwner
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Tablet tablet;
	private FormuleVakHouder tabletUser;
	private boolean tabletAdded;
	
	
	

	/**
	 * Constructor for the tablet owning root pane.
	 * The root pane contains a layered pane, so that 
	 * a tablet (keyboard) can be added as a top layer.
	 */
	public TabletOwningLayeredPane()
	{
		super();
	}

	/**
	 * Constructor for the tablet owning root pane.
	 * The root pane contains a layered pane, so that 
	 * a tablet (keyboard) can be added as a top layer.
	 */
	public TabletOwningLayeredPane(Component content)
	{
		super();
		this.setLayer(content, JLayeredPane.PALETTE_LAYER.intValue());
	}

	// methoden TabletOwner

	public void zetTabletUser(FormuleVakHouder formuleVakHouder)
	{
		if (tablet == null)
			return;
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
	}

	public void zetTablet(FormuleVakHouder formuleVakHouder, int x, int y)
	{
		if (tablet == null)
		{
			tablet = new Tablet(formuleVakHouder);
			tablet.setLocation(x, y);
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
	}

	public void addTablet(FormuleVakHouder formuleVakHouder, int xx, int yy)
	{
		if (tablet == null)
		{
			tablet = new Tablet(formuleVakHouder);
		}
		if (!tabletAdded)
		{
			this.setLayer(tablet, JLayeredPane.PALETTE_LAYER.intValue());
			super.add(tablet, 0);

			// add(tablet,0);
			int tb = tablet.getWidth();
			int th = tablet.getHeight();
			xx = Math.min(xx, getWidth() - tb);
			yy = Math.min(yy, getHeight() - th);
			tablet.setLocation(xx, yy);
			tabletAdded = true;
			repaint();
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
	}

	public void removeTablet()
	{
		if (tablet == null)
			return;
		super.remove(tablet);
		repaint();
		tabletAdded = false;

	}

	public Tablet getTablet()
	{
		return tablet;
	}

	// einde methode TabletOwner

}
