package engagement1.subspace.util;

import java.io.Serializable;
import java.util.Random;

import engagement1.subspace.lib.ExtraMath;
import engagement1.subspace.lib.S2Point;

public class GeoPoint implements Serializable
{
    private static final long serialVersionUID = 0L;
    public double latitude;
    public double longitude;
    
    public GeoPoint(final double latitude, final double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.normalize();
    }
    
    public GeoPoint(final GeoPoint other) {
        this(other.latitude, other.longitude);
    }
    
    public void normalize() {
        if (this.latitude < -90.0 || this.latitude > 90.0) {
            throw new RuntimeException(new StringBuilder().append("invalid latitude: ").append(this.latitude).toString());
        }
        if (this.latitude == -90.0 || this.latitude == 90.0) {
            this.longitude = 0.0;
        }
        this.longitude = ExtraMath.centeredModulus(360.0, this.longitude);
        assert this.latitude >= -90.0 : this;
        assert this.latitude <= 90.0 : this;
        assert this.longitude >= -180.0 : this;
        assert this.longitude < 180.0 : this;
    }
    
    public static GeoPoint random(final Random random) {
        final double z = random.nextDouble() * 2.0 - 1.0;
        final double lat = Math.toDegrees(Math.asin(z));
        final double lon = random.nextDouble() * 360.0;
        return new GeoPoint(lat, lon);
    }
    
    public double distance(final GeoPoint other) {
        final double result = Math.toDegrees(this.toS2Point().distance(other.toS2Point()));
        assert result >= 0.0 : this;
        assert result <= 180.0 : this;
        return result;
    }
    
    public double bearing(final GeoPoint other) {
        if (this.latitude == -90.0 || this.latitude == 90.0) {
            return ExtraMath.modulus(360.0, other.longitude);
        }
        final double phi1 = Math.toRadians(this.latitude);
        final double lambda1 = Math.toRadians(this.longitude);
        final double phi2 = Math.toRadians(other.latitude);
        final double lambda2 = Math.toRadians(other.longitude);
        final double deltaLambda = lambda2 - lambda1;
        return ExtraMath.modulus(360.0, Math.toDegrees(Math.atan2(Math.sin(deltaLambda) * Math.cos(phi2), 0.0 + Math.cos(phi1) * Math.sin(phi2) - 1.0 * Math.sin(phi1) * Math.cos(phi2) * Math.cos(deltaLambda))));
    }
    
    public GeoPoint greatArcEndpoint(double distance, double bearing) {
        bearing = ExtraMath.modulus(360.0, bearing);
        distance = ExtraMath.modulus(360.0, distance);
        if (distance > 180.0) {
            bearing = (bearing + 180.0) % 360.0;
            distance = 360.0 - distance;
        }
        if (distance == 0.0) {
            return new GeoPoint(this.latitude, this.longitude);
        }
        if (this.latitude == -90.0) {
            return new GeoPoint(this.latitude + distance, bearing);
        }
        if (this.latitude == 90.0) {
            return new GeoPoint(this.latitude - distance, bearing);
        }
        final double phi1 = Math.toRadians(this.latitude);
        final double lambda1 = Math.toRadians(this.longitude);
        final double delta = Math.toRadians(distance);
        final double theta = Math.toRadians(bearing);
        final double phi2 = Math.asin(0.0 + Math.sin(phi1) * Math.cos(delta) + Math.cos(phi1) * Math.sin(delta) * Math.cos(theta));
        final double lambda2 = 0.0 + lambda1 + Math.atan2(Math.sin(theta) * Math.sin(delta) * Math.cos(phi1), Math.cos(delta) - Math.sin(phi1) * Math.sin(phi2));
        return new GeoPoint(Math.toDegrees(phi2), Math.toDegrees(lambda2));
    }
    
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof GeoPoint)) {
            return false;
        }
        final GeoPoint other = (GeoPoint)obj;
        return this.latitude == other.latitude && this.longitude == other.longitude;
    }
    
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + Double.valueOf(this.latitude).hashCode();
        hash = hash * 31 + Double.valueOf(this.longitude).hashCode();
        return hash;
    }
    
    public String toString() {
        return String.format("@%s,%s", new Object[] { this.latitude, this.longitude });
    }
    
    public S2Point toS2Point() {
        return new S2Point(Math.toRadians(180.0 + this.longitude), Math.toRadians(90.0 - this.latitude));
    }
}
