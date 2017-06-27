import java.util.Random;

public class BubbleSort {
    public void sort(int a[]) {
        int i, j, temp, n = a.length;
        for (i = 0; i < n - 1; ++i) {
            for (j = 0; j < n - 1 - i; ++j) {
                if (a[j] > a[j + 1]) {
                    temp = a[j + 1];
                    a[j + 1] = a[j];
                    a[j] = temp;
                }
            }
        }
    }

    public static void main(String[] args) {
        int length = 4;
        if (args.length > 0) {
            try {
                length = Integer.parseInt(args[0]);
            } catch (Exception e) {}
        }

        Random rand = new Random();
        BubbleSort bs = new BubbleSort();

        int[] arr = new int[length];
        for (int i = 0; i < length; i++) {
            arr[i] = rand.nextInt(200) - 100;
        }

        bs.sort(arr);
    }
}
