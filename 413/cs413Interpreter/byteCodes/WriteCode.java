package byteCodes;

import java.util.*;
import interpreter.*;

/**
 * WriteCode writes the contents of the top of the stack onto
 * standard output.
*/
public class WriteCode extends ByteCode {

  public void loadArgs(Vector<String> args){

  }

  public void execute(VirtualMachine vm) {
    System.out.println(vm.runStackPeek());

    vm.setCurrCode(this.toString());
  }

  @Override
  public String toString() {
    return "Writecode";
  }
}
