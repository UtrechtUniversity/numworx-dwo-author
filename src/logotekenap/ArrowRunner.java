package logotekenap;
/**
  * class ArrowRunner
  * Helper class for InvoerVariabele, it will time how long arrow is being 'mouseheld'
  * and generates 'kicks' for InvoerVariabele 
  */
 class ArrowRunner extends Thread
 {	private InvoerVariabele owner;
 
 	public ArrowRunner(InvoerVariabele o)
 	{	owner = o;
 	}
 	
 	public void run()
 	{	int teller = 0;				// count how many times the thread 'kicks'
 		try
 		{	sleep(500);
 		} catch ( InterruptedException e ) { }
 		while (true)				// will be stopped by release of mouse!
 		{	teller++;				// increase count (first kick will be 1)
 			owner.kick(teller);		// pass on count (so NumberArrow can increase steps
 			try					// while 'mouse hold' lasts
 			{	sleep(200);
 			} catch ( InterruptedException e ) 
 			{ }
 		}
 	}
 }	// end class ArrowRunner
