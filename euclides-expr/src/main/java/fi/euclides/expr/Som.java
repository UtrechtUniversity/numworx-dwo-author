package fi.euclides.expr;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelValue;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.JMath;
import fi.euclides.util.Observable;

public class Som extends LabelValue {

	public static final String TYPE = "+";

	protected Som(String string) {
		super(string);
	}

	protected Som() {
		this(TYPE);
	}
	
	public String getSymbolicValue(Label l) {
		Destroyable[] d = l.getDepend();
		return "(" + s(d[0])  + getSubKey() + s(d[1]) + ")";
	}

	
	public Destroyable[] createDepend() {
		return new Label[2];
	}

	// mischien later, voor de display 3π ipv 9.42
	private boolean withPI(Label l)
	{
		return l.getString().endsWith("\u03c0");
	}
	
	static boolean isHoek(Label l)
	{
		return l.getState() == Label.HOEK;
	}
	
	public void update(Observable observable, Object arg) {
		if(arg == Destroyable.DESTROY 
		|| arg == Destroyable.VISIBLE
		|| arg == Label.STATE
		|| arg == Destroyable.RENAME)
			return;
		
		Label l = (Label)observable;
		recalc(l, (Label[]) l.getDepend());
	}

	protected void recalc(Label l, Label[] labels) {
		Numbers value;
		if(isHoek(labels[0]) && isHoek(labels[1]))
		{
			fixextra(labels);
			Numbers[] h0 = labels[0].getAdapter().adapt(Numbers[].class); // s0 + c0
			Numbers[] h1 = labels[1].getAdapter().adapt(Numbers[].class); // s1 + c1
			Numbers c0 = h0[0];
			Numbers s0 = h0[1];
			Numbers c1 = h1[0];
			Numbers s1 = h1[1];
			/*
			 * tan(a+b) = sin(a+b)/cos(a+b) = etc.
			 * 
			 */
			Numbers s = Numbers.add(Numbers.mul(s0,c1), Numbers.mul(c0,s1));
			Numbers c = Numbers.sub(Numbers.mul(c0,c1), Numbers.mul(s0,s1));
			setAngleValue(l, c, s);
			return;
		} 
		
		if(isVector(labels[0]) && isVector(labels[1]))
		{	fixextra(labels);
			DefaultAdapter.getDefault(l).put( evalExtra(labels) );
			l.setState(Label.VECTOR);
		}
		{	
			value = eval(labels);	
			setStringValue(l,value);
		}
	}

	private Numbers[] evalExtra(Label[] ll) {
		Numbers[] extra0 = ll[0].getAdapter().adapt(Numbers[].class);
		Numbers[] extra1 = ll[1].getAdapter().adapt(Numbers[].class);
		return new Numbers[] { Numbers.add(extra0[0],extra1[0]),  Numbers.add(extra0[1],extra1[1]) };
	}

	protected static boolean isVector(Label label) {
		return Label.VECTOR == label.getState();
	}

	protected void fixextra(Label[] ll) {
		//if(ll[0].extra == null)
			ll[0].getRegistered().define(ll[0]); // anders h0 = null
		//if(ll[1].extra == null)
			ll[1].getRegistered().define(ll[1]);
	}

	void setAngleValue(Label l, Numbers c, Numbers s) {
		Numbers value;
		DefaultAdapter.getDefault(l).put( new Numbers[] { c, s });
		//value = Numbers.div(c,s);
		double y = s.doubleValue();
		double x = c.doubleValue();
		//if(y<0) { y = -y; x = -x; } // bij modulo 180
		double result = JMath.atan2(y, x);
		if(result < 0)
			result += Math.PI*2.0;
		value = Numbers.createDouble(result);

		l.setState(Label.HOEK);
		l.setString(hoekAsString(value));
		l.setValue(value);
	}

	Numbers eval(Label[] ll) {
		return Numbers.add(ll[0].value, ll[1].value);
	}

	protected String s(Destroyable d) {
		if(d.getIndex()==0 && d instanceof Label) {
			Label d2 = (Label) d;
			if(d2.getRegistered() instanceof LabelValue)
				return ((LabelValue)d2.getRegistered()).getSymbolicValue(d2);
			return d2.getString();
		}
		return "$"+ super.s(d);
	}

}
