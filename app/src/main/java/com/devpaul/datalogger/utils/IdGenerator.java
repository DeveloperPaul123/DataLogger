package com.devpaul.datalogger.utils;

import java.util.Random;

/**
 * Created by Pauly D on 3/14/2015.
 *
 * This class easily creates random, unique long ids for identifying the subjects within the
 * database.
 */
public class IdGenerator {

    /**
     * Generates a random long id.
     * @return the new id.
     */
    public static long generateId() {
        Random random = new Random();
        return random.nextLong();
    }
}
