package fi.euclides.openmath;

import java.io.IOException;

import nl.tue.win.riaca.openmath.lang.OMObject;
import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.util.DefaultAdapter;

final class Lambda extends fi.euclides.expr.Lambda {

	Lambda() {
	}

	public Destroyable[] createDepend(Codec codec, Label label)
			throws IOException {
		try {
			DefaultAdapter.getDefault(label).put(OMObject.class, new Popcorn(label.getString()).start());
		} catch (ParseException e) {
			throw (IOException) new IOException(e.getMessage()).initCause(e);
		}
		return super.createDepend(codec, label);
	}

}
