package engagement1.subspace.util;

import java.util.*;

public class GeoSearchResult<Value> implements Comparable<GeoSearchResult<Value>>
{
    public double distance;
    public GeoPoint point;
    public Value value;
    
    public int compareTo(final GeoSearchResult<Value> other) {
        return Double.compare(this.distance, other.distance);
    }
    
    public String toString() {
        return String.format("%s[distance = %s, point = %s, value = %s]", new Object[] { this.getClass().getName(), this.distance, this.point, this.value });
    }
    
    public static <Value> List<GeoSearchResult<Value>> upTo(final Iterator<GeoSearchResult<Value>> iterator, final double maxDistance, final int maxResults) {
        final List<GeoSearchResult<Value>> results = (List<GeoSearchResult<Value>>)new ArrayList();
        try {
            while (maxResults < 0 || results.size() < maxResults) {
                final GeoSearchResult<Value> result = (GeoSearchResult<Value>)iterator.next();
                if (result.distance > maxDistance) {
                    break;
                }
                results.add(result);
            }
        }
        catch (NoSuchElementException ex) {}
        return results;
    }
    
    public static <Value> List<GeoSearchResult<Value>> upTo(final Iterator<GeoSearchResult<Value>> iterator, final int maxResults) {
        return upTo(iterator, Double.POSITIVE_INFINITY, maxResults);
    }
}
