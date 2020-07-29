package sample;


import javafx.scene.control.TextArea;

import java.util.ArrayList;

public class Scanner {
    private ArrayList<Token> tokenList;
    private States state = States.start;
    private char c;
    private String currentTokenValue = "";
    private String currentTokenType;
     private int pointer = 0;
    public enum States {start, number , identifier ,  assign,  Comment, Error, done}
    public Scanner(){
        this.tokenList=null;
    }
    public Scanner(ArrayList<Token> t1) {
        this.tokenList = t1;
    }

    public static boolean isSpecialSymbol(char c) {
        if (c == ';' || c == '*' || c == '=' || c == '-' || c == '+'
                || c == ')' || c == '(' || c == '<' || c == '>'||
                c == '/' || c == ',' || c== '['|| c== ']') {
            return true;
        }
        return false;

    }
    private boolean isReservedWord(String s) {
        if (s.equals("write") || s.equals("WRITE")
                || s.equals("if") ||s.equals("IF")||
                s.equals("until") || s.equals("read")||
                s.equals("UNTIL") || s.equals("READ")
                || s.equals("end")|| s.equals("return")
                || s.equals("END")|| s.equals("RETURN")||
                s.equals("begin") || s.equals("main") ||
                s.equals("BEGIN") || s.equals("MAIN") ||
                s.equals("real")|| s.equals("else") ||
                s.equals("REAL")|| s.equals("ELSE")
                || s.equals("then")|| s.equals("string") ||
                s.equals("THEN")|| s.equals("STRING") ||
                s.equals("int")|| s.equals("repeat") ||
                s.equals("INT")|| s.equals("REPEAT"))
            return true;
        return false;
    }
    private void setState() {
        if (state.equals(States.start)) {
            if (Character.isWhitespace(c)) {
                state = States.start;
            } else if (c == '{') {
                state = States.Comment;
            } else if (Character.isDigit(c)) {
                state = States.number;
            } else if (Character.isLetter(c)) {
                state = States.identifier;
            } else if (c == ':') {
                state = States.assign;
            } else if (c == '!') {
                state = States.assign;
            } else if (c == '=' ) {
                state = States.assign;
            } else if (isSpecialSymbol(c)&& c!= '=') {
                state = States.done;
            } else if (c == 65279 ) {
                state = States.start;
            }else
                state = States.Error;
        } else if (state.equals(States.Comment)) {
            if (c == '}') {
                currentTokenValue += c;
                state = States.done;
            } else {
                state = States.Comment;
            }
        } else if (state.equals(States.identifier) ) {
            if (Character.isLetter(c)) {
                state = States.identifier;
            } else if (Character.isDigit(c)) {
                state = States.Error;
            } else if(c==' ') {
                state = States.done;
            } else {
                state = States.Error;
            }
        } else if (state.equals(States.number)) {
            if (Character.isDigit(c) || c=='.') {
                state = States.number;
            } else if (Character.isLetter(c)) {
                state = States.Error;
            } else if(c==' ') {
                state = States.done;
            } else {
                state = States.Error;
            }
        } else if (state.equals(States.assign)) {
            if (c == '='  ) {
                state = States.assign;
            } else if(c== ' ') {
                state = States.done;
            }
            else {
                state = States.Error;
            }
        } else if (state.equals(States.done)) {
            state = States.start;
        }
        else if (state.equals(States.Error)) {
            if (c == ' '  )
                state = States.done;
            else
                state = States.Error;
        }
    }
    public void getTokens(String wholeFile  ) {
        Token t ;

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
                case  Comment:
                    currentTokenValue += c;
                    currentTokenType = state.toString();
                    continue;
                case  assign:
                    currentTokenValue += c;
                    currentTokenType = state.toString();
                    continue;
                case identifier:
                    currentTokenValue += c;
                    currentTokenType = state.toString();
                    continue;
                case number :
                    currentTokenValue += c;
                    currentTokenType = state.toString();
                    continue;
                case Error:
                    currentTokenValue += c;
                    currentTokenType = state.toString();
                    continue;
                case done:
                    currentTokenValue = currentTokenValue.trim();
                    if (isSpecialSymbol(c)||(wholeFile.charAt(i-1)=='=' && c==' ')) {
                        if(c=='+') {
                            currentTokenType = Constants.PLUS;
                            currentTokenValue += c;
                        }if(c=='-') {
                            currentTokenType = Constants.MINUS;
                            currentTokenValue += c;
                        }if(c=='*') {
                            currentTokenType = Constants.MULTIPLY;
                            currentTokenValue += c;
                        }if(c=='/') {
                            currentTokenType = Constants.DIVIDE;
                            currentTokenValue += c;
                        }
                        else if(c=='(')
                        {   currentTokenType = Constants.OPENING_BRACKET;
                            currentTokenValue += c;
                        }

                        else if(c ==')')
                        {   currentTokenType = Constants.CLOSING_BRACKET;
                            currentTokenValue += c;
                        }
                        else if (c=='<')
                        {   currentTokenType = Constants.SMALLER_THAN;
                            currentTokenValue += c;
                        }
                        else if(c==';')
                        {
                            currentTokenType = Constants.SPECIAL_SYMBOL;
                            currentTokenValue += c;
                        }
                    }
                    if (isReservedWord(currentTokenValue) && currentTokenType == States.identifier.toString()) {
                        if(currentTokenValue.equals("write") ||currentTokenValue.equals("WRITE") )
                            currentTokenType = "RW : WRITE token";
                        else if (currentTokenValue.equals("if")||currentTokenValue.equals("IF"))
                            currentTokenType = "RW : IF token";
                        else if (currentTokenValue.equals("until")||currentTokenValue.equals("UNTIL"))
                            currentTokenType = "RW : UNTIL token";
                        else if (currentTokenValue.equals("read")||currentTokenValue.equals("READ"))
                            currentTokenType = "RW : READ token";
                        else if (currentTokenValue.equals("end")||currentTokenValue.equals("END"))
                            currentTokenType = "RW : END token";
                        else if (currentTokenValue.equals("return")||currentTokenValue.equals("RETURN"))
                            currentTokenType = "RW : RETURN token";
                        else if (currentTokenValue.equals("begin")||currentTokenValue.equals("BEGIN"))
                            currentTokenType = "RW : BEGIN token";
                        else if (currentTokenValue.equals("main")||currentTokenValue.equals("MAIN"))
                            currentTokenType = "RW : MAIN token";
                        else if (currentTokenValue.equals("real")||currentTokenValue.equals("REAL"))
                            currentTokenType = "RW : REAL token";
                        else if (currentTokenValue.equals("else")||currentTokenValue.equals("ELSE"))
                            currentTokenType = "RW : ELSE token";
                        else if (currentTokenValue.equals("then")||currentTokenValue.equals("THEN"))
                            currentTokenType = "RW : THEN token";
                        else if (currentTokenValue.equals("repeat")||currentTokenValue.equals("REPEAT"))
                            currentTokenType = "RW : REPEAT token";
                        else if (currentTokenValue.equals("int")||currentTokenValue.equals("INT"))
                            currentTokenType = "RW : INT token";
                        else if (currentTokenValue.equals("string")||currentTokenValue.equals("STRING"))
                            currentTokenType = "RW : STRING token";
                    }
                    t = new Token(currentTokenType, currentTokenValue);
                    System.out.println(t);
                    tokenList.add(t);
                    currentTokenValue = "";
                    if (Character.isLetter(c) && currentTokenType != States.identifier.toString()
                            || Character.isDigit(c) && currentTokenType != States.number.toString()
                            || c == ':')
                        currentTokenValue += c;
                    currentTokenType = "";
                    state = States.start;
                    break;
            }
        }
    }
    /*private enum STATE_TYPES{
        START,INCOMMENT,INNUM,INID,INASSIGN
    }
    private static boolean inComment=false;
    public Scanner(){
        this.tokens=null;
    }
    public Scanner(ArrayList<Token> t1) {
        this.tokens = t1;
    }
    private static boolean isAReservedWord(String input){
        String reservedWords[]={"if","then","else","end","repeat","until","read","write"};
        for(int i=0;i<reservedWords.length;i++){
            if(reservedWords[i].equals(input))
                return true;
        }
        return false;
    }
    private static boolean isADigit(char c){
        if((int)c>=48 && (int)c<=57)
            return true;
        return false;
    }
    private static boolean isALetter(char c){
        if(((int)c>=65 && (int)c<=90) || ((int)c>=97 && (int)c<=122))
            return true;
        return false;
    }
    public void scan(String input) {
        STATE_TYPES state;
        if(inComment)
            state=STATE_TYPES.INCOMMENT;
        else
            state = STATE_TYPES.START;
        Token t=new Token();
        StringBuilder s=new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            switch (state) {
                case START:
                    if (input.charAt(i) == ' ' || input.charAt(i)=='\t' )
                        state = STATE_TYPES.START;
                    else if (input.charAt(i) == '{') {
                        state = STATE_TYPES.INCOMMENT;
                        if(i==input.length()-1)
                            inComment=true;
                    }
                    else if (isADigit(input.charAt(i))) {
                        t.setType(Constants.NUMBER);
                        s.append(input.charAt(i));
                        state = STATE_TYPES.INNUM;
                        if (i == input.length() - 1) {
                            t.setValue(s.toString());
                            Token temp=new Token(t.getType(),t.getValue());
                            tokens.add(temp);
                        }
                    } else if (isALetter(input.charAt(i))) {
                        s.append(input.charAt(i));
                        state = STATE_TYPES.INID;
                        if (i == input.length() - 1) {
                            t.setValue(s.toString());
                            if (isAReservedWord(s.toString()))
                                t.setType(Constants.RESERVED_WORD);
                            else
                                t.setType(Constants.IDENTIFIER);
                            Token temp=new Token(t.getType(),t.getValue());
                            tokens.add(temp);
                        }
                    }
                    else if (input.charAt(i) == ':') {
                        s.append(input.charAt(i));
                        state = STATE_TYPES.INASSIGN;
                    } else {
                        t.setType(Constants.SPECIAL_SYMBOL);
                        s.append(input.charAt(i));
                        t.setValue(s.toString());
                        s.delete(0, s.length());
                        if(input.charAt(i)=='+')
                            t.setType(t.getType() + Constants.PLUS);
                        else if(input.charAt(i)=='-')
                            t.setType(t.getType() + Constants.MINUS);
                        else if(input.charAt(i)=='*')
                            t.setType(t.getType() + Constants.MULTIPLY);
                        else if(input.charAt(i)=='/')
                            t.setType(t.getType() + Constants.DIVIDE);
                        else if(input.charAt(i)=='(')
                            t.setType(t.getType() + Constants.OPENING_BRACKET);
                        else if(input.charAt(i)==')')
                            t.setType(t.getType() + Constants.CLOSING_BRACKET);
                        else if (input.charAt(i)=='<')
                            t.setType(t.getType() + Constants.SMALLER_THAN);
                        Token temp=new Token(t.getType(),t.getValue());
                        tokens.add(temp);
                    }
                    break;
                case INCOMMENT:
                    if (input.charAt(i) != '}') {
                        state = STATE_TYPES.INCOMMENT;
                        if(i==input.length()-1)
                            inComment=true;
                    } else {
                        inComment=false;
                        state = STATE_TYPES.START;
                    }
                    break;
                case INNUM:
                    if (isADigit(input.charAt(i))) {
                        s.append(input.charAt(i));
                        state = STATE_TYPES.INNUM;
                        if (i == input.length() - 1) {
                            t.setValue(s.toString());
                            Token temp=new Token(t.getType(),t.getValue());
                            tokens.add(temp);
                        }
                    }
                    else {
                        t.setValue(s.toString());
                        i--;
                        s.delete(0, s.length());
                        state = STATE_TYPES.START;
                        Token temp=new Token(t.getType(),t.getValue());
                        tokens.add(temp);
                    }
                    break;
                case INID:
                    if (isALetter(input.charAt(i))) {
                        s.append(input.charAt(i));
                        state = STATE_TYPES.INID;
                        if (i == input.length() - 1) {
                            t.setValue(s.toString());
                            if (isAReservedWord(s.toString()))
                                t.setType(Constants.RESERVED_WORD);
                            else
                                t.setType(Constants.IDENTIFIER);
                            Token temp=new Token(t.getType(),t.getValue());
                            tokens.add(temp);
                        }
                    }
                    else {
                        t.setValue(s.toString());
                        i--;
                        if (isAReservedWord(s.toString()))
                            t.setType(Constants.RESERVED_WORD);
                        else
                            t.setType(Constants.IDENTIFIER);
                        s.delete(0, s.length());
                        state = STATE_TYPES.START;
                        Token temp=new Token(t.getType(),t.getValue());
                        tokens.add(temp);
                    }
                    break;
                case INASSIGN:
                    if (input.charAt(i) == '=') {
                        s.append(input.charAt(i));
                        t.setType(Constants.ASSIGN);
                        t.setValue(s.toString());
                        s.delete(0, s.length());
                        state = STATE_TYPES.START;
                        Token temp=new Token(t.getType(),t.getValue());
                        tokens.add(temp);

                    } else {
                        s.delete(0, s.length());
                        state = STATE_TYPES.START;
                        Token temp=new Token(t.getType(),t.getValue());
                        tokens.add(temp);
                        i--;
                    }
                    break;
            }
        }

    }*/
}