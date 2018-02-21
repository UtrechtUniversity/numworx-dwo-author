package fi.euclides.event;

import java.util.Enumeration;
import java.util.Vector;

import fi.euclides.util.Messages;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.MP;
import fi.euclides.model.Cirkel;
import fi.euclides.model.CirkelLijnSnijpunt;
import fi.euclides.model.CirkelSnijpunt;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Lijn;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp2;
import fi.euclides.model.Track;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.IncidentHandler;

public class AddPuntHandler extends EventHandler {

	private static final String NIEUW_PUNT = Messages.getString("AddPuntHandler.0"); //$NON-NLS-1$
	private int state;
	private Punt punt;
	/**
	 * 
	 */
	public AddPuntHandler() {
		this(NIEUW_PUNT);
	}

	/**
	 * @param string
	 */
	public AddPuntHandler(String string) {
		super(string);
		this.testLijn = true;
		this.testPunt = false;
	}

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#pointerPressed(double, double)
	 */
	public void pointerPressed(Numbers x, Numbers y) {
		track = new Track(x, y);
		tracker.setTrack(track);
		pointerDragged(x,y);
	}

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#visitPunt(fi.euclides.model.Punt)
	 */
	public void visitPunt(Punt p) {
		this.punt = p;
		done = true;
	}

	/* (non-Javadoc)
	 * @see fi.euclides.event.EventHandler#pointerReleased(double, double)
	 */
	public void pointerReleased(Numbers x, Numbers y) {
		punt = null;
		HitTester hitTester = getTracker().getHitTester();
		hitTester.setVisitor(this);
		hitTester.setXY(x.doubleValue(), y.doubleValue());
		done = false;
		Enumeration<Punt> e = getModel().getPunten().elements();
		while (!done && e.hasMoreElements()) {
			Punt p = e.nextElement();
			if(p.isDefined() && p.isVisible())
				hitTester.visitPunt(p);
		}
		hitTester.done();
		if(punt != null)
		{
			getModel().clearSelection();
		} else
		{
			Punt p = buildPunt(x, y);
			if(p instanceof PuntOp2)
			{
				PuntOp2 p2 = (PuntOp2) p;
				if(p2.getOther() == null && (p.key() == CirkelLijnSnijpunt.TYPE || p.key() == CirkelSnijpunt.TYPE))
				{
					findOther(p2);
				}
			}
		}
		tracker.setTrack(null);
		command();
	}

	protected Punt buildPunt(Numbers x, Numbers y) {
		return getModel().buildPunt(x, y);
	}

	private void findOther(PuntOp2 p2) {
		if(p2 instanceof CirkelLijnSnijpunt)
		{
			Cirkel c = (Cirkel) p2.getOp2();
			Lijn l = (Lijn) p2.getOp1();
			Enumeration<Punt> e = getModel().getPunten().elements();
			while (e.hasMoreElements()) {
				Punt p = e.nextElement();
				if(p == p2) break;
				if(PuntOp2.incident(p, c) && IncidentHandler.incident(p, l))
				{
					p2.setOther(p);
					break;
				}
			}
		} else if(p2 instanceof CirkelSnijpunt)
		{
			Cirkel c1 = (Cirkel) p2.getOp1();
			Cirkel c2 = (Cirkel) p2.getOp2();
			Enumeration e = getModel().getPunten().elements();
			while (e.hasMoreElements()) {
				Punt p = (Punt) e.nextElement();
				if(p == p2) break;
				if(PuntOp2.incident(p, c1) && PuntOp2.incident(p, c2))
				{
					p2.setOther(p);
					break;
				}
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#command()
	 */
	public void command() {
		Vector<Destroyable> select = getModel().getSelect();
		state = select.size();
		switch(state) {
		case 1: 
			Object d = select.firstElement();
			if(d instanceof Lijn)
			{ 
				string = Messages.getString("AddPuntHandler.1"); break; //$NON-NLS-1$
			} else if (d instanceof Cirkel)
			{
				string = Messages.getString("AddPuntHandler.2"); break; //$NON-NLS-1$
			} else if (d instanceof MP)
			{
				string = "Wijs een nieuw punt op object aan"; break;
			} else
				getModel().toggle((Destroyable) d);
		case 0: string = NIEUW_PUNT; break;
		case 2:
			if (select.firstElement() instanceof Lijn && select.lastElement() instanceof Lijn) {
				buildPunt(Numbers.NaN, Numbers.NaN);
				return;
			}
		default: 
			string = Messages.getString("AddPuntHandler.3"); break; //$NON-NLS-1$
		}
		super.command();
	}

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#pointerDragged(double, double)
	 */
	public void pointerDragged(Numbers x, Numbers y) {
		if(state != 0)
			track.setXY(x, y);
		else
			super.pointerDragged(x, y);
	}

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#clear()
	 */
	void clear() {
		if(testLijn)
			super.clear();
	}

	private boolean isLijnOfConic(Object o)
	{
		return o instanceof Lijn || o instanceof Cirkel || o instanceof Kegelsnede2;
	}
	
	private boolean isLijnOfCirkelMP(Object o)
	{
		return o instanceof Lijn || o instanceof Cirkel || o instanceof MP;
	}
	
	/* (non-Javadoc)
	 * @see fi.euclides.event.EventHandler#allowSelection(java.util.Vector)
	 */
	public boolean allowSelection(Vector selection) {
		return  selection.isEmpty() ||
				(selection.size() == 1 && isLijnOfCirkelMP(selection.firstElement()) ||
				(selection.size() == 2 && 
					isLijnOfConic(selection.firstElement()) && isLijnOfConic(selection.lastElement())));
	}


}
