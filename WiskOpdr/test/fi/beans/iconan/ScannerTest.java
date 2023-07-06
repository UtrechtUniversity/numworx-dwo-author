package fi.beans.iconan;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.Test;

public class ScannerTest {

  @Test
  public void test() {
    Scanner scan = new Scanner("w1234h321");
    scan.useDelimiter("[whv]");
    int n = scan.nextInt();
    System.out.println(n);
    n = scan.nextInt();
    System.out.println(n);
    scan.useDelimiter("");
    System.out.println(scan.hasNext());
  }

}
