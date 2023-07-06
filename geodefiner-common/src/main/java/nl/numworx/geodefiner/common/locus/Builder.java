package nl.numworx.geodefiner.common.locus;

import java.io.IOException;

import fi.euclides.event.NameMapper;
import fi.euclides.model.Codec;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import nl.numworx.geodefiner.common.LocusModelFX;
import nl.numworx.geodefiner.common.LocusModelXY;
import fi.euclides.model.Locus.LocusModel;

public class Builder implements LocusModel.Builder {

	@Override
	public LocusModel build(PuntOp<?> source, Punt dest,
			NameMapper mapper) {
		return new LocusModel2(source, dest, mapper);
	}
	@Override
	public LocusModel readModel(Codec codec) throws IOException {
		int type = codec.readNumber().intValue();
		switch(type) {
		case 0:
			PuntOp<?> een = (PuntOp<?>) codec.readPunt();
			Punt twee = codec.readPunt();
			return build( een, twee, codec.getModel());
		case 1:
			Label label = (Label) codec.readDestroyable();
			return build( label );
		case 2:
			Label label1 = (Label) codec.readDestroyable();
			Label label2 = (Label) codec.readDestroyable();
			Label label3 = (Label) codec.readDestroyable();
			return build(label1, label2, label3);
		}
		return null;
	}

	private LocusModel build(Label label1, Label label2, Label label3) {
		return new LocusModelXY(label1, label2, label3, label1.getRegistered().getTracker());
	}
	@Override
	public LocusModel build(Label f) {
		//return new LocusModelF(f, f.getRegistered().getTracker());
		return new LocusModelFX(f, f.getRegistered().getTracker());
	}
}