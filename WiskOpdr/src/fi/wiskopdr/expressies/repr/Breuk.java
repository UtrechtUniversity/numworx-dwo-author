package fi.wiskopdr.expressies.repr;

class Breuk extends Veelterm {
	Number a,b;

	Breuk(Object o, Number a, Number b) {
		super(o);
		this.a = a;
		this.b = b;
	}

	public Breuk(Object s1, Object s2, String r) {
		this(r, (Number)s1, (Number)s2);
	}

}
