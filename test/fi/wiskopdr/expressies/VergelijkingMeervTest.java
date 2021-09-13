package fi.wiskopdr.expressies;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.FormuleParser;

public class VergelijkingMeervTest {

  WiskOpdr w;
  @Before
  public void setUp() throws Exception {
    w = new WiskOpdr();
  }

  @Test
  public void test() {
      String vgl = "2*x=x+x";
      FunctieMVDefSet functieMVDefSet = new FunctieMVDefSet();
      VergelijkingMeerv check = FormuleParser.parseVergelijking("$f" + vgl + "@", functieMVDefSet);      
      boolean result = check.isOplossing(12.34);
      assertTrue("eindoplossing 12.34", result);
  }

  @Test
  public void test2() {
      String vgl = "123=100+23";
      FunctieMVDefSet functieMVDefSet = new FunctieMVDefSet();
      VergelijkingMeerv check = FormuleParser.parseVergelijking("$f" + vgl + "@", functieMVDefSet);      
      boolean result = check.isOplossing(12.34);
      assertTrue("eindoplossing 12.34", result);
  }

  @Test
  public void test3() {
    String vgl = "$dx*a$nx@@=a";
    FunctieMVDefSet functieMVDefSet = new FunctieMVDefSet();
    VergelijkingMeerv check = FormuleParser.parseVergelijking("$f" + vgl + "@", functieMVDefSet);      
    boolean result = check.isOplossing(12.34); // 2 varnamen  'a' en 'x'
    assertFalse("eindoplossing 12.34", result);
  }
  @Test
  public void test4() {
    String vgl = "$dx*2$nx@@=2";
    FunctieMVDefSet functieMVDefSet = new FunctieMVDefSet();
    VergelijkingMeerv check = FormuleParser.parseVergelijking("$f" + vgl + "@", functieMVDefSet);
    BasisExpressie exp = new BasisExpressie("x");
    boolean result = check.isOplossing(exp, "x"); //
    assertTrue("eindoplossing x", result);
  }
  @Test
  public void test5() {
    String vgl = "$dx^2$nx@@=2*x";
    FunctieMVDefSet functieMVDefSet = new FunctieMVDefSet();
    VergelijkingMeerv check = FormuleParser.parseVergelijking("$f" + vgl + "@", functieMVDefSet);
    BasisExpressie exp = new BasisExpressie("x");
    boolean result = check.isOplossing(exp, "x"); //
    assertTrue("eindoplossing x", result);
  }
  
  

}
