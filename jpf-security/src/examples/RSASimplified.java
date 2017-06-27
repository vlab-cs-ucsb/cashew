
/**
 * @author corina pasareanu corina.pasareanu@sv.cmu.edu
 *
 */
import gov.nasa.jpf.symbc.Debug;

//import java.math.BigInteger;
//import java.security.SecureRandom;

/**
 * Simple RSA public key encryption algorithm implementation.
 * <P>
 * Taken from "Paj's" website:
 * <TT>http://pajhome.org.uk/crypt/rsa/implementation.html</TT>
 * <P>
 * Adapted by David Brodrick
 */
public class RSASimplified {
	private int n, d, e;

	private int bitlen = 1024; // needs to change
	
	public static int count = 0;

	/** Create an instance that can encrypt using someone elses public key. */
	public RSASimplified(int newn, int newe) {
		n = newn;
		e = newe;
	}

	/** Create an instance that can both encrypt and decrypt. */
	public RSASimplified(int bits) {
		bitlen = bits;
		/*
		 * SecureRandom r = new SecureRandom(); BigInteger p = new
		 * BigInteger(bitlen / 2, 100, r); BigInteger q = new BigInteger(bitlen
		 * / 2, 100, r);
		 */

		int p = 3;
		int q = 11;
		n = p * q;// 33
		int m = (p - 1) * (q - 1);// φ(n) 20
		// e = 7;
		// d = 3;// (d * e) % φ(n) = 1
		e = 3;
		d = 7;
	}

	/** Encrypt the given plaintext message. */
	// should change the implementation of exponentiation

	public synchronized int encrypt(int message) {
		// message^7 % 33; m=2 -> 29
		//  return modPowSimple(message, e, n);
		return modPowFast(message, e, n);

	}

	int modPowSimple(int num, int e, int m) { // computes num^e mod m
		// if (e<0) return 0;
		int count = 0;
		Debug.assume(e > 0 && e < 32);
		int result = 1;
		for (int i = 0; i < e; i++) {
			result = result * num;
			count++;
		}
		result = result % m;
		System.out.println("Cost is: " + count);
		// System.out.println(Debug.getPC_prefix_notation());
		return result;
	}

	/*
	int modPowMontgomery(int num, int e, int n){
		int m = 1;
		    for (int i = 0; i<len; i++) {
		      m = m*m mod n;
		      if ( k[i] == 1 ) {
		        m = m*num mod n;
		      }
		    }
	}
	//*/
	
	/*
	public static int mul(int k, int m) { // computes num^e mod m
		int p = 0;
		while (k > 0){
			if (k % 2 == 1){
				p = p + m;
			}
			p = p * 2;
			k /= 2;
		}
		
		return p / 2;
	}
	//*/
	
	static int modPowFast1(int num, int e, int m) { // computes num^e mod m
		Debug.assume(e > 0 && e < 8);
		int x = 1;
		int y = num;
		
		count = 0;
		
		int nbit = 2;
	
		
		while (nbit > 0 && e > 0) {
			nbit--;
			if (e % 2 == 1) {
				int tmp = x * y;
				if (tmp > m){
					tmp = tmp - m;
					count++;
				}
				x = tmp % m;
				count++;
			}
			y = (y * y) % m; // squaring the base
			e /= 2;
			count++;
			// System.out.println(nbit);
		}
		// System.out.println("Cost is: " + count);
		// System.out.println(Debug.getPC_prefix_notation());
		return (int) x % m;
	}
	
	int modPowFast(int num, int e, int m) { // computes num^e mod m
		Debug.assume(e > 0 && e < 32);
		int x = 1;
		int y = num;
		int count = 0;
		while (e > 0) {
			if (e % 2 == 1) {
				x = (x * y) % m;
				count++;
			}
			y = (y * y) % m; // squaring the base
			e /= 2;
			count++;
		}
		System.out.println("Cost is: " + count);
		// System.out.println(Debug.getPC_prefix_notation());
		return (int) x % m;
	}

	/** Decrypt the given ciphertext message. */
	public synchronized int decrypt(int cipher) {
		// return modPowSimple(cipher,d, n);
		return modPowFast(cipher, d, n);
	}

	/** Decrypt the given ciphertext message. */
	public synchronized int decrypt1(int cipher) {
		// return modPowSimple(cipher,d, n);
		return modPowFast1(cipher, d, n);
	}
	
	/** Generate a new public and private key set. */
	/*
	 * public synchronized void generateKeys() { SecureRandom r = new
	 * SecureRandom(); BigInteger p = new BigInteger(bitlen / 2, 100, r);
	 * BigInteger q = new BigInteger(bitlen / 2, 100, r); n = p.multiply(q);
	 * BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q
	 * .subtract(BigInteger.ONE)); e = new BigInteger("3"); while
	 * (m.gcd(e).intValue() > 1) { e = e.add(new BigInteger("2")); } d =
	 * e.modInverse(m); }
	 */

	/** Return the modulus. */
	public synchronized int getN() {
		return n;
	}

	/** Return the public key. */
	public synchronized int getE() {
		return e;
	}

	/** Return the private key. */
	public synchronized int getD() {
		return d;
	}

	/** Trivial test program. */
	public static void main(String[] args) {
		//*
		RSASimplified rsa = new RSASimplified(1024);

		// String text1 = "just got back home";//"Yellow and Black Border
		// Collies";
		// String text1 = Debug.makeSymbolicString("text1");

		/*
		int plaintext = 5;// new BigInteger(text1.getBytes());
		System.out.println("d " + rsa.getD() + " e " + rsa.getE() + " n " + rsa.getN());
		int ciphertext = rsa.encrypt(plaintext);
		
		System.out.println("Plaintext: " + plaintext);
		System.out.println("Ciphertext: " + ciphertext);
		
		plaintext = rsa.decrypt(ciphertext);
		int plaintext1 = rsa.decrypt1(ciphertext);
		
		System.out.println("Plaintext: " + plaintext);
		System.out.println("Plaintext1: " + plaintext1);
		
		//*/
		
		/*
		System.out.println("The result is: " + mul(3,6) + " vs " + 3 * 6);
		System.out.println("The result is: " + mul(2,5) + " vs " + 2 * 5);
		System.out.println("The result is: " + mul(7,9) + " vs " + 7 * 9);
		System.out.println("The result is: " + mul(6,15)+ " vs " + 6 * 15);
		//*/
		
		//*
		// int plaintext1 = 5;// new BigInteger(text1.getBytes());
		// int plaintext2 = 10;
		// System.out.println("d " + rsa.getD() + " e " + rsa.getE() + " n " + rsa.getN());
		
		/*
		int plaintext1 = 5;// new BigInteger(text1.getBytes());
		int plaintext2 = 10;
		
		int ciphertext1 = rsa.encrypt(plaintext1);
		int ciphertext2 = rsa.encrypt(plaintext2);
		
		// int decrypt1 = rsa.decrypt1(ciphertext1);
		// int decrypt2 = rsa.decrypt1(ciphertext2);
		
		// System.out.println("Obserable is: " + decrypt1 + " " + decrypt2);
		//*/
		int l1 = Debug.makeSymbolicInteger("l1");
		int l2 = Debug.makeSymbolicInteger("l2");
		int h = Debug.makeSymbolicInteger("h");

		modPowFast1(l1,h,33);
		int count1 = count;
		modPowFast1(l2,h,33);
		System.out.println("Cost is: "+ count1 + " " + count);
		
		// System.out.println("Cost is: " + modPowFast1(l1,h,33) + " " +  modPowFast1(l2,h,33));
		// System.out.println(Debug.getPC_prefix_notation());

	}
}
