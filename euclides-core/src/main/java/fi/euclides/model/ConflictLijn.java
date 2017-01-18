package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;

public class ConflictLijn extends Lijn
{
	private static final Punt NULL = new Punt(Numbers.ZERO, Numbers.ZERO) { public boolean isDefined() { return false; } };
	public static final String TYPE = "lc";
	Punt p1 = NULL, p2 = p1;
	
	public Numbers getX1n() {
		return Numbers.div(Numbers.add(p1.getX(), p2.getX()), Numbers.TWO);
	}
	public Numbers getDXn() {
		return Numbers.sub(p2.getY(), p1.getY() );
	}
	public Numbers getX2n() {
		return Numbers.add(getX1n(), getDXn());
	}

	public Numbers getY1n() {
		return Numbers.div(Numbers.add(p1.getY(), p2.getY()),Numbers.TWO);
	}

	public Numbers getDYn() {
		return Numbers.sub(p1.getX(), p2.getX());
	}
	public Numbers getY2n() {
		return Numbers.add(getY1n(), getDYn());
	}

	// like PuntenLijn
	public boolean isDefined() {
		return p1.isDefined() && p2.isDefined() && (nonzero(getDXn()) || nonzero(getDYn()));
	}

	private boolean nonzero(Numbers yn) {
		return Numbers.signum(yn)!=0;
	}

	public String key() {
		return TYPE;
	}

	public void read(Codec codec) throws IOException {
		p1 = codec.readPunt();
		p2 = codec.readPunt();
		p1.addObserver(this);
		p2.addObserver(this);
	}

	public void write(Codec codec) throws IOException {
		codec.writePunt(p1);
		codec.writePunt(p2);
	}
	
	public Destroyable[] getDepend() {
		return new Punt[] { p1, p2 };
	}
	
	public void update(Observable o, Object arg) {
		if(o == p1 || o == p2 )
			setChanged();
		super.update(o, arg);
	}
	
	public Destroyable[] getImage(Destroyable mirror)
	{
		return getImage(mirror, new MiddelPunt(p1, p2), new Punt2(this)); // FIXME 2 punten toevoegen
	}
}