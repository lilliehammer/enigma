package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

import javafx.util.Pair;
import model.AlreadyMappedException;
import model.Decryption;
import model.Enigma;
import model.Plug;

public class Main {

	//Enigma and its encryption settings
	static Enigma enigma = new Enigma();
	static String rotorKey = "KEY";
	static ArrayList<Plug> mappedPlugs = new ArrayList<>();
	//Default message to encrypt
	static String plaintext = "MESSAGEFROMLILLIEHELLOTHISISASECRETENIGMAMESSAGE";
	
	//Decryption
	static ArrayList<Plug> goodPlugs = new ArrayList<>();
	static ArrayList<Plug> maybePlugs = new ArrayList<>();
	static ArrayList<Plug> removedPlugs = new ArrayList<>();
	static String ciphertext;
	static String decryptedText;
	static String knownCrib = "MESSAGE"; //known plaintext
	
	static Scanner scanner = new Scanner(System.in);
	
	final static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	String input2 = "There's a snakeskin bedroom Bang in the middle" + "Of a funny little place called Hell, oh well"
			+ "Everybody there's got trouble by the dozen" + "It's a lonely little place to dwell"
			+ "Hope I don't make the trip through the floor" + "You told me once" + "I want it but you wanted it more"
			+ "I know kung fu you know nothing" + "But together we can make it real, good deal"
			+ "Kicking our own way out of every prison" + "With my brains and your high heels"
			+ "Love your myth coupled with your vast ocean" + "Take my eyes as the prize for your devotion";

	public static void main(String[] args) {

		Main main = new Main();

		enigma.setRotorWheels(1, 2, 3); // set which rotors to use
		// set what letter is currently displayed in window for rotors
		enigma.setRotorSettings(rotorKey);
		
		System.out.println("Encrypt ('E') message or decrypt ('D')?");
		String line = scanner.nextLine();
		if (line.equalsIgnoreCase("E")) 
			plaintext = main.encryptString();
		else if (line.equals("D")) {
			//Rotor key option
			System.out.println("Enter in rotor key (in format 'ABC') or use default (which is 'KEY', press enter)");
			line = scanner.nextLine();
			if (!line.equals("")) 
				rotorKey = line;
			
			else
				rotorKey = "KEY";
			
			//Message to decrypt
			System.out.println("Enter in ciphertext to decrypt or use a default message (press enter)");
			line = scanner.nextLine();
			if (!line.equals("")) 
				ciphertext = line;
			else 
				ciphertext = enigma.encryptString(plaintext);
				
			
			main.plugboardMenuLoop();
		}
			
		
//		boolean userInput = false;
//		if (userInput) {
//			plaintext = main.encryptString();
//		}
//		else {
//			mapPlugboard();
//			ciphertext = enigma.encryptString(plaintext);
//		}
//		
//		main.plugboardMenuLoop();
		
		// encryptStrings();
	}
	
	public String encryptString() {
		System.out.println("The rotor key is what letters the rotors are currently displaying.\n" +
				"Enter in your own, formatted 'KEY', or use default (which is 'KEY') by not entering anything.");
		String line = scanner.nextLine();
		if (!line.equals("")) {
			rotorKey = line;
		}
		
		System.out.println("The plugs are what switches letters.\n If 'AB' is a plug, then the letter 'A' would " +
				"get turned to a 'B' in the plugboard.\nEnter in plugs, space-separated like 'AB EF'.\n" +
				"Must be letters that are next to each other in the alphabet. Options are:\n" +
				Decryption.generateSuccessivePlugs());
		line = scanner.nextLine();
		String[] plugsToAdd = line.split(" ");
		for (String s : plugsToAdd) {
			//Plug newPlug = new Plug(s);
			try {
				enigma.mapLetters(s);
				//Plug p = new Plug(s);
			} catch (AlreadyMappedException e){
				System.out.println("There was an error adding " + s );
			}
		}
		
		System.out.println("Enter in message to encrypt. Because decryption works with known plaintext " +
				"attacks, must begin with 'MESSAGE'.");
		boolean okay = false;
		String message = "";
		while (!okay) {
			message = scanner.nextLine();
			if (message.substring(0, knownCrib.length()).equalsIgnoreCase(knownCrib)) 
				okay = true;
			
		}
		ciphertext = enigma.encryptString(message);
		System.out.println("Message (in uppercase, all symbols and spaces removed) is:\n" + enigma.parseString(message));
		System.out.printf("Ciphertext is: (copy and paste this to decrypt next. And remember the key used!)\n" + ciphertext);
			
		return ciphertext;
		
	}

	public static void mapPlugboard() {
		try {
			 enigma.mapLetters('A', 'B');
			 enigma.mapLetters('D', 'E');
			 enigma.mapLetters('G', 'H');
			 enigma.mapLetters('N', 'M');
			 enigma.mapLetters('X', 'Y');
//			enigma.mapLetters("QJ");
//			enigma.mapLetters("PW");
//			enigma.mapLetters("VB");
//			enigma.mapLetters("UG");
//			enigma.mapLetters("LE");

		} catch (AlreadyMappedException e) {
			System.out.println("Error mapping letters: Trying to map already-mapped letters");
			e.printStackTrace();
			return;
		}
	}

	public static void mapPlugboard(ArrayList<Plug> plugs) {
		for (Plug p : plugs) {
			try {
				enigma.mapLetters(p.getCharOne(), p.getCharTwo());
			} catch (AlreadyMappedException e) {
				System.out.println("Error mapping letters: Trying to map already-mapped letters");
				e.printStackTrace();
				return;
			}
		}
	}
	
	public void testPlugsWithLetter(char c, ArrayList<Plug> plugs, String ciphertext, String comparingText, String known) {
		System.out.println("Testing maybe plugs");
		
		ArrayList<Plug> testedPlugs = new ArrayList<>();
		
		
		for (int i = 0; i < plugs.size(); i++) {
			Plug testingPlug = plugs.get(i);
			if (testingPlug.contains(c)) {
				try {
					//reset enigma
					enigma.mapPlugboard(mappedPlugs);
					enigma.setRotorSettings(rotorKey);
					enigma.mapLetters(testingPlug.toString());
					
					testedPlugs.add(testingPlug);
					System.out.println(testedPlugs.size()-1 + " : " + testingPlug);
					System.out.printf("%s\n%s\n%s\n\n", known, comparingText, enigma.encryptString(ciphertext));
				} catch (AlreadyMappedException e) {
					
				}
				
				
			}
		}
	}
	
	public void testPlugsWithLetter(char c) {
		ArrayList<Plug> combined = new ArrayList<>(goodPlugs);
		combined.addAll(maybePlugs);
		System.out.println("Will test: " + combined);
		ArrayList<Plug> tested = new ArrayList<>();
		
		for (Plug p : combined) {
			if (p.contains(c)) {
				try {
					//reset enigma
					enigma.mapPlugboard(mappedPlugs);
					enigma.setRotorSettings(rotorKey);
					enigma.mapLetters(p.toString());
					
					tested.add(p);
					//removePlug("- " + p);
					System.out.println(tested.size()-1 + " : " + p);
					System.out.printf("     %s\nOld: %s\nNew: %s\n\n", knownCrib, decryptedText, enigma.encryptString(ciphertext));
				} catch (AlreadyMappedException e) {
					
				}
			}
		}
		
		System.out.println("Enter number to keep. To remove all, '-'. Uncertain, press enter");
		String line = scanner.nextLine();
		try {
			int i = Integer.parseInt(line);
			addPlug("+ " + tested.remove(i));
			
		} catch (NumberFormatException e) {
			//System.out.println("There was an error you should probably check in testing chars");
			//add the rest to removed
			if (line.equals("-")) {
				for (Plug p : tested) {
					removedPlugs.add(p);
				}
			}
		}
		
		
	}
	
	/* <knownCrib>
	 * <current decrypted text>
	 * <current plugs>
	 * <known good plugs (if any)
	 * <potential plugs>
	 * <------>
	 * '+ AM'	Add plug to mapping
	 * '- AM'	Remove plug from mapping
	 * '? AM'	Inquire if plug is alright
	 * 'T E'	Test all remaining plugs with specific letter in it
	 * 'T AM'	Test specific plug
	 * 'R'		Rerun it with new plaintext
	 * 'P'		Update known plaintext
	 */
	public void plugboardMenuLoop() {
		
		System.out.println("Rotor key is " + rotorKey);
		//mapPlugboard();
		//enigma.setRotorSettings(rotorKey);
		//ciphertext = enigma.encryptString(plaintext);
		
		enigma.resetPlugboard();
		enigma.setRotorSettings(rotorKey);


		updatePlugLists();
		//decryptedTextNoPlugs = enigma
		
		
		while (true) {
			//reset machine
			enigma.mapPlugboard(mappedPlugs);
			enigma.setRotorSettings(rotorKey);
			decryptedText = enigma.encryptString(ciphertext);
			
			enigma.mapPlugboard(mappedPlugs);
			enigma.setRotorSettings(rotorKey);
			
			System.out.printf("\n%s\n%s\n", knownCrib, decryptedText);
			System.out.printf("Current plugs: %s\nKnown good plugs: %s\nPotential plugs: %s\nRemoved plugs: %s\n", 
					mappedPlugs, goodPlugs, maybePlugs, removedPlugs);
			System.out.println("----------------");	
			System.out.println("'+ AM'	Add plug to mapping");
			System.out.println("'- AM'	Remove plug from mapping");
			System.out.println("'? AM'	Inquire if plug is alright");
			System.out.println("'T E'	Test all remaining plugs with specific letter in it");
			System.out.println("'T AM'	Test specific plug");
			System.out.println("'R'		Rerun it with new plaintext");
			System.out.println("'P'		Update known plaintext");
			System.out.println("'U'		Update plugs");
			
			String line = scanner.nextLine();
			char instruction = line.charAt(0);
			
			//+
			if (instruction == '+') 
				addPlug(line);
			
			else if (instruction == '-') 
				removePlug(line);
			
			else if (instruction == 'U')
				removeConflictingPlugs();
			
			else if (instruction == '?') {
				if (inquirePlug(line))
					System.out.println("Yes");
				else
					System.out.println("No");
			}
			
			else if (instruction == 'T') {
				String[] temp = line.split(" ");
				//Test one letter
				if (temp[1].length() == 1) {
					testPlugsWithLetter(temp[1].charAt(0));
				}
				//Test one plug without committing
				else {
					Plug newPlug = new Plug(temp[1]);
					
					try {
						enigma.mapLetters(newPlug.toString());
						String newDecryption = enigma.encryptString(ciphertext);
						System.out.printf("     %s\nOld: %s\nNew: %s\n\n", knownCrib, decryptedText, newDecryption);
						System.out.println("Keep (y or n)\n");
						String newline = scanner.nextLine();
						if (!newline.equalsIgnoreCase("y"))
							removePlug(line);
							
					} catch (AlreadyMappedException e) {
					}
					
				} 
			} //end 'T' command
			
			else if (instruction == 'R') {
				updatePlugLists();
			}
			
			else if (instruction == 'P') {
				System.out.println("Enter new known plaintext\n" + decryptedText);
				knownCrib = scanner.nextLine();
			}
			
			
		}
	}
	
	public boolean inquirePlug(String line) {
		String[] plugLine = line.split(" ");
		Plug p = new Plug(plugLine[1]);
		for (Plug inquire : goodPlugs) {
			if (inquire.equals(p))
				return true;
		}
		for (Plug inquire : maybePlugs) {
			if (inquire.equals(p))
				return true;
		}
		return false;
	}
	
	public void removeConflictingPlugs() {
		ArrayList<Plug> newG = new ArrayList<>();
		ArrayList<Plug> newM = new ArrayList<>();
		ArrayList<Plug> newR = new ArrayList<>();
		enigma.mapPlugboard(mappedPlugs);
		for (Plug p : goodPlugs) {
			if (enigma.plugboard.plugIsOkayToAdd(p) && !mappedPlugs.contains(p))
				newG.add(p);
			else
				newR.add(p);
		}
		for (Plug p : maybePlugs) {
			if (enigma.plugboard.plugIsOkayToAdd(p) && !mappedPlugs.contains(p))
				newM.add(p);
			else
				newR.add(p);
		}
		goodPlugs = newG;
		maybePlugs = newM;
		removedPlugs = newR;
	}
	
//	public void updatePlugDecryptionStuff() {
//		goodPlugs = Decryption.findGoodPlugs(enigma, knownCrib,
//				new Pair<String, Integer>(ciphertext.substring(0, knownCrib.length()), 0));
//		maybePlugs = Decryption.findMaybePlugs(enigma, knownCrib, 
//				new Pair<String, Integer>(ciphertext.substring(0, knownCrib.length()), 0));
//		
//		decryptedText = enigma.encryptString(ciphertext);
//		
//		removeConflictingPlugs();
//		//System.out.println("Testing: " + decryptedText);
//	}
	
	
	public void updatePlugLists() {
		
		
		goodPlugs = Decryption.findGoodPlugs(enigma, knownCrib,
				new Pair<String, Integer>(ciphertext.substring(0, knownCrib.length()), 0));
		maybePlugs = Decryption.findMaybePlugs(enigma, knownCrib, 
				new Pair<String, Integer>(ciphertext.substring(0, knownCrib.length()), 0));
		
		removeConflictingPlugs();
	}
	
	public void removePlug(String line) {
		String[] plugs = line.split(" ");
		Plug plug = new Plug(plugs[1]);
		//remove
		for (int i = 0; i < mappedPlugs.size(); i++) {
			if (mappedPlugs.get(i).equals(plug)) {
				mappedPlugs.remove(i);
				removedPlugs.add(plug);
				break;
			}
			
		}
		for (int i = 0; i < goodPlugs.size(); i++) {
			if (goodPlugs.get(i).equals(plug)) {
				goodPlugs.remove(i);
				removedPlugs.add(plug);
				break;
			}
			
		}
		for (int i = 0; i < maybePlugs.size(); i++) {
			if (maybePlugs.get(i).equals(plug)) {
				maybePlugs.remove(i);
				removedPlugs.add(plug);
				break;
			}
			
		}		
	
	}
	
	public void addPlug(String line) {
		String[] plugs = line.split(" ");
		Plug plug = new Plug(plugs[1]);
		if (enigma.plugboard.plugIsOkayToAdd(plug)) {
			mappedPlugs.add(plug);
		}
		else {
			System.out.println("Plug wasn't okay to add.");
			return;
		}
		
		//remove
		for (int i = 0; i < goodPlugs.size(); i++) {
			if (goodPlugs.get(i).equals(plug)) {
				
				goodPlugs.remove(i);
				break;
			}
			
		}
		for (int i = 0; i < maybePlugs.size(); i++) {
			if (maybePlugs.get(i).equals(plug)) {
				maybePlugs.remove(i);
				break;
			}
			
		}
		
		
	}

	public static void stringFreq(String s) {
		Map<Character, Integer> count = new HashMap<>();
		for (int i = 65; i < 91; i++) {
			count.put((char) i, 0);
		}
		for (int i = 0; i < s.length(); i++) {
			Character c = s.charAt(i);

			count.put(c, count.get(c) + 1);

		}
		for (int i = 65; i < 91; i++) {
			System.out.println((char) i + " frequency of " + count.get((char) i));

		}
	}

	

}
