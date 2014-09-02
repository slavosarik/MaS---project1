package logika;

import java.util.Comparator;

public class UdalostComparator implements Comparator<Udalost> {
	@Override
	public int compare(Udalost u1, Udalost u2) {

		if (u1.time < u2.time) {
			return -1;
		}
		if (u1.time > u2.time) {
			return 1;
		}
		return 0;
	}
}
