package byteCodes;

import byteCodes.ByteCode;
import interpreter.*;
import interpreter.debugger.*;
import java.util.*;

public class LineCode extends ByteCode {
  int lineNum;

  public void loadArgs(Vector<String> args) {
    lineNum = Integer.parseInt(args.get(0));
  }

  public void execute(VirtualMachine vm) {
    try{
      if(((DebugVM)vm).getCurr() > 0) {
        ((DebugVM)vm).setNewLine(true);
      }
      ((DebugVM)vm).lineFuncEnvRecord(lineNum);
    }
    catch(Exception e) {}
  }

  public String getArg() {
    return ""+lineNum;
  }

  public String toString() {
    return "LINE";
  }
}
