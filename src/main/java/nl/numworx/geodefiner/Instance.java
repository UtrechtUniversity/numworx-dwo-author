package nl.numworx.geodefiner;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Stroke;
import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.WeakHashMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import fi.euclides.event.NameMapper;
import fi.euclides.event.SelectHandler;
import fi.euclides.expr.Coord;
import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.model.Destroyable;
import fi.euclides.model.HorizontalPunt;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.algo.FreePoint;
import fi.euclides.model.math.Numbers;
import fi.euclides.persist.Memento;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.swing.AWTViewer;
import fi.euclides.util.Adapter;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;
import nl.numworx.geodefiner.common.Align;
import nl.numworx.geodefiner.common.CELL;
import nl.numworx.geodefiner.common.Grid;
import nl.numworx.geodefiner.ui.UIEditor;
import nl.numworx.geodefiner.common.UIModel;
import nl.numworx.geodefiner.ui.UIModelFactory;
import nl.tue.win.riaca.openmath.lang.OMObject;

import org.cbook.cbookif.AssessmentMode;
import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.CBookWidgetInstanceIF;
import org.cbook.cbookif.SuccessStatus;


class Instance extends JPanel implements CBookWidgetInstanceIF, CBookEventListener {

	private static final long serialVersionUID = 1L;
	static final Stroke DEFAULT_STROKE = new BasicStroke();

	private final class InstanceViewer extends AWTViewer implements Observer, NameMapper {

		@Override
		public void paint() {
			repaint();
		}

		@Override
		public void paint(Graphics g2) {
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, getWidth(), getHeight());
			super.paint(g2);
		}

		@Override
		public void setModel(Model model) { // Never null!
			Model old = getModel();
			old.deleteObserver(this);
			cache.clear();
			super.setModel(model);
			model.addObserver(this);
		}

		public void update(Observable observable, Object arg) {
			if(observable == getModel())
				paint();
		}

		@Override
		public void setStatus(String string) {
			// aanwijzingen...
		}

		private WeakHashMap<String, Destroyable> cache = new WeakHashMap<String,Destroyable>();
		
		public Destroyable fromString(String name) {
			Destroyable item = cache.get(name);
			if(item != null && toString(item).equals(name) && item.getIndex() > 0) return item; // cache hit		
			
			ArrayList<Destroyable> v = new ArrayList<Destroyable>(getModel().getPunten());
			v.addAll(getModel().getLijnen());
			for (Destroyable p : v) {
				if (name .equals( toString(p)) ) {
					cache.put(name, p);
					return p;
				}
			}
			return null;
		}

		public Punt getO() {
			return getModel().getO();
		}

		public Punt getU() {
			return getModel().getU();
		}

		public void rename(Destroyable p, String name) {
			DefaultAdapter.getDefault(p).put(name);
			cache.put(name,p);
		}

		@Override
		public String toString(Destroyable d) {
			String s = d.getAdapter().adapt(String.class);
			if(s == null)
				return getModel().toString(d);
			return s;
		}

		@Override
		public NameMapper getMapper() {
			return this;
		}

		InstanceViewer() {
			super();
			getModel().addObserver(this);
		}

		@Override
		public void selectColor(Destroyable object) {
			if(tracking || trail)
				return;
			Adapter a = object.getAdapter();
			Stroke stroke = a.adapt(Stroke.class);
			if(stroke == null) stroke = DEFAULT_STROKE;
			g.setStroke(stroke);

			Color c = a.adapt(Color.class);
			if (c != null) {
				if (getModel().getSelect().contains(object))
					g.setColor(c.brighter());				
				else
					g.setColor(c);
				return;
			}
			super.selectColor(object);
		}

		@Override
		public void visitLabel(Label label) {
			selectColor(label);
			String string = label.getString();
			FontMetrics fm = g.getFontMetrics();
			double x = label.getXd();
			double y = label.getYd();
			Align align = label.adapt(Align.class);
			if(align != null) {
				switch(align) {
				case LEFT: x -= fm.stringWidth(string)+4; 
				case RIGHT: x+=2;	
					y += fm.getAscent()/2; break;
				case TOP: x -= fm.stringWidth(string)/2; y -= fm.getDescent(); break;
				case BOTTOM: x -= fm.stringWidth(string)/2; y += fm.getAscent(); break;
				case BASE: 
				}
			}
			drawString(string, x, y);
		}
	
	
	
	
	}

	private CBookEventHandler handler = new CBookEventHandler(this);
	
	private final AWTViewer viewer = new InstanceViewer();
	private final SelectHandler selector = new SelectHandler();
	private final Definitions definitions = new Definitions(viewer);

	Definitions getDefinitions() {
		return definitions;
	}

	@Override
	protected void paintComponent(Graphics g) {
		viewer.paint(g);
	}

	public AWTViewer getViewer() {
		return viewer;
	}

	private Map<String, ? extends Object> launchData = Collections.emptyMap();

	private Map<String, ?> state = Collections.emptyMap();

	private Map<String, Number> random = Collections.emptyMap();
	
	public Instance() {
		setBackground(Color.white);
		setBorder(BorderFactory.createEtchedBorder());
		selector.setTracker(viewer);
		addMouseListener(viewer);
		addMouseMotionListener(viewer);
	}

	public void addCBookEventListener(CBookEventListener listener, String command) {
		handler.addCBookEventListener(listener, command);
	}

	public JComponent asComponent() {
		return this;
	}

	public CBookEventListener asEventListener() {
		return this;
	}

	public void destroy() {
		getViewer().getModel().destroy();
	}

	public int getScore() {
		return 0;
	}

	public Map<String, ?> getState() {
		Map<String, Object> map = new Hashtable<String,Object>(state);
		Map<String, List<Number>> positions = new Hashtable<String, List<Number>>();
		Vector<Punt> punten = viewer.getModel().getPunten();
		for (Iterator<Punt> iterator = punten.iterator(); iterator.hasNext();) {
			Punt punt = iterator.next();
			if(punt.adapt(FreePoint.class) != null) {
				try {
					NumberIO io = new NumberIO();
					punt.getX().writeNumber(io);
					punt.getY().writeNumber(io);
					positions.put(viewer.toString(punt), io.toList());
				} catch (IOException e) {
				}
			}
		}
		map.put("positions", positions);
		return map;
	}

	public SuccessStatus getSuccessStatus() {
		return SuccessStatus.PASSED;
	}

	// Assume getSize() is okay.
	public void init() {
		Model model = createModel();
		viewer.setModel(model);
		LabelDelegate.setAllTracker(viewer); // FIXME statics...... singleton considered harmfull!
		new Coord(Coord.xKey).setTracker(viewer);
		new Coord(Coord.yKey).setTracker(viewer);
		viewer.height = getHeight();
		viewer.width = getWidth();
		selector.command();
		definitions.clear();
	}

	private Model createModel() {
		Model m = new Model();
		int mx = getWidth()/2;
		int my = getHeight()/2;
		Punt O = m.buildPunt(Numbers.createInteger(mx), Numbers.createInteger(my));
		DefaultAdapter.getDefault(O).put("O");
		Punt U = new HorizontalPunt(Numbers.createInteger(mx+50), O.getX(), O);
		DefaultAdapter.getDefault(U).put("U");
		m.add(U);
		Vector<Destroyable> select = m.getSelect();
		select.add(U);
		select.add(O);
		Lijn xas = m.buildLijn();
		DefaultAdapter.getDefault(xas).put("x");
		select.add(O);
		select.add(xas);
		Lijn yas = m.buildLoodlijn();
		DefaultAdapter.getDefault(yas).put("y");
		
		Grid grid = new Grid(getViewer());
		DefaultAdapter.getDefault(grid).put("$#@");
		m.add(grid);
		return m;
	}

	public void removeCBookEventListener(CBookEventListener listener, String command) {
		handler.removeCBookEventListener(listener, command);
	}

	public void reset() {
		
	}

	public void setAssessmentMode(AssessmentMode mode) {
		// TODO Auto-generated method stub

	}

	public void setLaunchData(Map<String, ? extends Object> launchData, Map<String, Number> random) {
		this.launchData = launchData;
		this.random = random;		
		createDefinitions();
		installAxes();
		installConfiguration();
		installPositions();
	}

	private void installPositions() {
		setPositions(launchData.get("positions"));
	}

	private void installConfiguration() {
		Map<String, Map<String,Object>> configuration = (Map<String, Map<String, Object>>) this.launchData.get("configuration");
		install(configuration);
	}
	private void installAxes() {
		Map<String, Map<String,Object>> configuration = (Map<String, Map<String, Object>>) this.launchData.get("axes");
		install(configuration);
	}

	private void install(Map<String, Map<String, Object>> configuration) {
		if(configuration != null) {
			for( Map.Entry<String, Map<String,Object>> entry : configuration.entrySet()) {
				String name = entry.getKey();
				Destroyable d = getViewer().getMapper().fromString(name);
				UIModel<?, UIEditor> model = new UIModelFactory().build(d);
				model.fromMap(entry.getValue());
				model.install();
				CELL cell = d.adapt(CELL.class);
				if(cell == null) {
					cell = new CELL("$f@",d);
					DefaultAdapter.getDefault(d).put(cell);
				}
				cell.config = model;
				definitions.update(cell);
			}
		}
	}
	
	private void createDefinitions() {
		@SuppressWarnings("unchecked")
		List<String> strings = (List<String>) this.launchData.get("definitions");
		if(strings != null)
		for (Iterator<String> iterator = strings.iterator(); iterator.hasNext();) {
			String text = iterator.next();
			OMObject object;
			try {
				String toParse = randomize(this.random, text);
				object = new FormuleParser(text.substring(2)).parse();
				definitions.define(text, object);
			} catch (Throwable e) {
				break;
			}
		}
	}

	static String randomize(Map<String, Number> random, String text) {
		for(Map.Entry<String, Number> entry: random.entrySet()) {
			String key = "#" + entry.getKey() + "#";
			text = text.replaceAll(key, entry.getValue().toString());
		}
		return text;
	}

	public void setState(Map<String, ?> state) {
		this.state = state;
		setPositions(state.get("positions"));
	}

	void setPositions(Object object) {
		if(object instanceof Map) {
			Map<String,List<Number>> positions = (Map<String, List<Number>>) object;
			List<Punt> punten = viewer.getModel().getPunten();
			for (Iterator<Punt> iterator = punten.iterator(); iterator.hasNext();) {
				Punt punt = iterator.next();
				String name = viewer.toString(punt);
				FreePoint fp = punt.adapt(FreePoint.class);
				if(positions.containsKey(name) && fp != null) {
					try {
						List<Number> n = positions.get(name);
						DataInput in = new NumberIO(n);
						Numbers x = Memento.readNumber(in);
						Numbers y = Memento.readNumber(in);
						fp.setXY(x, y);
					} catch (IOException e) {
						// should not happen!
					}
				}
			}
		}
	}

	public void start() {
		viewer.paint();
	}

	public void stop() {
		// TODO Auto-generated method stub

	}

	public void acceptCBookEvent(CBookEvent ev) {
		// TODO Auto-generated method stub
		
	}

}
