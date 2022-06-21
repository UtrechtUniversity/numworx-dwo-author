package nl.numworx.geodefiner;

import java.awt.BasicStroke;
import java.awt.Stroke;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import nl.numworx.geodefiner.common.LineType;

public class IsLineType extends nl.numworx.geodefiner.common.math.IsLineType {

	private IsLineType chain;
	private Label label;
	
	public IsLineType() {
	}
	
	public IsLineType(Label label, IsLineType chain) {
		this.label = label;
		this.chain = chain;
	}

	@Override
	protected LineType getLineTypeA(Destroyable a) {
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
	
}
