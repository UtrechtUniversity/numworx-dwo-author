package nl.numworx.geodefiner.ui;

import javax.inject.Inject;

import fi.euclides.event.Tracker;
import fi.euclides.model.Model;

public class UModel extends PointModel {

	@Override
	public UIEditor editor() {
		return new UPane(this);
	}

	@Inject UModel(Tracker model) {
	  super();
	  set(model);
	  init(model.getModel().getU());
	}
}
