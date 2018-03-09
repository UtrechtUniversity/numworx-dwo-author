package nl.numworx.geodefiner.common.locus;

import java.io.IOException;

import fi.euclides.event.NameMapper;
import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.openmath.LocusModelF;
import fi.euclides.model.Locus.LocusModel;

public class Builder implements LocusModel.Builder {

	@Override
	public LocusModel build(PuntOp<?> source, Punt dest,
			NameMapper mapper) {
		return new LocusModel2(source, dest, mapper);
	}
	@Override
	public LocusModel readModel(Codec codec) throws IOException {
		Destroyable een = codec.readDestroyable();
		if(een instanceof PuntOp) {
			Punt twee = codec.readPunt();
			return build( (PuntOp<?>)een, twee, codec.getModel());
		}
		if(een instanceof Label) {
			return build( (Label) een);
		}
		return null;
	}

	@Override
	public LocusModel build(Label f) {
		return new LocusModelF(f, f.getRegistered().getTracker());
	}
}