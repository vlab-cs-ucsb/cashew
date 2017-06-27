package visualizer;


/**
 * random example...
 *
 */
public class SimpleSys {
	
	public static void main(String[] args) {
		SimpleSys s = new SimpleSys();
		int val = s.compAB(2, 2);
		if(val > 0) {
		  val++;
		} else
		  val *=2;
	}

	public int compAB(int a, int b) {
		if(a > b) {
		  if(a < b) {
        b += number(b); //infeasible
      } 
		  b += 300;
		  if(a > 300) {
		    b += number(b) + calc(12);
		  } else {
		    if(a  < 5) {
		      int i = 0, c=a;
		      while(i < a) {
		        if(a < 5) {
		          a =2+3+c;
		        } else if(c == 2) {
		          c++;
		        } else {
		          c = c+2+a;
		        }
		        while(a < 2) {
		          if(a < 5000) {
	              a =2 + 2;
	            }
		          c++;
		        }
		        c+=2;
		        i++;
		      }
		    }
		    b += 300;
		  }
		  return 200;
		}
		else
		  return 2* a;
	}
	
	public int number(int a) {
		if(a == 200) {
		  System.out.println("in number() if ");
		  return calc(2);
		} else {
		  System.out.println("in number() else");
		  return calc(2);
		}
	}
	
	 public int calc(int a) {
	    if(a == 200) {
	      System.out.println("in calc() if");
	      return 2;
	    } else {
	      System.out.println("in calc() else");
	      return 9;
	    }
	  }
}
