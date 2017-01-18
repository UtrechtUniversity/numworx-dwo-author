package fi.euclides.expr;

import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;

public class Minus extends Som {

	public Minus() {
		super("-");
		
	}

	Numbers eval(Label[] ll) {
		return Numbers.sub(ll[0].value, ll[1].value);
	}
	
	protected void recalc(Label l, Label[] ll) {
		Numbers value;
		if(isHoek(ll[0]) && isHoek(ll[1]))
		{	
			fixextra(ll);
			Numbers[] h0 = ll[0].getAdapter().adapt(Numbers[].class); // s0 + c0
			Numbers[] h1 = ll[1].getAdapter().adapt(Numbers[].class); // s1 + c1
			Numbers c0 = h0[0];
			Numbers s0 = h0[1];
			Numbers c1 = h1[0];
			Numbers s1 = h1[1];
			/*
			 * tan(a+b) = sin(a+b)/cos(a+b) = etc.
			 * 
			 */
			Numbers s = Numbers.sub(Numbers.mul(s0,c1), Numbers.mul(c0,s1));
			Numbers c = Numbers.add(Numbers.mul(c0,c1), Numbers.mul(s0,s1));
			setAngleValue(l, c, s);
			
		} else // TODO if (isVector() ...
		{	
			value = eval(ll);
			setStringValue(l,value);
		}
	}

}
