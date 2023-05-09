package nl.numworx.geodefiner.common.index;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Groep;

public class ListSelectorIntern extends ListSelector {

  public ListSelectorIntern(Groep grp, int index) {
    super(grp, index);
  }

  @Override
  public Destroyable[] getDepend() {
    return new Destroyable[] { index };
  }
}
