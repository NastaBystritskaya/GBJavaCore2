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

/**
 * Контроллер сцены "Настройки"
 */
public class SettingController {

    /**
     * Выбранный размер поля
     */
    @Getter
    private static int selectedSize = 3;

    /**
     * Выбранное количество для выигрыша
     */
    @Getter
    private static int winCount = 3;

    /**
     * Выбранный режим игры
     */
    @Getter
    private static GameRegime regime = GameRegime.PVP;

    /**
     * Слайдер размера поля
     */
    @FXML
    private Slider FieldSizeSlider;

    /**
     * Текст выбранного размера поля
     */
    @FXML
    private Text FieldSizeText;

    /**
     * Кнопка "Новая игра"
     */
    @FXML
    private Button NewGameButton;

    /**
     * Слайдер количества для победы
     */
    @FXML
    private Slider WinSizeSlider;

    /**
     * Текст размера выигрыша
     */
    @FXML
    private Text WinSizeText;

    /**
     * Переключатель режима "Человек против AI"
     */
    @FXML
    private RadioButton avpRadio;

    /**
     * Переключатель режима "Человек против Человека"
     */
    @FXML
    private RadioButton pvpRadio;

    /**
     * Инициализация
     */
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

    /**
     * Событие нажатия на кнопка "Новая игра"
     * @param mouseEvent Обработчик событий
     */
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

    /**
     * События изменения размера для победы
     * @param event Обработчик события
     */
    private void fireWinSizeChanged(Observable event) {
        winCount = (int) this.WinSizeSlider.getValue();
        this.WinSizeText.setText("Установленная длинна: " + winCount);
    }

    /**
     * Событие изменения размера поля
     * @param event Обработчик события
     */
    private void fileFieldSizeChanged(Observable event) {
        selectedSize = (int) this.FieldSizeSlider.getValue();
        this.FieldSizeText.setText("Установленный размер поля: " + selectedSize);
    }

    /**
     * Событие переключения режима на "Человек против Человека"
     * @param mouseEvent Обработчик события
     */
    private void firePvpEnabled(MouseEvent mouseEvent) {
        regime = GameRegime.PVP;
    }

    /**
     * Событие переключения режима на "Человек против AI"
     * @param mouseEvent Обработчик события
     */
    private void fireAvpEnabled(MouseEvent mouseEvent) {
        regime = GameRegime.PVE;
    }
}
