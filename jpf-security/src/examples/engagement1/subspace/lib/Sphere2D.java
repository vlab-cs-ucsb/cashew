package engagement1.subspace.lib;

import java.io.*;

public class Sphere2D implements Serializable, Space
{
    private static final long serialVersionUID = 20131218L;
    
    public static Sphere2D getInstance() {
        return LazyHolder.INSTANCE;
    }
    
    public int getDimension() {
        return 2;
    }
    
    public Sphere1D getSubSpace() {
        return Sphere1D.getInstance();
    }
    
    private Object readResolve() {
        return LazyHolder.INSTANCE;
    }
    
    private static class LazyHolder
    {
        private static final Sphere2D INSTANCE;
        
        static {
            INSTANCE = new Sphere2D();
        }
    }
}
