package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Word {

    private final String string;
    private final Map<Character, List<Integer>> lettersIndexes;
    private String result;

    public Word(String word) {
        string = word.toUpperCase();
        result = "*".repeat(word.length());
        lettersIndexes = toLettersIndexes(string);
    }

    public String getResult() {
        return result;
    }

    public Map<Character, List<Integer>> getLettersIndexes() {
        return lettersIndexes;
    }

    // Преобразовать слово в map по ключам из букв и их позиций
    public static Map<Character, List<Integer>> toLettersIndexes(String word) {
        Map<Character, List<Integer>> map = new HashMap<>();
        int index = 0;
        for (Character c : word.toCharArray()) {
            map.computeIfAbsent(c, ArrayList::new).add(index++);
        }
        return map;
    }

    // Слово отгадано, если в карте отгадываемых букв их больше нет
    public boolean guessWord() {
        return lettersIndexes.isEmpty();
    }

    public boolean hasLetter(char letter) {
        return guessLetter(letter);
    }

    // Угадывание буквы: проверка, есть ли буква в карте отгадываемых букв
    // Если буква есть, то включить букву во все её позиции слова, и
    // удалить её из карты отгадываемых букв
    private boolean guessLetter(char letter) {
        char newLetter = Character.toUpperCase(letter);
        List<Integer> indexes = lettersIndexes.get(newLetter);
        if (indexes == null) return false;
        StringBuilder newLetters = new StringBuilder(result);
        for (Integer index: indexes) {
            newLetters.setCharAt(index, newLetter);
        }
        result = newLetters.toString();
        return true;
    }

    public static void main(String[] args) {
        String word = "кооперация";
        Map<Character, List<Integer>> letters = toLettersIndexes(word);
        System.out.println(letters);
    }

    @Override
    public String toString() {
        return string;
    }
}
