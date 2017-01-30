package nl.numworx.geodefiner.ui;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import nl.numworx.geodefiner.common.UIModel;
import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMSymbol;
import nl.tue.win.riaca.openmath.lang.OMVariable;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.event.Tracker;
import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.formuleobjects.ParseException;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.algo.FreePoint;
import fi.euclides.openmath.Expression;
import fi.euclides.util.DefaultAdapter;

public class ColorModel<T extends Destroyable> implements UIModel<T, UIEditor> {

	private static final OMObject EUCLIDES_VISIBLE = new OMSymbol("euclides", "visible");
	T item;
	Color color = Color.black;
	boolean visible;
	Label visibility = new Label();
	Tracker tracker;
	
	public  void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void install(T item) {
		DefaultAdapter adapter = DefaultAdapter.getDefault(item);
		adapter.put(color);
		item.setVisible(visible);
		if (visibility.getString() != null && tracker != null) {
			try {
				final String formula = visibility.getString().substring(2);
				visibility.destroy();
				OMObject o = new FormuleParser(formula).logic();
				OMApplication oma = new OMApplication();
				oma.addElement(EUCLIDES_VISIBLE);
				oma.addElement(new OMVariable(tracker.getMapper().toString(item)));
				oma.addElement(o);
				Expression expr = new Expression(tracker);
				Destroyable v = expr.interpret(oma, visibility, tracker.getMapper());
				v.setVisible(false);
				tracker.getModel().add(v);
			
			} catch (Exception e) {
			}
		}
	}

	public void install() {
		install(item);
	}

	public UIModel<T, UIEditor> init(T item) {
		this.item = item;
		if(item != null) {
			color = item.getAdapter().adapt(Color.class);
			visible = item.isVisible();
		}
		if(color == null) color = Color.black;
		return this;
	}

	public Map<String, Object> toMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("color", color.getRGB());
		map.put("visible", visible);
		if(getVisibility() != null && !getVisibility().isEmpty())
		{
			map.put("visibility", getVisibility());
		}
		return map;
	}

	public void fromMap(ObjectMap map) {
		color = new Color( map.getInt("color"), true);
		visible = map.getBoolean("visible", true);
		visibility.setString(map.getString("visibility"));
	}

	public UIEditor editor() {
		return new ColorPane<ColorModel<?>>(this);
	}

	public String getVisibility() {
		return visibility.getString();
	}

	public UIModel<T, UIEditor> set(Tracker tracker) {
		this.tracker = tracker;
		return this;
	}

}
