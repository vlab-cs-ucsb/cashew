/**
* @author corina pasareanu corina.pasareanu@sv.cmu.edu
*
*/
// 
//simple example to illustrate Markov Chain Monte Carlo method

package mcmc;

import java.util.Random;

public class Main {

	public static boolean constraint (int x, int y) {
		boolean result = (x>0) && (x<=100) && (y>0) && (y<=100); 
		result = result && (x*x>=y);
		return result;
	}
	
	public static boolean mcmc(){
		assert(in.length==2);//to be lifted later
		/*
		// there are 6 possible moves
		// (in0-1,in1)(in0+1,in1),(in0,in1-1)(in0,in1+1)(in0-1,in1-1)(in0+1,in1+1)
		int in0=in[0];
		int in1=in[1];
		int rnd = new Random().nextInt(6);
		switch(rnd) {
		case 0: in0--;break;
		case 1: in0++;break;
		case 2: in1--;break;
		case 3: in1++;break;
		case 4: in0--;in1--;break;
		case 5: in0++;in1++;break;
		default: System.out.println("should not reach");break;
		}
		*/
		// try only 4 moves
				// (in0-1,in1)(in0+1,in1),(in0,in1-1)(in0,in1+1)
				int in0=in[0];
				int in1=in[1];
				int rnd = new Random().nextInt(4);
				switch(rnd) {
				case 0: in0--;break;
				case 1: in0++;break;
				case 2: in1--;break;
				case 3: in1++;break;
				default: System.out.println("should not reach");break;
				}
		if(constraint(in0,in1)) {
			in[0]=in0;
			in[1]=in1;
			return true;
		}
		return false;
		
	}
	
	static int in[];
	static int Nsamples=1000;
	
	public static void main(String[] args) {
		in=new int[2];
		//start with a solution
		in[0]=5;
		in[1]=25;
		for(int i=0;i<Nsamples;i++) {
			if(mcmc())
				System.out.println("in[0]:"+in[0]+" in[1]:"+in[1]);
		}

	}

}
