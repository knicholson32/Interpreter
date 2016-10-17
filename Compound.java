public class Compound extends AST{
  public AST children[];

  public Compound(AST children[]){
    this.children = children;
  }

  public Compound(){

  }

  public void append(AST item){
    //TODO: CHECK THIS
    AST temp[] = new AST[children.length+1];
    for(int i = 0; i < children.length; i++){
        temp[i] = children[i];
    }
    temp[temp.length-1]=item;
    this.children = temp;
  }
}
