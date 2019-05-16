package nl.numworx.geodefiner.common.index;

import fi.euclides.model.algo.FreePoint;
import fi.euclides.model.math.Numbers;

public class FreePointIndex extends PuntIndex implements FreePoint {

  public FreePointIndex(Selector listSelector) {
    super(listSelector);
  }

  @Override
  public void setFree(boolean free) {
    FreePoint fp = adapt(FreePoint.class);
    if (fp != null) fp.setFree(free);
  }

  @Override
  public boolean isFree() {
    FreePoint fp = adapt(FreePoint.class);
    if (fp != null) return fp.isFree();
    return false;
  }

  @Override
  public <T> T adapt(Class<T> clz) {
    if (getDelegate() != null && clz == FreePoint.class) return getDelegate().adapt(clz);
    return super.adapt(clz);
  }

  @Override
  public void setXY(Numbers x, Numbers y) {
    super.setXY(x, y);
    if (getDelegate() != null)
      getDelegate().setXY(x, y);
  }
  
}
