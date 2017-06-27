
import java.util.*;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sn
 */
public class CollectionsTest {

  public static int hashMapTest(int x) {
    Map<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i < 100; i++) {
      map.put(i, i * 2);
    }

    int limit = map.get(x);
    int result = 0;
    for (int i = 0; i < limit; i++) {
      result++;
    }

    return result;
  }

  public static int linkedListTest(int x) {
    List<Integer> list = new LinkedList<>();
    for (int i = 0; i < 1000; i++) {
      list.add(i);
    }
    return list.get(x);
  }

  public static void hashSetTest(int x) {

  }

  public static int priorityQueueTest(int[] x) {
    Queue<Integer> queue = new PriorityQueue<>();
    for (int i = 0; i < x.length; i++) {
      queue.add(x[i]);
    }
    int c = queue.poll();
    int result = 0;
    for (int i = 0; i < c; i++) {
      result++;
    }
    return result;    
  }

  public static void main(String args[]) {
    linkedListTest(0);
    priorityQueueTest(new int[] { 1, 2, 6 });
    hashMapTest(0);
    /*hashSetTest(0);
     */
  }
}
