package nl.numworx.fsm.shared;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Punt;
import fi.euclides.model.Track;
import fi.euclides.model.Visitor;
import fi.euclides.model.math.Numbers;
import nl.numworx.geodefiner.common.Polygon;

public class RechthoekTrack extends Track {
	private final Punt base;
	private Punt bp, pb;
	private Polygon polygon;

	public RechthoekTrack(Numbers d, Numbers e, Punt  base) {
		super(d, e);
		this.base = base;
		bp = new RechthoekPunt(base, p);
		pb = new RechthoekPunt(p,base);
		polygon = new Polygon ( new Punt[] {base, bp, p, pb });
	}

	@Override
	public boolean isTracked(Destroyable d) {
		return d == polygon;
	}

	@Override
	public void visit(Visitor v) {
		polygon.visit(v);
	}

	// minx, maxx, miny, maxy 
	public Numbers minx() {
		return Numbers.signum(Numbers.sub(p.getX(), base.getX())) <= 0 ? p.getX() : base.getX();
	}
	public Numbers maxx() {
		return Numbers.signum(Numbers.sub(p.getX(), base.getX())) >= 0 ? p.getX() : base.getX();
	}
	
}
