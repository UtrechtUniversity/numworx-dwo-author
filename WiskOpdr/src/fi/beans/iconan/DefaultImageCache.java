package fi.beans.iconan;

import java.awt.Image;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

@SuppressWarnings("serial")
public class DefaultImageCache extends LinkedHashMap<String, SoftReference<Image>> implements ImageCache {

  public DefaultImageCache() {
    super(8, 0.75f, true);
  }

  @Override
  public synchronized Image remove(String selected) {
    SoftReference<Image> ref = super.remove(selected);
    return image(ref);
  }

  @Override
  public synchronized Image get(String name) {
    SoftReference<Image> ref = super.get(name);
    if (ref != null && ref.get() == null) super.remove(name); 
    return image(ref);
  }

  private Image image(SoftReference<Image> ref) {
    return ref == null ? null : ref.get();
  }

  @Override
  public Image put(String name, Image result) {
    SoftReference<Image> ref = super.put(name, new SoftReference<Image>(result));
    return image(ref);
  }

  @Override
  protected boolean removeEldestEntry(java.util.Map.Entry<String, SoftReference<Image>> eldest) {
    return eldest.getValue().get() == null || size() > 32;
  }

}
