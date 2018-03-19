package nl.numworx.geodefiner.common;

import java.io.IOException;

import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.MP;
import fi.euclides.model.Punt;
import fi.euclides.model.Triangle;
import fi.euclides.model.algo.PointOnAlgorithm;
import fi.euclides.model.math.Numbers;
import fi.euclides.persist.CreateUtil;

public class Polygon extends Triangle {

	public static final String TYPE = "VH";
	public Polygon(Punt[] p) {
		super(p);
	}

	public Polygon() {
	}

	public Polygon(Destroyable[] depend) {
		this.depend = new Punt[depend.length];
		System.arraycopy(depend, 0, this.depend, 0, depend.length);
		for (int i = 0; i < depend.length; i++) {
			depend[i].addObserver(this);
		}
	}

	@Override
	public PointOnAlgorithm<MP> getAlgo() {
		// TODO Auto-generated method stub
		return super.getAlgo();
	}

	@Override
	public String key() {
		return TYPE;
	}

	@Override
	protected void createDepend(Codec codec) throws IOException {
		Numbers n = codec.readNumber();
		depend = new Punt[n.intValue()];
	}

	@Override
	public void write(Codec codec) throws IOException {
		codec.writeNumber(Numbers.createInteger(depend.length));
		super.write(codec);
	}

	@Override
	protected boolean degraded() {
		// TODO 
		return super.degraded();
	}

	@Override
	protected Triangle newInstance(Punt[] images) {
		return new Polygon(images);
	}

	public static void addCreator() {
		CreateUtil.buildmap.put(TYPE, new Creator());
	}

	static class Creator implements CreateUtil.Creator {

		@Override
		public Destroyable create() {
			return new Polygon();
		}
	}


}
