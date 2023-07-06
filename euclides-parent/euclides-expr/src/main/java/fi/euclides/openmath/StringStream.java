package fi.euclides.openmath;

import java.io.IOException;

class StringStream implements CharStream {

	private CharSequence sequence;
	private int index, start;
	private int length;
	
	StringStream(CharSequence sequence) {
		super();
		this.sequence = sequence;
		index = 0;start = 0;
		length = sequence.length();
	}

	public char readChar() throws IOException {
		if(index >= length) throw new IOException("EOF");
		return sequence.charAt(index++);
	}

	public int getColumn() {
		return index;
	}

	public int getLine() {
		return 0;
	}

	public int getEndColumn() {
		return index;
	}

	public int getEndLine() {
		return 0;
	}

	public int getBeginColumn() {
		return start;
	}

	public int getBeginLine() {
		return 0;
	}

	public void backup(int amount) {
		index -= amount;
	}

	public char BeginToken() throws IOException {
		start = index;
		return readChar();
	}

	public String GetImage() {
		return sequence.subSequence(start, index).toString();
	}
	  /**
	   * Returns an array of characters that make up the suffix of length 'len' for
	   * the currently matched token. This is used to build up the matched string
	   * for use in actions in the case of MORE. A simple and inefficient
	   * implementation of this is as follows :
	   *
	   *   {
	   *      String t = GetImage();
	   *      return t.substring(t.length() - len, t.length()).toCharArray();
	   *   }
	   */

	public char[] GetSuffix(int len) {
		return sequence.subSequence(index-len, len).toString().toCharArray();
	}

	public void Done() {
	}

}
