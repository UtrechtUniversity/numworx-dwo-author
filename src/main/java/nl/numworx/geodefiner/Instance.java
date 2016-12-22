package nl.numworx.geodefiner;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.ToolTipManager;

import nl.numworx.geodefiner.common.Align;
import nl.numworx.geodefiner.common.Check_DWO;
import nl.numworx.geodefiner.common.NamingModel;
import nl.numworx.geodefiner.ui.UIModelFactory;

import org.cbook.cbookif.AssessmentMode;
import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.CBookWidgetInstanceIF;
import org.cbook.cbookif.Constants;
import org.cbook.cbookif.SuccessStatus;

import fi.euclides.event.DescriptionBuilder;
import fi.euclides.event.HitTester;
import fi.euclides.event.NameMapper;
import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.Triangle;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.Const;
import fi.euclides.proof.FlipFlop;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.swing.AWTViewer;
import fi.euclides.swing.HitTester2;
import fi.euclides.util.Adapter;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.wiskopdr.formuleobjects.FormuleVak;


public class Instance extends nl.numworx.geodefiner.common.Instance implements CBookWidgetInstanceIF, CBookEventListener {

	static final Stroke DEFAULT_STROKE = new BasicStroke();

	private HitTester tiptest;
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

		@Override
		public String getToolTipText(MouseEvent e) {
			DescriptionBuilder builder = new DescriptionBuilder(viewer.getMapper());
			tiptest.setVisitor(builder);
			tiptest.setXY(e.getX()-getViewer().offX, e.getY()-getViewer().offY);
			Model r = viewer.getModel();
			r.visitPunten(tiptest);
			r.visitLijnen(tiptest);
			tiptest.done();
			return builder.toString();
		}
		
	};
	
	private JPanel panel = new JPanel(new BorderLayout());
	
	private final class InstanceViewer extends AWTViewer implements Observer {

		private static final float DEFAULT_POINTSIZE = 5f;

		private NamingModel nameMapper;

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
			System.out.println(string);
		}

		private WeakHashMap<String, Destroyable> cache = new WeakHashMap<String,Destroyable>();
		
		@Override
		public NameMapper getMapper() {
			return nameMapper;
		}

		InstanceViewer() {
			super();
			getModel().addObserver(this);
			hitTester = (new HitTester2(content.getFontMetrics(content.getFont())));
			nameMapper = new NamingModel(this, cache);
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
		
		private void formuleLabel(Label label) {
			FormuleVak fv = new FormuleVak();
			String string = "$f" + label.getString() + "@";
			fv.vulVak(string);
			fv.setEditable(false);
			fv.zetMaat();
			Dimension s = fv.getSize();
			int as = fv.ashoogte;
			int x = (int) label.getXd();
			int y = (int) label.getYd();
			Align align = label.adapt(Align.class);
			if(align == null) align = Align.BASE;
			switch(align) {
			case LEFT:  x -= s.width; 
			case RIGHT: y -= s.height/2; break;
			case TOP: y -= s.height;
			case BOTTOM :	x -= s.width/2; break;
			case BASE:  y -= as;
			}
			Graphics fvg = g.create();
			fvg.translate(x, y);
			fvg.clipRect(0, 0, s.width, s.height);
			fv.setLocation(10000, 10000);
			content.add(fv);
			fv.print(fvg);
			content.remove(fv);
			fvg.dispose();

			Rectangle2D.Double rect = 
					new Rectangle2D.Double(x, y, s.getWidth(), s.getHeight());
			DefaultAdapter.getDefault(label).put(Shape.class, rect);
		}
		
		private void visitCheckbox(Label label) {
			JCheckBox checkbox = new JCheckBox(label.adapt(String.class));
// TODO wat is het opschrift van de checkbox.
// bijv.	checkbox.setText(label.getString());
			int x = (int) label.getXd();
			int y = (int) label.getYd();
			checkbox.setSelected(label.getState() != Label.FALSE);
			checkbox.setSize(checkbox.getPreferredSize());
			Graphics g3 = g.create();
			g3.translate(x, y);
			g3.clipRect(0, 0, checkbox.getWidth(), checkbox.getHeight());
			checkbox.print(g3);
			checkbox.setLocation(x, y);
			DefaultAdapter.getDefault(label).put(Shape.class, checkbox.getBounds());
			g3.dispose();
		}
				
		@Override
		public void visitLabel(Label label) {
			selectColor(label);
			String string = label.getString();
			if(label.getRegistered() instanceof FlipFlop) {
				visitCheckbox(label);
				return;
			}
			if(string.contains("$")) {
				formuleLabel(label);
				return;
			}
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

		@Override
		public void visitPunt(Punt punt) {
			Float ps = punt.adapt(Float.class);
			if(ps != null) {
				pointSize = ps.floatValue();
			} else
				pointSize = DEFAULT_POINTSIZE;
			super.visitPunt(punt);
		}

		final Color grayish = new Color(0.125f,0.125f,0.125f,0.125f);
		final Color reddish = new Color(0.5f, 0, 0, 0.125f);

		public void visitTriangle(Triangle t)
		{
			Color c = t.adapt(Color.class);
			if(c != null) 
				g.setColor(c);
			else
			if (getModel().getSelect().contains(t))
				g.setColor(reddish);
			else
				g.setColor(grayish);
			Punt[] depend = (Punt[]) t.getDepend();
			int length = depend.length;
			Path2D path = new Path2D.Double(Path2D.WIND_EVEN_ODD, length);
			path.moveTo(depend[length-1].getXd(), depend[length-1].getYd());
			for (int i = 0; i < length; i++) {
				Punt p = depend[i];
				path.lineTo(p.getXd(), p.getYd());
			}			
			g.fill(path);
			DefaultAdapter.getDefault(t).put(Shape.class, path);
		}

		/* (non-Javadoc)
		 * @see fi.euclides.model.AbstractViewer#visitCirkel(fi.euclides.model.Cirkel)
		 */
		@Override
		public void visitCirkel(Cirkel c) {
			Paint p = c.adapt(Paint.class);
			if (p != null)
			{	g.setPaint(p);
				double d = c.getD();
				fillCircle(c.getX(), c.getY(),d);
			}
			super.visitCirkel(c);
		}

		protected void fillArc(double x, double y, double w, double startAngle, double arcAngle) {
			startAngle *= R_TO_D;
			arcAngle *= R_TO_D;
			Shape s = new Arc2D.Double(x, y, w, w, startAngle, arcAngle, Arc2D.PIE);
			g.fill(s);
		}

		/* (non-Javadoc)
		 * @see fi.euclides.model.AbstractViewer#visitBoog(fi.euclides.model.Boog)
		 */
		@Override
		public void visitBoog(Boog b) {
			Paint p = b.adapt(Paint.class);
			if (p != null)
			{	g.setPaint(p);
				double r = b.getR();
				double s = b.getStart();
				double l = b.length();
				Punt c = b.getCenter();
				fillArc(c.getXd()-r, c.getYd()-r, r*2, s, l);
			}
			super.visitBoog(b);
		}

	
	}

	private CBookEventHandler handler = new CBookEventHandler(this);
	
	{
		viewer = new InstanceViewer();
		definitions = new Definitions(viewer);
		uiModelFactory = new UIModelFactory(viewer);
		tiptest = viewer.getHitTester().copy();
	}
	
	Definitions getDefinitions() {
		return (Definitions) definitions;
	}

	public InstanceViewer getViewer() {
		return (InstanceViewer) viewer;
	}

	private Map<String, Number> random = Collections.emptyMap();

	JToolBar toolbox;
	
	public Instance() {
		
		content.setBackground(Color.white);
		content.setBorder(BorderFactory.createEtchedBorder());
		selector.setTracker(viewer);
		content.addMouseListener(getViewer());
		content.addMouseMotionListener(getViewer());
		ToolTipManager.sharedInstance().registerComponent(content);
		panel.add(content, BorderLayout.CENTER);
		toolbox = new JToolBar();
		toolbox.setFloatable(false);
		toolbox.setVisible(false);
		panel.add(toolbox, BorderLayout.NORTH);
	}

	public void addCBookEventListener(CBookEventListener listener, final String command) {
		handler.addCBookEventListener(listener, command);
		// command is double.NAAM
		if(command != null && command.startsWith("double.")) {
			int dot = command.indexOf('.');
			String name = command.substring(dot+1);
			Destroyable f = viewer.getMapper().fromString(name);
			if(f == null) {
				System.err.println("addCBookEventListener " + command + " not found");
				return; 
			}
			final Label label = (Label) f;
			f.addObserver(new Observer() {

				public void update(Observable observable, Object arg) {
					if(arg == null)
						handler.fire(command, "value", label.value.doubleValue());
				}});
		}
	}

	public JComponent asComponent() {
		return panel;
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
		Boolean status = getStatus();
		if(status == null) return SuccessStatus.UNKNOWN;
		if(status)
			return SuccessStatus.PASSED;
		return SuccessStatus.FAILED;
	}

	// Assume getSize() is okay.
	public void init() {
		panel.doLayout();
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

	}

	public void start() {
		viewer.paint();
	}

	public void stop() {

	}

	public void acceptCBookEvent(CBookEvent ev) {
		if(ev.getCommand().startsWith("double.")) {
			int dot = ev.getCommand().indexOf('.');
			String name = ev.getCommand().substring(dot+1);
			Number number = (Number)ev.getParameter("value");
			if(number == null) {
				number = Double.valueOf(ev.getMessage());
			}
			Label label = (Label) getViewer().getMapper().fromString(name);
			if(label.getSubKey() == Const.TYPE) { 
				label.setValue(Numbers.createDouble(number.doubleValue()));
				label.setString(Numbers.toString(label.value));
				label.notifyObservers();
			}
		}
	}

	@Override
	public String randomize(Map<String, Number> random, String text) {
		Hashtable randomVarWaarden = new Hashtable(random);
		String[] randomVarNamen = random.keySet().toArray(new String[random.size()]);
		Locale lcl = WiskOpdr.language;
		try {
			WiskOpdr.language = Locale.ROOT; // POSIX: decimal point
			return FormuleParser.randomizeString(text,randomVarNamen,randomVarWaarden);
		} catch (Exception e) {
		} finally {
			WiskOpdr.language = lcl;
		}
		return super.randomize(random, text);
	}

	@Override
	protected boolean installCheckDWO() {
		if  (super.installCheckDWO())
		{
			checkDWO.addObserver(this);
			return true;
		}
		checkDWO = new Check_DWO(viewer);
		return false;
	}

	@Override
	public void update(Observable observable, Object arg) {
		super.update(observable, arg);
		if(Constants.CHANGED.equals(arg)) {
			Map<String, ?> parameters = Collections.emptyMap();
			handler.fire(Constants.CHANGED, parameters);
		}
	}
	

	
	
	
}
