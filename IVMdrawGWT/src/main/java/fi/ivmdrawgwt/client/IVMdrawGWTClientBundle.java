package fi.ivmdrawgwt.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;

import fi.ivmdrawgwt.client.IVMdrawCssResource;

public interface IVMdrawGWTClientBundle extends ClientBundle {

	@Source("fi/ivmdrawgwt/client/css/IVMdrawGWT.css")
	public  IVMdrawCssResource getKladjeGWTCSS();

}
