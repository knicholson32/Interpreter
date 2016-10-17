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
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.nio.file.Files;

public class Interpreter {

  // Main method that creates an Interpreter and populates it with args[0]
  public static void main(String[] args) {
    if(args[0] == null || args[0].length() == 0){
      System.out.println("No Input");
      System.exit(1);
    }

    String input = "";
    try{
      input = readFile(args[0],Charset.defaultCharset());
    }catch(IOException e){
      System.out.println("IOException: ");
      System.out.println(e);
      System.exit(1);
    }

    //System.out.println(input);

    //return;

    //Creates the Interpreter and gives it the inputs along with the Parser and Lexer
    Interpreter i = new Interpreter(new Parser(new Lexer(input)));

    i.interpret();

    i.print();

    //Outputs the result
    //System.out.println(args[0] + " = " + ((Num)i.interpret()).value);
  }

  private static String readFile(String path, Charset encoding) throws IOException {
    byte[] encoded = Files.readAllBytes(Paths.get(path));
    return new String(encoded, encoding);
  }

  // -----------------------------Methods Start----------------------------- //
  private Parser parser;
  private AST tree;
  private STable stable;

  // Constructor for Interpreter which takes a string (the arguments)
  public Interpreter(Parser parser){
    this.parser = parser;
    stable = new STable();
  }

  //Sets the tree head to the parser.parse() result
  public void interpret(){
    tree = parser.parse();
    visit(tree);
  }

  public void print(){
    String out[] = stable.getAllNames();
    AST outAST[] = stable.getAllAST();
    //Print the ST output
    System.out.println();
    System.out.println("Var\tValue");
    System.out.println("--------------");
    for(int i = 0; i < out.length; i++){
        System.out.print(out[i] + "\t");
        System.out.println(((Num)(outAST[i])).value());
    }
  }

  //Called in the event of an error
  public void error(String msg){
    System.out.println("Interpreter: Error: " + msg);
    System.exit(1);
  }

  //Visits a node. The node must be a BinOP or a Num at this point
  private AST visit(AST node){
    if(node instanceof BinOP){
      return visitBinOP((BinOP) node);
    }else if(node instanceof Num){
      return visitNum((Num) node);
    }else if(node instanceof Compound){
      visitCompound((Compound) node);
      return null;
    }else if(node instanceof Assign){
      visitAssign((Assign) node);
      return null;
    }else if(node instanceof Var){
      return visitVar((Var) node);
    }else if(node instanceof Program){
      return visitProgram((Program) node);
    }else if(node instanceof Block){
      visitBlock((Block) node);
      return null;
    }else if(node instanceof VarDecl){
      visitVarDecl((VarDecl) node);
      return null;
    }else if(node instanceof TypeAST){
      visitType((TypeAST) node);
      return null;
    }else if(node instanceof NoOp){
      return null;
    }else{
      error("Visit was called on a node that is not listed.");
      return null;
    }
  }

  //If a BinOP is encountered, this method is ran
  public AST visitBinOP(BinOP node){
    Type t = node.op.type;

    if(node.left instanceof Num){
      //((Num)(node.left)).processInvert();
    }
    if(node.right instanceof Num){
      //((Num)(node.right)).processInvert();
    }

    //System.out.println(((BinOP)node.left).op);
    //System.out.println(node.op);

    System.out.println(((Num)(visit(node.left))).type);

    Num left = ((Num)(visit(node.left)));
    Num right = ((Num)(visit(node.right)));

    if(left.type == Type.INTEGER_CONST && right.type == Type.REAL_CONST)
      error("Lossly conversion from float to int.");

    switch(t){
      case PLUS:
        if(left.type == Type.INTEGER_CONST)
          return new Num(left.ivalue + right.ivalue);
        else
          return new Num(left.dvalue + right.dvalue);
      case MINUS:
        if(left.type == Type.INTEGER_CONST)
          return new Num(left.ivalue - right.ivalue);
        else
          return new Num(left.dvalue - right.dvalue);
      case MULT:
        if(left.type == Type.INTEGER_CONST)
          return new Num(left.ivalue * right.ivalue);
        else
          return new Num(left.dvalue * right.dvalue);
      case INTEGER_DIV:
        return new Num(left.ivalue / right.ivalue);
      case FLOAT_DIV:
        return new Num(left.dvalue / right.dvalue);
      case MOD:
        if(left.type == Type.INTEGER_CONST)
          return new Num(left.ivalue % right.ivalue);
        else
          return new Num(left.dvalue % right.dvalue);
      case EXP:
        if(left.type == Type.INTEGER_CONST)
          return new Num((int)Math.pow(left.ivalue,right.ivalue));
        else
          return new Num(Math.pow(left.dvalue,right.dvalue));
      //case SQRT:
      //  return new Num((int)Math.sqrt((double)((Num)visit(node.left)).value,(double)((Num)visit(node.right)).value));
      default:
        error("An opperation as attempted with an opperand that is unknown: " + node.op.type);
    }
    return null;
  }

  //If a Num is encountered, this method is ran, which returns the number
  public AST visitNum(Num n){
    return n;
  }

  public void visitCompound(Compound node){
    for (AST n : node.children){
      visit(n);
    }
  }

  public void visitNoOp(){}

  public void visitAssign(Assign node){
    String varName = ((Var)(node.left)).value;
    stable.add(varName,visit(node.right));
  }

  public AST visitVar(Var node){
    String name = node.value;
    AST val = stable.get(name);
    if(val == null){
      error("The variable \""+name+"\" can not be found.");
      return null;
    }else{
      //System.out.println("VISIT VAR: " + val);
      //((Num)val).processInvert();
      if(node.inverted == true){
        Num n = ((Num)val).duplicate();
        n.invert();
        val = (AST)n;
      }

      //System.out.println(val);
      return val;
    }

  }

  public AST visitProgram(Program p){
    return visit(p.block);
  }

  public void visitBlock(Block b){
    for(AST declaration : b.declarations)
      visit(declaration);
    visit(b.compoundStatement);
  }

  public void visitVarDecl(VarDecl v){}

  public void visitType(TypeAST t){}



}
