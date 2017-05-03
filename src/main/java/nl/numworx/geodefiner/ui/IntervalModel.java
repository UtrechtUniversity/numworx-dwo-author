package nl.numworx.geodefiner.ui;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.util.Map;

import nl.numworx.geodefiner.common.Align;
import nl.numworx.geodefiner.common.Animate;
import nl.numworx.geodefiner.common.Animator;
import nl.numworx.geodefiner.common.StepValue;
import nl.numworx.geodefiner.common.UIModel;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.model.Destroyable;
import fi.euclides.model.HorizontalPunt;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DefaultAdapter;

public class IntervalModel extends TextModel {
	Animate animate = Animate.NONE;
	double length = 50, x, y;
	int interval = 2000;
	Number step;
	Float  width = Float.valueOf(1f);
	
	public IntervalModel() {
		super();
		align = Align.TOP;
	}

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
		DefaultAdapter adapterP = DefaultAdapter.getDefault(item.getP());
		if (step == null || step.doubleValue() == 0.0)
		{
			adapter.put(StepValue.class, null);
			adapterP.put(Label.class, null);
		}
		else
		{	Label min = (Label) item.getDepend()[0];
			adapter.put(StepValue.class, new StepValue(Numbers.createDouble(step.doubleValue()),min));
			adapterP.put(Label.class, item);
		}
		
		adapterP.put(color);
		Destroyable segment = item.getP().getDepend()[0];
		DefaultAdapter.getDefault(segment).put(color);
		if(width != null) {
			DefaultAdapter.getDefault(segment).put(Stroke.class, new BasicStroke(width.floatValue()));
			adapterP.put(Float.class, 5.0f * width.floatValue());
		} else {
			DefaultAdapter.getDefault(segment).put(Stroke.class, null);
			adapterP.put(Float.class, null);
		}
		HorizontalPunt hp = (HorizontalPunt) ((Segment) segment).getP2();
		Punt h0 = ((Segment) segment).getP1();
		h0.setXY(x, y);
		hp.setDistance(Numbers.createDouble(length));
		super.install(item);
	}

	@Override
	public UIModel<Label, UIEditor> init(Label item) {
		if(item == null) {
			if(this.item != null && this.item.getP() instanceof fi.euclides.model.PuntOp ) {
				Segment s = (Segment) this.item.getP().getDepend()[0];
				length = s.getDX();
				x = s.getX1(); 
				y = s.getY1();
			}
			this.item = null;
			return this; // skip iff null
		}
		Animator animator = item.adapt(Animator.class);
		if(animator != null) {
			animate = animator.animate;
			interval = animator.interval;
		}
		StepValue step = item.adapt(StepValue.class);
		if(step != null) {
			this.step = step.doubleValue();
		} else
			this.step = null;
		
		Segment s = (Segment) item.getP().getDepend()[0];
		length = s.getDX();
		x = s.getX1(); 
		y = s.getY1();
		return super.init(item);
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		map.put("animate", animate.name());
		map.put("interval", interval);
		map.put("length", length);
		if(step!=null) map.put("step", step.doubleValue()); else map.remove("step");
		if(width!= null) map.put("width", width.doubleValue());
		if(item != null) {
			Segment s = (Segment) item.getP().getDepend()[0];
			map.put("x", s.getX1());
			map.put("y", s.getY1());
		} else {
			if (x != 0) map.put("x", x);
			if (y != 0) map.put("y", y);
		}
		
		
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
		if (map.containsKey("width"))
			width = new Float(map.getDouble("width"));
		if(map.containsKey("x")) {
			x = map.getDouble("x");
		} else x = 0;
		if(map.containsKey("y")) {
			y = map.getDouble("y");
		} else y = 0;
		super.fromMap(map);
	}

	@Override
	public UIEditor editor() {
		Segment segment = (Segment) item.getP().getDepend()[0];
		length = segment.getDX();
		x = segment.getX1();
		y = segment.getY1();
		return new IntervalPane(this);
	}

	
}
