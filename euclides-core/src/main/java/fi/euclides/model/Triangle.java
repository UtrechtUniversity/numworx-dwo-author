package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.Cirkel;
import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Punt;
import fi.euclides.model.Visitor;
import fi.euclides.model.algo.PointInTriangle;
import fi.euclides.model.algo.PointOnAlgorithm;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.DrieOpEenRij;
import fi.euclides.util.Arrays;
import fi.euclides.util.Observable;

public class Triangle extends MP {

	public static final String TYPE = "D";
	protected Punt[] depend;
	
	public Destroyable[] getDepend() {
		return depend;
	}
	
	public Punt[] getElements() {
		return depend;
	}
	
	@Override
	public PointOnAlgorithm<MP> getAlgo() {
		return PointInTriangle.INSTANCE;
	}

	public Triangle(Punt a, Punt b, Punt c) {
		depend = new Punt[3];
		setA(a);
		setB(b);
		setC(c);
	}

	public Triangle(Punt[] p)
	{	
		depend = new Punt[p.length];
		System.arraycopy(p, 0, depend, 0, p.length);
		for (int i = 0; i < p.length; i++) {
			p[i].addObserver(this);
		}
		
	}
	
	public Triangle() {
	}

	public Triangle(int n) {
		depend = new Punt[n];
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
			return Arrays.hashCode(depend);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Triangle)) {
			return false;
		}
		Triangle other = (Triangle) obj;
		return Arrays.equals(depend, other.depend);
	}

	public String key() {
		return TYPE;
	}

	public void read(Codec codec) throws IOException {
		createDepend(codec);
		codec.read(depend);
		for (int i = 0; i < depend.length; i++) {
			depend[i].addObserver(this);
		}
	}

	protected void createDepend(Codec codec) throws IOException {
		depend = new Punt[3];
	}

	public void write(Codec codec) throws IOException {
		codec.write(getDepend());
	}

	public void update(Observable observable, Object arg) {
		if(arg == DESTROY)
			destroy();
		else 
		{
			for (int i = 0; i < depend.length; i++) {
				if(observable == depend[i])
					setChanged();
			}		
			notifyObservers(arg);
		}
	}

	public void visitSegments(SegmentVisitor v) {
		copyTo(depend[depend.length-1], start);
		for (int i = 0; i < depend.length; i++) {
			copyTo(depend[i], stop);
			v.visitSegment(s);
			copyTo(stop, start);
		}
	}

	public boolean isDefined() {
		for (int i = 0; i < depend.length; i++) {
			if(!depend[i].isDefined()) return false;
		}
		return !degraded();
	}

	protected boolean degraded() {
		Numbers d = DrieOpEenRij.bracketn(getA(), getB(), getC());
		return Math.abs(d.doubleValue()) < DrieOpEenRij.EPS;
	}

	@Deprecated
	public Punt getA() {
		return depend[0];
	}

	public void setA(Punt a) {
		depend[0] = a;
		a.addObserver(this);
	}

	@Deprecated
	public Punt getB() {
		return depend[1];
	}

	public void setB(Punt b) {
		depend[1] = b;
		b.addObserver(this);
	}

	@Deprecated
	public Punt getC() {
		return depend[2];
	}

	public void setC(Punt c) {
		depend[2] = c;
		c.addObserver(this);
	}

	public Destroyable[] getImage(Destroyable mirror)
	{
		if(mirror instanceof Cirkel)
		{
			return super.getImage(mirror); // Locus on Triangle werkt niet!
			//return null;
		}
		Punt[] images = new Punt[depend.length];
		for (int i = 0; i < images.length; i++) {
			images[i] = depend[i].getImage(mirror, this);
			images[i].setVisible(false);
		}
		Destroyable[] result = new Destroyable[images.length+1];
		System.arraycopy(images, 0, result, 0, images.length);
		result[images.length] = newInstance(images);
		return result;
	}

	protected Triangle newInstance(Punt[] images) {
		return new Triangle(images);
	}

	public void visit(Visitor v) {
		v.visitTriangle(this);		
	}
	
}
