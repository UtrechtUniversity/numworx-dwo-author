package nl.numworx.geodefiner.common.index;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;

public class LabelSelectorIntern extends LabelSelector {

  public LabelSelectorIntern(Label grp, int index) {
    super(grp, index);
  }

  @Override
  public Destroyable[] getDepend() {
    return new Destroyable[] { index };
  }

}
