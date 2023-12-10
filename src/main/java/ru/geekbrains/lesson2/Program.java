package ru.geekbrains.lesson2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

public class Program extends Application {

    @Getter
    private static Stage root;


    public static void main(String[] args) {
        Application.launch(args);
    }

    public static Scene loadScene(String viewName) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Program.class.getResource("/views/" + viewName + ".fxml"));
        return new Scene(loader.load());
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = loadScene("Setting");
        stage.setTitle("Настройки");
        stage.setWidth(330);
        stage.setHeight(380);

        stage.setScene(scene);
        root = stage;
        stage.show();
    }
}
