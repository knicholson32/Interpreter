public class Interpreter {
  public static void main(String[] args) {
    Interpreter i = new Interpreter(args[0]);
    System.out.println(i.interpret());
  }

  private String text;
  private int pos;
  private Token currentToken;
  public Interpreter(String text){
    this.text = text;
    this.pos = 0;
    this.currentToken = null;
  }

  public void error(){
    System.out.println("Error parsing input");
    System.exit(1);
  }

  public Token getNextToken(){
    if(this.pos > text.length() - 1){
      return new Token(Type.EOF, '\n');
    }
    char currentChar = text.charAt(pos);
    this.pos++;

    //----Token Assignment----
    if(Character.isDigit(currentChar))
      return new Token(Type.INTEGER, currentChar);
    else if(currentChar == '+')
      return new Token(Type.PLUS, currentChar);

    //Throw Error
    error();
    return null;
  }

  public void eat(Type tokenType){
    if(this.currentToken.type == tokenType)
      this.currentToken = getNextToken();
    else
      this.error();
  }

  public int interpret() {
    currentToken = getNextToken();
    Token left = currentToken;
    eat(Type.INTEGER);
    Token op = currentToken;
    eat(Type.PLUS);
    Token right = currentToken;
    eat(Type.INTEGER);
    try{
      return Character.getNumericValue(left.value) + Character.getNumericValue(right.value);
    }catch(Exception e){
      error();
      return 0;
    }
  }
}
