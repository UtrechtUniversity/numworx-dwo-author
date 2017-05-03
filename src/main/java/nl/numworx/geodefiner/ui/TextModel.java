package nl.numworx.geodefiner.ui;

import java.awt.Font;
import java.util.Collections;
import java.util.Map;

import nl.numworx.geodefiner.common.Align;
import nl.numworx.geodefiner.common.UIModel;
import nl.numworx.geodefiner.common.Volgpunt;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DefaultAdapter;

public class TextModel extends ColorModel<Label> {
	public static final float DEFAULT_SIZE = 12;
	Align align = Align.BASE;
	Font  font  = fi.wiskopdr.WiskOpdr.tekstFont; // bijvoorbeeld.
	private float dx,dy;
	Boolean alwaysF;
	Boolean herleid;
	Label item;

	public boolean isAlwaysF() {
		return Boolean.TRUE.equals(alwaysF);
	}

	public boolean isHerleid() {
		return isAlwaysF() && Boolean.TRUE.equals(herleid);
	}

	public void install() {
		install(item);
	}
	
	public void install(Label item) {
		DefaultAdapter adapter = DefaultAdapter.getDefault(item);
		adapter.put(align);
		adapter.put(Font.class, font);
		adapter.put(Boolean.class, alwaysF);
		Punt p = item.getP();
		if(p instanceof Volgpunt) {
			((Volgpunt) p).setDxy(Numbers.createDouble(dx), Numbers.createDouble(dy));
		}
		super.install(item);
	}

	@Override
	public UIModel<Label, UIEditor> init(Label item) {
		if(item != null) align = item.adapt(Align.class);
		if(align == null) align= Align.BASE;
		if(item != null) alwaysF = item.adapt(Boolean.class);
		if(item == null && this.item != null && this.item.getP() instanceof Volgpunt)
		{	setdxy();
		}
		this.item = item;
		return super.init(item);
	}

	private void setdxy() {
		if(item.getP()instanceof Volgpunt) {
		Volgpunt p = (Volgpunt) this.item.getP();
			dx = (float) p.getDx().doubleValue();
			dy = (float) p.getDy().doubleValue();
	}}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		map.put("align", align.name());
		map.put("font", Collections.singletonMap("size", font.getSize()));
		if(isAlwaysF())
			map.put("alwaysF", Boolean.TRUE);
		if(isHerleid())
			map.put("herleid", Boolean.TRUE);
		Punt p = item != null ? item.getP() : null;
		if(p instanceof Volgpunt) {
			map.put("dx", ((Volgpunt) p).getDx().doubleValue());
			map.put("dy", ((Volgpunt) p).getDy().doubleValue());
		} else if (item == null) {
			if(dx != 0f) map.put("dx", dx);
			if(dy != 0f) map.put("dy", dy);
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
		if(map.containsKey("dx")) {
			dx = (float)map.getDouble("dx");
		}
		if(map.containsKey("dy")) {
			dy = (float)map.getDouble("dy");
		}
		alwaysF = Boolean.valueOf(map.getBoolean("alwaysF", false));
		herleid = Boolean.valueOf(map.getBoolean("herleid", false));
		super.fromMap(map);
	}

	@Override
	public UIEditor editor() {
		setdxy();
		return new TextPane(this);
	}
	
	public String sample() {
		return item.getString();
	}
}
