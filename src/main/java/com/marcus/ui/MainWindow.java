package com.marcus.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.marcus.application.Main.ActionCallback;
import com.marcus.observable.ResultObservable;

public class MainWindow extends JFrame {
	
	private JTextArea resultField = new JTextArea();
	private JTextField urlField = new JTextField();
	private JButton connectBtn = new JButton();
	private final ResultObservable resultObservable;
	private final ActionCallback callBack;

    class OkButtonClicked implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			MainWindow.this.callBack.newConnection(MainWindow.this.urlField.getText());
		}
		
	}
	public MainWindow(ResultObservable resultObservable, ActionCallback callBack) {
		this.resultObservable = resultObservable;
		this.resultObservable.getObservable().subscribe(it -> {
			updateResults(it);
		});
		this.callBack = callBack;
		init();
	}

	private void init() {
		setTitle("JSON Validator");
		setSize(300, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		JPanel pane = (JPanel)getContentPane() ;
		BoxLayout layout = new BoxLayout(pane, 3);
		pane.setLayout(layout);
		
		createResultPanel(pane);
		createInputArea();
	}
	
	private void createInputArea() {
		JPanel pane = (JPanel) getContentPane();
		urlField.setSize(10, 30);
		connectBtn.setText("Connect");
		pane.add(urlField);
		pane.add(connectBtn);
		connectBtn.addActionListener(new OkButtonClicked());
	}

	private void createResultPanel(JPanel pane) {
		resultField.setSize(30, 20);
		pane.setSize(30, 20);
		pane.add(resultField);
		resultField.setEnabled(false);
		resultField.setText("Give my friend below a url!");
	}

	private synchronized void updateResults(Map<String, Integer> results) {
		StringBuilder builder = new StringBuilder();
		for (String key : results.keySet()) {
			builder.append(key + ": " + results.get(key) + "\n");
		}
		resultField.setText(builder.toString());
	}
}
