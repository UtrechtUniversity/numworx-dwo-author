package nl.numworx.geodefiner.common;

import java.util.List;

import fi.euclides.event.Tracker;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;

public class NamedPoint extends Punt {

	Tracker tracker;
	String  name;
	Punt p;
	
	public Punt getP() {
		return p;
	}
	
	NamedPoint(String name, Tracker tracker) {
		super(Numbers.NaN, Numbers.NaN);
		this.name = name;
		this.tracker = tracker;
	}
	
	public boolean similar(Punt p) {
		List<Destroyable> lijnen = tracker.getModel().getLijnen();
		for(Destroyable l: lijnen) {
			if(l instanceof Label) {
				Label label = (Label) l;
				if(name.equals(label.getString())) {
					Punt vp = label.getP();
					if(vp instanceof Volgpunt) {
						vp = ((Volgpunt) vp).getP();
					}
					if(p == vp) {
						this.p = p;
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean similar(Label label) {
		if(name.equals(label.getString())) {
			Punt vp = label.getP();
			if(vp instanceof Volgpunt) {
				vp = ((Volgpunt) vp).getP();
			}
			this.p = vp;
			return true;
		}
		return false;
	}
}
