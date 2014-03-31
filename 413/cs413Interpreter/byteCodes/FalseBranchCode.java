package byteCodes;

import java.util.*;
import virtualMachine.VirtualMachine;

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

    if(vm.isDump()) {
      System.out.println(this.toString());
      vm.runStackDump();
    }
  }

  @Override
  public String toString() {
    return "FALSEBRANCH " + arg;
  }
}
