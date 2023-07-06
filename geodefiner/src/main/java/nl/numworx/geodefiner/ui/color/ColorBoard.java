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

	final static int XSIZE = 8, YSIZE=5;
	private java.awt.Color[][] colors = new java.awt.Color[XSIZE][YSIZE];
	
	ActionListener al;

	static int[][][] rgbCode = 
	{{{0,0,0},{153,51,0},{51,51,0},{0,51,0},{0,51,102},{0,0,128},{51,51,153},{51,51,51}},
	 {{128,0,0},{255,102,0},{128,128,0},{0,128,0},{0,128,128},{0,0,255},{102,102,153},{128,128,128}},
	 {{255,0,0},{255,153,0},{153,204,0},{51,153,102},{51,204,204},{51,102,255},{128,0,128},{150,150,150}},
	 {{255,0,255},{255,204,0},{255,255,0},{0,255,0},{0,255,255},{0,204,255},{153,51,102},{192,192,192}},
	 {{255,153,204},{255,204,153},{255,255,153},{204,255,204},{204,255,255},{153,204,255},{204,153,255},{255,255,255}},
	};
	
	public ColorBoard() {		
		for(int x = 0; x < XSIZE ; x++) {
			for(int y = 0; y < YSIZE; y++) {
				int rgb[] = rgbCode[y][x];
				colors[x][y] = new java.awt.Color(rgb[0], rgb[1], rgb[2]);
			}
		}
		setPreferredSize(new Dimension(XSIZE*20, YSIZE*20));
		setMinimumSize(new Dimension(XSIZE*3, YSIZE*3));
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
		x = x * XSIZE / w;x = Math.min(XSIZE-1, x);
		y = y * YSIZE / h;y = Math.min(YSIZE-1, y);
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
		int w = getWidth()/XSIZE;
		int h = getHeight()/YSIZE;
		Rectangle r = new Rectangle();
		r.width = w;
		r.height = h;
		for (int x = 0; x < XSIZE; x++) {
			for (int y = 0; y < YSIZE; y++) {
				g.setColor(colors[x][y]);
				r.x = x * w;
				r.y = y * h;
				g.fillRect(r.x, r.y, r.width, r.height);
			}
		}
	}

}
