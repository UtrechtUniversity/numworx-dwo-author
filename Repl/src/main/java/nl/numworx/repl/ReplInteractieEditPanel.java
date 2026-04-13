package nl.numworx.repl;

import java.awt.FlowLayout;
import java.util.Hashtable;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import fi.beans.numworxlf.JCheckBox;
import fi.beans.numworxlf.JComboBox;
import fi.beans.numworxlf.JLabel;
import fi.beans.numworxlf.JTextField;
import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class ReplInteractieEditPanel extends JPanel implements InteractieEditPanel {
	
	JComboBox<String> side;
	JTextField size;
	JCheckBox hasTurtle;

	public ReplInteractieEditPanel() {
		super(new FlowLayout());
		String[] sides = { "north", "south", "east", "west" };
		side = new JComboBox<>(sides);
		size = new JTextField("100%");
		size.setColumns(10);
		hasTurtle = new JCheckBox("gebruik turtle");
		
		Box turtle = Box.createVerticalBox();
		turtle.add(hasTurtle);
		Box line = Box.createHorizontalBox();
		line.add(new JLabel("Kant"));
		line.add(side);
		turtle.add(line);
		line = Box.createHorizontalBox();
		line.add(new JLabel("Maat"));
		line.add(size);
		turtle.add(line);
		TitledBorder title = BorderFactory.createTitledBorder("Turtle canvas");
		turtle.setBorder(title);
		
		add(turtle);
	}

	public Hashtable getEditState() {
		Hashtable launchData = new Hashtable();
		if (hasTurtle.isSelected()) {
			launchData.put("hasTurtle", true);
			launchData.put("side", side.getSelectedItem());
			launchData.put("size", size.getText());
		}
		return launchData;
	}

	public void setEditState(Hashtable h) {
		if (h.containsKey("side")) {
			Object value = h.get("side");
			side.setSelectedItem(value);
			hasTurtle.setSelected(true);
		} else {
			hasTurtle.setSelected(false);
			side.setSelectedIndex(0);
		}
		if (h.containsKey("size")) {
			Object value = h.get("size");
			size.setText(Objects.toString(value, "100%"));
		} else {
			size.setText("100%");
		}
		
	}

	public void start() {
	}

	public void stop() {
	}

	public void zetBreedte(int arg0) {
	}

	public void zetHoogte(int arg0) {
	}

}
