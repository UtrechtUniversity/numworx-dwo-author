package nl.numworx.geodefiner;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fi.euclides.event.DescriptionBuilder;
import fi.euclides.event.HitTester;
import fi.euclides.event.NameMapper;
import fi.euclides.model.AbstractViewer;
import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Locus;
import fi.euclides.model.MP;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.Ray;
import fi.euclides.model.Segment;
import fi.euclides.model.SegmentVisitor;
import fi.euclides.model.Track;
import fi.euclides.model.TrailBuilder;
import fi.euclides.model.Triangle;
import fi.euclides.model.math.Numbers;
import fi.euclides.openmath.Expression;
import fi.euclides.proof.FlipFlop;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.swing.AWTViewer;
import fi.euclides.util.Adaptee;
import fi.euclides.util.Adapter;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;
import fi.wiskopdr.formuleobjects.FormuleVak;
import nl.numworx.geodefiner.common.Align;
import nl.numworx.geodefiner.common.CELL;
import nl.numworx.geodefiner.common.CheckObject;
import nl.numworx.geodefiner.common.Integral;
import nl.numworx.geodefiner.common.Interval;
import nl.numworx.geodefiner.common.NamingModel;
import nl.numworx.geodefiner.common.Randomizer;
import nl.numworx.geodefiner.common.ShortSegment;
import nl.numworx.geodefiner.common.Tips;
import nl.numworx.geodefiner.ui.AxesModel;

@Singleton
final public class InstanceViewer extends AWTViewer implements Observer, TrailBuilder {

	private HitTester tiptest;
	final JLabel statusLabel = new JLabel();
	final Randomizer randomizer;
	final JPanel content = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				InstanceViewer.this.paint(g);
			}

			@Override
			public void setBounds(int x, int y, int width, int height) {
				super.setBounds(x, y, width, height);
				boolean same = InstanceViewer.this.height == height && InstanceViewer.this.width == width;
				InstanceViewer.this.height = height;
				InstanceViewer.this.width = width;
				if(!same)
					InstanceViewer.this.notifyViewport();
			}

			@Override
			public String getToolTipText(MouseEvent e) {
				DescriptionBuilder builder = new DescriptionBuilder(getMapper()) {

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
				tiptest.setXY(e.getX()-offX, e.getY()-offY);
				Model r = getModel();
				r.visitPunten(tiptest);
				r.visitLijnen(tiptest);
				tiptest.done();
				return builder.toString();
			}
			
		};
		{
			content.setOpaque(false);content.setBackground(null);
			content.addMouseListener(this);
			content.addMouseMotionListener(this);

		}
		class PathVisitor implements SegmentVisitor {
			
			GeneralPath path = new GeneralPath();
			double x = Double.NEGATIVE_INFINITY;
			double y = Double.NEGATIVE_INFINITY;
			
			@Override
			public void visitSegment(Segment s) {
				double x1, y1;
				x1 = s.getX1();
				y1 = s.getY1();
				if(x1 != x || y1 != y)
					path.moveTo(x1, y1);
				x = s.getX2();
				y = s.getY2();
				path.lineTo(x, y);
			}

			@Override
			public Numbers clipTop() {
				return InstanceViewer.this.clipTop();
			}

			@Override
			public Numbers clipBottom() {
				return InstanceViewer.this.clipBottom();
			}

			@Override
			public Numbers clipLeft() {
				return InstanceViewer.this.clipLeft();
			}

			@Override
			public Numbers clipRight() {
				return InstanceViewer.this.clipRight();
			}

			public void destroy() {
				if(path != null)
					g.draw(path);
				path = null;
			}
		}

		public void notifyViewport() {
			setChanged();
			super.notifyObservers();
		}

		static final float DEFAULT_POINTSIZE = 5f;

		private NamingModel nameMapper;
		private Snapper snapper = new Snapper(this);
		Expression expression; 

		@Override
		public void paint() {
			content.repaint();
		}

		@Override
		public <T> T adapt(Class<T> cls) {
			if(cls == nl.numworx.geodefiner.common.Snapper.class) return (T) snapper;
			if(cls == Snapper.class) return (T) snapper;
			if(cls == Expression.class || cls == nl.numworx.geodefiner.common.math.Expression.class) return (T) expression;
			if(cls == Randomizer.class) return (T) randomizer;
			if(cls == AbstractViewer.class) return (T) this;
			if(cls == Component.class) return (T) content;
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
			if(getModel().getLijnen().size() < 3) return;
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
			nameMapper.clear();
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
			statusLabel.setText(string);
		}

		private WeakHashMap<String, Destroyable> cache = new WeakHashMap<String,Destroyable>();

		private HighLighter hilighter;
		
		@Override
		public NameMapper getMapper() {
			return nameMapper;
		}

		@Inject InstanceViewer(NamingModel nm, nl.numworx.geodefiner.common.math.Expression expression, Randomizer r, Set<LabelDelegate> set) {
			super(nm.getModel());
			this.nameMapper = nm;
			this.expression = expression;
			this.randomizer = r;
			getModel().addObserver(this);
			getModel().setTrailBuilder(this);
			hitTester = (new HitTester3(content.getFontMetrics(content.getFont())));
			nameMapper = new NamingModel(this, cache);
			hilighter = new HighLighter(hitTester.copy(), this);
			tiptest = hitTester.copy();
			for(LabelDelegate ld: set) ld.setTracker(this);
			expression.setAllTracker(this);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			snapper.translate(e);
			super.mouseReleased(e);
			hilighter.mouseReleased(e);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			snapper.translate(e);
			super.mouseDragged(e);
			hilighter.mouseDragged(e);
		}

		
		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			hilighter.mouseClicked(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			//super.mouseEntered(e);
			hilighter.mouseEntered(e);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			//super.mouseExited(e);
			hilighter.mouseExited(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);
			hilighter.mousePressed(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			//super.mouseMoved(e);
			hilighter.mouseMoved(e);
		}

		@Override
		public void selectColor(Destroyable object) {
			Adapter a = object.getAdapter();
			Stroke stroke = a.adapt(Stroke.class);
			if(stroke == null) stroke = Instance.DEFAULT_STROKE;
			g.setStroke(stroke);
			if(tracking || trail)
			{
				//hilighter.hilight(object, g);
			    if (trail) {
			      Color c = a.adapt(Color.class);
			      if (c != null) { g.setColor(c); } else { g.setColor(Color.LIGHT_GRAY); }
			    }
			  
				return;
			}
			Color c = a.adapt(Color.class);
			CheckObject co = a.adapt(CheckObject.class);
			if (c != null && co == null) {
				if (getModel().getSelect().contains(object))
				{	g.setColor(c.brighter());	// grijs wordt wit...		
					setColor(RED);				// is te verwarrend, altijd SELECTCOLOR
				} else
				{
					g.setColor(c);
				}
				hilighter.hilight(object, g);
				return;
			}
// feedback color.
			if(co != null)
			{   // extra verificatie?
				g.setColor(Color.green);
			} else
				super.selectColor(object);
			hilighter.hilight(object, g);
		}
		
		
		
		
		@Override
		public void visitSegment(Segment s) {
			s = drawTips(s,s);
			super.visitSegment(s);
		}
	
		public void visitRay(Lijn r) {
			rr.setLijn(r);
			Segment l;
			l = drawTips(rr,r);
			selectColor(r);
			drawLine(l.getX1(), l.getY1() , l.getX2(), l.getY2());
		}

		public void visitLijn(Lijn l) {
			if(l instanceof Ray) visitRay(l);
			else super.visitLijn(l);
		}
		private Segment drawTips(Segment s, Destroyable o) {
			Tips tip = s.adapt(Tips.class);
			if(tip == null) return s;
			selectColor(o);
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
			case ATEND: tip(s.getP2(), -dx, -dy, s.getX2(), s.getY2()); break;
			case ATSTARTEND: tip(s.getP2(),-(dx), -(dy), s.getX2(), s.getY2());
			case ATSTART: tip(s.getP1(), dx, dy, s.getX1(), s.getY1()); break;
			case NOTIP: return s;
			}
			return new ShortSegment(s, dx, dy, tip);
		}

		private void tip(Punt p1, double dx, double dy, double xd, double yd) {
			Path2D.Double path = new Path2D.Double();
			double x = xd;
			double y = yd;
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
			int as = fv.ashoogte + g.getFontMetrics().getAscent()/2;
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
			int x = (int) label.getXd();
			int y = (int) label.getYd();
			boolean on = label.getState() != Label.FALSE;
			boolean withText = Align.NONE != label.adapt(Align.class);
			int square = g.getFontMetrics().getAscent();
			int space = 2;
			Graphics g3 = g.create();
			if(on) {
				g3.setColor(Color.gray);
				g3.fillRect(x, y, square, square);
			}
			g.setStroke(Instance.DEFAULT_STROKE);
			g.drawRect(x, y, square, square);
			Shape s;
			if(withText) {
				String text = getMapper().toString(label);
				//g.setFont(fi.wiskopdr.WiskOpdr.tekstFont);
				g.drawString(text, x+square+space, y+square);
				s = new Rectangle(x, y, square + space + g.getFontMetrics().stringWidth(text), square);
			} else
				s = new Rectangle(x, y, square, square);
			DefaultAdapter.getDefault(label).put(Shape.class, s);

			g3.dispose();
			
			
		}
		
		
		private void visitCheckboxJ(Label label) {
			JCheckBox checkbox = new JCheckBox(getMapper().toString(label));
			checkbox.setContentAreaFilled(false);
			if(Align.NONE == label.adapt(Align.class))
				checkbox.setText("");
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
			if(f==null) f = fi.wiskopdr.WiskOpdr.tekstFont; // NEVER NULL
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
			if (trail) {
			  selectColor(punt);
	          float p2 = pointSize/2f;
	          fillCircle(punt.getXd()-p2, punt.getYd()-p2 , pointSize);
	        } else 
	          super.visitPunt(punt);
		}

		final Color grayish = new Color(0.125f,0.125f,0.125f,0.125f);
		final Color reddish = new Color(0.5f, 0, 0, 0.125f);

		public void visitTriangle(Triangle t)
		{
			Punt[] depend = t.getElements();
			int length = depend.length;
			Path2D path = new Path2D.Double(Path2D.WIND_EVEN_ODD, length+1);
			path.moveTo(depend[0].getXd(), depend[0].getYd());
			for (int i = 1; i < length; i++) {
				Punt p = depend[i];
				path.lineTo(p.getXd(), p.getYd());
			}
			path.closePath();
			Paint c = t.adapt(Paint.class);
			CheckObject co = t.adapt(CheckObject.class);
			if (co != null) {
				g.setColor(new Color(0x80008000, true)); // CONSTANT
				g.fill(path);
			} else
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

		public void visitMP(MP l) {
			selectColor(l);
			boolean old = tracking;
			try {
			  tracking = true;
			  PathVisitor v = new PathVisitor();
			  l.visitSegments(v);
			  v.destroy();
			} finally {
			  tracking = old;
			}
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

		float getPointSize() {
			return pointSize;
		}

		void setPointSize(float f) {
			pointSize = f;
		}

	private Track track;	
    @Override
    public void setTrack(Track track) {
        this.track = track;
        if (track == null) super.setTrack((Iterable)null);
        else super.setTrack(this);
    }

    @Override
    public Track getTrack() {
      return this.track;
    }

    @Override
    public Destroyable trail(Destroyable d) {
      Destroyable copy = d.trail();
      if (copy == null) return copy;
      DefaultAdapter adapter = DefaultAdapter.getDefault(copy);
      adapter.put(Float.class, d.adapt(Float.class)); // point size
      adapter.put(Stroke.class, d.adapt(Stroke.class)); //line width/style
// Color, maak 80% transparant.
      Color c = d.adapt(Color.class);
      if (c != null ) {
        int a = Math.max(c.getAlpha()/8,10);
        int r = Math.max(1,c.getRed());
        int g = Math.max(1,c.getGreen());
        int b = Math.max(1,c.getBlue());
        c = new Color(r,g,b,a).brighter();
        adapter.put(c);
      }
      return copy;
    }
}