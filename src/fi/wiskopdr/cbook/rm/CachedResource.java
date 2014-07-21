package fi.wiskopdr.cbook.rm;

import java.net.URL;

import org.cbook.cbookif.rm.Resource;

public interface CachedResource extends Resource {

	void reparent(URL url);
}
