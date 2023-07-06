package fi.beans.iconan;

import java.awt.Image;
import java.util.Hashtable;


@SuppressWarnings("serial")
public class SimpleCache extends Hashtable<String, Image> implements ImageCache {

  @Override
  public Image remove(String selected) {
    return super.remove(selected);
  }

  @Override
  public Image get(String name) {
    return super.get(name);
  }


}
