package base;

import base.Dictionary;
import base.Word;

import java.util.Collections;
import java.util.Scanner;

public class DictionaryCommandLine {
    public void showAllWords(Dictionary dictionary) {
        Collections.sort(dictionary.getWords());

        System.out.printf("%-6s%c %-15s%c %-20s%n", "No", '|', "English", '|', "Vietnamese");

        int i = 1;
        for (Word word : dictionary.getWords()) {
            System.out.printf("%-6d%c %-15s%c %-20s%n", i, '|', word.getSearching(), '|', word.getMeaning());
            i++;
        }
    }

    /**
     * Từ điển cơ bản.
     */
    public void dictionaryBasic() {
        DictionaryManagement dictionaryManagement = new DictionaryManagement();
        dictionaryManagement.insertFromCommandline();
        showAllWords(dictionaryManagement.getDictionary());
    }

    /**
     * Tìm kiếm với tiền tố.
     */
    public void dictionarySearcher(Dictionary dictionary) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the prefix to search:");
        String prefix = scanner.nextLine().toLowerCase(); // convert to lower case

        System.out.println("Words starting with '" + prefix + "':");
        for (Word word : dictionary.getWords()) {
            if (word.getSearching().toLowerCase().startsWith(prefix)) { // convert to lower case before comparing
                System.out.println(word.getSearching() + " - " + word.getMeaning());
            }
        }
    }

    /**
     * Giao diện CommandLine.
     */
    public void dictionaryAdvanced() {
        DictionaryManagement dictionaryManagement = new DictionaryManagement();
        Scanner scanner = new Scanner(System.in);
        int action;

        do {
            System.out.println("Welcome to My Application!");
            System.out.println("[0] Exit");
            System.out.println("[1] Add");
            System.out.println("[2] Remove");
            System.out.println("[3] Update");
            System.out.println("[4] Display");
            System.out.println("[5] Lookup");
            System.out.println("[6] Search");
            System.out.println("[7] Game");
            System.out.println("[8] Import from file");
            System.out.println("[9] Export to file");
            System.out.print("Your action: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Action not supported");
                scanner.next();
            }
            action = scanner.nextInt();

            switch (action) {
                case 1:
                    dictionaryManagement.addWord();
                    break;
                case 2:
                    dictionaryManagement.deleteWord();
                    break;
                case 3:
                    dictionaryManagement.editWord();
                    break;
                case 4:
                    showAllWords(dictionaryManagement.getDictionary());
                    break;
                case 5:
                    dictionaryManagement.dictionaryLookup();
                    break;
                case 6:
                    dictionarySearcher(dictionaryManagement.getDictionary());
                    break;
                case 7:
                    int numOfQuestions = Game.getNumOfQuestions();
                    Game.loadQuestion(numOfQuestions);
                    break;
                case 8:
                    dictionaryManagement.insertFromFile();
                    break;
                case 9:
                    dictionaryManagement.dictionaryExportToFile();
                    break;
                default:
                    if (action != 0) {
                        System.out.println("Action not supported");
                    }
                    break;
            }
        } while (action != 0);
    }

    public static void main(String[] args) {
        DictionaryCommandLine commandLine = new DictionaryCommandLine();
        commandLine.dictionaryAdvanced();
    }
}