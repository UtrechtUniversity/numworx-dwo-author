package fi.heks;

import java.awt.*;
import fi.heks.scobjects.*;

public class TabelComponent extends ScComponent {
	private int aantalRijen;
	private int aantalKolommen;
	private boolean afdekbaar;

	public TabelComponent(int aantalR, int aantalK, int x, int y, int b, int h, boolean afdekbaar) {
		super(x, y, b, h);
		this.afdekbaar = afdekbaar;
		aantalRijen = aantalR;
		aantalKolommen = aantalK;
	}

	public void paint(Graphics g) {
		super.paint(g);
		int b = getSize().width - 1;
		int h = getSize().height - 1;

		int afwijkingBoven = (int) (schaal * -10);
		int afwijkingOnder = (int) (schaal * -30);
		g.setColor(new Color(235, 245, 255));
		g.fillRect(0, -afwijkingBoven, b, h + afwijkingOnder + afwijkingBoven);
		g.setColor(Color.black);
		g.drawRect(1, -afwijkingBoven + 1, b - 2, h + afwijkingOnder + afwijkingBoven - 2);

		for (int i = 0; i < aantalRijen + 1; i++) {
			int correctie;
			if (i == 0)
				correctie = -afwijkingBoven;
			else if (i == aantalRijen)
				correctie = afwijkingOnder;
			else
				correctie = 0;
			g.drawLine(0, i * h / aantalRijen + correctie, b, i * h / aantalRijen + correctie);
			if (afdekbaar && i == aantalRijen - 1) {
				g.drawLine(0, i * h / aantalRijen - 1 + correctie, b, i * h / aantalRijen - 1 + correctie);
				g.drawLine(0, i * h / aantalRijen + 1 + correctie, b, i * h / aantalRijen + 1 + correctie);
			}
		}
		for (int i = 0; i < aantalKolommen + 1; i++) {
			g.drawLine(i * b / aantalKolommen, -afwijkingBoven, i * b / aantalKolommen, h + afwijkingOnder);
		}
	}

}
