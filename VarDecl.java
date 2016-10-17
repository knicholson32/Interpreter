public class VarDecl extends AST {
  private final AST varNode;
  private final AST typeNode;

  public VarDecl(AST varNode, AST typeNode){
    this.varNode = varNode;
    this.typeNode = typeNode;
  }
}
