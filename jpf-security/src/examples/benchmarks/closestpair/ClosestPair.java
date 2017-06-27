package benchmarks.closestpair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import gov.nasa.jpf.symbc.Debug;

public class ClosestPair
{
  public static class Point
  {
    public final int x;
    public final int y;
 
    public Point(int x, int y)
    {
      this.x = x;
      this.y = y;
    }
 
    public String toString()
    {  return "(" + x + ", " + y + ")";  }
  }
 
  public static class Pair
  {
    public Point point1 = null;
    public Point point2 = null;
    public int distance = 0;
 
    public Pair()
    {  }
 
    public Pair(Point point1, Point point2)
    {
      this.point1 = point1;
      this.point2 = point2;
      calcDistance();
    }
 
    public void update(Point point1, Point point2, int distance)
    {
      this.point1 = point1;
      this.point2 = point2;
      this.distance = distance;
    }
 
    public void calcDistance()
    {  this.distance = distance(point1, point2);  }
 
    public String toString()
    {  return point1 + "-" + point2 + " : " + distance;  }
  }
 
  public static int distance(Point p1, Point p2)
  {
    int xdist = p2.x - p1.x;
    int ydist = p2.y - p1.y;
    return xdist-ydist;//(int)Math.sqrt(Math.pow(xdist,2) + Math.pow(ydist,2));
  }
 
  public static Pair bruteForce(List<? extends Point> points)
  {
    int numPoints = points.size();
    if (numPoints < 2)
      return null;
    Pair pair = new Pair(points.get(0), points.get(1));
    if (numPoints > 2)
    {
      for (int i = 0; i < numPoints - 1; i++)
      {
        Point point1 = points.get(i);
        for (int j = i + 1; j < numPoints; j++)
        {
          Point point2 = points.get(j);
          int distance = distance(point1, point2);
          if (distance < pair.distance)
            pair.update(point1, point2, distance);
        }
      }
    }
    return pair;
  }
 
  public static void sortByX(List<? extends Point> points)
  {
    Collections.sort(points, new Comparator<Point>() {
        public int compare(Point point1, Point point2)
        {
          if (point1.x < point2.x)
            return -1;
          if (point1.x > point2.x)
            return 1;
          return 0;
        }
      }
    );
  }
 
  public static void sortByY(List<? extends Point> points)
  {
    Collections.sort(points, new Comparator<Point>() {
        public int compare(Point point1, Point point2)
        {
          if (point1.y < point2.y)
            return -1;
          if (point1.y > point2.y)
            return 1;
          return 0;
        }
      }
    );
  }
 
  public static Pair divideAndConquer(List<Point> points)
  {
    List<Point> pointsSortedByX = new ArrayList<Point>(points);
    sortByX(pointsSortedByX);
    List<Point> pointsSortedByY = new ArrayList<Point>(points);
    sortByY(pointsSortedByY);
    return divideAndConquer2(pointsSortedByX, pointsSortedByY);
  }
 
  private static Pair divideAndConquer2(List<? extends Point> pointsSortedByX, List<? extends Point> pointsSortedByY)
  {
    int numPoints = pointsSortedByX.size();
    if (numPoints <= 3)
      return bruteForce(pointsSortedByX);
 
    int dividingIndex = numPoints >>> 1;
    List<? extends Point> leftOfCenter = pointsSortedByX.subList(0, dividingIndex);
    List<? extends Point> rightOfCenter = pointsSortedByX.subList(dividingIndex, numPoints);
 
    List<Point> tempList = new ArrayList<Point>(leftOfCenter);
    sortByY(tempList);
    Pair closestPair = divideAndConquer2(leftOfCenter, tempList);
 
    tempList.clear();
    tempList.addAll(rightOfCenter);
    sortByY(tempList);
    Pair closestPairRight = divideAndConquer2(rightOfCenter, tempList);
 
    if (closestPairRight.distance < closestPair.distance)
      closestPair = closestPairRight;
 
    tempList.clear();
    int shortestDistance =closestPair.distance;
    int centerX = rightOfCenter.get(0).x;
    for (Point point : pointsSortedByY)
      if (Math.abs(centerX - point.x) < shortestDistance)
        tempList.add(point);
 
    for (int i = 0; i < tempList.size() - 1; i++)
    {
      Point point1 = tempList.get(i);
      for (int j = i + 1; j < tempList.size(); j++)
      {
        Point point2 = tempList.get(j);
        if ((point2.y - point1.y) >= shortestDistance)
          break;
        int distance = distance(point1, point2);
        if (distance < closestPair.distance)
        {
          closestPair.update(point1, point2, distance);
          shortestDistance = distance;
        }
      }
    }
    return closestPair;
  }
 
  public static void main( String [ ] args )
  {
    
      int N = Integer.parseInt(args[0]);
      
      int[] a = new int[N];
      for(int i = 0; i < N; i++) {
        a[i] = Debug.makeSymbolicInteger("sym"+i);
      }
    
      List<Point> points = new ArrayList<Point>();
      for (int i = 0; i < N; i++)
        points.add(new Point(Debug.makeSymbolicInteger("x"+i), Debug.makeSymbolicInteger("y"+i)));
      Pair dqClosestPair = divideAndConquer(points);
      
      //Pair bruteForceClosestPair = bruteForce(points);

  }
 
}