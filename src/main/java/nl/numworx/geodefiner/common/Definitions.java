package nl.numworx.geodefiner.common;

import java.util.List;
import java.util.Vector;

import nl.numworx.geodefiner.common.CELL;
import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMSymbol;
import nl.tue.win.riaca.openmath.lang.OMVariable;
import fi.euclides.event.Tracker;
import fi.euclides.model.Coordinaten;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Locus;
import fi.euclides.model.Locus.LocusModel;
import fi.euclides.model.OpObject;
import fi.euclides.model.Punt;
import fi.euclides.model.Triangle;
import fi.euclides.openmath.Expression;
import fi.euclides.openmath.Lambda;
import fi.euclides.openmath.LocusModelF;
import fi.euclides.openmath.OMConstants;
import fi.euclides.openmath.Popcorn;
import fi.euclides.proof.LabelDelegate;
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
					fs.destroy(); 
					destroy(fs);
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
					l.setX(50); l.setY(50); // place at random
					viewer.getMapper().rename(l, var.getName());
					viewer.getModel().add(l);
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
					if(f instanceof Label && ((Label) f).getSubKey().equals(Lambda.INSTANCE.getSubKey()))
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
			}
		}
	}

	private void destroy(Object fs) {
		for(int i = 0; i < getSize(); i++ )
			if( getElementAt(i).item == fs)
			{ 	remove(i); break;
			}
	}

	protected void remove(int i) {
		delegate.remove(i);
	}

	public void update(Observable observable, Object arg) {
		if(arg == Destroyable.DESTROY) {
			observable.deleteObserver(this);
			destroy(observable);
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
