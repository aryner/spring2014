package byteCodes;

import java.util.*;
import virtualMachine.VirtualMachine;

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

    if(vm.isDump()) {
      System.out.print(this.toString());
      StringTokenizer func = new StringTokenizer(arg, "<");
      System.out.print(func.nextToken() + "(");
      if(vm.runStackSize() > vm.framePeek()) {
        Vector<Integer> args = vm.getFrame();
        for(int i = 0; i < args.size()-1; i++) {
          System.out.print(args.get(i) + ",");
        }
        System.out.println(args.get(args.size()-1) + ")");
      }
      else {
        System.out.println(")");
      }
      vm.runStackDump();
    }
  }

  @Override
  public String toString() {
    return "CALL " + arg + "\t";
  }
}
