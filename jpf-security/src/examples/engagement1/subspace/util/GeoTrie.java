package engagement1.subspace.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Set;

import engagement1.subspace.lib.ExtraMath;

public class GeoTrie<Value extends Serializable> implements Serializable
{
    private static final long serialVersionUID = 0L;
    public static final double PRECISION = 1.0E-4;
    private AnnotatedNode<Value> mRoot;
    
    public GeoTrie() {
        this.mRoot = new AnnotatedNode<Value>();
        this.mRoot.node = (Node<Value>)new Node<Serializable>();
        this.mRoot.rectangle = GeoRectangle.FULL;
    }
    
    public Value get(final GeoPoint point) {
        return this.mRoot.find(point, null, null);
    }
    
    public Value getOrThrow(final GeoPoint point) throws NoSuchElementException {
        final Value result = this.mRoot.find(point, null, null);
        if (result != null) {
            return result;
        }
        throw new NoSuchElementException(new StringBuilder().append("point not found: ").append(point).toString());
    }
    
    public Value put(final GeoPoint point, final Value value) {
        return this.mRoot.find(point, Action.INSERT, value);
    }
    
    public Value remove(final GeoPoint point) {
        return this.mRoot.find(point, Action.REMOVE, null);
    }
    
    public Iterator<GeoSearchResult<Value>> search(final GeoPoint point) {
        return (Iterator<GeoSearchResult<Value>>)new SearchIterator(point);
    }
    
    public int height() {
        return this.mRoot.height();
    }
    
    private enum Action
    {
        INSERT, 
        REMOVE;
    }
    
    private static class Node<Value extends Serializable> implements Serializable
    {
        private static final long serialVersionUID = 0L;
        Node<Value>[] children;
        List<Entry<Value>> entries;
        
        Node() {
            this.children = null;
            this.entries = null;
        }
        
        Value find(final GeoRectangle rectangle, final GeoPoint point, final Action action, final Value value) {
            assert rectangle.contains(point);
            if (this.children == null && this.entries == null) {
                if (action == Action.INSERT) {
                    if (rectangle.width() <= 1.0E-4 || rectangle.isFullyDegenerate()) {
                        (this.entries = (List<Entry<Value>>)new LinkedList()).add(new Entry(point, value));
                    }
                    else {
                        final GeoRectangle[] quadrants = rectangle.quadrants();
                        final Node<Value>[] tmpChildren = (Node<Value>[])new Node[quadrants.length];
                        this.children = tmpChildren;
                        for (int i = 0; i < quadrants.length; ++i) {
                            this.children[i] = new Node<Value>();
                            if (quadrants[i].contains(point)) {
                                this.children[i].find(quadrants[i], point, action, value);
                            }
                        }
                    }
                }
                return null;
            }
            if (this.children == null) {
                assert this.entries.size() >= 1;
                assert !(!rectangle.isFullyDegenerate());
                final Iterator<Entry<Value>> iterator = (Iterator<Entry<Value>>)this.entries.iterator();
                while (iterator.hasNext()) {
                    final Entry<Value> entry = (Entry<Value>)iterator.next();
                    assert entry.point != null;
                    if (entry.point.equals(point)) {
                        final Value ret = entry.value;
                        if (action == Action.INSERT) {
                            entry.value = value;
                        }
                        else if (action == Action.REMOVE) {
                            if (this.entries.size() > 1) {
                                iterator.remove();
                            }
                            else {
                                this.entries = null;
                            }
                        }
                        return ret;
                    }
                }
                if (action == Action.INSERT) {
                    this.entries.add(new Entry(point, value));
                }
                return null;
            }
            else {
                assert rectangle.width() > 1.0E-4;
                final GeoRectangle[] quadrants = rectangle.quadrants();
                assert this.children.length == quadrants.length;
                Value ret2 = null;
                for (int i = 0; i < quadrants.length; ++i) {
                    if (quadrants[i].contains(point)) {
                        ret2 = this.children[i].find(quadrants[i], point, action, value);
                        break;
                    }
                }
                if (action == Action.REMOVE) {
                    boolean nonEmptyChild = false;
                    for (int j = 0; j < this.children.length; ++j) {
                        if (this.children[j].children != null || this.children[j].entries != null) {
                            nonEmptyChild = true;
                        }
                    }
                    if (!nonEmptyChild) {
                        this.children = null;
                    }
                }
                return ret2;
            }
        }
        
        int height() {
            if (this.children == null) {
                return 0;
            }
            int maxChildHeight = 0;
            for (int i = 0; i < this.children.length; ++i) {
                final int childHeight = this.children[i].height();
                if (childHeight > maxChildHeight) {
                    maxChildHeight = childHeight;
                }
            }
            return 1 + maxChildHeight;
        }
        
        public String toString() {
            String description;
            if (this.children != null) {
                description = "non-leaf";
            }
            else if (this.entries != null) {
                description = String.format("%d entries", new Object[] { this.entries.size() });
            }
            else {
                description = "empty";
            }
            return String.format("%s[%s]", new Object[] { this.getClass().getName(), description });
        }
        
        private static class Entry<Value extends Serializable> implements Serializable
        {
            private static final long serialVersionUID = 0L;
            GeoPoint point;
            Value value;
            
            Entry(final GeoPoint point, final Value value) {
                this.point = point;
                this.value = value;
            }
        }
    }
    
    private static class AnnotatedNode<Value extends Serializable> implements Serializable
    {
        private static final long serialVersionUID = 0L;
        Node<Value> node;
        GeoRectangle rectangle;
        
        AnnotatedNode<Value>[] getChildren() {
            if (this.node.children == null) {
                return null;
            }
            final GeoRectangle[] quadrants = this.rectangle.quadrants();
            final AnnotatedNode<Value>[] children = (AnnotatedNode<Value>[])new AnnotatedNode[quadrants.length];
            for (int i = 0; i < children.length; ++i) {
                children[i] = new AnnotatedNode<Value>();
                children[i].node = this.node.children[i];
                children[i].rectangle = quadrants[i];
            }
            return children;
        }
        
        AnnotatedNode<Value> getLeafNode(final GeoPoint point) {
            if (this.node.children == null) {
                return this;
            }
            for (final AnnotatedNode<Value> child : this.getChildren()) {
                if (child.rectangle.contains(point)) {
                    return child.getLeafNode(point);
                }
            }
            throw new RuntimeException(new StringBuilder().append(this.rectangle).append(" does not contain ").append(point).toString());
        }
        
        Value find(final GeoPoint point, final Action action, final Value value) {
            return this.node.find(this.rectangle, point, action, value);
        }
        
        int height() {
            return this.node.height();
        }
        
        public String toString() {
            return String.format("%s[rectangle = %s, node = %s]", new Object[] { this.getClass().getName(), this.rectangle, this.node });
        }
    }
    
    private class SearchIterator implements Iterator<GeoSearchResult<Value>>
    {
        private GeoPoint mPoint;
        private PriorityQueue<GeoSearchResult<Value>> mNextResults;
        private double mMinDistance;
        
        SearchIterator(final GeoPoint point) {
            this.mPoint = point;
            this.mNextResults = (PriorityQueue<GeoSearchResult<Value>>)new PriorityQueue();
            this.mMinDistance = 0.0;
        }
        
        private void getNextResults() {
            while (this.mNextResults.isEmpty() && this.mMinDistance <= 180.0) {
                this.searchForResults();
            }
        }
        
        private void searchForResults() {
            final List<AnnotatedNode<Value>> searchArea = this.getSearchArea(this.mMinDistance);
            final double maxDistance = this.getMaxCoveredRadius(searchArea);
            assert maxDistance >= this.mMinDistance : new StringBuilder().append("point = ").append(this.mPoint).append(", min = ").append(this.mMinDistance).append(", max = ").append(maxDistance).toString();
            if (maxDistance <= this.mMinDistance) {
                this.mMinDistance = Math.nextUp(this.mMinDistance);
                return;
            }
            final Set<Node<Value>> completedNodes = (Set<Node<Value>>)new HashSet();
            for (final AnnotatedNode<Value> node : searchArea) {
                if (node.node.entries == null) {
                    continue;
                }
                if (!completedNodes.add(node.node)) {
                    continue;
                }
                for (final Node.Entry<Value> entry : node.node.entries) {
                    final double distance = this.mPoint.distance(entry.point);
                    if (distance >= this.mMinDistance) {
                        if (distance > maxDistance) {
                            continue;
                        }
                        final GeoSearchResult<Value> result = new GeoSearchResult<Value>();
                        result.distance = distance;
                        result.point = entry.point;
                        result.value = entry.value;
                        this.mNextResults.add(result);
                    }
                }
            }
            this.mMinDistance = Math.nextUp(maxDistance);
        }
        
        private List<AnnotatedNode<Value>> getSearchArea(final double radius) {
            final List<AnnotatedNode<Value>> result = (List<AnnotatedNode<Value>>)new ArrayList();
            final SearchAreaIterator it = new SearchAreaIterator(radius);
            try {
                while (true) {
                    result.add(it.next());
                }
            }
            catch (NoSuchElementException ex) {
                return result;
            }
        }
        
        private boolean searchAreaIsSubset(final Iterator<AnnotatedNode<Value>> x, final Set<Node<Value>> y) {
            try {
                while (y.contains(((AnnotatedNode)x.next()).node)) {}
                return false;
            }
            catch (NoSuchElementException ex) {
                return true;
            }
        }
        
        private double getMaxCoveredRadius(final List<AnnotatedNode<Value>> searchArea) {
            if (this.mMinDistance != 0.0) {
                final Set<Node<Value>> searchAreaNodes = (Set<Node<Value>>)new HashSet();
                for (final AnnotatedNode<Value> node : searchArea) {
                    searchAreaNodes.add(node.node);
                }
                double minRadius = this.mMinDistance;
                double maxRadius = 180.0;
                while (minRadius < maxRadius) {
                    double midRadius = ExtraMath.average(minRadius, maxRadius);
                    if (midRadius == minRadius) {
                        midRadius = Math.nextUp(midRadius);
                    }
                    if (this.searchAreaIsSubset((Iterator<AnnotatedNode<Value>>)new SearchAreaIterator(midRadius), searchAreaNodes)) {
                        minRadius = midRadius;
                    }
                    else {
                        maxRadius = Math.nextAfter(midRadius, Double.NEGATIVE_INFINITY);
                    }
                }
                return minRadius;
            }
            assert searchArea.size() == 1;
            final AnnotatedNode<Value> node2 = (AnnotatedNode<Value>)searchArea.get(0);
            double radius = Double.POSITIVE_INFINITY;
            for (int i = 0; i < 4; ++i) {
                radius = Math.min(radius, node2.rectangle.maxContainedDistance(this.mPoint, i * 90.0, 0.0));
            }
            return radius;
        }
        
        public boolean hasNext() {
            this.getNextResults();
            return !this.mNextResults.isEmpty();
        }
        
        public GeoSearchResult<Value> next() {
            this.getNextResults();
            return (GeoSearchResult<Value>)this.mNextResults.remove();
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        private class SearchAreaIterator implements Iterator<AnnotatedNode<Value>>
        {
            private double radius;
            private double bearing;
            private double bearingMax;
            
            SearchAreaIterator(final double radius) {
                this.radius = radius;
                this.bearing = 0.0;
                this.bearingMax = -1.0;
            }
            
            public boolean hasNext() {
                return this.bearingMax == -1.0 || this.bearing < this.bearingMax;
            }
            
            public AnnotatedNode<Value> next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final GeoPoint p = SearchIterator.this.mPoint.greatArcEndpoint(this.radius, this.bearing);
                final AnnotatedNode<Value> node = GeoTrie.this.mRoot.getLeafNode(p);
                if (this.bearingMax == -1.0) {
                    this.bearingMax = node.rectangle.getExtremumBearing(SearchIterator.this.mPoint, this.radius, false, this.bearing);
                    if (this.bearingMax == 0.0) {
                        this.bearingMax = 360.0;
                    }
                }
                this.bearing = node.rectangle.getExtremumBearing(SearchIterator.this.mPoint, this.radius, true, this.bearing);
                if (!Double.isInfinite(this.bearing)) {
                    this.bearing = Math.nextUp(this.bearing);
                }
                return node;
            }
            
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
    }
}
