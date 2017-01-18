package fi.euclides.expr;

import java.io.IOException;

import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.util.Observable;

public class Lambda extends LabelDelegate {

	public static final String TYPE = "λ";

	protected Lambda() {
		super(TYPE);
	}

	public Destroyable[] createDepend() {
		return Label.EMPTY;
	}

	public void update(Observable observable, Object arg) {
	}

	public void prepareDepend(Codec codec, Label label) throws IOException {
		super.prepareDepend(codec, label);
	}

	@Override
	public boolean equals(Label label, Label other) {
		return super.equals(label, other) && label.getString().equals(other.getString());
	}

}
