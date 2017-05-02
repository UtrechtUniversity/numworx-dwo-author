package nl.numworx.geodefiner.common;

import java.util.Enumeration;

import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMBinding;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMVariable;
import fi.euclides.event.NameMapper;
import fi.euclides.event.Tracker;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Groep;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import fi.euclides.openmath.Expression;
import fi.euclides.util.Adapter;
import fi.euclides.util.DefaultAdapter;


public class GroupOf extends Groep {
	
	private Tracker viewer;
	private Expression expression;
	private OMBinding  formula;
	private Strategy   strategy;
	
	abstract static class Strategy implements NameMapper { 
		abstract void recalc();
		
		Strategy() {} 
		Strategy(NameMapper mapper) {this.mapper = mapper; }
		NameMapper mapper;
		String name;
		Destroyable argument;
	
		@Override
		public Destroyable fromString(String name) {
			if(this.name.equals(name)) return argument;
			return mapper.fromString(name);
		}

		@Override
		public Punt getO() {
			return mapper.getO();
		}

		@Override
		public Punt getU() {
			return mapper.getU();
		}

		@Override
		public String toString(Destroyable destroyable) {
			if(destroyable == argument) return name;
			return mapper.toString(destroyable);
		}

		@Override
		public void rename(Destroyable p, String name) {
		}
		
		
	}
	
	class IntervalStrategy extends Strategy {
		Label interval;

		IntervalStrategy(Label interval) {
			super(viewer.getMapper());
			this.interval = interval;
		}


		@Override
		void recalc() {
			clear();
			Destroyable[] depend = interval.getDepend();
			long min = Math.round( ((Label)depend[0]).value.doubleValue() );
			long max = Math.round( ((Label)depend[1]).value.doubleValue() );
			if(max < min) { 
				long tmp = max; max = min; min = tmp;
			}
			OMObject oma = formula.getBody();
			name = ((OMVariable) formula.getVariableAt(0)).getName();
			for (long i = min; i <= max; i++) {
				Label l;
				argument = 	l = new Label();
				l.value = Numbers.createRational(i, 1);
				l.setString(Long.toString(i));
				Destroyable v = expression.interpret(oma, new Label(), this);
				addElement(v);
				setChanged();
			}
		}
		
	}
	
	class GroupStrategy extends Strategy {
		private Groep grp;

		@Override
		void recalc() {
			clear();
			Enumeration<Destroyable> depend = grp.elements();
			OMObject oma = formula.getBody();
			name = ((OMVariable) formula.getVariableAt(0)).getName();
			while(depend.hasMoreElements()) {
			Destroyable destroyable = depend.nextElement();
				argument = destroyable;
				Destroyable v = expression.interpret(oma, new Label(), this);
				addElement(v);
				setChanged();
			}
		}

		GroupStrategy(Groep l) {
			super(viewer.getMapper());
			this.grp = l;
		}
		
	}
	
	class ListStrategy extends Strategy implements NameMapper {

		private Label list;
		public ListStrategy(Label l) {
			super(viewer.getMapper());
			list = l;
		}
		
		@Override
		void recalc() {
			clear();
			Destroyable[] depend = list.getDepend();
			OMObject oma = formula.getBody();
			name = ((OMVariable) formula.getVariableAt(0)).getName();
			for (Destroyable destroyable : depend) {
				argument = destroyable;
				Destroyable v = expression.interpret(oma, new Label(), this);
				addElement(v);
				setChanged();
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
		if ( depend[1] instanceof Groep) {
			strategy = new GroupStrategy((Groep)depend[1]);
		} else {
		
			l = (Label) depend[1];
			if(l.getRegistered() instanceof Interval) 
				strategy = new IntervalStrategy(l);
			else
				strategy = new ListStrategy(l);
		}
		recalc();
	}

	private OMBinding createBinding(OMObject obj) {
		// TODO Auto-generated method stub
		return null;
	}

	private void recalc() {
		strategy.recalc();
		Adapter adapter = DefaultAdapter.getDefault(this);
		Enumeration<Destroyable> items = elements();
		while (items.hasMoreElements()) {
			Destroyable destroyable = (Destroyable) items.nextElement();
			destroyable.setAdapter(adapter);
		}
		notifyObservers();
	}

}
