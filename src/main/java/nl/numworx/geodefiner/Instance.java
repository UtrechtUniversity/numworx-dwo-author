package nl.numworx.geodefiner;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.ToolTipManager;

import nl.numworx.geodefiner.common.Align;
import nl.numworx.geodefiner.common.CELL;
import nl.numworx.geodefiner.common.Check_DWO;
import nl.numworx.geodefiner.common.Integral;
import nl.numworx.geodefiner.common.Interval;
import nl.numworx.geodefiner.common.NamingModel;
import nl.numworx.geodefiner.common.Randomizer;
import nl.numworx.geodefiner.common.ShortSegment;
import nl.numworx.geodefiner.common.Tips;
import nl.numworx.geodefiner.ui.AxesModel;
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
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Locus;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.SegmentVisitor;
import fi.euclides.model.Triangle;
import fi.euclides.model.math.Numbers;
import fi.euclides.openmath.Expression;
import fi.euclides.proof.Const;
import fi.euclides.proof.FlipFlop;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.swing.AWTViewer;
import fi.euclides.swing.HitTester2;
import fi.euclides.util.Adaptee;
import fi.euclides.util.Adapter;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.wiskopdr.formuleobjects.FormuleVak;


public class Instance extends nl.numworx.geodefiner.common.Instance implements CBookWidgetInstanceIF, CBookEventListener, PropertyChangeListener, Randomizer {

	public class KijkNaAction extends AbstractAction implements Icon, Observer {

		ImageIcon goed, half, fout, current;
		
		public KijkNaAction() {
			super(Messages.getString("kijkNa"));
			putValue(LARGE_ICON_KEY, this);
			fout = new ImageIcon(getClass().getResource("resources/foutkruis.gif"));
			half = new ImageIcon(getClass().getResource("resources/goedkrulhalf.gif"));
			goed = new ImageIcon(getClass().getResource("resources/goedkrul.gif"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			fetchScore();
			nagekeken = true;
			feedback();
			handler.fire(Constants.CHECKED); // Score changed
		}

		void feedback() {
			Boolean status = getStatus();
			if(status == null) putValue(LARGE_ICON_KEY, current = half);
			else if(status.booleanValue())
				putValue(LARGE_ICON_KEY, current = goed);
			else putValue(LARGE_ICON_KEY, current = fout);
		}


		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			if(current != null) current.paintIcon(c, g, x, y);
		}

		@Override
		public int getIconWidth() {
			return goed.getIconWidth();
		}

		@Override
		public int getIconHeight() {
			return goed.getIconHeight();
		}


		@Override
		public void update(Observable observable, Object arg) {
			current = null;
			putValue(LARGE_ICON_KEY, this);
		}

	}

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
			DescriptionBuilder builder = new DescriptionBuilder(viewer.getMapper()) {

				@Override
				public void visitTriangle(Triangle t) {
					visitDestroyable(t);
				}

				private void visitDestroyable(Destroyable k) {
					String name = toString(k);
					if(name.startsWith("%")) return;
					if(!string.isEmpty()) string += ", ";
					string += name;
				}

				@Override
				public void visitKegelsnede(Kegelsnede2 k) {
					visitDestroyable(k);
				}

				@Override
				public void visitLocus(Locus locus) {
					visitDestroyable(locus);
				}

				@Override
				public void visitBoog(Boog b) {
					visitDestroyable(b);
				}

				@Override
				public void visitCirkel(Cirkel c) {
					visitDestroyable(c);
				}

				@Override
				public void visitLabel(Label label) {
					//visitDestroyable(label);
				}

				@Override
				public void visitLijn(Lijn l) {
					visitDestroyable(l);
				}

				@Override
				public void visitPunt(Punt p) {
					visitDestroyable(p);
				}

				@Override
				public void visitSegment(Segment s) {
					visitDestroyable(s);
				}
				
				
				
			};
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
	JPanel south = new JPanel(new FlowLayout(FlowLayout.TRAILING, 2, 2));
	JButton checkBtn = new JButton(Messages.getString("kijkNa"));
	JLabel  checkLabel = new JLabel();

	public final class Snapper extends nl.numworx.geodefiner.common.Snapper {
		private final int SNAP = 3;

		public boolean isGravity() {
			return gravity;
		}
		
		public void translate(MouseEvent ev) {
			if (gravity) {
				int ox = (int) viewer.getModel().getO().getXd();
				int dx = (int) viewer.getModel().getU().getXd() - ox;
				//System.out.print(ev.getX() + " " + ox + " " + dx);
				int x = (ev.getX()-ox) % dx;
				if ( x < 0 ) x += dx;
				if ( x*2 > dx) x -= dx;
				//System.out.println(" " + x);
				if(x > SNAP || x < -SNAP) x = 0;

				int oy = (int) viewer.getModel().getO().getYd();
				int dy = dx;
				//System.out.print(ev.getX() + " " + ox + " " + dx);
				int y = (ev.getY()-oy) % dy;
				if ( y < 0 ) y += dy;
				if ( y*2 > dy) y -= dy;
				//System.out.println(" " + x);
				if(y > SNAP || y < -SNAP) y = 0;
				ev.translatePoint(-x, -y);
			}
// Keep mouse inside panel
			{
				int x = ev.getX();
				if (x < 0) ev.translatePoint(-x, 0);
				else if (x > getViewer().width) {
					ev.translatePoint(getViewer().width-x, 0);
				}
			}
			{
				int y = ev.getY();
				if (y < 0) ev.translatePoint(0, -y);
				else if (y > getViewer().height) {
					ev.translatePoint(0, getViewer().height-y);
				}
			}
		}
		
	}

	final class InstanceViewer extends AWTViewer implements Observer {

		private static final float DEFAULT_POINTSIZE = 5f;

		private NamingModel nameMapper;
		private Snapper snapper = new Snapper();
		Expression expression; 

		@Override
		public void paint() {
			content.repaint();
		}

		@Override
		public <T> T adapt(Class<T> cls) {
			if(cls == Snapper.class) return (T) snapper;
			if(cls == Expression.class) return (T) expression;
			if(cls == Randomizer.class) return (T) randomizer;
			return super.adapt(cls);
		}

		@Override
		public void paint(Graphics g2) {
			if(content.isOpaque()) {
				g2.setColor(content.getBackground());
				g2.fillRect(0, 0, content.getWidth(), content.getHeight());
			}
			super.paint(g2);
		}

		@Override
		public void drawAxes() {
			Destroyable grid = getModel().getLijnen().elementAt(2);
			if(grid.isVisible()) grid.visit(this);
// draw grid
			Lijn x = (Lijn) getModel().getLijnen().firstElement();
			if(grid.isVisible() && !x.isVisible() && x.isDefined()) {
				// draw x in grid mode
				ll.setLijn(x);
				drawLine(ll.getX1(), ll.getY1() , ll.getX2(), ll.getY2());		
			}
			Lijn y = (Lijn) getModel().getLijnen().elementAt(1);
			if(grid.isVisible() && !y.isVisible() && y.isDefined()) {
				// draw x in grid mode
				ll.setLijn(y);
				drawLine(ll.getX1(), ll.getY1() , ll.getX2(), ll.getY2());		
			}

			CELL item = x.adapt(CELL.class);
			boolean bx = false, by = false;
			if (item != null) {
				AxesModel configX = (AxesModel) item.config;
				bx = configX != null && configX.numbers && x.isVisible();
				if(bx) { drawXnumbers(); }
			}
			item = y.adapt(CELL.class);
			if (item != null) {
				AxesModel configY = (AxesModel) item.config;
				by = configY != null && configY.numbers && y.isVisible();
				if(by) { drawYnumbers(); }
			}
			if (bx || by) drawO();
		}

		private void drawO() {
			FontMetrics fm = g.getFontMetrics();
			double x, y;
			x = getModel().getO().getXd();
			y = getModel().getO().getYd();
			String O = "0";
			x -= fm.stringWidth(O)+1;
			y += fm.getAscent();
			g.setColor(Color.BLACK);
			drawString(O, x, y);	
		}

		private void drawXnumbers() {
			Rectangle r = new Rectangle();
			double left = clipLeft().doubleValue();
			double right = clipRight().doubleValue();
			double x = getModel().getO().getXd();
			double y = getModel().getO().getYd();
			FontMetrics fm = g.getFontMetrics();
			y += fm.getAscent();
			double dx = getModel().getU().getXd() - x;
			if(dx <= 1) return;
			int i = 0, s = 1;
			while(dx < 20) { dx += dx; s+=s; if(dx >= 20) break; dx = 2.5*dx; s += s+s/2; if(dx >= 20) break; dx += dx; s += s; }
			left -= dx;i=s;
			for(double xr = x+dx ; xr < right; xr += dx, i+=s) {
				String value = String.valueOf(i);
				int w = fm.stringWidth(value);
				r.width = w+2;
				r.height = fm.getAscent()+2;
				r.x = (int) xr-1-r.width/2;
				r.y = ((int)y)-r.height+1;
				g.setColor(content.getBackground());
				g.fill(r);
				g.setColor(Color.black);
				g.drawString(value, r.x+1, (int)y);
			}
			i = -s;
			for(double xr = x-dx ; xr > left; xr -= dx, i-=s) {
				String value = String.valueOf(i);
				int w = fm.stringWidth(value);
				r.width = w+2;
				r.height = fm.getAscent()+2;
				r.x = (int) xr-1-r.width/2;
				r.y = ((int)y)-r.height+1;
				g.setColor(content.getBackground());
				g.fill(r);
				g.setColor(Color.black);
				g.drawString(value, r.x+1, (int)y);
			}
		}

		private void drawYnumbers() {
			Rectangle r = new Rectangle();
			double bottom = clipBottom().doubleValue();
			double top = clipTop().doubleValue();
			double x = getModel().getO().getXd();
			double y = getModel().getO().getYd();
			double dy = getModel().getU().getXd() - x;
			if (dy <= 1) return;
			int i = 0, s = 1;
			while(dy < 20) { dy += dy; s+=s; if(dy >= 20) break; dy = 2.5*dy; s += s+s/2; if(dy >= 20) break; dy += dy; s += s; }
			FontMetrics fm = g.getFontMetrics();
			i=s;
			for(double yr = y+fm.getAscent()/2 -dy ; yr > top; yr -= dy, i+=s) {
				String value = String.valueOf(i);
				int w = fm.stringWidth(value);
				r.width = w+2;
				r.height = fm.getAscent()+2;
				r.x = (int) x-1-r.width;
				r.y = ((int)yr)-r.height+1;
				g.setColor(content.getBackground());
				g.fill(r);
				g.setColor(Color.black);
				g.drawString(value, r.x+1, (int)yr);
			}
			i = -s;
			bottom += dy;
			for(double yr = y+dy+fm.getAscent()/2 ; yr < bottom; yr += dy, i-=s) {
				String value = String.valueOf(i);
				int w = fm.stringWidth(value);
				r.width = w+2;
				r.height = fm.getAscent()+2;
				r.x = (int) x-1-r.width;
				r.y = ((int)yr)-r.height+1;
				g.setColor(content.getBackground());
				g.fill(r);
				g.setColor(Color.black);
				g.drawString(value, r.x+1, (int)yr);
			}
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
		public void mouseReleased(MouseEvent e) {
			snapper.translate(e);
			super.mouseReleased(e);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			snapper.translate(e);
			super.mouseDragged(e);
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
					g.setColor(c.brighter());	// grijs wordt wit...			
				else
					g.setColor(c);
				return;
			}
			super.selectColor(object);
		}
		
		
		
		
		@Override
		public void visitSegment(Segment s) {
			s = drawTips(s);
			super.visitSegment(s);
		}

		
		
		private Segment drawTips(Segment s) {
			Tips tip = s.adapt(Tips.class);
			if(tip == null) return s;
			selectColor(s);
			double dx = s.getDX();
			double dy = s.getDY();
			double len = Math.hypot(dx, dy);
			Float width = s.adapt(Float.class);
			double tiplen = 5;
			if(width != null) tiplen *= width.doubleValue();
			if(len < tiplen*3) tiplen = len/3;
			dx *= tiplen / len; 
			dy *= tiplen/len; 
			switch(tip) {
			case ATEND: tip(s.getP2(), -dx, -dy); break;
			case ATSTARTEND: tip(s.getP2(),-(dx), -(dy));
			case ATSTART: tip(s.getP1(), dx, dy); break;
			case NOTIP: return s;
			}
			return new ShortSegment(s, dx, dy, tip);
		}

		private void tip(Punt p1, double dx, double dy) {
			Path2D.Double path = new Path2D.Double();
			double x = p1.getXd();
			double y = p1.getYd();
			path.moveTo(x, y);
			path.lineTo(x + dx + dy/2, y + dy -dx/2);
			path.lineTo(x + dx - dy/2, y + dy +dx/2);
			path.closePath();
			g.fill(path);
			
		}

		private void formuleLabel(Label label) {
			Align align = label.adapt(Align.class);
			if(align == null) align = Align.BASE;
			else if(align == Align.NONE) return;
			FormuleVak fv = new FormuleVak();
			fv.setFont(g.getFont());
			fv.setFGColor(g.getColor());
			String string = "$f" + label.getString() + "@";
			fv.vulVak(string);
			fv.setEditable(false);
			fv.zetMaat();
			Dimension s = fv.getSize();
			int as = fv.ashoogte;
			int x = (int) label.getXd();
			int y = (int) label.getYd();
			switch(align) {
			case LEFT:  x -= s.width; 
			case RIGHT: y -= s.height/2; break;
			case TOP: y -= s.height;
			case BOTTOM :	x -= s.width/2; break;
			case NONE:
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
			checkbox.setForeground(g.getColor());
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
			Font f = label.adapt(Font.class);
			g.setFont(f);
			String string = label.getString();
			if(label.getRegistered() instanceof FlipFlop) {
				visitCheckbox(label);
				return;
			}
			if(string.contains("$") || Boolean.TRUE.equals(label.adapt(Boolean.class))) {
				formuleLabel(label);
				return;
			}
			FontMetrics fm = g.getFontMetrics();
			double x = label.getXd();
			double y = label.getYd();
			Align align = label.adapt(Align.class);
			int stringWidth = fm.stringWidth(string);
			float extra = 0;
// zet het label correct tov het puntje.
			if(label.getRegistered() instanceof Interval) {
				try {
					if(Align.NONE == align) return;
					extra = 2;
					extra = label.getP().adapt(Float.class) / 2.0f; // NPE? 
				} catch (Exception e) {
				}
			}
			if(align != null) {
				switch(align) {
				case LEFT: x -= stringWidth+4; 
				case RIGHT: x+=2;	
					y += fm.getAscent()/2; break;
				case TOP: x -= stringWidth/2; y -= fm.getDescent()+extra; break;
				case BOTTOM: x -= stringWidth/2; y += fm.getAscent()+extra; break;
				case BASE:
				case NONE: 
				}
			}
			Rectangle2D.Double rect = 
					new Rectangle2D.Double(x, y - fm.getAscent(), stringWidth, fm.getHeight());
			DefaultAdapter.getDefault((Adaptee) label).put(Shape.class, rect);
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
			Punt[] depend = (Punt[]) t.getDepend();
			int length = depend.length;
			Path2D path = new Path2D.Double(Path2D.WIND_EVEN_ODD, length+1);
			path.moveTo(depend[0].getXd(), depend[0].getYd());
			for (int i = 1; i < length; i++) {
				Punt p = depend[i];
				path.lineTo(p.getXd(), p.getYd());
			}
			path.closePath();
			Paint c = t.adapt(Paint.class);
			if(c != null) {
				g.setPaint(c);
				g.fill(path);
			}
			selectColor(t);
			g.draw(path);
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

		/* (non-Javadoc)
		 * @see fi.euclides.model.AbstractViewer#visitLocus(fi.euclides.model.Locus)
		 */
		@Override
		public void visitLocus(Locus l) {
			if(l instanceof Integral) {
				visitIntegral( (Integral) l);
			} else
			super.visitLocus(l);
		}

		private void visitIntegral(Integral l) {
			selectColor(l);
			double y00 = getModel().getO().getYd();
			if(l.base == Integral.GT)
				y00 = 0;
			else if (l.base == Integral.LT) {
				y00 = height;
			}
			final double y0 = y00;
			final Area shape = new Area();
			l.visitSegments(new SegmentVisitor() {

				@Override
				public void visitSegment(Segment s) {
					double x1 = s.getX1();
					double x2 = s.getX2();
					double y1 = s.getY1();
					double y2 = s.getY2();
					Path2D.Double path = new Path2D.Double();
					path.moveTo(x1, y0);
					path.lineTo(x1, y1);
					path.lineTo(x2, y2);
					path.lineTo(x2, y0);
					path.closePath();
					Area area = new Area(path);
					shape.add(area);
				}

				@Override
				public Numbers clipTop() {
					return Numbers.createDouble(Double.NEGATIVE_INFINITY);
				}

				@Override
				public Numbers clipBottom() {
					return Numbers.createDouble(Double.POSITIVE_INFINITY);
				}

				@Override
				public Numbers clipLeft() {
					return InstanceViewer.this.clipLeft();
				}

				@Override
				public Numbers clipRight() {
					return InstanceViewer.this.clipRight();
				} });
			g.fill(shape);
			DefaultAdapter.getDefault(l).put(Shape.class, shape);
		}
	}

	private CBookEventHandler handler = new CBookEventHandler(this);
	
	{
		viewer = new InstanceViewer();
		getViewer().expression = new nl.numworx.geodefiner.common.math.Expression(viewer); // Inject!!! 
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
	Randomizer randomizer = this;

	JToolBar toolbox;

	private KijkNaAction action;

	private AssessmentMode mode;
	
	public Instance() {
		
		//content.setBackground(Color.white);
		content.setOpaque(false);content.setBackground(null);
		panel.setOpaque(false);panel.setBackground(null);
		//content.setBorder(BorderFactory.createEtchedBorder());
		selector.setTracker(viewer);
		content.addMouseListener(getViewer());
		content.addMouseMotionListener(getViewer());
		panel.add(content, BorderLayout.CENTER);
		toolbox = new JToolBar();
		toolbox.setFloatable(false);
		toolbox.setVisible(false);
		panel.add(toolbox, BorderLayout.NORTH);
		checkBtn.setVisible(false);
		checkLabel.setVisible(false);
		south.add(checkBtn);
		south.add(checkLabel);
		south.setOpaque(false);
		panel.add(south, BorderLayout.SOUTH);
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
		if(mode == AssessmentMode.EINDTOETS) fetchScore(); // no feedback!
		return getState(new Hashtable<String, Object>());
	}
	
	public SuccessStatus getSuccessStatus() {
		Boolean status = getStatus();
		if(status == null) return SuccessStatus.UNKNOWN;
		if(status)
			return SuccessStatus.PASSED;
		return SuccessStatus.FAILED;
	}

	@Override
	public void setLaunchData(Map<String, ? extends Object> launchData,
			Map<String, Number> random) {		
		super.setLaunchData(launchData, random);
	}

	// Assume getSize() is okay.
	public void init() {
		checkBtn.setVisible(false);checkBtn.invalidate();
		checkLabel.setVisible(false);checkLabel.invalidate();
		toolbox.setVisible(false);
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
		this.mode = mode;
		if(mode == AssessmentMode.ZELFTOETS||mode == AssessmentMode.EINDTOETS) checkBtn.setVisible(false);
	}

	public void stop() {

	}

	@Override
	public void setState(Map<String, ?> state) {
		super.setState(state);
		if(nagekeken && action != null)
			action.feedback();
	}

	public void acceptCBookEvent(CBookEvent ev) {
		if(Constants.CHECK.equals(ev.getCommand()) && action != null)
		{
			action.actionPerformed(null); 
			return;
		}
		
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
			String randomizeString = FormuleParser.randomizeString(text,randomVarNamen,randomVarWaarden);
			return randomizeString;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			WiskOpdr.language = lcl;
		}
		return super.randomize(random, text);
	}

	@Override
	protected boolean installCheckDWO() {
		if  (super.installCheckDWO())
		{
			action = new KijkNaAction();
			checkBtn.addActionListener(action);
			checkBtn.setVisible(!checkDWO.isExtern());
			action.addPropertyChangeListener(this);
			checkLabel.setIcon(action);
			checkLabel.setVisible(true);
			checkDWO.addObserver(action);
			checkBtn.invalidate();
			checkLabel.invalidate();
			panel.validate();
			return true;
		}
		checkDWO = new Check_DWO(viewer); // dummy
		panel.validate();
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

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(Action.LARGE_ICON_KEY.equals(evt.getPropertyName())) checkLabel.repaint();
	}

	@Override
	public void start() {
		startToolbox();
		super.start();
	}

	void startToolbox() {
		ToolboxPanel p = new ToolboxPanel();
		p.viewer = getViewer();
		p.setToolbox(toolbox);
		p.fromList(launchData.getObjectList("toolbox"));
	}	

	void installToolTip() {
		ToolTipManager.sharedInstance().registerComponent(content);
	}

	@Override
	public String randomize(String input) {
		return randomize(random, input);
	}

	
}
