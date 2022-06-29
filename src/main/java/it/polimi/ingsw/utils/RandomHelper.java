package it.polimi.ingsw.utils;

import java.util.Random;

/**
 * Random Helper
 * Singleton used for generate casual numbers
 */
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

    /**
     * @param upperBound the max value
     * @return an int between 0 and the upperBound
     */
    public int getInt(int upperBound) {
        return this.random.nextInt(upperBound);
    }
}
