package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import nl.uu.fi.dwo.interaction.client.json.ObjectList;
import fi.euclides.event.AddCirkelHandler;
import fi.euclides.event.AddLijnHandler;
import fi.euclides.event.AddPuntHandler;
import fi.euclides.event.AddTriangleHandler2;
import fi.euclides.event.DestroyHandler;
import fi.euclides.event.SelectHandler;
import fi.euclides.swing.AWTViewer;
import fi.euclides.swing.CirkelAction;
import fi.euclides.swing.PuntAction;
import fi.euclides.util.Messages;
import fi.euclides.swing.XXXAction;

public class ToolboxPanel extends JPanel implements ItemListener {

	private JToolBar toolbox;
	AWTViewer viewer;

	ImageIcon getIcon(String name) {
		ImageIcon icon = new ImageIcon(getClass().getResource("/fi/euclides/resources" + name));
		return icon;
	}
	
	JCheckBox createCheckBox(String label, String name) {
		Icon icon = getIcon(name);
		JCheckBox check = new JCheckBox(label, icon);
		check.setSelectedIcon(selectedIcon(icon));
		check.addItemListener(this);
		boxes.add(check);
		return check;
	}
	
	private Icon selectedIcon(final Icon icon) {
		return new Icon() {

			public void paintIcon(Component c, Graphics g, int x, int y) {
				icon.paintIcon(c, g, x, y);
				int width = icon.getIconWidth();
				int height = icon.getIconHeight();
				g.setColor(Color.BLACK);
				g.drawRect(x, y, width, height);
			}

			public int getIconWidth() {
				return icon.getIconWidth();
			}

			public int getIconHeight() {
				return icon.getIconHeight();
			}
			
		};
	}

	List<Action> actions = new ArrayList<Action>();
	List<JCheckBox> boxes = new ArrayList<JCheckBox>();
	
	void createActions() {
		actions.clear();
		actions.add(new XXXAction(Messages.getString("Euclides.35"), "/move.png", new SelectHandler(), viewer));
		actions.add(new PuntAction(Messages.getString("Euclides.46"), "/point.png", new AddPuntHandler(),viewer));
		actions.add(new XXXAction(Messages.getString("Euclides.50"), "/line.png", new AddLijnHandler(AddLijnHandler.LINE),viewer));
		actions.add(new XXXAction(Messages.getString("Euclides.48"), "/segment.png", new AddLijnHandler(AddLijnHandler.SEGMENT),viewer));
		actions.add(new XXXAction("Driehoek", "/triangle.png", new AddTriangleHandler2(), viewer));
		actions.add(new CirkelAction(Messages.getString("Euclides.52"), "/circle.png", new AddCirkelHandler(),viewer));

		actions.add(new XXXAction(Messages.getString("Euclides.37"), "/delete.png", new DestroyHandler(),viewer));
	
	}
	
	ToolboxPanel() {
		super(new BorderLayout());
		setName("Toolbox");
		Box vbox = Box.createVerticalBox();		
		add(new JScrollPane(vbox));

		vbox.add( createCheckBox(Messages.getString("Euclides.35"), "/move.png"));
		vbox.add( createCheckBox(Messages.getString("Euclides.46"), "/point.png"));
		vbox.add( createCheckBox(Messages.getString("Euclides.50"), "/line.png"));
		vbox.add( createCheckBox(Messages.getString("Euclides.48"), "/segment.png"));
		vbox.add( createCheckBox("Driehoek", "/triangle.png"));
		vbox.add( createCheckBox(Messages.getString("Euclides.52"), "/circle.png"));

		vbox.add( createCheckBox(Messages.getString("Euclides.37"), "/delete.png"));
	}

	public JToolBar getToolbox() {
		return toolbox;
	}

	public void setToolbox(JToolBar toolbox) {
		this.toolbox = toolbox;
		createActions();
	}

	boolean hold = false;
	public void itemStateChanged(ItemEvent e) {
		if(!hold)
			insertActions();
	}

	private void insertActions() {
		toolbox.removeAll();
		for(int i = 0; i < actions.size(); i++) {
			if(boxes.get(i).isSelected())
				toolbox.add(actions.get(i));
		}
		toolbox.setVisible(toolbox.getComponentCount()>0);
		toolbox.getParent().repaint();
	}

	List<Integer> toList() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < actions.size(); i++) {
			if(boxes.get(i).isSelected())
				list.add(i);
		}
		return list;
	}
	
	void fromList(ObjectList list) {
		int size = list.size();
		hold = true; // on hold
		try {
		int last = 0;
		for(int i = 0; i < size; i++) {
			int n = list.getInt(i);
			for(; last < n; last ++ )
				boxes.get(last).setSelected(false);
			boxes.get(n).setSelected(true);
			last = n+1;
		} } finally {
			hold = false;
			insertActions();
		}
	}
}
