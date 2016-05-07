public class Num extends Literal {
  public final Token token;
  public int value;

  public Num(Token token){
    this.token = token;
    try{
      this.value = Integer.parseInt(token.value);
    }catch(Exception e){
      throw new RuntimeException("A Num was made with a token that does not contain a number as the input - LEXER");
    }
  }

  public Num(int i){
    this.token = new Token(Type.INTEGER, "" + i);
    this.value = i;
  }
}
