package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.TransferHandler;

import dagger.Lazy;
import nl.numworx.geodefiner.common.FilteredDestroyHandler;
import nl.numworx.geodefiner.common.AddCirkelHandler;
import nl.numworx.geodefiner.common.AddPolygonHandler;
import nl.numworx.geodefiner.common.AddSnapPuntHandler;
import nl.numworx.geodefiner.common.ResetHandler;
import nl.numworx.geodefiner.common.Tools;
import nl.uu.fi.dwo.interaction.client.JSONUtilities;
import nl.uu.fi.dwo.interaction.client.json.ObjectList;
import fi.euclides.event.AddBissectriceHandler;
import fi.euclides.event.AddBoogHandler;
import fi.euclides.event.AddBoogHandler2;
import fi.euclides.event.AddFocusHandler;
import fi.euclides.event.AddKegelsnedeHandler;
import fi.euclides.event.AddLijnHandler;
import fi.euclides.event.AddLocusHandler;
import fi.euclides.event.AddLoodLijnHandler;
import fi.euclides.event.AddMiddelPuntHandler;
import fi.euclides.event.AddParallelHandler;
import fi.euclides.event.AddPoollijnHandler;
import fi.euclides.event.AddPuntHandler;
import fi.euclides.event.AddRaakLijnHandler;
import fi.euclides.event.AddSpiegelHandler;
import fi.euclides.event.AddTriangleHandler2;
import fi.euclides.event.DestroyHandler;
import fi.euclides.event.SelectHandler;
import fi.euclides.proof.AfstandHandler;
import fi.euclides.proof.HoekHandler;
import fi.euclides.proof.OppHandler;
import fi.euclides.proof.VectorHandler;
import fi.euclides.swing.AWTViewer;
import fi.euclides.swing.CirkelAction;
import fi.euclides.swing.PanHandler;
import fi.euclides.swing.PuntAction;
import fi.euclides.util.Messages;

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

/*			case 19:
				btn = newBtn(url + "/segment.png", new AfstandHandler("lengte"), tracker); break;
			case 20:
				btn = newBtn(url + "/triangle.png", new OppHandler("oppervlakte"), tracker); break;
			case 21:
				btn = newBtn(url + "/angle.png", new HoekHandler("hoek"), tracker); break;
			case 22:
				btn = newBtn(url + "/ray.png", new VectorHandler("vector"), tracker); break;

	*/	
			
		for(int i = 0; i < TOOL_SIZE; i++) {
			Provider<Action> action = actionsMap.get(i);
			vbox.add(createCheckBox(action.get()));
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
				toolbox.add(actionsMap.get(n).get());
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
