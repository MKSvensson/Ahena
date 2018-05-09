package com.marcus.utils;

import java.util.HashMap;
import java.util.Map;

public class MapJoiner {
	public static synchronized Map<String, Integer> consolidateResults(final HashMap<String, Integer> newResults, final Map<String, Integer> old) {
		final Map<String, Integer> consolidatedResult = new HashMap<>(old);

		synchronized (consolidatedResult) {
			for (String key : newResults.keySet()) {
				Integer previousValue = (consolidatedResult.get(key) == null) ? 0 : consolidatedResult.get(key);
				consolidatedResult.put(key, ++previousValue);
			}
		}
		return consolidatedResult;
	}
}
