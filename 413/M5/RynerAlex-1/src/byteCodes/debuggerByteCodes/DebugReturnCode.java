package byteCodes.debuggerByteCodes;

import byteCodes.ReturnCode;
import interpreter.*;
import interpreter.debugger.*;

public class DebugReturnCode extends ReturnCode {
  public void execute(VirtualMachine vm) {
    super.execute(vm);
    try {
      if(((DebugVM)vm).isTrace()) {
        ((DebugVM)vm).pushTraceReturn();
      }
      ((DebugVM)vm).setNewLine(false);
      ((DebugVM)vm).endScope();
    }catch(Exception e) {}
  }
}
