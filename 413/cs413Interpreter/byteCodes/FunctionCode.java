package byteCodes;

import byteCodes.ByteCode;
import interpreter.debugger.*;
import interpreter.*;
import java.util.*;

public class FunctionCode extends ByteCode {
  String name;
  int start, end;

  public void loadArgs(Vector<String> args) {
    name = args.get(0);
    start = Integer.parseInt(args.get(1));
    end = Integer.parseInt(args.get(2));
  }

  public void execute(VirtualMachine vm) {
    try {
      if(((DebugVM)vm).isTrace()) {
        int end = name.indexOf("<<");
        String func;
        if(end > -1) {
          func = name.substring(0,end);
        }
        else {
          func = name;
        }
        ((DebugVM)vm).pushTraceFunc(func);
      }
      ((DebugVM)vm).funcEnvRecordFunction(name,start,end);
    }
    catch (Exception e) { }
  }
}
