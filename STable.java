public class STable {

  private String[] stringArr;
  private AST[] ASTArr;
  private int len;

  public STable(){
    len = 0;
    stringArr = new String[len];
    ASTArr = new AST[len];
  }

  public void add(String id, AST assign){
    AST astTemp[] = new AST[len+1];
    String strTemp[] = new String[len+1];
    int j = find(id);
    if(j != -1){
      modify(j,assign);
      return;
    }

    //System.out.println("ADD: " + len);
    if(len > 0){
      for(int i = 0; i < len; i++){
          astTemp[i]=ASTArr[i];
          strTemp[i]=stringArr[i];
      }
    }
    astTemp[len]=assign;
    strTemp[len]=id;
    stringArr = strTemp;
    ASTArr = astTemp;
    len++;
  }

  public AST get(String id){
    int i = find(id);
    if(i != -1){
      return ASTArr[i];
    }
    return null;
  }

  public boolean modify(String id, AST newAST){
    int i = find(id);
    return modify(i,newAST);
  }

  public String[] getAllNames(){
    return stringArr;
  }

  public AST[] getAllAST(){
    return ASTArr;
  }

  private boolean modify(int i, AST newAST){
    if(i != -1){
      ASTArr[i] = newAST;
      return true;
    }
    return false;
  }

  private int find(String id){
    //System.out.println("Find: " + len);
    for(int i = 0; i < len; i++){
      if(stringArr[i].equals(id)){
        return i;
      }
    }
    return -1;
  }

  public static void main(String[] args){
    STable table = new STable();
    table.add("test",new NoOp());

    System.out.println(table.get("test"));
    table.add("test",new Compound());

    System.out.println(table.get("test"));

    table.add("tesst",new Compound());
    table.add("te123st",new Compound());

    System.out.println(table.get("te123st"));
    System.out.println(table.get("te123st"));
    System.out.println(table.get("te123st"));


  }

}
