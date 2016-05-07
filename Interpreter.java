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
    if(args[0] == null || args[0].length() == 0){
      System.out.println("No Input");
      System.exit(1);
    }

    Interpreter i = new Interpreter(new Parser(new Lexer(args[0])));
    System.out.println(args[0] + " = " + ((Num)i.interpret()).value);
  }

  // -----------------------------Methods Start----------------------------- //
  private Parser parser;
  private AST tree;

  // Constructor for Interpreter which takes a string (the arguments)
  public Interpreter(Parser parser){
    this.parser = parser;
  }

  public AST interpret(){
    tree = parser.parse();
    return visit(tree);
  }

  public void error(String msg){
    System.out.println("Interpreter: Error: " + msg);
    System.exit(1);
  }

  private AST visit(AST node){
    if(node instanceof BinOP){
      return visitBinOP((BinOP) node);
    }else if(node instanceof Num){
      return visitNum((Num) node);
    }else{
      error("Visit was called on a node that is not listed.");
      return null;
    }
  }

  public AST visitBinOP(BinOP node){
    Type t = node.op.type;
    switch(t){
      case PLUS:
        return new Num(((Num)visit(node.left)).value + ((Num)visit(node.right)).value);
      case MINUS:
        return new Num(((Num)visit(node.left)).value - ((Num)visit(node.right)).value);
      case MULT:
        return new Num(((Num)visit(node.left)).value * ((Num)visit(node.right)).value);
      case DIVIDE:
        return new Num(((Num)visit(node.left)).value / ((Num)visit(node.right)).value);
      case MOD:
        return new Num(((Num)visit(node.left)).value % ((Num)visit(node.right)).value);
      case EXP:
        return new Num((int)Math.pow(((Num)visit(node.left)).value,((Num)visit(node.right)).value));
      //case SQRT:
      //  return new Num((int)Math.sqrt((double)((Num)visit(node.left)).value,(double)((Num)visit(node.right)).value));
      default:
      error("An opperation as attempted with an opperand that is unknown: " + node.op.type);
    }
    return null;
  }

  public AST visitNum(Num n){
    return n;
  }

}
