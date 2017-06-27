package engagement1.subspace.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import engagement1.subspace.lib.ExtraMath;

public class GeoRectangle implements Serializable
{
    private static final long serialVersionUID = 0L;
    public static final double LAT_SOUTH_EDGE = -90.0;
    public static final double LAT_NORTH_EDGE;
    public static final double LON_WEST_EDGE = -180.0;
    public static final double LON_EAST_EDGE = 180.0;
    public static final GeoRectangle FULL;
    protected double south;
    protected double north;
    protected double west;
    protected double east;
    
    public GeoRectangle(final double south, final double north, final double west, final double east) {
        if (south < -90.0 || north > GeoRectangle.LAT_NORTH_EDGE || west < -180.0 || east > 180.0 || south >= north || west >= east) {
            throw new RuntimeException("invalid rectangle");
        }
        this.south = south;
        this.north = north;
        this.west = west;
        this.east = east;
    }
    
    public double width() {
        return this.east - this.west;
    }
    
    public boolean contains(final GeoPoint p) {
        return this.south <= p.latitude && this.north > p.latitude && this.west <= p.longitude && this.east > p.longitude;
    }
    
    public boolean isFullyDegenerate() {
    	return false;
        // return Math.nextUp(this.south) >= this.north || Math.nextUp(this.west) >= this.east;
    }
    
    public GeoRectangle[] quadrants() {
        if (this.isFullyDegenerate()) {
            return null;
        }
        final double midLat = ExtraMath.average(this.south, this.north);
        final double midLon = ExtraMath.average(this.west, this.east);
        return new GeoRectangle[] { new GeoRectangle(midLat, this.north, midLon, this.east), new GeoRectangle(midLat, this.north, this.west, midLon), new GeoRectangle(this.south, midLat, this.west, midLon), new GeoRectangle(this.south, midLat, midLon, this.east) };
    }
    
    public GeoPoint[] vertices() {
        final double southmost = this.south;
        final double northmost = Math.nextAfter(this.north, Double.NEGATIVE_INFINITY);
        final double westmost = this.west;
        final double eastmost = Math.nextAfter(this.east, Double.NEGATIVE_INFINITY);
        return new GeoPoint[] { new GeoPoint(northmost, eastmost), new GeoPoint(northmost, westmost), new GeoPoint(southmost, westmost), new GeoPoint(southmost, eastmost) };
    }
    
    public double getExtremumBearing(final GeoPoint center, final double distance, final boolean cw, final double guess) {
        assert this.contains(center.greatArcEndpoint(distance, guess));
        boolean allCardinalsContained = true;
        final boolean[] containedCardinals = new boolean[4];
        for (int i = 0; i < containedCardinals.length; ++i) {
            if (!(containedCardinals[i] = this.contains(center.greatArcEndpoint(distance, 90.0 * i)))) {
                allCardinalsContained = false;
            }
        }
        if (allCardinalsContained) {
            return cw ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        double minBearing = guess;
        int cardinal;
        int cardinalProgression;
        if (cw) {
            cardinal = (int)Math.ceil(guess / 90.0) % 4;
            cardinalProgression = 1;
        }
        else {
            cardinal = (int)Math.floor(guess / 90.0);
            cardinalProgression = -1;
        }
        while (containedCardinals[cardinal]) {
            minBearing = cardinal * 90.0;
            cardinal = ExtraMath.modulus(4, cardinal + cardinalProgression);
        }
        double maxBearing = cardinal * 90.0;
        while (minBearing != maxBearing) {
            double midBearing = ExtraMath.midpoint(360.0, cw ? minBearing : maxBearing, cw ? maxBearing : minBearing);
            if (midBearing == minBearing) {
                midBearing = (cw ? ExtraMath.nextUp(360.0, midBearing) : ExtraMath.nextDown(360.0, midBearing));
            }
            if (this.contains(center.greatArcEndpoint(distance, midBearing))) {
                minBearing = midBearing;
            }
            else {
                maxBearing = (cw ? ExtraMath.nextDown(360.0, midBearing) : ExtraMath.nextUp(360.0, midBearing));
            }
        }
        return minBearing;
    }
    
    public double maxContainedDistance(final GeoPoint from, final double bearing, final double guess) {
        assert this.contains(from.greatArcEndpoint(guess, bearing));
        double minDistance = guess;
        double maxDistance = 180.0;
        while (minDistance < maxDistance) {
            double midDistance = ExtraMath.average(minDistance, maxDistance);
            if (midDistance == minDistance) {
                midDistance = Math.nextAfter(midDistance, Double.POSITIVE_INFINITY);
            }
            if (this.contains(from.greatArcEndpoint(midDistance, bearing))) {
                minDistance = midDistance;
            }
            else {
                maxDistance = Math.nextAfter(midDistance, Double.NEGATIVE_INFINITY);
            }
        }
        return minDistance;
    }
    
    public static Collection<GeoPoint> adjacentVertices(final GeoRectangle r1, final GeoRectangle r2) {
        final GeoPoint[] r1Vertices = r1.vertices();
        final GeoPoint[] r2Vertices = r2.vertices();
        final Collection<GeoPoint> result = (Collection<GeoPoint>)new HashSet();
        if (r1.south > r2.north || r1.north < r2.south) {
            return result;
        }
        if (r1.east == r2.west || (r1.east == 180.0 && r2.west == -180.0)) {
            if (r1.north <= r2.north) {
                result.add(r1Vertices[0]);
            }
            if (r2.north <= r1.north) {
                result.add(r2Vertices[1]);
            }
            if (r1.south >= r2.south) {
                result.add(r1Vertices[3]);
            }
            if (r2.south >= r1.south) {
                result.add(r2Vertices[2]);
            }
        }
        if (r2.east == r1.west || (r2.east == 180.0 && r1.west == -180.0)) {
            if (r1.north <= r2.north) {
                result.add(r1Vertices[1]);
            }
            if (r2.north <= r1.north) {
                result.add(r2Vertices[0]);
            }
            if (r1.south >= r2.south) {
                result.add(r1Vertices[2]);
            }
            if (r2.south >= r1.south) {
                result.add(r2Vertices[3]);
            }
        }
        return result;
    }
    
    public String toString() {
        return String.format("%s[%s, %s, %s, %s]", new Object[] { this.getClass().getName(), this.south, this.north, this.west, this.east });
    }
    
    static {
        // LAT_NORTH_EDGE = Math.nextUp(90.0);
    	// There is no model for the nextUp method, so we use the concrete value
    	LAT_NORTH_EDGE = 90.00000000000001;
        FULL = new GeoRectangle(-90.0, GeoRectangle.LAT_NORTH_EDGE, -180.0, 180.0);
    }
}
