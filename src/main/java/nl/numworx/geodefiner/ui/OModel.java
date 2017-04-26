package nl.numworx.geodefiner.ui;

public class OModel extends PointModel {

	@Override
	public UIEditor editor() {
		return new OPane(this);
	}

}
