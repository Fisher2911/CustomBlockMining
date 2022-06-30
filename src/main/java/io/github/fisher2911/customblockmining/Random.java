package io.github.fisher2911.customblockmining;

import java.util.SplittableRandom;

public class Random {

    public static final SplittableRandom RANDOM = new SplittableRandom();

    public static int nextInt(int bound) {
        return RANDOM.nextInt(bound);
    }

    public static double nextDouble() {
        return RANDOM.nextDouble();
    }

    public static double nextDouble(double max) {
        return RANDOM.nextDouble(max);
    }

    public static boolean nextBoolean() {
        return RANDOM.nextBoolean();
    }

    public static int nextInt(int min, int max) {
        return RANDOM.nextInt(min, max);
    }

}
