package byteCodes;

import java.util.*;
import interpreter.*;

/**
 * The ArgsCode class is responsible for starting a new frame
 * on the run time stack and loading it with the appropriate
 * arguments
*/
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
    vm.setCurrCode(this.toString());
  }

  @Override
  public String toString() {
    return "ARGS " + numArgs;
  }
}
