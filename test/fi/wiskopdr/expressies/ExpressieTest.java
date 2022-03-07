package fi.wiskopdr.expressies;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.FormuleParser;

public class ExpressieTest {

  WiskOpdr w;
  @Before
  public void setUp() throws Exception {
    w = new WiskOpdr();
  }

  @Test
  public void decideWithoutCASTest() {
// 0 variables
	assertTrue( decide("0=0"));
    assertTrue( decide("2=1+1"));
    assertTrue( decide("-1=i*i")); // too complex
    assertTrue( decide("sin(\u03C0)=0"));
// 1 variable
    assertTrue(  decide("x+x=x*2"));
    assertTrue(  decide("y+y=2*y")); // also y
// d/dx
    assertTrue(  decide("$dx^2$nx@@=x+x"));
    
// 2 variables    
    assertFalse( decide("x+y=y+x"));
   
// wrong
    assertFalse( decide( "1+1=3"));
    assertFalse( decide( "x=0"));
    
  }
  
  
  @Test
  public void ongelijkTest() {
    
    assertTrue (decide( "1>0"));
    assertFalse(decide(" 2x>2x"));
  }

  private boolean decide(String s) {

      boolean casNodig = s.contains("$i") || s.contains("$T") || s.contains("$P");

      if (casNodig) return false;
    
      VergelijkingMeerv vgl = FormuleParser.parseVergelijking("$f" + s + "@");
	  Vector namen = vgl.geefVarN();
	  if (namen.contains("i")) {
	    // complex? 
	    return vgl.isOplossing(new BasisExpressie("i"), "i");
	  }
	  if (namen.size() == 0) {
        boolean result = vgl.isOplossing(Math.E);
        return result;
	  }
	  if (namen.size()==1) {
	       String var = namen.get(0).toString();
	       boolean result =  vgl.isOplossing(new BasisExpressie(var), var);
//	       if (result) return true;
//	       double[] subst = { Math.PI, Math.E, 12.34, 5431.2356, -432.4521 };
//	       result = vgl.isOplossing(subst);
	       return result;
	  }
	  
	  return false; 
  }  
  
  
  // x*2 = x + x
  // d/dx x^2 = 2x
  // 2 = 1 + 1
  // sin(pi)=0
  // i^2 = -1
  // 2 > 1
  // d/dx log(x) = 1 / x
  

}
