package ru.geekbrains.lesson2.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;
import ru.geekbrains.lesson2.Program;
import ru.geekbrains.lesson2.services.GameLogic;

public class GameController {

    GameLogic logic = new GameLogic(SettingController.getSelectedSize(), SettingController.getSelectedSize(), SettingController.getWinCount());

    @FXML
    private Button chatButton;

    @FXML
    private Button closeGameButton;

    @FXML
    private GridPane gameField;

    @FXML
    private Button newGameButton;

    @FXML
    private Text regimeText;

    @FXML
    public void initialize() {
        this.chatButton.setOnMouseClicked(this::chatClicked);
        this.regimeText.setText("Режим игры: " + SettingController.getRegime().getValue());
        this.closeGameButton.setOnMouseClicked(this::fireCloseClick);
        this.gameField.getChildren().clear();
        double height = this.gameField.getPrefHeight() / SettingController.getSelectedSize();
        double width = this.gameField.getPrefWidth() / SettingController.getSelectedSize();
        for (int i = 0; i < SettingController.getSelectedSize(); i++) {

            ColumnConstraints constraints = new ColumnConstraints(0, width, Double.MAX_VALUE);
            constraints.setFillWidth(true);
            constraints.setHgrow(Priority.ALWAYS);
            this.gameField.getColumnConstraints().add(constraints);

        }

        this.gameField.getRowConstraints().clear();
        for (int i = 0; i < SettingController.getSelectedSize(); i++) {
            RowConstraints constraints = new RowConstraints(height);
            this.gameField.getRowConstraints().add(constraints);
        }

        this.gameField.setAlignment(Pos.CENTER);
        this.gameField.setGridLinesVisible(true);
        this.logic.start(this.gameField);
    }

    private void chatClicked(MouseEvent mouseEvent) {
        Stage dialogPane = new Stage();
        dialogPane.initModality(Modality.APPLICATION_MODAL);
        dialogPane.initOwner(Program.getRoot());
        try {
            Scene scene = Program.loadScene("chat");
            dialogPane.setScene(scene);
            dialogPane.setTitle("Чат");
            dialogPane.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Не удалось открыть чат");
            alert.show();
        }
    }

    private void fireCloseClick(MouseEvent mouseEvent) {
        System.exit(0);
    }


}