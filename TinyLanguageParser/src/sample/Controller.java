package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML private Button inputFileButton;
    @FXML private TextField inputTextField;
    @FXML private TextArea outputTextArea;
    @FXML private Button ParseButton;
    @FXML private Button delButton;
    private ArrayList<Token> tokens;
    byte[] data;
    String wholeFile=" ";
    public void initialize(URL url, ResourceBundle rb){
        //outputTextArea.setEditable(false);
    }
    public void inputButtonClicked(){

        FileChooser fc=new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text files","*.txt"));
        File selectedFile =fc.showOpenDialog(null);
        try{
            FileInputStream fis = new FileInputStream(selectedFile);
            data = new byte[(int) selectedFile.length()];
            fis.read(data);
            fis.close();
            wholeFile += new String(data, "UTF-8");

            String temp = "";
            for (int i = 0; i < wholeFile.length(); i++) {
                char d1 = wholeFile.charAt(i);


                if (Scanner.isSpecialSymbol(d1) && d1 != '=') {
                    temp += " ";
                } else if (d1 == '='&& (i != wholeFile.length()-1)) {
                    if (wholeFile.charAt(i + 1) == '=')
                        temp += " ";
                    else if (Character.isLetter(wholeFile.charAt(i - 1)) || Character.isDigit(wholeFile.charAt(i - 1)))
                        temp += " ";
                } else if ((d1 == ':' || d1 == '!') &&(i != wholeFile.length()-1) ) {
                    if (wholeFile.charAt(i + 1) == '=')
                        temp += " ";
                } else if ((Character.isLetter(d1) || Character.isDigit(d1)) &&
                        Scanner.isSpecialSymbol(wholeFile.charAt(i - 1))) {
                    temp += " ";
                }
                else if(d1 == '{' || d1=='\r' || d1=='\n'  )
                    temp += " ";
                temp += d1;

            }
            wholeFile = temp + " ";
        }catch (Exception p){

        }

        if(selectedFile !=null){
            inputTextField.setText(selectedFile.getAbsolutePath());
        }
        else{
            AlertBox alertBox=new AlertBox();
            alertBox.display("Error","an error occured");
        }
    }
    public void scan() {
        outputTextArea.clear();
        tokens=new ArrayList<>();
        Scanner scanner = new Scanner(tokens);
        try {
            //FileReader fr = new FileReader(inputTextField.getText());
            //BufferedReader br=new BufferedReader(fr);

                scanner.getTokens( wholeFile);

            //br.close();
        } catch (Exception e) {
            AlertBox alertBox=new AlertBox();
            alertBox.display("Error","an error occured");
        }

        System.out.println(tokens.size());
        for(int i=0;i<tokens.size();i++){
            System.out.println(tokens.get(i));
        }
          wholeFile=" ";
    }
    public void parse(){

        outputTextArea.clear();
        scan();
        for(int i=0; i<tokens.size(); i++)
        {
            if(tokens.get(i).getType().equals("Comment") || tokens.get(i).getType().equals("") )
                tokens.remove(i);
        }
        Parser p=new Parser(tokens);
        JSONObject obj=p.parse();
        outputTextArea.clear();
        String s=obj.toString();
        //s.replace('[',' ');
        //s.replace('[',' ');

        String formatted = obj.toString(4);
        outputTextArea.setText(formatted);
        try{
            PrintStream out = new PrintStream(new FileOutputStream("output.js"));
            out.print("var myObj="+s);
        }catch(Exception e){

        }

        ///


        StackPane secondaryLayout = new StackPane();
        Scene secondScene = new Scene(secondaryLayout, 1000, 700);
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.executeScript("let varname = "+s);
        browser.sceneProperty().addListener(new ChangeListener<Scene>() {

            @Override
            public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene scene) {
                if (scene != null) {
                    browser.setMaxSize(secondScene.getWidth(),secondScene.getHeight());
                    browser.maxWidthProperty().bind(secondScene.widthProperty());
                    browser.maxHeightProperty().bind(secondScene.heightProperty());
                } else {
                    browser.maxWidthProperty().unbind();
                    browser.maxHeightProperty().unbind();
                }
            }
        });
        File f = new File("index.html");
        webEngine.load(f.toURI().toString());

        secondaryLayout.getChildren().add(browser);



        // New window (Stage)
          Stage newWindow = new Stage();
        newWindow.setTitle("Tiny Syntax Tree");
        newWindow.setScene(secondScene);

        newWindow.show();
        ///
    }


}