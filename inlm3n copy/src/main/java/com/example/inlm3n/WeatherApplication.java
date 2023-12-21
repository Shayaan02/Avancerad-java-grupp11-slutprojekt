package com.example.inlm3n;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WeatherApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WeatherApplication.class.getResource("weather-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Weather Application!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();

        WeatherApp weatherApp = new WeatherApp("412d7317b84a83bcfe80cc39870a0515");
        System.out.println(weatherApp.getWeatherData("Malmö"));
    }
}