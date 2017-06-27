/**
* @author corina pasareanu corina.pasareanu@sv.cmu.edu
*
*/

package challenge2hack.app;

import challenge2hack.util.HashTable;

public class TestApp {
    
    public static void main(final java.lang.String[] array) {
        
        int int1 = 0;
        try {
            int1 = Integer.parseInt(array[0]);
        }
        catch (NumberFormatException ex) {
            System.out.println("table-size must be an integer: " + array[0]);
        }
        if (int1 <= 0) {
            throw new RuntimeException("table-size must be positive: ");
        }
        final HashTable hashTable = new HashTable(int1);
        for(int i=0;i<int1;i++) {
        	hashTable.put(new String(), new String());
        }
        hashTable.get(new String());
        System.err.println("Goodbye!");
    }
}