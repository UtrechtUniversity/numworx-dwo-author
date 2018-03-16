package nl.numworx.geodefiner;

import java.awt.event.MouseEvent;

import javax.inject.Inject;

import fi.euclides.swing.AWTViewer;

public final class Snapper extends nl.numworx.geodefiner.common.Snapper {
		
		private AWTViewer viewer;

		@Inject Snapper(AWTViewer viewer) {
			this.viewer = viewer;
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
		
	}