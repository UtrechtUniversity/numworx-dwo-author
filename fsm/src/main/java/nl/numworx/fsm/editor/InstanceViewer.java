package nl.numworx.fsm.editor;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.geom.Path2D;

import fi.euclides.event.NameMapper;
import fi.euclides.model.AbstractViewer;
import fi.euclides.model.Boog;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Dpunt;
import fi.euclides.model.GeoImage;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.Track;
import fi.euclides.model.VrijPunt;
import fi.euclides.swing.AWTViewer;
import nl.numworx.fsm.shared.FSMMapper;
import nl.numworx.fsm.shared.Hits;
import nl.numworx.fsm.shared.Hoekpunt;


public class InstanceViewer extends AWTViewer {
	
	private Track track;
	private Instance parent;
	private NameMapper mapper;

	public InstanceViewer(Model model, Instance instance) {
		super(model);
		parent = instance;
		pointSize = 75;
		hitTester = new Hits();
		mapper = new FSMMapper();
		setTrack(this);
	}

	@Override
	public void setTrack(Track track) {
		this.track = track;
	}

	@Override
	public Track getTrack() {
		return track;
	}

	@Override
	protected void drawImage(GeoImage image) {
	}

	@Override
	public NameMapper getMapper() {
		return mapper;
	}

	@Override
	public void paint() {
		parent.repaint();

	}

	public void notifyViewport() {
		setChanged();
		notifyObservers();	
	}

	@Override
	public void visitPunt(Punt punt) {
		selectColor(punt);
		g.setStroke(new BasicStroke(3));
		float p2 = pointSize/2f;
		if (punt instanceof Dpunt || punt instanceof Hoekpunt) {
			p2 = 3f;
			fillCircle(punt.getXd()-p2, punt.getYd()-p2 , p2*2);
			return;
		}
		
		drawCircle(punt.getXd()-p2, punt.getYd()-p2 , pointSize);
		if (Boolean.TRUE.equals(punt.adapt(Boolean.class)))
		{	float shrink = 0.8f;
			drawCircle(punt.getXd()-p2*shrink, punt.getYd()-p2*shrink, pointSize*shrink);
		}		
		String name = mapper.toString(punt);
		if (name != null) {
			g.drawString(name, (float) punt.getXd(), (float) punt.getYd());
		}
	}

	@Override
	public void visitSegment(Segment s) {
		selectColor(s);
		g.setStroke(new BasicStroke(3));
		double hyp = Math.hypot(s.getDX(), s.getDY());
		double x1 = s.getX1() + pointSize/2.0 * s.getDX() / hyp;
		double y1 = s.getY1() + pointSize/2.0 * s.getDY() / hyp;
		double x2 = s.getX2() - pointSize/2.0 * s.getDX() / hyp;
		double y2 = s.getY2() - pointSize/2.0 * s.getDY() / hyp;
		String name = mapper.toString(s);
		Segment o = s;
		s = new Segment(new VrijPunt(x1, y1), new VrijPunt(x2, y2));
		drawTips(s, o);
		drawLine(x1, y1, x2, y2);
		
		if (name != null) {
			g.drawString(name, (float) (x1+x2)/2, (float) (y1+y2)/2);
		}
	}


	private Segment drawTips(Segment s, Destroyable o) {
		
		selectColor(o);
		double dx = s.getDX();
		double dy = s.getDY();
		double len = Math.hypot(dx, dy);
		Float width = 3f; // segment width
		double tiplen = 5;
		if(width != null) tiplen *= width.doubleValue();
		if(len < tiplen*3) tiplen = len/3;
		dx *= tiplen / len; 
		dy *= tiplen/len; 
		tip(s.getP2(), -dx, -dy, s.getX2(), s.getY2()); 
		return s;
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

	@Override
	public void visitBoog(Boog b) {
		selectColor(b);
		Float width = 3f; // segment width
		g.setStroke(new BasicStroke(width));
		double d = b.getD();
		double s = b.getStart();
		double l = b.length();
		drawArc(b.getX(), b.getY(), d, s, l);
		Punt end = Boog.endOf(b);
		Punt center = b.getCenter();
		double xd = end.getXd();
		double yd = end.getYd();
		double dx = xd - center.getXd();
		double dy = yd - center.getYd();
		double tiplen = 5;
		if(width != null) tiplen *= width.doubleValue();
		double len = Math.hypot(dx, dy);
		if(len < tiplen*3) tiplen = len/3;
		dx *= tiplen / len; 
		dy *= tiplen/len; 
		
		tip(end, -dy, dx, xd, yd);		
		String name = mapper.toString(b);
		if (name != null) {
			Punt punt = b.getCenter();
			g.drawString(name, (float) punt.getXd(), (float) punt.getYd());
		}
	}

	@Override
	public <T> T adapt(Class<T> clz) {
		if (clz == AbstractViewer.class) return (T) this;
		if (clz == Component.class) return (T) parent;
		
		return super.adapt(clz);
	}

}
