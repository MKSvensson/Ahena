package com.marcus.observable;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.marcus.validator.JsonValidatorVpg9V;

//This class should only really be used to make sure that the two observables are aware of each other

public class ObservableFactory {

	protected static final ResultObservable resultObservable = new ResultObservable();
	protected static final ConnectionObservable connectionObservable = new ConnectionObservable();
	public ObservableFactory() {
		connectionObservable.getObservable().subscribe(it -> {
			JSONArray array = new JSONArray(it);
			array.forEach(item -> {
				JsonValidatorVpg9V validator = new JsonValidatorVpg9V();
				HashMap<String, Integer> result = validator.validate((JSONObject) item);
				if (!result.isEmpty()) {
					resultObservable.add(result);
				}
			});
		});
	}

	public ConnectionObservable getConnectionObservable() {
		return connectionObservable;
	}
	
	public ResultObservable getResultObserable() {
		return resultObservable;
	}
}
