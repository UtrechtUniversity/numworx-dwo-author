package fi.euclides.model;

import java.io.IOException;

import fi.euclides.util.Observable;

public class BoogRadiusHoek extends BoogHoek {

	public static final String TYPE = "br";
	private Label start;
	private Punt o,u;
	private Destroyable radius;
	public BoogRadiusHoek() {
	}

	public BoogRadiusHoek(Punt o1, Label o2, Label o3, Label o4, Punt O, Punt U) {
		base = new CirkelRadius(o1, o2, O, U);
		length = o4;
		start = o3;
		radius = o2;
		o = O;
		u = U;
		base.addObserver(this);
		start.addObserver(this);
		length.addObserver(this);
	}

	@Override
	protected
	Punt getA() {
		Punt c = new VrijPunt();
		double end = getStart();
		double cx = getCenter().getXd() + getR() * Math.cos(end);
		double cy = getCenter().getYd() - getR() * Math.sin(end);
		c.setXY(cx, cy);
		return c;
	}

	@Override
	public String key() {
		return TYPE;
	}

	@Override
	public double length() {
		return length.value.doubleValue();
	}

	@Override
	public double getStart() {
		return start.value.doubleValue() - angleOU();
	}

	private double angleOU() {
		double dx = u.getXd() - o.getXd();
		double dy = u.getYd() - o.getYd();
		return Math.atan2(dy, dx);
	}

	@Override
	public void read(Codec codec) throws IOException {
		CirkelRadius cr;
		base = cr = new CirkelRadius();
		base.read(codec);
		start  = (Label) codec.readDestroyable();
		length = (Label) codec.readDestroyable();
		radius = cr.radius;
	}

	@Override
	public void write(Codec codec) throws IOException {
		base.write(codec);
		start.write(codec);
		length.write(codec);
	}

	@Override
	public Destroyable[] getDepend() {
		return new Destroyable[] {
				base.getCenter(), radius, start, length
		};
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.Boog#update(fi.euclides.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object arg) {
		// TODO Auto-generated method stub
		super.update(observable, arg);
	}

	@Override
	public boolean isDefined() {
		return super.isDefined() && start.isDefined();
	}


}
