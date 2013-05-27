package fi.heks.scobjects;

import java.awt.*;
import java.util.*;

public class ScPanel extends Panel implements ScObject {
	private Image bufferimage;
	private Graphics gIm;

	public double schaal;
	public double relx, rely, relb, relh;
	public boolean resized;
	private boolean vastePlaats;

	public ScPanel() {
		setLayout(null);
		schaal = 1;
	}

	public ScPanel(int x, int y, int b, int h) {
		setLayout(null);
		schaal = 1;
		relx = x;
		rely = y;
		relb = b;
		relh = h;
		setBounds(x, y, b, h);
	}

	public void setState(Hashtable h) {
	}

	public Hashtable getState() {
		return null;
	}

	public double getScore() {
		return 0;
	}

	public void start() {
	}

	public void stop() {
	}

	public void paint(Graphics g) {
		Dimension dd = getSize();
		if (bufferimage == null || resized) {
			if (resized && gIm != null)
				gIm.dispose();
			bufferimage = createImage(dd.width, dd.height);
			gIm = bufferimage.getGraphics();
			resized = false;
		}
		gIm.setColor(getBackground());
		gIm.fillRect(0, 0, dd.width, dd.height);
		super.paint(gIm);
		g.drawImage(bufferimage, 0, 0, null);

	}

	public void update(Graphics g) { // gIm.setColor(getBackground());
										// gIm.fillRect(0,0,getSize().width,getSize().height);
		paint(g);
		// g.drawImage(bufferimage, 0, 0, null);
	}

	public void setResized(boolean b) {
		resized = b;
	}

	public void zetVastePlaats(boolean b) {
		vastePlaats = b;
	}

	public void schaal(double s) {
		schaal = s;
		int x = (int) (schaal * relx);
		int y = (int) (schaal * rely);
		int b = (int) (schaal * relb);
		int h = (int) (schaal * relh);
		if (vastePlaats)
			setSize(b, h);
		else
			setBounds(x, y, b, h);
		resized = true;

		int n = getComponentCount();
		for (int i = 0; i < n; i++) {
			Component c = getComponent(i);
			ScObject scc = null;
			try {
				scc = (ScObject) c;
			} catch (ClassCastException ce) {
			}
			if (scc != null)
				scc.schaal(schaal);

		}
	}
}