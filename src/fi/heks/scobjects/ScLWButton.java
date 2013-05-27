package fi.heks.scobjects;

import java.awt.*;
import java.awt.event.*;

public class ScLWButton extends Component implements MouseListener, ScObject {
	private Font defaultfont = new Font("SansSerif", Font.BOLD, 12);
	private Color bgColor = new Color(180, 180, 180);
	private Color darkColor = Color.black;
	private Color lightColor = Color.lightGray;

	private Polygon p0, p1, p2, p3;
	private Color bgBright, bgDark;

	protected ActionListener actionListener = null;
	protected String opschrift;
	protected Font labelfont;

	public double schaal;
	public double relx, rely, relb, relh;
	public boolean resized;

	public ScLWButton(int x, int y, int b, int h, String str) {
		opschrift = str;
		schaal = 1;
		relx = x;
		rely = y;
		relb = b;
		relh = h;
		setBounds(x, y, b, h);
		addMouseListener(this);

		bgBright = bgColor.brighter();
		bgDark = bgColor.darker();

		p0 = new Polygon();
		p0.addPoint(0, 0);
		p0.addPoint(b, 0);
		p0.addPoint(b - h / 6, h / 6);
		p0.addPoint(h / 6, h / 6);

		p1 = new Polygon();
		p1.addPoint(0, 0);
		p1.addPoint(0, h);
		p1.addPoint(h / 6, h - h / 6);
		p1.addPoint(h / 6, h / 6);

		p2 = new Polygon();
		p2.addPoint(b, 0);
		p2.addPoint(b, h);
		p2.addPoint(b - h / 6, h - h / 6);
		p2.addPoint(b - h / 6, h / 6);

		p3 = new Polygon();
		p3.addPoint(0, h);
		p3.addPoint(b, h);
		p3.addPoint(b - h / 6, h - h / 6);
		p3.addPoint(h / 6, h - h / 6);
		/**/

		// labelfont = defaultfont;
		// super.setFont(labelfont);
		// Toolkit tk = Toolkit.getDefaultToolkit();
		// FontMetrics fm = tk.getFontMetrics(labelfont);
		// opschrifty = (height +fm.getHeight())/2 - fm.getDescent();
	}

	public void addActionListener(ActionListener l) {
		actionListener = AWTEventMulticaster.add(actionListener, l);
	}

	public void removeActionListener(ActionListener l) {
		actionListener = AWTEventMulticaster.remove(actionListener, l);
	}

	public String getLabel() {
		return opschrift;
	}

	public void setEnabled(boolean b) {
		super.setEnabled(b);
		if (isVisible()) {
			repaint();
		}
	}

	public void setBackground(Color c) {
		bgColor = c;
		bgBright = bgColor.brighter();
		bgDark = bgColor.darker();
	}

	public void paint(Graphics g) {
		g.setColor(bgColor);
		g.fillRect(0, 0, getSize().width, getSize().height);

		g.setColor(bgBright);
		g.fillPolygon(p0);
		g.fillPolygon(p1);
		g.setColor(bgDark);
		g.fillPolygon(p2);
		g.fillPolygon(p3);
		if (isEnabled()) {
			g.setColor(darkColor);
		} else {
			g.setColor(lightColor);
		}
		Font f = new Font("SansSerif", Font.BOLD, (int) (3 * schaal * relh / 5));
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		int woordbreedte = fm.stringWidth(opschrift);
		g.drawString(opschrift, (getSize().width - woordbreedte) / 2, (getSize().height + fm.getHeight()) / 2 - fm.getDescent());
	}

	public void setResized(boolean b) {
		resized = b;
	}

	public void schaal(double s) {
		schaal = s;
		int x = (int) (schaal * relx);
		int y = (int) (schaal * rely);
		int b = (int) (schaal * relb);
		int h = (int) (schaal * relh);
		setBounds(x, y, b, h);

		p0 = new Polygon();
		p0.addPoint(0, 0);
		p0.addPoint(b, 0);
		p0.addPoint(b - h / 6, h / 6);
		p0.addPoint(h / 6, h / 6);

		p1 = new Polygon();
		p1.addPoint(0, 0);
		p1.addPoint(0, h);
		p1.addPoint(h / 6, h - h / 6);
		p1.addPoint(h / 6, h / 6);

		p2 = new Polygon();
		p2.addPoint(b, 0);
		p2.addPoint(b, h);
		p2.addPoint(b - h / 6, h - h / 6);
		p2.addPoint(b - h / 6, h / 6);

		p3 = new Polygon();
		p3.addPoint(0, h);
		p3.addPoint(b, h);
		p3.addPoint(b - h / 6, h - h / 6);
		p3.addPoint(h / 6, h - h / 6);

	}

	public void mousePressed(MouseEvent e) {
		bgBright = bgColor.darker();
		bgDark = bgColor.brighter();
		repaint();
	}

	public void mouseReleased(MouseEvent e) {
		if (isEnabled()) {
			if (actionListener != null) {
				actionListener.actionPerformed(new ActionEvent(this, 0, opschrift));
			}
		}
		bgBright = bgColor.brighter();
		bgDark = bgColor.darker();
		repaint();
	}

	public void mouseEntered(MouseEvent e) {
		darkColor = Color.yellow;
		repaint();

	}

	public void mouseExited(MouseEvent e) {
		darkColor = Color.black;
		repaint();
	}

	public void mouseClicked(MouseEvent e) {
		;
	}

}