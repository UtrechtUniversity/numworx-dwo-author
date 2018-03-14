package fi.euclides.formuleobjects;

import java.io.IOException;

import nl.tue.win.riaca.openmath.lang.OMObject;
import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.util.DefaultAdapter;

final public class Lambda extends fi.euclides.expr.Lambda {

	public static final String TYPE = "$f@";
	public Lambda() {
		super(TYPE);
	}

	public Destroyable[] createDepend(Codec codec, Label label)
			throws IOException {
		try {
			DefaultAdapter.getDefault(label).put(OMObject.class, new FormuleParser(getString(label)).expr());
		} catch (ParseException e) {
			throw (IOException) new IOException(e.getMessage()).initCause(e);
		}
		return super.createDepend(codec, label);
	}

	private String getString(Label label) {
		return label.getString();
	}

}
