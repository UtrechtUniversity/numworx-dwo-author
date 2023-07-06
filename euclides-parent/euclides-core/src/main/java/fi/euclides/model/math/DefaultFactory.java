package fi.euclides.model.math;

public class DefaultFactory implements ExactFactory {
	
	public static final ExactFactory INSTANCE = new DefaultFactory();
	protected DefaultFactory() {}
	private static class Zero extends Exact
	{

		protected Numbers sqrt() {
			return ZERO;
		}

		public double doubleValue() {
			return 0;
		}

		public String toString() {
			return "0";
		}

		protected int signum() {
			return 0;
		}

		protected Numbers neg() {
			return ZERO;
		}

		protected Numbers sqr() {
			return ZERO;
		}

		/* (non-Javadoc)
		 * @see fi.euclides.model.math.Numbers#abs()
		 */
		@Override
		protected Numbers abs() {
			return ZERO;
		}

		/* (non-Javadoc)
		 * @see fi.euclides.model.math.Numbers#real()
		 */
		@Override
		protected Numbers real() {
			return ZERO;
		}

		/* (non-Javadoc)
		 * @see fi.euclides.model.math.Numbers#imag()
		 */
		@Override
		protected Numbers imag() {
			return ZERO;
		}

		/* (non-Javadoc)
		 * @see fi.euclides.model.math.Numbers#conj()
		 */
		@Override
		protected Numbers conj() {
			return ZERO;
		}

		/* (non-Javadoc)
		 * @see fi.euclides.model.math.Numbers#round()
		 */
		@Override
		protected Numbers round() {
			return ZERO;
		}

		/* (non-Javadoc)
		 * @see fi.euclides.model.math.Numbers#floor()
		 */
		@Override
		protected Numbers floor() {
			return ZERO;
		}

		/* (non-Javadoc)
		 * @see fi.euclides.model.math.Numbers#ceiling()
		 */
		@Override
		protected Numbers ceiling() {
			return ZERO;
		}
		
	}
	
	private static Zero ZERO;
	private static Numbers ONE;
	private static Numbers TWO;
	private static Numbers PI;
	public Exact getZero() {
		if(ZERO == null)
			ZERO = new Zero();
		return ZERO;
	}

	public Numbers getOne() {
		if(ONE == null)
			ONE = new FloatingPoint(1);
		return ONE;
	}

	public Numbers getTwo() {
		if(TWO == null)
			TWO = new FloatingPoint(2);
		return TWO;
	}
	
	public Numbers getPi() {
		if(PI == null)
			PI = new FloatingPoint(Math.PI);
		return PI;
	}

	public Numbers createInteger(int value) {
		return new FloatingPoint(value);
	}

	public Numbers createRational(long teller, long noemer) {
		return new FloatingPoint((double)teller / (double) noemer);
	}

	public Numbers add(Exact a, Exact b) {
		return new FloatingPoint(a.doubleValue()+b.doubleValue());
	}

	public Numbers sub(Exact a, Exact b) {
		return new FloatingPoint(a.doubleValue()-b.doubleValue());
	}

	public Numbers mul(Exact a, Exact b) {
		return new FloatingPoint(a.doubleValue()*b.doubleValue());
	}

	public Numbers div(Exact a, Exact b) {
		return new FloatingPoint(a.doubleValue()/b.doubleValue());
	}

	public Numbers valueOf(String string) {
		if(ZERO.toString().equals(string))
			return ZERO;
		return Numbers.createDouble(Double.parseDouble(string));
	}

	public String toString(Exact value) {
		return value.toString();
	}

}
