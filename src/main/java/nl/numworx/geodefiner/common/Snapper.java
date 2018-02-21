package nl.numworx.geodefiner.common;

import fi.euclides.model.Model;
import fi.euclides.model.algo.FreePoint;
import fi.euclides.model.math.Numbers;

public abstract class Snapper {
	protected final int SNAP = 5;

	protected boolean gravity;
	
	public void setGravity(boolean gravity) {
		this.gravity = gravity;
	}

	public boolean isGravity() {
		return gravity;
	}
	
	public void snap(FreePoint fp, Model model) {
		if(fp != null && isGravity() && fp.isFree()) {
			long ox = model.getO().getX().longValue();
			long dx = model.getU().getX().longValue()-ox;
			long x0 = fp.getX().longValue();
			long x = (x0-ox)%dx;
			if (x < 0) x += dx;
			if (x*2 >dx) x =- dx;
			if (x > SNAP || x < -SNAP) x=0;
			
			long oy = model.getO().getY().longValue();
			long dy = dx;
			long y0 = fp.getY().longValue();
			long y = (y0-oy)%dy;
			if (y < 0) y += dx;
			if (y*2 > dy) y -= dy;
			if (y > SNAP || y < -SNAP) y = 0;
			Numbers nx = Numbers.createInteger((int) x);
			Numbers ny = Numbers.createInteger((int) y);
			fp.moveTo( Numbers.sub(fp.getX(), nx), Numbers.sub(fp.getY(), ny));
		}
	}
	}
