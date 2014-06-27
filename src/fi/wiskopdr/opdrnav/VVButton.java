package fi.wiskopdr.opdrnav;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.JButton;

import fi.wiskopdr.WiskOpdr;

public class VVButton extends JButton {
    
    VVButton() {
        super();
    }
    
    VVButton(String tekst) {
        super(tekst);
    }
    
    public void paintComponent(Graphics g) {
        int b = getSize().width;
        int h = getSize().height;
        Polygon p = new Polygon();
        if("volgende".equals(getText())) {
            p.addPoint(0, h / 4);
            p.addPoint(b - h, h / 4);
            p.addPoint(b - h, 0);
            p.addPoint(b, h / 2);
            p.addPoint(b - h, h);
            p.addPoint(b - h, 3 * h / 4);
            p.addPoint(0, 3 * h / 4);
        }else if("vorige".equals(getText())) {
            p.addPoint(b, h / 4);
            p.addPoint(h, h / 4);
            p.addPoint(h, 0);
            p.addPoint(0, h / 2);
            p.addPoint(h, h);
            p.addPoint(h, 3 * h / 4);
            p.addPoint(b, 3 * h / 4);
        }
        Color c = WiskOpdr.bgcolor;
        g.setColor(new Color(c.getRed() - 20, c.getGreen() - 20, c.getBlue() - 20));
        if (WiskOpdr.zoefi)
            g.setColor(c);
        g.fillRect(0, 0, b, h);

        for (int i = 0; i < 10; i++) {
            g.setColor(new Color(200 + 5 * i, 200 + 5 * i, 200 + 5 * i));
            g.fillRect(0, getHeight() - (i + 1) * getHeight() / 10, getWidth(), getHeight() / 10 + 1);
        }

        if (isEnabled())
            g.setColor(Color.gray);
        else
            g.setColor(Color.lightGray);
        g.fillPolygon(p);
        g.drawPolygon(p);
    }
}
