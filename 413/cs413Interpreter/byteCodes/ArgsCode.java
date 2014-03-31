package byteCodes;

import java.util.*;
import virtualMachine.VirtualMachine;

public class ArgsCode extends ByteCode {
  String numArgs;

  public void loadArgs(Vector<String> args){
    numArgs = args.get(0);
  }

  public String getArg() {
    return numArgs;
  }

  public void execute(VirtualMachine vm) {
    vm.newRunStackFrame(Integer.parseInt(numArgs));
    if(vm.isDump()) {
      System.out.println(this.toString());
      vm.runStackDump();
    }
  }

  @Override
  public String toString() {
    return "ARGS " + numArgs;
  }
}
