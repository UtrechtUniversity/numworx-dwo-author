package nl.numworx.geodefiner.ui;

import java.awt.Color;
import java.awt.Paint;
import java.util.Map;

import nl.numworx.geodefiner.common.UIModel;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.util.DefaultAdapter;

public class CircleModel extends LineModel {

	static final Color TRANSPARANT = new Color(0, true);
	
	public Paint fill = TRANSPARANT;
	
	public UIModel<Destroyable, UIEditor> init(Cirkel item) {
		return super.init(item);
	}

	public UIModel<Destroyable, UIEditor> init(Boog item) {
		return super.init(item);
	}

	/* (non-Javadoc)
	 * @see nl.numworx.geodefiner.ui.LineModel#toMap()
	 */
	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		if( fill == null || TRANSPARANT .equals(fill))
			return map;
		if(fill instanceof Color)
			map.put("fill", ((Color) fill).getRGB());
		return map;
	}

	/* (non-Javadoc)
	 * @see nl.numworx.geodefiner.ui.LineModel#fromMap(nl.uu.fi.dwo.interaction.client.json.ObjectMap)
	 */
	@Override
	public void fromMap(ObjectMap map) {
		super.fromMap(map);
		if(map.containsKey("fill")) {
			fill = new Color( map.getInt("fill"), true);
		} else
			fill = TRANSPARANT;
	}

	/* (non-Javadoc)
	 * @see nl.numworx.geodefiner.ui.LineModel#editor()
	 */
	@Override
	public UIEditor editor() {
		return new CirclePane(this);
	}

	/* (non-Javadoc)
	 * @see nl.numworx.geodefiner.ui.LineModel#install(fi.euclides.model.Destroyable)
	 */
	@Override
	public void install(Destroyable item) {
		super.install(item);	
		DefaultAdapter.getDefault(item)
		.put(Paint.class, fill.equals(TRANSPARANT)? null : fill);
	}
}
