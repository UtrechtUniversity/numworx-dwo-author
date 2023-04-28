package fi.heks;

import java.awt.*;
import java.awt.event.*;

import fi.heks.scobjects.*;

public class GetalComponent extends ScContainer implements ActionListener, FocusListener, MouseListener 
{
	private int waarde;
	private ScTextField beginWaardeTf;
	private boolean isTemp, instelbaar, bekend, leeg;
	private ActionListener actionListener;

	public GetalComponent(int x, int y, int b, int h) 
	{
		super(x, y, b, h);
		waarde = 0;
		bekend = true;
		leeg = false;
		instelbaar = false;
		isTemp = false;
		beginWaardeTf = new ScTextField(5, 0, b - 10, h, "0");
		beginWaardeTf.addActionListener(this);
		beginWaardeTf.addFocusListener(this);
		beginWaardeTf.setVisible(false);
		beginWaardeTf.setEnabled(false);
		beginWaardeTf.setLocation(getLocation().x, getLocation().y);
		add(beginWaardeTf);
	}

	public void paint(Graphics gr) 
	{
		Graphics g = (Graphics2D) gr;
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Font f = new Font("SansSerif", Font.PLAIN, (int) (3 * schaal * relh / 4));
		g.setColor(getForeground());
		g.setFont(f);
		String s;
		if (bekend) 
		{
			if (isTemp)
				s = Integer.toString(waarde) + "\u2103";
			else
				s = Integer.toString(waarde);
		} 
		else 
		{
			if (leeg)
				s = "";
			else if (isTemp)
				s = "...\u00B0C";
			else
				s = "...";
		}
		FontMetrics fm = g.getFontMetrics();
		int woordbreedte = fm.stringWidth(s);
		g.drawString(s, (getSize().width - woordbreedte) / 2, (getSize().height + fm.getHeight()) / 2 - fm.getDescent());
		super.paint(g);
	}

	public int geefWaarde() 
	{
		String s = beginWaardeTf.getText();
		int w;
		try 
		{
			w = Integer.parseInt(s);
			zetBekend(true);
			zetWaarde(w);
		} 
		catch (NumberFormatException ex) 
		{}
		return waarde;
	}

	public void zetWaarde(int t) 
	{
		zetBekend(true);
		waarde = t;
		beginWaardeTf.setText(Integer.toString(waarde));
		repaint();
	}

	public void zetBekend(boolean b) {
		bekend = b;
		if (!b)
			beginWaardeTf.setText("");
		if (!b)
			waarde = -999;
	}

	public void zetLeeg(boolean b) {
		leeg = b;
	}

	public void zetAlsTemp(boolean b) {
		isTemp = b;
	}

	public boolean isBekend() {
		return bekend;
	}

	public boolean isInstelbaar() {
		return instelbaar;
	}

	public void zetInstelbaar(boolean b) 
	{
		if (b && !instelbaar)
			addMouseListener(this);
		else if (!b && instelbaar)
		{	removeMouseListener(this);
System.out.println("mouseListener remove");		
		}
		instelbaar = b;
	}

	public void verhoog() 
	{
		waarde++;
		beginWaardeTf.setText(Integer.toString(waarde));
		repaint();
	}

	public void verlaag() {
		waarde--;
		beginWaardeTf.setText(Integer.toString(waarde));
		repaint();
	}

	public void verhoog(int d) {
		waarde += d;
		beginWaardeTf.setText(Integer.toString(waarde));
		repaint();
	}

	public void verlaag(int d) {
		waarde -= d;
		beginWaardeTf.setText(Integer.toString(waarde));
		repaint();
	}

	public void addActionListener(ActionListener listener) 
	{
		actionListener = AWTEventMulticaster.add(actionListener, listener);
	}

	public void removeActionListener(ActionListener listener) 
	{
		actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}

	public void vulIn() 
	{
//System.out.println("gc vulIn inst " + instelbaar);		
		if (instelbaar) 
		{
			beginWaardeTf.schaal(schaal);
			// add(beginWaardeTf);
			beginWaardeTf.setVisible(true);
			beginWaardeTf.setEnabled(true);
			beginWaardeTf.selectAll();
			beginWaardeTf.requestFocus();
		}
//System.out.println("gc vulIn Tf " + beginWaardeTf.isVisible());
	}

	
	public void mouseClicked(MouseEvent e) 
	{;}

	public void mousePressed(MouseEvent e) 
	{
//System.out.println("gc mP");		
		if (actionListener != null) 
		{
			actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "vulin"));
		}
	}

	public void mouseReleased(MouseEvent e) 
	{	vulIn();
	}

	public void mouseExited(MouseEvent e) {
		;
	}

	public void mouseEntered(MouseEvent e) {
		;
	}

	public void actionPerformed(ActionEvent e) 
	{
		String s = beginWaardeTf.getText();
		int w;
		try 
		{
			w = Integer.parseInt(s);
			zetBekend(true);
			zetWaarde(w);
		} catch (NumberFormatException ex) 
		{
			beginWaardeTf.setText("");
			zetBekend(false);
			waarde = -999;
			repaint();
		}
		beginWaardeTf.setEnabled(false);
		// remove(beginWaardeTf);
		beginWaardeTf.setVisible(false);
		repaint();
		if (actionListener != null) 
		{
			actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "action"));
		}
		this.requestFocus();
	}

	public void focusLost(FocusEvent e) 
	{
		String s = beginWaardeTf.getText();
		int w;
		try {
			w = Integer.parseInt(s);
			zetBekend(true);
			zetWaarde(w);
		} catch (NumberFormatException ex) {
			beginWaardeTf.setText("");
			zetBekend(false);
			waarde = -999;
			repaint();
		}
		beginWaardeTf.setEnabled(false);
		// remove(beginWaardeTf);
		beginWaardeTf.setVisible(false);
		repaint();
		if (actionListener != null) {
			actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "focuslost"));
		}
	}

	public void focusGained(FocusEvent e) {
		;
	}
}
