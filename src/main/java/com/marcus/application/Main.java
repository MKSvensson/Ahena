package com.marcus.application;

import com.marcus.observable.ObservableFactory;
import com.marcus.ui.MainWindow;

public class Main {

	private static ObservableFactory observableFactory;
	private static ActionCallback callBack = new ActionCallback();
	public static void main(String[] args) {
		observableFactory = new ObservableFactory();
		MainWindow ui = new MainWindow(observableFactory.getResultObserable(), callBack);
		
	}
	
	public static class ActionCallback {
		public void newConnection(final String url) {
			observableFactory.getResultObserable().clear();
			MConnection connection = new MConnection(url, observableFactory.getConnectionObservable());
			connection.read();
		}
	}
}
