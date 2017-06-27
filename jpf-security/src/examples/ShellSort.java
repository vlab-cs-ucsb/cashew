public class ShellSort {

	public static void sort(int[] a) {
		int increment = a.length / 2;
		while (increment > 0) {
			for (int i = increment; i < a.length; i++) {
				int j = i;
				int temp = a[i];
				while (j >= increment && a[j - increment] > temp) {
					a[j] = a[j - increment];
					j = j - increment;
				}
				a[j] = temp;
			}
			if (increment == 2) {
				increment = 1;
			} else {
				increment *= (5.0 / 11);
			}
		}
	}

	public static void main(String[] args) {
		int[] a = { 100, 5, 9, 4, 5, 6 };
		sort(a);
	}

}
