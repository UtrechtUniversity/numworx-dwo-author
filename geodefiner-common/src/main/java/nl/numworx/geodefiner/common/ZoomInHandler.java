package nl.numworx.geodefiner.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import fi.euclides.event.EventHandler;
import fi.euclides.event.Tracker;
import fi.euclides.event.TrackerContext;
import fi.euclides.model.Pair;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;

public class ZoomInHandler extends EventHandler {
	
	private static final Logger LOG = Logger.getLogger(ZoomInHandler.class.getName());

	Numbers mag = Numbers.createRational(11L, 10L);
	
	public ZoomInHandler(String string) {
		super(string);
	}
	
	ZoomInHandler(String string, Numbers mag) {
		super(string);
		this.mag = mag;
	}

	@Override
	public void pointerClicked(Numbers x, Numbers y, TrackerContext context) {

		LOG.info("click at " + x + ", " + y);

		Tracker widget = getTracker();
		Punt O = widget.getModel().getO();
		Numbers Ox = O.getX();
		Numbers Oy = O.getY();
		LOG.info("O at " + Ox + ", " + Oy);
		
		Numbers nOx = Numbers.add(x, Numbers.mul(mag, Numbers.sub(Ox, x)));
		Numbers nOy = Numbers.add(y, Numbers.mul(mag, Numbers.sub(Oy, y)));
		
		Punt U = widget.getModel().getU();
		Numbers dx = Numbers.sub(U.getX(), Ox);
		Numbers ndx = Numbers.mul(mag, dx);
		Numbers Ux = Numbers.add(nOx, ndx);

		List<Punt> p = widget.getModel().getPunten();
		p = p.subList(2, p.size());
		List<Punt> p2 = widget.getModel().getLijnen().stream()
				.filter(d -> d instanceof fi.euclides.model.Label)
				.map( d -> {
					fi.euclides.model.Label l = (fi.euclides.model.Label) d;
					return l.getP();
				}).collect(Collectors.toList());		
		List<Pair<Numbers, Numbers>> save = new ArrayList<>(p.size());
		p.forEach(n -> save.add(new Pair<>(n.getX(), n.getY())));
		p2.forEach(n ->save.add(new Pair<>(n.getX(), n.getY())));
	
		
		O.setXY(nOx, nOy);
		U.setXY(Ux, nOy);

		Iterator<Punt> i = p.iterator();
		Iterator<Pair<Numbers, Numbers>> pairs = save.iterator();
		while (i.hasNext() && pairs.hasNext()) {
			Punt punt = i.next();
			Pair<Numbers, Numbers> pair = pairs.next();
			//punt.moveTo(nX(rw, left, width, pair.getA()), nY(rh, top, height, pair.getB()));
			Numbers a = pair.getA();
			Numbers na = nC(Ox, nOx, dx, ndx, a);
			Numbers b = pair.getB();
			Numbers nb = nC(Oy, nOy, dx, ndx, b);
			punt.moveTo(na, nb);
			
		}
		i = p2.iterator();
		while (i.hasNext() && pairs.hasNext()) {
			Punt punt = i.next();
			Pair<Numbers, Numbers> pair = pairs.next();
			//punt.setXY(nX(rw, left, width, pair.getA()), nY(rh, top, height, pair.getB()));
			//if (!pairs.hasNext()) LOG.info("move to " + Oy  + " " + pair.getB() + " to " + nOy + " " + punt.getY());
			Numbers a = pair.getA();
			Numbers na = nC(Ox, nOx, dx, ndx, a);
			Numbers b = pair.getB();
			Numbers nb = nC(Oy, nOy, dx, ndx, b);
			punt.setXY(na, nb);

		}

		
		
	}

	private static Numbers nC(Numbers Ox, Numbers nOx, Numbers dx, Numbers ndx, Numbers a) {
		Numbers cx = Numbers.div(Numbers.sub(a, Ox),dx);
		Numbers na = Numbers.add(nOx, Numbers.mul(cx, ndx));
		return na;
	}

}
