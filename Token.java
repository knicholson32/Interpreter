// Tokens contain a type (ENUM Type) and a value (char)
public class Token{
  // The type of the Token
  public Type type;

  // the value of the Token
  public String value;

  // Token Constructor that takes a type and a clar
  public Token(Type type, String value){
    this.type = type;
    this.value = value;
  }

  // toString method for the token
  public String toString(){
    return "Token({" + type + "}, {" + value +"})";
  }
}
