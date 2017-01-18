package fi.euclides.model;

import fi.euclides.model.math.Numbers;

public class ParallelLijn extends LijnPuntCombi<Lijn> {

	public static final String TYPE = "lP";

	public Numbers getDXn() {
		return getDestroyable().getDXn();
	}
	public Numbers getDYn() {
		return getDestroyable().getDYn();
	}
	
	
	/* (non-Javadoc)
	 * @see fi.euclides.model.Lijn#getX2n()
	 */
	public Numbers getX2n() {
		return Numbers.add(punt.getX(), getDestroyable().getDXn());
	}
	
	/* (non-Javadoc)
	 * @see fi.euclides.model.Lijn#getY2n()
	 */
	public Numbers getY2n() {
		return Numbers.add(punt.getY(), getDestroyable().getDYn());
	}
	
	/**
	 * @param lijn
	 * @param punt
	 */
	public ParallelLijn(Lijn lijn, Punt punt) {
		super(lijn, punt);
	}

	public ParallelLijn() {
		// TODO Auto-generated constructor stub
	}

	public String key() {
		return TYPE;
	}

	public static boolean isParallel(Lijn l1, Lijn l2)
	{
		if(l1 instanceof ParallelLijn)
		{
			if(l2 == ((ParallelLijn)l1).getDestroyable())
				return true;
		}
		if(l2 instanceof ParallelLijn)
		{
			if(l1 == ((ParallelLijn)l2).getDestroyable())
				return true;
		}
		return false;
	}
	
	
}
