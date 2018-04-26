package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import javafx.util.Pair;

/**
 * This class has various methods to help in decryption.
 * @author Lillie Hammer
 *
 */
public class Decryption {
	
	/**
	 * Generates all 325 possible plug combinations (26 choose 2). 
	 * Not being used right now but some day...
	 */
	public static ArrayList<Plug> generateAllPlugs() {
		ArrayList<Plug> output = new ArrayList<>();
		
		for (int i = 65; i < 90; i++) {
			for (int j = i+1; j < 91; j++) {
				char a = (char) i;
				char b = (char) j;
				
				Plug p = new Plug(a, b);
				if (!output.contains(p))
					output.add(p);
			}
		}
		return output;
	}
	
	public void tryTryAgain(Enigma e, String knownPlaintext, Pair<String, Integer> cribPair) {
		ArrayList<Plug> goodPlugs;
		ArrayList<Plug> maybePlugs;
		ArrayList<Plug> badPlugs;
		Stack<Plug> toTry;
		
		//setup
		ArrayList<String> encryptionsNoPlugs = successiveAlphabetWithoutPlugboard(e, cribPair.getValue(), plaintext.length());
		String crib = cribPair.getKey();
		int length = Math.min(crib.length(), knownPlaintext.length());
		char[][] mappings = new char[length][4];
		// fill in what we know
		for (int i = 0; i < length; i++) {
			mappings[i][0] = knownPlaintext.charAt(i);
			mappings[i][1] = mappings[i][2] = '_';
			mappings[i][3] = crib.charAt(i);
		}
		
		
	}
	
	/**
	 * Generates plugs that are within one letter of each other.
	 * Currently the one being used.
	 */
	public static ArrayList<Plug> generateSuccessivePlugs() {
		ArrayList<Plug> output = new ArrayList<>();
		for (int i = 65; i < 89; i++) {
			char a = (char) i;
			char b = (char) (i+1);
			
			Plug p = new Plug(a, b);
			output.add(p);
		}
		output.add(new Plug("YZ")); //gets missed
		return output;
	}
	
	public static int howSimilar(String a, String b) {
		
		int length = Math.min(a.length(), b.length());
		a = a.substring(0, length);
		b = a.substring(0, length);
		int count = 0;
		for (int i = 0; i < length; i++) {
			if (a.charAt(i) == b.charAt(i))
				count++;
		}
		return count;
	}
	

	/**
	 * 
	 * @param e
	 *            Enigma machine
	 * @param plaintext
	 *            Known plaintext
	 * @param cribPairsList
	 *            List of potential cribs and their offsets
	 * @return List of potential cribs (and offsets) that have at least one good
	 *         plug found or maybe plugs
	 */
	public static ArrayList<Pair<String, Integer>> narrowCribs(Enigma e, String plaintext,
			ArrayList<Pair<String, Integer>> cribPairsList) {
		
		ArrayList<Pair<String, Integer>> output = new ArrayList<>();
		for (Pair<String, Integer> cribPair : cribPairsList) {

			ArrayList<String> encryptionsNoPlugs = successiveAlphabetWithoutPlugboard(e, cribPair.getValue(), plaintext.length());
			String crib = cribPair.getKey();

			// Make mapping
			char[][] mappings = new char[plaintext.length()][4];
			// fill in what we know
			for (int i = 0; i < plaintext.length(); i++) {
				mappings[i][0] = plaintext.charAt(i);
				mappings[i][1] = mappings[i][2] = '_';
				mappings[i][3] = crib.charAt(i);
			}

			ArrayList<Plug> toTry = generateSuccessivePlugs();
			ArrayList<Plug> goodPlugs = new ArrayList<>();
			ArrayList<Plug> maybePlugs = new ArrayList<>();
			ArrayList<Plug> badPlugs = new ArrayList<>();

			while (!toTry.isEmpty()) {
				// find valid plug
				boolean ok = true, maybe = true;

				Plug p = null;
				while (!toTry.isEmpty()) {
					p = toTry.remove(0);
					// make sure it isn't in bad plugs
					if (!badPlugs.contains(p))
						break;
				}

				if (!ok && toTry.isEmpty()) {
					// testing
					// System.out.printf("Try whatever.\nGood: %s\nMaybe: %s\nBad: %s\n",
					// goodPlugs.toString(),
					// maybePlugs.toString(), badPlugs.toString());

					return output;
				}

				// reset mappings
				mappings = eraseMapping(mappings, goodPlugs);

				for (int i = 0; i < crib.length(); i++) {
					if (p.contains(mappings[i][0])) {
						maybe = false; // know it's in the
						char m1 = p.get(mappings[i][0]);
						int index = ((int) m1) - 65;
						char m2 = encryptionsNoPlugs.get(i).charAt(index);
						if (mappings[i][2] == '_') {
							// error check
							// Should be either within one or the same
							if (Math.abs(mappings[i][3] - m2) != 1 && mappings[i][3] - m2 != 0) {
								ok = false;
								if (!badPlugs.contains(p))
									badPlugs.add(p);
							} else {
								mappings[i][1] = m1;
								mappings[i][2] = m2;

							}
						} // end error checking mappings[i][2]
					}
					if (p.contains(mappings[i][3])) {
						maybe = false;
						char m2 = p.get(mappings[i][3]);
						int index = (int) m2 - 65;
						char m1 = encryptionsNoPlugs.get(i).charAt(index);
						if (mappings[i][1] == '_') {
							// error check
							// Should be either within one or the same
							if (Math.abs(mappings[i][0] - m1) != 1 && mappings[i][0] - m1 != 0) {
								ok = false;
								if (!badPlugs.contains(p))
									badPlugs.add(p);

							} else {
								mappings[i][1] = m1;
								mappings[i][2] = m2;

							}
						}
					} // end if plug has mappings[i][3]

				} // end for length of crib
				if (ok && !maybe && !goodPlugs.contains(p))
					goodPlugs.add(p);

				if (maybe && !maybePlugs.contains(p)) {
					maybePlugs.add(p);
				}

			} // end toTry not empty
			if (goodPlugs.size() > 0 || maybePlugs.size() > 0) {
				output.add(cribPair);
			}
		}

		return output;

	}
	
	/**
	 * 
	 * @param p Plug to test
	 * @param mappings 
	 * @param encryptionsNoPlugs
	 * @return 0 if potential. 1 if okay. -1 if caused problems
	 */
	private static int plugOkay(Plug p, char[][] mappings, ArrayList<String> encryptionsNoPlugs) {
		//becomes false when it's found to be in the mapping
		//how you know if it's only a potential
		boolean maybe = true; 
		for (int i = 0; i < mappings.length; i++) {
			//check the first character in mapping to see if it corresponds to a plug switch
			if (p.contains(mappings[i][0])) { 
				maybe = false;
				char m1 = p.get(mappings[i][0]); //what plugboard would turn it to
				int index = ((int) m1) - 65; //for next calculation
				char m2 = encryptionsNoPlugs.get(i).charAt(index); //what rotors would turn it to
				
				//check if valid. Right now, plugs need to be within two letters of each other
				//If it isn't within two letters of each other or the same, invalid
				//Difference should be 2, unless it's AY (24) or AZ (25)
				int difference = Math.abs(mappings[i][3] - m2);
				
				if (difference > 1) {
					System.out.println("Error found with " + p);
					System.out.println("Difference is " + difference + " and chars are " + mappings[i][3] + " " + m2);
					printSuccessive(encryptionsNoPlugs);
					print(mappings);
					return -1;
				}
			}
			
			//similarly for the third character in mapping
			if (p.contains(mappings[i][3])) {
				char m2 = p.get(mappings[i][3]);
				int index = ((int) m2) - 65;
				char m1 = encryptionsNoPlugs.get(i).charAt(index);
				int difference = Math.abs(mappings[i][3] - m2);

				if (difference > 1) {
					System.out.println("Error found with " + p);
					System.out.println("Difference is " + difference + " and chars are " + mappings[i][3] + " " + m2);
					printSuccessive(encryptionsNoPlugs);
					print(mappings);
					return -1;
				}
			}
		}
		if (maybe)
			return 0;
		return 1;
	}
	
	private static void printSuccessive(ArrayList<String> list) {
		System.out.println("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		for (String s : list) {
			System.out.println(s);
		}
	}
	
	
	/**
	 * Finds either good plugs or maybe plugs 
	 * @param e Enigma machine
	 * @param plaintext Known plaintext 
	 * @param cribPair Ciphertext that corresponds to plaintext and its offset
	 * @param output 'G' if want good plugs. 'M' if want maybe plugs
	 */
	public static ArrayList<Plug> findPlugs(Enigma e, String plaintext, Pair<String, Integer> cribPair, char output) {
		//setup
		ArrayList<String> encryptionsNoPlugs = successiveAlphabetWithoutPlugboard(e, cribPair.getValue(), plaintext.length());
		String crib = cribPair.getKey();
		int length = Math.min(crib.length(), plaintext.length());
		char[][] mappings = new char[length][4];
		// fill in what we know
		for (int i = 0; i < length; i++) {
			mappings[i][0] = plaintext.charAt(i);
			mappings[i][1] = mappings[i][2] = '_';
			mappings[i][3] = crib.charAt(i);
		}
		
		ArrayList<Plug> goodPlugs = new ArrayList<>();
		ArrayList<Plug> maybePlugs = new ArrayList<>();
		
		ArrayList<Plug> plugs = generateSuccessivePlugs();
		//Go through all plugs and add it to either list of good ones or maybes
		for (Plug p : plugs) {
			if (plugOkay(p, mappings, encryptionsNoPlugs) == 1) {
				goodPlugs.add(p);
			}
			else if (plugOkay(p, mappings, encryptionsNoPlugs) == 0) {
				maybePlugs.add(p);
			}
		}
		
		//return
		if (output == 'G')
			return goodPlugs;
		else
			return maybePlugs;
	}

	public static ArrayList<Plug> findGoodPlugs(Enigma e, String plaintext, Pair<String, Integer> potentialCrib) {
		return findPlugs(e, plaintext, potentialCrib, 'G');
	}

	public static ArrayList<Plug> findMaybePlugs(Enigma e, String plaintext, Pair<String, Integer> potentialCrib) {
		return findPlugs(e, plaintext, potentialCrib, 'M');
	}

	/**
	 * Prints out a 2D array. Used for testing mappings
	 * @param m
	 */
	public static void print(char[][] m) {
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				System.out.print(m[i][j]);
			}
			System.out.println("");
		}
	}

	/**
	 * Encrypts the alphabet a set amount of times. This is called with no plugs set. 
	 * This is used for knowing what letter the rotors would encrypt a character as. 
	 * @param enigma
	 * @param buffer How many letters within the ciphertext 
	 * @param size How many times to encrypt the alphabet
	 * @return
	 */
	public static ArrayList<String> successiveAlphabetWithoutPlugboard(Enigma enigma, int buffer, int size) {
		// remember what rotor wheels were at
		String rotorSettings = enigma.getRotorSettings();
		System.out.println("Testing in successive. Key is " + rotorSettings);

		//initialize
		ArrayList<String> encryptions = new ArrayList<String>();
		enigma.resetPlugboard();

		// initialize with empty strings, because we go one letter at a time through them all
		for (int i = 0; i < size; i++) {
			encryptions.add("");
		}

		//go through alphabet
		for (int l = 65; l < 91; l++) {
			char letter = (char) l;
			enigma.setRotorSettings(rotorSettings);
			
			//throw away character encryptions
			for (int i = 0; i < buffer; i++) {
				enigma.encryptChar('A');
			}
			
			//actual encryptions
			for (int i = 0; i < size; i++) {
				String s = encryptions.get(i);
				s += enigma.encryptChar(letter);
				encryptions.set(i, s);
			}
		}
		
		// reset enigma back to what it was
		enigma.setRotorSettings(rotorSettings);

		return encryptions;

	}
	
	/**
	 * Goes through mapping and only puts in plugs from goodPlugs
	 */
	public static char[][] eraseMapping(char[][] map, List<Plug> goodPlugs) {
		//reset
		for (int i = 0; i < map.length; i++) {
			map[i][1] = '_';
			map[i][2] = '_';
		}
		for (Plug p : goodPlugs) {
			for (int i = 0; i < map.length; i++) {
				map[i][1] = p.get(map[i][0]);
				map[i][2] = p.get(map[i][3]);
			}

		}
		return map;
	}

	/**
	 * A crib is a match of ciphertext/plaintext. With the way Engima works, letters
	 * can't map to itself. So this method goes through ciphertext and finds all
	 * Strings where the plaintext fits in without letters mapping to itself. We also
	 * want to know the offset from the beginning of the message.
	 * This isn't actually really being called by anything, because assuming we know
	 * exactly where the crib is.
	 * @param ciphertext Should be longer than plaintext
	 * @param plaintext  Plaintext word you know appears in ciphertext. Usually 10-14
	 *            letters
	 */
	public static ArrayList<Pair<String, Integer>> findPotentialCribs(String ciphertext, String plaintext) {
		ArrayList<Pair<String, Integer>> output = new ArrayList<>();

		//Go through the ciphertext up to where the known plaintext can fit in
		for (int m = 0; m < ciphertext.length() - plaintext.length(); m++) {
			boolean good = true;
			//Go through entire length of known plaintext
			for (int i = 0; i < plaintext.length(); i++) {
				//check to see if their characters are the same, if so, not good
				if (((int) ciphertext.charAt(m + i) - (int) plaintext.charAt(i)) == 0) {
					good = false;
				}

			}
			//add to list along with its offset
			if (good) {
				String s = ciphertext.substring(m, m + plaintext.length());
				Pair<String, Integer> p = new Pair<>(s, m);
				output.add(p);
			}
		}

		return output;
	}

	/**
	 * Finds all disjoint cycles
	 * 
	 * @param encryptedKeys
	 *            Example: "ABCABC" --> "OJHUIP"
	 * @return String of cycles
	 */
	public static String findCycles(ArrayList<String> encryptedKeys) {
		HashMap<Character, Character> mapAD = new HashMap<>();
		HashMap<Character, Character> mapBE = new HashMap<>();
		HashMap<Character, Character> mapCF = new HashMap<>();

		for (String s : encryptedKeys) {
			mapAD.put(s.charAt(0), s.charAt(3));
			mapBE.put(s.charAt(1), s.charAt(4));
			mapCF.put(s.charAt(2), s.charAt(5));
		}

		System.out.println(mapAD);
		System.out.println(mapBE);
		System.out.println(mapCF);

		return "";
	}
}
