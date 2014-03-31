package byteCodes;

import java.util.*;
import virtualMachine.VirtualMachine;

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
