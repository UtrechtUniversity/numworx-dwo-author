package nl.numworx.geodefiner.ui;

import fi.euclides.model.Destroyable;
import nl.numworx.geodefiner.common.Tips;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;

public class RayModel extends SegmentModel {

	private void noEndTip() {
		switch(tip) {
			case ATEND: tip = Tips.NOTIP; break;
			case ATSTARTEND: tip = Tips.ATSTART;
			default:
		}
	}
	
	@Override
	public void fromMap(ObjectMap map) {
		super.fromMap(map);
		noEndTip();
	}

	@Override
	public UIEditor editor() {
		noEndTip();
		return new RayPane(this);
	}

	@Override
	public void install(Destroyable item) {
		noEndTip();
		super.install(item);
	}


}
