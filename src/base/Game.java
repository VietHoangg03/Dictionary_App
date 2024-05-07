package base;


import java.io.*;
import java.util.*;

public class Game {
    private static final String IN_PATH_Game = "src/resource/Game/game1.txt";

    public static int getNumOfQuestions() {
        int numOfQuestions = 0;
        File file = new File(IN_PATH_Game);

        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(file));

            while ((br.readLine()) != null) {
                numOfQuestions++;
            }
            br.close();
            return numOfQuestions;
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + file.toString());
        } catch (IOException e) {
            System.out.println("Unable to read file: " + file.toString());
        }
        return 0;
    }

    public static void loadQuestion(int numOfQuestions) {
        String line = choose(numOfQuestions);
        String question = "", A = "", B = "", C = "", D = "", ans = "";
        int currentTab = 0, numTab = 0, setTab = 0;
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
        if (s.toUpperCase().equals(ans)) {
            System.out.println("Đáp án chính xác");
        }else {
            System.out.println("Đáp án sai!! Đáp án đúng là : "+ ans);
        }
        System.out.println("Bạn có muốn tiếp tục?Y/N");
        s = sc.nextLine();
        if (s.toUpperCase().equals("Y")) {
            loadQuestion(numOfQuestions);
        }
    }

    public static String choose(int numOfQuestions) {
        File file = new File(IN_PATH_Game);
        BufferedReader br = null;
        try {
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


        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + file.toString());
        } catch (IOException e) {
            System.out.println("Unable to read file: " + file.toString());
        }
        return "";
    }
}