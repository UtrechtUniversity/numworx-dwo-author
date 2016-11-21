package nl.numworx.geodefiner.common;

import java.util.List;
import java.util.Vector;

import nl.numworx.geodefiner.common.CELL;
import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMBinding;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMSymbol;
import nl.tue.win.riaca.openmath.lang.OMVariable;
import fi.euclides.event.Tracker;
import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.formuleobjects.ParseException;
import fi.euclides.model.Coordinaten;
import fi.euclides.model.Destroyable;
import fi.euclides.model.HorizontalPunt;
import fi.euclides.model.Label;
import fi.euclides.model.Locus;
import fi.euclides.model.Locus.LocusModel;
import fi.euclides.model.Model;
import fi.euclides.model.OpObject;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.Segment;
import fi.euclides.model.Triangle;
import fi.euclides.model.math.Numbers;
import fi.euclides.openmath.Expression;
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
@SuppressWarnings("serial")
public class Definitions implements Observer /*, ListModel*/ {

	protected final List<CELL> delegate = new Vector<CELL>();
	final private Tracker viewer;

	public Definitions(Tracker viewer) {
		this.viewer = viewer;
		expression = new Expression(viewer);
		new Interval().setTracker(viewer);
	}
	static final OMSymbol POINT = new OMSymbol("geodefiner", "point");
	static final OMSymbol LINE  = new OMSymbol("geodefiner" , "line");
	static final OMSymbol CIRCLE = new OMSymbol("geodefiner", "circle");
	static final OMSymbol ARC    = new OMSymbol("geodefiner", "arc");
	static final OMSymbol SEGMENT = new OMSymbol("geodefiner", "segment");
	static final OMSymbol CURVE   = new OMSymbol("geodefiner", "curve");
	static final OMSymbol POLYGON = new OMSymbol("geodefiner","polygon");
	static final OMSymbol TEXT   = new OMSymbol("geodefiner", "text");
	static final OMSymbol INTERVAL = new OMSymbol("interval1","interval");
	private final Expression expression;
		
	public void addElement(CELL element) {
		element.item.addObserver(this);
		delegate.add(element);
	}

	public void define(String text,OMObject object) {

// Interpreter of GeoDefiner statements		
			
		if(object instanceof OMApplication) {
			OMApplication oma = (OMApplication) object;
			OMObject first = oma.firstElement();
			if( first.isSame(Popcorn.PROG1_ASSIGN))
			{
				OMVariable var = (OMVariable) oma.getElementAt(1);
// No reassignments, or delete?
// in geogebra reassignments
				Destroyable fs = viewer.getMapper().fromString(var.getName());
				if (fs != null) {
					destroy(fs);
					fs.destroy(); 
				} else {
					int cell = findCell(var.getName());
					if(cell >= 0) {
						remove(cell);
					}
				}
				
				if (oma.getElementAt(2) instanceof OMApplication)
				{ 	oma = (OMApplication) oma.getElementAt(2);
					OMObject f = oma.firstElement();
					Destroyable[] depend = new Destroyable[oma.getLength()-1];
					expression.copy(oma, viewer.getMapper(), depend);
				if(POINT.isSame(f)) {
// $P := point(1,2)
					Label ix = (Label) depend[0]; // toNumber(object)
					Label iy = (Label) depend[1];
					Punt p = viewer.getModel().buildCoordinaten(ix, iy);
					if(depend.length == 3 && depend[2] instanceof OpObject) {
// $P := point(1, 2, $lijn)
						Destroyable on = depend[2];
						OpObject op = (OpObject) on;
						p.destroy();
						p = op.pointOn(p.getX(), p.getY());
						viewer.getModel().add(p);
					}
					viewer.getMapper().rename(p, var.getName());
					addElement(new CELL(text, p));
					return;
				}
// $l := line($P, $Q)
				if (LINE.isSame(f)) {
					Destroyable l = viewer.getModel().buildLijn(depend);
					viewer.getMapper().rename(l, var.getName());
					addElement(new CELL(text, l));
					return;
				}
// $l := segment($P, $Q)
				if (SEGMENT.isSame(f)) {
					Destroyable l = viewer.getModel().buildSegment(depend);
					viewer.getMapper().rename(l, var.getName());
					addElement(new CELL(text, l));
					return;
				}
// $l := arc($P, ... )
				if (ARC.isSame(f)) {
					Destroyable l = viewer.getModel().buildBoog(depend);
					viewer.getMapper().rename(l, var.getName());
					addElement(new CELL(text, l));
					return;
				}				
// $l := circle($P, $Q)
				if (CIRCLE.isSame(f)) {
					Destroyable l = viewer.getModel().buildCirkel(depend);
					viewer.getMapper().rename(l, var.getName());
					addElement(new CELL(text, l));
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
					t.setP(p);
					viewer.getMapper().rename(t, var.getName());
					viewer.getModel().add(t);
					addElement(new CELL(text, t));
					return;
				}
// $l := polygon($P, ...)
				if (POLYGON.isSame(f)) {
					Triangle t3 = viewer.getModel().buildTriangle(depend);
					addElement(new CELL(text, t3));
					viewer.getMapper().rename(t3, var.getName());
					return;				
				}
// $l := interval1.interval($a,$b)
				if (INTERVAL.isSame(f)) {
					LabelDelegate ld = viewer.getRegistered("..");
					Label l = ld.define(depend);
					Model m = viewer.getModel();
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
					viewer.getMapper().rename(l, var.getName());
					m.add(l);
					addElement(new CELL(text, l));
					return;
				}
// $c := curve( $f, $f )
// $w := 1+2
				}
// $f := lambda[[$x] ->	$f($x) ]
// $a := 1
				{
					Label l = new Label();l.setString(text);
					l.setVisible(false);	
					Destroyable f = expression.interpret(oma, l, viewer.getMapper());
					viewer.getMapper().rename(f, var.getName());
					viewer.getModel().add(f);
// display function
					if(f instanceof Label && ((Label) f).getSubKey().equals(Lambda.TYPE))
					{    LocusModel lm = new LocusModelF((Label)f, viewer);
					     Locus locus = new Locus(lm);
					     viewer.getMapper().rename(locus, "y="+var.getName()+"(x)");
					     viewer.getModel().add(locus);
					     f = locus;
					}
					addElement(new CELL(text, f));
					return;
				}
			} else if ( first.isSame(OMConstants.RELATION1_EQ)) {
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
						viewer.getModel().add(fx);
						LocusModel lm = new LocusModelF(fx, viewer);
					    Locus locus = new Locus(lm);
					    viewer.getMapper().rename(locus, text);
					    viewer.getModel().add(locus);
						addElement(new CELL(text, locus));
					} else if("x".equals(var.getName())) {
						Label fy = new Label();
						fy.setString(text);
						OMBinding lambda= new OMBinding(FormuleParser.FNS1_LAMBDA, new Vector(), oma.getElementAt(2));
						lambda.addVariable(new OMVariable("y"));
						fy.register(
						viewer.getRegistered(Lambda.TYPE));
						DefaultAdapter.getDefault(fy).put(OMObject.class, lambda);
						viewer.getModel().add(fy);
						LocusModel lm = new LocusModelXY(null, fy, null, viewer);
					    Locus locus = new Locus(lm);
					    viewer.getMapper().rename(locus, text);
					    viewer.getModel().add(locus);
						addElement(new CELL(text, locus));
					}
				}
			}
		}
	}

	private int findCell(String name) {
		String text = "$f" + name + "=";
		for(int i = 0; i < getSize(); i++ )
			if( getElementAt(i).text.startsWith(text))
			 	return i;	
		return -1;
	}

	private void destroy(Object fs) {
		for(int i = 0; i < getSize(); i++ )
			if( getElementAt(i).item == fs)
			{ 	remove(i); break;
			}
	}

	private void unlink(Observable fs) {
		CELL cell = fs.adapt(CELL.class);
		if(cell != null) {
			cell.item = null;
			cell.config = null;
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
