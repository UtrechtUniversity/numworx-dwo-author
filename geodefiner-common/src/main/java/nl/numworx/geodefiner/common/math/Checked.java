package nl.numworx.geodefiner.common.math;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelTester;
import fi.euclides.util.Observable;

public class Checked extends LabelTester {

	private final Collection<Label> checks = new LinkedList<>();
	private Numbers value = Numbers.ONE;
	
	public Checked() {
		super("checked");
	}

	public void setValue(Numbers value) {
		this.value = value;
		for(Label check: checks) {
			setState(check,value,0.0);
		}
	}
	
	public void setValue(boolean b) {
		setValue(b?Numbers.ZERO:Numbers.ONE);
	}
	
	
	
	@Override
	public Destroyable[] createDepend() {
		return Label.EMPTY;
	}

	@Override
	public boolean test(Label l) {
		boolean r = setState(l, l.value, 0.0);
		return true;
	}

	@Override
	public boolean define(Label l) {
		l.setValue(value);
		l.register(this);
		l.setString(string);
		checks.add(l);
		return setState(l, l.value, 0.0);
	}

	@Override
	public boolean equals(Label label, Label other) {
		return label == other;
	}

	public void update(Observable observable, Object arg) {
		if(arg == Destroyable.DESTROY)
			remove(observable);
		super.update(observable, arg);
	}


	private void remove(Observable observable) {
		checks.remove(observable);
	}

}
