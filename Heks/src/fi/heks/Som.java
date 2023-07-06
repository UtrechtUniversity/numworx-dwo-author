package fi.heks;

import java.util.*;

public class Som {
	public static int PLUS = 1;
	public static int MIN = 2;
	public static int PLUSMIN = 0;
	public static int MAAL = 3;
	private int term1, term2, uitkomst;
	private String operator;

	public Som(int soort) {
		term1 = (int) (-10 + 20 * Math.random());
		term2 = (int) (-10 + 20 * Math.random());
		if (soort == Som.PLUS) {
			operator = "+";
			uitkomst = term1 + term2;
			if (term2 == 0)
				term2 = (int) (-10 + 9 * Math.random());
			if (term1 > -1 && term2 > -1 && uitkomst > -1) {
				if (term1 != 0)
					term1 = -term1;
				else
					term2 = -term2;
			}
			uitkomst = term1 + term2;
		} else if (soort == Som.MIN) {
			operator = "-";
			uitkomst = term1 - term2;
			if (term2 == 0)
				term2 = (int) (-10 + 9 * Math.random());
			if (term1 > -1 && term2 > -1 && uitkomst > -1) {
				if (term1 != 0)
					term1 = -term1;
				else
					term2 = -term2;
			}
			uitkomst = term1 - term2;

		} else if (soort == Som.PLUSMIN) {
			if (Math.random() > 0.5) {
				operator = "+";
				uitkomst = term1 + term2;
				if (term2 == 0)
					term2 = (int) (-10 + 9 * Math.random());
				if (term1 > -1 && term2 > -1 && uitkomst > -1) {
					if (term1 != 0)
						term1 = -term1;
					else
						term2 = -term2;
				}
				uitkomst = term1 + term2;
			} else {
				operator = "-";
				uitkomst = term1 - term2;
				if (term2 == 0)
					term2 = (int) (-10 + 9 * Math.random());
				if (term1 > -1 && term2 > -1 && uitkomst > -1) {
					if (term1 != 0)
						term1 = -term1;
					else
						term2 = -term2;
				}
				uitkomst = term1 - term2;
			}
		} else if (soort == Som.MAAL) {
			operator = "x";
			uitkomst = term1 * term2;
			if (term2 == 0 || term2 == 1)
				term2 = (int) (-10 + 9 * Math.random());
			if (term1 == 0 || term1 == 1)
				term1 = (int) (-10 + 9 * Math.random());
			if (term1 > -1 && term2 > -1) {
				if (term1 != 0)
					term1 = -term1;
				else
					term2 = -term2;
			}
			uitkomst = term1 * term2;
		}
	}

	public void setState(Hashtable h) {
		int term1 = ((Integer) h.get("term1")).intValue();
		int term2 = ((Integer) h.get("term2")).intValue();
		int uitkomst = ((Integer) h.get("uitkomst")).intValue();
		String operator = (String) h.get("operator");

		this.term1 = term1;
		this.term2 = term2;
		this.uitkomst = uitkomst;
		this.operator = operator;
	}

	public Hashtable getState() {
		int term1 = 0;
		int term2 = 0;
		int uitkomst = 0;
		String operator = null;

		term1 = this.term1;
		term2 = this.term2;
		uitkomst = this.uitkomst;
		operator = this.operator;

		Hashtable h = new Hashtable();
		h.put("term1", new Integer(term1));
		h.put("term2", new Integer(term2));
		h.put("uitkomst", new Integer(uitkomst));
		h.put("operator", operator);

		return h;
	}

	public int geefTerm1() {
		return term1;
	}

	public int geefTerm2() {
		return term2;
	}

	public String geefOperator() {
		return operator;
	}

	public int geefUitkomst() {
		return uitkomst;
	}

	public boolean evalueer(int uitk) {
		if (uitk == uitkomst)
			return true;
		else
			return false;
	}
}
