package fi.mathscratchgwt.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;

public interface MathScratchGWTClientBundle extends ClientBundle {

	@Source("fi/mathscratchgwt/client/css/MathScratchGWT.css")
	public MathScratchCssResource getMathScratchGWTCSS();

	@Source("fi/mathscratchgwt/client/images/formuleschrijf.png")
	public ImageResource formuleschrijfResource();

	@Source("fi/mathscratchgwt/client/images/formuleschrijfaan.png")
	public ImageResource formuleschrijfaanResource();

	@Source("fi/mathscratchgwt/client/images/bin.png")
	public ImageResource binResource();

	@Source("fi/mathscratchgwt/client/images/eye.png")
	public ImageResource eyeResource();

	@Source("fi/mathscratchgwt/client/images/approx.png")
	public ImageResource approxResource();

	@Source("fi/mathscratchgwt/client/images/goedvink-new.png")
	public ImageResource goedvinkResource();

	@Source("fi/mathscratchgwt/client/images/halfvink-new.png")
	public ImageResource halfvinkResource();

	@Source("fi/mathscratchgwt/client/images/foutkruis-new.png")
	public ImageResource foutkruisResource();
}
