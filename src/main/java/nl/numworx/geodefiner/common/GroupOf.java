package nl.numworx.geodefiner.common;

import nl.tue.win.riaca.openmath.lang.OMBinding;
import nl.tue.win.riaca.openmath.lang.OMObject;
import fi.euclides.event.Tracker;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Groep;
import fi.euclides.model.Label;
import fi.euclides.openmath.Expression;


public class GroupOf extends Groep {
	
	private Tracker viewer;
	private Expression expression;
	private OMBinding  formula;
	private Strategy   strategy;
	
	abstract class Strategy { 
		abstract void recalc();
	}
	
	class ListStrategy extends Strategy {

		private Label list;
		public ListStrategy(Label l) {
			list = l;
		}

		@Override
		void recalc() {
			Destroyable[] depend = list.getDepend();
			for (Destroyable destroyable : depend) {
				
			}
			
		}
		
	}
	

	public GroupOf(Destroyable[] depend, Expression expression, Tracker viewer) {
		super(depend);
		this.viewer = viewer;
		this.expression = expression;
		Label l = (Label) depend[0];
		OMObject obj = l.adapt(OMObject.class);
		if(obj instanceof OMBinding) {
			formula = (OMBinding) obj;
		} else {
			formula = createBinding(obj);
		}
		l = (Label) depend[1];
		strategy = new ListStrategy(l);
		
		recalc();
	}

	private OMBinding createBinding(OMObject obj) {
		// TODO Auto-generated method stub
		return null;
	}

	private void recalc() {
		// TODO Auto-generated method stub
		
	}

}
