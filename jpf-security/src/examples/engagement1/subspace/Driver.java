package engagement1.subspace;

import java.util.Iterator;
import java.util.Random;

import engagement1.subspace.util.GeoMultiTrie;
import engagement1.subspace.util.GeoPoint;
import engagement1.subspace.util.GeoSearchResult;
import gov.nasa.jpf.symbc.Debug;

public class Driver {
    
	protected GeoMultiTrie<String> locationToUsername;
	
	public Driver(){
	}
	
	private void initConcreteValues(){
		Random rg = new Random();
		for(int i = 0; i <= 10; i++){
			// double latitude = rg.nextDouble();
	        // double longitude = rg.nextDouble();
			double latitude = i * 5;
			double longitude = i * 7;
			GeoPoint pnt = new GeoPoint(latitude, longitude);
			locationToUsername.add(pnt, "username"+ i);
		}
	}
	
	public void testSearch(){
		locationToUsername = new GeoMultiTrie<String>();
		initConcreteValues();
		GeoPoint pnt = makeSymbolicGeoPoint();
		Iterator<GeoSearchResult<String>> iter = locationToUsername.search(pnt);
		if(iter.hasNext()){
			iter.next();
		}
	}
	
	public void testAdd(){
		locationToUsername = new GeoMultiTrie<String>();
		initConcreteValues();
		GeoPoint pnt = makeSymbolicGeoPoint();
		locationToUsername.add(pnt, "new user");
	}
	
	private GeoPoint makeSymbolicGeoPoint(){
		double latitude = Debug.makeSymbolicReal("latitude");
        double longitude = Debug.makeSymbolicReal("longtitude");
		return new GeoPoint(latitude, longitude);
	}
	
	public void testRegister(){
		Database db = new Database();
		/*
		for(int i = 1; i < 3; i++){
			db.registerUser("abc" + i, Integer.toString(123 + i * 10), "abc" 
						+ Integer.toString(i * 3) + "@localhost");
		}
		//*/
		String username1 = Debug.makeSymbolicString("username1", 3);
		db.registerUser(username1, "password1", "hail1@localhost");
		String username2 = Debug.makeSymbolicString("username2", 3);
		db.registerUser(username2, "password2", "hail2@localhost");
	}
	
    public static void main (String[] args) {
    	Driver driver = new Driver();
    	
    	if(args[0].equals("register")){
    		driver.testRegister();
    		return;
    	}
    	
    	// different type of test
    	
    	/*
    	if(args[0].equals("add")){
    		driver.testAdd();
    		return;
    	}
    	if(args[0].equals("search")){
    		driver.testSearch();
    		return;
    	}
    	//*/
    }
}
