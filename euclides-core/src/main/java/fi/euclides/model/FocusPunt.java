package fi.euclides.model;

import fi.euclides.model.OpObject;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.algo.FreePoint;
import fi.euclides.model.algo.PointOnAlgorithm;
import fi.euclides.model.math.Numbers;

public class FocusPunt extends PuntOp<Kegelsnede2> implements OpObject<Kegelsnede2>, PointOnAlgorithm<Kegelsnede2> {

	public static final String TYPE = "F";
		
	public FocusPunt() {
		setFree(false);
	}

	public FocusPunt(Kegelsnede2 o) {
		super(Numbers.ZERO, Numbers.ZERO, o);
		setFree(false);
		setOb(this);
		recalc(Numbers.ZERO, Numbers.ZERO);
	}

	public FocusPunt(Kegelsnede2 o, FocusPunt other) {
		super(Numbers.ZERO, Numbers.ZERO, o, other);
		setFree(false);
	}

	public String key() {
		return TYPE;
	}

	public void recalc(Kegelsnede2 on, FreePoint punt, double x, double y) {
		update(on, punt);
	}

	public PointOnAlgorithm<Kegelsnede2> getAlgo() {
		return this;
	}

	public PuntOp<Kegelsnede2> pointOn(Numbers x, Numbers y) {
		return null;
	}

	@Override
	public void update(Kegelsnede2 on, FreePoint punt) {
		if(punt != this) 
			on.strategy.focus(this, (Punt) punt);
	}

	@Override
	public void recalc(Kegelsnede2 on, FreePoint punt, Numbers x, Numbers y) {
		update(on, punt);
	}

}
