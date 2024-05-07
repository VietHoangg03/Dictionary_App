package base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DictionaryManagement {
    private final Dictionary dictionary;
    private static final String IN_PATH = "src/resource/oldVocab/dictionaries.txt";
    private static final String OUT_PATH = "src/resource/oldVocab/dictionaries_out.txt";

    public DictionaryManagement() {
        dictionary = new Dictionary();
    }

    public void insertFromCommandline() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of words:");
        int numberOfWords = scanner.nextInt();
        scanner.nextLine(); // consume newline left-over

        for (int i = 0; i < numberOfWords; i++) {
            System.out.println("Enter word in English:");
            String englishWord = scanner.nextLine();

            System.out.println("Enter word in Vietnamese:");
            String vietnameseWord = scanner.nextLine();

            Word word = new Word(englishWord, vietnameseWord);
            dictionary.addWord(word);
        }
    }

    public void insertFromFile() {
        File file = new File(IN_PATH);
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String englishWord = parts[0];
                    String vietnameseWord = parts[1];
                    Word word = new Word(englishWord, vietnameseWord);
                    dictionary.addWord(word);
                }
            }
            scanner.close();
            System.out.println("Đã thêm dữ liệu từ file thành công. Hãy chọn chức năng tiếp theo");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + file.getPath());
        }
    }

    public void dictionaryLookup() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the word to lookup:");
        String englishWord = scanner.nextLine();

        int index = binaryLookup(0, dictionary.getWords().size(), englishWord, dictionary.getWords());
        if (index >= 0) {
            System.out.println("Vietnamese meaning: " + dictionary.getWords().get(index).getMeaning());
        } else {
            System.out.println("Word not found in the dictionary.");
        }
    }

    public int binaryLookup(int start, int end, String word, ArrayList<Word> temp) {
        if (end >= start) {
            int mid = start + (end - start) / 2;
            int compare = word.compareTo(temp.get(mid).getSearching());

            // If the word is present at the middle itself
            if (compare == 0) {
                return mid;
            }

            // If word is smaller, ignore right half
            if (compare < 0) {
                return binaryLookup(start, mid - 1, word, temp);
            }

            // Else the word can only be present in right subarray
            return binaryLookup(mid + 1, end, word, temp);
        }

        // We reach here when the word is not present in the array
        return -1;
    }

    public void addWord() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the English word to add:");
        String englishWord = scanner.nextLine();

        System.out.println("Enter the Vietnamese meaning:");
        String vietnameseWord = scanner.nextLine();

        Word word = new Word(englishWord, vietnameseWord);
        dictionary.addWord(word);

        System.out.println("Word added successfully.");
    }

    public void editWord() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the English word to edit:");
        String englishWord = scanner.nextLine();

        int index = binaryLookup(0, dictionary.getWords().size(), englishWord, dictionary.getWords());
        if (index >= 0) {
            System.out.println("Enter the new Vietnamese meaning:");
            String vietnameseWord = scanner.nextLine();

            dictionary.getWords().get(index).setMeaning(vietnameseWord);

            System.out.println("Word edited successfully.");
        } else {
            System.out.println("Word not found in the dictionary.");
        }
    }

    public void deleteWord() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the English word to delete:");
        String englishWord = scanner.nextLine();

        int index = binaryLookup(0, dictionary.getWords().size(), englishWord, dictionary.getWords());
        if (index >= 0) {
            dictionary.getWords().remove(index);

            System.out.println("Word deleted successfully.");
        } else {
            System.out.println("Word not found in the dictionary.");
        }
    }

    public void dictionaryExportToFile() {
        try {
            FileWriter writer = new FileWriter(OUT_PATH);

            for (Word word : dictionary.getWords()) {
                writer.write(word.getSearching() + "\t" + word.getMeaning() + "\n");
            }

            writer.close();
            System.out.println("Từ điển được suất thành công ra file dictionary_out.txt.");
        } catch (IOException e) {
            System.out.println("Đã xảy ra lỗi khi xuất từ điển.");
            e.printStackTrace();
        }
    }

    public static int isContain(String str1, String str2) {
        for (int i = 0; i < Math.min(str1.length(), str2.length()); i++) {
            if (str1.charAt(i) > str2.charAt(i)) {
                return 1;
            } else if (str1.charAt(i) < str2.charAt(i)) {
                return -1;
            }
        }
        if (str1.length() > str2.length()) {
            return 1;
        }
        return 0;
    }
    public Dictionary getDictionary() {
        return dictionary;
    }
}