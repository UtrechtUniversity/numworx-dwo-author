package fi.euclides.event;

import fi.euclides.model.Locus;
import fi.euclides.model.MP;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.RaakLijnConic;
import fi.euclides.model.Triangle;
import fi.euclides.model.Bissectrice;
import fi.euclides.model.Boog;
import fi.euclides.model.CarryingLine;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Cirkel3;
import fi.euclides.model.CirkelRadius;
import fi.euclides.model.ConflictLijn;
import fi.euclides.model.Coordinaten;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Dpunt;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.LijnPuntCombi;
import fi.euclides.model.LoodLijn;
import fi.euclides.model.MiddelPunt;
import fi.euclides.model.ParallelLijn;
import fi.euclides.model.Poollijn;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.PuntOp2;
import fi.euclides.model.PuntenLijn;
import fi.euclides.model.RaakLijnCirkel;
import fi.euclides.model.Segment;
import fi.euclides.model.SpiegelPunt;
import fi.euclides.model.Visitor;
import fi.euclides.model.ZwaartePunt;
import fi.euclides.proof.LabelValue;
import fi.euclides.util.Messages;
import fi.euclides.util.Observer;

public class DescriptionBuilder implements Visitor {
		public void visitTriangle(Triangle t) {
			SpiegelPunt sp;
			if( null != (sp = getAnonSP(t.getDepend()[0])))
			{
				mirrorString(sp);
				return;
			}
			string = " is Δ";
			Destroyable depend[] = t.getDepend();
			for (int i = 0; i < depend.length; i++) {
				string += toString(depend[i]);
			}
			
		}

		public void visitKegelsnede(Kegelsnede2 k) {
			SpiegelPunt sp;
			if( null != (sp = getAnonSP(k.getA())))
			{
				mirrorString(sp);
				return;
			}
			string = " door " + toString(k.getA()) + ", "
			+ toString(k.getB()) + ", "
			+ toString(k.getC()) + ", "
			+ toString(k.getD()) + ", "
			+ toString(k.getE());			
		}

		public void visitLocus(Locus locus) {
			Destroyable[] depend = locus.getDepend();
			SpiegelPunt sp;
			if ( depend.length > 1 &&
				 null != (sp = getAnonSP(depend[1])))
			{
				mirrorString(sp);
				return;
			}
			if (depend.length >= 2)
			string = ( " bron " + toString(depend[0]) + 
					(depend.length > 1 ?
					(", spoor " + toString(depend[1])): ""));
			
		}

		protected NameMapper mapper;
		protected String string = "";
		
		public String toString() {
			return string;
		}
		
		/**
		 * @param m
		 */
		public DescriptionBuilder(NameMapper m) {
			this.mapper = m;
		}

		public String toString(Destroyable d)
		{
			return mapper.toString(d);
		}
		
		private SpiegelPunt getAnonSP(Destroyable d)
		{
			if(d != null && (!d.isVisible()||d.getIndex()==0) && d instanceof SpiegelPunt)
				return (SpiegelPunt) d;
			return null;
		}
		
		public void visitBoog(Boog b) {
// FIXME breekt bij BoogHoek
			Destroyable[] depend = b.getDepend();
			string = " van " + toString(depend[0]) + " door " + toString(depend[1]) +" tot " + toString(depend[2]);

		}
		
		public void visitCirkel(Cirkel c) {
			SpiegelPunt sp;
			if(c instanceof CirkelRadius)
			{
				Destroyable[] depend = c.getDepend();
				string = Messages.getString("OpenLijstAction.4") + toString(c.getCenter()) + Messages.getString("OpenLijstAction.5") + toString(depend[1]); //$NON-NLS-1$ //$NON-NLS-2$
				return;
			}
			if(c instanceof Cirkel3)
			{
				Destroyable[] depend = c.getDepend();
				if( (sp = getAnonSP(depend[0])) != null)
				{
					mirrorString(sp);
					return;
				}
				
				string = " door " + toString(depend[0]) + "," + toString(depend[1]) +"," + toString(depend[2]);
				return;
			} 
			sp = getAnonSP(c.getCenter());
			// centrum is anonieme spiegel:
			if(sp != null)
			{
				mirrorString(sp);
				return;
			}
			string = Messages.getString("OpenLijstAction.4") + toString(c.getCenter()) + Messages.getString("OpenLijstAction.5") + toString(c.getRadius())+toString(c.getRadius2()); //$NON-NLS-1$ //$NON-NLS-2$
		}

		void mirrorString(SpiegelPunt sp) {
			Destroyable mirror = sp.getMirror();
			string = " is " + toString(sp.orig) + spiegelTxt(mirror) + toString(mirror);
		}

		public void visitLabel(Label label) {
			string = " " + label.getString(); //$NON-NLS-1$
			Observer ov = label.getRegistered();
			if(ov instanceof LabelValue)
			{
				string += " is "  + ((LabelValue) ov).getSymbolicValue(label); //$NON-NLS-1$
			}  
		}

		public void visitLijn(Lijn l) {
			if(l instanceof Poollijn)
			{
				Destroyable[] d = l.getDepend();
				string = " poollijn van " + toString(d[0])  + " met " + toString(d[1]);
				return;
			}
			if(l instanceof CarryingLine)
			{
				Destroyable d = l.getDepend()[0];
				string = " carrying line of " + toString(d);
				return;
			}
			if(l instanceof PuntenLijn)
			{	SpiegelPunt sp;
				PuntenLijn pl = (PuntenLijn)l;
				Destroyable p1 = pl.getP1();
				if(null != (sp = getAnonSP(p1)))
				{
					mirrorString(sp);
					return;
				}
				Destroyable p2 = pl.getP2();
				string = Messages.getString("OpenLijstAction.7") + toString(p1) + Messages.getString("OpenLijstAction.8") + toString(p2); //$NON-NLS-1$ //$NON-NLS-2$
			} else if( l instanceof LoodLijn)
			{
				LoodLijn ll = (LoodLijn) l;
				Destroyable p =  ll.getPunt();
				Lijn l2 = ll.getDestroyable();
				string = " \u22A5 " + toString(l2) + Messages.getString("OpenLijstAction.10") + toString(p); //$NON-NLS-1$ //$NON-NLS-2$
			} else if(l instanceof ParallelLijn)
			{
				ParallelLijn pl = (ParallelLijn)l;
				Destroyable p = pl.getPunt();
				Lijn l2 = pl.getDestroyable();
				string =  " \u2225 " + toString(l2) + Messages.getString("OpenLijstAction.12") + toString(p) ; //$NON-NLS-1$ //$NON-NLS-2$
			} else if(l instanceof Bissectrice)
			{
				Bissectrice b = (Bissectrice)l;
				string = Messages.getString("OpenLijstAction.13") + toString(b.getP1()) + toString(b.getP2()) + toString(b.getP3()); //$NON-NLS-1$
			} else if( l instanceof ConflictLijn)
			{
				string = " middelloodlijn van " + toString(l.getDepend()[0]) + " en " + toString(l.getDepend()[1]);
			} else if (l instanceof RaakLijnConic || l instanceof RaakLijnCirkel) {
				LijnPuntCombi lp = (LijnPuntCombi) l;
				string = " raaklijn aan " + toString(lp.getDestroyable())  + " door " + lp.getPunt();
			} else
				string = "";
		}

		public void visitPunt(Punt p) {	
				string = "";
				Destroyable[] d = p.getDepend();
				if(p instanceof Dpunt)
				{
					return;
				}
				if(p instanceof PuntOp)
				{
					Destroyable l = d[0];
					string = Messages.getString("OpenLijstAction.17") + toString(l); //$NON-NLS-1$
				} else if (p instanceof PuntOp2) {
					Destroyable l = d[0];
					Destroyable l2 = d[1];
					string = Messages.getString("OpenLijstAction.18") + toString(l) + Messages.getString("OpenLijstAction.19") + toString(l2); //$NON-NLS-1$ //$NON-NLS-2$
				} else if (p instanceof MiddelPunt)
				{
					Destroyable p1 = d[0];
					Destroyable p2 = d[1];
					string = Messages.getString("OpenLijstAction.20")+ toString(p1) + Messages.getString("OpenLijstAction.21") + toString(p2);	 //$NON-NLS-1$ //$NON-NLS-2$
				} else if (p instanceof SpiegelPunt)
				{
					Destroyable p1  = d[1];
					Destroyable p2  = d[0];
					String txt = spiegelTxt(p2);
					string = Messages.getString("OpenLijstAction.22") + toString(p1) + txt + toString(p2); //$NON-NLS-1$ //$NON-NLS-2$
				} else if (p instanceof Coordinaten) 
				{
					String sx=null, sy=null;
					Label cx = (Label) d[0];
					Label cy = (Label) d[1];
					if(cx.getRegistered() instanceof LabelValue)
					{
						LabelValue lv = (LabelValue) cx.getRegistered();
						sx = lv.getSymbolicValue(cx);						
					} else
						sx = cx.getString();
					if(cy.getRegistered() instanceof LabelValue)
					{
						LabelValue lv = (LabelValue) cy.getRegistered();
						sy = lv.getSymbolicValue(cy);						
					}
					else
						sy = cy.getString();
					string = " is ( " + sx + " , " + sy + " )";
				} else if (p instanceof ZwaartePunt)
				{
					string = " is het midden van " + toString(d[0]);
				}
				
		}

		String spiegelTxt(Destroyable p2) {
			int type = Label.UNKNOWN;
			if(p2 instanceof Label)
			{
				type = ((Label) p2).getState();
			}
			String txt;
			switch(type)
			{ 
				default:
					txt = Messages.getString("OpenLijstAction.23");
					break;
				case Label.VECTOR:
					txt = " verschoven met ";
					break;
				case Label.HOEK:
					txt = " gedraaid over ";
			}
			return txt;
		}

		public void visitSegment(Segment s) {
			Destroyable p1 = s.getP1();
			SpiegelPunt sp;
			if(null != (sp = getAnonSP(p1)))
			{
				mirrorString(sp);
				return;
			}
			Destroyable p2 = s.getP2();
			string = Messages.getString("OpenLijstAction.26") + toString(p1) + Messages.getString("OpenLijstAction.27") + toString(p2); //$NON-NLS-1$ //$NON-NLS-2$
		}

}
