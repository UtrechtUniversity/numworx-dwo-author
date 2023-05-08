package fi.euclides.util;

import java.util.Arrays;
import java.util.Comparator;

public class DComparator implements Comparator<Observable> {

	static private final DComparator instance = new DComparator();
	
	public int compare(Observable arg0, Observable arg1) {
		int i0 = arg0.getIndex();
		int i1 = arg1.getIndex();
		return i0<i1?-1: i0>i1?1:0;
	}
	
	public static void sort(Observable[] d)
	{
		Arrays.sort(d, instance);
	}
	
}
