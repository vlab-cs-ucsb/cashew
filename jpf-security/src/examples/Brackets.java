// The classic brackets problem: print out all the combinations of opening and closing brackets
public class Brackets {
	
	private static void doBrackets(String current, int opened, int closed, int total){
		if (opened==closed && opened==total){
			System.out.println(current);
		}
		if (opened<total){
			doBrackets(current+"(", opened+1, closed, total);
		}
		if (opened>closed)
			doBrackets(current+")", opened, closed+1, total);
	}
	
	public static void brackets(int n){
		doBrackets("", 0, 0, n);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int n = 15;
		brackets(n);
	}

}
