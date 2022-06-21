package nl.numworx.geodefiner;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Stroke;
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
import nl.numworx.geodefiner.common.LineType;
import nl.numworx.geodefiner.ui.LineModel;

public class DashHandler extends EventHandler {

	static Stroke solid = LineModel.getStroke(LineType.SOLID);
	
	static class DashIcon implements Icon {
		final LineType col;
		final Stroke stroke;
		DashIcon(LineType c) {
			this.col=c; 
			stroke = LineModel.getStroke(c,3);}
		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.setColor(c.getForeground());
			((Graphics2D) g).setStroke(stroke);
			g.drawLine(x, y+getIconHeight()/2, x+getIconWidth(), y+getIconHeight()/2);
			if ( ((JRadioButton)c).isSelected()) {
				g.setColor(Color.BLACK);
				((Graphics2D) g).setStroke(solid);
				g.drawRect(x, y, getIconWidth()-1, getIconHeight()-1);
			}
		}
		@Override
		public int getIconWidth() {
			return 64;
		}
		@Override
		public int getIconHeight() {
			return 32;
		}		
	}
	

	JPanel panel;
	JRadioButton[] radios = new JRadioButton[LineType.values().length];
	
	private Map<String, Map<String, Object>> state;
	private ButtonGroup group;
	public DashHandler(String string, Map<String, Map<String, Object>> state) {
		super(string);
		this.state = state;
		group = new ButtonGroup();
		panel = new JPanel(new GridLayout(radios.length,1,4,4));
		for(int i = 0; i < radios.length; i++) {
			radios[i] = new JRadioButton(null, new DashIcon(LineType.values()[i]));
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
		Optional<LineType> get = getDash();
		get.ifPresent(value -> {
			for(Destroyable p: selection) {
				DefaultAdapter.getDefault(p).put(Stroke.class, LineModel.getStroke(value));
				String name = getTracker().getMapper().toString(p);
				Map<String,Object> pstate = state.computeIfAbsent(name, k -> new TreeMap<>());
				pstate.put("type", value.name());
				IsLineType linetype = p.adapt(IsLineType.class);
				if (linetype != null) linetype.updateLineType();
			}
			getModel().clearSelection();
		});
	}

	private Optional<LineType> getDash() {
		Component c = getTracker().adapt(Component.class);
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER)); p.add(panel);
		int ok = JOptionPane.showConfirmDialog(c, p, string, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (ok == JOptionPane.OK_OPTION)
		{
			for(int i = 0; i < radios.length; i++) {
				if (radios[i].isSelected())
					return Optional.of(LineType.values()[i]);
			}
		}
		return Optional.empty();
	}

}
