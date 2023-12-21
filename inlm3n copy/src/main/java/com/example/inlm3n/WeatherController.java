package com.example.inlm3n;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class WeatherController {
	@FXML
	private Label adress;
	@FXML
	private TextField search;
	@FXML
	private Label temperature;
	@FXML
	private Label temperatureFeelsLike;
	@FXML
	private Label weatherDesc;
	@FXML
	private ImageView weatherimg;
	private WeatherApp weatherApp;

	@FXML
	void search(ActionEvent event) {
		String apiKey = "412d7317b84a83bcfe80cc39870a0515";
		String city = search.getText();

		if (apiKey.isEmpty()) {
			System.out.println("412d7317b84a83bcfe80cc39870a0515");
		} else {
			if (weatherApp == null) {
				weatherApp = new WeatherApp(apiKey);
			} else {
				weatherApp.updateApiKey(apiKey);
			}

			weatherApp.search(city, adress, temperature, temperatureFeelsLike, weatherDesc, weatherimg);
		}
	}
}
