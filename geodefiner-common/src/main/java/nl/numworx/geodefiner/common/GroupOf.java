package nl.numworx.geodefiner.common;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import nl.numworx.geodefiner.common.index.LabelSelector;
import nl.numworx.geodefiner.common.index.LabelSelectorIntern;
import nl.numworx.geodefiner.common.index.ListSelector;
import nl.numworx.geodefiner.common.index.ListSelectorIntern;
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
import fi.euclides.openmath.LocusModelF;
import fi.euclides.util.Adapter;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Observable;


public class GroupOf extends Groep {
	
	private Expression expression;
	private OMBinding  formula;
	private Strategy   strategy;
	private List<Destroyable> vars;
	private NameMapper mapper;
	
	abstract class Strategy implements NameMapper { 
		abstract void recalc();
		
		Strategy() {} 
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
			this.interval = interval;
			Destroyable[] depend = interval.getDepend();
			vars.addAll(Arrays.asList(depend));
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
			int size = grp.size();
			OMObject oma = formula.getBody();
			name = ((OMVariable) formula.getVariableAt(0)).getName();
			for(int i = 1; i <= size; i++)
			{
				argument = new ListSelectorIntern(grp, i).get();
				Destroyable v = expression.interpret(oma, new Label(), this);
				addElement(v);
				setChanged();
			}
		}

		GroupStrategy(Groep l) {
			this.grp = l;
		}
		
	}
	
	class ListStrategy extends Strategy implements NameMapper {

		private Label list;
		public ListStrategy(Label l) {
			list = l;
		}
		
		@Override
		void recalc() {
			clear();
			Destroyable[] depend = list.getDepend();
			int size = depend.length;
			OMObject oma = formula.getBody();
			name = ((OMVariable) formula.getVariableAt(0)).getName();
			for (int i = 1; i <= size; i++) {
				argument = new LabelSelectorIntern(list, i).get();
				Destroyable v = expression.interpret(oma, new Label(), this);
				addElement(v);
				setChanged();
			}
		}

	}
	
	public GroupOf(Destroyable[] depend, Expression expression, Tracker viewer) {
		this(depend, expression, viewer.getMapper());
	}

	public GroupOf(Destroyable[] depend, Expression expression,
			NameMapper mapper) {
		super(depend);
		this.mapper = mapper;
		this.expression = expression;
		Label l = (Label) depend[0];
		OMObject obj = l.adapt(OMObject.class);
		if(obj instanceof OMBinding) {
			formula = (OMBinding) obj;
		} else {
			formula = createBinding(obj);
		}
		vars = LocusModelF.varsOf(formula, mapper);
		if ( depend[1] instanceof Groep) {
			strategy = new GroupStrategy((Groep)depend[1]);
		} else {
		
			l = (Label) depend[1];
			if(l.getRegistered() instanceof Interval) 
				strategy = new IntervalStrategy(l);
			else
				strategy = new ListStrategy(l);
		}
		for (Destroyable destroyable : vars) {
			destroyable.addObserver(this);
		}
		recalc();
	}

	@Override
	public void update(Observable observable, Object arg) {
		super.update(observable, arg);
		if(arg == null)
			recalc();
	}
	

	@Override
	public void destroy() {
		for(Destroyable d: vars) d.deleteObserver(this);
		super.destroy();
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

	@Override
	public void setAdapter(Adapter adapter) {
		super.setAdapter(adapter);
		Enumeration<Destroyable> items = elements();
		while (items.hasMoreElements()) {
			Destroyable destroyable = (Destroyable) items.nextElement();
			destroyable.setAdapter(adapter);
		}
	}

}
