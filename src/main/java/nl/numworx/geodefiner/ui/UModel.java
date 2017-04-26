package nl.numworx.geodefiner.ui;

public class UModel extends PointModel {

	@Override
	public UIEditor editor() {
		return new UPane(this);
	}

}
