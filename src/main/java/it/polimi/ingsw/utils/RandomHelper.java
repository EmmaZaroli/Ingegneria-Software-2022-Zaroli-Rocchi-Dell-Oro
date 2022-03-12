package it.polimi.ingsw.utils;

import java.util.Random;

public final class RandomHelper {
    private static RandomHelper instance;
    private final Random random;

    private RandomHelper() {
        this.random = new Random();
    }

    public static RandomHelper getInstance() {
        if (instance == null) {
            instance = new RandomHelper();
        }
        return instance;
    }

    public int getInt(int upperBound) {
        return this.random.nextInt(upperBound);
    }
}
