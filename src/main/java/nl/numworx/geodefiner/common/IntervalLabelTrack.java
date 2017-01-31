package nl.numworx.geodefiner.common;

import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.Track;
import fi.euclides.model.Visitor;
import fi.euclides.model.math.Numbers;

class IntervalLabelTrack extends Track {

	private Label l;
	
	IntervalLabelTrack(Label label, Numbers x, Numbers y) {
		super(x, y);
		l = label;
	}

	public void visit(Visitor v) {
		l.visit(v);
	}
	/* (non-Javadoc)
	 * @see euclides.Track#setXY(double, double)
	 */
	public void setXY(Numbers x, Numbers y) {
//		l.moveTo(x,y);
		Numbers dx = Numbers.sub(x, p.getX());
		Numbers dy = Numbers.sub(y, p.getY());
		Numbers newX = Numbers.add(dx, l.getX());
// HIER SNAPPEN.....		
		
		l.moveTo(newX, Numbers.add(dy, l.getY()));
		super.setXY(x,y);
		
	}

}
