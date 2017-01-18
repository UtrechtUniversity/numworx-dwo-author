package fi.euclides.expr;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelTester;

public class Relation1 extends LabelTester {

	private byte bits;

	public Relation1(String string, byte b) {
		super(string);
		bits = b;
	}
	public final static byte lt  = 1;
	public final static byte eq  = 2;
	public final static byte gt  = 4;
	public final static byte leq = (byte) (lt+eq);
	public final static byte geq = (byte) (gt+eq);
	public final static byte neq = (byte) (lt+gt);
	
//	public final static Relation1 LEQ = new Relation1("\u2264", leq);
//	public final static Relation1 GEQ = new Relation1(">=", geq);
//	public final static Relation1 LT = new Relation1("<", lt);
//	public final static Relation1 GT = new Relation1(">", gt);
//	public final static Relation1 NEQ = new Relation1("<>", neq);
	
	public Destroyable[] createDepend() {
		return new Label[2];
	}

	public boolean define(Label l) {
		test(l);
		Destroyable[] depend = l.getDepend();
		l.setString( s(depend[0]) + string + s(depend[1]));
		return true;
	}

	protected boolean test(Label l) {
		Label[] ll = (Label[])l.getDepend();
		Numbers value = Numbers.sub(ll[0].value, ll[1].value);
		int sign = Numbers.signum(value);

		sign = 1 << (sign+1);
		if((bits & sign) != 0)
			l.setValue(Numbers.ZERO); // true
		else
			l.setValue(Numbers.ONE);  // false
		return setState(l, l.value, 0.0);
	}

}
