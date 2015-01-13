package fi.wiskopdr.cbook.rm;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;

public class PageManager extends AbstractPageManager {

	Sardine sardine;
	URI prefix;
	
	public PageManager(URL root, String unit, String user, String passwd) {
		sardine = SardineFactory.begin(user, passwd);
		if(! unit.endsWith("/"))
			unit += "/";
		try {
			URI r = root.toURI();
			prefix = URI.create(unit);
			prefix = r.resolve("Unit/").resolve(prefix);
		} catch (URISyntaxException e) {
		}

		destroy();
	}

	@Override
	public void duplicatePage(int page, int total) throws IOException {
			insertPage(page, total);
			String dest = page(page);
			String source = page(page+1);
			if(sardine.exists(source))
				sardine.copy(source, dest);		
	};
	
	@Override
	public void insertPage(int page, int total) throws IOException {
		for(int i = total-1; i >= page; i-- )
		{
			String dest = page(i+1);
			String source = page(i);
			if(sardine.exists(source))
				sardine.move(source, dest);
		}
	}
	
	@Override
	public void deletePage(int page, int total) throws IOException {
		String dest = page(page);
		if(sardine.exists(dest))
			sardine.delete(dest);
		while(page < total) {
			page ++;
			String source = page(page);
			if(sardine.exists(source))
				sardine.move(source, dest);	
		}
	}

	String page(String page) {
		return prefix.resolve(page.toString()).toASCIIString();
	}
	String page(int page) {
		return page(Integer.toString(page));
	}
	
	String COPY_PAGE = "__clipboard__";

	@Override
	public void copyPage(int page) throws IOException {
		String source = page(page);
		String dest = page(COPY_PAGE);
		if(sardine.exists(dest))
			sardine.delete(dest);
		if (sardine.exists(source)) {
			sardine.copy(source, dest);
		}
	}
		
	@Override
	public void pastePage(int page, int total) throws IOException {
		insertPage(page, total);
		String source = page(COPY_PAGE);
		if(sardine.exists(source))
			sardine.copy(source, page(page));
	}
	
	@Override
	public void swapPage(int p1, int p2) throws IOException {
		String dest = page(p2);
		String source = page(p1);
		boolean b1 = sardine.exists(source);
		boolean b2 = sardine.exists(dest);
		if(b1 && b2) {
			String swap = page(-p1-p2);
			sardine.move(source, swap);
			sardine.move(dest, source);
			sardine.move(swap, source);
		} else if(b1) {
			sardine.move(source, dest);
		} else if(b2) {
			sardine.move(dest, source);
		}
	}

	@Override
	public void destroy() {
		String dest = page(COPY_PAGE);
		try {
			if(sardine.exists(dest))
				sardine.delete(dest);
		} catch (IOException e) {
		}
		
	}
	
}
