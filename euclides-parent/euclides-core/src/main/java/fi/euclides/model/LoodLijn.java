package fi.euclides.model;

import fi.euclides.model.math.Numbers;

public class LoodLijn extends LijnPuntCombi<Lijn> {

	/* (non-Javadoc)
	 * @see fi.euclides.model.Lijn#getDXn()
	 */
	public Numbers getDXn() {
		return Numbers.neg(getDestroyable().getDYn());
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.Lijn#getDYn()
	 */
	public Numbers getDYn() {
		return getDestroyable().getDXn();
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.Lijn#getX2n()
	 */
	public Numbers getX2n() {
		return Numbers.sub(punt.getX(), getDestroyable().getDYn());
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.Lijn#getY2n()
	 */
	public Numbers getY2n() {
		// TODO Auto-generated method stub
		return Numbers.add(punt.getY(), getDestroyable().getDXn());
	}

	public static final String TYPE = "lL";

	/**
	 * @param lijn
	 * @param punt
	 */
	public LoodLijn(Lijn lijn, Punt punt) {
		super(lijn, punt);
	}

	public LoodLijn() {
		super();
	}

	public String key() {
		return TYPE;
	}

	public static boolean isLoodRecht(Lijn l1, Lijn l2) {
		if(l1 instanceof LoodLijn)
		{
			LoodLijn ll = (LoodLijn)l1;
			if(ll.getDestroyable()==l2)
				return true;
		}
		if(l2 instanceof LoodLijn)
		{
			LoodLijn ll = (LoodLijn)l2;
			if(ll.getDestroyable()==l1)
				return true;
		}
		return false;
	}
}
