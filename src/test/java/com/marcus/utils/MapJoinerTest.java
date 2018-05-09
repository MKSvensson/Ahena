package com.marcus.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static com.marcus.utils.Constants.*;
import static org.junit.Assert.assertEquals;
public class MapJoinerTest {
	
	@Test 
	public void canJoinNewWithEmptyMap() {
		
		HashMap<String, Integer> newValues = new HashMap<>();
		newValues.put(NULL_VALUE, 1);
		newValues.put(INVALID_STRING, 1);
		Map<String, Integer> actual = MapJoiner.consolidateResults(newValues, new HashMap<String, Integer>());
		assertEquals(2, actual.size());
	}
	
	@Test 
	public void canJoinNewWithMapHavingDifferentValues() {
		
		HashMap<String, Integer> newValues = new HashMap<>();
		newValues.put(NULL_VALUE, 1);
		HashMap<String, Integer> oldValues = new HashMap<>();
		oldValues.put(INVALID_STRING, 1);
		Map<String, Integer> actual = MapJoiner.consolidateResults(newValues, oldValues);
		assertEquals(2, actual.size());
		assertEquals((Integer)1, actual.get(NULL_VALUE));
		assertEquals((Integer)1, actual.get(INVALID_STRING));
	}
	
	@Test 
	public void canJoinNewWithMapHavingSameValues() {
		
		HashMap<String, Integer> newValues = new HashMap<>();
		newValues.put(NULL_VALUE, 1);
		HashMap<String, Integer> oldValues = new HashMap<>();
		oldValues.put(NULL_VALUE, 1);
		Map<String, Integer> actual = MapJoiner.consolidateResults(newValues, oldValues);
		assertEquals(1, actual.size());
		assertEquals((Integer)2, actual.get(NULL_VALUE));
	}

}
