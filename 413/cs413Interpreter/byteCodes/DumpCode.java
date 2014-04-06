package byteCodes;

import java.util.*;
import interpreter.*;

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
    vm.setCurrCode("dump");
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
