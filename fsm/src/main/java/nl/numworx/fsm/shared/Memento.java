package nl.numworx.fsm.shared;

import fi.euclides.model.Model;

public class Memento extends fi.euclides.persist.Memento {
	public Model getModel() {
		return model;
	}
}
