package byteCodes;

import java.util.*;
import virtualMachine.VirtualMachine;

/**
 * WriteCode writes the contents of the top of the stack onto
 * standard output.
*/
public class WriteCode extends ByteCode {

  public void loadArgs(Vector<String> args){

  }

  public void execute(VirtualMachine vm) {
    System.out.println(vm.runStackPeek());

    if(vm.isDump()) {
      System.out.println(this.toString());
      vm.runStackDump();
    }
  }

  @Override
  public String toString() {
    return "Writecode";
  }
}
