package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;

public class CarryingLine extends Lijn {

	public static final String TYPE = "lco";

	public Numbers getX1n() {
		return lijn.getX1n();
	}

	public Numbers getX2n() {
		return lijn.getX2n();
	}

	public Numbers getY1n() {
		return lijn.getY1n();
	}

	public Numbers getY2n() {
		return lijn.getY2n();
	}

	public boolean isDefined() {
		return lijn.isDefined();
	}

	public CarryingLine(Segment lijn) {
		super();
		setLijn(lijn);
	}
	
	public CarryingLine(Ray lijn) {
		super();
		setLijn(lijn);
	}

	private PuntenLijn lijn;
		
	public CarryingLine() {
	}

	public String key() {
		return TYPE;
	}

	public void write(Codec codec) throws IOException {
		codec.writeLijn(lijn);
	}

	public void read(Codec codec) throws IOException {
		setLijn((PuntenLijn) codec.readLijn());
	}

	private void setLijn(PuntenLijn lijn) {
		this.lijn = lijn;
		lijn.addObserver(this);
	}

	public Destroyable[] getDepend() {
		return new Destroyable[] { lijn };
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o == lijn) setChanged();
		super.update(o, arg);
	}

}
