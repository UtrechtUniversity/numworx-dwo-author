package fi.euclides.model;

import fi.euclides.model.Lijn;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp2;
import fi.euclides.model.math.Numbers;

public class SnijpuntLijn extends PuntOp2<Kegelsnede2, Lijn> {

	public static final String TYPE = "Pkl";
	
	public SnijpuntLijn(Kegelsnede2 lijn1, Lijn lijn2, Numbers x, Numbers y) {
		super(lijn1, lijn2);
		setXY(x,y);
	}

	public SnijpuntLijn() {
		super();
	}

	public String key() {
		return TYPE;
	}

	protected void recalc() {
		Numbers[][] A = lijn1.A;
		Numbers Px = lijn2.getX1n();
		Numbers Py = lijn2.getY1n();
		Punt p = new VrijPunt(Px, Py);
		Numbers Vx = lijn2.getDXn();
		Numbers Vy = lijn2.getDYn();

//        // we have to solve   u t² + 2d t + w = 0  
//        // to intersect line g: X = p + t v  with conic
//        // calc u, d, w:
//        //      u = v.S.v           (S is upper left submatrix of A)
//        //      d = p.S.v + a.v
//        //      w = evaluate(p)
//        
//        // precalc S.v for u and d
//        double SvX = A[0] * g.y - A[3] * g.x;
		double SvX = A[0][0].doubleValue() * Vx.doubleValue() + A[1][0].doubleValue() * Vy.doubleValue();
//        double SvY = A[3] * g.y - A[1] * g.x;
		double SvY = A[0][1].doubleValue() * Vx.doubleValue() + A[1][1].doubleValue() * Vy.doubleValue();
//        double u = g.y * SvX - g.x * SvY;
		double u = Vx.doubleValue() * SvX + Vy.doubleValue() * SvY;
//        double d = px * SvX + py * SvY + A[4] * g.y - A[5] * g.x;
		double d = Px.doubleValue() * SvX + Py.doubleValue() * SvY + A[2][0].doubleValue() * Vx.doubleValue() + A[2][1].doubleValue() * Vy.doubleValue();
//        double w = c.evaluate(px, py);
		double w = lijn1.incident(p).doubleValue();
//        
//        // Erzeugende, Asymptote oder Treffgerade
//        if (kernel.isZero(u)) {
		  if(u == 0) {
			 // Erzeugende oder Asymptote
		  
//            if (kernel.isZero(d)) {
			  if ( d == 0) {
                // Erzeugende
//                if (kernel.isZero(w)) {
				  if( w == 0) {
//                    sol[0].setUndefined();
//                    sol[1].setUndefined();
//                    return INTERSECTION_PRODUCING_LINE;
					  setDefined(false);
					  return;
				  }
                // Asymptote
                  else { // w != 0
//                    sol[0].setUndefined();
//                    sol[1].setUndefined();                    
//                    return INTERSECTION_ASYMPTOTIC_LINE;
                	setDefined(false);
                	return;
                }
            }
            // Treffgerade
            else { // d != 0
                double t1 = -w / (2.0 * d);
//                sol[0].setCoords(px + t1 * g.y, py - t1 * g.x, 1.0d);
//                sol[1].setUndefined();
//                return INTERSECTION_MEETING_LINE;
            	setXY(Px.doubleValue() + t1 * Vx.doubleValue(), Py.doubleValue() + t1 * Vy.doubleValue());
            	lijn2.contains(this);
                return;
            }            
        }
        // Tangente, Sekante, Passante
        else { // u != 0
            double dis = d * d - u * w;
//            // Tangente
//            if (kernel.isZero(dis)) {
              if ( dis == 0) {
            	  double t1 = -d / u;
//                sol[0].setCoords(px + t1 * g.y,  py - t1 * g.x, 1.0);
//                sol[1].setCoords(sol[0]);
//                return INTERSECTION_TANGENT_LINE;
            	  setXY(Px.doubleValue() + t1 * Vx.doubleValue(), Py.doubleValue() + t1 * Vy.doubleValue());
            	  lijn2.contains(this);
            	  return;
              }
            // Sekante oder Passante
            else {
//                // Sekante
                if (dis > 0) {
                    dis = Math.sqrt(dis);
                    // For accuracy, calculate one root using:
                    //     (-d +/- dis) / u
                    // and the other using:
                    //      w / (-d +/- dis)
                    // Choose the sign of the +/- so that d+dis gets larger in magnitude                   
                    boolean swap = d < 0.0;
                    if (swap) {
                        dis = -dis;
                    }
                    double q = -(d + dis);
                    double t1 = swap ? w / q : q / u;
                    double t2 = swap ? q / u : w / q;
//                                        
//                    sol[0].setCoords(px + t1 * g.y, py - t1 * g.x, 1.0);
           setFusedXY(        
                    Px.doubleValue() + t1 * Vx.doubleValue(), Py.doubleValue() + t1 * Vy.doubleValue(),
//                    sol[1].setCoords(px + t2 * g.y, py - t2 * g.x, 1.0); 
           			Px.doubleValue() + t2 * Vx.doubleValue(), Py.doubleValue() + t2 * Vy.doubleValue());
//                    return INTERSECTION_SECANT_LINE;
           			setDefined(lijn2.contains(this));

                    return;
                }
//                // Passante
               else { // dis < 0
//                    sol[0].setUndefined();
//                    sol[1].setUndefined();                    
//                    return INTERSECTION_PASSING_LINE;  
            	   setDefined(false);
               }                
            }
        }        

		
		
	}
	
	
}
