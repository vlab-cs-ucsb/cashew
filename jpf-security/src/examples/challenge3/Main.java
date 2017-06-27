package challenge3;

import java.util.*;
import java.io.*;

public class Main
{
    public static void main(final String[] array) {
        Scanner scanner = null;
        try {
            scanner = new Scanner((InputStream)new FileInputStream(array[0]));
        }
        catch (FileNotFoundException ex) {
            System.err.println("File not found");
            System.exit(-1);
        }
        if (new NaivePWCheck(scanner.nextLine()).verifyPassword(array[1])) {
            System.out.println("Accept");
        }
        else {
            System.out.println("Reject");
        }
    }
}
