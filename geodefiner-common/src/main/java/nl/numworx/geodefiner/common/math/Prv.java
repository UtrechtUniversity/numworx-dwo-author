package nl.numworx.geodefiner.common.math;

import java.util.List;

import fi.euclides.event.NameMapper;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.openmath.Expression;
import fi.euclides.openmath.LocusModelF;
import fi.euclides.openmath.OMConstants;
import fi.euclides.proof.LabelValue;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;
import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMVariable;

public class Prv extends LabelValue {

  private final class PrvMapper implements NameMapper, Observer {
    private final Label b;
    private final Label a;
    private final Label f;
    private final Label l;
    private List<Destroyable> output;

    private PrvMapper(Label l, Label b, Label a, Label f) {
      this.l = l;
      this.b = b;
      this.a = a;
      this.f = f;
      this.output = LocusModelF.varsOf(f);
      output.add(a);
      output.add(b);
      for(Destroyable e: output) e.addObserver(this);
    }

    private void destroy() {
      for (Destroyable e: output) e.deleteObserver(this);
    }
    
    @Override
    public Destroyable fromString(String name) {
      if (_A.getName().equals(name)) return a;
      if (_B.getName().equals(name)) return b;
      if (_F.getName().equals(name)) return f;
      return getTracker().getMapper().fromString(name);
    }

    @Override
    public Punt getO() {
      return getTracker().getMapper().getO();
    }

    @Override
    public Punt getU() {
      return getTracker().getMapper().getU();
    }

    @Override
    public String toString(Destroyable destroyable) {
      return getTracker().getMapper().toString(destroyable);
    }

    @Override
    public void rename(Destroyable p, String name) {
    }

    @Override
    public void update(Observable observable, Object arg) {
      Prv.this.update(l, arg); 
    }
  }

  private static final OMVariable _A = new OMVariable("%a");
  private static final OMVariable _B = new OMVariable("%b");
  private static final OMVariable _F = new OMVariable("%f");
  private static final OMApplication FORMULA = new OMApplication();
  static {
    FORMULA.addElement(OMConstants.ARITH1_MINUS);
    OMApplication Fb = new OMApplication(), Fa = new OMApplication();
    Fb.addElement(_F); Fb.addElement(_B);
    Fa.addElement(_F); Fa.addElement(_A);
    FORMULA.addElement(Fb);
    FORMULA.addElement(Fa);

  }
  
  Prv() {
	super("prv");
  }

	@Override
	public String getSymbolicValue(Label l) {
		Destroyable[] depend = l.getDepend();
		return "prv(" + s(depend[0]) +"," + s(depend[1]) + ")";
	}

	@Override
	public Destroyable[] createDepend() {
		return new Label[2];
	}

	
  @Override
  public boolean define(Label l) {
    Label[] depend = (Label[]) l.getDepend();
    Label[] interval = (Label[]) depend[0].getDepend();
    final Label a = interval[0];
    final Label b = interval[1];
    final Label f = depend[1];
    NameMapper mapper = new PrvMapper(l, b, a, f);
    DefaultAdapter.getDefault(l).put(mapper);
    return super.define(l);
  }

  @Override
  public void update(Observable observable, Object arg) {
    if (arg == null) {
      Label l = (Label) observable;   
      Expression expression = getTracker().adapt(Expression.class);
      PrvMapper mapper = l.adapt(PrvMapper.class);
      Label result = (Label) expression.interpret(FORMULA, new Label(), mapper);
      setStringValue(l, result.value);
    } else if (arg == Destroyable.DESTROY) {
      Label l = (Label) observable;   
      PrvMapper mapper = l.adapt(PrvMapper.class);
      mapper.destroy();
    }
  }

}
