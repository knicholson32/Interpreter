public class TypeAST extends AST {
  private final Token token;
  private final String value;

  public TypeAST(Token token){
    this.token = token;
    this.value = this.token.value;
  }
}
