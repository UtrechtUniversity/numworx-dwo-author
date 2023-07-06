package fi.beans.iconan;

import java.awt.Image;

public interface ImageCache {

  Image remove(String selected);

  Image get(String name);

  Image put(String name, Image result);

}
