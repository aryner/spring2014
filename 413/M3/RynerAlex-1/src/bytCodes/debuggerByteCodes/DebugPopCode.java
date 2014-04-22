package byteCodes.debuggerByteCodes;

import byteCodes.PopCode;
import interpreter.*;
import interpreter.debugger.*;

public class DebugPopCode extends PopCode {
  public void execute(VirtualMachine vm) {
    super.execute(vm);

    try {
      ((DebugVM)vm).popFuncEnvRecord(Integer.parseInt(super.getArg()));
    } catch(Exception e) {}
  } 
}
