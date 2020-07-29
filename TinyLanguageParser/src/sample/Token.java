package sample;

public class Token {
    private String type;
    private String value;
    public Token(){
        this.type="";
        this.value="";
    }
    public Token(String type,String value){
        this.type=type;
        this.value=value;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    @Override
    public String toString(){
        return this.value + " : " + this.type;
    }
}
