package interpreter.debugger;

public class FunctionEnvironmentRecord {
  private SymbolTable varTable;
  int startLine;
  int endLine;
  int currentLine;
  String name;

  public FunctionEnvironmentRecord() {
    varTable = new SymbolTable();
    startLine = 0;
    endLine = 0;
    currentLine = 0;
  }

  public void beginScope() {
    varTable.beginScope();
  }

  public void endScope() {
    varTable.endScope();
  }

  public void enter(String id, int value) {
    varTable.enter(id, value);
  }
 
  public void pop(int n) {
    varTable.pop(n);
  }

  public void function(String n, int s, int e) {
    setName(n);
    setStart(s);
    setEnd(e);
  }

  public void line(int c) {
    setCurr(c);
  }

  public void setStart(int n) {
    startLine = n;
  }

  public void setEnd(int n) {
    endLine = n;
  }

  public void setCurr(int n) {
    currentLine = n;
  }

  public void setName(String n) {
    name = n;
  }

  public int getStart() {
    return startLine;
  }

  public int getEnd() {
    return endLine;
  }

  public int getCurr() {
    return currentLine;
  }

  public String getName() {
    return name;
  }

  public String toString() {
    String result = "<"+varTable.toString();
    result += ",";

    if(startLine == 0) {
      result += "-,";
    }
    else {
      result += startLine+",";
    }
    if(endLine == 0) {
      result += "-,";
    }
    else {
      result += endLine+",";
    }
    if(name == null || name.isEmpty()) {
      result += "-,";
    }
    else {
      result += name+",";
    }
    if(currentLine == 0) {
      result += "->";
    }
    else {
      result += currentLine+">";
    }

    return result;
  }
/*
//Test of FunctionEnvironmentRecord
  public static void main(String[] args) {
    FunctionEnvironmentRecord record = new FunctionEnvironmentRecord();

    System.out.print("BS\t\t\t");
    record.beginScope();
    System.out.println(record.toString());

    System.out.print("Function g 1 20\t\t");
    record.function("g",1,20);
    System.out.println(record.toString());

    System.out.print("Line 5\t\t\t");
    record.line(5);
    System.out.println(record.toString());
    
    System.out.print("Enter a 4\t\t");
    record.enter("a",4);
    System.out.println(record.toString());

    System.out.print("Enter b 2\t\t");
    record.enter("b",2);
    System.out.println(record.toString());
    
    System.out.print("Enter c 7\t\t");
    record.enter("c", 7);
    System.out.println(record.toString());

    System.out.print("Enter a 1\t\t");
    record.enter("a", 1);
    System.out.println(record.toString());

    System.out.print("Pop 2\t\t\t");
    record.pop(2);
    System.out.println(record.toString());

    System.out.print("Pop 1\t\t\t");
    record.pop(1);
    System.out.println(record.toString());
  }
*/
}
