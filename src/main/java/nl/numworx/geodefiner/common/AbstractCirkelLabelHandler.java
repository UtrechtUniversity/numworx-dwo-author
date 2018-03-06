package nl.numworx.geodefiner.common;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.Const;
import fi.euclides.proof.LabelDelegate;

public abstract class AbstractCirkelLabelHandler extends AbstractTextHandler {

	public AbstractCirkelLabelHandler(String string) {
		super(string);
	}
	
	protected void build(Punt p, Numbers r) {
		Label l = new Label();
		l.setString(Numbers.toString(r));
		l.setValue(r);
		LabelDelegate CONST = getTracker().getRegistered(Const.TYPE);
		CONST.define(l);
		Destroyable[] depend = { p, l };
		getModel().buildCirkel(depend);
	}

}
