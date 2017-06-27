public class QuickSort {

	public static int partition(int arr[], int left, int right) {
		int i = left, j = right;
		int tmp;
		int pivot = arr[(left + right) / 2];

		while (i <= j) {
			while (arr[i] < pivot)
				i++;
			while (arr[j] > pivot)
				j--;
			if (i <= j) {
				tmp = arr[i];
				arr[i] = arr[j];
				arr[j] = tmp;
				i++;
				j--;
			}
		}

		return i;
	}

	public static void quickSort(int arr[], int left, int right) {
		int index = partition(arr, left, right);
		if (left < index - 1)
			quickSort(arr, left, index - 1);
		if (index < right)
			quickSort(arr, index, right);
	}

	public static void sort(int[] arr) {
		quickSort(arr, 0, arr.length - 1);
	}

	public static void print(int[] arr) {
		for (int i : arr)
			System.out.print(i + " ");

		System.out.println();
	}

	public static void main(String[] args) {
		//int[] arr = { 8, 7, 6, 5, 4, 3, 2, 1 };
		int[] arr = { 6, 5, 4, 3, 2, 1 };
		sort(arr);
	}
}
