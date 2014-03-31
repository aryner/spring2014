package byteCodes;

import java.util.*;
import virtualMachine.VirtualMachine;

/**
 * ReturnCode is responsible for removing the current frame, placing
 * the top element of the current frame onto the frame we are returning
 * to and setting the pc back to the instruction just after the function
 * call
*/
public class ReturnCode extends ByteCode {
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
    int result = vm.popFrame();
    vm.setPC(vm.getReturn());

    if(vm.isDump()) {
      System.out.println(this.toString()+result);
      vm.runStackDump();
    }
  }

  @Override
  public String toString() {
    StringTokenizer base = new StringTokenizer(arg, "<");
    return "RETURN " + arg + "\texit "+base.nextToken()+": ";
  }
}
