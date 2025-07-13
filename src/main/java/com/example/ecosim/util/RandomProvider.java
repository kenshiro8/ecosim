package com.example.ecosim.util;

import java.util.Random;

public final class RandomProvider {
    // テスト時にシードを入れ替えられるよう、直接参照しない
    private static final Random INSTANCE = new Random();

    private RandomProvider() {}

    public static Random get() {
        return INSTANCE;
    }
}