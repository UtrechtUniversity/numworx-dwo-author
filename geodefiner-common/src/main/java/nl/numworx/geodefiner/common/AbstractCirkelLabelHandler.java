package nl.numworx.geodefiner.common;

import java.util.Vector;

import fi.euclides.event.TrackerContext;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Model;
import fi.euclides.model.OpObject;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.Const;
import fi.euclides.proof.LabelDelegate;

public abstract class AbstractCirkelLabelHandler extends AbstractTextHandler {

	public AbstractCirkelLabelHandler(String string) {
		super(string);
		testLijn=true;
	}
	
	protected void build(Punt p, Numbers r) {
		Label l = new Label();
		l.setString(Numbers.toString(r));
		l.setValue(r);
		LabelDelegate CONST = getTracker().getRegistered(Const.TYPE);
		CONST.define(l);
		Destroyable[] depend = { p, l };
		visit(getModel().buildCirkel(depend));
	}

	@Override
	public void pointerReleased(Numbers x, Numbers y, TrackerContext context) {
		pointerDragged(x,y,context);
		context.setTrack(null);
		Model model = getModel();
		Vector<Destroyable> select = model.getSelect();		
		if (select.isEmpty() 
			|| select.firstElement() instanceof OpObject
		) {
			Destroyable p = model.buildPunt(x, y);
			p = visit(p);
			model.toggle(p);
		} 
		attachSelection();
	}


}
