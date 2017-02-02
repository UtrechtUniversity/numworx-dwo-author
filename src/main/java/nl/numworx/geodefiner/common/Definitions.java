package nl.numworx.geodefiner.common;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import nl.numworx.geodefiner.common.CELL;
import nl.numworx.geodefiner.common.math.Expression;
import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMBinding;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMSymbol;
import nl.tue.win.riaca.openmath.lang.OMVariable;
import nl.uu.fi.dwo.interaction.client.JSONUtilities;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.event.Tracker;
import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.formuleobjects.ParseException;
import fi.euclides.model.Destroyable;
import fi.euclides.model.HorizontalPunt;
import fi.euclides.model.Label;
import fi.euclides.model.Locus;
import fi.euclides.model.Locus.LocusModel;
import fi.euclides.model.Model;
import fi.euclides.model.OpObject;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.PuntOp2;
import fi.euclides.model.Segment;
import fi.euclides.model.Triangle;
import fi.euclides.model.VrijPunt;
import fi.euclides.model.math.Numbers;
import fi.euclides.expr.DestroyDependency;
import fi.euclides.expr.InterpretException;
import fi.euclides.expr.Lambda;
import fi.euclides.openmath.LocusModelF;
import fi.euclides.openmath.OMConstants;
import fi.euclides.openmath.Popcorn;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

/** FIXME split in a observable and a ListModel
 * 
 * @author wim
 *
 */
public class Definitions implements Observer /*, ListModel*/ {

	protected final List<CELL> delegate = new Vector<CELL>();
	final private Tracker viewer;
	public int readonly = 4;

	public Definitions(Tracker viewer) {
		this.viewer = viewer;
		expression = new Expression(viewer);
		Interval delegate = new Interval();
		delegate.setTracker(viewer);
		expression.put(INTERVAL, delegate);
	}
	static final OMSymbol POINT = new OMSymbol("geodefiner", "point");
	static final OMSymbol LINE  = new OMSymbol("geodefiner" , "line");
	static final OMSymbol CIRCLE = new OMSymbol("geodefiner", "circle");
	static final OMSymbol ARC    = new OMSymbol("geodefiner", "arc");
	static final OMSymbol SEGMENT = new OMSymbol("geodefiner", "segment");
	static final OMSymbol CURVE   = new OMSymbol("geodefiner", "curve");
	static final OMSymbol POLYGON = new OMSymbol("geodefiner","polygon");
	static final OMSymbol TEXT   = new OMSymbol("geodefiner", "text");
	static final OMSymbol INTERVAL = OMConstants.INTERVAL1_INTERVAL;
	static final OMSymbol INT = new OMSymbol("calculus1", "int");
	static final OMSymbol DEFINT = new OMSymbol("calculus1", "defint");
	
	private final Expression expression;
		
	public void addElement(CELL element) {
		if(element.item != null)
			element.item.addObserver(this);
		delegate.add(element);
	}

	private void installConfig(CELL cell, Map<String, ?> config) {
		installConfig(cell, config, cell.var);
	}
	
	public void define(String text,OMObject object) {

// Interpreter of GeoDefiner statements		
			
		if(object instanceof OMApplication) {
			OMApplication oma = (OMApplication) object;
			OMObject first = oma.firstElement();
			Model model = viewer.getModel();
			Map<String,?> config = null;
			if( first.isSame(OMConstants.PROG1_ASSIGN))
			{
				OMVariable var = (OMVariable) oma.getElementAt(1);
// No reassignments, or delete?
// in geogebra reassignments
				int cell = findCell(var.getName());
				Destroyable fs = viewer.getMapper().fromString(var.getName());
				if (fs != null) {
					if(fs.getIndex() <= readonly)
						throw new RuntimeException("readonly");
					if(cell >= 0) {
						UIModel<?, ?> cellConfig = getElementAt(cell).config;
						if(cellConfig != null) config = cellConfig.toMap();
					}
					destroy(fs, var.getName());
					fs.destroy(); 
				} else {
					if(cell >= 0) {
						UIModel<?, ?> cellConfig = getElementAt(cell).config;
						if(cellConfig != null) config = cellConfig.toMap();
						remove(cell);
					}
				}
				
				if (oma.getElementAt(2) instanceof OMApplication)
				{ 	oma = (OMApplication) oma.getElementAt(2);
					OMObject f = oma.firstElement();
					Destroyable[] depend = new Destroyable[oma.getLength()-1];
					expression.copy(oma, viewer.getMapper(), depend);
				if(POINT.isSame(f)) {
					Punt p;
					if(depend.length != 3 && depend.length != 2) {
						throw new InterpretException("point: 2 of 3 parameters");
					}
					Destroyable arg0 = depend[0];
					Destroyable arg1 = depend[1];
					if(arg0 instanceof Label && arg1 instanceof Label) {
// $P := point(1,2)
						Label ix = (Label) arg0; // toNumber(object)
						Label iy = (Label) arg1;
						p = model.buildCoordinaten(ix, iy);
						if(depend.length == 3 && depend[2] instanceof OpObject) {
// $P := point(1, 2, $lijn)
							Destroyable on = depend[2];
							OpObject op = (OpObject) on;
							p.destroy();
							p = op.pointOn(p.getX(), p.getY());
							model.add(p);
						}
					} else {
						model.getSelect().clear();
						model.toggle(arg0);
						model.toggle(arg1);
						p = model.buildPunt(Numbers.ZERO, Numbers.ZERO);
						model.getSelect().clear();
						if ( p instanceof PuntOp2 && depend.length == 3)
						{	PuntOp2 p2 = (PuntOp2)p;
							Destroyable arg2 = depend[2];
							if(arg2 instanceof Label) {
								byte b = (byte) ((Label) arg2).value.doubleValue();
								p2.setFuse(b);
								p2.update(arg0, arg1);
							}
						}
						if (p instanceof VrijPunt) { 
							p.destroy();
							p = null;
						}
						if(p == null) {
							throw new InterpretException("wrong point");
						}
					}
					
					installConfig(new CELL(text, p, var), config);
					return;
				}
// $l := line($P, $Q)
				if (LINE.isSame(f)) {
					Destroyable l = model.buildLijn(depend);
					if(l == null) throw new InterpretException("line error");
					installConfig(new CELL(text, l, var), config);
					return;
				}
// $l := segment($P, $Q)
				if (SEGMENT.isSame(f)) {
					Destroyable l = model.buildSegment(depend);
					if(l == null) throw new InterpretException("segment error");
					installConfig(new CELL(text, l, var), config);
					return;
				}
// $l := arc($P, ... )
				if (ARC.isSame(f)) {
					Destroyable l = model.buildBoog(depend);
					if(l == null) throw new InterpretException("arc error");
					installConfig(new CELL(text, l, var), config);
					return;
				}				
// $l := circle($P, $Q)
				if (CIRCLE.isSame(f)) {
					Destroyable l = model.buildCirkel(depend);
					if(l == null) throw new InterpretException("circle error");
					installConfig(new CELL(text, l, var), config);
					return;
				}
// $t := text("text", $P)
				if(TEXT.isSame(f)) {
					Punt  p = (Punt) depend[1];
					Label t = (Label) depend[0]; // "text", ["x=",$x]  FIXME if label is defined make indirection
					if(t.getIndex() > 0) return; // FIXME
// "te{x}t" -> [ "te",x,"t" ]
					if( "".equals(t.getSubKey())) {
						String plain = t.getString();
						if(plain.contains("{") && plain.contains("}"))
						{
							plain = plain.replace("{", "\",").replace("}",",\"");
							FormuleParser parser = new FormuleParser("[\""+plain+"\"]");
							try {
								OMObject o = parser.bracket();
								depend[0] = expression.interpret(o, t, viewer.getMapper());
							} catch (ParseException e) {
								// log.fine(e.toString())
								;
							}				
						}
					}
					t.setP(new Volgpunt(p));
					model.add(t);
					installConfig(new CELL(text, t, var), config);
					return;
				}
// $l := polygon($P, ...)
				if (POLYGON.isSame(f)) {
					Triangle t3;
					if(depend.length > 3)
					{
						t3 = new Polygon(depend);
						model.add(t3);
					}
					else
						t3 = model.buildTriangle(depend);
					installConfig(new CELL(text, t3, var), config);
					return;				
				}
// $l := interval1.interval($a,$b)
				if (INTERVAL.isSame(f)) {
					LabelDelegate ld = viewer.getRegistered("..");
					Label l = ld.define(depend);
					Model m = model;
// place at random
					Punt x1 = m.buildPunt(Numbers.createInteger(25), Numbers.createInteger(50));
					Punt x2 = new HorizontalPunt(Numbers.createInteger(75), x1.getY(), x1);
					x1.setVisible(false);
					x2.setVisible(false);
					m.add(x2);
					Segment s = m.buildSegment(new Punt[] { x1, x2 } );
					PuntOp<?> x3 = s.pointOn(Numbers.createInteger(50), x1.getY());
					x3.setFree(true);
					m.add(x3);
					x3.addObserver(l);
					x1.addObserver(l);
					x2.addObserver(l);
					l.setP(x3);
// FIXED NAMES
					viewer.getMapper().rename(x1, var.getName() + "%min");
					viewer.getMapper().rename(x2, var.getName() + "%max");
					m.add(l);
					installConfig(new CELL(text, l, var), config);
					return;
				}
// $c := curve( $f, $f )
				if(CURVE.isSame(f)) {
					Label fx = (Label) depend[0];
					Label fy = (Label) depend[1];
					Label interval = depend.length > 2 ? (Label) depend[2]: null;				
					LocusModel lm = new LocusModelXY(fx, fy, interval, viewer);
					Locus locus = new Locus(lm);
					model.add(locus);
					installConfig(new CELL(text, locus, var), config);
					return;
				}
// $f := int(lambda[$x -> ... ])
				if(INT.isSame(f)) {
					Label fy = (Label) depend[0];
					fy.setString(text); // FIXME Why?
					LocusModel lm = new LocusModelF(fy, viewer);
				    Locus locus = new Integral(lm);
				    model.add(locus);
					installConfig(new CELL(text, locus, var), config);
					return;
				}
// $f := defint($a .. $b, lambda[$x -> ... ])
				if(DEFINT.isSame(f)) {
					Label fy = (Label) depend[1];
					fy.setString(text);
					Label interval = (Label) depend[0];
					Label fx = new Label();
					fx.setString("identity");
					fx.register(viewer.getRegistered(Lambda.TYPE));
					fx.setVisible(false);
					DefaultAdapter.getDefault(fx).put(OMObject.class, OMConstants.FNS1_IDENTITY);
					model.add(fx);// Why?
					LocusModel lm = new LocusModelXY(fx, fy, interval, viewer);
				    Locus locus = new Integral(lm);
				    model.add(locus);
					installConfig(new CELL(text, locus, var), config);
					return;
				} 
				destroy(depend);
// $w := 1+2
				}
// $f := lambda[[$x] ->	$f($x) ]
// $a := 1
				{
					Label l = new Label();l.setString(text);
					l.setVisible(false);
					l.setX(20);l.setY(30);
					Destroyable f = expression.interpret(oma, l, viewer.getMapper());
					if(f != l) {
						throw new InterpretException("Exists:" + viewer.getMapper().toString(f));
					}
					viewer.getMapper().rename(f, var.getName());
					model.add(f);
// display function
					if(f instanceof Label && isYFX((Label) f))
					{    LocusModel lm = new LocusModelF((Label)f, viewer);
					     Locus locus = new Locus(lm);
					     String name = "y="+var.getName()+"(x)";
						viewer.getMapper().rename(locus, name);
					     model.add(locus);
					     final Destroyable destroyable = f;
					     locus.addObserver(new DestroyDependency(destroyable));
					     f = locus;
					     CELL c = new CELL(text, f, var);
					     installConfig(c, config, name);
					} else
						installConfig(new CELL(text, f, var), config);
					return;
				}
			} else if ( first.isSame(OMConstants.RELATION1_EQ)) {
				int found = findCell(text);
				if(found >= 0) {
					// ????? assert(getElementAt(found).item != null)
					return; // duplicate				
				}
					// $x = 1;
// $y = $x + 1;
				OMObject arg = oma.getElementAt(1);
				if(arg instanceof OMVariable) {
					OMVariable var = (OMVariable) arg;
					if("y".equals(var.getName())) {
						Label fx = new Label();
						fx.setString(text);
						fx.setVisible(false);
						OMBinding lambda= new OMBinding(FormuleParser.FNS1_LAMBDA, new Vector(), oma.getElementAt(2));
						lambda.addVariable(new OMVariable("x"));
						fx.register(
						viewer.getRegistered(Lambda.TYPE));
						DefaultAdapter.getDefault(fx).put(OMObject.class, lambda);
						model.add(fx);
						LocusModel lm = new LocusModelF(fx, viewer);
					    Locus locus = new Locus(lm);
					    viewer.getMapper().rename(locus, text);
					    model.add(locus);
						installConfig(new CELL(text, locus, text), config);
					} else if("x".equals(var.getName())) {
						Label fx = new Label();fx.setVisible(false);
						fx.setString("identity");
						fx.register(viewer.getRegistered(Lambda.TYPE));
						DefaultAdapter.getDefault(fx).put(OMObject.class, OMConstants.FNS1_IDENTITY);
						model.add(fx);
						Label fy = new Label();
						fy.setString(text);
						fy.setVisible(false);
						OMBinding lambda= new OMBinding(FormuleParser.FNS1_LAMBDA, new Vector(), oma.getElementAt(2));
						lambda.addVariable(new OMVariable("y"));
						fy.register(viewer.getRegistered(Lambda.TYPE));
						DefaultAdapter.getDefault(fy).put(OMObject.class, lambda);
						model.add(fy);
						LocusModel lm = new LocusModelXY(fy, fx, null, viewer);
					    Locus locus = new Locus(lm);
					    viewer.getMapper().rename(locus, text);
					    model.add(locus);
						installConfig(new CELL(text, locus, text), config);
					} else {
						throw new InterpretException("syntax error");
					}
				}
			}
		}
	}

	private void destroy(Destroyable[] depend) {
		for (int i = 0; i < depend.length; i++) {
			Destroyable destroyable = depend[i];
			if (destroyable.getIndex() == 0) // only anonymous objects
				destroyable.destroy();
		}	
	}

	protected void installConfig(CELL c, Map<String, ?> config, String name) {
		Destroyable p = c.item;
		viewer.getMapper().rename(p, name);
		addElement(c);
	}

	protected boolean isYFX(Label f) {
		boolean typeOk = f.getSubKey().equals(Lambda.TYPE);
		if(typeOk) {
			OMObject obj = f.adapt(OMObject.class);
			if (obj instanceof OMBinding) {
				obj = ((OMBinding) obj).getBody();
			}
			if(obj instanceof OMApplication) {
				obj = ((OMApplication) obj).firstElement();
			}
			// symbol, variable, float int.
		}
		return typeOk;
	}

	private int findCell(String name) {
		for(int i = 0; i < getSize(); i++ )
		{	
			String t = getElementAt(i).var;
			if( t.equals(name))
			{
			 		return i;	
			}
		}
		return -1;
	}

	private void destroy(Object fs, String name) {
		for(int i = 0; i < getSize(); i++ )
			if( getElementAt(i).item == fs)
			{ 	remove(i);break;
			} else if( getElementAt(i).var.equals(name))
			{
				remove(i);break;
			}
	}

	private void unlink(Observable fs) {
		CELL cell = fs.adapt(CELL.class);
		if(cell != null) {
			cell.item = null;
			if(cell.config != null) {
				cell.config.init(null);
			}
			update(cell);
		}
	}
	
	protected void remove(int i) {
		delegate.remove(i);
	}

	public void update(Observable observable, Object arg) {
		if(arg == Destroyable.DESTROY) {
			observable.deleteObserver(this);
			unlink(observable);
		}	
	}

	@Deprecated
	public void update(CELL cell) {
		
	}

	public void clear() {
		delegate.clear();
	}
	
	public int getSize() {
		return delegate.size();
	}

	public CELL getElementAt(int index) {
		return delegate.get(index);
	}
	
	public int indexOf(CELL c) {
		return delegate.indexOf(c);
	}
}
