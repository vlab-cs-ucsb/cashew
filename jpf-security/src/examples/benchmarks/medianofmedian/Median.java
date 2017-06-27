package benchmarks.medianofmedian;

/**
 * @author Kasper Luckow
 *
 */
public class Median {
  public static void main(String[] args) {
    
  }
  /*
  private int find(int[] a, int s, int n, int k ) {
    // start point s, length n, find k-th number
    if ( n == 1 && k == 1 )
        return a[s];
     
    int m = (n+4) /5;
    int[] mid = new int[m];
     
    for (int i=0; i<m; i++) {
        int t = s+i*5;      // 5-elements block pointer
        if ( n-t > 4 ) {
            sort(a, t, 5);      // sort 5 elements
            mid[i] = a[t+2];
        }
        else {      // less than 5 left
            sort(a, t, n-t);    // sort the rest 
            mid[i] = a[t+(n-t-1)/2];
        }
    }
     
    int pivot = find(mid, 0, m, (m+1)/2);
     
    for (int i=0; i<n; i++) {        // find pivot location
        if (a[s+i] == pivot ) {
            swap(a, s+i, s+n-1);
            break;
        }
    }
     
    int pos = 0;
    for (int i=0; i<n-1; i++) {      // using pivot to part
        if ( a[s+i] < pivot ) {
            if ( i != pos )
                swap(a, s+i, s+pos);
            pos++;
        }
    }
    swap(a, s+pos, s+n-1);
     
    if ( pos == k-1 )
        return pivot;
    else if ( pos > k-1 )
        return find(a, s, pos, k);
    else
        return find(a, s+pos+1, n-pos-1, k-pos-1);
}*/
}
