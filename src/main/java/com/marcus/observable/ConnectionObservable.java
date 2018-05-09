package com.marcus.observable;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ConnectionObservable {

	protected PublishSubject<String> publisher;
	
    public ConnectionObservable() {
        this.publisher = PublishSubject.create();
    }
    public void add(String value) {
        publisher.onNext(value);
    }
    public Observable<String> getObservable() {
        return publisher;
    }
    public void onFinished() {
    	publisher.onComplete();
    }
}
