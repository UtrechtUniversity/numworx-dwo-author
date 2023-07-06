package nl.numworx.geodefiner.ui.color;

public enum Color {
	black(0),
	red(0xFF0000),
	magenta(0xFF00FF),
	green(0x00FF00),
	cyan(0x00FFFF),
	blue(0x0000FF),
	yellow(0xFFFF00),
	white(0xFFFFFF)

	;
	final private int value;
	Color(int value) { this.value = value; }
	int intValue() { return value; }
	public String toString() {
		return name();
	}
}
