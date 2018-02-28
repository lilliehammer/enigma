package model;

public class Reflector {
	final static String reflector = "ENKQAUYWJICOPBLMDXZVFTHRGS";
	final static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static char getChar(char c) {
		int temp = alphabet.indexOf(c);
		return reflector.charAt(temp);
	}
}
