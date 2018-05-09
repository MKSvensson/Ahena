package com.marcus.validator;

import java.util.Map;

import org.json.JSONObject;

public interface JsonValidator {

	Map<String, Integer> validate(JSONObject jsonObj);
}
