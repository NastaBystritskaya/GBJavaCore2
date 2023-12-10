package ru.geekbrains.lesson2.controllers;

import lombok.Getter;

public enum GameVariantRegime {
    PVP("Человек против Человека"),
    PVE("Человек против AI");

    @Getter
    String value;

    GameVariantRegime(String s) {
        this.value = s;
    }
}
