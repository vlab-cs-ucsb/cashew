package engagement1.subspace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import engagement1.subspace.util.GeoMultiTrie;

public class Database implements Serializable
{
	/*/
    private static final long serialVersionUID = 0L;
    private static final Logger LOGGER;
    private static final int CORE_POOL_SIZE = 4;
    protected transient ReadWriteLock lock;
    private transient File backingFile;
    protected transient ScheduledExecutorService writeExecutorService;
    protected transient ScheduledExecutorService executorService;
    //*/
    protected Map<String, User> usernameToUser;
    protected Map<String, String> tokenToUsername;
    protected Map<String, String> emailToUsername;
    /*
    protected GeoMultiTrie<String> locationToUsername;
    protected Map<String, Set<String>> aliasToUsers;
    protected transient Random random;
    //*/
   
    
    // Sang: this is for the sake of modelling only
    public Database(){
    	usernameToUser = new HashMap<String, User>();
    	tokenToUsername = new HashMap<String, String>();
    	emailToUsername = new HashMap<String, String>();
    }
    
    public String registerUser(final String username, final String password, final String emailAddress) {
        boolean changed = false;
        // this.lock.writeLock().lock();
        try {
            boolean conflict = false;
            if (this.usernameToUser.containsKey(username)) {
                conflict = true;
            }
            if (this.emailToUsername.containsKey(emailAddress)) {
                conflict = true;
            }
            if (conflict) {
                return null;
            }
            changed = true;
            final User user = new User(username, password, emailAddress);
            // final String token = UUID.randomUUID().toString();
            this.usernameToUser.put(username, user);
            // this.tokenToUsername.put(token, username);
            this.emailToUsername.put(emailAddress, username);
            // return token;
            return "";
        }
        finally {
           //  this.lock.writeLock().unlock();
            if (changed) {
            	// TODO check, this does a lot of things
                // this.saveHint();
            }
        }
    }
    
    
}
