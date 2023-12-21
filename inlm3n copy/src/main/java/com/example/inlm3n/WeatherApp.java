package com.example.inlm3n;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class WeatherApp {
	private static final String SUNNY_IMAGE = "/assets/sunny.png";
	private static final String CLOUD_IMAGE = "/assets/cloud.png";
	private static final String SNOW_IMAGE = "/assets/snowflake.png";
	private static final String RAIN_IMAGE = "/assets/heavy-rain.png";
	private static final String HAZE_IMAGE = "/assets/mist.png";
	// Lägg till en mist png också (glöm inte att lägga den på updateWeatherImage)
	private String apiKey;

	public WeatherApp(String apiKey) {
		this.apiKey = apiKey;
	}

	public void updateApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void search(String city, Label adressLabel, Label temperatureLabel, Label temperatureFeelsLikeLabel, Label weatherDescLabel, ImageView weatherimg) {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() {
				String weatherData = getWeatherData(city);
				updateUI(weatherData, adressLabel, temperatureLabel, temperatureFeelsLikeLabel, weatherDescLabel, weatherimg);

				return null;
			}
		};
		new Thread(task).start();
	}

	public String getWeatherData(String city) {
		try {
			String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";
			URL url = new URL(apiUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder response = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();
			connection.disconnect();
			return response.toString();
		} catch (IOException e) {
			e.printStackTrace();
			showErrorAlert("Ange en giltig stad");
			return null;
		}

	}

	private void updateUI(String weatherData, Label adressLabbel, Label temperatureLabel, Label temperatureFeelsLikeLabel, Label weatherDescLabel, ImageView weatherimg) {

		int temperatureIndex = weatherData.indexOf("\"temp\":");
		int temperatureEndIndex = weatherData.indexOf(",", temperatureIndex);
		String temperatureValueString = weatherData.substring(temperatureIndex + 7, temperatureEndIndex).trim();
		double temperatureValue = Double.parseDouble(temperatureValueString);

		int temperatureFeelsLikeIndex = weatherData.indexOf("\"feels_like\":");
		int temperatureFeelsLikeEndIndex = weatherData.indexOf(",", temperatureFeelsLikeIndex);
		String temperatureFeelsLikeValueString = weatherData.substring(temperatureFeelsLikeIndex + 13, temperatureFeelsLikeEndIndex).trim();
		double temperatureFeelsLikeValue = Double.parseDouble(temperatureFeelsLikeValueString);

		int weatherDescIndex = weatherData.indexOf("\"description\":\"");
		int weatherDescEndIndex = weatherData.indexOf("\"", weatherDescIndex + 15);
		String weatherDescription = weatherData.substring(weatherDescIndex + 15, weatherDescEndIndex).trim();

		int countryIndex = weatherData.indexOf("\"country\":");
		int countryEndIndex = weatherData.indexOf(",", countryIndex);
		String countryValueString = weatherData.substring(countryIndex + 11, countryEndIndex).trim();
		System.out.println(countryValueString);

		int cityIndex = weatherData.indexOf("\"name\":");
		int cityEndIndex = weatherData.indexOf(",", cityIndex);
		String cityValueString = weatherData.substring(cityIndex + 8, cityEndIndex).trim();
		System.out.println(cityValueString);

		Platform.runLater(() -> {
			adressLabbel.setText("Location: " + cityValueString.replaceAll("\"", "") + ", " + countryValueString.replaceAll("\"", ""));
			temperatureLabel.setText("Temperature: " + temperatureValue + "°C");
			temperatureFeelsLikeLabel.setText("Feels Like: " + Math.round(temperatureFeelsLikeValue) + "°C");
			weatherDescLabel.setText("Weather: " + weatherDescription);

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
		} else if (weatherCondition.contains("clear")) {
			weatherimg.setImage(new Image(getClass().getResourceAsStream(SUNNY_IMAGE)));
		}
		else if (weatherCondition.contains("haze")) {
			weatherimg.setImage(new Image(getClass().getResourceAsStream(HAZE_IMAGE)));
		}
	}

	private void showErrorAlert(String errorMessage) {
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.ERROR, errorMessage, ButtonType.OK);
			alert.showAndWait();
		});
	}
}
