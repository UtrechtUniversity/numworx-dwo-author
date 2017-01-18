package fi.euclides.swing;

import javax.swing.Icon;

import fi.euclides.event.EventHandler;
import fi.euclides.model.Model;
import fi.euclides.model.Segment;
import fi.euclides.util.Observable;

public class CirkelAction extends XXXAction {

	Icon cirkelIcon, cirkel3Icon, compassIcon;
	
	
	public CirkelAction(String name, String icon, EventHandler handler,
			AWTViewer viewer) {
		super(name, icon, handler, viewer);
		cirkelIcon = (Icon) getValue(SMALL_ICON);
		cirkel3Icon = reduced("/fixedcircle.png");
		compassIcon = reduced("/circle3.png");
	}
	/* (non-Javadoc)
	 * @see fi.euclides.swing.XXXAction#update(fi.euclides.util.Observable, java.lang.Object)
	 */
	public void update(Observable observable, Object arg) {
		super.update(observable, arg);
		if(arg != Model.SELECT)
			return;
		int cnt;
		if(!isEnabled())
			cnt = 0;
		else
			cnt = viewer.getModel().getSelect().size();
		switch(cnt)
		{
		case 1:
		case 2:
			Object f = viewer.getModel().getSelect().firstElement();
			Object l = viewer.getModel().getSelect().lastElement();
			if(f instanceof Segment || l instanceof Segment)
			{
				putValue(SMALL_ICON, compassIcon); break;
			}
		default:
			putValue(SMALL_ICON, cirkelIcon); break;

		case 3: 
			putValue(SMALL_ICON, cirkel3Icon); break;
		}
	}

}
