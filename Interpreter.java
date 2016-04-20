// ----------------------------------+---------------------------------- //
/* Usage:
 *  Interpreter "<int> <Opperator> <int>"
 *  Operators: +, -
 */

// -------------------------------Grammer------------------------------- //
// expr:    term[(PLUS|MINUS) term]*
// term:    exp[(MULT|DIV|MOD) exp]*
// exp:     factor(EXP factor)*
// factor:  [(MINUS|PLUS) factor] | INTEGER | LPAR expr RPAR
// --------------------------------------------------------------------- //

public class Interpreter {

  // Main method that creates an Interpreter and populates it with args[0]
  public static void main(String[] args) {


    // Create new Interpreter and pass the first argument from the command line
    Interpreter i = new Interpreter(new Lexer(args[0]));
    // run the interpret method and print the result
    System.out.println(args[0] + " = " + i.expr());
  }

  // -----------------------------Methods Start----------------------------- //


  private Lexer lexer;
  private Token currentToken;

  // Constructor for Interpreter which takes a string (the arguments)
  public Interpreter(Lexer lexer){
    this.lexer = lexer;
    currentToken = lexer.getNextToken();
  }

  public void error(String msg){
    System.out.println("Interpreter: Error parsing input: " + msg);
    System.exit(1);
  }

  private void eat(Type t){
    if(currentToken.type == t){
      currentToken = lexer.getNextToken();
      //System.out.println(currentToken.type);
    }else
      error("Invalid Varification: Looking for " + t +" but found " + currentToken.type);
  }

  public int expr(){
    int result = term();
    //System.out.println(result);
    while(currentToken.type == Type.PLUS || currentToken.type == Type.MINUS){
      Token token = currentToken;
      if(token.type == Type.PLUS){
        eat(Type.PLUS);
        result = result + term();
      }else if(token.type == Type.MINUS){
        eat(Type.MINUS);
        result = result - term();
      }else{
        error("Unexpected Character");
      }
    }
    return result;
  }

  public int term(){
    //System.out.println(currentToken);
    int result = exp();
    while(currentToken.type == Type.MULT || currentToken.type == Type.DIVIDE || currentToken.type == Type.MOD){
      Token token = currentToken;
      //System.out.println(currentToken);
      if(token.type == Type.MULT){
        eat(Type.MULT);
        //System.out.println(currentToken);
        result = result * exp();
      }else if(token.type == Type.DIVIDE){
        eat(Type.DIVIDE);
        //System.out.println(currentToken);
        result = result / exp();
      }else if(token.type == Type.MOD){
        eat(Type.MOD);
        //System.out.println(currentToken);
        result = result % exp();
      }else{
        error("Unexpected Character");
      }
    }
    return result;
  }

  public int exp(){
    int result = factor();
    //System.out.println(result);
    while(currentToken.type == Type.EXP){
      Token token = currentToken;
      if(token.type == Type.EXP){
        eat(Type.EXP);
        result = (int)Math.pow(result,factor());
      }else{
        error("Unexpected Character");
      }
    }
    return result;
  }

  private int factor(){
    Token token = currentToken;

    if(token.type == Type.MINUS){
      currentToken = lexer.getNextToken();
      return -factor();
    }else if(token.type== Type.PLUS){
      currentToken = lexer.getNextToken();
      return factor();
    }else if(token.type==Type.INTEGER){
      eat(Type.INTEGER);
      try{
        return Integer.parseInt(token.value);
      }catch(Exception e){
        error("Integer Error");
        return 0;
      }
    }else if(token.type == Type.LPAR){
      eat(Type.LPAR);
      int result = 0;
      result = expr();
      eat(Type.RPAR);
      return result;
    }else{
      error("Factor Error");
      return 0;
    }
  }

}
