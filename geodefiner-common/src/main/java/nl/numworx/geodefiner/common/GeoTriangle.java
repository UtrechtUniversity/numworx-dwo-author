package nl.numworx.geodefiner.common;

import java.io.IOException;

import fi.euclides.event.Tracker;
import fi.euclides.model.Codec;
import fi.euclides.model.Coordinaten;
import fi.euclides.model.Destroyable;
import fi.euclides.model.GeoImage;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.Triangle;
import fi.euclides.model.Visitor;
import fi.euclides.model.VrijPunt;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Observable;

public abstract class GeoTriangle extends GeoImage {
	
	public static final String NAME = "geo";
	
	private final Tracker viewer;
	private final Triangle t;

	public GeoTriangle(Tracker viewer, double size) {
		this.t = new Triangle(3);
		this.viewer = viewer;
		this.size = size;
		DefaultAdapter adapter = new DefaultAdapter();
		setAdapter(adapter); // Share
		t.setAdapter(adapter);
		
		Punt o = viewer.getModel().getO();
		Punt u = viewer.getModel().getU();
		a = new Coordinaten(newLabel(), newLabel(), o, u);
		a.setCy(Numbers.ZERO);
		a.setCx(Numbers.createDouble(-size));
		a.setFree(true);
		b = new Coordinaten(newLabel(), newLabel(), o, u);
		b.setCy(Numbers.ZERO);
		b.setCx(Numbers.createDouble(size));
		b.setFree(true);
		c = new Coordinaten(newLabel(), newLabel(), o, u);
		c.setCx(Numbers.ZERO);
		c.setCy(Numbers.createDouble(size));
		c.setFree(true);
		t.setA(a);
		t.setB(b);
		t.setC(c);
	}

	private Label newLabel() {
		Label l = new Label();
		l.register(viewer.getRegistered("1"));
		return l;
	}

	// model 
	
	protected final double size;
	
	Coordinaten a, b, c;
	
	@Override
	public boolean isDefined() {
		return true;
	}

	public void update(Observable observable, Object arg) {
	}

	@Override
	public void write(Codec codec) throws IOException {
	}

	@Override
	public void read(Codec codec) throws IOException {
	}

	@Override
	public void visit(Visitor v) {
		t.visit(v);
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		t.setVisible(visible);
	}

	@Override
	public Punt center() {
		return new VrijPunt( (a.getXd()+b.getXd())/2.0, (a.getYd()+b.getYd())/2);
	}

	@Override
	public Numbers rotation() {
		Numbers r = Numbers.createComplex(Numbers.sub(b.getX(), a.getX())
										, Numbers.sub(b.getY(), a.getY())
										);
		return r;
	}

	public Destroyable shape() {
		return t;
	}

	@Override
	public boolean isMove(Numbers x, Numbers y) {
		Punt c = center();
		x = Numbers.sub(x, c.getX());
		y = Numbers.sub(y, c.getY());
		double u = viewer.getModel().getU().getXd() - viewer.getModel().getO().getXd(); // 1 unit
		return Numbers.hypot(x, y).doubleValue() < u*3;
	}
	
	
}
