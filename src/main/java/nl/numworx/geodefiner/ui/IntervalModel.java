package nl.numworx.geodefiner.ui;

import java.util.Map;

import nl.numworx.geodefiner.common.Align;
import nl.numworx.geodefiner.common.Animate;
import nl.numworx.geodefiner.common.Animator;
import nl.numworx.geodefiner.common.UIModel;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.model.HorizontalPunt;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Segment;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DefaultAdapter;

public class IntervalModel extends TextModel {
	Animate animate = Animate.NONE;
	double length = 50;
	int interval = 2000;
	Double step;
	
	@Override
	public void install(Label item) {
		DefaultAdapter adapter = DefaultAdapter.getDefault(item);
		Animator instance = adapter.adapt(Animator.class);
		if (instance != null) instance.install(null);
		if (animate == Animate.NONE) {
			adapter.put(Animator.class, null);
		} else {
			instance = new Animator(animate, interval);
			adapter.put(instance);
			instance.install(item);
		}
		if (step == null)
			adapter.put(Numbers.class, null);
		else
			adapter.put(Numbers.class, Numbers.createDouble(step.doubleValue()));
		
		DefaultAdapter.getDefault(item.getP()).put(color);
		DefaultAdapter.getDefault(item.getP().getDepend()[0]).put(color);
		HorizontalPunt hp = (HorizontalPunt) ((Segment) item.getP().getDepend()[0]).getP2();
		hp.setDistance(Numbers.createDouble(length));
		super.install(item);
	}

	@Override
	public UIModel<Label, UIEditor> init(Label item) {
		Animator animator = item.adapt(Animator.class);
		if(animator != null) {
			animate = animator.animate;
			interval = animator.interval;
		}
		Numbers step = item.adapt(Numbers.class);
		if(step != null) {
			this.step = step.doubleValue();
		} else
			this.step = null;
		
		Segment s = (Segment) item.getP().getDepend()[0];
		length = s.getDX();
		return super.init(item);
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		map.put("animate", animate.name());
		map.put("interval", interval);
		map.put("length", length);
		if(step!=null) map.put("step", step); else map.remove("step"); 
		return map;
	}

	@Override
	public void fromMap(ObjectMap map) {
		if (map.containsKey("animate"))
			animate = Animate.valueOf(map.getString("animate"));
		else
			animate = Animate.NONE;
		if (map.containsKey("interval"))
			interval = map.getInt("interval");
		else
			interval = 2000;
		if (map.containsKey("length"))
			length = map.getDouble("length");
		else
			length = 50.0;
		if (map.containsKey("step"))
			step = map.getDouble("step");
		else
			step = null;
		super.fromMap(map);
	}

	@Override
	public UIEditor editor() {
		length = ((Segment) item.getP().getDepend()[0]).getDX();
		return new IntervalPane(this);
	}

	
}
