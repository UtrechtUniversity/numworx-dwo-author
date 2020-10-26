package fi.previewhtml;

import java.applet.Applet;

public class TestAPI extends DefaultAPI {

	String data = "{\"mode\":\"0\",\"opdracht_1_1\":{\"titel\":\"Titel\\n\",\"tekst2\":\"\\n\",\"hasAntwoordVak\":false,\"tekst\":\"\\n\",\"hasTitle\":false,\"scoreMax\":0,\"interactiePanelLaunchData\":[null,null,null,null,null],\"scheidingX\":425,\"randVarString\":\"\\n\",\"eindX\":770},\"$IMAGE$MAP$\":{},\"bgcolor\":\"#FFFFFF\",\"aantalOpdrachten_1\":\"1\",\"opnieuwMogelijk\":\"false\",\"language\":\"nl\",\"instellingen\":{\"opnieuw\":false,\"margeBoven\":10,\"pagina\":false,\"timeLimit\":60,\"eerderGeenCorr\":false,\"fontOvererving\":false,\"docHeight\":300,\"tweeHoofdletterVar\":false,\"margeOnder\":10,\"zelftoetsGeschiedenis\":false,\"condPerc\":100,\"zelftoetsHighScore\":false,\"combinedComponents\":false,\"hasObjectives\":false,\"navigatieSize\":12,\"itemOpnieuw\":false,\"globalParam\":false,\"fontName\":\"SansSerif\",\"vorigeKnopZichtbaar\":false,\"fontSize\":12,\"fToets\":true,\"woordFormule\":false,\"margeRechts\":10,\"condNav\":false,\"fontOverervingForm\":false,\"zelftoetsGeenCorr\":false,\"voortgang\":false,\"keyboardNr\":0,\"docWidth\":425,\"writeMathSetNr\":0,\"scoresZichtbaar\":true,\"hasLayers\":false,\"bolletjesZichtbaar\":true,\"margeLinks\":10,\"timer\":false,\"significantie\":false,\"formTimes\":true,\"diffOperatoren\":false,\"abcDeelOpdr\":false,\"maalTeken\":false,\"volgendeKnopZichtbaar\":false,\"hasMisconceptions\":false,\"checkPerOpdracht\":false,\"templateEdit\":false,\"aftrekCorrectieZelftoets\":5,\"hoekGraden\":false},\"activiteit_1\":\"Onderdeel 1\",\"gekoppeldeOpdrachten\":\"false\",\"aantalActiviteiten\":\"1\"}";
	
	
	
	public TestAPI() {
		// TODO Auto-generated constructor stub
	}

	public TestAPI(Applet applet) {
		super(applet);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String LMSGetValue(String key) {
		if ("cmi.launch_data".equals(key)) return data;
		return super.LMSGetValue(key);
	}

	@Override
	public String LMSSetValue(String iDataModelElement, String iValue) {
		// TODO Auto-generated method stub
		return super.LMSSetValue(iDataModelElement, iValue);
	}

}
