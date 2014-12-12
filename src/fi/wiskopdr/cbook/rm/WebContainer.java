package fi.wiskopdr.cbook.rm;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.WeakHashMap;

import org.cbook.cbookif.rm.ReadOnlyException;
import org.cbook.cbookif.rm.Resource;
import org.cbook.cbookif.rm.ResourceContainer;
import org.cbook.cbookif.rm.ResourceException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;

public class WebContainer implements ResourceContainer, CachedResource {

	
	
	static final String LINK = "httpd/url";
	private WebContainer parent;
	protected URL url;
	private String name;
	Sardine sardine;
	
	WeakHashMap<String, CachedResource> children = new WeakHashMap<String, CachedResource>();

	public WebContainer() {
	}

	public WebContainer(URL url, String name, Sardine sardine) {
		this.url = url;
		this.name = name;
		this.sardine = sardine;
	}

	public WebContainer(URL url, String name, WebContainer parent) {
		this(url, name, parent.sardine);
		this.parent = parent;
	}

	@Override
	public boolean isContainer() {
		return true;
	}

	@Override
	public ResourceContainer getParent() {
		return parent;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public URL getURL() throws ResourceException {
		return url;
	}

	@Override
	public InputStream getStream() throws ResourceException {
		try {
			return url.openStream();
		} catch (IOException e) {
			throw new ResourceException(e);
		}
	}

	@Override
	public String getMimeType() {
		return "httpd/unix-folder";
	}

	@Override
	public void remove() throws ResourceException {
		try {
			sardine.delete(url.toExternalForm());
			parent.children.remove(name);
		    url = null;
		    parent = null;
		    sardine = null;
		    name = null;
		    children = null;
		} catch (IOException e) {
			throw new  ResourceException(e);
		};

	}

	@Override
	public void setName(String name) throws ResourceException {
		try {
			if(name.contains("/")||name.startsWith(".")
					) throw new ResourceException("illegal name " + name);
			URL dest = new URL(url, "../" + name + "/");
			if(sardine.exists(dest.toExternalForm()))
				throw new ResourceException("name exists " + name);
			sardine.move(url.toExternalForm(), dest.toExternalForm());
			parent.children.remove(this.name);
			parent.children.put(name, this);
			this.name = name;
			this.url = dest;
			reparentChildren();
		} catch (IOException e) {
			throw new  ResourceException(e);
		}
	}

	private void reparentChildren() {
		for (CachedResource resource : children.values()) {
			resource.reparent(url);
		}
	}

	@Override
	public Resource open(String name) throws ResourceException {
		if(name.startsWith("/"))
			return getRoot().open(name.substring(1));
		while(name.startsWith("../"))
		{
			name = name.substring(3);
			if(parent != null)
				return parent.open(name);
		}
		while(name.startsWith("./"))
		{
			name = name.substring(2);
		}
		int i = name.indexOf('/');
		if(i >= 0) {
			String dir = name.substring(0,i);
			name = name.substring(i+1);
			return openContainer(dir).open(name);
		}
		if(".".equals(name) || "".equals(name))
			return this;
		if("..".equals(name))
		{
			if(parent == null) return this; else return parent;
		}
		
		try {
			
			URL u = new URL(url, name );
			return open(u);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	WebContainer getRoot() {
		if(parent == null) return this;
		return parent.getRoot();
	}

	@Override
	public Resource create(String name, InputStream in, String mimetype)
			throws ResourceException {
		if(name.startsWith("/"))
			return getRoot().create(name.substring(1),in, mimetype);
		while(name.startsWith("../"))
		{
			name = name.substring(3);
			if(parent != null)
				return parent.create(name,in,mimetype);
		}
		while(name.startsWith("./"))
		{
			name = name.substring(2);
		}
		int i = name.indexOf('/');
		if(i >= 0) {
			String dir = name.substring(0,i);
			name = name.substring(i+1);
			return openContainer(dir).create(name,in,mimetype);
		}
		if(".".equals(name) || "".equals(name))
			readonly();
		if("..".equals(name))
		{
			readonly();
		}
		
		try {
			URL u = new URL(url, name );
			String externalForm = u.toExternalForm();
//			if(sardine.exists(externalForm))
//				readonly();
			sardine.put(externalForm, in, mimetype, false);
			return open(u);
			
		} catch (Exception e) {
			throw new ResourceException(e);
		}
	}

	public Resource create(String name, URL in, String mimetype)
			throws ResourceException {
		return create(name,in);
	}

	public Resource create(String name, URL in)
			throws ResourceException {
		if(name.startsWith("/"))
			return getRoot().create(name.substring(1),in);
		while(name.startsWith("../"))
		{
			name = name.substring(3);
			if(parent != null)
				return parent.create(name,in);
		}
		while(name.startsWith("./"))
		{
			name = name.substring(2);
		}
		int i = name.indexOf('/');
		if(i >= 0) {
			String dir = name.substring(0,i);
			name = name.substring(i+1);
			return ((WebContainer)openContainer(dir)).create(name,in);
		}
		if(".".equals(name) || "".equals(name))
			readonly();
		if("..".equals(name))
		{
			readonly();
		}
		
		try {
			URL u = new URL(url, name );
			String externalForm = u.toExternalForm();
//			if(sardine.exists(externalForm))
//				readonly();
			sardine.put(externalForm, in.toExternalForm().getBytes(), LINK);
			return open(u);
			
		} catch (Exception e) {
			throw new ResourceException(e);
		}
	}

	@Override
	public Resource create(String name, Resource resource)
			throws ResourceException {
		if(name.startsWith("/"))
			return getRoot().create(name.substring(1),resource);
		while(name.startsWith("../"))
		{
			name = name.substring(3);
			if(parent != null)
				return parent.create(name,resource);
		}
		while(name.startsWith("./"))
		{
			name = name.substring(2);
		}
		int i = name.indexOf('/');
		if(i >= 0) {
			String dir = name.substring(0,i);
			name = name.substring(i+1);
			return openContainer(dir).create(name,resource);
		}
		if(".".equals(name) || "".equals(name))
			readonly();
		if("..".equals(name))
		{
			readonly();
		}
		
		try {
			URL u = new URL(url, name );
			String externalForm = u.toExternalForm();
//			if(sardine.exists(externalForm))
//				readonly();
			sardine.copy(resource.getURL().toExternalForm(), externalForm);
			return open(u);
			
		} catch (Exception e) {
			throw new ResourceException(e);
		}
	}

	protected ResourceException readonly() {
		return new ReadOnlyException(name);
	}

	@Override
	public ResourceContainer createContainer(String name)
			throws ResourceException {
		if(name.startsWith("/"))
			return getRoot().createContainer(name.substring(1));
		while(name.startsWith("../"))
		{
			name = name.substring(3);
			if(parent != null)
				return parent.createContainer(name);
		}
		while(name.startsWith("./"))
		{
			name = name.substring(2);
		}
		int i = name.indexOf('/');
		if(i >= 0) {
			String dir = name.substring(0,i);
			name = name.substring(i+1);
			return openContainer(dir).createContainer(name);
		}
		if(".".equals(name) || "".equals(name))
			readonly();
		if("..".equals(name))
		{
			readonly();
		}
		
		try {
			URL u = new URL(url, name );
			String externalForm = u.toExternalForm();
//			if(sardine.exists(externalForm))
//				readonly();
			sardine.createDirectory(externalForm);
			return (ResourceContainer) open(u);
			
		} catch (Exception e) {
			throw new ResourceException(e);
		}
	}

	@Override
	public ResourceContainer openContainer(String name)
			throws ResourceException {
		Resource r = open(name);
		if(r instanceof ResourceContainer)
			return (ResourceContainer) r;
		return null;
	}

	@Override
	public Resource[] list() throws ResourceException {
		try {
			List<DavResource> list = sardine.list(url.toExternalForm(), 1);
			Resource[] result = new Resource[list.size()-1];
			for (int i = 0; i < result.length; i++) {
				DavResource object = list.get(i+1);
				result[i] = open(object);
			}
			return result;
		} catch (Exception e) {
			throw new ResourceException(e);
		}
	}

	Resource open(URL object) throws IOException {
		String u = object.toExternalForm();
		List<DavResource> rl = sardine.list(u, 0);
		if(rl == null || rl.isEmpty())
			throw new ResourceException("does not exist " + object);
		DavResource r0 = rl.get(0);
		return open(r0);
	}

	Resource open( DavResource r0) throws MalformedURLException {
		String name = r0.getDisplayName();
		Long length = r0.getContentLength();
		CachedResource r = children.get(name);
		URL object = r0.getHref().toURL();
		if(r0.isDirectory())
		{
			if(r instanceof WebContainer)
				return r;
			r = new WebContainer(object, name, this);	
		}
		else
		{	
			if(r instanceof WebResource)
			{	((WebResource)r).length = length;
				return r;
			}
			if(object.getPath().endsWith("/"))
			{
			   object = new URL(object.getProtocol(), object.getHost(), object.getPort(), strip(object.getPath()));
			}
			r = new WebResource(object, name, this, r0.getContentType(), length);
		}
		children.put(name, r);
		return r;
	}

	protected String strip(String path) {
		return path.substring(0, path.length()-1);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WebContainer other = (WebContainer) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
	}

	public void reparent(URL url) {
		try {
			this.url = new URL(url, name + "/");
			reparentChildren();
		} catch (MalformedURLException e) {
		}
		
	}

	public String toString() {
		return getName();
	}

	public Long getContentLength() {
		return null;
	}
}
