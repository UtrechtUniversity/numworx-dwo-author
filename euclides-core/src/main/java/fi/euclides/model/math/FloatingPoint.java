package fi.euclides.model.math;

public class FloatingPoint extends Numbers {

	double value;

	/**
	 * 
	 */
	public FloatingPoint() {
		super();
	}
	public double doubleValue() {
		return value;
	}	

	public void setValue(double value)
	{
		this.value = value;
	}
	
	public String toString() {
		return String.valueOf(value);
	}
	
	FloatingPoint(double value)
	{
		setValue(value);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return (int)value;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final FloatingPoint other = (FloatingPoint) obj;
		if( Double.isNaN(value))
			return Double.isNaN(other.value);
		if (value != other.value)  // Niet helemaal goed, Nan != Nan 
			return false;
		return true;
	}
	
	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	public int signum()
	{
		return value<0.0?-1:value>0.0?+1:0;
	}
	
	Numbers sqrt() {
		return createDouble(Math.sqrt(value));
	}
	
}
