package modelcounting.utils;

import java.util.Comparator;

public class ClausesScorer implements Comparator<String> {

	public int compare(String o1, String o2) {
		int o1Score = (o1.indexOf("&&") > -1) ? o1.split("&&").length : 0;
		int o2Score = (o2.indexOf("&&") > -1) ? o2.split("&&").length : 0;

		int firstCheck = -1 * o2Score + o1Score;
		if (firstCheck == 0) {
			int secondCheck = o1.length() - o2.length();
			if (secondCheck == 0) {
				// Just to make a deterministic order, unless the strings are
				// exacly the same which should never occur for correct symbolic
				// exec
				return o1.compareTo(o2);
			} else {
				return secondCheck;
			}
		} else {
			return firstCheck;
		}
	}
}
