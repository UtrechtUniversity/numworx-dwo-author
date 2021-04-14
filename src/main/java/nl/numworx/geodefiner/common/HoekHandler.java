package nl.numworx.geodefiner.common;


import fi.euclides.model.Label;

public class HoekHandler extends fi.euclides.proof.HoekHandler {

	public HoekHandler(String string) {
		super(string);
	}

	public HoekHandler() {
		super();
	}

	@Override
	protected String hoekAsString(Label label) {
		AngleType type = label.adapt(AngleType.class);
		boolean rad = type == null || type == AngleType.RAD;
		return super.hoekAsString(label.value, rad);
	}



}
