public class SelectionSort {

	public static void sort(int[] a) {
		for (int i = a.length - 1; i > 0; --i) {
			int first = 0;
			for (int j = 1; j <= i; ++j) {
				if (a[j] < a[first]) {
					first = j;
				}
			}

			int tmp = a[first];
			a[first] = a[i];
			a[i] = tmp;
		}
	}

	public static void main(String[] args) {
		int[] a = { 100, 5, 9, 4, 5, 6 };
		sort(a);
	}

}
