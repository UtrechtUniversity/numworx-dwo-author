package nl.numworx.geodefiner;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.WeakHashMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import nl.numworx.geodefiner.common.Align;
import nl.numworx.geodefiner.ui.UIModelFactory;

import org.cbook.cbookif.AssessmentMode;
import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.CBookWidgetInstanceIF;
import org.cbook.cbookif.SuccessStatus;

import fi.euclides.event.NameMapper;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.swing.AWTViewer;
import fi.euclides.swing.HitTester2;
import fi.euclides.util.Adapter;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;


public class Instance extends nl.numworx.geodefiner.common.Instance implements CBookWidgetInstanceIF, CBookEventListener {

	private static final long serialVersionUID = 1L;
	static final Stroke DEFAULT_STROKE = new BasicStroke();

	private final JPanel content = new JPanel() {
		@Override
		protected void paintComponent(Graphics g) {
			getViewer().paint(g);
		}

		@Override
		public void setBounds(int x, int y, int width, int height) {
			super.setBounds(x, y, width, height);
			getViewer().height = height;
			getViewer().width = width;
		}
		
	};
	
	
	private final class InstanceViewer extends AWTViewer implements Observer, NameMapper {

		@Override
		public void paint() {
			content.repaint();
		}

		@Override
		public void paint(Graphics g2) {
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, content.getWidth(), content.getHeight());
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
			hitTester = (new HitTester2(content.getFontMetrics(content.getFont())));
			
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
			int stringWidth = fm.stringWidth(string);
			if(align != null) {
				switch(align) {
				case LEFT: x -= stringWidth+4; 
				case RIGHT: x+=2;	
					y += fm.getAscent()/2; break;
				case TOP: x -= stringWidth/2; y -= fm.getDescent(); break;
				case BOTTOM: x -= stringWidth/2; y += fm.getAscent(); break;
				case BASE: 
				}
			}
			Rectangle2D.Double rect = 
					new Rectangle2D.Double(x, y - fm.getAscent(), stringWidth, fm.getHeight());
			DefaultAdapter.getDefault(label).put(Shape.class, rect);
			drawString(string, x, y);
		}
	}

	private CBookEventHandler handler = new CBookEventHandler(this);
	
	{
		viewer = new InstanceViewer();
		definitions = new Definitions(viewer);
		uiModelFactory = new UIModelFactory();
	}
	
	Definitions getDefinitions() {
		return (Definitions) definitions;
	}

	public InstanceViewer getViewer() {
		return (InstanceViewer) viewer;
	}

	private Map<String, Number> random = Collections.emptyMap();
	
	public Instance() {
		content.setBackground(Color.white);
		content.setBorder(BorderFactory.createEtchedBorder());
		selector.setTracker(viewer);
		content.addMouseListener(getViewer());
		content.addMouseMotionListener(getViewer());
	}

	public void addCBookEventListener(CBookEventListener listener, String command) {
		handler.addCBookEventListener(listener, command);
	}

	public JComponent asComponent() {
		return content;
	}

	public CBookEventListener asEventListener() {
		return this;
	}

	public void destroy() {
		getViewer().getModel().destroy();
	}

	
	public Map<String,?> getState() {
		return getState(new Hashtable<String, Object>());
	}
	
	public SuccessStatus getSuccessStatus() {
		return SuccessStatus.PASSED;
	}

	// Assume getSize() is okay.
	public void init() {
		createModel(viewer.getModel(), content.getWidth(), content.getHeight());
		LabelDelegate.setAllTracker(viewer); // FIXME statics...... singleton considered harmfull!
		getViewer().height = content.getHeight();
		getViewer().width = content.getWidth();
		selector.command();
		definitions.clear();
	}

	public void removeCBookEventListener(CBookEventListener listener, String command) {
		handler.removeCBookEventListener(listener, command);
	}

	public void reset() {
		
	}

	public void setAssessmentMode(AssessmentMode mode) {
		// TODO Auto-generated method stub

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
