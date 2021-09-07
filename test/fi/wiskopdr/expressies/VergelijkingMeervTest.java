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


}
