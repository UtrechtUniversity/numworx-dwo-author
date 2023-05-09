package nl.numworx.geodefiner.common;

import java.util.Enumeration;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Groep;
import fi.euclides.model.Punt;
import fi.euclides.model.Triangle;

public class GroepPolygon extends Triangle {

	private Groep grp;

	public Groep getGrp() {
		return grp;
	}

	public void setGrp(Groep grp) {
		this.grp = grp;
		if(grp != null) grp.addObserver(this);
	}

	public GroepPolygon() {
	}
	
	public GroepPolygon(Groep grp) {
		super(new Punt[0]);
		setGrp(grp);
	}

	@Override
	public String key() {
		return "VG";
	}

	public Punt[] getElements() {
		Punt[] result = new Punt[grp.size()];
		Enumeration<Destroyable> e = grp.elements();
		for (int i = 0; i < result.length; i++) {
			result[i] = (Punt) e.nextElement();
		}
		return result;
	}

	@Override
	protected boolean degraded() {
		return grp.size() < 3;
	}

	@Override
	protected Triangle newInstance(Punt[] images) {
		// TODO Auto-generated method stub
		return super.newInstance(images);
	}

	@Override
	public void destroy() {
		super.destroy();
	}
	
	
}
