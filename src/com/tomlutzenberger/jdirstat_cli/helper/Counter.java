package com.tomlutzenberger.jdirstat_cli.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class Counter {

	private Map<String,Long> counters = new HashMap<>();


	public Counter() {}


	public void set(String key, long value) throws Exception {

		if (value < 0) {
			throw new Exception("Counter can\'t go below 0");
		}

		counters.put(key, value);
	}


	public void set(String key, int value) throws Exception {
		set(key, (long)value);
	}


	public long get(String key) throws Exception {

		if (!counters.containsKey(key)) {
			throw new Exception(String.format("Counter `%s` does not exist", key));
		}

		return counters.get(key);
	}


	public Set<String> getAll() {

		return counters.keySet();
	}


	public void reset(String key) {
		counters.put(key, 0L);
	}


	public long up(String key) throws Exception {

		long count = counters.getOrDefault(key, 1L);

		if (count == Long.MAX_VALUE) {
			throw new Exception("Counter limit reached: " + count);
		}

		count++;
		counters.put(key, count);

		return count;
	}


	public long down(String key) throws Exception {

		long count = counters.getOrDefault(key, 1L);

		if (count == 0) {
			throw new Exception("Counter can\'t go below 0");
		}

		count--;
		counters.put(key, count);

		return count;
	}

}
