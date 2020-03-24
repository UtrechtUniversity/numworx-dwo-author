package nl.numworx.geodefiner.ui;

import nl.numworx.geodefiner.common.UIModel;

import javax.inject.Inject;

import fi.euclides.event.Tracker;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;

public class OModel extends PointModel {

	Punt item;

	@Override
	public UIEditor editor() {
		return new OPane(this);
	}

	@Override
	public UIModel<Destroyable, UIEditor> init(Punt item) {
		this.item = item;
		return super.init(item);
	}

	@Inject OModel() {}
	@Inject void setP(Tracker model) { init(model.getModel().getO()); }
}
