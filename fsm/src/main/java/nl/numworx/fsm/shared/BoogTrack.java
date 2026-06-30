package nl.numworx.fsm.shared;

import fi.euclides.model.Boog;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Punt;
import fi.euclides.model.Track;
import fi.euclides.model.Visitor;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.DrieOpEenRij;

public class BoogTrack extends Track {
	@Override
	public void visit(Visitor v) {
		boog.visit(v);
	}

	final Boog boog;
	final Punt start;
	final Punt end;
	final Punt mid;
	
	public BoogTrack(Numbers d, Numbers e, Boog b) {
		super(d, e);
		this.boog = b;
		start = Boog.startOf(b);
		end = Boog.endOf(b);
		mid = (Punt) b.getDepend()[1];
		calc();
	}

	void calc() {
		Numbers d = DrieOpEenRij.bracketn(start, p, end);
		Numbers dx = Numbers.sub(end.getY(), start.getY());
		Numbers dy = Numbers.sub(start.getX(), end.getX());
		Numbers l = Numbers.add(Numbers.sqr(dx), Numbers.sqr(dy));
		d = Numbers.div(d, l);
		Numbers midx = Numbers.div(Numbers.add(start.getX(), end.getX()), Numbers.TWO);
		Numbers midy = Numbers.div(Numbers.add(start.getY(), end.getY()), Numbers.TWO);
		mid.moveTo(Numbers.add(midx, Numbers.mul(d, dx)), Numbers.add(midy, Numbers.mul(d, dy)));
	}

	@Override
	public void setXY(Numbers x, Numbers y) {
		super.setXY(x, y);
		calc();
	}

	@Override
	public boolean isTracked(Destroyable d) {
		return d == boog;
	}



}
