import gov.nasa.jpf.symbc.Debug;


public class AutoBox {

 public static void test1(Integer x) {
	System.out.println("result "+Debug.getSymbolicIntegerValue(x));
 }
 public static void test(int x) {
	 test1(x);
	 System.out.println("result "+Debug.getSymbolicIntegerValue(x));
 }
 public static void testfloat1(Float x) {
		System.out.println("result float"+Debug.getSymbolicRealValue(x));
	 }
	 public static void testfloat(float x) {
		 testfloat1(x);
		 System.out.println("result float"+Debug.getSymbolicRealValue(x));
	 }
 public static void main(String[] args) {
		test(0);
		testfloat(0.0f);
	}
}