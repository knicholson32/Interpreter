public class Num extends Literal {
  public final Token token;
  public int ivalue;
  public double dvalue;

  public final Type type;

  public Num(Token token){
    this.token = token;
    if(token.type == Type.INTEGER_CONST){
      try{
        ivalue = Integer.parseInt(token.value);
        dvalue = (double)ivalue;
      }catch(Exception e){
        throw new RuntimeException("A Num was made with a token that does not contain a number as the input - LEXER");
      }
    }else if(token.type == Type.REAL_CONST){
      try{
        dvalue = Double.parseDouble(token.value);
        ivalue = (int)dvalue;
      }catch(Exception e){
        throw new RuntimeException("A Num was made with a token that does not contain a number as the input - LEXER");
      }
    }else{
      System.out.println("Invalid Num op");
      System.exit(1);
    }
    type = this.token.type;
  }

  public Num duplicate(){
    return new Num(token);
  }

  public Num(int i){
    this.token = new Token(Type.INTEGER_CONST, "" + i);
    this.ivalue = i;
    this.dvalue = (double)ivalue;
    this.type = this.token.type;
  }

  public Num(double i){
    this.token = new Token(Type.REAL_CONST, "" + i);
    this.dvalue = i;
    this.ivalue = (int)dvalue;
    this.type = this.token.type;
  }

  public String value(){
    if(type==Type.INTEGER_CONST)
      return "" + ivalue;
    else
      return "" + dvalue;
  }

  public void invert(){
    //System.out.println("INVERTING: OLD: " + this.value + "\tNEW: " + (-this.value));
    this.ivalue  = - this.ivalue;
    this.dvalue  = - this.dvalue;
  }

  /*public void processInvert(){
    if(inv){
      this.value  = - this.value;
    }
  }*/
}
