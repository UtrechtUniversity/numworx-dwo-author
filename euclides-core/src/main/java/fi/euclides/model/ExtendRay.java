package fi.euclides.model;

public class ExtendRay extends ExtendedLijn {

	void extendH() {
		if(x2 > x1 && x2 < ex)
		{
			y2 = y2 + lijn.getDY()/lijn.getDX() * (ex-x2);
			x2 = ex;
		} else if ( x1 > x2 && x2 > bx)
		{
			y2 = y2 + lijn.getDY()/lijn.getDX() * (bx-x2);
			x2 = bx;
		}
	}

	void extendV() {
		if(y2 > y1 && y2 < ey)
		{
			x2 = x2 + lijn.getDX()/lijn.getDY() * (ey-y2);
			y2 = ey;
		} else if ( y1 > y2 && y2 > by)
		{
			x2 = x2 + lijn.getDX()/lijn.getDY() * (by-y2);
			y2 = by;
		}
	}

}
