package model;

public class Rotor {
	String rotor; //letters jumbled up somehow
	int currentIndex; 
	char[] spinChars; //letters where wheel turns next one. Easier to input
	int[] spinPlaces; //indices where wheel turns next one. Actually used in code
	
	final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; //plaintext alphabet
	
	//Input:
	//		String s: letters jumbled up somehow
	//		char[] spinChars: characters where wheel turns next one
	public Rotor(String s, char[] spinChars) {
		rotor = s;
		this.spinChars = spinChars;
		currentIndex = 0;
		spinPlaces = new int[2];
		spinPlaces[0] = rotor.indexOf(spinChars[0]);
		if (spinChars.length > 1)
			spinPlaces[1] = rotor.indexOf(spinChars[1]);
		else
			spinPlaces[1] = spinPlaces[0];
	}	
		
	//set starting index
	public void setIndex(char c) {
		currentIndex = rotor.indexOf(c);
	}
	
	//Returns true if it spins its neighbor
	//Returns false otherwise
	public boolean incrementSpinNeighbor() {
		currentIndex = Math.floorMod(currentIndex+1, 26);
		if (spinPlaces[0] == currentIndex || spinPlaces[1] == currentIndex)
			return true;
		return false;
	}
	
	public char getChar() {
		return rotor.charAt(currentIndex);
	}
	
	/*
	//Input: char to encrypt
	//Output: encrypted char
	public char getChar(char c) {
		int temp = alphabet.indexOf(c);
		return rotor.charAt(temp);
	}
	*/
	
	public char addToChar(char c, char d) {
		int temp = (int) c + (int) d;
		while (temp < 65) 
			temp += 26;
		
		while (temp > 90)
			temp -= 26;
		
		return (char) temp;
	}
	
	public char addToChar(char c, int d) {
		int temp = (int) c + d;
		while (temp < 65) 
			temp += 26;
		
		while (temp > 90)
			temp -= 26;
		
		return (char) temp;
	}
	
	//INPUT: number 0-25 representing letter in string
	public char passThroughOne(char c, int n) {
		c = addToChar(c, n);
		
		c = addToChar(c, rotor.charAt(currentIndex));
		
		int temp = rotor.indexOf(c);
		c = alphabet.charAt(temp);
		
		c = addToChar(c, (int) rotor.charAt(currentIndex) * -1);
		
		c = addToChar(c, -n);
		
		return c;
	}
	
	public char passThroughTwo(char c, int n) {
		c = addToChar(c, n);
		
		c = addToChar(c, rotor.charAt(currentIndex));
		
		int temp = alphabet.indexOf(c);
		c = rotor.charAt(temp);
		
		c = addToChar(c, (int) rotor.charAt(currentIndex) * -1);
		
		c = addToChar(c, -n);
		
		return c;
	}
}
