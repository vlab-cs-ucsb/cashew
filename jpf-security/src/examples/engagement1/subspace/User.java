package engagement1.subspace;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import engagement1.subspace.util.Crypto;
import engagement1.subspace.util.GeoPoint;

public class User implements Serializable
{
    private static final long serialVersionUID = 0L;
    // private static final String CURRENT_CRYPT_ALG;
    private Status mStatus;
    private final String mUsername;
    private String mPasswordCrypt;
    private final String mEmailAddress;
    private GeoPoint mLocation;
    
    public User(final String username, final String password, final String emailAddress) {
        this.mStatus = Status.NEW;
        this.mUsername = username;
        //TODO this.mPasswordCrypt = Crypt.crypt(password);
        this.mPasswordCrypt = password;
        this.mEmailAddress = emailAddress;
        this.mLocation = null;
    }
    
    public synchronized Status getStatus() {
        return this.mStatus;
    }
    
    public synchronized boolean isActive() {
        switch (this.mStatus) {
            case OK: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public synchronized void setStatus(final Status status) {
        this.mStatus = status;
    }
    
    public String getUsername() {
        return this.mUsername;
    }
    
    public String getEmailAddress() {
        return this.mEmailAddress;
    }
    
    public synchronized GeoPoint getLocation() {
        return this.mLocation;
    }
    
    public synchronized void setLocation(final GeoPoint location) {
        this.mLocation = location;
    }
    
    public synchronized boolean authenticate(final String password) {
        // TODO final boolean authOk = Crypto.isEqual((CharSequence)this.mPasswordCrypt, 
    	// TODO			(CharSequence)Crypt.crypt(password, this.mPasswordCrypt));
    	final boolean authOk = Crypto.isEqual(this.mPasswordCrypt, password);
        if (!authOk) {
            return false;
        }
        /*TODO
        if (!this.mPasswordCrypt.startsWith(User.CURRENT_CRYPT_ALG)) {
             this.mPasswordCrypt = Crypt.crypt(password);
        	
        }
        //*/
        this.mPasswordCrypt = password;
        return true;
    }
    
    public String toString() {
        return String.format("%s[%s]", new Object[] { this.getClass().getName(), this.mUsername });
    }
    /*
    static {
        final Pattern algPattern = Pattern.compile("^\\$[^\\$]+\\$");
        // TODO final String crypt = Crypt.crypt("");
        final String crypt = "";
        final Matcher algMatcher = algPattern.matcher((CharSequence)crypt);
        if (!algMatcher.find()) {
            throw new RuntimeException("Invalid crypt() output: " + crypt);
        }
        CURRENT_CRYPT_ALG = algMatcher.group();
    }
    //*/
    
    public enum Status
    {
        OK, 
        NEW, 
        INACTIVE;
    }
}
