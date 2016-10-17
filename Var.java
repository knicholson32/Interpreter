public class Var extends Literal {
  public final Token token;
  public final String value;
  public boolean inverted = false;

  public Var(Token token){
    this.token = token;
    this.value = token.value;
  }

  public void invert(){
    inverted = !inverted;
  }


}
