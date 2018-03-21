package nl.numworx.geodefiner;

import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.FormuleParser;
import nl.numworx.geodefiner.common.DefaultRandomizer;
import nl.numworx.geodefiner.common.Randomizer;

@Singleton
public class WiskOpdrRandomizer extends DefaultRandomizer implements Randomizer {

	@Inject public WiskOpdrRandomizer(@Named("random") Map<String,Number> random) {
		super(random);
	}

	public String randomize(Map<String, Number> random, String text) {
		Hashtable randomVarWaarden = new Hashtable(random);
		String[] randomVarNamen = random.keySet().toArray(new String[random.size()]);
		Locale lcl = WiskOpdr.language;
		try {
			WiskOpdr.language = Locale.ROOT; // POSIX: decimal point
			String randomizeString = FormuleParser.randomizeString(text,randomVarNamen,randomVarWaarden);
			return randomizeString;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			WiskOpdr.language = lcl;
		}
		return super.randomize(text);
	}

}
