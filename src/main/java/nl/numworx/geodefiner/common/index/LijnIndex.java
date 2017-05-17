package nl.numworx.geodefiner.common.index;

import java.io.IOException;

import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Groep;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.math.Numbers;

class LijnIndex extends Lijn implements Indexed {

	@Override
	public Destroyable asDestroyable() {
		return this;
	}

	@Override
	public void setGrp(Groep grp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setIdx(Label idx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void recalc() {
		// TODO Auto-generated method stub

	}

	@Override
	public Numbers getX1n() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Numbers getX2n() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Numbers getY1n() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Numbers getY2n() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDefined() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String key() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write(Codec codec) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void read(Codec codec) throws IOException {
		// TODO Auto-generated method stub

	}

}
