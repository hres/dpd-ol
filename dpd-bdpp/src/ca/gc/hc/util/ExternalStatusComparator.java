package ca.gc.hc.util;

import java.util.Comparator;
import java.util.Locale;

import ca.gc.hc.model.ExternalStatus;

public class ExternalStatusComparator implements Comparator<ExternalStatus> {
	private String lang="";

	public ExternalStatusComparator(String lang) {
		this.lang = lang;
	}

	@Override
	public int compare(ExternalStatus object1, ExternalStatus object2) {
		final int BEFORE = -1;
		final int AFTER = 1;

		String status1 = "";
		String status2 = "";

		if (lang.equals(ApplicationGlobals.LANG_FR)) {
			status1 = object1.getExternalStatusF();
			status2 = object2.getExternalStatusF();
		} else {
			status1 = object1.getExternalStatusE();
			status2 = object2.getExternalStatusE();
		}

		if (status1 == null) {
			return AFTER;
		} else if (status2 == null) {
			return BEFORE;
		} else {
			return status1.compareTo(status2);
		}
	}

}
