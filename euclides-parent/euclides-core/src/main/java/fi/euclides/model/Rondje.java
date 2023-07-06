package fi.euclides.model;

import fi.euclides.model.math.Numbers;

abstract class Rondje extends Destroyable {

	abstract Punt getRadius();

	abstract boolean isr2c();

	abstract Punt getCenter();

	abstract double getD();

	abstract Numbers getR2n();

	abstract double getR();
	
	abstract boolean contains(Punt p);
}
