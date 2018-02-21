package nl.numworx.geodefiner.common;

import fi.euclides.event.AddPuntHandler;
import fi.euclides.model.Punt;
import fi.euclides.model.algo.FreePoint;
import fi.euclides.model.math.Numbers;

public class AddSnapPuntHandler extends AddPuntHandler {

	@Override
	protected Punt buildPunt(Numbers x, Numbers y) {
		Punt buildPunt = super.buildPunt(x, y);
		getTracker().adapt(Snapper.class).snap(buildPunt.adapt(FreePoint.class), getModel());
		return buildPunt;
	}

}
