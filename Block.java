public class Block extends AST {
  public final AST[] declarations;
  public final AST compoundStatement;

  public Block(AST[] declarations, AST compoundStatement){
    this.declarations = declarations;
    this.compoundStatement = compoundStatement;
  }
}
