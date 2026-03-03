package fi.wiskopdr.domainmodel;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class DomStudentModelMethodInfo {

	private String method, book;
	private Number chapter;
	private String variant;
	
	
	private Number x, y;
	
	public DomStudentModelMethodInfo() {
	}

	public DomStudentModelMethodInfo(String methodeName, String leerjaarName, Number i) {
		method = methodeName;
		book = leerjaarName;
		chapter = i;
	}
	
	public DomStudentModelMethodInfo(DomStudentModelMethodInfo source) {
		this(source.method, source.book, source.chapter);
		x = source.x;
		y = source.y;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the book
	 */
	public String getBook() {
		return book;
	}

	/**
	 * @param book the book to set
	 */
	public void setBook(String book) {
		this.book = book;
	}

	/**
	 * @return the chapter
	 */
	public Number getChapter() {
		return chapter;
	}

	/**
	 * @param chapter the chapter to set
	 */
	public void setChapter(Number chapter) {
		this.chapter = chapter;
	}

	/**
	 * @return the x
	 */
	public Number getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(Number x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public Number getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(Number y) {
		this.y = y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(book, chapter, method, x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DomStudentModelMethodInfo other = (DomStudentModelMethodInfo) obj;
		return Objects.equals(book, other.book) && Objects.equals(chapter, other.chapter)
				&& Objects.equals(method, other.method) && Objects.equals(x, other.x) && Objects.equals(y, other.y);
	}

	public String key() {
		return method + "-" + book + "-" + chapter;
	}

  public void setVariant(Object object) {
    this.variant = Objects.toString(object, null);
  }

  public String getVariant() {
    return variant;
  }

  Collection<String> variantDeselections = Collections.emptySet();
  public Collection<String> getVariantDeselections() {
    return variantDeselections;
  }

  public void setVariantDeselections(Collection<String> variantDeselections) {
    this.variantDeselections = variantDeselections;
  }
  
  
	
}
