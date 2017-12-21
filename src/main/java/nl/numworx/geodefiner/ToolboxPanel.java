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
import java.util.Vector;

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

import nl.numworx.geodefiner.common.FilteredDestroyHandler;
import nl.numworx.geodefiner.common.AddPolygonHandler;
import nl.numworx.geodefiner.common.ResetHandler;
import nl.numworx.geodefiner.common.Tools;
import nl.uu.fi.dwo.interaction.client.JSONUtilities;
import nl.uu.fi.dwo.interaction.client.json.ObjectList;
import fi.euclides.event.AddBissectriceHandler;
import fi.euclides.event.AddBoogHandler;
import fi.euclides.event.AddBoogHandler2;
import fi.euclides.event.AddCirkelHandler;
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
import fi.euclides.expr.TrailHandler;
import fi.euclides.proof.AfstandHandler;
import fi.euclides.proof.HoekHandler;
import fi.euclides.proof.OppHandler;
import fi.euclides.proof.VectorHandler;
import fi.euclides.swing.AWTViewer;
import fi.euclides.swing.CirkelAction;
import fi.euclides.swing.PanHandler;
import fi.euclides.swing.PuntAction;
import fi.euclides.util.Messages;
import fi.euclides.swing.XXXAction;

public class ToolboxPanel extends JPanel implements ItemListener, Tools {

	private JToolBar toolbox;
	AWTViewer viewer;

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

	Vector<Action> actions = new Vector<Action>();
	List<JCheckBox> boxes = new ArrayList<JCheckBox>();
	
	void createActions(Instance instance) {
		actions.clear();
		resetter = new ResetHandler("Reset", instance);
		actions.setSize(TOOL_SIZE);
		actions.set(SELECTOR, new XXXAction(Messages.getString("Euclides.35"), "/move.png", selector, viewer));
		actions.set(POINT, new PuntAction(Messages.getString("Euclides.46"), "/point.png", new AddPuntHandler(),viewer));

		actions.set(LINE, new XXXAction(Messages.getString("Euclides.50"), "/line.png", new AddLijnHandler(AddLijnHandler.LINE),viewer));
		actions.set(HALFLINE, new XXXAction(Messages.getString("Euclides.49"), "/ray.png", new AddLijnHandler(AddLijnHandler.RAY),viewer));
		actions.set(SEGMENT, new XXXAction(Messages.getString("Euclides.48"), "/segment.png", new AddLijnHandler(AddLijnHandler.SEGMENT),viewer));

		actions.set(PERPENDICULAR, new XXXAction(Messages.getString("Euclides.56"), "/plumb.png", new AddLoodLijnHandler(),viewer));
		actions.set(PARALLEL, new XXXAction(Messages.getString("Euclides.58"), "/parallel.png", new AddParallelHandler(),viewer));

		
		XXXAction xaction=new XXXAction(Messages.getString("Euclides.41"), "/pan.png", new PanHandler(Messages.getString("Euclides.41"), viewer), viewer);
		xaction.cursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
		actions.set(PAN, xaction);
		
		actions.set(TRIANGLE, new XXXAction("Veelhoek", "/triangle.png", new AddPolygonHandler("Veelhoek"), viewer));
		actions.set(CIRCLE, new CirkelAction(Messages.getString("Euclides.52"), "/circle.png", new AddCirkelHandler(),viewer));

		actions.set(DESTROY,new XXXAction(Messages.getString("Euclides.37"), "/delete.png", new FilteredDestroyHandler(instance),viewer));

		actions.set(ARC, new XXXAction("Boog", "/angle.png", new AddBoogHandler("Boog"),viewer));
		actions.set(MIDPOINT, new XXXAction(Messages.getString("Euclides.54"), "/midpoint.png", new AddMiddelPuntHandler(),viewer));
		actions.set(BISECTRICE, new XXXAction(Messages.getString("Euclides.60"), "/bissectrice.png", new AddBissectriceHandler(),viewer));
		actions.set(MIRROR, new XXXAction(Messages.getString("Euclides.62"), "/mirror.png", new AddSpiegelHandler(), viewer));
		actions.set(CONIC_SECTION, new XXXAction("Kegelsnede", "/quadric.png", new AddKegelsnedeHandler("Kegelsnede"), viewer));
		actions.set(FOCUS, new XXXAction("Brandpunt", "/quadric.png", new AddFocusHandler(), viewer));
		actions.set(LOCUS, new XXXAction("Meetkundige plaats", "/objecttracker.png", new AddLocusHandler("Meetkundige plaats"), viewer));
		actions.set(TANGENT, new XXXAction("Raaklijn", "/line.png", new AddRaakLijnHandler(), viewer));
		actions.set(POLELINE, new XXXAction("Poollijn", "/line.png", new AddPoollijnHandler(), viewer));

/*			case 19:
				btn = newBtn(url + "/segment.png", new AfstandHandler("lengte"), tracker); break;
			case 20:
				btn = newBtn(url + "/triangle.png", new OppHandler("oppervlakte"), tracker); break;
			case 21:
				btn = newBtn(url + "/angle.png", new HoekHandler("hoek"), tracker); break;
			case 22:
				btn = newBtn(url + "/ray.png", new VectorHandler("vector"), tracker); break;

	*/	
		actions.set(DISTANCE,new XXXAction(Messages.getString("Euclides.88"), "/distance.png", new AfstandHandler(Messages.getString("Euclides.90")), viewer));
		actions.set(AREA,new XXXAction(Messages.getString("Euclides.91"), "/area.png", new OppHandler(Messages.getString("Euclides.93")), viewer));
		actions.set(ANGLE, new XXXAction(Messages.getString("Euclides.85"), "/angle.png", new HoekHandler(Messages.getString("Euclides.85")), viewer));
		actions.set(VECTOR,new XXXAction("Vector", "/ray.png", new VectorHandler("Vector"), viewer));
		
		
		actions.set(FORMULA, new XXXAction("Definitie", "/formuleknop.gif", formule, viewer));
		actions.set(TEXT, new XXXAction("Text", "/text.png", text, viewer));

		actions.set(TRAIL, new TrailAction(Messages.getString("Euclides.44"), viewer)); //$NON-NLS-1$

		actions.set(RESET, new XXXAction("Reset", "/reseticon.gif", resetter, viewer));

		
		
		
		for(Action action: actions) {
			vbox.add(createCheckBox(action));
		}
	}

	class TrailAction extends XXXAction {

		public TrailAction(String name, AWTViewer viewer) {
			super(name, "/thickness2.png", new TrailHandler(name), viewer);
		}
	}
	

	ToolboxPanel() {
		super(new BorderLayout());
		setName("Toolbox");
		vbox = Box.createVerticalBox();		
		add(new JScrollPane(vbox));
	}

	private Component createCheckBox(Action action) {
		Icon icon = (Icon) action.getValue(Action.LARGE_ICON_KEY);
		String name = (String) action.getValue(Action.NAME);
		return createCheckBox(name, icon);
	}

	public JToolBar getToolbox() {
		return toolbox;
	}

	public void setToolbox(JToolBar toolbox, Instance instance) {
		this.toolbox = toolbox;
		createActions(instance);
	}

	boolean hold = false;
	SelectHandler selector = new SelectHandler();
	ResetHandler  resetter;
	FormuleHandler formule = new FormuleHandler("Definitie");
	TextHandler text = new TextHandler("Text");
	
	private Box vbox;
	public void itemStateChanged(ItemEvent e) {
		if(!hold)
			insertActions();
	}

	private void insertActions() {
		toolbox.removeAll();
		for(int i = 0; i < actions.size(); i++) {
			JCheckBox box = ((ToolPanel) vbox.getComponent(i)).getCheck();
			if(box.isSelected())
			{
				int n = boxes.indexOf(box);
				toolbox.add(actions.get(n));
			}
				
		}
		toolbox.setVisible(toolbox.getComponentCount()>0);
		toolbox.getParent().repaint();
	}

	List<Integer> toList() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < actions.size(); i++) {
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
