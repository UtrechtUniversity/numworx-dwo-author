package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;

public class RaaklijnLocus extends LijnPuntCombi<MP> implements SegmentVisitor {
	public static final String TYPE = "lM";
	private static final Numbers _100 = Numbers.createInteger(100);
	
	public RaaklijnLocus() {
		super();
		x1 = Numbers.ZERO;
		x2 = Numbers.ZERO;
		y1 = Numbers.ZERO;
		y2 = Numbers.ZERO;
			
	}

	public RaaklijnLocus(MP lijn, Punt punt) {
		super(lijn, punt);
		x1 = punt.getX();
		x2 = punt.getX();
		y1 = punt.getY();
		y2 = punt.getY();
		recalc();
	}

	private void recalc() {
		dist = Double.MAX_VALUE;
		lijn.visitSegments(this);
	}

	Numbers x1,	x2,	y1,	y2;
	private double dist;
	
	@Override
	public Numbers getX1n() {
		return x1;
	}

	@Override
	public Numbers getY1n() {
		return y1;
	}

	@Override
	public Numbers getX2n() {
		return x2;
	}

	@Override
	public Numbers getY2n() {
		return y2;
	}

	
	
	@Override
	public String key() {
		return TYPE;
	}

	public void visitSegment(Segment s) {
		double x = getPunt().getXd();
		double y = getPunt().getYd();
		Punt pb = s.getP2();
		if(pb.isDefined())
		{
			double d = Math.hypot(x-pb.getXd(), y-pb.getYd());
			if(d < dist)
			{
				x1 = pb.getX(); y1 = pb.getY();
				Punt pa = s.getP1();
				x2 = pa.getX(); y2 = pa.getY();
				punt.setXY(pb.getX(),pb.getY());
				dist = d;
			}
		}
	}
	
	public Numbers clipTop() {
		return Numbers.sub(getPunt().getY(), _100);
	}

	public Numbers clipBottom() {
		return Numbers.add(getPunt().getY(), _100);
	}

	public Numbers clipLeft() {
		return Numbers.sub(getPunt().getX(), _100);
	}

	public Numbers clipRight() {
		return Numbers.add(getPunt().getX(), _100);
	}

	public void update(Observable o, Object arg) {
		super.update(o, arg);
		if(o == lijn || o == getPunt()) 
			recalc();
	}

	@Override
	public void read(Codec codec) throws IOException {
		super.read(codec);
		if(lijn != null && getPunt() != null) recalc();
	}


}
