
public class TestFPOps {

    public static double testFPEq(double x) {
        double y = 3.14;
        if (x == y) {
            for (int i = 0; i < 1000; i++)
              y=y*5.5;
        } else {
            return 0;
        }
        return y;
    }
    
    public static double testFPNEq(double x) {
        double y = 3.14;
        if (x != y) {
            for (int i = 0; i < 1000; i++)
              y=y*5.5;
        } else {
            return 0;
        }
        return y;
    }

    public static double testFPL(double x) {
        double y = 3.14;
        if (x < y) {
            for (int i = 0; i < 1000; i++)
              y=y*5.5;
        } else {
            return 0;
        }
        return y;
    }

    public static double testFPG(double x) {
        double y = 3.14;
        if (x > y) {
            for (int i = 0; i < 1000; i++)
              y=y*5.5;
        } else {
            return 0;
        }
        return y;
    }

    public static void main(String[] args) {
        testFPEq(0);
        testFPNEq(0);
        testFPL(0);
        testFPG(0);
    }
}
