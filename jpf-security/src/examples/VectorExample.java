

import java.util.Vector;

public class VectorExample {

	public static void add(int simple) {
		Vector<Integer> v = new Vector<Integer>(10);
		v.add(10);
		v.add(20);
		v.add(30);
		System.out.println("result1 "+v.get(simple));
	}

	public static void main(String[] args) {
		add(0);
	}
}
