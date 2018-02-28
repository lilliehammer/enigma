package model;

public class Enigma {

	
	final Rotor rotorOne = new Rotor("EKMFLGDQVZNTOWYHXUSPAIBRCJ", new char[]{'Y'}); //Q
	final Rotor rotorTwo = new Rotor("AJDKSIRUXBLHWTMCQGZNPYFVOE", new char[]{'M'}); //E
	final Rotor rotorThree = new Rotor("BDFHJLCPRTXVZNYEIWGAKMUSQO", new char[]{'D'}); //V
	final Rotor rotorFour = new Rotor("ESOVPZJAYQUIRHXLNFTGKDCMWB", new char[]{'R'}); //J
	final Rotor rotorFive = new Rotor("VZBRGITYUPSDNHLXAWMJQOFECK", new char[]{'H'}); //Z
	
	final String reflector = "ENKQAUYWJICOPBLMDXZVFTHRGS";
	
	final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
	final Rotor[] allRotors = {rotorOne, rotorTwo, rotorThree, rotorFour, rotorFive};
	
	public Rotor[] rotors; //rotors in use
	
	public Plugboard plugBoard = new Plugboard();
	
	public Enigma() {
		rotors = new Rotor[3];
	}
		
	public void setRotors(int one, int two, int three) {
		rotors[0] = allRotors[one]; //Left
		rotors[1] = allRotors[two]; //Middle
		rotors[2] = allRotors[three]; //Right
	}
	
	//A is 65, Z is 90
	/*	INPUT:
	 * 		char c : character to be added to
	 * 		int  n : integer to be added to c
	 *  OUTPUT:
	 *  	uppercase character 
	 */
	public char addToCharacter(char c, int n) {
		int temp = (int) c + n;
		
		//Make sure still in range
		while (temp > 90)
			temp -= 26;
		while (temp < 65)
			temp += 26;
		
		return (char) temp;
		
	}
	
	/*	STEPS
	 *  1) Rotate wheels
	 *  2) Plugboard
	 *  3) Right wheel (rotors.get(1))
	 *  4) Middle wheel
	 *  5) Left wheel
	 *  6) Reflector
	 *  7) Left wheel
	 *  8) Middle wheel
	 *  9) Right wheel
	 *  10) Plugboard
	 * 
	*/
	public char encryptChar(char c) {		
		char eChar = c;
		
		//ROTATE WHEELS
		//first wheel always rotates
		if (rotors[0].incrementSpinNeighbor()) {
			//second wheel might rotate
			if (rotors[1].incrementSpinNeighbor()) {
				//third wheel might rotate, doesn't spin anything
				rotors[2].incrementSpinNeighbor();
			}
		}
		
		//PLUGBOARD
		eChar = plugBoard.getChar(eChar);
		
		//RIGHT WHEEL
		eChar = rotors[2].passThroughOne(eChar, 1);
		
		//MIDDLE WHEEL
		eChar = rotors[1].passThroughOne(eChar, 2);
		
		//LEFT WHEEL
		eChar = rotors[0].passThroughOne(eChar, 3);
		
		//REFLECTOR
		eChar = Reflector.getChar(eChar);
		
		//LEFT WHEEL
		eChar = rotors[0].passThroughTwo(eChar, 3);
		
		//MIDDLE WHEEL
		eChar = rotors[1].passThroughTwo(eChar, 2);
		
		//RIGHT WHEEL
		eChar = rotors[2].passThroughTwo(eChar, 1);
		
		//PLUGBOARD
		eChar = plugBoard.getChar(eChar);
		
		return eChar;
	}
	
	//initialize right rotor
	//Input: character that should be displayed on wheel
	public void setUpRightRotor(char c) {
		rotors[2].setIndex(c);
	}
	
	//initialize middle rotor
	//Input: character that should be displayed on wheel
	public void setUpMiddleRotor(char c) {
		rotors[1].setIndex(c);
	}
	
	//initialize left rotor
	//Input: character that should be displayed on wheel
	public void setUpLeftRotor(char c) {
		rotors[0].setIndex(c);
	}
	
	//maps two characters with the plugboard
	public void mapLetters(char c, char d) throws AlreadyMappedException{
		plugBoard.map(c, d);
	}
	
	//encrypts an entire String of characters
	public String encryptString(String s) {
		String output = "";
		
		s = parseString(s);
		for (int i = 0; i < s.length(); i++) {
			output += encryptChar(s.charAt(i));
		}
		
		return output;
	}
	
	//ignores symbols, removes spaces, and turns to uppercase
	public String parseString(String s) {
		String output = "";
		for (int i = 0; i < s.length(); i++) {
			Character c = s.charAt(i);
			if (Character.isLetter(c))
				output += Character.toUpperCase(c);
		}
		return output;
	}
}
