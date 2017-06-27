package benchmarks.stringrot;

import gov.nasa.jpf.symbc.Debug;

/**
 * @author Kasper Luckow
 *
 */
public class StringRotation {
//This functionr return lexicographically minimum
//rotation of str
public String minLexRotation(String str)
{
   // Find length of given string
   int n = str.length();

   // Create an array of strings to store all rotations
   String arr[] = new String[n];

   // Create a concatenation of string with itself
   String concat = str + str;

   // One by one store all rotations of str in array.
   // A rotation is obtained by getting a substring of concat
   for (int i = 0; i < n; i++)
       arr[i] = concat.substring(i, n);

   // Sort all rotations
   //sort(arr, arr+n);

   // Return the first rotation from the sorted array
   return arr[0];
}

private static void solve(char[] necklace) {
  int n = necklace.length, range = n<<1;
  int i = 0, j = 1, gap = 0;
  while (i+gap < range && j+gap < range) {
      char ci = (i+gap < n) ? necklace[i+gap] : necklace[i+gap-n];
      char cj = (j+gap < n) ? necklace[j+gap] : necklace[j+gap-n];
      if (ci == cj) {
          gap++;
      } else if (ci < cj) {
          j = (j+gap+1 > i) ? j+gap+1 : i+1;
          gap = 0;
      } else {
          i = (i+gap+1 > j) ? i+gap+1 : j+1;
          gap = 0;
      }
  }
  System.out.println((i < j) ? i+1 : j+1);
}

public static void main(String[] args) {
  int N = Integer.parseInt(args[0]);
  
  char[] arr = new char[N];
  
  for(int i = 0; i < N; i++) {
    arr[i] = Debug.makeSymbolicChar("ch" + i);
  }
  
  solve(arr);
}

}
