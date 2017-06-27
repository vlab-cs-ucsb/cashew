package engagement1.subspace.lib;

import java.io.*;
import org.apache.commons.math3.exception.*;

public interface Space extends Serializable
{
    int getDimension();
    
    Space getSubSpace() throws MathUnsupportedOperationException;
}
