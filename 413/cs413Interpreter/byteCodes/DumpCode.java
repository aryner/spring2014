package byteCodes;

import java.util.*;
import virtualMachine.VirtualMachine;

/**
 * The DumpCode class is respnsible for switching on and off
 * the virtualPrograms dump switch
*/
public class DumpCode extends ByteCode {
   String arg;

  public void loadArgs(Vector<String> args){
    arg = args.get(0);
  }

  public void execute(VirtualMachine vm) {
    if (arg.equals("ON")) {
      vm.dumpOn();
    }
    else {
      vm.dumpOff();
    }
  }

  @Override
  public String toString() {
    return "DUMP";
  }
}
