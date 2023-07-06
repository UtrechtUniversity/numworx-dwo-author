package fi.heks.scobjects;

import java.awt.*;

public class ScTextField extends TextField implements ScObject {
	public double schaal;
	public double relx, rely, relb, relh;
	public boolean resized;

	public ScTextField(int x, int y, int b, int h, String str) {
		super(str);
		schaal = 1;
		relx = x;
		rely = y;
		relb = b;
		relh = h;
		setBounds(x, y, b, h);
		Font f = new Font("SansSerif", Font.PLAIN, 2 * h / 3);
		setFont(f);
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
		Font f = new Font("SansSerif", Font.PLAIN, 2 * h / 3);
		setFont(f);
	}
}
