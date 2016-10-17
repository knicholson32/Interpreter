public class Program extends AST {
  private final String name;
  public final AST block;

  public Program(String name, AST block){
    this.name = name;
    this.block = block;
  }
}
