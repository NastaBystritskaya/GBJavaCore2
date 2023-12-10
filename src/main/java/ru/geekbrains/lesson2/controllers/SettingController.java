package ru.geekbrains.lesson2.controllers;

import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;
import ru.geekbrains.lesson2.Program;

public class SettingController {

    @Getter
    private static int selectedSize = 3;

    @Getter
    private static int winCount = 3;

    @Getter
    private static GameVariantRegime regime = GameVariantRegime.PVP;

    @FXML
    private Slider FieldSizeSlider;

    @FXML
    private Text FieldSizeText;

    @FXML
    private Button NewGameButton;

    @FXML
    private Slider WinSizeSlider;

    @FXML
    private Text WinSizeText;

    @FXML
    private RadioButton avpRadio;

    @FXML
    private RadioButton pvpRadio;


    @FXML
    public void initialize() {
        ToggleGroup toggleGroup = new ToggleGroup();
        this.avpRadio.setToggleGroup(toggleGroup);
        this.pvpRadio.setToggleGroup(toggleGroup);
        this.avpRadio.setOnMouseClicked(this::fireAvpEnabled);
        this.pvpRadio.setOnMouseClicked(this::firePvpEnabled);

        this.FieldSizeSlider.valueProperty().addListener(this::fileFieldSizeChanged);
        this.WinSizeSlider.valueProperty().addListener(this::fireWinSizeChanged);

        this.NewGameButton.setOnMouseClicked(this::fireOnNewClick);
    }

    private void fireOnNewClick(MouseEvent mouseEvent) {
        try {
            Scene game = Program.loadScene("game");
            Stage root = Program.getRoot();
            root.setTitle("Игра");
            root.setWidth(620);
            root.setHeight(450);
            root.setScene(game);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Не удалось загрузить сцену");
            alert.setTitle("Ошибка сцены");
            alert.show();
        }
    }

    private void fireWinSizeChanged(Observable event) {
        winCount = (int) this.WinSizeSlider.getValue();
        this.WinSizeText.setText("Установленная длинна: " + this.winCount);
    }

    private void fileFieldSizeChanged(Observable dragEvent) {
        selectedSize = (int) this.FieldSizeSlider.getValue();
        this.FieldSizeText.setText("Установленный размер поля: " + this.selectedSize);
    }

    private void firePvpEnabled(MouseEvent mouseEvent) {
        System.out.println("pvp work");
        regime = GameVariantRegime.PVP;
    }

    private void fireAvpEnabled(MouseEvent mouseEvent) {
        System.out.println("avp work");
        regime = GameVariantRegime.PVE;
    }


}
