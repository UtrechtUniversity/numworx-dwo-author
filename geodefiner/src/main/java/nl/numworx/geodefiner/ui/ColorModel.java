package nl.numworx.geodefiner.ui;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import nl.numworx.geodefiner.GeoDefiner;
import nl.numworx.geodefiner.IsColor;
import nl.numworx.geodefiner.common.Randomizer;
import nl.numworx.geodefiner.common.UIModel;
import nl.numworx.geodefiner.merge.RenameAction;
import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMSymbol;
import nl.tue.win.riaca.openmath.lang.OMVariable;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.event.Tracker;
import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.formuleobjects.TokenMgrError;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.openmath.Expression;
import fi.euclides.util.DefaultAdapter;

public class ColorModel<T extends Destroyable> implements UIModel<T, UIEditor> {

	public static final String VISIBILITY = "visibility";
	private static final OMObject EUCLIDES_VISIBLE = new OMSymbol("euclides", "visible");
	public Destroyable item;
	Color color = Color.black, trailColor = Color.LIGHT_GRAY;
	boolean visible = true, trail,log;
	Label visibility = new Label();
	Tracker tracker;
	Integer zOrder;
	private Optional<RenameAction> rename = Optional.empty();
	
	public  void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void install(Destroyable item) {
		DefaultAdapter adapter = DefaultAdapter.getDefault(item);
		adapter.put(color);
		item.setVisible(visible);
        if(trail && tracker != null) {
          tracker.getModel().startTrail(item);
        }
		if (visibility.getString() != null && tracker != null) {
			try {
				String formula = visibility.getString();
				final String orig = formula; 
				Randomizer r = tracker.adapt(Randomizer.class);
				if(r != null) formula = r.randomize(formula);
				formula = formula.substring(2);
				visibility.destroy(); visibility = new Label();
				visibility.setString(orig);
				OMObject o = new FormuleParser(formula).logic();
				OMApplication oma = new OMApplication();
				oma.addElement(EUCLIDES_VISIBLE);
				oma.addElement(new OMVariable(tracker.getMapper().toString(item)));
				oma.addElement(o);
				Expression expr = tracker.adapt(Expression.class);
				Destroyable v = expr.interpret(oma, visibility, tracker.getMapper());
				v.setVisible(false);
				tracker.getModel().add(v);
			} catch (Exception e) {
			} catch (TokenMgrError tme) {
				tme.printStackTrace();
			}
		}
		isColor(item);
	}

	private void isColor(Destroyable d) {
		IsColor is = d.adapt(IsColor.class);
		if (is != null) 
			is.updateColor();
	}

	public void install() {
		if(item != null)
			install(item);
	}

	public void installLight() {
		DefaultAdapter.getDefault(item).put(color);
		isColor(item);
	}
	
	public UIModel<T, UIEditor> init(T item) {
		return init2(item);
	}

	public Map<String, Object> toMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("color", color.getRGB());
		map.put("visible", visible);
		if(getVisibility() != null && !getVisibility().isEmpty())
		{
			map.put(VISIBILITY, getVisibility());
		}
		if(trail) {
		  map.put("trail", Boolean.TRUE);
		}
		if (log) {
		  map.put("log", Boolean.TRUE);
		}
		return map;
	}

	public void fromMap(ObjectMap map) {
		if(map.containsKey("color"))
			color = new Color( map.getInt("color"), true);
		visible = map.getBoolean("visible", visible);
		visibility.setString(map.getString(VISIBILITY));
		trail = map.getBoolean("trail", false);
		log = map.getBoolean("log", false);
	}

	public UIEditor editor() {
		return new ColorPane<ColorModel<?>>(this);
	}

	@Override
	public void fromLightMap(ObjectMap map) {
		if (map.containsKey("color")) {
			color = new Color ( map.getInt("color"), true);
		} else {
			color = item.adapt(Color.class);
			if (color == null) color = Color.black;
		}
	}
	
	@Override
	public Map<String,Object> toLightMap() {
		Map<String,Object> m = new TreeMap<>();
		m.put("color", color.getRGB());
		return m;
	}
	
	
	public String getVisibility() {
		return visibility.getString();
	}

	public UIModel<T, UIEditor> set(Tracker tracker) {
		this.tracker = tracker;
		return this;
	}

	@Override
	public UIModel<T, UIEditor> init2(Destroyable item) {
		this.item = item;
		if(item != null) {
			color = item.getAdapter().adapt(Color.class);
			visible = item.isVisible();
		} else if (this.item != null) 
		{
			visible = this.item.isVisible();
		}
		if(color == null) color = Color.black;
		return this;
	}

	public Optional<RenameAction> getRename() {
		return rename;
	}

	public void setRename(Optional<RenameAction> rename) {
		if (GeoDefiner.isExperimental && GeoDefiner.isPremium)
			this.rename = rename;
	}

}
