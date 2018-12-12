package fi.euclides.model.algo;

public class GoldenSectionSearch {
  public static final double invphi = (Math.sqrt(5.0)-1)/2.0;
  public static final double invphi2 = (3-Math.sqrt(5.0))/2.0;

  public interface Function {
    double of(double x);
  }

  // returns subinterval of [a,b] containing minimum of f                                                                                                   
  public static double[] gss(Function f, double a, double b, double tol) {
    return gss(f,a,b,tol,b-a,true,0,0,true,0,0);
  }
  private static double[] gss(Function f, double a, double b, double tol,
                              double h, boolean noC, double c, double fc,
                              boolean noD, double d, double fd) {
    if (Math.abs(h) <= tol) {
      return new double[] { a, b };
    }
    if (noC) {
      c = a + invphi2*h;
      fc = f.of(c);
    }
    if (noD) {
      d = a + invphi*h;
      fd = f.of(d);
    }
    if (fc < fd) {
      return gss(f,a,d,tol,h*invphi,true,0,0,false,c,fc);
    } else {
      return gss(f,c,b,tol,h*invphi,false,d,fd,true,0,0);
    }
  }

  public static void main(String[] args) {
    Function f = (x)->Math.pow(x-2,2);
    double a = 1;
    double b = 5;
    double tol = 1e-5;
    double [] ans = gss(f,a,b,tol);
    System.out.println("[" + ans[0] + "," + ans[1] + "]");
    // [1.9999959837979107,2.0000050911830893]                                                                                                              
  }
}