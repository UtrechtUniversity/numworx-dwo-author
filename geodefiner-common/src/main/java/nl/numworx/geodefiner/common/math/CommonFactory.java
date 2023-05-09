package nl.numworx.geodefiner.common.math;

import fi.euclides.bigmath.ExactImpl;
import fi.euclides.model.math.DoubleFormat;
import fi.euclides.model.math.Exact;
import fi.euclides.model.math.ExactFactory;

public class CommonFactory extends ExactImpl implements ExactFactory {

  @Override
  public String toString(Exact value) {
    if(isIntegral(value)) {
      return super.toString(value);
    }
    return DoubleFormat.toString(value);
  }

}
