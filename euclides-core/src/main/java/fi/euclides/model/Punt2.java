package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;

public class Punt2 extends PuntOp<Lijn> {

	public static final String TYPE = "P'";

	public Punt2() {
		setFree(false);
	}

	public Punt2(Lijn l)
	{
		super(l);
		setFree(false);
		setOp(l);
		setVisible(false);
	}

	public Numbers getX() {
		return op.getX2n();
	}

	public Numbers getY() {
		return op.getY2n();
	}

	Lijn getL0() {
		return op;
	}


	public boolean isDefined() {
		return op.isDefined();
	}

	public void update(Observable observable, Object arg) {
		if(arg == DESTROY)
		{
			destroy();
			return;
		}
		
		setChanged();
		if(observable == op) notifyObservers(arg);
	}
	
	public String key() {
		return TYPE;
	}

	public void read(Codec codec) throws IOException {
		Lijn l = codec.readLijn();
		setOp(l);setOb(l);
	}

	public void write(Codec codec) throws IOException {
		codec.write(getDepend());
	}
	
}
