package byteCodes;

import java.util.*;
import virtualMachine.VirtualMachine;

/**
 * The GoToCode class is responsible for setting the
 * program counter to a new location
*/
public class GoToCode extends ByteCode {
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
    vm.setPC(Integer.parseInt(arg));

    if(vm.isDump()) {
      System.out.println(this.toString());
      vm.runStackDump();
    }
  }

  @Override
  public String toString() {
    return "GOTO " + arg;
  }
}
