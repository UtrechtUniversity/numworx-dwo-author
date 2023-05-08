package fi.euclides.expr;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
/**
 * Som voor arith1.plus(a,b,c,d,...)
 * @author wim
 *
 */
public class SomN extends Som {
	/**
	 * @see fi.euclides.proof.LabelDelegate#createDepend(int)
	 */
	public Destroyable[] createDepend(int args) {
		return new Label[args];
	}

	/**
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
