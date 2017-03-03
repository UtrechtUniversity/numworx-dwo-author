package nl.numworx.geodefiner.ui.color;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

public class RecentColors extends JComponent implements MouseListener {

	final static int SIZE = 6;
	private static java.awt.Color[] colors = new java.awt.Color[SIZE];
	static {
		for(int x = 0; x < SIZE ; x++) {
			colors[x] = java.awt.Color.black;
		}
	}

	ActionListener al;
	
	public RecentColors() {		
		setPreferredSize(new Dimension(20, SIZE*20));
		setMinimumSize(new Dimension(20, SIZE*3));
		addMouseListener(this);
	}
	
	
	public void addActionListener(ActionListener l) {
		al = java.awt.AWTEventMulticaster.add(al, l);
	}
	public void removeActionListener(ActionListener l) {
		al = java.awt.AWTEventMulticaster.remove(al, l);
	}

	private void fire(int value) {
		if(al == null) return;
		String cmd  = Integer.toHexString(value);
		while(cmd.length() < 6) cmd = '0' + cmd;
		cmd = '#'+cmd;
		ActionEvent ev = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, cmd, System.currentTimeMillis(), 0);
		al.actionPerformed(ev);
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int value = posToColor(e.getX(), e.getY());
		fire(value);
	}

	private int posToColor(int x, int y) {
		int h = getHeight();
		y = y * SIZE / h;y = Math.min(SIZE-1, y);
		return colors[y].getRGB()&0xFFFFFF;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	
	@Override
	protected void paintComponent(Graphics g) {
		int w = getWidth();
		int h = getHeight()/SIZE;
		w = Math.min(h, w);
		Rectangle r = new Rectangle();
		r.x = 0;
		r.width = w;
		r.height = h;
		for (int y = 0; y < SIZE; y++) {
			g.setColor(colors[y]);
			r.y = y * h;
			g.fillRect(r.x, r.y, r.width, r.height);
		}
	}

	public void insertColor(java.awt.Color c) {
		for(java.awt.Color cc : colors) {
			if(cc.equals(c))
				return;
		}
		System.arraycopy(colors, 0, colors, 1, SIZE-1);
		colors[0] = c;
		repaint();
	}


	public void insertColor(int v) {
		v = v & 0xFFFFFF;
		for(java.awt.Color cc : colors) {
			if( (cc.getRGB()&0xFFFFFF) == v )
				return;
		}
		System.arraycopy(colors, 0, colors, 1, SIZE-1);
		colors[0] = new java.awt.Color(v);
		repaint();
	}

}
