package fi.euclides.proof;

import java.util.Vector;

import fi.euclides.util.Messages;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Locus;
import fi.euclides.model.MP;
import fi.euclides.model.Triangle;
import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.MiddelPunt;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.SpiegelPunt;
import fi.euclides.model.Visitor;
import fi.euclides.model.VrijPunt;
import fi.euclides.model.ZwaartePunt;
import fi.euclides.model.math.Numbers;

public class MidpointTester extends LabelTester {

	public static final String TYPE = "M"; //$NON-NLS-1$
	public String getSubKey() { return TYPE; }
	
	public MidpointTester() {
		super(""); //$NON-NLS-1$
	}

	public void command() {
		Vector select = getModel().getSelect();
		if(select.size() == 3)
		{
			try {
				Punt p1 = (Punt) select.firstElement();
				Punt p3 = (Punt) select.lastElement();
				Punt p2 = (Punt) select.elementAt(1);

				addLabel(new Punt[]{p1,p2,p3});
			} catch (ClassCastException e) {
				setStatus("Selecteer alleen punten");
			}			
		} else 
			setStatus("Selecteer eerst 3 punten");

	}

	/**
	 * Check voor triviale midpoint relaties.
	 * Ook een test voor 3op1rij.
	 * @param p1 Middelpunt?
	 * @param p2 Spiegelpunt?
	 * @param p3 Spiegelpunt?
	 * @return true als de relatie triviaal is.
	 */
	 static boolean check(Punt p1, Punt p2, Punt p3) {
		if(p1 instanceof MiddelPunt)
		{ MiddelPunt mp = (MiddelPunt)p1;
			if ( mp.getP1()==p3 && mp.getP2()==p2 ||
					mp.getP1()==p2&& mp.getP2()==p3)
				return true;
		}
		if(p2 instanceof SpiegelPunt)
		{
			SpiegelPunt sp = (SpiegelPunt)p2;
			if( sp.getPunt()==p3 && sp.getMirror()==p1 )
				return true;
		}
		if(p3 instanceof SpiegelPunt)
		{	SpiegelPunt sp = (SpiegelPunt) p3;
			if( sp.getPunt()==p2 && sp.getMirror()==p1)
				return true;
		}
		return false;
	}

	public Destroyable[] createDepend() {
		return new Destroyable[3];
	}

	protected boolean test(final Label l) {
		final Destroyable[] punten = l.getDepend();
		final Punt m = (Punt) punten[0];
		final boolean[] result = { false };
		Visitor v = new Visitor() {
			public void visitPunt(Punt p1) {
				Punt p2 = (Punt) punten[2];
				Numbers mC = Numbers.createComplex(m.getX(), m.getY());
				mC = Numbers.mul(mC, Numbers.TWO);
				mC = Numbers.sub(mC, Numbers.createComplex(p1.getX(),p1.getY()));
				mC = Numbers.sub(mC, Numbers.createComplex(p2.getX(),p2.getY()));
				ok(mC);
			}
			public void visitLijn(Lijn l) {}
			public void visitCirkel(Cirkel c) {
				equal(m, c.getCenter());
			}
			public void visitBoog(Boog b) {
				equal(m, b.getCenter());
			}
			private void equal(final Punt m, Punt p1) {
				Numbers mC = Numbers.createComplex(m.getX(), m.getY());
				mC = Numbers.sub(mC, Numbers.createComplex(p1.getX(),p1.getY()));
				ok(mC);
			}
			public void visitSegment(Segment s) {
				Numbers mC = Numbers.createComplex(m.getX(), m.getY());
				mC = Numbers.mul(mC, Numbers.TWO);
				Punt p1 = s.getP1();
				Punt p2 = s.getP2();
				mC = Numbers.sub(mC, Numbers.createComplex(p1.getX(),p1.getY()));
				mC = Numbers.sub(mC, Numbers.createComplex(p2.getX(),p2.getY()));	
				ok(mC);
			}
			public void visitLabel(Label label) {}

			public void visitTriangle(Triangle t) {
				Numbers mC = Numbers.createComplex(m.getX(), m.getY());
				mC = Numbers.mul(mC, Numbers.createInteger(3));
				mC = Numbers.sub(mC, Numbers.createComplex(t.getA().getX(),t.getA().getY()));
				mC = Numbers.sub(mC, Numbers.createComplex(t.getB().getX(),t.getB().getY()));
				mC = Numbers.sub(mC, Numbers.createComplex(t.getC().getX(),t.getC().getY()));
				ok(mC);
			}
			public void visitKegelsnede(Kegelsnede2 k) {
				Punt  p = new VrijPunt();
				k.getCenter(p);
				if(p.isDefined())
					equal(m, p);
			}
			public void visitLocus(Locus l) {
			}
			
			private void ok(Numbers value) {
				result[0] = setState(l,value, 0.0000001);
			}
		};
		punten[1].visit(v);
		return  result[0];   // add(abs,abs)
	}

	public boolean define(Label label) {
		if(test(label))
		{
			Destroyable[] pp = label.getDepend();
			Punt p1 = (Punt) pp[0];
			if(pp[1]instanceof Punt) {
				Punt p2 = (Punt) pp[1];
				Punt p3 = (Punt) pp[2];
				if(p2.getIndex()>p3.getIndex())
				{
					pp[2]=p2;
					pp[1]=p3;
					p2= p3;
					p3= (Punt) pp[2];
				}
				if(check(p1,p2,p3))
					label.setState(Label.CONFIGURATION);
				label.setString(s(p1) +Messages.getString("MidpointTester.2")+s(p2) +Messages.getString("MidpointTester.3")+s(p3) ); //$NON-NLS-1$ //$NON-NLS-2$
			} 
			else {
				if(pp[1] instanceof Cirkel && pp[0] == ((Cirkel) pp[1]).getCenter())
					label.setState(Label.CONFIGURATION);
				if(p1 instanceof ZwaartePunt && p1.getDepend()[0] == pp[1])
					label.setState(Label.CONFIGURATION);
				label.setString(s(p1) + Messages.getString("MidpointTester.2") + s(pp[1]));
			}
			label.register(this);
 			label.setX(p1.getX());
			label.setY(p1.getY());
			return true;
		}
		return false;
	}

}
