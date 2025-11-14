import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.HashMap;

public class Wordle {

    @FXML
    Label title;

    @FXML
    VBox screen;

    @FXML
    VBox textArea;

    @FXML
    VBox rowBoardOne;

    // variables
    int i = 0;
    int p = 0;
    boolean enterClicked = false;
    boolean valid;

    // Arrays
    HBox[] wordleBox = new HBox[5];
    HBox[] row = new HBox[3];
    TextField[][] wordleField = new TextField[5][5];
    TextField[][] keyBoard = new TextField[10][10];
    String[] words = { "cycle", "sheep", "unify", "peace", "happy", "angry", "space", "crazy", "forte", "crate" }; // 5
    String[] letterOne = { "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P" };
    String[] letterTwo = { "A", "S", "D", "F", "G", "H", "J", "K", "L", ";" };
    String[] letterThree = { "ENTER", "Z", "X", "C", "V", "B", "N", "M", "DELETE", "," };

    Random rand = new Random();
    String chosenWord = words[rand.nextInt(words.length)];
    ActionEvent KeyEvent;
    File file;

    public void initialize() {
        file = new File(App.class.getResource("words.txt").toString().replace("file:/", ""));

        createKeyBoard();

        // Start of textboxes for word input
        for (int j = 0; j < wordleBox.length; j++) {
            wordleBox[j] = new HBox();

            for (int n = 0; n < wordleField.length; n++) {
                wordleField[j][n] = new TextField();
                wordleField[j][n].setPrefSize(80, 150);
                wordleField[j][n].setAlignment(Pos.CENTER);
                wordleField[j][n].setFont(Font.font("Helvetica", FontWeight.BOLD, 36));
                wordleField[j][n].setEditable(false);
                wordleBox[j].getChildren().add(wordleField[j][n]);

                // listener to allow only strings to be inputed
                wordleField[j][n].textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\sa-zA-Z*")) {
                        for (int k = 0; k < wordleField.length; k++) {
                            wordleField[p][i].setText(newValue.replaceFirst("[^\\sa-zA-Z]", ""));
                        }
                    }
                });

                // KeyPressed event
                wordleField[j][n].setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(javafx.scene.input.KeyEvent key) {
                        if (key.getCode().equals(KeyCode.ENTER) && wordleField[p][4].isFocused()) {
                            try {
                                checkWord();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            if (wordleField[p][4].getText().length() > 0 && valid) {
                                checkChar();
                                nextRow();
                            } else {
                                for (int s = 0; s < wordleField.length; s++) {
                                    wordleField[p][s].setEditable(true);
                                    wordleField[p][i].setText("");
                                    i = 4;
                                }

                            }
                        }

                    }

                });
                // KeyTyped event
                wordleField[j][n].setOnKeyTyped(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(javafx.scene.input.KeyEvent event) {
                        int max = 1;
                        for (int a = 0; a < wordleField.length; a++) {
                            if (wordleField[p][a].getText().length() > max) {
                                event.consume();
                            }
                        }

                        if (wordleField[p][i].getText().length() > 0 && (!event.getCode().equals(KeyCode.DELETE))) {
                            i++;
                        } else {
                            i--;
                        }
                        nextBox();
                    }
                });

            }
            wordleBox[j].setSpacing(5);
            wordleBox[j].setAlignment(Pos.TOP_CENTER);
            textArea.getChildren().add(wordleBox[j]);

        }

    }

    public void createKeyBoard() {
        /**
         * <p>
         * Creates keyboard and hbox using loops and sets attributes.
         * Adds letters to keyboard and displays it on screen.
         * 
         * </p>
         * 
         */
        for (int v = 0; v < row.length; v++) {
            row[v] = new HBox();
            for (int e = 0; e < keyBoard.length; e++) {
                keyBoard[v][e] = new TextField();
                keyBoard[v][e].setPrefSize(50, 50);
                keyBoard[v][e].setAlignment(Pos.CENTER);
                keyBoard[v][e].setFont(Font.font("Helvetica", FontWeight.BOLD, 10));
                keyBoard[v][e].setEditable(false);
                keyBoard[v][e].setStyle("-fx-background-color: LIGHTGREY;");
                HBox.setMargin(keyBoard[v][e], new Insets(1.5, 3, 1.5, 3));
                row[v].getChildren().add(keyBoard[v][e]);

            }
            row[v].setAlignment(Pos.TOP_CENTER);
            rowBoardOne.getChildren().add(row[v]);
        }

        for (int i = 0; i < keyBoard.length; i++) { // sets text to each row of keyboard
            keyBoard[0][i].setText(letterOne[i]);
            keyBoard[1][i].setText(letterTwo[i]);
            keyBoard[2][i].setText(letterThree[i]);

        }
    }

    public void checkWord() throws IOException {
        /**
         * <p>
         * Checks if input matches a word from word file, if it does it is a valid input
         * otherwise it is invalid.
         * </p>
         */

        // inputs
        String inputZero = wordleField[p][0].getText().toLowerCase();
        String inputOne = wordleField[p][1].getText().toLowerCase();
        String inputTwo = wordleField[p][2].getText().toLowerCase();
        String inputThree = wordleField[p][3].getText().toLowerCase();
        String inputFour = wordleField[p][4].getText().toLowerCase();
        String totalInput = inputZero + inputOne + inputTwo + inputThree + inputFour;

        BufferedReader read = new BufferedReader(new FileReader(file));
        if (wordIsThere(totalInput, read)) {
            valid = true;
        } else {
            valid = false;
        }
    }

    private boolean wordIsThere(String totalInput, BufferedReader read) throws IOException {
        /**
         * <p>
         * Reads through word file and if input matches a word from file, then return
         * true.
         * If it does not match it returns false.
         * 
         * function referenced from
         * https://stackoverflow.com/questions/69225453/how-to-compare-an-user-input-to-a-text-file-using-bufferedreader
         * Author - Abra
         * </p>
         * 
         * @Param totalinput (the word player guessed)
         * @Param read (file reader)
         */

        String line = read.readLine(); // read first line of file
        while (line != null) {
            if (totalInput.equals(line)) {
                return true;
            }
            line = read.readLine(); // read next line of file
        }
        return false;
    }

    public HashMap<String, int[]> createKeyBoardHashMap() {
        /**
         * <p>
         * Creates hashmap for keyboard using letter as key and indexs as value.
         * 
         * </p>
         * 
         * @returns keyboardMap
         */
        HashMap<String, int[]> keyBoardMap = new HashMap<String, int[]>();
        for (int i = 0; i < keyBoard.length; i++) {
            for (int n = 0; n < row.length; n++) {
                int[] mapping = { n, i };
                keyBoardMap.put(keyBoard[n][i].getText().toLowerCase(), mapping);
            }
        }

        return keyBoardMap;
    }

    public void checkChar() {
        /**
         * <p>
         * Checks whether inputed leters matches letters of word to be guessed. If it is
         * in the
         * correct pos then keyboard
         * and textbox turns green , if it is in the wrong pos then turns yellow.
         * 
         * </p>
         * 
         */

        HashMap<String, int[]> keyBoardMap = createKeyBoardHashMap();

        // inputs
        String inputZero = wordleField[p][0].getText().toLowerCase();
        String inputOne = wordleField[p][1].getText().toLowerCase();
        String inputTwo = wordleField[p][2].getText().toLowerCase();
        String inputThree = wordleField[p][3].getText().toLowerCase();
        String inputFour = wordleField[p][4].getText().toLowerCase();
        String totalInput = inputZero + inputOne + inputTwo + inputThree + inputFour;

        // chars
        char zero = inputZero.charAt(0);
        char one = inputOne.charAt(0);
        char two = inputTwo.charAt(0);
        char three = inputThree.charAt(0);
        char four = inputFour.charAt(0);
        char[] charArray = { zero, one, two, three, four };

        // Strings
        String currCharZero = String.valueOf(charArray[0]);
        String currCharOne = String.valueOf(charArray[1]);
        String currCharTwo = String.valueOf(charArray[2]);
        String currCharThree = String.valueOf(charArray[3]);
        String currCharFour = String.valueOf(charArray[4]);

        // ints
        int[] indicesZero = keyBoardMap.get(currCharZero);
        int[] indicesOne = keyBoardMap.get(currCharOne);
        int[] indicesTwo = keyBoardMap.get(currCharTwo);
        int[] indicesThree = keyBoardMap.get(currCharThree);
        int[] indicesFour = keyBoardMap.get(currCharFour);
        System.out.println(chosenWord);
        for (int d = 0; d < wordleField.length; d++) {

            String currChar = String.valueOf(charArray[d]);
            int[] indices = keyBoardMap.get(currChar);

            if (chosenWord.charAt(0) == charArray[0]) { // if first letter of chosen words matches first inputted
                                                        // letter...
                wordleField[p][0].setStyle("-fx-background-color: green;"); // textfield colour set to green
                keyBoard[indicesZero[0]][indicesZero[1]].setStyle("-fx-background-color: green;"); // keyboard letter
                                                                                                   // set to green

            } else if (chosenWord.charAt(0) == charArray[d]) {
                wordleField[p][d].setStyle("-fx-background-color: yellow;"); // textfield colour set to yellow
                keyBoard[indices[0]][indices[1]].setStyle("-fx-background-color: yellow;");// keyboard letter set to
                                                                                           // yellow
            }

            if (chosenWord.charAt(1) == charArray[1]) {
                wordleField[p][1].setStyle("-fx-background-color: green;");
                keyBoard[indicesOne[0]][indicesOne[1]].setStyle("-fx-background-color: green;");

            } else if (chosenWord.charAt(1) == charArray[d]) {
                wordleField[p][d].setStyle("-fx-background-color: yellow;");
                keyBoard[indices[0]][indices[1]].setStyle("-fx-background-color: yellow;");
            }

            if (chosenWord.charAt(2) == charArray[2]) {
                wordleField[p][2].setStyle("-fx-background-color: green;");
                keyBoard[indicesTwo[0]][indicesTwo[1]].setStyle("-fx-background-color: green;");
            } else if (chosenWord.charAt(2) == charArray[d]) {
                wordleField[p][d].setStyle("-fx-background-color: yellow;");
                keyBoard[indices[0]][indices[1]].setStyle("-fx-background-color: yellow;");
            }

            if (chosenWord.charAt(3) == charArray[3]) {
                wordleField[p][3].setStyle("-fx-background-color: green;");
                keyBoard[indicesThree[0]][indicesThree[1]].setStyle("-fx-background-color: green;");

            } else if (chosenWord.charAt(3) == charArray[d]) {
                wordleField[p][d].setStyle("-fx-background-color: yellow;");
                keyBoard[indices[0]][indices[1]].setStyle("-fx-background-color: yellow;");
            }

            if (chosenWord.charAt(4) == charArray[4]) {
                wordleField[p][4].setStyle("-fx-background-color: green;");
                keyBoard[indicesFour[0]][indicesFour[1]].setStyle("-fx-background-color: green;");

            } else if (chosenWord.charAt(4) == charArray[d]) {
                wordleField[p][d].setStyle("-fx-background-color: yellow;");
                keyBoard[indices[0]][indices[1]].setStyle("-fx-background-color: yellow;");
            }

            if (totalInput.equals(chosenWord)) { // if input equals word to be guessed
                wordleField[d][d].setEditable(false);
            }
        }
    }

    public void nextRow() {
        /**
         * <p>
         * nextRow method checks which row is currently focused and
         * if it is not the last row, it will move to the next row everytime
         * method is run. If it is on the last row, then it will stay on the last row.
         * </p>
         * 
         */

        i = 0;

        if (p < 4) { // if smaller than 4
            p++;
        } else {
            wordleField[p][i].setEditable(false);
            p = 4;
            i = 4;
        }
    }

    public void nextBox() {
        /**
         * <p>
         * nextBox method checks which textfield is focused. If it is not the last
         * textfield in the row then everytime function is run it will move to the next
         * textfield.
         * </p>
         * 
         */

        if (i <= 0) {
            i = 0;
        }

        if (i <= 4) {

            wordleField[p][i].setEditable(true);
            wordleField[p][i].requestFocus();

        } else {
            i = 4;
            for (int m = 0; m < wordleField.length; m++) {
                wordleField[p][m].setEditable(false);

            }
        }

    }

}
