package fi.wiskopdr.cbook.rm;

import java.io.IOException;
import java.net.URL;

import junit.framework.TestCase;

public class PageManagerIT extends TestCase {

	AbstractPageManager man;
	int total = 10;
	
	protected void setUp() throws Exception {
		URL root = new URL("http://localhost:8888/dav/");
		String user = "test";
		String password = "0123456789";
		man = new PageManager(root, "something", user, password);
	}

	protected void tearDown() throws Exception {
		man.destroy();
	}

	public void testDuplicatePage() throws Exception {
		man.duplicatePage(0, total);
	}

	public void testDeletePage() throws Exception {
		man.deletePage(1, total);
	}

	public void testCopyPage() throws Exception {
		man.copyPage(0);
		man.pastePage(5, total);
	}


	public void testSwapPage() throws Exception {
		man.swapPage(0, 1);
	}

}
