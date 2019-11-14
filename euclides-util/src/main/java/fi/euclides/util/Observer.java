package fi.euclides.util;


public interface Observer extends java.util.Observer {

	void update(Observable observable, Object arg);
	
	default void update( java.util.Observable ob, Object arg) {
	  update ( (Observable) ob, arg);
	}

}
