package fi.wiskopdr.cbook.rm;

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
	
	@Override
	public ResourceContainer getWidgetContainer() {
		URL widget = null;
		try {
			widget = new URL(root, "Widget/" + this.widget + "/");
		} catch (MalformedURLException _) {
		}
		return new WebContainer(widget, "/", sardine);
	}

	public WebManager(URL root, String widget, String unit, String instance,
			String student, String username, String password) {
		super();
		this.root = root;
		this.widget = widget;
		this.unit = unit;
		this.instance = instance;
		this.student = student;
		this.sardine = SardineFactory.begin(username, password);
	}

	@Override
	public ResourceContainer getInstanceContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceContainer getStudentContainer() {
		// TODO Auto-generated method stub
		return null;
	}

}
