package sidechannel.dynamicadaptive;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import static javafx.scene.input.KeyCode.H;

/**
 * Created by bang on 12/16/16.
 */
public class SimpleTestProfiler extends SimpleTest{


    static class Witness{
        int[] H = null;
        int[] L = null;

        Witness(int H_size, int L_size){
            H = new int[H_size];
            L = new int[L_size];
        }

    }

    public static Vector<Witness> getWitnesses(String filename){

        Vector<Witness> witnesses = new Vector<Witness>();

        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            for(String line; (line = br.readLine()) != null; ) {

//                System.out.println(line);
                String[] lineStrings = line.split(" ");

                Vector<Integer> lineValues = new Vector<Integer>();

                int H_size = Integer.parseInt(lineStrings[0]);
                int L_size = Integer.parseInt(lineStrings[1]);

                Witness w = new Witness(H_size, L_size);

                for(int i = 0; i < H_size; i++){
                    w.H[i] = Integer.parseInt(lineStrings[i + 2]);
//                    System.out.println("H[" + i + "]=" + w.H[i]);
                }

                for(int i = 0; i < L_size; i++){
                    w.L[i] = Integer.parseInt(lineStrings[i + H_size + 2]);
//                    System.out.println("L[" + i + "]=" + w.L[i]);
                }

                witnesses.add(w);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return witnesses;
    }

    public static void profile(Vector<Witness> witnesses, int samples) throws InterruptedException {
        for(int i = 0; i < samples; i++){
            for(Witness w : witnesses) {
                long startTime = System.nanoTime();
                f3(w.H,w.L);
                long endTime = System.nanoTime();
                long duration = endTime - startTime;
                System.out.print(duration + "\t");
            }
            System.out.println("");
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        System.out.println("Profiling");

        String filename = args[0];
        int samples = Integer.parseInt(args[1]);

        Vector<Witness> witnesses = getWitnesses(filename);

        profile(witnesses, samples);

    }
}
