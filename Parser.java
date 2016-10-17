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
      error("Invalid Varification: Looking for " + t + " but found " + currentToken.type);
  }

  private AST program(){
    eat(Type.PROGRAM);
    Var var_node = (Var)variable();
    String prog_name = var_node.value;
    eat(Type.SEMI);
    AST block_node = block();
    AST program_node = new Program(prog_name, block_node);
    eat(Type.DOT);
    return program_node;
  }

  private AST compoundStatment(){
    eat(Type.BEGIN);
    AST nodes[] = statementList();
    eat(Type.END);

    AST root = new Compound(nodes);
    return root;
  }

  private AST[] statementList(){
    AST node = statment();

    AST results[] = {node};

    while (currentToken.type == Type.SEMI){
        eat(Type.SEMI);
        results = append(results, statment());
    }
    if (currentToken.type == Type.ID){
        error("Invalid Statment");
    }

    return results;
  }

  private AST statment(){
    AST node;
    if (currentToken.type == Type.BEGIN){
      node = compoundStatment();
    }else if(currentToken.type == Type.ID){
      node = assignmentStatement();
    }else{
      node = empty();
    }
    return node;
  }

  private AST assignmentStatement(){
    AST left = variable();
    Token token = currentToken;
    eat(Type.ASSIGN);
    AST right = expr();
    AST node = new Assign(left, token, right);
    return node;
  }

  private AST variable(){
    AST node = new Var(currentToken);
    eat(Type.ID);
    return node;
  }

  private AST empty(){
    return new NoOp();
  }

  private AST block(){
    return new Block(declarations(), compoundStatment());
  }

  private AST[] declarations(){
    //declarations : VAR (variable_declaration SEMI)+ | empty
    AST declarations[] = new AST[0];
    if (currentToken.type == Type.VAR){
        eat(Type.VAR);
        while (currentToken.type == Type.ID){
            append(declarations,variableDeclaration());
            eat(Type.SEMI);
        }
    }
    return declarations;
  }

  private AST[] variableDeclaration(){
    AST var_nodes[] = {new Var(currentToken)};
    eat(Type.ID);

    while(currentToken.type == Type.COMMA){
        eat(Type.COMMA);
        append(var_nodes,new Var(currentToken));
        eat(Type.ID);
      }

    eat(Type.COLON);

    AST type_node = typeSpec();

    AST var_declarations[] = new AST[0];
    for(AST var_node : var_nodes){
      append(var_declarations,new VarDecl(var_node, type_node));
    }
    return var_declarations;
  }

  private AST typeSpec(){
    Token token = currentToken;
    if (currentToken.type == Type.INTEGER)
        eat(Type.INTEGER);
    else
        eat(Type.REAL);
    return new TypeAST(token);
  }

  public AST[] append(AST[] arr, AST item){
    AST temp[] = new AST[arr.length+1];
    for(int i = 0; i < arr.length; i++){
        temp[i] = arr[i];
    }
    temp[temp.length-1]=item;
    return temp;
  }

  public AST[] append(AST[] arr, AST[] items){
    AST temp[] = new AST[arr.length+items.length];
    for(int i = 0; i < arr.length; i++){
        temp[i] = arr[i];
    }
    for(int i = arr.length; i < temp.length; i++){
      temp[i]=items[i - arr.length];
    }
    return temp;
  }




  //----------------------------------------------------------------------------
  //------------------Section that calculates the opperations-------------------
  //----------------------------------------------------------------------------
  private AST factor(){
    /*factor : PLUS factor
              | MINUS factor
              | INTEGER_CONST
              | REAL_CONST
              | LPAREN expr RPAREN
              | variable            */

    Token token = currentToken;


    if(token.type == Type.MINUS){
      currentToken = lexer.getNextToken();
      AST out = factor();
      //System.out.println(out);
      if(out instanceof Num){
        ((Num)out).invert();
      }
      if(out instanceof Var){
        ((Var)out).invert();
      }
      return out;
    }else if(token.type== Type.PLUS){
      currentToken = lexer.getNextToken();
      return factor();
    }else if(token.type==Type.REAL_CONST){
      eat(Type.REAL_CONST);
      return new Num(token);
    }else if(token.type==Type.INTEGER_CONST){
      eat(Type.INTEGER_CONST);
      return new Num(token);
    }else if(token.type == Type.LPAR){
      eat(Type.LPAR);
      AST result = expr();
      eat(Type.RPAR);
      return result;
    }else{
      //error("Factor Error: Valid token not found.");
      //return null;
      return variable();
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
    //term : factor ((MUL | INTEGER_DIV | FLOAT_DIV) factor)*
    AST node = exp();
    while(currentToken.type == Type.MULT || currentToken.type == Type.INTEGER_DIV || currentToken.type == Type.MOD || currentToken.type == Type.FLOAT_DIV){
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
    AST node = program();
    if (currentToken.type != Type.EOF){
      error("EOF Expected");
    }
    return node;
  }
}
