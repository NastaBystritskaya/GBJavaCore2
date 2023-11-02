package ru.geekbrains.lesson2;

import java.util.Random;
import java.util.Scanner;

public class Program {

    private static final char DOT_HUMAN = 'X'; // Фишка игрока - человека
    private static final char DOT_AI = '0'; // Фишка игрока - компьютер
    private static final char DOT_EMPTY = '*'; // Признак пустого поля
    private static final Scanner scanner = new Scanner(System.in);
    private static  final Random random = new Random();
    private static char[][] field; // Двумерный массив хранит состояние игрового поля
    private static int fieldSizeX; // Размерность игрового поля
    private static int fieldSizeY; // Размерность игрового поля
    private static final int WIN_COUNT = 4; // Кол-во фишек для победы

    public static void main(String[] args) {
        while (true){
            initialize();
            printField();
            while (true){
                humanTurn();
                printField();
                if (gameCheck(DOT_HUMAN, "Вы победили!"))
                    break;
                aiTurn();
                printField();
                if (gameCheck(DOT_AI, "Победил компьютер!"))
                    break;
            }
            System.out.print("Желаете сыграть еще раз? (Y - да): ");
            if (!scanner.next().equalsIgnoreCase("Y"))
                break;
        }
    }

    /**
     * Инициализация начального состояния игры
     */
    private static void initialize(){
        fieldSizeX = 5;
        fieldSizeY = 5;
        field = new char[fieldSizeY][fieldSizeX];
        for (int y = 0; y < fieldSizeY; y++){
            for (int x = 0; x < fieldSizeX; x++){
                field[y][x] = DOT_EMPTY;
            }
        }
    }

    /**
     * Отрисовать текущее состояние игрового поля
     */
    private static void printField(){
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
     * Обработка хода игрока (человека)
     */
    private static void humanTurn(){
        int x, y;
        do{
            System.out.print("Укажите координаты хода X и Y (от 1 до 3)\nчерез пробел: ");
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
    }

    /**
     * Обработка хода компьютера
     */
    static void aiTurn() {
        if(blockUser())
            return;

        int x, y;
        do{
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        }
        while (!isCellEmpty(x, y));
        field[x][y] = DOT_AI;
    }

    private static boolean blockUser() {
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

    private static boolean checkSequenceAndBlock(int x, int y) {
        // проверка вертикали
        if(checkVerticalSeqAndBlock(x, y))
            return true;

        // проверка по вертикали
        return checkHorizontalSeqAndBlock(x, y);
    }

    /**
     * Проверка по горизонтвли и блокирование ходов
     * @param x X координата
     * @param y Y координата
     * @return Результат проверки
     */
    private static boolean checkHorizontalSeqAndBlock(int x, int y) {
        int count = 1;
        for (int i = y + 1; i < fieldSizeY; i++) {
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
    private static boolean checkVerticalSeqAndBlock(int x, int y) {
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
     * @param x
     * @param y
     * @return
     */
    static boolean isCellEmpty(int x, int y){
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка состояния игры
     * @param dot фишка игрока
     * @param winStr победный слоган
     * @return признак продолжения игры (true - завершение игры)
     */
    static boolean gameCheck(char dot, String winStr){
        if (checkWin(dot)){
            System.out.println(winStr);
            return true;
        }
        if (checkDraw()){
            System.out.println("Ничья!");
            return true;
        }
        return false; // Продолжим игру
    }

    /**
     * Проверка корректности ввода
     * @param x
     * @param y
     * @return
     */
    static boolean isCellValid(int x, int y){
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Проверка победы
     * @param c фишка игрока (X или 0)
     * @return
     */
    static boolean checkWin(char c){

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
    private static boolean checkDiagWin(char c) {
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
     * подсчет по диагонали "/"
     * @param c Проверяемый символ
     * @param x Х координата
     * @param y У координата
     * @return Количество символов в последовательности
     */
    private static int calculateRightDiag(char c, int x, int y) {
        int count = 0;
        boolean stopFlag = false;
        for (int row = y + 1; row < fieldSizeY; row++) {
            for (int cell = x - 1; cell >= 0; cell--) {
                if(field[row][cell] == c) {
                    count++;
                }
                else {
                    stopFlag = false;
                    break;
                }
            }
            if(stopFlag)
                break;
        }

        stopFlag = false;
        for (int row = y - 1; row >= 0; row--) {
            for (int cell = x + 1; cell < fieldSizeX; cell++) {
                if(field[row][cell] == c) {
                    count++;
                }
                else {
                    stopFlag = false;
                    break;
                }
            }
            if(stopFlag)
                break;
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
    private static int calculateLeftDiag(char c, int x, int y) {
        int count = 1;
        boolean stopFlag = false;
        for (int row = y - 1; row >= 0; row--) {
            for (int cell = x - 1; cell >= 0; cell--) {
                if(field[row][cell] == c) {
                    count++;
                }
                else {
                    stopFlag = false;
                    break;
                }
            }
            if(stopFlag)
                break;
        }

        stopFlag = false;
        for (int row = y + 1; row < fieldSizeY; row++) {
            for (int cell = x + 1; cell < fieldSizeX; cell++) {
                if(field[row][cell] == c) {
                    count++;
                }
                else {
                    stopFlag = false;
                    break;
                }
            }
            if(stopFlag)
                break;
        }

        return count;

    }

    /**
     * Проверка победы по вертикали
     * @param c Проверямый символ
     * @return Результат проверки
     */
    private static boolean checkVerWin(char c) {
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
    static boolean checkHorWin(char c) {
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
     * @return
     */
    static boolean checkDraw(){
        for (int i = 0; i < fieldSizeY; i++){
            for (int j = 0; j < fieldSizeX; j++){
                if (isCellEmpty(i, j)) return false;
            }
        }
        return true;
    }

}
