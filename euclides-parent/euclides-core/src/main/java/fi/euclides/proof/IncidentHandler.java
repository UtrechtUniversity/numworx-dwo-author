package fi.euclides.proof;

import fi.euclides.event.EventHandler;
import fi.euclides.model.MP;
import fi.euclides.model.Triangle;
import fi.euclides.model.Visitor;
import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Label;
import fi.euclides.model.LabelTrack;
import fi.euclides.model.Lijn;
import fi.euclides.model.Locus;
import fi.euclides.model.MiddelPunt;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp2;
import fi.euclides.model.Segment;
import fi.euclides.model.SpiegelPunt;
import fi.euclides.model.math.Exact;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class IncidentHandler extends EventHandler implements Observer, Visitor {

	
	boolean selected, trivial = false;
	private Punt p;
	private Lijn l;
	private Cirkel c;
	private Label label;
	private Triangle t;
	
	public void command() {
		selected = !selected;
		if(selected)
			getModel().addObserver(this);
		else
			getModel().deleteObserver(this);
	
	}

	public IncidentHandler(String string) {
		super(string);
	}

	public IncidentHandler() {
		super("");
	}

	public void update(Observable observable, Object arg) {
		if(observable == getModel() && arg instanceof Destroyable)
		{
			if(arg instanceof Punt)
			{
				p = (Punt)arg;
				getModel().visitLijnen(this);
			} else if(arg instanceof Lijn)
			{
				l = (Lijn)arg;
				c = null;
				p = null;
				label = null;
				getModel().visitPunten(this);
				getModel().visitLijnen(this);
			} else if(arg instanceof Cirkel)
			{
				c = (Cirkel)arg;
				l = null;
				getModel().visitPunten(this);
				getModel().visitLijnen(this);
			} else if(arg instanceof Label)
			{
				label = (Label)arg;
				if(label.getRegistered() instanceof LabelValue)
				{ 	l = null;
					c = null;
					p = null;
					getModel().visitLijnen(this);
				}
			} else if(arg instanceof Triangle)
			{
				t = (Triangle) arg;
				l = null;
				c = null;
				p = null;
				getModel().visitLijnen(this);
			}
		}

	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		if(selected!=this.selected)
		{
			command();
		}
	}

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#visitCirkel(euclides.Cirkel)
	 */
	public void visitCirkel(Cirkel c2) {
		if(p!=null)
		{
			testIncident(p,c2);
			testLabelTester(p, c2, MidpointTester.TYPE);

		}
		if(c != null && c != c2)
			testEquivalent(c, c2);
	}

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#visitLabel(euclides.Label)
	 */
	public void visitLabel(Label label) {
		if(this.label != null && label != this.label && label.getRegistered() instanceof LabelValue)
		{
			testEquivalent(this.label, label);
		}
	}

	private void testEquivalent(Label label1, Label label2) {
		if(label1.getString().equals((label2.getString())))
		{
			testLabelTester(label1, label2, Equidistant.TYPE);
		}
	}
	
	private void testEquivalent(Segment s1, Segment s2) {
		testLabelTester(s1,  s2, Equidistant.TYPE);
	}
	
	private void testEquivalent(Cirkel c1, Cirkel c2) {
		testLabelTester(c1,  c2, Equidistant.TYPE);
	}
	
	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#visitLijn(euclides.Lijn)
	 */
	public void visitLijn(Lijn l) {
		if(p!=null)
			testIncident(p,l);
		else if(this.l!=null && l != this.l)
		{
			testLabelTester(this.l, l, LijnLijnTest.PARALLEL);
			testLabelTester(this.l, l, LijnLijnTest.PERPENDICULAR);
		}
	}

	private boolean testLabelTester(Destroyable l1, Destroyable l2, String subkey) {
		LabelDelegate tester = getTracker().getRegistered(subkey);
		Label l = new Label();
		Destroyable d[] = tester.createDepend(2);
		d[0] = l1; d[1] = l2;
		l.register(tester);
		l.setDepend(d);
		if(tester.define(l))
		{
			if(l.getState() == Label.CONFIGURATION && !trivial)
			{
					l.destroy();
					return false;
			}
			getModel().add(l);
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#visitPunt(euclides.Punt)
	 */
	public void visitPunt(Punt p) {
		if(c != null)
		{
			testIncident(p, c);
			testLabelTester(p, c, MidpointTester.TYPE);
			
		} else if(l != null)
		{
			testIncident(p, l);
		}
		if(l instanceof Segment)
		{
			testLabelTester(p, l, MidpointTester.TYPE);
		}
	}

	/**
	 * @param p
	 * @param cirkel TODO
	 */
	private void testIncident(Punt p, Cirkel cirkel) {
		nqtrivial = false;
		if(PuntOp2.incident(p,cirkel))
		{
			if(trivial||nqtrivial)
				createLabel(p, cirkel, Label.CONFIGURATION, Numbers.ZERO);
				
		} else {
			final Numbers bracket = PointOnObject.bracket(p, cirkel);
			if(bracket == Numbers.ZERO)
			{
				createLabel(p, cirkel, Label.EXACT, Numbers.ZERO);
			}
			else if( !(bracket instanceof Exact)&& bracket.doubleValue()< PointOnObject.EPS)
			{
				createLabel(p, cirkel, Label.INEXACT, bracket);
			}
		}
	}

	/**
	 * @param p
	 * @param d
	 */
	private void createLabel(Punt p, Destroyable d, int state, Numbers value) {
		final Label label = PointOnObject.createLabel(p, d, getTracker());
		label.setState(state);
		label.value = value;
		getModel().add(label);
	}

	/**
	 * @param p
	 * @param lijn TODO
	 */
	private void testIncident(Punt p, Lijn lijn) {
		nqtrivial = false;
		if(incident(p,lijn))
		{
			if(trivial||nqtrivial)
				createLabel(p,lijn, Label.CONFIGURATION, Numbers.ZERO);
		} else {
			final Numbers bracket = PointOnObject.bracket(p, lijn);
			if(bracket == Numbers.ZERO)
			{
				createLabel(p, lijn,Label.EXACT, Numbers.ZERO);
			} else
			if(!(bracket instanceof Exact) && bracket.doubleValue()< PointOnObject.EPS)
			{
				createLabel(p, lijn, Label.INEXACT, bracket);
			}
		}
	}
	public static boolean nqtrivial; // not quite trivial
	public static boolean incident(Punt p, Lijn l) {
		if(l.incident(p) || p.incident(l))
			return true;
		
		if (p instanceof MiddelPunt)
		{ // een middelpunt op de lijn als beide eindpunten op de lijn liggen.
			MiddelPunt mp = (MiddelPunt)p;
			if(incident(mp.getP1(), l) && incident(mp.getP2(), l))
			{	nqtrivial = true;
				return true;
			}
		} else if (p instanceof SpiegelPunt)
		{
			SpiegelPunt sp = (SpiegelPunt)p;
			Punt org = sp.getPunt();
			Destroyable mirror = sp.getMirror();
			if( mirror instanceof Punt && incident(org,l) && incident((Punt) mirror, l))
			{
				nqtrivial = true;
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#visitSegment(euclides.Segment)
	 */
	public void visitSegment(Segment s) {
		if(p != null)
			testLabelTester(p, s, MidpointTester.TYPE);

		if(l instanceof Segment && l != s)
		{
			testEquivalent( (Segment) l, s);
		}
		visitLijn(s);
	}

	public void visitMP(MP l) {
		if(t != null && l instanceof Triangle && t != l)
		{
			if (!testLabelTester(t, l, Equidistant.TYPE))
				testLabelTester(t, l, SimTri.TYPE);
		}
	
	}

  @Override
  public void visitTriangle(Triangle t) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void visitKegelsnede(Kegelsnede2 k) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void visitLocus(Locus l) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void visitBoog(Boog b) {
    // TODO Auto-generated method stub
    
  }

}
