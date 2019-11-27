package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import fi.beans.numworxlf.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import fi.beans.numworxlf.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.TransferHandler;
import nl.numworx.geodefiner.common.Tools;
import nl.uu.fi.dwo.interaction.client.json.ObjectList;

@SuppressWarnings("serial")
public class ToolboxPanel extends JPanel implements ItemListener, Tools {

	private JToolBar toolbox;
	ImageIcon getIcon(String name) {
		ImageIcon icon = new ImageIcon(getClass().getResource("/fi/euclides/resources" + name));
		return icon;
	}
	
	public class ToolPanel extends JPanel {
		ToolPanel(String name) {
			super(new FlowLayout(FlowLayout.LEADING,0,0));
			setBackground(Color.white);
			setName(name);
			setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 10));
			TransferHandler newHandler = new TransferHandler("pos");
			this.setTransferHandler(newHandler);
			addMouseListener(new MouseAdapter(){
				            public void mousePressed(MouseEvent e){
				                ToolPanel  button = (ToolPanel)e.getSource();
				                TransferHandler handle = button.getTransferHandler();
				                handle.exportAsDrag(button, e, TransferHandler.COPY);
				            }
				        });
		}

		public String getPos() {
			return getName();
		}
		public void setPos(String var) {
			System.out.println("setPos " + var);
			if(var.equals(getPos())) return;
			Container parent = getParent();
			int cnt = parent.getComponentCount();
			int s = cnt, d = 0;
			Component c = null;
			for(int i = 0; i < cnt; i++) {
				ToolPanel item = (ToolPanel) parent.getComponent(i);
				if(item == this) s = i;
				if(item.getPos().equals(var)) { c = item; d = i; }
			}
			parent.remove(d);
			parent.add(c, s);
			parent.invalidate();
			parent.validate();
			parent.repaint();
			if(!hold)
				insertActions();

		}

		public JCheckBox getCheck() {
			return (JCheckBox) getComponent(0);
		}
	}
	
	JComponent createCheckBox(String label, Icon icon) {
		JCheckBox check = new JCheckBox(label, icon);
		check.setSelectedIcon(selectedIcon(icon));
		check.addItemListener(this);
		check.setBackground(Color.white);
		boxes.add(check);
		ToolPanel panel = new ToolPanel(label);
		panel.add(check);
		return panel;
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

	List<JCheckBox> boxes = new ArrayList<JCheckBox>();
	private Map<Integer, Provider<Action>> actionsMap;
	
	void createActions(nl.numworx.geodefiner.common.Instance instance) {
			
		for(int i = 0; i < TOOL_SIZE; i++) {
			Provider<Action> action = actionsMap.get(i);
			vbox.add(createCheckBox(action.get()));
		}
		if (!GeoDefiner.isExperimental) 
		{
		  int[] experimental = { FOCUS, FORMULA, TANGENT };
		  for(int i: experimental) {
		    vbox.getComponent(i).hide();
		  }
		}
	}

	private ToolboxPanel() {
		super(new BorderLayout());
		setName("Toolbox");
		vbox = Box.createVerticalBox();		
		add(new JScrollPane(vbox));
	}

	@Inject ToolboxPanel(nl.numworx.geodefiner.common.Instance instance, JToolBar toolbox, Map<Integer, Provider<Action>> actions) {
		this();
		this.toolbox = toolbox;
		this.actionsMap = actions;
		createActions(instance);
	}
	
	
	private Component createCheckBox(Action action) {
		Icon icon = (Icon) action.getValue(Action.LARGE_ICON_KEY);
		String name = (String) action.getValue(Action.NAME);
		return createCheckBox(name, icon);
	}

	public JToolBar getToolbox() {
		return toolbox;
	}
	
	
	boolean hold = false;
	
	private Box vbox;
	public void itemStateChanged(ItemEvent e) {
		if(!hold)
			insertActions();
	}

	private void insertActions() {
		toolbox.removeAll();
		for(int i = 0; i < TOOL_SIZE; i++) {
			JCheckBox box = ((ToolPanel) vbox.getComponent(i)).getCheck();
			if(box.isSelected())
			{
				int n = boxes.indexOf(box);
				javax.swing.JButton b = toolbox.add(actionsMap.get(n).get());
//				Border border = b.getBorder();
//				Dimension size = b.getPreferredSize();
// XXX opvullen tot 40x40, werkt niet op de MAC
//				Border outsideBorder = BorderFactory.createEmptyBorder(0, 0, 40-size.height, 40-size.width);
//				if (border != null) 
//					b.setBorder(BorderFactory.createCompoundBorder(border, outsideBorder));
//				else
//					b.setBorder(outsideBorder);
			}
				
		}
		toolbox.setVisible(toolbox.getComponentCount()>0);
		toolbox.getParent().repaint();
	}

	List<Integer> toList() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < TOOL_SIZE; i++) {
			JCheckBox box = ((ToolPanel) vbox.getComponent(i)).getCheck();
			if(box.isSelected())
				list.add(boxes.indexOf(box));
		}
		return list;
	}
	
	void fromList(ObjectList list) {
		int size = list == null ? 0 : list.size();
		hold = true; // on hold
		int nn[] = new int[size];
		try {
		for(JCheckBox box: boxes) box.setSelected(false);
		for(int i = 0; i < size; i++) {
			int n = list.getInt(i);
			nn[i] = n;
			boxes.get(n).setSelected(true);
		} } finally {
			Component[] cc = vbox.getComponents();
			vbox.removeAll();
			Arrays.sort(cc, new Comparator<Component>() {

				@Override
				public int compare(Component o1, Component o2) {
					if(o1 == o2) return 0;
					int a = boxes.indexOf(((ToolPanel)o1).getCheck());
					int b = boxes.indexOf(((ToolPanel)o2).getCheck());
					if(a==b) return 0;
					if(a>b) return +1;
					return -1;
				}});
			for(int i = 0; i < size; i++) {
				vbox.add(cc[nn[i]]);
				cc[nn[i]]=null;
			}
			for(int i = 0; i < cc.length; i++) {
				if (cc[i]!= null) vbox.add(cc[i]);
			}
			
			
			hold = false;
			insertActions();
		}
	}
}
