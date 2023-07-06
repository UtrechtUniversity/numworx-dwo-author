package fi.beans.browser;

public abstract class Console {
	abstract public void log(String object);
	
	public void log(Object o) {
		log(String.valueOf(o));
	}
	
	public void log(Object first, Object... message) {
		StringBuilder sb = new StringBuilder();
		sb.append(first);
		for(Object item : message) {
			sb.append(item);
		}
		log(sb.toString());
	}
	
	public void error(Object msg) {
		log(msg);
	}

	public void warn(Object msg) {
		log(msg);
	}
	public void info(Object msg) {
		log(msg);
	}
	public void debug(Object msg) {
		log(msg);
	}
}
