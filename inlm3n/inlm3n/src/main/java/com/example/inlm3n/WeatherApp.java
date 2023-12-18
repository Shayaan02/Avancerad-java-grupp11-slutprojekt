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
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;


public class WeatherApp {
	private String apiKey;

	private static final String SUNNY_IMAGE = "/assets/sunny.png";
	private static final String CLOUD_IMAGE = "/assets/cloud.png";
	private static final String SNOW_IMAGE = "/assets/snowflake.png";
	private static final String RAIN_IMAGE = "/assets/heavy-rain.png";

	public WeatherApp(String apiKey) {
		this.apiKey = apiKey;
	}

	public void updateApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public void search(String city, Label temperatureLabel, Label weatherDescLabel, ImageView weatherimg) {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() {
				String weatherData = getWeatherData(city);
				updateUI(weatherData, temperatureLabel, weatherDescLabel, weatherimg);

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
	private void updateUI(String weatherData, Label temperatureLabel, Label weatherDescLabel, ImageView weatherimg) {
		int temperatureIndex = weatherData.indexOf("\"temp\":");
		int temperatureEndIndex = weatherData.indexOf(",", temperatureIndex);
		String temperatureValueString = weatherData.substring(temperatureIndex + 7, temperatureEndIndex).trim();
		double temperatureValue = Double.parseDouble(temperatureValueString);

		int weatherDescIndex = weatherData.indexOf("\"description\":\"");
		int weatherDescEndIndex = weatherData.indexOf("\"", weatherDescIndex + 15);
		String weatherDescription = weatherData.substring(weatherDescIndex + 15, weatherDescEndIndex).trim();

		Platform.runLater(() -> {
			temperatureLabel.setText("Temperature: " + temperatureValue + "°C");
			weatherDescLabel.setText("Weather: " + weatherDescription);

			// Additional logic to update other UI elements based on the weather data
			// For example, you might want to change an image based on the weather condition
			String weatherCondition = weatherDescription.toLowerCase();
			updateWeatherImage(weatherCondition, weatherimg);
		});
	}

	private void updateWeatherImage(String weatherCondition, ImageView weatherimg) {
		// Updaterar bilderna beroende på vädret
		if (weatherCondition.contains("sun")) {
			weatherimg.setImage(new Image(getClass().getResourceAsStream(SUNNY_IMAGE)));
		} else if (weatherCondition.contains("cloud")) {
			weatherimg.setImage(new Image(getClass().getResourceAsStream(CLOUD_IMAGE)));
		} else if (weatherCondition.contains("rain")) {
			weatherimg.setImage(new Image(getClass().getResourceAsStream(RAIN_IMAGE)));
		} else if (weatherCondition.contains("snow")) {
			weatherimg.setImage(new Image(getClass().getResourceAsStream(SNOW_IMAGE)));
		}
	}

	private void showErrorAlert(String errorMessage) {
		Platform.runLater(() ->{
			Alert alert = new Alert(Alert.AlertType.ERROR, errorMessage, ButtonType.OK);
			alert.showAndWait();
		});
	}
}
