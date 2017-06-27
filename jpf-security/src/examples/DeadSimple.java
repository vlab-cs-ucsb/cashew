import java.lang.Thread;

public class DeadSimple {

	public static void takeTime(int n) {
		if (n < 0) n = -n;
		if (n < 50) return;

		try {
			if (n < 75) Thread.sleep(2000);
			if (n > 75) Thread.sleep(4000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		takeTime(1);
	}
}
