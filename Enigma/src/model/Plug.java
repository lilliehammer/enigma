package model;

/**
 * Pair of two letters. If 'AB' was a plug, all 'A's that go through
 * plugboard get turned to 'B' for example.
 * @author Lillie Hammer
 *
 */
public class Plug implements Comparable {

	private char a, b;
	
	public Plug(char a, char b) {
		this.a = (char) Math.min(a, b);
		this.b = (char) Math.max(a, b);
	}
	
	public Plug(String s) {
		this.a = (char) Math.min(s.charAt(0), s.charAt(1));
		this.b = (char) Math.max(s.charAt(0), s.charAt(1));;
	}
	
	public char get(char c) {
		if (a == c)
			return b;
		if (b == c)
			return a;
		return '_';
	}
	
	public boolean contains(char c) {
		if (a == c || b == c)
			return true;
		return false;
	}
	
	public boolean equals(Plug p) {
		if (p.contains(a) && p.contains(b))
			return true;
		return false;
	}
	
	public char getCharOne() {
		return a;
	}
	
	public char getCharTwo() {
		return b;
	}
	
	public String toString() {
		return "" + a + b;
	}

	@Override
	public int compareTo(Object arg0) {
		if (((Plug) arg0).equals(this))
			return 0;
		return 1;
	}
	
	
	/**
	 * @return True if plug p shares letters with this one
	 */
	public boolean sharesLetter(Plug p) {
		if (p.contains(a) || p.contains(b))
			return true;
		return false;
	}
}
