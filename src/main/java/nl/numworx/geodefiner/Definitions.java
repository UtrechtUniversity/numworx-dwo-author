package nl.numworx.geodefiner;

import java.beans.PropertyChangeEvent;
import java.util.Vector;

import javax.swing.DefaultListModel;

import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMSymbol;
import nl.tue.win.riaca.openmath.lang.OMVariable;
import fi.euclides.model.AbstractViewer;
import fi.euclides.model.Coordinaten;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Locus;
import fi.euclides.model.Locus.LocusModel;
import fi.euclides.model.Punt;
import fi.euclides.model.Triangle;
import fi.euclides.model.math.Numbers;
import fi.euclides.openmath.Expression;
import fi.euclides.openmath.Lambda;
import fi.euclides.openmath.LocusModelF;
import fi.euclides.openmath.Popcorn;

/** FIXME split in a observable and a ListModel
 * 
 * @author wim
 *
 */
public class Definitions extends DefaultListModel<CELL> {

	private AbstractViewer viewer;

	public Definitions(AbstractViewer viewer) {
		this.viewer = viewer;
	}
	static final OMSymbol POINT = new OMSymbol("geodefiner", "point");
	static final OMSymbol LINE  = new OMSymbol("geodefiner" , "line");
	static final OMSymbol CIRCLE = new OMSymbol("geodefiner", "circle");
	static final OMSymbol ARC    = new OMSymbol("geodefiner", "arc");
	static final OMSymbol SEGMENT = new OMSymbol("geodefiner", "segment");
	static final OMSymbol CURVE   = new OMSymbol("geodefiner", "curve");
	static final OMSymbol POLYGON = new OMSymbol("geodefiner","polygon");
	static final OMSymbol TEXT   = new OMSymbol("geodefiner", "text");
		
	public void define(String text,OMObject object) {

// Interpreter of GeoDefiner statements		
			
		if(object instanceof OMApplication) {
			OMApplication oma = (OMApplication) object;
			OMObject first = oma.firstElement();
			if( first.isSame(Popcorn.PROG1_ASSIGN))
			{
				OMVariable var = (OMVariable) oma.getElementAt(1);
// No reassignments, or delete?
				Destroyable fs = viewer.getMapper().fromString(var.getName());
				if (fs != null) {
					fs.destroy(); 
					destroy(fs);
				}
				
				if (oma.getElementAt(2) instanceof OMApplication)
				{ 	oma = (OMApplication) oma.getElementAt(2);
					OMObject f = oma.firstElement();
					Destroyable[] depend = new Destroyable[oma.getLength()-1];
					Expression.copy(oma, viewer.getMapper(), depend);
				if(POINT.isSame(f)) {
// $P := point(1,2)
					Label ix = (Label) depend[0]; // toNumber(object)
					Label iy = (Label) depend[1];
					Numbers x = ix.value;
					Numbers y = iy.value;
					Coordinaten p = viewer.getModel().buildCoordinaten(x, y);
					p.setCx(ix); p.setCy(iy);
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
					addElement(new CELL(text, l));
					viewer.getMapper().rename(l, var.getName());
					return;
				}				
// $l := circle($P, $Q)
				if (CIRCLE.isSame(f)) {
					Destroyable l = viewer.getModel().buildCirkel(depend);
					addElement(new CELL(text, l));
					viewer.getMapper().rename(l, var.getName());
					return;
				}
// $t := text("text", $P)
				if(TEXT.isSame(f)) {
					Punt  p = (Punt) depend[1];
					Label t = (Label) depend[0]; // "text", ["x=",$x]
					t.setP(p);
					viewer.getModel().add(t);
					addElement(new CELL(text, t));
					viewer.getMapper().rename(t, var.getName());
					return;
				}
// $l := polygon($P, ...)
				if (POLYGON.isSame(f)) {
					Triangle t3 = viewer.getModel().buildTriangle(depend);
					addElement(new CELL(text, t3));
					viewer.getMapper().rename(t3, var.getName());
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
					Destroyable f = Expression.interpret(oma.getElementAt(2), l, viewer.getMapper());
					viewer.getModel().add(f);
					viewer.getMapper().rename(f, var.getName());
// display function
					if(f instanceof Label && ((Label) f).getSubKey().equals(Lambda.INSTANCE.getSubKey()))
					{    LocusModel lm = new LocusModelF((Label)f, viewer.getMapper());
					     Locus locus = new Locus(lm);
					     viewer.getModel().add(locus);
					     f = locus;
					}
					addElement(new CELL(text, f));
					return;
				}
			}
		}
	}

	private void destroy(Destroyable fs) {
		for(int i = 0; i < size(); i++ )
			if( getElementAt(i).item == fs)
			{ 	remove(i); break;
			}
	}
	

}
