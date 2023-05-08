package fi.euclides.model;

import fi.euclides.model.math.Numbers;

public class LabelTrack extends Track {

	private Label l;

	public LabelTrack(Label l) {
		super(l.getX(), l.getY());
		this.l = l;
	}

	public LabelTrack(Label l, Numbers x, Numbers y) {
		super(x,y);
		this.l = l;
	}

	/* (non-Javadoc)
	 * @see euclides.Track#setXY(double, double)
	 */
	public void setXY(Numbers x, Numbers y) {
//		l.moveTo(x,y);
		Numbers dx = Numbers.sub(x, p.getX());
		Numbers dy = Numbers.sub(y, p.getY());
		l.moveTo(Numbers.add(dx, l.getX()), Numbers.add(dy, l.getY()));
		super.setXY(x,y);
		
	}

	/* (non-Javadoc)
	 * @see euclides.Track#visit(euclides.Visitor)
	 */
	public void visit(Visitor v) {
		l.visit(v);
	}

}
