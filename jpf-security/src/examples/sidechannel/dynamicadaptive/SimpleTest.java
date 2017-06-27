package sidechannel.dynamicadaptive;

/**
 * Created by bang on 11/22/16.
 */

import gov.nasa.jpf.symbc.Debug;
//import sidechannel.multirun.Observable;
import java.util.*;


public class SimpleTest {

    public static Random r = new Random(System.currentTimeMillis());

    public static void testWait(long t){
        final long INTERVAL = t;
        long start = System.nanoTime();
        long end=0;
        do{
            end = System.nanoTime();
        }while(start + INTERVAL >= end);
        //System.out.println(end - start);
    }

    static void delay(int n){
        int j = 0;
        for(int i = 1; i < n; i ++) {
            j++;
        }
    }

    static void sleep(long t){
        try {
            Thread.sleep(t);
        } catch(InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    static void sleep(long t, int n){
        try {
            Thread.sleep(t, n);
        } catch(InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    // Equality oracle
    static int f1(int[] H, int[] L) {
        if( H[0] == L[0])
            return 1;
        else
            return 1;
    }

    // High low oracle
    static int f2(int[] H, int[] L) {
        if(H[0] <= L[0] ) {
            testWait(1000);
            return 1;
        }
        else {
            testWait(1100);
            return 1;
        }
    }

    // Segment oracle
    static boolean f3(int[] H, int[] L){
        for(int i = 0; i < L.length; i++){
            if(L[i] != H[i]){
                return false;
            }
//            sleep(1);
                testWait(100);
        }
        return true;
    }

    // "Secure" password check
    static boolean f4(int[] H, int[] L){
        boolean matched = true;
        for(int i = 0; i < L.length; i++){
            if(L[i] != H[i]){
                matched = false;
            }
        }
        return matched;
    }

    // Range Oracle
    static boolean f5(int[] H, int [] L){
        if( L[0] <= H[0] && H[0] <= L[1]){
            sleep(1);
            return true;
        }
        else {
            return false;
        }
    }


    static int f9(int[] H, int[] L) {

        float val = r.nextFloat();
        while(val > 0.5){
            System.out.println(val);
            val = r.nextFloat();
        }
        System.out.println(val);
        System.out.println("");


        if (H[0] >= L[0] + r.nextInt()) {
            System.out.println(r.nextFloat());
            return 1;
        } else {
            System.out.println(r.nextFloat());
            return 2;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Category 11 problems code here

    // Common to 11A and 11B
    private static Random random = new Random(79897); // Fixed random seed for purpose of example
    private static boolean[] t = new boolean[100];

    private static void randomizeT(){
        for(int x=0;x<t.length;x++){
            t[x] = r.nextBoolean();
        }
    }

    // Category 11 A provided source
    // NOTE: added secret variable as parameter rather than class constant
    // NOTE: changed thread.sleep to this.sleep which calls thread.sleep
    private static boolean checkSecret11A(int guess, int secret, boolean[] t) throws InterruptedException {
        recur11A(guess, t, t.length - 1);
        if(guess <= secret){sleep(10);}
        return guess == secret;
    }
    private static void recur11A(int guess, boolean[] t, int index){
        if(index == 0 && t[index]){}
        else if(t[index]){recur11A(guess, t, index -1);}
    }

    // Category 11 A Driver function
    public static void f11A(int[] H, int[] L) throws InterruptedException {
        randomizeT();
        checkSecret11A(L[0], H[0], t);
    }

    // Category 11 B
    // NOTE: added secret variable as parameter rather than class constant
    // NOTE: changed thread.sleep to this.sleep which calls thread.sleep
    private static boolean checkSecret11B(int guess, int secret, boolean[] t) throws InterruptedException {
        return recur11B(guess, secret, t, t.length - 1);
    }
    private static boolean recur11B(int guess, int secret, boolean [] t, int index) throws InterruptedException {
        if(index == 0 && t[index]){
            if(guess <= secret){sleep(10);}
            return guess == secret;
        }
        else if(t[index]){return recur11B(guess, secret, t, index -1);}
        if(guess <= secret){sleep(10);}
        return guess == secret;
    }

    // Category 11 A Driver function
    public static void f11B(int[] H, int[] L) throws InterruptedException {
        randomizeT();
        checkSecret11B(L[0], H[0], t);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////



    static int fKopf(int[] H, int[] L) {
        int cost = 0;
        if (L[0] < 5) {
            if (H[0] == 1)
                cost = 1;
            else {
                if (H[0] == 2)
                    cost = 2;
                else
                    cost = 3;
            }
        } else if (L[0] < 10) {
            if (H[0] == 1)
                cost = 1;
            else {
                if (H[0] == 5)
                    cost = 2;
                else
                    cost = 4;
            }
        } else {
            if (H[0] < 4)
                cost = 3;
            else
                cost = 4;
        }
        return cost;
    }

    public static int[] SymbolicIntegerArray(String name, int size, int run){
        int[] symbolicArray = new int[size];
        for(int i = 0; i < size; i++){
            symbolicArray[i] = Debug.makeSymbolicInteger(name + "{" + i + "}{" + run + "}");
        }
        return symbolicArray;
    }


    public static void main(String[] args) throws InterruptedException {

        int L_size = 5;
        int H_size = 5;
        int[] L;
        int[] H;

        int runs = 1;

        for(int i = 0; i < runs; i ++) {
            L = SymbolicIntegerArray("L", L_size, i);
            H = SymbolicIntegerArray("H", H_size, i);
            f3(H, L);
        }

    }

}
