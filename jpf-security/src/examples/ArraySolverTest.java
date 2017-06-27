/*
 * Copyright (C) 2014, United States Government, as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All rights reserved.
 *
 * Symbolic Pathfinder (jpf-symbc) is licensed under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0. 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

import java.util.ArrayList;

public class ArraySolverTest {
	public static boolean testBasic(int x) {
		int[] arr = new int[100];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = i;
		}

		if (arr[x] == 3) {
			for (int j = 0; j < 10000; j++) {
				if (j == 9999) return true;
			}
			return false;
		} else {
			return false;
		}
	}
	
	public static void testArrayList(int x) {		
		int arrSize = 50;
		ArrayList<Integer> arrList = new ArrayList<>();
		for (int i = 0; i < arrSize; i++) {
			arrList.add(i);
		}
		
		/*System.out.println("Contents of arr: " );
		for (int i : arrList) {
			System.out.print(i + " ");
		}
		System.out.println("\n");*/

    double d = 1;
    for (int j = 0; j< arrList.get(x); j++){
      d*=5;
    }
	}
	
	public static void main(String[] args) {
		testBasic(0);
		testArrayList(0);
	}
}