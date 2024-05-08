package base;

import java.util.ArrayList;

/**
 * Định nghĩa lớp Dictionary.
 */
public class Dictionary {
    private ArrayList<Word> words; //Danh sách words

    /**
     * Constructor khởi tạo array lưu trữ từ.
     */
    public Dictionary() {
        words = new ArrayList<>();
    }

    /**
     * Thêm từ mới vào danh sách
     * @param word từ cần thêm.
     */
    public void addWord(Word word) {
        words.add(word);
    }

    //Getter words
    public ArrayList<Word> getWords() {
        return words;
    }
}