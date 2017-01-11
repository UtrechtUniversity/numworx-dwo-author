package nl.numworx.geodefiner.ui;

import java.awt.Font;
import java.util.Collections;
import java.util.Map;

import nl.numworx.geodefiner.common.Align;
import nl.numworx.geodefiner.common.UIModel;
import nl.numworx.geodefiner.common.Volgpunt;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DefaultAdapter;

public class TextModel extends ColorModel<Label> {
	Align align = Align.BASE;
	Font  font  = fi.wiskopdr.WiskOpdr.formuleFont0; // bijvoorbeeld.

	@Override
	public void install(Label item) {
		DefaultAdapter.getDefault(item).put(align);
		DefaultAdapter.getDefault(item).put(Font.class, font);
		super.install(item);
	}

	@Override
	public UIModel<Label, UIEditor> init(Label item) {
		align = item.adapt(Align.class);
		if(align == null) align= Align.BASE;
		return super.init(item);
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		map.put("align", align.name());
		map.put("font", Collections.singletonMap("size", font.getSize()));
		Punt p = item.getP();
		if(p instanceof Volgpunt) {
			map.put("dx", ((Volgpunt) p).getDx().doubleValue());
			map.put("dy", ((Volgpunt) p).getDy().doubleValue());
		}
		return map;
	}

	@Override
	public void fromMap(ObjectMap map) {
		try {
			align = Align.valueOf(map.getString("align"));
		} catch (Exception e) {
			align = Align.BASE;
		}
		if(map.containsKey("font")) {
			ObjectMap fontmap = map.getObjectMap("font");
			float size = fontmap.getInt("size");
			font = font.deriveFont(size);
		}
		if(item.getP() instanceof Volgpunt) {
		Numbers dx = Numbers.ZERO;
		if(map.containsKey("dx")) {
			dx = Numbers.createDouble(map.getDouble("dx"));
		}
		Numbers dy = Numbers.ZERO;
		if(map.containsKey("dy")) {
			dy = Numbers.createDouble(map.getDouble("dy"));
		}
			((Volgpunt)item.getP()).setDxy(dx, dy);
		}
		
		
		super.fromMap(map);
	}

	@Override
	public UIEditor editor() {
		return new TextPane(this);
	}
	
	public String sample() {
		return item.getString();
	}
}
