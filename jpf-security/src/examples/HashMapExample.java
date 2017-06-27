




import java.util.HashMap;

import gov.nasa.jpf.symbc.Debug;



public class HashMapExample {

	public static void addThings(int[] nums) {
		boolean[] truths = new boolean[] { true, false, true };
		int size = nums.length < truths.length ? nums.length : truths.length;

		System.out.println("size "+size);
		HashMap<Integer, Boolean> h = new HashMap<>();
		for (int i = 0; i < size; ++i) {
				h.put(nums[i], truths[i]);
				System.out.println("result "+i+" "+h.get(nums[i]));
		}
		
	}

	public static void main(String[] args) {
		addThings(new int[] { 1, 2, 3 });
	}
}
