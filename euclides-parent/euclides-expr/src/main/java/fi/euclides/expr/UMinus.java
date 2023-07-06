package fi.euclides.expr;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DefaultAdapter;

public class UMinus extends Som {

	public UMinus() {
		super("0-");
	}

	public String getSymbolicValue(Label l) {
		return "-" + s(l.getDepend()[0]);
	}

	public Destroyable[] createDepend() {
		return new Label[1];
	}

	protected void recalc(Label l, Label[] labels) {
		Numbers value;
		Label ll0 = labels[0];
		if(isHoek(ll0))
		{
			if(ll0.getAdapter().adapt(Numbers[].class) == null)
				ll0.getRegistered().define(ll0); // anders h0 = null
			Numbers[] extra = ll0.getAdapter().adapt(Numbers[].class);
			setAngleValue(l, extra[0], Numbers.neg(extra[1]));
			return;
		} else if(isVector(ll0))
		{
			if(ll0.getAdapter().adapt(Numbers[].class) == null)
			{
				ll0.getRegistered().define(ll0);
			}
				Numbers[] extra = ll0.getAdapter().adapt(Numbers[].class);
				l.setState(Label.VECTOR);
				DefaultAdapter.getDefault(l).put( new Numbers[] { Numbers.neg(extra[0]), Numbers.neg(extra[1]) });

		}
		
		value = ll0.value;
		value = Numbers.neg(value);
		setStringValue(l,value);
	}

	/** -CONSTANT is ook CONSTANT
	 * @see fi.euclides.proof.LabelDelegate#define(fi.euclides.model.Label)
	 */
	public boolean define(Label l) {
		if(((Label) l.getDepend()[0]).getState() == Label.CONSTANT)
			l.setState(Label.CONSTANT);
		return super.define(l);
	}

}
