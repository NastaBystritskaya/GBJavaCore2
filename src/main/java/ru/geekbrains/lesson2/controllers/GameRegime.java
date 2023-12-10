package ru.geekbrains.lesson2.controllers;

import lombok.Getter;

/**
 * Перечисления режима игры
 */
public enum GameRegime {

    /**
     * Человек против Человека
     */
    PVP("Человек против Человека"),

    /**
     * Человек против AI
     */
    PVE("Человек против AI");

    /**
     * Значение
     */
    @Getter
    private final String value;

    /**
     * Конструктор перечисления
     * @param s Значения перечисления
     */
    GameRegime(String s) {
        this.value = s;
    }
}
