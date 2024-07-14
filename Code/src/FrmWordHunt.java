/*

Kayla Sodergard
June 17, 2024

This Culminating Project Program displays a form in which the user is given instruction on how to play the game, and is then
prompted to press start whenever they'd like to start the game. Once started, the a board will be filled with randomly selected
letter tiles, and within the time limit the user must find as many 3+ letter words as possible. The second user will then be
allowed to play and at the end the winner will be decided based on who has accumulated the post points.

 */

//import the necessary java classes
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.ImageIcon;

public class FrmWordHunt extends javax.swing.JFrame {

    JButton[][] boardButtons = new JButton[4][4]; // JButton array initialization to represent the 4x4 board
    final int SIZE = 4; // final integer variable used to store the size of each column and row in the 2D array for easy iteration
    final Color BUTTON_NS = Color.getColor("btnStart.background"); // final Color variable to store the desired lighter grey background colour for a button that hasn't been selected yet
    final Color BUTTON_S = new Color(99, 99, 99).brighter(); // final Color variable to store the desired darker grey background colour for a button that has been selected
    final Color BUTTON_G = new Color(134, 230, 78).brighter(); // final Color variable to store the desired green background colour for when a word has been correctly guessed
    final Color BUTTON_Y = new Color(247, 228, 49).brighter(); // final Color variable to store the desired yellow background colour for when a word has already been guessed
    final ImageIcon P1_IMG = new ImageIcon("src/images/p12.png"); // final ImageIcon variable to store the path to the player 1's icon
    final ImageIcon P2_IMG = new ImageIcon("src/images/p22.png"); // final ImageIcon variable to store the path to the player 2's icon
    HashSet<String> viableWords = new HashSet<>(); // Initialize a String HashSet to store all of the viable word options for this game, to be read in from a text file
    String guess = ""; // String variable to be used to store the user's current string of letters for their word guess
    ArrayList<String> foundWordsP1 = new ArrayList<>(); // Initialize an ArrayList to store all of the words player 1 finds as strings
    ArrayList<String> foundWordsP2 = new ArrayList<>(); // Initialize an ArrayList to store all of the words player 2 finds as strings
    int[] points = {100, 400, 800, 1400, 1800, 2200, 2600, 2800, 3000, 3200, 3400, 3600, 3800, 4500}; // Integer Array used to store all of the possible point value options
    JButton lastButton; // Declare a JButton to keep track of the last interacted with button 
    JButton thisButton; // Declare a JButton to keep track of the current interacted with button
    int p1Points = 0; // Integer variable to store the player 1's total accumulated points
    int p2Points = 0; // Integer variable to store the player 2's total accumulated points
    boolean next = false; // Boolean variable to indicate whether it's time for the next player to have their turn yet (next player being player 2)
    boolean held = false; // Boolean variable used to indicate if a button is currently being pressed, which is one of the requirements to allow the user to start a chain of letters and create their guess

    /**
     * Creates new form FrmWordHunt
     */
    public FrmWordHunt() {
        initComponents();
        generateWordList(); // when the form is first created, generate the HashSet of all the viable words by calling our generateWordList() method
        setResizable(false); // set the window resizability to false as we don't want the users to be able to manipulate it

    }

    /* Initializes the board for the game. It first sets up a predefined array of letters that correspond with the letter's commonness. 
    Then it places the buttons making up the game into a 2d array to allow for easy iteration and manipulation. It also fills the board 
    with 16 randomly selected letters using Math.random() from the array of letters created earlier and updates the button text accordingly,
    also setting the button colour before the game starts*/
    public void setupBoard() {

        //a string of 100 letters representing out of 100, how common each letter is im the english language
        String[] letters = {"N", "O", "S", "B", "S", "F", "S", "T", "N", "V", "A", "O", "N", "A",
            "S", "L", "E", "H", "T", "H", "I", "E", "L", "S", "D", "E", "E", "S", "H", "O", "I", "P",
            "U", "E", "E", "N", "O", "E", "E", "L", "E", "M", "H", "E", "P", "U", "E", "D", "R", "R",
            "N", "G", "R", "A", "R", "A", "O", "R", "T", "D", "C", "N", "I", "B", "A", "S", "W", "K",
            "O", "A", "G", "O", "M", "Z", "O", "W", "E", "I", "E", "H", "L", "D", "Y", "H", "Y",
            "I", "R", "A", "N", "S", "S", "I", "C", "A", "C", "E", "R", "F", "I", "N"};

        //Assign each button to their corresponding location in the 2D array
        boardButtons[0][0] = btnLetterTile1;
        boardButtons[0][1] = btnLetterTile2;
        boardButtons[0][2] = btnLetterTile3;
        boardButtons[0][3] = btnLetterTile4;
        boardButtons[1][0] = btnLetterTile5;
        boardButtons[1][1] = btnLetterTile6;
        boardButtons[1][2] = btnLetterTile7;
        boardButtons[1][3] = btnLetterTile8;
        boardButtons[2][0] = btnLetterTile9;
        boardButtons[2][1] = btnLetterTile10;
        boardButtons[2][2] = btnLetterTile11;
        boardButtons[2][3] = btnLetterTile12;
        boardButtons[3][0] = btnLetterTile13;
        boardButtons[3][1] = btnLetterTile14;
        boardButtons[3][2] = btnLetterTile15;
        boardButtons[3][3] = btnLetterTile16;

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                double randomDub = Math.random();
                randomDub *= 100;
                int randomNum = (int) randomDub;
                System.out.println(randomNum);

                boardButtons[r][c].setText(letters[randomNum]);
                boardButtons[r][c].setBackground(BUTTON_NS);
            }
        }

    }

    /* Read in words from a file named "viableWords.txt" located in the src directory of this project
        and store each line into the viableWords HashSet as strings to allow for easy comparisons - it uses a Scanner to read in the text file line by line.
        If the file is not found, a FileNotFoundException is caught using a try catch and the stack trace 
        is printed for debugging purposes, and once the file has been fully read, the Scanner is closed. */
    public final void generateWordList() {
        try {
            Scanner scanner = new Scanner(new File("src/viableWords.txt"));

            while (scanner.hasNextLine()) {
                viableWords.add(scanner.nextLine());

            }

            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }

    }

    /* Sets up a countdown timer for 90 seconds and updates a label to display the remaining time, formatting it so that the time 
    appears in min:sec instead of pure seconds. It uses a Timer and a TimerTask to handle the countdown logic, updating the timer 
    every second. When the countdown reaches zero, the timer stops and changes the tab in a JTabbedPane to either the next user's turn,
    or the final scoring screeen based on whether the next user (next boolean) needs to go still. If ready for the scoring screen, the
    ArrayList of each user's found words is iterated through to display the top 5 largest words (if possible) and their corresponding
    point values. Then the scores are compared and the winner is displayed, along with a button to play again. */
    public void timer() {

        int totalSeconds = 90;

        Timer timer = new Timer();

        // Create a TimerTask to handle the countdown logic
        TimerTask task = new TimerTask() {
            int secondsRemaining = totalSeconds;

            @Override
            public void run() {
                
                if (secondsRemaining >= 0) { // if statement to keep the timer going if there's still time left
                    int mins = secondsRemaining / 60;
                    int secs = secondsRemaining % 60;
                    lblTimer.setText(String.format("%02d:%02d", mins, secs));
                    System.out.println(guess);
                    secondsRemaining--;
                } else { // else statement to otherwise stop the timer
                    timer.cancel();
                    
                    if (!next) { // if the timer's up but the next user hasn't gone yet
                        gameScreens.setSelectedIndex(0); // change the screen to the starting screen again for the second user to take their turn
                        next = true; // set the boolean next variable to true to indicate that the next use has now had their turn
                    } else { // else statement to run if both user's have done and the timer is up
                        gameScreens.setSelectedIndex(2); // change the screen to the ending screen
                        
                        
                        // create 5 empty strings to store the longest words from player 1 (blue user)
                        String longestString1B = "";
                        String longestString2B = "";
                        String longestString3B = "";
                        String longestString4B = "";
                        String longestString5B = "";

                        
                        // iterate through the ArrayList of found words for player 1 to find the largest words
                        for (String s : foundWordsP1) {
                            if (s.length() > longestString1B.length()) {
                                longestString5B = longestString4B;
                                longestString4B = longestString3B;
                                longestString3B = longestString2B;
                                longestString2B = longestString1B;
                                longestString1B = s;
                                
                            } else if (s.length() > longestString2B.length()) {
                                longestString5B = longestString4B;
                                longestString4B = longestString3B;
                                longestString3B = longestString2B;
                                longestString2B = s;
                                
                            } else if (s.length() > longestString3B.length()) {
                                longestString5B = longestString4B;
                                longestString4B = longestString3B;
                                longestString3B = s;
                                
                            } else if (s.length() > longestString4B.length()) {
                                longestString5B = longestString4B;
                                longestString4B = s;
                                
                            } else if (s.length() > longestString5B.length()) {
                                longestString5B = s;
                                
                            }
                        }

                        // display the 5 largest words
                        lblBlueW1.setText(longestString1B);
                        lblBlueW2.setText(longestString2B);
                        lblBlueW3.setText(longestString3B);
                        lblBlueW4.setText(longestString4B);
                        lblBlueW5.setText(longestString5B);

                        // if there actually was a word saved in a top 5 slot, display the corresponding score attached to it
                        if (!lblBlueW1.getText().equals("")) {
                            lblBlueS1.setText(String.valueOf(points[longestString1B.length() - 3]));
                        }

                        if (!lblBlueW2.getText().equals("")) {
                            lblBlueS2.setText(String.valueOf(points[longestString2B.length() - 3]));
                        }

                        if (!lblBlueW3.getText().equals("")) {
                            lblBlueS3.setText(String.valueOf(points[longestString3B.length() - 3]));
                        }

                        if (!lblBlueW4.getText().equals("")) {
                            lblBlueS4.setText(String.valueOf(points[longestString4B.length() - 3]));
                        }

                        if (!lblBlueW5.getText().equals("")) {
                            lblBlueS5.setText(String.valueOf(points[longestString5B.length() - 3]));
                        }

                        // repeat for player 2 (red player)
                        String longestString1R = "";
                        String longestString2R = "";
                        String longestString3R = "";
                        String longestString4R = "";
                        String longestString5R = "";

                        for (String s : foundWordsP2) {
                            if (s.length() > longestString1R.length()) {
                                longestString5R = longestString4R;
                                longestString4R = longestString3R;
                                longestString3R = longestString2R;
                                longestString2R = longestString1R;
                                longestString1R = s;
                            } else if (s.length() > longestString2R.length()) {
                                longestString5R = longestString4R;
                                longestString4R = longestString3R;
                                longestString3R = longestString2R;
                                longestString2R = s;
                            } else if (s.length() > longestString3R.length()) {
                                longestString5R = longestString4R;
                                longestString4R = longestString3R;
                                longestString3R = s;
                            } else if (s.length() > longestString4R.length()) {
                                longestString5R = longestString4R;
                                longestString4R = s;
                            } else if (s.length() > longestString5R.length()) {
                                longestString5R = s;
                            }
                        }

                        lblRedW1.setText(longestString1R);
                        lblRedW2.setText(longestString2R);
                        lblRedW3.setText(longestString3R);
                        lblRedW4.setText(longestString4R);
                        lblRedW5.setText(longestString5R);

                        if (!lblRedW1.getText().equals("")) {
                            lblRedS1.setText(String.valueOf(points[longestString1R.length() - 3]));
                        }

                        if (!lblRedW2.getText().equals("")) {
                            lblRedS2.setText(String.valueOf(points[longestString2R.length() - 3]));
                        }

                        if (!lblRedW3.getText().equals("")) {
                            lblRedS3.setText(String.valueOf(points[longestString3R.length() - 3]));
                        }

                        if (!lblRedW4.getText().equals("")) {
                            lblRedS4.setText(String.valueOf(points[longestString4R.length() - 3]));
                        }

                        if (!lblRedW5.getText().equals("")) {
                            lblRedS5.setText(String.valueOf(points[longestString5R.length() - 3]));
                        }

                        
                        if (p1Points > p2Points) { // if statement to check if player 1 scored more points
                            lblBlueWin.setVisible(true); // display a crown for the blue user (p1) as they just won
                            lblRedWin.setVisible(false);
                            lblWinner.setText("PLAYER 1 WON!!!"); // display a message for the winner

                        } else if (p2Points > p1Points) { // else if statement to check if player 2 scored more points
                            lblBlueWin.setVisible(false);
                            lblRedWin.setVisible(true); // display a crown for the blue user (p1) as they just won
                            lblWinner.setText("PLAYER 2 WON!!!"); // display a message for the winner

                        } else { // else statement for if they otherwise tied
                            // display a crown for the both as they both won
                            lblBlueWin.setVisible(true);
                            lblRedWin.setVisible(true);
                            lblWinner.setText("ITS A TIE!!!"); // display a message for both users

                        }

                    }

                }
            }
        };

        // Schedule the TimerTask to run every 1 second
        timer.schedule(task, 0, 1000);

    }

    
    /* Determines if the current button being interacted with is adjacent to the last button interacted with on the board.
    The buttons are considered adjacent if they are directly above, below, to the left, to the right, or diagonally adjacent 
    to each other. It first identifies the row and column positions of both buttons on the board, and then calculates the absolute differences 
    in their row and column positions to see if they're adjacent at all. This method will return true if in any circumstance an adjacency is found,
    allowing the main program and other methods to limit movement as seen fit by the rules of the game. */
    public boolean isNear() {
        int btnColN = 0, btnRowN = 0, btnColB = 0, btnRowB = 0;
        
        // iterate through the 2D board array to find the positions of thisButton and lastButton so we can compare them and figure out if they're adjacent
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (boardButtons[r][c] == thisButton) {
                    btnColN = c;
                    btnRowN = r;
                    System.out.println("thisButton found at: (" + btnRowN + ", " + btnColN + ")");
                }
                if (boardButtons[r][c] == lastButton) {
                    btnRowB = r;
                    btnColB = c;
                    System.out.println("lastButton found at: (" + btnRowB + ", " + btnColB + ")");

                }

            }

        }

        //calculate the absolute difference in the row and column positions for the buttons
        int rowDiff = Math.abs(btnRowN - btnRowB);
        int colDiff = Math.abs(btnColN - btnColB);

       // return true if the buttons are adjacent
       return (rowDiff == 1 && colDiff == 0) || // directly above or below
              (rowDiff == 0 && colDiff == 1) || // directly across
              (rowDiff == 1 && colDiff == 1);   // diagonal

    }

    
    /* Method to check if the current string of letters (the guessed word) is valid or not by checking if it can be found in the viableWords HashSet of possible Strings. 
    If the word is in viableWords and hasn't been found before (isn't in the ArrayList of previously found words), the word gets added to one of 2 ArrayLists of found words
    dependent on which user is currently playing, the score gets updated based on how long the string is, the final screen holding the score and number of words is updated 
    dependent on the user, and a visual indicator is updated with the colour green to indicate a new word has been found. If the word has already been found, a visual indicator 
    is updated with the colour yellow to indicate that the word has already been found and the user should try again. And otherwise if the word is invalid, a visual indicator is 
    updated with the colour grey to tell the user that their selection was not in fact a word. */
    public void checkWord() {
        String lowerGuess = guess.toLowerCase(); // converts the string holding the tile the user has selected for their guess to all lowercase as the HashSet is case sensitive
        if (viableWords.contains(lowerGuess)) { // if statement to check if the guessed word is in the list of viable words
            if (!next) { // if it's player 1's turn
                if (!foundWordsP1.contains(lowerGuess)) {
                    foundWordsP1.add(lowerGuess);
                    
                    lblWords.setText(String.valueOf(foundWordsP1.size()));
                    
                    /*the smallest word is 3 letters long so instead of using the length to directly find the score, we shift it 
                    until the lowest letter count matches up with the lowest score*/
                    p1Points += points[guess.length() - 3];
                    
                    lblPoints.setText(String.valueOf(p1Points));
                    lblBlueScore.setText(String.valueOf(p1Points));
                    lblBlueWords.setText(String.valueOf(foundWordsP1.size()));
                    panWIndicator.setBackground(BUTTON_G);
                    lblWordInfo.setText("New Word!");

                } else {
                    panWIndicator.setBackground(BUTTON_Y);
                    lblWordInfo.setText("Old Word!");
                }

            } else { // if it's player 2's turn
                if (!foundWordsP2.contains(lowerGuess)) {
                    foundWordsP2.add(lowerGuess);
                    
                    lblWords.setText(String.valueOf(foundWordsP2.size()));

                    p2Points += points[guess.length() - 3];
                    
                    lblPoints.setText(String.valueOf(p2Points));
                    lblRedScore.setText(String.valueOf(p2Points));
                    lblRedWords.setText(String.valueOf(foundWordsP2.size()));
                    panWIndicator.setBackground(BUTTON_G);
                    lblWordInfo.setText("New Word!");

                } else {

                    panWIndicator.setBackground(BUTTON_Y);
                    lblWordInfo.setText("Old Word!");
                }
            }
        } else {
            panWIndicator.setBackground(BUTTON_S);
            lblWordInfo.setText("Not a Word :(");

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        gameScreens = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnStart = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblPoints = new javax.swing.JLabel();
        lblWords = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        imgPlayer = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        lblTimer = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        btnLetterTile16 = new javax.swing.JButton();
        btnLetterTile13 = new javax.swing.JButton();
        btnLetterTile14 = new javax.swing.JButton();
        btnLetterTile15 = new javax.swing.JButton();
        btnLetterTile1 = new javax.swing.JButton();
        btnLetterTile2 = new javax.swing.JButton();
        btnLetterTile3 = new javax.swing.JButton();
        btnLetterTile4 = new javax.swing.JButton();
        btnLetterTile5 = new javax.swing.JButton();
        btnLetterTile6 = new javax.swing.JButton();
        btnLetterTile7 = new javax.swing.JButton();
        btnLetterTile8 = new javax.swing.JButton();
        btnLetterTile12 = new javax.swing.JButton();
        btnLetterTile11 = new javax.swing.JButton();
        btnLetterTile10 = new javax.swing.JButton();
        btnLetterTile9 = new javax.swing.JButton();
        panWIndicator = new javax.swing.JPanel();
        lblWordInfo = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        lblRedScore = new javax.swing.JLabel();
        lblRedWords = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        lblBlueScore = new javax.swing.JLabel();
        lblBlueWords = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        lblRedW1 = new javax.swing.JLabel();
        lblRedW2 = new javax.swing.JLabel();
        lblRedW3 = new javax.swing.JLabel();
        lblRedS3 = new javax.swing.JLabel();
        lblRedS1 = new javax.swing.JLabel();
        lblRedS2 = new javax.swing.JLabel();
        lblRedW4 = new javax.swing.JLabel();
        lblRedS4 = new javax.swing.JLabel();
        lblRedS5 = new javax.swing.JLabel();
        lblRedW5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        lblBlueW1 = new javax.swing.JLabel();
        lblBlueW2 = new javax.swing.JLabel();
        lblBlueW3 = new javax.swing.JLabel();
        lblBlueS1 = new javax.swing.JLabel();
        lblBlueS2 = new javax.swing.JLabel();
        lblBlueS3 = new javax.swing.JLabel();
        lblBlueW4 = new javax.swing.JLabel();
        lblBlueS4 = new javax.swing.JLabel();
        lblBlueW5 = new javax.swing.JLabel();
        lblBlueS5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblBlueWin = new javax.swing.JLabel();
        btnPlayAgain = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        lblWinner = new javax.swing.JLabel();
        lblRedWin = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jLabel6.setText("jLabel6");

        jRadioButton1.setText("jRadioButton1");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(74, 165, 97));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("How to play:");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Connect letters together by clicking and dragging");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("across the letter tiles. Make as many words as you can.");

        btnStart.setBackground(new java.awt.Color(204, 204, 204));
        btnStart.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnStart.setText("Start!");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/introPhoto.png"))); // NOI18N
        jLabel20.setText("jLabel20");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Tips: Use a mouse, go back a tile if stuck & have patience!!");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(123, 123, 123)
                        .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(104, 104, 104))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addGap(8, 8, 8)
                .addComponent(jLabel2)
                .addGap(4, 4, 4)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(btnStart)
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(97, Short.MAX_VALUE))
        );

        gameScreens.addTab("tab1", jPanel1);

        jPanel4.setBackground(new java.awt.Color(74, 165, 97));
        jPanel4.setToolTipText("");
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("WORDS:");

        lblPoints.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblPoints.setText("0000");

        lblWords.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblWords.setText("00");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel8.setText("SCORE:");

        imgPlayer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/p12.png"))); // NOI18N
        imgPlayer.setText("jLabel9");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(imgPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblWords, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPoints, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblWords)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(lblPoints))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(imgPlayer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, 290, 60));

        jPanel7.setBackground(new java.awt.Color(49, 132, 69));

        lblTimer.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTimer.setForeground(new java.awt.Color(255, 255, 255));
        lblTimer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTimer.setText("01:30");
        lblTimer.setPreferredSize(new java.awt.Dimension(28, 18));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTimer, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(lblTimer, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 60, 80, -1));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnLetterTile16.setText("jButton20");
        btnLetterTile16.setFocusable(false);
        btnLetterTile16.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                btnLetterTile16MouseDragged(evt);
            }
        });
        btnLetterTile16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLetterTile16MouseEntered(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnLetterTile16MouseReleased(evt);
            }
        });
        jPanel8.add(btnLetterTile16, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 220, 70, 60));

        btnLetterTile13.setText("jButton20");
        btnLetterTile13.setFocusable(false);
        btnLetterTile13.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                btnLetterTile13MouseDragged(evt);
            }
        });
        btnLetterTile13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLetterTile13MouseEntered(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnLetterTile13MouseReleased(evt);
            }
        });
        jPanel8.add(btnLetterTile13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 70, 60));

        btnLetterTile14.setText("jButton20");
        btnLetterTile14.setFocusable(false);
        btnLetterTile14.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                btnLetterTile14MouseDragged(evt);
            }
        });
        btnLetterTile14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLetterTile14MouseEntered(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnLetterTile14MouseReleased(evt);
            }
        });
        jPanel8.add(btnLetterTile14, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 220, 70, 60));

        btnLetterTile15.setText("jButton20");
        btnLetterTile15.setFocusable(false);
        btnLetterTile15.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                btnLetterTile15MouseDragged(evt);
            }
        });
        btnLetterTile15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLetterTile15MouseEntered(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnLetterTile15MouseReleased(evt);
            }
        });
        jPanel8.add(btnLetterTile15, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 220, 70, 60));

        btnLetterTile1.setText("jButton20");
        btnLetterTile1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnLetterTile1.setDefaultCapable(false);
        btnLetterTile1.setFocusable(false);
        btnLetterTile1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                btnLetterTile1MouseDragged(evt);
            }
        });
        btnLetterTile1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLetterTile1MouseEntered(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnLetterTile1MouseReleased(evt);
            }
        });
        jPanel8.add(btnLetterTile1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 70, 60));

        btnLetterTile2.setText("jButton20");
        btnLetterTile2.setFocusable(false);
        btnLetterTile2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                btnLetterTile2MouseDragged(evt);
            }
        });
        btnLetterTile2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLetterTile2MouseEntered(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnLetterTile2MouseReleased(evt);
            }
        });
        btnLetterTile2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLetterTile2ActionPerformed(evt);
            }
        });
        jPanel8.add(btnLetterTile2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, 70, 60));

        btnLetterTile3.setText("jButton20");
        btnLetterTile3.setFocusable(false);
        btnLetterTile3.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                btnLetterTile3MouseDragged(evt);
            }
        });
        btnLetterTile3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLetterTile3MouseEntered(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnLetterTile3MouseReleased(evt);
            }
        });
        jPanel8.add(btnLetterTile3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 70, 60));

        btnLetterTile4.setText("jButton20");
        btnLetterTile4.setFocusable(false);
        btnLetterTile4.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                btnLetterTile4MouseDragged(evt);
            }
        });
        btnLetterTile4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLetterTile4MouseEntered(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnLetterTile4MouseReleased(evt);
            }
        });
        jPanel8.add(btnLetterTile4, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 70, 60));

        btnLetterTile5.setText("jButton20");
        btnLetterTile5.setFocusable(false);
        btnLetterTile5.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                btnLetterTile5MouseDragged(evt);
            }
        });
        btnLetterTile5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLetterTile5MouseEntered(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnLetterTile5MouseReleased(evt);
            }
        });
        jPanel8.add(btnLetterTile5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 70, 60));

        btnLetterTile6.setText("jButton20");
        btnLetterTile6.setFocusable(false);
        btnLetterTile6.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                btnLetterTile6MouseDragged(evt);
            }
        });
        btnLetterTile6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLetterTile6MouseEntered(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnLetterTile6MouseReleased(evt);
            }
        });
        jPanel8.add(btnLetterTile6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 70, 60));

        btnLetterTile7.setText("jButton20");
        btnLetterTile7.setFocusable(false);
        btnLetterTile7.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                btnLetterTile7MouseDragged(evt);
            }
        });
        btnLetterTile7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLetterTile7MouseEntered(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnLetterTile7MouseReleased(evt);
            }
        });
        jPanel8.add(btnLetterTile7, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 70, 60));

        btnLetterTile8.setText("jButton20");
        btnLetterTile8.setFocusable(false);
        btnLetterTile8.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                btnLetterTile8MouseDragged(evt);
            }
        });
        btnLetterTile8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLetterTile8MouseEntered(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnLetterTile8MouseReleased(evt);
            }
        });
        jPanel8.add(btnLetterTile8, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 80, 70, 60));

        btnLetterTile12.setText("jButton20");
        btnLetterTile12.setFocusable(false);
        btnLetterTile12.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                btnLetterTile12MouseDragged(evt);
            }
        });
        btnLetterTile12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLetterTile12MouseEntered(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnLetterTile12MouseReleased(evt);
            }
        });
        jPanel8.add(btnLetterTile12, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 150, 70, 60));

        btnLetterTile11.setText("jButton20");
        btnLetterTile11.setFocusable(false);
        btnLetterTile11.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                btnLetterTile11MouseDragged(evt);
            }
        });
        btnLetterTile11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLetterTile11MouseEntered(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnLetterTile11MouseReleased(evt);
            }
        });
        jPanel8.add(btnLetterTile11, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 150, 70, 60));

        btnLetterTile10.setText("jButton20");
        btnLetterTile10.setFocusable(false);
        btnLetterTile10.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                btnLetterTile10MouseDragged(evt);
            }
        });
        btnLetterTile10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLetterTile10MouseEntered(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnLetterTile10MouseReleased(evt);
            }
        });
        jPanel8.add(btnLetterTile10, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 150, 70, 60));

        btnLetterTile9.setText("jButton20");
        btnLetterTile9.setFocusable(false);
        btnLetterTile9.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                btnLetterTile9MouseDragged(evt);
            }
        });
        btnLetterTile9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLetterTile9MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLetterTile9MouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnLetterTile9MouseReleased(evt);
            }
        });
        jPanel8.add(btnLetterTile9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 70, 60));

        jPanel4.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 140, 330, 290));

        lblWordInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panWIndicatorLayout = new javax.swing.GroupLayout(panWIndicator);
        panWIndicator.setLayout(panWIndicatorLayout);
        panWIndicatorLayout.setHorizontalGroup(
            panWIndicatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panWIndicatorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWordInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                .addContainerGap())
        );
        panWIndicatorLayout.setVerticalGroup(
            panWIndicatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panWIndicatorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWordInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(panWIndicator, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, -1, 30));

        gameScreens.addTab("tab2", jPanel4);

        jPanel12.setBackground(new java.awt.Color(74, 165, 97));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/p12.png"))); // NOI18N
        jLabel11.setText("jLabel11");
        jPanel12.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 77, -1));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/p22.png"))); // NOI18N
        jPanel12.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 70, -1, -1));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("WORDS:");

        lblRedScore.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblRedScore.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRedScore.setText("0000");

        lblRedWords.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblRedWords.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRedWords.setText("00");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel19.setText("SCORE:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblRedScore, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblRedWords, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRedWords))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRedScore)
                    .addComponent(jLabel19))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel12.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 140, 190, 60));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel12.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 240, -1, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("WORDS:");

        lblBlueScore.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblBlueScore.setText("0000");

        lblBlueWords.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblBlueWords.setText("00");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel15.setText("SCORE:");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblBlueScore))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblBlueWords, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBlueWords)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(lblBlueScore))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel12.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 190, 60));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        lblRedW1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblRedW1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRedW1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        lblRedW2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblRedW2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRedW2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        lblRedW3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblRedW3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRedW3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        lblRedS3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        lblRedS1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        lblRedS2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        lblRedW4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblRedW4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRedW4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        lblRedS4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        lblRedS5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        lblRedW5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblRedW5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRedW5.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel9.setText("Your top 5 words");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblRedS3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRedS1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRedS2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblRedW3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRedW1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRedW2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(lblRedS4, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblRedW4, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(lblRedS5, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblRedW5, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(49, 49, 49))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRedW1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRedS1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRedW2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRedS2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRedW3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRedS3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRedW4)
                    .addComponent(lblRedS4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRedS5)
                    .addComponent(lblRedW5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addContainerGap())
        );

        jPanel12.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 210, 190, 220));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel12.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 180, -1, -1));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel12.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 210, -1, -1));

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel12.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 210, -1, -1));

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel12.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 320, -1, -1));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel12.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 190, -1, -1));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel12.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 230, -1, -1));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel12.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 200, -1, -1));

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));

        lblBlueW1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        lblBlueW2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        lblBlueW3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        lblBlueS1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblBlueS1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBlueS1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        lblBlueS2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblBlueS2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBlueS2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        lblBlueS3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblBlueS3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBlueS3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        lblBlueW4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        lblBlueS4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblBlueS4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBlueS4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        lblBlueW5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        lblBlueS5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblBlueS5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBlueS5.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel7.setText("Your top 5 words");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblBlueW2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblBlueW1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblBlueW3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblBlueS1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblBlueS3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblBlueS2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(lblBlueW4, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblBlueS4, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(lblBlueW5, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblBlueS5, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBlueW1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBlueS1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBlueW2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBlueS2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBlueW3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBlueS3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBlueW4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBlueS4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBlueW5, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBlueS5, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addContainerGap())
        );

        jPanel12.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 190, 220));

        lblBlueWin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/winnerCrown.png"))); // NOI18N
        lblBlueWin.setText("jLabel37");
        jPanel12.add(lblBlueWin, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, 70, -1));

        btnPlayAgain.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnPlayAgain.setText("Play Again?");
        btnPlayAgain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayAgainActionPerformed(evt);
            }
        });
        jPanel12.add(btnPlayAgain, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 450, -1, -1));

        jPanel21.setBackground(new java.awt.Color(255, 255, 102));

        lblWinner.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblWinner.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWinner, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWinner, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel12.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 150, 40));

        lblRedWin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/winnerCrown.png"))); // NOI18N
        lblRedWin.setText("jLabel37");
        jPanel12.add(lblRedWin, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 60, 70, -1));

        gameScreens.addTab("tab3", jPanel12);

        getContentPane().add(gameScreens, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -30, -1, 530));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /* Called when the start button is pressed */
    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        gameScreens.setSelectedIndex(1); // Change the current screen to the index holding the main components of the game
        lblWords.setText("00"); // reset the label storing the number of words found incase it has prior data
        lblPoints.setText("0000"); // reset the label storing the current user's score incase it has prior data
        lblWordInfo.setText(""); // reset the label storing what type of word has been found incase it has prior data
        panWIndicator.setBackground(BUTTON_S); // set the colour of the indicator to grey as it is the most neutral while the program waits for the user's first input
        if (!next) { // if statement to see if the first player still needs to play
            setupBoard(); // setup the board for this round of the game
            imgPlayer.setIcon(P1_IMG); // set the player icon to that of the first player as they are going first
            System.out.println("hello");
        } else { //  otherwise it's time for the second user to play, so we don't need to regenerate the board
            imgPlayer.setIcon(P2_IMG); // set the player icon to that of the second player as it is their turn
        }
        timer(); // Start the timer as the game has now started

    }//GEN-LAST:event_btnStartActionPerformed


    private void btnLetterTile2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLetterTile2ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_btnLetterTile2ActionPerformed

    boolean hasHovered1 = false; // boolean variable storing whether this button has been hovered over before
    
    // runs when the user's mouse has touched/hovered over the button
    private void btnLetterTile1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile1MouseEntered
        
        thisButton = btnLetterTile1; // as this button is the latest to be interacted with, set the current button to this button (btnLetterTile1)
        
        /* if statement that checks if the button hasn't been interacted/hovered over before, if a button is currently being held down, if the last 
        button before this is greyed out indicating a selection, and if this button is adjacent to the prior one*/
        if (!hasHovered1 && held && lastButton.getBackground() == BUTTON_S && isNear()) {
            
            //if all of the tests pass, then the button accepts the user's interaction and indicates their selection by:
            btnLetterTile1.setBackground(BUTTON_S); // setting the background of the button to a darker grey to indicate a selection
            hasHovered1 = true; // setting the hasHovered1 boolean to true so it can't be interacted with further
            guess += btnLetterTile1.getText(); // gets the letter of the current tile/button and adds it to the user's current guess string for later comparison
            lastButton = thisButton; // making this button the last button now so the chain can continue
        } else { // otherwise if any of the tests don't pass then the interaction doesn't follow through
            hasHovered1 = false;
            
        }

    }//GEN-LAST:event_btnLetterTile1MouseEntered

    /* called when the play again button is pressed to restart the game*/
    private void btnPlayAgainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayAgainActionPerformed
        
        next = false; // set the next boolean variable to false so the first user can start the game again
        
        //reset all of the textboxes and UI elements storing the users' data from the paste game, doing the same for any variables that need to be cleared before the next game starts
        foundWordsP1.clear();
        foundWordsP2.clear();
        lblBlueWin.setVisible(false); // hide the winners crown as the game has reset
        lblRedWin.setVisible(false); // hide the winners crown as the game has reset

        lblBlueW1.setText("");
        lblBlueW2.setText("");
        lblBlueW3.setText("");
        lblBlueW4.setText("");
        lblBlueW5.setText("");
        lblBlueS1.setText("");
        lblBlueS2.setText("");
        lblBlueS3.setText("");
        lblBlueS4.setText("");
        lblBlueS5.setText("");

        lblRedW1.setText("");
        lblRedW2.setText("");
        lblRedW3.setText("");
        lblRedW4.setText("");
        lblRedW5.setText("");
        lblRedS1.setText("");
        lblRedS2.setText("");
        lblRedS3.setText("");
        lblRedS4.setText("");
        lblRedS5.setText("");

        // reset total points to 0 for both users
        p1Points = 0;
        p2Points = 0;
        
        // set the screen to the starting screen
        gameScreens.setSelectedIndex(0);

    }//GEN-LAST:event_btnPlayAgainActionPerformed

    // same as the previous Mouse Entered Button
    boolean hasHovered2 = false;
    private void btnLetterTile2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile2MouseEntered
        thisButton = btnLetterTile2;
        if (!hasHovered2 && held && lastButton.getBackground() == BUTTON_S && isNear()) {
            btnLetterTile2.setBackground(BUTTON_S);
            hasHovered2 = true;
            guess += btnLetterTile2.getText();
            lastButton = thisButton;
        } else {
            hasHovered2 = false;
        }
    }//GEN-LAST:event_btnLetterTile2MouseEntered

    // same as the previous Mouse Entered Button
    boolean hasHovered3 = false;
    private void btnLetterTile3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile3MouseEntered
        // TODO add your handling code here:
        thisButton = btnLetterTile3;
        if (!hasHovered3 && held && lastButton.getBackground() == BUTTON_S && isNear()) {
            btnLetterTile3.setBackground(BUTTON_S);
            hasHovered3 = true;
            guess += btnLetterTile3.getText();
            lastButton = thisButton;
        } else {
            //btnLetterTile3.setBackground(BUTTON_NS);
            hasHovered3 = false;
        }
    }//GEN-LAST:event_btnLetterTile3MouseEntered

    // same as the previous Mouse Entered Button
    boolean hasHovered5 = false;
    private void btnLetterTile5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile5MouseEntered
        // TODO add your handling code here:
        thisButton = btnLetterTile5;

        if (!hasHovered5 && held && lastButton.getBackground() == BUTTON_S && isNear()) {
            btnLetterTile5.setBackground(BUTTON_S);
            hasHovered5 = true;
            guess += btnLetterTile5.getText();
            lastButton = thisButton;
        } else {
            //btnLetterTile5.setBackground(BUTTON_NS);
            hasHovered5 = false;
        }
    }//GEN-LAST:event_btnLetterTile5MouseEntered

    // same as the previous Mouse Entered Button
    boolean hasHovered6 = false;
    private void btnLetterTile6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile6MouseEntered
        // TODO add your handling code here:
        thisButton = btnLetterTile6;
        if (!hasHovered6 && held && lastButton.getBackground() == BUTTON_S && isNear()) {
            btnLetterTile6.setBackground(BUTTON_S);
            hasHovered6 = true;
            guess += btnLetterTile6.getText();
            lastButton = thisButton;
        } else {
            //btnLetterTile6.setBackground(BUTTON_NS);
            hasHovered6 = false;
        }
    }//GEN-LAST:event_btnLetterTile6MouseEntered

    //runs when the user clicks and drags their mouse initially over the button
    private void btnLetterTile1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile1MouseDragged
        if (!held) { // if statement to check if there isn't a button currently being held as this is needed to allow for the user to select tiles and create words
            hasHovered1 = true; // set the hasHovered boolean for this button to true as this serves as the start of the letter chain as well as taking over the button hover event for this button so we don't reselect the same button without purposefully meaning to
            guess += btnLetterTile1.getText(); // gets the letter of the current tile/button and adds it to the user's current guess string for later comparison
            lastButton = btnLetterTile1; // making this button the last button now so the chain can continue so long as the mouse button remains pressed
            held = true; // set held to true to again allow the chain to continue
            btnLetterTile1.setBackground(BUTTON_S); // setting the background of the button to a darker grey to indicate a selection
        }

    }//GEN-LAST:event_btnLetterTile1MouseDragged

    //runs when the user lets go of their mouse button
    private void btnLetterTile1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile1MouseReleased
        held = false; // set held to false to disrupt the chain
        checkWord(); // now that the chain is over, it signifies a completed word so now we call the checkWord() method to see if the user's guess was viable
        for (int r = 0; r < SIZE; r++) { // for loop to iterate through the 2D array and reset the background of each button to indicate a reset and deselection
            for (int c = 0; c < SIZE; c++) {
                boardButtons[r][c].setBackground(BUTTON_NS);
            }
        }
        guess = ""; // reset the user's guess to nothing initially to allow for further guesses as a guess is only valid as long as the mouse button is being pressed
    }//GEN-LAST:event_btnLetterTile1MouseReleased

    // same as the previous Mouse Entered Button
    boolean hasHovered4 = false;
    private void btnLetterTile4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile4MouseEntered
        thisButton = btnLetterTile4;
        if (!hasHovered4 && held && lastButton.getBackground() == BUTTON_S && isNear()) {
            btnLetterTile4.setBackground(BUTTON_S);
            hasHovered4 = true;
            guess += btnLetterTile4.getText();
            lastButton = thisButton;
        } else {
            hasHovered4 = false;
        }
    }//GEN-LAST:event_btnLetterTile4MouseEntered

    // same as the previous Mouse Entered Button
    boolean hasHovered7 = false;
    private void btnLetterTile7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile7MouseEntered
        // TODO add your handling code here:
        thisButton = btnLetterTile7;
        if (!hasHovered7 && held && lastButton.getBackground() == BUTTON_S && isNear()) {
            btnLetterTile7.setBackground(BUTTON_S);
            hasHovered7 = true;
            guess += btnLetterTile7.getText();
            lastButton = thisButton;
        } else {
            //btnLetterTile7.setBackground(BUTTON_NS);
            hasHovered7 = false;
        }
    }//GEN-LAST:event_btnLetterTile7MouseEntered

    // same as the previous Mouse Entered Button
    boolean hasHovered8 = false;
    private void btnLetterTile8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile8MouseEntered
        // TODO add your handling code here:
        thisButton = btnLetterTile8;
        if (!hasHovered8 && held && lastButton.getBackground() == BUTTON_S && isNear()) {
            btnLetterTile8.setBackground(BUTTON_S);
            hasHovered8 = true;
            guess += btnLetterTile8.getText();
            lastButton = thisButton;
        } else {
            //btnLetterTile8.setBackground(BUTTON_NS);
            hasHovered8 = false;
        }
    }//GEN-LAST:event_btnLetterTile8MouseEntered

    private void btnLetterTile9MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile9MouseExited

        // TODO add your handling code here:

    }//GEN-LAST:event_btnLetterTile9MouseExited

    // same as the previous Mouse Entered Button
    boolean hasHovered10 = false;
    private void btnLetterTile10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile10MouseEntered
        // TODO add your handling code here:
        thisButton = btnLetterTile10;
        if (!hasHovered10 && held && lastButton.getBackground() == BUTTON_S && isNear()) {
            btnLetterTile10.setBackground(BUTTON_S);
            hasHovered10 = true;
            guess += btnLetterTile10.getText();
            lastButton = thisButton;
        } else {
            //btnLetterTile10.setBackground(BUTTON_NS);
            hasHovered10 = false;
        }
    }//GEN-LAST:event_btnLetterTile10MouseEntered

    // same as the previous Mouse Entered Button
    boolean hasHovered11 = false;
    private void btnLetterTile11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile11MouseEntered
        // TODO add your handling code here:
        thisButton = btnLetterTile11;
        if (!hasHovered11 && held && lastButton.getBackground() == BUTTON_S && isNear()) {
            btnLetterTile11.setBackground(BUTTON_S);
            hasHovered11 = true;
            guess += btnLetterTile11.getText();
            lastButton = thisButton;
        } else {
            //btnLetterTile11.setBackground(BUTTON_NS);
            hasHovered11 = false;
        }
    }//GEN-LAST:event_btnLetterTile11MouseEntered

    // same as the previous Mouse Entered Button
    boolean hasHovered12 = false;
    private void btnLetterTile12MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile12MouseEntered
        // TODO add your handling code here:
        thisButton = btnLetterTile12;
        if (!hasHovered12 && held && lastButton.getBackground() == BUTTON_S && isNear()) {
            btnLetterTile12.setBackground(BUTTON_S);
            hasHovered12 = true;
            guess += btnLetterTile12.getText();
            lastButton = thisButton;
        } else {
            // btnLetterTile12.setBackground(BUTTON_NS);
            hasHovered12 = false;
        }
    }//GEN-LAST:event_btnLetterTile12MouseEntered

    // same as the previous Mouse Entered Button
    boolean hasHovered13 = false;
    private void btnLetterTile13MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile13MouseEntered
        // TODO add your handling code here:
        thisButton = btnLetterTile13;
        if (!hasHovered13 && held && lastButton.getBackground() == BUTTON_S && isNear()) {
            btnLetterTile13.setBackground(BUTTON_S);
            hasHovered13 = true;
            guess += btnLetterTile13.getText();
            lastButton = thisButton;
        } else {
            //btnLetterTile13.setBackground(BUTTON_NS);
            hasHovered13 = false;
        }
    }//GEN-LAST:event_btnLetterTile13MouseEntered

    // same as the previous Mouse Entered Button
    boolean hasHovered14 = false;
    private void btnLetterTile14MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile14MouseEntered
        // TODO add your handling code here:
        thisButton = btnLetterTile14;
        if (!hasHovered14 && held && lastButton.getBackground() == BUTTON_S && isNear()) {
            btnLetterTile14.setBackground(BUTTON_S);
            hasHovered14 = true;
            guess += btnLetterTile14.getText();
            lastButton = thisButton;
        } else {
            //btnLetterTile14.setBackground(BUTTON_NS);
            hasHovered14 = false;
        }
    }//GEN-LAST:event_btnLetterTile14MouseEntered

    // same as the previous Mouse Entered Button
    boolean hasHovered15 = false;
    private void btnLetterTile15MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile15MouseEntered
        // TODO add your handling code here:
        thisButton = btnLetterTile15;
        if (!hasHovered15 && held && lastButton.getBackground() == BUTTON_S && isNear()) {
            btnLetterTile15.setBackground(BUTTON_S);
            hasHovered15 = true;
            guess += btnLetterTile15.getText();
            lastButton = thisButton;
        } else {
            //btnLetterTile15.setBackground(BUTTON_NS);
            hasHovered15 = false;
        }
    }//GEN-LAST:event_btnLetterTile15MouseEntered

    // same as the previous Mouse Entered Button
    boolean hasHovered16 = false;
    private void btnLetterTile16MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile16MouseEntered
        // TODO add your handling code here:
        thisButton = btnLetterTile16;
        if (!hasHovered16 && held && lastButton.getBackground() == BUTTON_S && isNear()) {
            btnLetterTile16.setBackground(BUTTON_S);
            hasHovered16 = true;
            guess += btnLetterTile16.getText();
            lastButton = thisButton;
        } else {
            //btnLetterTile16.setBackground(BUTTON_NS);
            hasHovered16 = false;
        }
    }//GEN-LAST:event_btnLetterTile16MouseEntered

    // same as the previous Mouse Dragged Button
    private void btnLetterTile2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile2MouseDragged
        // TODO add your handling code here:
        if (!held) {
            hasHovered2 = true;
            guess += btnLetterTile2.getText();
            lastButton = btnLetterTile2;
            held = true;
            btnLetterTile2.setBackground(BUTTON_S);
        }

    }//GEN-LAST:event_btnLetterTile2MouseDragged

    // same as the previous Mouse Dragged Button
    private void btnLetterTile3MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile3MouseDragged
        // TODO add your handling code here:
        if (!held) {
            hasHovered3 = true;
            guess += btnLetterTile3.getText();
            lastButton = btnLetterTile3;
            held = true;
            btnLetterTile3.setBackground(BUTTON_S);
        }

    }//GEN-LAST:event_btnLetterTile3MouseDragged

    // same as the previous Mouse Released Button
    private void btnLetterTile2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile2MouseReleased
        // TODO add your handling code here:
        held = false;
        checkWord();
        System.out.println("worked omg");
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                boardButtons[r][c].setBackground(BUTTON_NS);
            }
        }
        guess = "";
    }//GEN-LAST:event_btnLetterTile2MouseReleased

    // same as the previous Mouse Entered Button
    boolean hasHovered9 = false;
    private void btnLetterTile9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile9MouseEntered
        // TODO add your handling code here:
        thisButton = btnLetterTile9;
        if (!hasHovered9 && held && lastButton.getBackground() == BUTTON_S && isNear()) {
            btnLetterTile9.setBackground(BUTTON_S);
            hasHovered9 = true;
            guess += btnLetterTile9.getText();
            lastButton = thisButton;
        } else {
            hasHovered9 = false;
        }
    }//GEN-LAST:event_btnLetterTile9MouseEntered

    // same as the previous Mouse Released Button
    private void btnLetterTile3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile3MouseReleased
        held = false;
        checkWord();
        System.out.println("worked omg");
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                boardButtons[r][c].setBackground(BUTTON_NS);
            }
        }
        guess = "";
    }//GEN-LAST:event_btnLetterTile3MouseReleased

    // same as the previous Mouse Released Button
    private void btnLetterTile4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile4MouseReleased
        held = false;
        checkWord();
        System.out.println("worked omg");
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                boardButtons[r][c].setBackground(BUTTON_NS);
            }
        }
        guess = "";
    }//GEN-LAST:event_btnLetterTile4MouseReleased

    // same as the previous Mouse Released Button
    private void btnLetterTile5MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile5MouseReleased
        held = false;
        checkWord();
        System.out.println("worked omg");
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                boardButtons[r][c].setBackground(BUTTON_NS);
            }
        }
        guess = "";
    }//GEN-LAST:event_btnLetterTile5MouseReleased

    // same as the previous Mouse Released Button
    private void btnLetterTile6MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile6MouseReleased
        held = false;
        checkWord();
        System.out.println("worked omg");
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                boardButtons[r][c].setBackground(BUTTON_NS);
            }
        }
        guess = "";
    }//GEN-LAST:event_btnLetterTile6MouseReleased

    // same as the previous Mouse Released Button
    private void btnLetterTile7MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile7MouseReleased
        held = false;
        checkWord();
        System.out.println("worked omg");
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                boardButtons[r][c].setBackground(BUTTON_NS);
            }
        }
        guess = "";
    }//GEN-LAST:event_btnLetterTile7MouseReleased

    // same as the previous Mouse Released Button
    private void btnLetterTile8MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile8MouseReleased
        held = false;
        checkWord();
        System.out.println("worked omg");
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                boardButtons[r][c].setBackground(BUTTON_NS);
            }
        }
        guess = "";
    }//GEN-LAST:event_btnLetterTile8MouseReleased

    // same as the previous Mouse Released Button
    private void btnLetterTile9MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile9MouseReleased
        held = false;
        checkWord();
        System.out.println("worked omg");
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                boardButtons[r][c].setBackground(BUTTON_NS);
            }
        }
        guess = "";
    }//GEN-LAST:event_btnLetterTile9MouseReleased

    // same as the previous Mouse Released Button
    private void btnLetterTile10MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile10MouseReleased
        held = false;
        checkWord();
        System.out.println("worked omg");
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                boardButtons[r][c].setBackground(BUTTON_NS);
            }
        }
        guess = "";
    }//GEN-LAST:event_btnLetterTile10MouseReleased

    // same as the previous Mouse Released Button
    private void btnLetterTile11MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile11MouseReleased
        held = false;
        checkWord();
        System.out.println("worked omg");
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                boardButtons[r][c].setBackground(BUTTON_NS);
            }
        }
        guess = "";
    }//GEN-LAST:event_btnLetterTile11MouseReleased

    // same as the previous Mouse Released Button
    private void btnLetterTile12MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile12MouseReleased
        held = false;
        checkWord();
        System.out.println("worked omg");
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                boardButtons[r][c].setBackground(BUTTON_NS);
            }
        }
        guess = "";
    }//GEN-LAST:event_btnLetterTile12MouseReleased

    // same as the previous Mouse Released Button
    private void btnLetterTile13MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile13MouseReleased
        held = false;
        checkWord();
        System.out.println("worked omg");
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                boardButtons[r][c].setBackground(BUTTON_NS);
            }
        }
        guess = "";
    }//GEN-LAST:event_btnLetterTile13MouseReleased

    // same as the previous Mouse Released Button
    private void btnLetterTile14MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile14MouseReleased
        held = false;
        checkWord();
        System.out.println("worked omg");
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                boardButtons[r][c].setBackground(BUTTON_NS);
            }
        }
        guess = "";
    }//GEN-LAST:event_btnLetterTile14MouseReleased

    // same as the previous Mouse Released Button
    private void btnLetterTile15MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile15MouseReleased
        held = false;
        checkWord();
        System.out.println("worked omg");
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                boardButtons[r][c].setBackground(BUTTON_NS);
            }
        }
        guess = "";
    }//GEN-LAST:event_btnLetterTile15MouseReleased

    // same as the previous Mouse Released Button
    private void btnLetterTile16MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile16MouseReleased
        held = false;
        checkWord();
        System.out.println("worked omg");
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                boardButtons[r][c].setBackground(BUTTON_NS);
            }
        }
        guess = "";
    }//GEN-LAST:event_btnLetterTile16MouseReleased

    // same as the previous Mouse Dragged Button
    private void btnLetterTile4MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile4MouseDragged
        // TODO add your handling code here:
        if (!held) {
            hasHovered4 = true;
            guess += btnLetterTile4.getText();
            lastButton = btnLetterTile4;
            held = true;
            btnLetterTile4.setBackground(BUTTON_S);
        }

    }//GEN-LAST:event_btnLetterTile4MouseDragged

    // same as the previous Mouse Dragged Button
    private void btnLetterTile5MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile5MouseDragged
        // TODO add your handling code here:
        if (!held) {
            hasHovered5 = true;
            guess += btnLetterTile5.getText();
            lastButton = btnLetterTile5;
            held = true;
            btnLetterTile5.setBackground(BUTTON_S);
        }

    }//GEN-LAST:event_btnLetterTile5MouseDragged

    // same as the previous Mouse Dragged Button
    private void btnLetterTile6MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile6MouseDragged
        // TODO add your handling code here:
        if (!held) {
            hasHovered6 = true;
            guess += btnLetterTile6.getText();
            lastButton = btnLetterTile6;
            held = true;
            btnLetterTile6.setBackground(BUTTON_S);
        }

    }//GEN-LAST:event_btnLetterTile6MouseDragged

    // same as the previous Mouse Dragged Button
    private void btnLetterTile7MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile7MouseDragged
        // TODO add your handling code here:
        if (!held) {
            hasHovered7 = true;
            guess += btnLetterTile7.getText();
            lastButton = btnLetterTile7;
            held = true;
            btnLetterTile7.setBackground(BUTTON_S);
        }
    }//GEN-LAST:event_btnLetterTile7MouseDragged

    // same as the previous Mouse Dragged Button
    private void btnLetterTile8MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile8MouseDragged
        // TODO add your handling code here:
        if (!held) {
            hasHovered8 = true;
            guess += btnLetterTile8.getText();
            lastButton = btnLetterTile8;
            held = true;
            btnLetterTile8.setBackground(BUTTON_S);
        }
    }//GEN-LAST:event_btnLetterTile8MouseDragged

    // same as the previous Mouse Dragged Button
    private void btnLetterTile9MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile9MouseDragged
        // TODO add your handling code here:
        if (!held) {
            hasHovered9 = true;
            guess += btnLetterTile9.getText();
            lastButton = btnLetterTile9;
            held = true;
            btnLetterTile9.setBackground(BUTTON_S);
        }

    }//GEN-LAST:event_btnLetterTile9MouseDragged

    // same as the previous Mouse Dragged Button
    private void btnLetterTile10MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile10MouseDragged
        // TODO add your handling code here:
        if (!held) {
            hasHovered10 = true;
            guess += btnLetterTile10.getText();
            lastButton = btnLetterTile10;
            held = true;
            btnLetterTile10.setBackground(BUTTON_S);
        }

    }//GEN-LAST:event_btnLetterTile10MouseDragged

    // same as the previous Mouse Dragged Button
    private void btnLetterTile11MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile11MouseDragged
        // TODO add your handling code here:
        if (!held) {
            hasHovered11 = true;
            guess += btnLetterTile11.getText();
            lastButton = btnLetterTile11;
            held = true;
            btnLetterTile11.setBackground(BUTTON_S);
        }

    }//GEN-LAST:event_btnLetterTile11MouseDragged

    // same as the previous Mouse Dragged Button
    private void btnLetterTile12MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile12MouseDragged
        // TODO add your handling code here:
        if (!held) {
            hasHovered12 = true;
            guess += btnLetterTile12.getText();
            lastButton = btnLetterTile12;
            held = true;
            btnLetterTile12.setBackground(BUTTON_S);
        }

    }//GEN-LAST:event_btnLetterTile12MouseDragged

    // same as the previous Mouse Dragged Button
    private void btnLetterTile13MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile13MouseDragged
        // TODO add your handling code here:
        if (!held) {
            hasHovered13 = true;
            guess += btnLetterTile13.getText();
            lastButton = btnLetterTile13;
            held = true;
            btnLetterTile13.setBackground(BUTTON_S);
        }

    }//GEN-LAST:event_btnLetterTile13MouseDragged

    // same as the previous Mouse Dragged Button
    private void btnLetterTile14MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile14MouseDragged
        // TODO add your handling code here:
        if (!held) {
            hasHovered14 = true;
            guess += btnLetterTile14.getText();
            lastButton = btnLetterTile14;
            held = true;
            btnLetterTile14.setBackground(BUTTON_S);
        }

    }//GEN-LAST:event_btnLetterTile14MouseDragged

    // same as the previous Mouse Dragged Button
    private void btnLetterTile15MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile15MouseDragged
        // TODO add your handling code here:
        if (!held) {
            hasHovered15 = true;
            guess += btnLetterTile15.getText();
            lastButton = btnLetterTile15;
            held = true;
            btnLetterTile15.setBackground(BUTTON_S);
        }

    }//GEN-LAST:event_btnLetterTile15MouseDragged

    // same as the previous Mouse Dragged Button
    private void btnLetterTile16MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLetterTile16MouseDragged
        // TODO add your handling code here:
        if (!held) {
            hasHovered16 = true;
            guess += btnLetterTile16.getText();
            lastButton = btnLetterTile16;
            held = true;
            btnLetterTile16.setBackground(BUTTON_S);
        }

    }//GEN-LAST:event_btnLetterTile16MouseDragged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmWordHunt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmWordHunt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmWordHunt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmWordHunt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmWordHunt().setVisible(true);

            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLetterTile1;
    private javax.swing.JButton btnLetterTile10;
    private javax.swing.JButton btnLetterTile11;
    private javax.swing.JButton btnLetterTile12;
    private javax.swing.JButton btnLetterTile13;
    private javax.swing.JButton btnLetterTile14;
    private javax.swing.JButton btnLetterTile15;
    private javax.swing.JButton btnLetterTile16;
    private javax.swing.JButton btnLetterTile2;
    private javax.swing.JButton btnLetterTile3;
    private javax.swing.JButton btnLetterTile4;
    private javax.swing.JButton btnLetterTile5;
    private javax.swing.JButton btnLetterTile6;
    private javax.swing.JButton btnLetterTile7;
    private javax.swing.JButton btnLetterTile8;
    private javax.swing.JButton btnLetterTile9;
    private javax.swing.JButton btnPlayAgain;
    private javax.swing.JButton btnStart;
    private javax.swing.JTabbedPane gameScreens;
    private javax.swing.JLabel imgPlayer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblBlueS1;
    private javax.swing.JLabel lblBlueS2;
    private javax.swing.JLabel lblBlueS3;
    private javax.swing.JLabel lblBlueS4;
    private javax.swing.JLabel lblBlueS5;
    private javax.swing.JLabel lblBlueScore;
    private javax.swing.JLabel lblBlueW1;
    private javax.swing.JLabel lblBlueW2;
    private javax.swing.JLabel lblBlueW3;
    private javax.swing.JLabel lblBlueW4;
    private javax.swing.JLabel lblBlueW5;
    private javax.swing.JLabel lblBlueWin;
    private javax.swing.JLabel lblBlueWords;
    private javax.swing.JLabel lblPoints;
    private javax.swing.JLabel lblRedS1;
    private javax.swing.JLabel lblRedS2;
    private javax.swing.JLabel lblRedS3;
    private javax.swing.JLabel lblRedS4;
    private javax.swing.JLabel lblRedS5;
    private javax.swing.JLabel lblRedScore;
    private javax.swing.JLabel lblRedW1;
    private javax.swing.JLabel lblRedW2;
    private javax.swing.JLabel lblRedW3;
    private javax.swing.JLabel lblRedW4;
    private javax.swing.JLabel lblRedW5;
    private javax.swing.JLabel lblRedWin;
    private javax.swing.JLabel lblRedWords;
    private javax.swing.JLabel lblTimer;
    private javax.swing.JLabel lblWinner;
    private javax.swing.JLabel lblWordInfo;
    private javax.swing.JLabel lblWords;
    private javax.swing.JPanel panWIndicator;
    // End of variables declaration//GEN-END:variables
}
