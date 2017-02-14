package nl.numworx.geodefiner.ui.color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class ColorBoard extends JComponent implements MouseListener {

	final static int SIZE = 6;
	private java.awt.Color[][] colors = new java.awt.Color[SIZE][SIZE];
	
	ActionListener al;
	
	public ColorBoard() {		
		for(int x = 0; x < SIZE ; x++) {
			for(int y = 0; y < SIZE; y++) {
				int t = y * SIZE + x;
				int c = t * 0xFFFFFF / (SIZE*SIZE-1);
				colors[x][y] = new java.awt.Color(c);
			}
		}
		setPreferredSize(new Dimension(SIZE*20, SIZE*20));
		setMinimumSize(new Dimension(SIZE*3, SIZE*3));
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
		int w = getWidth();
		int h = getHeight();
		x = x * SIZE / w;x = Math.min(SIZE-1, x);
		y = y * SIZE / h;y = Math.min(SIZE-1, y);
		return colors[x][y].getRGB()&0xFFFFFF;
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	
	@Override
	protected void paintComponent(Graphics g) {
		int w = getWidth()/SIZE;
		int h = getHeight()/SIZE;
		Rectangle r = new Rectangle();
		r.width = w;
		r.height = h;
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				g.setColor(colors[x][y]);
				r.x = x * w;
				r.y = y * h;
				g.fillRect(r.x, r.y, r.width, r.height);
			}
		}
	}

}
