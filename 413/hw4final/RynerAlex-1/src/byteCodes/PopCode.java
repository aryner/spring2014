package byteCodes;

import java.util.*;
import virtualMachine.VirtualMachine;

/**
 * PopCode class is responsible for removing the top element
 * from the run time stack
*/
public class PopCode extends ByteCode {
  String arg;

  public void loadArgs(Vector<String> args){
    arg = args.get(0);
  }

  public String getArg() {
    return arg;
  }

  public void execute(VirtualMachine vm) {
    for(int i=0; i<Integer.parseInt(arg); i++) {
      vm.runStackPop();
    }

    vm.setCurrCode(this.toString());
  }

  @Override
  public String toString() {
    return "POP " +arg;
  }
}
