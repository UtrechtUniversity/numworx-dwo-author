package nl.numworx.geodefiner.common;

import java.util.Vector;

import fi.euclides.event.TrackerContext;
import fi.euclides.model.CarryingLine;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.Ray;
import fi.euclides.model.Segment;
import fi.euclides.model.SnijPunt;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.AngleType;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Messages;

public class HoekHandler extends fi.euclides.proof.HoekHandler {

	private int state;
	Punt p0, p1;
	Lijn l0;

	public HoekHandler(String string) {
		super(string);
	}

	public HoekHandler() {
		super();
	}

	@Override
	public <T extends Destroyable> T visit(T t) {
		DefaultAdapter.getDefault(t).put(AngleType.DEGREE);
		volg( (Label) t);
		return super.visit(t);
	}

	private void volg(Label l) {
		Destroyable depend[] = l.getDepend();
		if (depend[1] instanceof Punt)
		{
			Punt p = (Punt) depend[1];
			l.setP(p);
		} else if (depend[1] instanceof Lijn) {
			SnijPunt p = new SnijPunt(lijn(depend[0]), lijn( depend[1]));		
			l.setP(p);
		}
	}

	private Lijn lijn(Destroyable d) {
		if (d instanceof Segment) {
			return new CarryingLine((Segment)d);
		}
		if (d instanceof Ray) {
			return new CarryingLine((Ray)d);

		}
		return (Lijn) d;
	}

	@Override
	public void command() {
		Vector<Destroyable> select = getModel().getSelect();
		state = select.size();
		if (state == 0) {
			setStatus(string);
			testLijn = true;
			testPunt = true;
			getTracker().setPointerHandler(this);
			return;
		}
		if (state == 1) {
			Destroyable d = select.firstElement();
			
			String message;
			if (d instanceof Lijn) {
				message = "AddLoodLijnHandler.0";
				testPunt = false;
				testLijn = true;
				l0 = (Lijn) d; p0 = p1 = null;
			}
			else if (d instanceof Punt) {
				message = "Euclides.87";
				p0 = (Punt) d; p1 = null; l0 = null;
				testPunt = true;
				testLijn = false;
			}
			else {
				message = "Euclides.86";
				testLijn = true;
				testPunt = true;
				getModel().clearSelection();
				state = 0;
			}
			getTracker().setPointerHandler(this);
			setStatus(Messages.getString(message));
			return;
		}
		if (state == 2 && select.firstElement() instanceof Punt && select.lastElement() instanceof Punt) {
			p0 = (Punt) select.firstElement();
			p1 = (Punt) select.lastElement();
			l0 = null;
			setStatus(Messages.getString("Euclides.89"));
			getTracker().setPointerHandler(this);
			return; 
		}
		state = 0;
		super.command();
	}

	@Override
	public void pointerReleased(Numbers x, Numbers y, TrackerContext context) {
		pointerDragged(x,y,context);
		context.setTrack(null);
		Model m = getModel();
		Vector<Destroyable> select = context.selection();
		if (state == 2) {
			if (select.size() == 1 && select.firstElement() instanceof Punt) {
				select.insertElementAt(p0, 0);
				select.insertElementAt(p1, 1);
				state = 0;
				super.command();
				setStatus(string);
				return;
			}
		} else
		if (state == 1) {
			if (l0 != null && select.size() == 1 && select.firstElement() instanceof Lijn) {
				select.insertElementAt(l0, 0);
				state = 0;
				super.command();
				setStatus(string);
				return;
			}
			if (p0 != null && select.size() == 1 && select.firstElement() instanceof Punt) {
				p1 = (Punt) select.firstElement();
				state = 2;
				context.clearSelection();
				setStatus(Messages.getString("Euclides.89"));
				return;
			}
		} else 
		if (state == 0 && select.size() == 1) {
			Destroyable d = select.firstElement();
			if (d instanceof Punt) {
				p0 = (Punt)d; p1 = null; l0 = null; testLijn = false;
				state = 1;
				context.clearSelection();
				setStatus(Messages.getString("Euclides.87"));
				return;
			}
			if (d instanceof Lijn) {
				l0 = (Lijn) d; p0=p1=null;
				state = 1;
				context.clearSelection();
				setStatus(Messages.getString("AddLoodLijnHandler.0"));
			}
		}
	}

}
