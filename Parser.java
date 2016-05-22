public class Parser{
  private Lexer lexer;
  public Token currentToken;

  //Initializes with a Lexer and asks the Lexer for the first token
  public Parser(Lexer lexer){
    this.lexer = lexer;
    currentToken = lexer.getNextToken();
  }

  //Called if an error is encountered
  public void error(String msg){
    System.out.println("Parser: Error parsing input: " + msg);
    System.exit(1);
  }

  //Eats the next type (and checks that the proper type is encountered at the same time)
  private void eat(Type t){
    if(currentToken.type == t){
      currentToken = lexer.getNextToken();
      //System.out.println(currentToken.type);
    }else
      error("Invalid Varification: Looking for " + t +" but found " + currentToken.type);
  }

  //----------------------------------------------------------------------------
  //------------------Section that calculates the opperations-------------------
  //----------------------------------------------------------------------------
  private AST factor(){
    Token token = currentToken;
    if(token.type == Type.MINUS){
      currentToken = lexer.getNextToken();
      Num out = (Num)factor();
      out.value=-out.value;
      return out;
    }else if(token.type== Type.PLUS){
      currentToken = lexer.getNextToken();
      return factor();
    }else if(token.type==Type.INTEGER){
      eat(Type.INTEGER);
      return new Num(token);
    }else if(token.type == Type.LPAR){
      eat(Type.LPAR);
      AST result = expr();
      eat(Type.RPAR);
      return result;
    }else{
      error("Factor Error");
      return null;
    }
  }

  public AST exp(){
    //System.out.println(currentToken);
    AST node = factor();
    while(currentToken.type == Type.EXP || currentToken.type == Type.SQRT){
      Token token = currentToken;
      eat(currentToken.type);
      node = new BinOP(node, token, factor());
    }
    return node;
  }

  public AST term(){
    //System.out.println(currentToken);
    AST node = exp();
    while(currentToken.type == Type.MULT || currentToken.type == Type.DIVIDE || currentToken.type == Type.MOD){
      Token token = currentToken;
      eat(currentToken.type);
      node = new BinOP(node, token, exp());
    }
    return node;
  }

  public AST expr(){
    AST node = term();
    //System.out.println(result);
    while(currentToken.type == Type.PLUS || currentToken.type == Type.MINUS){
      Token token = currentToken;
      eat(currentToken.type);
      node = new BinOP(node, token, term());
    }
    return node;
  }

  public AST parse(){
    return expr();
  }
}
