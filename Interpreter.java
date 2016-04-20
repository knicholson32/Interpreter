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

  public void error(String msg){
    System.out.println("Error parsing input: " + msg);
    System.exit(1);
  }

  public Token getNextToken(){
    if(this.pos > text.length() - 1){
      return new Token(Type.EOF, '\n');
    }
    char currentChar = text.charAt(pos);
    this.pos++;

    //System.out.println("Char: " + currentChar + " " + (currentChar==' '));



    //----Token Assignment----

    Type type = null;
    if(currentChar == ' ')
      return getNextToken();
    else if(Character.isDigit(currentChar))
      return new Token(Type.INTEGER, currentChar);
    else if(currentChar == '+')
        return new Token(Type.PLUS, currentChar);
    else if(currentChar == '-')
        return new Token(Type.MINUS, currentChar);

    error("Invalid Character");
    return null;
  }

  public boolean check(Type tokenType){
    System.out.println("Check: " + currentToken.type + "  vs  " + tokenType);
    return currentToken.type == tokenType;
  }

  public void validate(Type tokenType){
    if(currentToken.type!=tokenType)
      error("Validate");
  }

  public int grabInteger(){
    validate(Type.INTEGER);
    int output = 0;
    do{
      output = 10*output + Character.getNumericValue(currentToken.value);
      this.currentToken = getNextToken();
    }while(check(Type.INTEGER));
    return output;
  }


  public int interpret() {
    currentToken = getNextToken();


    int firstNum = grabInteger();




    Token op = currentToken;
    if(!check(Type.PLUS) && !check(Type.MINUS)){
      error("Opperator");
    }else{
      this.currentToken = getNextToken();
    }


    int lastNum = grabInteger();

    //System.out.println("First: " + firstNum + "\tLast: " + lastNum);

    return (op.type==Type.PLUS?firstNum + lastNum:firstNum-lastNum);
  }
}
