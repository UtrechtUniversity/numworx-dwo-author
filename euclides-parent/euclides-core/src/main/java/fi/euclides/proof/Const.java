package fi.euclides.proof;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;

final public class Const extends LabelValue {

	public static final String TYPE = "1";
	private Vector consts = new Vector();
		
	public Enumeration getConsts()
	{
		return consts.elements();
	}
	
	public Const() {
		super(TYPE);
	}

	public String getSymbolicValue(Label l) {
		return l.getString();
	}

	public Destroyable[] createDepend() {
		return Label.EMPTY;
	}

	public boolean equals(Label label, Label other) {
		boolean result  = super.equals(label, other);
		if(result && label.value != null && other.value != null && label.value.getClass() == other.value.getClass())
			return Numbers.signum(Numbers.sub(label.value, other.value)) == 0;
		return false;
	}

	public void update(Observable observable, Object arg) {
		if(arg == Destroyable.DESTROY)
			remove(observable);
		super.update(observable, arg);
	}

	private void remove(Observable observable) {
		int length = consts.size();
		for (int i = 0; i < length; i++) {
			if(observable == consts.elementAt(i))
			{
				consts.remove(i);
				length--;
			}
		}
	}

	public Destroyable[] createDepend(Codec codec, Label label)
			throws IOException {
		consts.addElement(label);
		return super.createDepend(codec, label);
	}

	public boolean define(Label l) {
		consts.addElement(l);
		l.setState(Label.CONSTANT);
		l.setDepend(createDepend());
		l.register(this);
		return super.define(l);
	}

}
