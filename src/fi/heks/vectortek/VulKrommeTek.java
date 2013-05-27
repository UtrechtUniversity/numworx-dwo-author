package fi.heks.vectortek;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

public class VulKrommeTek extends TekenObjectTek {
	Polygon basisPolygon;
	public Polygon buigPolygon;
	Color vulkleur;
	Color lijnkleur;
	boolean isOmlijnd, isGevuld;
	double[] exactePuntenX, exactePuntenY;

	public VulKrommeTek(DataInputStream inv) {
		isGevuld = false;
		isOmlijnd = false;
		basisPolygon = new Polygon();
		try {
			int gevuld = inv.readByte();
			if (gevuld != 0) {
				isGevuld = true;
				int r = inv.readByte() + 128;
				int g = inv.readByte() + 128;
				int b = inv.readByte() + 128;
				vulkleur = new Color(r, g, b);
			}
			int omlijnd = inv.readByte();
			if (omlijnd != 0) {
				isOmlijnd = true;
				int r = inv.readByte() + 128;
				int g = inv.readByte() + 128;
				int b = inv.readByte() + 128;
				lijnkleur = new Color(r, g, b);
			}
			int n = inv.readByte();
			for (int j = 0; j < n; j++) {
				int x = inv.readByte() * 2 + 254;
				int y = inv.readByte() * 2 + 254;
				basisPolygon.addPoint(x, y);
			}
		} catch (IOException io) {
		}
		buigPolygon = buig(basisPolygon);
	}

	public void paint(Graphics gr) {
		Graphics g = (Graphics2D) gr;
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (isGevuld && vulkleur != null) {
			g.setColor(vulkleur);
			g.fillPolygon(buigPolygon);
		}

		if (isOmlijnd && lijnkleur != null) {
			g.setColor(lijnkleur);
			g.drawPolygon(buigPolygon);
		}
	}

	public boolean contains(int x, int y) {
		return (buigPolygon.contains(x, y));
	}

	public void zetVulkleur(Color c) {
		vulkleur = c;
		isGevuld = true;
	}

	public void zetLijnkleur(Color c) {
		lijnkleur = c;
		isOmlijnd = true;
	}

	public Polygon buig(Polygon pol) {
		Polygon pNieuw = new Polygon();
		int aantalP = pol.npoints;
		int aantalPW;
		boolean klaar = false;
		double[] puntenX = new double[aantalP];
		double[] puntenY = new double[aantalP];
		for (int i = 0; i < pol.npoints; i++) {
			puntenX[i] = pol.xpoints[i];
			puntenY[i] = pol.ypoints[i];
		}

		for (int f = 0; f < 10; f++) {
			while (!klaar) {
				aantalPW = 0;
				double[] puntenXW = new double[2 * aantalP];
				double[] puntenYW = new double[2 * aantalP];

				for (int i = 0; i < aantalP; i++) {
					double x0 = puntenX[i];
					double y0 = puntenY[i];
					double x1 = puntenX[(i + 1) % aantalP];
					double y1 = puntenY[(i + 1) % aantalP];
					double x2 = puntenX[(i + 2) % aantalP];
					double y2 = puntenY[(i + 2) % aantalP];
					double x3 = puntenX[(i + 3) % aantalP];
					double y3 = puntenY[(i + 3) % aantalP];

					double xv0 = x1 - x0;
					double yv0 = y1 - y0;
					double xv1 = x2 - x1;
					double yv1 = y2 - y1;
					double xv2 = x3 - x2;
					double yv2 = y3 - y2;

					double uitprodukt1 = xv0 * yv1 - xv1 * yv0;
					double orientatie1;
					if (uitprodukt1 == 0)
						orientatie1 = 0;
					else
						orientatie1 = uitprodukt1 / Math.abs(uitprodukt1);
					double improdukt1 = xv0 * xv1 + yv0 * yv1;
					double norm0 = Math.sqrt((xv0) * (xv0) + (yv0) * (yv0));
					double norm1 = Math.sqrt((xv1) * (xv1) + (yv1) * (yv1));
					double hoekA = 0;
					if (norm0 * norm1 > 0.000010 && Math.abs(improdukt1 / (norm0 * norm1)) < 1)
						hoekA = orientatie1 * Math.acos(improdukt1 / (norm0 * norm1));
					else if (norm0 * norm1 > 0.000010 && improdukt1 / (norm0 * norm1) > 1)
						hoekA = 0;
					else if (norm0 * norm1 > 0.000010 && improdukt1 / (norm0 * norm1) < -1)
						hoekA = Math.PI;
					double uitprodukt2 = xv1 * yv2 - xv2 * yv1;
					double orientatie2;
					if (uitprodukt2 == 0)
						orientatie2 = 0;
					else
						orientatie2 = uitprodukt2 / Math.abs(uitprodukt2);
					double improdukt2 = xv1 * xv2 + yv1 * yv2;
					double norm2 = Math.sqrt((xv2) * (xv2) + (yv2) * (yv2));
					double hoekB = 0;
					if (norm1 * norm2 > 0.000010 && Math.abs(improdukt2 / (norm1 * norm2)) <= 1)
						hoekB = orientatie2 * Math.acos(improdukt2 / (norm1 * norm2));
					else if (norm1 * norm2 > 0.000010 && improdukt2 / (norm1 * norm2) > 1)
						hoekB = 0;
					else if (norm1 * norm2 > 0.000010 && improdukt2 / (norm1 * norm2) < -1)
						hoekB = Math.PI;
					double d = 0.5 * Math.tan((hoekA + hoekB) / 8);
					double xn = (0.5 * (x1 + x2) + yv1 * d);
					double yn = (0.5 * (y1 + y2) - xv1 * d);

					puntenXW[aantalPW] = x1;
					puntenYW[aantalPW] = y1;
					aantalPW++;
					if (Math.sqrt((x1 - xn) * (x1 - xn) + (y1 - yn) * (y1 - yn)) > 2 && Math.sqrt((x2 - xn) * (x2 - xn) + (y2 - yn) * (y2 - yn)) > 2
							&& Math.abs(d * norm1) > 0.5) {
						puntenXW[aantalPW] = xn;
						puntenYW[aantalPW] = yn;
						aantalPW++;
					}
				}
				if (aantalPW > aantalP) {
					puntenX = new double[aantalPW];
					puntenY = new double[aantalPW];

					for (int i = 0; i < aantalPW; i++) {
						puntenX[i] = puntenXW[i];
						puntenY[i] = puntenYW[i];
					}
					aantalP = aantalPW;
				} else
					klaar = true;
			}
		}

		for (int i = 0; i < aantalP; i++) {
			pNieuw.addPoint((int) puntenX[i], (int) puntenY[i]);
		}
		return pNieuw;
	}

	public void zetPolygon() {
		Polygon p = new Polygon();
		for (int j = 0; j < basisPolygon.npoints; j++) {
			p.addPoint(basisPolygon.xpoints[j], basisPolygon.ypoints[j]);
		}
		basisPolygon = p;
	}

	public void verplaats(int dx, int dy) {
		for (int j = 0; j < buigPolygon.npoints; j++) {
			buigPolygon.xpoints[j] += dx;
			buigPolygon.ypoints[j] += dy;
			if (exactePuntenX != null) {
				exactePuntenX[j] += dx;
				exactePuntenY[j] += dy;
			}
		}
	}

	public void schaal(double factor) {
		schaal(factor, factor);
	}

	public void schaal(double factorX, double factorY) {
		if (exactePuntenX == null) {
			exactePuntenX = new double[basisPolygon.npoints];
			exactePuntenY = new double[basisPolygon.npoints];
			for (int j = 0; j < basisPolygon.npoints; j++) {
				exactePuntenX[j] = basisPolygon.xpoints[j];
				exactePuntenY[j] = basisPolygon.ypoints[j];
			}
		}
		for (int j = 0; j < basisPolygon.npoints; j++) {
			exactePuntenX[j] = factorX * exactePuntenX[j];
			exactePuntenY[j] = factorY * exactePuntenY[j];
			basisPolygon.xpoints[j] = ((int) exactePuntenX[j]);
			basisPolygon.ypoints[j] = ((int) exactePuntenY[j]);
		}
		buigPolygon = buig(basisPolygon);
	}

	public void draai(double dh) {
		if (exactePuntenX == null) {
			exactePuntenX = new double[basisPolygon.npoints];
			exactePuntenY = new double[basisPolygon.npoints];
			for (int j = 0; j < basisPolygon.npoints; j++) {
				exactePuntenX[j] = basisPolygon.xpoints[j];
				exactePuntenY[j] = basisPolygon.ypoints[j];
			}
		}
		double cos = Math.cos(dh * Math.PI / 180);
		double sin = Math.sin(dh * Math.PI / 180);

		for (int j = 0; j < basisPolygon.npoints; j++) {
			double x = exactePuntenX[j] - ((Tekening) getParent()).breedte / 2;
			double y = exactePuntenY[j] - ((Tekening) getParent()).hoogte / 2;
			exactePuntenX[j] = cos * x + sin * y + ((Tekening) getParent()).breedte / 2;
			exactePuntenY[j] = cos * y - sin * x + ((Tekening) getParent()).hoogte / 2;
			basisPolygon.xpoints[j] = ((int) exactePuntenX[j]);
			basisPolygon.ypoints[j] = ((int) exactePuntenY[j]);
		}
		buigPolygon = buig(basisPolygon);

	}
}
