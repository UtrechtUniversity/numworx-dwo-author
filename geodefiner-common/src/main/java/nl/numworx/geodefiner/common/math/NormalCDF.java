package nl.numworx.geodefiner.common.math;

import fi.euclides.expr.Som;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.wiskopdr.expressies.StatUtil;

class NormalCDF extends Som {

	double phi(double z)
	{	return (1 + StatUtil.erf(z / Math.sqrt(2))) / 2;
	}

	double geefWaarde(double grensLinks, double grensRechts, double mu, double sigma)
	{	
		double waarde = Double.NaN;
		if(Double.isNaN(grensLinks) || Double.isNaN(grensRechts) || Double.isNaN(mu) || Double.isNaN(sigma))waarde = Double.NaN;
		else waarde = phi((grensRechts - mu) / sigma) - phi((grensLinks - mu) / sigma);			
		return waarde;
	}
	@Override
	public Destroyable[] createDepend() {
		return new Label[4];
	}

	@Override
	protected void recalc(Label l, Label[] labels) {
		
		double mu = d(labels[2]);
		double grensLinks = d(labels[0]);
		double sigma = d(labels[3]);
		double grensRechts = d(labels[1]);
		Numbers waarde = Numbers.createDouble( geefWaarde(grensLinks, grensRechts, mu, sigma));
		setStringValue(l, waarde);
	}

	private double d(Label label) {
		return label.value.doubleValue();
	}

	NormalCDF() {
		super("normalcdf");
	}
}
