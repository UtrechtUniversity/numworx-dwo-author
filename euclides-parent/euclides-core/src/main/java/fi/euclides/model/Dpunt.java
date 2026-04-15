package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.algo.FreePoint;
import fi.euclides.model.algo.PointOnAlgorithm;
import fi.euclides.model.math.Complex;
import fi.euclides.model.math.Numbers;

public class Dpunt extends PuntOp<Punt> implements PointOnAlgorithm<Punt> {

	private Numbers d;
	/* (non-Javadoc)
	 * @see euclides.PuntOp#setOp(euclides.Destroyable)
	 */
	public void setOp(Punt op) {
		super.setOp(op);
	}
	
	public static final String TYPE = "Pd";
	
	/* (non-Javadoc)
	 * @see euclides.Punt#key()
	 */
	public String key() {
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see euclides.Punt#read(euclides.Codec)
	 */
	public void read(Codec codec) throws IOException {
		super.read(codec);
		setD(codec.readNumber());
	}

	/* (non-Javadoc)
	 * @see euclides.Punt#write(euclides.Codec)
	 */
	public void write(Codec codec) throws IOException {
		super.write(codec);
		codec.writeNumber(getD());
	}

	public Dpunt() {
		pon = this;
	}

	public Dpunt(Numbers x, Numbers y, Punt p, Numbers d) {
		super(x,y,p);
		pon = this;
		setD(d);
	}
	
	public Dpunt(Numbers x, Numbers y, Punt p) {
		super(x, y, p);
		pon = this;
		//setD(JMath.hypot(x.doubleValue()-p.getXd(),y.doubleValue()-p.getYd()));
		Numbers dx = Numbers.sub(x, p.getX());
		Numbers dy = Numbers.sub(y, p.getY());
		d = Numbers.hypot(dx, dy);
//System.out.println(d);
	}
	/**
	 * @return the d
	 */
	public Numbers getD() {
		return d;
	}

	/**
	 * @param d the d to set
	 */
	public void setD(Numbers d) {
		this.d = d;
	}

	protected void recalc(Numbers x, Numbers y) {
		Punt p = op;
//System.out.println("number " + x  + " " + y);
		if (d instanceof Complex) {
			x = Numbers.real(d);
			y = Numbers.imag(d);
		} else {
			x = Numbers.sub(x, p.getX());
			y = Numbers.sub(y, p.getY());
			Numbers ln = Numbers.hypot(x, y);
			ln = Numbers.div(ln, d);
			x = Numbers.div(x, ln);
			y = Numbers.div(y, ln);
		}			
		setXY(Numbers.add(p.getX(),x), Numbers.add(p.getY(),y));
	}

	
	public void setD(double doubleValue) {
		setD(Numbers.createDouble(doubleValue));
	}

	public void recalc(Punt p, FreePoint punt, double x, double y) {
		//System.out.println("double " + x  + " " + y);
		// assert punt == this
				double r = getD().doubleValue();
				double cx = p.getXd();
				double cy = p.getYd();
				x = x - cx;
				y = y - cy;
				double ln = Math.hypot(x,y) / r;
				setXY(cx+x/ln, cy+y/ln);		
	}

	@Override
	public void update(Punt on, FreePoint punt) {
		recalc(punt.getX(), punt.getY());
	}

	@Override
	public void recalc(Punt on, FreePoint punt, Numbers x, Numbers y) {
		recalc(x,y);
	}
}
