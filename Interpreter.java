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
    else if(currentChar == '*')
        return new Token(Type.MULT, currentChar);
    else if(currentChar == '/')
        return new Token(Type.DIVIDE, currentChar);

    // If none of the above return, return an error.
    error("Invalid Character");
    return null;
  }

  // Checks if the curent Token matches the given type. Returns true on success
  public boolean check(Type tokenType){
    //System.out.println("Check: " + currentToken.type + "  vs  " + tokenType);
    return currentToken.type == tokenType;
  }

  // returns an error of the current Token does not match the given type
  public void validate(Type tokenType){
    if(currentToken.type!=tokenType)
      error("Validate");
  }

  // Returns the integer value of the current token.
  public int grabInteger(){
    // check that the current token is an integer
    validate(Type.INTEGER);

    // Collect all of the integers in a sequence to make a number.
    // EG. '1' '2' '3' ==> 123
    int output = 0;
    do{
      output = 10*output + Character.getNumericValue(currentToken.value);
      this.currentToken = getNextToken();
    }while(check(Type.INTEGER));
    return output;
  }

  public boolean isOpperator(Token t){
    return (t.type == Type.PLUS || t.type == Type.MINUS || t.type == Type.MULT || t.type == Type.DIVIDE);
  }


  // Function that interprets the text (Argument)
  // This can only work with one operator based on the following format:
  //  "<int> <operator> <int>"
  public int interpret() {
    // Get the next token within the text and set it to currentToken
    currentToken = getNextToken();

    // find the first integer
    int firstNum = grabInteger();

    // Deturmine the operator type
    Token op = currentToken;
    if(!isOpperator(op)){
      error("Opperator - Ver");
    }else{
      this.currentToken = getNextToken();
    }

    // find the last integer
    int lastNum = grabInteger();

    //System.out.println("First: " + firstNum + "\tLast: " + lastNum);
    switch(op.type){
      case PLUS:
        return firstNum + lastNum;
      case MINUS:
        return firstNum - lastNum;
      case MULT:
        return firstNum * lastNum;
      case DIVIDE:
        return firstNum / lastNum;
      default:
        error("Opperator");
        return 0;
    }
  }
}
