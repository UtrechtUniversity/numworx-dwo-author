package fi.wiskopdr.cbook.rm;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.cbook.cbookif.rm.Resource;
import org.cbook.cbookif.rm.ResourceContainer;
import org.cbook.cbookif.rm.ResourceException;

import com.github.sardine.Sardine;
import com.sun.org.apache.xml.internal.serializer.ToUnknownStream;

public class RootContainer extends WebContainer {

	Boolean exists;
	
	public RootContainer(URL url, String name, Sardine sardine) {
		super(url, name, sardine);

	}

	@Override
	public void setName(String name) throws ResourceException {
		readonly();
	}

	@Override
	public Resource open(String name) throws ResourceException {
		checkExists();
		if(Boolean.FALSE == exists)
			return null;
		
		return super.open(name);
	}

	@Override
	WebContainer getRoot() {
		return this;
	}

	@Override
	public Resource create(String name, InputStream in, String mimetype)
			throws ResourceException {
		makeExists();
		return super.create(name, in, mimetype);
	}

	private void makeExists() throws ResourceException {
		if(exists != Boolean.TRUE)
		{
			String u = url.toExternalForm();
			u  = strip(u); // String / 
			try {
				makeExists(u);
			} catch (IOException e) {
				throw new ResourceException(e);
			}
			exists = Boolean.TRUE;
		}
		
	}

	private void makeExists(String u) throws IOException {
		if(! sardine.exists(u))
		{
			try {
				sardine.createDirectory(u);
				return;
			} catch (Exception e) {
				System.err.println(e);
			}
			String p = u.substring(0, u.lastIndexOf('/'));
			makeExists(p);
			sardine.createDirectory(u);
		}
	}

	@Override
	public Resource create(String name, URL in, String mimetype)
			throws ResourceException {
		makeExists();
		return super.create(name, in, mimetype);
	}

	@Override
	public Resource create(String name, URL in) throws ResourceException {
		makeExists();
		return super.create(name, in);
	}

	@Override
	public Resource create(String name, Resource resource)
			throws ResourceException {
		makeExists();
		return super.create(name, resource);
	}

	@Override
	public ResourceContainer createContainer(String name)
			throws ResourceException {
		makeExists();
		return super.createContainer(name);
	}

	@Override
	public Resource[] list() throws ResourceException {
		checkExists();
		if(Boolean.FALSE == exists)
			return new Resource[0];
		return super.list();
	}

	private void checkExists() throws ResourceException {
		if(exists == null)
		{
			try {
				exists = Boolean.valueOf(sardine.exists(url.toExternalForm()));
			} catch (IOException e) {
				throw new ResourceException(e);
			}
		}
		
	}


}
