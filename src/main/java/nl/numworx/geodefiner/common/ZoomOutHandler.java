package nl.numworx.geodefiner.common;
import fi.euclides.model.math.Numbers;

public class ZoomOutHandler extends ZoomInHandler {

	public ZoomOutHandler(String string) {
		super(string, Numbers.createRational(10, 11));
	}

}
