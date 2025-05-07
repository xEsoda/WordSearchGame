package uta.cse3310;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;


public class WordGrid {
    int size;
    Character [][] grid = new Character[size][size];
    double validCharCount = 0;
    double totalCharCount = 0;
    double density = 0;
    ArrayList<String> usedWords = new ArrayList<>();
    ArrayList<String> wordBank = new ArrayList<>();
    int[] startPos = new int[]{0,0};
    transient Random rand = new Random();
    ArrayList<Character> alphabet = new ArrayList<>(Arrays.asList('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'));
    double horizontal = 0;
    double vertical = 0;
    double reverse = 0;
    double diagUp = 0;
    double diagDown = 0;
    double VerticalUp = 0;
    int sharedWords = 0;
    ArrayList<int [][]> wordCoordinates = new ArrayList<>();
    final double minPercentage = 0.15;
    ArrayList<Double> stats = new ArrayList<>();
    ArrayList<int [][]> diagDownCoord = new ArrayList<>();
    ArrayList<int [][]> diagUpCoords = new ArrayList<>();

    public WordGrid(int size, ArrayList<String> wordBank){
        grid = new Character[size][size];
        rand = new Random();
        this.size = size;
        this.wordBank = wordBank;
    }

    public void generateGrid(ArrayList<String> word_bank){
        for(String word: word_bank){
            int length = word.length();
            place(word, startPos, length);
        }
        fillGrid();
        totalCharCount += validCharCount;
        density = (validCharCount)/(totalCharCount);
        System.out.println(density);
        double numWords = usedWords.size();
        double horPercentage = horizontal/numWords;
        double vertPercentage = vertical/numWords;
        double diagUpPerc = diagUp/numWords;
        double diagDownPerc = diagDown/numWords;
        double revPerc = reverse/numWords;
        //(density < 0.67) || ( (horPercentage < minPercentage) && (vertPercentage < minPercentage) && (diagUpPerc < minPercentage) && (diagDownPerc < minPercentage) && (revPerc < minPercentage))
        while(density < 0.67){
            resetGrid();
            generateGrid(word_bank);
        }
    }
    public Character[][] getgrid(){       
        return grid;
    }
    public void place(String word,int[] start, int length){
        int[] newStart = new int[]{rand.nextInt(size),rand.nextInt(size)};
        int allocation = rand.nextInt(6);
        switch(allocation){
            case 1: 
            placeHorizontal(word,newStart,length);
            break;
            case 2:
            placeVertical(word,newStart,length);
            break;
            case 3:
            placeDiagonalDown(word,newStart,length);
            break;
            case 4:
            placeDiagonalUp(word,newStart,length);
            break;
            case 5:
            placeVerticalUp(word,newStart,length);
            break;
        }
    }

    public void placeHorizontal(String word, int[] start, int length){
        int row = start[0];
        int col = start[1];
        boolean isEmpty = true;
        boolean shared = false;
        if(col + length < size){
            for(int i = 0; i < length; i++){
                if(grid[row][col] != null){
                    if(sharedWords < 10 && i == 0 && grid[row][col] == word.charAt(0)){
                        shared = true;
                    }
                    else if(sharedWords < 10 && i == length -1 && grid[row][col] == word.charAt(i)){
                        shared = true;
                    }
                    else{
                        isEmpty = false;
                    }
                }
                col++;
            }
            row = start[0];
            col = start[1];
            if(isEmpty){
                for(int i = 0; i < length; i++){
                    grid[row][col] = word.charAt(i);
                    col++;
                    validCharCount++;
                }
                int end[] = new int[]{row, (col-1)};
                horizontal++;
                usedWords.add(word);
                wordCoordinates.add(new int[][]{start,end});
                if(shared){
                    sharedWords++;
                }
            }
        }
    }
    public void placeVertical(String word, int[] start, int length){
        int row = start[0];
        int col = start[1];
        boolean isEmpty = true;
        boolean shared = false;
        if(row + length < size) {
            for(int i = 0; i < length; i++){
                if(grid[row][col] != null){
                    if(sharedWords < 10 && i == 0 && grid[row][col] == word.charAt(0)){
                        shared = true;
                    }
                    else if(sharedWords < 10 && i < length -1  && grid[row][col] == word.charAt(i)){
                        shared = true;
                    }
                    else{
                        isEmpty = false;
                    }
                }
                row++;
            }
            if(isEmpty){
                row = start[0];
                col = start[1];
                for(int i = 0; i < length; i++){
                    grid[row][col] = word.charAt(i);
                    row++;
                    validCharCount++;
                }
                int[] end = new int[]{row-1,col};
                vertical++;
                wordCoordinates.add(new int[][]{start,end});
                usedWords.add(word);
                if(shared){
                    sharedWords++;
                }
            }
        }
    }
    public void placeDiagonalUp(String word, int[] start, int length){
        int row = start[0];
        int col = start[1];
        boolean isEmpty = true;
        boolean shared = false;
        if(row - length >= 0 && col + length < size){
            for(int i = 0; i < length; i++){
                if (grid[row][col] != null){
                    if(sharedWords < 10 && i == 0 && word.charAt(0) == grid[row][col]){
                        shared = true;
                    }
                    else if(sharedWords < 10 && i < length -1  && grid[row][col] == word.charAt(i)){
                        shared = true;
                    }
                    else{
                        isEmpty = false;
                    }
                }
                row--;
                col++;
            }
            if(isEmpty){
                row = start[0];
                col = start[1];
                for(int i = 0; i < length; i++){
                    grid[row][col] = word.charAt(i);
                    row--;
                    col++;
                    validCharCount++;
                }
                int[] end = new int[]{row+1,col-1};
                diagUp++;
                wordCoordinates.add(new int[][]{start, end});
                diagUpCoords.add(new int[][]{start,end});
                usedWords.add(word);
                if(shared){
                    sharedWords++;
                }
            }
            
        }
    }

    public void placeDiagonalDown(String word, int[] start, int length){
        int row = start[0];
        int col = start[1];
        boolean isEmpty = true;
        boolean shared = false;
        if(row + length < size && col - length >= 0){
            for(int i = 0; i < length; i++){
                if (grid[row][col] != null){
                    if(sharedWords < 10 && i == 0 && word.charAt(0) == grid[row][col]){
                        shared = true;
                    }
                    else if(sharedWords < 10 && i < length -1  && grid[row][col] == word.charAt(i)){
                        shared = true;
                    }
                    else{
                        isEmpty = false;
                    }
                }
                row++;
                col--;
            }
            if(isEmpty){
                row = start[0];
                col = start[1];
                for(int i = 0; i < length; i++){
                    grid[row][col] = word.charAt(i);
                    row++;
                    col--;
                    validCharCount++;
                }
                int[] end = new int[]{row-1, col+1};
                wordCoordinates.add(new int[][]{start,end});
                diagDownCoord.add(new int[][]{start,end});
                diagDown++;
                usedWords.add(word);
                if(shared){
                    sharedWords++;
                }
            }
        }
    }
    public void placeReverse(String word, int[] start, int length){
        int row = start[0];
        int col = start[1];
        boolean isEmpty = true;
        if(col - length  >= 0){
            for(int i = 0; i < length; i++){
                if (grid[row][col] != null){
                    isEmpty = false;
                }
                col--;
            }
            if(isEmpty){
                row = start[0]; col = start[1];
                for(int i = 0; i < length; i++){
                    grid[row][col] = word.charAt(i);
                    col--;
                    validCharCount++;
                }
                usedWords.add(word);
                reverse++;
                int[] end = new int[]{row, col+1};
                wordCoordinates.add(new int[][]{start,end});
            }
        }
    }
    public void placeVerticalUp(String word, int[] start, int length){
        int row = start[0];
        int col = start[1];
        boolean isEmpty = true;
        boolean shared = false;
        if(row - length >=  0) {
            for(int i = 0; i < length; i++){
                if(grid[row][col] != null){
                    if(sharedWords < 10 && i == 0 && word.charAt(0) == grid[row][col]){
                        shared = true;
                    }
                    else if(sharedWords < 10 && i < length -1  && grid[row][col] == word.charAt(i)){
                        shared = true;
                    }
                    else{
                        isEmpty = false;
                    }
                }
                row--;
            }
            if(isEmpty){
                row = start[0];
                col = start[1];
                for(int i = 0; i < length; i++){
                    grid[row][col] = word.charAt(i);
                    row--;
                    validCharCount++;
                }
                int[] end = new int[]{row+1,col};
                VerticalUp++;
                wordCoordinates.add(new int[][]{start,end});
                usedWords.add(word);
                if(shared){
                    sharedWords++;
                }
            }
        }
    }
    public boolean checkGrid(ArrayList<String> wordBank){
        double numWords = usedWords.size();
        double horPercentage = horizontal/numWords;
        double vertPercentage = vertical/numWords;
        double diagUpPerc = diagUp/numWords;
        double diagDownPerc = diagDown/numWords;
        double revPerc = reverse/numWords;
        double vertUpPerc = VerticalUp/numWords;
        //         while(vertPercentage < minPercentage || horPercentage < minPercentage || diagDownPerc < minPercentage || diagUpPerc < minPercentage || revPerc < minPercentage){

        if(vertPercentage < minPercentage || horPercentage < minPercentage || diagDownPerc < minPercentage || diagUpPerc < minPercentage || vertUpPerc < minPercentage){
            resetGrid();
            return false;
        }
        else{
            return true;
        }
    }
    public void fillGrid(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(grid[i][j] == null){
                    grid[i][j] = alphabet.get(rand.nextInt(26));
                    totalCharCount++;
                }
            }
        }
    }

    public void printGrid(){
        for(Character[] row : grid){
            for(Character c : row ){
                System.out.print(c + " ");
            }
            System.out.println();
        } 
    }

    public void resetGrid(){
        for(int i = 0; i < size; i ++){
            for(int j = 0; j < size; j++){
                grid[i][j] = null;
            }
        }
        totalCharCount = 0;
        validCharCount = 0;
        reverse = 0;
        diagDown = 0;
        diagUp = 0;
        horizontal = 0;
        vertical = 0;
        VerticalUp = 0;
        sharedWords = 0;
        wordCoordinates.clear();
        diagDownCoord.clear();
        diagUpCoords.clear();
        usedWords.clear();
    }

    public boolean checkCoordinates(int[][] button){
        for(int i = 0; i < wordCoordinates.size(); i++){
            int[][] coords = wordCoordinates.get(i);
            if(button[0][0] == coords[0][0] && button[0][1] == coords[0][1] && button[1][0] == coords[1][0] && button[1][1] == coords[1][1]){
                return true;
            }
        }
        return false;
    }

    public void printGridStats(){
        double numWords = usedWords.size();
        double horPercentage = horizontal/numWords;
        double vertPercentage = vertical/numWords;
        double diagUpPerc = diagUp/numWords;
        double diagDownPerc = diagDown/numWords;
        double revPerc = reverse/numWords;
        double verticalUpPerc = VerticalUp/numWords;

        System.err.println("totalchar: " + totalCharCount + " validChar = "+ validCharCount);
        System.out.println("Horizontal\tVertical\tDiagUp\tDiagDown\tReverse");

        System.out.println( horizontal+"      \t" + vertical+"      \t" + diagUp+"   \t" +diagDown+ "    \t" + VerticalUp);
        System.out.println("horizontal: " + horPercentage+ " vert: "+ vertPercentage+" diagUp: "+diagUpPerc + " diagDown: "+ diagDownPerc+ "vert Up: " + verticalUpPerc);
        System.out.println("number of shared words: " + sharedWords);
        // for(int[][] w : diagDownCoord){
        //     System.out.println(Arrays.deepToString(w));
        // }
        System.out.println("now for diag up");
        // for(int[][] w : diagUpCoords){
        //     System.out.println(Arrays.deepToString(w));
        // }

    }
    public void calculateStats(){
        double numWords = usedWords.size();
        double horPercentage = horizontal/numWords;
        double vertPercentage = vertical/numWords;
        double diagUpPerc = diagUp/numWords;
        double diagDownPerc = diagDown/numWords;
        // double revPerc = reverse/numWords;
        double verticalUpPerc = VerticalUp/numWords;
        stats.add(density);
        stats.add(horPercentage);
        stats.add(diagDownPerc);
        stats.add(diagDownPerc);
        stats.add(vertPercentage);
        stats.add(verticalUpPerc);
        stats.add((double)sharedWords);
    }
}