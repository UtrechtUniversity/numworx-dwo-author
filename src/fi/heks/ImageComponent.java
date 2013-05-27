package fi.heks;

import java.awt.*;

public class ImageComponent extends Panel {
	private Image image;

	public ImageComponent(Image im) {
		image = im;
		// setSize(25,25);
		if (image != null)
			setSize(image.getWidth(null), image.getHeight(null));
	}

	public void paint(Graphics g) {
		if (image != null)
			g.drawImage(image, 0, 0, null);
	}
}
