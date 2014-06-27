package fi.wiskopdr.cbook.rm;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.cbook.cbookif.rm.Resource;
import org.cbook.cbookif.rm.ResourceContainer;
import org.cbook.cbookif.rm.ResourceException;

import com.github.sardine.Sardine;

public class WebResource implements Resource {

	WebContainer parent;
	URL url;
	String name;
	private String type;
	
	@Override
	public boolean isContainer() {
		return false;
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

	public Reader getReader() throws ResourceException {
		try {
			URLConnection c = url.openConnection();
			String type = c.getContentType();
			String charset = "UTF-8";
			int index = type.indexOf("charset=");
			if(index >=0) charset = type.substring(index+7);
			return new InputStreamReader(c.getInputStream(), charset);
		} catch (Exception e) {
			throw new ResourceException(e);
		} 
	}

	@Override
	public String getMimeType() {
		if(type == null) {
			try {
				URLConnection c = url.openConnection();
				c.setDoOutput(false);
				//c.setDoInput(false);
				c.connect();
				type = c.getContentType();
				c.getInputStream().close();
			} catch (IOException e) {
			}
		}
		return type;
	}

	@Override
	public void remove() throws ResourceException {
		Sardine sardine = parent.sardine;
		try {
			sardine.delete(url.toExternalForm());
			url = null;
			parent = null;
			name = null;
		} catch (IOException e) {
			throw new ResourceException(e);
		}
	}

	@Override
	public void setName(String name) throws ResourceException {
		Sardine sardine = parent.sardine;
		if(name == null || name.contains("/") || name.equals(".") || name.equals("..") || name.isEmpty()) throw new ResourceException("illegal name " + name);
		try {
			URL destinationUrl = new URL(url, name);
			String sourceUrl = url.toExternalForm();
			if(sardine.exists(destinationUrl.toExternalForm()))
				throw new ResourceException("name exists "  + name);
			sardine.move(sourceUrl, destinationUrl.toExternalForm());
			this.name = name;
			this.url = destinationUrl;
		} catch (IOException e) {
			throw new ResourceException(e);
		}
	}

	public WebResource(URL url, String name, WebContainer parent, String type) {
		super();
		this.url = url;
		this.name = name;
		this.parent = parent;
		if("httpd/url".equals(type)) type = null;
		this.type = type;
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
		WebResource other = (WebResource) obj;
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

}
