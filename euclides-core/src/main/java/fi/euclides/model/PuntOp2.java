package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.math.Numbers;
import fi.euclides.proof.IncidentHandler;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

/**
 * Base for Snijpunt op lijn/lijn, lijn/cirkel, en cirkel/cirkel
 * @author Wim
 *
 */
abstract public class PuntOp2<D1 extends Destroyable, D2 extends Destroyable> extends Punt implements Observer {

	D1 lijn1;
	D2 lijn2;
	Punt other;
	private byte fuse; // 0, 1, 2
	public abstract String key();
	/**
	 * @param lijn1
	 * @param lijn2
	 */
	protected PuntOp2(D1 lijn1, D2 lijn2) {
		this.lijn1 = lijn1;
		this.lijn2 = lijn2;
		lijn1.addObserver(this);
		lijn2.addObserver(this);
	}

	protected PuntOp2() {
	}

	public void moveTo(Numbers dx, Numbers dy) {
	}

	public void update(Observable o, Object arg) {
		if(arg == DESTROY)
		{
			destroy();
		}
		else if(o == lijn1 || o == lijn2) {
			setDefined(lijn1.isDefined()&&lijn2.isDefined());
			if(isDefined())
				recalc();
			notifyObservers(arg);
		}
	}

	protected abstract void recalc();

	protected void setFusedXY(double p1x, double p1y, double p2x,
			double p2y) {
				if(fuse == 0) {
				if( Math.abs(p1x-getXd())+ Math.abs(p1y-getYd())
						< 
					Math.abs(p2x-getXd())+ Math.abs(p2y-getYd())			
				) 
					fuse = 2;
				else 
					fuse = 1;
				}
				if(other != null)
				{
					double px = other.getXd();
					double py = other.getYd();
					if(fuse==2)
					{	if( near(px, p1x) && near(py, p1y))
							fuse = 1;
					} else {
						if (near(px, p2x) && near(py, p2y))
							fuse = 2;
					}
				}
				if (fuse==2)
					setXY(p1x, p1y);
				else
					setXY(p2x, p2y);
			}

	protected void setFusedXY(Numbers p1x, Numbers p1y, Numbers p2x,
			Numbers p2y) {
				if(fuse == 0) {
				if( Math.abs(p1x.doubleValue()-getXd())+ Math.abs(p1y.doubleValue()-getYd())
						< 
					Math.abs(p2x.doubleValue()-getXd())+ Math.abs(p2y.doubleValue()-getYd())			
				) 
					fuse = 2;
				else 
					fuse = 1;
				}
				if(other != null)
				{
					double px = other.getXd();
					double py = other.getYd();
					if(fuse==2)
					{	if( near(px, p1x.doubleValue()) && near(py, p1y.doubleValue()))
							fuse = +1;
					} else {
						if (near(px, p2x.doubleValue()) && near(py, p2y.doubleValue()))
							fuse = -1;
					}
				}
				if (fuse==2)
					setXY(p1x, p1y);
				else
					setXY(p2x, p2y);
			}

	private boolean near(double a, double b) {
		return Math.abs(a-b) < 0.0001;
	}
	public D1 getOp1() {
		return lijn1;
	}
	public void setOp1(D1 op1)
	{
		this.lijn1 = op1;
		op1.addObserver(this);
	}
	
	public D2 getOp2() {
		return lijn2;
	}

	public void setOp2(D2 op2)
	{
		this.lijn2 = op2;
		op2.addObserver(this);
	}
	
	/* (non-Javadoc)
	 * @see euclides.Punt#read(euclides.Codec)
	 */
	public void read(Codec codec) throws IOException {
		super.read(codec);
		D1 op1 = (D1) codec.readDestroyable();
		if(op1 != null) setOp1(op1);
		D2 op =(D2) (codec.readDestroyable());
		if(op != null) setOp2(op);
		if(getOp1() != null && getOp2() != null )
		{	setOther();
			recalc();
		}
	}

	protected void setOther() {
	}
	/* (non-Javadoc)
	 * @see euclides.Punt#write(euclides.Codec)
	 */
	public void write(Codec codec) throws IOException {
		super.write(codec);
		codec.writeDestroyable(getOp1());
		codec.writeDestroyable(getOp2());
	}
	
	/**
	 * Bewijsbare incidenties. De middelpunt regel is een niet-triviale.
	 * @param punt
	 * @param cirkel
	 * @return
	 */
	public static boolean incident(Punt punt, Rondje cirkel) {
		
		if(cirkel.incident(punt) || punt.incident(cirkel))
			return true;
		if( cirkel.isr2c() && cirkel.getCenter() instanceof MiddelPunt)
		{
			MiddelPunt mp = (MiddelPunt) cirkel.getCenter();
			Object r = cirkel.getRadius();
			if(punt == mp.getP1() && r == mp.getP2()) 
			{
				IncidentHandler.nqtrivial=true;
				return true;
			}
			if(punt == mp.getP2() && r == mp.getP1()) 
			{
				IncidentHandler.nqtrivial=true;
				return true;
			}
		}
		// TODO een of andere constructie met center op loodlijn.
		if(cirkel.isr2c())
		{
		
		if (cirkel.getCenter().key() .equals (Lijn.PUNTOP) )
		{
			PuntOp pl = (PuntOp)cirkel.getCenter();
			boolean middelpuntregel = middelpuntregel(punt, cirkel, pl.getOp());
			if(middelpuntregel)
				IncidentHandler.nqtrivial=true;
			return middelpuntregel;
		} else if(cirkel.getCenter() instanceof PuntOp2)
		{
			PuntOp2 pl = (PuntOp2) cirkel.getCenter();
			boolean b = middelpuntregel(punt,cirkel, pl.getOp1()) ||
				   middelpuntregel(punt,cirkel, pl.getOp2());
			if(b)
				IncidentHandler.nqtrivial=true;
			return b;
		}
		
		}
		return false;
	}
	/**
	 * @param punt
	 * @param cirkel
	 * @param op
	 */
	private static boolean middelpuntregel(Punt punt, Rondje cirkel,
			final Destroyable op) {
		if(op instanceof LoodLijn)
		{
			LoodLijn ll = (LoodLijn) op;
			if(ll.getPunt() instanceof MiddelPunt)
			{
				MiddelPunt mp = (MiddelPunt) ll.getPunt();
				if ( 
						mp.p1 == punt && mp.p2 == cirkel.getRadius() ||
						mp.p2 == punt && mp.p1 == cirkel.getRadius()
				)
					return IncidentHandler.incident(mp.p1,ll.getDestroyable()) && IncidentHandler.incident(mp.p2, ll.getDestroyable());
			}
		}
		return false;
	}
	/**
	 * @return the other
	 */
	public Punt getOther() {
		return other;
	}
	/**
	 * @param other the other to set
	 */
	public void setOther(Punt other) {
		this.other = other;
	}
	/**
	 * @return the fuse
	 */
	public byte getFuse() {
		return fuse;
	}
	/**
	 * @param fuse the fuse to set
	 */
	public void setFuse(byte fuse) {
		this.fuse = fuse;
	}
	/**
	 * @param c1x
	 * @param c1y
	 * @param ox
	 * @param oy
	 * @param ldxn
	 * @param ldyn
	 */
	protected void spiegeling(Numbers c1x, Numbers c1y, Numbers ox,
			Numbers oy, final Numbers ldxn, final Numbers ldyn) {
				Numbers d = Numbers.add(Numbers.mul(Numbers.sub(ox,c1x), ldxn) , Numbers.mul(Numbers.sub(oy,c1y) , ldyn));
				Numbers hl = Numbers.add(Numbers.sqr(ldxn), Numbers.sqr(ldyn));
				d = Numbers.div(d , hl);
				setXY( Numbers.sub(Numbers.mul(Numbers.add(c1x , Numbers.mul(d , ldxn)),Numbers.TWO),ox), 
					   Numbers.sub(Numbers.mul(Numbers.add(c1y , Numbers.mul(d , ldyn)),Numbers.TWO),oy)
				);
			}

	public Destroyable[] getDepend() {
		return new Destroyable[] { lijn1, lijn2 };
	}
	/* (non-Javadoc)
	 * @see fi.euclides.model.Destroyable#incident(fi.euclides.model.Destroyable)
	 */
	public boolean incident(Destroyable other) {
		return super.incident(other) || other == lijn1 || other == lijn2;
	}
	
}
