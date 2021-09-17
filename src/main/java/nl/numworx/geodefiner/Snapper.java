package nl.numworx.geodefiner;

import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.inject.Inject;

import fi.euclides.event.HitTester;
import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Locus;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.Triangle;
import fi.euclides.model.Visitor;
import fi.euclides.swing.AWTViewer;

public final class Snapper extends nl.numworx.geodefiner.common.Snapper implements Visitor {
		
		private AWTViewer viewer;
		boolean test;
		@Inject Snapper(AWTViewer viewer) {
			this.viewer = viewer;
		}
		
		private boolean testHits(MouseEvent ev) {
			HitTester test = viewer.getHitTester().copy();
			test.setXY(ev.getX(), ev.getY());
			test.setVisitor(this);
			this.test = false;
			Vector<Punt> points = viewer.getModel().getPunten();
			for(Punt p: points) {
				p.visit(test);
				if (this.test)
					return true; // no gravity at points.
			}
			return false;
		}
		
		
		public void translate(MouseEvent ev) {
			if (gravity) {
				int ox = (int) viewer.getModel().getO().getXd();
				int dx = (int) viewer.getModel().getU().getXd() - ox;
				//System.out.print(ev.getX() + " " + ox + " " + dx);
				int x = (ev.getX()-ox) % dx;
				if ( x < 0 ) x += dx;
				if ( x*2 > dx) x -= dx;
				//System.out.println(" " + x);
				if(x > SNAP || x < -SNAP) x = 0;

				int oy = (int) viewer.getModel().getO().getYd();
				int dy = dx;
				//System.out.print(ev.getX() + " " + ox + " " + dx);
				int y = (ev.getY()-oy) % dy;
				if ( y < 0 ) y += dy;
				if ( y*2 > dy) y -= dy;
				//System.out.println(" " + x);
				if(y > SNAP || y < -SNAP) y = 0;

				if ( (x != 0 || y != 0) && !testHits(ev) )
					ev.translatePoint(-x, -y);
			}
// Keep mouse inside panel
			{
				int x = ev.getX();
				if (x < 0) ev.translatePoint(-x, 0);
				else if (x > viewer.width) {
					ev.translatePoint(viewer.width-x, 0);
				}
			}
			{
				int y = ev.getY();
				if (y < 0) ev.translatePoint(0, -y);
				else if (y > viewer.height) {
					ev.translatePoint(0, viewer.height-y);
				}
			}
		}

		@Override
		public void visitPunt(Punt p) {
			test = !viewer.isTracked(p);
		}

		@Override
		public void visitLijn(Lijn l) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void visitCirkel(Cirkel c) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void visitSegment(Segment s) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void visitLabel(Label label) {
			// TODO Auto-generated method stub
			
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