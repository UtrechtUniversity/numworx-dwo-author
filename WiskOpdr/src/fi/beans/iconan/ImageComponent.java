package fi.beans.iconan;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;

class ImageComponent extends JComponent {

  final Image image;

  ImageComponent(Image image, int width, int height) {
    setSize(Math.max(0, width), Math.max(height,0));
    setPreferredSize(getSize());
    this.image = image;
    setOpaque(false); // transparant!
  }

  public void paintComponent(Graphics g) {
    try {
      g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    } catch (Exception e) {
      java.util.logging.Logger.getLogger(getClass().getName()).severe("drawImage " + e);
    }
  }
}