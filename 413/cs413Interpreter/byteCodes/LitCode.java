package byteCodes;

import java.util.*;
import interpreter.*;

/**
 * LitCode class is responsible for loading integers onto
 * the run time stack
*/
public class LitCode extends ByteCode {
  String literal;
  String var;

  public void loadArgs(Vector<String> args){
    literal = args.get(0);
    if (args.size() > 1) {
      var = args.get(1);
    }
    else {
      var = " ";
    }
  }

  public String getArg() {
    return literal;
  }

  public String getVar() {
    return var;
  }

  public void execute(VirtualMachine vm) {
    vm.runStackPush(Integer.parseInt(literal));
    
    String code = this.toString();
    if(!var.equals(" ")) {  
      code += "int " + var;
    }
    vm.setCurrCode(code);
  }

  @Override
  public String toString() {
    return "LIT " + literal + " " + var +"\t";
  }
}
