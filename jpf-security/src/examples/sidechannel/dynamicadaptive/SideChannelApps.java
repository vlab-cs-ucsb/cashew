package sidechannel.dynamicadaptive;

public class SideChannelApps {

    public static String add(int[] H, int[] L) {
        if(L[0] + L[1] == H[0]) {
            return "RIGHT";
        } else {
            if(L[0] + L[1] > H[0]) {
                sleep(100);
            }
            return "WRONG";
        }
    }

    public static String password(int[] H, int[] L) {
        int i;
        int n = H.length < L.length ? H.length : L.length; // minimum
        for(i = 0; i < n; i++) {
            if(L[i] != H[i]) {
                return "WRONG";
            } else {
                sleep(10);
            }
        }
        if(i == L.length && i == H.length) {
            return "RIGHT";
        } else {
            return "WRONG";
        }
    }

    private static void sleep(long msec) {
        try {
            Thread.sleep(msec);
        } catch(InterruptedException e) {
            e.printStackTrace();
            System.exit(5);
        }
    }

}

