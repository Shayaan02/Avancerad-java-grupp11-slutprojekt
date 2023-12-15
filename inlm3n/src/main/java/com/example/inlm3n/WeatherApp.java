package com.example.inlm3n;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class WeatherApp {
	private String apiKey;

	public WeatherApp(String apiKey) {
		this.apiKey = apiKey;
	}

	public void updateApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public void search(String city) {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() {
				String weatherData = getWeatherData(city);
				// updateUI(weatherData);
				return null;
			}
		};
		new Thread(task).start();
	}
	private String getWeatherData(String city){
		try{
			String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
			URL url = new URL(apiUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder response = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null){
				response.append(line);
			}
			reader.close();
			connection.disconnect();
			return response.toString();
		} catch (IOException e){
			e.printStackTrace();
			showErrorAlert("Error");
			return null;
		}

	}
	private void updateUI(String weatherData) {
		// Ändra så t.ex när de soligt så ska den soliga bilden visas o temperaturen ska ändras osv.
		// Implement the logic to update the UI elements (labels, images, etc.)
		// based on the weather data.
		// Example:
		// Platform.runLater(() -> {
		//    temperature.setText("Temperature: " + temperatureValue + "°C");
		//    weatherDesc.setText("Weather: " + weatherDescription);
		//    // Update other UI elements as needed
		// });
	}

	private void showErrorAlert(String errorMessage) {
		Platform.runLater(() ->{
			Alert alert = new Alert(Alert.AlertType.ERROR, errorMessage, ButtonType.OK);
			alert.showAndWait();
		});
	}
}
