package nl.numworx.geodefiner.common;

import java.io.IOException;

import fi.euclides.event.Tracker;
import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Locus.LocusModel;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.util.Observer;

public class LocusModelXY implements LocusModel {

	public LocusModelXY(Label fx, Label fy, Label interval, Tracker viewer) {
	}

	@Override
	public Punt getDest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PuntOp<?> getSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addObserver(Observer obs) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeModel(Codec codec) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Destroyable[] getDepend() {
		// TODO Auto-generated method stub
		return null;
	}

}
