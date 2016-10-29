package nl.numworx.geodefiner.ui;

import java.util.Map;

import javax.swing.JLabel;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Lijn;

public class LineModel extends ColorModel<Destroyable> {

	public UIModel<Destroyable> init(Lijn item) {
		return super.init(item);
	}

	public Map<String, Object> toMap() {
		return super.toMap();
	}

	public void fromMap(Map<String, Object> map) {
		super.fromMap(map);
	}

	public UIEditor editor() {
		ColorPane<LineModel> colorPane = new ColorPane<LineModel>(this);
		colorPane.chooser.setPreviewPanel(new JLabel("⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
		return colorPane;
	}

}
