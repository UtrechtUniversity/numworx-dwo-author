package fi.wiskopdr.cbook.rm;

import java.io.IOException;

public  class AbstractPageManager {

	public  void destroy() {}

	public void swapPage(int p1, int p2)
			throws IOException {}

	public void pastePage(int page, int total)
			throws IOException {}

	public void copyPage(int page) throws IOException {}

	public void deletePage(int page, int total)
			throws IOException {}

	public void insertPage(int page, int total)
			throws IOException {}

	public void duplicatePage(int page, int total)
			throws IOException {}

}