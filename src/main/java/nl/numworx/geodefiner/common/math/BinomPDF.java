package nl.numworx.geodefiner.common.math;

import fi.euclides.expr.Som;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.wiskopdr.expressies.Bin;

public class BinomPDF extends Som {
	
	BinomPDF() {
		super("binomPDF");
	}

	public double geefWaarde(Numbers kind1, Numbers kind2, Numbers kind3)
	{	
		if ( kind1.isNaN() || kind2.isNaN() || kind3.isNaN())
			return Double.NaN;
		
		long n = Numbers.round(kind1).longValue();
		double p = kind2.doubleValue();
		long k = Numbers.round(kind3).longValue();
		double waarde;
		double nBovenK = Bin.binom((int)(n),(int)(k));
		double kans = Math.pow(p, k) * Math.pow(1-p, n-k);
		waarde = nBovenK*kans;
		return waarde;
	}

	@Override
	public Destroyable[] createDepend() {
		return new Label[3];
	}

	@Override
	protected void recalc(Label l, Label[] labels) {
		double waarde = geefWaarde(labels[0].value, labels[1].value, labels[1].value);
		setStringValue(l, Numbers.createDouble(waarde));
	}

}
