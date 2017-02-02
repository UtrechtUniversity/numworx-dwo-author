package nl.numworx.geodefiner.common;

import fi.euclides.model.Destroyable;
import fi.euclides.model.HorizontalPunt;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.Segment;
import fi.euclides.model.Track;
import fi.euclides.model.Visitor;
import fi.euclides.model.math.Numbers;

class IntervalLabelTrack extends Track {

	private Label l;
	private Segment segment;
	private StepValue step;
	private PuntOp<Segment> ps;
	private HorizontalPunt hp;
	private Label min,max;
	private Numbers orgX;
	
	IntervalLabelTrack(Label label, Numbers x, Numbers y) {
		super(x, y);
		l = label;
		step = l.adapt(StepValue.class);
		ps = (PuntOp<Segment>) l.getP();
		orgX = ps.getX();
		segment = ps.getOp();
		Destroyable[] depend = l.getDepend();
		min = (Label) depend[0];
		max = (Label) depend[1];
		hp = (HorizontalPunt) segment.getP2();
	}

	public void visit(Visitor v) {
		ps.visit(v);
	}
	/* (non-Javadoc)
	 * @see euclides.Track#setXY(double, double)
	 */
	public void setXY(Numbers x, Numbers y) {
//		l.moveTo(x,y);
		Numbers dx = Numbers.sub(x, p.getX());
		Numbers dy = Numbers.sub(y, p.getY());
		Numbers newX = Numbers.add(dx, orgX);
		orgX = newX;
// HIER SNAPPEN.....				
		ps.moveTo(newX, Numbers.add(dy, l.getY()));
		Numbers value = l.value;
		value = Numbers.sub(value, min.value);
		value = Numbers.div(value, Numbers.sub(max.value, min.value));
		value = Numbers.mul(value, hp.getDistance());
		newX =  Numbers.add(segment.getX1n(), value);
		ps.moveTo(newX, ps.getY());
		super.setXY(x,y);
		
	}

}
