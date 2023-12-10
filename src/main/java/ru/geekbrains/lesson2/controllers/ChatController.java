package ru.geekbrains.lesson2.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Контроллер чата
 */
public class ChatController {

    /**
     * Файл с чатом
     */
    Path chat = Paths.get("chat/chat.txt");

    /**
     * Обертка чата
     */
    @FXML
    private VBox dialogPane;

    /**
     * Поле сообщения
     */
    @FXML
    private TextField messageField;

    /**
     * Кнопка отправить
     */
    @FXML
    private Button sendButton;

    /**
     * Инициализация сцены
     */
    @FXML
    public void initialize() {
        this.messageField.setOnKeyPressed(this::enterPressed);
        this.sendButton.setOnMouseClicked(this::fireSendClicked);
        StackPane.setAlignment(this.messageField, Pos.CENTER);
        if(Files.exists(this.chat)) {
            try {
                Files.readAllLines(this.chat).forEach(line -> this.dialogPane.getChildren().add(new Text(line)));
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setContentText("Не удалось вывести чат");
                alert.show();
            }
        }
    }

    /**
     * Событие нажатия кнопки "Отправить"
     * @param mouseEvent Обработчик событий
     */
    private void fireSendClicked(MouseEvent mouseEvent) {
        this.saveMessage();
    }

    /**
     * Нажатие над полем "Сообщение" кнопки "Enter"
     * @param keyEvent Обработчик событий
     */
    private void enterPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER)
            this.saveMessage();
    }

    /**
     * Сохранение сообщения в чат
     */
    public void saveMessage() {
        System.out.println(this.messageField.getText());
        try {
            if(!Files.exists(chat.getParent()))
                Files.createDirectories(this.chat.getParent());

            if(!Files.exists(this.chat))
                Files.createFile(this.chat);

            Files.write(this.chat, this.messageField.getText().getBytes(), StandardOpenOption.APPEND);

            Text message = new Text(this.messageField.getText());

            this.dialogPane.getChildren().add(message);
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка сохранения");
            alert.setContentText("Не удалось сохранить чат");
            alert.show();
        }
    }
}