package fi.euclides.expr;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DefaultAdapter;


final public class If extends Som {

	public If() {
		super("if");
	}

	public String getSymbolicValue(Label l) {
		Destroyable[] depend = l.getDepend();
		
	return  "if " + s(depend[0]) + " then " + s(depend[1]) + " else " + s(depend[2]) + " endif";
	}

	public Destroyable[] createDepend() {
		return new Label[3];
	}

	protected void recalc(Label l, Label[] depend) {
		getTracker().getModel().executeDelay();
		Label copy = depend[1];
		if(Label.FALSE == depend[0].getState())
		{
			copy = depend[2];
		}
		DefaultAdapter adapter = DefaultAdapter.getDefault(l);
		adapter.put(Numbers.class, copy.getAdapter().adapt(Numbers.class));
		adapter.put(Numbers[].class, copy.getAdapter().adapt(Numbers[].class));
		l.setState(copy.getState());
		l.setString(copy.getString());
		l.setValue(copy.value);
	}

}
