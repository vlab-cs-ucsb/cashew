package challenge2hack.app;

import challenge2hack.util.*;
import java.util.Vector;
import java.io.*;

public class CollisionFinder {

	static final char chars[] = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','X','Y','Z','0','1','2','3','4','5','6','7','8','9'};
	static final int NEEDED = 100000;
	static Vector<String> collisions = new Vector<String>();
	static final String FILENAME = "/home/rodykers/cmu/Challenge/ex2/wcinput.txt";
	
	private static String getString(int num) {
		String s = "";
		while (num>0) {
			s = chars[(num-1)%(chars.length)] + s;
			num = (num-1)/(chars.length);
		}
		return s;
	}
	
	public static void findCollisionsWith(int target) {
		HashTable hashTable = new HashTable(65536);
		int hash;
		for (int i = 0; collisions.size() < NEEDED; i++) {
			String s = getString(i);
			hash = hashTable.getBucket(s);
			if (hash==target) {
				System.out.println("Collision found: " + s);
				collisions.add(s);
				int avgTries = i/collisions.size();
				System.out.println("Average tries before finding collision: " + avgTries);
			}
		}
	}
	
	public static void writeToFile() {
		try {
			FileWriter fw = new FileWriter(new File(FILENAME));
			for (String key : collisions) {
				fw.write("put " + key + " something\n");
			}
			fw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		System.out.println("Here we go...");
		long start = System.nanoTime();
		findCollisionsWith(0);
		long end = System.nanoTime();
		long timesec = (end-start)/1000000000;
		System.out.println("Found " + collisions.size() + " collisions with hash 0 in " + timesec + " seconds");
		System.out.println("Writing to file: " + FILENAME);
		writeToFile();
		System.out.println("Done.");
	}
}
