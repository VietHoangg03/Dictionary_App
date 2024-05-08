package base;


import java.io.*;
import java.util.*;

public class Game {
    private static final String IN_PATH_Game = "src/resource/Game/game1.txt";

    /**
     * Lưu số lượng câu hỏi.
     * @return
     */
    public static int getNumOfQuestions() {
        int numOfQuestions = 0;
        File file = new File(IN_PATH_Game); //Tạo đối tượng file từ đường dẫn chỉ định

        BufferedReader br = null; //Đọc từng dòng trong tệp tin

        try {
            br = new BufferedReader(new FileReader(file));

            while ((br.readLine()) != null) {
                numOfQuestions++;
            }
            br.close(); //đóng file
            return numOfQuestions;
        } catch (FileNotFoundException e) { //Xử lí ném ngoại lệ
            System.out.println("Không tìm thấy file: " + file.toString());
        } catch (IOException e) {
            System.out.println("Không thể đọc file: " + file.toString());
        }
        return 0;
    }


    /**
     * Tải câu hỏi từ dòng văn bản từ file.
     * @param numOfQuestions số lượng câu hỏi
     */
    public static void loadQuestion(int numOfQuestions) {
        String line = choose(numOfQuestions); //xác định câu hỏi random
        String question = "", A = "", B = "", C = "", D = "", ans = "";
        int currentTab = 0, numTab = 0, setTab = 0;

        //Các phần được ngăn cách bởi dấu Tab
        for (int i = 0; i< line.length(); i++) {
            if(line.charAt(i) == '\t') {
                setTab = i;
                if(numTab == 0) {
                    question = new String(line.substring(currentTab, setTab));
                    numTab++;
                }
                else if(numTab == 1) {
                    A = new String(line.substring(currentTab, setTab));
                    numTab++;
                }
                else if(numTab == 2) {
                    B = new String(line.substring(currentTab, setTab));
                    numTab++;
                }
                else if(numTab == 3) {
                    C = new String(line.substring(currentTab, setTab));
                    numTab++;
                }
                else if(numTab == 4) {
                    D = new String(line.substring(currentTab, setTab));
                    ans = new String(line.substring(setTab+1));
                    break;
                }
                currentTab = setTab+1;
            }
        }

        System.out.println(question + "\n" + A + "\n" + B + "\n" + C + "\n" + D + "\nBạn hãy chọn đáp án đúng? [A/B/C/D] ");
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();

        //So sánh lựa chọn với đáp án.
        if (s.toUpperCase().equals(ans)) {
            System.out.println("Đáp án chính xác");
        }else {
            System.out.println("Đáp án sai!! Đáp án đúng là : "+ ans);
        }

        // Khởi tạo câu hỏi tiếp theo.
        System.out.println("Bạn có muốn tiếp tục?Y/N");
        s = sc.nextLine();
        if (s.toUpperCase().equals("Y")) {
            loadQuestion(numOfQuestions);
        }
    }

    /**
     * Chọn câu hỏi ngẫu nhiên.
     * @param numOfQuestions số lượng câu hỏi
     * @return
     */
    public static String choose(int numOfQuestions) {
        File file = new File(IN_PATH_Game);
        BufferedReader br = null;
        try {
            // Đọc dữ liệu từ file
            br = new BufferedReader(new FileReader(file));
            Random random = new Random();
            int randomInt = random.nextInt(numOfQuestions);
            int count=0;
            String icaocode;
            while ( (icaocode = br.readLine()) != null) {
                if (count == randomInt) {
                    br.close();
                    return icaocode;
                }
                count++;
            }
            br.close();

            //xử lí ngoại lệ
        } catch (FileNotFoundException e) {
            System.out.println("Không tìm thấy file: " + file.toString());
        } catch (IOException e) {
            System.out.println("Không thể đọc file: " + file.toString());
        }
        return "";
    }
}