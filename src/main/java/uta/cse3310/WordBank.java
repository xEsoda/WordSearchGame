package uta.cse3310;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

public class WordBank {
    private int startPosition;
    private int endPosition;
    private boolean found;

    public static ArrayList<String> readFileIntoArray(String FileName){
        ArrayList<String>  words = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(FileName))){
            String line;
            while((line = reader.readLine()) != null){
                if(line.length() > 3)
                {
                    words.add(line.toLowerCase());
                }
            }
        }
        catch(Exception e){
            System.out.println("File not found");
        }
        return words;
    }
    public boolean markFound(){
        return true;
    }

    public static int randomNum(){
        Random rand = new Random();
        return rand.nextInt(7748);
    }

    public void displayWordBank (ArrayList<String> wordBank){
        System.out.println(wordBank);
    }
    
    public static ArrayList<String> wordBank (ArrayList<String> found, ArrayList<String> wordList){
        ArrayList<String> wordBank = new ArrayList<>();
        
        while(wordBank.size() != 7000){
            int i = randomNum();
            String word = wordList.get(i);
            if(!found.contains(word)){
                wordBank.add(word);
                found.add(word);
            }
        }
        return wordBank;
    }
}