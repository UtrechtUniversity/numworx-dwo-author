package fi.euclides.event;

import fi.euclides.model.Lijn;
import fi.euclides.model.LijnPuntCombi;
import fi.euclides.model.ParallelLijn;

public class AddParallelHandler extends AddLoodLijnHandler {

	Lijn build() {
		return getModel().buildParallelLijn();
	}

	LijnPuntCombi<Lijn> newLijnCombi() {
		return new ParallelLijn();
	}
}
