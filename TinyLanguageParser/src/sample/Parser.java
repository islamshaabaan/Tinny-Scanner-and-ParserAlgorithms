package sample;


import jdk.nashorn.internal.scripts.JS;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Stack;

public class Parser {
    private JSONObject root;
    private ArrayList<Token> tokens;
    private Stack<JSONObject> references;
    private int currentIndex;
    private static boolean isANumber;
    private int numberOfAss;
    public Parser(ArrayList<Token> tokens){
        this.tokens=tokens;
    }

    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    private void matchType(String s){
        if(tokens.get(currentIndex).getType().equals(s))
            currentIndex++;
        else{
            AlertBox alertBox=new AlertBox();
            alertBox.display("Error","syntax error");
        }
    }

    private void match(String tokenValue){
        if(tokens.get(currentIndex).getValue().equals(tokenValue))
            currentIndex++;
        else{
            AlertBox alertBox=new AlertBox();
            alertBox.display("Error","syntax error");
        }
    }

    public JSONObject parse(){
        this.root=new JSONObject();
        this.references=new Stack<>();
        this.references.push(root);
        currentIndex=0;
        numberOfAss=0;
        isANumber=false;
        program();
        System.out.println(currentIndex);
        return root;
    }

    private void program(){
        stmt_sequence();
    }

    private void stmt_sequence(){
        statement();
        if(currentIndex==tokens.size())
            return;
        while(tokens.get(currentIndex).getValue().equals(";")){
            match(";");
            statement();
            if(currentIndex==tokens.size())
                break;
        }
    }

    private void statement(){
        JSONObject ref,obj;
        ref=references.peek();
        obj=new JSONObject();
        ref.put(Integer.toString(numberOfAss++),obj);
        references.push(obj);
        if(tokens.get(currentIndex).getValue().equals("if"))
            if_stmt();
        else if(tokens.get(currentIndex).getValue().equals("repeat"))
            repeat_stmt();
        else if(tokens.get(currentIndex).getType().equals(Constants.IDENTIFIER))
            assign_stmt();
        else if(tokens.get(currentIndex).getValue().equals("read"))
            read_stmt();
        else if(tokens.get(currentIndex).getValue().equals("write"))
            write_stmt();
        //else if(tokens.get(currentIndex).getType().equals("Comment"))
            //comment_stmt();
        else{
            AlertBox alertBox=new AlertBox();
            alertBox.display("Error","syntax error");
        }
        references.pop();
    }

    private void if_stmt(){
        match("if");
        JSONObject ref;
        ref=references.peek();
        JSONObject obj=new JSONObject();
        ref.put("ifStatement",obj);
        references.push(obj);

        ref=references.peek();
        ref.put("testPart",exp());

        match("then");

        ref=references.peek();
        obj=new JSONObject();
        ref.put("thenPart",obj);
        references.push(obj);

        stmt_sequence();

        references.pop();

        if(tokens.get(currentIndex).getValue().equals("else")){
            ref=references.peek();
            obj=new JSONObject();
            ref.put("elsePart",obj);
            references.push(obj);
            match("else");

            stmt_sequence();
            match("end");
            references.pop();
        }
        references.pop();
    }
    private void comment_stmt(){
        currentIndex++;
     }
    private void repeat_stmt(){
        match("repeat");
        JSONObject ref;
        ref=references.peek();
        JSONObject obj=new JSONObject();
        ref.put("repeatStatement",obj);
        references.push(obj);

        ref=references.peek();
        obj=new JSONObject();
        ref.put("repeatBody",obj);
        references.push(obj);

        stmt_sequence();

        references.pop();

        match("until");

        ref=references.peek();
        ref.put("repeatTest",exp());

        references.pop();
    }

    private void assign_stmt(){
        JSONObject ref;
        ref=references.peek();
        JSONObject obj=new JSONObject();
        ref.put("assign",obj);
        obj.put("IdentifierName",tokens.get(currentIndex).getValue());
        matchType(Constants.IDENTIFIER);
        match(":=");
        obj.put("rightOperand",exp());

    }

    private void read_stmt(){
        match("read");
        JSONObject obj=references.peek();
        obj.put("readStatement",tokens.get(currentIndex).getValue());
        matchType(Constants.IDENTIFIER);
    }

    public void write_stmt(){
        match("write");
        JSONObject ref;
        ref=references.peek();
        JSONObject obj=new JSONObject();
        ref.put("writeStatement",exp());

    }

    public JSONObject exp(){
        JSONObject temp=simple_exp();
        if(currentIndex==tokens.size())
            return temp;
        if((tokens.get(currentIndex).getValue().equals("<")) ||
                (tokens.get(currentIndex).getValue().equals("="))){
            String op=tokens.get(currentIndex).getValue();
            currentIndex++;
            JSONObject newTemp=new JSONObject();
            newTemp.put("op",op);
            newTemp.put("leftOperand",temp);
            newTemp.put("rightOperand",simple_exp());
            JSONObject x=new JSONObject();
            x.put("operation",newTemp);
            temp=x;
        }
        return temp;
    }

    public JSONObject simple_exp(){
        JSONObject temp=term();
        if(currentIndex==tokens.size())
            return temp;
        while((currentIndex!=tokens.size()) && (tokens.get(currentIndex).getValue().equals("+") ||
                tokens.get(currentIndex).getValue().equals("-"))){
            String op;
            if(tokens.get(currentIndex).getValue().equals("+"))
                op = "+";
            else
                op="-";
            currentIndex++;
            JSONObject newTemp=new JSONObject();
            newTemp.put("op",op);
            newTemp.put("leftOperand",temp);
            newTemp.put("rightOperand",term());
            JSONObject x=new JSONObject();
            x.put("operation",newTemp);
            temp=x;
        }
        return  temp;
    }

    public JSONObject term(){
        JSONObject temp=factor();
        if(currentIndex==tokens.size())
            return temp;
        while((currentIndex!=tokens.size()) && (tokens.get(currentIndex).getValue().equals("*") ||
                tokens.get(currentIndex).getValue().equals("/"))){
            String op;
            if(tokens.get(currentIndex).getValue().equals("*"))
                op = "*";
            else
                op="/";
            currentIndex++;
            JSONObject newTemp=new JSONObject();
            newTemp.put("op",op);
            newTemp.put("leftOperand",temp);
            newTemp.put("rightOperand",factor());
            JSONObject x=new JSONObject();
            x.put("operation",newTemp);
            temp=x;
        }
        return  temp;
    }

    public JSONObject factor(){
        JSONObject temp=new JSONObject();
        String s=tokens.get(currentIndex).getValue();
        if(s.equals("(")){
            match("(");
            temp=exp();
            match(")");
        }
        else if(isInteger(s)){
            temp.put("number",tokens.get(currentIndex).getValue());
            currentIndex++;
            isANumber=true;
        }
        else if(s.equals("-")){
            match("-");
            temp.put("number","-"+tokens.get(currentIndex).getValue());
            currentIndex++;
            isANumber=true;
        }
        else if(tokens.get(currentIndex).getType().equals(Constants.IDENTIFIER)){
            temp.put("identifier",tokens.get(currentIndex).getValue());
            currentIndex++;
            isANumber=false;
        }
        else{
            AlertBox alertBox=new AlertBox();
            alertBox.display("Error","syntax error");
        }
        return temp;
    }

}