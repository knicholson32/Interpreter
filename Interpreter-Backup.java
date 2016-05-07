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

public class Interpreter implements NodeVisitor{

  // Main method that creates an Interpreter and populates it with args[0]
  public static void main(String[] args) {
    if(args[0] == null || args[0].length() == 0){
      System.out.println("No Input");
      System.exit(1);
    }

    // Create new Interpreter and pass the first argument from the command line
    Interpreter i = new Interpreter(new Lexer(args[0]));
    // run the interpret method and print the result
    System.out.println(args[0] + " = " + i.expr().value);
  }

  // -----------------------------Methods Start----------------------------- //


  private Parser parser;

  // Constructor for Interpreter which takes a string (the arguments)
  public Interpreter(Parser parser){
    this.parser = parser;
    //currentToken = lexer.getNextToken();
  }

  public int visitBinOP(BinOP node){
    if (node.op.type == Type.PLUS){
      return visit(node.left) + visit(node.right);
    }else if (node.op.type == MINUS){
      return visit(node.left) - visit(node.right);
    }else if (node.op.type == MULT){
      return visit(node.left) * visit(node.right);
    }else if (node.op.type == DIV){
      return visit(node.left) / visit(node.right);
    }
  }

  public int visitNum(Num n){
    return n.value;
  }



}
