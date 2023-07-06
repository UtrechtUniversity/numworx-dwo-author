package nl.numworx.geodefiner.common;

import java.util.HashMap;
import java.util.Map;

public class DefaultRandomizer implements Randomizer {

	private Map<String, Number> map;

	public DefaultRandomizer() {
		this(new HashMap<String,Number>());
	}

	public DefaultRandomizer(Map<String, Number> map) {
		this.map = map;
	}

	public Map<String, Number> getRandom() {
		return map;
	}

	public void setRandom(Map<String, Number> map) {
		this.map = map;
	}

	@Override
	public String randomize(String input) {
		return randomize(map, input);
	}

	protected String randomize(Map<String, Number> random, String text) {
		for(Map.Entry<String, Number> entry: random.entrySet()) {
			String key = "#" + entry.getKey() + "#";
			text = text.replaceAll(key, "(" + entry.getValue().toString() + ")");
		}
		return text;
	}

	
}
