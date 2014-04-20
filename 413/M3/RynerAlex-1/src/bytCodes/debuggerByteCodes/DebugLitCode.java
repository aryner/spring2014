package byteCodes.debuggerByteCodes;

import byteCodes.LitCode;
import interpreter.*;
import interpreter.debugger.*;

public class DebugLitCode extends LitCode {
  public void execute(VirtualMachine vm) {
    super.execute(vm);
    if(!super.getVar().trim().isEmpty()) {
      try{
        ((DebugVM)vm).enterEnvRecord(super.getVar().trim(), vm.getCurrentOffset());
      } catch(Exception e) {
        System.out.println("workz: " + e);
      }
    }
  }
}
