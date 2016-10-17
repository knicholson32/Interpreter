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

  int saveVal = 0;
  public void save(){
    saveVal = pos;
  }

  public void resume(){
    pos = saveVal;
    if(pos > text.length()-1)
      currentChar = END;
    else
      currentChar = text.charAt(pos);
  }

  // Skips one character ahead and properly assigns the currentChar
  public void advance(){
    pos++;
    if(pos > text.length()-1)
      currentChar = END;
    else
      currentChar = text.charAt(pos);
  }

  public char peak(){
    if(pos+1 > text.length()-1)
      return END;
    else
      return text.charAt(pos+1);
  }

  // Skips ahead until there are no more spaces
  public void skipWhitepsace(){
    while(currentChar != END && (currentChar == ' ' || currentChar == '\n'))
        advance();
  }

  //Skips ahead past comments
  public void skipComment(){
    while(currentChar != '}')
        advance();
    advance();
  }


  //Sets an id and searchs for reserved words
  public Token id(){
    String result = "";
    do{
      result += currentChar;
      advance();
    }while(currentChar != END && Character.isLetterOrDigit(currentChar));

    //Reserved Words
    switch(result.toLowerCase()){
      case "program":
        return new Token(Type.PROGRAM, "PROGRAM");
      case "var":
        return new Token(Type.VAR, "VAR");
      case "div":
        return new Token(Type.DIVIDE, "DIV");
      case "integer":
        return new Token(Type.INTEGER, "INTEGER");
      case "real":
        return new Token(Type.REAL, "REAL");
      case "begin":
        return new Token(Type.BEGIN, "BEGIN");
      case "end":
        return new Token(Type.END, "END");
      default:
        return new Token(Type.ID, result);
    }
  }

  // Returns the integer value of the current token.
  public Token number(){
    // Collect all of the integers in a sequence to make a number.
    // EG. '1' '2' '3' ==> 123
    String output = "";

    while(currentChar != END && Character.isDigit(currentChar)){
      output += currentChar;
      advance();
    }
    if(currentChar == '.'){
      output += '.';
      advance();
      while(currentChar != END && Character.isDigit(currentChar)){
        output += currentChar;
        advance();
      }
      return new Token(Type.REAL_CONST, output);
    }else{
      //return new Token(Type.INTEGER_CONST, output);
      return new Token(Type.INTEGER, output);
    }
  }

  // Reads text starting at the current pos (index) and deturmines the type of
  // token to produce. It then incriments the pos (index) and returns a Token
  public Token getNextToken(){
    //System.out.println("Token");
    while (currentChar != END){
      //System.out.println(currentChar);
      if(currentChar == ' ' || currentChar == '\n' ){
        skipWhitepsace();
        continue;
      }
      if(currentChar == '{'){
        skipComment();
        continue;
      }


      if(Character.isDigit(currentChar)){
        return number();
      }
      if(currentChar == '*'){
        advance();
        return new Token(Type.MULT, "*");
      }
      if(currentChar == 'd'){
        save();
        advance();
        if(currentChar == 'i'){
          advance();
          if(currentChar == 'v'){
            advance();
            if(!Character.isLetterOrDigit(peak())){
              return new Token(Type.DIVIDE, "div");
            }
          }
        }
        resume();
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
      if(currentChar == '/'){
        advance();
        return new Token(Type.FLOAT_DIV, "/");
      }

      if(Character.isLetterOrDigit(currentChar) || (currentChar == '_' && Character.isLetterOrDigit(peak()))){
        return id();
      }
      if(currentChar == ':' && peak() == '='){
        advance();
        advance();
        return new Token(Type.ASSIGN, ":=");
      }
      if(currentChar == ';'){
        advance();
        return new Token(Type.SEMI, ";");
      }
      if(currentChar == '.'){
        advance();
        return new Token(Type.DOT, ".");
      }
      if(currentChar == ':'){
        advance();
        return new Token(Type.COLON, ":");
      }
      if(currentChar == ','){
        advance();
        return new Token(Type.COMMA, ",");
      }
      error("No Valid Tokens Found");
    }
    return new Token(Type.EOF, null);
  }
}
