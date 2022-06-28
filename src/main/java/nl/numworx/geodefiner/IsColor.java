package nl.numworx.geodefiner;

import java.awt.Color;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DefaultAdapter;

public class IsColor extends nl.numworx.geodefiner.common.math.IsColor {

	private Label label;
	private IsColor chain;
	
	public IsColor() {
		super();
	}

	private IsColor(Label label, IsColor chain) {
		super();
		this.label = label;
		this.chain = chain;
	}

	@Override
	protected
	Object getColorA(Destroyable a) {
		Color adapt = a.adapt(Color.class);
		if (adapt == null) adapt = Color.BLACK;
		return adapt;
	}

	@Override
	protected
	Object getColorB(Destroyable b) {
		if (b instanceof Label) {
			Label l = (Label) b;
			Numbers value = l.value;
			int n = value.intValue();
			if (n >= 0 && n < ColorHandler.colors.length) {
				return ColorHandler.colors[n];
			}
		}
		return getColorA(b);
	}

	@Override
	public boolean define(Label l) {
		DefaultAdapter adapter = DefaultAdapter.getDefault(l.getDepend()[0]);
		adapter.put(new IsColor(l, adapter.adapt(IsColor.class)));
		adapter = DefaultAdapter.getDefault(l.getDepend()[1]);
		adapter.put(new IsColor(l, adapter.adapt(IsColor.class)));
		return super.define(l);
	}

	public void updateColor() {
		test(label);
		if (chain != null) chain.updateColor();
	}

	
}
