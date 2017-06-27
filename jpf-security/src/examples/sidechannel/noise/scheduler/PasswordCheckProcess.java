package sidechannel.noise.scheduler;

/**
 *
 * @author Quoc-Sang Phan <sang.phan@sv.cmu.edu>
 *
 */
public class PasswordCheckProcess {
	
	protected static final int INIT = 0;
	protected static final int CHECK = 1;
	protected static final int DO = 2;
	protected static final int INCREASE = 3;
	protected static final int DONE = 4;
	
	protected int[] password;
	protected int[] input;
	
	int index;
	
	int state;
	
	/*
	 * This program assumes password and input have the same length
	 */
	public PasswordCheckProcess(int[] password, int[] input){
		this.password = password;
		this.input = input;
		state = INIT;
	}
	
	/*
	 * this method returns true if the process is done,
	 * regardless of its result
	 */
	public boolean execute() throws InterruptedException{
		switch(state){
		case INIT:
			index = 0;
			state = CHECK;
			return false;
		case CHECK:
			state = (index < password.length) ? DO : DONE;
			return false;
		case DO:
			if(password[index] != input[index]){
				state = DONE;
			} else{
				Thread.sleep(300);
				state = INCREASE;
			}
			return false;
		case INCREASE:
			++index;
			state = CHECK;
			return false;
		case DONE:
			return true;
		}
		return true;
	}
}
