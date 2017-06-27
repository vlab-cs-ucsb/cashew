public class InsertionSort {

	public static void sort(int[] a) {
		for (int i = 1; i < a.length; ++i) {
			int j, tmp = a[i];
			for (j = i - 1; j >= 0 && tmp < a[j]; --j) {
				a[j + 1] = a[j];
			}

			a[j + 1] = tmp;
		}
	}

	public static void main(String[] args) {
		int[] a = { 100, 5, 9, 4, 5, 6 };
		sort(a);
	}

}
