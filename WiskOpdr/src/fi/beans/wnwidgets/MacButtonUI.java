package fi.beans.wnwidgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.basic.BasicButtonUI;

public class MacButtonUI extends BasicButtonUI {

	private static final Color FOREGROUND = new Color(100,100,100);
	Image backgr, backgrOver, backgrClick;
	static MacButtonUI _instance;
	static public ButtonUI getInstance() {
		if(_instance == null)
			_instance = new MacButtonUI();
		return _instance;
	}
	/**
	 * 
	 */
	MacButtonUI() {
		super();
		backgr = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/macknop.png"));
		backgrOver = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/macknop_over.png"));
		backgrClick = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/macknop_click.png"));
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicButtonUI#paint(java.awt.Graphics, javax.swing.JComponent)
	 */
	public void paint(Graphics g, JComponent c) {
		Dimension size = c.getSize();
		Image bgImage = backgr;
		ButtonModel model = ((AbstractButton)c).getModel();
		boolean rollOver = model.isRollover();
		boolean isPressed = model.isPressed();
		
		if(isPressed)
		{	bgImage = backgrClick;
			c.setForeground(Color.white);
		}
		else if(rollOver)
		{	bgImage = backgrOver;
			c.setForeground(Color.black);
		}
		else c.setForeground(FOREGROUND);

		int w = bgImage.getWidth(c);
		int h = bgImage.getHeight(c);
		int W = 18;
//		int a = Math.min(size.height, size.width);
//		g.setColor(Color.pink);
//		g.fillRoundRect(0, 0, size.width, size.height, a, a);
//		g.setColor(Color.gray);
//		g.drawRoundRect(0, 0, size.width, size.height, a, a);
		g.drawImage(bgImage, 0, 0, W, h, 0, 0, W, h, c);
		g.drawImage(bgImage, size.width-W, 0, size.width, h, w-W, 0, w, h, c);
		g.drawImage(bgImage, W, 0, size.width-W, h, W, 0, W+8, h, c);
		super.paint(g, c);
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicButtonUI#paintButtonPressed(java.awt.Graphics, javax.swing.AbstractButton)
	 */
	protected void paintButtonPressed(Graphics g, AbstractButton b) {
		// TODO Auto-generated method stub
		/*Dimension size = b.getSize();
		int w = backgrClick.getWidth(b);
		int h = backgrClick.getHeight(b);
		int W = 8;
//		int a = Math.min(size.height, size.width);
//		g.setColor(Color.pink);
//		g.fillRoundRect(0, 0, size.width, size.height, a, a);
//		g.setColor(Color.gray);
//		g.drawRoundRect(0, 0, size.width, size.height, a, a);
		g.drawImage(backgrClick, 0, 0, W, h, 0, 0, W, h, b);
		g.drawImage(backgrClick, size.width-W, 0, size.width, h, w-W, 0, w, h, b);
		g.drawImage(backgrClick, W, 0, size.width-W, h, W, 0, W+8, h, b);*/
		super.paintButtonPressed(g, b);
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicButtonUI#paintFocus(java.awt.Graphics, javax.swing.AbstractButton, java.awt.Rectangle, java.awt.Rectangle, java.awt.Rectangle)
	 */
	protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect,
			Rectangle textRect, Rectangle iconRect) {
		// TODO Auto-generated method stub
		
		super.paintFocus(g, b, viewRect, textRect, iconRect);
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicButtonUI#paintIcon(java.awt.Graphics, javax.swing.JComponent, java.awt.Rectangle)
	 */
	protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
		// TODO Auto-generated method stub
		
		//super.paintIcon(g, c, new Rectangle(x,y,iconRect.width,iconRect.height));
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicButtonUI#paintText(java.awt.Graphics, javax.swing.AbstractButton, java.awt.Rectangle, java.lang.String)
	 */
	protected void paintText(Graphics g, AbstractButton b, Rectangle textRect,
			String text) {
		// TODO Auto-generated method stub
		super.paintText(g, b, textRect, text);
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
	 */
	public void update(Graphics g, JComponent c) {
		// TODO Auto-generated method stub
		super.update(g, c);
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicButtonUI#installUI(javax.swing.JComponent)
	 */
	public void installUI(JComponent c) {
		super.installUI(c);
		MediaTracker tr = new MediaTracker(c);
		tr.addImage(backgr, 0);
		tr.addImage(backgrOver, 0);
		tr.addImage(backgrClick, 0);
		try {
			tr.waitForAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		c.setBorder(null);
		c.setOpaque(false);
		c.setFont(new Font("SansSerif", Font.BOLD, 13));
		c.setForeground(FOREGROUND);
	}
	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicButtonUI#getPreferredSize(javax.swing.JComponent)
	 */
	public Dimension getPreferredSize(JComponent c) {
		Dimension s = super.getPreferredSize(c);
		s.height = backgr.getHeight(null);
		s.width  = backgr.getWidth(null);
		return s;
	}

}
