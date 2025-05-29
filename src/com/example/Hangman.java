package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Hangman {

    private static final String DATA_FILE = "data.txt";
    private final List<String> words;

    public static Hangman game;
    private final Word word;

    private static final String YES_STRING = "yes";
    private static final char YES_CHAR = 'y';
    private static final String NO_STRING = "no";
    private static final char NO_CHAR = 'n';


    private static final int MAX_ERROR = 6;
    private static final String TOP = "_____\n|/   |";
    private static final String BOTTOM = "|________";
    private static final String ERROR_0 = "|";
    private static final String ERROR_1 = "|    o";
    private static final String ERROR_2 = "|    |";
    private static final String ERROR_3 = "|   /|";
    private static final String ERROR_4 = "|   /|\\";
    private static final String ERROR_5 = "|   /";
    private static final String ERROR_6 = "|   / \\";

    private int error;

    public Hangman() {
        words = loadWords();
//        word = new Word(words.get(new Random().nextInt(words.size())));
        word = new Word("галлюцинация");
    }

    // Прорисовка виселицы
    // _____
    // |/   |
    // |    o
    // |   /|\
    // |   / \
    // |________
    private void printHangman(int level) {
        System.out.println(TOP);
        System.out.println(level > 0 ? ERROR_1 : ERROR_0);
        System.out.println(level > 1 ? level > 2 ? level > 3 ? ERROR_4 : ERROR_3 : ERROR_2 : ERROR_0);
        System.out.println(level > 4 ? level > 5 ? ERROR_6 : ERROR_5 : ERROR_0);
        System.out.println(BOTTOM);
    }

    // Загрузить список слов из файла данных в resources
    private List<String> loadWords() {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/" + DATA_FILE))))) {
            while (reader.ready()) {
                words.add(reader.readLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return words;
    }

    private static boolean isYes(String answer) {
        if (answer.isEmpty()) return false;
        return answer.equalsIgnoreCase(YES_STRING) ||
                YES_CHAR == Character.toLowerCase(answer.charAt(0));
    }

    private static boolean isNo(String answer) {
        if (answer.isEmpty()) return true;
        return answer.equalsIgnoreCase(NO_STRING) ||
                NO_CHAR == Character.toLowerCase(answer.charAt(0));
    }

    private boolean isWin() {
        return word.guessWord();
    }

    private boolean isFault() {
        return error == MAX_ERROR;
    }

    // Игровой цикл
    private void start() {
        Scanner scanner = new Scanner(System.in);
        while (!(isWin() || isFault())) {
            System.out.println("Ваше слово: " + word.getResult());
            System.out.print("Введите букву: ");
            char letter = scanner.nextLine().charAt(0);
            if (!word.hasLetter(letter)) {
                error++;
                printHangman(error);
                System.out.println("Вы не отгадали букву. Буквы '" + letter + "' нет в слове(\n" +
                        "Использована " + error + " из " + MAX_ERROR + " попыток\n");
            } else {
                System.out.println("Вы отгадали букву! Буква '" + letter + "' есть в слове!");
            }
        }
        if (isWin()) {
            System.out.println("Вы выиграли!!! ");
        } else {
            System.out.println("Вы проиграли((( ");
        }
        System.out.println("Было загадано слово: " + word);
    }

    public static void testMain(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String answer = "";
        while (isNo(answer)) {
            if (isYes(answer)) {
                game = new Hangman();
                game.start();
            }
            System.out.print("Хотите начать новую игру [yes/no]? ");
            answer = scanner.nextLine();
        }
    }

    public static void main(String[] args) {
//        testMain(args);
        game = new Hangman();
        game.start();
    }
}

