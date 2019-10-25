package sample;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
public class TinyScanner extends Application {

    public enum States {start, Number , Identifier , inAssign, inComment, done, error}
    private String wholeFile;
    private int pointer = 0;
    private States state = States.start;
    private String currentTokenValue = "";
    private String currentTokenType;
    private Token t;
    private char c;

    private ArrayList<Token> tokenList= new ArrayList<Token>();
    private boolean isSpecialCharacter(char c) {
        if (c == ';' || c == '*' || c == '=' || c == '-' || c == '+'
                || c == ')' || c == '(' || c == '<' || c == '>'|| c == '/' || c == ',' ) {
            return true;
        }
        return false;
    }
    public void getTokens() {
        for (int i = pointer; i <= wholeFile.length(); i++) {
            if (i == wholeFile.length())
                c = ' ';
            else {
                c = wholeFile.charAt(i);
            }

            setState();
            switch (state) {
                case start:
                    continue;
                case inComment:
                    currentTokenValue += c;
                    currentTokenType = state.toString();
                    continue;
                case inAssign:
                    currentTokenValue += c;
                    currentTokenType = state.toString();
                    continue;
                case Identifier:
                    currentTokenValue += c;
                    currentTokenType = state.toString();
                    continue;
                case Number :
                    currentTokenValue += c;
                    currentTokenType = state.toString();
                    continue;
                case error:
                    currentTokenValue += c;
                    currentTokenType = state.toString();
                    continue;
                case done:
                    currentTokenValue = currentTokenValue.trim();
                    if (isSpecialCharacter(c)||(wholeFile.charAt(i-1)=='=' && c==' ')) {
                        currentTokenType = "Special Symbol";
                        currentTokenValue += c;
                    }
                    if (isReservedWord(currentTokenValue) && currentTokenType == States.Identifier.toString())
                        currentTokenType = "Reserved Word";
                    t = new Token(currentTokenType, currentTokenValue);
                    System.out.println(t);
                    tokenList.add(t);
                    currentTokenValue = "";
                    if (Character.isLetter(c) && currentTokenType != States.Identifier.toString()
                            || Character.isDigit(c) && currentTokenType != States.Number.toString()
                            || c == ':')
                        currentTokenValue += c;
                    currentTokenType = "";
                    state = States.start;
                    break;
            }
        }
    }
    private void setState() {
        if (state.equals(States.start)) {
            if (Character.isWhitespace(c)) {
                state = States.start;
            } else if (c == '{') {
                state = States.inComment;
            } else if (Character.isDigit(c)) {
                state = States.Number;
            } else if (Character.isLetter(c)) {
                state = States.Identifier;
            } else if (c == ':') {
                state = States.inAssign;
            } else if (c == '!') {
                state = States.inAssign;
            } else if (c == '=' ) {
                state = States.inAssign;
            } else if (isSpecialCharacter(c)) {
                state = States.done;
            }
            else
                state = States.error;
        } else if (state.equals(States.inComment)) {
            if (c == '}') {
                currentTokenValue += c;
                state = States.done;
            } else {
                state = States.inComment;
            }
        } else if (state.equals(States.Identifier)) {
            if (Character.isLetter(c)) {
                state = States.Identifier;
            } else if (Character.isDigit(c)) {
                state = States.Identifier;
            } else if(c==' ') {
                state = States.done;
            } else {
                state = States.error;
            }
        } else if (state.equals(States.Number)) {
            if (Character.isDigit(c) || c=='.') {
                state = States.Number;
            } else if (Character.isLetter(c)) {
                state = States.error;
            } else if(c==' ') {
                state = States.done;
            } else {
                state = States.error;
            }
        } else if (state.equals(States.inAssign)) {
            if (c == '='  ) {
                state = States.done;
            } else {
                state = States.done;
            }
        } else if (state.equals(States.done)) {
            state = States.start;
        }
         else if (state.equals(States.error)) {
            if (c == ' '  )
                state = States.done;
            else
                state = States.error;
        }
    }


    private boolean isReservedWord(String s) {
        if (s.equals("write") || s.equals("if") || s.equals("until") || s.equals("read")
                || s.equals("end")|| s.equals("return")|| s.equals("begin")
                || s.equals("main") || s.equals("real")|| s.equals("else")
                || s.equals("then")|| s.equals("string") || s.equals("int")|| s.equals("repeat"))
            return true;
        return false;
    }
    public void start(Stage primaryStage)  {
        primaryStage.setTitle("TinyScanner App");
        TableView table = new TableView ();
        final Label label = new Label("Scanner");
        label.setFont(new Font("Arial", 20));

         table.setEditable(true);
        table.setPrefSize( 500, 100 );
        TableColumn<String, Token> column1 = new TableColumn<>("Type");
        column1.setCellValueFactory(new PropertyValueFactory<>("type"));
        column1.setPrefWidth(150);

        TableColumn<String, Token> column2 = new TableColumn<>("Value");
        column2.setCellValueFactory(new PropertyValueFactory<>("value"));
        column2.setPrefWidth(350);

        table.getColumns().add(column1);
        table.getColumns().add(column2);



        FileChooser fileChooser = new FileChooser();
        Button button1 = new Button("DeleteTable");
        button1.setOnAction(e -> {
            tokenList.clear();
            table.getItems().clear();
        });
        Button button = new Button("Select File");
        button.setOnAction(e -> {
            File f = fileChooser.showOpenDialog(primaryStage);
            try {

                FileInputStream fis = new FileInputStream(f);
                PrintStream fOut = new PrintStream("scanner_output.txt");
                System.setOut(fOut);
                byte[] data = new byte[(int) f.length()];
                fis.read(data);
                fis.close();
                wholeFile = new String(data, "UTF-8");
                if(wholeFile.length()>0 )
                    c = wholeFile.charAt(pointer);
                String temp = "";
                for (int i = 0; i < wholeFile.length(); i++) {
                    char d = wholeFile.charAt(i);
                    if (isSpecialCharacter(d)) {
                        if ((wholeFile.charAt(i - 1) != ':'))
                            if((wholeFile.charAt(i - 1) != '!'))
                                if((wholeFile.charAt(i - 1) != '=') )

                                    temp += " ";

                    }
                    temp += d;

                }
                wholeFile = temp + " ";
                getTokens();
                ObservableList d= FXCollections.observableArrayList(tokenList);
                for(int b=0; b<tokenList.size(); b++) {
                    table.getItems().add(new Token(tokenList.get(b).getType(), tokenList.get(b).getValue()));
                }

             } catch (Exception e1) {
                e1.printStackTrace();
            }

        });

         HBox hBox = new HBox(table,button,button1);
        hBox.setSpacing(20);
        hBox.setPadding(new Insets(10, 10, 10, 10));
        Scene  scene = new Scene(hBox, 960, 600);
        scene.getStylesheets().add(getClass().getResource("viper.css").toExternalForm());


        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
