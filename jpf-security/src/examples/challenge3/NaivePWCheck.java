package challenge3;

public class NaivePWCheck
{
    private String password;
    
    // for adaptive greedy
    private long count;
    
    public NaivePWCheck(final String password) {
        this.password = password;
    }
    
    public boolean verifyPassword(final String s) {
    	count = 0;
        for (int i = 0; i < s.length(); ++i) {
            if (i >= this.password.length() || s.charAt(i) != this.password.charAt(i)) {
                return false;
            }
            count++;
            try {
                Thread.sleep(25L);
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        return s.length() >= this.password.length();
    }
    
    public long getCost(){
    	return count;
    }
}
