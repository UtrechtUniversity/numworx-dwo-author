package fi.wiskopdr.cbook.rm;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.cbook.cbookif.rm.ResourceContainer;
import org.cbook.cbookif.rm.ResourceManager;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;

public class WebManager implements ResourceManager {

	private URL root;
	private String widget;
	private String unit, instance, student;
	private Sardine sardine;
	private WebContainer global,perinstance,perstudent;
	
	
	@Override
	public ResourceContainer getWidgetContainer() {
		if(global != null)
			return global;
		URL url = null;
		try {
			url = new URL(root, "Widget/" + this.widget + "/");
			sardine.createDirectory(url.toExternalForm());
		} catch (IOException _) {
		}
		return global = new WebContainer(url, "/", sardine);
	}

	public WebManager(URL root, String widget, String unit, String instance,
			String student, String username, String password) {
		this.root = root;
		this.widget = widget;
		this.unit = unit;
		this.instance = instance;
		this.student = student;
		this.sardine = SardineFactory.begin(username, password);
	}

	@Override
	public ResourceContainer getInstanceContainer() {
		if( perinstance != null)
			return perinstance;
		URL url = null;
		try {
			url = new URL(root, "Instance/" + this.unit + "/"+ this.instance + "/");
			sardine.createDirectory(url.toExternalForm());
		} catch (IOException _) {
		}
		return perinstance = new WebContainer(url, "/", sardine);
	}

	@Override
	public ResourceContainer getStudentContainer() {
		if(perstudent != null)
			return perstudent;
		URL url = null;
		try {
			url = new URL(root, "Student/" + this.unit + "/"+ this.student + "/" + this.instance + "/");
			sardine.createDirectory(url.toExternalForm());
		} catch (IOException _) {
		}
		return perstudent = new WebContainer(url, "/", sardine);
	}

}
