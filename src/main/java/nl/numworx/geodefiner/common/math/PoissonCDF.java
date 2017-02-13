package nl.numworx.geodefiner.common.math;

import fi.euclides.expr.Som;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;

class PoissonCDF extends Som {

	static double poissonCum(double lambda, int x)
	{	double cum = 0;
		for(int i=1 ; i<x+1 ; i++)
		{	cum = cum + PoissonPDF.poisson(lambda, i);
		}
		return cum;
	}
	
	private double geefWaarde(Numbers kind1, Numbers kind2)
	{	double lambda = kind1.doubleValue();
		double x = kind2.doubleValue();
		double waarde = Double.NaN;
		if(Double.isNaN(lambda) || Double.isNaN(x)) return Double.NaN;
		waarde = poissonCum(lambda,(int)Math.rint(x));
		return waarde;
	}

	@Override
	protected void recalc(Label l, Label[] labels) {
		double waarde = geefWaarde(labels[0].value, labels[1].value);
		setStringValue(l, Numbers.createDouble(waarde));
	}

	PoissonCDF() {
		super("poissoncdf");
	}

}
