public class NodeVisitor{
  public NodeVisitor(){
no
  }

  String methodName;

  public node? visit(node){
    methodName = 'visit_' + type(node).__name__
    visitor = getattr(self, method_name, self.generic_visit)
    return visitor(node)
  }

  public void genericVisit(node){
    throw new RuntimeException("No visit_{} method");
  }
}
