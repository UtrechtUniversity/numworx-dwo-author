package fi.euclides.model.algo;

import fi.euclides.model.Lijn;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.Segment;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DefaultAdapter;

public class PointOnSegment extends PointOnLine {

	PointOnSegment() {
	}

	public final static PointOnSegment INSTANCE = new PointOnSegment();
	
	@Override
	public void recalc(Lijn seg, FreePoint punt, double x, double y) {
		double dx = seg.getDX();
		double dy = seg.getDY();
		double x1 = seg.getX1();
		double y1 = seg.getY1();
		double inp = (dx*(x-x1)+dy*(y-y1))/(dx*dx+dy*dy);
// endpoints
		double xx = x1 + inp*dx;
			if(dx<0)
			{
				if(xx > seg.getX1())
				{
					DefaultAdapter.getDefault(punt).put(Numbers.class, Numbers.ZERO);
					punt.setXY(seg.getX1n(), seg.getY1n());
					return;
				} 
				if(xx < seg.getX2())
				{
					DefaultAdapter.getDefault(punt).put(Numbers.class, Numbers.ONE);
					punt.setXY(seg.getX2n(), seg.getY2n());
					return;	
				}
			} else if(dx>0)
			{
				if(xx < seg.getX1())
				{
					DefaultAdapter.getDefault(punt).put(Numbers.class, Numbers.ZERO);
					punt.setXY(seg.getX1n(), seg.getY1n());
					return;
				} 
				if(xx > seg.getX2())
				{
					DefaultAdapter.getDefault(punt).put(Numbers.class, Numbers.ONE);
					punt.setXY(seg.getX2n(), seg.getY2n());
					return;	
				}
			} else if(dy>0)
			{
				xx = y1+inp*dy;
				if(xx < seg.getY1())
				{
					DefaultAdapter.getDefault(punt).put(Numbers.class, Numbers.ZERO);
					punt.setXY(seg.getX1n(), seg.getY1n());
					return;
				}
				if(xx > seg.getY2())
				{
					DefaultAdapter.getDefault(punt).put(Numbers.class, Numbers.ONE);
					punt.setXY(seg.getX2n(), seg.getY2n());
					return;
				}
			} else if(dy < 0) 
			{
				xx = y1+inp*dy;
				if(xx > seg.getY1())
				{
					DefaultAdapter.getDefault(punt).put(Numbers.class, Numbers.ZERO);
					punt.setXY(seg.getX1n(), seg.getY1n());
					return;
				}
				if(xx < seg.getY2())
				{
					DefaultAdapter.getDefault(punt).put(Numbers.class, Numbers.ONE);
					punt.setXY(seg.getX2n(), seg.getY2n());
					return;
				}
				
			}
		online(seg, punt, x1, y1, dx, dy, inp);

	}

}
