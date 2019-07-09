package fi.euclides.model;

import fi.euclides.model.algo.PointOnAlgorithm;
import fi.euclides.model.algo.PointOnLine;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public abstract class Lijn extends Destroyable implements Observer, OpObject<Lijn> {

	public static final String PUNTOP = "Pl";

	/* (non-Javadoc)
	 * @see euclides.Destroyable#visit(euclides.Visitor)
	 */
	public void visit(Visitor v) {
			v.visitLijn(this);
	}
	
	public double getX1() {
		return getX1n().doubleValue();
	}

	public abstract Numbers getX1n();

	public double getX2() {
		return getX2n().doubleValue();
	}
	
	public abstract Numbers getX2n();

	public double getY1() {
		return getY1n().doubleValue();
	}
	
	public abstract Numbers getY1n();

	public double getY2(){
		return getY2n().doubleValue();
	}
	
	public abstract Numbers getY2n();
	
	public abstract boolean isDefined();

	public double getDX() {
		return getX2() - getX1();
	}
	
	public Numbers getDXn() {
		return Numbers.sub(getX2n(), getX1n());
	}
	
	public double getDY() {
		return getY2() - getY1();
	}
	
	public Numbers getDYn() {
		return Numbers.sub(getY2n(), getY1n());
	}
	
	public void update(Observable o, Object arg) {
		if(arg == DESTROY)
		{
				destroy();
				return;
		}
		notifyObservers(arg);
	}
	
	/* (non-Javadoc)
	 * @see fi.euclides.model.Destroyable#trail()
	 */
	public Destroyable trail() {
		return new PuntenLijn(new VrijPunt(getX1(), getY1()), new VrijPunt(getX2(), getY2()));
	}

	public PointOnAlgorithm<Lijn> getAlgo() {
		return PointOnLine.INSTANCE;
	}
	
	public PuntOp<Lijn> pointOn(Numbers x, Numbers y) {
		return new PuntOp<Lijn>(x, y, this, getAlgo());
	}
	
//	/* (non-Javadoc)
//	 * @see fi.euclides.model.OpObject#recalc(fi.euclides.model.PuntOp, double, double)
//	 */
//	public void recalc(PuntOp punt, double x, double y) {
//		double dx = this.getDX();
//		double dy = this.getDY();
//		double x1 = this.getX1();
//		double y1 = this.getY1();
//		double inp = (dx*(x-x1)+dy*(y-y1))/(dx*dx+dy*dy);
//		online(punt, x1, y1, dx, dy, inp);
//	}
//
//	protected void online(PuntOp punt, double x1, double y1, double dx,
//			double dy, double inp) {
//		if(Math.abs(dx)>Math.abs(dy))
//		{
//			// horizontal line, round to x pixels
//			long lx = JMath.round(x1 + inp*dx);
//			Numbers lxn = Numbers.createRational(lx, 1);
//			Numbers lyn = Numbers.add(this.getY1n(),
//					Numbers.div(Numbers.mul(Numbers.sub(lxn,this.getX1n()), this.getDYn()), this.getDXn())
//					);
//			punt.setXY(lxn, lyn);
//			return;
//		}
//		// vertical line, round to y pixels
//		long ly = JMath.round(y1 + inp*dy);
//		Numbers lyn = Numbers.createRational(ly, 1);
//		Numbers lxn = Numbers.add(this.getX1n(),
//				Numbers.div(Numbers.mul(Numbers.sub(lyn,this.getY1n()), this.getDXn()), this.getDYn())
//				);
//		punt.setXY(lxn, lyn);
//	}

//	public String subkey() {
//		return PUNTOP;
//	}

	protected Destroyable[] getImage(Destroyable mirror, Punt p1, Punt p2) {
		p1 = p1.getImage(mirror, this); p1.setVisible(false);
		p2 = p2.getImage(mirror, this); p2.setVisible(false);
		if(mirror instanceof Cirkel)
		{
			Punt p3 = ((Cirkel) mirror).getCenter(); // was midpoint
			return new Destroyable[] { p1, p2, new Cirkel3(p1, p2, p3)};
		}
		return new Destroyable[] { p1, p2, new PuntenLijn(p1, p2) };
	}
	
	public boolean contains(Punt p)
	{
		return true;
	}
}
