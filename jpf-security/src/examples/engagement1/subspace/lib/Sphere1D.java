package engagement1.subspace.lib;

import java.io.*;
import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;

public class Sphere1D implements Serializable, Space
{
    private static final long serialVersionUID = 20131218L;
    
    public static Sphere1D getInstance() {
        return LazyHolder.INSTANCE;
    }
    
    public int getDimension() {
        return 1;
    }
    
    public Space getSubSpace() throws NoSubSpaceException {
        throw new NoSubSpaceException();
    }
    
    private Object readResolve() {
        return LazyHolder.INSTANCE;
    }
    
    private static class LazyHolder
    {
        private static final Sphere1D INSTANCE;
        
        static {
            INSTANCE = new Sphere1D();
        }
    }
    
    public static class NoSubSpaceException extends MathUnsupportedOperationException
    {
        private static final long serialVersionUID = 20140225L;
        
        public NoSubSpaceException() {
            super(LocalizedFormats.NOT_SUPPORTED_IN_DIMENSION_N, new Object[] { 1 });
        }
    }
}
