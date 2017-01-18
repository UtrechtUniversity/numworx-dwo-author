package fi.euclides.model;

import java.io.IOException;

public class BoogHoek extends Boog {

	@Override
	Punt getC() {
		Punt c = new VrijPunt();
		double end = length() + getStart();
		double cx = getCenter().getXd() + getR() * Math.cos(end);
		double cy = getCenter().getYd() - getR() * Math.sin(end);
		c.setXY(cx, cy);
		return c;
	}

	@Override
	Punt getB() {
		Punt c = new VrijPunt();
		double end = length() / 2.0 + getStart();
		double cx = getCenter().getXd() + getR() * Math.cos(end);
		double cy = getCenter().getYd() - getR() * Math.sin(end);
		c.setXY(cx, cy);
		return c;
	}

	public static String TYPE = "bh";
	Label length;
	
	public BoogHoek() {
	}

	public BoogHoek(Punt a, Punt r, Label c) {
		base = new Cirkel(a,r);
		base.addObserver(this);
		length = c;
		c.addObserver(this);
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
	public void read(Codec codec) throws IOException {
		base = new Cirkel();
		base.read(codec);
		base.addObserver(this);
		length = (Label) codec.readDestroyable();
	}

	@Override
	public void write(Codec codec) throws IOException {
		super.write(codec);
		codec.writeDestroyable(length);
	}

	@Override
	public Destroyable[] getDepend() {
		return new Destroyable[] { base.getCenter(), base.getRadius(), length };
	}

	@Override
	public boolean isDefined() {
		return super.isDefined() && length.isDefined();
	}

}
