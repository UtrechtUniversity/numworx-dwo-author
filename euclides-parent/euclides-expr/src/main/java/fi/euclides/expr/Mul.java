package fi.euclides.expr;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Exact;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DefaultAdapter;

public class Mul extends Som {

	public static Mul INSTANCE = new Mul();

	public Mul() {
		super("*");
	}

	Numbers eval(Label[] ll) {
		return Numbers.mul(ll[0].value, ll[1].value);
	}

	Numbers[] vectorExtra(Numbers vect)
	{
		Punt u = getModel().getU();
		Punt o = getModel().getO();
		Numbers ux = Numbers.sub(u.getX(), o.getX());
		Numbers uy = Numbers.sub(o.getY(), u.getY());
		Numbers vx = Numbers.neg(uy);
		Numbers vy = ux;
		Numbers cx = Numbers.real(vect);
		Numbers cy = Numbers.imag(vect);
		Numbers x = Numbers.add(Numbers.mul(cx, ux), Numbers.mul(cy, vx));
		Numbers y = Numbers.add(Numbers.mul(cx, uy), Numbers.mul(cy, vy));
		
		return new Numbers[] { 
			x,
			Numbers.neg(y)
		};
	}
	
	// special cases: n * hoek n = 0, 1/2, 1, 3/2, 2, 5/2, ...
	// n in { n elem Q | n*2 elem Z }
	
	
	
	
	
	protected void recalc(Label l, Label[] labels) {
		if(isHoek(labels[1]) && labels[0].getState() == Label.CONSTANT)
		{
			fixextra(labels);
			Numbers v = labels[0].value;
			double vv = v.doubleValue();
			long ii = (long)(vv);
			
//			Numbers[] extra1;
//			Numbers[] extra = (Numbers[]) labels[1].extra;
//			extra  = new Numbers[] { extra[0], extra[1] };
//			extra1 = new Numbers[] { Numbers.ONE,Numbers.ZERO};
//			if ( v instanceof Exact && ii == vv )
//			{
//				ii = Math.abs(ii);
//				while(ii != 0)
//				{
//					if((ii & 1) == 1)
//						addh(extra, extra1);
//					addh(extra, extra);
//					ii >>>= 1;	
//				}
//				if(vv < 0)
//				{
//					extra1[1] = Numbers.neg(extra1[1]);
//				}
//				
//				setAngleValue(l, extra1[0], extra1[1]);
			
			if ( v instanceof Exact && ii == vv )
			{
				Numbers extra1 = Numbers.ONE;
				Numbers[] e = labels[1].getAdapter().adapt(Numbers[].class);
				Numbers extra  = Numbers.createComplex(e[0], e[1]);
				ii = Math.abs(ii);
				while(ii != 0)
				{
					if((ii&1) == 1)
						extra1 = Numbers.mul(extra,extra1);
					extra = Numbers.sqr(extra);
					ii >>>=1;
				}
				if(vv<0)
					extra1 = Numbers.conj(extra1);
				setAngleValue(l, Numbers.real(extra1), Numbers.imag(extra1));
				
				return;
			}
		}
		
		if(isVector(labels[1]))
		{
			fixextra(labels);
			l.setState(Label.VECTOR);
			Numbers v = eval(labels);
			DefaultAdapter.getDefault(l).put(vectorExtra(v));
			setStringValue(l,v);
			return;
		}
		super.recalc(l, labels);
	}

	private void addh(Numbers[] extra, Numbers[] extra1) {
		Numbers c0 = extra[0];
		Numbers s0 = extra[1];
		Numbers c1 = extra1[0];
		Numbers s1 = extra1[1];
		Numbers s = Numbers.add(Numbers.mul(s0,c1), Numbers.mul(c0,s1));
		Numbers c = Numbers.sub(Numbers.mul(c0,c1), Numbers.mul(s0,s1));
		extra1[0] = c;
		extra1[1] = s;
	}

	/* (non-Javadoc)
	 * @see fi.euclides.proof.LabelDelegate#createDepend(int)
	 */
	public Destroyable[] createDepend(int args) {
		return new Label[args];
	}

	/* (non-Javadoc)
	 * @see fi.euclides.proof.LabelDelegate#define(fi.euclides.model.Label)
	 */
	public boolean define(Label l) {
		if(l.getDepend().length>2)
		{
			Destroyable[] d = null,depend = l.getDepend();
			Destroyable mul = depend[0];
			for(int i = 1; i < depend.length; i++) {
				d = createDepend(2);
				d[0] = mul;
				d[1] = depend[i];
				if(i < depend.length-1)
					mul  =	define(d);
			}
			l.setDepend(d);
		}
		return super.define(l);
	}


}
