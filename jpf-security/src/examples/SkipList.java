import java.util.*;

public class SkipList {

	ArrayList<LinkedList<Integer>> lists = new ArrayList<LinkedList<Integer>>(4);

	public void insert(int value, int weight) {
		if (weight >= lists.size()) {
			for (int i = 0; i < weight - lists.size(); ++i) {
				lists.add(new LinkedList<Integer>());
			}
		}

		for (int i = 0; i < weight; ++i) {
			lists.get(i).add(value);
		}
	}

	public static void main(String[] args) {
		SkipList s = new SkipList();
		s.insert(1, 1);
		s.insert(1, 1);
		s.insert(1, 1);
		s.insert(1, 1);
		s.insert(1, 1);
	}
}
