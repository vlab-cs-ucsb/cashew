package engagement1.subspace.lib;

import java.io.*;

public interface Point<S extends Space> extends Serializable
{
    Space getSpace();
    
    boolean isNaN();
    
    double distance(final Point<S> p0);
}
