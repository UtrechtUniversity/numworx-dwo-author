package nl.numworx.geodefiner;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JPanel;

import fi.beans.numworxlf.JOptionPane;
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
			g.fillRoundRect(x, y, getIconWidth(), getIconHeight(),5,5);
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
	static Color colors[]= {
			new Color(0x7fcc99),
			new Color(0x7ddfff),
			new Color(0xff7f7f),
			new Color(0xffe67f),
			new Color(0xc97dff),
			new Color(100,100,100),
			Color.black,
			new Color(180,180,180),
	};
	private ButtonGroup group;
	private Map<String, Map<String, Object>> state;
	
	public ColorHandler(String string, Map<String, Map<String, Object>> state) {
		super(string);
		this.state = state;
		group = new ButtonGroup();
		panel = new JPanel(new GridLayout(2,4,4,4));
		for(int i = 0; i < radios.length; i++) {
			radios[i] = new JRadioButton(null, new ColorIcon(colors[i]));
			radios[i].setText(null);
			radios[i].setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			radios[i].getPreferredSize();
			panel.add(radios[i]);
			group.add(radios[i]);
		}
		radios[0].setSelected(true);
		panel.setMaximumSize(panel.getPreferredSize());
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
				String name = getTracker().getMapper().toString(p);
				Map<String,Object> pstate = state.computeIfAbsent(name, k -> new TreeMap<>());
				pstate.put("color", value.getRGB());
// shortcut
				IsColor iscolor = p.adapt(IsColor.class);
				if (iscolor != null) iscolor.updateColor();
			}
			getModel().clearSelection();
		});
	}

	private Optional<Color> getColor() {
		Component c = getTracker().adapt(Component.class);
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER)); p.add(panel);
		int ok = JOptionPane.showConfirmDialog(c, p, string, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
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
