package byteCodes;

import java.util.*;
import virtualMachine.VirtualMachine;

/**
 * The HaltCode class is responsible for setting the virtualMachine
 * to no longer running when the program is finished
*/
public class HaltCode extends ByteCode {

  public void loadArgs(Vector<String> args){

  }

  public void execute(VirtualMachine vm) {
    vm.setNotRunning();
    if(vm.isDump()) {
      System.out.println(this.toString());
      vm.runStackDump();
    }
  }

  @Override
  public String toString() {
    return "HALT";
  }
}
