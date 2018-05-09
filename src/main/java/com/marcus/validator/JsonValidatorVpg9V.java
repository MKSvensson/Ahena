package com.marcus.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import static com.marcus.utils.Constants.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonValidatorVpg9V implements JsonValidator {

	private HashMap<String, Integer> validationResults = new HashMap<>();
	private static Map<String, Object> validFields;
	static {
		validFields = new HashMap<String, Object>();
		validFields.put("web_pages", ARRAY_STRING);
		validFields.put("name", STRING);
		validFields.put("alpha_two_code", STRING);
		validFields.put("state-province", STRING);
		validFields.put("domains", ARRAY_STRING);
		validFields.put("country", STRING);
	}

	public HashMap<String, Integer> validate(JSONObject jsonObj) {
		Set<String> keySet = jsonObj.keySet();
		validationResults.put(PARSED_ITEMS, (Integer) 0);
		validateKeys(keySet);

		for (String key : keySet) {
			if (STRING.equals(validFields.get(key))) {
				validateString(jsonObj, key);
			} else if (ARRAY_STRING.equals(validFields.get(key))) {
				validateStringArray(jsonObj, key);
			} else {
				//No validation rule i.e unknown type, undefined behavior in spec. This does technically mean a parsed item though
				Integer parsedItems = (Integer) validationResults.get(PARSED_ITEMS)+1;
				validationResults.put(PARSED_ITEMS, parsedItems);
			}
		}
		return validationResults;
	}

	private void validateStringArray(JSONObject jsonObj, String key) {
		Integer parsedItems = (Integer) validationResults.get(PARSED_ITEMS)+1;
		validationResults.put(PARSED_ITEMS, parsedItems);
		JSONArray array = null;
		try {
			array = jsonObj.getJSONArray(key);
		} catch (JSONException e) {
			//This means array is null
			Integer previousValue = (validationResults.get(NULL_ARRAY) == null) ? 0
					: validationResults.get(NULL_ARRAY);
			validationResults.put(NULL_ARRAY, ++previousValue);
			return;
		}
		for (int i = 0; i < array.length(); i++) {
			validateString(array, i);
		}
	}

	private void validateString(JSONArray array, int index) {
		Integer parsedItems = (Integer) validationResults.get(PARSED_ITEMS)+1;
		validationResults.put(PARSED_ITEMS, parsedItems);
		Object toCheck = null;
		try {
			toCheck = array.get(index);
			if(toCheck == JSONObject.NULL)
				throw new JSONException("null");

		} catch (JSONException e) {
			Integer previousValue = (validationResults.get(NULL_VALUE) == null) ? 0 : validationResults.get(NULL_VALUE);
			validationResults.put(NULL_VALUE, ++previousValue);
			return;

		}
		try {
			// Should be null safe now, any exception thrown should be any other failure to
			// convert into a valid string
			array.getString(index);
		} catch (Exception e) {
			Integer previousValue = (validationResults.get(INVALID_STRING) == null) ? 0
					: validationResults.get(INVALID_STRING);
			validationResults.put(INVALID_STRING, ++previousValue);
		}

	}

	private void validateString(JSONObject jsonObj, String key) {
		Integer parsedItems = (Integer) validationResults.get(PARSED_ITEMS)+1;
		validationResults.put(PARSED_ITEMS, parsedItems);
		if (jsonObj.isNull(key)) {
			Integer previousValue = (validationResults.get(NULL_VALUE) == null) ? 0 : validationResults.get(NULL_VALUE);
			validationResults.put(NULL_VALUE, ++previousValue);
			return;
		}

		try {
			jsonObj.getString(key);
		} catch (Exception e) {
			Integer previousValue = (validationResults.get(INVALID_STRING) == null) ? 0
					: validationResults.get(INVALID_STRING);
			validationResults.put(INVALID_STRING, ++previousValue);
		}
	}

	//Noted that this is undefined behavior, spec does not say whether the keys themselves should be validated
	private void validateKeys(Set<String> keys) {

		for (String key : keys) {
			if (!validFields.keySet().contains(key)) {
				Integer previousValue = (validationResults.get(UNKNOWN_FIELD) == null) ? 0
						: validationResults.get(UNKNOWN_FIELD);
				validationResults.put(UNKNOWN_FIELD, ++previousValue);
			}
		}

		if (keys.size() != validFields.size()) {
			validationResults.put(FIELD_AMOUNT_MISMATCH, 1);
		}
	}
}
