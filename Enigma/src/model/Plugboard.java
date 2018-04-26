package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Simulates the plugboard of an Enigma machine.
 * @author lilyp
 *
 */
public class Plugboard {
	
	/* Use a Map data structure with all letters of alphabet in it.
	 * If two letters map to each other, they'll trade places. If a
	 * letter is still in its original position, means it's unmapped
	 */
	Map<Character, Character> map;
	ArrayList<Plug> plugs;
	
	String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public Plugboard() {
		//ASCII
		//A - 65
		//Z - 90
		map = new HashMap<>();
		for (int i = 65; i < 91; i++) {
			
			map.put((char)i, (char)i);
		}
		plugs = new ArrayList<>();
	}
	
	//Make sure characters are uppercase
	//a=97, z=122
	public void map(char a, char b) throws AlreadyMappedException {
		//turn to uppercase
		a = Character.toUpperCase(a);
		b = Character.toUpperCase(b);

		//check to see if it's already mapped
		if (map.get(a) != a) {
			throw new AlreadyMappedException();
		}
		
		map.put(a, b);
		map.put(b, a);
		
		plugs.add(new Plug(a, b));
	}
	
	/**
	 * 
	 * @param map Should be two characters long
	 * @throws AlreadyMappedException
	 */
	public void map(String map) throws AlreadyMappedException {
		map(map.charAt(0), map.charAt(1));
		
	}
	
	public char getChar(char a) {
		return map.get(a);
	}
	
	public ArrayList<Plug> getPlugs() {
		return plugs;
	}
	
	
	/**
	 * Goes through all current plugs and makes sure they don't share any
	 * letters with plug p. Makes sure mapping this one wouldn't throw
	 * any errors.
	 * @param p Plug checking to see if it's okay to add
	 * @return True if okay to add. False if not.
	 */
	public boolean plugIsOkayToAdd(Plug p) {
		for (Plug plug : plugs) {
			if (plug.sharesLetter(p))
				return false;
		}
		return true;
	}
	

}
