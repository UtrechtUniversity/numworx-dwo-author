package nl.numworx.geodefiner.ui;

import java.awt.Color;
import java.util.Optional;
import javax.inject.Inject;

import fi.euclides.event.Tracker;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Triangle;
import fi.euclides.util.DefaultAdapter;
import nl.numworx.geodefiner.merge.RenameAction;

public class TriangleModel extends CircleModel {

	@Inject TriangleModel(Tracker t, Optional<RenameAction> ra) {
		super(t, ra);
	}

	@Override
	public void install(Destroyable item) {
		super.install(item);
		if (item instanceof Triangle) {
			DefaultAdapter.getDefault(item).put(Color.class, TRANSPARANT);
		}
	}

	@Override
	public void installLight() {
		super.installLight();
		if (item instanceof Triangle) {
			DefaultAdapter.getDefault(item).put(Color.class, TRANSPARANT);
		}
	}

}
