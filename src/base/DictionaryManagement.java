package base;

import java.io.*;
import java.util.*;

public class DictionaryManagement {
    private final Dictionary dictionary;
    private static final String IN_PATH = "src/resource/oldVocab/dictionaries.txt";
    private static final String OUT_PATH = "src/resource/oldVocab/dictionaries_out.txt";

    public DictionaryManagement() {
        dictionary = new Dictionary();
    }

    /**
     * Thêm từ mới vào từ điển.
     */
    public void insertFromCommandline() {
        Scanner scanner = new Scanner(System.in);

        //Nhập số lượng từ cần thêm
        System.out.println("Enter the number of words:");
        int numberOfWords = scanner.nextInt();
        scanner.nextLine(); // consume newline left-over

        //Thêm từ tiếng anh kèm nghĩa tiếng việt
        for (int i = 0; i < numberOfWords; i++) {
            System.out.println("Enter word in English:");
            String englishWord = scanner.nextLine();

            System.out.println("Enter word in Vietnamese:");
            String vietnameseWord = scanner.nextLine();

            Word word = new Word(englishWord, vietnameseWord);
            dictionary.addWord(word);
        }
    }

    /**
     * Thêm từ vào từ điển bằng dữ liệu trong file.
     */
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

    /**
     * Tìm kiếm từ trong từ điển.
     */
    public void dictionaryLookup() {
        Scanner scanner = new Scanner(System.in); //scan file

        System.out.println("Enter the word to lookup:");
        String englishWord = scanner.nextLine().toLowerCase(); // nhập từ và chuyển về dạng chữ thường

        int index = binaryLookup(0, dictionary.getWords().size(), englishWord, dictionary.getWords());
        if (index >= 0) {
            System.out.println("Vietnamese meaning: " + dictionary.getWords().get(index).getMeaning());
        } else {
            System.out.println("Word not found in the dictionary.");
        }
    }

    /**
     * Tìm kiếm nhị phân để tìm từ trong từ điển.
     * @param start mục bắt đầu
     * @param end   mục cuối
     * @param word  từ cần tìm
     * @param temp  Danh sách từ điển đã được sắp xếp theo bảng chữ cái
     * @return
     */
    public int binaryLookup(int start, int end, String word, ArrayList<Word> temp) {
        if (end >= start) {
            int mid = start + (end - start) / 2; //xác định vị trí trung tâm
            int compare = word.compareTo(temp.get(mid).getSearching());

            // Nếu tìm thấy từ ở vị trí trung tâm, return ra mid
            if (compare == 0) {
                return mid;
            }

            // Nếu từ tìm kiếm nhỏ hơn thì tìm kiếm nửa bên trái của mid
            if (compare < 0) {
                return binaryLookup(start, mid - 1, word, temp);
            }

            // Nếu từ tìm kiếm lớn hơn thì tìm kiếm nửa bên phải của mid
            return binaryLookup(mid + 1, end, word, temp);
        }

        // Return không tìm thấy
        return -1;
    }

    /**
     * Thêm từ mới vào từ điển.
     */
    public void addWord() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the English word to add:");
        String englishWord = scanner.nextLine().toLowerCase(); // convert to lower case

        System.out.println("Enter the Vietnamese meaning:");
        String vietnameseWord = scanner.nextLine();

        Word word = new Word(englishWord, vietnameseWord);
        dictionary.addWord(word);

        // Write the new word to the file
        try {
            FileWriter writer = new FileWriter(IN_PATH, true); // Mở tệp với chế độ ghi append
            writer.write(englishWord + "," + vietnameseWord + "\n");
            writer.close();
        } catch (IOException e) {  // Xử lí ngoại lệ
            System.out.println("Xảy ra lỗi khi thêm từ mới vào từ điển.");
            e.printStackTrace();
        }

        System.out.println("Thêm từ mới thành công");
    }

    /**
     * Sửa từ.
     */
    public void editWord() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the English word to edit:");
        String englishWord = scanner.nextLine().toLowerCase(); // convert to lower case

        //Tìm kiếm từ cần sửa bằng hàm tìm kiếm nhị phn
        int index = binaryLookup(0, dictionary.getWords().size(), englishWord, dictionary.getWords());
        if (index >= 0) {
            System.out.println("Enter the new Vietnamese meaning:");
            String vietnameseWord = scanner.nextLine();

            dictionary.getWords().get(index).setMeaning(vietnameseWord);

            System.out.println("Từ được sửa thành công.");
        } else {
            System.out.println("Không tìm thấy từ trong từ điển.");
        }
    }

    /**
     * Xóa từ.
     */
    public void deleteWord() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the English word to delete:");
        String englishWord = scanner.nextLine().toLowerCase(); // convert to lower case

        //Tìm kiếm từ cần xoá bằng hàm tìm kiếm nhị phân.
        int index = binaryLookup(0, dictionary.getWords().size(), englishWord, dictionary.getWords());
        if (index >= 0) {
            dictionary.getWords().remove(index);

            System.out.println("Xoá từ thành công.");
        } else {
            System.out.println("Không tìm thấy từ cần xoá.");
        }
    }

    /**
     * Xuất ra file từ điển.
     */
    public void dictionaryExportToFile() {
        try {
            FileWriter writer = new FileWriter(OUT_PATH);

            for (Word word : dictionary.getWords()) {
                writer.write(word.getSearching() + "\t" + word.getMeaning() + "\n");
            }

            writer.close();
            System.out.println("Từ điển được suất thành công ra file dictionary_out.txt.");
        } catch (IOException e) { //ném ngoại lệ
            System.out.println("Đã xảy ra lỗi khi xuất từ điển.");
            e.printStackTrace();
        }
    }

    /**
     * Kiếm tra xem chuỗi s1 có chứa chuỗi s2 hay không?.
     * @param str1 chuỗi số một
     * @param str2 chuỗi số hai
     * @return
     */
    public static int isContain(String str1, String str2) {
        for (int i = 0; i < Math.min(str1.length(), str2.length()); i++) {
            //So sánh vị trí tại i của hai chuỗi.
            if (str1.charAt(i) > str2.charAt(i)) {
                return 1; //s1 đứng sau s2.
            } else if (str1.charAt(i) < str2.charAt(i)) {
                return -1; //s1 đứng trước s2.
            }
        }
        if (str1.length() > str2.length()) {
            return 1; //s1 có độ dài lớn hơn s2 thì s1 đứng trước s2
        }
        return 0; //hai chuỗi bằng nhau
    }

    /**
     * Getter of Dictionary.
     * @return
     */
    public Dictionary getDictionary() {
        return dictionary;
    }
}