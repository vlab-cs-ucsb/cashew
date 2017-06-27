
public class BasicArrayExample {

  public static void get(int idx) {
    int[] a = new int[]{0, 1, 2};
    boolean[] ba = new boolean[]{true, true, true};
    char[] ca = new char[]{'a', 'b', 'c'};
    float[] fa = new float[]{0.1f, 0.2f, 0.3f};
    short[] sa = new short[]{1, 2, 3};
    double[] da = new double[]{0.1, 0.2, 0.3};
    long[] la = new long[]{0, 1, 2};

    Integer[] aI = new Integer[3];
    for (int i = 0; i < 3; i++) {
      aI[i] = new Integer(i);
    }

    aI[idx] = new Integer(20);
    a[idx] = 10;
    ba[idx] = false;
    ca[idx] = 'x';
    fa[idx] = 11.11f;
    sa[idx] = -3;
    da[idx] = 111.11;
    la[idx] = 111;

    System.out.println("result " + a[idx] + " " + aI[idx] + " "
            + ba[idx] + " " + ca[idx] + " " + fa[idx] + " " + sa[idx] + " " + da[idx] + " " + la[idx]);

  }

  public static void main(String[] args) {
    get(0);
    System.out.println("done");
  }
}
