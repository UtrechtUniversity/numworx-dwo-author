package nl.numworx.fsm.shared;

import java.io.IOException;

import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Punt;
import fi.euclides.model.VrijPunt;
import fi.euclides.model.math.Numbers;
import fi.euclides.persist.CreateUtil;
import fi.euclides.proof.DrieOpEenRij;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class MidBoogPunt extends VrijPunt implements Observer {
		
	static class Creator implements CreateUtil.Creator {

		@Override
		public Destroyable create() {
			return new MidBoogPunt();
		}
	}
	public static void addCreator() {
		CreateUtil.buildmap.put(TYPE, new Creator());
	}

	final private Punt[] depend = new Punt[2];

	public static final String TYPE = "Pmb";

	public MidBoogPunt() {
	}

	public String key() {
		return TYPE;
	}

	public MidBoogPunt(Numbers x, Numbers y, Punt start, Punt end) {
		super(x, y);
		this.depend[0] = start;
		this.depend[1] = end;
		start.addObserver(this);
		end.addObserver(this);
		calc();
	}

	public void setXY(Numbers x, Numbers y) {
		setX(x);
		setY(y);
		calc();
	}

	private void calc() {
		Punt start = depend[0];
		Punt end = depend[1];
		Numbers d = DrieOpEenRij.bracketn(start, this, end);
		Numbers dx = Numbers.sub(end.getY(), start.getY());
		Numbers dy = Numbers.sub(start.getX(), end.getX());
		Numbers l = Numbers.add(Numbers.sqr(dx), Numbers.sqr(dy));
		d = Numbers.div(d, l);
		Numbers midx = Numbers.div(Numbers.add(start.getX(), end.getX()), Numbers.TWO);
		Numbers midy = Numbers.div(Numbers.add(start.getY(), end.getY()), Numbers.TWO);
		super.setXY(Numbers.add(midx, Numbers.mul(d, dx)), Numbers.add(midy, Numbers.mul(d, dy)));
	}

	@Override
	public void update(Observable observable, Object arg) {
		if (observable == depend[0] || observable == depend[1]) calc();
		else
			super.update(observable, arg);
	}

	@Override
	public void read(Codec codec) throws IOException {
		super.read(codec);
		codec.read(depend);
		depend[0].addObserver(this);
		depend[1].addObserver(this);
	}

	@Override
	public void write(Codec codec) throws IOException {
		super.write(codec);
		codec.write(depend);
	}

	@Override
	public Destroyable[] getDepend() {
		return depend;
	}

}
