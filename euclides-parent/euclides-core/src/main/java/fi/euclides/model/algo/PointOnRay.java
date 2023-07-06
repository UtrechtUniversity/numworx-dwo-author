package fi.euclides.model.algo;

import fi.euclides.model.Lijn;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.Ray;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DefaultAdapter;

public class PointOnRay extends PointOnLine {

	PointOnRay() {
	}

	public static PointOnRay INSTANCE = new PointOnRay();
	@Override
	public void recalc(Lijn ray, FreePoint punt, double x, double y) {
		double dx = ray.getDX();
		double dy = ray.getDY();
		double x1 = ray.getX1();
		double y1 = ray.getY1();
		double inp = (dx*(x-x1)+dy*(y-y1))/(dx*dx+dy*dy);
// TODO 1 endpoint
		double xx = x1 + inp*dx;
			if(dx<0)
			{
				if(xx > ray.getX1())
				{
					DefaultAdapter.getDefault(punt).put(Numbers.class, Numbers.ZERO);
					punt.setXY(ray.getX1n(), ray.getY1n());
					return;
				} else
				if(xx < ray.getX2())
				{
					//punt.setXY(ray.getX2n(), ray.getY2n());
					//return;	
				}
			} else if(dx>0)
			{
				if(xx < ray.getX1())
				{
					DefaultAdapter.getDefault(punt).put(Numbers.class, Numbers.ZERO);
					punt.setXY(ray.getX1n(), ray.getY1n());
					return;
				} else
				if(xx > ray.getX2())
				{
					//punt.setXY(ray.getX2n(), ray.getY2n());
					//return;	
				}
			} else if(dy>0)
			{
				xx = y1+inp*dy;
				if(xx < ray.getY1())
				{
					DefaultAdapter.getDefault(punt).put(Numbers.class, Numbers.ZERO);
					punt.setXY(ray.getX1n(), ray.getY1n());
					return;
				} else
				if(xx > ray.getY2())
				{
					//punt.setXY(ray.getX2n(), ray.getY2n());
					//return;
				}
			} else if(dy < 0) 
			{
				xx = y1+inp*dy;
				if(xx > ray.getY1())
				{
					DefaultAdapter.getDefault(punt).put(Numbers.class, Numbers.ZERO);
					punt.setXY(ray.getX1n(), ray.getY1n());
					return;
				} else
				if(xx < ray.getY2())
				{
					//punt.setXY(ray.getX2n(), ray.getY2n());
					//return;
				}
				
			}
		online(ray, punt, x1, y1, dx, dy, inp);
	}

}
