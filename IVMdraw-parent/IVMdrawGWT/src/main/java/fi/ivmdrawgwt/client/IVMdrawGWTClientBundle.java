package fi.ivmdrawgwt.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;

import fi.ivmdrawgwt.client.IVMdrawCssResource;

public interface IVMdrawGWTClientBundle extends ClientBundle {

	@Source("fi/ivmdrawgwt/client/css/IVMdrawGWT.css")
	public  IVMdrawCssResource getKladjeGWTCSS();
	
	@Source("fi/ivmdrawgwt/client/images/goedvink-new.png")
  	public ImageResource goedvinkResource();
   
    
  	@Source("fi/ivmdrawgwt/client/images/foutkruis-new.png")
  	public ImageResource foutkruisResource();
  	
  	@Source("fi/ivmdrawgwt/client/images/bin.png")
  	public ImageResource binResource();
  	
  	@Source("fi/ivmdrawgwt/client/images/feedback.png")
  	public ImageResource feedbackResource();

}
