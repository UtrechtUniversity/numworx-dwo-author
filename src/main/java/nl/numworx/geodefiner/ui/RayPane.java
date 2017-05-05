package nl.numworx.geodefiner.ui;

import nl.numworx.geodefiner.common.Tips;

public class RayPane extends SegmentPane {

	public RayPane(SegmentModel model) {
		super(model);
	}
	static final Tips[] STARTTIPS = { Tips.NOTIP, Tips.ATSTART };
	
	Tips[] tips() { return STARTTIPS; }
}
