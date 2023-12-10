package ru.geekbrains.lesson2.services;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import lombok.RequiredArgsConstructor;

import java.util.Random;

/**
 * Логика игры
 */
@RequiredArgsConstructor
public class GameLogic {

    /**
     * Игровое поле
     */
    GridPane grid;

    private final char DOT_HUMAN = 'X'; // Фишка игрока - человека
    private final char DOT_AI = '0'; // Фишка игрока - компьютер
    private final char DOT_EMPTY = '*'; // Признак пустого поля
    private final Random random = new Random();
    private char[][] field; // Двумерный массив хранит состояние игрового поля
    private final int fieldSizeX; // Размерность игрового поля
    private final int fieldSizeY; // Размерность игрового поля
    private final int WIN_COUNT; // Кол-во фишек для победы

    /**
     * Начало игры
     * @param pane Игровое поле
     */
    public void start(GridPane pane) {
        this.grid = pane;
            initialize();
            printField();
    }

    /**
     * Инициализация начального состояния игры
     */
    private void initialize(){
        field = new char[fieldSizeY][fieldSizeX];
        for (int y = 0; y < fieldSizeY; y++){
            for (int x = 0; x < fieldSizeX; x++){
                field[y][x] = DOT_EMPTY;
                Button l = new Button(String.valueOf(DOT_EMPTY));
                GridPane.setFillHeight(l, true);
                GridPane.setFillWidth(l, true);
                l.setOnMouseClicked(event -> this.fireCellClick(l));
                this.grid.add(l, y, x);
            }
        }
    }

    /**
     * Событие выбора пользователя ячейки
     * @param cell Ячейка
     */
    private void fireCellClick(Button cell) {
        if (!cell.getText().equalsIgnoreCase(String.valueOf(DOT_EMPTY))) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Неверный ход");
            alert.setContentText("Данная ячейка занята");
            alert.show();
            return;
        }
        cell.setText(String.valueOf(DOT_HUMAN));
        printField();
        if (gameCheck(DOT_HUMAN, "Вы победили!"))
            return;
        aiTurn();
        printField();
        if (gameCheck(DOT_AI, "Победил компьютер!"))
            return;
    }

    /**
     * Отрисовать текущее состояние игрового поля
     */
    private  void printField(){
        System.out.print("+");
        for (int i = 0; i < fieldSizeX*2 + 1; i++){
            System.out.print((i % 2 == 0) ? "-" : i / 2 + 1);
        }
        System.out.println();

        for (int i = 0; i < fieldSizeY; i++){
            System.out.print(i + 1 + "|");
            for (int j = 0; j < fieldSizeX; j++){
                System.out.print(field[i][j] + "|");
            }
            System.out.println();
        }

        for (int i = 0; i < fieldSizeX*2 + 2; i++){
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Обработка хода компьютера
     */
     void aiTurn() {
        if(blockUser())
            return;

        int x, y;
        do{
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        }
        while (!isCellEmpty(x, y));
        field[x][y] = DOT_AI;
        this.grid.add(new Label(String.valueOf(DOT_AI)), x, y);
    }

    /**
     * Блокировка пользователя
     * @return Результат блокировки
     */
    private boolean blockUser() {
        boolean painted = false;
        for (int row = 0; row < fieldSizeY; row++) {
            for (int cell = 0; cell < fieldSizeX; cell++) {
                if(field[row][cell] == DOT_HUMAN) {
                    if (checkSequenceAndBlock(cell, row)) {
                        painted = true;
                    }
                    break;
                }
                if(painted)
                    break;

            }
        }
        return painted;
    }

    /**
     * Проверка последовательности и блокировка
     * @param x X Координата
     * @param y Y Координата
     * @return Результат проверки
     */
    private boolean checkSequenceAndBlock(int x, int y) {
        // проверка вертикали
        if(checkVerticalSeqAndBlock(x, y))
            return true;

        // проверка по вертикали
        return checkHorizontalSeqAndBlock(x, y);
    }

    /**
     * Проверка по горизонтали и блокирование ходов
     * @param x X координата
     * @param y Y координата
     * @return Результат проверки
     */
    private boolean checkHorizontalSeqAndBlock(int x, int y) {
        int count = 1;
        for (int i = y + 1; i < this.fieldSizeY; i++) {
            if(field[i][x] == DOT_HUMAN)
                count++;
            else break;
        }


        if(count > 2 && y + count < fieldSizeY) {
            if(field[y + count][x] != DOT_AI) {
                field[y + count][x] = DOT_AI;
                return  true;
            }
        }

        int countReverse = 1;
        for (int i = y - 1; i >= 0; i--) {
            if(field[i][x] == DOT_HUMAN)
                countReverse++;
            else break;
        }

        count += countReverse;

        if(count > 2 && countReverse - y > fieldSizeY) {
            if(field[countReverse - y][x] != DOT_AI) {
                field[countReverse - y][x] = DOT_AI;
                return true;
            }
        }

        return false;
    }

    /**
     * Проверка по вертикали и блокирование ходов
     * @param x X координата
     * @param y Y координата
     * @return Результат проверки
     */
    private  boolean checkVerticalSeqAndBlock(int x, int y) {
        int count = 1;
        for (int i = x + 1; i < fieldSizeX; i++) {
            if(field[y][i] == DOT_HUMAN)
                count++;
            else break;
        }


        if(count >= 2 && x + count < fieldSizeX) {
            if(field[y][x + count] != DOT_AI) {
                field[y][x + count] = DOT_AI;
                return true;
            }
        }

        int countReverse = 1;
        for (int i = x - 1; i >= 0; i--) {
            if(field[y][i] == DOT_HUMAN)
                countReverse++;
            else break;
        }

        count += countReverse;

        if(count >= 2 && countReverse - x > fieldSizeX) {
            if(field[y][countReverse - x] != DOT_AI) {
                field[y][countReverse - x] = DOT_AI;
                return true;
            }
        }

        return false;
    }

    /**
     * Проверка, ячейка является пустой (DOT_EMPTY)
     * @param x Ячейка X
     * @param y Ячейка Y
     * @return Результат проверки
     */
     boolean isCellEmpty(int x, int y){
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка состояния игры
     * @param dot фишка игрока
     * @param winStr победный слоган
     * @return признак продолжения игры (true - завершение игры)
     */
     boolean gameCheck(char dot, String winStr){
        if (checkWin(dot)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Конец игры");
            alert.setContentText(winStr);
            alert.show();
            return true;
        }
        if (checkDraw()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Конец игры");
            alert.setContentText("Ничья!");
            alert.show();
            return true;
        }
        return false; // Продолжим игру
    }

    /**
     * Проверка победы
     * @param c фишка игрока (X или 0)
     * @return Проверка победы
     */
    boolean checkWin(char c) {

        if(checkHorWin(c))
            return true;

        if (checkVerWin(c))
            return true;

        return checkDiagWin(c);

    }

    /**
     * Проверка победы по диагонали.
     * @param c Проверяемыйй символ
     * @return Результат проверки
     */
    private  boolean checkDiagWin(char c) {
        for (int row = 0; row < fieldSizeY; row++) {
            for (int cell = 0; cell < fieldSizeX; cell++) {
                if(field[row][cell] == c) {
                    if (calculateRightDiag(c, cell, row) == WIN_COUNT)
                        return true;
                    if (calculateLeftDiag(c, cell, row) == WIN_COUNT)
                        return true;
                }
                else
                    break;
            }
        }

        return false;
    }

    /**
     * Подсчёт по диагонали "/"
     * @param c Проверяемый символ
     * @param x Х координата
     * @param y У координата
     * @return Количество символов в последовательности
     */
    private  int calculateRightDiag(char c, int x, int y) {
        int count = 0;
        for (int row = y + 1; row < fieldSizeY; row++) {
            for (int cell = x - 1; cell >= 0; cell--) {
                if(field[row][cell] == c) {
                    count++;
                }
                else {
                    break;
                }
            }
        }

        for (int row = y - 1; row >= 0; row--) {
            for (int cell = x + 1; cell < fieldSizeX; cell++) {
                if(field[row][cell] == c) {
                    count++;
                }
                else {
                    break;
                }
            }
        }

        return count;

    }

    /**
     * подсчет по диагонали "\"
     * @param c Проверяемый символ
     * @param x Х координата
     * @param y У координата
     * @return Количество символов в последовательности
     */
    private  int calculateLeftDiag(char c, int x, int y) {
        int count = 1;
        for (int row = y - 1; row >= 0; row--) {
            for (int cell = x - 1; cell >= 0; cell--) {
                if(field[row][cell] == c) {
                    count++;
                }
                else {
                    break;
                }
            }
        }

        for (int row = y + 1; row < fieldSizeY; row++) {
            for (int cell = x + 1; cell < fieldSizeX; cell++) {
                if(field[row][cell] == c) {
                    count++;
                }
                else {
                    break;
                }
            }
        }

        return count;

    }

    /**
     * Проверка победы по вертикали
     * @param c Проверямый символ
     * @return Результат проверки
     */
    private  boolean checkVerWin(char c) {
        int count = 0;

        for (int cell = 0; cell < fieldSizeX; cell++) {
            for (int row = 0; row < fieldSizeY; row++) {
                if(field[row][cell] == c)
                    count++;
                else count = 0;
            }
        }

        return count >= WIN_COUNT;
    }

    /**
     * Проверка победы по горизонтали
     * @param c Проверяемое значение
     * @return Результат проверки
     */
     boolean checkHorWin(char c) {
        int count = 0;
        // Проверка по четырем горизонталям
        for (int row = 0; row < fieldSizeY; row++) {
            for (int cell = 0; cell < fieldSizeX; cell++) {
                if(field[row][cell] == c)
                    count++;
                else count = 0;
            }
        }

        return count >= WIN_COUNT;
    }

    /**
     * Проверка на ничью
     */
     boolean checkDraw(){
        for (int i = 0; i < fieldSizeY; i++){
            for (int j = 0; j < fieldSizeX; j++){
                if (isCellEmpty(i, j)) return false;
            }
        }
        return true;
    }

}
