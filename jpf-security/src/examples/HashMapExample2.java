
import java.util.HashMap;
//import java.util.HashMap;
import java.util.Map;

//import java7.util.HashMap;
//import gov.nasa.jpf.symbc.Debug;

public class HashMapExample2 {

  public static int test(int x){
    HashMap<Integer,Integer> map = new HashMap<>();
    for (int i = 0; i < 10; i++){
      map.put(i, i*2);
    }
    Integer ii = x;
    int limit=map.get(ii);
    
    int result = 0;
    for (int i = 0; i <limit; i++){
      result++;
    }
    System.out.println("result "+result);
    return result;
  }
  public static void main(String[] args) {
	test(-1);
  }
}