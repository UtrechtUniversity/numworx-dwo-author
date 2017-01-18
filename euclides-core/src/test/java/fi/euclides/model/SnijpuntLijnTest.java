package fi.euclides.model;

import junit.framework.TestCase;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Lijn;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp2;
import fi.euclides.model.PuntenLijn;
import fi.euclides.model.math.Numbers;

public class SnijpuntLijnTest extends TestCase {


	public void testSnijPunt() {
		Punt a,b,c,d,e,f,g;
		
		a = new VrijPunt(0,0);
		b = new VrijPunt(10,10);
		c = new VrijPunt(0,10);
		d = new VrijPunt(10,0);
		e = new VrijPunt(20,5);
		f = new VrijPunt(5, 5);
		g = new VrijPunt(5,0);
		Kegelsnede2 k = new Kegelsnede2(a,b,c,d,e);
		PuntenLijn  l = new PuntenLijn(g,f);
		SnijpuntLijn pkl = new SnijpuntLijn(k, l, Numbers.ZERO, Numbers.ZERO);
		pkl.recalc();
		System.out.println(pkl.getXd());
		System.out.println(pkl.getYd());
		System.out.println(k.incident(pkl));		
	}

}