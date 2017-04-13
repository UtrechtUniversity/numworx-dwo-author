package fi.euclides.swing;

import java.awt.FontMetrics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import fi.euclides.event.HitTester;
import fi.euclides.model.Triangle;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;

public class HitTester2 extends HitTester {

	protected FontMetrics fm;
	
	public HitTester2(FontMetrics fm) {
		this.fm = fm;
	}

	/* (non-Javadoc)
	 * @see fi.euclides.event.HitTester#copy()
	 */
	public HitTester copy() {
		return new HitTester2(fm);
	}

	/* (non-Javadoc)
	 * @see fi.euclides.event.HitTester#visitMP(fi.euclides.locus.MP)
	 */
	public void visitTriangle(Triangle mp) {
		Shape s = mp.adapt(Shape.class);
		if(s != null)
		{
			if(s.contains(lastx, lasty))
			{
				call(mp);
			}
			return;
		}

		Punt[] depend = (Punt[]) mp.getDepend();
		Polygon p = new Polygon();
		for (int i = 0; i < depend.length; i++) {
			Punt punt = depend[i];
			p.addPoint((int)punt.getXd(), (int)punt.getYd());				
		}
		if(p.contains(lastx, lasty))
		{
			call(mp);
			return;
		}
		super.visitTriangle(mp);
	}

	public void visitLabel(Label label) {
		Shape s = label.adapt(Shape.class);
		if(s != null)
		{
			if(s.contains(lastx, lasty))
			{
				call(label);
			}
			return;
		}
		
		if(fm != null)
		{
			long x = Math.round(label.getXd());
			long y = Math.round(label.getYd());
			String string = label.getString();
			Rectangle r = new Rectangle();
			int width = fm.stringWidth(string);
			r.x = (int) x;
			r.y = (int) (y - fm.getAscent());
			r.height = fm.getHeight();
			r.width = width;
			if(r.contains(lastx, lasty))
			{
				call(label);
			}
			
		} 
		else
			super.visitLabel(label);
	}

	
	
	protected Collection<Destroyable> triangles = new LinkedList<Destroyable>();

	public void done() {
		if(triangles != null) {
			Iterator<Destroyable> iter = triangles.iterator();
			while (iter.hasNext()) {
				Destroyable object = iter.next();
				super.call(object);
			}
			triangles.clear();
		} else 
			triangles = new LinkedList<Destroyable>();
		
	}
	
	protected void call(Destroyable d) {
		if(d instanceof Triangle) {
			if(triangles != null)
			{
				triangles.add(d); // delay...
			}
			return;
		}
		super.call(d); // d.visit(v);
		triangles = null;
	}
}
