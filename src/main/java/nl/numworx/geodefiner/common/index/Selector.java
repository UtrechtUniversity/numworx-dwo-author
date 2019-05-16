package nl.numworx.geodefiner.common.index;

import fi.euclides.model.Destroyable;
import fi.euclides.util.Observer;

interface Selector extends Observer {

  Destroyable[] getDepend();

}
