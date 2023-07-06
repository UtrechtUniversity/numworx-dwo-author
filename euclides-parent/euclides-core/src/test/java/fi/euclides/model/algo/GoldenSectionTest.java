package fi.euclides.model.algo;

import fi.euclides.model.algo.GoldenSectionSearch.Function;
import junit.framework.TestCase;

public class GoldenSectionTest extends TestCase {
	  public  void testgss() {
		    Function f = (x)->Math.pow(x-2,2);
		    double a = 1;
		    double b = 5;
		    double tol = 1e-5;
		    double [] ans = GoldenSectionSearch.gss(f,a,b,tol);
		    System.out.println("[" + ans[0] + "," + ans[1] + "]");
		    // [1.9999959837979107,2.0000050911830893]  
		    assertEquals("ans 0",1.9999959837979107, ans[0], 1E-17);
		    assertEquals("ans 1",2.0000050911830893, ans[1], 1E-17);
		  }

}
