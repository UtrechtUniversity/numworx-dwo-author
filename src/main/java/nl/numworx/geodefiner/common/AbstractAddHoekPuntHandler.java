package nl.numworx.geodefiner.common;

import java.util.Vector;

import fi.euclides.event.EventHandler;
import fi.euclides.event.TrackerContext;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.Track;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.DrieOpEenRij;

public abstract class AbstractAddHoekPuntHandler extends EventHandler {

	protected static final double EPS = DrieOpEenRij.EPS;
	protected int state;
	protected Punt p1;
	protected Punt p2;
	protected Track track;

	public AbstractAddHoekPuntHandler(String string) {
		super(string);
		testLijn = true;
		testPunt = true;
	}

	protected abstract void createTrack();

	public void command() {
		final Vector<?> select = getModel().getSelect();
		state = select.size();
		switch(state) {
		case 2: Object o2 = select.lastElement();
			if(o2 instanceof Punt)
			{
				p2 = (Punt) o2;
				if(select.firstElement() instanceof Punt) {
					p1 = (Punt) select.firstElement();
					createTrack(); // uses p1 and p2
					break;
				}
			} else {
				state = 0;
			}
		case 1: Object object = select.firstElement();
			if(object instanceof Punt)
			{
				p1 = (Punt) object;
			} else {
				state = 0;
			}
		case 0:	super.command();
				break;
		}
	}

	protected void build(Punt p, Punt q, Numbers d) {
		if (d.equals(Numbers.ZERO)) return;
		Numbers r = Numbers.round(d);
		Numbers eps = Numbers.abs(Numbers.sub(d, r));
		Punt hp;
		if (eps.doubleValue() < EPS) {
			long l = r.longValue();
			int ll = (int) (l % 360);
			switch(ll) {
			case 0: return;
			case 90:
			case -270: hp = new Hoekpunt(p,q,Numbers.ZERO, Numbers.ONE); break;
			case 180:
			case -180: hp = new Hoekpunt(p,q,Numbers.neg(Numbers.ONE), Numbers.ZERO); break;// Spiegelpunt?
			case 270:
			case -90:  hp = new Hoekpunt(p,q,Numbers.ZERO, Numbers.neg(Numbers.ONE)); break;
			
			default:	
				d = Numbers.div(d, Numbers.createInteger(180));
				d = Numbers.mul(d, Numbers.PI);
				hp = new Hoekpunt(p,q,d);
			}
		} else {
			d = Numbers.div(d, Numbers.createInteger(180));
			d = Numbers.mul(d, Numbers.PI);
			hp = new Hoekpunt(p,q,d);
		}
		visit(getModel().add(hp));
	}

	public boolean allowSelection(@SuppressWarnings("rawtypes") Vector selection) {
		int size = selection.size();
		if(size > 2)
			return false;
		for(int i = 0; i < size; i++)
			if(!(selection.elementAt(i) instanceof Punt))
				return false;
		return super.allowSelection(selection);
	}

	public void pointerPressed(Numbers x, Numbers y, TrackerContext context) {
		if(state == 0 || state == 1)
		{	
			track=new Track(x, y);
			context.setTrack(track);
			pointerDragged(x,y,context);
		}		
	}

	public void pointerReleased(Numbers x, Numbers y, TrackerContext context) {
		pointerDragged(x,y,context);
		context.setTrack(null);
		Model m = getModel();
		Vector<Destroyable> select = context.selection();
		switch(state) {
		case 0:
			if(select.size() == 1 && select.firstElement() instanceof Punt) 
				; // okay
			else {
				context.toggle(visit(m.buildPunt(x, y)));
			}
			break;
		case 1:
			if(select.size()==1 && select.firstElement() instanceof Punt)
				select.insertElementAt(p1, 0);
			else {
				Punt p = visit(m.buildPunt(x, y));
				context.toggle(p1);
				context.toggle(p);
			}
			break;
		}
		command();
	}
}
