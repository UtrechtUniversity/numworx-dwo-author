package fi.euclides.swing;


import javax.swing.Icon;

import fi.euclides.util.Messages;
import fi.euclides.event.EventHandler;
import fi.euclides.model.Lijn;
import fi.euclides.model.Model;
import fi.euclides.util.Observable;

public class PuntAction extends XXXAction {

	Icon puntOpIcon; // FIXME icons not same size!
	Icon puntOp2Icon;
	Icon puntIcon;
	
	public PuntAction(String name, String icon, EventHandler handler,
			AWTViewer viewer) {
		super(name, icon, handler, viewer);
		puntOpIcon = reduced("/qpointon.png");
		puntOp2Icon = reduced("/intersection.png");
		puntIcon = (Icon) getValue(SMALL_ICON);
		putValue(SHORT_DESCRIPTION,Messages.getString("AddPuntHandler.0"));
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
			Object d = viewer.getModel().getSelect().firstElement();
			String string;
			if(d instanceof Lijn)
			{ 
				string = Messages.getString("AddPuntHandler.1"); //$NON-NLS-1$
			} else
			{
				string = Messages.getString("AddPuntHandler.2"); //$NON-NLS-1$
			} 
			putValue(SHORT_DESCRIPTION, string);
			putValue(LARGE_ICON_KEY, puntOpIcon);
			putValue(SMALL_ICON, puntOpIcon); break;
		case 2: 
			putValue(SHORT_DESCRIPTION, Messages.getString("AddPuntHandler.3"));
			putValue(LARGE_ICON_KEY, puntOp2Icon);
			putValue(SMALL_ICON, puntOp2Icon); break;
		default:
			putValue(SHORT_DESCRIPTION,Messages.getString("AddPuntHandler.0"));
			putValue(LARGE_ICON_KEY, puntIcon);
			putValue(SMALL_ICON, puntIcon); break;
		}
	}

}
