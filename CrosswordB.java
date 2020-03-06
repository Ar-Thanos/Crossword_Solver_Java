import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CrosswordB {
    private DictInterface dictionary;
    private StringBuilder[] rows;
    private StringBuilder[] columns;
    private char[][] board; // To store the pluses and minuses and characters from the user input file
    private int boardSize;
    private char[] alphabet; // Alphabet plus a - character
    private int count = 0; // for debugging. Stops solve function after x amount so I can see what my board
    private String type;        // what type of holder for dictionary : DLB is fast
    private boolean solved; // How to stop
    private int countSolutions;
    public CrosswordB(String typ,String dict, String boardz) throws FileNotFoundException {
       // long startTime = System.nanoTime(); //measure time board solving takes
        type = typ;
        if(type.equals("DLB"))
        dictionary = new DLB();
        else if(type.equals( "MyDictionary"))
        dictionary = new MyDictionary();
        solved = false;                         //Initialize solved as false
        Scanner file = new Scanner(new File(dict)); // Load in dictionary
        while (file.hasNext()) {
            dictionary.add(file.nextLine());        //Add words from dict file to myDictionary object
        }
        file.close();
        Scanner scanner = new Scanner(new File(boardz)); // Load in board 
        boardSize = Integer.parseInt(scanner.nextLine()); //Gets boardsize
        rows = new StringBuilder[boardSize]; //Will hold every row of board
        columns = new StringBuilder[boardSize]; //Will hold every column of board
        for (int i = 0; i < boardSize; i++) {  //Initialize StringBuilder
            rows[i] = new StringBuilder();
            columns[i] = new StringBuilder();
        }
        board = new char[boardSize][boardSize]; // This is a reference for what the user input crossword layout is like
        alphabet = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
                'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '-' }; // The alphabet and the - character
        int count = 0; //Initializing count. Again only use is in debugging. can be commented out but I find it easier not to due to how much debugging I had to do
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            char[] characs = line.toCharArray();
            for (int i = 0; i < characs.length; i++) {
                board[count][i] = characs[i];       //filling board array with the info in text file.
            }
            count++; 
        }
        countSolutions = 0;
        solve(0, 0);    //Start at top left of board
       // long endTime = System.nanoTime(); //Use to measure time traversal took.
        if(!type.equals("MyDictionary"))
        System.out.println(countSolutions);
    }

    public int getLetter(char c) {                 //Returns the index at which a letter is in my alphabet array. useful in solve function
        for (int i = 0; i <= 26; i++) {
            if (alphabet[i] == c)
                return i;
        }
        return -1;          //Not found in alphabet
    }

    public void solve(int row, int col) {        
        if (row >= rows.length || col >= columns.length)        //base case for index out of bounds
            return;
        if (board[row][col] != '+') {                       //If letter is predetermined on board
            int n = getLetter(board[row][col]);
            if(isValid(row,col,n)){
                rows[row].append(board[row][col]);  //Add it on!
                columns[col].append(board[row][col]);
                if (row == rows.length - 1 && col == columns.length - 1) {   
                        if(type.equals("MyDictionary")){
                            showBoard();
                            solved = true;              
                            return;
                        }
                        countSolutions++;
                }

              else  if ((col < boardSize - 1) && !solved)       //So if i am not at the last column and my board aint solved, solve the next column
                    solve(row, col + 1);

                else if (col == boardSize - 1 && row != boardSize - 1 && !solved)   //If i'm not in last row but am in last column and not solved, start next row
                    solve(row + 1, 0);

                if (!solved) {                                          //If my board is not solved
                    rows[row].deleteCharAt(rows[row].length() - 1);     //Delete last character added to row (wont be seen unless something doesnt fit)
                    columns[col].deleteCharAt(columns[col].length() - 1); //Delete last character added to column
                }
            }
            
            else
                return;
        }
            
         else
        for (int i = 0; i < 26; i++) {              //If + present on board
            if (isValid(row, col, i) && !solved) { 
                    rows[row].append(alphabet[i]);
                    columns[col].append(alphabet[i]);
                    
                if (row == rows.length - 1 && col == columns.length - 1) {    
                    if(type.equals("MyDictionary")){
                        showBoard();
                        solved = true;              
                        return;
                    }
                    countSolutions++;
                }
               else if ((col < boardSize - 1) && !solved)      
                    solve(row, col + 1);

                else if (col == boardSize - 1 && row != boardSize - 1 && !solved)  
                    solve(row + 1, 0);

                if (!solved) {                                          
                    rows[row].deleteCharAt(rows[row].length() - 1);     
                    columns[col].deleteCharAt(columns[col].length() - 1); 
                }
            }

            if (solved) 
                break;
        }
        return;     
    }

    public boolean isValid(int r, int c, int letter) {   

        if (letter == 26)       //alphabet[26] = - and whenever there is a - you must add it so return true
            return true;

        rows[r].append(alphabet[letter]);
        columns[c].append(alphabet[letter]);
        int rowResult = dictionary.searchPrefix(rows[r],1+rows[r].lastIndexOf("-"),rows[r].length()-1); //is my row being built a prefix/word? same for column below. Note only considers characters that are after any dash present in row or column
        int colResult = dictionary.searchPrefix(columns[c],1+columns[c].lastIndexOf("-"),columns[c].length()-1);
        rows[r].deleteCharAt(rows[r].length()-1);
        columns[c].deleteCharAt(columns[c].length()-1);
       
        if (rowResult==0||colResult==0||r > boardSize|| c> boardSize ) {      
            return false;
        }

         if (((rowResult == 1) || (rowResult == 3)) && (c != (columns.length - 1)&&board[r][c+1]!='-')) {  //If I am a prefix at all and not at last column nor a dash next to me

            if (r != (rows.length - 1)&&board[r+1][c]!='-') {               //if i am not at last row and there is no dash beneath me
                if (((colResult == 1) || (colResult == 3))) //column must be at least prefix too to be valid
                    return true;
                else                //ya not valid.
                    return false;
            } else{       //If i am in last row
                if (((colResult == 2) || (colResult == 3))) //Column must be a complete word to be valid
                    return true;
                else
                    return false;
            }
        } else if (((rowResult == 2) || rowResult == 3) && (c == (columns.length - 1)||board[r][c+1]=='-')) { //Row is word and at last column or dash next to me
            if (r != (rows.length - 1)&&board[r+1][c]!='-') {           //if i am not in last row or column there
                if (((colResult == 1) || (colResult == 3))) //column must be at least prefix to be valid here cuz not at bottom of column yet
                    return true;
                else
                    return false;
            } else {  //At last row or dash there yo
                if (colResult == 2 || (colResult == 3)) //Column must be a word too for it to be valid
                    return true;
                else
                    return false;
            }
        }
        return false;   //Not valid. 
    }
    public void showBoard(){            //Print board out if MyDictionary type
        for (int j = 0; j < boardSize; j++) {
            System.out.println(rows[j]);       
        }
        if(!solved)
        System.out.println();
    }
    public static void main(String[] args) throws FileNotFoundException {
        
        CrosswordB cross = new CrosswordB(args[0],args[1], args[2]); //Dictionary_holder, dictionary file, test_Cross

        
    }
}