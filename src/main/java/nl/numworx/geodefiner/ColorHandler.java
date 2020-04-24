package nl.numworx.geodefiner;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Optional;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import fi.beans.numworxlf.JRadioButton;
import fi.euclides.event.EventHandler;
import fi.euclides.model.Destroyable;
import fi.euclides.util.DefaultAdapter;

public class ColorHandler extends EventHandler {

	static class ColorIcon implements Icon {
		final Color col;
		ColorIcon(Color c) {this.col=c; }
		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.setColor(col);
			g.fillRect(x, y, getIconWidth(), getIconHeight());
			if ( ((JRadioButton)c).isSelected()) {
				g.setColor(Color.BLACK);
				g.drawRect(x, y, getIconWidth()-1, getIconHeight()-1);
			}
		}
		@Override
		public int getIconWidth() {
			return 32;
		}
		@Override
		public int getIconHeight() {
			return 32;
		}		
	}
	
	
	JPanel panel;
	JRadioButton[] radios = new JRadioButton[8];
	Color colors[]= {
			Color.RED,
			Color.GREEN,
			Color.CYAN,
			Color.BLUE,
			Color.GRAY,
			Color.YELLOW,
			Color.WHITE,
			Color.black
	};
	private ButtonGroup group;
	
	public ColorHandler(String string) {
		super(string);
		group = new ButtonGroup();
		panel = new JPanel(new GridLayout(2, 4));
		for(int i = 0; i < radios.length; i++) {
			radios[i] = new JRadioButton(new ColorIcon(colors[i]));
			panel.add(radios[i]);
			group.add(radios[i]);
		}
		radios[0].setSelected(true);
	}

	@Override
	public boolean allowSelection(Vector selection) {
		return !selection.isEmpty();
	}

	public void command() {
		Vector<Destroyable> selection = getModel().getSelect();
		Optional<Color> get = getColor();
		get.ifPresent(value -> {
			for(Destroyable p: selection) {
				DefaultAdapter.getDefault(p).put(Color.class, value);
			}
			getModel().clearSelection();
		});
	}

	private Optional<Color> getColor() {
		Component c = getTracker().adapt(Component.class);
		int ok = JOptionPane.showConfirmDialog(c, panel, string, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (ok == JOptionPane.OK_OPTION)
		{
			for(int i = 0; i < radios.length; i++) {
				if (radios[i].isSelected())
					return Optional.of(colors[i]);
			}
		}
		return Optional.empty();
	}
	
}
