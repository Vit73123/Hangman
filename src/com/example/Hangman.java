package com.example;

import com.example.exception.DictionaryEmptyExeption;
import com.example.exception.DictionaryNotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Hangman {

    private static final String DATA_FILE = "resources/data.txt";

    private static final String YES_STRING = "yes";
    private static final String YES_CHAR = "y";
    private static final String NO_STRING = "no";
    private static final String NO_CHAR = "n";


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

    public static Hangman game;

    private final List<String> dictionary;
    private final Word word;

    private int error;

    public Hangman() {
        dictionary = loadWords();
        if (dictionary.isEmpty()) {
            throw new DictionaryEmptyExeption(
                    String.format("В словаре нет слов. Наполните словарь в файле %s", DATA_FILE));
        }

        Random random = new Random();
        int index = random.nextInt(dictionary.size());
        word = new Word(dictionary.get(index));
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
        if (!Files.exists(Path.of(DATA_FILE))) {
            throw new DictionaryNotFoundException(
                    String.format("Отсутствует файл словаря слов. Проверьте наличие файла %s", DATA_FILE));
        }
        List<String> dictionary;
        try {
            dictionary = Files.readAllLines(Path.of(DATA_FILE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dictionary;
    }

    private static boolean isYes(String answer) {
        if (answer.isEmpty()) return false;
        return answer.equalsIgnoreCase(YES_STRING) || answer.equalsIgnoreCase(YES_CHAR);
    }

    private static boolean isNo(String answer) {
        if (answer.isEmpty()) return false;
        return answer.equalsIgnoreCase(NO_STRING) || answer.equalsIgnoreCase(NO_CHAR);
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String answer = "";
        while (!isNo(answer)) {
            if (isYes(answer)) {
                game = new Hangman();
                if (game.word != null) {
                    game.start();
                }
            }
            System.out.print("Хотите начать новую игру [yes/no]? ");
            answer = scanner.nextLine();
        }
    }
}

