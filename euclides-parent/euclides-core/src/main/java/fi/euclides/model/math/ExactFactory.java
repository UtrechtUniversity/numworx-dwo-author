package fi.euclides.model.math;

public interface ExactFactory {

	Exact getZero();

	Numbers getOne();

	Numbers getTwo();
	
	Numbers getPi();

	Numbers createInteger(int value);

	Numbers createRational(long teller, long noemer);

	Numbers add(Exact a, Exact b);

	Numbers sub(Exact a, Exact b);

	Numbers mul(Exact a, Exact b);

	Numbers div(Exact a, Exact b);

	Numbers valueOf(String string);

	String toString(Exact value);

}
