public class Assign extends AST{
  public final AST left;
  public final Token token;
  public final AST right;

  public Assign(AST left, Token token, AST right){
    this.left = left;
    this.token = token;
    this.right = right;
  }
}
