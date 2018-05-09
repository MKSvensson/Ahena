package com.marcus.validator;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import static com.marcus.utils.Constants.*;

public class JsonValidatorVpg9VTest {
	private static final String WEB_PAGES = "web_pages";
	private static final String NAME = "name";
	private static final String ALPHA_TWO_CODE = "alpha_two_code";
	private static final String STATE_PROVINCE = "state-province";
	private static final String DOMAINS = "domains";
	private static final String COUNTRY = "country";

	private JsonValidator validator;

	@Before
	public void init() {
		validator = new JsonValidatorVpg9V();
	}

	@Test
	public void shouldPassWithAllFieldsHavingValues() {
		JSONObject jsonObj = new JSONObject();
		JSONArray webpages = new JSONArray();
		webpages.put("www.blubbsaysthefish.com");
		JSONArray domainsArr = new JSONArray();
		domainsArr.put("MINE!");

		jsonObj.put(WEB_PAGES, webpages);
		jsonObj.put(NAME, "Fish pages");
		jsonObj.put(ALPHA_TWO_CODE, "MyCode");
		jsonObj.put(STATE_PROVINCE, "London");
		jsonObj.put(DOMAINS, domainsArr);
		jsonObj.put(COUNTRY, "UK");

		Map<String, Integer> actual = validator.validate(jsonObj);
		assertEquals(1, actual.size());
		assertEquals((Integer) 8, actual.get(PARSED_ITEMS));
	}

	@Test
	public void shouldDetectMissingKey() {
		JSONObject jsonObj = new JSONObject();
		JSONArray webpages = new JSONArray();
		webpages.put("www.blubbsaysthefish.com");
		JSONArray domainsArr = new JSONArray();
		domainsArr.put("MINE!");

		jsonObj.put(WEB_PAGES, webpages);
		jsonObj.put(NAME, "Fish pages");
		jsonObj.put(ALPHA_TWO_CODE, "MyCode");
		jsonObj.put(STATE_PROVINCE, "London");
		jsonObj.put(DOMAINS, domainsArr);

		Map<String, Integer> actual = validator.validate(jsonObj);
		assertEquals(2, actual.size());
		assertEquals((Integer) 1, (Integer) actual.get(FIELD_AMOUNT_MISMATCH));
		assertEquals((Integer) 7, (Integer) actual.get(PARSED_ITEMS));
	}

	@Test
	public void shouldDetectUnsupportedKey() {
		JSONObject jsonObj = new JSONObject();
		JSONArray webpages = new JSONArray();
		webpages.put("www.blubbsaysthefish.com");
		JSONArray domainsArr = new JSONArray();
		domainsArr.put("MINE!");

		jsonObj.put(WEB_PAGES, webpages);
		jsonObj.put(NAME, "Fish pages");
		jsonObj.put(ALPHA_TWO_CODE, "MyCode");
		jsonObj.put(STATE_PROVINCE, "London");
		jsonObj.put(DOMAINS, domainsArr);
		jsonObj.put("FISHES", "Are tasty?");

		Map<String, Integer> actual = validator.validate(jsonObj);
		assertEquals(2, actual.size());
		assertEquals((Integer) 1, (Integer) actual.get(UNKNOWN_FIELD));
		assertEquals((Integer) 8, (Integer) actual.get(PARSED_ITEMS));

	}

	@Test
	public void shouldDetectMultipleUnknownFields() {
		JSONObject jsonObj = new JSONObject();
		JSONArray webpages = new JSONArray();
		webpages.put("www.blubbsaysthefish.com");
		JSONArray domainsArr = new JSONArray();
		domainsArr.put("MINE!");

		jsonObj.put(WEB_PAGES, webpages);
		jsonObj.put(NAME, "Fish pages");
		jsonObj.put(ALPHA_TWO_CODE, "MyCode");
		jsonObj.put(STATE_PROVINCE, "London");
		jsonObj.put(DOMAINS, domainsArr);
		jsonObj.put("FISHES", "Are tasty?");
		jsonObj.put("AnotherField", "This shouldnt be here");

		Map<String, Integer> actual = validator.validate(jsonObj);
		assertEquals(3, actual.size());
		assertEquals((Integer) 2, (Integer) actual.get(UNKNOWN_FIELD));
		assertEquals((Integer) 1, (Integer) actual.get(FIELD_AMOUNT_MISMATCH));
		assertEquals((Integer) 9, (Integer) actual.get(PARSED_ITEMS));
	}

	@Test
	public void shouldReturnMapWithNullFieldsDetected() {
		JSONObject jsonObj = new JSONObject();
		JSONArray webpages = new JSONArray();
		webpages.put("www.blubbsaysthefish.com");
		JSONArray domainsArr = new JSONArray();
		domainsArr.put("MINE!");

		jsonObj.put(WEB_PAGES, webpages);
		jsonObj.put(NAME, JSONObject.NULL);
		jsonObj.put(ALPHA_TWO_CODE, JSONObject.NULL);
		jsonObj.put(STATE_PROVINCE, JSONObject.NULL);
		jsonObj.put(DOMAINS, domainsArr);
		jsonObj.put(COUNTRY, JSONObject.NULL);

		Map<String, Integer> actual = validator.validate(jsonObj);
		assertEquals(2, actual.size());
		assertEquals((Integer) 4, (Integer) actual.get(NULL_VALUE));
		assertEquals((Integer) 8, (Integer) actual.get(PARSED_ITEMS));
	}

	@Test
	public void shouldDetectedNullArray() {
		JSONObject jsonObj = new JSONObject();
		JSONArray domainsArr = new JSONArray();
		domainsArr.put("MINE!");

		jsonObj.put(WEB_PAGES, JSONObject.NULL);
		jsonObj.put(NAME, "Fish pages");
		jsonObj.put(ALPHA_TWO_CODE, "MyCode");
		jsonObj.put(STATE_PROVINCE, "London");
		jsonObj.put(DOMAINS, domainsArr);
		jsonObj.put(COUNTRY, "UK");

		Map<String, Integer> actual = validator.validate(jsonObj);
		assertEquals(2, actual.size());
		assertEquals((Integer) 1, (Integer) actual.get(NULL_ARRAY));
		assertEquals((Integer) 7, (Integer) actual.get(PARSED_ITEMS));

	}

	@Test
	public void shouldDetectedTwoNullArrays() {
		JSONObject jsonObj = new JSONObject();

		jsonObj.put(WEB_PAGES, JSONObject.NULL);
		jsonObj.put(NAME, "Fish pages");
		jsonObj.put(ALPHA_TWO_CODE, "MyCode");
		jsonObj.put(STATE_PROVINCE, "London");
		jsonObj.put(DOMAINS, JSONObject.NULL);
		jsonObj.put(COUNTRY, "UK");

		Map<String, Integer> actual = validator.validate(jsonObj);
		assertEquals(2, actual.size());
		assertEquals((Integer) 2, (Integer) actual.get(NULL_ARRAY));
		assertEquals((Integer) 6, (Integer) actual.get(PARSED_ITEMS));
	}

	@Test
	public void shouldDetectMultipleValidationIssues() {
		JSONObject jsonObj = new JSONObject();

		jsonObj.put(WEB_PAGES, JSONObject.NULL);
		jsonObj.put(NAME, JSONObject.NULL);
		jsonObj.put(ALPHA_TWO_CODE, "MyCode");
		jsonObj.put(STATE_PROVINCE, "London");
		jsonObj.put(DOMAINS, JSONObject.NULL);
		jsonObj.put(COUNTRY, "UK");

		Map<String, Integer> actual = validator.validate(jsonObj);
		assertEquals(3, actual.size());
		assertEquals((Integer) 2, (Integer) actual.get(NULL_ARRAY));
		assertEquals((Integer) 1, (Integer) actual.get(NULL_VALUE));
		assertEquals((Integer) 6, (Integer) actual.get(PARSED_ITEMS));
	}

	@Test
	public void shouldDetectedNullStringInArray() {
		JSONObject jsonObj = new JSONObject();
		JSONArray domainsArr = new JSONArray();
		JSONArray webpages = new JSONArray();
		webpages.put("www.blubbsaysthefish.com");
		domainsArr.put("MINE!");
		domainsArr.put(JSONObject.NULL);

		jsonObj.put(WEB_PAGES, webpages);
		jsonObj.put(NAME, "Fish pages");
		jsonObj.put(ALPHA_TWO_CODE, "MyCode");
		jsonObj.put(STATE_PROVINCE, "London");
		jsonObj.put(DOMAINS, domainsArr);
		jsonObj.put(COUNTRY, "UK");

		Map<String, Integer> actual = validator.validate(jsonObj);
		assertEquals(2, actual.size());
		assertEquals((Integer) 1, (Integer) actual.get(NULL_VALUE));
		assertEquals((Integer) 9, (Integer) actual.get(PARSED_ITEMS));
	}

	@Test
	public void shouldDetectNonStringValue() {
		JSONObject jsonObj = new JSONObject();
		JSONArray webpages = new JSONArray();
		webpages.put("www.blubbsaysthefish.com");
		JSONArray domainsArr = new JSONArray();
		domainsArr.put("MINE!");

		jsonObj.put(WEB_PAGES, webpages);
		jsonObj.put(NAME, 1);
		jsonObj.put(ALPHA_TWO_CODE, "MyCode");
		jsonObj.put(STATE_PROVINCE, "London");
		jsonObj.put(DOMAINS, domainsArr);
		jsonObj.put(COUNTRY, "UK");

		Map<String, Integer> actual = validator.validate(jsonObj);
		assertEquals(2, actual.size());
		assertEquals((Integer) 1, (Integer) actual.get(INVALID_STRING));
		assertEquals((Integer) 8, (Integer) actual.get(PARSED_ITEMS));
	}

	@Test
	public void shouldDetectMultipleNonStringValue() {
		JSONObject jsonObj = new JSONObject();
		JSONArray webpages = new JSONArray();
		webpages.put("www.blubbsaysthefish.com");
		JSONArray domainsArr = new JSONArray();
		domainsArr.put("MINE!");

		jsonObj.put(WEB_PAGES, webpages);
		jsonObj.put(NAME, 1);
		jsonObj.put(ALPHA_TWO_CODE, "MyCode");
		jsonObj.put(STATE_PROVINCE, 2);
		jsonObj.put(DOMAINS, domainsArr);
		jsonObj.put(COUNTRY, "UK");

		Map<String, Integer> actual = validator.validate(jsonObj);
		assertEquals(2, actual.size());
		assertEquals((Integer) 2, (Integer) actual.get(INVALID_STRING));
		assertEquals((Integer) 8, (Integer) actual.get(PARSED_ITEMS));
	}

	@Test
	public void shouldDetectNonStringValueInArray() {
		JSONObject jsonObj = new JSONObject();
		JSONArray webpages = new JSONArray();
		webpages.put(1);
		JSONArray domainsArr = new JSONArray();
		domainsArr.put("MINE!");

		jsonObj.put(WEB_PAGES, webpages);
		jsonObj.put(NAME, "SomeName");
		jsonObj.put(ALPHA_TWO_CODE, "MyCode");
		jsonObj.put(STATE_PROVINCE, "London");
		jsonObj.put(DOMAINS, domainsArr);
		jsonObj.put(COUNTRY, "UK");

		Map<String, Integer> actual = validator.validate(jsonObj);
		assertEquals(2, actual.size());
		assertEquals((Integer) 1, (Integer) actual.get(INVALID_STRING));
		assertEquals((Integer) 8, (Integer) actual.get(PARSED_ITEMS));
	}

	@Test
	public void shouldDetectMultipleNonStringValuesInArray() {
		JSONObject jsonObj = new JSONObject();
		JSONArray webpages = new JSONArray();
		webpages.put(1);
		webpages.put(2);
		JSONArray domainsArr = new JSONArray();
		domainsArr.put("MINE!");

		jsonObj.put(WEB_PAGES, webpages);
		jsonObj.put(NAME, "SomeName");
		jsonObj.put(ALPHA_TWO_CODE, "MyCode");
		jsonObj.put(STATE_PROVINCE, "London");
		jsonObj.put(DOMAINS, domainsArr);
		jsonObj.put(COUNTRY, "UK");

		Map<String, Integer> actual = validator.validate(jsonObj);
		assertEquals(2, actual.size());
		assertEquals((Integer) 2, (Integer) actual.get(INVALID_STRING));
		assertEquals((Integer) 9, (Integer) actual.get(PARSED_ITEMS));

	}

}
