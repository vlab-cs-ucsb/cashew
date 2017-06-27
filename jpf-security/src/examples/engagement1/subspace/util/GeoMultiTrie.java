package engagement1.subspace.util;

import java.io.*;
import java.util.*;

public class GeoMultiTrie<Value extends Serializable> implements Serializable
{
    private static final long serialVersionUID = 0L;
    private GeoTrie<HashSet<Value>> mTrie;
    
    public GeoMultiTrie() {
        this.mTrie = new GeoTrie<HashSet<Value>>();
    }
    
    public Set<Value> get(final GeoPoint point, final boolean create) {
        try {
            return (Set<Value>)this.mTrie.getOrThrow(point);
        }
        catch (NoSuchElementException e) {
            if (create) {
                final HashSet<Value> result = (HashSet<Value>)new HashSet();
                this.mTrie.put(point, result);
                return (Set<Value>)result;
            }
            return null;
        }
    }
    
    public boolean add(final GeoPoint point, final Value value) {
        return this.get(point, true).add(value);
    }
    
    public boolean remove(final GeoPoint point, final Value value) {
        final Set<Value> set = this.get(point, false);
        if (set == null) {
            return false;
        }
        final boolean ret = set.remove((Object)value);
        if (set.isEmpty()) {
            this.mTrie.remove(point);
        }
        return ret;
    }
    
    public Iterator<GeoSearchResult<Value>> search(final GeoPoint point) {
        return (Iterator<GeoSearchResult<Value>>)new SearchIterator(point);
    }
    
    private class SearchIterator implements Iterator<GeoSearchResult<Value>>
    {
        private Iterator<GeoSearchResult<HashSet<Value>>> mSearchIt;
        private GeoSearchResult<HashSet<Value>> mLastResult;
        private Iterator<Value> mValueIt;
        private GeoSearchResult<Value> mNextResult;
        
        SearchIterator(final GeoPoint point) {
            this.mSearchIt = GeoMultiTrie.this.mTrie.search(point);
            this.mLastResult = null;
            this.mValueIt = null;
            this.mNextResult = null;
        }
        
        private void getNextResult() {
            final GeoSearchResult<Value> result = new GeoSearchResult<Value>();
            while (this.mNextResult == null) {
                if (this.mValueIt == null) {
                    try {
                        this.mLastResult = (GeoSearchResult<HashSet<Value>>)this.mSearchIt.next();
                    }
                    catch (NoSuchElementException e) {
                        return;
                    }
                    this.mValueIt = (Iterator<Value>)this.mLastResult.value.iterator();
                }
                try {
                    result.value = (Value)this.mValueIt.next();
                }
                catch (NoSuchElementException e) {
                    this.mLastResult = null;
                    this.mValueIt = null;
                    continue;
                }
                result.distance = this.mLastResult.distance;
                result.point = this.mLastResult.point;
                this.mNextResult = result;
            }
        }
        
        public boolean hasNext() {
            this.getNextResult();
            return this.mNextResult != null;
        }
        
        public GeoSearchResult<Value> next() {
            this.getNextResult();
            if (this.mNextResult == null) {
                throw new NoSuchElementException();
            }
            final GeoSearchResult<Value> result = this.mNextResult;
            this.mNextResult = null;
            return result;
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
