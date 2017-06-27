public class Except {

	public static void go(int a) throws Exception {
//		if (a < 0)
			throw new Exception();
	}

	public static void main(String[] args) throws Exception {
		go(1);
	}
}
