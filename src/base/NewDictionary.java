package base;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;

public class NewDictionary {
    private final String PATH;
    private static final String SPLITTING_PATTERN = "<html>";
    private final String HISTORY_PATH;
    private final String BOOKMARK_PATH;

    private final ArrayList<Word> vocab = new ArrayList<>();
    private final ArrayList<Word> historyVocab = new ArrayList<>();
    private final ArrayList<Word> bookmarkVocab = new ArrayList<>();


    public NewDictionary(String path, String historyPath, String bookmarkPath) {
        PATH = path;
        HISTORY_PATH = historyPath;
        BOOKMARK_PATH = bookmarkPath;
        loadDataFromHTMLFile(PATH, vocab);
        loadDataFromHTMLFile(HISTORY_PATH, historyVocab);
        loadDataFromHTMLFile(BOOKMARK_PATH, bookmarkVocab);
    }

    public String getPATH() {
        return PATH;
    }

    public String getHISTORY_PATH() {
        return HISTORY_PATH;
    }

    public String getBOOKMARK_PATH() {
        return BOOKMARK_PATH;
    }

    public ArrayList<Word> getVocab() {
        return vocab;
    }

    public ArrayList<Word> getHistoryVocab() {
        return historyVocab;
    }

    public ArrayList<Word> getBookmarkVocab() {
        return bookmarkVocab;
    }

    /**
     * Nhận vào đường dẫn của tệp tin HTML và một ArrayList(temp) để lưu trữ dữ liệu.
     * @param path đường dẫn
     * @param temp Danh sách được sắp xếp
     */
    public void loadDataFromHTMLFile(String path, ArrayList<Word> temp) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = reader.readLine()) != null) {
                //thêm từ
                String[] parts = line.split(SPLITTING_PATTERN);
                String word = parts[0];
                //thêm định nghĩa cho từ
                String definition = SPLITTING_PATTERN + parts[1];
                Word wordObj = new Word(word, definition);
                temp.add(wordObj);
            }
            Collections.sort(temp); //sắp xếp các từ
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * load dữ liệu lịch sử từ.
     */
    public void loadDataFromHistoryFile(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(HISTORY_PATH));
            String line;
            while ((line = reader.readLine()) != null) {
                //Thêm từ
                String[] parts = line.split(SPLITTING_PATTERN);
                String word = parts[0];
                // Thêm nghĩa cho từ
                String definition = SPLITTING_PATTERN + parts[1];
                Word wordObj = new Word(word, definition);
                historyVocab.add(wordObj); //Thêm vào list historyVocab
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Xuất từ ra file html.
     * @param path
     * @param spelling
     */
    public void exportWordToHTMLFile(String path, String spelling) {
        try {
            File file = new File(path);
            FileWriter fileWriter = new FileWriter(file, true);

            //Tìm kiếm bằng tìm kiếm nhị phân và lấy định nghĩa
            int index = Collections.binarySearch(vocab, new Word(spelling, null));
            String meaning = vocab.get(index).getMeaning();

            // Viết từ và định nghĩa vào tệp HTML
            fileWriter.write(spelling + meaning + "\n");
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addWordToFile(String spelling, String meaning, String path) {
        try {
            //Mở tệp
            File file = new File(path);
            FileWriter fileWriter = new FileWriter(file, true);

            //Ghi từ và định nghĩa
            fileWriter.write(spelling + meaning + "\n");
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật nội dung từ của file.
     * @param path  đường dẫn
     * @param temp  list đã sắp xếp
     */
    public void updateWordToFile(String path, ArrayList<Word> temp) {
        try {
            File file = new File(path);
            FileWriter fileWriter = new FileWriter(file);
            for (Word word : temp) {
                fileWriter.write(word.getSearching() + word.getMeaning() + "\n");
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Thêm từ vào danh sách.
     * @param searching từ cần thêm
     * @param meaning nghĩa của từ
     * @return
     */
    public boolean addWord(String searching, String meaning) {
        searching = searching.toLowerCase(); //chuyển thành chữ thường

        //Kiểm tra bằng nhị phân để tìm vị trí thích hợp cho từ cần thêm
        int posAddWord = binaryCheck(0, vocab.size(), searching);
        if (posAddWord == -1) {
            return false; // từ đã tồn tại
        }
        vocab.add(new Word()); //Đối tượng từ mới được thêm

        //Dịch chuyển các từ sang phải 2 vị trí để thêm từ mới
        for (int i = vocab.size() - 2; i >= posAddWord; i--) {
            vocab.get(i + 1).setSearching(vocab.get(i).getSearching());
            vocab.get(i + 1).setMeaning(vocab.get(i).getMeaning());
        }

        // Gán từ cần thêm vào vị trí
        vocab.get(posAddWord).setSearching(searching);
        vocab.get(posAddWord).setMeaning(meaning);
        Collections.sort(vocab);
        updateWordToFile(PATH, vocab);
        return true;
    }

    /**
     * Xoá từ trong list.
     * @param searching từ cần xoá
     * @param path  đường dẫn tới file
     * @param temp  Danh sách word đã sắp ếp
     */
    public void removeWord(String searching, String path, ArrayList<Word> temp) {
        searching = searching.toLowerCase();

        //Tìm kiếm từ cần xoá bằng tìm kiếm nhị phân
        int index = Collections.binarySearch(temp, new Word(searching, null));
        if (index >= 0) {
            temp.remove(temp.get(index));
        } else {
            return;
        }
        updateWordToFile(path, temp);
    }

    /**
     * Sửa từ.
     * @param searching từ cần sửa.
     * @param meaning   nghĩa của từ.
     */
    public void modifyWord(String searching, String meaning) {
        searching = searching.toLowerCase();
        meaning = meaning.toLowerCase();
        int pos = -1;
        pos = Collections.binarySearch(vocab, new Word(searching, null));
        if (pos >= 0) {
            vocab.get(pos).setMeaning(meaning);
        } else {
            System.out.println("Không tìm thấy từ bạn muốn sửa đổi!");
        }
        updateWordToFile(PATH, vocab);
    }

    /**
     * Tìm kiếm nhị phân trên danh sách để tìm vị trí thích hợp ể chèn một từ mới vào danh sách.
     * @param start vị tr bắt đầu
     * @param end   vị trí kết thúc
     * @param word  từ cần tìm
     * @return
     */
    public int binaryCheck(int start, int end, String word) {
        if (end < start) {
            return -1;
        }
        int mid = start + (end - start) / 2;
        int compareNext = word.compareTo(vocab.get(mid).getSearching());
        if (mid == 0) {
            if (compareNext < 0) {
                return 0;
            } else if (compareNext > 0) {
                return binaryCheck(mid + 1, end, word);
            } else {
                return -1;
            }
        } else {
            int comparePrevious = word.compareTo(vocab.get(mid - 1).getSearching());
            if (comparePrevious > 0 && compareNext < 0) {
                return mid;
            } else if (comparePrevious < 0) {
                return binaryCheck(start, mid - 1, word);
            } else if (compareNext > 0) {
                if (mid == vocab.size() - 1) {
                    return vocab.size();
                }
                return binaryCheck(mid + 1, end, word);
            } else {
                return -1;
            }
        }
    }

    /**
     * Tìm kiếm từ trong danh sách bằng tìm kiếm nhị phân.
     * @param start vị trí bắt đầu
     * @param end   vị trí kết thúc
     * @param word  từ cần tìm
     * @param temp  danh sách từ điển cần tìm kiếm
     * @return
     */
    public int binaryLookup(int start, int end, String word, ArrayList<Word> temp) {
        if (end < start) {
            return -1;
        }
        int mid = start + (end - start) / 2;
        int compare = DictionaryManagement.isContain(word, temp.get(mid).getSearching());
        if (compare == -1) {
            return binaryLookup(start, mid - 1, word, temp);
        } else if (compare == 1) {
            return binaryLookup(mid + 1, end, word, temp);
        } else {
            return mid;
        }
    }
}

