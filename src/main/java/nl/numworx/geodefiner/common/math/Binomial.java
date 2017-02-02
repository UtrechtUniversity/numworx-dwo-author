package nl.numworx.geodefiner.common.math;

import fi.euclides.expr.Som;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;

public class Binomial extends Som {

	public Binomial() {
		super("binomial");
	}

	@Override
	public String getSymbolicValue(Label l) {
		Destroyable d[] = l.getDepend();
		return string + "(" + s(d[0]) + "," + s(d[1]) + ")";
	}

	@Override
	protected void recalc(Label l, Label[] labels) {
		int a = (int)Math.round(labels[0].value.doubleValue());
		int b = (int)Math.round(labels[1].value.doubleValue());
		Numbers value = Numbers.createDouble(binom(a,b));
		setStringValue(l, value);
	}

	public static double binom(int n, int k)
	{	if(n<k)return 0;
		double[] b = new double[n+1];
		b[0] = 1;
		for(int i=1 ; i<n+1 ; i++)
		{	b[i] = 1;
			for(int j=i-1 ; j>0 ; j--)
			{	b[j] += b[j-1];
			}
		}
		return b[k];
	}

}
