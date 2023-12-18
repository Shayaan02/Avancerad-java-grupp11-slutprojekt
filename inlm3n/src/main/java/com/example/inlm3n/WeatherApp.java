package com.example.inlm3n;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class WeatherController {

	@FXML
	private Label address;

	@FXML
	private TextField search;

	@FXML
	private TextField apiKeyTextField;

	@FXML
	private Label temperature;

	@FXML
	private Label weatherDesc;

	@FXML
	private ImageView weatherimg;

	private WeatherApp weatherApp;

	@FXML
	void initialize() {
		// Perform initialization tasks when the FXML is loaded
	}

	@FXML
	void search(ActionEvent event) {
		String apiKey = apiKeyTextField.getText();
		String city = search.getText();

		if (apiKey.isEmpty()) {
			// Handle the case where the API key is not provided
			System.out.println("412d7317b84a83bcfe80cc39870a0515");
		} else {
			// Initialize or update the WeatherApp instance with the new API key
			if (weatherApp == null) {
				weatherApp = new WeatherApp(apiKey);
			} else {
				weatherApp.updateApiKey(apiKey);
			}


			// Use the weatherApp instance to perform weather-related functionality
			weatherApp.search(city, temperature, weatherDesc, weatherimg);
		}
	}
}
