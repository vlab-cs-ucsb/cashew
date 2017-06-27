package engagement1.subspace.lib;

import org.apache.commons.math3.geometry.euclidean.threed.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;

public class S2Point implements Point<Sphere2D>
{
    public static final S2Point PLUS_I;
    public static final S2Point PLUS_J;
    public static final S2Point PLUS_K;
    public static final S2Point MINUS_I;
    public static final S2Point MINUS_J;
    public static final S2Point MINUS_K;
    public static final S2Point NaN;
    private static final long serialVersionUID = 20131218L;
    private final double theta;
    private final double phi;
    private final Vector3D vector;
    
    public S2Point(final double theta, final double phi) throws OutOfRangeException {
        this(theta, phi, vector(theta, phi));
    }
    
    public S2Point(final Vector3D vector) throws MathArithmeticException {
        this(FastMath.atan2(vector.getY(), vector.getX()), Vector3D.angle(Vector3D.PLUS_K, vector), vector.normalize());
    }
    
    private S2Point(final double theta, final double phi, final Vector3D vector) {
        this.theta = theta;
        this.phi = phi;
        this.vector = vector;
    }
    
    private static Vector3D vector(final double theta, final double phi) throws OutOfRangeException {
        if (phi < 0.0 || phi > 3.141592653589793) {
            throw new OutOfRangeException(phi, 0, 3.141592653589793);
        }
        final double cosTheta = FastMath.cos(theta);
        final double sinTheta = FastMath.sin(theta);
        final double cosPhi = FastMath.cos(phi);
        final double sinPhi = FastMath.sin(phi);
        return new Vector3D(cosTheta * sinPhi, sinTheta * sinPhi, cosPhi);
    }
    
    public double getTheta() {
        return this.theta;
    }
    
    public double getPhi() {
        return this.phi;
    }
    
    public Vector3D getVector() {
        return this.vector;
    }
    
    public Space getSpace() {
        return Sphere2D.getInstance();
    }
    
    public boolean isNaN() {
        return Double.isNaN(this.theta) || Double.isNaN(this.phi);
    }
    
    public S2Point negate() {
        return new S2Point(-this.theta, 3.141592653589793 - this.phi, this.vector.negate());
    }
    
    public double distance(final Point<Sphere2D> point) {
        return distance(this, (S2Point)point);
    }
    
    public static double distance(final S2Point p1, final S2Point p2) {
        return Vector3D.angle(p1.vector, p2.vector);
    }
    
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof S2Point)) {
            return false;
        }
        final S2Point rhs = (S2Point)other;
        if (rhs.isNaN()) {
            return this.isNaN();
        }
        return this.theta == rhs.theta && this.phi == rhs.phi;
    }
    
    public int hashCode() {
        if (this.isNaN()) {
            return 542;
        }
        return 134 * (37 * MathUtils.hash(this.theta) + MathUtils.hash(this.phi));
    }
    
    static {
        PLUS_I = new S2Point(0.0, 1.5707963267948966, Vector3D.PLUS_I);
        PLUS_J = new S2Point(1.5707963267948966, 1.5707963267948966, Vector3D.PLUS_J);
        PLUS_K = new S2Point(0.0, 0.0, Vector3D.PLUS_K);
        MINUS_I = new S2Point(3.141592653589793, 1.5707963267948966, Vector3D.MINUS_I);
        MINUS_J = new S2Point(4.71238898038469, 1.5707963267948966, Vector3D.MINUS_J);
        MINUS_K = new S2Point(0.0, 3.141592653589793, Vector3D.MINUS_K);
        NaN = new S2Point(Double.NaN, Double.NaN, Vector3D.NaN);
    }
}
