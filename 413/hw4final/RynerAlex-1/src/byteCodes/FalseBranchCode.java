package byteCodes;

import java.util.*;
import virtualMachine.VirtualMachine;

/**
 * The FalseBranchCode class is responsible for branching
 * to a new spot in the program if the top of the stack is 0
*/
public class FalseBranchCode extends ByteCode {
  String arg;

  public void loadArgs(Vector<String> args){
    arg = args.get(0);
  }

  public String getArg() {
    return arg;
  }

  public void setArg(String location) {
    arg = location;
  }

  public void execute(VirtualMachine vm) {
    int pop = vm.runStackPop();
    if(pop == 0) {
      vm.setPC(Integer.parseInt(arg));
    }
   
    vm.setCurrCode(this.toString());
  }

  @Override
  public String toString() {
    return "FALSEBRANCH " + arg;
  }
}
