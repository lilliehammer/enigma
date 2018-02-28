package model;

import java.util.HashMap;
import java.util.Map;

public class Plugboard {
	Map<Character, Character> map;
	
	String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public Plugboard() {
		//ASCII
		//A - 65
		//Z - 90
		map = new HashMap<>();
		for (int i = 65; i < 91; i++) {
			
			map.put((char)i, (char)i);
		}
	}
	
	//Make sure characters are uppercase
	//a=97, z=122
	public void map(char a, char b) throws AlreadyMappedException{
		//turn to uppercase if need be
		if ((int) a >= 97 && (int) a <= 122)
			a -= 32;
		if ((int) b >= 97 && (int) b <= 122) 
			b -= 32;
		
		
		if (map.get(a) != a)
			throw new AlreadyMappedException();
		
		map.put(a, b);
		map.put(b, a);
	}
	
	public char getChar(char a) {
		return map.get(a);
	}
	

}
