package byteCodes.debuggerByteCodes;

import byteCodes.CallCode;
import interpreter.*;
import interpreter.debugger.*;
import java.util.*;

public class DebugCallCode extends CallCode {
  public void execute(VirtualMachine vm) {
    super.execute(vm);
    try {
      ((DebugVM)vm).setArgCount(vm.runStackSize() - vm.framePeek());
      ((DebugVM)vm).beginScope();
    }catch(Exception e) {}
  }
}
