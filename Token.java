public class Token{
  public Type type;
  public char value;
  public Token(Type type, char value){
    this.type = type;
    this.value = value;
  }

  public String toString(){
    return "Token({" + type + "}, {" + value +"})";
  }
}
