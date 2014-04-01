package byteCodes;

import java.util.*;
import virtualMachine.VirtualMachine;

/**
 * The CallCode class is responsible for saving the return
 * location for the program counter and changing the program
 * counter to the location of the function being called
*/
public class CallCode extends ByteCode {
  String arg;
  String pos;

  public void loadArgs(Vector<String> args){
    arg = args.get(0);
  }

  public String getArg() {
    return arg;
  }

  public void setArg(String location) {
    pos = location;
  }

  public void execute(VirtualMachine vm) {
    vm.setReturn(vm.getPC());
    vm.setPC(Integer.parseInt(pos)-1);

    String code = this.toString();
    StringTokenizer func = new StringTokenizer(arg, "<");
    code += func.nextToken() + "(";
    if(vm.runStackSize() > vm.framePeek()) {
      Vector<Integer> args = vm.getFrame();
      for(int i = 0; i < args.size()-1; i++) {
        code += args.get(i) + ",";
      }
      code += args.get(args.size()-1) + ")";
    }
    else {
      code += ")";
    }
    vm.setCurrCode(code);
  }

  @Override
  public String toString() {
    return "CALL " + arg + "\t";
  }
}
