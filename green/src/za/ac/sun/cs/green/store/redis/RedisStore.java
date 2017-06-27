package za.ac.sun.cs.green.store.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Level;

import redis.clients.jedis.Jedis;
import za.ac.sun.cs.green.Green;
import za.ac.sun.cs.green.store.BasicStore;
import za.ac.sun.cs.green.util.Base64;
import za.ac.sun.cs.green.util.Configuration;
import za.ac.sun.cs.green.util.Reporter;

/**
 * An implementation of a {@link za.ac.sun.cs.green.store.Store} based on redis (<code>http://www.redis.io</code>).
 * 
 * @author Jaco Geldenhuys <jaco@cs.sun.ac.za>
 */
public class RedisStore extends BasicStore {

	/**
	 * The time (in seconds) of inactivity until the connection to the redis store timeout.
	 */
	private static final int TIMEOUT = 2000;
	
	/**
	 * Connection to the redis store.
	 */
	private Jedis db = null;

	/**
	 * Number of times <code>get(...)</code> was called.
	 */
	private int retrievalCount = 0;

	/**
	 * Number of times <code>put(...)</code> was called.
	 */
	private int insertionCount = 0;

	/**
	 * The default host of the redis server.
	 */
	private final String DEFAULT_REDIS_HOST = "localhost";

	/**
	 * Options passed to the LattE executable.
	 */
	private final int DEFAULT_REDIS_PORT = 6379;
	
	/**
	 * Constructor to create a default connection to a redis store running on the local computer.
	 */
	public RedisStore(Green solver, Properties properties) {
		super(solver);
		String h = properties.getProperty("green.redis.host", DEFAULT_REDIS_HOST);
		int p = Configuration.getIntegerProperty(properties, "green.redis.port", DEFAULT_REDIS_PORT);
		db = new Jedis(h, p, TIMEOUT);
	}
	
	/**
	 * Constructor to create a connection to a redis store given the host and the port.
	 * 
	 * @param host the host on which the redis store is running
	 * @param port the port on which the redis store is listening
	 */
	public RedisStore(Green solver, String host, int port) {
		super(solver);
		db = new Jedis(host, port, TIMEOUT);
	}

	@Override
	public void report(Reporter reporter) {
		reporter.report(getClass().getSimpleName(), "retrievalCount = " + retrievalCount);
		reporter.report(getClass().getSimpleName(), "base64EncodingTime = " + base64EncodingTime);
		reporter.report(getClass().getSimpleName(), "base64DecodingTime = " + base64DecodingTime);
		reporter.report(getClass().getSimpleName(), "javaObjectSerializationTime = " + javaObjectSerializationTime);
		reporter.report(getClass().getSimpleName(), "javaObjectDeserializationTime = " + javaObjectDeserializationTime);
		reporter.report(getClass().getSimpleName(), "redisGetTime = " + redisGetTime);
		reporter.report(getClass().getSimpleName(), "redisPutTime = " + redisPutTime);
	}
	
	@Override
	public synchronized Object get(String key) {
		retrievalCount++;
		long t0, t1;
		try {
			t0 = System.currentTimeMillis();
			String s = db.get(key);
			t1 = System.currentTimeMillis();
			redisGetTime += (t1-t0);			
			return (s == null) ? null : fromString(s);
		} catch (IOException x) {
			log.log(Level.SEVERE, "io problem", x);
		} catch (ClassNotFoundException x) {
			log.log(Level.SEVERE, "class not found problem", x);
		}
		return null;
	}

	@Override
	public synchronized void put(String key, Serializable value) {
		insertionCount++;
		try {
			long t0, t1;
			String valueString = toString(value);
			t0 = System.currentTimeMillis();
			//System.out.println("Putting: " + valueString + " (" + valueString.length() + " bytes)");
			System.out.println("Putting value into redis (" + valueString.length() + " bytes)");
			db.set(key, valueString);
			t1 = System.currentTimeMillis();
			redisPutTime += (t1-t0);
		} catch (IOException x) {
			log.log(Level.SEVERE, "io problem", x);
		}
	}

	
	
	
	
	
	// Adding this to profile Base64 encoding time (looking for bottleneck when storing matrices)
	protected static long base64EncodingTime = 0;
	protected static long base64DecodingTime = 0;
	protected static long javaObjectSerializationTime = 0;
	protected static long javaObjectDeserializationTime = 0;
	protected static long redisPutTime = 0;
	protected static long redisGetTime = 0;

	/** Read the object from Base64 string. */
	protected static Object fromString(String s) throws IOException, ClassNotFoundException {
		long t0, t1;
		
		// Decode Base64-encoded plaintext string obtained from store into byte array
		t0 = System.currentTimeMillis();
		byte[] blob = Base64.decode(s);
		t1 = System.currentTimeMillis();
		base64DecodingTime += (t1-t0);

		// De-serialize byte array into Java object
		t0 = System.currentTimeMillis();
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(blob));
		Object o = ois.readObject();
		ois.close();
		t1 = System.currentTimeMillis();
		javaObjectDeserializationTime += (t1-t0);

		return o;
	}

	/** Write the object to a Base64 string. */
	protected static String toString(Serializable o) throws IOException {
		long t0, t1;

		// Serialize Java object into byte array
		t0 = System.currentTimeMillis();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		byte[] blob = baos.toByteArray();
		t1 = System.currentTimeMillis();
		javaObjectSerializationTime += (t1-t0);

		// Encode byte array into Base64-encoded plaintext string
		t0 = System.currentTimeMillis();
		char[] encodedBlob = Base64.encode(blob);
		String encodedBlobString = new String(encodedBlob);
		t1 = System.currentTimeMillis();
		base64EncodingTime += (t1-t0);
		
		return encodedBlobString;
	}
	
	
	
	
	
}
