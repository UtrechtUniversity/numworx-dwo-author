package nl.numworx.geodefiner;

import java.awt.BasicStroke;
import java.awt.Stroke;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;
import nl.numworx.geodefiner.common.LineType;

public class IsLineType extends nl.numworx.geodefiner.common.math.IsLineType implements Observer {

	private IsLineType chain;
	private Label label;
	
	public IsLineType() {
	}
	
	public IsLineType(Label label, IsLineType chain) {
		this.label = label;
		this.chain = chain;
	}

	@Override
	public LineType getLineTypeA(Destroyable a) {
		Stroke stroke = a.adapt(Stroke.class);
		if (stroke == null) return LineType.SOLID;
		if (stroke instanceof BasicStroke) {
			BasicStroke bs = (BasicStroke) stroke;
			float[] dashes = bs.getDashArray();
			if (dashes == null) return LineType.SOLID;
			if (dashes.length == 4) return LineType.DASHDOTTED;			
			float width = bs.getLineWidth();
			return width == dashes[0] ? LineType.DOTTED : LineType.DASHED;
		}
		return LineType.SOLID;
	}


	public void updateLineType() {
		test(label);
		if (chain != null) chain.updateLineType();
	}

	@Override
	public boolean define(Label l) {
//		Destroyable[] depend = l.getDepend();
//		DefaultAdapter adapter = DefaultAdapter.getDefault(depend[0]);
//		adapter.put(new IsLineType(l, adapter.adapt(IsLineType.class)));
//		adapter = DefaultAdapter.getDefault(depend[1]);
//		adapter.put(new IsLineType(l, adapter.adapt(IsLineType.class)));
//		depend[0].addObserver(this);
//		depend[1].addObserver(this);
		return super.define(l);
	}

	
}
