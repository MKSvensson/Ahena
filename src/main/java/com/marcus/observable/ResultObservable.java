package com.marcus.observable;

import java.util.HashMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.marcus.utils.MapJoiner;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ResultObservable {

	private ConcurrentHashMap<String, Integer> results = new ConcurrentHashMap<>();

	private final PublishSubject<Map<String, Integer>> publisher;

	public ResultObservable() {
		this.publisher = PublishSubject.create();
	}

	public Observable<Map<String, Integer>> getObservable() {
		return publisher;
	}

	public void add(HashMap<String, Integer> newResults) {
		results = new ConcurrentHashMap<String, Integer>(MapJoiner.consolidateResults(newResults, new HashMap<String, Integer>(results)));
		publisher.onNext(new HashMap<String, Integer>(results));
	}
	
	public void clear() {
		results.clear();
	}
}
