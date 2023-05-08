package fi.euclides.model;

import fi.euclides.model.math.Numbers;


public class CirkelTrack extends Track {

	Punt p2;
	Cirkel l;
	public CirkelTrack(Numbers d, Numbers e) {
		super(d, e);
		p2 = new VrijPunt(d,e);
		p2.setDefined(false);
		l = new Cirkel(p, p2);
	}
	/* (non-Javadoc)
	 * @see Track#setXY(int, int)
	 */
	public void setXY(Numbers x, Numbers y) {
		p2.setXY(x, y);
		p2.setDefined(x != p.getX() && y != p.getY());
	}
	/* (non-Javadoc)
	 * @see Track#visit(euclides.Visitor)
	 */
	public void visit(Visitor v) {
		if(p2.isDefined())
			l.visit(v);
		else
			super.visit(v);
	}

}
