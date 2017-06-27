package cashew;

import gov.nasa.jpf.symbc.Debug;

public class Sorting {
	
	static int compares = 0;
	
	public static void Insertion(int[] array) {
		int n = array.length;
		for (int k = 1; k < n; k++) {
			int x = array[k];
			int j = k;
			while (j > 0 && array[j - 1] > x) {
				compares++;
				array[j] = array[j - 1];
				j--;
			}
			if (j < k) {
				array[j] = x;
			}
		}
	}
	
	public static void Bubble(int[] array) {
		int n = array.length;
		boolean modified = true;
		while (modified) {
			modified = false;
			for (int i = 1; i < n; i++) {
				if (array[i - 1] > array[i]) {
					int t = array[i - 1];
					array[i - 1] =  array[i];
					array[i] = t;
					modified = true;
				}
			}
		}
	}
	
	public static void Heap(int[] array) {
		int n = array.length;
		for (int k = 1; k < n; k++) {
			// heapify(k)
			int x = array[k];
			int j = k;
			int p = ((j + 1) / 2) - 1;
			while (j > 0) {
				int y = array[p];
				if (x > y) {
					array[j] = y;
					j = p;
					p = ((j + 1) / 2) - 1;
				} else {
					break;
				}
			}
			if (j != k) {
				array[j] =  x;
			}
		}
		for (int k = n - 1; k > 0; k--) {
			int t = array[k];
			array[k] = array[0];
			array[0] = t;
			// bubbledown(0) up to k - 1
			int j = 0;
			while (true) {
				int i = j;
				int z = t;
				// bubble
				int l = j * 2 + 1;
				if (l < k) {
					int x = array[l];
					if (x > z) {
						i = l;
						z = x;
					}
					int r = l + 1;
					if (r < k) {
						x = array[r];
						if (x > z) {
							i = r;
							z = x;
						}
					}
					if (i != j) {
						array[j] =  z;
					}
				}
				if (i == j) {
					break;
				}
				j = i;
			}
			if (j > 0) {
				array[j] = t;
			}
		}
	}
	
	public static void Selection(int[] array) {
		int n = array.length;
		for (int k = 0; k < n - 1; k++) {
			int min = array[k];
			int minpos = k;
			for (int j = k + 1; j < n; j++) {
				if (array[j] < min) {
					min = array[j];
					minpos = j;
				}
			}
			if (minpos != k) {
				array[minpos] = array[k];
				array[k] = min;
			}
		}
	}
	
	public static void Quick(int[] inputArr) {
        
        if (inputArr == null || inputArr.length == 0) {
            return;
        }
        int length = inputArr.length;
        quickSort(inputArr, 0, length - 1);
    }
 
    private static void quickSort(int[] array, int lowerIndex, int higherIndex) {
         
        int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot number, I am taking pivot as middle index number
        int pivot = array[lowerIndex+(higherIndex-lowerIndex)/2];
        // Divide into two arrays
        while (i <= j) {
 
            while (array[i] < pivot) {
                i++;
            }
            while (array[j] > pivot) {
                j--;
            }
            if (i <= j) {
                exchangeNumbers(array, i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j)
            quickSort(array, lowerIndex, j);
        if (i < higherIndex)
            quickSort(array, i, higherIndex);
    }
 
    private static void exchangeNumbers(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    
    public static void Merge(int inputArr[]) {
    	int length = inputArr.length;
        int[] tempArr = new int[length];
        doMergeSort(inputArr, tempArr, 0, length - 1);
    }
 
    private static void doMergeSort(int[] inputArr, int[] tempArr, int lowerIndex, int higherIndex) {
         
        if (lowerIndex < higherIndex) {
            int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
            // Below step sorts the left side of the array
            doMergeSort(inputArr, tempArr, lowerIndex, middle);
            // Below step sorts the right side of the array
            doMergeSort(inputArr, tempArr, middle + 1, higherIndex);
            // Now merge both sides
            mergeParts(inputArr, tempArr, lowerIndex, middle, higherIndex);
        }
    }
 
    private static void mergeParts(int[] array, int[] tempArr, int lowerIndex, int middle, int higherIndex) {
 
        for (int i = lowerIndex; i <= higherIndex; i++) {
            tempArr[i] = array[i];
        }
        int i = lowerIndex;
        int j = middle + 1;
        int k = lowerIndex;
        while (i <= middle && j <= higherIndex) {
            if (tempArr[i] <= tempArr[j]) {
                array[k] = tempArr[i];
                i++;
            } else {
                array[k] = tempArr[j];
                j++;
            }
            k++;
        }
        while (i <= middle) {
            array[k] = tempArr[i];
            k++;
            i++;
        }
 
    }


	public static void runTestDriver(int n) {
		int[] values = new int[n];
		int i = 0;
		while (i < n) {
			values[i] = Debug.makeSymbolicInteger("v" + i);
			i++;
		}
		Merge(values);
		// Quick(values);
		// Heap(values);
		// Selection(values);
		// Insertion(values);
		// Bubble(values);
		
		// assert compares < (n*(n-1))/2; // this only works for insertion of course
	}
	
    public static int binarySearch(int[] inputArr, int key) {
        
        int start = 0;
        int end = inputArr.length - 1;
        while (start <= end) {
            int mid = (start + end) / 2;
            if (key == inputArr[mid]) {
                return mid;
            }
            if (key < inputArr[mid]) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
        return -1;
    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int n = Integer.parseInt(args[0]);
		int[] values = new int[n];
		int i = 0;
		while (i < n) {
			values[i] = Debug.makeSymbolicInteger("v" + i);
			i++;
		}
		
		String sortAlg = args[1].trim();
		if(sortAlg.equals("Merge")){
			System.out.println("Merge sort");
			Merge(values);
		} else if(sortAlg.equals("Quick")){
			System.out.println("Quick sort");
			Quick(values);
		} else if(sortAlg.equals("Heap")){
			System.out.println("Heap sort");
			Heap(values);
		} else if(sortAlg.equals("Selection")){
			System.out.println("Selection sort");
			Selection(values);
		} else if(sortAlg.equals("Insertion")){
			System.out.println("Insertion sort");
			Insertion(values);
		} else if(sortAlg.equals("Bubble")){
			System.out.println("Bubble sort");
			Bubble(values);
		} else if(sortAlg.equals("Binary")){
			System.out.println("Binary search");
			int key = Debug.makeSymbolicInteger("key");
			boolean b = true;
			for (int j = 1; j < values.length; ++j) {
				b = b && values[j-1] <= values[j];
			}
			Debug.assume(b);
			binarySearch(values, key); 
		} else{
			System.out.println("Sorting method not specified");
		}
			
			
			
	}

}
