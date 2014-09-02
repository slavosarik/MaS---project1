package logika;

import java.util.Comparator;

public class LinkaComparator implements Comparator<Linka> {
	@Override
	public int compare(Linka l1, Linka l2) {

		if (l1.casKoncaHovoru < l2.casKoncaHovoru) {
			return -1;
		}
		if (l1.casKoncaHovoru > l2.casKoncaHovoru) {
			return 1;
		}
		return 0;
	}
}
