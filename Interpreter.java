// ----------------------------------+---------------------------------- //
/* Usage:
 *  Interpreter "<int> <Opperator> <int>"
 *  Operators: +, -
 */

public class Interpreter {

  // Main method that creates an Interpreter and populates it with args[0]
  public static void main(String[] args) {
    // Create new Interpreter and pass the first argument from the command line
    Interpreter i = new Interpreter(args[0]);
    // run the interpret method and print the result
    System.out.println(i.interpret());
  }

  // -----------------------------Methods Start----------------------------- //

  // text contains the initial argument that is passed in the constructor
  private String text;

  // pos repersents the index within the String text that is beening interpreted
  private int pos;

  // The current token that the interpreter is working with
  private Token currentToken;


  // Constructor for Interpreter which takes a string (the arguments)
  public Interpreter(String text){
    // set the initial arguemnt variable
    this.text = text;

    // set the initial index to 0
    this.pos = 0;

    // set the initial current Token to null (none have been created)
    this.currentToken = null;
  }

  // To be run in the event of an error. Takes a string as an error message
  public void error(String msg){
    System.out.println("Error parsing input: " + msg);
    System.exit(1);
  }

  // Reads text starting at the current pos (index) and deturmines the type of
  // token to produce. It then incriments the pos (index) and returns a Token
  public Token getNextToken(){
    // If the entire text has already been tokenized, return the EOF token
    if(this.pos > text.length() - 1){
      return new Token(Type.EOF, '\n');
    }

    // Get the char at pos and incriment pos (for next time)
    char currentChar = text.charAt(pos);
    this.pos++;

    //System.out.println("Char: " + currentChar + " " + (currentChar==' '));


    // ------------------------Token Interpretation------------------------- //

    Type type = null;
    // If the current charactor is a space, skip over it
    if(currentChar == ' ')
      return getNextToken();

    // If the charactor is a digit, return an integer token with that value
    else if(Character.isDigit(currentChar))
      return new Token(Type.INTEGER, currentChar);

    // If the charactor is a plus, retuen a plus token.
    else if(currentChar == '+')
        return new Token(Type.PLUS, currentChar);

    // If the charactor is a minus, return a minus token.
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
