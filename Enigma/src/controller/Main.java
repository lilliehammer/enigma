package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import model.AlreadyMappedException;
import model.Enigma;

public class Main {

	public static void main(String[] args) {
	
		Scanner scanner = new Scanner(System.in);
		Enigma enigma = new Enigma();
		
		int debug = 1;
		
		if (debug == 0) {
			//INPUT
			System.out.println("Which rotor would you like to use for rotor one?");
		}
		
		else {
			enigma.setRotors(1, 2, 3); //set which rotors to use
			//set what letter is currently displayed in window for rotors
			
			enigma.setUpLeftRotor('A');
			enigma.setUpMiddleRotor('D');
			enigma.setUpRightRotor('R');
			
			//set plugboard mappings
			try {
				enigma.mapLetters('C', 'O');
				enigma.mapLetters('D', 'E');
				enigma.mapLetters('H', 'A');
				enigma.mapLetters('I', 'M');
				enigma.mapLetters('F', 'U');
				enigma.mapLetters('N', 'Y');
			} catch (AlreadyMappedException e) {
				System.out.println("Error mapping letters: Trying to map already-mapped letters");
				e.printStackTrace();
				return;
			}
			
			//testing
			String input1 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
			"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
			"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
			//String input1 = "KZPDVRYMTOOTGRBMCHDQCMXQKKOBUYRRSIPLQHUUGIKJSQRBNURXUGJOISQMIJZKPHZJDSKFRFSROYY";
			System.out.println(enigma.parseString(input1));
			String output1 = enigma.encryptString(input1);
			System.out.println(output1);
			stringFreq(output1);
			
			
			String input2 = "It started out as nothing in the strangest sense" + 
					"He was never in his right mind, no defense" + 
					"She prayed for his attention, often on repeat" + 
					"Every time she heard his name and his heartbeat" +
					"He was in denial in his own backyard" +
					"Pleading for the rain just to come down hard" +
					"She bought him a bracelet and put it on his wrist" +
					"Like every time before she got close but missed" +
					"Romeo, Romeo I'm your Juliet" +
					"I'm the pot of gold that you haven't found yet" +
					"I'm here, right here" +
					"He said Juliet I believe every word you say" +
					"Time is running backwards every single day" +
					"I'm here, right here";
					
			//String input2 = "VBAWRWXJSADBKMJGQCLFYHXNVOLISPDBLZBAWEAVZTFSTYQYKWVGZOFOPQJLQFJAMHLAKCKTFVJKTCHP" +
			//"IFHVAXXRPEJOQDKQTFMSHEEALPRNINVPLQSEFRGUBZQROVSDDCAZYSKXCUFQIWVTSRZZU";
			System.out.println(enigma.parseString(input2));
			String output2 = enigma.encryptString(input2);
			System.out.println(output2);
			stringFreq(output2);
			
			//System.out.println("TESTING: Capital letter as number " + (char)65);
			
			
			
			
		}
		
	}
	
	public static void stringFreq(String s) {
		Map<Character, Integer> count = new HashMap<>();
		for (int i = 65; i < 91; i++) {
			count.put((char)i, 0);
		}
		for (int i = 0; i < s.length(); i++) {
			Character c = s.charAt(i);

			count.put(c, count.get(c) + 1);

		}
		for (int i = 65; i < 91; i++) {
			System.out.println((char)i + " frequency of " + count.get((char)i));
			
		}
	}

}
