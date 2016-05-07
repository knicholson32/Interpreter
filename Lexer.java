public class Lexer{
  public static final char END = (char)-1;
  // text contains the initial argument that is passed in the constructor
  private String text;

  // pos repersents the index within the String text that is beening interpreted
  private int pos;

  // The current token that the interpreter is working with
  private Token currentToken;

  // The current character that the lexer is working with
  char currentChar;

  public Lexer(String text){
    // set the initial arguemnt variable
    this.text = text;

    // set the initial index to 0
    this.pos = 0;

    // set the initial current Token to null (none have been created)
    this.currentToken = null;

    // set the initial character to the initial position
    currentChar = text.charAt(pos);
  }

  // To be run in the event of an error. Takes a string as an error message
  public void error(String msg){
    System.out.println("LEXER: Error parsing input: " + msg);
    System.exit(1);
  }

  // Skips one character ahead and properly assigns the currentChar
  public void advance(){
    pos++;
    if(pos > text.length()-1)
      currentChar = END;
    else
      currentChar = text.charAt(pos);
  }

  // Skips ahead until there are no more spaces
  public void skipWhitepsace(){
    while(currentChar != END && currentChar == ' ')
        advance();
  }

  // Returns the integer value of the current token.
  public int integer(){
    // Collect all of the integers in a sequence to make a number.
    // EG. '1' '2' '3' ==> 123
    int output = 0;

    while(currentChar != END && Character.isDigit(currentChar)){
      output = output * 10 + Character.getNumericValue(currentChar);
      advance();
    }
    //System.out.println("Integer: " + output);
    return output;
  }

  // Reads text starting at the current pos (index) and deturmines the type of
  // token to produce. It then incriments the pos (index) and returns a Token
  public Token getNextToken(){
    while (currentChar != END){
      //System.out.println(currentChar);
      if(currentChar == ' '){
        skipWhitepsace();
        continue;
      }


      if(Character.isDigit(currentChar)){
        //System.out.println("Digit: " + currentChar);
        return new Token(Type.INTEGER, (""+integer()));
      }
      if(currentChar == '*'){
        advance();
        return new Token(Type.MULT, "*");
      }
      if(currentChar == '/'){
        advance();
        return new Token(Type.DIVIDE, "/");
      }
      if(currentChar == '+'){
        advance();
        return new Token(Type.PLUS, "+");
      }
      if(currentChar == '-'){
        advance();
        return new Token(Type.MINUS, "-");
      }
      if(currentChar == '('){
        advance();
        return new Token(Type.LPAR, "(");
      }
      if(currentChar == ')'){
        advance();
        return new Token(Type.RPAR, ")");
      }
      if(currentChar == '^'){
        advance();
        return new Token(Type.EXP, "^");
      }
      if(currentChar == '%'){
        advance();
        return new Token(Type.MOD, "%");
      }
      if(currentChar == 's'){
        advance();
        if(currentChar == 'q'){
          advance();
          if(currentChar == 'r'){
            advance();
            if(currentChar =='t'){
              advance();
              if(currentChar == '('){
                return new Token(Type.SQRT, "sqrt(");
              }
            }
          }
        }
      }
      error("No Valid Tokens Found");
    }
    return new Token(Type.EOF, null);
  }
}
