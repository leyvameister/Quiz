package me.ciakid;

import me.ciakid.exception.NoArenasAvailableException;
import me.ciakid.game.Quiz;

import java.util.Map;
import java.util.Random;

public class Test {

    public static void main(String[] args) {
        Random random = new Random();
        int attempts = 0;
        int arenasSize = 2;
        while (attempts < arenasSize) {

            System.out.println(random.nextInt(123));

            attempts++;
        }

    }
}
