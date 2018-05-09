package com.marcus.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.marcus.observable.ConnectionObservable;

public class MConnection {
	private URL url;
	private HttpURLConnection connection;
	private final ConnectionObservable connectionObservable;

	public MConnection(String address, ConnectionObservable connectionObservable) {
		try {
			url = new URL(address);
		} catch (MalformedURLException e) {
			//Lets just pretend this is the logger
			System.out.println("Not a valid URL");
		}
		this.connectionObservable = connectionObservable;
	}

	public void read() {
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			//Lets just pretend this is the logger
			System.out.println("Error open connection to " + url.toString());
		}

		connection.setRequestProperty("Content-Type", "application/json");
		connection.setConnectTimeout(2000);
		connection.setReadTimeout(2000);

		StringBuilder builder = new StringBuilder();

		try {
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String result;

			while ((result = inputReader.readLine()) != null) {
				builder.append(result);
			}
			inputReader.close();
			connection.disconnect();

		} catch (IOException e) {
			//Lets just pretend this is the logger
			System.out.println("Error reading from " + url.toString());
		}

		connectionObservable.add(builder.toString());
	}
}
