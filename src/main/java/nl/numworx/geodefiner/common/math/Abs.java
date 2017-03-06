package nl.numworx.geodefiner.common.math;

import fi.euclides.event.Tracker;
import fi.euclides.expr.Transc1;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.proof.AfstandHandler;
import fi.euclides.proof.LabelValue;
import fi.euclides.util.Observable;

class Abs extends LabelValue {

	AfstandHandler afstand = new AfstandHandler("||");
	LabelValue abs = Transc1.ABS;

	Abs() {
		super("||");
	}

	@Override
	public String getSymbolicValue(Label l) {
		return "|" + s(l) + "|";
	}

	@Override
	public Destroyable[] createDepend() {
		Destroyable[] depend = new Destroyable[4];
		depend[2] = getTracker().getModel().getO();
		depend[3] = getTracker().getModel().getU();
		return depend;
	}

	@Override
	public boolean define(Label l) {
		if(l.getDepend()[0] instanceof Label) {
			l.setDepend(new Label[] { (Label) l.getDepend()[0] });
			getTracker().getModel().getO().deleteObserver(l); // not dependent on U or O
			getTracker().getModel().getU().deleteObserver(l);
			l.register(abs);
			return abs.define(l);
		} else if (l.getDepend()[0] instanceof Punt) {
			l.getDepend()[1] = l.getDepend()[2];
			l.register(afstand);
			return afstand.define(l);			
		} else {
			l.getDepend()[1] = l.getDepend()[0];
			l.register(afstand);
			return afstand.define(l);
		}
	}

	@Override
	public void setTracker(Tracker tracker) {
		afstand.setTracker(tracker);
		abs.setTracker(tracker);
		super.setTracker(tracker);
	}


	
	
}
