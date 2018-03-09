package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.algo.FreePoint;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.util.JMath;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class Label extends Destroyable implements Observer {
	
	public static final String STATE = "STATE";
	public static final String TYPE = "T";

	public static class DefaultHandler extends LabelDelegate {
		public DefaultHandler(String string) {
			super(string);
		}

		public Destroyable[] createDepend() {
			return EMPTY;
		}

		public void update(Observable observable, Object arg) {
		}

		/** labels are same if
		 *  string equal and registration identical.
		 * @see fi.euclides.proof.LabelDelegate#equals(fi.euclides.model.Label, fi.euclides.model.Label)
		 */
		public boolean equals(Label label, Label other) {
			if (label == other)
				return true;
			if (other.registered != this)
				return false;
			return  label.getString() == other.getString() ||
					label.getString() != null && 
					label.getString().equals(other.getString());
				
		}

		/* (non-Javadoc)
		 * @see fi.euclides.proof.LabelDelegate#hashCode(fi.euclides.model.Label)
		 */
		public int hashCode(Label label) {
			return System.identityHashCode(label);
		}

		public String getSubKey() { return ""; }
	}

	public static final Destroyable[] EMPTY = new Destroyable[0];
	private Punt p = new VrijPunt(Numbers.ZERO, Numbers.ZERO);
	private boolean defined = true;
	private String string;
	private Destroyable[] depend = EMPTY;
	//public Object extra;
	
	public static final int
		FALSE = -1,
		UNKNOWN=0,
		INEXACT=1,
		EXACT=2,
		PROVEN=3,
		OPGAVE=4,
		CONFIGURATION=5,
		NAGEKEKEN=6, // same as proven
		VOLDOENDE=7; // nagekeken, maar te veel.
// Label value Types, moeten buiten de andere states.
		public static final int
			STRING = 0,     // no value
			VALUE = 101,    // value = value, extra = null
			VECTOR = 102,   // value = x/y, extra = {x, y} 
			HOEK = 103,     // value = arctan2(y,x), extra = {x, y} 
			CONSTANT = 104, // value = immutable!
			EXPR = 105; 	// unevaluated expression, lambda, symbol etc
			
	private int state; // enumeration
	public Numbers value;
	
	public Label() {
		value = Numbers.NaN;
	}

	public String key() {
		return TYPE;
	}

	/**
	 * @return the depend
	 */
	public Destroyable[] getDepend() {
		return depend;
	}

	/**
	 * @param depend the depend to set
	 */
	public void setDepend(Destroyable[] depend) {
		this.depend = depend;
		for (int i = 0; i < depend.length; i++) {
			if(depend[i]!=null) depend[i].addObserver(this);		
		}
	}

	/**
	 * @return the subKey
	 */
	public String getSubKey() {
		return getRegistered().getSubKey();
	}

//	/**
//	 * @param subKey the subKey to set
//	 */
//	public void setSubKey(String subKey) {
//		if("".equals(subKey))
//		{
//			register(DEFAULT);
//			return;
//		}
//// protocol met euclides.proof stuff.
//		LabelDelegate r = LabelDelegate.getRegistered(subKey);
//		if(r == null)
//			throw new IllegalArgumentException(subKey);
//		register(r);
//	}

	public void read(Codec codec) throws IOException {
		//getP().read(codec);
		setP(codec.readPunt());
		setString(codec.readUTF());
		register(codec.readDelegate());
		setDepend(getRegistered().createDepend(codec, this));
		for (int i = 0; i < depend.length; i++) {
			depend[i] = codec.readDestroyable();
			if(depend[i]!=null) depend[i].addObserver(this);		
		}
		// state/value
		Numbers n;
		n = codec.readNumber();
		state = (int)n.longValue();
		setValue( codec.readNumber() );
	}

	public void visit(Visitor v) {
		v.visitLabel(this);
	}

	public void write(Codec codec) throws IOException {
		codec.writePunt(getP());
		codec.writeUTF(getString());
		codec.writeDelegate(getRegistered());
		getRegistered().prepareDepend(codec, this);
		for (int i = 0; i < depend.length; i++) {
				codec.writeDestroyable(depend[i]);
		}
		codec.writeNumber(Numbers.createInteger(state));
		codec.writeNumber(value);
	}

	/**
	 * @return the string
	 */
	public String getString() {
		return string;
	}

	/**
	 * @param string the string to set
	 */
	public void setString(String string) {
		if(string != null && string.equals(this.string))
				return;
		setChanged();
		this.string = string;
	}

	private boolean defined() {
		Destroyable[] depend = getDepend();
		for (int i = 0; i < depend.length; i++) {
			if(depend[i] != null && !depend[i].isDefined())
				return false;
		}
		return true;
	}
	
	public void update(Observable observable, Object arg) {
		if(arg==DESTROY)
		{
			destroy();
		} else if(arg != VISIBLE){
			setDefined(defined());
			registered.update(this, arg);
			//setChanged();
			notifyObservers(arg);
		}
	}

	@Override
	public void destroy() {
		registered.update(this, DESTROY);
		super.destroy();
	}

	public  LabelDelegate registered = DEFAULT;

	static public final LabelDelegate DEFAULT = new DefaultHandler("");

	public LabelDelegate getRegistered()
	{
		return registered;
	}
	
	public void register(LabelDelegate register) {
		registered = register;
	}
	
	/**
	 * @return the defined
	 */
	public boolean isDefined() {
		return defined;
	}

	/**
	 * @param defined the defined to set
	 */
	public void setDefined(boolean defined) {
		this.defined = defined;
	}

	@Override
	public boolean isVisible() {
		return super.isVisible() && getP().isDefined();
	}

	/**
	 * @return
	 * @see fi.euclides.model.Punt#getX()
	 */
	public Numbers getX() {
		return getP().getX();
	}

	/**
	 * @return
	 * @see fi.euclides.model.Punt#getXd()
	 */
	public final double getXd() {
		return getP().getXd();
	}

	/**
	 * @return
	 * @see fi.euclides.model.Punt#getY()
	 */
	public Numbers getY() {
		return getP().getY();
	}

	/**
	 * @return
	 * @see fi.euclides.model.Punt#getYd()
	 */
	public final double getYd() {
		return getP().getYd();
	}

	/**
	 * @param x
	 * @param y
	 * @see fi.euclides.model.Punt#moveTo(fi.euclides.model.math.Numbers, fi.euclides.model.math.Numbers)
	 */
	public void moveTo(Numbers x, Numbers y) {
		getP().moveTo(x, y);
	}

	/**
	 * @param x
	 * @see fi.euclides.model.Punt#setX(fi.euclides.model.math.Numbers)
	 */
	public void setX(Numbers x) {
		getP().setX(x);
	}

	/**
	 * @param y
	 * @see fi.euclides.model.Punt#setY(fi.euclides.model.math.Numbers)
	 */
	public void setY(Numbers y) {
		getP().setY(y);
	}

	/**
	 * @param x
	 * @deprecated
	 * @see fi.euclides.model.Punt#setX(double)
	 */
	public void setX(double x) {
		getP().setX(x);
	}

	/**
	 * @param y
	 * @deprecated
	 * @see fi.euclides.model.Punt#setY(double)
	 */
	public void setY(double y) {
		getP().setY(y);
	}

	public void setP(Punt p) {
		this.p = p;
		p.addObserver(this);
	}

	public Punt getP() {
		return p;
	}
	
	@Override
	public <T> T adapt(Class<T> clz) {
		if(clz == FreePoint.class && p != null)
			return p.adapt(clz);
		return super.adapt(clz);
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		if(state!=this.state)
			setChanged();
		this.state = state;
		notifyObservers(STATE);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean same(Object obj) {
		if(obj == this)
			return true;
		if(obj instanceof Label)
			return getRegistered().equals(this,(Label)obj);
		return false;
	}

	public void setValue(Numbers value) {
		if(value == null )
		{
			if(this.value != null)
				setChanged();
			this.value = Numbers.createDouble(Double.NaN);
			setDefined(false);
			notifyObservers();
			return;
		}		
		if(!value.equals(this.value))
		{
			this.value = value;
			setChanged();
			notifyObservers();
		}
	}

	public void addObserver(Observer observer) {
		if(observer instanceof LabelDelegate)
		{
			throw new IllegalArgumentException();
		}
		super.addObserver(observer);
	}

	
	
	
}
