package byteCodes;

import byteCodes.ByteCode;
import interpreter.*;
import interpreter.debugger.*;
import java.util.*;

public class FormalCode extends ByteCode {
  String var;
  int arg;
  public void loadArgs(Vector<String> args) {
    var = args.get(0);
    arg = Integer.parseInt(args.get(1));
  }

  public void execute(VirtualMachine vm) {
    try {
      ((DebugVM)vm).decArgCount();
      ((DebugVM)vm).enterEnvRecord(var, arg);
    }
    catch(Exception e) {}
  }
}
