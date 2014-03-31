package byteCodes;

import java.util.*;
import virtualMachine.VirtualMachine;

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

    if(vm.isDump()) {
      System.out.print(this.toString());
      if(!var.equals(" ")) {
        System.out.print("int " + var);
      }
      System.out.println();
      vm.runStackDump();
    }
  }

  @Override
  public String toString() {
    return "LIT " + literal + " " + var +"\t";
  }
}
