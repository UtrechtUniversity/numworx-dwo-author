package fi.euclides.model;

import fi.euclides.model.math.Numbers;

public class SnijPunt extends PuntOp2<Lijn, Lijn>  {

	public static final String TYPE = "Ps";

	public SnijPunt(Lijn lijn1, Lijn lijn2) {
		super(lijn1, lijn2);
		recalc();
	}

	public SnijPunt() {
	}

	protected void recalc() {
		Numbers l1dy = lijn1.getDYn();
		Numbers l2dy = lijn2.getDYn();
		Numbers l1dx = lijn1.getDXn();
		Numbers l2dx = lijn2.getDXn();
		Numbers detn = Numbers.sub(Numbers.mul(l1dy, l2dx), Numbers.mul(l1dx, l2dy));
		setDefined(Math.abs(detn.doubleValue()) > 1.0e-10);
		if(isDefined())	
		{
			Numbers an = Numbers.div(
					Numbers.sub(
							Numbers.mul(Numbers.sub(lijn2.getY1n(), lijn1.getY1n()), l2dx),
							Numbers.mul(Numbers.sub(lijn2.getX1n(), lijn1.getX1n()), l2dy)),
					detn);
			setXY( Numbers.add(lijn1.getX1n(), Numbers.mul(an, l1dx)),
					Numbers.add(lijn1.getY1n(), Numbers.mul(an, l1dy)));
			
			setDefined(lijn1.contains(this) && lijn2.contains(this));
		}
	}

	public String key() {
		return TYPE;
	}

}
