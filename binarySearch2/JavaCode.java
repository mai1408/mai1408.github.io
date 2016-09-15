package binarySearch;

import java.util.LinkedList;

public class JavaCode {

	private int low;
	private int high;
	private int mid;
	private int i = 0;

	public JavaCode(int array[]) {
		low = 0;
		high = array.length - 1;
		//midValues = new int[array.length];
	}

	public int findElement(int key, int array[]) {
		while (low <= high) {			
			mid = (low + high) / 2;						
			if (array[mid] > key) {
				
				high = mid - 1;
				
			}
			else if (array[mid] < key) {
				
				low = mid + 1;
				
			}				
			else {
				
				return mid;				
			}
		}
		return -1;
	}
}
