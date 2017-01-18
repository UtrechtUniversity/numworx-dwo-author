package fi.euclides.expr;

import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DefaultAdapter;

public class Div extends Som {

	public Div() {
		super("/");
	}

	Numbers eval(Label[] ll) {
		return Numbers.div(ll[0].value, ll[1].value);
	}

	protected void recalc(Label l, Label[] labels) {
		if(isVector(labels[0]))
		{
			fixextra(labels);
			Numbers[] xy = labels[0].getAdapter().adapt(Numbers[].class);
			Numbers m    = labels[1].value;
			Numbers v    = Numbers.createComplex(xy[0], xy[1]);
			v = Numbers.div(v,m);
			DefaultAdapter.getDefault(l).put( new Numbers[] {
					Numbers.real(v), 
					Numbers.imag(v)
			});
			l.setState(Label.VECTOR);
			v = eval(labels);
			setStringValue(l,v);
			return;
		}
		super.recalc(l, labels);
	}



}
