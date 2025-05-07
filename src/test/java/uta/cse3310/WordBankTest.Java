package uta.cse3310;
import java.util.ArrayList;

public class WordBankTest 
extends TestsCase{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public WordBankTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(WordBankTest.class);
    }

    public void testReadFile(){
        //this method will test the reading of a file name into an ArrayList 
        //a bad file name or path will throw an exception, otherwise an ArrayList<String> is returned
        WordBank wordMethods = new WordBank();
        ArrayList<String> words = WordBank.readFileIntoArray("src/main/java/uta/cse3310/filtered_word 1.txt");
        assertNotNull(words);

        //we now have a list of all the words from the file, running the method wordBank() with a list of already found words (at this point none) will return a word bank in randomized order 
        ArrayList<String> found = new ArrayList<>();
        ArrayList<String> wordBank = WordBank.wordBank(found,words);
        assertNotNull(wordBank);
        //found should now also contain words that were used 
        assertNotNull(found);
        //these should now be the same
        assertEquals(found,wordBank);
        //the wordBank shall now be randomized order of the words read in from the file, and the found list is updated to have all of the words added

        //test that if the words have already been found, then the wordBank should be null
        ArrayList<String> wordBank2 = WordBank.wordBank(found,words);
        assertNull(wordBank2);

        words.clear(); //clear words list
        //testing bad file name
        try{
            words = WordBank.readFileIntoArray("src/main/java/uta/cse3310/filtered_word.txt");
        } catch(Exception e){
            assertTrue(true);
        }


        try{
            //testing bad path with correct file name (same outcome)
            words = WordBank.readFileIntoArray("test/main/java/uta/cse3310/filtered_word 1.txt");
        }catch(Exception e){
            assertTrue(true);
        }
        assertNull(words);
        //prints file not found for both cases
    }   
    
    public void testRandomNum(){
        int randomNumber = WordBank.randomNum();
        assertTrue(randomNumber >=0 && randomNumber < 7748);
    }

    public void testWordLength(){
        ArrayList<String> words = WordBank.readFileIntoArray("src/main/java/uta/cse3310/filtered_word 1.txt");
        assertNotNull(words);
        ArrayList<String> found = new ArrayList<>();
        ArrayList<String> wordBank = WordBank.wordBank(found,words);
        assertNotNull(wordBank);
        //the length of every word should be true after reading in the list 
        for(String word: words){
            assertTrue(word.length() > 3);
        }
    }

}
